<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%--
<!--
*   '$RCSfile: Aux_RolePKTranslateViewData.jsp,v $'
*   Purpose: View a translation of PK: gets the identifying text field in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2003-07-22 01:31:38 $'
*  '$Revision: 1.1 $'
*
*
--> --%>
    <!-- data -->

    <logic:iterate id="aux_role" name="genericBean" type="org.vegbank.common.model.Aux_role">
      <bean:write name="aux_role" property="rolecode"/>
    </logic:iterate>
