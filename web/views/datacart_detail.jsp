@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
<!-- purpose : show datacart (singular selected dataset) for the user -->
 
<TITLE>Your Datacart</TITLE>

  
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
<div style="display: block;">
<strong>
    <a href="@web_context@forms/download.jsp?dsId=<%=lngDatacartId%>">DOWNLOAD this dataset to a file</a> 
</strong>
</div>
<a href="@web_context@forms/download.jsp?dsId=<%=lngDatacartId%>" class="nobg"><img src="@images_link@downarrow_green.gif" border="0"></a>

<br />

 <!-- this is guaranteed not to be null, so we can use .toString() with it : your datacart ID is: <%= lngDatacartId %> -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_userdataset_pk" wparam="<%= lngDatacartId.toString() %>" pager="false"/>
    
<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br/><hr/><br/>
<h2>Other Datasets</h2>
<p class="instructions">
Switch the datacart to another dataset here.</p>
<p>COMING SOON!</p>

          @webpage_footer_html@
