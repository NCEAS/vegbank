<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!--
  * '$Id: change_pwd.jsp,v 1.1 2004-03-01 01:07:10 anderson Exp $'
  * Purpose: Display change user password form.
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: anderson $'
  * '$Date: 2004-03-01 01:07:10 $'
  * '$Revision: 1.1 $'
  *
  *
  -->
<html>
<HEAD>
<!-- #BeginEditable "doctitle" -->
<TITLE>Change Your Password</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">
@vegbank_header_html_normal@

<br>
<br>
<h2 align="center" class="vegbank">Change Password</h2>

<html:errors/>

<html:form method="post" action="/UpdatePassword.do" >
<html:hidden property="action" value="update"/>

	<table width="700" border="0" cellspacing="5" cellpadding="3">
	<tr> 
      <td colspan="2"><span class="vegbank_small">Current Password: </span><br/>
          <input type="password" name="password" size="25" maxlength="100"/>
	  </td>
    </tr>

	
	<tr> 
      <td colspan="2"><span class="vegbank_small">New Password: </span><br/>
        <input type="password" name="newpassword1" size="25" maxlength="100"/>
	  </td>
    </tr>

	<tr> 
      <td colspan="2"><span class="vegbank_small">Re-type New Password: </span><br/>
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


