@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- purpose : show user's datacart (singular selected dataset)

-->
 
<TITLE>View Your VegBank DATACART - Detail</TITLE>

  
 @webpage_masthead_html@ 
  @possibly_center@  
  
  <!-- get user ID -->
<%@ include file="includeviews/sub_getwebuserid.jsp" %>
  
<h1>View Your VegBank Datacart</h1>
<p class="instructions">Your datacart is a set of plots (and perhaps other items too) 
  that you are interested in.  You can add plots to your datacart by selecting or unselected 
  checks when you are viewing a plot in VegBank, and also below: </p>


 <% Long lngDatacartID = DatasetUtility.getDatacartId(request.getSession());  ; %>
 <!-- this is guaranteed not to be null, so we can use .toString() with it -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_userdataset_pk" wparam="<%= lngDatacartID.toString() %>" pager="false"/>
    
<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br/><hr/><br/>
<h2>Change the datacart to a different dataset</h2>
<p class="instructions">
You can switch the datacart to another dataset here:</p>
<p>COMING soon!</p>

          @webpage_footer_html@
