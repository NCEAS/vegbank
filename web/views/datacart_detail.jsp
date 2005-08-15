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

<logic:empty name="userdataset-BEANLIST">
  Your datacart is empty.  Click items after searching for or viewing data to add them to your datacart.
</logic:empty>

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
    
    
    </table>
    </div>

<!-- tell included file to show datacart specific stuff -->
<bean:define id="thisisdatacart" value="true" type="java.lang.String" />
<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<hr/>
<h2>Dataset Manager</h2>
<p>
<!-- query by datacart -->
     
      <h3>Advanced Datacart Features:</h3>   
    </p>
      <ul>
      <li class="evenrow">
          <span class="bright"><strong>NEW!</strong></span> You can view a constancy table for plots in your datacart.<br/>
          <logic:present name="datacart_contains_observation">
            <a href="@get_link@constancyanalysis/userdataset/<%= lngDatacartId.toString() %>">
                  &raquo; View Constancy Table</a><br/> 
            
            <a href="@forms_link@ConstancyTableRequest.jsp">&raquo;  More Information / View Constancy Table of multiple datasets here.</a> 
            
         
          </logic:present>
          <logic:notPresent name="datacart_contains_observation"> 
            You have <strong>no plots</strong> in your datacart.<br/> 
            <a href="@plotquery_page_advanced@">&raquo; Search for plots here.</a>
          </logic:notPresent>
      
      </li>


      <li class="oddrow">
         <span class="bright"><strong>NEW!</strong></span> You can view a map of the plots in your datacart.<br/>
         <logic:present name="datacart_contains_observation">
           <a target="_blank" href="@views_link@userdataset_map.jsp?wparam=<%= lngDatacartId.toString() %>">
                &raquo; Map datacart plots</a> <br/>
           
           <span class="small">
                  <a href="@forms_link@MapDatasets.jsp"> &raquo; More Information / Map multiple datasets here.</a> 
                   
           </span>
         </logic:present>
         <logic:notPresent name="datacart_contains_observation"> 
           You have <strong>no plots</strong> in your datacart. <br/> 
           <a href="@plotquery_page_advanced@"> &raquo;  Add plots here.</a>
         </logic:notPresent>
         
      </li>
      
      
      <li class="evenrow">     
      
            Search for plots with plants in your datacart<br/>
          <logic:present name="datacart_contains_plantconcept"> 
             <a href="@get_link@std/observation/<%= lngDatacartId.toString() %>?where=where_datacart_obs_hasplants&criteriaAsText=Plots+with+plants+in+your+datacart">
               &raquo; Search
             </a> 
          </logic:present>
         
          <logic:notPresent name="datacart_contains_plantconcept"> 
             You have <strong>no plants</strong> in your datacart.  
            <br/> <a href="@forms_link@PlantQuery.jsp"> &raquo; Add plants here.</a>
          </logic:notPresent>
          
          </li>
      
      <li class="oddrow">
          Search for plots interpreted as a community in your datacart<br/>
          <logic:present name="datacart_contains_commconcept">
          <a href="@get_link@std/observation/<%= lngDatacartId.toString() %>?where=where_datacart_obs_hascomms&criteriaAsText=Plots+interpreted+as+communities+in+your+datacart">
            &raquo; Search
          </a> 
          </logic:present>
          <logic:notPresent name="datacart_contains_commconcept"> 
             You have <strong>no communities</strong> in your datacart. <br/> 
             <a href="@forms_link@CommQuery.jsp"> &raquo; Add communities here.</a>
          
          </logic:notPresent>
      </li>
      
      
   


      <li class="evenrow">Save the datacart<br/>
        You can save this datacart and start adding more items to a new one, if you are logged in. <br/>
            <% if ( strWebUserId != "-1" ) {  %>  
              <!-- you are logged in --> 
              
                <a href="@views_link@datacart_detail.jsp?delta=set&deltaItems=-1"> &raquo; Create a new datacart.</a>
                This saves the current datacart to your account. 
               
            <% } %>
            <% if ( strWebUserId == "-1" ) {  %>  
                    You are not logged in. 
                    <br/> <a href="@general_link@login.jsp"> &raquo; Login Here.</a> 
            <% } %>
      
         </logic:notEmpty>
       </li>
   </ul>


<h3>Other Datasets</h3>
<p class="instructions">
You can activate another dataset to make it the current datacart by pressing the "activate" button
next to another dataset, if you have another dataset.  You must be logged in to do so.</p>
<!-- set perPage to -1 -->
<bean:define id="perPage" value="-1" />
<!-- sets to force all datasets to be displayed, no pagination: -->
<bean:define id="forceAll" value="true" />
  <!-- do the right get -->
 <%@ include file="includeviews/sub_getuserdatasets.jsp" %>
  <!-- display stuff -->
<%@ include file="includeviews/sub_userdataset_summary.jsp" %>

          @webpage_footer_html@
