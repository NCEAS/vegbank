/*
 *	'$RCSfile: JarLoader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-11-25 17:48:23 $'
 *	'$Revision: 1.1 $'
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * 
 * DESCRIPTION:
 * Loads jar files.
 *
 * @author anderson
 */

package org.vegbank.common.utility;

import java.util.*;
import java.util.zip.*;
import java.io.*;


public class JarLoader {

	protected String jarPath = "";
	protected String cacheDir = "";
	protected String cacheExt = "";
	protected HashMap cache = null;
	protected boolean useDir;
	protected boolean useExt;


	public JarLoader() {
		cacheByDirectory();
	}

	public JarLoader(String jarPath) {
		this.jarPath = jarPath;
		cacheByDirectory();
	}



// CACHING MECHANISM ////////////////////////////////////////////

	public String getCachedFile(String fileName) {
		String file = null;

		try {
			if (this.cache != null) {
				file = (String)this.cache.get(fileName);
			}
		} catch (Exception e) {
			LogUtility.log("getCachedFile(): ", e);
			return null;
		}

		return file;
	}


	public void cacheFiles(String jarPath) {
		setJarPath(jarPath);
		cacheFiles();
	}


	/**
	 * Clears the old cache and caches files from the jar.
	 */
	public void cacheFiles() {
		try {
			this.cache = new HashMap();

			if (this.jarPath == null || this.jarPath.equals("")) {
				LogUtility.log("cacheFiles():  need jar path");
				return;
			}

			String fullFileName = new String(this.jarPath);
			ZipInputStream stream = new ZipInputStream(
					new FileInputStream(fullFileName));

			String curLine = new String();
			ZipEntry entry = null;

			while((entry=stream.getNextEntry()) != null) {

				String entryName = entry.getName();

				if(!entryName.endsWith("/")) {

					// certain files will be cached.
					// check if the entry name starts or ends with
					// the specified string.
					if ((this.useDir && entryName.startsWith(this.cacheDir)) ||
						(this.useExt && entryName.endsWith(this.cacheExt))) {

						BufferedReader is = new BufferedReader(
								new InputStreamReader(stream));
						StringBuffer theDataFile = new StringBuffer();

						//loop through the file reading in a line at a time
						while((curLine = is.readLine()) != null) 
							theDataFile.append(curLine+"\n");

						//is.close();
						curLine = null;

						// Remove the cache dir, if it was used
						if (this.useDir) {
							entryName = 
								entryName.substring(this.cacheDir.length());
						}

						cacheFile(entryName, theDataFile.toString());
					}  // end if 
				}  // end if 
			}  // end of jar-searching while

		} catch(Exception e) {
			LogUtility.log("cacheFiles(): ", e);
		}

		return;
	}


	/**
	 * Uses the full fileName (path and extension as stored in
	 * the cache) as the key to the file's contents.
	 */
	public void cacheFile(String fileName, String retval) {

		if (this.cache == null)
			this.cache = new HashMap();

		this.cache.put(fileName, retval);
	}


////////////////////////////////////////////////////////////////


	

	public BufferedReader getReader(String fileName) {

		BufferedReader is = null;

		try {
			ZipInputStream zipstream = 
				new ZipInputStream(new FileInputStream( this.jarPath ) );
			ZipEntry entry = null;
			String entryName = null;

			// Search the jar for the right file
			while( ( entry=zipstream.getNextEntry() ) != null ) {
				entryName = entry.getName();               
				if( entryName.endsWith( fileName ) ) {
					is = new BufferedReader( 
						new InputStreamReader( zipstream ) );
					break;
				}
			}
		} catch(FileNotFoundException fnf) { 
			LogUtility.log("getReader() FNF!:\n", fnf);
		} catch(Exception ex) { 
			LogUtility.log("getReader() error:\n", ex);
		}

		return is;
	}

	// ======= ACCESS METHODS ========================================= //
	public String getJarPath() { 
		return this.jarPath; 
	}

	public void setJarPath(String path) {
		if (path == null) {
			LogUtility.log("setJarPath(): null path to set");
		}
		this.jarPath = path; 
	}

	public String getCacheDirectory() { 
		return this.cacheDir; 
	}

	public void setCacheDirectory(String dir) {
		this.cacheDir = dir; 
		if (this.cacheDir == null) {
			this.cacheDir = "";
		} else if (!this.cacheDir.endsWith("/")) {
			this.cacheDir += "/";
		}
	}

	public String getCacheExtension() { 
		return this.cacheExt; 
	}

	public void setCacheExtension(String ext) {
		this.cacheExt = ext; 
		if (this.cacheExt == null) {
			this.cacheExt = "";
		} 
	}

	public HashMap getCache() { 
		return this.cache; 
	}

	public void setCache(HashMap cache) { 
		this.cache = cache; 
	}




	public void cacheByExtension() { 
		this.useDir = false; 
		this.useExt = true; 
	}

	public void cacheByDirectory() { 
		this.useDir = true; 
		this.useExt = false; 
	}


}
