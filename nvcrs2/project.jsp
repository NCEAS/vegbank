<!--

  project.jsp
   Created on Wed Apr 21 12:25:19 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	function submitClick(act)
	{
		document.project_form.action.value=act;
		document.project_form.submit();
	}

	function validate()
	{

		return true;
	}
	
	function selectProject()
	{
		
	}

</script>

<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Proposal :: Project
</td></tr>
</table >
<br>
<br>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	project_Form fr=(project_Form)pageContext.getAttribute("project_form", PageContext.SESSION_SCOPE);
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
<center><font color=blue size="1"><%= msg %></font></center>
<%
	manager.setMessage("");
	}
%>
<%
if(fr!=null && manager!=null)
{
	String primaryKey=fr.getPrimaryKey();
	String primaryKeyValue=fr.getFieldValue(primaryKey);
	int intAction=manager.getPermissionByRecord("project",primaryKey,primaryKeyValue);
	
	String doc1=fr.getDocument1();
	String doc2=fr.getDocument2();
	String doc3=fr.getDocument3();
	
%>
<center>
<html:form action="/project" onsubmit="return validate()" >
		<html:hidden  property="PROJECT_ID" />
		<html:hidden  property="document1" />
		<html:hidden  property="document2" />
		<html:hidden  property="document3" />
		<font size=2 >
		<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>PROJECT</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
    	<%
    		if(intAction>-1 && intAction<4)
    		{
    	%>
    	<tr>
			<td align=right>&nbsp;</td>
			<td><a href="select_project.go">Select from existing projects</a></td>
		</tr> 
		<%
			}
		%>
		 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		<tr>
			<td align=right>Project name:</td>
			<td><html:text  property="projectName" /></td>
		</tr>
		<tr>
			<td align=right>Project Description:</td>
			<td width=350><html:textarea  property="projectDescription" cols="35" rows="10"/></td>
		</tr>
		<tr>
			<td align=right valign=top>Supporting Documents:</td>
			<td>
				<table width=350 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<%
					if(!primaryKeyValue.equals(BeanManager.UNKNOWN_ID) && (intAction==1 || intAction==2||intAction==3))
					{
				%>
					<tr><td><a href="project_document_main.jsp">[ Edit ]</a></td></tr>
				<%
					}
				%>
				<%
					if(doc1.trim().length()>0)
					{
				%>
					<tr><td><a href="documents/<%= doc1 %>" target="_blank"><%= doc1 %></a></td></tr>
				<%
					}
				%>
				<%
					if(doc2.trim().length()>0)
					{
				%>
					<tr><td><a href="documents/<%= doc2 %>" target="_blank"><%= doc2 %></a></td></tr>
				<%
					}
				%>
				<%
					if(doc3.trim().length()>0)
					{
				%>
					<tr><td><a href="documents/<%= doc3 %>" target="_blank"><%= doc3 %></a></td></tr>
				<%
					}
				%>
				</table>
			</td>
		</tr>
		<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
        <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%


if(intAction==0)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="add" value="add" onclick="submitClick('add')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==1)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="save" value="save" onclick="submitClick('save')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==2)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
		</td>
</tr>
<%
}
if (intAction==3)
{
%>
<tr>
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> 
	<td align="left"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
		<html:submit property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
%>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
</table>
</html:form>
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

