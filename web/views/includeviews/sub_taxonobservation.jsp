<!-- this subform displays taxonimprtance/taxonobservation info -->

<!-- pass a bean: strata2Show to control what gets displayed here -->
<!-- if 1 then shows no strata, 2 shows (default) strata if found, if 3 then shows all records,  -->

<!-- logic: check to see if 3, and if so, write all records
       now, if it's not 1, then write strata records (default) unless none are found, then write non-strata records (next)
       
       otherwise, if no reocrds written do this:, and write the records without any strata if so
       -->


     <table cellpadding="2" class="leftrightborders">
  <logic:equal name="smallheader" value="yes">
     <tr><th colspan="9">--Taxa--</th></tr>
  </logic:equal>
  <logic:notEqual name="smallheader" value="yes">
   <tr><th colspan="9" class="major">Taxa occurring on this plot-observation</th></tr>  
  </logic:notEqual>
  
  
  


<bean:define id="stratawritten" value="no" />



<logic:equal parameter="strata2Show" value="3"> <!-- show all records of taxImp -->
   <vegbank:get id="taxonimportanceall" select="taxonimportance" where="where_taxonimportance_obsid_addsort" beanName="map" 
   wparam="observation_pk" perPage="-1"/>
     
     <tr><th colspan="9"><a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=2">show strata</a> | 
	      <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=1">show overall cover</a>
	      | SHOWING strata + overall
	      </th></tr>
          
       <logic:empty name="taxonimportanceall-BEANLIST">
          <tr><td colspan="2">No cover values for taxa on this plot: error!</td></tr>
       </logic:empty> <!-- without strata -->
  
       <logic:notEmpty name="taxonimportanceall-BEANLIST">             
    
         <tr>
         <%@ include file="../autogen/taxonimportance_summary_head.jsp" %>
         </tr>
    
         <logic:iterate id="onerowoftaxonimportance" name="taxonimportanceall-BEANLIST">
     
         <tr class='@nextcolorclass@'>
          <%@ include file="../autogen/taxonimportance_summary_data.jsp" %>
         </tr>

       </logic:iterate>
       </logic:notEmpty>
  <bean:define id="stratawritten" value="yes" />


</logic:equal><!-- show all records of taxImp -->



<!--     ################## STRATA ONLY ##################### -->
<logic:notEqual parameter="strata2Show" value="1">
<!-- also make sure didn't just write them: -->
<logic:notEqual name="stratawritten" value="yes"><!-- default -->
 <vegbank:get id="taxonimportance" select="taxonimportance_onlystrata" where="where_taxonimportance_obsid_addsort" 
 beanName="map"  wparam="observation_pk" perPage="-1"/>
 
 
  <logic:notEmpty name="taxonimportance-BEANLIST">
     <tr><th colspan="9">SHOWING STRATA | 
     <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=1">show overall cover </a>
     | <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=3">show strata + overall </a> 
     </th></tr>
     
     <tr>
     <%@ include file="../autogen/taxonimportance_summaryonlystrata_head.jsp" %>
     </tr>

     <logic:iterate id="onerowoftaxonimportance" name="taxonimportance-BEANLIST">
 
     <tr class='@nextcolorclass@'>
      <%@ include file="../autogen/taxonimportance_summaryonlystrata_data.jsp" %>
     </tr>
            
     </logic:iterate>
     <bean:define id="stratawritten" value="yes" />

  
</logic:notEmpty>
<!-- note that if empty, we'll write the non-stratum values below -->

</logic:notEqual>
</logic:notEqual>







<logic:notEqual name="stratawritten" value="yes"><!-- default -->
 
 <!-- show WITH NO strata -->
   <vegbank:get id="taxonimportancens" select="taxonimportance_nostrata" where="where_taxonimportance_obsid_addsort" beanName="map" 
   wparam="observation_pk" perPage="-1"/>
     
     <tr><th colspan="9"><a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=2">show strata</a> | 
	      SHOWING overall cover
	      | <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=3">show strata + overall </a> 
	      </th></tr>
          
       <logic:empty name="taxonimportancens-BEANLIST">
          <tr><td colspan="2">No overall cover values for taxa on this plot: error!</td></tr>
       </logic:empty> <!-- without strata -->
  
       <logic:notEmpty name="taxonimportancens-BEANLIST">             
    
         <tr>
         <%@ include file="../autogen/taxonimportance_summarynostrata_head.jsp" %>
         </tr>
    
         <logic:iterate id="onerowoftaxonimportance" name="taxonimportancens-BEANLIST">
     
         <tr class='@nextcolorclass@'>
          <%@ include file="../autogen/taxonimportance_summarynostrata_data.jsp" %>
         </tr>
               
       </logic:iterate>
       </logic:notEmpty>
  <bean:define id="stratawritten" value="yes" />
       
 


 <!-- end showing with NO strata -->
 

 


</logic:notEqual>  <!-- name="stratawritten" value="yes"  default -->




     </table>
     <br/>
<!-- ############################# stratum definitions #################################### -->     
     <vegbank:get id="stratum" select="stratum" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
	 <table class="leftrightborders" cellpadding="2">
	 <tr><th colspan="9">Stratum Definitions:</th></tr>
	 <logic:empty name="stratum-BEANLIST">
	 <tr><td class="@nextcolorclass@">  Sorry, no Stratum Definitions found.</td></tr>
	 </logic:empty>
	 <logic:notEmpty name="stratum-BEANLIST">
	 <tr>
	 <%@ include file="../autogen/stratum_summary_head.jsp" %>
	 </tr>
	 <logic:iterate id="onerowofstratum" name="stratum-BEANLIST">
	 <tr class="@nextcolorclass@">
	 <%@ include file="../autogen/stratum_summary_data.jsp" %>
	 </tr>
	 </logic:iterate>
	 </logic:notEmpty>
</table>