<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!-- 
*   '$RCSfile: ReferenceSummaryView.jsp,v $'
*   Purpose: View a summary of all references in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: farrell $'
*  '$Date: 2003-07-01 16:57:03 $'
*  '$Revision: 1.1 $'
*
*
-->
<head>

<title>Reference Form -- view extant -- short view</title>
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


   
  <p>Click on the left-most column to see more on a reference <font color="red">Not wired up yet</font></p>
  <table border="1" cellspacing="0" cellpadding="0">
  

    <tr class="grey">
      <td><p><span class="category">ref ID</span></p></td>
      <td><p><span class="category">short Name</span></p></td>    
      <td><p><span class="category">reference Type</span></p></td>
      <td><p><span class="category">title</span></p></td>    
    </tr>
    <br/>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="reference" name="references" type="org.vegbank.common.model.ReferenceSummary">
    
    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>
	 
    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td><span class="item">
	<a href="">
	<bean:write name="reference" property="id"/>
	</a>
	</span>
      </td>
      <td><span class="item"><bean:write name="reference" property="shortname"/>&nbsp;</span></td>    
      <td><span class="item"><bean:write name="reference" property="referenceType"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="reference" property="title"/>&nbsp;</span></td>    
    </tr>
    </logic:iterate>

  </table>

  <br/>
  
  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
