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
<p>You can view roles for: <!-- this gets the current URL and appends proper where and wparam parameters to get requested info -->
<a href="<%= request.getRequestURI() %>">all tables</a> | 
<a href="<%= request.getRequestURI() %>?where=where_aux_role_project&wparam=1">Project Contributor</a> | 
<a href="<%= request.getRequestURI() %>?where=where_aux_role_observation&wparam=1">Observation Contributor</a> | 
<a href="<%= request.getRequestURI() %>?where=where_aux_role_taxonint&wparam=1">Taxon Interpretaters</a> | 
<a href="<%= request.getRequestURI() %>?where=where_aux_role_classint&wparam=1">Community Interpreters</a> </p>
<vegbank:get id="MY_ROLES" select="aux_role" beanName="map" pager="true"/>

        <p>
        <br>

        <logic:empty name="MY_ROLES-BEANLIST">
                No roles were found.
                
        </logic:empty>

        <logic:notEmpty name="MY_ROLES-BEANLIST">
          <table class="leftrightborders" cellpadding="3">
           <tr>
<%@ include file="autogen/aux_role_summary_head.jsp" %>
	<logic:equal value="where_aux_role_project" parameter="where">
	<th>     Project Contributor	   </th>
	</logic:equal>
	<logic:equal value="where_aux_role_observation" parameter="where">
	<th>     Observation  Contributor </th>
	</logic:equal>
	<logic:equal value="where_aux_role_taxonint" parameter="where">
	<th>     Taxon Interpretation	  </th>
	</logic:equal> 
	 <logic:equal value="where_aux_role_classint" parameter="where">
	 <th>    Community Interpretation     </th>
	 </logic:equal>		     
           </tr>
           <%      String rowClass = "evenrow";   %>
          <logic:iterate id="onerowofaux_role" name="MY_ROLES-BEANLIST" >     
           
           <tr class='@nextcolorclass@'>
           
          <%@ include file="autogen/aux_role_summary_data.jsp" %>
           	<logic:equal value="where_aux_role_project" parameter="where">
           	<td>   <bean:write name="onerowofaux_role" property="roleproject_transl"/>&nbsp;	    </td>
           	</logic:equal>
           	<logic:equal value="where_aux_role_observation" parameter="where">
           	<td>   <bean:write name="onerowofaux_role" property="roleobservation_transl"/> &nbsp;     </td>
           	</logic:equal>
           	<logic:equal value="where_aux_role_taxonint" parameter="where">
           	<td>   <bean:write name="onerowofaux_role" property="roletaxonint_transl"/>	&nbsp;    </td>
           	</logic:equal>
           	<logic:equal value="where_aux_role_classint" parameter="where">
           	<td>   <bean:write name="onerowofaux_role" property="roleclassint_transl"/>  &nbsp;       </td>
            </logic:equal>
            </tr> 
          </logic:iterate>
          </table>


        </logic:notEmpty>

		<p>
		<br>
		  <vegbank:pager id="MY_ROLES"/>

@vegbank_footer_html_tworow@
</BODY>
</html>
