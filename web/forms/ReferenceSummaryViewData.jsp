<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: ReferenceSummaryViewData.jsp,v $'
*   Purpose: View a summary of all references in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2004-07-13 18:42:00 $'
*  '$Revision: 1.4 $'
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

<title>View Current References -- Summary</title>
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

<h2>View Current References -- Summary</h2>

  <p>Click on the left-most column to see more on a reference.</p>
  <table border="1" cellspacing="0" cellpadding="0">


    <tr class="grey">
       <td><p><span class="category">more?</span></p></td>
       <td><p><span class="category">Short Name</span></p></td>
       <td><p><span class="category">Reference Type</span></p></td>
       <td><p><span class="category">Title</span></p></td>
       <td><p><span class="category">Publication Date</span></p></td>
       <td><p><span class="category">Journal</span></p></td>
       <td><p><span class="category">Volume</span></p></td>
       <td><p><span class="category">Issue</span></p></td>
       <td><p><span class="category">Page Range</span></p></td>

       <td><p><span class="category">Publisher</span></p></td>
       <td><p><span class="category">ISBN</span></p></td>
       <td><p><span class="category">Edition</span></p></td>
       <td><p><span class="category">Additional Information</span></p></td>


    </tr>
    <br/>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="reference" name="genericBean" type="org.vegbank.common.model.Reference">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >

<td><span class="item">
  <a
href='/vegbank@url2get_reference_v_dtl_pk@<bean:write name="reference" property="reference_id"/>'>more</a>&nbsp;
</span></td>
<td><span class="item"><bean:write name="reference" property="shortname"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="referencetype"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="title"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="pubdate"/>&nbsp;</span></td>

<!-- translate journal ID -->
<td><span class="item">

<a href='/vegbank@url2get_referencejournal_v_dtl_pk@<bean:write name="reference" property="referencejournal_id"/>'>

      <!-- get referenceJournal_ID translation -->
	  		<bean:define id="current__refjournalid" name="reference" property="referencejournal_id"/>
	  		<bean:include id="current__refjournalname"
			   page='<%= "@url2get_referencejournal_v_pktranslate_pk@" + current__refjournalid %>' />
	          <bean:write name="current__refjournalname" filter="false" />
		</a>&nbsp;

</a>
&nbsp;</span></td>

<td><span class="item"><bean:write name="reference" property="volume"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="issue"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="pagerange"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="publisher"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="isbn"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="edition"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="reference" property="additionalinfo"/>&nbsp;</span></td>


    </tr>
    </logic:iterate>

  </table>

  <br/>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
