@stdvegbankget_jspdeclarations@


<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=">
@defaultHeadToken@
 
<TITLE>View VegBank Plant Concept Hierarchy</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@ 

<h2>View Plant Concept Hierarchy</h2>
  <vegbank:get id="concept" select="plantconcept_hierarchy" 
  beanName="map" pager="false" xwhereEnable="false" perPage="-1"
  where="where_conceptid" />


<logic:empty name="concept-BEANLIST">
             <p>Sorry, nothing found about the hierarchy for the plant requested.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->


<p class="instructions">Note: click the "H" next to a plant to see it in the hierarchy, click "D" to see details about it.</p>
<table class="leftrightborders" width="100%" cellpadding="2" cellspacing="0">
<tr><th>Parent</th><th>Plant</th><th>Children</th></tr>

<bean:define id="firstLoop" value="yes" />
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set  -->

<logic:equal name="firstLoop" value="yes">
<!-- parent and concept are only for first iteration! -->
<tr><td colspan="3">
<logic:notEmpty name="onerow" property="parentconcept_id" > <!-- has parent -->
  <bean:write name="onerow" property="parentname" /> 
  (<a href='@get_link@hierarchy/plantconcept/<bean:write name="onerow" property="parentconcept_id" />'>H</a> 
  |<a href='@get_link@detail/plantconcept/<bean:write name="onerow" property="parentconcept_id" />'>D</a>) 
</logic:notEmpty> <!-- has parent -->
<logic:empty name="onerow" property="parentconcept_id">
  [No parent]
</logic:empty>
</td></tr><!-- parent -->


<tr><!-- plant -->
<td /><td colspan="2">
  <bean:write name="onerow" property="conceptname" />
    (H 
    |<a href='@get_link@detail/plantconcept/<bean:write name="onerow" property="concept_id" />'>D</a>) 

</td></tr><!--plant -->
<bean:define id="firstLoop" value="no" />
</logic:equal>


<!-- get children -->
<tr>
 <td colspan="2" />
 <td>
   <logic:notEmpty name="onerow" property="childconcept_id">
     <!-- has children -->
   <bean:write name="onerow" property="childname" />
     (<a href='@get_link@hierarchy/plantconcept/<bean:write name="onerow" property="childconcept_id" />'>H</a> 
     |<a href='@get_link@detail/plantconcept/<bean:write name="onerow" property="childconcept_id" />'>D</a>) 
   </logic:notEmpty>
     <!-- if no children: -->
   <logic:empty  name="onerow" property="childconcept_id">
     [no children]
   </logic:empty>
 </td>
</tr> <!-- child -->
</logic:iterate>
</table>

 <!-- concept -->


</logic:notEmpty>

<br/>


<br/>
          @vegbank_footer_html_tworow@
