<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: CoverMethodDetailViewData.jsp,v $'
*   Purpose: View a summary of all CoverMethods in vegbank
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

<title>View Current Cover Methods -- Details</title>
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
<h2>View Current Cover Methods -- Details</h2>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>


    <logic:iterate id="covermethod" name="genericBean" type="org.vegbank.common.model.Covermethod">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>
<table border="1" cellspacing="0" cellpadding="0">



    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td class="grey"><p><span class="category">Cover Method ID</span></p></td>
      <td><span class="item">

	<bean:write name="covermethod" property="covermethod_id"/>

	</span>
      </td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >
	      <td class="grey"><p><span class="category">Cover Type</span></p></td>
	      <td><span class="item"><bean:write name="covermethod" property="covertype"/>&nbsp;</span></td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Reference</span></p></td>
      <td>

      <span class="item"><a href='/vegbank@url2get_reference_v_dtl_pk@<bean:write name="covermethod" property="reference_id"/>'>

      <!-- get reference_ID translation -->
	  		<bean:define id="current__refid" name="covermethod" property="reference_id"/>
	  		<bean:include id="current__refshortname"
page='<%= "@url2get_reference_v_pktranslate_pk@" + current__refid %>' />
	          <bean:write name="current__refshortname" filter="false" />
		</a>&nbsp;</span>
	  </td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Cover Indexes belonging to this Cover Method</span></p></td>
      <td >
        <!-- get coverIndexes -->
		<bean:define id="current__covermethod" name="covermethod" property="covermethod_id"/>
		<bean:include id="currentcovermethodindexes"
page='<%= "@url2get_coverindex_v_austere@&WHERE=where_covermethod_pk&wparam=" + current__covermethod %>' />


        <bean:write name="currentcovermethodindexes" filter="false" />

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
