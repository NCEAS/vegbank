<!--

  usr.jsp
   Created on Thu Apr 15 18:20:09 EDT 2004
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

<tr>
	<td></td>
	<td align="right"><html:button property="add" value="register" onclick="submitClick('register')" /> 

</tr>

</table>
</html:form>
</center>
