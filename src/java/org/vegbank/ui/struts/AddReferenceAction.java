/*
 * Created on May 2, 2003
 *
 *	'$RCSfile: AddReferenceAction.java,v $'
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

import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vegbank.common.model.Reference;
import org.vegbank.common.model.ReferenceAltIdent;
import org.vegbank.common.model.ReferenceContributor;
import org.vegbank.common.model.ReferenceParty;
import org.vegbank.common.utility.ObjectToDB;
import org.vegbank.common.utility.Utility;

/**
 * @author gabriel
 * 
 * Action for adding Reference and its dependant objects ReferenceContributor,
 * ReferenceAltIdentifier and ReferenceParty to Vegbank.
 *
 */
public class AddReferenceAction extends Action
{

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In action ");
		ActionErrors errors = new ActionErrors();

		AddReferenceForm refForm = (AddReferenceForm) form;

		Reference ref = refForm.getReference();
		Vector refContribs = new Vector();
		Vector refParties = new Vector();

		// handle the ReferenceAltIdent sub form
		processReferenceAltIdents(errors, refForm, ref);

		// handle the ReferenceContributor & ReferenceParty sub form
		processReferencePartyandContributor(
			errors,
			refForm,
			refContribs,
			refParties);

		
		// If no errors then we can write to database
		if (errors.isEmpty())
		{
			// Write the RefernceParty to Database and finish populating the ReferenceContributor
			for (int i = 0; i < refContribs.size(); i++)
			{
				ReferenceParty rp = (ReferenceParty) refParties.elementAt(i);
				ReferenceContributor rc =
					(ReferenceContributor) refContribs.elementAt(i);

				// Must add rp to database to get pk
				ObjectToDB rp2db = new ObjectToDB(rp);
				try
				{
					rp2db.write();
				}
				catch (Exception e)
				{
					errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.database", e.getMessage()));
					System.out.println(
						"AddReferenceAction > Added an error: "
							+ e.getMessage());
					e.printStackTrace();
				}

				int partyId = rp2db.getPrimaryKey();

				rc.setReferenceParty_ID(partyId);

				// Add to the reference Object
				ref.addreferenceReferenceContributor(rc);
			}
		}

		if (errors.isEmpty())
		{
			// Write the reference to the database
			ObjectToDB ref2db = new ObjectToDB(ref);
			try
			{
				ref2db.write();
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

	private void processReferencePartyandContributor(
		ActionErrors errors,
		AddReferenceForm refForm,
		Vector refContribs,
		Vector refParties)
	{
		String[] parties = refForm.getParty();
		String[] roleTypes = refForm.getRoletype();
		
		for (int i = 0; i < parties.length; i++)
		{
			String party = parties[i];
			String roleType = roleTypes[i];
		
			// Only write to db if not part is not blank or null
			if (!Utility.isStringNullOrEmpty(party))
			{
				refParties.add(this.getReferenceParty(party));
		
				// Now I can fill in the linking object
				ReferenceContributor rc = new ReferenceContributor();
		
				// ignore the default -1
				if (!roleType.equals("-1"))
				{
					rc.setRoleType(roleType);
				}
		
				rc.setPosition(new Integer(i).toString());
				refContribs.add(rc);
			}
			else
			{
				// Enforce party as a required field if user filled out a sister field
				if (!roleType.equals("-1"))
				{
					// This is an error condition 
					errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.required.if",
							"referenceContributor.party",
							"referenceContributor.roleType is filled in"));
				}
			}
		}
	}

	private void processReferenceAltIdents(
		ActionErrors errors,
		AddReferenceForm refForm,
		Reference ref)
	{
		String[] systems = refForm.getIdentifier();
		String[] identifiers = refForm.getSystem();
		
		for (int i = 0; i < systems.length; i++)
		{
			String system = systems[i];
			String identifier = identifiers[i];
		
			if (!Utility.isStringNullOrEmpty(system)
				&& !Utility.isStringNullOrEmpty(identifier))
			{
				ReferenceAltIdent refAlt = new ReferenceAltIdent();
				refAlt.setIdentifier(identifier);
				refAlt.setSystem(system);
		
				ref.addreferenceReferenceAltIdent(refAlt);
			}
			else // enforce validation
				{
				// I only  filled one required field out error
				if (!Utility.isStringNullOrEmpty(system)
					|| !Utility.isStringNullOrEmpty(identifier))
				{
					// This is an error condition 
					errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.required.whenadding",
							"referenceAltIdentifier",
							"system and identifier"));
				}
			}
		
		}
	}

	/**
	 * Create a Reference party from a single string. Wow ?
	 * 
	 * We are hopping that the input conforms to the following standard.
	 * 	
	 * <pre>
	 * 	"Surname, GivenName blah blah blah"
	 * </pre>
	 * 
	 * Currently we only want the surname and givename.
	 * 
	 * @param party
	 * @return
	 */
	private ReferenceParty getReferenceParty(String party)
	{
		ReferenceParty rp = new ReferenceParty();
		String surName = "";
		String givenName = "";

		// parse the name into its parts 		
		StringTokenizer st = new StringTokenizer(party, ",");
		surName = st.nextToken();

		if (st.hasMoreTokens())
		{
			StringTokenizer st2 = new StringTokenizer(st.nextToken());
			givenName = st2.nextToken();
		}

		rp.setSurname(surName);
		rp.setGivenName(givenName);

		return rp;
	}

}
