package org.vegbank.ui.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/*
 * '$RCSfile: RegisterNewUserAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-25 20:04:16 $'
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
/**
 * <p>Struts <code>Action</code> to handle registration of new users.</p>
 * 
 * @author farrell
 *
 */
public class RegisterNewUserAction extends Action
{
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In RegisterNewUserAction ");
		ActionErrors errors = new ActionErrors();

		DynaActionForm thisForm = (DynaActionForm) form;

		// Get the properties of the form
		Party party = (Party) thisForm.get("party");
		Usr user = (Usr) thisForm.get("user");
		Address address = (Address) thisForm.get("address");
		Telephone telephone = (Telephone) thisForm.get("telephone");
		String termsaccept = (String) thisForm.get("termsaccept");
		String password1 = (String) thisForm.get("password1");
		String password2 = (String) thisForm.get("password2");

		try
		{
			UserDatabaseAccess udba = new UserDatabaseAccess();
			if (! udba.isEmailUnique(user.getEmail_address()))
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(
						"errors.user.already.created",
						user.getEmail_address()));
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
				// Fill out defaut fields
				// Start of as registered user TODO: Get these as Constants
				user.setPermission_type("1");
				// FIXME: Should be set upon first logon?
				user.setBegin_time(Utility.getCurrentDate());

				// Already check that they are identical
				user.setPassword(password1);

				// Get as a constant
				telephone.setPhonetype(Telephone.PHONETYPE_WORK);

				party.addparty_address(address);
				party.addparty_telephone(telephone);
				party.addparty_usr(user);

				// Put in the database

				VBModelBeanToDB party2db = new VBModelBeanToDB();
				party2db.insert(party);
			}
		}
		catch (Exception e)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.database", e.getMessage()));
			LogUtility.log("RegisterNewUserAction: " + e.getMessage(), e);
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
