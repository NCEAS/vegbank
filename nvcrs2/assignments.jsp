<!--

  events.jsp
   Created on Wed Apr 21 14:02:14 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="org.vegbank.nvcrs.util.*" %>
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
		if(act=="view")
			document.hideform.action="/nvcrs/proposals.go";
		if(act=="evaluation")
			document.hideform.action="/nvcrs/proposal.go";
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
		<b>Proposal :: Assignments</b>
	</td></tr>
</table >
<BR><BR>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	Assignments assigns=(Assignments)pageContext.getAttribute("assignments", PageContext.SESSION_SCOPE);
%>

<%
if(manager!=null && assigns!=null)
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
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>PROPOSAL ASSIGNMENTS</font></center></th></tr>
	     <tr><td>&nbsp;</td></tr>
    	 
    	 <tr><td align=center>
			<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<TR>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Date
					<TH  background="image/blue_back.gif"><font size=2 color=white>Proposal
					<TH  background="image/blue_back.gif"><font size=2 color=white>Peer-viewer
					<TH  background="image/blue_back.gif"><font size=2 color=white>Status

<%
		if(role.equals("Peer-viewer"))
		{
%>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Accept
					<TH  background="image/blue_back.gif"><font size=2 color=white>Decline
<%
		}
%>
				</TR>
<%
	int num=assigns.getAssignmentCount();
	int i;
	for(i=0;i<num;i++)
	{
		Assignment assign=assigns.getAssignment(i);
		String id=assign.getEventId();
		String dt=assign.getDate();
		String note=assign.getNote();
		String uid=assign.getAssignToId();
		String uname=assign.getAssignTo();
		String status=assign.getStatus();
		String proposalid=assign.getProposalId();
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
			<TD align=center><font size=2><%= dt %>
			<TD align=center><font size=2><button onclick="recordClick('view',<%= proposalid %>)"><img src="image/view.gif"></button>
<%
		if(role.equals("Manager"))
		{
%>
			<TD align=center><font size=2><a href="assignments.go?target=peer-viewer&id=<%= uid %>"><%= uname %></a>
<%
		}
		else
		{
%>
			<TD align=center><font size=2><%= uname %>
<%      
		}   
%>
	
			<%
				if(status.equals("new"))
				{
			%>
				<TD align=center><font size=2><img src="image/newanima.gif" />
			<%
				}
				else
				{
			%>
				<TD align=center><font size=2><%= status %>
			<%
				}
			%>

<%
		if(role.equals("Peer-viewer"))
		{
			if(status.equals("new"))
			{
%>
				<TD align=center><font size=2><button onclick="recordClick('accept', <%= id %>)">Accept</button>
				<TD align=center><font size=2><button onclick="recordClick('decline',<%= id %>)">Decline</button>
<%
			}
			else
			{
%>
				<TD  align=center><font size=2>-
				<TD  align=center><font size=2>-
<%
			}
		}
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
<form name="hideform" method="post" action="/nvcrs/peer-viewer.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

