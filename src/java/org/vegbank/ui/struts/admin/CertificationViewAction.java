/*
 *	'$RCSfile: CertificationViewAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-03-11 00:27:58 $'
 *	'$Revision: 1.1 $'
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

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.vegbank.ui.struts.CertificationForm;
import org.vegbank.ui.struts.VegbankAction;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.model.WebUser;


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

		// get the cert ID
		String cert = (String)dform.get("cert");
		if (cert == null || cert.equals("")) {

			// check the request next
			log.debug("CertificationViewAction: cert ID not in form; checking request...");
			if (cert == null || cert.equals("")) {
				cert = request.getParameter("cert");
				log.debug("CertificationViewAction: no given cert");
				errors.add(Globals.ERROR_KEY, new ActionMessage(
							"errors.required", "cert=usercertification_id"));
				saveErrors(request, errors);
				return mapping.findForward("failure");
			}
		}

		long usr = 0;
		UserDatabaseAccess uda = new UserDatabaseAccess(); 

		// handle approval & declination
		try {
			log.debug("CertificationViewAction: getting cert: " + cert);
			certForm = uda.getCertificationApp(Integer.parseInt(cert));

			// get action, if there is one
			String action = (String)dform.get("action");
			/////String action = request.getParameter("action");
			if (action == null || action.equals("")) {
				///// //////////////////////////
				///// PRELOADING
				///// //////////////////////////

				usr = certForm.getUsrId();
				log.debug("CertificationViewAction: Preloading cert: " + 
					certForm.getRequestedCert() + ", " +
					certForm.getCurrentCertLevelName() + ", " +
					certForm.getUsrId());

				// set current certification level; use String for display ONLY
				int userPerms = uda.getUserPermissionSum(usr);

				if (PermComparison.matchesOne("pro", userPerms)) {
					certForm.setCurrentCertLevelName("professional");
				} else if (PermComparison.matchesOne("certified", userPerms)) {
					certForm.setCurrentCertLevelName("certified");
				} else {
					certForm.setCurrentCertLevelName("registered");
				}

				log.debug("CertificationViewAction: setting usr: " + Long.toString(usr));
				// set the form et al. in dyna form
				//dform.set("certBean", certForm);
				dform.set("usr", Long.toString(usr));
				dform.set("cert", cert);

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

			} else if (action.equals("APPROVE")) {
				///// //////////////////////////
				///// ACTION
				///// //////////////////////////
				log.debug("CertificationViewAction: action = " + action);

				log.debug("CertificationViewAction: getting usr from dform");
				String tmp = (String)dform.get("usr");
				if (tmp == null || tmp.equals("")) {
					log.debug("CertificationViewAction: usr is empty!");
				}
				usr = Long.parseLong(tmp);
				/////usr = request.getParameter("usr");
				log.debug("CertificationViewAction: usr = " + usr);

				if (action.equals("APPROVE")) {
					// handle approval
					int curRoles = uda.getUserPermissionSum(usr);
					int reqRole = Integer.parseInt(certForm.getRequestedCert());
					int sum = curRoles | reqRole;

					log.debug("Adding roles: (sum = cur | req): "  +
							sum + " = " + curRoles + " | " + reqRole);

					uda.setUserPermissionSum(usr, sum);

					// send email -- how do i load a template?
					///// see CertSave
					
					log.debug("Leaving CertificationViewAction");
					return mapping.findForward("success");

				} else {
					// handle decline
				}

			} else {
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
		

		log.debug("Leaving CertificationViewAction");
		return mapping.findForward("view");
	}

}
