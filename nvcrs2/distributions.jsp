<!--

  distributions.jsp
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

<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	distribution_Form fr=(distribution_Form)pageContext.getAttribute("distribution_form", PageContext.SESSION_SCOPE);
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
		<center><font size=1 color=red><%= msg %></font></center>
<%
		manager.setMessage("");
	}
%>
	<center>
	<TABLE width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">  
	<TR>
<%
	if(current_status.equals("unsubmitted"))
	{
%>
		<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>Select
<%
	}
%>
		<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>Place name
		<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>Type
		<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>Distribution
<!--		<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>Pattern
		<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>Confidence -->
		<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>View
<%
		if(current_status.equals("unsubmitted"))
		{
%>
			<TH background="image/blue_back.gif"  align=center valign=center><font size=2 color=white>Delete
<%
		}
%>				

<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String placeName=(String)tb.get("placeName");
		if(placeName.length()>40) placeName=placeName.substring(0,40)+" ...";
		String VB_AccessionCode=(String)tb.get("VB_AccessionCode");
		if(VB_AccessionCode.length()>40) VB_AccessionCode=VB_AccessionCode.substring(0,40)+" ...";
		String placeType=(String)tb.get("placeType");
		if(placeType.length()>40) placeType=placeType.substring(0,40)+" ...";
		String placeNotes=(String)tb.get("placeNotes");
		if(placeNotes.length()>40) placeNotes=placeNotes.substring(0,40)+" ...";
		String placeDistribution=(String)tb.get("placeDistribution");
		if(placeDistribution.length()>40) placeDistribution=placeDistribution.substring(0,40)+" ...";
		String placePattern=(String)tb.get("placePattern");
		if(placePattern.length()>40) placePattern=placePattern.substring(0,40)+" ...";
		String confidence=(String)tb.get("confidence");
		if(confidence.length()>40) confidence=confidence.substring(0,40)+" ...";
		String id=(String)tb.get(primaryKey);

	    int intAction=manager.getPermissionByRecord("distribution",primaryKey,id);
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
		<TD align=center valign=center><input type="checkbox" name=<%= id %> onclick="checkClick(<%= id %>,this)">
<%
		}
%>
		<TD align=center valign=center><font size=2><%= placeName %>
		<TD align=center valign=center><font size=2><%= placeType %>
		<TD align=center valign=center><font size=2><%= placeDistribution %>
<!--		<TD align=center valign=center><font size=2>><%= placePattern %>
		<TD align=center valign=center><font size=2>><%= confidence %> -->
		<TD align=center valign=center><font size=2><button onclick="recordClick('view', <%= id %>)"><img src="image/view.gif"></button>
<%
	if(current_status.equals("unsubmitted"))
	{
		if(intAction==1 || intAction==2 || intAction==3)
		{
%>
		<TD align=center valign=center><font size=2><button onclick="recordClick('delete',<%= id %>)"><img src="image/delete.gif"></button>
<%
		}
		else
		{
%>
		<TD align=center valign=center><font size=2>-
<%
		}
	}
}
%>
</TABLE>
<TABLE width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">  
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<%
String primaryKeyValue="";
int intAction=manager.getPermissionByRecord("distribution",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
	<tr>
		<td align="center"><button onclick="submitClick('add')" >Add</button>
		&nbsp;&nbsp;&nbsp;<button onclick="submitClick('delete')" >Delete</button> </td>
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
		<td align="center"><button onclick="submitClick('add')" >Add</button>
		&nbsp;&nbsp;&nbsp;<button onclick="submitClick('delete')" >Delete</button> </td>
	</tr>
<%
}
%>
<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	</tr>
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
<form name="hideform" method="post" action="/nvcrs/distributions.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

