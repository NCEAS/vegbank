/*
 * Created on May 2, 2003
 *
 *	'$RCSfile: AddPartyAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-10 00:33:27 $'
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

			if (!Utility.isStringNullOrEmpty(phoneNumber)
				&& !Utility.isStringNullOrEmpty(phoneType))
			{
				Telephone tel = new Telephone();
				tel.setPhoneNumber(phoneNumber);
				tel.setPhoneType(phoneType);
				party.addPARTYTelephone(tel);
			}
		}

		// handle the Address subform
		String[] organizationIds = partyForm.getOrganization_ID();
		String[] orgPositions = partyForm.getOrgPosition();
		String[] emails = partyForm.getEmail();
		String[] deliveryPoints = partyForm.getDeliveryPoint();
		String[] cities = partyForm.getCity();
		String[] areas = partyForm.getAdministrativeArea();
		String[] postalCodes = partyForm.getPostalCode();
		String[] countries = partyForm.getCountry();
		String[] startDates = partyForm.getStartDate();
		
		//boolean[] currentAddresses = partyForm.getCurrentAddress();
		
		for (int i = 0; i < organizationIds.length; i++)
		{
			String organizationId = organizationIds[i];
			String orgPosition = orgPositions[i];
			String email = emails[i];
			String deliveryPoint = deliveryPoints[i];
			String city = cities[i];
			String area = areas[i];
			String postalCode = postalCodes[i];
			String country = countries[i];
			String startDate = startDates[i];

			Address address = new Address();

			String[] allAddressFields =
				{
					orgPosition,
					email,
					deliveryPoint,
					city,
					area,
					postalCode,
					country};
					
			// Ignore if all fields are empty
			if ( Utility.isAnyStringNotNullorEmpty(allAddressFields))
			{
				System.out.println("Adding an Address");
				// This will blow up if a non integer is passed -- trusting on previous validation
				if (!Utility.isStringNullOrEmpty(organizationIds[i]))
				{
					address.setOrganization_ID(
						new Integer(organizationIds[i]).intValue());
				}
				address.setOrgPosition(orgPositions[i]);
				address.setEmail(emails[i]);
				address.setDeliveryPoint(deliveryPoints[i]);
				address.setCity(cities[i]);
				address.setAdministrativeArea(areas[i]);
				address.setPostalCode(postalCodes[i]);
				address.setCountry(countries[i]);
				address.setAddressStartDate(startDates[i]);
				//address.setCurrentFlag( new Boolean(currentAddresses[i]).toString() );

				party.addorganizationAddress(address);
			}
		}

		// Write to the db
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

		// Report any errors we have discovered back to the original form
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}

		return mapping.findForward("success");
	}

}
