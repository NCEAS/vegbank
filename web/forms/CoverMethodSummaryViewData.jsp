<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: CoverMethodSummaryViewData.jsp,v $'
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

<title>View Current Cover Methods -- Summary</title>
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


  <h2>View Current Cover Methods -- Summary</h2>
  <p>Click on the left-most column (ID) to see details of a Cover Method. </p>
  <table border="1" cellspacing="0" cellpadding="0">



    <tr class="grey">

      <td><p><span class="category">ID</span></p></td>

      <td><p><span class="category">Cover Type</span></p></td>
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

    <logic:iterate id="covermethod" name="genericBean" type="org.vegbank.common.model.Covermethod">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td><span class="item">
	<a href='/vegbank@url2get_covermethod_v_dtl_pk@<bean:write name="covermethod" property="covermethod_id"/>'>
	<bean:write name="covermethod" property="covermethod_id"/>
	</a>
	</span>
      </td>



      <td><span class="item"><bean:write name="covermethod" property="covertype"/>&nbsp;</span></td>

<td> <span class="item"><a href='/vegbank@url2get_reference_v_dtl_pk@<bean:write name="covermethod" property="reference_id"/>'>

      <!-- get reference_ID translation -->
	  		<bean:define id="current__refid" name="covermethod" property="reference_id"/>
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
