
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  <bean:define id="FullPageWidthSuffix" value="_100perc" /><!-- sets stylesheet to full width stylesheet -->
  @webpage_head_html@
 
<TITLE>Constancy Table : VegBank </TITLE>
  
  <!-- include script for sorting tables -->
  <script language="javascript" src="@includes_link@sort_table.js"></script>
  
  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>VegBank Constancy Table</h2>

  <!-- look for special set of dataset params: -->
  <%@ include file="includeviews/get_datasetparams.jsp" %>
  
  <logic:notPresent parameter="where">
    <!-- use default where -->
    <bean:define id="where" value="where_userdataset_pk" />
  </logic:notPresent>
<logic:notEqual name="wparambad" value="true">
    <vegbank:get id="multobssummary" select="userdataset_currentplants_analysis" 
      beanName="map" pager="false" perPage="-1" allowOrderBy="true" orderBy="orderby_plantconc_dataset" 
     />
     <!-- this gets a list of plants, datasets with constancy values and average cover, where present -->
     <!-- strategy, loop over this, but check to make sure it's in the right column before writing -->
        <!-- if not in the right column, add an empty cell and check again -->
        <!-- if it is a new plant, then start a new row -->
     
     
 
   <!--Where statement removed from preceding: -->
   <logic:empty name="multobssummary-BEANLIST">
     <p>  Sorry, no data found.</p>
   </logic:empty>
   <logic:notEmpty name="multobssummary-BEANLIST">
     <p><a href="javascript:void setupConfig('<bean:write name='thisviewid' />');">Configure data displayed on this page</a></p>
     <table class="thinlines sortable" cellpadding="2" id="maintable">
       <tr>
         <!--<th>Plant Concept ID</th>-->
         <th class="plant_concept_name"></th>                
         <th class="plant_full_scientific_name"></th>
         <th class="plant_scientific_name_noauthors"></th>
         <th class="plant_common_name"></th>
         <th class="plant_code"></th>
         <% rowClass="evenrow" ; %><!-- reset colors for row -->
         <!-- show dataset details -->
         <!-- wparam is on the URL line -->
         <vegbank:get id="datasettop" select="userdataset_countobs" beanName="map"  pager="false" perPage="-1"
            allowOrderBy="true" orderBy="xorderby_userdataset_id" />
         <% String userdatasetsInOrder = "-1" ; %><!-- the first id, -1 will be ignored later -->
         <logic:notEmpty name="datasettop-BEANLIST">
           <logic:iterate id="onerowofdatasettop" name="datasettop-BEANLIST">
             <!-- 4 COLS for EACH DATASET! -->
             <bean:define id="currdatasetid"><bean:write name="onerowofdatasettop" property="userdataset_id" /></bean:define>
             <th colspan="4">
               Dataset: 
               <a href="@get_link@std/userdataset/<bean:write name='currdatasetid' />"><bean:write name="onerowofdatasettop" property="datasetname" /></a>
               Total: <bean:write name="onerowofdatasettop" property="countobs" /> plots
               
             </th>
             <% userdatasetsInOrder = userdatasetsInOrder + "," + currdatasetid ; %>
           </logic:iterate>
           </tr><!-- end of top top row -->
           <tr class="sortbythisrow"><!-- start of secondary header -->

         <th class="plant_concept_name">             Plant Concept Name</th>                
         <th class="plant_full_scientific_name">     Full Scientific Name</th>
         <th class="plant_scientific_name_noauthors">Scientific Without Authors</th>
         <th class="plant_common_name">              Common Name</th>
         <th class="plant_code">                     Code</th>

           <logic:iterate id="onerowofdatasettop" name="datasettop-BEANLIST">
             <!-- 4 COLS for EACH DATASET! -->
             <bean:define id="currdatasetid"><bean:write name="onerowofdatasettop" property="userdataset_id" /></bean:define>
             <th class="@nextcolorclass@">Plots</th>
             <th class="<%= rowClass %>" >Avg Cover %</th>
             <th class="<%= rowClass %>" >min Cover %</th>
             <th class="<%= rowClass %>" >MAX Cover %</th>
           </logic:iterate>
         </logic:notEmpty>
          <!-- create an array of userdataset_ids in ascending order here, with first one ignored (-1) -->

           <% String[] userdatasets = userdatasetsInOrder.split(",") ; %>
           <% int currentColumn = 1; %> <!-- set to 1, b/c the first userdataset id will be ignored -->
           <% int thisdatasetid = -1; %>  

       </tr>
      
       <!-- define initial plant concept of neg1 -->
       <bean:define id="lastpc">-1</bean:define>
       
       <logic:iterate id="onerowofmultobssummary" name="multobssummary-BEANLIST">
         <bean:define id="plantconcept_pk"><bean:write name="onerowofmultobssummary" property="plantconcept_id" /></bean:define>
         <logic:notEqual name="lastpc" value="<%= plantconcept_pk %>"> 
           <!-- new row b/c new plant-->
             <logic:notEqual name="lastpc" value="-1"><!-- end last row -->
                <!-- write any cells that weren't written -->
                <%
                         for (int i=currentColumn; i<userdatasets.length ; i++)
                          { 
                            %>
                              <!--SKIP b/c:new row to be written! -->
                              <td class="@nextcolorclass@ numeric">0</td>
                              <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
                              <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
                              <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
                            <%
                              currentColumn++ ;
                          }  
                  %>
             </tr>
             </logic:notEqual>
             <tr>
             <% rowClass="evenrow" ; %><!-- reset colors for row -->
             <!-- <td><bean:write name="onerowofmultobssummary" property="plantconcept_id" /></td> -->
           <!-- now get name(s) -->
           
               <td class="smallfield plant_concept_name">             
                 <a href="@get_link@detail/plantconcept/<bean:write name='onerowofmultobssummary' property='plantconcept_id' />">
                   <bean:write name="onerowofmultobssummary" property="plantname" />
                 </a>
               </td>
               <!-- need to put the "other names" with asterisks at same place as names with (for sorting), so these lines are really long -->
               <td class="smallfield plant_full_scientific_name">     
                 <a href="@get_link@detail/plantconcept/<bean:write name='onerowofmultobssummary' property='plantconcept_id' />">
                   <bean:write name="onerowofmultobssummary" property="sciname" /><logic:empty name="onerowofmultobssummary" property="sciname"><bean:write name="onerowofmultobssummary" property="plantname" />**</logic:empty>
                 </a>
               </td>
               <td class="smallfield plant_scientific_name_noauthors">
                 <a href="@get_link@detail/plantconcept/<bean:write name='onerowofmultobssummary' property='plantconcept_id' />">
                   <bean:write name="onerowofmultobssummary" property="scinamenoauth" /><logic:empty name="onerowofmultobssummary" property="scinamenoauth"><bean:write name="onerowofmultobssummary" property="plantname" />**</logic:empty>
                 </a>
               </td>
               <td class="smallfield plant_common_name">              
                 <a href="@get_link@detail/plantconcept/<bean:write name='onerowofmultobssummary' property='plantconcept_id' />">
                   <bean:write name="onerowofmultobssummary" property="common" /><logic:empty name="onerowofmultobssummary" property="common"><bean:write name="onerowofmultobssummary" property="plantname" />**</logic:empty>
                 </a>
               </td>
               <td class="smallfield plant_code">                     
                 <a href="@get_link@detail/plantconcept/<bean:write name='onerowofmultobssummary' property='plantconcept_id' />">
                   <bean:write name="onerowofmultobssummary" property="code" /><logic:empty name="onerowofmultobssummary" property="code"><bean:write name="onerowofmultobssummary" property="plantname" />**</logic:empty>
                 </a>
               </td>
               <!-- reset the current column -->
               <% currentColumn = 1; %><!-- set to 1, b/c the first userdataset id will be ignored -->
         </logic:notEqual>  
         <!-- ok so now we have some summary info to write.  
            BUT we have to be in the right column to write it.  The lastColumn index is recorded, and the
            userdataset IDs are in an array.  From this we figure it out -->
         <bean:define id="thisdatasetid_temp"><bean:write name="onerowofmultobssummary" property="userdataset_id" /></bean:define>
         <%
         for (int i=currentColumn; i<userdatasets.length ; i++)
          { 
            thisdatasetid = java.lang.Integer.parseInt(thisdatasetid_temp)  ;
            if (java.lang.Integer.parseInt(userdatasets[i]) < thisdatasetid) {
            %>
              <!--SKIP b/c:<bean:write name="thisdatasetid_temp" />-->
              <td class="@nextcolorclass@ numeric">0</td>
              <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
              <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
              <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
            <%
              currentColumn++ ;
            }
          }  
            %>
         <td class="@nextcolorclass@ numeric"><bean:write name="onerowofmultobssummary" property="countobs" /></td>
         <td class="<%= rowClass %> numeric" ><bean:write name="onerowofmultobssummary" property="avgcover" /></td>
         <td class="<%= rowClass %> numeric" ><bean:write name="onerowofmultobssummary" property="mincover" /></td>
         <td class="<%= rowClass %> numeric" ><bean:write name="onerowofmultobssummary" property="maxcover" /></td>
          <% currentColumn++ ; %>
         
         <!-- remember last pc -->
         <bean:define id="lastpc"><bean:write name="plantconcept_pk"/></bean:define>
         
       </logic:iterate><!-- iterating over plants -->
                 <%
                               for (int i=currentColumn; i<userdatasets.length ; i++)
                                { 
                                  %>
                                    <!--SKIP b/c:new row to be written! -->
                                    <td class="@nextcolorclass@ numeric">0</td>
                                    <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
                                    <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
                                    <td class="<%= rowClass %> numeric" >&nbsp;</td><!-- skip this dataset -->
                                    
                                  <%
                                    currentColumn++ ;
                                }  
                  %>
      
      </tr><!-- end last row that is left over -->
     </table>
   </logic:notEmpty>
   <br />
</logic:notEqual>  

          @webpage_footer_html@
