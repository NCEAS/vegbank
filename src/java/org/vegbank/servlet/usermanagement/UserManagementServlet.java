/*
 *    '$RCSfile: UserManagementServlet.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: anderson $'
 *   '$Date: 2004-02-07 06:45:56 $'
 *   '$Revision: 1.21 $'
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
package org.vegbank.servlet.usermanagement; 
 
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.GetURL;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.UserDatabaseAccess;


public class UserManagementServlet extends HttpServlet 
{
	private static final String vegBankAdmin = "help@vegbank.org";
	//private UserDatabaseAccess userdb;
	private ServletUtility util = new ServletUtility();
	private GetURL gurl = new GetURL();
	
	// these are the forms for the certification input:
	private static final String certificationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/certification_valid.html";
	private static final String certificationValidation = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/valid.html";
	private static final String genericForm = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/generic_form.html";
	private static final String userUpdateValidation = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/valid.html";
	private static final String userUpdateTemplate =  "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/user_update_form.html";
	private static final String userPasswordUpdateTemplate =  "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/user_update_password_form.html";
	
	//constructor
	public UserManagementServlet()
	{
		System.out.println("init: UserManagementServlet");
	}
	
	/** Handle "GET" method requests from HTTP clients */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws IOException, ServletException
	{
		try
		{
			//get the parameters
			Hashtable params = util.parameterHash(req);
			System.out.println("UserManagementServlet > in params: " + params);

			String action = getAction(params);
			if (action == null)
			{
				System.out.println(
					"UserManagementServlet > action parameter required: ");
			}
			else
			{

				// CHANGE USER SETTINGS 
				if (action.equals("changeusersettings"))
				{
					res.setContentType("text/html");
					this.handleSettingsModification(req, res);
				}
				// GET THE USER'S PERMISSION LEVEL
				else if (action.equals("getpermissionlevel"))
				{
					this.handlePermissionLevelRequest(req, res);
				}
				else if (action.equals("changeuserpassword"))
				{
					this.handlePasswordModification(req, res);
				}
				else
				{
					System.out.println(
						"UserManagementServlet > unrecognized action: "
							+ action);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** Handle "POST" method requests from HTTP clients */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		this.doGet(req, res);
	}
	/**
	 * Handle password modification 
	 * 
	 * @param req
	 * @param res
	 */
	private void handlePasswordModification(HttpServletRequest req, HttpServletResponse res)
	{
		StringWriter output = new StringWriter();
		String errorMessage = "";
		boolean success = true;
		UserDatabaseAccess userdb = new UserDatabaseAccess();
		
		System.out.println("UserManagementServlet > performing user password  modification");
		
		String currentpassword = req.getParameter("currentpassword");
		String newpassword1 = req.getParameter("newpassword1");
	 	String newpassword2 = req.getParameter("newpassword2");		
		String emailAddress = (String) req.getSession().getAttribute("emailAddress");
		String task = req.getParameter("task");


		
		System.out.println("UserManagementServlet > passwords " + newpassword1 + " "+ newpassword2 + " " + currentpassword  + " " + emailAddress);
		// just requesting form display
		if ( task != null && task.equals("display") )
		{
			success =false;
			errorMessage = ""; // No error to display		
		}
		// Autenticate the user
		else if (! userdb.authenticateUser(emailAddress, currentpassword) )
		{
			success = false;
			errorMessage = "<b class=\"error\">Please enter correct current password</b>";
		}
		// Disallow empty passwords
		else if (newpassword1.length() < 1 )
		{
			success = false;
			errorMessage = "<b class=\"error\">Empty password not allowed</b>";				
		}
		// Check that the new passwords are identical
		else if (! newpassword1.equals(newpassword2) )
		{
			success = false;
			errorMessage = "<b class=\"error\">Please enter identical passwords</b>";
		}
			
		if (success)
		{
			userdb.updatePassword(newpassword1, emailAddress);		 
			// send a success message
			Hashtable replaceHash = new Hashtable();
			String message = "Your VegBank password has been updated. <br>";
			replaceHash.put("messages",  message);
			util.filterTokenFile(genericForm, output, replaceHash);		
		}
		else
		{
			// Return user to the form
			Hashtable h = new Hashtable();
			h.put("errormessage", errorMessage );
			// Back to the form
			util.filterTokenFile(userPasswordUpdateTemplate, output, h);
		}
		
		//print the outfile to the browse
		try
		{
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.print( output.toString() );	
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}	

	}

	/**
	 * method that returns the integer value between 1-5 of the user's 
	 * permission level based on the name that is passed 
	 *
	 * 
	 * @param req -- the http request object
	 * @param res -- the http response object
	 */
	 private void handlePermissionLevelRequest(HttpServletRequest req, HttpServletResponse res)
	 {
		 try
		 {
			 PrintWriter out = res.getWriter();
			 Hashtable params = util.parameterHash(req);
			 if ( params.containsKey("user") )
			 {
				 String email = (String)params.get("user");
				 System.out.println("UserManagementServlet > looking up the permission level for: " + email);
				UserDatabaseAccess userdb = new UserDatabaseAccess();
				 int level = userdb.getUserPermissionLevel(email);
				 out.println(level);
				 out.close();
			 } 
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
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
	private void handleSettingsModification(
		HttpServletRequest req,
		HttpServletResponse res)
	{
		String errorMessage = "";
		try
		{
			System.out.println(
				"UserManagementServlet > performing user settings modification");

			WebUser user = 
				(WebUser) req.getSession().getAttribute(Constants.USER_KEY);
			String emailAddress = user.getEmail();

			// FIGURE OUT WHETHER TO SEND THE UPDATE FORM OR GET THE PARAMETRS
			Hashtable params = util.parameterHash(req);
			if (params.containsKey("surName"))
			{
				System.out.println(
					"UserManagementServlet > reading the update parameters");

				//get the input paramters
				String newEmailAddress =
					(String) params.get("emailAddress");
				String surName = (String) params.get("surName");
				String givenName = (String) params.get("givenName");
				String permissionType =
					(String) params.get("permissionType");
				String institution = (String) params.get("institution");
				String ticketCount = (String) params.get("ticketCount");
				String address = (String) params.get("address");
				String city = (String) params.get("city");
				String state = (String) params.get("state");
				String country = (String) params.get("country");
				String postalcode = (String) params.get("postalcode");
				String phoneNumber = (String) params.get("phoneNumber");
				String userId = (String) params.get("userId");
				String partyId = (String) params.get("partyId");

				//put into a hashtable
				Hashtable h = new Hashtable();
				h.put("emailAddress", newEmailAddress);
				h.put("surName", "" + surName);
				h.put("givenName", "" + givenName);
				h.put("permissionType", "" + permissionType);
				h.put("institution", "" + institution);
				h.put("ticketCount", "" + ticketCount);
				h.put("address", "" + address);
				h.put("city", "" + city);
				h.put("state", "" + state);
				h.put("country", "" + country);
				h.put("postalcode", "" + postalcode);
				h.put("phoneNumber", "" + phoneNumber);
				h.put("userId", "" + userId);
				h.put("partyId", "" + partyId);

				// instantiate the database
				UserDatabaseAccess userdb = new UserDatabaseAccess();

				StringWriter output = new StringWriter();
				Hashtable replaceHash = new Hashtable();
				StringBuffer sb = new StringBuffer();

				if (userdb.isEmailUnique(newEmailAddress))
				{
					userdb.updateUserInfo(h);

					// send a success message
					sb.append(
						"Thank you "
							+ givenName
							+ " "
							+ surName
							+ "! <br> ");
					sb.append(
						"Your VegBank profile has been updated. <br>");

					req.getSession().setAttribute(
						"emailAddress",
						newEmailAddress);
					// = newEmailAddress;
				}
				else
				{
					// Send an error message
					sb.append("Please choose an unique email address");
				}

				replaceHash.put("messages", sb.toString());
				util.filterTokenFile(genericForm, output, replaceHash);
				//print the output to the browser
				PrintWriter out = res.getWriter();
				out.print(output.toString());

			}
			else
			{
				System.out.println(
					"UserManagementServlet > sending the current parametrs");
				UserDatabaseAccess userdb = new UserDatabaseAccess();
				WebUser userBean = userdb.getUser(emailAddress);

				//System.out.println("-----> "  + partyId);

				Hashtable replaceHash = new Hashtable();
				replaceHash.put("messages", " ");
				replaceHash.put("surName", "" + userBean.getSurname());
				replaceHash.put("givenName", "" + userBean.getGivenname());
				replaceHash.put("password", "" + userBean.getPassword());
				replaceHash.put("address", ""+ userBean.getAddress() );
				replaceHash.put("city", ""+userBean.getCity() );
				replaceHash.put("state", ""+userBean.getState() );
				replaceHash.put("country", ""+userBean.getCountry());
				replaceHash.put("postalcode", ""+userBean.getPostalcode() );
				replaceHash.put("phoneNumber", ""+userBean.getDayphone() );
				replaceHash.put("institution", "" + userBean.getInstitution());
				replaceHash.put("ticketCount", ""+userBean.getTicketcount() );
				replaceHash.put("permissionType", String.valueOf(userBean.getPermissiontype()));
				replaceHash.put("emailAddress", emailAddress);
				replaceHash.put("userId", ""+ userBean.getUserid() );
				replaceHash.put("partyId", ""+userBean.getPartyid() );
				replaceHash.put("errormessage", errorMessage);

				StringWriter output = new StringWriter();
				util.filterTokenFile(
					userUpdateTemplate,
					output,
					replaceHash);
				//print the outfile to the browser
				PrintWriter out = res.getWriter();
				out.println(output.toString());
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

	}
		

	 /**
	  * this method takes the input parameters to become a vegbank certified user 
		* and sends them to the vegbank system administrator(s)
		* @param params -- the input parameters 
		*/
		private void sendAdminCertRequest(Hashtable params)
		{
			StringBuffer messageBody = new StringBuffer();
			try
			{
			 
			 String submittedEmail = (String)params.get("email");
			 String surName = (String)params.get("surName");
			 String givenName  = (String)params.get("givenName");
			 String phoneNumber = (String)params.get("phoneNumber");
			 String phoneType = (String)params.get("phoneType");
			 String currentCertLevel = (String)params.get("currentCertLevel");
			 String certReq = (String)params.get("certReq");
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
			 String useVegbank = (String)params.get("useVegbank");
			 String plotdbDoc = (String)params.get("plotdbDoc");
			 String expRegionA = (String)params.get("expRegionA");
			 String expRegionAVeg = (String)params.get("expRegionAVeg");
			 String expRegionAFlor = (String)params.get("expRegionAFlor");
			 String expRegionANVC = (String)params.get("expRegionANVC");
			 
			 String expRegionB = (String)params.get("expRegionB");
			 String expRegionBVeg = (String)params.get("expRegionBVeg");
			 String expRegionBFlor = (String)params.get("expRegionBFlor");
			 String expRegionBNVC = (String)params.get("expRegionBNVC");
			 
			 String expRegionC = (String)params.get("expRegionC");
			 String expRegionCVeg = (String)params.get("expRegionCVeg");
			 String expRegionCFlor = (String)params.get("expRegionCFlor");
			 String expRegionCNVC = (String)params.get("expRegionCNVC");
			 
			 
			 String esaSponsorNameA  = (String)params.get("esaSponsorNameA");
			 String esaSponsorEmailA = (String)params.get("esaSponsorEmailA");
			 String esaSponsorNameB = (String)params.get("esaSponsorNameB");
			 String esaSponsorEmailB = (String)params.get("esaSponsorEmailB");
			 String peerReview = (String)params.get("peerReview");
			 String additionalStatements = (String)params.get("additionalStatements");
			 
			 messageBody.append("VegBank Administrator, <br>\n");
			 messageBody.append(" Please review the following certification information. <br><br>\n");
			 messageBody.append(" Email: "+submittedEmail+" <br>\n");
			 messageBody.append(" Sur Name: "+surName +" <br>\n");
			 messageBody.append(" Given Name: "+givenName+" <br>\n");
			 messageBody.append(" Phone Number: "+phoneNumber+" <br>\n");
			 messageBody.append(" Phone Type: "+phoneType+" <br>\n");
			 messageBody.append(" Current Cert Level: "+currentCertLevel+" <br>\n");
			 messageBody.append(" Requested Cert Level: " + certReq +" <br>\n");
			 messageBody.append(" Highest Degree: "+highestDegree+" <br>\n");
			 messageBody.append(" Degree Year: "+degreeYear+" <br>\n");
			 messageBody.append(" Degree Isnt: "+degreeInst+" <br>\n");
			 messageBody.append(" Current Inst: "+currentInst+" <br>\n");
			 messageBody.append(" Current Position: "+currentPos+" <br>\n");
			 messageBody.append(" ESA Memeber: "+esaPos+" <br>\n");
			 messageBody.append(" Prof. Experience: "+profExperienceDoc+" <br>\n");
			 messageBody.append(" Relevant Pubs: "+relevantPubs+" <br>\n");
			 messageBody.append(" Veg Sampling Exp: "+vegSamplingDoc+" <br>\n");
			 messageBody.append(" Veg Analaysis Exp: "+vegAnalysisDoc+" <br>\n");
			 messageBody.append(" USNVC Exp: "+usnvcExpDoc+" <br>\n");
			 messageBody.append(" VegBank Experience: "+vegbankExpDoc+" <br>\n");
			 messageBody.append(" Intended VB Use: "+useVegbank+" <br>\n");
			 messageBody.append(" Plot DB/Tools Exp: "+ plotdbDoc+" <br>\n");
			 messageBody.append(" expRegionA: "+expRegionA+" <br>\n");
			 messageBody.append(" expRegionAVeg: "+expRegionAVeg+" <br>\n");
			 messageBody.append(" expRegionAFlor: "+expRegionAFlor+" <br>\n");
			 messageBody.append(" expRegionANVC: "+expRegionANVC+" <br>\n");
			 messageBody.append(" esaSponsorNameA: "+esaSponsorNameA+" <br>\n");
			 messageBody.append(" esaSponsorEmailA: "+esaSponsorEmailA+" <br>\n");
			 messageBody.append(" esaSponsorNameB: "+esaSponsorNameB+" <br>\n");
			 messageBody.append(" esaSponsorEmailB: "+esaSponsorEmailB+" <br>\n");
			 messageBody.append(" expRegionB: "+expRegionB+" <br>\n");
			 messageBody.append(" expRegionBVeg: "+expRegionBVeg+" <br>\n");
			 messageBody.append(" expRegionBFlor: "+expRegionBFlor+" <br>\n");
			 messageBody.append(" expRegionBNVC: "+expRegionBNVC+" <br>\n");
			 
			 messageBody.append(" expRegionC: "+expRegionC+" <br>\n");
			 messageBody.append(" expRegionCVeg: "+expRegionCVeg+" <br>\n");
			 messageBody.append(" expRegionCFlor: "+expRegionCFlor+" <br>\n");
			 messageBody.append(" expRegionCNVC: "+expRegionCNVC+" <br>\n");
			 messageBody.append(" peerReview: "+peerReview+" <br>\n");
			 messageBody.append(" additionalStatements: "+additionalStatements+" <br>\n");
			 
			 System.out.println( messageBody.toString() );
			 //email this to the vegbank admin
			 // send email message to the user 
			 String mailHost = "vegbank.org";
			 String from = "vegbank";
			 String to = vegBankAdmin;
			 String cc = "panel@vegbank.org";
			 String subject = "VegBank Certification for " + givenName + " " + surName;
			 String body = messageBody.toString();
			 util.sendHTMLEmail(mailHost, from, to, cc, subject, body);
			
			
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
			if (params.containsKey("action"))
			{
				s = (String) params.get("action");
			}
			else
			{
				System.out.println("UserManagementServlet > no action found");
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (s);
	}
}

