package org.vegbank.nvcrs.web;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;


public class SystemManager
{
	static final int ASSIGNMENT_POLICY_RANDOM=0;
	static final int ASSIGNMENT_POLICY_LEAST_FIRST=1;
	
	static final int DECISION_THRESHOLD_TYPE_ABSOLUTE=0;
	static final int DECISION_THRESHOLD_TYPE_PERCENTAGE=1;
	
	static final int EVENT_PROPOSAL_SUBMITTED=0;
	static final int EVENT_PROPOSAL_ASSIGNED=1;
	static final int EVENT_ASSIGNMENT_DECLINED=2;
	static final int EVENT_ALL_ASSIGNMENT_FINISHED=3;
	static final int EVENT_ROLE_CHANGED=4;
		
	/*
	long upload_file_max_size;
	int  upload_file_threshold;
	String manager_email;
	boolean auto_manager_activated;	
	//Auto email
	boolean auto_email_activated;
	String  email_server;
	boolean send_email_to_author_after_submission;
	boolean send_email_to_manager_after_submission;
	boolean send_email_to_author_after_decided;
	boolean send_email_to_peerviewer_after_assigned;
	boolean resend_email_to_peerviewer;
	int     resend_email_to_peerviewer_times;  
	int     resend_email_to_peerviewer_interval;
	boolean send_email_to_manager_after_declined_assignment;
	boolean send_email_to_manager_after_all_evaluation_finished;
	boolean send_email_to_user_after_role_changed;
	
	//Auto assignment
	boolean auto_assignment_activated;
	int     max_number_peerviewers_per_proposal;
	int     assignment_policy;  //least-first: choose peerviewer with the least current assignments
	                            //random: choose any peerviewer.
	boolean another_assignment_after_declined;
	
	//Auto decision
	boolean auto_decision_activated;
	int     min_number_evaluations;
	int     decision_threshold_type;  //Relative: percentage, absolute: number
	int     decision_threshold;     
	*/
	Properties system_settings;
	
		
	public SystemManager()
	{
		system_settings=new Properties();	
	}
	
	public SystemManager(String file) throws Exception
	{
		system_settings=new Properties();	
		load(file);
	}
	
	public void load(String file) throws Exception
	{
		
		File f=new File(file);
		FileInputStream in=new FileInputStream(f);
		system_settings.load(in);
		in.close();
		in=null;
	}
	
	
	public void save(String file) throws Exception
	{
		File f=new File(file);
		FileOutputStream out=new FileOutputStream(f);
		system_settings.store(out,"nvcrs system settings");
		out.close();
		out=null;
	} 
	
