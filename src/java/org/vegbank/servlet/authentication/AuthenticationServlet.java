package org.vegbank.servlet.authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.UserDatabaseAccess;


/**
 * Servlet to perform user authentication for all the
 * servlets within a domain.  This servlet will recognize varying
 * degrees of authentication for users and will place cookies representing
 * the authentication level
 * 
 * Logon and logoff functions are handled now by struts actions.
 *
 * <p>Valid parameters are:<br><br>
 * REQUIRED PARAMETERS
 * @param userName - user name of the user tying to be authenticated <br>
 * @param password - the password of the user attempting to be authenticated <br>
 * @param authType -- the authentication type {loginUser, uploadFile}
 *
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-11-13 22:38:16 $'
 *  '$Revision: 1.13 $'
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

			//get the request - parameters using the
			//servlet Utility class
			Hashtable requestParams = su.parameterHash(request);
			//LogUtility.log("AuthenticationServlet > " + requestParams.toString() );
			//grab the remote host information
			String remoteHost=request.getRemoteHost();
			String remoteAddress = request.getRemoteAddr();
			LogUtility.log("AuthenticationServlet > remoteHost: "+remoteHost+" Address: "+remoteAddress);


			//determine what the client wants to do

			//GET A LOST PASSWORD
			if ( requestParams.get("authType").toString().equalsIgnoreCase("GETPASSWORD")  )
			{
				String s =  handlePasswordRetreival(requestParams);
				out.println(s);
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
}


