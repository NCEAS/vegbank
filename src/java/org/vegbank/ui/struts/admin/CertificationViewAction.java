/*
 *	'$RCSfile: CertificationViewAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-26 20:48:29 $'
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

package org.vegbank.ui.struts.admin;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import javax.mail.internet.AddressException;
import javax.mail.MessagingException;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.vegbank.ui.struts.CertificationForm;
import org.vegbank.ui.struts.VegbankAction;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.VelocityParser;
import org.vegbank.common.utility.Utility;


/**
 *
 * @author anderson
 */
public class CertificationViewAction extends VegbankAction {

	private static Log log = LogFactory.getLog(CertificationViewAction.class); 

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		log.debug("In action CertificationViewAction");
		ActionErrors errors = new ActionErrors();


		// get the certId from the form
		CertificationForm certForm = (CertificationForm)form; 
		long certId = certForm.getCertId();

		if (certId == 0) {
			// check the request next
			log.debug("certId not in form; checking request...");
			String strCertId = request.getParameter("certId");
			if (Utility.isStringNullOrEmpty(strCertId)) {
				log.debug("no given certId");
				errors.add(Globals.ERROR_KEY, new ActionMessage(
							"errors.required", "certId=usercertification_id"));
				saveErrors(request, errors);
				return mapping.findForward("failure");
			} else {
				certId = Long.parseLong(strCertId);
			}
		}

		long usrId = 0;
		UserDatabaseAccess uda = new UserDatabaseAccess(); 

		// handle approval & declination
		try {
			log.debug("getting certId: " + certId);
			certForm = uda.getCertificationApp(certId);

			// get action, if there is one
			String cmd = getDispatchCommand(request);
			if (Utility.isStringNullOrEmpty(cmd)) {
				///// //////////////////////////
				///// PRELOADING
				///// //////////////////////////

				usrId = certForm.getUsrId();
				log.debug("Preloading certId: " + 
					certForm.getRequestedCert() + ", " +
					certForm.getCurrentCertLevelName() + ", " +
					certForm.getUsrId());

				// set current certification level; use String for display ONLY
				long userPerms = uda.getUserPermissionSum(usrId);

				if (PermComparison.matchesOne("pro", userPerms)) {
					certForm.setCurrentCertLevelName("professional");
				} else if (PermComparison.matchesOne("certified", userPerms)) {
					certForm.setCurrentCertLevelName("certified");
				} else {
					certForm.setCurrentCertLevelName("registered");
				}

				log.debug("setting usr: " + usrId);
				certForm.setUsrId(usrId);
				certForm.setCertId(certId);

				// using <bean:write> requires this
				request.setAttribute("certBean", certForm);

				/*
				BasicDynaBean hash = new BasicDynaBean( 
						new BasicDynaClass() );
				hash.set("key1", "Value of key1");
				request.setAttribute("hash", hash);
				*/

				log.debug("Forwarding to 'view'");
				return mapping.findForward("view");

			} else if (cmd.equals("updateStatus")) {
				///// //////////////////////////
				///// ACTION
				///// //////////////////////////
				log.debug("Taking action: " + cmd);
				usrId = certForm.getUsrId();
				log.debug("got usrId: " + usrId);

				// get status comment
				String comment = certForm.getCertificationstatuscomments();
				String newStatus = certForm.getCertificationstatus();
				log.debug("Updating status: " + newStatus);

				if (newStatus.equals("approved")) {
					// handle approval
					long curRoles = uda.getUserPermissionSum(usrId);
					long reqRole = certForm.getRequestedCert();
					long sum = curRoles | reqRole;

					// update applicant's permissions
					log.debug("Adding roles: (sum = cur | req): "  +
							sum + " = " + curRoles + " | " + reqRole);
					uda.setUserPermissionSum(usrId, sum);
					
					// send email message to the applicant 
					/*
					notifyApplicant(certForm.getEmailAddress(), 
							certForm.getGivenName() + " " + certForm.getSurName(),
							certForm.getRequestedCertName(), comment);
					*/

					log.debug("updating status of #" + certId + " to " + newStatus);
					ActionMessages messages = new ActionMessages();
					messages.add("updated", new ActionMessage(
								"messages.cert.updated", Long.toString(certId)));
					saveMessages(request, messages);
					
				} else if (newStatus.equals("rejected")) {
					// might want to handle this in a special way, some day
					log.debug("Reject!");
				}

				// update the application
				uda.updateCertificationStatus(certId, newStatus, comment);

				log.debug("Forwarding to 'list'");
				return mapping.findForward("list");
			}

		
		} catch (Exception ex) {
			log.error("ERROR CertificationViewAction: FAILURE: " + ex.toString());
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("failure");
		}
		return mapping.findForward("failure");
	}

	/**
	 * Send an email to the applicant.
	 */
	private void notifyApplicant(String emailAddress, String applicantName, 
				String requestedRole, String comment) 
				throws Exception {

		Map tags = new HashMap();
		tags.put("applicantName", applicantName);
		tags.put("requestedRole", requestedRole);
		if (!Utility.isStringNullOrEmpty(comment)) {
			tags.put("comment", comment);
		}

		String from = "panel@vegbank.org";
		String to = emailAddress;
		String cc = "";
		String subject = "VegBank Certification Response";

		ServletUtility.sendEmailTemplate("admin/certification-approval.vm", 
				tags, from, to, cc, subject, true);
	}

}
