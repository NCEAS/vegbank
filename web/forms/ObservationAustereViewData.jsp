<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<!--
*   '$RCSfile: ObservationAustereViewData.jsp,v $'
*   Purpose: View a summary of all references in vegbank
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




  <table border="1" cellspacing="0" cellpadding="0">


    <tr class="grey">
      <td><p><span class="category">Author Observation Code</span></p></td>

    </tr>
    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>
    <logic:iterate id="row" name="genericBean" type="org.vegbank.common.model.Observation">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >
      <td><span class="item"><a href="/vegbank/servlet/DataRequestServlet?requestDataFormatType=html&clientType=browser&requestDataType=vegPlot&resultType=full&queryType=simple&plotId=<bean:write name='row' property='observation_id'/>"><bean:write name="row" property="authorobscode"/>&nbsp;</a></span></td>
    </tr>
    </logic:iterate>

  </table>


