@webpage_top_html@
<!-- RCSFile: $RCSfile: stratummethod_summary.jsp,v $ \ -->
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




 
<TITLE>View VegBank Data: Stratum Methods - Summary</TITLE>


  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Stratum Methods</h2>
<!-- add search box -->
<bean:define id="entityToSearch" value="stratummethod" />
<bean:define id="NameOfEntityToPresent" value="stratum method" />
<bean:define id="SearchInstructions" value="(enter a name, description, type)" /> 
<%@ include file="includeviews/sub_searchEntity.jsp" %>




        <vegbank:get id="stratummethod" select="stratummethod" beanName="map" pager="true"  
          allowOrderBy="true" xwhereEnable="true"/>

<vegbank:pager />
<logic:empty name="stratummethod-BEANLIST">
<p>  Sorry, no Stratum Methods found.</p>
</logic:empty>
<logic:notEmpty name="stratummethod-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/stratummethod_summary_head.jsp" %>
                 
                 
                  <bean:define id="thisfield" value="d_obscount" />
                                   <bean:define id="fieldlabel">Plots</bean:define>
                    <%@ include file="../includes/orderbythisfield.jsp" %>
                  </tr>
<logic:iterate id="onerowofstratummethod" name="stratummethod-BEANLIST">
<tr class="@nextcolorclass@">
<td class="smallfield">
<a href="@get_link@detail/stratummethod/@subst_lt@bean:write name='onerowofstratummethod' property='stratummethod_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/stratummethod_summary_data.jsp" %>
                      
<bean:define id="stratummethod_pk" name="onerowofstratummethod" property="stratummethod_id" />
<td class="numeric">
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

</td>

 </tr>
 
 
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
