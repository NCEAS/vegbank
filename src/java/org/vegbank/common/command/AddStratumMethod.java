/*
 *	'$RCSfile: AddStratumMethod.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-29 00:24:54 $'
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
 
package org.vegbank.common.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.ValidatorResources;
import org.vegbank.common.model.StratumMethod;
import org.vegbank.common.model.StratumType;
import org.vegbank.common.utility.ObjectToDB;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class AddStratumMethod implements VegbankCommand
{

	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		System.out.println("AddStratumMethod command called");
		
		
		// Get the parameters out of the request
		String stratumMethodName = request.getParameter("stratumMethod.stratumMethodName");
		String stratumMethodDescription = request.getParameter("stratumMethod.stratumMethodDescription");
		String stratumMethodReference = request.getParameter("reference_ID");
		
		// I HATE the fact that there are many rows on the form !! It sucks and is ugly, %$@#!!!
		String[] stratumIndexes = (String[]) request.getParameterValues("stratumType.stratumIndex");
		String[] stratumNames = (String[]) request.getParameterValues("stratumType.stratumName");
		String[] stratumDescriptions = (String[]) request.getParameterValues("stratumType.stratumDescription");	

		
		// Validate the  input
		String[] stringsToCheck = {stratumMethodName, stratumMethodDescription};
		if ( Utility.isAnyStringNullorEmpty(stringsToCheck) )
		{
			throw new Exception("Please fill out the required fields");
		}
		
		
		// Construct the Objects
		StratumMethod sm = new StratumMethod();
		sm.setStratumMethodName(stratumMethodName);
		sm.setStratumMethodDescription(stratumMethodDescription);
		sm.setReference_ID( Integer.valueOf(stratumMethodReference).intValue() );
		
		//ObjectToDB o2db = new ObjectToDB(sm);
		//o2db.write();
		
		// Create the StratumType Objects 

		for (int i=0; i<stratumIndexes.length ; i++ )
		{
			String sIndex =stratumIndexes[i];
			String sName = stratumNames[i];
			String sDesc = stratumDescriptions[i];
			
			// Are the required fields here?
			if (  ! Utility.isStringNullOrEmpty(sIndex)  &&  ! Utility.isStringNullOrEmpty(sIndex) )
			{
				StratumType st = new StratumType();
			
				st.setStratumIndex(sIndex);
				st.setStratumName( sName);
				st.setStratumDescription(sDesc);
				sm.addSTRATUMMETHODStratumType(st);
			}
		}

		
		// Write the Objects
		ObjectToDB o2db = new ObjectToDB(sm);
		o2db.insert();

		request.setAttribute("message", "You have successfully added a StratumMethod");
		// Return the state of play		
		return "/forms/message.jsp";
	}

}
