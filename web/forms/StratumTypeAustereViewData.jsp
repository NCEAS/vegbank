<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%-- <!--
*   '$RCSfile: StratumTypeAustereViewData.jsp,v $'
*   Purpose: View a summary of all StratumTypes in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2003-07-22 01:31:39 $'
*  '$Revision: 1.1 $'
*
*
--> --%>



  <table border="1" cellspacing="0" cellpadding="0">

    <tr class="grey">
      <td colspan="3"><p><span class="category">Stratum Type</span></p></td>
    </tr>
    <tr class="grey">


      <td><p><span class="category">Index</span></p></td>
      <td><p><span class="category">Name</span></p></td>
      <td><p><span class="category">Description</span></p></td>

    </tr>

    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="stratumtype" name="genericBean" type="org.vegbank.common.model.Stratumtype">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td><span class="item"><bean:write name="stratumtype" property="stratumindex"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="stratumtype" property="stratumname"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="stratumtype" property="stratumdescription"/>&nbsp;</span></td>

    </tr>
    </logic:iterate>

  </table>

