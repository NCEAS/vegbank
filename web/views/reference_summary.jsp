@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<TITLE>View VegBank Data: references - Summary</TITLE>

<%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@  
@possibly_center@
<h2>View VegBank References</h2>

<!-- add search box -->
<bean:define id="entityToSearch" value="reference" />
<bean:define id="SearchInstructions" value="(enter a title, author, etc.)" /> 
<%@ include file="includeviews/sub_searchEntity.jsp" %>

        
<vegbank:get id="reference" select="reference" beanName="map" pager="true" xwhereEnable="true" allowOrderBy="true"/>
<!--Where statement removed from preceding: -->
<vegbank:pager/>
<logic:empty name="reference-BEANLIST">
<p>  Sorry, no references found.</p>
</logic:empty>
<logic:notEmpty name="reference-BEANLIST">
<table cellpadding="2" class="leftrightborders">
<tr>
<th>More</th>
                  <%@ include file="autogen/reference_summary_head.jsp" %></tr>
<logic:iterate name="reference-BEANLIST" id="onerowofreference">
<tr class="@nextcolorclass@">
<td class="smallfield">
<a href="@get_link@detail/reference/@subst_lt@bean:write name='onerowofreference' property='reference_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/reference_summary_data.jsp" %>
                       </tr>
<bean:define property="reference_id" name="onerowofreference" id="reference_pk"/>

</logic:iterate>
</table>
</logic:notEmpty>
<br/>
<vegbank:pager/>


          @webpage_footer_html@
