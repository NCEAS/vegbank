package org.vegbank.ui.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.vegbank.common.model.Address;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Telephone;
import org.vegbank.common.model.Usr;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.Constants;
import org.vegbank.common.utility.PermComparison;

/*
 * '$RCSfile: RegisterNewUserAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-02-28 11:22:01 $'
 *	'$Revision: 1.7 $'
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
/**
 * <p>Struts <code>Action</code> to handle registration of new users.</p>
 * 
 * @author farrell
 *
 */
public class RegisterNewUserAction extends VegbankAction
{
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		LogUtility.log(" In RegisterNewUserAction ");
		ActionErrors errors = new ActionErrors();

		DynaActionForm dform = (DynaActionForm) form;

		// Get the properties of the form
		Party party = (Party) dform.get("party");
		Usr usr = (Usr) dform.get("usr");
		String termsaccept = (String) dform.get("termsaccept");
		String password1 = (String) dform.get("password1");
		String password2 = (String) dform.get("password2");

		try
		{
			UserDatabaseAccess uda = new UserDatabaseAccess();
			if (! uda.isEmailUnique(usr.getEmail_address()))
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(
						"errors.user.already.created",
						usr.getEmail_address()));
				LogUtility.log("RegisterNewUserAction: Username is taken.");
			}
			// Assert that the passwords match
			else if (!password1.equals(password2))
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(
						"errors.action.failed",
						"Password mismatch"));
				LogUtility.log("RegisterNewUserAction: Password mismatch");
			}
			else if (!termsaccept.equals("accept"))
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(
						"errors.action.failed",
						"Must accept vegbank terms."));
				LogUtility.log("RegisterNewUserAction: Terms not accepted");
			}
			else
			{
				// Fill out default fields
				// Start off as registered user 
				usr.setPermission_type(String.valueOf(
						PermComparison.getRoleConstant("reg")));

				usr.setBegin_time(Utility.getCurrentDate());

				// Already checked that they are identical
				usr.setPassword(password1);

				// add the usr to the party
				party.addparty_usr(usr);

				// Put in the database
				VBModelBeanToDB party2db = new VBModelBeanToDB();
				party2db.insert(party);

				// create a session
				long usrId = uda.getUserId(usr.getEmail_address());
				LogUtility.log("RegisterNewUserAction: logging in new user, ID #" + usrId);
				HttpSession session = request.getSession();
				session.setAttribute(Constants.USER_KEY, new Long(usrId));
			}
		}
		catch (Exception e)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.database", e.getMessage()));
			LogUtility.log("RegisterNewUserAction error: " + e.getMessage(), e);
			return mapping.findForward("vberror");
		}

		// Report any errors we have discovered back to the original form
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}

		return mapping.findForward("success");
	}

}
