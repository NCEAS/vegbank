/*
 *	'$RCSfile: CertificationLoadAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-01-31 01:29:25 $'
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


		int iTmp = user.getUserid();
		if (iTmp != 0)  certForm.setUsrId(iTmp);

		String tmp = user.getGivenname();
		if (tmp != null)  certForm.setGivenName(tmp);
		tmp = user.getSurname();
		if (tmp != null)  certForm.setSurName(tmp);
		tmp = user.getDayphone();
		if (tmp != null)  certForm.setPhoneNumber(tmp);
		tmp = user.getEmail();
		if (tmp != null)  certForm.setEmailAddress(tmp);

		if (certForm.getCurrentOrg() == null) {
			if (user.getOrganizationname() == null) {
				tmp = user.getInstitution();
				if (tmp != null)  certForm.setCurrentOrg(tmp);
			} else {
				certForm.setCurrentOrg(user.getOrganizationname());
			}
		}

		// set current certification level
		// ONLY AS STRINGS for display
		// but note that the Save action uses ints
		if (PermComparison.matchesOne("pro", user.getPermissiontype())) {
			certForm.setCurrentCertLevelName("professional");
		} else {
			certForm.setCurrentCertLevelName("certified");
		}
	
		
		// using <bean:write> requires this
		request.setAttribute("reqAttribBean", certForm);

		LogUtility.log("Leaving CertificationLoadAction");
		return mapping.findForward("success");
	}

}
