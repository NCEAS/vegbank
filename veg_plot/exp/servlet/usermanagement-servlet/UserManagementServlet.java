package servlet.usermanagement;

/**
 *  '$RCSfile: UserManagementServlet.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-06-24 15:32:35 $'
 * '$Revision: 1.11 $'
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
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;


import servlet.util.ServletUtility;
import servlet.util.GetURL;
import org.apache.tools.ant.taskdefs.Copy;
import servlet.authentication.UserDatabaseAccess;


public class UserManagementServlet extends HttpServlet 
{
	
	private UserDatabaseAccess userdb;
	private ServletUtility util = new ServletUtility();
  private GetURL gurl = new GetURL();
	
	//the cookie value is the same as the user name
	//which is the same as the user's email addy
	private String cookieValue;
	
	//this is the html paget that will get edited by this servlet so that is
	//appers to be a custom page for the correponding client
	private String actionFile = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/general/actions.html";
	//this is the modified page that is to be sent back to the client
	private String outFile ="/tmp/actions_dev.html";

	// these are the forms for the certification input:
	private String certificationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/certification_valid.html";
	private String certificationValidation = "/usr/local/devtools/jakarta-tomcat/webapps/forms/valid.html";
	private String genericForm = "/usr/local/devtools/jakarta-tomcat/webapps/forms/generic_form.html";
	private String userUpdateValidation = "/usr/local/devtools/jakarta-tomcat/webapps/forms/valid.html";
	private String userUpdateTemplate =  "/usr/local/devtools/jakarta-tomcat/webapps/forms/user_update_form.html";
	//constructor
	public UserManagementServlet()
	{
		System.out.println("init: UserManagementServlet");
		//print some info about the instance variables
		System.out.println("UserManagementServlet > action file: " + actionFile );
		System.out.println("UserManagementServlet > modified action file: " + outFile );
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
///			res.setContentType("text/html");
///			PrintWriter out = res.getWriter();
			try 
			{
				//get the parameters
				Hashtable params = util.parameterHash(req);
				System.out.println("UserManagementServlet > in params: " + params  );
				
				//the cookie value is the same as the user name and email addy
				cookieValue = getCookieValue(req);
				String action = getAction( params );
				if (action == null)
				{
					System.out.println("UserManagementServlet > action parameter required: ");
				}
				else
				{
					//SHOW THE USER'S CUSTOMIZED OPTIONS PAGE
					if ( action.equals("options") )
					{
						res.setContentType("text/html");
						PrintWriter out = res.getWriter();
						//copy the actions file and then displat to the browser
						//String actionFile = "/usr/local/devtools/jakarta-tomcat_dev/webapps/vegbank/general/actions.html";
						//String outFile ="/tmp/actions_dev.html";
						String token = "user";
						String value = cookieValue;
						util.filterTokenFile(actionFile, outFile, token, value);
						//print the outfile to the browser
						String s = util.fileToString(outFile);
						out.println(s);
					}
					//SHOW USER FILES 
					else if ( action.equals("showfiles") )
					{
						res.setContentType("text/html");
						PrintWriter out = res.getWriter();
						this.showUserFiles(req, res, out);
					}
					//SHOW USER FILES 
					else if ( action.equals("logout") )
					{
						res.setContentType("text/html");
						PrintWriter out = res.getWriter();
						this.handleLogout(req, res, out);
					}
					// DOWN LOAD THE CLIENT
					else if ( action.equals("download") )
					{
						//res.setContentType("text/html");
						//PrintWriter out = res.getWriter();
						this.handleDownload(req, res);
					}
					// APPLY FOR A CERTIFICATION LEVEL
					else if ( action.equals("certification") )
					{
						this.handleCertification(req, res);
					}
					// CHANGE USER SETTINGS 
					else if ( action.equals("changeusersettings") )
					{
						this.handleSettingsModification(req, res);
					}
					else
					{
						System.out.println("UserManagementServlet > unrecognized action: " + action);
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
	 * method that handles the modification settings there are two 
	 * sub-proccesses enveloped in this method:
	 * 1] initiate the process - to display to the user the current 
	 * 	attributes stored in the database,
	 * 2] take the input parameters and make the changes in the database
	 * 
	 * @param req -- the http request object
	 * @param res -- the http response object
	 */
	 private void handleSettingsModification(HttpServletRequest req, HttpServletResponse res )
	 {
		 try
		 {
			 System.out.println("UserManagementServlet > performing user settings modification");
			 String emailAddress = this.cookieValue;
			 if ( emailAddress != null && emailAddress.length() > 2)
			 {
				 // FIGURE OUT WHETHER TO SEND THE UPDATE FORM OR GET THE PARAMETRS
				 Hashtable params = util.parameterHash(req);
				 if ( params.containsKey("surName") )
				 {
					 System.out.println("UserManagementServlet > reading the update parameters");
					 
					 //get the input paramters
					 String surName = (String)params.get("surName");
					 String givenName = (String)params.get("givenName");
					 String password = (String)params.get("password");
					 String permissionType = (String)params.get("permissionType");
					 String institution = (String)params.get("institution");
					 String ticketCount = (String)params.get("ticketCount");
					 String address = (String)params.get("address");
					 String city = (String)params.get("city");
					 String state = (String)params.get("state");
					 String country = (String)params.get("country");
					 String zipCode = (String)params.get("zipCode");
					 String phoneNumber = (String)params.get("phoneNumber");
					 
					 //put into a hashtable
					 Hashtable h = new Hashtable();
					 h.put("emailAddress", emailAddress);
					 h.put("password", ""+password);
					 h.put("surName", ""+surName);
					 h.put("givenName", ""+givenName);
					 h.put("permissionType", ""+permissionType);
					 h.put("institution", ""+institution);
					 h.put("ticketCount", ""+ticketCount);
					 h.put("address", ""+address);
					 h.put("city", ""+city);
					 h.put("state", ""+state);
					 h.put("country", ""+country);
					 h.put("zipCode", ""+zipCode);
					 h.put("phoneNumber", ""+phoneNumber);
					 
					 // instantiate the database
					 userdb = new UserDatabaseAccess();
					 userdb.updateUserInfo(h);
					 
					 // send a success message
					 Hashtable replaceHash = new Hashtable();
					 StringBuffer sb = new StringBuffer();
					 sb.append("Thank you "+givenName+" "+surName+"! <br> ");
					 sb.append("Your VegBank profile has been updated. <br>");
					 replaceHash.put("messages",  sb.toString());
					 util.filterTokenFile(genericForm, certificationValidation, replaceHash);
						//print the outfile to the browser
						PrintWriter out = res.getWriter();
						String s = util.fileToString(certificationValidation);
						out.println(s);
					 //res.sendRedirect("/forms/valid.html");
				 }
				 else
				 {
					 System.out.println("UserManagementServlet > sending the current parametrs");
					 userdb = new UserDatabaseAccess();
					 Hashtable h = userdb.getUserInfo(emailAddress);
					 
					 String surName = (String)h.get("surName");
					 String givenName = (String)h.get("givenName");
					 String password = (String)h.get("password");
					 String address = (String)h.get("address");
					 String city = (String)h.get("city");
					 String state = (String)h.get("state");
					 String country = (String)h.get("country");
					 String zipCode = (String)h.get("zipCode");
					 String dayPhone = (String)h.get("dayPhone");
					 String ticketCount =  (String)h.get("ticketCount");
					 String permissionType = (String)h.get("permissionType");
					 String institution = (String)h.get("institution");
					 
					 Hashtable replaceHash = new Hashtable();
					 replaceHash.put("messages", " ");
					 replaceHash.put("surName", ""+surName);
					 replaceHash.put("givenName", ""+givenName);
					 replaceHash.put("password", ""+password);
					 replaceHash.put("address", ""+address );
					 replaceHash.put("city", ""+city );
					 replaceHash.put("state", ""+state );
					 replaceHash.put("country", ""+country);
					 replaceHash.put("zipCode", ""+zipCode );
					 replaceHash.put("phoneNumber", ""+dayPhone );
					 replaceHash.put("institution", ""+institution );
					 replaceHash.put("ticketCount", ""+ticketCount );
					 replaceHash.put("permissionType", ""+permissionType );
					 replaceHash.put("emailAddress", emailAddress );
					 
					 util.filterTokenFile(userUpdateTemplate, userUpdateValidation, replaceHash);
						//print the outfile to the browser
						PrintWriter out = res.getWriter();
						String s = util.fileToString(userUpdateValidation);
						out.println(s);
					 //res.sendRedirect("/forms/valid.html");
					 // delete the form 
					 Thread.sleep(1000);
					 util.flushFile(userUpdateValidation);
				 }
			 }
			 else
			 {
				 System.out.println("UserManagementServlet > missing require attributes");
				 // assume that they are not logged in and send them there
				res.sendRedirect("/forms/redirection_template.html");

			 }
			 
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 
	 }
		
	/**
	 * method that hendles the insertion of certification-request data in to the 
	 * user database ( the USER_CERTIFICATION table ) and notification of the appropriate
	 * people that the request has been submitted. <br> <br>
	 * 
	 * The parameters that can / should be passed to this method are:
	 * param emailAddress
	 * param surName 
	 * param givenName
	 * param phoneNumber
	 * param phoneType
	 * param currentCertLevel
	 * param cvDoc
	 * param highestDegree
	 * param degreeYear
	 * param degreeInst
	 * param currentInst
	 * param currentPos
	 * param esaPos
	 * param profExperienceDoc
	 * param relevantPubs
	 * param vegSamplingDoc
	 * param vegAnalysisDoc
	 * param usnvcExpDoc
	 * param vegbankExpDoc
	 * param plotdbDoc
	 * param nvcExpRegionA
	 * param nvcExpExpVegA
	 * param nvcExpFloristicsA
	 * param nvcExpNVCA
	 * param esaSponsorNameA
	 * param esaSponsorEmailA
	 * param esaSponsorNameB
	 * param esaSponsorEmailB
	 * param peerReview
	 * param additionalStatements
	 * 
	 */
	 private void handleCertification(HttpServletRequest req, HttpServletResponse res)
	 {
		 try
		 {
			 System.out.println("UserManagementServlet > performing certification action");
			 
			 //get the attributes from the form
			 Hashtable params = util.parameterHash(req);
			 String emailAddress = this.cookieValue;
			 String submittedEmail = (String)params.get("email");
			 String surName = (String)params.get("surName");
			 String givenName  = (String)params.get("givenName");
			 String phoneNumber = (String)params.get("phoneNumber");
			 String phoneType = (String)params.get("phoneType");
			 String currentCertLevel = (String)params.get("currentCertLevel");
			 String cvDoc = (String)params.get("cvDoc");
			 String highestDegree = (String)params.get("highestDegree");
			 String degreeYear = (String)params.get("degreeYear");
			 String currentInst = (String)params.get("currentInst");
			 String degreeInst = (String)params.get("degreeInst");
			 String currentPos = (String)params.get("currentPos");
			 String esaPos = (String)params.get("esaPos");
			 String profExperienceDoc = (String)params.get("profExperienceDoc");
			 String relevantPubs = (String)params.get("relevantPubs");
			 String vegSamplingDoc = (String)params.get("vegSamplingDoc");
			 String vegAnalysisDoc = (String)params.get("vegAnalysisDoc");
			 String usnvcExpDoc = (String)params.get("usnvcExpDoc");
			 String vegbankExpDoc = (String)params.get("vegbankExpDoc");
			 String plotdbDoc = (String)params.get("plotdbDoc");
			 String nvcExpRegionA = (String)params.get("nvcExpRegionA");
			 String nvcExpVegA = (String)params.get("nvcExpVegA");
			 String nvcExpFloristicsA = (String)params.get("nvcExpFloristicsA");
			 String nvcExpNVCA = (String)params.get("nvcExpNVCA");
			 String esaSponsorNameA  = (String)params.get("esaSponsorNameA");
			 String esaSponsorEmailA = (String)params.get("esaSponsorEmailA");
			 String esaSponsorNameB = (String)params.get("esaSponsorNameB");
			 String esaSponsorEmailB = (String)params.get("esaSponsorEmailB");
			 String peerReview = (String)params.get("peerReview");
			 String additionalStatements = (String)params.get("additionalStatements");
			 // deterimine if the correct attributes were passed and if so then 
			 // commit to the database etc.
			 if ( emailAddress != null && emailAddress.length() > 2)
			 {
			 	// check that the required paramteres are upto snuff
				if ( surName.length() > 2 && givenName.length() > 2 && phoneNumber.length() > 2  && currentCertLevel.length() > 0 
					&& degreeInst.length() > 2  && currentInst.length() > 2  && esaPos.length() > 2  && vegSamplingDoc.length() > 2 
					&&  vegAnalysisDoc.length() > 2  && submittedEmail.length() > 2  )
					{
						System.out.println("surName: '"+surName+"'");
			 			userdb = new UserDatabaseAccess();
						userdb.insertUserCertificationInfo(emailAddress, surName, givenName,
						phoneNumber, phoneType, currentCertLevel, cvDoc, highestDegree,
			 			degreeYear, degreeInst, currentInst, currentPos, esaPos,
			 			profExperienceDoc, relevantPubs, vegSamplingDoc,  vegAnalysisDoc, usnvcExpDoc, 
			 			vegbankExpDoc, plotdbDoc, nvcExpRegionA, nvcExpVegA, nvcExpFloristicsA, 
			 			nvcExpNVCA, esaSponsorNameA, esaSponsorEmailA, esaSponsorNameB, esaSponsorEmailB, 
			 			peerReview, additionalStatements);
						
						// send a success message
						Hashtable replaceHash = new Hashtable();
						StringBuffer sb = new StringBuffer();
						sb.append("Thank you "+emailAddress+"! <br> ");
						sb.append("Your certification request has been passed onto the VegBank administration. <br>");
						sb.append("We will contact you shortly.");
						
						replaceHash.put("messages",  sb.toString());
						util.filterTokenFile(genericForm, certificationValidation, replaceHash);
						//res.sendRedirect("/forms/valid.html");	
						//print the outfile to the browser
						PrintWriter out = res.getWriter();
						String s = util.fileToString(certificationValidation);
						out.println(s);
						
						// send email message 
						String mailHost = "nceas.ucsb.edu";
						String from = "vegbank";
						String to = emailAddress;
						String cc = "vegbank@nceas.ucsb.edu";
						String subject = "VEGBANK CERTIFICATION RECEIPT";
						String body = emailAddress + ": Please consider this as your receipt for VegBank certification";
						util.sendEmail(mailHost, from, to, cc, subject, body);
						
						// delete the form
						Thread.sleep(1000);
						util.flushFile(certificationValidation);
						
					}
					else
					{
						System.out.println("UserManagementServlet > missing required attributes ");
						// send them a note to go back
						
						Hashtable replaceHash = new Hashtable();
						replaceHash.put("messages", "You are missing a required attribute to submit this form");
						replaceHash.put("surName", ""+surName);
						replaceHash.put("givenName", ""+givenName);
						replaceHash.put("submittedEmail", submittedEmail);
						replaceHash.put("registeredEmail", emailAddress);
						replaceHash.put("phoneNumber", phoneNumber);
						replaceHash.put("currentCertLevel", currentCertLevel);
						replaceHash.put("degreeInst", degreeInst);
						replaceHash.put("currentInst", currentInst);
						replaceHash.put("esaPos", esaPos);
						replaceHash.put("vegSamplingDoc", vegSamplingDoc);
						replaceHash.put("vegAnalysisDoc", vegAnalysisDoc);
						
						
						util.filterTokenFile(certificationTemplate, certificationValidation, replaceHash);
						//print the outfile to the browser
						PrintWriter out = res.getWriter();
						String s = util.fileToString(certificationValidation);
						out.println(s);
						//res.sendRedirect("/forms/valid.html");
						// delete the form 
						Thread.sleep(1000);
						util.flushFile(certificationValidation);
						
					}
			 }
			 else
			 {
			 	System.out.println("UserManagementServlet > not a valid email to commit certification");
				// redirect to the page that redirects to the login
				res.sendRedirect("/forms/redirection_template.html");
			 }
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
		
	/**
	 * method that handles the download action.  The idea behind the addition
	 * of this method is to provide download functionality for the vegbank 
	 * client. The attributes that are proccessed by this method will be input
	 * to the 'framework' database 'user management tables' specifically the 
	 * 'downloads' table <br> <br>
	 * 
	 * The parameters that can / should be entered to this method: <br>
	 *  downloadtype -- can be vegclient etc... <br>
	 *	additionaldata -- is the additional data sets (comma separated that the user wants) <br>
	 *	useremail -- the email address of the user <br>
	 * 
	 * @param req -- the http request object
	 * @param res -- the http response object
	 * 
	 */
		private void handleDownload(HttpServletRequest req, HttpServletResponse res)
		{
			try
			{
				System.out.println("UserManagementServlet > performing download action");
				String cookieVal = this.getCookieValue(req);
				String cookieName = this.getCookieName(req);
				System.out.println("UserManagementServlet > cookie value: " + cookieVal);
				System.out.println("UserManagementServlet > cookie name: " + cookieName);
				
				//if the user is not currently logged in then let tem know and send them 
				if ( cookieVal == null || cookieName == null || cookieVal.length() < 2 )
				{
					//the cookie is not valid
					System.out.println("UserManagementServlet > not logged in");
					// redirect to the page that redirects to the login
					res.sendRedirect("/forms/redirection_template.html");
				}
				// else figure out what the client wants, update the database and let them 
				// have it
				else
				{
					Hashtable params = util.parameterHash(req);
					String downloadType = (String)params.get("downloadtype");
					String additionaldata = (String)params.get("additionaldata");
					System.out.println("UserManagementServlet > downloads: " + downloadType+" "+additionaldata);
					//make sure that the download request(s) are valid
					if ( downloadType != null )
					{
						System.out.println("UserManagementServlet > proccessing download: ");
						// update the database
						userdb = new UserDatabaseAccess();
						userdb.insertDownloadInfo(cookieVal, downloadType); // the users email addy
						// create the archive
						Vector fileVec = new Vector();
						// add the client software
						if ( downloadType.equals("vegclient") )
						{
							fileVec.addElement("/tmp/base.mdb");
							// add the additional data
							if ( additionaldata != null ) 
							{
								fileVec.addElement("/tmp/additional.mdb");
							}
						}
						String outFile = "/tmp/test.zip";
						// make the zip file
						System.out.println("UserManagementServlet > attempting a zip creation");
						util.setZippedFile(fileVec, outFile);
						
						// redirect the browser there
						res.setContentType("application/zip");
						res.setHeader("Content-Disposition","attachment; filename=download.zip;");
						//ServletOutputStream out = res.getOutputStream();
						ServletOutputStream out = res.getOutputStream();
						BufferedInputStream in = new BufferedInputStream( new FileInputStream( outFile ));
            byte[] buf = new byte[4 * 1024]; //4K buffer
            int i = in.read(buf);

            while(i != -1 )
            {
              out.write(buf, 0, i);
              i = in.read(buf);
						}
					}
					else
					{
						// let the user know that what they requested could not be found
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		}
		
	
	/**
	 * method that handles the user logout by making the cookie age equal to zero
	 * 
	 * @param req -- the http request object
	 * @param res -- the http response object
	 * @param out -- the PrintWriter object back to the client
	 * 
	 */
	 private void handleLogout(HttpServletRequest req, HttpServletResponse res,
	 	PrintWriter out)
	 {
		 try
		 {
		 	System.out.println("UserManagementServlet > performing logout");
			String cookieVal = this.getCookieValue(req);
			String cookieName = this.getCookieName(req);
			System.out.println("UserManagementServlet > cookie value: " + cookieVal);
			System.out.println("UserManagementServlet > cookie name: " + cookieName);
			
			if ( cookieVal == null || cookieName == null )
			{
				//the cookie is not valid
				System.out.println("UserManagementServlet > not setting cookie");
			}
			else
			{
				Cookie cookie = new Cookie(cookieName, cookieVal);
				cookie.setMaxAge(0);  //set cookie to end
				res.addCookie(cookie);
			}
			
			res.sendRedirect("http://vegbank.nceas.ucsb.edu/vegbank/general/login.html");
			
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 
	 }
	
		
	/**
	 * method that returns the desired action requested by the user
	 *
	 * @param params - hashtable with all the servlet parameters
	 *
	 */
	 private String getAction(Hashtable params)
	 {
		 String s = null;
		 try
		 {
			 if (params.containsKey("action") )
			 {
		 		s=(String)params.get("action");
			 }
			 else
			 {
				 System.out.println("UserManagementServlet > no action found"); 
			 }
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		  return(s);
	 }

	
	
	/**
	 * method to show the user the files that they
	 * have stored in the profile on the server file
	 * system
	 */
	 private void showUserFiles(HttpServletRequest req, HttpServletResponse res,
	 	PrintWriter out)
	 {
		 try
		 {
			 	
				out.println("<html> \n");
				out.println("<head> \n");
				out.println("<body class=\"BODY\" >");
				out.println("<title> Database User Manager: "+ cookieValue+" </title> \n");
				out.println("<link rel=\"STYLESHEET\" href=\"http://numericsolutions.com/includes/default.css\" type=\"text/css\">");
				
				//get the java-script functions into the html here -- later remove this
				//and add a link to an external js file
				out.println( getJavaScriptFunctions() );
				out.println("</head> \n");
				
				//out.println("<br class=\"category\"> Welcome Vegbank User: " + cookieValue +"<br> \n");
				out.println( getNavigationHeader() );
				
				//this is the table that has all the registered queries
				out.println( getUserRegisteredQuerySummary(cookieValue) );
				
				//some space
				out.println("<br> <br>");
				
				//out.println("<br> you have uploaded "+getUserRegisteredFileNum(cookieValue)+"files registered on the server <br>");
				out.println( getUserRegisteredFileSummary(cookieValue) );
				out.println("</body>");
				out.println("</html>");
			 
		 }
		 catch (Exception e)
			{
				System.out.println("Exception: "+ e.getMessage() );
				e.printStackTrace();
			}
	 }
	
	
	/** 
	 * method that returns the navigation header -- which is the template that
	 * is consistent with the content that we have been developing for vegbank
	 */
	private String getNavigationHeader()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<table width=\"87%\" class=\"navigation\"> \n");
		sb.append("<tr>");
		sb.append("<td width=\"10%\"> <a href=\"http://vegbank.nceas.ucsb.edu/forms/uploadFile.html\"> UPLOAD FILE </a></td> \n");
		sb.append("<td  width=\"10%\"> <a href=\"http://vegbank.nceas.ucsb.edu/forms/utilities.html\"> UTILITIES </a></td> \n");
		sb.append("<td  width=\"10%\"> <a href=\"http://vegbank.nceas.ucsb.edu/forms/create_user.html\"> CREATE USER ACCOUNT </a></td> \n");
		sb.append("<td  width=\"10%\"> <a href=\"http://vegbank.org/\"> VEGBANK DOCUMENTATION </a></td> \n");
		//next image is an exclamation
		sb.append("<td  width=\"14%\"> <img src=\"/vegbank/images/icon_cat31.gif\"> <a href=\"http://vegbank.nceas.ucsb.edu/vegbank/sampledata/\"> SAMPLE DATA</a></td> \n");
		sb.append("</tr> \n");
		sb.append("</table>");
		
		//this is the form that uses the java script for negotiation of the query
		//forms
		sb.append("<table  width=\"50%\" class=\"navigation\"> \n");
		sb.append("<tr> \n");
		sb.append("<td> Choose a Query Option </td> \n");
		sb.append("<td>");
		sb.append("<form name=\"form\"> \n");
		sb.append("<select name=\"menu\" onChange=\"MM_jumpMenu('parent',this,0)\"> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/plot-query.html\" selected>Simple Plot Query</option> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/nested-plot-query.html\">Nested Plot Query</option> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/community-query.html\">Vegtation Community Query</option> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/plant-query.html\">Plant Taxonomy Query</option> \n");
		sb.append("</select> \n");
		sb.append("</form> \n");
		sb.append("</td> \n");
		sb.append("</tr> \n");
		sb.append("</table>");
		sb.append("<br> <br>");
		return(sb.toString());
	}
	
	/**
	 * method to return the number of files the user has registered on the 
	 * serever
	 */
	 private String getUserRegisteredFileNum(String userName )
	 {
		  String htmlResults = null;
    try
    {
      //create the parameter string to be passed to the DataRequestServlet -- 
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?action=userfilenum&username="+userName);
			
      //connect to the dataExchaneServlet
			String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange"+sb.toString().trim();
			System.out.println("UserManagementServlet > OUT PARAMETERS: "+uri);
      int port=80;
      String requestType="POST";
      htmlResults = gurl.requestURL(uri);
    }
    catch( Exception e )
    {
      System.out.println("Exception :  "
      +e.getMessage());
			e.printStackTrace();
    }
    return(htmlResults);
	 }
	
	
	
	/**
	 * method to return a summary of the query files the user has registered 
	 * on the server
	 */
	 private String getUserRegisteredQuerySummary(String userName )
	 {
		  String htmlResults = null;
    try
    {
      //create the parameter string to be passed to the DataRequestServlet -- 
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?action=userfilesummary&username="+userName);
			
      //connect to the dataExchaneServlet
			String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange"+sb.toString().trim();
			System.out.println("UserManagementServlet > OUT PARAMETERS: "+uri);
      int port=80;
      String requestType="POST";
      String s = gurl.requestURL(uri);
			
			//get all the files back for the main table -- pass the method the 
			// all flag
			htmlResults  = getQueryFileDataTable( s, "query", "Cached Queries");
    }
    catch( Exception e )
    {
      System.out.println("Exception:  "
      +e.getMessage());
    }
    return(htmlResults);
	 }
	 
	/**
	 * method that takes tabular data and retuns the data in an html table 
	 * with the appropriate markup -- this function is specific to tabularizing
	 * the files which the user uploaded to the file system
	 * @param data the data String that is returned from the database class and
	 *	kinda a funky looking fiting structure which is pipe deleimeted and each
	 * 	line is terminated by (**)
	 *
	 * @param userFileStartString the start string of the  file name assigned by
	 * the client user -- this can be used to return a table having like file
	 * names where for instance the term query can be applied  
	 */
	private String getQueryFileDataTable(String data, 
		String userFileStartString, String tableName)
	{
		//System.out.println("UserManagementServlet > tabularizing data: " + data);
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellpadding=0 cellspacing=0 width=75% class=\"filetable\"> \n");
		//put the name of the table here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" > <td> "+tableName+"</td> <td></td> <td></td> <td></td> </tr>");
		//put the table header here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" >");
		sb.append("<td>Run</td>");
		sb.append("<td>Delete</td>");
		sb.append("<td>View</td>");
		sb.append("<td>Date</td>");
		sb.append("<tr>");
		//notice the end-of-line token is two *
		StringTokenizer tok = new StringTokenizer(data, "**"); 
		while ( tok.hasMoreTokens()  ) 
		{
			String buf = tok.nextToken();
			sb.append("<tr  class=\"itemsmall\">  \n");
			
			StringTokenizer tok2 = new StringTokenizer(buf, "|"); 
			int colCnt = 0;
			String userFileName = null;
			String createDate = null;
			String accessionNumber = null;
			String fileType = null;
			String buf2 = null;
			
			//parse the columns into the cells
			while (tok2.hasMoreTokens() )
			{
				colCnt++;
				if ( colCnt == 1 )
					userFileName = tok2.nextToken();
				else if ( colCnt == 2 )
					accessionNumber = tok2.nextToken();
				else if ( colCnt == 4 )
					createDate = tok2.nextToken();
				else if ( colCnt == 3 )
					fileType = tok2.nextToken();
				else
					buf2 = tok2.nextToken();
			}
				if (accessionNumber != null && userFileName.startsWith("query") )
				{
					//first add the option to delete the file then add the file name, date and
					//type
					sb.append("	<td width=\"8%\"><input type=\"image\"  src=\"/vegbank/images/runIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					//sb.append("	<td width=\"8%\"><input type=\"image\"  src=\"/vegbank/images/deleteIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					//sb.append("	<td width=\"10%\"><input type=\"checkbox\" name=\""+accessionNumber+"\" ></td> \n");
					
					//here is the function to delet a file
					sb.append("	<td width=\"8%\"> <a href=\"http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange?action=deletefile&filenumber="+accessionNumber+"&username="+this.cookieValue+"\"> " 
					+" <img src=\"/vegbank/images/deleteIcon.gif\">  </a> </td> \n");
					
					sb.append("	<td width=\"25%\"> <a href=\"http://vegbank.nceas.ucsb.edu/uploads/"+accessionNumber+"\">"  + userFileName + "</a> </td> \n");
					sb.append("	<td>" +createDate+ "</td> \n");
					//sb.append("	<td>" +fileType+ "</td> \n");
					sb.append("</tr> \n \n");
				}
			}
		
		sb.append("</table> \n");
		return(sb.toString() );
	}
	
	
	
	
	
	
	/**
	 * method to return a summary  of files the user has registered on the 
	 * serever
	 */
	 private String getUserRegisteredFileSummary(String userName )
	 {
		  String htmlResults = null;
    try
    {
      //create the parameter string to be passed to the DataRequestServlet -- 
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?action=userfilesummary&username="+userName);
			
      //connect to the dataExchaneServlet
			String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange"+sb.toString().trim();
			System.out.println("UserManagementServlet > OUT PARAMETERS: " + uri);
      int port=80;
      String requestType="POST";
      String s = gurl.requestURL(uri);
			
			//get all the files back for the main table -- pass the method the 
			// all flag
			htmlResults  = getUploadedFileDataTable( s, "all", "Registered Files" );
    }
    catch( Exception e )
    {
      System.out.println("Exception:  "
      +e.getMessage());
    }
    return(htmlResults);
	 }
	
	/**
	 * method that takes tabular data and retuns the data in an html table 
	 * with the appropriate markup -- this function is specific to tabularizing
	 * the files which the user uploaded to the file system
	 * @param data the data String that is returned from the database class and
	 *	kinda a funky looking fiting structure which is pipe deleimeted and each
	 * 	line is terminated by (**)
	 *
	 * @param userFileStartString the start string of the  file name assigned by
	 * the client user -- this can be used to return a table having like file
	 * names where for instance the term query can be applied  
	 */
	private String getUploadedFileDataTable(String data, 
		String userFileStartString, String tableName)
	{
		//System.out.println("UserManagementServlet > tabularizing data: " + data);
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellpadding=0 cellspacing=0 width=75% class=\"filetable\"> \n");
		//put the name of the table here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" > <td> "+tableName+"</td> <td></td> <td></td> <td></td> </tr>");
		//put the table header here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" >");
		sb.append("<td>Load</td>");
		sb.append("<td>Delete</td>");
		sb.append("<td>File Name</td>");
		sb.append("<td>Upload Date</td>");
		sb.append("<td>Type</td>");
		sb.append("<tr>");
		//notice the end-of-line token is two *
		StringTokenizer tok = new StringTokenizer(data, "**"); 
		while ( tok.hasMoreTokens()  ) 
		{
			String buf = tok.nextToken();
			sb.append("<tr  class=\"itemsmall\">  \n");
			
			StringTokenizer tok2 = new StringTokenizer(buf, "|"); 
			int colCnt = 0;
			String userFileName = null;
			String createDate = null;
			String accessionNumber = null;
			String fileType = null;
			String buf2 = null;
			
			//parse the columns into the cells
			while (tok2.hasMoreTokens() )
			{
				colCnt++;
				if ( colCnt == 1 )
					userFileName = tok2.nextToken();
				else if ( colCnt == 2 )
					accessionNumber = tok2.nextToken();
				else if ( colCnt == 4 )
					createDate = tok2.nextToken();
				else if ( colCnt == 3 )
					fileType = tok2.nextToken();
				else
					buf2 = tok2.nextToken();
			}
			//first add the option to delete the file then add the file name, date and
			//type
			
			
				if (accessionNumber != null &&  ! userFileName.startsWith("query") )
				{
					//sb.append("	<td width=\"5%\"><input type=\"image\"  src=\"/vegbank/images/funnelIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					//sb.append("	<td width=\"8%\"><input type=\"image\"  src=\"/vegbank/images/deleteIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					
					//here is the function to init the data loading process, where by the 
					//filename, url, file type, submitter name are issued to the 
					//data loading plugin on the frame work servlet which will describe
					//the data set in a form that the user can use to upload some or all
					//of the plots
					sb.append("	<td width=\"8%\"> "
					+"<a href=\"http://guest06.nceas.ucsb.edu/framework/servlet/framework?action=initPlotLoad&filename="
					+accessionNumber+"&username="+this.cookieValue+"&plot=all&filetype=tnc&datafileurl=vegbank.nceas.ucsb.edu/framework/servlet/dataexchage\"> " 
					+" <img src=\"/vegbank/images/funnelIcon.gif\">  </a> </td> \n");
					
					
					
					//here is the function to delet a file
					sb.append("	<td width=\"8%\"> "
					+"<a href=\"http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange?action=deletefile&filenumber="
					+accessionNumber+"&username="+this.cookieValue+"\"> " 
					+" <img src=\"/vegbank/images/deleteIcon.gif\">  </a> </td> \n");
					
					sb.append("	<td> <a href=\"http://vegbank.nceas.ucsb.edu/uploads/"+accessionNumber+"\">"  + userFileName + "</a> </td> \n");
					sb.append("	<td>" +createDate+ "</td> \n");
					sb.append("	<td>" +fileType+ "</td> \n");
					sb.append("</tr> \n \n");
				}
		}
		sb.append("</table> \n");
		return(sb.toString() );
	}
	
	
	
	
	/**
	 * method that returns the cookie value associated with the 
	 * current browser
	 */
	private String getCookieValue(HttpServletRequest req)
	{
		//get the cookies - if there are any
		String cookieName = null;
		String cookieValue = null;

		Cookie[] cookies = req.getCookies();
		//determine if the requested page should be shown
    if (cookies.length > 0) 
		{
			for (int i = 0; i < cookies.length; i++) 
			{
      	Cookie cookie = cookies[i];
				//out.print("Cookie Name: " +cookie.getName()  + "<br>");
        cookieName=cookie.getName();
				//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
				cookieValue=cookie.getValue();
				System.out.println("UserManagementServlet > client passing the cookie: "+cookieName+" value: "
					+cookieValue);
			}
  	}
		return(cookieValue);
	}
	
	/**
	 * method that returns the cookie value associated with the 
	 * current browser
	 * 
	 */
	private String getCookieName(HttpServletRequest req)
	{
		//get the cookies - if there are any
		String cookieName = null;
		String cookieValue = null;

		Cookie[] cookies = req.getCookies();
		//determine if the requested page should be shown
    if (cookies.length > 0) 
		{
			for (int i = 0; i < cookies.length; i++) 
			{
      	Cookie cookie = cookies[i];
				//out.print("Cookie Name: " +cookie.getName()  + "<br>");
        cookieName=cookie.getName();
				//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
				cookieValue=cookie.getValue();
				System.out.println("UserManagementServlet > client passing the name: "+cookieName+" value: "
					+cookieValue);
			}
  	}
		return(cookieName);
	}
	
	
	
	//method that retuns the java script functions for this page -- in the 
	//future consider using a link to an extrenal java script file
	private String getJavaScriptFunctions()
	{
		//there is only one java script file here and it is the 
		//one that will allow a user to choose a query type
		StringBuffer sb = new StringBuffer();
		sb.append("<script language=\"JavaScript\"> \n");
		sb.append("<!-- \n");
		sb.append("function MM_jumpMenu(targ,selObj,restore){ //v3.0 \n");
  	sb.append("eval(targ+\".location='\"+selObj.options[selObj.selectedIndex].value+\"'\"); \n");
  	sb.append("if (restore) selObj.selectedIndex=0; \n");
		sb.append("} \n");
		sb.append("//--> \n");
		sb.append("</script> \n");
		
		return(sb.toString() );

	}
	
}

