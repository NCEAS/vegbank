@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- $Id: userdataset_detail.jsp,v 1.17 2005-07-28 21:04:01 mlee Exp $ -->
<!-- purpose : show user's datasets.  THIS WILL NOT show datasets for a user
   other than the current user, nor will it show a dataset for someone who isn't logged in-->
<!-- we should prompt users to log in to access this page -->
 
<TITLE>View Your VegBank Datasets - Detail</TITLE>

  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
  
<h1>View Your VegBank Datasets - Detail</h1>
  <!-- do the right get -->
 <%@ include file="includeviews/sub_getuserdatasets.jsp" %>


<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br />



          @webpage_footer_html@
