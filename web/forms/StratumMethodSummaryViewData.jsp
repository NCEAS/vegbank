<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: StratumMethodSummaryViewData.jsp,v $'
*   Purpose: View a summary of all StratumMethods in vegbank
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

<title>View Current Stratum Methods -- Summary</title>
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


  <h2>View Current Stratum Methods -- Summary</h2>
  <p>Click on the left-most column to see details of a Stratum Method. </p>
  <table border="1" cellspacing="0" cellpadding="0">


    <tr class="grey">

      <td><p><span class="category">ID</span></p></td>

      <td><p><span class="category">Stratum Method Name</span></p></td>
      <td><p><span class="category">Stratum Method Description</span></p></td>
      <td><p><span class="category">Reference</span></p></td>
    </tr>
    <br/>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="stratummethod" name="genericBean" type="org.vegbank.common.model.Stratummethod">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td><span class="item">
	<a href='/vegbank@url2get_stratummethod_v_dtl_pk@<bean:write name="stratummethod" property="stratummethod_id"/>'>
	<bean:write name="stratummethod" property="stratummethod_id"/>
	</a>
	</span>
      </td>


      <td><span class="item"><bean:write name="stratummethod" property="stratummethodname"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="stratummethod" property="stratummethoddescription"/>&nbsp;</span></td>
<td> <span class="item"><a href='/vegbank@url2get_reference_v_dtl_pk@<bean:write name="stratummethod" property="reference_id"/>'>

      <!-- get reference_ID translation -->
	  		<bean:define id="current__refid" name="stratummethod" property="reference_id"/>
	  		<bean:include id="current__refshortname"
page='<%= "@url2get_reference_v_pktranslate_pk@" + current__refid %>' />
	          <bean:write name="current__refshortname" filter="false" />
		</a>&nbsp;</span></td>
    </tr>
    </logic:iterate>

  </table>

  <br/>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
