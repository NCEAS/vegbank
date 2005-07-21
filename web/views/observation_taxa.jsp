@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<bean:define id="FullPageWidthSuffix" value="_100perc" /><!-- sets stylesheet to full width stylesheet -->
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@

  
  
<script type="text/javascript">
function getHelpPageId() {
  return "plot-search-results";
}
</script>

<title>View Taxa on Plot(s): VegBank</title>
<%     String rowClass = "evenrow";  %>

<%@ include file="includeviews/inlinestyles.jsp" %>


@webpage_masthead_html@

 @possibly_center@  
  <h2>Taxa on Plots View</h2>
  <div id="tut_plotcriteriamessages">

<!-- tell the user what was searched for, if criteriaAsText is passed here : -->
<logic:present parameter="criteriaAsText">
  <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
  <logic:notEmpty name="bean_criteriaAsText">
    <p class="psmall">You searched for plots: <bean:write name="bean_criteriaAsText" /></p>
  </logic:notEmpty>
</logic:present>  
</div>


<vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true" 
    allowOrderBy="true" />  <!-- save="plot-search-results" /-->


<vegbank:pager />
<div style="clear:both; display: block;">
                                                                                                                                                                                                  
   <table class="noborder">
    <tr>
    <!-- add all results -->
    <td>
        <a href="javascript:addAllResults('observation');"
            title="add all query results to datacart" class="nobg"><img src="/vegbank/images/cart_star_on_blue2.gif" border="0" id="datacart-addallresults-icon" /></a>
    </td>
    <td>
        <a href="javascript:addAllResults('observation');"
            title="add all results to datacart">add all query results</a> to datacart,&nbsp;&nbsp;
    </td>
                                                                                                                                                                                                  
    <!-- add page -->
    <td>
        <a href="javascript:addAllOnPage()" title="add all on page to datacart" class="nobg"><img src="/vegbank/images/cart_add_one.gif" border="0" /></a>
    </td>
    <td>
        <a href="javascript:addAllOnPage()" title="add all on page">add plots on page</a> to datacart,&nbsp;&nbsp;
    </td>
                                                                                                                                                                                                  
    <!-- drop page -->
    <td>
        <a href="javascript:dropAllOnPage()" title="drop all on page from datacart" class="nobg"><img src="/vegbank/images/cart_drop_one.gif" border="0" /></a>
    </td>
    <td>
        <a href="javascript:dropAllOnPage()" title="drop all on page">drop plots on page</a> from datacart
    </td>
    </tr>
                                                                                                                                                                                                  
  </table>
</div>


<%@ include file="../includes/setup_rowindex.jsp" %>

<logic:empty name="plotobs-BEANLIST">
                Sorry, no plot-observations are available.
          </logic:empty>
<logic:notEmpty name="plotobs-BEANLIST"><!-- set up table -->

    <logic:equal parameter="delta" value="findadd-observationaccessioncode">
        <vegbank:datacart delta="findadd:observation:observation:observation_id:observationaccessioncode" deltaItems="getQuery" display="false" />
    </logic:equal>


<form action="" method="GET" id="cartable">
 
<TABLE width="100%" class="thinlines" cellpadding="2">

<logic:iterate id="onerowofobservation" name="BEANLIST">
<!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofplot" name="onerowofobservation" />
<bean:define id="obsId" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>

<!-- start of plot & obs fields-->

<tr class='@nextcolorclass@' align="left">
<td><!-- that td cannot have a class, it gets overwritten -->
<bean:define id="delta_ac" name="onerowofobservation" property="observationaccessioncode" />
<strong>CART: </strong> Drop/Add Plot #<%= rowIndex++ %> 

<%@ include file="../includes/datacart_checkbox.jsp" %>

  
</td>
<td>
<strong><bean:write name="onerowofobservation" property="authorplotcode" /></strong>
<a href="@get_link@comprehensive/observation/<bean:write name='observation_pk' />">More Detail</a> |
<a href="@get_link@summary/observation/<bean:write name='observation_pk' />">Less Detail</a> 
</td>
</tr>
<tr><td colspan="2">
<bean:parameter id="showHelpBean" name="showHelp" value="false" />
<bean:parameter id="strataToShowBean" name="strata2Show" value="2" />
<bean:define id="requestedPageURL"><%= "/views/raw/raw_taxonobservation.jsp?observation_pk=" + observation_pk  + "&showHelp=" + showHelpBean + "&strata2Show=" + strataToShowBean %></bean:define>
<!-- page requesting :  <bean:write name="requestedPageURL" /> --> 
<bean:include id="taxonobs_page" page='<%= requestedPageURL %>' />
<!-- get RAW view -->
<bean:write name="taxonobs_page" filter="false" />
<!-- end of RAW view -->
</td>
</tr>
</logic:iterate><!-- plot -->
</TABLE>
</form>
@mark_datacart_items@

<vegbank:pager />

</logic:notEmpty>



<br />
@webpage_footer_html@


