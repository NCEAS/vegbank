<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%-- <!--
*   '$RCSfile: ReferenceContributorAustereViewData.jsp,v $'
*   Purpose: View a summary of all ReferenceContributors in vegbank
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
<td><p><span class="category">Role Type</span></p></td>
<td><p><span class="category">Position (order)</span></p></td>



    </tr>

    <!-- data -->

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

<logic:iterate id="referencecontributor" name="genericBean" type="org.vegbank.common.model.Referencecontributor">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr valign="top" bgcolor="<%= bgColor %>" >
<!-- translate ReferenceParty_ID -->

<td><span class="item">

<a href='/vegbank@url2get_referenceparty_v_dtl_pk@<bean:write name="referencecontributor" property="referenceparty_id"/>'>

      	  		<bean:define id="current__refpartyid" name="referencecontributor" property="referenceparty_id"/>
	  		<bean:include id="current__refpartyname"
			   page='<%= "@url2get_referenceparty_v_pktranslate_pk@" + current__refpartyid %>' />
	          <bean:write name="current__refpartyname" filter="false" />
		</a>&nbsp;

</span></td>


<td><span class="item"><bean:write name="referencecontributor" property="roletype"/>&nbsp;</span></td>
<td><span class="item"><bean:write name="referencecontributor" property="position"/>&nbsp;</span></td>

    </tr>
    </logic:iterate>

  </table>

