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

import multipart.*;




/**
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
 *
 * <p>Valid parameters are:<br><br>
 * 
 * exchangeType - type of data exchange, either upload or download<br>
 * <br>
 * 
 * @version Feb. 09 2001
 * @author John Harris
 * 
 */

public class DataExchangeServlet extends HttpServlet 
{
	ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

	private String exchangeType = null;
	private String submitter = null;
	private String password = null;
	private String uploadDir =(rb.getString("requestparams.uploadDir"));

	
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
				Enumeration enum =req.getParameterNames();
				Hashtable params = new Hashtable();
				System.out.println("***"+req.getContentType());

				// determine if the request is encoded and if so pass to the MultipartRequest
				// assuming that the user wants to upload a file and if not multipart encoded
				// then check to see if the user wants to upload or download the file and handle
				// it from there

				if (req.getContentType().startsWith("multipart")) 
				{
					System.out.println("request type: multipart");
					uploadMultipartDataFile(req, res, out);
				}
				else if ( req.getContentType().startsWith("application") ) 
				{ 
					System.out.println("request type: not encoded");
					//determine the file exchange type (up or down - load)
					if (req.getParameter("exchangeType") != null ) 
					{
						exchangeType=req.getParameter("exchangeType");
						handleRawDataExchange(req, res, out);
					}
					else 
					{
						out.println("unrecognized parameters");
					}
				}
			}
			catch ( Exception e ) 
			{
				System.out.println("** failed in: DataExchangeServlet.main "
				+e.getMessage());
			}
		}



 	/**
 	 * This method handles the file upload for a file and associated parameters that
 	 * have been encoded using the multipart/form-data codec.  This method uses the
 	 * MultiPartRequest package that was obtained at the servlets.com site
 	 *
 	 * @param params - the Hashtable of parameters that should be included in the
 	 * 	query
 	 * @param out - the output stream to the client
 	 * @param response - the response object linked to the client 
 	 *
 	 */
	private void  uploadMultipartDataFile (HttpServletRequest request,  
	HttpServletResponse response, PrintWriter out) 
	{
		try 
		{
		
			// Construct a MultipartRequest to help read the information.
			// Pass in the request, a directory to saves files to, and the
			// maximum POST size we should attempt to handle.
			// Here we (rudely) write to the server root and impose 5 Meg limit.
			MultipartRequest multi=new MultipartRequest(request, uploadDir, 5 * 1024 * 1024);

			//get the un-encoded parameter names back from the MultipartRequest class
			Enumeration params = multi.getParameterNames();
      while (params.hasMoreElements()) 
			{
        String name = (String)params.nextElement();
        String value = multi.getParameter(name);
        out.println(name + " = " + value);
			}

			// Show which files we received
			out.println("<H3>Files:</H3>");
			out.println("<PRE>");
			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) 
			{
				String name = (String)files.nextElement();
				String filename = multi.getFilesystemName(name);
				String type = multi.getContentType(name);
				File f = multi.getFile(name);
				out.println("name: " + name);
				out.println("filename: " + filename);
				out.println("type: " + type);
	
				if (f != null) 
				{
					out.println("length: " + f.length());
					out.println();
				}
				out.println("</PRE>");
			}
		}
 		catch (Exception e) 
		{ 
			out.println("<PRE>"); e.printStackTrace(out);
      out.println("</PRE>");  
		}
	}


 /**
  * This method handles the data file exchange of non-encoded data and is
  * intended for transfer of data between the servlet and a non-browser client
  * application using sockets.  After the submitter and password are validated the
  * request, reponse and outwriter are passed along to the handleRawDataUpload 
  * method where socket-connection negotiation and file upload takes place with the 
  * client application.  This is the code that has been taken from the
  * Metacat/berkley project
  *
  * @param params - the Hashtable of parameters that should be included in the
  * 	query
  * @param out - the output stream to the client
  * @param response - the response object linked to the client 
  *
  */


	private void handleRawDataExchange (HttpServletRequest request,  
	HttpServletResponse response, PrintWriter out) 
	{
		try 
		{
			Enumeration enum =request.getParameterNames();
			Hashtable params = new Hashtable();
			while (enum.hasMoreElements()) 
			{
				String name = (String) enum.nextElement();
				String values[] = request.getParameterValues(name);
				if (values != null) 
				{
					for (int i=0; i<values.length; i++) 
					{
						params.put(name,values[i]);
						System.out.println( name+" "+values[i] );
					}
				}
			}
 
			//determine if the user wants to upload or download data
			if ( (String)params.get("exchangeType") != null )
			{
 				exchangeType = ((String)params.get("exchangeType")).trim();
				//out.println("exchangeType: "+exchangeType);
	
				if (exchangeType.equals("upload")) 
				{
					//assume that if the user wants to upload a file he/she will include a
					//username and password as an input parameter -- which should be validated
		
					submitter=((String)params.get("submitter")).trim();
					password=((String)params.get("password")).trim();
	
					//later check this information in the authenticate method
					handleRawDataUpload (request, response, out);
				}
				if (exchangeType.equals("download")) 
				{
					out.println("comming soon -- now use the fileDownload class");
				}
			}
		}
		catch ( Exception e ) 
		{
			System.out.println("** failed in: DataExchangeServlet.handleRawDataExchange "
			+e.getMessage());
		}
	}



	/**
 	 * This method handles the actual file upload of raw data from the client using
 	 * sockets.  Again this method assumes that it is communicating with a client
 	 * application
 	 *
 	 * @param params - the Hashtable of parameters that should be included in the
 	 * 	query
 	 * @param out - the output stream to the client
 	 * @param response - the response object linked to the client 
 	 *
 	 */

 private void handleRawDataUpload (HttpServletRequest request,  
	HttpServletResponse response, PrintWriter out) 
	{
		try 
		{
			System.out.println("DataExchangeServlet.handleRawDataUpload - communicating with"
			+"an application");

			//grab an open port from a random pool between 0-8089
			Random r = new Random();
			int port = r.nextInt(8089);
			System.out.println("random port is: " + port);
			while(!DataFileServer.portIsAvailable(port))
			{
				port = r.nextInt(8090);
				//System.out.println("next port used: " + port);
			}
			//pass the port back to the client
			out.println("port|"+port);

			//make a cookie (random number between 0-999) and send it back to the browser
			Random s = new Random();
			int c = r.nextInt(999);
			String cookie=""+c;
			out.println("|cookie|"+cookie);
			System.out.println("cookie: " + cookie);

			//assign the user name of the client to authorize for connection
			String user="authenticatedUser";

			//spawn the DataFileServer thread
			DataFileServer dfs = new DataFileServer(port, user, cookie.trim());
			dfs.start();
		}
		catch (Exception e) 
		{
			System.out.println("error in DataExchangeServlet.handleRawDataUpload: " 
			+ e.getMessage());
		}
	}
}

