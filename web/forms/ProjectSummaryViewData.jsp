<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: ProjectSummaryViewData.jsp,v $'
*   Purpose: View a summary of all projects in vegbank
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

<title>View Current Projects -- Summary</title>
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

<h2>View Current Projects -- Summary</h2>

  <p>Click on the ID (left-most) column to see more on a project.</p>
  <table border="1" cellspacing="0" cellpadding="0">


    <tr class="grey">
       <td><p><span class="category">ID</span></p></td>
       <td><p><span class="category">Name</span></p></td>
       <td><p><span class="category">Description</span></p></td>
       <td><p><span class="category">Start Date</span></p></td>
       <td><p><span class="category">Stop Date</span></p></td>

    </tr>

    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="project" name="genericBean" type="org.vegbank.common.model.Project">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>
    <tr valign="top" bgcolor="<%= bgColor %>" >

<td><span class="item">
  <a
href='/vegbank@url2get_project_v_dtl_pk@<bean:write name="project" property="project_id"/>'><bean:write name="project" property="project_id"/></a>&nbsp;
</span></td>
<td><span class="item"><bean:write name="project" property="projectname"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="project" property="projectdescription"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="project" property="startdate"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="project" property="stopdate"/>&nbsp;</span></td>


    </tr>
    </logic:iterate>

  </table>

  <br/>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
