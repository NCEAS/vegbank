package org.vegbank.ui.struts;
/*
 * '$RCSfile: DisplayEntityAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-02 03:36:43 $'
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
import org.vegbank.common.model.VBModelBean;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.StopWatchUtil;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.XMLUtil;
import org.vegbank.plots.datasink.ASCIIReportsHelper;
import org.vegbank.plots.datasource.DBModelBeanReader;
 
/**
 * @author farrell
 *
 * Struts action to Display a view of a Plot
 */
public class DisplayEntityAction extends Action
{
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		LogUtility.log("DisplayEntityAction: In DisplayEntityAction ");
		ActionErrors errors = new ActionErrors();
		String fwd = "success";

		// Get the form
		DynaActionForm thisForm = (DynaActionForm) form;
		
		String accessionCode = (String ) thisForm.get("accessionCode");
		String resultType = (String) thisForm.get("resultType");
		//String accessionCode = (String)dpform.get("plotId")
		
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
			try
			{
				StopWatchUtil sw =
					new StopWatchUtil("Read an Object from the database");
				sw.startWatch();
				
				DBModelBeanReader mbReader = new DBModelBeanReader();
				VBModelBean bean =
					mbReader.getVBModelBean(accessionCode);
				mbReader.releaseConnection();
						
				sw.stopWatch();
				sw.printTimeElapsed();

				// rawXML			
				if ( resultType.equalsIgnoreCase("rawXML"))
				{
					response.setContentType("text/xml");
					response.getWriter().write( XMLUtil.getVBXML(bean) );
					// This is to prevent struts from grabbing the response away
					fwd = null;
				}
				// ascii Species Report
				else if (resultType.equalsIgnoreCase("ASCIISpecies"))
				{
					response.setContentType("text/plain");
					response.getWriter().write( ASCIIReportsHelper.getSpeciesData((Observation) bean) );
					// This is to prevent struts from grabbing the response away
					fwd = null;
				}
				// ascii Enviroment Report
				else if (resultType.equalsIgnoreCase("ASCIIEvironment"))
				{
					response.setContentType("text/plain");
					response.getWriter().write( ASCIIReportsHelper.getEnvironmentalData((Observation) bean) );
					// This is to prevent struts from grabbing the response away
					fwd = null;
				}
				else if ( resultType.equalsIgnoreCase("detail"))
				{
					// FIXME: Hard coded for observation for a starting point
					String jspDir = "DisplayEntity";
					String jspToUse = "DisplayPlot.jsp"; // Should be param/lookup?
					
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
				LogUtility.log("DisplayEntityAction: Error > " + e.getMessage(), e);
			} 
			catch (Exception e)
			{
				fwd = "failure";
				errors.add(
					Globals.ERROR_KEY,
					new ActionMessage("errors.action.failed",e.getMessage()));
				LogUtility.log("DisplayEntityAction: Error > " + e.getMessage(), e);
			}
		}

		
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
		}
		return mapping.findForward(fwd);
	}
}

	
