<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%-- <!--
*   '$RCSfile: ProjectContributorAustereViewData.jsp,v $'
*   Purpose: View a summary of all ProjectContributors in vegbank
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

<td><p><span class="category">Party Name</span></p></td>
<td><p><span class="category">Role Code</span></p></td>

    </tr>

    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

<logic:iterate id="projectcontributor" name="genericBean" type="org.vegbank.common.model.Projectcontributor">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >



<!-- translate PARTY_ID -->

<td><span class="item">

<a href='/vegbank@url2get_party_v_dtl_pk@<bean:write name="projectcontributor" property="party_id"/>'>

      	  		<bean:define id="current__partyid" name="projectcontributor" property="party_id"/>
	  		<bean:include id="current__partyname"
			   page='<%= "@url2get_party_v_pktranslate_pk@" + current__partyid %>' />
	          <bean:write name="current__partyname" filter="false" />
		</a>&nbsp;


</span></td>

<!-- translate ROLE_ID -->

<td><span class="item">

<a href='/vegbank@url2get_aux_role_v_dtl_pk@<bean:write name="projectcontributor" property="role_id"/>'>

      	  		<bean:define id="current__roleid" name="projectcontributor" property="role_id"/>
	  		<bean:include id="current__rolecode"
			   page='<%= "@url2get_aux_role_v_pktranslate_pk@" + current__roleid %>' />
	          <bean:write name="current__rolecode" filter="false" />
		</a>&nbsp;

</span></td>



    </tr>
    </logic:iterate>

  </table>

