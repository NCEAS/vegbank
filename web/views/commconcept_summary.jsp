@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>View VegBank Community Concepts - Summary</TITLE>



  
 @webpage_masthead_html@ 
  @possibly_center@ 

<h2>View Community Concepts - Summary</h2>
  <vegbank:get id="concept" select="commconcept" beanName="map" pager="true" xwhereEnable="true"/>

<vegbank:pager />
<logic:empty name="concept-BEANLIST">
             <p>Sorry, no Community concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<table width="100%" cellpadding="2" class="leftrightborders" ><!--each field, only write when HAS contents-->
<!-- header -->
<tr>
  <th width="33%">Name</th><th width="7%">Reference</th><th width="60%">Description</th>
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
