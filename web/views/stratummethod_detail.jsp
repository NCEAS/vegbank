
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




 
<TITLE>View VegBank Data: Stratum Methods - Detail</TITLE>


  
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Stratum Methods</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="stratummethod" select="stratummethod" beanName="map" pager="true"  xwhereEnable="true"/>
<!--Where statement removed from preceding: -->

<vegbank:pager />
<logic:empty name="stratummethod-BEANLIST">
<p>  Sorry, no stratum methods found.</p>
</logic:empty>
<logic:notEmpty name="stratummethod-BEANLIST">
<logic:iterate id="onerowofstratummethod" name="stratummethod-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
        <tr><th class="major" colspan="4"><bean:write name="onerowofstratummethod" property="stratummethodname" /></th></tr>
        <%@ include file="autogen/stratummethod_detail_data.jsp" %>
        <bean:define id="stratummethod_pk" name="onerowofstratummethod" property="stratummethod_id" />
<!-- custom bits:
-->


<vegbank:get id="observation" select="observation_count" beanName="map" pager="false" perPage="-1" 
  where="where_stratummethod_pk" wparam="stratummethod_pk" />

<tr  class='@nextcolorclass@'><td class="datalabel">Count of Observations using this method</td>
<td>
<logic:empty name="observation-BEAN">
-none-
</logic:empty>
<logic:notEmpty name="observation-BEAN">
<bean:write name="observation-BEAN" property="count_observations" />
<logic:notEqual name="observation-BEAN" property="count_observations" value="0">
<a href="@get_link@summary/observation/<bean:write name='stratummethod_pk' />?where=where_stratummethod_pk">View observations</a>
</logic:notEqual>
</logic:notEmpty>

  

<!--Insert a nested get statement here:
   example:   

<vegbankget id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_stratummethod_pk" wparam="stratummethod_pk" />-->

<TR><TD COLSPAN="2">
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="8">Stratum Types:</th></tr>
<vegbank:get id="stratumtype" select="stratumtype" beanName="map" pager="false" where="where_stratummethod_pk" wparam="stratummethod_pk" perPage="-1" />
<logic:empty name="stratumtype-BEANLIST">
<tr><td class='@nextcolorclass@'>  Sorry, no Stratum Types found.</td></tr>
</logic:empty>
<logic:notEmpty name="stratumtype-BEANLIST">

<tr class='@nextcolorclass@'>
<%@ include file="autogen/stratumtype_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofstratumtype" name="stratumtype-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/stratumtype_summary_data.jsp" %>
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
