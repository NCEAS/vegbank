@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

 
<TITLE>View VegBank Data: Parties - Summary</TITLE>
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Parties</h2>

<!-- add search box -->
<bean:define id="entityToSearch" value="party" />
<bean:define id="SearchInstructions" value="(enter a name, organization, etc.)" /> 
<%@ include file="includeviews/sub_searchEntity.jsp" %>


       <logic:notPresent parameter="orderBy">
            <!-- set default sorting -->
            <bean:define id="orderBy" value="xorderby_surname" />
       </logic:notPresent>
        
<vegbank:get id="party" select="party" beanName="map" pager="true" 
 allowOrderBy="true" xwhereEnable="true"/>

<!--Where statement removed from preceding: -->
<logic:empty name="party-BEANLIST">
<p>  Sorry, no parties found.</p>
</logic:empty>
<logic:notEmpty name="party-BEANLIST">
<table cellpadding="2" class="leftrightborders">
<tr>
<th>More</th>
                  <%@ include file="autogen/party_summary_head.jsp" %>
          <bean:define id="thisfield" value="d_obscount" />
      <bean:define id="fieldlabel">Plots</bean:define>
      <%@ include file="../includes/orderbythisfield.jsp" %>
    
                  
                  </tr>
<logic:iterate name="party-BEANLIST" id="onerowofparty">
<bean:define property="party_id" name="onerowofparty" id="party_pk"/>
<tr class="@nextcolorclass@">
<td class="smallfield">
<a href="@get_link@detail/party/@subst_lt@bean:write name='onerowofparty' property='party_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/party_summary_data.jsp" %>
      <!-- plots -->
      <td>
       <logic:empty name="onerowofparty" property="d_obscount">0</logic:empty>
       <logic:notEmpty name="onerowofparty" property="d_obscount">
        <logic:equal name="onerowofparty" property="d_obscount" value="0">0</logic:equal>
        <logic:notEqual name="onerowofparty" property="d_obscount" value="0">

          <bean:define id="critAsTxt">
          with <bean:write name="onerowofparty" property="party_id_transl"/> as Contributor.
          </bean:define>
          <%  
              /* create a map of parameters to pass to the new link: */
              java.util.HashMap params = new java.util.HashMap();
              params.put("wparam", party_pk);
              params.put("where", "where_obs_allparty");
              params.put("criteriaAsText", critAsTxt);
              pageContext.setAttribute("paramsName", params);
          %>

          <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
            <bean:write name="onerowofparty" property="d_obscount" />
          </html:link>

        </logic:notEqual>
       </logic:notEmpty> 
      </td>
     </tr>

<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_party_pk" wparam="party_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br/>
<vegbank:pager/>


          @webpage_footer_html@
