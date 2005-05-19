package org.vegbank.ui.struts;
/*
 * '$RCSfile: DownloadManagerAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-05-19 01:27:47 $'
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
import org.vegbank.common.utility.LogUtility;

/**
 * @author farrell
 *
 * Struts action to take list of plots to download save to session and 
 * display a download options page.
 * 
 */
public class DownloadManagerAction extends Action
{
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		LogUtility.log(" In DownloadManagerAction ");
		ActionErrors errors = new ActionErrors();
		String fwd = "success";
		
		// Get the form
		DynaActionForm thisForm = (DynaActionForm) form;
		String[] observationAccessions = (String[]) thisForm.get("selectedPlots");
		for ( int i = 0 ; i < observationAccessions.length ; i++)
		{
			LogUtility.log("DownloadManagerAction: " + observationAccessions[i]);
		}
		
		if ( observationAccessions.length < 1 )
		{
			// Invalid Request --- At least on plot is required
			errors.add(
				Globals.ERROR_KEY,
				new ActionMessage(
					"errors.action.failed",
	 				"Plot(s) must be selected to download"));
		}
		else
		{
			// Save this array to request for use latter
			request.setAttribute("selectedPlots", observationAccessions );
		}
		
		if (! errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}
		

		
		return mapping.findForward(fwd);
	}
}
