<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%-- <!--
*   '$RCSfile: TelephoneAustereViewData.jsp,v $'
*   Purpose: View a summary of all Telephones in vegbank
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


      <td><p><span class="category">Phone Number</span></p></td>
      <td><p><span class="category">Phone Type</span></p></td>


    </tr>

    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="telephone" name="genericBean" type="org.vegbank.common.model.Telephone">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >

      <td><span class="item"><bean:write name="telephone" property="phonenumber"/>&nbsp;</span></td>
      <td><span class="item"><bean:write name="telephone" property="phonetype"/>&nbsp;</span></td>

    </tr>
    </logic:iterate>

  </table>

