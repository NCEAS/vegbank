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
*  '$Author: farrell $'
*  '$Date: 2003-08-21 21:16:43 $'
*  '$Revision: 1.2 $'
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

