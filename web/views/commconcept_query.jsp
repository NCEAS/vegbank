@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>View VegBank Community Concepts - Summary</TITLE>



  
 @webpage_masthead_html@ 
  @possibly_center@ 

<h2>View Community Concepts - Summary</h2>
  <vegbank:get id="concept" select="commconcept_forquery" beanName="map" pager="true" xwhereEnable="true"  />
  
  <bean:parameter id="beanwparam" name="wparam" value="" />
<logic:empty name="concept-BEANLIST">
  <!-- try to append wildcard to plantName if not there already -->
  <logic:notMatch name="beanwparam" value="%">
    <!-- if user isn't using wildcards, add them if no plants were found -->
    <bean:define id="beanwparamwild1"><bean:write name="beanwparam" />%</bean:define>
    <bean:define id="beanwparamwild2">%<bean:write name="beanwparam" />%</bean:define>
  </logic:notMatch>
  <!-- try get again, but specify wparam here -->
  <vegbank:get id="concept" select="commconcept_forquery" 
    beanName="map" pager="true" xwhereEnable="true" wparam="beanwparamwild1" />
     
    <logic:empty name="concept-BEANLIST">
      <!-- try last time, with wildcard start and end -->
      <vegbank:get id="concept" select="commconcept_forquery" 
    beanName="map" pager="true" xwhereEnable="true" wparam="beanwparamwild2" />
     
    </logic:empty>
  
</logic:empty>



 <logic:empty name="concept-BEANLIST">
             <p>Sorry, no Community concepts match your criteria.  Please <a href="javascript:history.back()">try again</a>.</p>
 </logic:empty>
          
 <logic:notEmpty name="beanwparam">
  <p>You searched for communities with names like: "<bean:write name="beanwparam" />" .  Try <a href="@forms_link@plot-query-simple.jsp">another search</a>.</p>
 </logic:notEmpty>

<vegbank:pager />
          
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<table width="100%" cellpadding="2" class="leftrightborders" ><!--each field, only write when HAS contents-->
<!-- header -->
<tr>
  <th>More</th><th width="40%">Name</th><th width="20%">Level</th><th>Party</th>
</tr>
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->

<tr class='@nextcolorclass@'>
<td><a href='@get_link@detail/commconcept/<bean:write name="onerow" property="commconcept_id"/>'>details</a></td>
<td><bean:write name="onerow" property="commname"/>&nbsp;</td>
<td class="largefield"><bean:write name="onerow" property="commlevel"/>&nbsp;</td>
<td><a href='@get_link@std/party/<bean:write name="onerow" property="party_id"/>'><bean:write name="onerow" property="party_id_transl"/></a>&nbsp;</td>


</td>
</tr>

</logic:iterate>
</table>


</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @webpage_footer_html@
