package servlet.datafile_exchange;
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
import servlet.datafile_exchange.DataFileServer;
import servlet.util.ServletUtility;
import servlet.datafile_exchange.DataFileDB;



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
 * REQUIRED PARAMETERS:
 * @param exchangeType - type of data exchange, either upload or download<br>
 * @param user - the user name of the priveledged user
 * @param pass - the password of the priveledged user
 * @param clienttype - the type of client {browser application}
 * 
 * <br>
 * 
 * @version 
 * @author 
 * 
 */

public class DataExchangeServlet extends HttpServlet 
{
	//ResourceBundle rb = ResourceBundle.getBundle("dataexchange");

	private String exchangeType = null;
	private String submitter = null;
	private String username = null; // the user name that must be authenticated
	private String password = null;
	private String fileType = null; //non-required parameter
	private String requestContentType = null; // this will be null unless	multipart
	
//	private String uploadDir = rb.getString("uploadDir");
	private String uploadDir = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/";
	private ServletUtility util = new ServletUtility();
  private MultipartRequest multi;
	//if false then no authentication done
	private boolean authenticate = false;
	//if false then dont store the file in a database
	private boolean databaseStorage = false;
	//the database class
	private DataFileDB filedb;
	//if the string below is null then store the uploaded file as the name it is given
	//otherwise copy the filecontents to this file
	private String uploadFileName = "input.data"; 
	//the strings below refer to the page name and url for the user to have access to
	//if the upload process succeeds
	private String referPage = "validate input data for interpolation: ";
	private String referUrl = "http://30.8.11.21:8080/forms/validate.html";
	private int fileMaxSize = 200000000;
	
	
	/**
	 * constructor method
	 */
	public DataExchangeServlet()
	{
		System.out.println("init: DataExchangeServlet");
		//if the database storage option is turned on then
		//construct a method of the datafile database class
		if (databaseStorage == true )
		{
			System.out.println("DataExchangeServlet > using the datafile storage database");
			filedb = new DataFileDB();
		}
		else
		{
			System.out.println("DataExchangeServlet > not using the datafile storage database");
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
			///res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			//for now all the authentication of users for data transfer is going to
			//be handled using the username and password, so if they do not exist let
			//the user know it
			
			
			try 
			{
				//this variable will remain null inless there it is accessed thru a
				//multipart browser
				
				requestContentType = getRequestContentType(req);
				System.out.println("DataExchangeServlet > Request Content Type: " + requestContentType);
				System.out.println("DataExchangeServlet > DataExchangeServlet contacted");
				
				//determine if the client is using multipart encoding
				// which if it is -- assume that they want to upload a file
				if (requestContentType.startsWith("multipart"))
				{
					try 
					{
						System.out.println("DataExchangeServlet > client connected using multipart encoding");
						//then get the user name and password -- this is where the upload 
						//file is defined
						multi=new MultipartRequest(req, uploadDir, fileMaxSize);
					
						// get the un-encoded parameter names back from the 
						// MultipartRequest class
						Enumeration enum = multi.getParameterNames();
						username = multi.getParameter("username");
						password = multi.getParameter("password");
						//if authentication is turned on then do it otherwise just upload
						if (authenticate == true )
						{
							if (authenticateUser(username, password) ==  true)
							{
								//copy the request
								HttpServletRequest reqCopy = req;
								//do the exchange
								out.println( handleExchangeRequest(reqCopy, res) );
							}
							else
							{
								System.out.println("DataExchangeServlet > go home you perp");
							}
						}
						else
						{
							//copy the request
							HttpServletRequest reqCopy = req;
							//do the exchange
							out.println( handleExchangeRequest(reqCopy, res) );
						}
					}
					catch(Exception e)
					{
						System.out.println("Exception: " + e.getMessage() );
					}
				}
				
				
				//no encoding
				else
				{
					System.out.println("DataExchangeServlet > client connected using no encoding");
					// figure out the action
					if ( req.getParameter("action") == null )
					{
						System.out.println("DataExchangeServlet > input params: " + req.toString() );
						out.println( getServletParameters() );
					}
					else
					{
						Hashtable params = util.parameterHash(req);
						System.out.println("DataExchangeServlet > input params: " + params.toString() );
						String action = req.getParameter("action");
						System.out.println("DataExchangeServlet > action: " + action);
						if (action.equals("userfilenum")&&( req.getParameter("username") != null) )
						{
								username = req.getParameter("username");
								out.println( filedb.getUserFileNumber( username) );
						}
						
						else if (action.equals("userfilesummary") && ( req.getParameter("username") != null) )
						{
							 username = req.getParameter("username");
							 System.out.println("DataExchangeServlet > username: " + username);
							 String summary = filedb.getUserFileSummary( username);
							 System.out.println("DataExchangeServlet > file summary: " + summary);
							 out.println(" file summary " + summary);
						}
						//this is for an application to upload a file to the servlet
						else if (action.equals("uploadFile") && ( req.getParameter("username") != null) )
						{
							System.out.println("DataExchangeServlet >  handeling a file upload from an application");
							String s = handleRawDataExchange(req, res) ;
							System.out.println("DataExchangeServlet >  the response to the client: " + s);
							out.println(s); 
						}
						//this is to delete a file from the data file database
						//based on a user name and an file accession number
						else if (action.equals("deletefile") && ( req.getParameter("username") != null) )
						{
							System.out.println("DataExchangeServlet > deleting a file from the database");
							String s = handleFileDeletion(req, res) ;
							System.out.println("DataExchangeServlet > the response to the client: " + s);
							out.println(s ); 
						}
						
						//this is to delete a specific file type from the data file database
						//based on a user name and an file type -- like 'stylesheet'
						else if (action.equals("deletefiletype") && ( req.getParameter("username") != null) )
						{
							System.out.println("DataExchangeServlet > deleting a filetype from the database");
							String s = handleFileTypeDeletion(req, res) ;
							System.out.println("DataExchangeServlet > the response to the client: " + s);
							out.println(s ); 
						}
						
						//if the requestor asks for a style sheet
						else if (action.equals("userdefaultstyle") && ( req.getParameter("username") != null) )
						{
							System.out.println("DataExchangeServlet > looking up a users style sheet");
							String s = handleStyleSheetLookup(req, res);
							System.out.println("DataExchangeServlet > the response to the client: " + s);
							out.println(s ); 
						}
						
						else 
						{
							System.out.println("DataExchangeServlet > unrecognized parameter string");
							out.println( getServletParameters() );
							
						}
					}
				}
			
			}
			catch (Exception e)
			{
				System.out.println("Exception: "+ e.getMessage() );
				e.printStackTrace();
			}
		}
		
		/**
		 * method that will look up a users style sheet
		 */
		 private String handleStyleSheetLookup(HttpServletRequest req, 
		 	HttpServletResponse res)
		 {
			 String s = null;
			 try
			 {
				 username = req.getParameter("username");
				 s = filedb.getUserStyleSheet(username) ;
			 }
			 catch(Exception e) 
			 {
				 System.out.println("Exception: " + e.getMessage() );
				 e.printStackTrace();
			 }
			 return(s);
		 }
	
	/**
	 * methodf to delete a file from the database -- uses the username and the 
	 * file accession number
	 */
	 private String handleFileDeletion(HttpServletRequest req, 
	 	HttpServletResponse res)
	 {
		 StringBuffer s = new StringBuffer();
		 String fileAccession = null;
		 try
		 {
		 		//start the html -- do not do this in the post method bc
		 		// it screws up the upload
		 		s.append("<html>");
		 		Hashtable params = util.parameterHash(req);
		 		String user = params.get("username").toString();
		 		if ( params.containsKey("filenumber") )
				{
					fileAccession = params.get("filenumber").toString() ;
					System.out.println("DataExchangeServlet > fileNumber: " + fileAccession );
				}
				
				else
				{
					System.out.println("DataExchangeServlet > no file number passed" );
				}
				
		 		//make sure they are not null
		 		if (user == null || fileAccession == null)
		 		{
					 s.append("failed because the fileAccessionNumber of username is null");
			 		return( s.toString() );
		 		}
		 		else
		 		{
			 		boolean deletion = filedb.deleteFile(user, fileAccession);
			 
			 		//determin if the deletion succedded at the 
			 		//database and if so copy the file to delete
			 		//to be cleaned up by the DBA
			 		if (deletion == false)
			 		{
				 		s.append("database deletion failed");
			 		}
			 		else
			 		{
				 		s.append("<br> database deletion succeeded <br>");
				 		util.fileCopy(uploadDir+fileAccession, uploadDir+fileAccession+".delete");
			 	 		s.append("<br> set the file for deletion by DBA <br>");
				 		s.append("<a href=\"http://vegbank.nceas.ucsb.edu/framework/servlet/usermanagement\"> profile home </a>" );
			 		}
		 		}
			}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
			s.append("</html>");
		 return(s.toString());
		 
		 
	 }
	 
	 
	 /**
	 * method to delete a specific file type from the database -- 
	 * uses the username and the file type attributes
	 */
	 private String handleFileTypeDeletion(HttpServletRequest req, 
	 	HttpServletResponse res)
	 {
		 StringBuffer s = new StringBuffer();
		 String fileType = null;
		 try
		 {
		 		//start the html -- do not do this in the post method bc
		 		// it screws up the upload
		 		s.append("<html>");
		 		Hashtable params = util.parameterHash(req);
		 		String user = params.get("username").toString();
		 		if ( params.containsKey("filetype") )
				{
					fileType = params.get("filetype").toString() ;
					System.out.println("DataExchangeServlet > deleting fileType: " + fileType );
				}
				
				else
				{
					System.out.println("DataExchangeServlet > no file type passed" );
				}
				
		 		//make sure they are not null
		 		if (user == null || fileType == null)
		 		{
					 s.append("failed because the fileAccessionNumber of username is null");
			 		return( s.toString() );
		 		}
		 		else
		 		{
			 		boolean deletion = filedb.deleteFileType(user, fileType);
			 
			 		//determin if the deletion succedded at the 
			 		//database and if so copy the file to delete
			 		//to be cleaned up by the DBA
			 		if (deletion == false)
			 		{
				 		s.append("database deletion failed");
			 		}
			 		else
			 		{
				 		s.append("<br> database deletion succeeded <br>");
				 		//util.fileCopy(uploadDir+fileAccession, uploadDir+fileAccession+".delete");
			 	 		//s.append("<br> set the file for deletion by DBA <br>");
				 		//s.append("<a href=\"http://vegbank.nceas.ucsb.edu/framework/servlet/usermanagement\"> profile home </a>" );
			 		}
		 		}
			}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
			s.append("</html>");
		 return(s.toString());
		 
		 
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
				 System.out.println("DataExchangeServlet > content type: " + req.getContentType() );
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
		 * methosd that authenticates the user by using the http utility
		 * in the servlet utility -- currently this method requies as input
		 * the user's username and password, but will be extended to use a
		 * cookie in the near future
		 *
		 * @param username -- the user name 
		 * @param password -- the user's password
		 */
		private boolean authenticateUser(String userName, String passWord)
		{
			String xmlResponse = util.httpAuthenticationHandler(userName, passWord
			, "uploadfile");
			
			System.out.println("DataExchangeServlet > " + xmlResponse );
			//if the authentication returns a true response it will look like
			//<authentivation>true</authentication>
			if ( xmlResponse.trim().indexOf("true") > 0)
				return(true);
			else if (xmlResponse.trim().indexOf("false") < 0)
				return(false);
			else
				return(false);
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
				System.out.println("DataExchangeServlet > Request Content Type: "+req.getContentType());

				// determine if the request is encoded and if so pass to the MultipartRequest
				// assuming that the user wants to upload a file and if not multipart encoded
				// then check to see if the user wants to upload or download the file and handle
				// it from there

				if (req.getContentType().startsWith("multipart")) 
				{
					System.out.println("DataExchangeServlet > request type: multipart encoded");
					String s = uploadMultipartDataFile(req, res);
					sb.append(" \n" + s + " \n" );
					System.out.println("DataExchangeServlet > handleExchangeRequest: " + s);
					
				}
				else if ( req.getContentType().startsWith("application") ) 
				{ 
					System.out.println("DataExchangeServlet > request type: not encoded");
					//determine the file exchange type (up or down - load)
					if (req.getParameter("exchangeType") != null ) 
					{
						exchangeType=req.getParameter("exchangeType");
						String s = handleRawDataExchange(req, res);
						sb.append(" \n" + s + " \n" );
					}
					else 
					{
						System.out.println("DataExchangeServlet: unrecognized parameters");
					}
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
	private String uploadMultipartDataFile(HttpServletRequest request, HttpServletResponse response) 
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			// Construct a MultipartRequest to help read the information.
			// Pass in the request, a directory to saves files to, and the
			// maximum POST size we should attempt to handle.
			// Here we (rudely) write to the server root and impose 5 Meg limit.
			// MultipartRequest multi=new MultipartRequest(request, uploadDir, 5 * 1024 * 1024);

      sb.append("<html>");  
      sb.append(" <body>");  
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
				//initialize the global fileType variable 
				if (  name.toUpperCase().equals("FILETYPE") )
				{
					fileType = value;
				}
			}

			// Show which files we received
			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) 
			{
				String name = (String)files.nextElement();
        System.out.println("DataExchangeServlet > name from multipart: " + name );
				String filename = multi.getFilesystemName(name);
        System.out.println("DataExchangeServlet > filesystem name from multipart: " + filename);
				String type = multi.getContentType(name);
        System.out.println("DataExchangeServlet > content type: " + type );

				//try to substitue the name
				File f = multi.getFile(name);
				
				//sb.append("name: " + name +"<br>");
				sb.append("file max size limit (in bytes): " + fileMaxSize  +" <br> \n");	
				sb.append("filename: " + filename +"<br> \n");
				sb.append("type: " + type +"<br> \n");
				
				//if the file is not null then return the length of the file
				//and register the document in the document database if the 
				//flag database storage is true otherwise just put the file
				// in the specified director
				if (f != null && databaseStorage == true ) 
				{
					String fileSize = ""+f.length();
					sb.append("file length: " + fileSize + "bytes <br> \n");
					
					//register the document with the database
					String currentDate = filedb.getCurrentDate();
					//register and make sure that we get back an
					//accession number 
					String accessionNumber = filedb.registerDocument(username, 
					"givenname", currentDate, fileSize, uploadDir, filename, fileType);
					
					//copy the file to a file with the name of the accesion number
					util.fileCopy(uploadDir+filename, uploadDir+accessionNumber);
					//delete the file
					util.flushFile(uploadDir+filename);
					sb.append("file accession number: " + accessionNumber);
				}
				else if ( f != null && databaseStorage == false )
				{
					String fileSize = ""+f.length();
					sb.append("file length: " + fileSize + " bytes <br> \n");
					//see if we should copy the file or leave it with the 
					//current name
					if ( uploadFileName != null )
					{
						// rename the file to the appropriate name -- the name of the file
            // that wil be sent to the rmi server
            File dest = new File(uploadDir+uploadFileName);
            f.renameTo(dest);
					}	
				}
				else
				{
					System.out.println("DataExchangeServlet > don't know what to do w/ request" );
				}
				
				//get the referURL and referPage which can be passed as a parameter 
				//in the html form and if not the default instance variables ar used
				Enumeration rparams = multi.getParameterNames();
				Hashtable h = getURLRefereral( rparams );
				
				sb.append("<br> </br> \n");
				sb.append(" <a href="+h.get("referUrl").toString()+"> "+ 
				h.get("referPage").toString() + "</a> </br> \n");
				sb.append("</html> \n");
        // because this method returns html set the http response to text/html
        response.setContentType("text/html");
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
		 System.out.println("DataExchangeServlet > looking for a referal");
		 Hashtable h = new Hashtable();
		 try
		 {
				String page = null;
				String url = null;
				System.out.println("DataExchangeServlet > parameterd dump: " + params.toString() );
				while (params.hasMoreElements()) 
				{
					//System.out.println("test");
      	  String name = (String)params.nextElement();
    	   	String value = multi.getParameter(name);
					System.out.println("DataExchangeServlet > parameter: "+name+" "+value);
					if ( name.equals("referPage") )
					{
						System.out.println("DataExchangeServlet > refePage: "+ value);
						page = value;
					}
					if ( name.equals("referUrl") )
					{	
						System.out.println("DataExchangeServlet > referUrl: "+ value);
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
  * @param response - the response object linked to the client 
  *
  */
	private String handleRawDataExchange (HttpServletRequest request,  
	HttpServletResponse response) 
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			Enumeration enum =request.getParameterNames();
			Hashtable params = new Hashtable();
			while (enum.hasMoreElements()) 
			{
				String name = (String)enum.nextElement();
				String values[] = request.getParameterValues(name);
				if (values != null) 
				{
					for (int i=0; i<values.length; i++) 
					{
						params.put(name,values[i]);
						System.out.println("DataExchangeServlet > param: " +name+ ", param val: " + values[i] );
					}
				}
			}
 
			//determine if the user wants to upload or download data
			if ( (String)params.get("exchangeType") != null )
			{
 				exchangeType = ((String)params.get("exchangeType")).trim();
				
				//UPLOAD A FILE
				if (exchangeType.equals("upload")) 
				{
					//assume that if the user wants to upload a file he/she will include a
					//username and password as an input parameter -- which should be validated
					
					this.fileType = ((String)params.get("filetype")).trim();
					submitter=((String)params.get("submitter")).trim();
					
					//later check this information in the authenticate method
					sb.append(handleRawDataUpload(request, response, submitter, this.fileType));
					//attempt to register the document in the database
					
				
				}
				if (exchangeType.equals("download")) 
				{
					sb.append("comming soon -- now use the fileDownload class");
				}
			}
		}
		catch ( Exception e ) 
		{
			System.out.println("** failed in: DataExchangeServlet.handleRawDataExchange "
			+e.getMessage());
			e.printStackTrace();
		}
		return(sb.toString());
	}

	


	/**
 	 * This method handles the actual file upload of raw data from the client using
 	 * sockets.  Again this method assumes that it is communicating with a client
 	 * application
 	 *
 	 * @param params - the Hashtable of parameters that should be included in the
 	 * 	query
 	 * @param response - the response object linked to the client 
 	 *
 	 */
 private String handleRawDataUpload (HttpServletRequest request,  
	HttpServletResponse response, String userName, String fileType) 
	{
		//this is the response to the client
		 //the input looks like 'port|xxxx|cookie|xxx' tokenize the interger on the pipe
		StringBuffer sb = new StringBuffer();
		try 
		{
			System.out.println("DataExchangeServlet.handleRawDataUpload - communicating with"
			+"an application");

			//grab an open port from a random pool between 0-8089
			Random r = new Random();
			int port = r.nextInt(8089);
			System.out.println("DataExchangeServlet >  random port is: " + port);
			while(!DataFileServer.portIsAvailable(port))
			{
				port = r.nextInt(8090);
			}
			//pass the port back to the client
			sb.append("port|"+port);

			//make a cookie (random number between 0-999) and send it back to the browser
			Random s = new Random();
			int c = r.nextInt(999);
			String cookie=""+c;
			sb.append("|cookie|"+cookie);
			System.out.println("DataExchangeServlet > cookie: " + cookie);

			//assign the user name of the client to authorize for connection
			String user= userName;

			//spawn the DataFileServer thread
			System.out.println("DataExchangeServlet > spawning: DataFileServer");
			System.out.println("DataExchangeServlet > constructing DFS with: " 
				+ fileType+" "+port+" "+user+" "+cookie.trim() );
			DataFileServer dfs = new DataFileServer(port, user, cookie.trim(), fileType);
			dfs.start();
			
		}
		catch (Exception e) 
		{
			System.out.println("error in DataExchangeServlet.handleRawDataUpload: " 
			+ e.getMessage());
		}
		return(sb.toString() );
	}
}

