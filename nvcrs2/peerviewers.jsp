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
		if(act=="assign")
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
			document.hideform.submit();
		}
		else
			history.goback(-1);
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
		<b>Peer-viewers</b>
	</td></tr>
</table >
<BR><BR>

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
	<center>
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>PEER-VIEWERS</font></center></th></tr>
	     <tr><td>&nbsp;</td></tr>
    	 
    	 <tr><td align=center>
			<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<TR>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Select
					<TH   background="image/blue_back.gif"><font size=2 color=white>last_name
					<TH  background="image/blue_back.gif"><font size=2 color=white>first_name
					<TH  background="image/blue_back.gif"><font size=2 color=white>middle_initial
					<TH  background="image/blue_back.gif"><font size=2 color=white>phone
					<TH   background="image/blue_back.gif"><font size=2 color=white>email
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String login_name=(String)tb.get("login_name");
		if(login_name.length()>40) login_name=login_name.substring(0,40)+" ...";
		String password=(String)tb.get("password");
		if(password.length()>40) password=password.substring(0,40)+" ...";
		String permission=(String)tb.get("permission");
		if(permission.length()>40) permission=permission.substring(0,40)+" ...";
		String role=(String)tb.get("role");
		if(role.length()>40) role=role.substring(0,40)+" ...";
		String last_name=(String)tb.get("last_name");
		if(last_name.length()>40) last_name=last_name.substring(0,40)+" ...";
		String first_name=(String)tb.get("first_name");
		if(first_name.length()>40) first_name=first_name.substring(0,40)+" ...";
		String middle_initial=(String)tb.get("middle_initial");
		if(middle_initial.length()>40) middle_initial=middle_initial.substring(0,40)+" ...";
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
		<TD align=center><font size=2><input type="checkbox" name=<%= id %> onclick="checkClick(<%= id %>,this)">
		<TD align=center><font size=2><%= last_name %>
		<TD align=center><font size=2><%= first_name %>
		<TD align=center><font size=2><%= middle_initial %>
		<TD align=center><font size=2><%= phone %>
		<TD align=center><font size=2><%= email %>
<%	
	}
%>
	</TABLE>
		<td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td align=center>
			<table width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
<%
	String role=manager.getUserCurrentRole();
	if(role.equals("Manager"))
	{
%>
		<tr>
			<td align="right"><button onclick="submitClick('assign')" >Assign</button> </td>
		</tr>
<%
	}
%>
	</table>
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
</font>
</center>
<form name="hideform" method="post" action="/nvcrs/manager.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="step" value="2" >
<input type="hidden" name="act" value="assign">
</form>

