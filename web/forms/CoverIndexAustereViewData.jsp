<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%-- <!--
*   '$RCSfile: CoverIndexAustereViewData.jsp,v $'
*   Purpose: View a summary of all CoverIndexs in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2003-07-22 01:31:38 $'
*  '$Revision: 1.1 $'
*
*
--> --%>



  <table border="1" cellspacing="0" cellpadding="0">

    <tr class="grey">
      <td colspan="5"><p><span class="category">Cover Index</span></p></td>
    </tr>
    <tr class="grey">
      <td><p><span class="category">Cover Code</span></p></td>
      <td><p><span class="category">Cover Percent</span></p></td>
      <td><p><span class="category">Lower Limit (%)</span></p></td>
      <td><p><span class="category">Upper Limit (%)</span></p></td>
      <td><p><span class="category">Description</span></p></td>

    </tr>

    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="coverindex" name="genericBean" type="org.vegbank.common.model.Coverindex">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td><span class="item"><bean:write name="coverindex" property="covercode"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="coverindex" property="coverpercent"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="coverindex" property="lowerlimit"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="coverindex" property="upperlimit"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="coverindex" property="indexdescription"/>&nbsp;</span></td>
    </tr>
    </logic:iterate>

  </table>

