/*
 *	'$RCSfile: GenericDispatcherAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-02-27 21:39:57 $'
 *	'$Revision: 1.8 $'
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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import org.vegbank.common.Constants;
import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.command.RetrieveVBModelBean;
import org.vegbank.common.model.VBModelBean;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;

public class GenericDispatcherAction extends Action
{
	// TODO: Make these properties
	private static final String genericCommandName = "GenericCommand";
	private static final String commandLocation = "org.vegbank.common.command.";
	private static final String jspLocation = "/forms/";
	private static final String ROOT_ENTITY = "rootEntity";  
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		LogUtility.log("GenericDispatcherAction: begin");
		DynaActionForm dynaForm = (DynaActionForm) form; 
		
		ActionErrors errors = new ActionErrors();
		String command = request.getParameter("command");
		String jsp = request.getParameter("jsp");
		String expandEntity = request.getParameter("expandEntity");
		String contractEntity = request.getParameter("contractEntity");
		String accessionCode = (String)dynaForm.get("accessionCode");
		try
		{
			System.out.println( "GD: command == " + command );
			if ( command.equals(genericCommandName)) 
			{
				LogUtility.log( "GD: executing new gen cmd" );
				new GenericCommand().execute(request, response);
				LogUtility.log( "GD: done executing GC" );
			}
			else if ( command.equals("RetrieveVBModelBean")) 
			{
				LogUtility.log( "GD: executing new RetriveVBModelBean cmd" );
				VBModelBean bean = new RetrieveVBModelBean().execute(accessionCode);
				String rootEntity = VBObjectUtils.getUnQualifiedName( bean.getClass().getName() );
				LogUtility.log("Putting genericBean in request " + bean);
				request.setAttribute("genericBean",  bean);
				LogUtility.log( "GD: done executing RetriveVBModelBean" );
				
				//Testing out
				HttpSession session = request.getSession();
				
				ExpandStatusStore ess = 
					(ExpandStatusStore) session.getAttribute("DisplayProps");
				if ( ess == null )
				{	
					ess = new ExpandStatusStore();
				}
				
				if ( expandEntity != null )
				{
					LogUtility.log("Expand: " + expandEntity);
					ess.setNodeExpanded(expandEntity);
				}
				if ( contractEntity != null )
				{
					LogUtility.log("Expand: " + contractEntity);
					ess.setNodeContracted(contractEntity);
				}
				
				// put in  session
				session.setAttribute("DisplayProps", ess);
				LogUtility.log("Setting rootEntity = " + rootEntity);
				request.setAttribute(ROOT_ENTITY,rootEntity);
				request.setAttribute(Constants.ACCESSIONCODENAME,accessionCode);
			}
			else
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
			}
			
			// Forward to a jsp
			LogUtility.log( "GD: fwd to" + jspLocation + jsp );
			RequestDispatcher dispatcher = request.getRequestDispatcher(jspLocation + jsp);
			LogUtility.log( "GD: got dispatcher");
			LogUtility.log( "rootEntity = " + request.getAttribute(ROOT_ENTITY) );
			dispatcher.forward(request, response);
		}
		catch (Exception e)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.resource.not.found", e.getMessage()));
			LogUtility.log( e.getMessage(),e );
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
