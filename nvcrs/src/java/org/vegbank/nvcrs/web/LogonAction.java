/*
 * '$Id: LogonAction.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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



package org.vegbank.nvcrs.web;

import java.util.Locale;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
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
 * @version $Revision: 1.1.1.1 $ $Date: 2004-04-21 17:10:06 $
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
	
	String strSQL="select * from usr where LOWER(login_name)='" + username.toLowerCase() +
			"' and LOWER(password)='" + password.toLowerCase() +"'";
	try
	{
		usr_form.findRecordBySQL(strSQL);
	}
	catch(Exception e)
	{
		errors.add("Failed to find your infomation. Please check your username and password and try again. " +
				e.toString());
	}	
	
	if (!errors.isEmpty())
	{
		if(usr_form!=null)
			session.removeAttribute("usr_form");
        return (mapping.findForward("error"));
	}

	
	session.setAttribute("usr_form",usr_form);
	manager.setUserId(usr_form.getUSR_ID());
	manager.setUserName(usr_form.getFirst_name());
	manager.setUserEmail(usr_form.getEmail());
	manager.setUserPermission(usr_form.getPermission());
	String permission=usr_form.getPermission();
	if(permission.equals("0") || permission.equals("3") ||  permission.equals("4")||permission.equals("6"))
		manager.setUserCurrentRole("Author");
	else if(permission.equals("1") || permission.equals("5"))
		manager.setUserCurrentRole("Peer-viewer");
	else if(permission.equals("2"))
		manager.setUserCurrentRole("Manager");
	else
		manager.setUserCurrentRole("Author");
	
	return (mapping.findForward("main"));

    }

}
