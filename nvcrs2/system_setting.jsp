<!--

  system_setting.jsp
 
-->


<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >

	function updateAll(checked)
	{
		document.settings.auto_email_activated.disabled=!checked;
		if(!checked)updateAutoEmail(false);
		else updateAutoEmail(document.settings.auto_email_activated.checked);
	}
	
	function updateAutoEmail(checked)
	{
		document.settings.send_email_to_author_after_submission.disabled=!checked;
		document.settings.send_email_to_manager_after_submission.disabled=!checked;
		document.settings.send_email_to_author_after_decided.disabled=!checked;
		document.settings.send_email_to_peerviewer_after_assigned.disabled=!checked;
		document.settings.send_email_to_manager_after_declined_assignment.disabled=!checked;
		document.settings.send_email_to_manager_after_all_evaluation_finished.disabled=!checked;
		document.settings.send_email_to_user_after_role_changed.disabled=!checked;
	
	}
	function validate()
	{

		return true;
	}

</script>

<font face=Arial >
<img src="image/title_sep_line.gif">
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>System Settings
</td></tr>
</table >
<br>
<br>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	if(manager!=null)
	{
		String role=manager.getUserCurrentRole();
		if(role.equals("Manager"))
		{
			SystemManager sysManager=(SystemManager)pageContext.getAttribute("system_manager", PageContext.APPLICATION_SCOPE);
			String upload_file_max_size=String.valueOf(sysManager.getUpload_file_max_size());
			String upload_file_threshold=String.valueOf(sysManager.getUpload_file_threshold());
			String manager_email=sysManager.getManager_email();
			boolean auto_manager_activated=sysManager.getAuto_manager_activated();
			boolean  auto_email_activated=sysManager.getAuto_email_activated();
			String email_server=sysManager.getEmail_server();
			boolean  send_email_to_author_after_submission=sysManager.getSend_email_to_author_after_submission();
			boolean  send_email_to_manager_after_submission=sysManager.getSend_email_to_manager_after_submission();
			boolean  send_email_to_author_after_decided=sysManager.getSend_email_to_author_after_decided();
			boolean  send_email_to_peerviewer_after_assigned=sysManager.getSend_email_to_peerviewer_after_assigned();
			boolean  resend_email_to_peerviewer=sysManager.getResend_email_to_peerviewer();
			String resend_email_to_peerviewer_times=String.valueOf(sysManager.getResend_email_to_peerviewer_times()); 
			String resend_email_to_peerviewer_interval=String.valueOf(sysManager.getResend_email_to_peerviewer_interval());
			boolean send_email_to_manager_after_declined_assignment=sysManager.getSend_email_to_manager_after_declined_assignment();
			boolean send_email_to_manager_after_all_evaluation_finished=sysManager.getSend_email_to_manager_after_all_evaluation_finished();
			boolean send_email_to_user_after_role_changed=sysManager.getSend_email_to_user_after_role_changed();
			boolean auto_assignment_activated=sysManager.getAuto_assignment_activated();
			String max_number_peerviewers_per_proposal=String.valueOf(sysManager.getMax_number_peerviewers_per_proposal());
			int assignment_policy=sysManager.getAssignment_policy();
			boolean another_assignment_after_declined=sysManager.getAnother_assignment_after_declined();
			boolean auto_decision_activated=sysManager.getAuto_decision_activated();
			String min_number_evaluations=String.valueOf(sysManager.getMin_number_evaluations());
			int decision_threshold_type=sysManager.getDecision_threshold_type();
			String decision_threshold=String.valueOf(sysManager.getDecision_threshold());
			
			
			String msg=(String)manager.getMessage();
			if(msg!=null)
			{
		%>
		<center><font color=red size="1"><%= msg %></font></center>
		<%
			manager.setMessage("");
			}
		%>
		
		<center>
		<form action="/nvcrs/manager.go?act=system_settings" method="POST" name="settings" onsubmit="return validate()" >
			<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
		    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>SYSTEM SETTINGS</font></center></th>
			     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				
				<tr >
				<td colspan=2 align=center>
					<table width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
					<tr>
						<td background="image/blue_back.gif" align="center" colspan=2><font face=Arial size=2 color=white>General settings</td> 
					</tr>
					</table>
				</td>
				</tr>
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr>
					<td align=right><font size=2>Maximum size of uploaded file:</td>
					<td><input type="text" name="upload_file_max_size" value="<%=upload_file_max_size %>" /></td>
				</tr>
				<tr>
					<td align=right><font size=2>Threshold size of uploaded file:</td>
					<td><input type="text" name="upload_file_threshold" value="<%=upload_file_threshold %>"/></td>
				</tr>
				<tr>
					<td align=right><font size=2>STMP host for sending email:</td>
					<td><input type="text" name="email_server" value="<%=email_server %>"/></td>
				</tr>
				<tr>
					<td align=right><font size=2>Email of manager:</td>
					<td><input type="text" name="manager_email" value="<%=manager_email %>"/></td>
				</tr>
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr >
				<td colspan=2 align=center>
					<table width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
					<tr>
						<td background="image/blue_back.gif" align="center" colspan=2><font face=Arial size=2 color=white>Auto Manager</td> 
					</tr>
					</table>
				</td>
				</tr>
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				
				<tr><td colspan=2 align=center>
					<table width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
					<tr>
						<td colspan=2>
						<input type=CHECKBOX name=auto_manager_activated 
						<% 
							if(auto_manager_activated)
							{
						%>
								checked
						<%
							}
						%>
						 onclick ="updateAll(this.checked)"><font face=Arial size=2 >Activate Auto-manager</td> 
					</tr>
					<tr>
						<td colspan=2>
						&nbsp;&nbsp;
						<input type=CHECKBOX name=auto_email_activated 
						<% 
							if(auto_email_activated)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated)
							{
						%>
								disabled
						<%
							}
						%>
						
						 onclick ="updateAutoEmail(this.checked)"><font face=Arial size=2 >Activate Auto-email</td> 
					</tr>
					<tr>
						<td width=30></td>
						<td>
						<input type=CHECKBOX name=send_email_to_author_after_submission 
						<% 
							if(send_email_to_author_after_submission)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated || !auto_email_activated )
							{
						%>
								disabled
						<%
							}
						%>
						
						><font face=Arial size=2 >Send email to author after submission of proposal</td> 
					</tr>
					<tr>
						<td width=30></td>
						<td>
						<input type=CHECKBOX name=send_email_to_manager_after_submission 
						<% 
							if(send_email_to_manager_after_submission)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated || !auto_email_activated )
							{
						%>
								disabled
						<%
							}
						%>
						
						><font face=Arial size=2 >Send email to manager after submission of proposal</td> 
					</tr>
					<tr>
						<td width=30></td>
						<td>
						<input type=CHECKBOX name=send_email_to_author_after_decided 
						<% 
							if(send_email_to_author_after_decided)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated || !auto_email_activated )
							{
						%>
								disabled
						<%
							}
						%>
						
						><font face=Arial size=2 >Send email to author after proposal has been decided</td> 
					</tr>
					<tr>
						<td width=30></td>
						<td>
						<input type=CHECKBOX name=send_email_to_peerviewer_after_assigned 
						<% 
							if(send_email_to_peerviewer_after_assigned)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated || !auto_email_activated )
							{
						%>
								disabled
						<%
							}
						%>
						
						><font face=Arial size=2 >Send email to peerviewer after proposal has been assigned</td> 
					</tr>
					<tr>
						<td width=30></td>
						<td>
						<input type=CHECKBOX name=send_email_to_manager_after_declined_assignment 
						<% 
							if(send_email_to_manager_after_declined_assignment)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated || !auto_email_activated )
							{
						%>
								disabled
						<%
							}
						%>
						
						><font face=Arial size=2 >Send email to manager after assignment has been declined by peerviewer</td> 
					</tr>
					<tr>
						<td width=30></td>
						<td>
						<input type=CHECKBOX name=send_email_to_manager_after_all_evaluation_finished 
						<% 
							if(send_email_to_manager_after_all_evaluation_finished)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated || !auto_email_activated )
							{
						%>
								disabled
						<%
							}
						%>
						
						><font face=Arial size=2 >Send email to manager after all evaluations have been finished</td> 
					</tr>
					<tr>
						<td width=30></td>
						<td>
						<input type=CHECKBOX name=send_email_to_user_after_role_changed 
						<% 
							if(send_email_to_user_after_role_changed)
							{
						%>
								checked 
						<%
							}
						%>
						
						<% 
							if(!auto_manager_activated || !auto_email_activated )
							{
						%>
								disabled
						<%
							}
						%>
						
						><font face=Arial size=2 >Send email to users after their roles have been changed</td> 
					</tr>
					</table>
				</td></tr>
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr>
					<td align="center" colspan=2><input type="submit" value="Save" /></td> 
				</tr>
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		</table>
		</form>
<%
		}
		else
		{
%>
			<center>Sorry! you are not allowed to access this information.</center>
<%
		}
	}
	else
	{
%>
		<center>Sorry! you have not logged in.</center>
<%
	}
%>
<br><br>
<!--
upload_file_max_size=2000000
upload_file_threshold=5000
manager_email=peet@unc.edu
auto_manager_activated=Yes	
auto_email_activated=Yes
email_server=smtp.unc.edu
send_email_to_author_after_submission=Yes
send_email_to_manager_after_submission=No
send_email_to_author_after_decided=No
send_email_to_peerviewer_after_assigned=No
resend_email_to_peerviewer=No
resend_email_to_peerviewer_times=0  
resend_email_to_peerviewer_interval=7
send_email_to_manager_after_declined_assignment=No
send_email_to_manager_after_all_evaluation_finished=No
send_email_to_user_after_role_changed=No
auto_assignment_activated=No
max_number_peerviewers_per_proposal=5
assignment_policy=1
another_assignment_after_declined=No
auto_decision_activated=No
min_number_evaluations=5
decision_threshold_type=1
decision_threshold=100

-->