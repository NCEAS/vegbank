<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<!-- 
*   '$RCSfile: plot-query.jsp,v $'
*     Purpose: web page querying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: mlee $'
*      '$Date: 2003-07-10 21:54:20 $'
*  '$Revision: 1.4 $'
*
*
-->
<head>
<meta name="generator" content="HTML Tidy, see www.w3.org"/>
<link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
<title>
VEGBANK - Simple Plots Query
</title>
</head>

<body>
@vegbank_header_html_normal@

  

  <html:form action="/PlotQuery" method="get">

  <!-- SECOND TABLE -->
  <table align="left" border="0" width="90%" cellspacing="0" cellpadding="0">

    <tr>
      <td colspan="2" bgcolor="white">
	<img align="center" border="0" height="144" src="/vegbank/images/owlogoBev.jpg" alt="Veg plots logo "> 
      </td>
      <td align="left" valign="middle">
	<table border="0" cellpadding="5">
	  <tr>
	    <td align="left" valign="bottom">
	      <font face="Helvetica,Arial,Verdana" size="6" color="#23238E">Plot Query Form</font>
	      <br/>
	    </td>
	  </tr>
	</table>
      </td>
    </tr>

    <!-- Instructions Row -->
    <tr>
      <!-- LEFT MARGIN -->
      <td width="10%"  bgcolor="white" align="left" valign="top"></td>
      <td width="5%"  bgcolor="white" align="left" valign="top"></td>
      
      <td align="left">

	<input name="requestDataFormatType" type="hidden" value="html"/>
	<input name="clientType" type="hidden" value="browser"/>
	<input name="requestDataType" type="hidden" value="vegPlot"/>
	<input name="resultType" type="hidden" value="identity"/> 

	<table border="0" align="center">
	  <!--variables that are used by the servlet to figure out which query(s) to issue -->
	  <tbody>
	  <tr valign="top">
	    <td align="left" colspan="2" valign="center">
	      <font color="#23238E" face="Helvetica,Arial,Verdana" size="2">
	      <b>This form can be used to specify criteria for simple
	      queries for VegBank plot-observations. Here, the term "plot" is
	      used synonymously with "observation."  <br />

	      Each section allows querying of different types of attributes.  Leave
	      fields blank to ignore these fields in the query.  Make sure you select whether the query
	      should match ALL or ANY criteria you specify at the <a href="#typeOfQuery">end of this form</a>. <br />
	      For more instructions for this form, <a href="#instructions">click here</a>.
	      </b>

	      </font> 
	    </td>
	  </tr>
	  </tbody>
	</table>
      </td>
    </tr>

    <!-- ERROR DISPLAY -->
    <tr>
      <td colspan="3">
	<html:errors/>
      </td>
    </tr>
    
    <!-- Header Location -->
    <tr bgcolor="#FFFFCC">
      <td width="10%"></td>
      <td colspan="2">
	<p><font face="Helvetica,Arial,Verdana" size="3"><b>Find Plots based on Location</b></font></p>
      </td>
    </tr>


    <!-- State -->
    <tr>
      <td colspan="2"></td>
      <td>
	<!-- STATE -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <font face="Helvetica,Arial,Verdana" size="3" color="#23238E"><b>State:</b></font> 
	    </td>
	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation" width="15" height="15"> 
	    </td>
	    <td class="item">
	      <p>Please select the state or province in which the plot was sampled. <br />
	      Note that you may select more than more state or province at a time.  To select multiple choices, hold down the ctrl key and then select each state or province you want to query.</p>
	    </td>
	  </tr>
	</table>


	<table>
	  <tr>
	    <td width="12%">&nbsp;</td>
	    <td>
	      <html:select property="state" size="6" multiple="true">
		<option value="ANY">--ANY--</option>
