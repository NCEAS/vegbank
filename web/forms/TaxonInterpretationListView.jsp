<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%--
<!--
*   '$RCSfile: TaxonInterpretationListView.jsp,v $'
*   Purpose: View details of all references in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: anderson $'
*  '$Date: 2004-06-29 06:51:57 $'
*  '$Revision: 1.1 $'
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
-->
--%>

<%
    String bgColor = "#FFFFF";
%>

<logic:notEmpty name="genericBean">
 <table border="1" cellspacing="3" cellpadding="3">
	 <tr class="category">
		  <td>Plant Name</td>
		  <td>Code</td>
		  <td>Type</td>
		  <td>Fit</td>
		  <td>Confidence</td>
	 </tr>

 <logic:iterate id="tint" name="genericBean" type="org.vegbank.common.model.Taxoninterpretation">

<%
	bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
%>
	 <tr class="item" bgcolor="<%= bgColor %>">
	   <td><bean:write name="tint" property="plantconceptobject.plantname"/></td>
	   <td><bean:write name="tint" property="plantconceptobject.plantcode"/></td>
	   <td><bean:write name="tint" property="interpretationtype"/></td>
	   <td><bean:write name="tint" property="taxonfit"/></td>
	   <td><bean:write name="tint" property="taxonconfidence"/></td>
	 </tr>

 </logic:iterate>
 </table>
<br/>
</logic:notEmpty>

