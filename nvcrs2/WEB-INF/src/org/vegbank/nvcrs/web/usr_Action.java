///////////////////////////////////////////////////////////
//
//  usr_Action.java
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


public class usr_Action extends org.apache.struts.action.Action
{

	public usr_Action()
	{

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if(!SuperForm.class.isInstance(form))
			throw new Exception("Object is not a SuperForm.");
		HttpSession session = request.getSession();
		String strAct=request.getParameter("action");
		BeanManager manager=null;
		usr_Form usr_form=null;
		manager=(BeanManager)session.getAttribute("beanManager");
		usr_form=(usr_Form)session.getAttribute("usr_form");
		
		if(strAct.equals("register"))
		{
			
			
			if(usr_form==null)
			{
				return (mapping.findForward("register_error"));
			}
			try
			{
				((SuperForm)form).addRecord();
			}
			catch(Exception e)
			{
				return (mapping.findForward("register_error"));
			}
			
			if(manager==null)
			{
				manager=new BeanManager();
				session.setAttribute("beanManager",manager);
			}
			manager.clear();
			manager.setUserCurrentRole("Author");
			manager.setUserEmail(usr_form.getEmail());
			manager.setUserId(usr_form.getUSR_ID());
			manager.setUserName(usr_form.getFirst_name());
			manager.setUserPermission(usr_form.getPermission());
		
			manager.setMessage("Congratulations! You have successfully registered to NVC revision system.");
			try{ return (mapping.findForward("register_success")); }
			catch(Exception e){}
				
		}
		
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

		if(usr_form==null)
		{
			errors.add("Failed to find the FormBean:usr_form");
			return (mapping.findForward("error"));
		}

		
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