<option value="AG">Aguascalientes</option>
<option value="AL">Alabama</option>
<option value="AK">Alaska</option>
<option value="AB">Alberta</option>
<option value="AS">American Samoa</option>
<option value="AZ">Arizona</option>
<option value="AR">Arkansas</option>
<option value="AE">Armed Forces Africa (AE)</option>
<option value="AA">Armed Forces Americas (AA)</option>
<option value="AE">Armed Forces Canada (AE)</option>
<option value="AE">Armed Forces Europe (AE)</option>
<option value="AE">Armed Forces Middle East (AE)</option>
<option value="AP">Armed Forces Pacific (AP)</option>
<option value="BN">Baja California Norte</option>
<option value="BS">Baja California Sur</option>
<option value="BC">British Columbia</option>
<option value="CA">California</option>
<option value="CP">Campeche</option>
<option value="CS">Chiapas</option>
<option value="CI">Chihuahua</option>
<option value="CH">Coahuila</option>
<option value="CL">Colima</option>
<option value="CO">Colorado</option>
<option value="CT">Connecticut</option>
<option value="DE">Delaware</option>
<option value="DC">District Of Columbia</option>
<option value="DF">Districto Federal</option>
<option value="DG">Durango</option>
<option value="FL">Florida</option>
<option value="GA">Georgia</option>
<option value="GU">Guam</option>
<option value="GJ">Guanajuato</option>
<option value="GE">Guerrero</option>
<option value="HI">Hawaii</option>
<option value="HD">Hidalgo</option>
<option value="ID">Idaho</option>
<option value="IL">Illinois</option>
<option value="IN">Indiana</option>
<option value="IA">Iowa</option>
<option value="JA">Jalisco</option>
<option value="KS">Kansas</option>
<option value="KY">Kentucky</option>
<option value="LA">Louisiana</option>
<option value="ME">Maine</option>
<option value="MB">Manitoba</option>
<option value="MH">Marshall Islands</option>
<option value="MD">Maryland</option>
<option value="MA">Massachusetts</option>
<option value="MI">Michigan</option>
<option value="MN">Minnesota</option>
<option value="MS">Mississippi</option>
<option value="MO">Missouri</option>
<option value="MT">Montana</option>
<option value="NE">Nebraska</option>
<option value="NV">Nevada</option>
<option value="NB">New Brunswick</option>
<option value="NH">New Hampshire</option>
<option value="NJ">New Jersey</option>
<option value="NM">New Mexico</option>
<option value="NY">New York</option>
<option value="NF">Newfoundland</option>
<option value="NC">North Carolina</option>
<option value="ND">North Dakota</option>
<option value="MP">Northern Mariana Is</option>
<option value="NT">Northwest Territories</option>
<option value="NS">Nova Scotia</option>
<option value="OH">Ohio</option>
<option value="OK">Oklahoma</option>
<option value="ON">Ontario</option>
<option value="OR">Oregon</option>
<option value="PW">Palau</option>
<option value="PA">Pennsylvania</option>
<option value="PE">Prince Edward Island</option>
<option value="PQ">Province du Quebec</option>
<option value="PU">Puebla</option>
<option value="PR">Puerto Rico</option>
<option value="QE">Quer&#233;taro</option>
<option value="QI">Quintana Roo</option>
<option value="RI">Rhode Island</option>
<option value="SL">San Luis Potos&#237;</option>
<option value="SK">Saskatchewan</option>
<option value="SI">Sinaloa</option>
<option value="SO">Sonora</option>
<option value="SC">South Carolina</option>
<option value="SD">South Dakota</option>
<option value="TB">Tabasco</option>
<option value="TA">Tamaulipas</option>
<option value="TN">Tennessee</option>
<option value="TX">Texas</option>
<option value="TL">Tlaxcala</option>
<option value="UT">Utah</option>
<option value="VC">Veracruz</option>
<option value="VT">Vermont</option>
<option value="VI">Virgin Islands</option>
<option value="VA">Virginia</option>
<option value="WA">Washington</option>
<option value="WV">West Virginia</option>
<option value="WI">Wisconsin</option>
<option value="WY">Wyoming</option>
<option value="YU">Yucat&#225;n</option>
<option value="YT">Yukon Territory</option>
<option value="ZA">Zacatecas</option>

		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select>
	    </td>
	  </tr>
	</table>
	<hr/>

	<!-- LAT / LONG BOUNDING BOX -->
	<!-- TODO:
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <font face="Helvetica,Arial,Verdana" size="3" color="
	      #23238E"><b>Latitude / Longitude:</b></font> 
	    </td>
	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation" width="15" height="15"> 
	    </td>
	    <td class="item">
	      <p>Please specify a bounding box for Latitude and Longitude.  
	      Enter values in decimal degrees and use negative degrees to indicate Western and Southern Hemispheres.  Plots found inside this box will be returned by the query. <br />
	      While you may enter only a Northern or Southern Latitude, you must enter both Western and Eastern Longitudes for Longitude to be included in the query (otherwise, all longitudes would be included on the globe).<br />

	      Example: (Northern-Latitude: 39.99106, Western-Longitude: -102.05282, Eastern-Longitude: -94.62205, Southern-Latitude: 37.01697) yields all plots in Kansas (roughly).</p>
	    </td>
	  </tr>
	</table>
	<table border="0" width="80%">
	  <tr>
	    <td colspan="2" width="100%" align="center">
	      <span class="itemsmall">Northern-Latitude:<input name="max__plot.latitude" size="20" /></span>
	    </td>
	  </tr>
	  <tr>
	    <td align="left"><span class="itemsmall">Western-Longitude:<input name="min__plot.longitude" size="20" /></span></td>
	    <td align="right"><span class="itemsmall">Eastern-Longitude:<input name="max__plot.longitude" size="20" /></span></td>
	  </tr>
	  <tr>
	    <td colspan="2" width="100%" align="center">
	      <span class="itemsmall">Southern-Latitude:<input name="min__plot.latitude" size="20" /></span>
	    </td>
	  </tr>
	</table>
	-->
	
      </td>
    </tr>

    <!-- Find Based on Plot Attributes -->
    <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
      <p><font face="Helvetica,Arial,Verdana" size="3"><b>Find Plots based on Plot Attributes</b></font></p></td>
      <tr><td colspan="2"></td><td>
	<!-- ELEVATION, slope, aspect -->
	<table border="0" width="100%" bgcolor="#DFE5FA">

	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <font face="Helvetica,Arial,Verdana" size="3" color="
	      #23238E"><b>Ranges for Fields:</b></font> 
	    </td>
	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>

	    <td class="item"><p>
	      Please enter the upper and/or lower limits for each field of interest.  Note that you may enter just an upper value or a lower value.  You also do not need to enter values for all fields.</p>
	    </td>
	  </tr>
	</table>
	<table border="0">
	  <tr bgcolor="#DDDDDD">
	    <th><p><span class="category">Field</span></p></th>

	    <th><p><span class="category">Minimum</span></p></th>
	    <th><p><span class="category">Maximum</span></p></th>
	    <th><p><span class="category">Units</span></p></th>
	    <th><p><span class="category">Include Nulls?</span></p></th>
	  </tr>
	  <tr><!-- ELEVATION --> 
	    <td><span class="itemsmall"><b>Elevation</b></span></td>
	    <td>
	      <html:text property="minElevation" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxElevation" size="20"/>
	    </td>
	    <td><span class="itemsmall">meters</span></td>
	    <td>
	      <html:checkbox property="allowNullElevation" value="false"/>
	    </td>
	  </tr>
	  <tr><!-- Slope Aspect -->
	    <td><span class="itemsmall"><b>Slope Aspect</b></span></td>
	    <td>
	      <html:text property="minSlopeAspect" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxSlopeAspect" size="20"/>
	    </td>
	    <td><span class="itemsmall">degrees</span></td>
	    <td>
	      <input name="allowNullSlopeAspect" type="checkbox">
	    </td>
	  </tr>
	  <tr><!-- Slope Gradient -->
	    <td><span class="itemsmall"><b>Slope Gradient</b></span></td>
	    <td>
	      <html:text property="minSlopeGradient" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxSlopeGradient" size="20"/>
	    </td>
	    <td><span class="itemsmall">degrees</span></td>
	    <td>
	      <input name="allowNullSlopeGradient" type="checkbox">
	    </td>
	  </tr>

	  <!-- Soil Depth --><!-- <tr>
	    <td><span class="itemsmall"><b>Soil Depth</b></span></td>
	    <td><input name="min__observation.soildepth" size="20"></td>
	    <td><input name="max__observation.soildepth" size="20"></td>
	    <td><span class="itemsmall">meters</span></td>
	    <td><input name="nulls__observation.soildepth" type="checkbox"></td>
	  </tr> -->
	  
	</table>
	<hr size=".5">
	<!-- closed list fields -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <font face="Helvetica,Arial,Verdana" size="3" color="
	      #23238E"><b>Picklist fields:</b></font> 
	    </td>

	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>
	    <td class="item"><p>
	      Please select values for VegBank fields that are constrained to limited vocabulary.  You do not need to select values for all fields.  Select "--ANY--" to ignore the field in the query.</p>
	    </td>
	  </tr>

	</table>

	<table>
	  <tr>
	    <!-- picklist values to select -->               
	    <td align="left" valign="top">
	      <span class="itemsmall"><b>Rock Type</b></span><br/>
	      <html:select property="rockType" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>
		<html:options property="rockTypes"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select>
	    </td>
	    
	    <td align="left" valign="top">
	      <span class="itemsmall"><b>Surficial Deposits</b></span><br/>
	      <html:select property="surficialDeposit" size="6" multiple="true">
	        <option value="ANY" selected>--ANY--</option>
		 <html:options property="surficialDeposits"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>

	    <td align="left" valign="top">
	      <span class="itemsmall"><b>Hydrologic Regime</b></span><br />
	      <html:select property="hydrologicRegime" size="6" multiple="true">
	        <option value="ANY" selected>--ANY--</option>
		<html:options property="hydrologicRegimes"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>

	    
	  </tr>
	</table>
	<!--  <hr size=".5"> -->
	<table>

	  <tr>
	    <!-- picklist values to select -->               
	    <td align="left" valign="top"><span class="itemsmall"><b>Topo Position</b></span><br />
	      <html:select property="topoPosition" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    
		<html:options property="topoPositions"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>
	    <!-- This is an open list i.e. not supported yet -->
	    <td align="left" valign="top"><span class="itemsmall"><b>Landform</b></span><br />
	      <html:select property="landForm" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    

		<option>alluvial fan</option><option>alluvial flat</option><option>alluvial terrace</option><option>backshore terrace</option><option>backwater</option><option>badlands</option><option>bajada</option><option>bald</option><option>bank</option><option>bar</option><option>barrier beach</option><option>barrier flat</option><option>barrier island(s)</option><option>barrier reef</option><option>basin</option><option>basin floor</option><option>bay</option><option>bayou</option><option>beach</option><option>beach ridge</option><option>bench</option><option>blowout</option><option>bottomlands</option><option>butte</option><option>caldera</option><option>canyon</option><option>carolina bay</option><option>channel</option><option>chenier</option><option>chenier plain</option><option>cirque</option><option>cirque floor</option><option>cirque headwall</option><option>cliff</option><option>coast</option><option>coastal plain</option><option>col</option><option>collapse sinkhole</option><option>colluvial shoulder</option><option>colluvial slope</option><option>cove</option><option>cuesta</option><option>debris slide</option><option>delta</option><option>delta plain</option><option>depositional levee</option><option>depositional stream terrace</option><option>depression</option><option>desert pavement</option><option>dike</option><option>doline</option><option>dome</option><option>drainage</option><option>drainage channel (undifferentiated)</option><option>draw</option><option>drumlin</option><option>dune (undifferentiated)</option><option>dune field</option><option>earth flow</option><option>earth hummock</option><option>eroded bench</option><option>eroding stream channel system</option><option>erosional stream terrace</option><option>escarpment</option><option>esker</option><option>estuary</option><option>exogenous dome</option><option>fan piedmont</option><option>fault scarp</option><option>fault terrace</option><option>fissure</option><option>fissure vent</option><option>flood plain</option><option>fluvial</option><option>foothills</option><option>foredune</option><option>frost creep slope</option><option>frost mound</option><option>frost scar</option><option>gap</option><option>glaciated uplands</option><option>glacier</option><option>gorge</option><option>graben</option><option>ground moraine</option><option>gulch</option><option>hanging valley</option><option>headland</option><option>highland</option><option>hills</option><option>hillslope bedrock outcrop</option><option>hogback</option><option>hoodoo</option><option>hummock</option><option>inlet</option><option>inselberg</option><option>interdune flat</option><option>interfluve</option><option>island</option><option>kame</option><option>kame moraine</option><option>kame terrace</option><option>karst</option><option>karst tower</option><option>karst window</option><option>kegel karst</option><option>kettle</option><option>kettled outwash plain</option><option>knob</option><option>knoll</option><option>lagoon</option><option>lake</option><option>lake bed</option><option>lake plain</option><option>lake terrace</option><option>lateral moraine</option><option>lateral scarp (undifferentiated)</option><option>lava flow (undifferentiated)</option><option>ledge</option><option>levee</option><option>loess deposit (undifferentiated)</option><option>longshore bar</option><option>lowland</option><option>marine terrace (undifferentiated)</option><option>meander belt</option><option>meander scar</option><option>mesa</option><option>mid slope</option><option>mima mound</option><option>monadnock</option><option>moraine (undifferentiated)</option><option>mound</option><option>mountain valley</option><option>mountain(s)</option><option>mountain-valley fan</option><option>mud flat</option><option>noseslope</option><option>outwash fan</option><option>outwash plain</option><option>outwash terrace</option><option>oxbow</option><option>patterned ground (undifferentiated)</option><option>peat dome</option><option>periglacial boulderfield</option><option>piedmont</option><option>pimple mounds</option><option>pingo</option><option>pinnacle</option><option>plain</option><option>plateau</option><option>playa</option><option>polygon (high-centered)</option><option>polygon (low-centered)</option><option>pothole</option><option>raised beach</option><option>raised estuary</option><option>raised mudflat</option><option>raised tidal flat</option><option>ravine</option><option>relict coastline</option><option>ridge</option><option>ridge and valley</option><option>ridgetop bedrock outcrop</option><option>rift valley</option><option>rim</option><option>riverbed</option><option>rock fall avalanche</option><option>saddle</option><option>sag pond</option><option>sandhills</option><option>scarp</option><option>scarp slope</option><option>scour</option><option>scoured basin</option><option>sea cliff</option><option>seep</option><option>shoal</option><option>shoreline</option><option>sinkhole (undifferentiated)</option><option>slide</option><option>slope</option><option>slough</option><option>slump and topple prone slope</option><option>slump pond</option><option>soil creep slope</option><option>solution sinkhole</option><option>spit</option><option>splay</option><option>stone circle</option><option>stone stripe</option><option>stream terrace (undifferentiated)</option><option>streambed</option><option>subjacent karst collapse sinkhole</option><option>subsidence sinkhole</option><option>swale</option><option>talus</option><option>tarn</option><option>tidal flat</option><option>tidal gut</option><option>till plain</option><option>toe slope</option><option>toe zone (undifferentiated)</option><option>transverse dune</option><option>trench</option><option>trough</option><option>valley</option><option>valley floor</option><option>wave-built terrace</option><option>wave-cut platform</option>

		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>
	
	    
	  </tr>
	</table>
      </td>
    </tr>


    <!-- FIND BASED ON SAMPLING METHODS -->
    <tr bgcolor="#FFFFCC" >
      <td width="10%"></td>
      <td colspan="2">
	<p><font face="Helvetica,Arial,Verdana" size="3"><b>Find Plots based on Sampling Methods</b></font></p>
      </td>
      <tr>
	<td colspan="2"></td>
	<td>
	
	  <!-- sampling methodology -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <font face="Helvetica,Arial,Verdana" size="3" color="
	      #23238E"><b>Plot Date / Size:</b></font> 
	    </td>
	  </tr>

	  <tr>
	    <td align="center" width="5%">
	      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>
	    <td class="item"><p>
	      Enter date ranges or plot size ranges that apply to plots of interest.</p>
	    </td>
	  </tr>
	</table>

	<table border="0">
	  <tr bgcolor="#DDDDDD">
	    <th><p><span class="category">Field</span></p></th>
	    <th><p><span class="category">Minimum</span></p></th>
	    <th><p><span class="category">Maximum</span></p></th>
	    <th><p><span class="category">Units</span></p></th>
	    <th><p><span class="category">Include Nulls?</span></p></th>
	  </tr>
	  <tr>
	    <!-- date --> 
	    <td><span class="itemsmall"><b>Date Sampled</b></span></td>

	    <td>
	      <html:text property="minObsStartDate" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxObsEndDate" size="20"/>
	    </td>
	    <td><span class="itemsmall">date</span></td>
	    <td>
	      <html:checkbox property="allowNullObsDate"/>
	    </td>
	  </tr>
	  <tr><!-- date2 -->
	    <td><span class="itemsmall"><b>Date Entered into VegBank</b></span></td>
	    <td>
	      <html:text property="minDateEntered" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxDateEntered" size="20"/>
	    </td>

	    <td><span class="itemsmall">date</span></td>
	    <td><span class="itemsmall">(required)</span>
	  </tr>
	  <tr><!-- plot size -->
	    <td><span class="itemsmall"><b>Plot Size</b></span></td>
	    <td>
	      <html:text property="minPlotArea" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxPlotArea" size="20"/>
	    </td>
	    <td><span class="itemsmall">square meters</span></td>

	    <td>
	      <html:checkbox property="allowNullPlotArea"/>
	    </td>
	  </tr>
	  
	</table>
	<hr size=".5"> 
	
	<!-- sampling methodology : MORE -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <font face="Helvetica,Arial,Verdana" size="3" color="#23238E"><b>Methods<!-- and People-->:</b></font> 
	    </td>

	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>
	    <td class="item"><p>
	      Enter method(s) <!--or people--> that apply to plots of interest.
	      <!--Please enter a person's name in the format: "SurName, GivenName(s)".  Example: "Jones, Pat" <br />-->
	      <font class="item" color="red"><b>Use % for the wildcard.</b></font></p>
	    </td>

	  </tr>
	</table>

	<table>
	  <tr>
	    <td><span class="itemsmall"><b>Cover Method Name:</b></span></td>
	    <td>
	      <html:text property="coverMethodType" size="40"/>
	    </td>
	    <td>
	      <span class="itemsmall">Include plots without a Cover Method:</span>
	      <html:checkbox property="allowNullCoverMethodType"/>
	    </td>
	    </tr>
	    <tr>

	      <td><span class="itemsmall"><b>Stratum Method Name:</b></span></td>
	      <td>
		<html:text property="stratumMethodName" size="40"/>
	      </td>
	      <td>
		<span class="itemsmall">Include plots without a Stratum Method:</span>
		<html:checkbox property="allowNullStratumMethodName" />
	      </td>

	    </tr>
	    
	    <!-- TODO:
	    <tr>
	      <td><span class="itemsmall"><b>Name of Plot Submitter (to VegBank):</b></span></td>
	      <td>
		<html:text property="plotSubmitterName" size="40"/>
	      </td>
	    </tr>
	    <tr>
	      <td><span class="itemsmall"><b>Name of Observation Contributor:</b></span></td>
	      <td>
		<html:text property="observationContributorName" size="40"/>
	      </td>
	      <td>
		<span class="itemsmall">Include plots without any Observation Contributors:</span>
		<html:checkbox property="allowNullObservationContributorName"/>
	      </td>
	    </tr>
	    -->
	    <tr>
	      <td><span class="itemsmall"><b>Project Name:</b></span></td>
	      <td>
		<html:text property="projectName" size="40"/>
	      </td>
	    </tr>

	  </table>
	
	</td>
      </tr>



      <!-- FIND USING VEGATATION -->
      

      <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
	<p><font face="Helvetica,Arial,Verdana" size="3"><b>Find Plots based on Vegetation</b></font></p></td>

	<tr><td colspan="2"></td><td>
	  <!-- PLANT TAXON -->
	  <table border="0" width="100%" bgcolor="#DFE5FA">
	    <tr>
	      <td align="left" valign="top" width="5%" colspan="2">
		<font face="Helvetica,Arial,Verdana" size="3" color="
		#23238E"><b>Plant Taxa:</b></font> 
	      </td>
	    </tr>
	    <tr>

	      <td align="center" width="5%">
		<img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
		width="15" height="15"> 
	      </td>
	      <td class="item"><p>
		Please enter names for plants that you wish to query.  You may also include
		criteria about other attributes that apply to that plant.  Plots will be returned that match ALL criteria for a row. 
		Plots will be returned that match all rows, or any row, based on the settings
		for <a href="#typeOfQuery">"Type of Query" setting at the end of this form.</a>
		<br />                <font class="item" color="red"><b>Use % for the wildcard.</b></font></p>
	      </td>
	    </tr>

	    <tr>
	      <td>
	      </td>
	      <td class="itemsmall">
		      <b>index of taxon names currently in the DB - by scientific	name:</b> <br>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=A%25">A</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=B%25">B</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=C%25">C</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=D%25">D</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=E%25">E</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=F%25">F</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=G%25">G</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=H%25">H</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=I%25">I</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=J%25">J</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=K%25">K</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=L%25">L</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=M%25">M</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=N%25">N</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=O%25">O</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=P%25">P</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=Q%25">Q</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=R%25">R</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=S%25">S</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=T%25">T</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=U%25">U</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=V%25">V</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=W%25">W</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=X%25">X</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=Y%25">Y</a><b>&middot;</b>
      		<a target="_blank" href="/vegbank/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonNameType=&amp;taxonLevel=&amp;targetDate=&amp;party=&amp;taxonName=Z%25">Z</a><b>&middot;</b>
      </td>
    </tr>
  </table>

