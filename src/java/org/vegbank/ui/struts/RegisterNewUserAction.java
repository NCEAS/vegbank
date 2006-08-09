package org.vegbank.ui.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.model.Address;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Telephone;
import org.vegbank.common.model.Usr;
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
 *	'$Author: berkley $'
 *	'$Date: 2006-08-09 16:56:58 $'
 *	'$Revision: 1.9 $'
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
	private static Log log = LogFactory.getLog(RegisterNewUserAction.class);
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		log.debug(" In RegisterNewUserAction ");
		ActionErrors errors = new ActionErrors();

		DynaActionForm dform = (DynaActionForm) form;

		try {
			// Get the properties of the form
			Party party = (Party) dform.get("party");
			Usr usr = (Usr) dform.get("usr");
			String termsaccept = (String) dform.get("termsaccept");
			String password1 = (String) dform.get("password1");
			String password2 = (String) dform.get("password2");

			UserDatabaseAccess uda = new UserDatabaseAccess();
			if (! uda.isEmailUnique(usr.getEmail_address()))
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(
						"errors.user.already.created",
						usr.getEmail_address()));
				log.debug("Username is taken.");
			}
			// Assert that the passwords match
			else if (!password1.equals(password2))
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(
						"errors.action.failed",
						"Password mismatch"));
				log.debug("Password mismatch");
			}
			else if (!termsaccept.equals("accept"))
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(
						"errors.action.failed",
						"Must accept vegbank terms."));
				log.debug("Terms not accepted");
			}
			else
			{
				// Fill out default fields
				// Start off as registered user 
				usr.setPermission_type(Long.toString(
							PermComparison.getRoleConstant("reg")));

				usr.setBegin_time(Utility.getCurrentDate());

				// Already checked that they are identical
				usr.setPassword(password1);

				// add the usr to the party
				party.addparty_usr(usr);

				// Put in the database
				VBModelBeanToDB party2db = new VBModelBeanToDB();
				log.debug("inserting new usr");
				party2db.insert(party);

				// create a session
				long usrId = uda.getUserId(usr.getEmail_address());
				log.debug("logging in new user, ID #" + usrId);
				HttpSession session = request.getSession();
				session.setAttribute(Constants.USER_KEY, new Long(usrId));
        uda.updatePassword(password1, usr.getEmail_address());
			}
		} catch (Exception e) {
			errors.add(Globals.ERROR_KEY,
				new ActionError("errors.general", e.toString()));
			log.debug("error: " + e.toString(), e);
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
