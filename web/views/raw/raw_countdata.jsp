  @stdvegbankget_jspdeclarations@
  <% String rowClass = "evenrow" ; %>
  <vegbank:get select="observation_summ" beanName="map" />
  <table class="leftrightborders" cellpadding="1">
       
       <logic:empty name="BEANLIST">
        <tr class="sizetiny @nextcolorclass@"><td colspan="2">ERROR! There are no plots!</td></tr>
       </logic:empty>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>Plots:</td>
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
      
      
      <vegbank:get select="commconcept_summ" beanName="map"/>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>Community Concepts:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_commconcept"/></td>
         </tr> 
       </logic:notEmpty> 
      
      <vegbank:get select="party_summ" beanName="map"/>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>People:</td>
           <td class="numeric" >&nbsp;<bean:write name="BEAN" property="count_party"/></td>
         </tr> 
       </logic:notEmpty>
      
      <vegbank:get select="taxonobservation_summ" beanName="map"/>
       <logic:notEmpty name="BEANLIST">
         <tr class="sizetiny @nextcolorclass@">
           <td>Taxa observed on plots:</td>
           <td  class="numeric" >&nbsp;<bean:write name="BEAN" property="count_taxonobservation"/></td>
         </tr> 
       </logic:notEmpty>
      </table>