/*
 *	'$RCSfile: GenericDispatcherAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-03 20:30:47 $'
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

/**
 * Allows the request to specific the command to run and the jsp page to dispatch too.
 * 
 * @author farrell
 */


import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.vegbank.common.utility.Utility;

public class GenericDispatcherAction extends Action
{
	
	private static final String commandLocation = "org.vegbank.common.command.";
	private static final String jspLocation = "/forms/";  
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		ActionErrors errors = new ActionErrors();
		String command = request.getParameter("command");
		String jsp = request.getParameter("jsp");
		
		try
		{
			Object commandObject = Utility.createObject(commandLocation + command);
			Class commandClass = commandObject.getClass();
			Class[] parameterTypes = null;
			//	{ Class.forName(" javax.servlet.http.HttpServletRequest"),  
			//		Class.forName("javax.servlet.http.HttpServletResponse"), };
			Method commandExecute = commandClass.getMethod("execute",parameterTypes);
			Object[] arguments = null;//{ request, response };
			Collection collection = (Collection) commandExecute.invoke(commandObject, arguments);
			
			request.setAttribute("genericBean",  collection);
			
			// Forward to a jsp
			RequestDispatcher dispatcher = request.getRequestDispatcher(jspLocation + jsp);
			dispatcher.forward(request, response);
		}
		catch (Exception e)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.resource.not.found", e.getMessage()));
			e.printStackTrace();
		}
		
		// Report any errors we have discovered to failure page
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.findForward("failure"));
		}
		return null;	
	}
	
}
