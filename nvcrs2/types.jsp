<!--

  types.jsp
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
		document.hideform_types.id.value=tmp;
		document.hideform_types.act.value=act;
		document.hideform_types.submit();
	}

	function recordClick(act,id)
	{
		document.hideform_types.act.value=act;
		document.hideform_types.id.value=id;
		document.hideform_types.submit();
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
		<b>Proposal :: Types</b>
	</td></tr>
</table >
<BR><BR>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	
	type_Form fr=(type_Form)pageContext.getAttribute("type_form", PageContext.SESSION_SCOPE);
	String primaryKey=fr.getPrimaryKey();
%>

<%
if(fr!=null && manager!=null)
{
	String msg=(String)manager.getMessage();
	String current_status=manager.getProposalStatus();
	if(msg!=null)
	{
%>
<center><font color=blue size="1"><%= msg %></font></center>
<%
		manager.setMessage("");
	}
%>

	
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>TYPES</font></center></th></tr>
	     <tr><td>&nbsp;</td></tr>
    	 
    	 <tr><td align=center>
			<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<TR>
					<%
					if(current_status.equals("unsubmitted"))
					{
					%>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Select
					<%
					}
					%>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Action
					<TH  background="image/blue_back.gif"><font size=2 color=white>Level
					<TH  background="image/blue_back.gif"><font size=2 color=white>Primary Name
					<TH  background="image/blue_back.gif"><font size=2 color=white>View
					<%
					if(current_status.equals("unsubmitted"))
					{
					%>
					<TH  background="image/blue_back.gif"><font size=2 color=white>Delete
					<%
					}
					%>
					
				</TR>
<%
		ArrayList records=fr.getRecords();
		int num=records.size();
		int i;
		for(i=0;i<num;i++)
		{
			Hashtable tb=(Hashtable)records.get(i);
			String actionType=(String)tb.get("actionType");
		
			String level=(String)tb.get("level");
			if(level.length()>40) level=level.substring(0,40)+" ...";
			String primaryName=(String)tb.get("primaryName");
			if(primaryName.length()>40) primaryName=primaryName.substring(0,40)+" ...";
		
			String id=(String)tb.get(primaryKey);

	    	int intAction=manager.getPermissionByRecord("type",primaryKey,id);
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
					%>
					
					<TD align=center><font size=2><%= actionType %>
					<TD align=center><font size=2><%= level %>
					<TD align=center><font size=2><%= primaryName %>
					<TD align=center><font size=2><button onclick="recordClick('view', <%= id %>)"><img src="image/view.gif"></button>
<%
			if(current_status.equals("unsubmitted"))
			{
				if(intAction==1 || intAction==2 || intAction==3)
				{
%>
					<TD align=center><button onclick="recordClick('delete',<%= id %>)"><img src="image/delete.gif"></button>
<%
				}
				else
				{
%>
					<TD align=center><font size=2>-
<%
				}
			}
%>
				</TR>
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
	String primaryKeyValue="-1";
	int intAction=manager.getPermissionByRecord("type",primaryKey,primaryKeyValue);
	if(intAction==0)
	{
%>
				<tr>
					<td align="right"><button onclick="submitClick('add')" >Add</button> </td>
					<td align="left"><button onclick="submitClick('delete')" >Delete</button> </td>

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
					<td align="right"><button onclick="submitClick('add')" >Add</button> </td>
					<td align="left"><button onclick="submitClick('delete')" >Delete</button> </td>
				
<%
	}
%>
			<td align="right"><a href="proposal.go?target=correlations">Correlations</a></td>	
			</tr>
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
<form name="hideform_types" method="post" action="/nvcrs/types.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

