/*
 *	'$RCSfile: CertificationViewAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-15 02:08:05 $'
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

		// get Cert from DB
		DynaActionForm dform = (DynaActionForm)form;
		CertificationForm certForm = null;

		// get the certId
		String certId = (String)dform.get("certId");
		if (Utility.isStringNullOrEmpty(certId)) {
			// check the request next
			log.debug("CertificationViewAction: certId not in form; checking request...");
			if (certId == null || certId.equals("")) {
				certId = request.getParameter("certId");
				log.debug("CertificationViewAction: no given certId");
				errors.add(Globals.ERROR_KEY, new ActionMessage(
							"errors.required", "certId=usercertification_id"));
				saveErrors(request, errors);
				return mapping.findForward("failure");
			}
		}

		long usrId = 0;
		UserDatabaseAccess uda = new UserDatabaseAccess(); 

		// handle approval & declination
		try {
			log.debug("CertificationViewAction: getting certId: " + certId);
			certForm = uda.getCertificationApp(Integer.parseInt(certId));

			// get action, if there is one
			String action = (String)dform.get("action");
			/////String action = request.getParameter("action");
			if (Utility.isStringNullOrEmpty(action)) {
				///// //////////////////////////
				///// PRELOADING
				///// //////////////////////////

				usrId = certForm.getUsrId();
				log.debug("CertificationViewAction: Preloading certId: " + 
					certForm.getRequestedCert() + ", " +
					certForm.getCurrentCertLevelName() + ", " +
					certForm.getUsrId());

				// set current certification level; use String for display ONLY
				int userPerms = uda.getUserPermissionSum(usrId);

				if (PermComparison.matchesOne("pro", userPerms)) {
					certForm.setCurrentCertLevelName("professional");
				} else if (PermComparison.matchesOne("certified", userPerms)) {
					certForm.setCurrentCertLevelName("certified");
				} else {
					certForm.setCurrentCertLevelName("registered");
				}

				log.debug("CertificationViewAction: setting usr: " + Long.toString(usrId));
				// set the form et al. in dyna form
				//dform.set("certBean", certForm);
				dform.set("usrId", Long.toString(usrId));
				dform.set("certId", certId);

				// then set the form in the request
				request.setAttribute("dform", dform);


				// using <bean:write> requires this
				request.setAttribute("certBean", certForm);

				/*
				BasicDynaBean hash = new BasicDynaBean( 
						new BasicDynaClass() );
				hash.set("key1", "Value of key1");
				request.setAttribute("hash", hash);
				*/

				log.debug("Leaving CertificationViewAction");
				return mapping.findForward("view");

			} else {
				///// //////////////////////////
				///// ACTION
				///// //////////////////////////
				log.debug("CertificationViewAction: action = " + action);

				log.debug("CertificationViewAction: getting usr from dform");
				String tmp = (String)dform.get("usrId");
				if (tmp == null || tmp.equals("")) {
					log.debug("CertificationViewAction: usr is empty!");
				}
				usrId = Long.parseLong(tmp);
				/////usr = request.getParameter("usrId");
				log.debug("CertificationViewAction: usr = " + usrId);

				// get status comment
				String statusComment = (String)dform.get("statusComment");
				String certStatus;  // set this after getting action

				if (action.equals("APPROVE")) {
					// handle approval
					certStatus = "approved";
					int curRoles = uda.getUserPermissionSum(usrId);
					int reqRole = Integer.parseInt(certForm.getRequestedCert());
					int sum = curRoles | reqRole;

					log.debug("Adding roles: (sum = cur | req): "  +
							sum + " = " + curRoles + " | " + reqRole);

					uda.setUserPermissionSum(usrId, sum);
					

					// send email message to the user 
					Map tags = new HashMap();
					tags.put("applicantName", certForm.getGivenName() + " " + certForm.getSurName());
					tags.put("requestedRole", certForm.getRequestedCertName());
					if (!Utility.isStringNullOrEmpty(statusComment)) {
						tags.put("comment", statusComment);
					}

					String from = "panel@vegbank.org";
					String to = certForm.getEmailAddress();
					String cc = "";
					String subject = "VegBank Certification Response";

					ServletUtility.sendEmailTemplate("admin/certification-approval.vm", 
							tags, from, to, cc, subject, true);
					
				} else if (action.equals("REJECT")) {
					certStatus = "rejected";

				} else if (action.equals("DISCARD")) {
					certStatus = "discarded";

				} else {
					certStatus = "ERROR";
					log.error("unknown action");
				}

				// update the application
				log.debug("Updating status: " + certStatus);
				uda.updateCertificationStatus(Integer.parseInt(certId), certStatus, statusComment);


				log.debug("Leaving CertificationViewAction");
				return mapping.findForward("success");

			}

		
		} catch (Exception ex) {
			log.error("ERROR CertificationViewAction: FAILURE: " + ex.toString());
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("failure");
		}

	}

}
