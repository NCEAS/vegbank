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
*  '$Author: mlee $'
*  '$Date: 2003-07-22 01:31:39 $'
*  '$Revision: 1.1 $'
*
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

