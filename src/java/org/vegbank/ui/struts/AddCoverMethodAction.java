/*
 *	'$RCSfile: AddCoverMethodAction.java,v $'
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
import org.vegbank.common.model.CoverIndex;
import org.vegbank.common.model.CoverMethod;
import org.vegbank.common.utility.ObjectToDB;
import org.vegbank.common.utility.Utility;


/**
 * @author farrell
 */

public class AddCoverMethodAction extends Action
{
	

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In action AddCoverMethodAction");
		
		ActionErrors errors = new ActionErrors();		
		int coverMethodPK = 0;
		
		// Get the form
		AddCoverMethodForm coverForm = (AddCoverMethodForm) form;
		
		CoverMethod cm = coverForm.getCoverMethod();
		
		// Write this sucker to the database
		ObjectToDB cm2db = new ObjectToDB(cm);
		try
		{
			cm2db.write();
			coverMethodPK = cm2db.getPrimaryKey();
		}
		catch (Exception e)
		{
			errors.add(ActionErrors.GLOBAL_ERROR , 
												new ActionError("errors.database", e.getMessage()) );
			System.out.println("AddJournalAction > Added an error: " + e.getMessage() );
			e.printStackTrace();
		}		
		
		
		// handle the CoverIndex sub form
		String[] coverCode = coverForm.getCoverCode();
		String[] coverPercent = coverForm.getCoverPercent();	
		String[] lowerLimit = coverForm.getLowerLimit();	
		String[] upperLimit = coverForm.getUpperLimit();			
		String[] indexDescription = coverForm.getIndexDescription();	
			
		for (int i=0; i<coverCode.length; i++)
		{
			// coverCode and coverPercent must exist
			String[] stringstoCheck = {coverCode[i], coverPercent[i]};
			if ( ! Utility.isAnyStringNullorEmpty( stringstoCheck ) )
			{			
				CoverIndex ci =  new CoverIndex();
				
				System.out.println(coverCode[i] + " and " + indexDescription[i] );
				
				ci.setCoverCode(coverCode[i]);
				ci.setCoverPercent(coverPercent[i]);
				ci.setLowerLimit(lowerLimit[i]);
				ci.setUpperLimit(upperLimit[i]);
				ci.setIndexDescription(indexDescription[i]);
				ci.setCOVERMETHOD_ID(coverMethodPK);
				
				//  add ci to database to get pk
				ObjectToDB ci2db = new ObjectToDB(ci);
				try
				{
					ci2db.write();
				}
				catch (Exception e)
				{
					errors.add(ActionErrors.GLOBAL_ERROR , 
														new ActionError("errors.database", e.getMessage()) );
					System.out.println("AddReferenceAction > Added an error: " + e.getMessage() );
					e.printStackTrace();
				}
			}
		}
		
		if (!errors.isEmpty()) 
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}
		
		return mapping.findForward("success");	
	}
}
