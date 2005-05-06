@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@


<TITLE>View VegBank Plant Concepts - Summary</TITLE>
 @webpage_masthead_html@ 
  @possibly_center@ 

<h2>View Plant Concepts - Summary</h2>
  <!-- tell the user what was searched for, if criteriaAsText is passed here : -->
  <logic:present parameter="criteriaAsText">
    <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
    <logic:notEmpty name="bean_criteriaAsText">
      <p class="psmall">You searched for plants: <bean:write name="bean_criteriaAsText" /></p>
  
  
    </logic:notEmpty>
  </logic:present>  

       <logic:notPresent parameter="orderBy">
            <!-- set default sorting -->
            <bean:define id="orderBy" value="xorderby_plantname" />
       </logic:notPresent>
       
  <vegbank:get id="concept" select="plantconcept" beanName="map" pager="true" xwhereEnable="true"
     allowOrderBy="true"  />

<vegbank:pager />
<logic:empty name="concept-BEANLIST">
             <p>Sorry, no Plant concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<table width="100%" cellpadding="2" class="leftrightborders" ><!--each field, only write when HAS contents-->
<!-- header -->
<tr>
  <th>More</th>
  
  <bean:define id="thisfield" value="plantname" />
  <bean:define id="fieldlabel">Name</bean:define>
  <bean:define id="thatts">width="40%"</bean:define>
  <%@ include file="../includes/orderbythisfield.jsp" %>
  
  <bean:define id="thisfield" value="reference_id_transl" />
    <bean:define id="fieldlabel">Reference</bean:define>
    <bean:define id="thatts">width="20%"</bean:define>
    <%@ include file="../includes/orderbythisfield.jsp" %>
  
  <bean:define id="thisfield" value="d_obscount" />
      <bean:define id="fieldlabel">Plots</bean:define>
      <%@ include file="../includes/orderbythisfield.jsp" %>
  
  <th>Description</th><th>Accession Code</th>
</tr>
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->
  <bean:define id="concId" name="onerow" property="plantconcept_id"/>
<tr class='@nextcolorclass@'>
<td><a href='@get_link@detail/plantconcept/<bean:write name="onerow" property="plantconcept_id"/>'>details</a></td>
<td><bean:write name="onerow" property="plantname_id_transl"/>&nbsp;</td>
<td><a href='@get_link@std/reference/<bean:write name="onerow" property="reference_id"/>'><bean:write name="onerow" property="reference_id_transl"/></a>&nbsp;</td>
<td class='numeric'>
<logic:notEqual name="onerow" property="d_obscount" value="0">
      <bean:define id="critAsTxt">
      With the plant: <bean:write name="onerow" property="plantname_id_transl"/> [<bean:write name="onerow" property="reference_id_transl"/>]
      </bean:define>
      <%  
          /* create a map of parameters to pass to the new link: */
          java.util.HashMap params = new java.util.HashMap();
          params.put("wparam", concId);
          params.put("where", "where_plantconcept_observation_complex");
          params.put("criteriaAsText", critAsTxt);
          pageContext.setAttribute("paramsName", params);
      %>
      
      <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
        <bean:write name="onerow" property="d_obscount" />
      </html:link>
  
</logic:notEqual>
<logic:equal name="onerow" property="d_obscount" value="0">0</logic:equal>
</td>
<td class="largefield"><bean:write name="onerow" property="plantdescription"/>&nbsp;</td>
<td class="largefield"><bean:write name="onerow" property="accessioncode"/>&nbsp;</td>
</tr>

</logic:iterate>
</table>


</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @webpage_footer_html@
