

package org.vegbank.nvcrs.web;

import java.util.Locale;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.ModuleException;
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.vegbank.nvcrs.util.*;


/**
 * Implementation of <strong>Action</strong> that validates a user logon.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.1.1.1 $ $Date: 2004-06-16 19:48:21 $
 */

public final class LogonAction extends Action {

        
    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws Exception 
	{
		
	HttpSession session = request.getSession();
	
	
	BeanManager manager=(BeanManager)session.getAttribute("beanManager");
	if(manager==null)
	{
		manager=new BeanManager();
		session.setAttribute("beanManager",manager);
	}
		
	usr_Form usr_form=(usr_Form)session.getAttribute("usr_form");
	if(usr_form==null)
		usr_form=new usr_Form();
	
	
	ArrayList errors = manager.getErrors();
	if(errors==null)
		errors=new ArrayList();
	else
		errors.clear();
	

	String username = (String)
            PropertyUtils.getSimpleProperty(form, "username");
        String password = (String)
            PropertyUtils.getSimpleProperty(form, "password");
	
	String strSQL="select * from usr where login_name='" + username +"' and password='" + password +"'";
	try
	{
		usr_form.findRecordBySQL(strSQL);
	}
	catch(Exception e)
	{
		errors.add("Failed to find your infomation. Please check your username and password and try again");
	}	
	
	if (!errors.isEmpty())
	{
		if(usr_form!=null)
			session.removeAttribute("usr_form");
        return (mapping.findForward("login"));
	}
	
	session.setAttribute("usr_form",usr_form);
	manager.setUserId(usr_form.getUSR_ID());
	manager.setUserName(usr_form.getFirst_name());
	manager.setUserEmail(usr_form.getEmail());
	manager.setUserPermission(usr_form.getPermission());
	String permission=usr_form.getPermission();
	if(permission.equals("0 ") || permission.equals("3 ") ||  permission.equals("4 ")||permission.equals("6 "))
	{
		manager.setUserCurrentRole("Author");
		manager.setCurMenu("Author");
		proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
		if(pForm==null)
		{
			pForm=new proposal_Form();
			session.setAttribute("proposal_form",pForm);
		}
		String strSql="select proposal.* from proposal,event where event.USR_ID='"+manager.getUserId()+"'"; 
		strSql+=" AND event.PROPOSAL_ID=proposal.PROPOSAL_ID";
		strSql+=" AND proposal.current_status='unsubmitted' AND event.ACTION_ID='unsubmitted'";
		pForm.findRecords(strSql);
	}
	else if(permission.equals("1 ") || permission.equals("5 "))
	{
		manager.setUserCurrentRole("Peer-viewer");
		manager.setCurMenu("Peer-viewer");
		Assignments asgns=(Assignments)session.getAttribute("assignments");
		if(asgns==null)
		{
			asgns=new Assignments();
			session.setAttribute("assignments",asgns);
		}
		asgns.reset();
		asgns.setPeerviewerId(manager.getUserId());
		asgns.setManager(manager);
				
		asgns.addStatus(Assignment.STATUS_NEW);
		asgns.addStatus(Assignment.STATUS_ACCEPTED);
		asgns.addStatus(Assignment.STATUS_EVALUATING);
		asgns.getAssignmentsByPeerviewer();
	}
	else if(permission.equals("2 "))
	{
		manager.setUserCurrentRole("Manager");
		manager.setCurMenu("Manager");
		proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
		if(pForm==null)
		{
			pForm=new proposal_Form();
			session.setAttribute("proposal_form",pForm);
		}
		String strSql="select * from proposal where"; 
		strSql+=" proposal.current_status='submitted' OR proposal.current_status='evaluated'";
		pForm.findRecords(strSql);
	}
	else
	{
		manager.setUserCurrentRole("Author");
		manager.setCurMenu("Author");
		proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
		if(pForm==null)
		{
			pForm=new proposal_Form();
			session.setAttribute("proposal_form",pForm);
		}
		String strSql="select proposal.* from proposal,event where event.USR_ID='"+manager.getUserId()+"'"; 
		strSql+=" AND event.PROPOSAL_ID=proposal.PROPOSAL_ID";
		strSql+=" AND proposal.current_status='unsubmitted' AND event.ACTION_ID='unsubmitted'";
		pForm.findRecords(strSql);
	}
	

	return (mapping.findForward("main"));
	}
}
