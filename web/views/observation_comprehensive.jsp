@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<bean:define id="FullPageWidthSuffix" value="_100perc" /><!-- sets stylesheet to full width stylesheet -->
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@

   <%     String rowClass = "evenrow";  %>
<title>VegBank Comprehensive Plot View</title>
<script type="text/javascript">
function getHelpPageId() {
  return "view-plot-details";
}

</script>

<%@ include file="includeviews/inlinestyles.jsp" %>
  @webpage_masthead_html@   @possibly_center@  

  <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>
<vegbank:pager />
<logic:empty name="plotobs-BEANLIST"> Sorry, no plot-observations are available. </logic:empty>
<!-- NOPE logic:notEmpty name="plotobs-BEANLIST" NOPE--><!-- set up table -->
<!-- NOPE logic:iterate id="onerowofobservation" name="BEANLIST" NOPE--><!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofobservation" name="plotobs-BEAN" />
<bean:define id="onerowofplot" name="onerowofobservation" />
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>

  <table class="noborders"><tr><td align="left">
  
    <h2 align="center">Comprehensive View of a Plot</h2>
  

<!-- datacart item -->
  <bean:define id="delta_ac" name="onerowofobservation" property="observationaccessioncode" />
  <% rowClass = ""; %>
<div style="display: block; float: left; width: 55%; text-align: left;">
    <form action="" method="GET" id="cartable">
       <%@ include file="../includes/datacart_checkbox.jsp" %> click to update datacart
    </form>
</div>

<div style="display: block; float: right; width: 45%; text-align: center;" id="tut_showhidefieldinfo">
   <strong>Configure View</strong> <br/>
   <a href="@get_link@taxa/observation/<bean:write name='observation_pk' />">Less Plot Detail</a>  --
   <a href="javascript:void setupConfig('<bean:write name='thisviewid' />');">Configure data displayed on this page</a>
</div>


<!-- start of plot & obs fields-->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2" style="clear: both; display: block;">
<TR><TD width="55%" valign="top" id="tut_plotdetailleft"><!-- plot level info -->
<div class="padded">
<table width="100%" class="leftrightborders" cellpadding="2"><!--each field, only write when HAS contents-->

    <tr><th class="major" colspan="2">
            <bean:write name="onerowofplot" property="authorplotcode"/>
    </th></tr>

  @mark_datacart_items@

<tr class="@nextcolorclass@"><td colspan="2">&raquo; Citation URL: <a href='/cite/<bean:write name="onerowofplot" property="observationaccessioncode" />'>http://vegbank.org/cite/<bean:write name="onerowofplot" property="observationaccessioncode" /></a>
<br/>&raquo; <a href="@general_link@cite.html">Citing info</a></td></tr>
<tr><th>Plot ID Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotidlong_data.jsp" %>         
<%@ include file="autogen/observation_plotidlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>

<!-- custom, show children if there are any -->
 <logic:greaterThan name="onerowofplot" property="countchildplots" value="0">
   <tr class="@nextcolorclass@"><td class="datalabel">Has Sub Plots</td>
     <td>
       <a href="@get_link@summary/observation/<bean:write name='onerowofplot' property='plot_id' />?where=where_plot_childrenof">
         <bean:write name="onerowofplot" property="countchildplots" /> plot(s)
       </a>
     </td>
   </tr>
 </logic:greaterThan>
<tr><th>Location Fields:</th>
  <bean:define id="trunclat"><bean:write name='onerowofplot' property='latitude' /></bean:define>
  <bean:define id="trunclong"><bean:write name='onerowofplot' property='longitude' /></bean:define>
  <% if (trunclat.length() > 7 ) { trunclat = trunclat.substring(0,7); } %>
  <% if (trunclong.length() > 7 ) { trunclong = trunclong.substring(0,7); } %>
  <th>MAP:<a title="TopoZone provides a close up map of the plot's location (new window)" target="_new" href="http://www.topozone.com/map.asp?lat=<bean:write name='onerowofplot' property='latitude' />&lon=<bean:write name='onerowofplot' property='longitude' />&datum=nad83&u=5">TopoZone</a>
  | <a title="MapQuest provides a more general map of the plot's location (new window)" target="_new" href="http://www.mapquest.com/maps/map.adp?searchtype=address&formtype=latlong&latlongtype=decimal&latitude=<%= trunclat %>&longitude=<%= trunclong %>">MapQuest</a>
  </th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotloclong_data.jsp" %>
