<!--

  select_projects.jsp
   Created on Wed Apr 21 12:25:19 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="java.util.*" %>


<script language="javascript">

	var selected=0;
	function checkClick(id,obj)
	{
		selected=id;
	}

	function submitClick(act)
	{
		if(selected!=0)
		{
			document.hideform.id.value=selected;
			document.hideform.act.value=act;
			document.hideform.submit();
		}
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
<font face=Arial >
<img src="image/title_sep_line.gif">
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Proposal :: Project :: Select
</td></tr>
</table >
<br>
<br>
<center>
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
<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=1 background="image/blue_back.gif"><center><font color=white>PROJECTS</font></center></th></tr>
	     <tr><td>&nbsp;</td></tr>
    	 
    	 <tr><td align=center>
			<TABLE width=560 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<TR>
				<TH   background="image/blue_back.gif"><font size=2 color=white>Select
				<TH   background="image/blue_back.gif"><font size=2 color=white>Name
				<TH   background="image/blue_back.gif"><font size=2 color=white>Description
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String projectName=(String)tb.get("projectName");
		String projectDescription=(String)tb.get("projectDescription");
		String id=(String)tb.get(primaryKey);
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
		<TD align=center><font size=2><input type="radio" name="selected_project" onclick="checkClick(<%= id %>,this)">
		<TD align=center><font size=2><%= projectName %>
		<TD align=center><font size=2><%= projectDescription %>
<%
	}
%>
</TABLE>
	</td></tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td>
		<table width=600>
			<tr>
				<td align="center"><button onclick="submitClick('select')" >Select</button> </td>
			</tr>
		</table>
	</td></tr>
</table>
<br>

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

</center>
<form name="hideform" method="post" action="/nvcrs/projects.go">
<input type="hidden" name="id" value=" ">
<input type="hidden" name="act" value=" ">
</form>
