@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 <%@ include file="/views/includeviews/inlinestyles.jsp" %> 
  <title>
   Search for Plots using a Dataset
  </title>


<!-- VegBank Header -->
  @webpage_masthead_html@
  
 <h1>Search for plots using a dataset</h1> 

<p class="instructions">Use this page to search
for plots using items that you have added to a dataset.
</p>

<p class="instructions">You must be a registered user who is logged in to use this feature.</p>
  <logic:equal parameter="searchby" value="comm">
    <bean:define id="where" value="where_userdataset_hastable" />
    <bean:define id="wparam" value="commconcept" />
    
  </logic:equal>
  <logic:equal parameter="searchby" value="plant">
    <bean:define id="where" value="where_userdataset_hastable" />
    <bean:define id="wparam" value="plantconcept" />
    
  </logic:equal>
  
  <form name="datasetform">
  <%@ include file="/views/includeviews/sub_userdataset_picklist.jsp" %>
  <p>
  <logic:present name="successfulget">
    <!-- only put this button if there were user datasets retrieved -->
    <logic:equal parameter="searchby" value="comm">
      <input type="button" value="Find Plots" onclick="document.location='@get_link@std/observation/' + document.datasetform.userdataset_id.value + '?where=where_datacart_obs_hascomms&criteriaAsText=Plots+with+communities+in+a+dataset';"
      /> With communities in the selected dataset.
      <bean:define id="searchbydone" value="true" />
    </logic:equal>  
    <logic:notPresent name="searchbydone">
      <!-- default -->
      <input type="button" value="Find Plots" 
       onclick="document.location='@get_link@std/observation/' + document.datasetform.userdataset_id.value + '?where=where_datacart_obs_hasplants&criteriaAsText=Plots+with+plants+in+a+dataset';"
      /> With plants in the selected dataset.
    </logic:notPresent>
    <br/>
    <input type="button" value="View Selected Dataset"
    onclick="document.location='@get_link@detail/userdataset/' + document.datasetform.userdataset_id.value;" />
  </logic:present> 
  </p>
  </form>
  
 


 
@webpage_footer_html@


