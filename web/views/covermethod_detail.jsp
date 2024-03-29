
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

  <!-- include script for sorting tables -->
  <script language="javascript" src="@includes_link@sort_table.js"></script>



 
<TITLE>View VegBank Data: Cover Methods - Detail</TITLE>


  
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


<tr class='@nextcolorclass@'><td class="datalabel">Count of Observations using this method</td>
<td>
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
<!--Insert a nested get statement here:
   example:   

<vegbankget id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_covermethod_pk" wparam="covermethod_pk" />-->
<TR><TD colspan="2" class="datalabel">Cover Indexes:</TD></TR>
<TR><TD COLSPAN="2">
<vegbank:get id="coverindex" select="coverindex" beanName="map" pager="false" where="where_covermethod_pk" wparam="covermethod_pk" perPage="-1" />
<table class="leftrightborders sortable" cellpadding="2" id="coverclassesfor_<bean:write name='covermethod_pk' />">
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
