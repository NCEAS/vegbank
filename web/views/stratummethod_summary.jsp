@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




 
<TITLE>View VegBank Data: stratumMethods - Summary</TITLE>


  
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Stratum Methods</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="stratummethod" select="stratummethod" beanName="map" pager="true"  
          allowOrderBy="true" xwhereEnable="true"/>

<vegbank:pager />
<logic:empty name="stratummethod-BEANLIST">
<p>  Sorry, no stratumMethods found.</p>
</logic:empty>
<logic:notEmpty name="stratummethod-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/stratummethod_summary_head.jsp" %>
                 
                 
                  <bean:define id="thisfield" value="dobscount" />
                                   <bean:define id="fieldlabel">Plots</bean:define>
                    <%@ include file="../includes/orderbythisfield.jsp" %>
                  </tr>
<logic:iterate id="onerowofstratummethod" name="stratummethod-BEANLIST">
<tr class="@nextcolorclass@">
<td class="largefield">
<a href="@get_link@detail/stratummethod/@subst_lt@bean:write name='onerowofstratummethod' property='stratummethod_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/stratummethod_summary_data.jsp" %>
                      
<bean:define id="stratummethod_pk" name="onerowofstratummethod" property="stratummethod_id" />
<td>
<logic:empty name="onerowofstratummethod" property="d_obscount">0</logic:empty>
<logic:notEmpty name="onerowofstratummethod" property="d_obscount">
  <logic:equal value="0" name="onerowofstratummethod" property="d_obscount">0</logic:equal>
  <logic:notEqual value="0" name="onerowofstratummethod" property="d_obscount">
    <a href="@get_link@summary/observation/<bean:write name='stratummethod_pk' />?where=where_stratummethod_pk"><bean:write  name="onerowofstratummethod" property="d_obscount" /></a>
  </logic:notEqual>
</logic:notEmpty>

</td>

 </tr>
 
 
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