<table border="0" cellspacing="1" cellpadding="1">
  <tr bgcolor="#DDDDDD">

    <th rowspan="2"><p><span class="item">Row</span></p></th>
    <th rowspan="2"><p><span  class="category">Plant Name</span></p></th>
    <th colspan="2"><p><span class="category">Cover (%)</span></p></th>

    <th colspan="2"><p><span class="category">Basal Area (m2/ha)</span></p></th>
    <!-- stems not wired in yet
    <th colspan="2"><p><span class="category">Stem DBH (cm)</span></p></th>

    <th colspan="2"><p><span class="category">Stem Count</span></p></th>
    -->

  </tr>

  <tr bgcolor="#DDDDDD">


    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>
    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>
    <!-- stems not wired in yet
    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>
    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>
    -->
  </tr>

  <%
  for (int i=0; i<5 ; i++)
  {
  %>
  <tr>

    <td><p><span class="item"><%= i+1 %></span></p></td>    
    <td><html:text property='<%= "plantName[" + i + "]" %>' size="30"/></td>
    <td><html:text property='<%= "minTaxonCover[" + i + "]" %>' size="5"/></td>
    <td><html:text property='<%= "maxTaxonCover[" + i + "]" %>' size="5"/></td>
    <td><html:text property='<%= "minTaxonBasalArea[" + i + "]" %>' size="5"/></td>
    <td><html:text property='<%= "maxTaxonBasalArea[" + i + "]" %>' size="5"/></td>
    <!-- stems not wired in yet
    <td><input name="min__stemcount.stemdiameter-1" size="5"></td>
    <td><input name="max__stemcount.stemdiameter-1" size="5"></td>
    <td><input name="min__stemcount.stemcount-1" size="5"></td>
    <td><input name="max__stemcount.stemcount-1" size="5"></td> 
    -->
        
  </tr>
  <%
  }
  %>

