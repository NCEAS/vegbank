/*
 *	'$RCSfile: DataFileExchange.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-01 23:09:58 $'
 *	'$Revision: 1.4 $'
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

package org.vegbank.common.utility.datafileexchange;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import servlet.multipart.MultipartRequest;

import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.Utility;

/**
 * Handles file uploading / downloading to/from a browser using
 * multipart/form-data encription or any other client that uses this encoding
 * or an un-encoded data exchange.  The multipart encryption transfer uses the
 * code in the  book "Java Servlet Programming" by Hunter & Crawford and newer
 * versions of this code can be found at: 
 * http://www.servlets.com/resources/com.oreilly.servlet/
 * The code used for the non-encoded data-file-exchange was taken from the
 * Metacat project specifically the classes DataFileServer, and DataStreamTest
 * written by Chad Berkley.
 *
 *
 * <p>Valid parameters are:<br><br>
 * 
 * REQUIRED PARAMETERS:
 * @param exchangeType - type of data exchange, either upload or download<br>
 * @param user - the user name of the priveledged user
 * @param pass - the password of the priveledged user
 * @param clienttype - the type of client {browser application}
 * 
 * <br>
 * 
 * @version 
 * @author  farrell
 * 
 */
public class DataFileExchange
{

	private String originalDataStore = Utility.VB_HOME_DIR + "/originalDataStore";
	private ServletUtility util = new ServletUtility();
	private MultipartRequest multi;

	//if false then dont store the file in a database
	private boolean databaseStorage = true;
	//the database class
	private DataFileDB filedb;
	//if the string below is null then store the uploaded file as the name it is given
	//otherwise copy the filecontents to this file
	private String uploadFileName = "input.data";
	//the strings below refer to the page name and url for the user to have access to
	//if the upload process succeeds

	// FIXME: Hard coding
	private String referPage = "validate input data for interpolation: ";
	private int fileMaxSize = 200000000;

	/**
	 * constructor method
	 */
	public DataFileExchange(HttpServletRequest req)  throws IOException
	{
		System.out.println("init: DataFileExchange");
		//if the database storage option is turned on then
		//construct a method of the datafile database class
		if (databaseStorage == true)
		{
			System.out.println(
				"DataExchangeServlet > using the datafile storage database");
			filedb = new DataFileDB();
		}
		else
		{
			System.out.println(
				"DataExchangeServlet > not using the datafile storage database");
		}
		multi = new MultipartRequest(req, originalDataStore, fileMaxSize);
	}

	
  public String getMultiPartParameter(String parameterName)
  {
  	String result = null;
  	System.out.println("DataExchange > Looking for parameter: " + parameterName );
  	// Check for null
  	if ( parameterName != null)
  	{
			result = multi.getParameter(parameterName);
  	}
    return result;
  }
  
  public boolean uploadMultiPartFile() 
	{
		boolean success = uploadMultipartDataFile();
		return success;
	}

	/**
		* method that retuns the content request conetent type
		* if the request was made thru a browser using multipart
		* encoding then the return string will start with multipart
		* else the string 'null' si returned
		*/
	public String getRequestContentType(HttpServletRequest req)
	{
		String s = null;
		try
		{
			System.out.println(
				"DataExchangeServlet > content type: " + req.getContentType());
			if (req.getContentType() != null)
			{
				s = req.getContentType();
			}
			else
			{
				s = "null";
			}
		}
		catch (Exception e)
		{
			System.out.println("DataExchangeServlet > Exception: " + e.getMessage());
		}
		return (s);
	}


	/**
		* This method handles the file upload for a file and associated parameters that
		* have been encoded using the multipart/form-data codec.  This method uses the
		* MultiPartRequest package that was obtained at the servlets.com site
		*
		* @param params - the Hashtable of parameters that should be included in the
		* 	query
		* @param response - the response object linked to the client 
		*
		*/
	private boolean uploadMultipartDataFile()
	{
		boolean result = true;
		try
		{
			// Show which files we received
			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements())
			{
				String name = (String) files.nextElement();
				System.out.println(
					"DataExchangeServlet > name from multipart: " + name);
				String filename = multi.getFilesystemName(name);
				System.out.println(
					"DataExchangeServlet > filesystem name from multipart: " + filename);
				String type = multi.getContentType(name);
				System.out.println("DataExchangeServlet > content type: " + type);

				//try to substitue the name
				File f = multi.getFile(name);

				//if the file is not null then return the length of the file
				//and register the document in the document database if the 
				//flag database storage is true otherwise just put the file
				// in the specified director
				if (f != null && databaseStorage == true)
				{
					int fileSize = (int) f.length();
					//register the document with the database
					int accessionId = filedb.getNewAccessionId();
					//register 
					filedb.registerDocument(accessionId, fileSize, filename, originalDataStore);

					//copy the file to a file with the name of the accesion number
					util.fileCopy(
						originalDataStore + "/" + filename,
						originalDataStore + "/" + accessionId);

					// Ugly hack to make RMI processing happy
					File fileForRMI = new File(originalDataStore, uploadFileName);
					f.renameTo(fileForRMI);
					System.out.println(
						"DataExchangeServlet > Renamed file '"
							+ f.getName()
							+ "' to '"
							+ fileForRMI
							+ "'");

					//delete the file
					util.flushFile(originalDataStore + filename);
				}
				else
					if (f != null && databaseStorage == false)
					{
						String fileSize = "" + f.length();
						//see if we should copy the file or leave it with the 
						//current name
						if (uploadFileName != null)
						{
							// rename the file to the appropriate name -- the name of the file
							// that wil be sent to the rmi server
							File dest = new File(originalDataStore + uploadFileName);
							f.renameTo(dest);
						}
						else
						{
							// Need to get a unique name for file									
						}
					}
					else
					{
						result = false;
						System.out.println(
							"DataExchangeServlet > don't know what to do w/ request");
					}

			}
		}
		catch (Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		return true;
	}
}
