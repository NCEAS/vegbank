package org.vegbank.ui.struts;
/*
 * '$RCSfile: DisplayPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-24 19:26:40 $'
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
 
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import org.vegbank.common.model.Observation;
import org.vegbank.common.utility.Utility;
import org.vegbank.plots.datasource.DBObservationReader;
 
/**
 * @author farrell
 *
 * Struts action to Display a view of a Plot
 */
public class DisplayPlotAction extends Action
{
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In DisplayPlotAction ");
		ActionErrors errors = new ActionErrors();
		String fwd = "success";

		// Get the form
		DynaActionForm thisForm = (DynaActionForm) form;
		
		String plotId = (String ) thisForm.get("plotId");
		String resultType = (String) thisForm.get("resultType");
		//String accessionCode = (String)dpform.get("plotId")
		
		
		// Only support XML view for now
		if ( Utility.isStringNullOrEmpty(resultType) )
		{
			fwd = "failure";
			errors.add(
				Globals.ERROR_KEY,
				new ActionMessage("errors.required", "A resultType"));
		}
		else if ( resultType.equalsIgnoreCase("rawXML"))
		{
			try
			{
				DBObservationReader dbor = new DBObservationReader();
				Observation obs =
					dbor.getObservation(
						"observation_id",
						((Integer) new Integer(plotId)).intValue());
						
				response.setContentType("text/xml");
				response.getWriter().print( obs.toXML());
			}
			catch (SQLException e)
			{
				fwd = "failure";
				errors.add(
					Globals.ERROR_KEY,
					new ActionMessage("errors.database", e.getMessage()));
				e.printStackTrace();
			} 
			catch (NumberFormatException e)
			{
				fwd = "failure";
				errors.add(
					Globals.ERROR_KEY,
					new ActionMessage(
						"errors.action.failed",
						"plotId was not a number: " + plotId));
				e.printStackTrace();
			} 
			catch (Exception e)
			{
				fwd = "failure";
				errors.add(
					Globals.ERROR_KEY,
					new ActionMessage("errors.action.failed",e.getMessage()));
				e.printStackTrace();
			}
			
		}
		else
		{
			fwd = "failure";
			errors.add(
				Globals.ERROR_KEY,
				new ActionMessage(
					"errors.action.failed",
					"resultType - '" + resultType + "' is not supported"));
		}
		
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
		}
		return mapping.findForward(fwd);
	}
}

	
