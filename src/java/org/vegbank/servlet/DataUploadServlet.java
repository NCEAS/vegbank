/*
 * '$RCSfile: DataUploadServlet.java,v $'
 * 
 * Purpose: Upload and save any file.
 * 
 * '$Author: anderson $'
 * '$Date: 2004-11-01 23:51:01 $'
 * '$Revision: 1.1 $'
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
 * Servlet to handle file uploading / downloading to/from a browser using
 * multipart/form-data encription or any other client that uses this encoding
 * or an un-encoded data exchange.  The multipart encryption transfer uses the
 * code in the  book "Java Servlet Programming" by Hunter & Crawford and newer
 * versions of this code can be found at: 
 * http://www.servlets.com/resources/com.oreilly.servlet/
 * The code used for the non-encoded data-file-exchange was taken from the
 * Metacat project specifically the classes DataFileServer, and DataStreamTest
 * written by Chad Berkley.
 *
 */

package org.vegbank.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import servlet.multipart.*;
import servlet.util.ServletUtility;


public class DataUploadServlet extends HttpServlet 
{
	private static ResourceBundle vegbankPropFile;

	private String uploadDir;
	private ServletUtility util = new ServletUtility();
    private MultipartRequest multi;

	private String uploadFileName = null;

	private String referPage = "validate input data for interpolation: ";
	private String referUrl = "/forms/validate.html";
	private int fileMaxSize = 200000000;
	
	
	/**
	 * constructor method
	 */
	public DataUploadServlet()
	{
		System.out.println("init: DataUploadServlet");
		vegbankPropFile = ResourceBundle.getBundle("vegbank");
		uploadDir = vegbankPropFile.getString("vegbank.data.dir");
		if (Utility.isStringNullOrEmpty(uploadDir)) {
			uploadDir = "/usr/vegbank/upload/tmp/";
		} else {
			if (!uploadDir.endsWith("/")) {
				uploadDir += "/";
			}
			uploadDir += "tmp";
		}
	}
	
	
	/** Handle "GET" method requests from HTTP clients */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
  	throws IOException, ServletException 	
		{
    	doPost(request, response);
		}

	/** Handle "POST" method requests from HTTP clients */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
  	throws ServletException, IOException 
		{
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			try 
			{
				if (getRequestContentType(req).startsWith("multipart"))
				{
					try 
					{
						System.out.println("DataUploadServlet > client connected using multipart encoding");
						//then get the user name and password -- this is where the upload 
						//file is defined
						multi=new MultipartRequest(req, uploadDir, fileMaxSize);
						// get the un-encoded parameter names back from the 
						// MultipartRequest class
						Enumeration enum = multi.getParameterNames();
						
						//copy the request
						HttpServletRequest reqCopy = req;
						//do the exchange
						out.println( handleExchangeRequest(reqCopy, res) );
						
					}
					catch(Exception e)
					{
						System.out.println("Exception: " + e.getMessage() );
					}
				}
				//no encoding
			}
			catch (Exception e)
			{
				System.out.println("Exception: "+ e.getMessage() );
				e.printStackTrace();
			}
		}
		
		
	
	
	
