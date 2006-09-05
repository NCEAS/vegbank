package org.vegbank.common.utility;

/*
 * Utility class for java servltes for doing a range of utility 
 * type functions including:
 * 		emailing
 * 		figuring the the type of client browser
 *    etc.. 
 *
 *	'$Author: berkley $'
 *  '$Date: 2006-09-05 23:00:02 $'
 *  '$Revision: 1.2 $'
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
 */


import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;
import org.vegbank.common.Constants;

public class ZipUtility 
{
	private static Log log = LogFactory.getLog(ZipUtility.class); 
	
	/**
	 * method that takes a Hashtable containing fileContent and desired fileName 
	 * and an OutputStream that represents the created zip file .
	 * 
	 * @param nameContent -- the Hashtable containing the filesContent (as String) and the file names
	 * @param outStream -- OutputStream to  take zipped up files
	 *
	 */
	public static void zipTextFiles(Hashtable nameContent, OutputStream outStream) throws IOException {

		ZipOutputStream zipstream = new ZipOutputStream(outStream);
		
		Enumeration names = nameContent.keys();
		while ( names.hasMoreElements() )
		{
			String fileName = (String) names.nextElement();
			String fileContent = (String) nameContent.get(fileName);
		
			ZipEntry zipentry = new ZipEntry(fileName);
			zipstream.putNextEntry(zipentry);
			
            // the old way used ByteArrayInputStream which doesn't handle charset encoding well
            // so we're using the StringReader method below instead
            StringReader reader = new StringReader(fileContent);
            int numread;
            char[] c = new char[1024];
            numread = reader.read(c, 0, 1024);
            while (numread != -1) {
              String s = new String(c, 0, numread);
              zipstream.write(s.getBytes());
              numread = reader.read(c, 0, 1024);
            }

			reader.close();	
		}

		zipstream.flush();
		zipstream.close();
	}
	

	/**
	 *  Method to compress an inputstream using GZIP compression
	 *
	 * @param  inFile  a string representing the input file
	 * @param  outFile a string representing the output, compressed, file
	 */
	public static byte[] gzipCompress(byte[]  ba) throws IOException
	{
		ByteArrayOutputStream compressed = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(compressed);
		gzout.write( ba );
		gzout.flush();
		gzout.close();
		return compressed.toByteArray();
	}


	/**
	 * Takes a File and  unzips it 
	 * 
	 */
	public static Collection unZip(FormFile inFile) throws Exception 
	{
		Vector fileList = new Vector();
		InputStream in = inFile.getInputStream();
		ZipInputStream zis = new ZipInputStream(in);
		java.util.zip.ZipEntry e;
		// Loop over every ZipEntry in ZIP file
		while( ( e = zis.getNextEntry()) != null)
		{
			File outFile = new File(e.getName());
			FileOutputStream out = new FileOutputStream(outFile);
			
			byte[] b = new byte[512];
			int len = 0;
			while ( ( len=zis.read(b) ) != -1)
			{
				out.write(b,0,len);
			}
			fileList.add( new FileWrapper(outFile) );
		}
		zis.close();
		
		return fileList;
	}


    public static void unzipToDir(String inputFilePath) {
      try {
              File file = new File(inputFilePath);
              ZipFile zipFile = new ZipFile(file);
              
              // create a directory named the same as the zip file in the 
              // same directory as the zip file.
              File zipDir = new File(file.getParentFile(), inputFilePath.substring(0, inputFilePath.length()-".zip".length()));
              zipDir.mkdir();
              
              Enumeration entries = zipFile.entries();
              while(entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry)entries.nextElement();
                    
                    String nme = entry.getName();
                    // File for current file or directory
                    File entryDestination = new File(zipDir, nme);
                    
                    // This file may be in a subfolder in the Zip bundle
                    // This line ensures the parent folders are all
                    // created.
                    entryDestination.getParentFile().mkdirs();
                    
                    // Directories are included as seperate entries 
                    // in the zip file.
                    if(!entry.isDirectory()) {
                            generateFile(entryDestination, entry, zipFile);
                    }
              }
      }
      catch(IOException e) {
            e.printStackTrace();
      }
      
    }
 
    public static void generateFile(File destination, ZipEntry entry, ZipFile owner) throws IOException {
            InputStream in = null;
            OutputStream out = null;
            try {
                    InputStream rawIn = owner.getInputStream(entry);
                    in = new BufferedInputStream(rawIn);
                                            
                    FileOutputStream rawOut = new FileOutputStream(destination);
                    out = new BufferedOutputStream(rawOut);
                                            
                    // pump data from zip file into new files
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                    }
            }
            finally {
                    if(in != null) {
                            in.close();
                    }
                    if(out != null) {
                            out.close();
                    }
            }
    } 

    public static void zipToFile(String inputFileDir, String inputFileName, String outputFileName) {
     
            ZipOutputStream out = null;
            InputStream in = null;
            try {
                    File inputFile1 = new File(inputFileDir + File.separator + inputFileName);
                    File outputFile = new File(outputFileName);
                    
                    OutputStream rawOut = new BufferedOutputStream(new FileOutputStream(outputFile));
                    out = new ZipOutputStream(rawOut);
                    // optional - manages amount of compression
                    // out.setLevel(java.util.zip.Deflator.BEST_COMPRESSION);
                    
                    InputStream rawIn = new FileInputStream(inputFile1);
                    in = new BufferedInputStream(rawIn);
                    
                     // entry for our file
                     // should be root/sub/sub2/myFile.txt if we want it
                     // 3 folders deep
                    ZipEntry entry = new ZipEntry(inputFileName);
                    // notify output stream of entry.
                    out.putNextEntry(entry);
                    
                    // pump data from file into zip file
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                    }
            }
            catch(IOException e) {
                    e.printStackTrace();
            }
            finally {
                    try {
                            if(in != null) {
                                    in.close();
                            }
                            if(out != null) {
                                    out.close();
                            }
                    }
                    catch(IOException ignored) { }
                    
            }
     
    }

}	

