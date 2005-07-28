@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<TITLE>View VegBank Data: Plant Names - Summary</TITLE>
      <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@
      @possibly_center@
        <h2>View VegBank Plant Names</h2>
        <vegbank:get id="plantname" select="plantname" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="plantname-BEANLIST">
<p>  Sorry, no Plant Names found.</p>
</logic:empty>
<logic:notEmpty name="plantname-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
                  <%@ include file="autogen/plantname_summary_head.jsp" %></tr>
<logic:iterate id="onerowofplantname" name="plantname-BEANLIST">
<tr class="@nextcolorclass@">
                       <%@ include file="autogen/plantname_summary_data.jsp" %>
                       </tr>
<bean:define id="plantname_pk" name="onerowofplantname" property="plantname_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_plantname_pk" wparam="plantname_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
