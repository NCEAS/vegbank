/*
 *	'$RCSfile: CertificationLoadAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-01-16 19:28:56 $'
 *	'$Revision: 1.2 $'
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

package org.vegbank.ui.struts;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.ui.struts.CertificationForm;
import org.vegbank.common.model.WebUser;


/**
 * Prepopulates a certification form with the logged in user's profile and 
 * optionally a saved usercertification record by passing cert_id via URI.
 *
 * @author anderson
 */
public class CertificationLoadAction extends VegbankAction {

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		LogUtility.log("In action CertificationLoadAction");
		ActionErrors errors = new ActionErrors();

		// Get the form
		CertificationForm certForm = (CertificationForm)form;
		if (certForm == null) {
			LogUtility.log("CertificationLoadAction: constructing new certForm");
			certForm = new CertificationForm();
		}

		// this param is optional -if null, render blank form
		String cert_id = request.getParameter("cert_id");
		WebUser user = getUser(request.getSession());
		if (user == null) {
			LogUtility.log("CertificationLoadAction: user is null -- VERY BAD!");
		}


		String tmp = user.getGivenname();
		if (tmp != null)  certForm.setGivenName(tmp);
		tmp = user.getSurname();
		if (tmp != null)  certForm.setSurName(tmp);
		tmp = user.getDayphone();
		if (tmp != null)  certForm.setPhoneNumber(tmp);
		tmp = user.getEmail();
		if (tmp != null)  certForm.setEmailAddress(tmp);

		if (user.getOrganizationname() == null) {
			tmp = user.getInstitution();
			if (tmp != null)  certForm.setCurrentOrg(tmp);
		} else {
			certForm.setCurrentOrg(user.getOrganizationname());
		}

		if (PermComparison.matchesOne("pro", user.getPermissiontype())) {
			certForm.setCurrentCertLevel("professional");
		} else {
			certForm.setCurrentCertLevel("certified");
		}

