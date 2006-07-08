@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

 
<TITLE>View VegBank Data: Parties - Summary</TITLE>
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Parties</h2>

<!-- ############################################### -->
<!--            start of search box                  --> 
<!-- ############################################### -->

<!-- % String searchString = request.getParameter("xwhereParams"); % -->
<!-- collect previous info on parties: -->
<bean:parameter id="beansearchString" value="" name="xwhereParams" />
<bean:parameter id="beanxwhereMatchAny" value="" name="xwhereMatchAny" />
<bean:parameter id="beanxwhereMatchWholeWords" value="" name="xwhereMatchWholeWords"/>

<form action="@views_link@party_summary.jsp" method="get">
    <table cellpadding="0" cellspacing="0" border="0" bgcolor="#DDDDDD">
    <tr>
        <td><img src="@image_server@uplt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@uprt3.gif"/></td>
    </tr>

    <tr>
        <td></td>
        <td><span id="searchPartyShown">
            <!-- link to hide this: -->
            
            <input type="hidden" name="clearSearch" value="" />
            <input type="hidden" name="xwhereKey" value="xwhere_kw_match" />
            <input type="hidden" name="where" value="where_keywords_pk_in" />
            <input type="hidden" name="wparam" value="party__party" />
            <input type="hidden" name="xwhereSearch" value="true" />
            <span class="greytext">
            &nbsp; Search for a party (enter a name, organization, etc.):
            &nbsp;&nbsp;
              <a href="#" onclick="showorhidediv('searchPartyShown');showorhidediv('searchPartyHidden');return false;">
                &lt;&lt;Hide search
              </a>
            </span>
            <br />
             <input type="text" name="xwhereParams" size="30" value="<bean:write name='beansearchString' />"/>
             <html:submit value="search" />
       
       <!--</td>
        </tr>
        <tr><td></td><td align="right"> -->
        
        <div align="right">
             <input type="checkbox" name="xwhereMatchAny" <logic:equal value='on' name='beanxwhereMatchAny' >checked="checked" </logic:equal> />
                Match any word
        </div>
       
        <!--
        </td>
        <td></td>
    </tr>
        <tr><td></td><td align="right">
        -->
        <div align="right">
        <!-- check this box if this search is a wparam search and it was checked before, or if it isn't a search: -->
              <input type="checkbox" 
                     name="xwhereMatchWholeWords" 
                     <logic:empty  name='beansearchString' > checked="checked" </logic:empty> 
                     <logic:equal value='on' name='beanxwhereMatchWholeWords' >checked="checked" </logic:equal>  
                />
                Match whole words only
          </div>
       <!-- end of shown search span --></span>
       
       <span id="searchPartyHidden" style="display:none" class="greytext">
           <a href="#" onclick="showorhidediv('searchPartyShown');showorhidediv('searchPartyHidden');return false;">
             Search for a party &gt;&gt;
           </a>
        </span></td>
            <td></td> 
            
    </tr>
    <tr>
        <td><img src="@image_server@lwlt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@lwrt3.gif"/></td>
    </tr>
    
    </table>
</form>
<br />


<!-- ############################################### -->
<!--              end of search box                  --> 
<!-- ############################################### -->


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
<td class="largefield">
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
