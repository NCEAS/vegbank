/*
 *	'$Id: UserProfileAction.java,v 1.2 2004-04-17 02:53:20 anderson Exp $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-17 02:53:20 $'
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
import javax.mail.internet.AddressException;
import javax.mail.MessagingException;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;

/**
 * Action responsible for viewing, editing and updating
 * a user profile.
 *
 * @author anderson
 */
public class UserProfileAction extends VegbankAction {
	
	static final String FWD_CHANGE_PWD = "change_pwd";
	static final String FWD_EDIT_USER = "edit_user";
	static final String FWD_VIEW_USER = "view_user";
	static final String FWD_MAIN_MENU = "MainMenu";
	static final String FWD_SUCCESS = "success";
	static final String FWD_FAILURE = "failure";


	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors errors = new ActionErrors();
		VegbankActionMapping vbMapping = (VegbankActionMapping)mapping;

		
		// param specifies which element (pwd, user)
		String param = vbMapping.getParameter();
		if (param == null || param.equals("")) { param = "user"; }

		// find the action in the mapping, then request
		String action = vbMapping.getAction();
		if (action == null || action.equals("")) {
			action = (String)request.getParameter("action");
			if (action == null || action.equals("")) { 
				// default case
				action = "edit"; 
			}
		}
		
		LogUtility.log("UserProfileAction: Focus,Action="+param+","+action);

		WebUser curUser = getUser(request.getSession());
		UserDatabaseAccess userdb;
		HttpSession session = request.getSession();
		
		try {
			if (param.equals("user")) {
			//// USER FUNCTIONS
				UserProfileForm upForm = (UserProfileForm)form;
				String act2 = upForm.getAction();
				if (act2 != null && act2.equals("Cancel")) {
					///////////////////////session.removeAttribute("UserProfileForm");
					return mapping.findForward(FWD_MAIN_MENU);
				}

				if (action.equals("edit")) {
					// EDIT by default
					LogUtility.log("UserProfileAction: edit");
					LogUtility.log("UserProfileAction: putting webuser in request attribute 'webuser'");
					//////////////request.setAttribute("webuser", curUser); 
					LogUtility.log("UserProfileAction: calling upForm.setWebUser ");
					upForm.setWebuser(curUser);
					return mapping.findForward(FWD_EDIT_USER);
					
				} else if (action.equals("view")) {
					// VIEW
					LogUtility.log("UserProfileAction: view");
					request.setAttribute("webuser", curUser); 
					return mapping.findForward(FWD_VIEW_USER);
		
				} else if (action.equals("update")) {
					// UPDATE
					LogUtility.log("UserProfileAction: update");
					userdb = new UserDatabaseAccess();
					WebUser changedUser = upForm.getWebuser();
					String newEmail = changedUser.getEmail();

					if (!newEmail.equals(curUser.getEmail())) {
						if (!userdb.isEmailUnique(newEmail)) {
							errors.add(Globals.ERROR_KEY, new ActionError(
										"errors.user.already.created", newEmail));
							saveErrors(request, errors);
							//session.removeAttribute("UserProfileForm");
							request.setAttribute("webuser", changedUser); 
							return mapping.getInputForward();
						}
					}

					changedUser.setUserid(curUser.getUserid());
					changedUser.setPartyid(curUser.getPartyid());
					changedUser.setAddressid(curUser.getAddressid());
					userdb.updateUserInfo(changedUser);
									
					LogUtility.log("UserProfileAction: done updating");
		
					session.removeAttribute("UserProfileForm");
					request.setAttribute("webuser", changedUser); 
					return mapping.findForward(FWD_SUCCESS);
				} // end if action


			} else if (param.equals("pwd")) {
			//// PASSWORD FUNCTIONS
				DynaActionForm dform = (DynaActionForm)form;
				String act2 = (String)dform.get("action");
				LogUtility.log("UserProfileAction: act2=" + act2);
				if (act2 != null && act2.equals("Cancel")) {
					session.removeAttribute("ChangePasswordForm");
					return mapping.findForward(FWD_MAIN_MENU);
				}
		
				if (action.equals("edit")) {
					// CHANGE PASSWORD FORM
					LogUtility.log("UserProfileAction: pwd");
					return mapping.findForward(FWD_CHANGE_PWD);
		
				} else if (action.equals("update")) {
					// UPDATE PASSWORD
					
					// make sure old pwd matches entry
					String str = (String)request.getParameter("password");
					LogUtility.log("UserProfileAction: your pwd is " + str);
					
					if ( str != null && !str.equals(
					curUser.getPassword())) {
						LogUtility.log("UserProfileAction: wrong pwd");
						errors.add(Globals.ERROR_KEY, new ActionError(
									"errors.password.mismatch"));
						saveErrors(request, errors);
						LogUtility.log("UserProfileAction: returning to input (pwd form)");

						return mapping.getInputForward();
					}
	
					// check retyped pwd
					if ( !((String)request.getParameter("newpassword1")).equals(
					(String)request.getParameter("newpassword2"))) {
						LogUtility.log("UserProfileAction: pwds don't match");
						errors.add(Globals.ERROR_KEY, new ActionError(
									"errors.password.wrong_retype"));
						saveErrors(request, errors);
						LogUtility.log("UserProfileAction: returning to input (pwd form)");
						return mapping.getInputForward();
					}		

					LogUtility.log("UserProfileAction: updating pwd");
					userdb = new UserDatabaseAccess();
					userdb.updatePassword((String)request.getParameter("newpassword1"),
							curUser.getEmail());

					session.removeAttribute("ChangePasswordForm");
					return mapping.findForward(FWD_SUCCESS);
				} // end if action
				
			} // end if param

		} catch (Exception ex) {
			LogUtility.log("ERROR: UserProfileAction", ex);
			errors.add(Globals.ERROR_KEY, new ActionError(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("vberror");
		}
		return mapping.findForward(FWD_FAILURE);
	}


}
