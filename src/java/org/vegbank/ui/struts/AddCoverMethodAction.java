/*
 *	'$RCSfile: AddCoverMethodAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-24 05:27:21 $'
 *	'$Revision: 1.5 $'
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
import org.vegbank.common.model.Coverindex;
import org.vegbank.common.model.Covermethod;
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
		
		Covermethod cm = coverForm.getCoverMethod();
		
	
		
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
				Coverindex ci =  new Coverindex();
				
				System.out.println(coverCode[i] + " and " + coverPercent[i] );
				
				ci.setCovercode(coverCode[i]);
				ci.setCoverpercent(coverPercent[i]);
				ci.setLowerlimit(lowerLimit[i]);
				ci.setUpperlimit(upperLimit[i]);
				ci.setIndexdescription(indexDescription[i]);
			  	cm.addcovermethod_coverindex(ci);
			}
      else
      {
        // Check for errors
        String[] stringsToCheck = {coverCode[i],coverPercent[i],lowerLimit[i],upperLimit[i],indexDescription[i]};
        // If any field is filled out we have a problem
        if (Utility.isAnyStringNotNullorEmpty(stringsToCheck) )
        {
          errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("errors.required.whenadding","coverIndex","coverCode and coverPercent")
          );
        }
      }
		}

		// Write this sucker to the database if all clear
    if (errors.isEmpty())
    {
  		ObjectToDB cm2db = new ObjectToDB(cm);
	  	try
		  {
			  cm2db.insert();
		  }
  		catch (Exception e)
	  	{
		  	errors.add(ActionErrors.GLOBAL_ERROR , 
			    new ActionError("errors.database", e.getMessage()) );
			  System.out.println("AddJournalAction > Added an error: " + e.getMessage() );
			  e.printStackTrace();
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
