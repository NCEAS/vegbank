<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: ProjectDetailViewData.jsp,v $'
*   Purpose: View a summary of all Projects in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2004-04-08 05:44:28 $'
*  '$Revision: 1.3 $'
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
<head>@defaultHeadToken@

<title>View Current Projects -- Details</title>
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

<h2>View Current Projects -- Details</h2>

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
<table border="1" cellspacing="0" cellpadding="0">










    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td class="grey"><p><span class="category">Project ID</span></p></td>
      <td><span class="item">

	<bean:write name="project" property="project_id"/>

	</span>
      </td>
    </tr>
    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Project Name</span></p></td>
      <td><span class="item"><bean:write name="project" property="projectname"/>&nbsp;</span></td>
    </tr>
    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td class="grey"><p><span class="category">Project Description</span></p></td>
      <td><span class="item"><bean:write name="project" property="projectdescription"/>&nbsp;</span></td>
    </tr>
    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Start Date</span></p></td>
      <td><span class="item"><bean:write name="project" property="startdate"/>&nbsp;</span></td>
    </tr>
    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Stop Date</span></p></td>
      <td><span class="item"><bean:write name="project" property="stopdate"/>&nbsp;</span></td>

    </tr>
    <!-- project contributors for this project -->
    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Project Contributors for this project</span></p></td>
      <td >

		<bean:define id="current__project" name="project" property="project_id"/>
		<bean:include id="currentprojectpc"
page='<%= "@url2get_projectcontributor_v_austere@&WHERE=where_project_pk&wparam=" + current__project %>' />


        <bean:write name="currentprojectpc" filter="false" />

      </td>
    </tr>
    <!-- observations for this project : -->

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Plot-Observations belonging to this project</span></p></td>
      <td >

<%-- already defined	<bean:define id="current__project" name="project" property="project_id"/> --%>
		<bean:include id="currentprojectplots"
page='<%= "@url2get_observation_v_austere@&WHERE=where_project_pk&wparam=" + current__project %>' />


        <bean:write name="currentprojectplots" filter="false" />

      </td>
    </tr>

  </table>
  <br />
    </logic:iterate>



  <br/>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
