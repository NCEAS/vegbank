/*
 *
 *	'$Id : $'
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-15 03:00:15 $'
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
import org.vegbank.common.model.Plant;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.utility.Utility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author anderson
 *
 *  Action for adding a plant to Vegbank
 */
public class AddPlantAction extends VegbankAction
{
	private static Log log = LogFactory.getLog(AddPlantAction.class);

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		log.debug(" In AddPlantAction ");
		ActionErrors errors = new ActionErrors();

		/*
		AddPlantForm plantForm = (AddPlantForm) form;

		Plant plant = plantForm.getPlant();

		// handle the Phone sub form
		String[] phoneNumbers = plantForm.getPhoneNumber();
		String[] phoneTypes = plantForm.getPhoneType();

		for (int i = 0; i < phoneNumbers.length; i++)
		{
			String phoneNumber = phoneNumbers[i];
			String phoneType = phoneTypes[i];

			//log.debug(phoneNumber + " and '" + phoneType + "'");
			if (!Utility.isStringNullOrEmpty(phoneNumber)
				&& !phoneType.equals("-1"))
			{
				Telephone tel = new Telephone();
				tel.setPhonenumber(phoneNumber);
				tel.setPhonetype(phoneType);
				plant.AddPlant_telephone(tel);
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

		// Get the Address and add it to the plant
		Address address = plantForm.getAddress();
		address.setCurrentflag("" + plantForm.isCurrentFlag());
		plant.AddPlant_address(address);

		// Write to the db if all clear
		if (errors.isEmpty())
		{
			try
			{
				VBModelBeanToDB plant2db = new VBModelBeanToDB();
				plant2db.insert(plant);
			}
			catch (Exception e)
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.database", e.getMessage()));
				log.debug(
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

		*/
		return mapping.findForward("success");
	}

}
