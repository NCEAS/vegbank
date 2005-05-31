@webpage_top_html@
  @stdvegbankget_jspdeclarations@
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

  @webpage_masthead_html@   @possibly_center@  
  <h2>Comprehensive View of a Plot</h2>
  <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>
<vegbank:pager />
<logic:empty name="plotobs-BEANLIST"> Sorry, no plot-observations are available. </logic:empty>
<!--NOPE logic:notEmpty name="plotobs-BEANLIST" --><!-- set up table -->
<!--NOPE logic:iterate id="onerowofobservation" name="BEANLIST"--><!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofobservation" name="plotobs-BEAN" />
<bean:define id="onerowofplot" name="plotobs-BEAN" />
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>


<!-- datacart item -->
  <bean:define id="delta_ac" name="onerowofobservation" property="observationaccessioncode" />
  <% rowClass = ""; %>
<div style="display: block; float: left; width: 55%; text-align: left;">
    <form action="" method="GET" id="cartable">
       <%@ include file="../includes/datacart_checkbox.jsp" %> click to update datacart
    </form>
</div>

<div style="display: block; float: right; width: 45%; text-align: right;" id="tut_showhidefieldinfo">
   <a href="<vegbank:changeParam paramName='showHelp' paramValue='true' />">Show</a>
   | <a href="<vegbank:changeParam paramName='showHelp' paramValue='false' />">Hide</a> Field Info
</div>


<!-- start of plot & obs fields-->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2" style="clear: both; display: block;">
<TR><TD width="55%" align="center" valign="top" id="tut_plotdetailleft"><!-- plot level info -->
<table width="98%" class="leftrightborders" cellpadding="2"><!--each field, only write when HAS contents-->

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
<tr><th>Location Fields:</th><th>&nbsp;</th></tr>
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
</TD><TD valign="top" align="center" id="tut_plotdetailplants"><!-- plants in this plot -->

<!-- %@ include file="includeviews/sub_taxonobservation.jsp" %-->
<!-- get params to pass if they are here -->
<bean:parameter id="showHelpBean" name="showHelp" value="false" />
<bean:parameter id="strataToShowBean" name="strata2Show" value="2" />
<bean:define id="requestedPageURL"><%= "/views/raw/raw_taxonobservation.jsp?observation_pk=" + observation_pk  + "&showHelp=" + showHelpBean + "&strata2Show=" + strataToShowBean %></bean:define>
<!-- page requesting :  <bean:write name="requestedPageURL" /> --> 
<bean:include id="taxonobs_page" page='<%= requestedPageURL %>' />
<bean:write name="taxonobs_page" filter="false" />

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

<bean:include id="commclass_page" page='<%= "/views/raw/raw_commclass.jsp?observation_pk=" + observation_pk  + "&showHelp=" + showHelpBean%>' />
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
<!--NOPE /logic:iterate NOPE-->
<vegbank:pager/>
<!--NOPE /logic:notEmpty NOPE--><br />
@webpage_footer_html@
