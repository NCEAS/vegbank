<!--

  proposal.jsp
   Created on Fri Apr 16 13:43:24 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	function submitClick(act)
	{
		document.proposal_form.action.value=act;
		document.proposal_form.submit();
	}

	function validate()
	{

		return true;
	}

</script>


<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	proposal_Form fr=(proposal_Form)pageContext.getAttribute("proposal_form", PageContext.SESSION_SCOPE);
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
<center><h2><%= msg %></h2></center>
<%
	manager.setMessage("");
	}
%>
<%
if(fr!=null && manager!=null)
{
%>
<center>
<h3>proposal</h3>
<br><br>
<html:form action="/proposal" onsubmit="return validate()" >
		<html:hidden  property="PROPOSAL_ID" />
		<html:hidden  property="previousProposal_ID" />
		<html:hidden  property="PROJECT_ID" />
<table border="0">
	<tr>
		<td>summary:</td>
		<td><html:text  property="summary" /></td>
		<td>supporting_doc_URL:</td>
		<td><html:text  property="supporting_doc_URL" /></td>
	</tr>
	<tr>
		<td>current_status:</td>
		<td><html:text  property="current_status" /></td>
		<td></td>
		<td></td>
	</tr>
<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("proposal",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="add" value="add" onclick="submitClick('add')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==1)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==2)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
		</td>
</tr>
<%
}
if (intAction==3)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> 
	<td align="right"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
		<html:submit property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
%>
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

