<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!--
  * '$Id: change_pwd.jsp,v 1.5 2004-11-16 07:41:14 mlee Exp $'
  * Purpose: Display change user password form.
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: mlee $'
  * '$Date: 2004-11-16 07:41:14 $'
  * '$Revision: 1.5 $'
  *
  *
  -->
<html>
<HEAD>@defaultHeadToken@
<!-- #BeginEditable "doctitle" -->
<TITLE>Change Your Password</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>
@vegbank_header_html_normal@

<br>
<br>
<h2 align="center" class="vegbank">Change Password</h2>

<html:errors/>

<html:form method="post" action="/UpdatePassword.do" >
<html:hidden property="action" value="update"/>
 	<input type="hidden" name="usrId" value="<%=request.getParameter("usrId")%>"/>

	<table width="700" border="0" cellspacing="5" cellpadding="3">
<% 
	Boolean isAdmin = (Boolean)(request.getSession().getAttribute("isAdmin"));

	if (isAdmin != null) {
		if (!isAdmin.booleanValue()) {
%>
	<tr> 
      <td colspan="2"><span class="sizesmall">Current Password: </span><br/>
          <input type="password" name="password" size="25" maxlength="100"/>
	  </td>
    </tr>
<% 
		}
	} 
%>

	
	<tr> 
      <td colspan="2"><span class="sizesmall">New Password: </span><br/>
        <input type="password" name="newpassword1" size="25" maxlength="100"/>
	  </td>
    </tr>

	<tr> 
      <td colspan="2"><span class="sizesmall">Re-type New Password: </span><br/>
        <input type="password" name="newpassword2" size="25" maxlength="100"/>
	  </td>
    </tr>

	<tr> 
      <td colspan="2">
        <html:submit value="Change My Password"/>
		&nbsp; &nbsp; &nbsp; <html:link forward="MainMenu">Cancel</html:link>
	  </td>
    </tr>

  </table>
  
</html:form>

@vegbank_footer_html_tworow@
</BODY>
</HTML>


