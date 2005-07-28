@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@






 
<TITLE>View Data in VegBank : commClass(s) : Summary</TITLE>


  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  <h2>View VegBank Community Classifications</h2>
<vegbank:get id="commclass" select="commclass" beanName="map" pager="true"/>
<vegbank:pager />

<%@ include file="includeviews/sub_commclass_summary.jsp" %>

<br /><vegbank:pager />
          @webpage_footer_html@
