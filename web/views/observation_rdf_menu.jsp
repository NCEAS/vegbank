<!--@ webpage_top_html @-->
  @stdvegbankget_jspdeclarations@

  <!--@ webpage_head_html @-->
  <!--@ ajax_js_include @ -->
  <!--@ datacart_js_include @ -->



<script type="text/javascript">
function getHelpPageId() {
  return "plot-search-rdf-menu";
}
</script>
  <!-- include script for sorting tables -->
  <!--script language="javascript" src="@includes_link@sort_table.js"></script-->

<title>Download VegBank Metadata</title>


<%@ include file="includeviews/inlinestyles.jsp" %>

<!--@ webpage_masthead_html @-->

 <!--@ possibly_center @-->
  <h2>Download VegBank Metadata</h2>

<vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk"
    whereNonNumeric="where_observation_ac" beanName="map" pager="false" xwhereEnable="true"
    perPage="-1"
    allowOrderBy="false" />  <!-- save="plot-search-results" /-->


<%@ include file="../includes/setup_rowindex.jsp" %>

  <div id="tut_plotcriteriamessages">

<!-- tell the user what was searched for, if criteriaAsText is passed here : -->
<logic:present parameter="criteriaAsText">
  <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
  <logic:notEmpty name="bean_criteriaAsText">
    <p class="psmall">You searched for plots: <bean:write name="bean_criteriaAsText" /></p>
  </logic:notEmpty>
</logic:present>
</div>

<logic:empty name="plotobs-BEANLIST">
                Sorry, no plot-observations are available.
          </logic:empty>
<logic:notEmpty name="plotobs-BEANLIST"><!-- set up table -->


<TABLE width="100%" class="thinlines" cellpadding="2">

<% int obsNum = 0; %>

<logic:iterate id="onerowofobservation" name="BEANLIST">
<!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofplot" name="onerowofobservation" />
<bean:define id="obsId" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>

<!-- start of plot & obs fields-->
<% if ((obsNum % 1000) == 0 ) { %>

<tr class='@nextcolorclass@' align="left">
<td>
  <a href="@views_link@observation_rdf.jsp?pageNumber=<%= ( ( obsNum / 1000 ) + 1 ) %>&<%= request.getQueryString() %>">file <%= ( ( obsNum / 1000 ) + 1 ) %> </a>
</td>
</tr>

<% } // end of 1000
  obsNum ++ ;
  %>

</logic:iterate><!-- plot -->
</TABLE>



</logic:notEmpty>



<br />
&copy; Ecological Society of America
<!-- @webpage_footer_html  @-->


