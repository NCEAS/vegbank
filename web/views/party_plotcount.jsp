
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<TITLE>Browse VegBank Data by Party</TITLE>
 
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>Browse Plots By Party</h2>




        <vegbank:get id="browseparty" select="browseparty" 
        beanName="map" pager="true"  xwhereEnable="false" />

<%@ include file="includeviews/sub_party_plotcount.jsp" %>

<br />
<vegbank:pager />

          @webpage_footer_html@
