<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: ReferencePartyDetailViewData.jsp,v $'
*   Purpose: View details of all ReferencePartys in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2003-07-22 01:31:39 $'
*  '$Revision: 1.1 $'
*
*
-->
<head>

<title>View Current Reference Parties -- Details</title>
<link rel="stylesheet" href="@stylesheet@" type="text/css"/>
  <style type="text/css">
  .oddrow { background-color : #FFFFCC }
  .evenrow {background-color : #FFFFFF }
  </style>
  <meta http-equiv="Content-Type" content="text/html; charset=">
  </head>

  <body>

  <!--xxx -->
  @vegbank_header_html_normal@
  <!--xxx -->
<h2>View Current Reference Parties -- Details</h2>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>
<table border="1" cellspacing="0" cellpadding="0">
<tr valign="top">

<td class="grey"><p><span class="category">ID</span></p></td>
<td class="grey"><p><span class="category">Party Type</span></p></td>
<td class="grey"><p><span class="category">Salutation</span></p></td>
<td class="grey"><p><span class="category">Given Name(s)</span></p></td>
<td class="grey"><p><span class="category">Surname</span></p></td>
<td class="grey"><p><span class="category">Suffix</span></p></td>
<td class="grey"><p><span class="category">Position Name</span></p></td>
<td class="grey"><p><span class="category">Organization Name</span></p></td>
	 </tr>
    <logic:iterate id="referenceparty" name="genericBean" type="org.vegbank.common.model.Referenceparty">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td><span class="item">
	<bean:write name="referenceparty" property="referenceparty_id"/>
	</span>
      </td>
          <td><span class="item"><bean:write name="referenceparty" property="type"/>&nbsp;</span></td>
          <td><span class="item"><bean:write name="referenceparty" property="salutation"/>&nbsp;</span>
          <td><span class="item"><bean:write name="referenceparty" property="givenname"/>&nbsp;</span>
          <td><span class="item"><bean:write name="referenceparty" property="surname"/>&nbsp;</span>
          <td><span class="item"><bean:write name="referenceparty" property="suffix"/>&nbsp;</span>
          <td><span class="item"><bean:write name="referenceparty" property="positionname"/>&nbsp;</span></td>
          <td><span class="item"><bean:write name="referenceparty" property="organizationname"/>&nbsp;</span>

	  </td>
    </tr>

    </logic:iterate>
  </table>
  <br />





  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
