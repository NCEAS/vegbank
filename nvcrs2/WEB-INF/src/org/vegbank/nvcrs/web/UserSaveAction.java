/*
 * UserSaveAction.java
 *
 * Generated on Wed Apr 07 16:24:58 EDT 2004
 * by Exadel Struts Studio
 */

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

public class UserSaveAction extends org.apache.struts.action.Action {
    
    // Global Forwards
    public static final String GLOBAL_FORWARD_home = "home";
    
    // Local Forwards
    private static final String FORWARD_userinfo = "userinfo";
    
    public UserSaveAction() {
        // TODO: Write constructor body
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO: Write method body
        if(!SuperForm.class.isInstance(form))
        	throw new Exception("Object is not a SuperForm.");
        HttpSession session = request.getSession();
		UserDetailForm userDetailForm=(UserDetailForm)session.getAttribute("userDetailForm");
		String msg=(String)session.getAttribute("message1");
		if(msg!=null)
		{
			session.setAttribute("message1","");
		}
		
		ArrayList errors = (ArrayList)session.getAttribute("errors1");
		if(errors==null)
			errors=new ArrayList();
		else
			errors.clear();
			
        if(userDetailForm==null)
		{
			errors.add("Please login or registered first");
			return (mapping.findForward("error"));
		}
			
        try
        {
        	((SuperForm)form).updateRecord();
        }
        catch(Exception e)
        {
        	errors.add(e.getMessage());
        	session.setAttribute("errors1",errors);
        	return (mapping.findForward("error"));
        }
        session.setAttribute("message1","updated successfully");
        
        return (mapping.findForward("userinfo"));
    }
}
