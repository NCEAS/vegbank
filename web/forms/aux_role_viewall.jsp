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

<h3>View Roles in VegBank </h3>

<vegbank:get id="MY_ROLES" select="aux_role" beanName="map" where="where_aux_role_pk"/>

        <p>
        <br>

        <logic:empty name="MY_ROLES-BEANLIST">
                Sorry, no roles are available in the databse!
                
        </logic:empty>

        <logic:notEmpty name="MY_ROLES-BEANLIST">
          <table border="1">
           <tr>

        <th>     role Code          </th>  
	<th>     role Description   </th>
	<th>     accession Code     </th>
	<th>     role-Project	   </th>
	<th>     role-Observation  </th>
	<th>     role-TaxonInt	  </th>
	 <th>    role-ClassInt     </th>
	 		     
           </tr>
           <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String bgColor = "#FFFFF";
    %>
          <logic:iterate id="onerec" name="MY_ROLES-BEANLIST" >     
           
		       <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>
           <tr bgcolor="<%= bgColor %>" class="sansserif">
           
              <td>     <bean:write name="onerec" property="rolecode"/>  &nbsp;               </td>
           	<td>   <bean:write name="onerec" property="roledescription"/>&nbsp;      </td>
           	<td>   <bean:write name="onerec" property="accessioncode"/> &nbsp;       </td>
           	<td>   <bean:write name="onerec" property="roleproject"/>&nbsp;	    </td>
           	<td>   <bean:write name="onerec" property="roleobservation"/> &nbsp;     </td>
           	<td>   <bean:write name="onerec" property="roletaxonint"/>	&nbsp;    </td>
           	<td>   <bean:write name="onerec" property="roleclassint"/>  &nbsp;       </td>
           
            </tr> 
          </logic:iterate>
          </table>


        </logic:notEmpty>

		<p>
		<br>
		  <vegbank:pager/>

		<br> &nbsp;
@vegbank_footer_html_tworow@
</BODY>
</html>
