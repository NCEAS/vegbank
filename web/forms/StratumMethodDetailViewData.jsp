<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: StratumMethodDetailViewData.jsp,v $'
*   Purpose: View a summary of all StratumMethods in vegbank
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

<title>View Current Stratum Methods -- Details</title>
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
<h2>View Current Stratum Methods -- Details</h2>
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
<table border="1" cellspacing="0" cellpadding="0">



    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td class="grey"><p><span class="category">Stratum Method ID</span></p></td>
      <td><span class="item">

	<bean:write name="stratummethod" property="stratummethod_id"/>

	</span>
      </td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >

	      <td class="grey"><p><span class="category">Stratum Method Name</span></p></td>
	      <td><span class="item"><bean:write name="stratummethod" property="stratummethodname"/>&nbsp;</span></td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td class="grey"><p><span class="category">Stratum Method Description</span></p></td>
      <td><span class="item"><bean:write name="stratummethod" property="stratummethoddescription"/>&nbsp;</span></td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Reference</span></p></td>
      <td>

      <span class="item"><a href='/vegbank@url2get_reference_v_dtl_pk@<bean:write name="stratummethod" property="reference_id"/>'>

      <!-- get reference_ID translation -->
	  		<bean:define id="current__refid" name="stratummethod" property="reference_id"/>
	  		<bean:include id="current__refshortname"
page='<%= "@url2get_reference_v_pktranslate_pk@" + current__refid %>' />
	          <bean:write name="current__refshortname" filter="false" />
		</a>&nbsp;</span>
	  </td>
    </tr>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td class="grey"><p><span class="category">Stratum Types belonging to this Stratum Method</span></p></td>
      <td >
        <!-- get stratumTypes -->
		<bean:define id="current__stratummethod" name="stratummethod" property="stratummethod_id"/>
		<bean:include id="currentstratummethodtypes"
page='<%= "@url2get_stratumtype_v_austere@&WHERE=where_stratummethod_pk&wparam=" + current__stratummethod %>' />


        <bean:write name="currentstratummethodtypes" filter="false" />

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
