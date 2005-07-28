@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@
<!-- purpose : show datacart (singular selected dataset) for the user -->
 
   
<TITLE>Your Datacart</TITLE>
<script type="text/javascript">
function getHelpPageId() {
  return "view-datacart";
}

</script>

 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
  
  <!-- get user Id -->
<%@ include file="includeviews/sub_getwebuserid.jsp" %>
<% Long lngDatacartId = DatasetUtility.getDatacartId(request.getSession()); %>
  
<h1>Your Datacart</h1>
<p class="instructions">
  Your datacart is the dataset that follows you around VegBank collecting plots and other items.<br />
  Add or remove datacart items by checking or unchecking their datacart checkboxes.</p>

<br />
 <!-- this is guaranteed not to be null, so we can use .toString() with it : your datacart ID is: <%= lngDatacartId %> -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_userdataset_pk" wparam="<%= lngDatacartId.toString() %>" pager="false"/>

<logic:notEmpty name="userdataset-BEANLIST">

<div style="clear:both; display: block;">
    <table class="noborder">
    <tr id="tut_downloadcart">
        <td align="center">
            <a href="@web_context@forms/download.jsp?dsId=<%=lngDatacartId%>" class="nobg"><img src="@images_link@downarrow_green.gif" border="0"></a>
        </td>
        <td>
            <strong><a href="@web_context@forms/download.jsp?dsId=<%=lngDatacartId%>">DOWNLOAD this dataset to a file</a></strong>
        </td>
    </tr>

    <tr>
        <td align="center">
            <a href="@web_context@views/datacart_detail.jsp?delta=dropall&deltaItems=<%= lngDatacartId.toString() %>" class="nobg"><img 
                src="@images_link@cart_star_off_dark.gif" border="0" id="datacart-results-icon"/></a>
        </td>
        <td>
            <strong><a href="@web_context@views/datacart_detail.jsp?delta=dropall&deltaItems=<%= lngDatacartId.toString() %>">PURGE all items from this dataset</a></strong><br>
            <span class="instructions">WARNING: dropping all items cannot be undone</span>
        </td>
    </tr>
    <tr><td colspan="2"><!-- query by datacart -->
      <strong>Query using the datacart:</strong><br/>
      <a href="@get_link@std/observation/<%= lngDatacartId.toString() %>?where=where_datacart_obs_hasplants&criteriaAsText=Plots+with+plants+in+your+datacart">
        Query for plots matching plants in your datacart
      </a> <br/>
      <a href="@get_link@std/observation/<%= lngDatacartId.toString() %>?where=where_datacart_obs_hascomms&criteriaAsText=Plots+interpreted+as+communities+in+your+datacart">
        Query for plots interpreted as a community in your datacart
      </a> <br/>
      
      
    
    </td></tr>
    </table>
    </div>
</logic:notEmpty>

<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br/><hr/><br/>
<h2>Other Datasets</h2>
<p class="instructions">
Switch the datacart to another dataset here.</p>
<!-- set perPage to -1 -->
<bean:define id="perPage" value="-1" />
<!-- sets to force all datasets to be displayed: -->
<bean:define id="forceAll" value="true" />
  <!-- do the right get -->
 <%@ include file="includeviews/sub_getuserdatasets.jsp" %>
  <!-- display stuff -->
<%@ include file="includeviews/sub_userdataset_summary.jsp" %>

          @webpage_footer_html@
