/*
 *	'$RCSfile: CertificationSaveAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-01-31 01:29:25 $'
 *	'$Revision: 1.4 $'
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
import javax.mail.internet.AddressException;
import javax.mail.MessagingException;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.Constants;
import org.vegbank.common.model.utility.Regionlist;
import org.vegbank.common.model.WebUser;

/**
 * @author anderson
 */
public class CertificationSaveAction extends VegbankAction {


	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors errors = new ActionErrors();

		try {
			CertificationForm certForm = getCertForm(form, getUser(request.getSession()));
			
			LogUtility.log("CertSave: Saving...");
			saveCertification(certForm, errors);
			
			LogUtility.log("CertSave: Notifying admin...");
			sendAdminCertRequest(certForm, errors);
			
		} catch(Exception ex) {
			LogUtility.log("ERROR: CertificationSaveAction", ex);
			//errors.add(ActionErrors.GLOBAL_ERROR, new ActionMessage(
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.general", ex.getMessage()));
			saveErrors(request, errors);
			return mapping.findForward("vberror");
		}

		
		return mapping.findForward("success");
	}


	/**
	 * method that handles the insertion of certification-request data in to the 
	 * user database ( the usercertification table ) and notification of the appropriate
	 * people that the request has been submitted.
	 * 
	 */
	 private void saveCertification(CertificationForm form, ActionErrors errors)
			throws AddressException, MessagingException, SQLException {

		UserDatabaseAccess userdb = new UserDatabaseAccess();

		userdb.insertUserCertificationInfo(form);
		
		// send email message to the user 
		String mailHost = "hyperion.nceas.ucsb.edu";
		String from = "help@vegbank.org";
		String to = form.getEmailAddress();
		String cc = "";
		String subject = "Your VegBank Certification Request";
		String body = "Dear " + form.getGivenName() + 
			": \n\nYour VegBank certification request has been received and will " +
			"be reviewed.  Please await our response.\n\n" +
			"Thank you,\nThe VegPanel\n\n\n";

		if (to == null || to.equals("")) {
			throw new AddressException("CertSave: no email address for user");
		}
		LogUtility.log("CertSave: Sending confirmation to user: to=" +
				to + ", from=" + from);
		ServletUtility.sendPlainTextEmail(mailHost, from, to, cc, subject, body);
	 }
	 
	/**
	 * This method takes the form bean values
	 * and sends them to the vegpanel email list.
	 * @param form -- the cert. form bean
	 */
	private void sendAdminCertRequest(CertificationForm form, ActionErrors errors) 
			throws javax.mail.internet.AddressException, javax.mail.MessagingException {
				
		// get the region names				
		javax.servlet.ServletContext sc = this.getServlet().getServletContext();
		Regionlist regions = (Regionlist)sc.getAttribute(
				Constants.REGIONLISTBEAN_KEY);
		String regionA=null, regionB=null, regionC=null;
		String tmp = form.getExpRegionA();			
		if (tmp != null) {	regionA = regions.getName(tmp); }
		tmp = form.getExpRegionB();			
		if (tmp != null) {	regionB = regions.getName(tmp); }
		tmp = form.getExpRegionC();
		if (tmp != null) {	regionC = regions.getName(tmp); }

		

		StringBuffer messageBody = new StringBuffer();

		messageBody.append("Dear VegPanel Member:\n")
			.append(" Please review the following certification application. \n\n")
			
			.append(" Name:  "+form.getSurName() +", "+form.getGivenName()+" \n")
			.append(" Email:  "+form.getEmailAddress()+" \n")
			.append(" Phone Number:  "+form.getPhoneNumber()+" \n\n")
			.append(" Current Cert Level:  "+form.getCurrentCertLevelName()+" \n")
			.append(" Requested Cert Level:  "+form.getRequestedCert()+" \n")
			.append(" Highest Degree:  "+form.getHighestDegree()+" \n");

		// degreeYear
		tmp = form.getDegreeYear();
		if (tmp != null && !tmp.equals("")) messageBody.append(" Degree Year:  "+tmp+" \n");

		// degreeInst
		tmp = form.getDegreeInst();
		if (tmp != null && !tmp.equals("")) messageBody.append(" Degree Inst:  "+tmp+" \n");

		messageBody.append(" Current Organization:  "+form.getCurrentOrg()+" \n")
			.append(" Current Position:  "+form.getCurrentPos()+" \n");

		tmp = form.getEsaMember();
		if (tmp == null || tmp.equals("") || tmp.equals("0")) 
				{ tmp="NO"; } else { tmp="YES"; }
		messageBody.append(" ESA Member:  "+tmp+" \n\n");

		// profExp
		tmp = form.getProfExp();
		if (tmp != null && !tmp.equals("")) messageBody.append(" Prof. Experience:  "+tmp+" \n\n");

		// relevantPubs
		tmp = form.getRelevantPubs();
		if (tmp != null && !tmp.equals("")) messageBody.append(" Relevant Pubs:  "+tmp+" \n\n");

		messageBody.append(" Veg Sampling Exp:  "+form.getVegSamplingExp()+" \n\n")
			.append(" Veg Analysis Exp:  "+form.getVegAnalysisExp()+" \n\n")
			.append(" US-NVC Exp:  "+form.getUsnvcExp()+" \n\n");

		// vbExp
		tmp = form.getVbExp();
		if (tmp != null && !tmp.equals("")) messageBody.append(" VegBank Experience:  "+tmp+" \n\n");

		// vbIntention
		messageBody.append(" Intended VB Use:  "+form.getVbIntention()+" \n\n");

		// toolsExp
		tmp =  form.getToolsExp();
		if (tmp != null && !tmp.equals("")) messageBody.append(" Plot DB/Tools Exp:  "+tmp+" \n\n");

		// self assessment
		messageBody.append("\n Self assessment of knowledge (1=least, 5=most)\n");
			
		if (regionA != null) {
			messageBody.append(" Region 1:  "+regionA+" \n")
			.append("   Vegetation :  "+form.getExpRegionAVeg()+" \n")
			.append("   Floristics :  "+form.getExpRegionAFlor()+" \n")
			.append("   US-NVC/IVC :  "+form.getExpRegionANVC()+" \n");
		}
			
		if (regionB != null) {
			messageBody.append(" Region 2:  "+regionB+" \n")
			.append("   Vegetation :  "+form.getExpRegionBVeg()+" \n")
			.append("   Floristics :  "+form.getExpRegionBFlor()+" \n")
			.append("   US-NVC/IVC :  "+form.getExpRegionBNVC()+" \n");
		}
			
		if (regionC != null) {
			messageBody.append(" Region 3:  "+regionC+" \n")
			.append("   Vegetation :  "+form.getExpRegionCVeg()+" \n")
			.append("   Floristics :  "+form.getExpRegionCFlor()+" \n")
			.append("   US-NVC/IVC :  "+form.getExpRegionCNVC()+" \n");
		}

		messageBody.append("\n ESA Sponsor #1 \n")
			.append("   Name :  "+form.getEsaSponsorNameA()+" \n")
			.append("   Email:  "+form.getEsaSponsorEmailA()+" \n")
			.append(" ESA Sponsor #2 \n")
			.append("   Name :  "+form.getEsaSponsorNameB()+" \n")
			.append("   Email:  "+form.getEsaSponsorEmailB()+" \n\n");

		tmp = form.getPeerReview();
		tmp = ((tmp != null && !tmp.equals("")) ? "YES" : "NO");
		messageBody.append(" Peer review interest?:  "+tmp+" \n\n");

		tmp = form.getAddlStmt();
		if (tmp != null && !tmp.equals("")) messageBody.append(" Additional statement:   "+tmp+" \n");

		messageBody.append(" \n\n")
			.append(" THANK YOU\n")
			.append(" \n\n\n");

		String mailHost = "vegbank.org";
		String from = "vegbank";
		String to = "panel@vegbank.org";
		String cc = null;
		String subject = "VegBank certification for " + form.getGivenName() + " " + form.getSurName();
		String body = messageBody.toString();
		ServletUtility.sendPlainTextEmail(mailHost, from, to, cc, subject, body);

	}

	/**
	 * Populates user info.
	 */
	private CertificationForm getCertForm(ActionForm form, WebUser user) {
		CertificationForm certForm = (CertificationForm)form;
	
		int iTmp = user.getUserid();
		if (iTmp != 0)  certForm.setUsrId(iTmp);

		String tmp = user.getGivenname();
		if (tmp != null && !tmp.equals("") && !tmp.equals("null")) {  
			certForm.setGivenName(tmp);
		}
		tmp = user.getSurname();
		if (tmp != null && !tmp.equals("") && !tmp.equals("null")) {  
			certForm.setSurName(tmp);
		}

		tmp = user.getDayphone();
		if (tmp != null && !tmp.equals("") && !tmp.equals("null")) {
			LogUtility.log("CertSave: setting phone="+tmp);
			certForm.setPhoneNumber(tmp);
		} else {
			LogUtility.log("CertSave: setting phone=n/a");
			certForm.setPhoneNumber("n/a");
		}

		tmp = user.getEmail();
		if (tmp != null && !tmp.equals("") && !tmp.equals("null")) {
			certForm.setEmailAddress(tmp);
		}

		if (certForm.getCurrentOrg() == null) {
			// user did not enter current org
			// so try saved org name
			tmp = user.getOrganizationname();
			if (tmp == null || tmp.equals("")) {
				// then try institution
				tmp = user.getInstitution();
				if (tmp != null && !tmp.equals(""))  certForm.setCurrentOrg(tmp);
				else certForm.setCurrentOrg("n/a");
			} else {
				certForm.setCurrentOrg(tmp);
			}
		}
		
		// set current certification level
		if (PermComparison.matchesOne("pro", user.getPermissiontype())) {
			certForm.setCurrentCertLevel(PermComparison.getRoleConstant("pro"));
		} else {
			certForm.setCurrentCertLevel(PermComparison.getRoleConstant("cert"));
		}

		
		return certForm;
	}

}