</table>
<hr size=".5"> 
<!-- richness-->
<!-- TODO:
<table border="0" width="100%" bgcolor="#DFE5FA">
  <tr>
    <td align="left" valign="top" width="5%" colspan="2">
      <font face="Helvetica,Arial,Verdana" size="3" color="
      #23238E"><b>Species Richness:</b></font> 
    </td>
  </tr>
  <tr>

    <td align="center" width="5%">
      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
      width="15" height="15"> 
    </td>
    <td class="item"><p>
      Please enter the maximum and minimum number of distinct 
      plant taxa occurring on each plot to be returned by this query.</p>
    </td>
  </tr>

</table>

<table border="0" cellspacing="1" cellpadding="1">
  <tr bgcolor="#DDDDDD">


    <th colspan="2"><p><span class="category">Richness</span></p></th>

  </tr>
  <tr bgcolor="#DDDDDD">

    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>
  </tr>

  <tr> 
    
    <td><input name="min__count_taxonobservation_plantname_id" size="10"></td>
    <td><input name="max__count_taxonobservation_plantname_id" size="10"></td>
  </tr>
</table>


<hr size=".5"> 
<!-- Veg Attributes in Observation Table -->

<!-- TODO:
<table border="0" width="100%" bgcolor="#DFE5FA">
  <tr>
    <td align="left" valign="top" width="5%" colspan="2">

      <font face="Helvetica,Arial,Verdana" size="3" color="
      #23238E"><b>Standard Strata / Growthforms:</b></font> 
    </td>
  </tr>
  <tr>
    <td align="center" width="5%">
      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
      width="15" height="15"> 
    </td>
    <td class="item"><p>
      This section deals with standardized strata covers and heights.  
      It also deals with the three predominant growthforms on a plot.  
      These values are all stored in the observation table.

      </p>
    </td>
  </tr>

