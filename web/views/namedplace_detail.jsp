
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>View VegBank Data: Named Places - Detail</TITLE>



      @webpage_masthead_html@
      @possibly_center@
        <h2>View VegBank Named Places</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="namedplace" select="namedplace" beanName="map" pager="true" xwhereEnable="true"/>
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="namedplace-BEANLIST">
<p>  Sorry, no Named Places found.</p>
</logic:empty>
<logic:notEmpty name="namedplace-BEANLIST">
<logic:iterate id="onerowofnamedplace" name="namedplace-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
<tr>
  <th class="major_smaller" colspan="4"><bean:write	name="onerowofnamedplace" property="placename" /></th>
</tr>
        <%@ include file="autogen/namedplace_detail_data.jsp" %>
        <bean:define id="namedplace_pk" name="onerowofnamedplace" property="namedplace_id" />
<!-- plots in this named place -->
<vegbank:get id="plot" select="place_count" beanName="map" pager="false" perPage="-1" 
  where="where_namedplace_pk" wparam="namedplace_pk" />

<tr class='@nextcolorclass@'><td class="datalabel">Plots in this place</td>
<td>
<logic:empty name="plot-BEAN">
-none-
</logic:empty>
<logic:notEmpty name="plot-BEAN">
<bean:write name="plot-BEAN" property="count_places" />
<logic:notEqual name="plot-BEAN" property="count_places" value="0">
  <bean:define id="critAsTxt">
  In the <bean:write name="onerowofnamedplace" property="placesystem"/>: <bean:write name="onerowofnamedplace" property="placename"/>
  </bean:define>
  <%  
      /* create a map of parameters to pass to the new link: */
      java.util.HashMap params = new java.util.HashMap();
      params.put("wparam", namedplace_pk);
      params.put("where", "where_place_complex");
      params.put("criteriaAsText", critAsTxt);
      pageContext.setAttribute("paramsName", params);
  %>
  
  <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >View 
  observation(s)</html:link>

</logic:notEqual>
</logic:notEmpty>
</td></tr>

<!-- children and parent places -->


<vegbank:get id="namedplacecorrelation_a" select="namedplacecorrelation" where="where_childplace" wparam="namedplace_pk"
  beanName="map" pager="false" perPage="-1" xwhereEnable="false" />
<logic:notEmpty name="namedplacecorrelation_a-BEANLIST">
<TR><TD colspan="9">
<table class="leftrightborders"><tr><th>parent places</th><th>convergence</th></tr>
 <logic:iterate id="onerowofnamedplacecorrelation_a" name="namedplacecorrelation_a-BEANLIST">
   <tr class='@nextcolorclass@'><td>
   <a href='@get_link@std/namedplace/<bean:write name="onerowofnamedplacecorrelation_a" property="parentplace_id" />'><bean:write name="onerowofnamedplacecorrelation_a" property="parentplace_id_transl" /></a></td>
   <td><bean:write name="onerowofnamedplacecorrelation_a" property="placeconvergence" /></td>
   </tr>
 </logic:iterate>
</table>

</TD></TR>
</logic:notEmpty>


<vegbank:get id="namedplacecorrelation_b" select="namedplacecorrelation" where="where_parentplace" wparam="namedplace_pk"
  beanName="map" pager="false" perPage="-1" xwhereEnable="false" />
<logic:notEmpty name="namedplacecorrelation_b-BEANLIST">
<TR><TD colspan="9">
<table class="leftrightborders"><tr><th>convergence</th><th>child places</th></tr>
 <logic:iterate id="onerowofnamedplacecorrelation_b" name="namedplacecorrelation_b-BEANLIST">
   <tr class='@nextcolorclass@'><td><bean:write name="onerowofnamedplacecorrelation_b" property="placeconvergence" /></td>
   <td><a href='@get_link@std/namedplace/<bean:write name="onerowofnamedplacecorrelation_b" property="childplace_id" />'><bean:write name="onerowofnamedplacecorrelation_b" property="childplace_id_transl" /></a></td>
   </tr>
 </logic:iterate>

</table>

</TD></TR>
</logic:notEmpty>


<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_namedplace_pk" wparam="namedplace_pk" />-->

</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
