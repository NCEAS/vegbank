/*
 * Created on May 2, 2003
 *
 *	'$RCSfile: AddPartyAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 02:48:34 $'
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vegbank.common.model.Address;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Telephone;
import org.vegbank.common.utility.ObjectToDB;
import org.vegbank.common.utility.Utility;

/**
 * @author gabriel
 *
 *  Action for adding a party to Vegbank
 */
public class AddPartyAction extends Action
{

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In AddPartyAction ");
		ActionErrors errors = new ActionErrors();

		AddPartyForm partyForm = (AddPartyForm) form;

		Party party = partyForm.getParty();

		// handle the Phone sub form
		String[] phoneNumbers = partyForm.getPhoneNumber();
		String[] phoneTypes = partyForm.getPhoneType();

		for (int i = 0; i < phoneNumbers.length; i++)
		{
			String phoneNumber = phoneNumbers[i];
			String phoneType = phoneTypes[i];

			//System.out.println(phoneNumber + " and '" + phoneType + "'");
			if (!Utility.isStringNullOrEmpty(phoneNumber)
				&& !phoneType.equals("-1"))
			{
				Telephone tel = new Telephone();
				tel.setPhoneNumber(phoneNumber);
				tel.setPhoneType(phoneType);
				party.addPARTYTelephone(tel);
			}
			else
			{
				// Check for errors
				if (!Utility.isStringNullOrEmpty(phoneNumber)
					|| !phoneType.equals("-1"))
				{
					errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.required.whenadding",
							"telephone",
							"phoneNumber and phoneType"));
				}
			}
		}

		// Get the Address and add it to the party
		Address address = partyForm.getAddress();
		address.setCurrentFlag("" + partyForm.isCurrentFlag());
		party.addorganizationAddress(address);

		// Write to the db if all clear
		if (errors.isEmpty())
		{
			ObjectToDB party2db = new ObjectToDB(party);
			try
			{
				party2db.write();
			}
			catch (Exception e)
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.database", e.getMessage()));
				System.out.println(
					"AddReferenceAction > Added an error: " + e.getMessage());
				e.printStackTrace();
			}
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
