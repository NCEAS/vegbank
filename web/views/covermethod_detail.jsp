
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




 
<TITLE>View VegBank Data: Cover Methods - Detail</TITLE>


  
 @webpage_masthead_html@ 
  @possibly_center@ 
<h2>View VegBank Cover Methods</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="covermethod" select="covermethod" beanName="map" pager="true"  xwhereEnable="true"/>
<!--Where statement removed from preceding: -->

<vegbank:pager />
<logic:empty name="covermethod-BEANLIST">
<p>  Sorry, no Cover Methods found.</p>
</logic:empty>
<logic:notEmpty name="covermethod-BEANLIST">
<logic:iterate id="onerowofcovermethod" name="covermethod-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
 <tr><th class="major_smaller" colspan="4"><bean:write name="onerowofcovermethod" property="covertype" /></th></tr>
 <%@ include file="autogen/covermethod_detail_data.jsp" %>
        <bean:define id="covermethod_pk" name="onerowofcovermethod" property="covermethod_id" />
<!-- custom bits:
-->


<vegbank:get id="observation" select="observation_count" beanName="map" pager="false" perPage="-1" 
  where="where_covermethod_pk" wparam="covermethod_pk" />

<tr class='@nextcolorclass@'><td class="datalabel">Count of Observations using this method</td>
<td>
<logic:empty name="observation-BEAN">
-none-
</logic:empty>
<logic:notEmpty name="observation-BEAN">
<bean:write name="observation-BEAN" property="count_observations" />
<logic:notEqual name="observation-BEAN" property="count_observations" value="0">
<a href="@get_link@simple/observation/<bean:write name='covermethod_pk' />?where=where_covermethod_pk">View observations</a>
</logic:notEqual>
</logic:notEmpty>

<!--Insert a nested get statement here:
   example:   

<vegbankget id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_covermethod_pk" wparam="covermethod_pk" />-->
<TR><TD COLSPAN="2">
<vegbank:get id="coverindex" select="coverindex" beanName="map" pager="false" where="where_covermethod_pk" wparam="covermethod_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="8">Cover Indexes:</th></tr>
<logic:empty name="coverindex-BEANLIST">
<tr><td class="@nextcolorclass@">  Sorry, no cover indexes found.</td></tr>
</logic:empty>
<logic:notEmpty name="coverindex-BEANLIST">
<tr>
<%@ include file="autogen/coverindex_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofcoverindex" name="coverindex-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/coverindex_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
</TD></TR>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
