<!--

  plot.jsp
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
		document.plot_form.action.value=act;
		document.plot_form.submit();
	}

	function validate()
	{

		return true;
	}

</script>


<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	plot_Form fr=(plot_Form)pageContext.getAttribute("plot_form", PageContext.SESSION_SCOPE);
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
<h3>plot</h3>
<br><br>
<html:form action="/plot" onsubmit="return validate()" >
		<html:hidden  property="PLOT_ID" />
		<html:hidden  property="TYPE_ID" />
<table border="0">
	<tr>
		<td>VB_AccessionCode:</td>
		<td><html:text  property="VB_AccessionCode" /></td>
		<td>plotUse:</td>
		<td><html:text  property="plotUse" /></td>
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
int intAction=manager.getPermissionByRecord("plot",primaryKey,primaryKeyValue);
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