	/** 
	 * merthod that passes back a string explaining the parameters that 
	 * amy / must be passed to this servlet
	 */
	private String getServletParameters()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<br> required parameters: <br>");
		sb.append("action {filedownload, userfilenumber, userfilesummary}");
		sb.append("</html>");
		return(sb.toString() );
	}
	
		/**
		 * method that retuns the content request conetent type
		 * if the request was made thru a browser using multipart
		 * encoding then the return string will start with multipart
		 * else the string 'null' si returned
		 */
		 private String getRequestContentType(HttpServletRequest req)
		 {
			 String s = null;
			 try
			 {
				 System.out.println("DataUploadServlet > content type: " + req.getContentType() );
				 if ( req.getContentType() != null )
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
				 System.out.println("Exception: " + e.getMessage() );
			 }
			 return(s);
		 }
		 

		
		/**
		 * method that acts as the top level exchange request broker -- where
		 * by the exchange type is determined and the exchange is carried out
		 * this method will return a message regarding the summary of the 
		 * exchange results
		 */
		 private String handleExchangeRequest( HttpServletRequest req, 
		 HttpServletResponse res)
		 {
			 //this is the response to be sent back to the browser
			StringBuffer sb = new StringBuffer();
    	try 
			{
				Enumeration enum =req.getParameterNames();
				Hashtable params = new Hashtable();
				System.out.println("DataUploadServlet > Request Content Type: "+req.getContentType());

				// determine if the request is encoded and if so pass to the MultipartRequest
				// assuming that the user wants to upload a file and if not multipart encoded
				// then check to see if the user wants to upload or download the file and handle
				// it from there

				if (req.getContentType().startsWith("multipart")) 
				{
					System.out.println("DataUploadServlet > request type: multipart encoded");
					String s = uploadMultipartDataFile(req, res);
					sb.append(" \n" + s + " \n" );
					System.out.println("DataUploadServlet > handleExchangeRequest: " + s);
				}
			}
			catch ( Exception e ) 
			{
				System.out.println("Exception: " + e.getMessage());
			}
			 return( sb.toString() );
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
	private String uploadMultipartDataFile (HttpServletRequest request,  
	HttpServletResponse response) 
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			// Construct a MultipartRequest to help read the information.
			// Pass in the request, a directory to saves files to, and the
			// maximum POST size we should attempt to handle.
			// Here we (rudely) write to the server root and impose 5 Meg limit.
			//MultipartRequest multi=new MultipartRequest(request, uploadDir, 5 * 1024 * 1024);

			
      sb.append("<html>");  
      sb.append("<body>");  
			//get the un-encoded parameter names back from the MultipartRequest class
			Enumeration params = multi.getParameterNames();
      while (params.hasMoreElements()) 
			{
        String name = (String)params.nextElement();
        String value = multi.getParameter(name);
				
        //do not produce the password to the screen
				if ( ! name.toUpperCase().equals("PASSWORD") )
				{
					sb.append(name + " = " + value + "<br>");
				}
			}

			// Show which files we received
			//out.println("<H3>Files:</H3>");
			//out.println("<PRE>");
			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) 
			{
				String name = (String)files.nextElement();
				String filename = multi.getFilesystemName(name);
				String type = multi.getContentType(name);
				
				//try to substitue the name
				File f = multi.getFile(name);
				
				//sb.append("name: " + name +"<br>");
				sb.append("file max size limit (in bytes): " + fileMaxSize  +" <br> \n");	
				sb.append("filename: " + filename +"<br> \n");
				
				
				if ( f != null )
				{
					String fileSize = ""+f.length();
					sb.append("file length: " + fileSize + " bytes <br> \n");
					//see if we should copy the file or leave it with the 
					//current name

					/*
					if ( uploadFileName != null )
					{
						//copy the file to a file with the name of the accesion number
						util.fileCopy(uploadDir+filename, uploadDir+uploadFileName);
						//delete the file
						util.flushFile(uploadDir+filename);
					}
					*/
					
				}
				else
				{
					System.out.println("DataUploadServlet > don't know what to do w/ request" );
				}
				
				//get the referURL and referPage which can be passed as a parameter 
				//in the html form and if not the default instance variables ar used
				Enumeration rparams = multi.getParameterNames();
				Hashtable h = getURLRefereral( rparams );
				
				sb.append("<br> </br> \n");
				sb.append("</PRE> \n");
			}
		}
 		catch (Exception e) 
		{ 
			sb.append("<PRE> \n"); 
			e.printStackTrace();
			sb.append( e.getMessage() );
      sb.append("</PRE> \n");  
      sb.append("</body> \n");  
      sb.append("</html> \n");  
		}
		return(sb.toString());
	}

	
	/**
	 * method that looks to see if a referal is sent as a parameter to this
	 * servelt and if it is it is used otherwise the instance variables are
	 * used
	 *
	 * @param params -- hashtable with the paramteres
	 *
	 */
	 private Hashtable getURLRefereral(Enumeration params )
	 {
		 System.out.println("DataUploadServlet > looking for a referal");
		 Hashtable h = new Hashtable();
		 try
		 {
				String page = null;
				String url = null;
				System.out.println("DataUploadServlet > parameterd dump: " + params.toString() );
				while (params.hasMoreElements()) 
				{
					//System.out.println("test");
      	  String name = (String)params.nextElement();
    	   	String value = multi.getParameter(name);
					System.out.println("DataUploadServlet > parameter: "+name+" "+value);
					if ( name.equals("referPage") )
					{
						System.out.println("DataUploadServlet > refePage: "+ value);
						page = value;
					}
					if ( name.equals("referUrl") )
					{	
						System.out.println("DataUploadServlet > referUrl: "+ value);
						url = value;
					}
				}
				//if the paramters were passed
				if (page != null && url != null )
				{
					h.put("referPage", page);
					h.put("referUrl", url);
				}
				//else just use the instance variables
				else
				{
					h.put("referPage", referPage);
					h.put("referUrl", referUrl);
				}
			
			}
			catch ( Exception e ) 
			{
				System.out.println("Exception: " + e.getMessage());
				e.printStackTrace();
			}
			return(h);
	 }
	 
	 



}

