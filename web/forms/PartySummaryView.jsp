<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: PartySummaryView.jsp,v $'
*   Purpose: View a summary of all Partys in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: farrell $'
*  '$Date: 2003-07-12 00:03:36 $'
*  '$Revision: 1.3 $'
*
*
-->
<head>

<title>Party Form -- view extant -- short view</title>
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



  <p>Click on the left-most column to see more on a Party <font color="red">Not wired up yet</font></p>
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
	<a href="">
	<bean:write name="party" property="PARTY_ID"/>
	</a>
	</span>
      </td>
      <td><span class="item"><bean:write name="party" property="salutation"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="surName"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="givenName"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="middleName"/>&nbsp;</span></td>
	  <td><span class="item"><bean:write name="party" property="organizationName"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="party" property="contactInstructions"/>&nbsp;</span></td>
    </tr>
    </logic:iterate>

  </table>

  <br/>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
