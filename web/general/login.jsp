<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"> 
<HTML>

<HEAD>@defaultHeadToken@
 
<TITLE>Login to VegBank</TITLE>

<link rel="stylesheet" href="@stylesheet@" type="text/css">
 
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">

@vegbank_header_html_normal@

<html:errors/>
 
<table cellspacing="0" cellpadding="0" border="0" width="799">
  <tr> 
    <td height="31" valign="top"></td>
  </tr>
  <tr> 
    <td align="center" bgcolor="#FFFFCC">
      <!-- Pull in Logon widget -->
      <%@ include file="../includes/Logon.jsp" %>

    </td>
  </tr>
</table>
<br/>

@vegbank_footer_html_onerow@
</BODY>
</HTML>
