<!--

   event_document.jsp
   Created on Wed Apr 21 12:25:19 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	function submitClick()
	{
		document.project_document.submit();
	}

	function validate()
	{

		return true;
	}

</script>
<center>
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Proposal :: Evaluation :: Documents
</td></tr>
</table >
<br>
<br>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	event_Form fr=(event_Form)pageContext.getAttribute("event_form", PageContext.SESSION_SCOPE);
	

	if(fr!=null && manager!=null)
	{
		String msg=(String)manager.getMessage();
		String document1=fr.getDocument1();
		String document2=fr.getDocument2();
		String document3=fr.getDocument3();
		
		String primaryKey=fr.getPrimaryKey();
		String primaryKeyValue=fr.getFieldValue(primaryKey);
		String status=manager.getAssignmentStatus(manager.getProposalId(),manager.getUserId());
		int intAction=-1;
		if(!status.equals("completed"))
		{
			intAction=manager.getPermissionByRecord("event",primaryKey,primaryKeyValue);
		}

		if(msg!=null)
		{
%>
			<center><font color=blue size="1"><%= msg %></font></center>
<%
			manager.setMessage("");
		}
%>
<%
    	if(intAction==0 || intAction==1 || intAction==2 || intAction==3)
		{
%>
			<FORM ACTION="/nvcrs/event.go?target=document&act=upload" METHOD="POST" ENCTYPE="multipart/form-data" name="event_document">
			<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    		 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>Evaluation Documents</font></center></th>
		     <tr><td>&nbsp;</td></tr>
    		<tr><td align=center>
    		<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">    
			<TR >
				<TH background="image/blue_back.gif" align=center><font size=2 color=white>File
				<TH background="image/blue_back.gif" align=center><font size=2 color=white>View
				<TH background="image/blue_back.gif" align=center><font size=2 color=white>Delete
				<TH background="image/blue_back.gif" align=center><font size=2 color=white>Upload
    		</TR>
    		<TR bgcolor="#cacaca">	
				
				<%
					if(!document1.equals(BeanManager.EMPTY_VALUE))
					{
				%>
					<td width=200 align=right><%= document1 %></td>
					<td width=60 align=center><a href="documents/<%= document1 %>" target="_blank"><img src="image/view.gif" border=0/></a></td>
					<td width =60 align=center><a href="event.go?target=document&act=delete&id=1"><img src="image/delete.gif" border=0 /></a></td>
				<%
					}
					else
					{
				%>
					 <td align=right>-</td>
				     <td>&nbsp;</td><td>&nbsp;</td>
				<%
					}
				%>
				<td width=200><INPUT TYPE="FILE" NAME="first" value="<%= document1 %>"></td>
				
			</tr>
			<TR bgcolor="#d8d8d8">	
				
				<%
					if(!document2.equals(BeanManager.EMPTY_VALUE))
					{
				%>
				
					<td width=200 align=right><%= document2 %></td>
					<td width=60 align=center><a href="documents/<%= document2 %>" target="_blank"><img src="image/view.gif" border=0/></a></td>
					<td width =60 align=center><a href="event.go?target=document&act=delete&id=2"><img src="image/delete.gif" border=0/></a></td>
				<%
					}
					else
					{
				%>
					<td align=right>-</td>
				   	<td>&nbsp;</td><td>&nbsp;</td>
				<%
					}
				%>
				<td width=200><INPUT TYPE="FILE" NAME="second" value=<%= document2 %>></td>
				
			</tr>
			<TR bgcolor="#cacaca">	
				
				<%
					if(!document3.equals(BeanManager.EMPTY_VALUE))
					{
				%>
					<td width=200 align=right><%= document3 %></td>	
					<td width=60 align=center><a href="documents/<%= document3 %>" target="_blank"><img src="image/view.gif" border=0/></a></td>
					<td width =60 align=center><a href="event.go?target=document&act=delete&id=3"><img src="image/delete.gif" border=0/></a></td>
				<%
					}
					else
					{
				%>
						<td align=right>-</td>
				 	    <td>&nbsp;</td><td>&nbsp;</td>
				<%
					}
				%>
				<td width=200><INPUT TYPE="FILE" NAME="third" value=<%= document3 %>></td>
				
			</tr>
			
			<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
			<tr>	
				<td></td>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td width=350><INPUT TYPE=submit VALUE="Upload"></td> <!-- onclick="submitClick()"></td> -->
			</tr>

			<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
			
			</table>
			</td>
			</tr>
			</table>
		</form>
<%
		}
		else
		{
%>
			<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    		 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>Documents</font></center></th>
		     <tr><td>&nbsp;</td></tr>
    		<tr><td align=center>
    		<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">    
			<TR >
				<TH width=200 background="image/blue_back.gif" align=right><font size=2 color=white>File
				<TH width=60 background="image/blue_back.gif" align=center><font size=2 color=white>View
    		</TR>
<%
    		int i=0;
%>


    	
				
			<%
			if(!document1.equals(BeanManager.EMPTY_VALUE))
			{
	    		if(i%2==0)
		    	{
%>
					<TR bgcolor="#cacaca">
<%
				}
				else
				{
%>
					<TR bgcolor="#d8d8d8">
<%
				}
%>    		
					<td width=200 align=right><%= document1 %></td>
					<td width=60 align=center><a href="documents/<%= document1 %>" target="_blank"><img src="image/view.gif" border=0/></a></td>
				</tr>	
<%
				i++;
			}
%>
<%
			if(!document2.equals(BeanManager.EMPTY_VALUE))
			{
	    		if(i%2==0)
		    	{
%>
					<TR bgcolor="#cacaca">
<%
				}
				else
				{
%>
					<TR bgcolor="#d8d8d8">
<%
				}
%>    		
					<td width=200 align=right><%= document2 %></td>
					<td width=60 align=center><a href="documents/<%= document2 %>" target="_blank"><img src="image/view.gif" border=0/></a></td>
				</tr>
<%
				i++;
			}
%>
<%
			if(!document3.equals(BeanManager.EMPTY_VALUE))
			{
	    		if(i%2==0)
		    	{
%>
					<TR bgcolor="#cacaca">
<%
				}
				else
				{
%>
					<TR bgcolor="#d8d8d8">
<%
				}
%>    		
					<td width=200 align=right><%= document3 %></td>	
					<td width=60 align=center><a href="documents/<%= document3 %>" target="_blank"><img src="image/view.gif" border=0/></a></td>
				</tr>
<%
				i++;
			}
%>
			
			<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
			</table>
			</td>
			</tr>
			</table>
<%
		}
%>
</center>
<%
}
else
{
%>
<center>Please login or register first!</center>
<%
}
%>