	public synchronized void handleEvent(int event_id, Object event_obj,Session session, BeanManager manager) throws Exception
	{
		if(getAuto_manager_activated())
		{
			String body="";
			String manager_email=system_settings.getProperty("manager_email");
			if(manager_email==null ||manager_email.trim().length()==0)
  				throw new Exception(" No email address for manager specified.");
			if(event_id==EVENT_PROPOSAL_SUBMITTED || event_id==EVENT_PROPOSAL_ASSIGNED || event_id==EVENT_ASSIGNMENT_DECLINED)
			{
			
				event_Form e=(event_Form)event_obj;
				String act=e.getACTION_ID();
				String status=e.getReviewText();
				String usr_id=e.getUSR_ID();
				String subject_usr_id=e.getSUBJECTUSR_ID();
				String usr_email="";
				String usr_name="";
				String subject_usr_email="";
				String subject_usr_name="";
				
				usr_Form usr=new usr_Form();
				usr.findRecordByPrimaryKey(usr_id);
				usr_email=usr.getEmail();
				usr_name=usr.getFirst_name();
				
				if(!subject_usr_id.equals(BeanManager.UNKNOWN_ID))
				{
					usr.findRecordByPrimaryKey(subject_usr_id);
					subject_usr_email=usr.getEmail();
					subject_usr_name=usr.getFirst_name();
				}
				
				//auto email
				if(getAuto_email_activated())
				{
					//after proposal submitted
					if(act.equals("submitted"))
					{
						//hanle send_email_to_author_after_submission
						if(getSend_email_to_author_after_submission())
						{
							body="Dear "+ usr_name+",\n\n";
							body+="Your proposal has been received. We will assign it to peer-viewers for evaluation soon.";
							body+="You can check the status of your proposal on NVCRS.\n\n";
							body+="Thank you very much for your contribution!\n\n";
							body+="Sincerely,\n";
							body+="Manager of NVCRS\n";
							body+="Email: "+manager_email+"\n";
							send(usr_email,"Your proposal received",body);
						}
						//handle send_email_to_manager_after_submission	
						if(getSend_email_to_manager_after_submission())
						{
							body="Dear manager,\n\n";
							body+="New proposal has been received. \n\n";
							body+="Thank you very much for your contribution!\n\n";
							body+="Sincerely,\n";
							body+="Auto-Manager of NVCRS\n";
							body+="Email: "+manager_email+"\n";
							send(manager_email,"New proposal received",body);
						}
						
					}
					//after decided
					else if(act.equals("decided"))
					{
						//hanle send_email_to_author_after_decided
						if(getSend_email_to_author_after_decided())
						{
							body="Dear "+ usr_name+",\n\n";
							body+="A final decision has been made to your proposal. Please log in to NVCRS for detailed information.\n\n";
							body+="Thank you very much for your contribution!\n\n";
							body+="Sincerely,\n";
							body+="Manager of NVCRS\n";
							body+="Email: "+manager_email+"\n";
							send(usr_email,"Your proposal decided",body);
						}
					}
					//after assigned
					else if(act.equals("assigned"))
					{
						//handle send_email_to_peerviewer_after_assigned
						if(status.equals("new"))
						{
							if(getSend_email_to_peerviewer_after_assigned())
							{
								body="Dear "+ subject_usr_name+",\n\n";
								body+="A new proposal has been assigned to you for evaluation. Please log in to NVCRS for detailed information.\n\n";
								body+="Thank you very much for your contribution!\n\n";
								body+="Sincerely,\n";
								body+="Manager of NVCRS\n";
								body+="Email: "+manager_email+"\n";
								send(subject_usr_email,"Your proposal decided",body);
							}
						}
						//handle send_email_to_manager_after_declined_assignment
						else if(status.equals("declined"))
						{
							if(getSend_email_to_manager_after_declined_assignment())	
							{
								body="Dear manager,\n\n";
								body+=subject_usr_name+" has declined the assignment.\n\n";
								body+="Sincerely,\n";
								body+="Auto-Manager of NVCRS\n";
								body+="Email: "+manager_email+"\n";
								send(manager_email,"New proposal received",body);
							}
						}
					}
				}
			}
			else if(event_id==EVENT_ALL_ASSIGNMENT_FINISHED)
			{
				if(getSend_email_to_manager_after_all_evaluation_finished())
				{
					body="Dear manager,\n\n";
					body+="A proposal is waiting for your final decision.\n\n";
					body+="Sincerely,\n";
					body+="Auto-Manager of NVCRS\n";
					body+="Email: "+manager_email+"\n";
					send(manager_email,"New proposal received",body);
				}
			}
			
			else if(event_id==EVENT_ROLE_CHANGED)
			{
				if(getSend_email_to_user_after_role_changed())
				{
				
					usr_Form usr=(usr_Form)event_obj;
					String usr_email=usr.getEmail();
					String usr_name=usr.getFirst_name();
					
					body="Dear "+ usr_name+",\n\n";
					body+="Your role has been changed. Please log in to NVCRS for detailed information.\n\n";
					body+="Sincerely,\n";
					body+="Manager of NVCRS\n";
					body+="Email: "+manager_email+"\n";
					send(usr_email,"Your proposal received",body);
				}
			}
			else
				throw new Exception("Invalid event type: " +event_id);
			/*
			//auto assignment
			if(getAuto_assignment_activated())
			{
				
			}
			//auto decision
			if(getAuto_decision_activated())
			{
				
			}*/
			
		}
	}
	
	
	public String getManager_email()
	{
		return system_settings.getProperty("manager_email");
	}
	
	public void setManager_email(String v)
	{
		system_settings.getProperty("manager_email",v);
	}
	
