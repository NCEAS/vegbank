package org.vegbank.servlet.authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vegbank.servlet.util.ServletUtility;



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
 *  '$Author: farrell $'
 *  '$Date: 2003-05-07 01:37:27 $'
 *  '$Revision: 1.4 $'
 *
 *  @version
 *  @author
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
			System.out.println("AuthenticationServlet > init");
			ResourceBundle rb = ResourceBundle.getBundle("authentication");
			this.mailHost = rb.getString("mailHost");
			this.cc = rb.getString("systemEmail");		
			this.genericForm =  rb.getString("genericForm");
			this.genericTemplate = 	 rb.getString("genericTemplate");
				
			System.out.println("AuthenticationServlet > init" );
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
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
		//System.out.println("the client info log file is: "+clientLogFile);

		//first block will look for the userName and password as well as valid cookie
		try
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			Cookie registeredCookie = null;

			//get the request - parameters using the
			//servlet Utility class
			Hashtable requestParams = su.parameterHash(request);
			System.out.println("AuthenticationServlet > " + requestParams.toString() );
			//grab the remote host information
			String remoteHost=request.getRemoteHost();
			String remoteAddress = request.getRemoteAddr();
			System.out.println("AuthenticationServlet > remoteHost: "+remoteHost+" Address: "+remoteAddress);


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
					System.out.println("AuthenticationServlet > user login attempt");
					if ( authenticateUser(requestParams, remoteAddress) == true)
					{
						//log the login in the clientLogger
						//set a cookie
						String user = getUserName(requestParams);
						Cookie cookie = new Cookie("null", "null");
						//AuthenticationServlet m =new AuthenticationServlet();
						registeredCookie = cookieDelegator(cookie, requestParams, remoteAddress, user);
						response.addCookie(registeredCookie);
						HttpSession session = request.getSession();
						session.setAttribute("emailAddress", request.getParameter("userName") );
						//send the user to the correct page
						Thread.sleep(1100);
						String redirect = "/vegbank/servlet/usermanagement?action=options";
						System.out.println("AuthentictionServlet > redirecting to: " + redirect);
						response.sendRedirect(redirect);
					}
					else
					{
						System.out.println("AuthenticationServlet > user login failed");
						out.println( getErrorRedirection() );
					}
				}
				//AUTHENTICATE THE USER TO UPLOAD A FILE OR SOMETHING SIMILAR
				else if ( requestParams.get("authType").toString().equals("uploadfile")  )
				{
					System.out.println("AuthenticationServlet > authenticating for file upload");
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
					System.out.println("AuthenticationServlet > creating a new user");
					if (createNewUser(requestParams, remoteAddress) == true )
					{
						System.out.println("AuthenticationServlet > created a new user");
						Thread.sleep(100);

						//this response was throwing an exception b/c the user did not yet
						//have a cookie associated with the browser, so as a fix create a
						//small statement and allow the oportunity to login
						//response.sendRedirect("/vegbank/servlet/usermanagement?action=options");
						String cresponse = getUserCreationResponse(true);
						out.println( cresponse );
					}
					else
					{
						String cresponse = getUserCreationResponse(false);
						out.println( cresponse );
					}
				}

				else
				{
					System.out.println("AuthenticationServlet > incorect input paramaters to authenticate");
				}
			}
		}
		catch( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
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
				System.out.println("AuthenticationServlet > emailing password to: " + email );

				boolean validUser = false;
				String givenName = "";
				String surName = "";
				String body = "";
				String from = "vegbank";
				String to = email;
				String subject = "Recovered VegBank Password";


				// if there is a name use it
				if ( (requestParams.containsKey("firstName") == true ) &&
				(requestParams.containsKey("lastName") == true  ))
				{
					givenName = (String)requestParams.get("firstName");
					surName = (String)requestParams.get("lastName");
					System.out.println("AuthenticationServlet > surName: " + surName
					+ " givenName: " + givenName );
					if ( surName.length() > 1 )
					{
						String wholename = givenName+ " "+surName;
						StringBuffer sb = new StringBuffer();
						sb.append("Dear "+wholename+", \n");
						sb.append("\t Your VegBank password is: "+passwd);
						body = sb.toString();
					}
				}
					// else a generic string
				else
				{
					body = "\n  Your Vegbank Password is: " + passwd;
				}

				//if a password is retreived then send it to the user
				if ( passwd != null && passwd.length() > 1 )
				{
					su.sendEmail( mailHost, from, to, cc, subject, body);
					validUser = true;
				}

				//create the message
				msg.append("<passwordRequest> \n");
				msg.append("<message> password sent to: </message>\n");
				msg.append("<user> \n");
				msg.append(" <email>"+email+"</email>\n");
				msg.append(" <surName>"+surName+"</surName>\n");
				msg.append(" <givenName>"+givenName+"</givenName>\n");
				msg.append(" <validUser>"+validUser+"</validUser>\n");
				msg.append("</user> \n");
				msg.append("</passwordRequest> \n");
		 }
		catch( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return( msg.toString() );
	 }

	/**
	 * method that provides a response to a user who has attempeted to
	 * make a new user account.  The input may either be true / false and the
	 * response will be modified accordingly
	 * @param createResult -- true or false
	 */
	 private String getUserCreationResponse(boolean createResult)
	 {
		 StringBuffer sb = new StringBuffer();
		 Hashtable replaceHash = new Hashtable();
		 if ( createResult == true )
		 {
			 sb.append("Thank you. <br> ");
			 sb.append("Your VegBank account has been created. <br>");
             /* The following 3 lines added by MTL Tuesday, September 10, 2002 */
			 sb.append("<br>To apply for certification, click ");
			 sb.append("<a href=\"/forms/certification.html\">here.</a><br>");
		   sb.append("<br>To begin using VegBank, click \"Login\" above. <br>");
		 }
		 else
		 {
			 sb.append("<b> USER CREATION ERROR! </b> <br> ");
			 sb.append("Please review the user-related attributes that you submitted<br>");
			 sb.append("and resubmit.  If you get this error again, please contact the <br>");

			 sb.append("<a href=\"mailto:dba@vegbank.org\" > ");
			 sb.append(" VegBank Administrator </a>");
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
		 System.out.println("AuthenticationServlet > compiling error rediection to:  " + errorPage );
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
			System.out.println("AuthenticationServlet > name password/pair: "+userName+" "+passWord);

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
	 private boolean createNewUser(Hashtable requestParams, String remoteAddress)
	 {
		 try
		 {
			System.out.println("AuthenticationServlet > REQUEST PARAMS: "+requestParams.toString() );
		 	boolean b = validateNewUserAttributes(requestParams);
			System.out.println("AuthenticationServlet > valid new user attributes: "+ b );
			if ( b == true )
			{
				//get the key variables that are required
		 		String emailAddress = requestParams.get("emailAddress").toString();
		 		String passWord =  requestParams.get("password").toString();
				String givenName= requestParams.get("givenname").toString();
				String surName =  requestParams.get("surname").toString();
				System.out.println("AuthenticationServlet > given name: "+givenName);
		 		System.out.println("AuthenticationServlet > sur name: "+surName);

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
				System.out.println("AuthenticationServlet > institution: "+inst);
				System.out.println("AuthenticationServlet > address: "+address);
				System.out.println("AuthenticationServlet > city: "+city);
				System.out.println("AuthenticationServlet > state: "+state);
				System.out.println("AuthenticationServlet > country: "+country);
				System.out.println("AuthenticationServlet > zip: "+zip);
				System.out.println("AuthenticationServlet > phone: "+phone);

				// USE THE DB CLASS TO CREATE THE USER
				//uda.createUser(emailAddress, passWord, givenName, surName, remoteAddress);
				boolean b2 = uda.createUser(emailAddress, passWord, givenName,surName, remoteAddress,
				inst, address, city, state, country, phone, zip);
				return( b2 );
			}
			else
			{
				System.out.println("AuthenticationServlet > no @ in email address");
				return(false);
			}
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
			 return(false);
		 }
	 }

	/*
	 * method that returns true if the user attemting to create a new account
	 * has passed the correct parmmeters
	 */
	 private boolean validateNewUserAttributes(Hashtable h)
	 {
	 	boolean valid = true;
		// CHECK THAT ALL THE PARAMTERS WERE SENT
		if ( h.containsKey("emailAddress")  && h.containsKey("password") && h.containsKey("password2")
		&& h.containsKey("surname") && h.containsKey("givenname"))
		{
			System.out.println("AuthenticationServlet > all required attributes passed");
			// CHECK THAT THE PASSWORD STRINGS ARE EQUAL
			String pw = (String)h.get("password");
			String pw2 = (String)h.get("password2");
			if ( pw.equals(pw2) )
			{
				System.out.println("AuthenticationServlet > password strings match" );
				// CHECK THAT THERE IS A '@' IN THE EMAIL ADDRESS
				String email = (String)h.get("emailAddress");
				if ( email.indexOf("@") > 0 )
				{
					System.out.println("AuthenticationServlet > email contains '@'" );
					// CHECK THAT THE SURNAME AND GIVEN NAME HAVE NOT NULL VALUES
					String sname = (String)h.get("surname");
					String gname = (String)h.get("givenname");
					if ( sname.length() > 2 && gname.length() > 2 )
					{
						System.out.println("AuthenticationServlet > surname and given names valid" );
					}
					else
					{
						valid = false;
					}
				}
				else
				{
					valid = false;
				}
			}
			else
			{
				valid = false;
			}
		}
		else
		{
			valid = false;
		}

		return(valid);
	 }

/**
	* Method to register a cookie and set it in the browser if there is a match
 * between the issued user name and password with a valid name password pair
 * stored in some data store
 *
 * @param cookie - a cookie whose name, value and max age are set depending on
 * 	the results of validation
 * @param userName - the user name issued
 * @param passWord - the issued password
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
		System.out.println("AuthenticationServlet > USER NAME: " + userName );
		String cookieValue = uda.getUserCookieValue(userName);
		String cookieName = "framework";
		
		Cookie registeredCookie = null;
		
		if (cookieName != null)
		{
			String cookieAddress = remoteAddress; //same
			cookie = new Cookie(cookieName, cookieValue);
			cookie.setMaxAge(3600);  //set cookie for an hour
			registeredCookie=cookie;
		  System.out.println("AuthenticationServlet > using cookie name : " + cookieName + " val: " + cookieValue );
		}
		else
		{
			System.out.println("AuthenticationServlet > ERROR null user name");
		}
		return registeredCookie;
	}

}


