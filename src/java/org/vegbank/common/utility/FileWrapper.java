/*	
 * '$RCSfile: FileWrapper.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-02-27 19:13:52 $'
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
 */
package org.vegbank.common.utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.struts.upload.FormFile;


/**
 * @author Gabriel
 *
 * Simple  wrapper around the java.util.File and the 
 * org.apache.struts.upload.FormFile classes
 */
public class FileWrapper
{			
	private String fileName;
	private InputStream inputstream;
	private int fileSize = 0;
	
	public FileWrapper( File file ) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		inputstream = new BufferedInputStream(new FileInputStream(file));
		fileName = file.getName();
	}
	
	public FileWrapper( FormFile file ) throws Exception
	{
		inputstream = file.getInputStream();
		fileName = file.getFileName();
		fileSize = file.getFileSize();
	}
	
	/**
	 * @return Returns an InputStream
	 */
	public InputStream getInputStream()
	{
		return inputstream;
	}
	
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName()
	{
		return fileName;
	}
	/**
	 * @return Returns the fileSize.
	 */
	public int getFileSize()
	{
		return fileSize;
	}
}
