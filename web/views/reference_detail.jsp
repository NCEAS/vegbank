@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<TITLE>View VegBank Data: references - Detail</TITLE>

@webpage_masthead_html@  @possibly_center@
<h2>View VegBank References</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        
<vegbank:get id="reference" select="reference" beanName="map" pager="true"/>
<!--Where statement removed from preceding: -->
<vegbank:pager/>
<logic:empty name="reference-BEANLIST">
<p>  Sorry, no references found.</p>
</logic:empty>
<logic:notEmpty name="reference-BEANLIST">
<logic:iterate name="reference-BEANLIST" id="onerowofreference">
<!-- iterate over all records in set : new table for each -->
<table cellpadding="2" class="leftrightborders">
<tr><th class="major" colspan="4"><bean:write name="onerowofreference" property="shortname" /></th></tr>
        <%@ include file="autogen/reference_detail_data.jsp" %>
        <bean:define property="reference_id" name="onerowofreference" id="reference_pk"/>
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_reference_pk" wparam="reference_pk" />-->
<TR><TD COLSPAN="2">
<vegbank:get id="referencecontributor" select="referencecontributor" beanName="map" pager="false" where="where_reference_pk" wparam="reference_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="9">Reference Contributors:</th></tr>
<logic:empty name="referencecontributor-BEANLIST">
<tr><td class="@nextcolorclass@">  Sorry, no Reference Contributors found.</td></tr>
</logic:empty>
<logic:notEmpty name="referencecontributor-BEANLIST">
<tr>
<%@ include file="autogen/referencecontributor_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofreferencecontributor" name="referencecontributor-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/referencecontributor_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
</TD></TR>

</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br/>
<vegbank:pager/>


          @webpage_footer_html@
 
