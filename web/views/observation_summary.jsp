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
*  '$Author: anderson $'
*  '$Date: 2004-09-23 03:56:12 $'
*  '$Revision: 1.9 $'
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
<% String where="where_observation_pk"; %>

  <h2>View Plot Observation(s)</h2>
  <vegbank:get select="plotandobservation" beanName="map" pager="true"/>


<logic:empty name="BEANLIST">
                Sorry, no plot-observations are available in the database!
          </logic:empty>
<logic:notEmpty name="BEANLIST"><!-- set up table -->


<logic:iterate id="onerow" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2">
<TR><TD width="55%" valign="top"><!-- plot level info -->


<table class="leftrightborders" cellpadding="1"><!--each field, only write when HAS contents-->
<tr><th colspan="2">Plot Level Data: <bean:write name="onerow" property="authorplotcode"/></th></tr>

<tr><th class="subheader">Plot ID Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<logic:notEmpty name="onerow" property="authorplotcode">
<tr class='@nextcolorclass@'><td width="35%" nowrap="true" class="datalabel">Author Plot Code</td>
<td><strong><bean:write name="onerow" property="authorplotcode"/></strong>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="plotaccessioncode">
<tr class='@nextcolorclass@'><td class="datalabel">Plot Accession Code</td>
<td class="sizetiny"><bean:write name="onerow" property="plotaccessioncode"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>

<logic:notEmpty name="onerow" property="authorobscode">
<tr class='@nextcolorclass@'><td class="datalabel">Author Observation Code</td>
<td><bean:write name="onerow" property="authorobscode"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="project_id">
<tr class='@nextcolorclass@'><td class="datalabel">Project</td>
<td><a href='@web_context@get/project/<bean:write name="onerow" property="project_id"/>'><bean:write name="onerow" property="project_id_transl"/></a>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="observationaccessioncode">
<tr class='@nextcolorclass@'><td class="datalabel">Observation Accession Code</td>
<td class="sizetiny"><bean:write name="onerow" property="observationaccessioncode"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="observationdateentered">
<tr class='@nextcolorclass@'><td class="datalabel">Date Entered into VegBank</td>
<td class="sizetiny"><bean:write name="onerow" property="dateentered"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:equal value="false" name="hadData">
<tr><td colspan="2">--no data--</td></tr>
</logic:equal>
<tr><th class="subheader">Location Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<logic:notEmpty name="onerow" property="confidentialitystatus">
<tr class='@nextcolorclass@'><td class="datalabel">Confidentiality Status</td>
<td><bean:write name="onerow" property="confidentialitystatus"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="latitude">
<tr class='@nextcolorclass@'><td class="datalabel">Latitude</td>
<td><bean:write name="onerow" property="latitude"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="longitude">
<tr class='@nextcolorclass@'><td class="datalabel">Longitude</td>
<td><bean:write name="onerow" property="longitude"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="authorlocation">
<tr class='@nextcolorclass@'><td class="datalabel">Author Location</td>
<td><bean:write name="onerow" property="authorlocation"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="locationnarrative">
<tr class='@nextcolorclass@'><td class="datalabel">Location Narrative</td>
<td class="sizetiny"><bean:write name="onerow" property="locationnarrative"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:equal value="false" name="hadData">
<tr><td colspan="2">--no data--</td></tr>
</logic:equal>
<tr><th class="subheader">Layout Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<logic:notEmpty name="onerow" property="shape">
<tr class='@nextcolorclass@'><td class="datalabel">Shape</td>
<td><bean:write name="onerow" property="shape"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="area">
<tr class='@nextcolorclass@'><td class="datalabel">Area</td>
<td><bean:write name="onerow" property="area"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>

<logic:notEmpty name="onerow" property="observationnarrative">
<tr class='@nextcolorclass@'><td class="datalabel">Observation Narrative</td>
<td class="sizetiny"><bean:write name="onerow" property="observationnarrative"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:equal value="false" name="hadData">
<tr><td colspan="2">--no data--</td></tr>
</logic:equal>
<tr><th class="subheader">Environment Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<logic:notEmpty name="onerow" property="elevation">
<tr class='@nextcolorclass@'><td class="datalabel">Elevation</td>
<td><bean:write name="onerow" property="elevation"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="slopeaspect">
<tr class='@nextcolorclass@'><td class="datalabel">Slope Aspect</td>
<td><bean:write name="onerow" property="slopeaspect"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="slopegradient">
<tr class='@nextcolorclass@'><td class="datalabel">Slope Gradient</td>
<td><bean:write name="onerow" property="slopegradient"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="topoposition">
<tr class='@nextcolorclass@'><td class="datalabel">Topographic Position</td>
<td><bean:write name="onerow" property="topoposition"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="landform">
<tr class='@nextcolorclass@'><td class="datalabel">Landform</td>
<td><bean:write name="onerow" property="landform"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="surficialdeposits">
<tr class='@nextcolorclass@'><td class="datalabel">Surficial Deposits</td>
<td><bean:write name="onerow" property="surficialdeposits"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="rocktype">
<tr class='@nextcolorclass@'><td class="datalabel">Rock Type</td>
<td><bean:write name="onerow" property="rocktype"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="landscapenarrative">
<tr class='@nextcolorclass@'><td class="datalabel">Landscape Narrative</td>
<td class="sizetiny"><bean:write name="onerow" property="landscapenarrative"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="hydrologicregime">
<tr class='@nextcolorclass@'><td class="datalabel">Hydrologic Regime</td>
<td><bean:write name="onerow" property="hydrologicregime"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="soildepth">
<tr class='@nextcolorclass@'><td class="datalabel">Soil Depth</td>
<td><bean:write name="onerow" property="soildepth"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:equal value="false" name="hadData">
<tr><td colspan="2">--no data--</td></tr>
</logic:equal>
<tr><th class="subheader">Methods Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<logic:notEmpty name="onerow" property="obsstartdate">
<tr class='@nextcolorclass@'><td class="datalabel">Observation Start Date</td>
<td><bean:write name="onerow" property="obsstartdate"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="covermethod_id">
<tr class='@nextcolorclass@'><td class="datalabel">Cover Method</td>
<td><a href='@web_context@get/covermethod/<bean:write name="onerow" property="covermethod_id"/>'><bean:write 
name="onerow" property="covermethod_id_transl"/></a>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="stratummethod_id">
<tr class='@nextcolorclass@'><td class="datalabel">Stratum Method</td>
<td><a href='@web_context@get/stratummethod/<bean:write name="onerow" property="stratummethod_id"/>'><bean:write 
  name="onerow" property="stratummethod_id_transl"/></a>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="stemsizelimit">
