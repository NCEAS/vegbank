/*
 *	'$RCSfile: CertificationSaveAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-01-16 19:28:56 $'
 *	'$Revision: 1.3 $'
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
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.utility.ServletUtility;

/**
 * @author anderson
 */
public class CertificationSaveAction extends VegbankAction {


	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		LogUtility.log(" In action CertificationSaveAction");
		ActionErrors errors = new ActionErrors();

		CertificationForm certForm = (CertificationForm)form;
		saveCertification(certForm, errors);
		sendAdminCertRequest(certForm, errors);
		
		LogUtility.log("Leaving CertificationSaveAction");
		return mapping.findForward("Certification");
	}


	/**
	 * method that handles the insertion of certification-request data in to the 
	 * user database ( the usercertification table ) and notification of the appropriate
	 * people that the request has been submitted.
	 * 
	 */
	 private void saveCertification(CertificationForm form, ActionErrors errors) {

		try {
			LogUtility.log("CertSave > saving certification application");
			 
			UserDatabaseAccess userdb = new UserDatabaseAccess();

			userdb.insertUserCertificationInfo(form);
			
			// send email message to the user 
			String mailHost = "hyperion.nceas.ucsb.edu";
			String from = "help@vegbank.org";
			String to = form.getEmailAddress();
			String cc = "anderson@nceas.ucsb.edu";
			//String cc = "";
			String subject = "Your VegBank Certification Request";
			String body = "Dear " + form.getGivenName() + 
				": \n\nYour VegBank certification request has been received and will " +
				"be reviewed.  Please await our response.\n\n" +
				"Thank you,\nThe VegPanel\n\n\n";

			ServletUtility.sendPlainTextEmail(mailHost, from, to, cc, subject, body);
			
		 } catch(Exception e) {
		 	LogUtility.log("CertificationSaveAction: ", e);
		 }
	 }
	 
	/**
	 * This method takes the form bean values
	 * and sends them to the vegpanel email list.
	 * @param form -- the cert. form bean
	 */
	private void sendAdminCertRequest(CertificationForm form, ActionErrors errors) {

		try {
			StringBuffer messageBody = new StringBuffer();

			messageBody.append("VegBank Administrator, \n")
				.append(" Please review the following certification information. \n\n")
				
				.append(" Name: "+form.getSurName() +", "+form.getGivenName()+" \n")
				.append(" Email: "+form.getEmailAddress()+" \n")
				.append(" Phone Number: "+form.getPhoneNumber()+" \n\n")
				
				.append(" Current Cert Level: "+form.getCurrentCertLevel()+" \n")
				.append(" Requested Cert Level: " + form.getRequestedCert() +" \n")
				.append(" Highest Degree: "+form.getHighestDegree()+" \n")
				.append(" Degree Year: "+form.getDegreeYear()+" \n")
				.append(" Degree Inst: "+form.getDegreeInst()+" \n")
				.append(" Current Organization: "+form.getCurrentOrg()+" \n")
				.append(" Current Position: "+form.getCurrentPos()+" \n")
				.append(" ESA Member: "+form.getEsaMember()+" \n")
				.append(" Prof. Experience: "+form.getProfExp()+" \n")
				.append(" Relevant Pubs: "+form.getRelevantPubs()+" \n")
				.append(" Veg Sampling Exp: "+form.getVegSamplingExp()+" \n")
				.append(" Veg Analysis Exp: "+form.getVegAnalysisExp()+" \n")
				.append(" US-NVC Exp: "+form.getUsnvcExp()+" \n")
				.append(" VegBank Experience: "+form.getVbExp()+" \n")
				.append(" Intended VB Use: "+form.getVbIntention()+" \n")
				.append(" Plot DB/Tools Exp: "+ form.getToolsExp()+" \n\n")

				.append(" Self assessment of knowledge (1=least, 5=most)\n")
				.append(" Region 1: "+form.getExpRegionA()+" \n")
				.append("   Vegetation : "+form.getExpRegionAVeg()+" \n")
				.append("   Floristics : "+form.getExpRegionAFlor()+" \n")
				.append("   US-NVC/IVC : "+form.getExpRegionANVC()+" \n")
				.append(" Region 2: "+form.getExpRegionB()+" \n")
				.append("   Vegetation : "+form.getExpRegionBVeg()+" \n")
				.append("   Floristics : "+form.getExpRegionBFlor()+" \n")
				.append("   US-NVC/IVC : "+form.getExpRegionBNVC()+" \n")
				.append(" Region 3: "+form.getExpRegionC()+" \n")
				.append("   Vegetation : "+form.getExpRegionCVeg()+" \n")
				.append("   Floristics : "+form.getExpRegionCFlor()+" \n")
				.append("   US-NVC/IVC : "+form.getExpRegionCNVC()+" \n\n")

				.append(" ESA Sponsor #1 \n")
				.append("   Name : "+form.getEsaSponsorNameA()+" \n")
				.append("   Email: "+form.getEsaSponsorEmailA()+" \n")
				.append(" ESA Sponsor #2 \n")
				.append("   Name : "+form.getEsaSponsorNameB()+" \n")
				.append("   Email: "+form.getEsaSponsorEmailB()+" \n\n")

				.append(" Peer review interest?: "+form.getPeerReview()+" \n")
				.append(" Additional statement: "+form.getAddlStmt()+" \n")
				.append(" \n\n")
				.append(" THANK YOU\n")
				.append(" \n\n\n");

			String mailHost = "vegbank.org";
			String from = "vegbank";
			String to = "anderson@nceas.ucsb.edu";
			//String to = "panel@vegbank.org";
			String cc = null;
			String subject = "VegBank Certification for " + form.getGivenName() + " " + form.getSurName();
			String body = messageBody.toString();
			ServletUtility.sendPlainTextEmail(mailHost, from, to, cc, subject, body);

		} catch(Exception e) {
			LogUtility.log("CertificationSaveAction.sendAdminCertRequest: ", e);
		}
	}

}
