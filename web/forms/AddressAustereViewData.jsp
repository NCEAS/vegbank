<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%-- <!--
*   '$RCSfile: AddressAustereViewData.jsp,v $'
*   Purpose: View a summary of all Addresss in vegbank
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

<td><p><span class="category">Organization ID</span></p></td>
<td><p><span class="category">Organization Position</span></p></td>
<td><p><span class="category">Email</span></p></td>
<td><p><span class="category">Delivery Point</span></p></td>
<td><p><span class="category">City</span></p></td>
<td><p><span class="category">Administrative Area</span></p></td>
<td><p><span class="category">Postal Code</span></p></td>
<td><p><span class="category">Country</span></p></td>
<td><p><span class="category">Current Address?</span></p></td>
<td><p><span class="category">Address Start Date</span></p></td>



    </tr>

    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="address" name="genericBean" type="org.vegbank.common.model.Address">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >

<td><span class="item"><bean:write name="address" property="organization_id"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="orgposition"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="email"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="deliverypoint"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="city"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="administrativearea"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="postalcode"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="country"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="currentflag"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="address" property="addressstartdate"/>&nbsp;</span></td>
    </tr>
    </logic:iterate>

  </table>

