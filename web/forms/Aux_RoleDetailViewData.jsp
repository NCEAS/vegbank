<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: Aux_RoleDetailViewData.jsp,v $'
*   Purpose: View a detail of all Aux_Roles in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2003-07-22 01:31:38 $'
*  '$Revision: 1.1 $'
*
*
-->
<head>

<title>View Current Roles -- Details</title>
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
<h2>View Current Roles -- Details</h2>
<!-- data -->


<table border="1" cellspacing="0" cellpadding="0">



    <tr class="grey">
      <td rowspan="2"><p><span class="category">Role ID</span></p></td>
      <td rowspan="2"><p><span class="category">Role Code</span></p></td>
      <td rowspan="2"><p><span class="category">Role Description</span></p></td>
      <td colspan="4"><p><span class="category">Role Allowed on Table? (1=req'd, 2=Allowed)</span></p></td>
    </tr>
    <tr class="grey">
      <td><p><span class="category">ProjectContributor</span></p></td>
      <td><p><span class="category">ObservationContributor</span></p></td>
      <td><p><span class="category">TaxonInterpretation</span></p></td>
      <td><p><span class="category">ClassInterpretation</span></p></td>

    </tr>




    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="aux_role" name="genericBean" type="org.vegbank.common.model.Aux_role">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td><span class="item">

	<bean:write name="aux_role" property="role_id"/>

	</span>
      </td>
    <td><span class="item"><bean:write name="aux_role" property="rolecode"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="aux_role" property="roledescription"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="aux_role" property="roleproject"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="aux_role" property="roleobservation"/>&nbsp;</span></td>
	  <td><span class="item"><bean:write name="aux_role" property="roletaxonint"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="aux_role" property="roleclassint"/>&nbsp;</span></td>
    </tr>


    </logic:iterate>

</table>

  <br/>



  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
