<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HEAD>@defaultHeadToken@
 
<TITLE>View Roles</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">


<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY >@vegbank_header_html_normal@

<h2>VegBank Roles</h2>

<vegbank:get id="MY_ROLES" select="aux_role" beanName="map" where="where_aux_role_pk" pager="true"/>

        <p>
        <br>

        <logic:empty name="MY_ROLES-BEANLIST">
                No roles were found.
                
        </logic:empty>

        <logic:notEmpty name="MY_ROLES-BEANLIST">
          <table border="1" class="thinlines" cellpadding="3">
           <tr>

        <th>     role Code          </th>  
	<th>     role Description   </th>
	<th>     accession Code     </th>
	<th>     Project Contributor	   </th>
	<th>     Observation  Contributor </th>
	<th>     Taxon Interpretation	  </th>
	 <th>    Community Interpretation     </th>
	 		     
           </tr>
           <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>
          <logic:iterate id="onerec" name="MY_ROLES-BEANLIST" >     
           
		       <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       rowClass = rowClass.equals("oddrow")? "evenrow" : "oddrow";
    %>
           <tr class="<%= rowClass %>">
           
              <td>     <bean:write name="onerec" property="rolecode"/>  &nbsp;               </td>
           	<td>   <bean:write name="onerec" property="roledescription"/>&nbsp;      </td>
           	<td>   <bean:write name="onerec" property="accessioncode"/> &nbsp;       </td>
           	<td>   <bean:write name="onerec" property="roleproject_transl"/>&nbsp;	    </td>
           	<td>   <bean:write name="onerec" property="roleobservation_transl"/> &nbsp;     </td>
           	<td>   <bean:write name="onerec" property="roletaxonint_transl"/>	&nbsp;    </td>
           	<td>   <bean:write name="onerec" property="roleclassint_transl"/>  &nbsp;       </td>
           
            </tr> 
          </logic:iterate>
          </table>


        </logic:notEmpty>

		<p>
		<br>
		  <vegbank:pager/>

@vegbank_footer_html_tworow@
</BODY>
</html>
