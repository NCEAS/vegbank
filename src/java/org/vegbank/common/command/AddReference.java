/*
 *	'$RCSfile: AddReference.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 02:46:58 $'
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
 
package org.vegbank.common.command;

import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResults;
import org.vegbank.common.model.Reference;
import org.vegbank.common.model.ReferenceAltIdent;
import org.vegbank.common.model.ReferenceContributor;
import org.vegbank.common.model.ReferenceParty;
import org.vegbank.common.utility.ObjectToDB;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class AddReference implements VegbankCommand
{


	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public String execute(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		Reference ref = new Reference();
		// Get the parameters for httprequest and populate reference Object
		ref.setShortName( request.getParameter("reference.shortname") );
		ref.setReferenceType( request.getParameter("reference.referencetype") );
		ref.setTitle( request.getParameter("reference.title") );
		ref.setTitleSuperior(request.getParameter("reference.titlesuperior") );
		ref.setPubDate( request.getParameter("reference.pubdate") );
		ref.setAccessDate( request.getParameter("reference.accessdate") );
		ref.setConferenceDate(request.getParameter("reference.conferencedate") );
		
		
		ref.setReferenceJournal_ID( 
			new Integer(request.getParameter("referencejournal.journal_id")).intValue() );
		
		ref.setVolume( request.getParameter("reference.volume") );
		ref.setIssue( request.getParameter("reference.issue") );
		ref.setPageRange( request.getParameter("reference.pagerange") );
		ref.setTotalPages( request.getParameter("reference.totalpages") );
		ref.setPublisher( request.getParameter("reference.publisher") );
		ref.setPublicationPlace( request.getParameter("reference.publicationplace") );
		ref.setIsbn( request.getParameter("reference.isbn") );
		ref.setEdition( request.getParameter("reference.edition") );		
		ref.setNumberOfVolumes( request.getParameter("reference.numberofvolumes") );
		ref.setChapterNumber( request.getParameter("reference.chapternumber") );		
		ref.setReportNumber( request.getParameter("reference.reportnumber") );
		ref.setCommunicationType( request.getParameter("reference.communicationtype") );
		ref.setDegree( request.getParameter("reference.degree") );
		ref.setUrl( request.getParameter("reference.url") );
		ref.setDoi( request.getParameter("reference.doi") );
		ref.setAdditionalInfo( request.getParameter("reference.additionalinfo") );
		
		
		// handle the ReferenceAltIdent sub form
		String[] systems = request.getParameterValues("referencealtident.system");
		String[] identifiers = request.getParameterValues("referencealtident.identifier");
		
		for (int i=0; i<systems.length ; i++ )
		{
			String system = systems[i];
			String identifier = identifiers[i];
			
			if ( ! Utility.isStringNullOrEmpty(system)  &&  ! Utility.isStringNullOrEmpty(identifier) )
			{
				ReferenceAltIdent refAlt = new ReferenceAltIdent();		
				refAlt.setIdentifier(identifier);
				refAlt.setSystem(system);
				
				ref.addreferenceReferenceAltIdent(refAlt);
			}
		}
		
		
		// handle the ReferenceContributor & ReferenceParty sub form
		String[] parties = request.getParameterValues("referencecontributor.party");
		String[] roleTypes = request.getParameterValues("referencecontributor.roletype");		

		for (int i=0; i<parties.length; i++)
		{
			String party = parties[i];
			String roleType = roleTypes[i];
			
			if ( ! Utility.isStringNullOrEmpty(party))
			{			
				ReferenceParty rp =  this.getReferenceParty(party);
				// Must add rp to database to get pk
				ObjectToDB o2db = new ObjectToDB(rp);
				o2db.write();
				int partyId = o2db.getPrimaryKey();
				
				// Now I can fill in the linking object
				ReferenceContributor rc = new ReferenceContributor();
				rc.setRoleType(roleType);
				rc.setReferenceParty_ID(partyId);
				
				ref.addreferenceReferenceContributor(rc);
			}
		}
		
		// Write the reference to the database
		ObjectToDB o2db = new ObjectToDB(ref);
		boolean success = o2db.write();

		// Return the state of play		
		if (success)
		{
			request.setAttribute("message", "You have successfully added a Reference");
			return "/forms/message.jsp";
		}
		else 
		{
			request.setAttribute("message", "You have not added a Reference");
			return "/forms/message.jsp";
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

		// parse the name into its parts 		
		StringTokenizer st = new StringTokenizer(party, ",");
		String surName = st.nextToken();
		
		StringTokenizer st2 = new StringTokenizer(st.nextToken());
		String givenName = st2.nextToken();
		
		rp.setSurname(surName);
		rp.setGivenName(givenName);
		
		return rp;
	}
	
	private boolean validate(Reference ref,  ValidatorResources validatorResources) throws ValidatorException
	{
		System.out.println("Im in the  validator");

		Validator validator = new Validator(validatorResources, "AddReference");
		//	Tell the validator which bean to validate against.  
		validator.addResource(Validator.BEAN_KEY, ref);
		//	Validate the checkoutForm object and store the validation results
		ValidatorResults results = validator.validate();
		if ( results.empty() )
		{
			System.out.println("======== no results from validator using " + validatorResources.getValidatorActions() + " " + ref.getShortName() + "<---");
		}
		Iterator i = results.get();
		while (i.hasNext())
		{
			System.out.println("======== " + i.next() );
		}
		
		return true;
	}


}
