/*
 *	'$RCSfile: AddStratumMethodAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $' 
 *	'$Date: 2003-05-29 00:24:54 $'
 *	'$Revision: 1.3 $'
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
import org.vegbank.common.model.StratumMethod;
import org.vegbank.common.model.StratumType;
import org.vegbank.common.utility.ObjectToDB;
import org.vegbank.common.utility.Utility;


/**
 * @author farrell
 */

public class AddStratumMethodAction extends Action
{
	

	public ActionForward execute( 
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In action AddStratumMethodAction");
		
		ActionErrors errors = new ActionErrors();		

		// Get the form
		AddStratumMethodForm stratumForm = (AddStratumMethodForm) form;
		
		StratumMethod sm = stratumForm.getStratumMethod();
				
		
		// handle the StratumType sub form
		String[] stratumIndex = stratumForm.getStratumIndex();
		String[] stratumName = stratumForm.getStratumName();
		String[] stratumDescription = stratumForm.getStratumDescription();
	
			
		for (int i=0; i<stratumIndex.length; i++)
		{
			// stratumIndex and stratumName must exist
			String[] stringstoCheck = {stratumIndex[i], stratumName[i]};
			if ( ! Utility.isAnyStringNullorEmpty( stringstoCheck ) )
			{			
				StratumType st =  new StratumType();
				
				st.setStratumDescription(stratumDescription[i]);
				st.setStratumIndex(stratumIndex[i]);
				st.setStratumName(stratumName[i]);
				
				sm.addSTRATUMMETHODStratumType(st);
			}
      else 
      {
        // Check for errors
        String[] stringsToCheck = {stratumIndex[i], stratumName[i], stratumDescription[i] };
        if ( Utility.isAnyStringNotNullorEmpty(stringsToCheck) )
        {
          errors.add(ActionErrors.GLOBAL_ERROR ,
            new ActionError("errors.required.whenadding", "stratumType", "stratumName and stratumIndex") 
          );
        }
      }
		}
		
		// Write this sucker to the database if all clear        
    if (errors.isEmpty())
    {
		  ObjectToDB sm2db = new ObjectToDB(sm);
  		try
	  	{
		  	sm2db.insert();
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
