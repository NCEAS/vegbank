<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	
<!-- 
*  '$RCSfile: observation_summary.jsp,v $'
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2004-10-04 18:07:54 $'
*  '$Revision: 1.16 $'
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
-->
  
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>

<head>@defaultHeadToken@
<title>Plot Observation Summary View</title>
<link REL=STYLESHEET HREF="@stylesheet@" TYPE="text/css">
</head>


<BODY>

@vegbank_header_html_normal@

  <h2>View Plot Observation(s)</h2>
  <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>

<vegbank:pager />

<logic:empty name="plotobs-BEANLIST">
                Sorry, no plot-observations are available.
          </logic:empty>
<logic:notEmpty name="plotobs-BEANLIST"><!-- set up table -->


<logic:iterate id="onerowofobservation" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofplot" name="onerowofobservation" />
<!-- start of plot & obs fields-->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2">
<TR><TD width="55%" valign="top"><!-- plot level info -->


<table class="leftrightborders" cellpadding="1"><!--each field, only write when HAS contents-->
<tr><th colspan="2">Plot Level Data: <bean:write name="onerowofplot" property="authorplotcode"/></th></tr>

<tr><th class="subheader">Plot ID Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotidshort_data.jsp" %>         
<%@ include file="autogen/observation_plotidshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th class="subheader">Location Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotlocshort_data.jsp" %>
<%@ include file="autogen/observation_plotlocshort_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th class="subheader">Layout Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->


<%@ include file="autogen/plot_plotlayoutshort_data.jsp" %>
<%@ include file="autogen/observation_plotlayoutshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th class="subheader">Environment Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotenvshort_data.jsp" %>
<%@ include file="autogen/observation_plotenvshort_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th class="subheader">Methods Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotmethodshort_data.jsp" %>
<%@ include file="autogen/observation_plotmethodshort_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th class="subheader">Plot quality Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotqualityshort_data.jsp" %>
<%@ include file="autogen/observation_plotqualityshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th class="subheader">Overall Plot Vegetation Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotoverallvegshort_data.jsp" %>
<%@ include file="autogen/observation_plotoverallvegshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>

<tr><th class="subheader">Misc Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<%@ include file="autogen/plot_plotmiscshort_data.jsp" %>
<%@ include file="autogen/observation_plotmiscshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>

<!-- end of plot/obs fields -->



<bean:define id="obsId" name="onerowofplot" property="observation_id"/>
<tr><th class="subheader">Community Classification:</th><th class="subheader">&nbsp;</th></tr>

<!-- community info -->
<vegbank:get id="comminterpretation" select="comminterpretation_withobs" beanName="map" 
  where="where_observation_pk" wparam="obsId" perPage="-1" pager="false"/>
<logic:empty name="comminterpretation-BEANLIST">
<tr class='@nextcolorclass@'><td>  No Community Interpretations.</td></tr>
</logic:empty>
<logic:notEmpty name="comminterpretation-BEANLIST">
<tr><td colspan="2">
<table class="leftrightborders" cellpadding="2" width="100%"><!--each field, only write when field HAS contents-->

<tr>
<%@ include file="autogen/comminterpretation_summary2_head.jsp" %>
</tr>
<logic:iterate id="onerowofcomminterpretation" name="comminterpretation-BEANLIST"><!-- iterate over all records in set : new table for each -->
<logic:notEmpty name="onerowofcomminterpretation" property="commconcept_id">
<tr class='@nextcolorclass@'>
<%@ include file="autogen/comminterpretation_summary2_data.jsp" %>
</tr>
</logic:notEmpty>
</logic:iterate>
</table>

</td></tr>
</logic:notEmpty>



</table>


</TD><TD valign="top"><!-- plants in this plot -->


<!-- Obs id is <%= obsId %> -->

<vegbank:get id="taxonimportance" select="taxonimportance_nostrata" where="where_observation_pk" beanName="map" 
 wparam="obsId" perPage="-1"/>

     <table cellpadding="1" class="leftrightborders">
     <tr><th colspan="2"><strong>Taxa occurring on <bean:write name="onerowofplot" property="authorplotcode"/></strong></th></tr>
  <logic:empty name="taxonimportance-BEANLIST">
                <tr class='@nextcolorclass@'><td colspan="2">(no plants recorded on this plot: error!)</td></tr>
  </logic:empty>
  <logic:notEmpty name="taxonimportance-BEANLIST">

     <tr>
     <%@ include file="autogen/taxonimportance_summarynostrata_head.jsp" %>
     </tr>

     <logic:iterate id="onerowoftaxonimportance" name="taxonimportance-BEANLIST">
 
     <tr class='@nextcolorclass@'>
      <%@ include file="autogen/taxonimportance_summarynostrata_data.jsp" %>
     </tr>
     </logic:iterate>


  
</logic:notEmpty>
     </table>
</TD>
</TR>

<TR><TD colspan="2"><hr noshade="true"/><br/></TD></TR>
</TABLE>

</logic:iterate>

<vegbank:pager/>

</logic:notEmpty>

<br>
@vegbank_footer_html_tworow@
</BODY>
</html>
