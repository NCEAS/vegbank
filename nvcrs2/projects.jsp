<!--

  projects.jsp
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

</script>;

<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	project_Form fr=(project_Form)pageContext.getAttribute("project_form", PageContext.SESSION_SCOPE);
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
	<TABLE border="0" >
	<CAPTION><EM>projects</EM></CAPTION>
	<TR>
		<TH bgcolor="#aacc00">Select
		<TH bgcolor="#ccffcc">projectName
		<TH bgcolor="#ccffcc">projectDescription
		<TH bgcolor="#ccffcc">supporting_doc_URL
		<TH bgcolor="#aacc00">View<TH bgcolor="#aacc00">Edit<TH bgcolor="#aacc00">Delete
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String projectName=(String)tb.get("projectName");
		if(projectName.length()>40) projectName=projectName.substring(0,40)+" ...";
		String projectDescription=(String)tb.get("projectDescription");
		if(projectDescription.length()>40) projectDescription=projectDescription.substring(0,40)+" ...";
		String supporting_doc_URL=(String)tb.get("supporting_doc_URL");
		if(supporting_doc_URL.length()>40) supporting_doc_URL=supporting_doc_URL.substring(0,40)+" ...";
		String id=(String)tb.get(primaryKey);

	    int intAction=manager.getPermissionByRecord("project",primaryKey,id);
	    
%>
		<TR><TD><input type="checkbox" name=<%= id %> onclick="checkClick(<%= id %>,this)">
		<TD><%= projectName %>
		<TD><%= projectDescription %>
		<TD><%= supporting_doc_URL %>
		<TD><button onclick="recordClick('view', <%= id %>)">View</button>
<%
		if(intAction==1 || intAction==2 || intAction==3)
		{
%>
		<TD><button onclick="recordClick('view', <%= id %>)">Edit</button>
		<TD><button onclick="recordClick('delete',<%= id %>)">Delete</button>
<%
		}
		else
		{
%>
		<TD bgcolor="#ccffcc">-
		<TD bgcolor="#ccffcc">-
<%
		}
	}
%>
</TABLE>
<table>
<%
String primaryKeyValue="";
int intAction=manager.getPermissionByRecord("project",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
	<tr>
		<td align="right"><button onclick="submitClick('add')" >Add</button> </td>
		<td align="right"><button onclick="submitClick('delete')" >Delete</button> </td>
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
		<td align="right"><button onclick="submitClick('delete')" >Delete</button> </td>
	</tr>
<%
}
%>
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

<form name="hideform" method="post" action="/nvcrs/projects.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