<%@ include file="autogen/observation_plotloclong_data.jsp" %>
<%@ include file="includeviews/sub_place.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Layout Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotlayoutlong_data.jsp" %>
<%@ include file="autogen/observation_plotlayoutlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Environment Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotenvlong_data.jsp" %>
<%@ include file="autogen/observation_plotenvlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Methods Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotmethodlong_data.jsp" %>
<%@ include file="autogen/observation_plotmethodlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Plot quality Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotqualitylong_data.jsp" %>
<%@ include file="autogen/observation_plotqualitylong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Overall Plot Vegetation Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotoverallveglong_data.jsp" %>
<%@ include file="autogen/observation_plotoverallveglong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Misc Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotmisclong_data.jsp" %>
<%@ include file="autogen/observation_plotmisclong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<!-- end of plot/obs fields -->
</table>
</div>
</TD><TD valign="top" id="tut_plotdetailplants">
<div class="padded">
<!-- plants in this plot -->

<!-- %@ include file="includeviews/sub_taxonobservation.jsp" %-->
<!-- get params to pass if they are here -->

<bean:parameter id="strataToShowBean" name="strata2Show" value="2" />
<bean:define id="requestedPageURL"><%= "/views/raw/raw_taxonobservation.jsp?observation_pk=" + observation_pk  + "&strata2Show=" + strataToShowBean %></bean:define>
<!-- page requesting :  <bean:write name="requestedPageURL" /> --> 
<bean:include id="taxonobs_page" page='<%= requestedPageURL %>' />
<bean:write name="taxonobs_page" filter="false" />
</div>
</TD>
</TR>
<!-- obs contrib -->
<TR><TD COLSPAN="2" align="left">
<vegbank:get id="observationcontributor" select="observationcontributor" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="9">Observation Contributors:</th></tr>
<logic:empty name="observationcontributor-BEANLIST">
<tr><td class="@nextcolorclass@">  No Observation Contributors found.</td></tr>
</logic:empty>
<logic:notEmpty name="observationcontributor-BEANLIST">
<tr>
<%@ include file="autogen/observationcontributor_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofobservationcontributor" name="observationcontributor-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/observationcontributor_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
</TD></TR>


<TR><TD colspan="2" valign="top" align="left">
<!-- community info -->

<bean:include id="commclass_page" page='<%= "/views/raw/raw_commclass.jsp?observation_pk=" + observation_pk  %>' />
<bean:write name="commclass_page" filter="false" />

<!--vegbank:get id="commclass" select="commclass" beanName="map" 
    where="where_observation_pk" wparam="observation_pk" pager="false"/-->
<!--%@ include file="includeviews/sub_commclass_summary.jsp" %-->

<vegbank:get id="soilobs" select="soilobs" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<logic:notEmpty name="soilobs-BEANLIST">
<br/><br/>
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="19">Soil Observations:</th></tr>
<tr>
<%@ include file="autogen/soilobs_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofsoilobs" name="soilobs-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/soilobs_summary_data.jsp" %>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>

<vegbank:get id="disturbanceobs" select="disturbanceobs" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<logic:notEmpty name="disturbanceobs-BEANLIST">
<br/><br/>
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="9">Disturbance Data:</th></tr>
<tr>
<%@ include file="autogen/disturbanceobs_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofdisturbanceobs" name="disturbanceobs-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/disturbanceobs_summary_data.jsp" %>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>
</TD>
</TR>
<TR><TD colspan="2"><hr /><br/></TD></TR>
</TABLE>
<!--/logic:iterate NOPE-->
<vegbank:pager/>
<!-- NOPE /logic:notEmpty NOPE--><br />
@webpage_footer_html@
