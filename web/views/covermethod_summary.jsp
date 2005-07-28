@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




 
<TITLE>View VegBank Data: coverMethods - Summary</TITLE>


  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Cover Methods</h2>
          <logic:notPresent parameter="orderBy">
              <!-- set default sorting -->
              <bean:define id="orderBy" value="xorderby_covertype" />
          </logic:notPresent>
       
       <vegbank:get id="covermethod" select="covermethod" beanName="map" pager="true"  
        allowOrderBy="true" xwhereEnable="true"/>
        


<vegbank:pager />
<logic:empty name="covermethod-BEANLIST">
<p>  Sorry, no coverMethods found.</p>
</logic:empty>
<logic:notEmpty name="covermethod-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/covermethod_summary_head.jsp" %>
                  <!-- extra -->
                  <bean:define id="thisfield" value="d_obscount" />
                  <bean:define id="fieldlabel">Plots</bean:define>
                    <%@ include file="../includes/orderbythisfield.jsp" %>
                  </tr>
<logic:iterate id="onerowofcovermethod" name="covermethod-BEANLIST">
  <bean:define id="covermethod_pk" name="onerowofcovermethod" property="covermethod_id" />
<tr class="@nextcolorclass@">
<td class="sizetiny">
<a href="@get_link@detail/covermethod/@subst_lt@bean:write name='onerowofcovermethod' property='covermethod_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/covermethod_summary_data.jsp" %>
                 <td class="numeric">
                  <logic:empty name="onerowofcovermethod" property="d_obscount">0</logic:empty>
                  <logic:notEmpty name="onerowofcovermethod" property="d_obscount">
                     <logic:equal name="onerowofcovermethod" property="d_obscount" value="0">0</logic:equal>
                     <logic:notEqual name="onerowofcovermethod" property="d_obscount" value="0">
                          <bean:define id="critAsTxt">
                            with Cover Method: <bean:write name="onerowofcovermethod" property="covertype"/>
                          </bean:define>
                          <%  
                              /* create a map of parameters to pass to the new link: */
                              java.util.HashMap params = new java.util.HashMap();
                              params.put("wparam", covermethod_pk);
                              params.put("where", "where_covermethod_pk");
                              params.put("criteriaAsText", critAsTxt);
                              pageContext.setAttribute("paramsName", params);
                          %>
                          
                          <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" ><bean:write name="onerowofcovermethod" property="d_obscount" /></html:link>
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
