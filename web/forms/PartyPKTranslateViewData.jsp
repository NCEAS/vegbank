<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%--
<!--
*   '$RCSfile: PartyPKTranslateViewData.jsp,v $'
*   Purpose: View a translation of PK: gets the identifying text field in vegbank
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

    <logic:iterate id="party" name="genericBean" type="org.vegbank.common.model.Party">
<bean:write name="party" property="surname"/>,&nbsp;<bean:write name="party" property="salutation"/>
<bean:write name="party" property="givenname"/>&nbsp;<bean:write name="party" property="middlename"/>
(organization: <bean:write name="party" property="organizationname"/>)
	    </logic:iterate>
