  @stdvegbankget_jspdeclarations@
  <% String rowClass = "evenrow" ; %>
  
  <table class="leftrightborders" cellpadding="1">
       <vegbank:get select="observation_summ" beanName="map" />
       <logic:empty name="BEANLIST">
        <tr class="sizetiny @nextcolorclass@"><td colspan="2">ERROR! There are no plots!</td></tr>
       </logic:empty>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>Plots:</td>
           <td class="numeric" >&nbsp;<bean:write name="BEAN" property="count_observation"/></td>
         </tr> 
       </logic:notEmpty>
      
      <!-- classified plots -->
      <vegbank:get select="observation_classified_summ" beanName="map" />
      <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>--Classified Plots:</td>
           <td class="numeric" >&nbsp;<bean:write name="BEAN" property="count_observation"/></td>
         </tr> 
       </logic:notEmpty>

      <!-- NVC classified plots -->
      <vegbank:get select="observation_classified_tonvc_summ" beanName="map" />
      <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>----to NVC communities:</td>
           <td class="numeric" >&nbsp;<bean:write name="BEAN" property="count_observation"/></td>
         </tr> 
       </logic:notEmpty>
      
      <vegbank:get select="plantconcept_summ" beanName="map"/>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>Plant Concepts:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_plantconcept"/></td>
         </tr> 
       </logic:notEmpty> 

      <!-- usda accepted plants --> 
      <vegbank:get select="plantconcept_usdaok_summ" beanName="map"/>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>--accepted by USDA:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_plantconcept"/></td>
         </tr> 
       </logic:notEmpty>
       
       <!-- usda accepted on plots -->
      <vegbank:get select="plantconcept_usdaok_summ" beanName="map" where="where_dobscount_min" wparam="1" />
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>----and on plots:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_plantconcept"/></td>
         </tr> 
       </logic:notEmpty>
       
      <vegbank:get select="commconcept_summ" beanName="map"/>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>Community Concepts:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_commconcept"/></td>
         </tr> 
       </logic:notEmpty> 
      
       <!-- NVC comms: -->
      <vegbank:get select="commconcept_nvcok_summ" beanName="map"/>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>--in the NVC:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_commconcept"/></td>
         </tr> 
       </logic:notEmpty> 
       <!-- NVC comms on plots: -->
      <vegbank:get select="commconcept_nvcok_summ" beanName="map" where="where_dobscount_min" wparam="1" />
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>----and on plots:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_commconcept"/></td>
         </tr> 
       </logic:notEmpty>
      

      </table>
