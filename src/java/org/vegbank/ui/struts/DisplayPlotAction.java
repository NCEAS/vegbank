package org.vegbank.ui.struts;
/*
 * '$RCSfile: DisplayPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-01-08 23:44:39 $'
 *	'$Revision: 1.7 $'
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
import org.vegbank.common.model.Plot;
import org.vegbank.common.utility.StopWatchUtil;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.XMLUtil;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.plots.datasink.ASCIIReportsHelper;
import org.vegbank.plots.datasource.DBModelBeanReader;
 
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
		LogUtility.log("DisplayPlotAction: begin");
		ActionErrors errors = new ActionErrors();
		String fwd = "success";

		// Get the form
		LogUtility.log("DisplayPlotAction: getting DynaActionForm");
		DynaActionForm thisForm = (DynaActionForm) form;
		
		LogUtility.log("DisplayPlotAction: getting accessionCode");
		String accessionCode = (String) thisForm.get("accessionCode");
		if (accessionCode == null) {
			// bacward compatibility
			accessionCode = (String) 
					thisForm.get("vegbankAccessionNumber");
		}
		
		LogUtility.log("DisplayPlotAction: accessionCode="+accessionCode);
		String resultType = (String) thisForm.get("resultType");
		LogUtility.log("DisplayPlotAction: Displaying as " + resultType);
		
		
		// Only support XML view for now
		if ( Utility.isStringNullOrEmpty(resultType) )
		{
			fwd = "failure";
			errors.add(
				Globals.ERROR_KEY,
				new ActionMessage("errors.required", "A resultType"));
		}
		// Got a resultType
		else 
		{
			resultType = resultType.toLowerCase();
			try
			{
				StopWatchUtil sw =
					new StopWatchUtil("Read an Observertion Object from the database");
				sw.startWatch();
				
				DBModelBeanReader mbReader = new DBModelBeanReader();
				Plot plot =
					mbReader.getPlotObservationBeanTree(accessionCode);
						
				sw.stopWatch();
				sw.printTimeElapsed();

				// rawXML			
				if ( resultType.equals("rawxml"))
				{
					response.setContentType("text/xml");
					response.getWriter().write( XMLUtil.getVBXML(plot) );
					// This is to prevent struts from grabbing the response away
					fwd = null;
				}
				// ascii Species Report
				else if (resultType.equals("asciispecies"))
				{
					LogUtility.log("DisplayPlotAction: getting asciispecies data");
					response.setContentType("text/plain");
					response.getWriter().write( ASCIIReportsHelper.getSpeciesData(plot) );
					// This is to prevent struts from grabbing the response away
					fwd = null;
				}
				// ascii Enviroment Report
				else if (resultType.equals("asciienvironment"))
				{
					response.setContentType("text/plain");
					response.getWriter().write( ASCIIReportsHelper.getEnvironmentalData(plot) );
					// This is to prevent struts from grabbing the response away
					fwd = null;
				}
				// Got an invalid resultType
				else
				{
					fwd = "failure";
					errors.add(
						Globals.ERROR_KEY,
						new ActionMessage(
							"errors.action.failed",
							"resultType - '" + resultType + "' is not supported"));
				}
			}
			catch (SQLException e)
			{
				fwd = "failure";
				errors.add(
					Globals.ERROR_KEY,
					new ActionMessage("errors.database", e.getMessage()));
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

		
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
		}
		return mapping.findForward(fwd);
	}
}

	
