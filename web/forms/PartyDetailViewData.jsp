<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: PartyDetailViewData.jsp,v $'
*   Purpose: View a detail of all Partys in vegbank
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
-->
<head>

<title>View Current Parties -- Details</title>
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
<h2>View Current Parties -- Details</h2>
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
<table border="1" cellspacing="0" cellpadding="0">





         <tr valign="top" bgcolor="<%= bgColor %>" ><td class="grey"><p><span class="category">Party ID</span></p></td>                 <td><span class="item"><bean:write name="party" property="party_id"/>&nbsp;</span></td></tr>
         <tr valign="top" bgcolor="<%= bgColor %>" ><td class="grey"><p><span class="category">Salutation</span></p></td>               <td><span class="item"><bean:write name="party" property="salutation"/>&nbsp;</span></td></tr>
         <tr valign="top" bgcolor="<%= bgColor %>" ><td class="grey"><p><span class="category">Surname</span></p></td>		     <td><span class="item"><bean:write name="party" property="surname"/>&nbsp;</span></td></tr>
         <tr valign="top" bgcolor="<%= bgColor %>" ><td class="grey"><p><span class="category">Given Name</span></p></td>		     <td><span class="item"><bean:write name="party" property="givenname"/>&nbsp;</span></td></tr>
         <tr valign="top" bgcolor="<%= bgColor %>" ><td class="grey"><p><span class="category">Middle Name</span></p></td>		     <td><span class="item"><bean:write name="party" property="middlename"/>&nbsp;</span></td></tr>
         <tr valign="top" bgcolor="<%= bgColor %>" ><td class="grey"><p><span class="category">Organization</span></p></td>	     <td><span class="item"><bean:write name="party" property="organizationname"/>&nbsp;</span></td></tr>
         <tr valign="top" bgcolor="<%= bgColor %>" ><td class="grey"><p><span class="category">Contact Instructions</span></p></td>     <td><span class="item"><bean:write name="party" property="contactinstructions"/>&nbsp;</span></td></tr>




    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Telephone number(s)</span></p></td>
      <td >
        <!-- get telephone numbers -->
		<bean:define id="current__partyid" name="party" property="party_id"/>
		<bean:include id="currentphones"
page='<%= "@url2get_telephone_v_austere@&WHERE=where_party_pk&wparam=" + current__partyid %>' />


        <bean:write name="currentphones" filter="false" />

      </td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Address(es)</span></p></td>
      <td >
        <!-- get addresses -->
	<%-- already defined	<bean:define id="current__stratummethod" name="stratummethod" property="stratummethod_id"/> --%>
		<bean:include id="currentaddresses"
page='<%= "@url2get_address_v_austere@&WHERE=where_party_pk&wparam=" + current__partyid %>' />


        <bean:write name="currentaddresses" filter="false" />

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
