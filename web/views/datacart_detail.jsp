@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
<!-- purpose : show datacart (singular selected dataset) for the user -->
 
<TITLE>Your Datacart</TITLE>

  
 @webpage_masthead_html@ 
  @possibly_center@  
  
  <!-- get user ID -->
<%@ include file="includeviews/sub_getwebuserid.jsp" %>
  
<h1>Your Datacart</h1>
<p class="instructions">
  Your datacart is the dataset that follows you around VegBank collecting plots and other items.<br />
  Add or remove datacart items by checking or unchecking their datacart checkboxes.</p>

<p><strong>COMING SOON: download items in this dataset.</strong>
<br />


 <% Long lngDatacartID = DatasetUtility.getDatacartId(request.getSession()); %>
 <!-- this is guaranteed not to be null, so we can use .toString() with it : your datacart ID is: <%= lngDatacartID %> -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_userdataset_pk" wparam="<%= lngDatacartID.toString() %>" pager="false"/>
    
<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br/><hr/><br/>
<h2>Other Datasets</h2>
<p class="instructions">
Switch the datacart to another dataset here.</p>
<p>COMING SOON!</p>

          @webpage_footer_html@
