<!--

  correlations.jsp
   Created on Wed Apr 21 15:31:07 EDT 2004
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
		<b>Proposal :: Types :: Correlations</b>
	</td></tr>
</table >
<BR><BR>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	correlation_Form fr=(correlation_Form)pageContext.getAttribute("correlation_form", PageContext.SESSION_SCOPE);
	type_Form tpForm=(type_Form)pageContext.getAttribute("type_form", PageContext.SESSION_SCOPE);
	String primaryKey=fr.getPrimaryKey();
%>

<%
if(fr!=null && manager!=null && tpForm!=null)
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
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>CORRELATIONS</font></center></th></tr>
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
					<TH  background="image/blue_back.gif"><font size=2 color=white>From type
					<TH  background="image/blue_back.gif"><font size=2 color=white>To type
					<TH  background="image/blue_back.gif"><font size=2 color=white>Convergence
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
		
		String typeid=(String)tb.get("TYPE_ID");
		String rel_typeid=(String)tb.get("CORRELATEDTYPE_ID");
		
		Hashtable t1=tpForm.getRecord(typeid);
		Hashtable t2=tpForm.getRecord(rel_typeid);
		
		if(t1!=null) typeid=(String)t1.get("primaryName");
		if(t2!=null) rel_typeid=(String)t2.get("primaryName");
		String convergence=(String)tb.get("convergence");
		String id=(String)tb.get(primaryKey);

	    int intAction=manager.getPermissionByRecord("correlation",primaryKey,id);
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
		<TD align=center><font size=2><%= typeid %>
		<TD align=center><font size=2><%= rel_typeid %>
		<TD align=center><font size=2><%= convergence %>
		<TD><button onclick="recordClick('view', <%= id %>)"><img src="image/view.gif"></button>
<%
   	if(current_status.equals("unsubmitted"))
		{
		if(intAction==1 || intAction==2 || intAction==3)
		{
%>
		<TD><button onclick="recordClick('delete',<%= id %>)"><img src="image/delete.gif"></button>
<%
		}
		else
		{
%>
		<TD bgcolor="#ccffcc">-
<%
		}
	}
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
String primaryKeyValue="";
int intAction=manager.getPermissionByRecord("correlation",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
	<tr>
		<td align="right"><button onclick="submitClick('add')" >Add</button> </td>
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
		<td align="right"><button onclick="submitClick('add')" >Add</button> </td>
		<td align="left"><button onclick="submitClick('delete')" >Delete</button> </td>
	</tr>
<%
}
%>
			</tr>
			</table>
		</td></tr>
		<tr><td>&nbsp;</td></tr>
</table>
</font>
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

<form name="hideform" method="post" action="/nvcrs/correlations.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