</table>
<table border="0" cellspacing="1" cellpadding="1">
  <tr bgcolor="#DDDDDD">

    <th rowspan="2"><p><span  class="category">Stratum Name</span></p></th>
    <th colspan="2"><p><span class="category">Cover (%)</span></p></th>

    <th colspan="2"><p><span class="category">Height (m)</span></p></th>
    <th rowspan="2"><p><span class="category">Include Nulls?</span></p></th>

  </tr>
  <tr bgcolor="#DDDDDD">


    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>
    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>

  </tr>
  <tr>
    <td><p><span class="category">tree</span></p></td>
    <td><input name="min__observation.treecover" size="5"></td>
    <td><input name="max__observation.treecover" size="5"></td>
    <td><input name="min__observation.treeht" size="5"></td>
    <td><input name="max__observation.treeht" size="5"></td>
    <td><input name="nulls__observation.treecover" type="checkbox"></td>
  </tr>
  <tr>
    <td><p><span class="category">shrub</span></p></td>

    <td><input name="min__observation.shrubcover" size="5"></td>
    <td><input name="max__observation.shrubcover" size="5"></td>
    <td><input name="min__observation.shrubht" size="5"></td>
    <td><input name="max__observation.shrubht" size="5"></td>
    <td><input name="nulls__observation.shrubcover" type="checkbox"></td>
  </tr>
  <tr>
    <td><p><span class="category">field</span></p></td>
    <td><input name="min__observation.fieldcover" size="5"></td>
    <td><input name="max__observation.fieldcover" size="5"></td>

    <td><input name="min__observation.fieldht" size="5"></td>
    <td><input name="max__observation.fieldht" size="5"></td>
    <td><input name="nulls__observation.fieldcover" type="checkbox"></td>
  </tr>
  <tr>
    <td><p><span class="category">nonvascular</span></p></td>
    <td><input name="min__observation.nonvascularcover" size="5"></td>
    <td><input name="max__observation.nonvascularcover" size="5"></td>
    <td><input name="min__observation.nonvascularht" size="5"></td>
    <td><input name="max__observation.nonvascularht" size="5"></td>

    <td><input name="nulls__observation.nonvascularcover" type="checkbox"></td>
  </tr>
  <tr>
    <td><p><span class="category">floating</span></p></td>
    <td><input name="min__observation.floatingcover" size="5"></td>
    <td><input name="max__observation.floatingcover" size="5"></td>
    <td><span class="itemsmall">n/a</span></td>
    <td><span class="itemsmall">n/a</span></td>
    <td><input name="nulls__observation.floatingcover" type="checkbox"></td>

  </tr>
  <tr>
    <td><p><span class="category">submerged</span></p></td>
    <td><input name="min__observation.submergedcover" size="5"></td>
    <td><input name="max__observation.submergedcover" size="5"></td>
    <td><input name="min__observation.submergedht" size="5"></td>
    <td><input name="max__observation.submergedht" size="5"></td>
    <td><input name="nulls__observation.submergedcover" type="checkbox"></td>
  </tr>

  

