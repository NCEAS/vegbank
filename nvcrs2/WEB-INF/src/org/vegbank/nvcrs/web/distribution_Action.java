///////////////////////////////////////////////////////////
//
//  distribution_Action.java
//  Created on Tue Apr 13 15:47:19 EDT 2004
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


public class distribution_Action extends org.apache.struts.action.Action
{

	public distribution_Action()
	{

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if(!SuperForm.class.isInstance(form))
			throw new Exception("Object is not a SuperForm.");
		HttpSession session = request.getSession();
		BeanManager manager=(BeanManager)session.getAttribute("beanManager");
		distribution_Form distribution_form=(distribution_Form)session.getAttribute("distribution_form");
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

		if(distribution_form==null)
		{
			errors.add("Failed to find the FormBean:distribution_form");
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
				((SuperForm)form).addRecord();
			else if (strAct.equals("delete"))
				((SuperForm)form).deleteRecord();
			else
				throw new Exception("Action not supported"+strAct);
		}
		catch(Exception e)
		{
			errors.add(e.getMessage());
			return (mapping.findForward("error"));
		}

		manager.setMessage("Action performed successfully: " + strAct);
		return (mapping.findForward("success"));
	}
}

