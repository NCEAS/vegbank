<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
  *   '$Id: list_certifications.jsp,v 1.2 2004-04-17 02:52:06 anderson Exp $ '
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2004-04-17 02:52:06 $'
  *  '$Revision: 1.2 $'
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

		<html:errors/>

        <p class="vegbank_normal">
			Below is a list of all VegBank certification applications.
			<br/>
        </p> 
	
	<!-- main table -->
	<table width="700" border="0" cellspacing="5" cellpadding="2">
	<html:form method="post" action="SaveCertification.do">
		<!--  display list containing applicant name, date, requested perm. Delim by status. -->

    <tr> 
      <th>Applicant</th>
      <th>Requested Permission</th>
      <th>Current Permissions</th>
      <th>Status</th>
      <th><b>ACTION</b></th>
	</tr>

    <logic:iterate id="certBean" name="allApps">
    <tr> 
		<td>
			<bean:write name="certBean" property="surName"/>, <bean:write name="certBean" property="givenName"/>
			<br/>
			(<html:link action="LoadUser" paramId="usrId" paramName="certBean" paramProperty="usrId" title="edit user">
				<bean:write name="certBean" property="emailAddress"/>
			</html:link>)
		</td>

		<td>
        	<bean:write name="certBean" property="requestedCertName"/>
		</td>

		<td>
        	<bean:write name="certBean" property="currentCertLevelName"/>
		</td>

		<td>
			<html:select property="certificationstatus">
			  <option value="">--<bean:write name="certBean" property="certificationstatus"/>--</option>
			  <html:optionsCollection name="certstatuslistbean" property="allCertStatusesNames"/>
			</html:select>
		</td>
		<td>
			<html:select property="certificationstatus">
			  <option value="<bean:write name="certBean" property="certificationstatus"/>">
			  	--<bean:write name="certBean" property="certificationstatus"/>--</option>
			  <html:optionsCollection name="certstatuslistbean" property="allCertStatusesNames"/>
			</html:select>
		</td>
		<td>
			<html:submit indexed="true" value="update status" property="action"/>
			&nbsp;&nbsp;
			<html:submit indexed="true" value="view" property="action"/>
		</td>
    </tr>
	</logic:iterate>
	</html:form>
  </table>


@vegbank_footer_html_onerow@
</BODY>
</HTML>