</table>
<hr size=".5"> 
<table border="0" cellspacing="1" cellpadding="1">
  <tr bgcolor="#DDDDDD">

    <th rowspan="2"><p><span  class="category">Field Name</span></p></th>
    <th rowspan="2"><p><span class="category">Field Value</span></p></th>

    <th colspan="2"><p><span class="category">Cover</span></p></th>
    <th rowspan="2"><p><span class="category">Include Nulls?</span></p></th>

  </tr>
  <tr bgcolor="#DDDDDD">


    <th><p><span class="category">Min</span></p></th>
    <th><p><span class="category">Max</span></p></th>
  </tr>
  <tr>
    <td><p><span class="category">dominant stratum</span></p></td>
    <td>
      <select multiple name="observation.dominantstratum" size="6">

	<option value="%">--ANY--</option>
	<option>Tree</option><option>Shrub</option><option>Herb</option><option>Nonvascular</option><option>Floating</option><option>Submerged</option>
	<option value="Is Not Null">--NOT NULL--</option>
	<option value="Is Null">--NULL--</option>

      </select> 
    </td>
    <td><span class="itemsmall">n/a</span></td>
    <td><span class="itemsmall">n/a</span></td>
    <td><span class="itemsmall">n/a</span></td>
  </tr>
  <tr>
    <td><p><span class="category">growthform1</span></p></td>
    <td>

      <select multiple name="observation.growthform1type" size="6">
	<option value="%">--ANY--</option>
	<option>Trees</option><option>Needle-leaved tree</option><option>Broad-leaved deciduous tree</option><option>Broad-leaved evergreen tree</option><option>Thorn tree</option><option>Evergreen sclerophyllous tree</option><option>Succulent tree</option><option>Palm tree</option><option>Tree fern</option><option>Bamboo</option><option>Other tree</option><option>Shrubs</option><option>Needle-leaved shrub</option><option>Broad-leaved deciduous shrub</option><option>Broad-leaved evergreen shrub</option><option>Thorn shrub</option><option>Evergreen sclerophyllous shrub</option><option>Palm shrub</option><option>Dwarf-shrub</option><option>Semi-shrub</option><option>Succulent shrub</option><option>Other shrub</option><option>Herbs</option><option>Forb</option><option>Graminoid</option><option>Fern and fern allies</option><option>Succulent forb</option><option>Aquatic herb</option><option>Other herbaceous</option><option>Moss</option><option>Liverwort/hornwort</option><option>Lichen</option><option>Alga</option><option>Epiphyte</option><option>Vine/liana</option><option>Other/unknown</option><option>Not assessed</option>

	<option value="Is Not Null">--NOT NULL--</option>
	<option value="Is Null">--NULL--</option>
      </select> 
    </td>
    <td><input name="min__observation.growthform1cover" size="5"></td>
    <td><input name="max__observation.growthform1cover" size="5"></td>
    <td><input name="nulls__observation.growthform1cover" type="checkbox"></td>
  </tr>
  
  <tr>
    <td><p><span class="category">growthform2</span></p></td>

    <td>
      <select multiple name="observation.growthform2type" size="6">
	<option value="%">--ANY--</option>
	<option>Trees</option><option>Needle-leaved tree</option><option>Broad-leaved deciduous tree</option><option>Broad-leaved evergreen tree</option><option>Thorn tree</option><option>Evergreen sclerophyllous tree</option><option>Succulent tree</option><option>Palm tree</option><option>Tree fern</option><option>Bamboo</option><option>Other tree</option><option>Shrubs</option><option>Needle-leaved shrub</option><option>Broad-leaved deciduous shrub</option><option>Broad-leaved evergreen shrub</option><option>Thorn shrub</option><option>Evergreen sclerophyllous shrub</option><option>Palm shrub</option><option>Dwarf-shrub</option><option>Semi-shrub</option><option>Succulent shrub</option><option>Other shrub</option><option>Herbs</option><option>Forb</option><option>Graminoid</option><option>Fern and fern allies</option><option>Succulent forb</option><option>Aquatic herb</option><option>Other herbaceous</option><option>Moss</option><option>Liverwort/hornwort</option><option>Lichen</option><option>Alga</option><option>Epiphyte</option><option>Vine/liana</option><option>Other/unknown</option><option>Not assessed</option>

	<option value="Is Not Null">--NOT NULL--</option>
	<option value="Is Null">--NULL--</option>
      </select> 
    </td>
    <td><input name="min__observation.growthform2cover" size="5"></td>
    <td><input name="max__observation.growthform2cover" size="5"></td>
    <td><input name="nulls__observation.growthform2cover" type="checkbox"></td>
  </tr>
  
  <tr>
    <td><p><span class="category">growthform3</span></p></td>
    <td>
      <select multiple name="observation.growthform3type" size="6">
	<option value="%">--ANY--</option>
	<option>Trees</option><option>Needle-leaved tree</option><option>Broad-leaved deciduous tree</option><option>Broad-leaved evergreen tree</option><option>Thorn tree</option><option>Evergreen sclerophyllous tree</option><option>Succulent tree</option><option>Palm tree</option><option>Tree fern</option><option>Bamboo</option><option>Other tree</option><option>Shrubs</option><option>Needle-leaved shrub</option><option>Broad-leaved deciduous shrub</option><option>Broad-leaved evergreen shrub</option><option>Thorn shrub</option><option>Evergreen sclerophyllous shrub</option><option>Palm shrub</option><option>Dwarf-shrub</option><option>Semi-shrub</option><option>Succulent shrub</option><option>Other shrub</option><option>Herbs</option><option>Forb</option><option>Graminoid</option><option>Fern and fern allies</option><option>Succulent forb</option><option>Aquatic herb</option><option>Other herbaceous</option><option>Moss</option><option>Liverwort/hornwort</option><option>Lichen</option><option>Alga</option><option>Epiphyte</option><option>Vine/liana</option><option>Other/unknown</option><option>Not assessed</option>

	<option value="Is Not Null">--NOT NULL--</option>
	<option value="Is Null">--NULL--</option>
      </select> 
    </td>
    <td><input name="min__observation.growthform3cover" size="5"></td>
    <td><input name="max__observation.growthform3cover" size="5"></td>
    <td><input name="nulls__observation.growthform3cover" type="checkbox"></td>
  </tr>
  
