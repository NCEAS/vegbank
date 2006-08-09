/*
 *	'$Id: UserProfileAction.java,v 1.8 2006-08-09 16:56:58 berkley Exp $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-08-09 16:56:58 $'
 *	'$Revision: 1.8 $'
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;

/**
 * Action responsible for viewing, editing and updating
 * a user profile.
 *
 * @author anderson
 */
public class UserProfileAction extends VegbankAction {
	
	static final Log log = LogFactory.getLog(UserProfileAction.class);

	static final String FWD_CHANGE_PWD = "change_pwd";
	static final String FWD_EDIT_USER = "edit_user";
  static final String FWD_DELETE_USER = "delete_user";
	static final String FWD_VIEW_USER = "view_user";
	static final String FWD_MAIN_MENU = "MainMenu";
  static final String FWD_EDIT_PERMISSION = "editPermission";
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
		
		log.debug("Focus,Action="+param+","+action);

		UserDatabaseAccess userdb;
		HttpSession session = request.getSession();
		WebUser curUser = null;
		String curUsrId = null;
		String reqUsrId = (String)request.getParameter("usrId");
		boolean isAdmin = userIsAdmin(session);

		// dig up the user's profile
		if (isAdmin && !Utility.isStringNullOrEmpty(reqUsrId)) {
			// Admin can edit other users with this action
			//curUser = getUser(session);
			//curUsrId = curUser.getUseridLong().toString();


			//if (!curUsrId.equals(reqUsrId) ) {
				// this is admin and given usrId is NOT admin's usrId
				curUser = getUser(Long.parseLong(reqUsrId));
			//} else {
				// error
				//log.error("ERROR: must be admin to edit other user: " + reqUsrId);
				//return mapping.findForward(RequestProcessor.CERTIFICATION_VIOLATION_FORWARD);
			//}
		} else {
			// this is a normal user
			curUser = getUser(session);
			curUsrId = curUser.getUseridLong().toString();
		}
		log.debug("curUsrId, reqUsrId: " + curUsrId +", "+reqUsrId);

		
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
					log.debug("edit");
					log.debug("putting webuser in request attribute 'webuser'");
					request.setAttribute("webuser", curUser); 

