package org.vegbank.nvcrs.web;



import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

import java.math.BigDecimal;
import org.vegbank.nvcrs.util.*;
import org.apache.commons.fileupload.*;

public class Dispatcher extends HttpServlet 
{
  
  String document_path = "";
  SystemManager sysManager=null;
		
  public void init() throws ServletException{
    super.init();
    try
    {
    	
	    ServletContext application = getServletContext();
    	document_path=application.getRealPath("/documents");
		String path1="";    
		path1=application.getRealPath("/WEB-INF/system_settings.property");
		sysManager=new SystemManager(path1);
		application.setAttribute("system_manager",sysManager);
    }
    catch(Exception e)
    {
    	
    };
  }

  
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
  {   
	   HttpSession session = request.getSession();
	   
	    String path=null;
	    try
	    {
	    	
	    	path=handleGet(request,response);
	    	
	    }
	    catch(Exception e)
	    {
	    	session.setAttribute("nvcrs_error",e.getMessage());
	    	path="error_main.jsp";
	    }
	    
	    path="/nvcrs/"+path;
	    
	    try{response.sendRedirect(path);}
		catch(Exception e){	}
  }
  
  //do post ///////////////////////////////////////////////////////////////////////////////////////
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    
    String path=null;
    
    HttpSession session = request.getSession();
    try
    {
    	
    	path=handlePost(request,response);
    }
    catch(Exception e)
    {
    	session.setAttribute("nvcrs_error",e.getMessage());
    	path="error_main.jsp";
    }
    
    path="/nvcrs/"+path;
    
    try{response.sendRedirect(path);}
	catch(Exception e){	}
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
	private boolean isAllowedAction(String path,BeanManager manager)
	{
		if(manager==null) return false;
		if(!manager.isRegistered()) return false;
		String role=manager.getUserCurrentRole();
		if(path.indexOf("/manager.go")>-1)
		{
			if(role.equals("Manager")) return true;
			else return false;
		}
		return true;
	}

