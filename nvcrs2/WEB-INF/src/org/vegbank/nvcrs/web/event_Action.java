///////////////////////////////////////////////////////////
//
//  event_Action.java
//  Created on Wed Apr 21 14:02:14 EDT 2004
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


public class event_Action extends org.apache.struts.action.Action
{

	public event_Action()
	{

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if(!SuperForm.class.isInstance(form))
			throw new Exception("Object is not a SuperForm.");
		HttpSession session = request.getSession();
		BeanManager manager=(BeanManager)session.getAttribute("beanManager");
		event_Form event_form=(event_Form)session.getAttribute("event_form");
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

		if(event_form==null)
		{
			errors.add("Failed to find the FormBean:event_form");
			return (mapping.findForward("error"));
		}

		String strAct=request.getParameter("action");
		if(strAct==null)
			strAct="save";

		String aid="";
		try
		{
			if(strAct.equals("cancel"))
				return (mapping.findForward("cancel"));
			else if(strAct.equals("save"))
			{
				((SuperForm)form).updateRecord();
			}
			else if (strAct.equals("add"))
			{
				event_Form fr=(event_Form)form;
				fr.addRecord();
				
				//update current status of proposal
				String pid=fr.getPROPOSAL_ID();
				proposal_Form pfr=new proposal_Form();
				pfr.findRecordByPrimaryKey(pid);
				aid=fr.getACTION_ID();
				if(aid.equals("assigned"))
				{
					pfr.setCurrent_status("assigned");
					pfr.updateRecord();
					
				}
				else if(aid.equals("evaluating"))
				{
					event_Form f=new event_Form();
					f.findRecordBySQL("select * from event where PROPOSAL_ID='"+pid+"' AND SUBJECTUSR_ID='"+fr.getUSR_ID()+"' AND ACTION_ID='assigned'");
					f.setReviewText("evaluating");
					f.updateRecord();
					pfr.setCurrent_status("evaluating");
					pfr.updateRecord();	
				}
				else if(aid.equals("decided"))
				{
					String decide=fr.getSummary();
					pfr.setCurrent_status(decide);
					pfr.updateRecord();
					if(decide.equals("revision"))
					{
							
						pfr.setCurrent_status("unsubmitted");
						pfr.setPreviousProposal_ID(pid);
						pfr.addRecord();
						event_Form f=new event_Form();
						f.findRecordBySQL("select * from event where PROPOSAL_ID='"+pid+"' AND ACTION_ID='unsubmitted'");
						f.setEventDate(fr.getEventDate());
						f.setPROPOSAL_ID(pfr.getPROPOSAL_ID());
						f.addRecord();
					}
					
				}
			}
			else if(strAct.equals("handin"))
			{
				event_Form fr=(event_Form)form;
				String pid=fr.getPROPOSAL_ID();
				fr.setACTION_ID("evaluated");
				fr.addRecord();
				event_Form f=new event_Form();
				f.findRecordBySQL("select * from event where PROPOSAL_ID='"+pid+"' AND SUBJECTUSR_ID='"+fr.getUSR_ID()+"' AND ACTION_ID='assigned'");
				f.setReviewText("completed");
				f.updateRecord();
				
				if(manager.isAllEvaluated(pid))
				{
					proposal_Form pfr=new proposal_Form();
					pfr.findRecordByPrimaryKey(pid);
					pfr.setCurrent_status("evaluated");
					pfr.updateRecord();
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
			return (mapping.findForward("error"));
		}

		manager.setMessage("Action performed successfully: " + strAct);
		if(!aid.equals("decided"))
				return (mapping.findForward("success"));	
			
			return (mapping.findForward("success_decide"));
	}
}