<tr class='@nextcolorclass@'><td class="datalabel">Stem Size Limit</td>
<td><bean:write name="onerow" property="stemsizelimit"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="methodnarrative">
<tr class='@nextcolorclass@'><td class="datalabel">Method Narrative</td>
<td class="sizetiny"><bean:write name="onerow" property="methodnarrative"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="taxonobservationarea">
<tr class='@nextcolorclass@'><td class="datalabel">Taxon Observation Area</td>
<td><bean:write name="onerow" property="taxonobservationarea"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="coverdispersion">
<tr class='@nextcolorclass@'><td class="datalabel">Cover Dispersion</td>
<td><bean:write name="onerow" property="coverdispersion"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="autotaxoncover">
<tr class='@nextcolorclass@'><td class="datalabel">Taxon Cover Automatically Calculated?</td>
<td><bean:write name="onerow" property="autotaxoncover_transl"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="stemobservationarea">
<tr class='@nextcolorclass@'><td class="datalabel">Stem Observation Area</td>
<td><bean:write name="onerow" property="stemobservationarea"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:equal value="false" name="hadData">
<tr><td colspan="2">--no data--</td></tr>
</logic:equal>
<tr><th class="subheader">Plot quality Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<logic:notEmpty name="onerow" property="effortlevel">
<tr class='@nextcolorclass@'><td class="datalabel">Effort Level</td>
<td><bean:write name="onerow" property="effortlevel"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="plotvalidationlevel">
<tr class='@nextcolorclass@'><td class="datalabel">Plot Validation Level</td>
<td><bean:write name="onerow" property="plotvalidationlevel"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="floristicquality">
<tr class='@nextcolorclass@'><td class="datalabel">Floristic Quality</td>
<td><bean:write name="onerow" property="floristicquality"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="bryophytequality">
<tr class='@nextcolorclass@'><td class="datalabel">Bryophyte Quality</td>
<td><bean:write name="onerow" property="bryophytequality"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:notEmpty name="onerow" property="lichenquality">
<tr class='@nextcolorclass@'><td class="datalabel">Lichen Quality</td>
<td><bean:write name="onerow" property="lichenquality"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:equal value="false" name="hadData">
<tr><td colspan="2">--no data--</td></tr>
</logic:equal>
<tr><th class="subheader">Overall Plot Vegetation Fields:</th><th class="subheader">&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<logic:notEmpty name="onerow" property="basalarea">
<tr class='@nextcolorclass@'><td class="datalabel">Basal Area</td>
<td><bean:write name="onerow" property="basalarea"/>&nbsp;</td>
</tr>
<bean:define id="hadData" value="true" />
</logic:notEmpty>
<logic:equal value="false" name="hadData">
<tr><td colspan="2">--no data--</td></tr>
</logic:equal>



</table>


</TD><TD valign="top"><!-- plants in this plot -->

<bean:define id="obsId" name="onerow" property="observation_id"/>
<!-- Obs id is <%= obsId %> -->

<vegbank:get select="taxonimportance" where="where_observation_pk" beanName="map" wparam="obsId" perPage="-1"/>

     <table cellpadding="1" class="leftrightborders">
     <tr><th colspan="2"><strong>Taxa occurring on <bean:write name="onerow" property="authorplotcode"/></strong></th></tr>
  <logic:empty name="BEANLIST">
                <tr class='@nextcolorclass@'><td colspan="2">(no plants recorded on this plot: error!)</td></tr>
  </logic:empty>
  <logic:notEmpty name="BEANLIST">

     <tr><th width="75%">Author's Plant Name</th><th>overall cover</th></tr>

     <logic:iterate id="oneobsplants" name="BEANLIST">
 
     <tr class='@nextcolorclass@'>
       <td><bean:write name="oneobsplants" property="authorplantname" />&nbsp;</td>
       <td>&nbsp;
       <logic:notEmpty name="oneobsplants" property="cover">
       <bean:write name="oneobsplants" property="cover" /> %
       <bean:define id="hadData" value="true" />
</logic:notEmpty>
       </td>
     </tr>
     </logic:iterate>
   <!--  <logic:notEmpty name="onerow" property="autotaxoncover">
	 <tr class='@nextcolorclass@'><td/><td class="datalabel">Cover Values Calculated?</td>
	 <td><bean:write name="onerow" property="autotaxoncover_transl"/>&nbsp;</td>
	 </tr>
	 <bean:define id="hadData" value="true" />
</logic:notEmpty> -->


  <bean:define id="hadData" value="true" />
</logic:notEmpty>
     </table>
</TD>
</TR>

<TR><TD colspan="2"><hr noshade="true"/><br/></TD></TR>
</TABLE>

</logic:iterate>

<vegbank:pager/>

<bean:define id="hadData" value="true" />
</logic:notEmpty>

<br>
@vegbank_footer_html_tworow@
</BODY>
</html>
