
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




 
<TITLE>View VegBank Data: Stratum Methods - Detail</TITLE>


  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Stratum Methods</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="stratummethod" select="stratummethod" beanName="map" pager="true"  
          allowOrderBy="true" xwhereEnable="true"/>

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


<tr  class='@nextcolorclass@'><td class="datalabel">Count of Observations using this method</td>
<td>
<logic:empty name="onerowofstratummethod" property="d_obscount">0</logic:empty>
<logic:notEmpty name="onerowofstratummethod" property="d_obscount">
  <logic:equal value="0" name="onerowofstratummethod" property="d_obscount">0</logic:equal>
  <logic:notEqual value="0" name="onerowofstratummethod" property="d_obscount">
    
    <bean:define id="critAsTxt">
    With Stratum Method: <bean:write name="onerowofstratummethod" property="stratummethodname"/>
    </bean:define>
    <%  
        /* create a map of parameters to pass to the new link: */
        java.util.HashMap params = new java.util.HashMap();
        params.put("wparam", stratummethod_pk);
        params.put("where", "where_stratummethod_pk");
        params.put("criteriaAsText", critAsTxt);
        pageContext.setAttribute("paramsName", params);
    %>
    
    <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
      <bean:write  name="onerowofstratummethod" property="d_obscount" />
    </html:link>
  </logic:notEqual>
</logic:notEmpty>

</td></tr>  


<TR><TD colspan="2" class="datalabel">Stratum Types:</TD></TR>
<TR><TD COLSPAN="2">
<table class="leftrightborders sortable" cellpadding="2" id="stratumTypesFor_<bean:write name='stratummethod_pk' />">
<vegbank:get id="stratumtype" select="stratumtype" beanName="map" pager="false" where="where_stratummethod_pk" wparam="stratummethod_pk" perPage="-1" />
<logic:empty name="stratumtype-BEANLIST">
<tr><td class='@nextcolorclass@'> There are no stratum Types associated with this method.</td></tr>
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
