<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	
<!-- 
*  '$RCSfile: observation_detail.jsp,v $'
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2004-10-09 00:54:32 $'
*  '$Revision: 1.1 $'
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
<title>Plot Observation Detail View</title>
<link REL=STYLESHEET HREF="@stylesheet@" TYPE="text/css">
</head>


<body>  

 

@vegbank_header_html_normal@
 @possibly_center@  
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


<table class="leftrightborders" cellpadding="2"><!--each field, only write when HAS contents-->
<tr><th class="major" colspan="2">Plot Level Data: <bean:write name="onerowofplot" property="authorplotcode"/></th></tr>

<tr><th>Plot ID Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotidlong_data.jsp" %>         
<%@ include file="autogen/observation_plotidlong_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Location Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotloclong_data.jsp" %>
<%@ include file="autogen/observation_plotloclong_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Layout Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->


<%@ include file="autogen/plot_plotlayoutlong_data.jsp" %>
<%@ include file="autogen/observation_plotlayoutlong_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Environment Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotenvlong_data.jsp" %>
<%@ include file="autogen/observation_plotenvlong_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Methods Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotmethodlong_data.jsp" %>
<%@ include file="autogen/observation_plotmethodlong_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Plot quality Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotqualitylong_data.jsp" %>
<%@ include file="autogen/observation_plotqualitylong_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Overall Plot Vegetation Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotoverallveglong_data.jsp" %>
<%@ include file="autogen/observation_plotoverallveglong_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>

<tr><th>Misc Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<%@ include file="autogen/plot_plotmisclong_data.jsp" %>
<%@ include file="autogen/observation_plotmisclong_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>

<!-- end of plot/obs fields -->
</table>


<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>


</TD><TD valign="top"><!-- plants in this plot -->

<%@ include file="includeviews/sub_taxonobservation.jsp" %>

</TD>


</TR><TR><TD colspan="2" valign="top" align="left">



<!-- community info -->

<vegbank:get id="commclass" select="commclass" beanName="map" 
    where="where_observation_pk" wparam="observation_pk" pager="false"/>
<%@ include file="includeviews/sub_commclass_summary.jsp" %>


<br/><br/>
<vegbank:get id="soilobs" select="soilobs" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">

<tr><th colspan="19">Soil Observations:</th></tr>
<logic:empty name="soilobs-BEANLIST">
<tr><td class="@nextcolorclass@">  Sorry, no Soil Observations found.</td></tr>
</logic:empty>
<logic:notEmpty name="soilobs-BEANLIST">
<tr>
<%@ include file="autogen/soilobs_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofsoilobs" name="soilobs-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/soilobs_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
<br/><br/>
<vegbank:get id="disturbanceobs" select="disturbanceobs" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="9">Disturbance Data:</th></tr>
<logic:empty name="disturbanceobs-BEANLIST">
<tr><td class="@nextcolorclass@">  Sorry, no Disturbance Data found.</td></tr>
</logic:empty>
<logic:notEmpty name="disturbanceobs-BEANLIST">
<tr>
<%@ include file="autogen/disturbanceobs_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofdisturbanceobs" name="disturbanceobs-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/disturbanceobs_summary_data.jsp" %>
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
