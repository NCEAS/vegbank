<!--

  usr.jsp
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
		document.usr_form.action.value=act;
		document.usr_form.submit();
	}

	function validate()
	{

		return true;
	}

</script>


<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	usr_Form fr=(usr_Form)pageContext.getAttribute("usr_form", PageContext.SESSION_SCOPE);
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
	fr.findRecordByPrimaryKey(manager.getUserId());
%>
<center>
<h3>usr</h3>
<br><br>
<html:form action="/usr" onsubmit="return validate()" >
		<html:hidden  property="USR_ID" />
<table border="0">
	<tr>
		<td>login_name:</td>
		<td><html:text  property="login_name" /></td>
		<td>password:</td>
		<td><html:text  property="password" /></td>
	</tr>
	<tr>
		<td>permission:</td>
		<td><html:text  property="permission" /></td>
		<td>role:</td>
		<td><html:text  property="role" /></td>
	</tr>
	<tr>
		<td>last_name:</td>
		<td><html:text  property="last_name" /></td>
		<td>first_name:</td>
		<td><html:text  property="first_name" /></td>
	</tr>
	<tr>
		<td>middle_initial:</td>
		<td><html:text  property="middle_initial" /></td>
		<td>street:</td>
		<td><html:text  property="street" /></td>
	</tr>
	<tr>
		<td>city:</td>
		<td><html:text  property="city" /></td>
		<td>state:</td>
		<td><html:text  property="state" /></td>
	</tr>
	<tr>
		<td>zip:</td>
		<td><html:text  property="zip" /></td>
		<td>phone:</td>
		<td><html:text  property="phone" /></td>
	</tr>
	<tr>
		<td>email:</td>
		<td><html:text  property="email" /></td>
		<td></td>
		<td></td>
	</tr>
<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("usr",primaryKey,primaryKeyValue);
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

