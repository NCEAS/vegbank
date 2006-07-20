@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
 <TITLE>View VegBank Data: Soil Taxa - Summary</TITLE>
        <%@ include file="includeviews/inlinestyles.jsp" %>
 @webpage_masthead_html@ 

 @possibly_center@
 <h2>View VegBank Soil Taxa</h2>

<!-- add search box -->
<bean:define id="entityToSearch" value="soiltaxon" />
<bean:define id="NameOfEntityToPresent" value="soil taxon" />
<bean:define id="SearchInstructions" value="(enter a name, or part of a name)" /> 
<bean:define id="alternateSearchInputs">
  <input type="hidden" name="xwhereKey" value="xwhere_match" />
  <input type="hidden" name="where" value="where_simple" />
  <input type="hidden" name="xwhereParams_custom_1" value="soilname" /><!-- name of field to search -->
</bean:define>


<%@ include file="includeviews/sub_searchEntity.jsp" %>
  <logic:notPresent parameter="perPage">
    <bean:define id="perPage">25</bean:define>
  </logic:notPresent> 
  <vegbank:get id="soiltaxon" select="soiltaxon" beanName="map" pager="true" xwhereEnable="true"/>
 
  <vegbank:pager />
  <logic:empty name="soiltaxon-BEANLIST">
    <p>  Sorry, no Soil Taxa found.</p>
  </logic:empty>
  <logic:notEmpty name="soiltaxon-BEANLIST">
    <table class="leftrightborders" cellpadding="2">
      <tr>
         <%@ include file="autogen/soiltaxon_summary_head.jsp" %>
         <th title="A link to children of this soil taxon">Soil Children Count</th>
      </tr>
      <logic:iterate id="onerowofsoiltaxon" name="soiltaxon-BEANLIST">
        <bean:define id="soiltaxon_pk" name="onerowofsoiltaxon" property="soiltaxon_id"></bean:define>
        <tr class="@nextcolorclass@">
           <%@ include file="autogen/soiltaxon_summary_data.jsp" %>
           <td><a href="@views_link@soiltaxon_summary.jsp?wparam=<bean:write name='soiltaxon_pk' />&where=where_soil_parentis"><bean:write name="onerowofsoiltaxon" property="countsoilchildren" /></a></td>
        </tr>
        
      </logic:iterate>
    </table>
  </logic:notEmpty>
  <br></br>
  <vegbank:pager />
     
@webpage_footer_html@