@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




 
<TITLE>View Data in VegBank : Public Dataset(s)</TITLE>



  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  <h2>View Public VegBank Datasets</h2>
<vegbank:get select="userdataset_nonprivate" beanName="map" where="where_usr_email" pager="true"/>

<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br />
          @webpage_footer_html@
