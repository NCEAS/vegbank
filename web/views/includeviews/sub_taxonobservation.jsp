
     <table cellpadding="2" class="leftrightborders">
     <tr><th colspan="9" class="major">Taxa occurring on this plot-observation</th></tr>
 
 <!-- first try WITH strata -->
 <vegbank:get id="taxonimportance" select="taxonimportance_onlystrata" where="where_observation_pk" 
 beanName="map"  wparam="observation_pk" perPage="-1"/>
 
 
  <logic:notEmpty name="taxonimportance-BEANLIST">
     <tr><th colspan="9">SHOWING STRATA | 
     <a href="@get_link@summarynostrata/taxonimportance/<bean:write name='observation_pk' />?where=where_observation_pk">show overall cover only</a>
     | <a href="@get_link@summary/taxonimportance/<bean:write name='observation_pk' />?where=where_observation_pk">show strata and overall cover</a> 
     </th></tr>
     
     <tr>
     <%@ include file="../autogen/taxonimportance_summaryonlystrata_head.jsp" %>
     </tr>

     <logic:iterate id="onerowoftaxonimportance" name="taxonimportance-BEANLIST">
 
     <tr class='@nextcolorclass@'>
      <%@ include file="../autogen/taxonimportance_summaryonlystrata_data.jsp" %>
     </tr>
     </logic:iterate>


  
</logic:notEmpty>

 <!-- if didn't get any strata, show without strata -->
 <logic:empty name="taxonimportance-BEANLIST">
  
 <vegbank:get id="taxonimportancens" select="taxonimportance_nostrata" where="where_observation_pk" beanName="map" 
 wparam="observation_pk" perPage="-1"/>
     
     <logic:empty name="taxonimportancens-BEANLIST">
        <tr><td colspan="2">No taxa were observed on this plot: error!</td></tr>
     </logic:empty> <!-- without strata -->

     <logic:notEmpty name="taxonimportancens-BEANLIST">             
  
       <tr><th colspan="9">(there are no strata for this plot) | SHOWING overall over 
      
       </th></tr>
       <tr>
       <%@ include file="../autogen/taxonimportance_summarynostrata_head.jsp" %>
       </tr>
  
       <logic:iterate id="onerowoftaxonimportance" name="taxonimportancens-BEANLIST">
   
       <tr class='@nextcolorclass@'>
        <%@ include file="../autogen/taxonimportance_summarynostrata_data.jsp" %>
       </tr>
     </logic:iterate>
  
  
     </logic:notEmpty>

     
  </logic:empty><!-- with strata -->

     </table>
     <br/>
     
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