		// using <bean:write> requires this
		request.setAttribute("reqAttribBean", certForm);
		
		
		LogUtility.log("Leaving CertificationLoadAction");
		return mapping.findForward("success");
	}


	/**
	 * method that handles the insertion of certification-request data in to the 
	 * user database ( the USER_CERTIFICATION table ) and notification of the appropriate
	 * people that the request has been submitted. <br> <br>
	 * 
	 */
	 private void handleCertification(HttpServletRequest req, HttpServletResponse res) {

		 /*
		 try {
			 LogUtility.log("UserManagementServlet > performing certification action");
			 
			 // determine if the correct attributes were passed and if so then 
			 // commit to the database etc.
			 if ( emailAddress != null && emailAddress.length() > 2)
			 {
				// Need much better validation on the form with relevant error messages.
				// check that the required paramteres are upto snuff
				if ( surName.length() > 0 && givenName.length() > 0 && phoneNumber.length() > 0  && currentCertLevel.length() > 0 
					&& degreeInst.length() > 0  && currentInst.length() > 0  && esaPos.length() > 0  && vegSamplingDoc.length() > 0 
					&& vegAnalysisDoc.length() > 0 
					&& vegSamplingDoc.length() > 0 && vegAnalysisDoc.length() > 0  && usnvcExpDoc.length() >0 
					&& vegbankExpDoc.length() > 0 &&  useVegbank.length() > 0 && plotdbDoc.length() > 0)
					{
						LogUtility.log("surName: '"+surName+"'");
						UserDatabaseAccess userdb = new UserDatabaseAccess();
						userdb.insertUserCertificationInfo(emailAddress, surName, givenName,
						phoneNumber, phoneType, currentCertLevel, cvDoc, highestDegree,
						degreeYear, degreeInst, currentInst, currentPos, esaPos,
						profExperienceDoc, relevantPubs, vegSamplingDoc,  vegAnalysisDoc, usnvcExpDoc, 
						vegbankExpDoc, plotdbDoc, expRegionA, expRegionAVeg, expRegionAFlor, 
						expRegionANVC, esaSponsorNameA, esaSponsorEmailA, esaSponsorNameB, esaSponsorEmailB, 
						peerReview, additionalStatements);
						
						// send a success message
						Hashtable replaceHash = new Hashtable();
						StringBuffer sb = new StringBuffer();
						sb.append("Thank you "+emailAddress+"! <br> ");
						sb.append("Your certification request has been passed onto the VegBank administration. <br>");
						sb.append("We will contact you shortly.");
						
						replaceHash.put("messages",  sb.toString());
						
						StringWriter output = new StringWriter();
						util.filterTokenFile(genericForm, output, replaceHash);
						
						//print the outfile to the browser
						PrintWriter out = res.getWriter();
						
						out.print( output.toString() );
						
						// send email message to the user 
						String mailHost = "hyperion.nceas.ucsb.edu";
						String from = "help@vegbank.org";
						String to = emailAddress;
						//String cc = "vegbank@vegbank.org";
						String cc = "";
						String subject = "Your VegBank Certification Request";
						String body = emailAddress + ": Your VegBank certification request has been received.";
						util.sendHTMLEmail(mailHost, from, to, cc, subject, body);
						
						// send the data to the vegbank administrator 
						this.sendAdminCertRequest(params);
						
						// delete the form
						Thread.sleep(1000);
						util.flushFile(certificationValidation);
						
					}
					else
					{
						LogUtility.log("UserManagementServlet > inadequate value(s) for required attributes ");
						// send them a note to go back and fill in the correct required attributes
						Hashtable replaceHash = new Hashtable();
						replaceHash.put("messages", "You are missing a required attribute to submit this form");
						replaceHash.put("surName", ""+surName);
						replaceHash.put("givenName", ""+givenName);
						replaceHash.put("registeredEmail",  ""+emailAddress);
						replaceHash.put("phoneNumber",  ""+phoneNumber);
						replaceHash.put("currentCertLevel",  ""+currentCertLevel);
						replaceHash.put("degreeInst",  ""+degreeInst);
						replaceHash.put("currentInst",  ""+currentInst);
						replaceHash.put("esaPos",  ""+esaPos);
						replaceHash.put("vegSamplingDoc",  ""+vegSamplingDoc);
						replaceHash.put("vegAnalysisDoc",  ""+vegAnalysisDoc);
						replaceHash.put("usnvcExpDoc",  ""+usnvcExpDoc); // start
						replaceHash.put("vegbankExpDoc",  ""+vegbankExpDoc);
						replaceHash.put("useVegbank",  ""+useVegbank);
						replaceHash.put("plotdbDoc",  ""+plotdbDoc);
						
						StringWriter output = new StringWriter();
						util.filterTokenFile(certificationTemplate, output, replaceHash);
						//print the outfile to the browser
						PrintWriter out = res.getWriter();

						out.println( output.toString() );
						// delete the form 
						Thread.sleep(1000);
						util.flushFile(certificationValidation);
						
					}
			 }
			 else
			 {
				LogUtility.log("UserManagementServlet > not a valid email to commit certification");
				// redirect to the page that redirects to the login
				res.sendRedirect("/vegbank/forms/redirection_template.html");
			 }

		 } catch(Exception e) {

		 	LogUtility.log("CertificationLoadAction: ", e);
		 }
	 */
	 }
	 
	/**
	 * this method takes the input parameters to become a vegbank certified user 
	 * and sends them to the vegbank system administrator(s)
	 * @param params -- the input parameters 
	 */
	private void sendAdminCertRequest(Hashtable params) {

		/*
		try {
			StringBuffer messageBody = new StringBuffer();

			messageBody.append("VegBank Administrator, <br>\n");
			messageBody.append(" Please review the following certification information. <br><br>\n");
			messageBody.append(" Email: "+emailAddress+" <br>\n");
			messageBody.append(" Sur Name: "+surName +" <br>\n");
			messageBody.append(" Given Name: "+givenName+" <br>\n");
			messageBody.append(" Phone Number: "+phoneNumber+" <br>\n");
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

			LogUtility.log( messageBody.toString() );
			//email this to the vegbank admin
			// send email message to the user 
			String mailHost = "vegbank.org";
			String from = "vegbank";
			String to = vegBankAdmin;
			String cc = "panel@vegbank.org";
			String subject = "VegBank Certification for " + givenName + " " + surName;
			String body = messageBody.toString();
			util.sendHTMLEmail(mailHost, from, to, cc, subject, body);

		} catch(Exception e) {
			LogUtility.log("CertificationLoadAction: ", e);
		}
		*/
	}

}