					log.debug("calling upForm.setWebUser ");
					upForm.setWebuser(curUser);
					log.debug("edit: putting upform in request attribute 'upform'");
					request.setAttribute("upform", upForm); 
					return mapping.findForward(FWD_EDIT_USER);
					
				} else if (action.equals("view")) {
					// VIEW
					log.debug("view");
					log.debug("putting webuser in request attribute 'webuser'");
					request.setAttribute("webuser", curUser); 

					log.debug("calling upForm.setWebUser ");
					upForm.setWebuser(curUser);
					log.debug("view: putting upform in request attribute 'upform'");
					request.setAttribute("upform", upForm); 
					return mapping.findForward(FWD_VIEW_USER);
		
				} else if (action.equals("update")) {
					// UPDATE
					log.debug("update");
					userdb = new UserDatabaseAccess();
					WebUser changedUser = upForm.getWebuser();
					String newEmail = changedUser.getEmail();

					if (!newEmail.equals(curUser.getEmail())) {
						log.debug("newEmail ("+newEmail+") != current ("+curUser.getEmail() +")");
						if (!userdb.isEmailUnique(newEmail)) {
							log.debug("newEmail is not unique");
							errors.add(Globals.ERROR_KEY, new ActionError(
										"errors.user.already.created", newEmail));
							saveErrors(request, errors);
							//session.removeAttribute("UserProfileForm");
							request.setAttribute("webuser", changedUser); 
							log.debug("update: putting upform in request attribute 'upform'");
							upForm.setWebuser(changedUser);
							request.setAttribute("upform", upForm); 
							return mapping.getInputForward();
						}
					}

					changedUser.setUserid(curUser.getUserid());
					changedUser.setPartyid(curUser.getPartyid());
					changedUser.setAddressid(curUser.getAddressid());
					userdb.updateUserInfo(changedUser);
									
					log.debug("done updating");
		
					session.removeAttribute("UserProfileForm");
					request.setAttribute("webuser", changedUser); 
					return mapping.findForward(FWD_SUCCESS);
				} else if (action.equals("delete")) {
          //delete the user
          log.debug("deleting user");
          UserDatabaseAccess uda = new UserDatabaseAccess();
          //nuke 'em
          uda.deleteUser(new Long(reqUsrId).longValue());
          
          return mapping.findForward(FWD_SUCCESS);
          
        } else if (action.equals("editPermission")) {
          request.setAttribute("webuser", curUser); 
					upForm.setWebuser(curUser);
					request.setAttribute("upform", upForm); 
					return mapping.findForward(FWD_EDIT_PERMISSION);
          
        } else if (action.equals("changePermission")) {
          //actually change the permission here
          userdb = new UserDatabaseAccess();
          String permType = (String)request.getParameter("permissiontype");
					
          WebUser changedUser = upForm.getWebuser();
          //curUser.setPermissiontype(new Long(permType).longValue());
          //upForm.setWebuser(curUser);
          
					//long newPerm = changedUser.getPermissiontype();
          userdb.setUserPermissionSum(new Long(reqUsrId).longValue(), new Long(permType).longValue());
          request.setAttribute("webuser", changedUser); 
					log.debug("calling upForm.setWebUser ");
					upForm.setWebuser(changedUser);
          log.debug("edit: putting upform in request attribute 'upform'");
					request.setAttribute("upform", upForm); 
          
          return mapping.findForward(FWD_SUCCESS);
        }// end if action


			} else if (param.equals("pwd")) {
			//// PASSWORD FUNCTIONS
				DynaActionForm dform = (DynaActionForm)form;
				String act2 = (String)dform.get("action");
				log.debug("act2=" + act2);
				if (act2 != null && act2.equals("Cancel")) {
					session.removeAttribute("ChangePasswordForm");
					return mapping.findForward(FWD_MAIN_MENU);
				}
		
				if (action.equals("edit")) {
					// CHANGE PASSWORD FORM
					log.debug("pwd");
					return mapping.findForward(FWD_CHANGE_PWD);
		
				} else if (action.equals("update")) {
					// UPDATE PASSWORD
					
					if (!isAdmin) {
						String str = (String)request.getParameter("password");
            UserDatabaseAccess uda = new UserDatabaseAccess();
            str = uda.getDigest(str, curUser.getEmail());
						
						if (str != null && !str.trim().equals(curUser.getPassword().trim())) {
							log.error("wrong pwd");
							errors.add(Globals.ERROR_KEY, new ActionError(
										"errors.password.mismatch"));
							saveErrors(request, errors);
							log.debug("returning to input (pwd form)");

							return mapping.getInputForward();
						}
					}
	
					// check retyped pwd
					if ( !((String)request.getParameter("newpassword1")).equals(
					(String)request.getParameter("newpassword2"))) {
						log.error("pwds don't match");
						errors.add(Globals.ERROR_KEY, new ActionError(
									"errors.password.wrong_retype"));
						saveErrors(request, errors);
						log.debug("returning to input (pwd form)");
						return mapping.getInputForward();
					}		

					log.debug("updating pwd");
					userdb = new UserDatabaseAccess();
					userdb.updatePassword((String)request.getParameter("newpassword1"),
							curUser.getEmail());

					session.removeAttribute("ChangePasswordForm");
					return mapping.findForward(FWD_SUCCESS);
				} // end if action
				
			} // end if param

		} catch (Exception ex) {
			log.error("ERROR: ", ex);
			errors.add(Globals.ERROR_KEY, new ActionError(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("vberror");
		}
		return mapping.findForward(FWD_FAILURE);
	}


}
