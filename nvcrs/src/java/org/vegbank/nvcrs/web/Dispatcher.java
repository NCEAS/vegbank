/*
 * '$Id: Dispatcher.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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


import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

import java.math.BigDecimal;
import org.vegbank.nvcrs.util.*;

public class Dispatcher extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response) {   
    
    HttpSession session = request.getSession();
    
    BeanManager beanManager = (BeanManager)session.getAttribute("beanManager");
    String selectedScreen = request.getServletPath();

    if (selectedScreen.equals("/register.go")) 
   	{
	   	if(beanManager==null)
	   	{
	   		beanManager=new BeanManager();
	   		session.setAttribute("beanManager",beanManager);
	   	}
	   	beanManager.clear();
	   	usr_Form fr=(usr_Form)session.getAttribute("usr_form");
	   	if(fr==null)
	   	{
	   		fr=new usr_Form();
	   		session.setAttribute("usr_form",fr);
	   	}
	   	
	   	
	   	try{
	   		fr.clearForm();
	   		fr.setRole("Author");
	   		fr.setPermission("0");
	   		beanManager.setUserCurrentRole("Author");
	   		response.sendRedirect("/nvcrs/register_main.jsp");
	   	}
	   	catch(Exception e)
	   	{
	   	}
	}
	
    if (beanManager == null) {
      
      	try{
	   		response.sendRedirect("/nvcrs/please_login.jsp");
	   	}
	   	catch(Exception e)
	   	{
	   	}
    }
    else
    {
    beanManager.getErrors().clear();
    
    
	
	if (selectedScreen.equals("/logoff.go")) 
   	{
	   	//beanManager.logoff();	
	   	beanManager.clear();
	   	session.removeAttribute("beanManager");
	   	removeAllFormBeans(session);
	   	try{
	   	response.sendRedirect("/nvcrs/index.jsp");
	   	}
	   	catch(Exception e)
	   	{
	   	}
	}
	
	else if(selectedScreen.equals("/viewtype.go"))
	{
		String target=request.getParameter("target");
		String id=request.getParameter("id");
		if(target==null ||id==null)
		{
			beanManager.getErrors().add("Invalid parameters");
			try
			{
				response.sendRedirect("/nvcrs/error_body.jsp");
			}
			catch(Exception e){}
		}
		if(target.equals("type"))
		{
			type_Form fm=(type_Form)session.getAttribute("type_form");
			
			try{
				fm.findRecordByPrimaryKey(id);
			   	response.sendRedirect("/nvcrs/type_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   			beanManager.getErrors().add(e.getMessage());
				try
				{
					response.sendRedirect("/nvcrs/error_body.jsp");
				}
				catch(Exception ex){}
	   		}
	   	}
	   	else if(target.equals("composition"))
		{
			composition_Form fm=(composition_Form)session.getAttribute("composition_form");
			try{
				fm.findRecordByPrimaryKey(id);
			   	response.sendRedirect("/nvcrs/composition_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   			beanManager.getErrors().add(e.getMessage());
				try
				{
					response.sendRedirect("/nvcrs/error_body.jsp");
				}
				catch(Exception ex){}
	   		}
	   	}
	   	else if(target.equals("distribution"))
		{
			distribution_Form fm=(distribution_Form)session.getAttribute("distribution_form");
			try{
				fm.findRecordByPrimaryKey(id);
			   	response.sendRedirect("/nvcrs/distribution_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   			beanManager.getErrors().add(e.getMessage());
				try
				{
					response.sendRedirect("/nvcrs/error_body.jsp");
				}
				catch(Exception ex){}
	   		}
	   	}
		else if(target.equals("typename"))
		{
			typename_Form fm=(typename_Form)session.getAttribute("typename_form");
			try{
				fm.findRecordByPrimaryKey(id);
			   	response.sendRedirect("/nvcrs/typename_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   			beanManager.getErrors().add(e.getMessage());
				try
				{
					response.sendRedirect("/nvcrs/error_body.jsp");
				}
				catch(Exception ex){}
	   		}
	   	}
	   	else if(target.equals("reference"))
		{
			typereference_Form fm=(typereference_Form)session.getAttribute("typereference_form");
			try{
				fm.findRecordByPrimaryKey(id);
			   	response.sendRedirect("/nvcrs/typereference_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   			beanManager.getErrors().add(e.getMessage());
				try
				{
					response.sendRedirect("/nvcrs/error_body.jsp");
				}
				catch(Exception ex){}
	   		}
	   	}
	   	else if(target.equals("plot"))
		{
			plot_Form fm=(plot_Form)session.getAttribute("plot_form");
			try{
				fm.findRecordByPrimaryKey(id);
			   	response.sendRedirect("/nvcrs/plot_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   			beanManager.getErrors().add(e.getMessage());
				try
				{
					response.sendRedirect("/nvcrs/error_body.jsp");
				}
				catch(Exception ex){}
	   		}
	   	}
	   	else
	   	{
	   		beanManager.getErrors().add("Unsupported action: "+target);
			try
			{
				response.sendRedirect("/nvcrs/error_body.jsp");
			}
			catch(Exception ex){}
	   	}
	}
	else if(selectedScreen.equals("/plots.go"))
	{
		type_Form tpForm=(type_Form)session.getAttribute("type_form");
		if(tpForm==null)
		{
			beanManager.getErrors().add("No type form bean created.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		
		String typeId=null;
		try
		{
			typeId=tpForm.getFieldValue(tpForm.getPrimaryKey());
		}
		catch(Exception ex)
		{
			beanManager.getErrors().add(ex.getMessage());
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}		
		}
		String action=request.getParameter("action");
		if(action==null)
		{
			beanManager.getErrors().add("Empty action.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		else if(action.equals("add"))
		{
			plot_Form ptForm=(plot_Form)session.getAttribute("plot_form");
 			if(ptForm==null)
 			{
 				ptForm=new plot_Form();
 				session.setAttribute("plot_form",ptForm);
 			}
 			
 			
 			try
 			{
 				ptForm.clearForm();
 				ptForm.setTYPE_ID(typeId);
	   			response.sendRedirect("/nvcrs/plot_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
	   		}
		}
	}
	else if(selectedScreen.equals("/compositions.go"))
	{
		type_Form tpForm=(type_Form)session.getAttribute("type_form");
		if(tpForm==null)
		{
			beanManager.getErrors().add("No type form bean created.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		
		String typeId=null;
		try
		{
			typeId=tpForm.getFieldValue(tpForm.getPrimaryKey());
		}
		catch(Exception ex)
		{
			beanManager.getErrors().add(ex.getMessage());
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}		
		}
		String action=request.getParameter("action");
		if(action==null)
		{
			beanManager.getErrors().add("Empty action.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		
		else if(action.equals("add"))
		{
			composition_Form ptForm=(composition_Form)session.getAttribute("composition_form");
 			if(ptForm==null)
 			{
 				ptForm=new composition_Form();
 				session.setAttribute("plot_form",ptForm);
 			}
 			
 			
 			try
 			{
 				ptForm.clearForm();
 				ptForm.setTYPE_ID(typeId);
	   			response.sendRedirect("/nvcrs/composition_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
	   		}
		}
	}
	else if(selectedScreen.equals("/typenames.go"))
	{
		type_Form tpForm=(type_Form)session.getAttribute("type_form");
		if(tpForm==null)
		{
			beanManager.getErrors().add("No type form bean created.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		
		String typeId=null;
		try
		{
			typeId=tpForm.getFieldValue(tpForm.getPrimaryKey());
		}
		catch(Exception ex)
		{
			beanManager.getErrors().add(ex.getMessage());
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}		
		}
		String action=request.getParameter("action");
		if(action==null)
		{
			beanManager.getErrors().add("Empty action.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		else if(action.equals("add"))
		{
			typename_Form ptForm=(typename_Form)session.getAttribute("typename_form");
 			if(ptForm==null)
 			{
 				ptForm=new typename_Form();
 				session.setAttribute("typename_form",ptForm);
 			}
 			
 			
 			try
 			{
 				ptForm.clearForm();
 				ptForm.setTYPE_ID(typeId);
	   			response.sendRedirect("/nvcrs/typename_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
	   		}
		}
	}
	else if(selectedScreen.equals("/distributions.go"))
	{
		type_Form tpForm=(type_Form)session.getAttribute("type_form");
		if(tpForm==null)
		{
			beanManager.getErrors().add("No type form bean created.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		
		String typeId=null;
		try
		{
			typeId=tpForm.getFieldValue(tpForm.getPrimaryKey());
		}
		catch(Exception ex)
		{
			beanManager.getErrors().add(ex.getMessage());
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}		
		}
		String action=request.getParameter("action");
		if(action==null)
		{
			beanManager.getErrors().add("Empty action.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		else if(action.equals("add"))
		{
			distribution_Form ptForm=(distribution_Form)session.getAttribute("distribution_form");
 			if(ptForm==null)
 			{
 				ptForm=new distribution_Form();
 				session.setAttribute("distribution_form",ptForm);
 			}
 			
 			
 			try
 			{
 				ptForm.clearForm();
 				ptForm.setTYPE_ID(typeId);
	   			response.sendRedirect("/nvcrs/distribution_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
	   		}
		}
	}
	else if(selectedScreen.equals("/typereferences.go"))
	{
		type_Form tpForm=(type_Form)session.getAttribute("type_form");
		if(tpForm==null)
		{
			beanManager.getErrors().add("No type form bean created.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		
		String typeId=null;
		try
		{
			typeId=tpForm.getFieldValue(tpForm.getPrimaryKey());
		}
		catch(Exception ex)
		{
			beanManager.getErrors().add(ex.getMessage());
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}		
		}
		String action=request.getParameter("action");
		if(action==null)
		{
			beanManager.getErrors().add("Empty action.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		else if(action.equals("add"))
		{
			typereference_Form ptForm=(typereference_Form)session.getAttribute("typereference_form");
 			if(ptForm==null)
 			{
 				ptForm=new typereference_Form();
 				session.setAttribute("typereference_form",ptForm);
 			}
 			 			
 			
 			try
 			{
 				ptForm.clearForm();
 				ptForm.setTYPE_ID(typeId);
	   			response.sendRedirect("/nvcrs/typereference_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
		   		beanManager.getErrors().add("Typereference problem");
		   		try
		   		{
		   			response.sendRedirect("/nvcrs/error_body.jsp");
		   
		   		}
		   		catch(Exception ex)
		   		{
		   		}
	   		}
		}
	}
	else if(selectedScreen.equals("/type.go"))
	{
		type_Form tpForm=(type_Form)session.getAttribute("type_form");
		if(tpForm==null)
		{
			beanManager.getErrors().add("No type form bean created.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		
		String typeId=null;
		try
		{
			typeId=tpForm.getFieldValue(tpForm.getPrimaryKey());
		}
		catch(Exception ex)
		{
			beanManager.getErrors().add(ex.getMessage());
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}		
		}
		
		
		
		String target=request.getParameter("target");
		
		if(target==null)
		{
			beanManager.getErrors().add("Empty target.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		else if(target.equals("plot"))
		{
			plot_Form ptForm=(plot_Form)session.getAttribute("plot_form");
			if(ptForm==null)
			{
				ptForm=new plot_Form();
				ptForm.setTYPE_ID(typeId);
				session.setAttribute("plot_form",ptForm);
			}
			String sql="select * from plot where TYPE_ID='"+typeId+"'";
 			try
 			{
 				ptForm.findRecords(sql);
 			}
 			catch(Exception e)
 			{
 				beanManager.getErrors().add(e.getMessage());
				try
 				{
	   				response.sendRedirect("/nvcrs/error_body.jsp");
	   			}
				catch(Exception ex)
	   			{
	   			}
 			}
 			
 			try
 			{
	   			response.sendRedirect("/nvcrs/type_plot_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
				
		}
		else if(target.equals("composition"))
		{
			composition_Form ptForm=(composition_Form)session.getAttribute("composition_form");
			if(ptForm==null)
			{
				ptForm=new composition_Form();
				ptForm.setTYPE_ID(typeId);
				session.setAttribute("composition_form",ptForm);
			}
			String sql="select * from composition where TYPE_ID='"+typeId+"'";
 			try
 			{
 				ptForm.findRecords(sql);
 			}
 			catch(Exception e)
 			{
 				beanManager.getErrors().add(e.getMessage());
				try
 				{
	   				response.sendRedirect("/nvcrs/error_body.jsp");
	   			}
				catch(Exception ex)
	   			{
	   			}
 			}
 			
 			try
 			{
	   			response.sendRedirect("/nvcrs/type_comp_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
				
		}
		else if(target.equals("typename"))
		{
			typename_Form ptForm=(typename_Form)session.getAttribute("typename_form");
			if(ptForm==null)
			{
				ptForm=new typename_Form();
				ptForm.setTYPE_ID(typeId);
				session.setAttribute("typename_form",ptForm);
			}
			String sql="select * from typename where TYPE_ID='"+typeId+"'";
 			try
 			{
 				ptForm.findRecords(sql);
 			}
 			catch(Exception e)
 			{
 				beanManager.getErrors().add(e.getMessage());
				try
 				{
	   				response.sendRedirect("/nvcrs/error_body.jsp");
	   			}
				catch(Exception ex)
	   			{
	   			}
 			}
 			
 			try
 			{
	   			response.sendRedirect("/nvcrs/type_name_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
				
		}
		else if(target.equals("distribution"))
		{
			distribution_Form ptForm=(distribution_Form)session.getAttribute("distribution_form");
			if(ptForm==null)
			{
				ptForm=new distribution_Form();
				ptForm.setTYPE_ID(typeId);
				session.setAttribute("distribution_form",ptForm);
			}
			String sql="select * from distribution where TYPE_ID='"+typeId+"'";
 			try
 			{
 				ptForm.findRecords(sql);
 			}
 			catch(Exception e)
 			{
 				beanManager.getErrors().add(e.getMessage());
				try
 				{
	   				response.sendRedirect("/nvcrs/error_body.jsp");
	   			}
				catch(Exception ex)
	   			{
	   			}
 			}
 			
 			try
 			{
	   			response.sendRedirect("/nvcrs/type_dist_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
				
		}
		else if(target.equals("typereference"))
		{
			typereference_Form ptForm=(typereference_Form)session.getAttribute("typereference_form");
			if(ptForm==null)
			{
				ptForm=new typereference_Form();
				ptForm.setTYPE_ID(typeId);
				session.setAttribute("typereference_form",ptForm);
			}
			String sql="select * from typereference where TYPE_ID='"+typeId+"'";
 			try
 			{
 				ptForm.findRecords(sql);
 			}
 			catch(Exception e)
 			{
 				beanManager.getErrors().add(e.getMessage());
				try
 				{
	   				response.sendRedirect("/nvcrs/error_body.jsp");
	   			}
				catch(Exception ex)
	   			{
	   			}
 			}
 			
 			try
 			{
	   			response.sendRedirect("/nvcrs/type_ref_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
				
		}
	}
	else if(selectedScreen.equals("/types.go"))
	{
		String action=request.getParameter("action");
		if(action==null)
		{
			beanManager.getErrors().add("Empty action.");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
		}
		else if(action.equals("add"))
		{
			type_Form tpForm=(type_Form)session.getAttribute("type_form");
 			if(tpForm==null)
 			{
 				tpForm=new type_Form();
 				
 				
 				session.setAttribute("type_form",tpForm);
 			}
 			
 			try
 			{
 				tpForm.clearForm();
 				tpForm.setPROPOSAL_ID(beanManager.getProposalId());
 				tpForm.setActionType("new");
	   			response.sendRedirect("/nvcrs/type_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
	   		}
		}
	}
	else if (selectedScreen.equals("/menu.go")) 
   	{
	   	String role=request.getParameter("role");	   	
	   	if(role!=null && (role.equals("Author") ||role.equals("Peer-viewer")||role.equals("Manager")))
	   	{
	   		beanManager.setUserCurrentRole(role);
		   	try{
			   	response.sendRedirect("/nvcrs/main.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
	   	}
	   	else
	   	{
	   		beanManager.getErrors().add("Invalid role");
		   	try{
			   	response.sendRedirect("/nvcrs/error_body.jsp");
		   	}
	   		catch(Exception e)
	   		{
	   		}
	   	}
	}
	
	else if(selectedScreen.equals("/author.go"))
 	{
 		String action=request.getParameter("action");
 		String condition=request.getParameter("con");
 		if(action==null)
 		{
 			beanManager.getErrors().add("Invalid parameter");
			 try {
        		response.sendRedirect("/nvcrs/error_body.jsp");
    			} catch(Exception ex) {
    			}
    
 		}
 		else if(action.equals("new"))  //add new proposal
 		{
 			proposal_Form form=new proposal_Form();
 			form.setCurrent_status("unsubmitted");
 			beanManager.setProposalOwnerId(beanManager.getUserId());
 			beanManager.setProposalStatus("unsubmitted");
 			session.setAttribute("proposal_form",form);
 			try
 			{
	   			response.sendRedirect("/nvcrs/proposal_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
 		}
 		else if(action.equals("query"))
 		{
 			String con=request.getParameter("con");
 			proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
 			if(pForm==null)
 			{
 				pForm=new proposal_Form();
 				session.setAttribute("proposal_form",pForm);
 			}
 				
 			String strSql="";
 			if(con.equals("unsubmitted"))
 			{
 				strSql="select proposal.* from proposal,event where proposal.current_status='unsubmitted' AND ";
 				strSql+="event.USR_ID='"+beanManager.getUserId()+"' AND event.PROPOSAL_ID=proposal.PROPOSAL_ID";
 				
 				try
 				{
 					pForm.findRecords(strSql);
 					response.sendRedirect("/nvcrs/proposals_main.jsp");
 				}
 				catch(Exception e)
 				{
 					beanManager.getErrors().add(e.getMessage());
 					try
 					{	
 						response.sendRedirect("/nvcrs/error_body.jsp");
 					}
 					catch(Exception ex){}
 				}
 			}
 		}
 	}
	
	else if(selectedScreen.equals("/proposal.go"))
 	{
 		
 		proposal_Form form=(proposal_Form)session.getAttribute("proposal_form");
 		if(form==null)
 		{
 			beanManager.getErrors().add("Sorry, no proposal form bean created.");
	 		try {
    	    		response.sendRedirect("/nvcrs/error_body.jsp");
    		} catch(Exception ex) {
    		}
    	}
 		String target1=request.getParameter("target");
 		if(target1==null)
 		{
 			beanManager.getErrors().add("Invalid parameter: target needed");
			try {
        		response.sendRedirect("/nvcrs/error_body.jsp");
    			} catch(Exception ex) {
    			}
    	}
 		else if(target1.equals("proposal"))  
 		{
 			try
 			{
	   			response.sendRedirect("/nvcrs/proposal_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
	   		}
	
	   }
 		else if(target1.equals("project"))  
 		{
 						
 			project_Form pjForm=(project_Form)session.getAttribute("project_form");
 			if(pjForm==null)
 			{
 				pjForm=new project_Form();
 				session.setAttribute("project_form",pjForm);
 			}
 			try
 			{
	   			response.sendRedirect("/nvcrs/project_main.jsp");
	   		}
		   	catch(Exception e)
		   	{
	   		}
	   }
	   else if(target1.equals("types"))
 	   {	 	
 			type_Form tpform=(type_Form)session.getAttribute("type_form");
 			if(tpform==null)
 			{
 				tpform=new type_Form();
 				tpform.setPROPOSAL_ID(beanManager.getProposalId());
 			}
 			
 			String sql="select * from type where PROPOSAL_ID='"+beanManager.getProposalId()+"'";
 			try
 			{
 				tpform.findRecords(sql);
 			}
 			catch(Exception e)
 			{
 				beanManager.getErrors().add(e.getMessage());
				try
 				{
	   				response.sendRedirect("/nvcrs/error_body.jsp");
	   			}
				catch(Exception ex)
	   			{
	   			}
 			}
 			session.setAttribute("type_form",tpform);
 			
 			try
 			{
	   			response.sendRedirect("/nvcrs/types_main.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
 		}
 		else if(target1.equals("submit"))
 		{
 				String proposalId=beanManager.getProposalId();
 				proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
 				if(pForm==null)
 				{
 					beanManager.getErrors().add("No proposal form bean creates.");
					try
 					{
	   					response.sendRedirect("/nvcrs/error_body.jsp");
	   				}
			   		catch(Exception ex)
	   				{
	   				}
 				}
 				
 				
 				event_Form fm=new event_Form();
 				fm.setACTION_ID("1");
				
				Calendar date=Calendar.getInstance();
				int day = date.get(Calendar.DATE);
				int month = date.get(Calendar.MONTH);
				int year = date.get(Calendar.YEAR);
				String dt=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
				
 				fm.setEventDate(dt);
				fm.setPROPOSAL_ID(proposalId);
				fm.setROLE_ID("0");
				fm.setUSR_ID(beanManager.getUserId());
				fm.setACTION_ID("submitted");
				pForm.setCurrent_status("submitted");
				try
				{
					fm.addRecord();
					pForm.updateFields();
				}
				catch(Exception e)
				{
					beanManager.getErrors().add(e.getMessage());
					try
 					{
	   					response.sendRedirect("/nvcrs/error_body.jsp");
	   				}
			   		catch(Exception ex)
	   				{
	   				}
				}
			
				
				
				beanManager.setProposalStatus("submitted");
				
 				try
 				{
	   				response.sendRedirect("/nvcrs/submit_main.jsp");
	   			}
	   			catch(Exception e)
	   			{
	   			}
 		    }
 		    else
 		    {
 		    	beanManager.getErrors().add("Unsupported Action");
		 		try {
        			response.sendRedirect("/nvcrs/error_body.jsp");
    			} catch(Exception ex) {
    			}
 		    }
 			
 	   }
 	   else
 	   {
 			beanManager.getErrors().add("Unsupported Action: "+selectedScreen);
		 	try {
        		response.sendRedirect("/nvcrs/error_body.jsp");
    		} catch(Exception ex) {
    		}
    
 		}
    }
    
    
  }
  
  //do post ///////////////////////////////////////////////////////////////////////////////////////
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    
    HttpSession session = request.getSession();
    
    BeanManager beanManager = (BeanManager)session.getAttribute("beanManager");
        
    if (beanManager == null) {
      beanManager = new BeanManager();
      session.setAttribute("beanManager", beanManager);
    }
    
    checkRegistration(beanManager,request,response);
    
    String selectedScreen = request.getServletPath();
	
	if (selectedScreen.equals("/logoff.go")) 
   	{
	   	//beanManager.logoff();	
	   	beanManager.clear();
	   	try{
	   	response.sendRedirect("/nvcrs/home.jsp");
	   	}
	   	catch(Exception e)
	   	{
	   	}
	}
 	else if(selectedScreen.equals("/author.go"))
 	{
 		String action=request.getParameter("action");
 		String condition=request.getParameter("con");
 		if(action.equals("new"))  //add new proposal
 		{
 			proposal_Form form=new proposal_Form();
 			form.setCurrent_status("unsubmiited");
 			session.setAttribute("proposal_form",form);
 			try
 			{
	   			response.sendRedirect("/nvcrs/newproposal.jsp");
	   		}
	   		catch(Exception e)
	   		{
	   		}
 		}
 	}
 	else
 	{
 		
 	}
 
    
    try {
        response.sendRedirect("/nvcrs/home.jsp");
    } catch(Exception ex) {
    }
    
  }
  
  private void checkRegistration(BeanManager beanManager,HttpServletRequest request,HttpServletResponse response)
  {
  
	  if(!beanManager.isRegistered())
	    {
	    	beanManager.getErrors().add("<p>Sorry,you are not allowed to access this page.</p><p> Please Sign in and try again</P>");
	    	
	    	try {
		        response.sendRedirect("/template/errpage.jsp");
		    } catch(Exception ex) {
		      ex.printStackTrace();
		    }
		}
	}
	
	private void removeAllFormBeans(HttpSession session)
	{
		
		SuperForm f=(SuperForm)session.getAttribute("proposal_form");
		if(f!=null)
			session.removeAttribute("proposal_form");
		f=(SuperForm)session.getAttribute("type_form");
		if(f!=null)
			session.removeAttribute("type_form");
			
		f=(SuperForm)session.getAttribute("project_form");
		if(f!=null)
			session.removeAttribute("project_form");
			
		f=(SuperForm)session.getAttribute("plot_form");
		if(f!=null)
			session.removeAttribute("plot_form");
		f=(SuperForm)session.getAttribute("composition_form");
		if(f!=null)
			session.removeAttribute("composition_form");
		f=(SuperForm)session.getAttribute("typename_form");	
		if(f!=null)
			session.removeAttribute("typename_form");
		f=(SuperForm)session.getAttribute("typereference_form");
		if(f!=null)
			session.removeAttribute("typereference_form");
		f=(SuperForm)session.getAttribute("usr_form");
		if(f!=null)
			session.removeAttribute("usr_form");
						
	}
}
