<!-- this subform displays taxonimprtance/taxonobservation info -->

<!-- pass a bean: strata2Show to control what gets displayed here -->
<!-- if 1 then shows no strata, 2 shows (default) strata if found, if 3 then shows all records,  -->

<!-- logic: check to see if 3, and if so, write all records
       now, if it's not 1, then write strata records (default) unless none are found, then write non-strata records (next)
       
       otherwise, if no reocrds written do this:, and write the records without any strata if so
       -->

<!-- also pass a bean: limitPlantRecs2Show : "yes" if limiting, "no" or blank otherwise  -->

<!-- also CAN pass a bean: showStrataDefn : if "no" then doesn't show strata definitions, else does -->
<logic:empty name="showStrataDefn">
 <bean:define id="showStrataDefn" value="yes" /><!-- default value -->
</logic:empty>

<!-- % String strWhereVariable = "where_taxonimportance_obsid_addsort" ; % --> <!-- default value -->
<!-- logic:equal name="limitPlantRecs2Show" value="yes" -->
  <!--  % strWhereVariable = "where_taxonimportance_obsid_addsort_limited" ; %  --> <!-- limit to only a few taxa -->
<!-- /logic:equal -->



<%     int rowOrder = 0;  %><!-- this is the number for the original sort order -->
<%     int strataGroup = 0; %> <!-- which class to use for stratum coloring -->
<%     String strataClass = "" ; %> <!-- class to use for stratum coloring where non-strata also exist -->
    <bean:define id="lastStratum">
      ---nonEe------
    </bean:define>
		 <!-- this is the last stratum-- attempting to shade according to stratum values -->

     <TABLE cellpadding="0" class="thinlines">
  <logic:equal name="smallheader" value="yes">
     <TR><TH colspan="9">--Taxa--
     
     

</TH></TR>
  </logic:equal>
  <logic:notEqual name="smallheader" value="yes">
   <TR><TH colspan="9" class="major">Taxa occurring on this plot-observation
   
   
   </TH></TR>  
  </logic:notEqual>
  

<bean:define id="stratawritten" value="no" />



<logic:equal parameter="strata2Show" value="3"> <!-- show all records of taxImp -->
  
<logic:notEqual name="limitPlantRecs2Show" value="yes">
   <!-- show all -->
    <vegbank:get id="taxonimportanceall" select="taxonimportance" where="where_taxonimportance_obsid_addsort" 
    beanName="map"     wparam="observation_pk"  pager="false" perPage="-1"  />
   
</logic:notEqual>


<logic:equal name="limitPlantRecs2Show" value="yes">
  <!-- show only some -->
   <vegbank:get id="taxonimportanceall" select="taxonimportance" where="where_taxonimportance_obsid_addsort_limited" beanName="map" 
   wparam="observation_pk"  pager="false" perPage="-1" />
</logic:equal>  

  
  
     <TR><TH colspan="9"><a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=2">show strata</a> | 
	      <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=1">show overall cover</a>
	      | SHOWING strata + overall
	      </TH></TR>
          
       <logic:empty name="taxonimportanceall-BEANLIST">
          <TR><TD colspan="2">No cover values for taxa on this plot: error!</TD></TR>
       </logic:empty> <!-- without strata -->
  
       <logic:notEmpty name="taxonimportanceall-BEANLIST">             
   
   <TR><TD colspan="9">
     <!-- sortable table: -->
     <table cellpadding="2" class="thinlines sortable" id="taxonObservationof<bean:write name='observation_pk' />">

         <tr><th>ord</th>
         <%@ include file="../autogen/taxonimportance_summary_head.jsp" %>
         </tr>
    
         <logic:iterate id="onerowoftaxonimportance" name="taxonimportanceall-BEANLIST">
        	<logic:notEqual name="onerowoftaxonimportance" property="stratum_id_transl"
	       	  value='<%= lastStratum %>'>
	       	  <!-- change the color of this -->
	       	   <% strataGroup ++ ; %>
	       	   
	       	   <% if (( strataGroup == 13 )) 
	       	   {
	       	   
	       	     strataGroup = 1;
	       	   } 
	       	   %>
	       	   
	       	  
	            <bean:define id="lastStratum"><bean:write name="onerowoftaxonimportance" property="stratum_id_transl" /></bean:define>
	            <logic:equal name="lastStratum" value="-all-">
	             <!-- set class to normal -->
	                <% strataClass = "normal" ; %>
	             
	            </logic:equal>
	            <logic:notEqual name="lastStratum" value="-all-" >
	               <!-- set class to group -->
	               <% strataClass = "group" + strataGroup ; %>
	            </logic:notEqual>
      	</logic:notEqual> 
       
     
         <tr class='<%= strataClass %>'>
          <td class="sizetiny"><% rowOrder ++ ; %><%= rowOrder %></td>
          <%@ include file="../autogen/taxonimportance_summary_data.jsp" %>
         </tr>

       </logic:iterate>
     </table>
   </TD></TR>  
       
       </logic:notEmpty>
  <bean:define id="stratawritten" value="yes" />


