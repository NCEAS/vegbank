/*
 * '$Id: project_Action.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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

///////////////////////////////////////////////////////////
//
//  project_Action.java
//  Created on Tue Apr 13 15:47:20 EDT 2004
//  By Auto FormBean,Action and JSP Builder 1.0
//
///////////////////////////////////////////////////////////



package org.vegbank.nvcrs.web;
import java.io.*;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;


public class project_Action extends org.apache.struts.action.Action
{

	public project_Action()
	{

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if(!SuperForm.class.isInstance(form))
			throw new Exception("Object is not a SuperForm.");
		HttpSession session = request.getSession();
		BeanManager manager=(BeanManager)session.getAttribute("beanManager");
		project_Form project_form=(project_Form)session.getAttribute("project_form");
		if(manager==null)
		{
			manager=new BeanManager();
			manager.getErrors().add("Please register or login first.");
			session.setAttribute("beanManager",manager);
			return (mapping.findForward("error"));
		}

		if(manager!=null)
		{
			manager.clearErrors();
			if(!manager.isRegistered())
			{
				manager.getErrors().add("Please register or login first.");
				return (mapping.findForward("error"));
			}
		}
		String msg=manager.getMessage();
		if(msg!=null)
			manager.setMessage("");
		ArrayList errors = manager.getErrors();
		errors.clear();

		if(project_form==null)
		{
			errors.add("Failed to find the FormBean:project_form");
			return (mapping.findForward("error"));
		}

		String strAct=request.getParameter("action");
		if(strAct==null)
			strAct="save";

		try
		{
			if(strAct.equals("cancel"))
				return (mapping.findForward("cancel"));
			else if(strAct.equals("save"))
				((SuperForm)form).updateRecord();
			else if (strAct.equals("add"))
			{
				proposal_Form proposal_form=(proposal_Form)session.getAttribute("proposal_form");

				if(proposal_form==null)
				{
					throw new Exception("No proposal form bean created.");
				}
				else
				{
					((SuperForm)form).addRecord();
					String id=((SuperForm)form).getFieldValue(((SuperForm)form).getPrimaryKey());
					proposal_form.setPROJECT_ID(id);
					proposal_form.updateRecord();
				}
			}
			else if (strAct.equals("delete"))
				((SuperForm)form).deleteRecord();
			else
				throw new Exception("Action not supported"+strAct);
		}
		catch(Exception e)
		{
			errors.add(e.getMessage());
		}
		
		if(!errors.isEmpty())
		{
			return (mapping.findForward("error"));
		}
		manager.setMessage("Action performed successfully: " + strAct);
		return (mapping.findForward("success"));
	}
}
