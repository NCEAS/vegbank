<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: ReferenceJournalDetailViewData.jsp,v $'
*   Purpose: View details of all ReferenceJournals in vegbank
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

<title>View Current Reference Journals -- Details</title>
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
<h2>View Current Reference Journals -- Details</h2>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>
<table border="1" cellspacing="0" cellpadding="0">
<tr valign="top">
<td class="grey"><p><span class="category">Journal ID</span></p></td>
	      <td class="grey"><p><span class="category">Journal Name</span></p></td>
	 <td class="grey"><p><span class="category">Abbreviation</span></p></td>
  <td class="grey"><p><span class="category">ISSN</span></p></td>
	 </tr>
    <logic:iterate id="referencejournal" name="genericBean" type="org.vegbank.common.model.Referencejournal">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>


    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td><span class="item">
	<bean:write name="referencejournal" property="referencejournal_id"/>
	</span>
      </td>
          <td><span class="item"><bean:write name="referencejournal" property="journal"/>&nbsp;</span></td>
          <td><span class="item"><bean:write name="referencejournal" property="abbreviation"/>&nbsp;</span></td>


      <td><span class="item"><bean:write name="referencejournal" property="issn"/>&nbsp;</span>
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
