<!--

  usr.jsp
   Created on Wed Apr 21 12:25:19 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	function recordClick(act,role,id)
	{
		document.hideform.act.value=act;
		document.hideform.id.value=id;
		document.hideform.role.value=role;
		document.hideform.submit();
	}
	
	function addRole(id)
	{
		document.hideform.act.value="add";
		document.hideform.id.value=id;
		document.hideform.role.value=document.roleform.role.value;
		document.hideform.submit();
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
		<b>User :: Roles</b>
	</td></tr>
</table >
<BR><BR>

<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	usr_Form fr=(usr_Form)pageContext.getAttribute("usr_form", PageContext.SESSION_SCOPE);
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
<center><font color=blue size="1"><%= msg %></font></center>
<%
	manager.setMessage("");
	}
%>
<%
if(fr!=null && manager!=null)
{
	String id=fr.getUSR_ID();
%>
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>USER INFORMATION</font></center></th></tr>
	     <tr><td><font size=2>&nbsp;</td><td><font size=2>&nbsp;</td></tr>
	    		 <tr><td width="50%" align=right><font size=2>First name:</td><td width="50%" ><font size=2><%= fr.getFirst_name() %></td></tr>
				 <tr><td width="50%"  align=right><font size=2>Last name:</td><td width="50%" ><font size=2><%= fr.getLast_name() %></td></tr>
				<tr><td width="50%"  align=right><font size=2>Phone:</td><td width="50%" ><font size=2><%= fr.getPhone() %></td></tr>
				<tr><td width="50%"  align=right><font size=2>Email:</td><td width="50%" ><font size=2><%= fr.getEmail() %></td></tr>
				<tr><td width="50%" ><font size=2>&nbsp;</td><td width="50%" ><font size=2>&nbsp;</td></tr>
	</table>
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    			 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>DELETE ROLE</font></center></th></tr>
			     <tr><td><font size=2>&nbsp;</td><td><font size=2>&nbsp;</td></tr>
				 <tr bgcolor="#ccffcc"><td align=center><font size=2>Role</td><td align=center><font size=2>Delete</td></tr>
<%
	String p=fr.getPermission().trim();
	if(p.equals("0"))
	{
%>
<!--				<TR bgcolor="#cacaca"><td align=center><font size=2>Author</td><td align=center><font size=2><button onclick="recordClick('delete','Author',<%= id %>)"><img src="image/delete.gif"></button></td></tr> -->
				<TR bgcolor="#cacaca"><td align=center><font size=2>Author</td><td align=center><font size=2>-</td></tr>
<%
	}
	else if(p.equals("1"))
	{
%>
				<TR bgcolor="#cacaca"><td align=center><font size=2>Peer-viewer</td><td align=center><font size=2><button onclick="recordClick('delete','Peer-viewer',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
<%
	}
	else if(p.equals("2"))
	{
%>
				<TR bgcolor="#cacaca"><td align=center><font size=2>Manager</td><td align=center><font size=2><button onclick="recordClick('delete','Manager',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
<%
	}
	else if(p.equals("3"))
	{
%>
				<TR bgcolor="#cacaca"><td align=center><font size=2>Author</td><td align=center><font size=2>-</td></tr>
				<TR bgcolor="#d8d8d8"><td align=center><font size=2>Peer-viewer</td><td align=center><font size=2><button onclick="recordClick('delete','Peer-viewer',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
<%
	}
	else if(p.equals("4"))
	{
%>
				<TR bgcolor="#cacaca"><td align=center><font size=2>Author</td><td align=center><font size=2>-</td></tr>
				<TR bgcolor="#d8d8d8"><td align=center><font size=2>Manager</td><td align=center><font size=2><button onclick="recordClick('delete','Manager',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
<%
	}
	else if(p.equals("5"))
	{
%>
				<TR bgcolor="#cacaca"><td align=center><font size=2>Peer-viewer</td><td align=center><font size=2><button onclick="recordClick('delete','Peer-viewer',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
				<TR bgcolor="#d8d8d8"><td align=center><font size=2>Manager</td><td align=center><font size=2><button onclick="recordClick('delete','Manager',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
<%
	}
	else if(p.equals("6"))
	{
%>
				<TR bgcolor="#cacaca"><td align=center><font size=2>Author</td><td align=center><font size=2>-</td></tr>
				<TR bgcolor="#d8d8d8"><td align=center><font size=2>Peer-viewer</td><td align=center><font size=2><button onclick="recordClick('delete','Peer-viewer',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
				<TR bgcolor="#cacaca"><td align=center><font size=2>Manager</td><td align=center><font size=2><button onclick="recordClick('delete','Manager',<%= id %>)"><img src="image/delete.gif"></button></td></tr>
<%
	}
%>
				<tr><td><font size=2>&nbsp;</td><td><font size=2>&nbsp;</td></tr>
	</table>
	
<% 
 if(!p.equals("6"))
 {
%>
	<FORM name="roleform">
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>ADD ROLE</font></center></th></tr>
	     <tr><td><font size=2>&nbsp;</td><td><font size=2>&nbsp;</td><td><font size=2>&nbsp;</td></tr>
		<tr>
			<td align=right><font size=2>Select role:</td>
			<td align=left><font size=2>
				<SELECT NAME="role" size="1">
					<OPTION VALUE="Peer-viewer">Peer-viewer</OPTION>
					<OPTION VALUE="Manager">Manager</OPTION>
				</SELECT>
			</td>
		</tr>
		<tr><td><font size=2>&nbsp;</td>
			<td align=left><font size=2><button onclick="addRole(<%= id %>)">Add</button></td>
		</tr>
		<tr><td><font size=2>&nbsp;</td><td><font size=2>&nbsp;</td><td><font size=2>&nbsp;</td></tr>
	</table>
	</FORM>
<% 
}
%>


<form name="hideform" action="/nvcrs/manager.go" method="post">
<input type="hidden" name="target" value="role">
<input type="hidden" name="act" value="delete">
<input type="hidden" name="id" value="">
<input type="hidden" name="role" value="Author">
</form>
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
<br><br>
