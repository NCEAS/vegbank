<!--

  type.jsp
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
		document.type_form.action.value=act;
		document.type_form.submit();
	}

	function validate()
	{

		return true;
	}

</script>


<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	type_Form fr=(type_Form)pageContext.getAttribute("type_form", PageContext.SESSION_SCOPE);
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
<h3>type</h3>
<br><br>
<html:form action="/type" onsubmit="return validate()" >
		<html:hidden  property="TYPE_ID" />
		<html:hidden  property="PROPOSAL_ID" />
<table border="0">
	<tr>
		<td>actionType:</td>
		<td><html:text  property="actionType" /></td>
		<td>userTypeCode:</td>
		<td><html:text  property="userTypeCode" /></td>
	</tr>
	<tr>
		<td>VB_AccessionCode:</td>
		<td><html:text  property="VB_AccessionCode" /></td>
		<td>sausageAccessionCode:</td>
		<td><html:text  property="sausageAccessionCode" /></td>
	</tr>
	<tr>
		<td>level:</td>
		<td><html:text  property="level" /></td>
		<td>primaryName:</td>
		<td><html:text  property="primaryName" /></td>
	</tr>
	<tr>
		<td>confidence:</td>
		<td><html:text  property="confidence" /></td>
		<td>typeSummary:</td>
		<td><html:text  property="typeSummary" /></td>
	</tr>
	<tr>
		<td>classifyComments:</td>
		<td><html:text  property="classifyComments" /></td>
		<td>GRank:</td>
		<td><html:text  property="GRank" /></td>
	</tr>
	<tr>
		<td>GRankDate:</td>
		<td><html:text  property="GRankDate" /></td>
		<td>GRankReasons:</td>
		<td><html:text  property="GRankReasons" /></td>
	</tr>
	<tr>
		<td>wetlandIndicator:</td>
		<td><html:text  property="wetlandIndicator" /></td>
		<td>environmentSummary:</td>
		<td><html:text  property="environmentSummary" /></td>
	</tr>
	<tr>
		<td>vegtationSummary:</td>
		<td><html:text  property="vegtationSummary" /></td>
		<td>succession:</td>
		<td><html:text  property="succession" /></td>
	</tr>
	<tr>
		<td>rational:</td>
		<td><html:text  property="rational" /></td>
		<td>additionalNotes:</td>
		<td><html:text  property="additionalNotes" /></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("type",primaryKey,primaryKeyValue);
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

