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
*  '$Date: 2004-09-17 00:15:35 $'
*  '$Revision: 1.3 $'
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
  


<head>@defaultHeadToken@
<title>Plot Observation Summary View</title>
<link REL=STYLESHEET HREF="@stylesheet@" TYPE="text/css">
</head>


<BODY>

@vegbank_header_html_normal@

  <h2>View plot-observation(s) in VegBank</h2>
  <vegbank:get select="plotandobservation" beanName="map" where="where_observation_pk" pager="true"/>


<logic:empty name="BEANLIST">
                Sorry, no plot-observations are available in the database!
          </logic:empty>
<logic:notEmpty name="BEANLIST"><!-- set up table -->


<logic:iterate id="onerow" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2">
<TR><TD width="55%" valign="top"><!-- plot level info -->

<table border="1" cellpadding="0" cellspacing="0" class="item"><!--each field, only write when HAS contents-->


<logic:notEmpty name="onerow" property="authorplotcode">
<tr><td width="15%"><!--label:--><p><span class="category">Author Plot Code</span></p></td>
<td width="40%"><p><span class="category"><bean:write name="onerow" property="authorplotcode"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="confidentialitystatus">
<tr><td><!--label:--><p><span class="category">Confidentiality Status</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="confidentialitystatus"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="latitude">
<tr><td><!--label:--><p><span class="category">Latitude</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="latitude"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="longitude">
<tr><td><!--label:--><p><span class="category">Longitude</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="longitude"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="authorlocation">
<tr><td><!--label:--><p><span class="category">Author Location</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="authorlocation"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="locationnarrative">
<tr><td><!--label:--><p><span class="category">Location Narrative</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="locationnarrative"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="shape">
<tr><td><!--label:--><p><span class="category">Shape</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="shape"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="area">
<tr><td><!--label:--><p><span class="category">Area</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="area"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="elevation">
<tr><td><!--label:--><p><span class="category">Elevation</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="elevation"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="slopeaspect">
<tr><td><!--label:--><p><span class="category">Slope Aspect</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="slopeaspect"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="slopegradient">
<tr><td><!--label:--><p><span class="category">Slope Gradient</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="slopegradient"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="topoposition">
<tr><td><!--label:--><p><span class="category">Topographic Position</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="topoposition"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="landform">
<tr><td><!--label:--><p><span class="category">Landform</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="landform"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="surficialdeposits">
<tr><td><!--label:--><p><span class="category">Surficial Deposits</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="surficialdeposits"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="rocktype">
<tr><td><!--label:--><p><span class="category">Rock Type</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="rocktype"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="plotaccessioncode">
<tr><td><!--label:--><p><span class="category">Accession Code</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="accessioncode"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>

<logic:notEmpty name="onerow" property="authorobscode">
<tr><td><!--label:--><p><span class="category">Author Observation Code</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="authorobscode"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="project_id">
<tr><td><!--label:--><p><span class="category">Project</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="project_id_transl"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="obsstartdate">
<tr><td><!--label:--><p><span class="category">Observation Start Date</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="obsstartdate"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="covermethod_id">
<tr><td><!--label:--><p><span class="category">Cover Method</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="covermethod_id_transl"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="stratummethod_id">
<tr><td><!--label:--><p><span class="category">Stratum Method</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="stratummethod_id_transl"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="stemsizelimit">
<tr><td><!--label:--><p><span class="category">Stem Size Limit</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="stemsizelimit"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="methodnarrative">
<tr><td><!--label:--><p><span class="category">Method Narrative</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="methodnarrative"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="taxonobservationarea">
<tr><td><!--label:--><p><span class="category">Taxon Observation Area</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="taxonobservationarea"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="coverdispersion">
<tr><td><!--label:--><p><span class="category">Cover Dispersion</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="coverdispersion"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="autotaxoncover">
<tr><td><!--label:--><p><span class="category">Taxon Cover Automatically Calculated?</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="autotaxoncover_transl"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="stemobservationarea">
<tr><td><!--label:--><p><span class="category">Stem Observation Area</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="stemobservationarea"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="effortlevel">
<tr><td><!--label:--><p><span class="category">Effort Level</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="effortlevel"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="plotvalidationlevel">
<tr><td><!--label:--><p><span class="category">Plot Validation Level</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="plotvalidationlevel"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="floristicquality">
<tr><td><!--label:--><p><span class="category">Floristic Quality</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="floristicquality"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="bryophytequality">
<tr><td><!--label:--><p><span class="category">Bryophyte Quality</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="bryophytequality"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="lichenquality">
<tr><td><!--label:--><p><span class="category">Lichen Quality</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="lichenquality"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="observationnarrative">
<tr><td><!--label:--><p><span class="category">Observation Narrative</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="observationnarrative"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="landscapenarrative">
<tr><td><!--label:--><p><span class="category">Landscape Narrative</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="landscapenarrative"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="basalarea">
<tr><td><!--label:--><p><span class="category">Basal Area</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="basalarea"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="hydrologicregime">
<tr><td><!--label:--><p><span class="category">Hydrologic Regime</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="hydrologicregime"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="soildepth">
<tr><td><!--label:--><p><span class="category">Soil Depth</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="soildepth"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="observationaccessioncode">
<tr><td><!--label:--><p><span class="category">Observation Accession Code</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="observationaccessioncode"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="observationdateentered">
<tr><td><!--label:--><p><span class="category">Date Entered into VegBank</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="dateentered"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>


</table>


</TD><TD width="45%" valign="top"><!-- plants in this plot -->

<bean:define id="obsId" name="onerow" property="observation_id"/>
<!-- Obs id is <%= obsId %> -->

<vegbank:get select="taxonimportance" where="where_observation_pk" beanName="map" wparam="obsId" perPage="-1"/>


  <logic:empty name="BEANLIST">
                <p>(no plants recorded on this plot: error!)</p>
  </logic:empty>
  <logic:notEmpty name="BEANLIST">
     <table border="1" cellpadding="0" cellspacing="0" class="item">
     <tr colspan="2"><p>Taxa occurring on this plot</p></tr>
     <tr><th>Author's Plant Name</th><th>overall cover</th></tr>
     <logic:iterate id="oneobsplants" name="BEANLIST">
     <tr>
       <td><p><span class="item"><bean:write name="oneobsplants" property="authorplantname" />&nbsp;</span></p></td>
       <td width="20%"><p><span class="item">&nbsp;
       <logic:notEmpty name="oneobsplants" property="cover">
       <bean:write name="oneobsplants" property="cover" /> %
       </logic:notEmpty>
       </span></p></td>
     </tr>
     </logic:iterate>
     <logic:notEmpty name="onerow" property="autotaxoncover">
	 <tr><td><!--label:--><p><span class="category">Cover Values Calculated?</span></p></td>
	 <td><p><span class="item"><bean:write name="onerow" property="autotaxoncover_transl"/>&nbsp;</span></p></td>
	 </tr>
	 </logic:notEmpty>

     </table>
  </logic:notEmpty>
</TD>
</TR>
</TABLE>

</logic:iterate>

<vegbank:pager/>

</logic:notEmpty>

<br>
@vegbank_footer_html_tworow@
</BODY>
</html>