	public long getUpload_file_max_size()
	{
		try
		{
			String tmp=system_settings.getProperty("upload_file_max_size");
			return Long.valueOf(tmp).longValue();
		}
		catch(Exception e)
		{
			return 2000000L;	
		}
		
	}
	
	public void setUpload_file_max_size(long v)
	{
		system_settings.setProperty("upload_file_max_size",String.valueOf(v));
	}
	
	public int getUpload_file_threshold()
	{
		try
		{
			String tmp=system_settings.getProperty("upload_file_threshold");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setUpload_file_threshold(int v)
	{
		system_settings.setProperty("upload_file_threshold",String.valueOf(v));
	}
	
	
	///////
	public boolean getAuto_manager_activated()
	{
		String tmp=system_settings.getProperty("auto_manager_activated");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setAuto_manager_activated(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("auto_manager_activated",tmp);
	}
	
	
	/////////////////////////////////////////////////////////////
	public boolean getAuto_email_activated()
	{
		String tmp=system_settings.getProperty("auto_email_activated");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setAuto_email_activated(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("auto_email_activated",tmp);
	}
	
	///////////////////////////////////////////////////////////
	public boolean getSend_email_to_author_after_submission()
	{
		String tmp=system_settings.getProperty("send_email_to_author_after_submission");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setSend_email_to_author_after_submission(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("send_email_to_author_after_submission",tmp);
	}
	
	///////////////////////////////////////////////////////////
	public boolean getSend_email_to_manager_after_submission()
	{
		String tmp=system_settings.getProperty("send_email_to_manager_after_submission");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setSend_email_to_manager_after_submission(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("send_email_to_manager_after_submission",tmp);
	}
	
	///////////////////////////////////////////////////////////
	public boolean getSend_email_to_author_after_decided()
	{
		String tmp=system_settings.getProperty("send_email_to_author_after_decided");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setSend_email_to_author_after_decided(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("send_email_to_author_after_decided",tmp);
	}
	
	///////////////////////////////////////////////////////////
	public boolean getSend_email_to_peerviewer_after_assigned()
	{
		String tmp=system_settings.getProperty("send_email_to_peerviewer_after_assigned");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setSend_email_to_peerviewer_after_assigned(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("send_email_to_peerviewer_after_assigned",tmp);
	}
		///////////////////////////////////////////////////////////
	public boolean getResend_email_to_peerviewer()
	{
		String tmp=system_settings.getProperty("resend_email_to_peerviewer");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setResend_email_to_peerviewer(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("resend_email_to_peerviewer",tmp);
	}
			///////////////////////////////////////////////////////////
	public boolean getSend_email_to_manager_after_declined_assignment()
	{
		String tmp=system_settings.getProperty("send_email_to_manager_after_declined_assignment");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setSend_email_to_manager_after_declined_assignment(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("send_email_to_manager_after_declined_assignment",tmp);
	}
	
	
	///////////////////////////////////////////////////////////
	public boolean getSend_email_to_manager_after_all_evaluation_finished()
	{
		String tmp=system_settings.getProperty("send_email_to_manager_after_all_evaluation_finished");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setSend_email_to_manager_after_all_evaluation_finished(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("send_email_to_manager_after_all_evaluation_finished",tmp);
	}
	
	///////////////////////////////////////////////////////////
	public boolean getSend_email_to_user_after_role_changed()
	{
		String tmp=system_settings.getProperty("send_email_to_user_after_role_changed");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setSend_email_to_user_after_role_changed(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("send_email_to_user_after_role_changed",tmp);
	}
		///////////////////////////////////////////////////////////
	public boolean getAuto_assignment_activated()
	{
		String tmp=system_settings.getProperty("auto_assignment_activated");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setAuto_assignment_activated(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("auto_assignment_activated",tmp);
	}
	///////////////////////////////////////////////////////////
	public boolean getAnother_assignment_after_declined()
	{
		String tmp=system_settings.getProperty("another_assignment_after_declined");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setAnother_assignment_after_declined(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("another_assignment_after_declined",tmp);
	}
		///////////////////////////////////////////////////////////
	public boolean getAuto_decision_activated()
	{
		String tmp=system_settings.getProperty("auto_decision_activated");
		if(tmp.trim().equals("Yes")) return true;
		
		return false;
	}
	
	public void setAuto_decision_activated(boolean v)
	{
		String tmp="No";
		if(v) tmp="Yes";
		system_settings.setProperty("auto_decision_activated",tmp);
	}
	
	///////////////////////////////////////////////////////////
	public String getEmail_server()
	{
		return system_settings.getProperty("email_server");
	}
	
	public void setEmail_server(String v)
	{
		system_settings.setProperty("email_server",v);
	}
	/////////////////////////////////////////////////
	public int getResend_email_to_peerviewer_times()
	{
		try
		{
			String tmp=system_settings.getProperty("resend_email_to_peerviewer_times");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setResend_email_to_peerviewer_times(int v)
	{
		system_settings.setProperty("resend_email_to_peerviewer_times",String.valueOf(v));
	}
	
	/////////////////////////////////////////////////
	public int getResend_email_to_peerviewer_interval()
	{
		try
		{
			String tmp=system_settings.getProperty("resend_email_to_peerviewer_interval");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setResend_email_to_peerviewer_interval(int v)
	{
		system_settings.setProperty("resend_email_to_peerviewer_interval",String.valueOf(v));
	}
	
	/////////////////////////////////////////////////
	public int getMax_number_peerviewers_per_proposal()
	{
		try
		{
			String tmp=system_settings.getProperty("max_number_peerviewers_per_proposal");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setMax_number_peerviewers_per_proposal(int v)
	{
		system_settings.setProperty("max_number_peerviewers_per_proposal",String.valueOf(v));
	}
	
		/////////////////////////////////////////////////
	public int getAssignment_policy()
	{
		try
		{
			String tmp=system_settings.getProperty("assignment_policy");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setAssignment_policy(int v)
	{
		system_settings.setProperty("assignment_policy",String.valueOf(v));
	}
		/////////////////////////////////////////////////
	public int getMin_number_evaluations()
	{
		try
		{
			String tmp=system_settings.getProperty("min_number_evaluations");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setMin_number_evaluations(int v)
	{
		system_settings.setProperty("min_number_evaluations",String.valueOf(v));
	}
	
	/////////////////////////////////////////////////
	public int getDecision_threshold_type()
	{
		try
		{
			String tmp=system_settings.getProperty("decision_threshold_type");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setDecision_threshold_type(int v)
	{
		system_settings.setProperty("decision_threshold_type",String.valueOf(v));
	}
		/////////////////////////////////////////////////
	public int getDecision_threshold()
	{
		try
		{
			String tmp=system_settings.getProperty("decision_threshold");
			return Integer.valueOf(tmp).intValue();
		}
		catch(Exception e)
		{
			return 4000;	
		}
	}
	
	public void setDecision_threshold(int v)
	{
		system_settings.setProperty("decision_threshold",String.valueOf(v));
	}
	
	/*
	 * send email
	 */
	public void send(String to, String subject, String body) throws Exception
  	{
  		String manager_email=system_settings.getProperty("manager_email");
		String email_server=system_settings.getProperty("email_server");
  		if(email_server==null || email_server.trim().length()==0)
  			throw new Exception(" No SMTP host specified for sending emails.");
  		if(manager_email==null ||manager_email.trim().length()==0)
  			throw new Exception(" No email address for manager specified.");
  				
	    try
	    {
	      
	      Properties props = System.getProperties();
	      
	      props.put("mail.smtp.host",email_server);
	      Session session = Session.getDefaultInstance(props, null);
		  //Session session = Session.getInstance(props, null);
		 
	      Message msg = new MimeMessage(session);
	      
	      msg.setFrom(new InternetAddress(manager_email));
	      msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
	      msg.setSubject(subject);
	      msg.setText(body);
	      msg.setHeader("X-Mailer", "NVCRS-AutoManager");
	      msg.setSentDate(new Date());
		  
		  Transport.send(msg);

	    }
	    catch (Exception ex)
	    {
	        throw ex;
	    }
	 } 
	
}