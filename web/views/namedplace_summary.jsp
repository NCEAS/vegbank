@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>View VegBank Data: Named Places - Summary</TITLE>


  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Named Places</h2>


<!-- add search box -->
<bean:define id="entityToSearch" value="namedplace" />
<bean:define id="NameOfEntityToPresent" value="named place" />
<bean:define id="SearchInstructions" value="(enter a name, or part of a name)" /> 
<bean:define id="alternateSearchInputs">
  <input type="hidden" name="xwhereKey" value="xwhere_match" />
  <input type="hidden" name="where" value="where_simple" />
  <input type="hidden" name="xwhereParams_custom_1" value="placename" /><!-- name of field to search -->
</bean:define>
<%@ include file="includeviews/sub_searchEntity.jsp" %>


  <logic:notPresent parameter="perPage">
    <bean:define id="perPage">25</bean:define>
  </logic:notPresent> 



        <vegbank:get id="namedplace" select="namedplace" beanName="map" pager="true" xwhereEnable="true"  />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="namedplace-BEANLIST">
<p>  Sorry, no Named Places found.</p>
</logic:empty>
<logic:notEmpty name="namedplace-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/namedplace_summary_head.jsp" %>
                  
                  </tr>
<logic:iterate id="onerowofnamedplace" name="namedplace-BEANLIST">
<tr class="@nextcolorclass@">
<td class="smallfield">
<a href="@get_link@detail/namedplace/@subst_lt@bean:write name='onerowofnamedplace' property='namedplace_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/namedplace_summary_data.jsp" %>
<bean:define id="namedplace_pk" name="onerowofnamedplace" property="namedplace_id" />

                       </tr>

<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_namedplace_pk" wparam="namedplace_pk" />-->

</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
