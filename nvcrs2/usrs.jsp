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

</script>;

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
	<TABLE border="0" >
	<CAPTION><EM>usrs</EM></CAPTION>
	<TR>
		<TH bgcolor="#aacc00">Select
		<TH bgcolor="#ccffcc">login_name
		<TH bgcolor="#ccffcc">password
		<TH bgcolor="#ccffcc">permission
		<TH bgcolor="#ccffcc">role
		<TH bgcolor="#ccffcc">last_name
		<TH bgcolor="#ccffcc">first_name
		<TH bgcolor="#ccffcc">middle_initial
		<TH bgcolor="#ccffcc">street
		<TH bgcolor="#ccffcc">city
		<TH bgcolor="#ccffcc">state
		<TH bgcolor="#ccffcc">zip
		<TH bgcolor="#ccffcc">phone
		<TH bgcolor="#ccffcc">email
		<TH bgcolor="#aacc00">View<TH bgcolor="#aacc00">Edit<TH bgcolor="#aacc00">Delete
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
		String street=(String)tb.get("street");
		if(street.length()>40) street=street.substring(0,40)+" ...";
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
	    
%>
		<TR><TD><input type="checkbox" name=<%= id %> onclick="checkClick(<%= id %>,this)">
		<TD><%= login_name %>
		<TD><%= password %>
		<TD><%= permission %>
		<TD><%= role %>
		<TD><%= last_name %>
		<TD><%= first_name %>
		<TD><%= middle_initial %>
		<TD><%= street %>
		<TD><%= city %>
		<TD><%= state %>
		<TD><%= zip %>
		<TD><%= phone %>
		<TD><%= email %>
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
String primaryKeyValue="-1";
int intAction=manager.getPermissionByRecord("usr",primaryKey,primaryKeyValue);
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

<form name="hideform" method="post" action="/nvcrs/usrs.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>

