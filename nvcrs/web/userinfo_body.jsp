<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.util.UsrDetails" %> 
<%@ page import="org.vegbank.nvcrs.web.UserDetailForm" %> 

<%
usr_Form fr=(UserDetailForm)pageContext.getAttribute("userDetailForm", PageContext.SESSION_SCOPE);
String msg=(String)pageContext.getAttribute("message1", PageContext.SESSION_SCOPE);
if(msg!=null)
{
%>
<center><h2><%= msg %></h2></center>
<%
	pageContext.setAttribute("message1","", PageContext.SESSION_SCOPE);
}
%>
<% 
if(fr!=null)
{
%>
<center>
<h3>Your Personal Information</h3>
<br><br>
<html:form action="/user_save" >
	<table border="0">
	   <tr>
		   <td>First name:</td>
		   <td><html:text  property="firstName" /></td>
	   </tr>
	   <tr>
		   <td>Last name:</td>
		   <td><html:text  property="lastName"/></td>
	   </tr>
	   <tr>
		   <td>Middle initial:</td>
		   <td><html:text   property="middleInitial"/></td>
	   </tr>
	   <tr>
		   <td>Street:</td>
		   <td><html:text  property="street"/></td>
	   </tr>
	   <tr>
		   <td>City:</td>
		   <td><html:text   property="city"/></td>
	   </tr>
	   <tr>
		   <td>State:</td>
		   <td><html:text  property="state"/></td>
	   </tr>
	   <tr>
		   <td>Zip:</td>
		   <td><html:text  property="zip"/></td>
	   </tr>
	   <tr>
		   <td>Phone:</td>
		   <td><html:text  property="phone"/></td>
	   </tr>
	   <tr>
		   <td>Email:</td>
		   <td><html:text  property="email"/></td>
	   </tr>
	   <tr>
		   <td>Login name:</td>
		   <td><html:text   property="loginName"/></td>
	   </tr>
	   <tr>
		   <td>Password:</td>
		   <td><html:password  property="password"/></td>
	   </tr>
	   <tr>
		   <td>Password again:</td>
		   <td><html:password property="passwordConfirm"/></td>
	   </tr>
	   <tr>
		   <td></td>
		   <td align="right"><html:submit value="Save"/> </td>
	   </tr>
	   <tr>
		<td><br></td>
	   </tr>
	</table>
	</html:form>
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
