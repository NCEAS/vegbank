<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
  *   '$Id: list_certifications.jsp,v 1.3 2004-04-26 20:49:43 anderson Exp $ '
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2004-04-26 20:49:43 $'
  *  '$Revision: 1.3 $'
  *
  *
  -->
<html>
<HEAD>

<TITLE>VegBank Certification Administration</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=">


</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005660" alink="#0066FF">

@vegbank_header_html_normal@

        <h2 align="center" class="vegbank">Certification Administration</h2>

		Updated: 
		<br>
		<html:messages id="updated"/>

        <p class="vegbank_normal">
			Below is a list of all VegBank certification applications.
			<br/>
        </p> 
	
	<!-- main table -->
	<table width="700" border="0" cellspacing="5" cellpadding="2">
		<!--  display list containing applicant name, date, requested perm. Delim by status. -->

    <tr> 
      <th>Applicant</th>
      <th>Requested Permission</th>
      <th>Current Permissions</th>
      <th>Status</th>
      <th><b>ACTION</b></th>
	</tr>

	<tr>

    <logic:iterate id="certBean" name="allApps">
	<html:form method="get" action="ListCertifications.do">
		<html:hidden name="certBean" property="certId"/>
    <tr> 
		<td>
			#<bean:write name="certBean" property="certId"/>: 
			<bean:write name="certBean" property="surName"/>, 
			<bean:write name="certBean" property="givenName"/>
			<br/>
			(<html:link action="LoadUser" paramId="usrId" paramName="certBean" paramProperty="usrId" title="edit user">
				<bean:write name="certBean" property="emailAddress"/>
			</html:link>)
		</td>

		<td align="center"> <bean:write name="certBean" property="requestedCertName"/> </td>

		<td align="center"> <bean:write name="certBean" property="currentCertLevelName"/> </td>

		<td align="center"> <bean:write name="certBean" property="certificationstatus"/> </td>
		<td align="center" class="vegbank">
			<html:link action="ViewCertification" paramId="certId" 
						paramName="certBean" paramProperty="certId" title="view">View</html:link>
		</td>
		</html:form>
    </tr>
	</logic:iterate>
  </table>


@vegbank_footer_html_onerow@
</BODY>
</HTML>