	private String handlePost(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		String selectedScreen = request.getServletPath();
    	
    	if(selectedScreen.equals("/registerme.go"))
    	{
    		usr_Form fr=new usr_Form();
    		fr.clearForm();
    		String login_name=request.getParameter("login_name").trim();
    		String password=request.getParameter("password").trim();
    		String first_name=request.getParameter("first_name").trim();
    		String last_name=request.getParameter("last_name").trim();
    		String middle_initial=request.getParameter("middle_initial").trim();
    		String institute=request.getParameter("institute").trim();
    		String address1=request.getParameter("address1").trim();
    		String address2=request.getParameter("address2").trim();
    		String city=request.getParameter("city").trim();
    		String state=request.getParameter("state").trim();
    		String country=request.getParameter("country").trim();
    		String zip=request.getParameter("zip").trim();
    		String phone=request.getParameter("phone").trim();
    		String email=request.getParameter("email").trim();
    		
    		fr.setLogin_name(login_name);
    		fr.setPassword(password);
    		fr.setFirst_name(first_name);
    		fr.setLast_name(last_name);
    		fr.setEmail(email);
    		if(middle_initial!=null) fr.setMiddle_initial(middle_initial);
    		if(institute!=null) fr.setInstitute(institute);
    		if(address1!=null) fr.setAddress1(address1);
    		if(address2!=null) fr.setAddress2(address2);
    		
    		if(city!=null) fr.setCity(city);
    		if(phone!=null) fr.setPhone(phone);
    		if(zip!=null) fr.setZip(zip);
    		if(country!=null) fr.setCountry(country);
    		if(state!=null) fr.setState(state);
    		
    		fr.setPermission("0");
    		fr.addRecord();
    		String uid=fr.getUSR_ID();
    		
    		BeanManager beanManager = new BeanManager();
    		beanManager.clear();
    		
    		beanManager.setUserId(uid);
    		beanManager.setUserName(first_name);
    		beanManager.setUserEmail(email);
    		beanManager.setUserPermission("0");
    		beanManager.setUserCurrentRole("Author");
    		beanManager.setSystemManager(sysManager);
    		session.setAttribute("beanManager", beanManager);
   
    		return "registrationAck_main.jsp";
    	}
    	BeanManager beanManager = (BeanManager)session.getAttribute("beanManager");
     	if(beanManager!=null)beanManager.setSystemManager(sysManager);
     	
     	if(selectedScreen==null)
     		throw new Exception("No path is defined.");
     	if(!isAllowedAction(selectedScreen,beanManager))
			throw new Exception("Sorry! <b>Please login or register first.</b>");
		
	
		String cur_proposal_id=beanManager.getProposalId();
		String cur_type_id=beanManager.getTypeId();
		String cur_usr_id=beanManager.getUserId();	    
    	
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle propoposals.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
    	if(selectedScreen.equals("/proposals.go"))
    	{
    		String action=request.getParameter("act");
    		proposal_Form fr=(proposal_Form)session.getAttribute("proposal_form");
    		if(fr==null)
    		{
    			fr=new proposal_Form();
    			session.setAttribute("proposal_form",fr);
    		}
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			fr.clearForm();
    			fr.setCurrent_status("unsubmitted");
    			beanManager.setProposalId(BeanManager.UNKNOWN_ID);
    			beanManager.setProposalOwnerId(beanManager.getUserId());
    			beanManager.setProposalStatus("unsubmitted");
    			return "proposal_main.jsp";
    		}
    		else
    		{
    			
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No proposal id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteProposal(strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "proposals_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				beanManager.setProposalId(ids);
    				beanManager.setProposalStatus(fr.getCurrent_status());
    				return "proposal_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                        POST      handle evaluations.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	else if(selectedScreen.equals("/evaluations.go"))
	    {
	    	String act=request.getParameter("act");	
	    	if(act==null) throw new Exception("No action has been specified.");
			if(act.equals("view"))
			{
				String id=request.getParameter("id");	
				if(id==null) throw new Exception("Invalid parameter.");
				
				event_Form pForm=(event_Form)session.getAttribute("event_form");
		 		if(pForm==null)
		 		{
		 			pForm=new event_Form();
		 			session.setAttribute("event_form",pForm);
		 		}
		 		pForm.clearForm();
		 		pForm.findRecordByPrimaryKey(id);
		 		return "evaluate_main.jsp";
			}
			else
				throw new Exception("Unsupported action: "+act);
				
	    }
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                        POST      handle projects.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	else if(selectedScreen.equals("/projects.go"))
	    {
	    	String act=request.getParameter("act");	
	    	if(act==null) throw new Exception("No action has been specified.");
			if(act.equals("select"))
			{
				String id=request.getParameter("id");	
				if(id==null) throw new Exception("Invalid parameter.");
				
				project_Form pjf=(project_Form)session.getAttribute("project_form");
				if(pjf==null)throw new Exception("No project form created.");
				pjf.findRecordByPrimaryKey(id);
				pjf.setPROJECT_ID(BeanManager.UNKNOWN_ID);
				
				return "project_main.jsp";
			}
			else
				throw new Exception("Unsupported action: "+act);
				
	    }
	    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                        POST      handle project.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	else if(selectedScreen.equals("/project.go"))
	    {
	    	project_Form form=(project_Form)session.getAttribute("project_form");
				
			if(form==null) throw new Exception("No project form bean created.");
		 	
			String target=request.getParameter("target");
			if(target==null)throw new Exception("Invalid parameter.");
			else if(target.equals("document"))  
			{
				
				String act=request.getParameter("act");	
				if(act==null) throw new Exception("No action has been specified.");
				else if(act.equals("upload"))
					return handleUpload("project",request,response);
				else if(act.equals("delete"))
				{
					String doc1=form.getDocument1();
					String doc2=form.getDocument1();
					String doc3=form.getDocument1();
					String id=request.getParameter("id");	
					
					if(id==null) throw new Exception("Invalid parameter");
					else if(id.equals("1") && doc1.trim().length()>0)
					{
						String strFile="C:\\StrutsStudioCom\\tomcat\\webapps\\nvcrs\\documents\\"+doc1;
		           		File file=new File(strFile);
		           		file.delete();
					}
					else if(id.equals("2") && doc2.trim().length()>0)
					{
						String strFile="C:\\StrutsStudioCom\\tomcat\\webapps\\nvcrs\\documents\\"+doc2;
		           		File file=new File(strFile);
		           		file.delete();
					}
					else if(id.equals("3") && doc3.trim().length()>0)
					{
						String strFile="C:\\StrutsStudioCom\\tomcat\\webapps\\nvcrs\\documents\\"+doc3;
		           		File file=new File(strFile);
		           		file.delete();
					}
					else
						throw new Exception("Invalid parameter");
					
					
				}
				else
					throw new Exception("Unsupported action: "+act);
			}
				
	    }
	     //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                        POST      handle event.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	else if(selectedScreen.equals("/event.go"))
	    {
	    	event_Form form=(event_Form)session.getAttribute("event_form");
				
			if(form==null) throw new Exception("No event form bean created.");
		 	
			String target=request.getParameter("target");
			if(target==null)throw new Exception("Invalid parameter.");
			else if(target.equals("document"))  
			{
				
				String act=request.getParameter("act");	
				if(act==null) throw new Exception("No action has been specified.");
				else if(act.equals("upload"))
					return handleUpload("event",request,response);
				else
					throw new Exception("Unsupported action: "+act);
			}
			else
				throw new Exception("Invalid parameter: "+target);
	    }
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle proposal.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	else if(selectedScreen.equals("/proposal.go"))
	    {
	    	proposal_Form form=(proposal_Form)session.getAttribute("proposal_form");
				
			if(form==null) throw new Exception("No proposal form bean created.");
		 	
			String target=request.getParameter("target");
			if(target==null||target.equals("proposal"))
				return "proposal_main.jsp";
			else if(target.equals("document"))  
			{
				
				String act=request.getParameter("act");	
		//		throw new Exception("target="+target+" act="+act);
					
				if(act==null) throw new Exception("No action has been specified.");
				else if(act.equals("upload"))
					return handleUpload("proposal",request,response);
				else if(act.equals("delete"))
				{
					String doc1=form.getDocument1();
					String doc2=form.getDocument1();
					String doc3=form.getDocument1();
					String id=request.getParameter("id");	
					
					if(id==null) throw new Exception("Invalid parameter");
					else if(id.equals("1") && doc1.trim().length()>0)
					{
						String strFile="C:\\StrutsStudioCom\\tomcat\\webapps\\nvcrs\\documents\\"+doc1;
		           		File file=new File(strFile);
		           		file.delete();
					}
					else if(id.equals("2") && doc2.trim().length()>0)
					{
						String strFile="C:\\StrutsStudioCom\\tomcat\\webapps\\nvcrs\\documents\\"+doc2;
		           		File file=new File(strFile);
		           		file.delete();
					}
					else if(id.equals("3") && doc3.trim().length()>0)
					{
						String strFile="C:\\StrutsStudioCom\\tomcat\\webapps\\nvcrs\\documents\\"+doc3;
		           		File file=new File(strFile);
		           		file.delete();
					}
					else
						throw new Exception("Invalid parameter");
					
					
				}
				else
					throw new Exception("Unsupported action: "+act);
			}
		}
   		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle types.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	else if(selectedScreen.equals("/types.go"))
    	{
    		String action=request.getParameter("act");
    		type_Form fr=(type_Form)session.getAttribute("type_form");
    		beanManager.setSomething("nothing");
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			if(fr==null)
    			{
    				fr=new type_Form();
    				session.setAttribute("type_form",fr);
    			}
    			fr.clearForm();
    			fr.setPROPOSAL_ID(cur_proposal_id);
    			updateAfterTypeChanged(null,session);
    			beanManager.setTypeId(BeanManager.UNKNOWN_ID);
    			return "type_main.jsp";
    		}
    		else
    		{
    			if(fr==null)throw new Exception("No type form is created.");
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No type id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteType(strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "types_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				beanManager.setTypeId(ids);
    				return "type_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle compositions.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    	else if (selectedScreen.equals("/compositions.go"))
    	{
    		String action=request.getParameter("act");
    		composition_Form fr=(composition_Form)session.getAttribute("composition_form");
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			if(fr==null)
    			{
    				fr=new composition_Form();
    				session.setAttribute("composition_form",fr);
    			}
    			fr.clearForm();
    			fr.setTYPE_ID(cur_type_id);
    			return "composition_main.jsp";
    		}
    		else
    		{
    			if(fr==null)throw new Exception("No composition form is created.");
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No composition id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteSingle("composition",fr.getPrimaryKey(),strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "compositions_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				return "composition_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle distributions.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if (selectedScreen.equals("/distributions.go"))
    	{
    		String action=request.getParameter("act");
    		distribution_Form fr=(distribution_Form)session.getAttribute("distribution_form");
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			if(fr==null)
    			{
    				fr=new distribution_Form();
    				session.setAttribute("distribution_form",fr);
    			}
    			fr.clearForm();
    			fr.setTYPE_ID(cur_type_id);
    			return "distribution_main.jsp";
    		}
    		else
    		{
    			if(fr==null)throw new Exception("No distribution form is created.");
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No distribution id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteSingle("distribution",fr.getPrimaryKey(),strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "distributions_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				return "distribution_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle typenames.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if (selectedScreen.equals("/typenames.go"))
    	{
    		String action=request.getParameter("act");
    		typename_Form fr=(typename_Form)session.getAttribute("typename_form");
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			if(fr==null)
    			{
    				fr=new typename_Form();
    				session.setAttribute("typename_form",fr);
    			}
    			fr.clearForm();
    			fr.setTYPE_ID(cur_type_id);
    			return "typename_main.jsp";
    		}
    		else
    		{
    			if(fr==null)throw new Exception("No typename form is created.");
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No typename id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteSingle("typename",fr.getPrimaryKey(),strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "typenames_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				return "typename_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle typereferences.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if (selectedScreen.equals("/typereferences.go"))
    	{
    		String action=request.getParameter("act");
    		typereference_Form fr=(typereference_Form)session.getAttribute("typereference_form");
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			if(fr==null)
    			{
    				fr=new typereference_Form();
    				session.setAttribute("typereference_form",fr);
    			}
    			fr.clearForm();
    			fr.setTYPE_ID(cur_type_id);
    			return "typereference_main.jsp";
    		}
    		else
    		{
    			if(fr==null)throw new Exception("No typereference form is created.");
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No typereference id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteSingle("typereference",fr.getPrimaryKey(),strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "typereferences_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				return "typereference_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle plots.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if (selectedScreen.equals("/plots.go"))
    	{
    		String action=request.getParameter("act");
    		plot_Form fr=(plot_Form)session.getAttribute("plot_form");
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			if(fr==null)
    			{
    				fr=new plot_Form();
    				session.setAttribute("plot_form",fr);
    			}
    			fr.clearForm();
    			fr.setTYPE_ID(cur_type_id);
    			return "plot_main.jsp";
    		}
    		else
    		{
    			if(fr==null)throw new Exception("No plot form is created.");
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No plot id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteSingle("plot",fr.getPrimaryKey(),strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "plots_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				return "plot_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}    		
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle correlations.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if (selectedScreen.equals("/correlations.go"))
    	{
    		String action=request.getParameter("act");
    		correlation_Form fr=(correlation_Form)session.getAttribute("correlation_form");
    		type_Form tpForm=(type_Form)session.getAttribute("type_form");
    		if(tpForm==null || tpForm.getRecords().size()<2) throw new Exception("No types or less than 2 types created");
    		if(action==null) throw new Exception("No action is specified.");
    		if(action.equals("add"))
    		{
    			if(fr==null)
    			{
    				fr=new correlation_Form();
    				session.setAttribute("correlation_form",fr);
    			}
    			fr.clearForm();
    			ArrayList records=tpForm.getRecords();
    			Hashtable t=(Hashtable)records.get(0);
    			Hashtable t1=(Hashtable)records.get(1);
    			fr.setTYPE_ID((String)t.get("TYPE_ID"));
    			fr.setCORRELATEDTYPE_ID((String)t1.get("TYPE_ID"));
    				
    			return "correlation_main.jsp";
    		}
    		else
    		{
    			if(fr==null)throw new Exception("No correlation form is created.");
    			String ids=request.getParameter("id");
    			if(ids==null) throw new Exception("No correlation id(s) specified");
    			
    			String [] strIds=ids.split(",");
	    		int num=strIds.length;
    			if(action.equals("delete"))
    			{
    				for(int i=0;i<num;i++)
    				{
    					beanManager.deleteSingle("correlation",fr.getPrimaryKey(),strIds[i]);
    					fr.removeRecord(strIds[i]);
    				}
    				return "correlations_main.jsp";
    			}
    			else if(action.equals("view"))
    			{
    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    				fr.findRecordByPrimaryKey(ids);
    				return "correlation_main.jsp";
    			}
    			else
    				throw new Exception("Unsupported action: "+action);
    		}    		
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle events.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if (selectedScreen.equals("/events.go"))
    	{
    		String action=request.getParameter("act");
    		event_Form fr=(event_Form)session.getAttribute("event_form");
    		if(action==null) throw new Exception("No action is specified.");
    		
    		if(fr==null)throw new Exception("No event form is created.");
    		String ids=request.getParameter("id");
    		if(ids==null) throw new Exception("No correlation id(s) specified");
    			
    		String [] strIds=ids.split(",");
	    	int num=strIds.length;
    		if(action.equals("view"))
    		{
    			if(num!=1)throw new Exception("Invalid parameter: "+ ids);
    			fr.findRecordByPrimaryKey(ids);
    			return "event_main.jsp";
    		}
    		else
    			throw new Exception("Unsupported action: "+action);
    		    		
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle usrs.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if (selectedScreen.equals("/usrs.go"))
    	{
    		
    	}
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                                                              handle peer-viewer.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	else if(selectedScreen.equals("/peer-viewer.go"))
			 {
		 	 	String action=request.getParameter("act");
		 		
		 		if(action==null)
		 			throw new Exception("No action is specified.");
		 		else if(action.equals("accept"))
		 		{
		 			
		 			String id=request.getParameter("id");
		 			if(id==null) throw new Exception("Invalid parameter");
		 			
		 			String [] ids=id.split(",");
		 			int num=ids.length;
		 			if(num==0)  throw new Exception("Invalid parameter");
		 			
		 			event_Form pForm1=new event_Form();
		 			Assignments asigns=(Assignments)session.getAttribute("assignments");
		 			if(asigns==null) throw new Exception("No assignments bean created");
		 			event_Form pForm=new event_Form();
		 			
		 			pForm.clearForm();
		 			pForm.setUSR_ID(cur_usr_id);
				 	
				 	pForm.setROLE_ID("1");
				 	Calendar date=Calendar.getInstance();
					int day = date.get(Calendar.DATE);
					int month = date.get(Calendar.MONTH);
					int year = date.get(Calendar.YEAR);
					String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
						
			 		pForm.setEventDate(dt);
					pForm.setACTION_ID("accepted");
		 			for(int i=0;i<num;i++)
		 			{
		 				pForm1.findRecordByPrimaryKey(ids[i]);
		 				String proposalId=pForm1.getPROPOSAL_ID();
		 				pForm.setPROPOSAL_ID(proposalId);
		 				pForm.addRecord();
		 				asigns.removeAssignmentById(ids[i]);
		 			//	pForm1.findRecordBySQL("select * from event where ACTION_ID='assigned' AND PROPOSAL_ID='"+proposalId+
		 			//		"' AND SUBJECTUSR_ID='"+cur_usr_id+"'");
		 				pForm1.setReviewText("accepted");
		 				pForm1.updateRecord();
		 			}
		 			
					return "assignments_main.jsp";
		 			
		 		}
		 		else if(action.equals("decline"))
		 		{
		 			String id=request.getParameter("id");
		 			if(id==null) throw new Exception("Invalid parameter");
		 			
		 			String [] ids=id.split(",");
		 			int num=ids.length;
		 			if(num==0)  throw new Exception("Invalid parameter");
		 			
		 			event_Form pForm1=new event_Form();
		 			Assignments asigns=(Assignments)session.getAttribute("assignments");
		 			if(asigns==null) throw new Exception("No assignments bean created");
		 			
		 			event_Form pForm=new event_Form();
		 			
		 			pForm.clearForm();
		 			pForm.setUSR_ID(cur_usr_id);
				 	
				 	pForm.setROLE_ID("1");
				 	Calendar date=Calendar.getInstance();
					int day = date.get(Calendar.DATE);
					int month = date.get(Calendar.MONTH);
					int year = date.get(Calendar.YEAR);
					String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
						
			 		pForm.setEventDate(dt);
					pForm.setACTION_ID("declined");
		 			for(int i=0;i<num;i++)
		 			{
		 				pForm1.findRecordByPrimaryKey(ids[i]);
		 				String proposalId=pForm1.getPROPOSAL_ID();
		 				pForm.setPROPOSAL_ID(proposalId);
		 				pForm.addRecord();
		 				asigns.removeAssignmentById(ids[i]);
		 			//	pForm1.removeRecord(ids[i]);
		 			//	pForm1.findRecordBySQL("select * from event where ACTION_ID='assigned' AND PROPOSAL_ID='"+proposalId+
		 			//		"' AND SUBJECTUSR_ID='"+cur_usr_id+"'");
		 				pForm1.setReviewText("declined");
		 				pForm1.updateRecord();
		 			}
		 			
					return "assignments_main.jsp";
		 			
		 		}
		 		
		 		else
		 			throw new Exception("Unsupported action: "+action);
		 	 }
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	////                                       POST                                   handle manager.go
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	
    	else if(selectedScreen.equals("/manager.go"))
		 	 {
		 	 	String target=request.getParameter("target");
		 	 	String action=request.getParameter("act");
		 		
		 	
		 		if(action==null) throw new Exception("Null parameters.");
					
		 		if(action.equals("editrole"))
		 		{
		 			String id=request.getParameter("id");
		 			if(id==null) throw new Exception("Invalid parameter");
		 			usr_Form ufr=(usr_Form)session.getAttribute("usr_form");
			 		if(ufr==null)
			 		{
			 			ufr=new usr_Form();
			 			session.setAttribute("usr_form",ufr);
			 		}	
			 		ufr.findRecordByPrimaryKey(id);
			 		return "roles_main.jsp";
		 		}
		 		else if(action.equals("system_settings"))
		 		{
		 			updateSystemSettings(request);
		 			beanManager.setMessage("Successfully saved");
					return "system_setting_main.jsp";
		 		}
		 		else if(action.equals("query"))
		 		{
		 			if(target==null)
		 				throw new Exception("Invalid parameters.");
		 			if(target.equals("proposal"))
			 		{
			 		
				 		if(action.equals("query"))
				 		{
				 			String con=request.getParameter("con");
				 			proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
				 			if(pForm==null)
				 			{
				 				pForm=new proposal_Form();
				 				session.setAttribute("proposal_form",pForm);
				 			}
				 				
				 			String strSql="select * from proposal where"; 
				 			if(con!=null)
				 			{
					 			if(con.equals("submitted"))
					 				strSql+=" current_status='submitted'";
					 			else if(con.equals("assigned"))
					 				strSql+=" current_status='assigned'";
					 			else if(con.equals("evaluating"))
					 				strSql+=" current_status='evaluating'";
					 			else if(con.equals("evaluated"))
					 				strSql+=" current_status='evaluated'";
					 			else if(con.equals("approved"))
					 				strSql+=" current_status='approved'";
					 			else if(con.equals("declined"))
					 				strSql+=" current_status='declined'";
					 			else if(con.equals("search"))
					 				return "search_proposal_main.jsp";
					 			else
					 				throw new Exception("Unsupported action: query with condition: "+con);
				 			}	
				 			else
				 				throw new Exception("No condition is specified.");
				 			
				 			pForm.findRecords(strSql);
				 			return "proposals_main.jsp";
				 		}
				 	}
				 	else if(target.equals("usr"))
			 		{
			 			throw new Exception("Action in implementation");
			 		}
				 }
			 	 else if(action.equals("assign"))
			 	 {
			 		String step=request.getParameter("step");
			 		String ids=request.getParameter("id");
			 		if(step==null || ids==null)
			 		{
			 			beanManager.setProposalIds(cur_proposal_id);
			 			usr_Form ufr=(usr_Form)session.getAttribute("usr_form");
			 			if(ufr==null)
			 			{
			 				ufr=new usr_Form();
			 				session.setAttribute("usr_form",ufr);
			 			}	
			 			ufr.findRecords("select * from usr where permission='1' OR permission='3' OR permission='5' OR permission='6'");
			 			return "choose_peerviewers.jsp";
			 		}
			 		else if(step.equals("1")) //shoose proposals
			 		{
			 			beanManager.setProposalIds(ids);
			 			usr_Form ufr=(usr_Form)session.getAttribute("usr_form");
			 			if(ufr==null)
			 			{
			 				ufr=new usr_Form();
			 				session.setAttribute("usr_form",ufr);
			 			}	
			 			ufr.findRecords("select * from usr where permission='1' OR permission='3' OR permission='5' OR permission='6'");
			 			return "choose_peerviewers.jsp";
			 		}
			 		else if(step.equals("2"))
			 		{
			 			beanManager.setUsrIds(ids);
			 			beanManager.assign();
			 			beanManager.setMessage("Successfully assigned to selected peer-viewers");
			 			return "manager.go?target=proposal&action=query&con=assigned";
			 		}
			 		else
			 			throw new Exception("Invalid step for assignment: "+step);
			 	
			 	}
			 	else if(action.equals("delete"))
			 	{
			 		if(target==null)
		 				throw new Exception("Invalid parameters.");
		 			if(target.equals("role"))
			 		{
			 			String id=request.getParameter("id");
			 			String role=request.getParameter("role");
			 			if(id==null || role==null)
			 				throw new Exception("Invalid parameters.");
			 			else
			 			{
			 				usr_Form ufr=(usr_Form)session.getAttribute("usr_form");
			 				if(ufr==null)
			 				{
			 					ufr=new usr_Form();
			 					session.setAttribute("usr_form",ufr);
			 				}	
			 				ufr.findRecordByPrimaryKey(id);
			 				String pm=ufr.getPermission();
			 				if(role.equals("Author"))
			 				{
			 					if(pm.equals("0")) ufr.setPermission("-1");
				 				else if(pm.equals("3")) ufr.setPermission("1");
				 				else if(pm.equals("4")) ufr.setPermission("2");
				 				else if(pm.equals("6")) ufr.setPermission("5");
				 				else throw new Exception("User does not have the role you want to delete: "+role);
				 			}
				 			else if(role.equals("Peer-viewer"))
			 				{
			 					if(pm.equals("1")) ufr.setPermission("-1");
				 				else if(pm.equals("3")) ufr.setPermission("0");
				 				else if(pm.equals("5")) ufr.setPermission("2");
				 				else if(pm.equals("6"))ufr.setPermission("4");
				 				else throw new Exception("User does not have the role you want to delete: "+role);
				 			}
				 			else if(role.equals("Manager"))
			 				{
			 					if(pm.equals("2")) ufr.setPermission("-1");
				 				else if(pm.equals("4")) ufr.setPermission("0");
				 				else if(pm.equals("5")) ufr.setPermission("1");
				 				else if(pm.equals("6")) ufr.setPermission("3");
				 				else throw new Exception("User does not have the role you want to delete: "+role);
				 			}
				 			else 
				 				throw new Exception("Invlid role: "+role);
			 				
			 				ufr.updateRecord();
			 				return "roles_main.jsp";
			 			}
			 		}
			 		else
			 			throw new Exception("Invalid parameters.");
			 	}
			 	else if(action.equals("add"))
			 	{
			 		if(target==null)
		 				throw new Exception("Invalid parameters.");
		 			if(target.equals("role"))
			 		{
			 			String id=request.getParameter("id");
			 			String role=request.getParameter("role");
			 			if(id==null || role==null)
			 				throw new Exception("Invalid parameters.");
			 			else
			 			{
			 				usr_Form ufr=(usr_Form)session.getAttribute("usr_form");
			 				if(ufr==null)
			 				{
			 					ufr=new usr_Form();
			 					session.setAttribute("usr_form",ufr);
			 				}	
			 				ufr.findRecordByPrimaryKey(id);
			 				String pm=ufr.getPermission().trim();
			 				if(role.equals("Author"))
			 				{
			 					if(pm.equals("1")) ufr.setPermission("3");
				 				else if(pm.equals("2")) ufr.setPermission("4");
				 				else if(pm.equals("5")) ufr.setPermission("6");
				 				else if(pm.equals("-1")) ufr.setPermission("0");
				 				else throw new Exception("User already has the role you want to add: "+role+"with id: "+id);
				 			}
				 			else if(role.equals("Peer-viewer"))
			 				{
			 					if(pm.equals("0")) ufr.setPermission("3");
				 				else if(pm.equals("2")) ufr.setPermission("5");
				 				else if(pm.equals("4")) ufr.setPermission("6");
				 				else if(pm.equals("-1")) ufr.setPermission("1");
				 				else throw new Exception("User already has the role you want to add: "+role+"with id: "+id);
				 			}
				 			else if(role.equals("Manager"))
			 				{
			 					if(pm.equals("0")) ufr.setPermission("4");
				 				else if(pm.equals("1")) ufr.setPermission("5");
				 				else if(pm.equals("3")) ufr.setPermission("6");
				 				else if(pm.equals("-1")) ufr.setPermission("2");
				 				else throw new Exception("User already has the role you want to add: "+role+"with id: "+id);
				 			}
				 			else 
				 				throw new Exception("Invlid role: "+role);
			 				
			 				ufr.updateRecord();
			 				return "roles_main.jsp";
			 			}
			 		}
			 		else
			 			throw new Exception("Invalid parameters.");
			 	}
			 	
		 	 }
    	else
    	{
    		throw new Exception("Unsupported action: "+selectedScreen);
    	}
    	throw new Exception("Unsupported action: "+selectedScreen);
	}
	private String handleGet(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
				
			HttpSession session = request.getSession();
			String selectedScreen = request.getServletPath();
	    	BeanManager beanManager = (BeanManager)session.getAttribute("beanManager");
	    	if(beanManager!=null)beanManager.setSystemManager(sysManager);
    	
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
			   
			   
			   	fr.clearForm();
			   	fr.setRole("Author");
			   	fr.setPermission("0");
			   	beanManager.setUserCurrentRole("Author");
			   	
			   	return "register_main.jsp";
			   
			}
			if(selectedScreen.equals("/menu.go"))
			{
				String role=request.getParameter("role");
				if(role==null) role="Home";
				if(beanManager!=null)
				{
					beanManager.setCurMenu(role);
					beanManager.setUserCurrentRole(role);
					
					if(role.equals("Author"))
					{
						proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
			 			if(pForm==null)
			 			{
			 				pForm=new proposal_Form();
			 				session.setAttribute("proposal_form",pForm);
			 			}
			 				
			 			String strSql="select proposal.* from proposal,event where event.USR_ID='"+beanManager.getUserId()+"'"; 
			 			strSql+=" AND event.PROPOSAL_ID=proposal.PROPOSAL_ID";
			 			strSql+=" AND proposal.current_status='unsubmitted' AND event.ACTION_ID='unsubmitted'";
			 			pForm.findRecords(strSql);
				
					}
					else if(role.equals("Peer-viewer"))
					{
						beanManager.setUserCurrentRole(role);
						Assignments asgns=(Assignments)session.getAttribute("assignments");
			 			if(asgns==null)
			 			{
			 				asgns=new Assignments();
			 				session.setAttribute("assignments",asgns);
			 			}
			 			asgns.reset();
			 			asgns.setPeerviewerId(beanManager.getUserId());
			 			asgns.setManager(beanManager);
			 			
			 			asgns.addStatus(Assignment.STATUS_NEW);
				 		asgns.addStatus(Assignment.STATUS_ACCEPTED);
				 		asgns.addStatus(Assignment.STATUS_EVALUATING);
			 			asgns.getAssignmentsByPeerviewer();
					}
					if(role.equals("Manager"))
					{
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
					
				}
				else
				 	session.setAttribute("menu",role);
				
				 	
				if(role.equals("Home")) return "index.jsp";
				if(role.equals("About")) return "index.jsp";
				return "main.jsp";
				
			}
	
	    	
	     	if(selectedScreen==null)
	     		throw new Exception("No path is defined.");
	     	if(!isAllowedAction(selectedScreen,beanManager))
				throw new Exception("Sorry! <b>Please login or register first.</b>");
			
			beanManager.getErrors().clear();
			String cur_proposal_id=beanManager.getProposalId();
			String cur_type_id=beanManager.getTypeId();
			String cur_usr_id=beanManager.getUserId();
				    
	    	if (selectedScreen.equals("/logoff.go")) 
		   	{
			   	beanManager.clear();
			   	session.removeAttribute("beanManager");
			   	removeAllFormBeans(session);
			   	
			   	return "index.jsp";
			}
	    	
	    	
			if(selectedScreen.equals("/proposals.go"))
    		{
	    		String action=request.getParameter("act");
	    		proposal_Form fr=(proposal_Form)session.getAttribute("proposal_form");
	    		if(fr==null)
	    		{
	    			fr=new proposal_Form();
	    			session.setAttribute("proposal_form",fr);
	    		}
	    		if(action==null) throw new Exception("No action is specified.");
	    		if(action.equals("add"))
	    		{
	    			fr.clearForm();
	    			fr.setCurrent_status("unsubmitted");
	    			beanManager.setProposalId(BeanManager.UNKNOWN_ID);
	    			beanManager.setProposalOwnerId(beanManager.getUserId());
	    			beanManager.setProposalStatus("unsubmitted");
	    			return "proposal_main.jsp";
	    		}
	    		else
	    		{
	    			
	    			String ids=request.getParameter("id");
	    			if(ids==null) throw new Exception("No proposal id(s) specified");
	    			
	    			String [] strIds=ids.split(",");
		    		int num=strIds.length;
	    			if(action.equals("delete"))
	    			{
	    				for(int i=0;i<num;i++)
	    				{
	    					beanManager.deleteProposal(strIds[i]);
	    					fr.removeRecord(strIds[i]);
	    				}
	    				return "proposals_main.jsp";
	    			}
	    			else if(action.equals("view"))
	    			{
	    				if(num!=1)throw new Exception("Invalid parameter: "+ ids);
	    				fr.findRecordByPrimaryKey(ids);
	    				beanManager.setProposalId(ids);
	    				beanManager.setProposalStatus(fr.getCurrent_status());
	    				return "proposal_main.jsp";
	    			}
	    			else
	    				throw new Exception("Unsupported action: "+action);
	    		}
	    	}
			else if(selectedScreen.equals("/usredit.go"))
			{
				usr_Form fr=(usr_Form)session.getAttribute("usr_form");
		 	 	if(fr==null)
		 	 	{
		 	 		fr=new usr_Form();
		 	 		session.setAttribute("usr_form",fr);
		 	 	}
		 	 	fr.findRecordByPrimaryKey(beanManager.getUserId());
		 	 	return "userinfo.jsp";
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    		////    GET                 handle project.go
    		////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	    	else if(selectedScreen.equals("/project.go"))
	    	{
	    		
		    	project_Form form=(project_Form)session.getAttribute("project_form");
					
				if(form==null) throw new Exception("No project form bean created.");
			 	
				String target=request.getParameter("target");
				if(target==null)throw new Exception("Invalid parameter");
				
				if(target.equals("document"))  
				{
				
					String act=request.getParameter("act");	
						
					if(act==null) throw new Exception("No action has been specified.");
					else if(act.equals("upload"))
						return handleUpload("project",request,response);
					else if(act.equals("delete"))
					{
						String doc1=form.getDocument1();
						String doc2=form.getDocument2();
						String doc3=form.getDocument3();
						String id=request.getParameter("id");	
						
						if(id==null) throw new Exception("Invalid project");
						else if(id.equals("1") && doc1.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc1;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument1(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "project_document_main.jsp";
						}
						else if(id.equals("2") && doc2.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc2;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument2(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "project_document_main.jsp";
						}
						else if(id.equals("3") && doc3.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc3;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument3(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "project_document_main.jsp";
						}
						else
							throw new Exception("Invalid parameter");
					}
					else
						throw new Exception("Invalid parameter");			
				}
				else
					throw new Exception("Invalid parameter");
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    		////    GET                 handle event.go
    		////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	    	else if(selectedScreen.equals("/event.go"))
	    	{
	    		
		    	event_Form form=(event_Form)session.getAttribute("event_form");
					
				if(form==null) throw new Exception("No event form bean created.");
			 	
				String target=request.getParameter("target");
				if(target==null)throw new Exception("Invalid parameter");
				
				if(target.equals("document"))  
				{
				
					String act=request.getParameter("act");	
						
					if(act==null) throw new Exception("No action has been specified.");
					else if(act.equals("upload"))
						return handleUpload("event",request,response);
					else if(act.equals("delete"))
					{
						String doc1=form.getDocument1();
						String doc2=form.getDocument2();
						String doc3=form.getDocument3();
						String id=request.getParameter("id");	
						
						if(id==null) throw new Exception("Invalid event");
						else if(id.equals("1") && doc1.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc1;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument1(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "event_document_main.jsp";
						}
						else if(id.equals("2") && doc2.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc2;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument2(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "event_document_main.jsp";
						}
						else if(id.equals("3") && doc3.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc3;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument3(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "event_document_main.jsp";
						}
						else
							throw new Exception("Invalid parameter");
					}
					else
						throw new Exception("Invalid parameter");			
				}
				else
					throw new Exception("Invalid parameter");
			}	
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    		////    GET                 handle proposal.go
    		////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	    	else if(selectedScreen.equals("/proposal.go"))
	    	{
	    		
		    	
		    	proposal_Form form=(proposal_Form)session.getAttribute("proposal_form");
					
				if(form==null) throw new Exception("No proposal form bean created.");
			 	
				String target=request.getParameter("target");
				if(target==null||target.equals("proposal"))
					return "proposal_main.jsp";
				else if(target.equals("document"))  
				{
				
					String act=request.getParameter("act");	
						
					if(act==null) throw new Exception("No action has been specified.");
					else if(act.equals("upload"))
						return handleUpload("proposal",request,response);
					else if(act.equals("delete"))
					{
						String doc1=form.getDocument1();
						String doc2=form.getDocument2();
						String doc3=form.getDocument3();
						String id=request.getParameter("id");	
						
						if(id==null) throw new Exception("Invalid parameter");
						else if(id.equals("1") && doc1.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc1;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument1(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "proposal_document_main.jsp";
						}
						else if(id.equals("2") && doc2.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc2;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument2(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "proposal_document_main.jsp";
						}
						else if(id.equals("3") && doc3.trim().length()>0)
						{
							String strFile=document_path+"\\"+doc3;
			           		File file=new File(strFile);
			           		file.delete();
			           		form.setDocument3(BeanManager.EMPTY_VALUE);
			           		form.updateRecord();
			           		return "proposal_document_main.jsp";
						}
						else
							throw new Exception("Invalid parameter");
					}
				}
				else if(target.equals("project"))  
		 		{	
		 			String projectid=form.getPROJECT_ID();
		 			
		 			project_Form pjForm=(project_Form)session.getAttribute("project_form");
		 			if(pjForm==null)
		 			{
		 				pjForm=new project_Form();
		 				session.setAttribute("project_form",pjForm);
		 			}
		 			
		 			pjForm.clearForm();
		 			
		 			if(!projectid.equals(BeanManager.UNKNOWN_ID))
		 			{
		 				
			 			try
			 			{
				   			pjForm.findRecordByPrimaryKey(projectid);
				   		}
					   	catch(Exception e)
					   	{
					   		throw e;
				   		}
				   	}
				   	
			   		return "project_main.jsp";
			   }
			   else if(target.equals("assignments"))
			   {
			   		Assignments asgns=(Assignments)session.getAttribute("assignments");
		 			if(asgns==null)
		 			{
		 				asgns=new Assignments();
		 				session.setAttribute("assignments",asgns);
		 			}
		 			asgns.reset();
		 			asgns.setProposalId(cur_proposal_id);
		 			asgns.setManager(beanManager);
		 			
		 			asgns.getAssignmentsByProposal();
		 			return "assignments_proposal_main.jsp";
		 	   }	
			   else if(target.equals("types"))
		 	   {	 	
		 					 			
		 			type_Form tpform=(type_Form)session.getAttribute("type_form");
		 			correlation_Form corform=(correlation_Form)session.getAttribute("correlation_form");
		 			if(tpform==null)
		 			{
		 				tpform=new type_Form();
		 				session.setAttribute("type_form",tpform);
		 			}
		 			
		 			String sql="select * from type where PROPOSAL_ID="+beanManager.getProposalId();
		 			
		 			try{tpform.findRecords(sql);}
		 			catch(Exception ex){throw ex;}
		 			
		 			tpform.setPROPOSAL_ID(beanManager.getProposalId());
		 			
		 				 			
		 			return "types_main.jsp";
			   		
		 		}
			   else if(target.equals("correlations"))
		 	   {	 	
		 					 			
		 			type_Form tpform=(type_Form)session.getAttribute("type_form");
		 			correlation_Form corform=(correlation_Form)session.getAttribute("correlation_form");
		 			if(tpform==null)throw new Exception("No type form created.");
		 			
		 			if(corform==null)
		 			{
		 				corform=new correlation_Form();
		 				session.setAttribute("correlation_form",corform);
		 			}
		 			
		 			
		 			
		 			
		 			ArrayList rcds=tpform.getRecords();
		 			
		 			String sql="";
		 			for(int i=0;i<rcds.size();i++)
		 			{
		 				Hashtable tb=(Hashtable)rcds.get(i);
		 				if(i==0)sql="select * from correlation where ";
		 				if(i>0)sql+=" OR ";
		 				sql+=" TYPE_ID="+(String)tb.get("TYPE_ID");
		 			}	
		 			
		 			if(sql.length()>0)
		 			{
		 			
		 				try{corform.findRecords(sql);}
		 				catch(Exception ex){throw ex;}
		 			}
		 			
		 				 			
		 			return "correlations_main.jsp";
			   		
		 		}		 		
		 		else if(target.equals("events"))
		 		{
		 			event_Form evform=(event_Form)session.getAttribute("event_form");
		 			String con=request.getParameter("con");
	
		 			if(evform==null)
		 			{
		 				evform=new event_Form();
		 				session.setAttribute("event_form",evform);
		 			}
		 			
		 			String sql="select * from event where PROPOSAL_ID="+beanManager.getProposalId();
		 			if(con!=null) 
		 			{
		 				beanManager.setSomething(con);
		 				sql+="AND ACTION_ID='"+ con +"'";
		 			}
		 			
		 			evform.findRecords(sql);
		 			
		 			return "events_main.jsp";
		 		}
		 		else if(target.equals("author"))  
		 		{	
		 			usr_Form uForm=(usr_Form)session.getAttribute("usr_form");
		 			if(uForm==null) throw new Exception("No usr form bean created.");
		 			
		 			try
		 			{
			   			String id=beanManager.getAuthorOfProposal(beanManager.getProposalId());
			   			uForm.findRecordByPrimaryKey(id);
			   		}
				   	catch(Exception e)
				   	{
				   		throw e;	
			   		}
			   		
			   		return "author_main.jsp";
			   }
			   else if(target.equals("evaluation"))  
		 		{	
		 			Evaluations es=(Evaluations)session.getAttribute("evaluations");
		 			if(es==null)
		 			{
		 				es=new Evaluations();
		 				session.setAttribute("evaluations",es);
		 			}
		 			
		 			try
		 			{
		 				es.setEvaluations(beanManager.getEvaluationsByProposal(cur_proposal_id));
		 			}
		 			catch(Exception e)
				   	{
				   		throw e;	
			   		}
			   		
			   		return "evaluations_main.jsp";
			   }
			   else if(target.equals("submit"))
		 		{
		 			String proposalId=beanManager.getProposalId();
		 			proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
		 			if(pForm==null) throw new Exception("No proposal form bean creates.");
					
		 			event_Form fm=new event_Form();
		 			
						
					Calendar date=Calendar.getInstance();
					int day = date.get(Calendar.DATE);
					int month = date.get(Calendar.MONTH);
					int year = date.get(Calendar.YEAR);
					String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
						
		 			fm.setEventDate(dt);
					fm.setPROPOSAL_ID(proposalId);
					fm.setROLE_ID("0");
					fm.setUSR_ID(beanManager.getUserId());
					fm.setACTION_ID("submitted");
					pForm.setCurrent_status("submitted");
					pForm.updateRecord();
					fm.addRecord();
					
					beanManager.setProposalStatus("submitted");
					
					sysManager.handleEvent(SystemManager.EVENT_PROPOSAL_SUBMITTED,fm,null,null);
					
					return "submit_main.jsp";
				
		 	   }
		 	   else if(target.equals("revise"))
		 		{
		 			String proposalId=beanManager.getProposalId();
		 			proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
		 			if(pForm==null) throw new Exception("No proposal form bean creates.");
					
		 			pForm.findRecordBySQL("select * from proposal where previousProposal_ID='"+proposalId+"'");
					beanManager.setProposalId(pForm.getPROPOSAL_ID());
					beanManager.setProposalStatus(pForm.getCurrent_status());
					return "proposal_main.jsp";
				
		 	   }
		 		else
		 		   	throw new Exception("Unsupported Action");
					
		 	}
		 	else if(selectedScreen.equals("/type.go"))
		 	{
		 		   	
			   	type_Form tpForm=(type_Form)session.getAttribute("type_form");
				if(tpForm==null) throw new Exception("No type form bean created.");
						
				String typeId=tpForm.getFieldValue(tpForm.getPrimaryKey());
				
				String target=request.getParameter("target");
				
				if(target==null||target.equals("type"))
				{
					beanManager.setSomething("nothing");
		 			return "type_main.jsp";
		 		}
				else
				{
					String frName=target.trim();
					beanManager.setSomething(frName);
					frName=frName.substring(0,frName.length()-1);
					SuperForm fm=(SuperForm)session.getAttribute(frName+"_form");
					if(fm==null)
					{
						if(target.equals("plots"))
							fm=new plot_Form();
						else if(target.equals("compositions"))
							fm=new composition_Form();
						else if(target.equals("distributions"))
							fm=new distribution_Form();
						else if(target.equals("typenames"))
							fm=new typename_Form();
						else if(target.equals("typereferences"))
							fm=new typereference_Form();	
						else
							throw new Exception("Unsupported action: " + selectedScreen+"with target: "+target);
						
						session.setAttribute(frName+"_form",fm);
					}
					
					String sql="select * from " + frName +" where TYPE_ID='"+typeId+"'";
		 			fm.findRecords(sql);
		 			fm.setFieldValue("TYPE_ID",typeId);
		 			
		 			return target+"_main.jsp";
				}
				
			 }
		 	 else if(selectedScreen.equals("/usr.go"))
		 	 {
		 	 	usr_Form fr=(usr_Form)session.getAttribute("usr_form");
		 	 	if(fr==null)
		 	 	{
		 	 		fr=new usr_Form();
		 	 		session.setAttribute("usr_form",fr);
		 	 	}
		 	 	fr.findRecordByPrimaryKey(beanManager.getUserId());
		 	 	return "userinfo.jsp";
		 	 }
		 	 else if(selectedScreen.equals("/author.go"))
		 	 {
		 	 	String action=request.getParameter("act");
		 		
		 		if(action==null)
		 			throw new Exception("No action is specified.");
		 		else if(action.equals("new"))  //add new proposal
		 		{
		 			proposal_Form form=new proposal_Form();
		 			form.clearForm();
		 			form.setCurrent_status("unsubmitted");
		 			beanManager.setProposalOwnerId(beanManager.getUserId());
		 			beanManager.setProposalStatus("unsubmitted");
		 			beanManager.setProposalId(BeanManager.UNKNOWN_ID);
		 			session.setAttribute("proposal_form",form);
		 			return "proposal_main.jsp";
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
		 				
		 			String strSql="select proposal.* from proposal,event where event.USR_ID='"+beanManager.getUserId()+"'"; 
		 			strSql+=" AND event.PROPOSAL_ID=proposal.PROPOSAL_ID";
		 			if(con!=null)
		 			{
			 			if(con.equals("unsubmitted"))
			 				strSql+=" AND proposal.current_status='unsubmitted' AND event.ACTION_ID='"+con+"'";
			 			else if(con.equals("submitted"))
			 				strSql+=" AND proposal.current_status='submitted' AND event.ACTION_ID='"+con+"'";
			 			else if(con.equals("processing"))
			 				strSql+=" AND (proposal.current_status='evaluated' OR proposal.current_status='assigned' OR proposal.current_status='evaluating') AND event.ACTION_ID='submitted'";
			 			else if(con.equals("approved"))
			 				strSql+=" AND proposal.current_status='approved' AND event.ACTION_ID='submitted'";
			 			else if(con.equals("declined"))
			 				strSql+=" AND proposal.current_status='declined' AND event.ACTION_ID='submitted'";
			 			else if(con.equals("revision"))
			 				strSql+=" AND proposal.current_status='revision' AND event.ACTION_ID='submitted'";
			 			else if(con.equals("search"))
			 				return "search_proposal_main.jsp";
			 			else
			 				throw new Exception("Unsupported action: query with condition: "+con);
		 			}	
		 			else
		 				throw new Exception("No condition is specified.");
		 			
		 			pForm.findRecords(strSql);
		 			return "proposals_main.jsp";
		 		}
		 		
		 	 }
		 	 else if(selectedScreen.equals("/peer-viewer.go"))
		 	 {
		 	 	String action=request.getParameter("action");
		 		
		 		if(action==null)
		 			throw new Exception("No action is specified.");
		 		else if(action.equals("query"))  
		 		{
		 			String con=request.getParameter("con");
		 			event_Form pForm=(event_Form)session.getAttribute("event_form");
		 			if(pForm==null)
		 			{
		 				pForm=new event_Form();
		 				session.setAttribute("event_form",pForm);
		 			}
		 				
		 			String strSql="";
		 			
		 			Assignments asgns=(Assignments)session.getAttribute("assignments");
		 			if(asgns==null)
		 			{
		 				asgns=new Assignments();
		 				session.setAttribute("assignments",asgns);
		 			}
		 			asgns.reset();
		 			asgns.setPeerviewerId(beanManager.getUserId());
		 			asgns.setManager(beanManager);
		 			
		 			
		 			if(con!=null)
		 			{
			 			if(con.equals("new")) asgns.addStatus(Assignment.STATUS_NEW);
			 			else if(con.equals("accepted"))asgns.addStatus(Assignment.STATUS_ACCEPTED);
			 			else if(con.equals("completed"))asgns.addStatus(Assignment.STATUS_COMPLETED);
			 			else if(con.equals("evaluating"))asgns.addStatus(Assignment.STATUS_EVALUATING);
			 			else if(con.equals("declined"))asgns.addStatus(Assignment.STATUS_DECLINED);
			 			else if(con.equals("search"))
			 				return "search_assignment_main.jsp";
			 			else
			 				throw new Exception("Unsupported action: query with condition: "+con);
		 			}	
		 			else
		 				throw new Exception("No condition is specified.");
		 			
		 			asgns.getAssignmentsByPeerviewer();
		 			return "assignments_main.jsp";
		 		}
		 		else if(action.equals("evaluate"))
		 		{
		 			event_Form pForm=(event_Form)session.getAttribute("event_form");
		 			if(pForm==null)
		 			{
		 				pForm=new event_Form();
		 				session.setAttribute("event_form",pForm);
		 			}
		 			pForm.clearForm();
		 			String sql="select * from event where PROPOSAL_ID='"+cur_proposal_id+"' AND USR_ID='"+
		 				cur_usr_id+"' AND ACTION_ID='evaluating'";
		 			
		 			try{
		 				pForm.findRecordBySQL(sql);
		 			}
		 			catch(Exception e)
		 			{
		 				if(e.getMessage().indexOf("Failed to find the record")>-1) 
		 				{
		 					pForm.setUSR_ID(cur_usr_id);
				 			pForm.setPROPOSAL_ID(cur_proposal_id);
				 			pForm.setROLE_ID("1");
				 			Calendar date=Calendar.getInstance();
							int day = date.get(Calendar.DATE);
							int month = date.get(Calendar.MONTH);
							int year = date.get(Calendar.YEAR);
							String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
							
			 				pForm.setEventDate(dt);
							pForm.setACTION_ID("evaluating");
		 				}
		 				else
		 					throw e;
		 					
		 			}
		 			
					return "evaluate_main.jsp";
		 			
		 		}
		 		else if(action.equals("accept"))
		 		{
		 			event_Form pForm=(event_Form)session.getAttribute("event_form");
		 			
		 			if(pForm==null)
		 			{
		 				pForm=new event_Form();
		 				session.setAttribute("event_form",pForm);
		 			}
		 			
		 			pForm.clearForm();
		 			pForm.setUSR_ID(cur_usr_id);
				 	pForm.setPROPOSAL_ID(cur_proposal_id);
				 	pForm.setROLE_ID("1");
				 	Calendar date=Calendar.getInstance();
					int day = date.get(Calendar.DATE);
					int month = date.get(Calendar.MONTH);
					int year = date.get(Calendar.YEAR);
					String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
						
			 		pForm.setEventDate(dt);
					pForm.setACTION_ID("accepted");
		 			pForm.addRecord();
		 			
		 			pForm.findRecordBySQL("select * from event where PROPOSAL_ID='"+cur_proposal_id+"' AND SUBJECTUSR_ID='"+cur_usr_id + 
  		 								"' AND ACTION_ID='assigned'");
					pForm.setReviewText("accepted");
					pForm.updateRecord();
					
					return "proposal_main.jsp";
		 			
		 		}
		 		else if(action.equals("decline"))
		 		{
		 			event_Form pForm=(event_Form)session.getAttribute("event_form");
		 			if(pForm==null)
		 			{
		 				pForm=new event_Form();
		 				session.setAttribute("event_form",pForm);
		 			}
		 			pForm.clearForm();
		 			pForm.setUSR_ID(cur_usr_id);
				 	pForm.setPROPOSAL_ID(cur_proposal_id);
				 	pForm.setROLE_ID("1");
				 	Calendar date=Calendar.getInstance();
					int day = date.get(Calendar.DATE);
					int month = date.get(Calendar.MONTH);
					int year = date.get(Calendar.YEAR);
					String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
						
			 		pForm.setEventDate(dt);
					pForm.setACTION_ID("declined");
		 			pForm.addRecord();
		 			
  		 			pForm.findRecordBySQL("select * from event where PROPOSAL_ID='"+cur_proposal_id+"' AND SUBJECTUSR_ID='"+cur_usr_id + 
  		 								"' AND ACTION_ID='assigned'");
					pForm.setReviewText("declined");
					pForm.updateRecord();
					
					
					return "main.jsp";
		 			
		 		}
		 		
		 		else
		 			throw new Exception("Unsupported action: "+action);
		 	 }
		 	 else if (selectedScreen.equals("/assignments.go"))
		 	 {
		 	 	String target=request.getParameter("target");
		 	 	String id=request.getParameter("id");
		 	 	if(target==null || id ==null)
		 			throw new Exception("Invalid parameters.");
		 		if(target.equals("peer-viewer"))
		 		{
		 			usr_Form uf=(usr_Form)session.getAttribute("usr_form");
		 			if(uf==null)
		 			{
		 				uf=new usr_Form();
		 			}
		 			uf.findRecordByPrimaryKey(id);
		 			return "author_main.jsp";
		 		}
		 		else
		 			throw new Exception("Unspported action: /assignments.go?"+target);
		 		
		 	 }
		 	 else if(selectedScreen.equals("/manager.go"))
		 	 {
		 	 	String target=request.getParameter("target");
		 	 	String action=request.getParameter("action");
		 		String con=request.getParameter("con");
		 	
		 		if(action==null)
		 			throw new Exception("Invalid parameters.");
		 		
		 		if(action.equals("query"))
		 		{
		 			if(target==null)
		 				throw new Exception("Invalid parameters.");
		 			if(target.equals("proposal"))
			 		{
			 		
				 		if(action.equals("query"))
				 		{
				 			proposal_Form pForm=(proposal_Form)session.getAttribute("proposal_form");
				 			if(pForm==null)
				 			{
				 				pForm=new proposal_Form();
				 				session.setAttribute("proposal_form",pForm);
				 			}
				 				
				 			String strSql="select * from proposal where"; 
				 			if(con!=null)
				 			{
					 			if(con.equals("submitted"))
					 				strSql+=" current_status='submitted'";
					 			else if(con.equals("assigned"))
					 				strSql+=" current_status='assigned'";
					 			else if(con.equals("evaluating"))
					 				strSql+=" current_status='evaluating'";
					 			else if(con.equals("evaluated"))
					 				strSql+=" current_status='evaluated'";
					 			else if(con.equals("decided"))
					 				strSql+=" current_status<>'evaluated' AND current_status<>'unsubmitted' AND current_status<>'evaluating' AND  current_status<>'assigned'  AND current_status<>'submitted'";
					 			else if(con.equals("approved"))
					 				strSql+=" current_status='approved'";
					 			else if(con.equals("declined"))
					 				strSql+=" current_status='declined'";
					 			else if(con.equals("search"))
					 				return "search_proposal_main.jsp";
					 			else
					 				throw new Exception("Unsupported action: query with condition: "+con);
				 			}	
				 			else
				 				throw new Exception("No condition is specified.");
				 			
				 			pForm.findRecords(strSql);
				 			return "proposals_main.jsp";
				 		}
				 	}
				 	else if(target.equals("usr"))
			 		{
			 			usr_Form uf=(usr_Form)session.getAttribute("usr_form");
			 			if(uf==null)
			 			{
			 				uf=new usr_Form();
			 				session.setAttribute("usr_form",uf);
			 			}
			 			
			 			String strSql="select * from usr where"; 
				 		if(con!=null)
				 		{
					 		if(con.equals("Peer-viewer"))
					 			strSql+=" permission='1' OR permission='3' OR permission='5' OR permission='6' ";
					 		else if(con.equals("Author"))
					 			strSql+=" permission='0' OR permission='3' OR permission='4' OR permission='6' ";
					 		else if(con.equals("Manager"))
					 			strSql+=" permission='2' OR permission='4' OR permission='5' OR permission='6' ";
					 		else
					 			throw new Exception("Unsupported action: query with condition: "+con);
				 		}	
				 		else
				 			throw new Exception("No condition is specified.");
				 			
				 		uf.findRecords(strSql);
				 		return "users_list_main.jsp";
			 		}
				 }
			 	 else if(action.equals("assign"))
			 	 {
			 		String step=request.getParameter("step");
			 		String ids=request.getParameter("id");
			 		if(step==null || ids==null)
			 		{
			 			beanManager.setProposalIds(cur_proposal_id);
			 			usr_Form ufr=(usr_Form)session.getAttribute("usr_form");
			 			if(ufr==null)
			 			{
			 				ufr=new usr_Form();
			 				session.setAttribute("usr_form",ufr);
			 			}	
			 			ufr.findRecords("select * from usr where permission='1' OR permission='3' OR permission='5' OR permission='6'");
			 			return "choose_peerviewers.jsp";
			 		}
			 		else if(step.equals("1")) //shoose proposals
			 		{
			 			beanManager.setProposalIds(ids);
			 			usr_Form ufr=(usr_Form)session.getAttribute("usr_form");
			 			if(ufr==null)
			 			{
			 				ufr=new usr_Form();
			 				session.setAttribute("usr_form",ufr);
			 			}	
			 			ufr.findRecords("select * from usr where permission='1' OR permission='3' OR permission='5' OR permission='6'");
			 			return "choose_peerviewers.jsp";
			 		}
			 		else if(step.equals("2"))
			 		{
			 			beanManager.setUsrIds(ids);
			 			beanManager.assign();
			 					
			 			return "manager.go?target=proposal&action=query&con=assigned";
			 		}
			 		else
			 			throw new Exception("Invalid step for assignment: "+step);
			 	
			 	}
			 	else if(action.equals("decide"))
			 	{
			 		event_Form pForm=(event_Form)session.getAttribute("event_form");
		 			if(pForm==null)
		 			{
		 				pForm=new event_Form();
		 				session.setAttribute("event_form",pForm);
		 			}
		 			pForm.clearForm();
		 			String sql="select * from event where PROPOSAL_ID='"+cur_proposal_id+"' AND ACTION_ID='decided'";
		 			
		 			try{
		 				pForm.findRecordBySQL(sql);
		 			}
		 			catch(Exception e)
		 			{
		 				if(e.getMessage().indexOf("Failed to find the record")>-1) 
		 				{
		 					pForm.setUSR_ID(cur_usr_id);
				 			pForm.setPROPOSAL_ID(cur_proposal_id);
				 			pForm.setROLE_ID("2");
				 			Calendar date=Calendar.getInstance();
							int day = date.get(Calendar.DATE);
							int month = date.get(Calendar.MONTH);
							int year = date.get(Calendar.YEAR);
							String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
							
			 				pForm.setEventDate(dt);
							pForm.setACTION_ID("decided");
		 				}
		 				else
		 					throw e;
		 					
		 			}
		 			
					return "decision_main.jsp";
			 	}
			 	
		 	 }
		 	 else if (selectedScreen.equals("/menu.go")) 
	   		 {
			   	String role=request.getParameter("role");
			   	if(role!=null)
			   	{
			   		beanManager.setCurMenu(role);
			   		   	
			   		if(role.equals("Author") ||role.equals("Peer-viewer")||role.equals("Manager"))
			   		{
			   			beanManager.setUserCurrentRole(role);
			   			return "main.jsp";
				   	}
				   	else if(role.equals("Home"))
				   		return "home.jsp";
				   	else
				   		return "home.jsp";
			   		
			   	}
		   		throw new Exception("Invalid role: "+role);
				   	
			 }
			 else if(selectedScreen.equals("/select_project.go")) 
	   		 {
	   		 	project_Form pjf=(project_Form)session.getAttribute("project_form");
	   		 	if(pjf==null)throw new Exception("No project form created.");
	   		 	
	   		 	String sql="select * from project";
	   		 	pjf.findRecords(sql);
	   		 	return "select_project_main.jsp";
	   		 }
	   		 
		 	 else
		 	  	throw new Exception("Unsupported action: "+selectedScreen);
		 	  	
		 	 return "index.jsp";
	
	}
	
	private void updateAfterTypeChanged(String id,HttpSession session)
	{
		if(id==null) //add a new type
		{
			composition_Form f=(composition_Form)session.getAttribute("composition_form");
			if(f!=null) f.clearRecords();
			distribution_Form f1=(distribution_Form)session.getAttribute("distribution_form");
			if(f1!=null) f1.clearRecords();
			plot_Form f2=(plot_Form)session.getAttribute("plot_form");
			if(f2!=null) f2.clearRecords();
			typename_Form f3=(typename_Form)session.getAttribute("typename_form");
			if(f3!=null) f3.clearRecords();
			typereference_Form f4=(typereference_Form)session.getAttribute("typereference_form");
			if(f4!=null) f4.clearRecords();
		}
	}
	
	private String handleUpload(String target,HttpServletRequest request, HttpServletResponse response) throws Exception
	{
				
		boolean isMultipart = FileUpload.isMultipartContent(request);
		if(!isMultipart) throw new Exception("It is not a multipart content.");
		
		HttpSession session = request.getSession();
		long max_size=sysManager.getUpload_file_max_size();
		int  threshold=sysManager.getUpload_file_threshold();

		if(target.equals("proposal"))
		{
			//if(true)throw new Exception("document_path:" + document_path);
			proposal_Form pf=(proposal_Form)session.getAttribute("proposal_form");
		
			if(pf==null) throw new Exception("No proposal form has been created.");
			
			String doc1=pf.getDocument1();
			String doc2=pf.getDocument2();
			String doc3=pf.getDocument3();
			
			String fileName="";
		
			
			try{
				
				DiskFileUpload fu = new DiskFileUpload();
	        	// maximum size before a FileUploadException will be thrown
	        	fu.setSizeMax(max_size);
		        // maximum size that will be stored in memory
		        fu.setSizeThreshold(threshold);
		        // the location for saving data that is larger than getSizeThreshold()
		        fu.setRepositoryPath(document_path);
		
		        List fileItems = fu.parseRequest(request);
		        // assume we know there are two files. The first file is a small
		        // text file, the second is unknown and is written to a file on
		        // the server
		        Iterator i = fileItems.iterator();
		        int index=0;
		        FileItem fi=null;
		        while(i.hasNext())
		        {
		        	fi=(FileItem)i.next();
		           	fileName = fi.getName();
		           	fileName=fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
		           	if(fileName.trim().length()>0)
		           	{
		           		
		           		String fileName1=document_path+"\\"+fileName;
		           		if(index==0 && doc1.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc1;
		           			File file=new File(strFile);
		           			file.delete();
		           		}
		           		if(index==1 && doc2.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc2;
		           			File file=new File(strFile);
		           			file.delete();
		           		}
		           		if(index==2 && doc3.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc3;
		           			File file=new File(strFile);
		           			file.delete();
		           		}

		           		
		           		File f=null;
		           		try{
		           			f=new File(fileName1);
		           		}
		           		catch(Exception ex){throw new Exception(" Failed to crate new file: "+fileName);}
		           		
		           		try{
		           			fi.write(f);	
		           		}
		           		catch(Exception ex1){throw new Exception(" Failed to write the file: "+fileName);}
		           		
		           		if(index==0)
		           			pf.setDocument1(fileName);
		           		
		           		else if(index==1)
		           			pf.setDocument2(fileName);
		           			
		           		else if(index==2)
		           			pf.setDocument3(fileName);	
						
								           			
		           		
		           	}
		           	index++;
		        }
		        
		        pf.updateRecord();
		        return "proposal_document_main.jsp";
		     }
		     catch(Exception e){throw new Exception(e.getMessage()+"\n"+document_path);} // new Exception(e.getMessage()+"\nFile:"+fileName);}
		     
		}
		else if(target.equals("project"))
		{
			project_Form pf=(project_Form)session.getAttribute("project_form");
		
			if(pf==null) throw new Exception("No project form has been created.");
			
			String doc1=pf.getDocument1();
			String doc2=pf.getDocument2();
			String doc3=pf.getDocument3();
			
			String fileName="";

			try{
				
				DiskFileUpload fu = new DiskFileUpload();
	        	// maximum size before a FileUploadException will be thrown
	        	fu.setSizeMax(max_size);
		        // maximum size that will be stored in memory
		        fu.setSizeThreshold(threshold);
		        // the location for saving data that is larger than getSizeThreshold()
		        fu.setRepositoryPath(document_path);
		
		        List fileItems = fu.parseRequest(request);
		        // assume we know there are two files. The first file is a small
		        // text file, the second is unknown and is written to a file on
		        // the server
		        Iterator i = fileItems.iterator();
		        int index=0;
		        FileItem fi=null;
		        while(i.hasNext())
		        {
		        	fi=(FileItem)i.next();
		           	fileName = fi.getName();
		           	fileName=fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
		           	if(fileName.trim().length()>0)
		           	{
		           		
		           		String fileName1=document_path+"\\"+fileName;
		           		if(index==0 && doc1.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc1;
		           			File file=new File(strFile);
		           			file.delete();
		           		}
		           		if(index==1 && doc2.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc2;
		           			File file=new File(strFile);
		           			file.delete();
		           		}
		           		if(index==2 && doc3.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc3;
		           			File file=new File(strFile);
		           			file.delete();
		           		}

		           		
		           		File f=null;
		           		try{
		           			f=new File(fileName1);
		           		}
		           		catch(Exception ex){throw new Exception(" Failed to crate new file: "+fileName);}
		           		
		           		try{
		           			fi.write(f);	
		           		}
		           		catch(Exception ex1){throw new Exception(" Failed to write the file: "+fileName);}
		           		
		           		if(index==0)
		           			pf.setDocument1(fileName);
		           		
		           		else if(index==1)
		           			pf.setDocument2(fileName);
		           			
		           		else if(index==2)
		           			pf.setDocument3(fileName);	
						
								           			
		           		
		           	}
		           	index++;
		        }
		        
		        pf.updateRecord();
		        return "project_document_main.jsp";
		     }
		     catch(Exception e){throw e;}// new Exception(e.getMessage()+"\nFile:"+fileName);}
		     
		     
		}
		else if(target.equals("event"))
		{
			event_Form pf=(event_Form)session.getAttribute("event_form");
		
			if(pf==null) throw new Exception("No event form has been created.");
			
			String doc1=pf.getDocument1();
			String doc2=pf.getDocument2();
			String doc3=pf.getDocument3();
			
			String fileName="";

			try{
				
				DiskFileUpload fu = new DiskFileUpload();
	        	// maximum size before a FileUploadException will be thrown
	        	fu.setSizeMax(max_size);
		        // maximum size that will be stored in memory
		        fu.setSizeThreshold(threshold);
		        // the location for saving data that is larger than getSizeThreshold()
		        fu.setRepositoryPath(document_path);
		
		        List fileItems = fu.parseRequest(request);
		        // assume we know there are two files. The first file is a small
		        // text file, the second is unknown and is written to a file on
		        // the server
		        Iterator i = fileItems.iterator();
		        int index=0;
		        FileItem fi=null;
		        while(i.hasNext())
		        {
		        	fi=(FileItem)i.next();
		           	fileName = fi.getName();
		           	fileName=fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
		           	if(fileName.trim().length()>0)
		           	{
		           		
		           		String fileName1=document_path+"\\"+fileName;
		           		if(index==0 && doc1.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc1;
		           			File file=new File(strFile);
		           			file.delete();
		           		}
		           		if(index==1 && doc2.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc2;
		           			File file=new File(strFile);
		           			file.delete();
		           		}
		           		if(index==2 && doc3.trim().length()>0)
		           		{
		           			String strFile=document_path+"\\"+doc3;
		           			File file=new File(strFile);
		           			file.delete();
		           		}

		           		
		           		File f=null;
		           		try{
		           			f=new File(fileName1);
		           		}
		           		catch(Exception ex){throw new Exception(" Failed to crate new file: "+fileName);}
		           		
		           		try{
		           			fi.write(f);	
		           		}
		           		catch(Exception ex1){throw new Exception(" Failed to write the file: "+fileName);}
		           		
		           		if(index==0)
		           			pf.setDocument1(fileName);
		           		
		           		else if(index==1)
		           			pf.setDocument2(fileName);
		           			
		           		else if(index==2)
		           			pf.setDocument3(fileName);	
						
								           			
		           		
		           	}
		           	index++;
		        }
		        
		        pf.updateRecord();
		        return "event_document_main.jsp";
		     }
		     catch(Exception e){throw e;}// new Exception(e.getMessage()+"\nFile:"+fileName);}
		     
		     
		}
		else
			throw new Exception("Invalid parameter");
		
			
			
	}
	private void updateSystemSettings(HttpServletRequest request) throws Exception
	{
		
		String str="";
		
		sysManager.setUpload_file_max_size(Long.valueOf(request.getParameter("upload_file_max_size")).longValue());
		sysManager.setUpload_file_threshold(Integer.valueOf(request.getParameter("upload_file_threshold")).intValue());
		sysManager.setEmail_server(request.getParameter("email_server"));
		sysManager.setManager_email(request.getParameter("manager_email"));
		
		str=request.getParameter("auto_manager_activated");
		sysManager.setAuto_manager_activated(str!=null && str.equals("on"));
		
		str=request.getParameter("auto_email_activated");
		sysManager.setAuto_email_activated(str!=null && str.equals("on"));
		
		str=request.getParameter("send_email_to_author_after_submission");
		sysManager.setSend_email_to_author_after_submission(str!=null && str.equals("on"));
		
		str=request.getParameter("send_email_to_manager_after_submission");
		sysManager.setSend_email_to_manager_after_submission(str!=null && str.equals("on"));
		
		str=request.getParameter("send_email_to_author_after_decided");
		sysManager.setSend_email_to_author_after_decided(str!=null && str.equals("on"));

		str=request.getParameter("send_email_to_peerviewer_after_assigned");
		sysManager.setSend_email_to_peerviewer_after_assigned(str!=null && str.equals("on"));
		
		str=request.getParameter("send_email_to_manager_after_declined_assignment");
		sysManager.setSend_email_to_manager_after_declined_assignment(str!=null && str.equals("on"));
		
		str=request.getParameter("send_email_to_manager_after_all_evaluation_finished");
		sysManager.setSend_email_to_manager_after_all_evaluation_finished(str!=null && str.equals("on"));
		
		str=request.getParameter("send_email_to_user_after_role_changed");
		sysManager.setSend_email_to_user_after_role_changed(str!=null && str.equals("on"));
		
		
		ServletContext application = getServletContext();
    	
		String path1="";    
		path1=application.getRealPath("/WEB-INF/system_settings.property");
		
		sysManager.save(path1);
		
	}
}

