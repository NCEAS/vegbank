<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: PartySummaryViewData.jsp,v $'
*   Purpose: View a summary of all Partys in vegbank
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

<title>View Current Parties -- Summary</title>
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
<h2>View Current Parties -- Summary</h2>


  <p>Click on ID to see details for a Party .</p>
  <table border="1" cellspacing="0" cellpadding="0">


    <tr class="grey">
      <td><p><span class="category">Party ID</span></p></td>
      <td><p><span class="category">Salutation</span></p></td>
      <td><p><span class="category">Surname</span></p></td>
      <td><p><span class="category">Given Name</span></p></td>
      <td><p><span class="category">Middle Name</span></p></td>
      <td><p><span class="category">Organization</span></p></td>
      <td><p><span class="category">Contact Instructions</span></p></td>

    </tr>
    <br/>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="party" name="genericBean" type="org.vegbank.common.model.Party">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td><span class="item">
	<a href='/vegbank@url2get_party_v_dtl_pk@<bean:write name="party" property="party_id"/>'>
	<bean:write name="party" property="party_id"/>
	</a>
	</span>
      </td>
      <td><span class="item"><bean:write name="party" property="salutation"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="surname"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="givenname"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="middlename"/>&nbsp;</span></td>
	  <td><span class="item"><bean:write name="party" property="organizationname"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="contactinstructions"/>&nbsp;</span></td>
    </tr>
    </logic:iterate>

  </table>

  <br/>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
