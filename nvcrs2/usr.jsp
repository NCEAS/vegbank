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

<center>
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>User Infomation
</td></tr>
</table >
<br>
<br>
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
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("usr",primaryKey,primaryKeyValue);
%>
<html:form action="/usr" onsubmit="return validate()" >
		<html:hidden  property="USR_ID" />
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>USER INFORMATION</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%
if(intAction>-1 && intAction<4)
{
%>
	<tr>
		<td align=right><font size=2>Login name:</td>
		<td><font size=2><html:text  property="login_name" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Password:</td>
		<td><html:password  property="password" /></td>
	</tr>
<%
	}
%>
	<tr>
		<td align=right><font size=2>Last name:</td>
		<td><html:text  property="last_name" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>First name:</td>
		<td><html:text  property="first_name" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Middle initial:</td>
		<td><html:text  property="middle_initial" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Institute:</td>
		<td><html:text  property="institute" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Address 1:</td>
		<td><html:text  property="address1" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Address 2:</td>
		<td><html:text  property="address2" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>City:</td>
		<td><html:text  property="city" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>State:</td>
		<td><html:text  property="state" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Country:</td>
		<td><html:text  property="country" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Zip:</td>
		<td><html:text  property="zip" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Phone:</td>
		<td><html:text  property="phone" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Email:</td>
		<td><html:text  property="email" /></td>
	</tr>
	<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%

if(intAction==0)
{
%>
<tr>
	<td align="right"><html:button property="add" value="add" onclick="submitClick('add')" /> </td>
	<td align="left"><html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==1)
{
%>
<tr>
	
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> </td>
	<td align="left"><html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==2)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="delete" value="delete" onclick="submitClick('delete')" /></td>
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
	<tr><td>&nbsp;</td></tr>
</TABLE>
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

