@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<bean:define id="FullPageWidthSuffix" value="_100perc" /><!-- sets stylesheet to full width stylesheet -->
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@

<title>VegBank Comprehensive Plot View</title>
<script type="text/javascript">
function getHelpPageId() {
  return "view-plot-details";
}

</script>
  <!-- include script for sorting tables -->
  <script language="javascript" src="@includes_link@sort_table.js"></script>



  <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk"
    whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>


<logic:empty name="plotobs-BEAN">
  <!-- define dummy variables : -->
   <bean:define id="onerowofobservation">-1</bean:define>
  <bean:define id="onerowofplot" >-1</bean:define>
  <bean:parameter name="wparam" id="beanwparam" value="-1" />
  <logic:redirect page="/views/observation_summary.jsp" paramId="wparam" paramName="beanwparam" />

</logic:empty>

<logic:notEmpty name="plotobs-BEAN">
<!-- NOPE logic:notEmpty name="plotobs-BEANLIST" NOPE--><!-- set up table -->
<!-- NOPE logic:iterate id="onerowofobservation" name="BEANLIST" NOPE--><!-- iterate over all records in set : new table for each -->
  <bean:define id="onerowofobservation" name="plotobs-BEAN" />
  <bean:define id="onerowofplot" name="onerowofobservation" />
  <bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
  <bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
</logic:notEmpty>

<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
  <bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>


<%@ include file="includeviews/inlinestyles.jsp" %>
  @webpage_masthead_html@   @possibly_center@
  <table class="noborders"><tr><td align="left">

    <h2 align="center">Comprehensive View of a Plot</h2>


<!-- datacart item -->

  <logic:notEmpty name="onerowofobservation" property="observationaccessioncode">
  <bean:define id="delta_ac" name="onerowofobservation" property="observationaccessioncode" />
  <% rowClass = ""; %>
<div style="display: block; float: left; width: 55%; text-align: left;">
    <form action="" method="GET" id="cartable">
       <%@ include file="../includes/datacart_checkbox.jsp" %> click to update datacart
    </form>
</div>

  </logic:notEmpty> <!-- accession code -->

<logic:empty name="onerowofobservation" property="observationaccessioncode">
  <bean:define id="delta_ac">unknown</bean:define>
</logic:empty>

<div style="display: block; float: right; width: 45%; text-align: center;" id="tut_showhidefieldinfo">
   <strong>Configure View</strong> <br/>
   <a href="@get_link@taxa/observation/<bean:write name='observation_pk' />">Less Plot Detail</a>  --
   <a href="@get_link@summary/stemcount/<bean:write name='observation_pk' />?where=where_observation_pk">
   Stems Detail</a>  --
   <a href="javascript:void setupConfig('<bean:write name='thisviewid' />');">Configure data displayed on this page</a>
</div>


<!-- start of plot & obs fields-->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2" style="clear: both; display: block;">
<TR><TD width="55%" valign="top" id="tut_plotdetailleft"><!-- plot level info -->
  <!-- plot-level info removed to different view -->
  <logic:equal name="onerowofobservation" property="hasobservationsynonym" value="t">
      <h3>THIS PLOT HAS BEEN <a href="@views_link@observation_summary.jsp?where=where_observationsynonym_synonym&criteriaAsText=that+are+updated+version(s)+of+a+plot&wparam=<bean:write name='observation_pk' />">UPDATED WITH A NEWER PLOT</a>.</h3>
  </logic:equal>
  
  
  <jsp:include page="observation_plotlevel.jsp">
    <jsp:param name="wparam"  value="<%= observation_pk %>" />
  </jsp:include>


</TD><TD valign="top" id="tut_plotdetailplants">
<div class="padded">
<!-- plants in this plot -->

<!-- %@ include file="includeviews/sub_taxonobservation.jsp" %-->
<!-- get params to pass if they are here -->

<bean:parameter id="strataToShowBean" name="strata2Show" value="2" />
<bean:define id="requestedPageURL"><%= "/views/raw/raw_taxonobservation.jsp?observation_pk=" + observation_pk  + "&strata2Show=" + strataToShowBean %><logic:present name="do_not_show_userdefined_data">&do_not_show_userdefined_data=true</logic:present></bean:define>
<!-- page requesting :  <bean:write name="requestedPageURL" /> -->
<bean:include id="taxonobs_page" page='<%= requestedPageURL %>' />
<bean:write name="taxonobs_page" filter="false" />
</div>
</TD>
</TR>
</TABLE>
<!-- obs contrib -->

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

<logic:notPresent name="do_not_show_userdefined_data">
     <bean:define id="soilobs_pk" name="onerowofsoilobs" property="soilobs_id" />
     <bean:include id="defval_soilobs" page='<%= "/views/raw/raw_definedvalue_summary.jsp?wparam=soilobs" + Utility.PARAM_DELIM + soilobs_pk  %>' />
     <logic:notMatch name="defval_soilobs" value="@!NO_USER_DEFINED_VALUES!@">
     <tr class="<%= rowClass %>"><td><strong>User Defined Values:</strong></td><td colspan="25">
     <table class="thinlines">
     <bean:write name="defval_soilobs" filter="false" />
     </table>
     </td></tr>
     </logic:notMatch>
</logic:notPresent>

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

<!-- if this bean is defined, then don't even look up userdefinded data, (and do not display it) -->
<logic:notPresent name="do_not_show_userdefined_data">
     <br/><br/>
     <table class="thinlines">
     <tr><th colspan="3">User Defined Values</th></tr>

     <bean:define id="userdefinedexist" value="false" /><!-- default value-->
     <bean:include id="defval_obs" page='<%= "/views/raw/raw_definedvalue_summary.jsp?wparam=observation" + Utility.PARAM_DELIM + observation_pk  %>' />
     <logic:notMatch name="defval_obs" value="@!NO_USER_DEFINED_VALUES!@">
       <bean:define id="userdefinedexist" value="true" /><!-- wrote some -->
       <bean:write name="defval_obs" filter="false" />
     </logic:notMatch>

     <bean:include id="defval_plot" page='<%= "/views/raw/raw_definedvalue_summary.jsp?wparam=plot" + Utility.PARAM_DELIM + plot_pk  %>' />
     <logic:notMatch name="defval_plot" value="@!NO_USER_DEFINED_VALUES!@">
       <bean:define id="userdefinedexist" value="true" /><!-- wrote some -->
       <bean:write name="defval_plot" filter="false" />
     </logic:notMatch>

     <logic:equal name="userdefinedexist" value="false"><!-- if I didn't write any -->
       <tr><td colspan="2">No User Defined Data for this plot</td></tr>
     </logic:equal>

     </table>
</logic:notPresent>
<!--/logic:iterate NOPE-->
<!--vegbank:pager/-->
<!-- NOPE /logic:notEmpty NOPE--><br />
@webpage_footer_html@
