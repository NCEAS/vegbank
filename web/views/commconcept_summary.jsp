@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>View VegBank Community Concepts - Summary</TITLE>



  
 @webpage_masthead_html@ 
  @possibly_center@ 

<h2>View Community Concepts - Summary</h2>
    
  <logic:notPresent parameter="orderBy">
         <!-- set default sorting -->
         <bean:define id="orderBy" value="xorderby_dobscount_desc" />
  </logic:notPresent>
    
  <vegbank:get id="concept" select="commconcept" beanName="map" pager="true" xwhereEnable="true" 
     allowOrderBy="true" />

<vegbank:pager />
<logic:empty name="concept-BEANLIST">
             <p>Sorry, no Community concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<table width="100%" cellpadding="2" class="leftrightborders" ><!--each field, only write when HAS contents-->
<!-- header -->
<tr>
  
     <bean:define id="thisfield" value="commname" />
      <bean:define id="fieldlabel">Name</bean:define>
      <bean:define id="thatts">width="30%"</bean:define>
    <%@ include file="../includes/orderbythisfield.jsp" %>
  
     <bean:define id="thisfield" value="reference_id_transl" />
      <bean:define id="fieldlabel">Reference</bean:define>
      <bean:define id="thatts">width="7%"</bean:define>
    <%@ include file="../includes/orderbythisfield.jsp" %>
  
     <bean:define id="thisfield" value="dobscount" />
      <bean:define id="fieldlabel">Plots</bean:define>
      <bean:define id="thatts">width="5%"</bean:define>
    <%@ include file="../includes/orderbythisfield.jsp" %>
  
  <th width="58%">Description</th>
</tr>
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->

<tr class='@nextcolorclass@'>

<td><bean:write name="onerow" property="commname_id_transl"/><br/><a href='@get_link@detail/commconcept/<bean:write name="onerow" property="commconcept_id"/>'>&raquo; more details</a></td>
<td><a href='@get_link@std/reference/<bean:write name="onerow" property="reference_id"/>'><bean:write name="onerow" property="reference_id_transl"/></a>&nbsp;</td>
<td class="numeric">
<logic:equal name="onerow" property="d_obscount" value="0">0</logic:equal>
<logic:notEqual name="onerow" property="d_obscount" value="0">
<a href="@get_link@summary/observation/<bean:write name='onerow' property='commconcept_id' />?where=where_commconcept_observation_complex"><bean:write name="onerow" property="d_obscount" />
</logic:notEqual></td>
<td class="largefield"><bean:write name="onerow" property="commdescription"/>&nbsp;</td>
</td>
</tr>

</logic:iterate>
</table>


</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @webpage_footer_html@
