<!--

  usrs.jsp
   Created on Wed Apr 21 12:25:19 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="java.util.*" %>


<script language="javascript">

	var t=100;
	var ids=new Array(100);
	for(var k=0;k<t;k++)
		ids[k]=0;
	function checkClick(id,obj)
	{
		var i;
		for(i=0;i<t;i++)
		{
			if(ids[i]==id)
			{
				if(obj.checked==false)
				{
					ids[i]=0;
					i=t+1;
				}
			}
		}
		if(i==t && obj.checked==true)
		{
			for(i=0;i<t;i++)
			{
				if(ids[i]==0)
				{
					ids[i]=id;
					i=t+1;
				}
			}
		}
	}

	function submitClick(act)
	{
		var tmp="";
		var i;
		for(i=0;i<t;i++)
		{
			if (ids[i]!=0)
			{
				if(tmp!="") tmp+=",";
				tmp+=ids[i];
			}
		}
		document.hideform.id.value=tmp;
		document.hideform.act.value=act;
		document.hideform.submit();
	}

	function recordClick(act,id)
	{
		document.hideform.act.value=act;
		document.hideform.id.value=id;
		document.hideform.submit();
	}

	function validate()
	{
		return true;
	}

</script>
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr><td width=30></td><td>
		<b>Users</b>
	</td></tr>
</table >
<BR><BR>
<center>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	usr_Form fr=(usr_Form)pageContext.getAttribute("usr_form", PageContext.SESSION_SCOPE);
	String primaryKey=fr.getPrimaryKey();
%>

<%
if(fr!=null && manager!=null)
{
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
		<center><h2><%= msg %></h2></center>
<%
		manager.setMessage("");
	}
%>
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>USERS</font></center></th></tr>
	     <tr><td>&nbsp;</td></tr>
    	 
    	 <tr><td align=center>
			<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<TR>
					<TH   background="image/blue_back.gif"><font size=2 color=white>Last name
					<TH   background="image/blue_back.gif"><font size=2 color=white>First name
					<TH   background="image/blue_back.gif"><font size=2 color=white>Phone
					<TH   background="image/blue_back.gif"><font size=2 color=white>Email
					<TH  background="image/blue_back.gif"><font size=2 color=white>Roles
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String login_name=(String)tb.get("login_name");
		
		String password=(String)tb.get("password");
		
		String permission=(String)tb.get("permission");
		
		String role=(String)tb.get("role");
		
		
		String last_name=(String)tb.get("last_name");
		
		String first_name=(String)tb.get("first_name");
		
		String middle_initial=(String)tb.get("middle_initial");
		
		String city=(String)tb.get("city");
		if(city.length()>40) city=city.substring(0,40)+" ...";
		String state=(String)tb.get("state");
		if(state.length()>40) state=state.substring(0,40)+" ...";
		String zip=(String)tb.get("zip");
		if(zip.length()>40) zip=zip.substring(0,40)+" ...";
		String phone=(String)tb.get("phone");
		if(phone.length()>40) phone=phone.substring(0,40)+" ...";
		String email=(String)tb.get("email");
		if(email.length()>40) email=email.substring(0,40)+" ...";
		String id=(String)tb.get(primaryKey);
		
		if(permission.trim().equals("0"))role="Author";
		else if(permission.trim().equals("1"))role="Peer-viewer";
		else if(permission.trim().equals("2"))role="Manager";
		else if(permission.trim().equals("3"))role="Author & Peer-viewer";
		else if(permission.trim().equals("4"))role="Author & Manager";
		else if(permission.trim().equals("5"))role="Peer-viewer & Manager";
		else if(permission.trim().equals("6"))role="Author & Peer-viewer & Manager";
		else role="Unknown"; 
	    int intAction=manager.getPermissionByRecord("usr",primaryKey,id);
	    
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
		<TD align=center><font size=2><%= last_name %>
		<TD align=center><font size=2><%= first_name %>
		<TD align=center><font size=2><%= phone %>
		<TD align=center><font size=2><%= email %>
		<TD align=center><font size=2><a href="javascript:recordClick('editrole', <%= id %>)"><%= role %></a>
<!--		<TD align=center><font size=2><button onclick="recordClick('editrole', <%= id %>)">Edit</button> -->
<%
	}
%>
	</TABLE>
	</td></tr>
		<tr><td>&nbsp;</td></tr>
</table>
<br>
<%
}
else
{
%>
	<center>Please login or register first!</center>
<%
}
%>
</center>
<form name="hideform" method="post" action="/nvcrs/manager.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

