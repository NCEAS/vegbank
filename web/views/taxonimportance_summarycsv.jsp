<?xml version="1.0" encoding="UTF-8" ?>
<?xml-stylesheet type="text/xsl" href="@xml_link@util/htmltable2csv.xsl"?>
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<!-- this form displays taxonimprtance/taxonobservation info -->

<!-- pass a parameter: strata2Show to control what gets displayed here -->
<!-- if 1 then shows no strata, 2 shows (default) strata if found, if 3 then shows all records,  -->

<!-- logic: check to see if 3, and if so, write all records
       now, if it's not 1, then write strata records (default) unless none are found, then write non-strata records (next)
       
       otherwise, if no reocrds written do this:, and write the records without any strata if so
       -->


<bean:define id="headerwritten" value="no" /> <!-- var to only write headers once! -->
<!-- THIS FORM MUST HANDLE MULTIPLE OBSERVATIONS in ONE table!! -->
 <vegbank:get id="observation" select="plotandobservation" whereNumeric="where_observation_pk" 
     whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true"/>

 <table class="thinlines"> 
<logic:notEmpty name="observation-BEANLIST">
<logic:iterate id="onerowofobservation" name="observation-BEANLIST">
<bean:define id="observation_pk" name="onerowofobservation" property="observation_id" />


<%     int rowOrder = 0;  %><!-- this is the number for the original sort order -->

 
		 <!-- this is the last stratum-- attempting to shade according to stratum values -->

  

<bean:define id="stratawritten" value="no" />

<logic:equal parameter="strata2Show" value="3"> <!-- show all records of taxImp -->

    <vegbank:get id="taxonimportanceall" select="taxonimportance" 
    beanName="map" where="where_observation_pk" wparam="observation_pk"  pager="false" perPage="-1"  />
 
     
         <logic:notEqual name="headerwritten" value="yes">
         <tr><th>OBSERVATION ID</th><th>ord</th>
         <%@ include file="autogen/taxonimportance_summary_head.jsp" %>
         </tr>
         </logic:notEqual>
         <bean:define id="headerwritten" value="yes" />
         
         <logic:iterate id="onerowoftaxonimportance" name="taxonimportanceall-BEANLIST">
             
         <tr>
          <td><bean:write name="onerowoftaxonimportance" property="observation_id" /></td>
          <td class="sizetiny"><% rowOrder ++ ; %><%= rowOrder %></td>
          <%@ include file="autogen/taxonimportance_summary_data.jsp" %>
         </tr>

       </logic:iterate>
       
   
  <bean:define id="stratawritten" value="yes" />


</logic:equal><!-- show all records of taxImp -->

<!--     ################## STRATA ONLY ##################### -->
<logic:notEqual parameter="strata2Show" value="1">
<!-- also make sure didn't just write them: -->
<logic:notEqual name="stratawritten" value="yes"><!-- default -->
 
 
    <!-- show all -->
   <vegbank:get id="taxonimportance" select="taxonimportance_onlystrata" 
   beanName="map"  where="where_observation_pk" wparam="observation_pk"  pager="false" perPage="-1" />

   <logic:notEqual name="headerwritten" value="yes">
     <tr><th>OBSERVATION ID</th><th>ord</th>
     <%@ include file="autogen/taxonimportance_summaryonlystrata_head.jsp" %>
     </tr>
    </logic:notEqual>
         <bean:define id="headerwritten" value="yes" />
         
     <logic:iterate id="onerowoftaxonimportance" name="taxonimportance-BEANLIST">
     <tr>
     <td><bean:write name="onerowoftaxonimportance" property="observation_id" /></td>
     <td class="sizetiny"><% rowOrder ++ ; %><%= rowOrder %></td>
      <%@ include file="autogen/taxonimportance_summaryonlystrata_data.jsp" %>
     </tr>
            
     </logic:iterate>
 
 
      <bean:define id="stratawritten" value="yes" />

<!-- note that if empty, we'll NOT! write the non-stratum values below -->

</logic:notEqual><!-- stratawritten -->
</logic:notEqual><!-- strata2Show -->


<logic:notEqual name="stratawritten" value="yes"><!-- default -->
 
 <!-- show WITH NO strata -->
  
 
    <!-- show all -->
   <vegbank:get id="taxonimportancens" select="taxonimportance_nostrata"  beanName="map" 
        where="where_observation_pk" wparam="observation_pk" pager="false" perPage="-1" />

       <logic:notEqual name="headerwritten" value="yes">
         <tr><th>OBSERVATION ID</th><th>ord</th>
         <%@ include file="autogen/taxonimportance_summarynostrata_head.jsp" %>
         </tr>
        </logic:notEqual>
         <bean:define id="headerwritten" value="yes" />
         
         <logic:iterate id="onerowoftaxonimportance" name="taxonimportancens-BEANLIST">
     
         <tr class='normal'>
         <td><bean:write name="onerowoftaxonimportance" property="observation_id" /></td>
         <td class="sizetiny"><% rowOrder ++ ; %><%= rowOrder %></td>
          <%@ include file="autogen/taxonimportance_summarynostrata_data.jsp" %>
         </tr>
               
       </logic:iterate>
  
  <bean:define id="stratawritten" value="yes" />
  
 <!-- end showing with NO strata -->


</logic:notEqual>  <!-- name="stratawritten" value="yes"  default -->

  

  
<!-- entire obs -->
</logic:iterate>
</logic:notEmpty>



   </table>