</table>
-->
      


      <!-- FIND USING COMMUNITIES -->
      <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
	<p><font face="Helvetica,Arial,Verdana" size="3"><b>Find Plots based on Community Classfication</b></font></p></td>
	<tr><td colspan="2"></td><td>
    
	  <!-- VEG COMMUNITY -->
	  <table border="0" width="100%" bgcolor="#DFE5FA">
	    <tr>
	      <td align="left" valign="top" width="5%" colspan="2">
		<font face="Helvetica,Arial,Verdana" size="3" color="
		#23238E"><b>Veg Community:</b></font> 
	      </td>

	    </tr>
	    <tr>
	      <td align="center" width="5%">
		<img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
		width="15" height="15"> 
	      </td>
	      <td class="item"><p>
		Use this section to query for plots that have been assigned to a community based on the criteria you specify here.  
		This section functions much like the plant section above.
		Plots will be returned that match ALL criteria for a row.  Plots will be returned that match all rows, or any row, based on the settings
		for <a href="#typeOfQuery">"Type of Query" setting at the end of this form.</a>
		<br /><font class="item" color="red"><b>Use % for the wildcard.</b></font>

		</p>
	      </td>
	    </tr>
	  </table>
	  <table border="0" cellspacing="1" cellpadding="1">
	    <tr bgcolor="#DDDDDD">
	      <th rowspan="2"><p><span class="item">Row</span></p></th>
	      <th rowspan="2"><p><span class="category">Community Name</span></p></th>
	      <!-- TODO:
	      <th rowspan="2"><p><span class="category">Fit</span></p></th>
	      <th rowspan="2"><p><span class="category">Confidence</span></p></th>
	      -->
	      <th colspan="2"><p><span class="category">Date Classified</span></p></th>
	      <!--
	      <th rowspan="2"><p><span class="category">Name of Person Classifying</span></p></th>
	      -->
	    </tr>
	    <tr bgcolor="#DDDDDD">
	      <th><p><span class="category">Min</span></p></th>
	      <th><p><span class="category">Max</span></p></th>
	    </tr>
	    <tr>

	      <%
	      for (int i=0; i<4 ; i++)
	      {
	      %>

	      <tr>
		<td><p><span class="item"><%= i+1 %></span></p></td>    
		<td><html:text property='<%= "commName[" + i + "]" %>' size="30"/></td>

		<!-- TODO:
		<td>
		  <select multiple name="comminterpretation.classfit-1" size="3">
		  <option value="%">--ANY--</option>
		  <option>Absolutely wrong</option><option>Understandable but wrong</option><option>Reasonable or acceptable answer</option><option>Good answer</option><option>Absolutely right</option>

		  <option value="Is Not Null">--NOT NULL--</option>
		  <option value="Is Null">--NULL--</option>
		</select>  
	      </td>
	      <td>
		<select multiple name="comminterpretation.classconfidence-1" size="3">
		  <option value="%">--ANY--</option>
		  <option>High</option><option>Medium</option><option>Low</option>
		  <option value="Is Not Null">--NOT NULL--</option>
		  <option value="Is Null">--NULL--</option>
		</select> 
	      </td>
	      -->
	      <td><html:text property='<%= "maxCommStartDate[" + i + "]" %>' size="10"/></td>
	      <td><html:text property='<%= "minCommStopDate[" + i + "]" %>' size="10"/></td>
	      <!--
	      <td><input name="classcontributorname-1" size="30"></td>
		-->

	      </tr>
	      <%
	      }
	      %>
	    </table>

  

	    <!-- SUBMIT THE FORM -->
	    <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
	      <p><font face="Helvetica,Arial,Verdana" size="3"><b><a name="typeOfQuery" > </a>Submit Query to VegBank</b></font></p></td>
	      <tr><td colspan="2"></td><td>
		<table border="0" width="100%" bgcolor="#DFE5FA">
		  <tr>
		    <td align="left" valign="top" width="5%" colspan="2">
		      <font face="Helvetica,Arial,Verdana" size="3" color="
		      #23238E"><b>Type of Query:</b></font> 
		    </td>
		  </tr>
		  
		  <tr>
		    <td align="center" width="5%">
		      <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
		      width="15" height="15"> 
		    </td>
		    <td class="item"><p>
		      Plots can be selected that <b>match all</b> the above criteria (AND)
		      or that <b>match any</b> of the above criteria (OR). <br />

		      <!-- For more complicated queries that allow a combination of AND and OR queries, see our <a href="nested-plot-query.html">nested query page</a>.--></p>
		    </td>
		  </tr>
		</table>
		<!-- table for buttons-->
		<table>

		  <tr>
		    <td align="left" colspan="2" valign="center">

		      <p>
		      <html:radio property="conjunction" value=" AND "/>match ALL criteria<br/>
		      <html:radio property="conjunction" value=" OR "/>match ANY criteria<br />
		      </p>

		      <small>
		      <html:submit value="search"/>&nbsp;&nbsp;
		      <html:reset value="reset"/>
		      </small>

		    </td>
		  </tr>
		</table>

		<!-- back one indent -->
	      </td>
	    </tr>


      
      
	    <!-- HOW TO USE THIS FORM -->
	    <tr bgcolor="#FFFFCC" >
	      <td width="10%"></td>
	      <td colspan="2">
		<p><a name="instructions" /> </a><font face="Helvetica,Arial,Verdana" size="3"><b>Notes on how to use this form</b></font></p>
	      </td>
	      <tr>
		<td colspan="2"></td><td>

      <!-- Note about Rows -->
      <table border="0" width="100%" bgcolor="#DFE5FA">
	<tr>
	  <td align="left" valign="top" width="5%" colspan="2">
	    <font face="Helvetica,Arial,Verdana" size="3" color="
	    #23238E"><b>Note about Marked Rows:</b></font> 
	  </td>

	</tr>
	<tr>
	  <td align="center" width="5%">
	    <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	    width="15" height="15"> 
	  </td>
	  <td class="item"><p>
	    For criteria on this page that are numbered in Rows (for example, querying by plant names),
	    an observation is returned if it matches ALL crieria for a row.  If you want to match ANY
	    criteria, use different rows.  <br />
	    For example, if you specify :<br />
	    ROW 1: plant name = "Acer" , cover minumum = "20" , cover maximum = "40"<br />

	    ---VegBank returns plots that have plants that are named "Acer" AND have a cover &gt;= 20% AND cover &lt;= 40%  
	    <br />If you specify:<br />
	    ROW 1: plant name = "Acer"  <br />
	    ROW 2: cover minumum = "20" <br />
	    ROW 3: cover maximum = "40" <br />
	    ---VegBank returns plots that have "Acer" on the plot (regardless of its cover value) AND/OR a taxon (regardless of name) of the plot has a 
	    cover &gt;= 20% AND/OR a taxon (regardless of name, not necessarily the same taxon as above) on the plot has a cover 
	    &lt;= 40% (where AND/OR is specified at the <a href="#typeOfQuery">end of the form</a>, as mentioned above).</p>

	  </td>
	</tr>

      </table>

      <hr size=".5"> 

      <!-- Minimums, Maximums, Include Nulls -->
      <table border="0" width="100%" bgcolor="#DFE5FA">
	<tr>
	  <td align="left" valign="top" width="5%" colspan="2">

	    <font face="Helvetica,Arial,Verdana" size="3" color="
	    #23238E"><b>Minimums, Maximums, Include Nulls:</b></font> 
	  </td>
	</tr>
	<tr>
	  <td align="center" width="5%">
	    <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	    width="15" height="15"> 
	  </td>
	  <td class="item"><p>
	    Similarly, if a field has min and max fields, observations will be returned that
	    have attribute values greater than or equal to the minimum AND less than or equal to the maxiumum. <br />

	    Any fields that have the option of including nulls in the query results will look for plots that have 
	    the attribute value specified (if any value is specified) OR the attribute value is null. <br />
	    For example, if you search for plots with elevation minimum of "50" and maximum of "200" and check the "include nulls?" box, <br/>
	    ---VegBank returns all plots that have an elevation between (50 AND 200) OR have no elevation value at all.<br/>

	    </p>
	  </td>
	</tr>

      </table>

      <hr size=".5"> 


      <!-- --NULL-- and --NOT NULL-- picklist values -->
      <table border="0" width="100%" bgcolor="#DFE5FA">
	<tr>
	  <td align="left" valign="top" width="5%" colspan="2">
	    <font face="Helvetica,Arial,Verdana" size="3" color="
	    #23238E"><b>--NULL-- and --NOT NULL-- picklist values</b></font> 
	  </td>
	</tr>
	<tr>
	  <td align="center" width="5%">

	    <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	    width="15" height="15"> 
	  </td>
	  <td class="item"><p>
	    Note also that you may select multiple choices in fields that have choices specified.  Choose "--ANY--" to ignore the field.  
	    The values "--NOT NULL--" and "--NULL--" appear at the end of each list, and can be used to specify
	    where the attribute value is non-null (the field has a value) or is null (the field in blank).  These, too, may
	    be selected along with other values in the list.


	    </p>
	  </td>
	</tr>

      </table>

      <hr size=".5">  


      <!-- Formats in which to enter data -->
      <table border="0" width="100%" bgcolor="#DFE5FA">
	<tr>
	  <td align="left" valign="top" width="5%" colspan="2">
	    <font face="Helvetica,Arial,Verdana" size="3" color="
	    #23238E"><b>How to Enter People, Dates, Units</b></font> 
	  </td>
	</tr>
	<tr>
	  <td align="center" width="5%">

	    <img src="/vegbank/images/icon_cat31.gif" alt="exclamation"
	    width="15" height="15"> 
	  </td>
	  <td class="item"><p>
	    Enter a person's name as "Surname, GivenName(s)" (example: "Doe, John" or "Smith, Paula Esther"). </p>
	    <p>
	    Enter dates either in the format "MM/DD/YYYY" (example: "10/23/1967") or "DD-MMM-YYYY" (example: "25-JUN-2002"). </p>
	    <p>
	    Enter numerical values without any units (example: for "500 feet", enter "500").  <br/>For fields that have a unit of percent, enter the value of the 
	    percent, not the decimal equivalent (example: for "35%", enter "35", not "0.35").

	    </p>
	  </td>
	</tr>

      </table>

    

	    
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
	<td colspan="3">
	  <!-- VEGBANK FOOTER -->
	  @vegbank_footer_html_tworow@
	</td>
      </tr>
    </table>
    </html:form>    
    </body>
  </html>
