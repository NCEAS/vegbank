/*	
 * '$RCSfile: FileWrapper.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-08-27 23:28:22 $'
 *	'$Revision: 1.2 $'
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
package org.vegbank.common.utility;

import java.io.*;

import org.apache.struts.upload.FormFile;


/**
 * @author Gabriel
 *
 * Simple  wrapper around the java.util.File and the 
 * org.apache.struts.upload.FormFile classes
 */
public class FileWrapper implements FormFile
{			
	private String fileName;
	private String contentType;
	private InputStream inputStream;
	private boolean isFormFile;
	private int fileSize = 0;
	private File file = null;
	private FormFile formFile = null;
	
	public FileWrapper( File f ) throws Exception
	{
		isFormFile = false;
		file = f;
		inputStream = new BufferedInputStream(new FileInputStream(f));
		fileName = f.getName();
	}
	
	public FileWrapper( FormFile f ) throws Exception
	{
		isFormFile = true;
		formFile = f;
		inputStream = f.getInputStream();
		fileName = f.getFileName();
		fileSize = f.getFileSize();
	}
	
	/**
	 * @return Returns an InputStream
	 */
	public InputStream getInputStream()
	{
		return inputStream;
	}
	
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @return 
	 */
	public void setFileName(String s)
	{
		if (isFormFile) {
			formFile.setFileName(s);
		} 
		fileName = s;
	}		


	/**
	 * @return Returns the fileSize.
	 */
	public int getFileSize()
	{
		if (isFormFile) {
			return formFile.getFileSize();
		}
		return fileSize;
	}		

	/**
	 * @return Sets the fileSize.
	 */
	public void setFileSize(int i)
	{
		if (isFormFile) {
			formFile.setFileSize(i);
		}
		fileSize = i;
	}		

	/**
	 * 
	 */
	public String getContentType()
	{
		if (isFormFile) {
			return formFile.getContentType();
		}
		return null;
	}

	/**
	 * 
	 */
	public void setContentType(String s)
	{
		if (isFormFile) {
			formFile.setContentType(s);
		}
		// else do nothing
	}

	/**
	 * 
	 */
	public void destroy()
	{
		if (isFormFile) {
			formFile.destroy();
		} else {
			file.delete();
			file = null;
		}
	}

	/**
	 * 
	 */
	public byte[] getFileData() throws java.io.FileNotFoundException, java.io.IOException
	{
		if (isFormFile) {
			return formFile.getFileData();

		} else {
			StringBuffer sb = new StringBuffer(fileSize);
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			for (int d=in.read(); d != -1; d=in.read()) {
				sb.append((char)d);
			}

			return sb.toString().getBytes();
		}
	}

}
