///////////////////////////////////////////////////////////
//
//  proposal_Action.java
//  Created on Mon Apr 12 16:35:37 EDT 2004
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


public class proposal_Action extends org.apache.struts.action.Action
{

	public proposal_Action()
	{

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if(!SuperForm.class.isInstance(form))
			throw new Exception("Object is not a SuperForm.");
		HttpSession session = request.getSession();
		BeanManager manager=(BeanManager)session.getAttribute("beanManager");
		proposal_Form proposal_form=(proposal_Form)session.getAttribute("proposal_form");
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

		if(proposal_form==null)
		{
			errors.add("Failed to find the FormBean:proposal_form");
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
				((SuperForm)form).addRecord();
				String proposalId=((SuperForm)form).getFieldValue(((SuperForm)form).getPrimaryKey());
				manager.setProposalId(proposalId);
				event_Form fm=new event_Form();
				
				fm.setACTION_ID("0");
				GregorianCalendar calendar=new GregorianCalendar();
				int day = calendar.get(Calendar.DATE);
				int month = calendar.get(Calendar.MONTH);
				int year = calendar.get(Calendar.YEAR);
				String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
				
				fm.setEventDate(dt);
				fm.setPROPOSAL_ID(proposalId);
				fm.setROLE_ID("0");
				fm.setACTION_ID("unsubmitted");
				fm.setUSR_ID(manager.getUserId());
				fm.addRecord();
			}
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

