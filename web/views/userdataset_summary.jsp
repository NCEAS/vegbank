@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
    @ajax_js_include@
    @datacart_js_include@

<!-- $Id: userdataset_summary.jsp,v 1.5 2005-08-11 20:20:42 mlee Exp $ -->
<!-- purpose : show user's datasets, either all of them (mode=all in URL) or only certain ones:

  mode=ac is to show accessioncodes
  mode=id is to show by primary key of dataset(s) 
 BOTH of the above are comma separated, and currently accessionCodes need to be surrounded by single quotes -->


 
<TITLE>View Your VegBank Datasets - Summary</TITLE>

  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<br/>  
<h1>View Your VegBank Datasets - Summary</h1>
<br/>
  <!-- do the right get -->
 <%@ include file="includeviews/sub_getuserdatasets.jsp" %>

 <!-- display the right stuff -->
<%@ include file="includeviews/sub_userdataset_summary.jsp" %>

<br />



          @webpage_footer_html@
