/*
 *	'$RCSfile: CertificationViewAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-06-09 22:47:07 $'
 *	'$Revision: 1.6 $'
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
		UserDatabaseAccess uda = new UserDatabaseAccess(); 
		CertificationForm adminCertForm = (CertificationForm)form; 
		CertificationForm dbCertForm = null;


		try {
			long certId = adminCertForm.getCertId();
			log.debug("loading cert #" + certId);

			// get the cert
			dbCertForm = uda.getCertificationApp(certId);


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

			// handle approval & declination
			// get action, if there is one
			String cmd = getDispatchCommand(request);
			if (Utility.isStringNullOrEmpty(cmd)) {
				///// //////////////////////////
				///// PRELOADING
				///// //////////////////////////

				usrId = dbCertForm.getUsrId();
				log.debug("Preloading certId: " + 
					dbCertForm.getRequestedCert() + ", " +
					dbCertForm.getCurrentCertLevelName() + ", " +
					dbCertForm.getUsrId());

				// set current certification level; use String for display ONLY
				long userPerms = uda.getUserPermissionSum(usrId);
				dbCertForm.setCurrentCertLevel(userPerms);

				log.debug("setting usr: " + usrId);
				dbCertForm.setUsrId(usrId);
				dbCertForm.setCertId(certId);

				// using <bean:write> requires this
				request.setAttribute("certBean", dbCertForm);

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
				usrId = dbCertForm.getUsrId();
				log.debug("got usrId: " + usrId);

				// get status comment
				String comment = adminCertForm.getCertificationstatuscomments();
				String newStatus = adminCertForm.getCertificationstatus();
				log.debug("Updating status: " + newStatus);


				if (newStatus.equals("approved")) {
					// handle approval
					long curRoles = uda.getUserPermissionSum(usrId);
					long reqRole = dbCertForm.getRequestedCert();
					long sum = curRoles | reqRole;

					// update applicant's permissions
					log.debug("Adding roles: (sum = cur | req): "  +
							sum + " = " + curRoles + " | " + reqRole);
					uda.setUserPermissionSum(usrId, sum);
					
					// send email message to the applicant 
					notifyApplicant(dbCertForm.getEmailAddress(), 
							dbCertForm.getGivenName() + " " + dbCertForm.getSurName(),
							dbCertForm.getRequestedCertName(), comment);

					log.debug("updating status of #" + certId + " to " + newStatus);
					// add and ActionMessage
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"messages.cert.updated", Long.toString(certId)));
					
					// add and ActionMessage
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"messages.cert.approval-email-sent", dbCertForm.getEmailAddress()));
					saveMessages(request, messages);
					///////////////////
					// Messages are not working.
					// Maybe because we're redirecting to the list
					///////////////////

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
			comment = "Thank you.";
		}
		tags.put("comment", comment);

		String from = "panel@vegbank.org";
		String to = emailAddress;
		String cc = "";
		String subject = "VegBank Certification APPROVED";

		log.debug("Sending approval mail to " + to);
		ServletUtility.sendEmailTemplate("admin/certification-approval.vm", 
				tags, from, to, cc, subject, true);
	}

}