</logic:equal><!-- show all records of taxImp -->



<!--     ################## STRATA ONLY ##################### -->
<logic:notEqual parameter="strata2Show" value="1">
<!-- also make sure didn't just write them: -->
<logic:notEqual name="stratawritten" value="yes"><!-- default -->
 
 <logic:notEqual name="limitPlantRecs2Show" value="yes">
    <!-- show all -->
   <vegbank:get id="taxonimportance" select="taxonimportance_onlystrata" where="where_taxonimportance_obsid_addsort" 
   beanName="map"  wparam="observation_pk"  pager="false" perPage="-1" />

   
 </logic:notEqual>
 
 
 <logic:equal name="limitPlantRecs2Show" value="yes">
   <!-- show only some -->
    <vegbank:get id="taxonimportance" select="taxonimportance_onlystrata" where="where_taxonimportance_obsid_addsort_limited" 
    beanName="map"  wparam="observation_pk"  pager="false" perPage="-1" />

</logic:equal> 
 
 
 
  <logic:notEmpty name="taxonimportance-BEANLIST">
     <tr><th colspan="9">SHOWING STRATA | 
     <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=1">show overall cover </a>
     | <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=3">show strata + overall </a> 
     </th></tr>
   
     <TR><TD colspan="9">
        <!-- sortable table: -->
        <table cellpadding="2" class="thinlines sortable" id="taxonObservationof<bean:write name='observation_pk' />">
 
  
   
     <tr><th>ord</th>
     <%@ include file="../autogen/taxonimportance_summaryonlystrata_head.jsp" %>
     </tr>

     <logic:iterate id="onerowoftaxonimportance" name="taxonimportance-BEANLIST">
        
      	<logic:notEqual name="onerowoftaxonimportance" property="stratum_id_transl"
      	  value='<%= lastStratum %>'>
      	  <!-- change the color of this -->
      	   <% strataGroup ++ ; %>
      	   
      	   <% if (( strataGroup == 13 )) 
      	   {
      	   
      	     strataGroup = 1;
      	   } 
      	   %>
      	   
      	  
           <bean:define id="lastStratum">
            <bean:write name="onerowoftaxonimportance" property="stratum_id_transl" />
           </bean:define>
      	</logic:notEqual>  

 
 
     <tr class='group<%= strataGroup %>'><td class="sizetiny"><% rowOrder ++ ; %><%= rowOrder %></td>
      <%@ include file="../autogen/taxonimportance_summaryonlystrata_data.jsp" %>
     </tr>
            
     </logic:iterate>
       </table>
      </TD></TR>   
      <bean:define id="stratawritten" value="yes" />

  
</logic:notEmpty>
<!-- note that if empty, we'll write the non-stratum values below -->

</logic:notEqual>
</logic:notEqual>







