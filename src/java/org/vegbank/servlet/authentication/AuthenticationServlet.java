package org.vegbank.servlet.authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.LogUtility;



/**
 * Servlet to perform user authentication for all the
 * servlets within a domain.  This servlet will recognize varying
 * degrees of authentication for users and will place cookies representing
 * the authentication level
 *
 * <p>Valid parameters are:<br><br>
 * REQUIRED PARAMETERS
 * @param userName - user name of the user tying to be authenticated <br>
 * @param password - the password of the user attempting to be authenticated <br>
 * @param authType -- the authentication type {loginUser, uploadFile}
 *
 *
 *  '$Author: anderson $'
 *  '$Date: 2003-11-10 19:18:36 $'
 *  '$Revision: 1.11 $'
 *
 *  @version
 *  @author
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
 */

public class AuthenticationServlet extends HttpServlet
{
	public ServletUtility su = new ServletUtility();
	public UserDatabaseAccess uda = new UserDatabaseAccess();

	// THE RESOURCE BUNDLE AND THE ASSOCIATED PARAMETERS
	private String mailHost = "";
	private String cc = "";
	private String genericForm = "";
	private String genericTemplate = "";

	//constructor
	public AuthenticationServlet()
	{
		try
		{
			LogUtility.log("AuthenticationServlet > init");
			ResourceBundle rb = ResourceBundle.getBundle("authentication");
			this.mailHost = rb.getString("mailHost");
			this.cc = rb.getString("systemEmail");		
			this.genericForm =  rb.getString("genericForm");
			this.genericTemplate = 	 rb.getString("genericTemplate");
				
			LogUtility.log("AuthenticationServlet > init" );
		}
		catch (Exception e)
		{
			LogUtility.log("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}

	/**
	 * Handle "GET" method requests from HTTP clients
	 */
	public void doPost(HttpServletRequest request,
	HttpServletResponse response)
	throws IOException, ServletException
	{
		doGet(request, response);
	}

	/**
	 * Handle "POST" method requests from HTTP clients
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{

		//clientLogFile=rb.getString("requestparams.clientLog");
		//LogUtility.log("the client info log file is: "+clientLogFile);

		//first block will look for the userName and password as well as valid cookie
		try
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			Cookie registeredCookie = null;

			//get the request - parameters using the
			//servlet Utility class
			Hashtable requestParams = su.parameterHash(request);
			//LogUtility.log("AuthenticationServlet > " + requestParams.toString() );
			//grab the remote host information
			String remoteHost=request.getRemoteHost();
			String remoteAddress = request.getRemoteAddr();
			LogUtility.log("AuthenticationServlet > remoteHost: "+remoteHost+" Address: "+remoteAddress);


			//determine what the client wants to do
			if ( requestParams.containsKey("authType") == false)
			{
				out.println("AuthenticationServlet > PARAMETERS INCLUDE: " + this.getParameters() );
			}
			else
			{
				//figure out what the client wants to do -- exactly
				//GET A LOST PASSWORD
				if ( requestParams.get("authType").toString().toUpperCase().equals("GETPASSWORD")  )
				{
					String s =  handlePasswordRetreival(requestParams);
					out.println(s);
				}
				//LOGIN THE USER
				else if ( requestParams.get("authType").toString().equals("loginUser")  )
				{
					LogUtility.log("AuthenticationServlet > user login attempt");
					if ( authenticateUser(requestParams, remoteAddress) == true)
					{
						//log the login in the clientLogger
						//set a cookie
						String user = getUserName(requestParams);
						Cookie cookie = new Cookie("null", "null");
						//AuthenticationServlet m =new AuthenticationServlet();
						registeredCookie = cookieDelegator(cookie, requestParams, remoteAddress, user);
						response.addCookie(registeredCookie);
						registeredCookie = cookieDelegator(cookie, requestParams, remoteAddress, user);
						response.addCookie(registeredCookie);
						HttpSession session = request.getSession();
						session.setAttribute("emailAddress", request.getParameter("userName") );
						//send the user to the correct page
						Thread.sleep(1100);
						String redirect = "/vegbank/servlet/usermanagement?action=options";
						LogUtility.log("AuthentictionServlet > redirecting to: " + redirect);
						response.sendRedirect(redirect);
					}
					else
					{
						LogUtility.log("AuthenticationServlet > user login failed");
						out.println( getErrorRedirection() );
					}
				}
				//AUTHENTICATE THE USER TO UPLOAD A FILE OR SOMETHING SIMILAR
				else if ( requestParams.get("authType").toString().equals("uploadfile")  )
				{
					LogUtility.log("AuthenticationServlet > authenticating for file upload");
					if ( authenticateUser(requestParams, remoteAddress) == true)
					{
						out.println("<authentication>true</authentication>");
						//set a cookie
						String user = getUserName(requestParams );
						Cookie cookie = new Cookie("null", "null");
						AuthenticationServlet m =new AuthenticationServlet();
						m.cookieDelegator(cookie, requestParams, remoteAddress, user);
						response.addCookie(registeredCookie);
					}
					else
					{
						out.println("<authentication>false</authentication>");
					}
				}

				// CREATE A NEW USER
				else if ( requestParams.get("authType").toString().equals("createUser")  )
				{
					LogUtility.log("AuthenticationServlet > creating a new user");
					
					Vector errors = new Vector();
					if (createNewUser(requestParams, remoteAddress, errors) == true )
					{
						LogUtility.log("AuthenticationServlet > created a new user");
						Thread.sleep(100);

						//this response was throwing an exception b/c the user did not yet
						//have a cookie associated with the browser, so as a fix create a
						//small statement and allow the oportunity to login
						//response.sendRedirect("/vegbank/servlet/usermanagement?action=options");
						String cresponse = getUserCreationResponse(true, request, response, errors);
						out.println( cresponse );
					}
					else
					{
						String cresponse = getUserCreationResponse(false, request, response, errors);
						out.println( cresponse );
					}
				}

				else
				{
					LogUtility.log("AuthenticationServlet > incorect input paramaters to authenticate");
				}
			}
		}
		catch( Exception e )
		{
			LogUtility.log("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * method that handles the retreival of a lost password,
	 * emails it to the valid user and then returns the
	 * message to the browser
	 * @param requestParams -- the hashtable containg the request paramteres
	 */
	 private String handlePasswordRetreival(Hashtable requestParams)
	 {
		 StringBuffer msg = new StringBuffer();
		 try
		 {
			 	String email = (String)requestParams.get("userLogin");
				String passwd = uda.getPassword(email);
				LogUtility.log("AuthenticationServlet > emailing password to: " + email );

				String body;
				String from = "help@vegbank.org";
				String subject = "Recovered VegBank Password";

				if (passwd != null && !passwd.equals("")) { 
					StringBuffer sb = new StringBuffer();
					sb.append("Dear VegBank user,<br>\n");
					sb.append("Your password is: <b>"+passwd+"</b><BR><BR>");
					sb.append("VegBank Support Team<BR>");
					sb.append("<a href='mailto:help@vegbank.org'>help@vegbank.org</a><BR>");
					sb.append("<a href='http://vegbank.org'>http://vegbank.org</a><BR><BR>");
					body = sb.toString();

					su.sendHTMLEmail( mailHost, from, email, cc, subject, body);

					// sent email: create the response page
					msg.append("<passwordRequest> \n");
					msg.append("<message>Your password has been sent to: </message>\n");
					msg.append("<user> \n");
					msg.append(" <email>"+email+"</email>\n");
					msg.append("</user> \n");
					msg.append("</passwordRequest> \n");

				} else {

					// no account: create the response page
					msg.append("<passwordRequest> \n");
					msg.append("<message>There is no account for this email address (username).</message>\n");
					msg.append("</passwordRequest> \n");
				}
		 }
		catch( Exception e )
		{
			LogUtility.log("AuthenticationServlet: ", e);
		}
		return( msg.toString() );
	 }

	/**
	 * method that provides a response to a user who has attempeted to
	 * make a new user account.  The input may either be true / false and the
	 * response will be modified accordingly
	 * @param createResult -- true or false
	 */
	 private String getUserCreationResponse(boolean createResult, HttpServletRequest request, HttpServletResponse response, Vector errors) throws IOException
	 {
		 StringBuffer sb = new StringBuffer();
		 Hashtable replaceHash = new Hashtable();
		 if ( createResult == true )
		 {				
			String emailAddress = request.getParameter("emailAddress");
			String passWord =  request.getParameter("password");
			response.sendRedirect(
				"/vegbank/servlet/authenticate?authType=loginUser&Submit=Submit"
				+ "&userName=" + emailAddress + "&password=" + passWord
				);
		 }
		 else
		 {
			 sb.append("<b> USER CREATION ERROR! </b> <br> ");
			 sb.append("Please review the user-related attributes that you submitted<br>");
			 sb.append("and resubmit.  If you get this error again, please contact the <br>");

				sb.append("<a href=\"mailto:dba@vegbank.org\" > ");
				sb.append(" VegBank Administrator </a><br/><br/>");
			
				sb.append("<font color=\"red\">Correct the following errors:</font>");
				sb.append("<ul>");
				Iterator errorsIt = errors.iterator();
				while ( errorsIt.hasNext() )
				{
					String error = (String) errorsIt.next();
					sb.append("<li>" + error + "</li>");
				}
				sb.append("</ul>");


		 }
		 replaceHash.put("messages",  sb.toString());
		 //su.filterTokenFile(genericForm, genericTemplate, replaceHash);
		 StringWriter out = new StringWriter();
		 su.filterTokenFile( genericForm, out, replaceHash);

		 return( out.toString() );
		 /*
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html> \n");
		 sb.append("<head> \n");
		 sb.append(" <title>-- New User Account Created Successfully! -- </title> \n");
		 sb.append("</head> \n");
		 sb.append("<body> \n");
			sb.append(" <p>  Thank you for using VegBank, your user account has successfully created! </p>");
			sb.append(" <br> To Login Please Click <br> \n");
		 	sb.append(" <a href=\"/vegbank/general/login.html\">here</a> \n");
			sb.append("");
			sb.append("");
			sb.append("");
			sb.append("");
		 sb.append("</body> \n");
		 sb.append("</html> \n");
		 return(sb.toString() );
		 */
	 }


	/**
	 * method that contains the html and java script to redirect the user to
	 * an error page and login
	 */
	 private String getErrorRedirection()
	 {
		 String errorPage = "/vegbank/general/login.html";
		 LogUtility.log("AuthenticationServlet > compiling error rediection to:  " + errorPage );
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html> \n");
		 sb.append("<head> \n");
		 sb.append("<title>-- Authentication Failure ! -- </title> \n");
		 sb.append("</head> \n");
		 sb.append("<body> \n");
		 sb.append("<script language=\"JavaScript\"> \n");
		 //sb.append("window.location=\"/vegbank/general/login.html\"; \n");
		 sb.append("window.location=\""+errorPage+"\"; \n");
		 sb.append("</script> \n");

		 sb.append("Please Click \n");
		 //sb.append("<a href=\"/vegbank/general/login.html\">here</a> \n");
		 sb.append("<a href=\""+errorPage+"\">here</a> \n");
		 sb.append("if your browser is not promptly redirected \n");
		 sb.append("");
		 sb.append("</body> \n");
		 sb.append("</html> \n");
		 return(sb.toString() );
	 }


	/**
	 * method that returns the user name for the given user from the request
	 * parameters
	 */
	 private String getUserName(Hashtable requestParams)
	 {
		 if (requestParams.containsKey("userName") )
		 {
			String user = (String)requestParams.get("userName");
		 	return(user);
		 }
		 else
		 {
			 return("null");
		 }
	 }

	/**
	 * method that returns the parameters required and accepted by this servlet
	 *
	 */
	 private String getParameters()
	 {
		 StringBuffer sb = new StringBuffer();
		 	sb.append("<br> authType {loginUser, createUser, uploadfile} <br>");
		 	sb.append("userName <br>");
		 	sb.append("password <br>");
		 return(sb.toString());
	 }

	/**
	 * method to authenticate a known user
	 *
	 */
	 private boolean authenticateUser(Hashtable requestParams, String remoteAddress)
	 {
		 	//grab thee user name and password
			String userName=requestParams.get("userName").toString();
			String passWord=requestParams.get("password").toString();
			LogUtility.log("AuthenticationServlet > authenticating: "+userName);

			//access the class in the dbAccess class to validate the
			//login and password with that stored in the database
			return( uda.authenticateUser(userName, passWord) );
	 }


	 /**
	 * method to create a new user identification
	 * in the vegetation user database. There are a
	 * number of required paramteres that must exist in
	 * the input hashtable including: <br>
	 *	emailAddress, passWord, retypePassWord, termsAccept, <br>
	 *	givenName, surName <br>
	 * and also optional paramters: <br>
	 *
	 *
	 */
	 private boolean createNewUser(Hashtable requestParams, String remoteAddress, Vector errors)
	 {
		 try
		 {
			LogUtility.log("AuthenticationServlet > REQUEST PARAMS: "+requestParams.toString() );
		 	boolean b = validateNewUserAttributes(requestParams, errors);
			LogUtility.log("AuthenticationServlet > valid new user attributes: "+ b );
			if ( b == true )
			{
				//get the key variables that are required
		 		String emailAddress = requestParams.get("emailAddress").toString();
		 		String passWord =  requestParams.get("password").toString();
				String givenName= requestParams.get("givenname").toString();
				String surName =  requestParams.get("surname").toString();
				LogUtility.log("AuthenticationServlet > given name: "+givenName);
		 		LogUtility.log("AuthenticationServlet > sur name: "+surName);

				// GET THE OTHER NOT REQUIRED ATTS
				String inst = requestParams.get("affilInst").toString();
				String address = requestParams.get("address").toString();
				String city = requestParams.get("usaCity").toString();
				String state = requestParams.get("state").toString();
				String country = requestParams.get("country").toString();
				String zip = requestParams.get("zipcode").toString();
				String acode = requestParams.get("areaCode").toString();
				String number = requestParams.get("phoneNumber").toString();
				String phone = acode+" "+number;
				LogUtility.log("AuthenticationServlet > institution: "+inst);
				LogUtility.log("AuthenticationServlet > address: "+address);
				LogUtility.log("AuthenticationServlet > city: "+city);
				LogUtility.log("AuthenticationServlet > state: "+state);
				LogUtility.log("AuthenticationServlet > country: "+country);
				LogUtility.log("AuthenticationServlet > zip: "+zip);
				LogUtility.log("AuthenticationServlet > phone: "+phone);

				// USE THE DB CLASS TO CREATE THE USER
				//uda.createUser(emailAddress, passWord, givenName, surName, remoteAddress);
				boolean b2 =
					uda.createUser(
						emailAddress,
						passWord,
						givenName,
						surName,
						remoteAddress,
						inst,
						address,
						city,
						state,
						country,
						phone,
						zip,
						errors);
				return( b2 );
			}
			else
			{
				LogUtility.log("AuthenticationServlet > form invalid: " + errors);
				return(false);
			}
		 }
		 catch(Exception e)
		 {
			 LogUtility.log("Exception: " + e.getMessage() );
			 e.printStackTrace();
			 return(false);
		 }
	 }

	/**
	 * method that returns true if the user attemting to create a new account
	 * has passed the correct parmeters.
	 * 
	 * Also populates a vector with Strings with error messages suitable for the user.
	 **/
	 private boolean validateNewUserAttributes(Hashtable h, Vector errors)
	 {
	 	boolean valid = true;
	 	
	 	LogUtility.log("Validating Form");
	 	
	 	// Check each required field
	 	if ( ! h.containsKey("emailAddress") || h.get("emailAddress").equals("") )
	 	{
	 		valid = false;
	 		errors.add("Please enter email address");
	 	}
	 	
	 	if ( ! h.containsKey("password") ||  ! h.containsKey("password2") || h.get("password").equals(""))
		{
			valid = false;
			errors.add("Please enter password");
		}	 	
	 	
		if (! h.containsKey("surname")  || h.get("surname").equals("") )
		{
			valid = false;
			errors.add("Please enter first name");
		}	 		 	
	 	
		if (! h.containsKey("givenname") || h.get("givenname").equals("") )
		{
			valid = false;
			errors.add("Please enter last name");
		}
		
		// CHECK THAT THE PASSWORD STRINGS ARE EQUAL
		String pw = (String)h.get("password");
		String pw2 = (String)h.get("password2");
		if ( ! pw.equals(pw2) )
		{
			valid = false;
			errors.add("Passwords are not the same");
		}
		
		String email = (String)h.get("emailAddress");
		LogUtility.log(">>>> " + email  + " >>> " + email.indexOf("@"));
		if ( email.indexOf("@") <= 0 )
		{
			valid = false;
			errors.add("Enter a valid email address");
		}

		if ( ! h.containsKey("termsaccept") || ! h.get("termsaccept").equals("accept") )
		{
			valid = false;
			errors.add("You must accept our conditions to become a user");
		}		
	 	
	 	LogUtility.log("success: " + valid + " and errors: " + errors);
		return(valid);
	 }

/**
	* Method to register a cookie and set it in the browser if there is a match
 * between the issued user name and password with a valid name password pair
 * stored in some data store
 *
 * @param cookie - a cookie whose name, value and max age are set depending on
 * 	the results of validation
 * @param requestParams - 
 * @param remoteAddress -
 * @param userName - the user name issued
 *
 */
	private Cookie cookieDelegator (
		Cookie cookie, 
		Hashtable requestParams,
		String remoteAddress, 
		String userName)
	{

		// below is quite crude but will suffice for the time being.
		// Soon will rewrite the method to send more meaning values
		// and will use a database query to access
		// user information -- the resource bundle is set here b/c
		// this method may be call from another class
		//ResourceBundle rbun = ResourceBundle.getBundle("LocalStrings");
		//String clientLogFile = rbun.getString("requestparams.clientLog");

		//get the cookie value form the user database class
		LogUtility.log("AuthenticationServlet > USER NAME: " + userName );
		String cookieValue = uda.getUserCookieValue(userName);
		String cookieName = "framework";
		
		Cookie registeredCookie = null;
		
		if (cookieName != null)
		{
			String cookieAddress = remoteAddress; //same
			cookie = new Cookie(cookieName, cookieValue);
			cookie.setMaxAge(3600);  //set cookie for an hour
     	 	cookie.setPath("/");  // cookie applies to all contexts
			registeredCookie=cookie;
		  LogUtility.log("AuthenticationServlet > using cookie name : " + cookieName + " val: " + cookieValue );
		}
		else
		{
			LogUtility.log("AuthenticationServlet > ERROR null user name");
		}
		return registeredCookie;
	}

}


