<!--

  events.jsp
   Created on Wed Apr 21 14:02:14 EDT 2004
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
<center>
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr><td width=30></td><td>
		<b>Proposal :: History</b>
	</td></tr>
</table >
<BR><BR>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	event_Form fr=(event_Form)pageContext.getAttribute("event_form", PageContext.SESSION_SCOPE);
	String primaryKey=fr.getPrimaryKey();
	
%>

<%
if(fr!=null && manager!=null)
{
	String msg=(String)manager.getMessage();
	String role=manager.getUserCurrentRole();
	if(msg!=null)
	{
%>
<center><font color=blue size="1"><%= msg %></font></center>
<%
		manager.setMessage("");
	}
%>
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>PROPOSAL HISTORY</font></center></th></tr>
	     <tr><td>&nbsp;</td></tr>
    	 
    	 <tr><td align=center>
			<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<TR>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Date
					<TH  background="image/blue_back.gif"><font size=2 color=white>Action
<%
		if(role.equals("Manager"))
		{
%>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Actor
					<TH  background="image/blue_back.gif"><font size=2 color=white>Subject
<%
		}
%>
				</TD>
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String ACTION_ID=(String)tb.get("ACTION_ID");
		String eventDate=(String)tb.get("eventDate");
		String id=(String)tb.get(primaryKey);
		String uid=(String)tb.get("USR_ID");
		String sid=(String)tb.get("SUBJECTUSR_ID");

	    int intAction=manager.getPermissionByRecord("event",primaryKey,id);
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
	    	<TD align=center><font size=2><%= eventDate %>
			<TD align=center><font size=2><%= ACTION_ID %>
<%
		if(role.equals("Manager"))
		{
%>
			<TD align=center><font size=2><%= uid %>
			<TD align=center><font size=2><%= sid %>
<%
		}
%>
		</TR>
<%
	}
%>
	</TABLE>
	<tr><td>&nbsp;</td></tr>
</TABLE>
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
<form name="hideform" method="post" action="/nvcrs/events.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

