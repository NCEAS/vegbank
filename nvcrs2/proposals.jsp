<!--

  proposals.jsp
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
		<b>Proposals</b>
	</td></tr>
</table >
<BR><BR>

<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	proposal_Form fr=(proposal_Form)pageContext.getAttribute("proposal_form", PageContext.SESSION_SCOPE);
	
%>

<%
if(fr!=null && manager!=null)
{
	String primaryKey=fr.getPrimaryKey();
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
	<center>
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>PROPOSALS</font></center></th></tr>
	     <tr><td>&nbsp;</td></tr>
	<tr><td align=center>
			<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">    
			<TR >
				<TH background="image/blue_back.gif"><font size=2 color=white>Select
				<TH background="image/blue_back.gif"><font size=2 color=white>Summary
				<TH background="image/blue_back.gif"><font size=2 color=white>Current status
				<TH background="image/blue_back.gif"><font size=2 color=white>View
				<TH background="image/blue_back.gif"><font size=2 color=white>Delete
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String summary=(String)tb.get("summary");
		if(summary.length()>40) summary=summary.substring(0,40)+" ...";
		String current_status=(String)tb.get("current_status");
		if(current_status.length()>40) current_status=current_status.substring(0,40)+" ...";
		String id=(String)tb.get(primaryKey);
		manager.setProposalId(id);
		manager.setProposalStatus(current_status);
	    int intAction=manager.getPermissionByRecord("proposal",primaryKey,id);
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
		if(current_status.equals("unsubmitted"))
		{
%>
				<TD align=center><input type="checkbox" name=<%= id %> onclick="checkClick(<%= id %>,this)">
<%
		}
		else
		{
%>
				<TD align=center><font size=2>-
<%
		}
%>	
				<TD align=center><font size=2><%= summary %>
				<TD align=center><font size=2><%= current_status %>
				<TD align=center><button onclick="recordClick('view', <%= id %>)"><img src="image/view.gif"></button>
<%
		if(intAction==1 || intAction==2 || intAction==3)
		{
%>
				<TD align=center><button onclick="recordClick('delete',<%= id %>)"><img src="image/delete.gif"></button>
<%
		}
		else
		{
%>
				<TD  align=center><font size=2>-
<%
		}
	}
%>
		</TABLE>
	</td></tr>
	<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td align=center>
			<table width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
<%
String primaryKeyValue="";
int intAction=manager.getPermissionByRecord("proposal",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
			<tr>
				<td align="left"><button onclick="submitClick('delete')" >Delete</button> </td>
			</tr>
<%
}
if (intAction==1)
{
%>
<%
}
if (intAction==2)
{
%>
<%
}
if (intAction==3)
{
%>
			<tr>
				<td align="left"><button onclick="submitClick('delete')" >Delete</button> </td>
			</tr>
<%
}
%>
		</table>
	</td></tr>
	<tr><td>&nbsp;</td></tr>

</table>
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
<form name="hideform" method="post" action="/nvcrs/proposals.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

