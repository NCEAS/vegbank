@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
 <TITLE>View VegBank Data: User Defined Variables - Detail</TITLE>
<%@ include file="includeviews/inlinestyles.jsp" %>
@webpage_masthead_html@ 

      @possibly_center@
        <h2>View VegBank User Defined Variables</h2>
<vegbank:get id="userdefined" select="userdefined" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager />
<logic:empty name="userdefined-BEANLIST">
<p>  Sorry, no User Defined Variables found.</p>
</logic:empty>
<logic:notEmpty name="userdefined-BEANLIST">
<logic:iterate id="onerowofuserdefined" name="userdefined-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
        <%@ include file="autogen/userdefined_detail_data.jsp" %>
        <bean:define id="userdefined_pk" name="onerowofuserdefined" property="userdefined_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_userdefined_pk" wparam="userdefined_pk" />-->
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />
     
          @webpage_footer_html@