<logic:notEqual name="stratawritten" value="yes"><!-- default -->
 
 <!-- show WITH NO strata -->
  
 <logic:notEqual name="limitPlantRecs2Show" value="yes">
    <!-- show all -->
   <vegbank:get id="taxonimportancens" select="taxonimportance_nostrata" where="where_taxonimportance_obsid_addsort" beanName="map" 
     wparam="observation_pk"  pager="false" perPage="-1" />

    
 </logic:notEqual>
 
 
 <logic:equal name="limitPlantRecs2Show" value="yes">
   <!-- show only some -->
   <vegbank:get id="taxonimportancens" select="taxonimportance_nostrata" where="where_taxonimportance_obsid_addsort_limited" beanName="map" 
     wparam="observation_pk"  pager="false" perPage="-1" />

</logic:equal> 


     
     <tr><th colspan="9"><a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=2">show strata</a> | 
	      SHOWING overall cover
	      | <a href="@get_link@summary/observation/<bean:write name='observation_pk' />?strata2Show=3">show strata + overall </a> 
	      </th></tr>
          
       <logic:empty name="taxonimportancens-BEANLIST">
          <tr><td colspan="2">No overall cover values for taxa on this plot: error!</td></tr>
       </logic:empty> <!-- without strata -->
  
       <logic:notEmpty name="taxonimportancens-BEANLIST">             
    <TR><TD colspan="9">
     <!-- sortable table: -->
     <table cellpadding="2" class="thinlines sortable" id="taxonObservationof<bean:write name='observation_pk' />">
 
    
         <tr><th>ord</th>
         <%@ include file="../autogen/taxonimportance_summarynostrata_head.jsp" %>
         </tr>
    
         <logic:iterate id="onerowoftaxonimportance" name="taxonimportancens-BEANLIST">
     
         <tr class='normal'><td class="sizetiny"><% rowOrder ++ ; %><%= rowOrder %></td>
          <%@ include file="../autogen/taxonimportance_summarynostrata_data.jsp" %>
         </tr>
               
       </logic:iterate>
      </table>
   </TD></TR>     </logic:notEmpty>
  <bean:define id="stratawritten" value="yes" />
       
 


 <!-- end showing with NO strata -->
 

 


</logic:notEqual>  <!-- name="stratawritten" value="yes"  default -->



     <TR><TD colspan="9" class="bright sizetiny">This table is SORTABLE.  Click the headers to sort ascending and descending.</TD></TR>
   
   
      <logic:equal name="limitPlantRecs2Show" value="yes">
         <!-- tell user that not all plants are shown! -->
        <tr><td colspan="9" class="psmall">Note: Some taxa may not be shown. See full view for all taxa. </td></tr>
      </logic:equal>
   </TABLE>
  
<logic:notEqual name="showStrataDefn" value="no"> <!-- if no, then don't show this part -->
  
  <br/>
<!-- ############################# stratum definitions #################################### -->     
     <vegbank:get id="stratum" select="stratum" beanName="map" pager="false" where="where_observation_pk" 
     wparam="observation_pk" perPage="-1" />
	 <TABLE class="thinlines" cellpadding="2">
	 <tr><th colspan="9">Stratum Definitions:</th></tr>
	 <logic:empty name="stratum-BEANLIST">
	 <tr><td class="@nextcolorclass@">  Sorry, no Stratum Definitions found.</td></tr>
	 </logic:empty>
	 <logic:notEmpty name="stratum-BEANLIST">
	  <TR><TD colspan="9">
	     <!-- sortable table: -->
	     <table cellpadding="2" class="thinlines sortable" id="Strataof<bean:write name='observation_pk' />">
	 
  
	
	<tr>
	 <%@ include file="../autogen/stratum_summary_head.jsp" %>
	 </tr>
	 <logic:iterate id="onerowofstratum" name="stratum-BEANLIST">
	 <tr class="@nextcolorclass@">
	 <%@ include file="../autogen/stratum_summary_data.jsp" %>
	 </tr>
	 </logic:iterate>
	   </table>
	   </TD></TR>  
	   </logic:notEmpty>
<TR><TD colspan="9" class="bright sizetiny">This table is SORTABLE.  Click the headers to sort ascending and descending.</TD></TR>
</TABLE>

</logic:notEqual> <!-- whether or not to show strata -->