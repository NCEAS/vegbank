<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%--
<!--
*   '$RCSfile: ReferencePartyPKTranslateViewData.jsp,v $'
*   Purpose: View a translation of PK: gets the string Name in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2003-07-22 01:31:39 $'
*  '$Revision: 1.1 $'
*
*
--> --%>
    <!-- data -->

    <logic:iterate id="referenceparty" name="genericBean" type="org.vegbank.common.model.Referenceparty">
      <bean:write name="referenceparty" property="surname"/>
      <bean:write name="referenceparty" property="suffix"/>,
      <bean:write name="referenceparty" property="salutation"/>
	  <bean:write name="referenceparty" property="givenname"/>
(organization: <bean:write name="referenceparty" property="organizationname"/>)
    </logic:iterate>
