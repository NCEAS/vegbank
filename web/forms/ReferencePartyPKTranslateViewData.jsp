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
*  '$Author: farrell $'
*  '$Date: 2003-08-21 21:16:43 $'
*  '$Revision: 1.2 $'
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
