@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

 
<TITLE>View VegBank Data: Parties - Summary</TITLE>
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Parties</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        
<vegbank:get id="party" select="party" beanName="map" pager="true" xwhereEnable="true"/>
<!--Where statement removed from preceding: -->
<logic:empty name="party-BEANLIST">
<p>  Sorry, no parties found.</p>
</logic:empty>
<logic:notEmpty name="party-BEANLIST">
<table cellpadding="2" class="leftrightborders">
<tr>
<th>More</th>
                  <%@ include file="autogen/party_summary_head.jsp" %></tr>
<logic:iterate name="party-BEANLIST" id="onerowofparty">
<tr class="@nextcolorclass@">
<td class="largefield">
<a href="@get_link@detail/party/@subst_lt@bean:write name='onerowofparty' property='party_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/party_summary_data.jsp" %>
                       </tr>
<bean:define property="party_id" name="onerowofparty" id="party_pk"/>
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_party_pk" wparam="party_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br/>
<vegbank:pager/>


          @webpage_footer_html@
