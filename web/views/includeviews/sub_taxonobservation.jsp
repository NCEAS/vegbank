<!-- this subform displays taxonimportance/taxonobservation info -->


<!-- TODO: look for strata2Show as cookie if not passed on URL -->
<!-- pass a bean: strata2Show to control what gets displayed here -->
<!-- if 1 then shows no strata, 2 shows (default) strata if found, if 3 then shows all records,  -->

<!-- logic: check to see if 3, and if so, write all records
       now, if it's not 1, then write strata records (default) unless none are found, then write non-strata records (next)
       
       otherwise, if no records written do this:, and write the records without any strata if so
       -->


<!-- observation_pk must also be defined for this to work -->

<!-- also CAN pass a bean: showStrataDefn : if "no" then doesn't show strata definitions, else does -->
<logic:empty name="showStrataDefn">
 <bean:define id="showStrataDefn" value="yes" /><!-- default value -->
</logic:empty>


<%     int rowOrder = 0;  %><!-- this is the number for the original sort order -->
<%     int strataGroup = 0; %> <!-- which class to use for stratum coloring -->
<%     String strataClass = "" ; %> <!-- class to use for stratum coloring where non-strata also exist -->
    
    <!-- this is the last stratum- shade according to stratum values -->
    <bean:define id="lastStratum">
      -none-
    </bean:define>
		 

<TABLE cellpadding="0" class="thinlines" width="98%">
  <logic:equal name="smallheader" value="yes">
     <TR><TH colspan="9">-Taxa-</TH></TR>
  </logic:equal>
  <logic:notEqual name="smallheader" value="yes">
     <TR><TH colspan="9" class="major">Taxa occurring on this plot-observation</TH></TR>  
  </logic:notEqual>
  
  <bean:define id="stratawritten" value="no" />

  <TR><TH colspan="9">
    <!-- menu of plant names : -->
    <bean:define id="showTaxonNameDivID">taxonObservationof<bean:write name='observation_pk' /></bean:define>
    <%@ include file="sub_taxonimportance_showallplantnames_menu.jsp" %>
  </TH></TR>
  
  
  <bean:parameter id="strata2Show" name="strata2Show" value="2" /> <!-- default of 2 -->
  
  <bean:define id="attemptFailed" value="0"/>
  <logic:equal name="strata2Show" value="1">
      <!-- show with no strata -->
      <vegbank:get id="taxonimportance" select="taxonimportance_nostrata" where="where_taxonimportance_obsid_addsort" beanName="map" 
        wparam="observation_pk"  pager="false" perPage="-1" />  
      <!-- if empty then set strata2Show to 3 to show everything available -->
      <logic:empty name="taxonimportance-BEANLIST"> 
        <bean:define id="strata2Show" value="3" />
        <bean:define id="attemptFailed" value="1" />
      </logic:empty>
      
  </logic:equal>
 
  <!--     ################## STRATA ONLY ##################### -->
  <logic:equal name="strata2Show" value="2">
    <vegbank:get id="taxonimportance" select="taxonimportance_onlystrata" where="where_taxonimportance_obsid_addsort" 
      beanName="map"  wparam="observation_pk"  pager="false" perPage="-1" />
      <!-- if empty then set strata2Show to 3 to show everything available -->
      <logic:empty name="taxonimportance-BEANLIST"> 
        <bean:define id="strata2Show" value="3" />
        <bean:define id="attemptFailed" value="2" />
      </logic:empty>
  </logic:equal>
   
  <!-- show all records of taxImp -->
  <logic:equal name="strata2Show" value="3">
    <!-- show all : this always gets stuff -->
    <vegbank:get id="taxonimportance" select="taxonimportance" where="where_taxonimportance_obsid_addsort" 
    beanName="map" wparam="observation_pk"  pager="false" perPage="-1"  />
  </logic:equal>  

      <TR><TH colspan="9">Change Strata Shown:
        <select onchange="postNewParam('strata2Show',this.value)">
          <option value=''>--choose one--</option>
          <option value='2' <logic:equal name="strata2Show" value="2">selected="selected"</logic:equal>>show strata <logic:equal name="attemptFailed" value="2">(no strata on this plot)</logic:equal></option>
          <option value='1' <logic:equal name="strata2Show" value="1">selected="selected"</logic:equal>>show overall cover <logic:equal name="attemptFailed" value="1">(no overall cover on this plot)</logic:equal></option>
          <option value='3' <logic:equal name="strata2Show" value="3">selected="selected"</logic:equal>>show strata + overall</option>
        </select>  
      </TH></TR>
          
    <logic:empty name="taxonimportance-BEANLIST">
       <TR><TD colspan="2">No cover values for taxa on this plot: error!</TD></TR>
    </logic:empty> <!-- without strata -->
  
    <logic:notEmpty name="taxonimportance-BEANLIST">             
   
      <TR><TD colspan="9">
       <!-- sortable table: --> <!-- this ID is used above, in the "select plant name to view" combo box -->
       <table cellpadding="2" class="thinlines sortable" id="taxonObservationof<bean:write name='observation_pk' />">

         <tr><th>ord</th>
         <%@ include file="../autogen/taxonobservation_summary_head.jsp" %>
         <%@ include file="../autogen/taxonimportance_summary_head.jsp" %>
          <th class="table_stemsize">Stems:</th><th class="graphic_stemsize">Stem Diameters (graphically):</th>
         </tr>
    
         <logic:iterate id="onerowoftaxonimportance" name="taxonimportance-BEANLIST">
           <bean:define id="onerowoftaxonobservation" name="onerowoftaxonimportance" /> <!-- clone taximp -->
           <bean:define id="taxonimportance_pk" name="onerowoftaxonimportance" property="taxonimportance_id" />
        
        	 <logic:notEqual name="onerowoftaxonimportance" property="stratum_id_transl"
	       	  value='<%= lastStratum %>'>
	       	   <!-- change the color of this -->
	       	   <% strataGroup ++ ; %>
	       	   
	       	   <% if (( strataGroup == 13 )) 
	       	   {
	       	   
	       	     strataGroup = 1;
	       	   } 
	       	   %>
              <bean:define id="lastStratum">-all-</bean:define> <!--default value -->
              <logic:notEmpty name="onerowoftaxonimportance" property="stratum_id_transl">
                <bean:define id="lastStratum"><bean:write name="onerowoftaxonimportance" property="stratum_id_transl" /></bean:define>
              </logic:notEmpty>
	       	    
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
              <%@ include file="../autogen/taxonobservation_summary_data.jsp" %>
              <%@ include file="../autogen/taxonimportance_summary_data.jsp" %>
              <!-- start STEMS -->
              <td class="table_stemsize">
                 
                  <vegbank:get id="stemcount" select="stemcount" beanName="map" pager="false" 
                   where="where_stemcount_taxonimportance_fk" wparam="taxonimportance_pk" perPage="-1" 
                   allowOrderBy="true" orderBy="xorderby_stemdiameter_asc" />
                <bean:define id="graphicalStems" value="<!-- init -->" />
                
                
                <logic:empty name="stemcount-BEANLIST">
                  -none-
                </logic:empty>
                <logic:notEmpty name="stemcount-BEANLIST">
                 <table class="leftrightborders" cellpadding="2">
                  <tr>
                    <%@ include file="../autogen/stemcount_summary_head.jsp" %>
                  </tr>
                
                  <logic:iterate id="onerowofstemcount" name="stemcount-BEANLIST">
                
                    <tr class="@nextcolorclass@">
                      <%@ include file="../autogen/stemcount_summary_data.jsp" %>
                    </tr>
                    <!-- store graphical stems for later: -->
                   <bean:define id="graphicalStems"><bean:write name="graphicalStems" filter="false" />
                    <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="0">
                      <!-- define value for stem image: this is done because the scaling of round objects is a little funny.  these sizes are (somewhat) optimal -->
                      <bean:define id="stemPic" value="1" /> <!-- default for small ones -->
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="18"><bean:define id="stemPic" value="3" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="23"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="24"><bean:define id="stemPic" value="2" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="29"><bean:define id="stemPic" value="1" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="32"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="37"><bean:define id="stemPic" value="3" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="50"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="53"><bean:define id="stemPic" value="2" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="56"><bean:define id="stemPic" value="5" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="69"><bean:define id="stemPic" value="6" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="77"><bean:define id="stemPic" value="3" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="80"><bean:define id="stemPic" value="7" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="94"><bean:define id="stemPic" value="8" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="101"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="104"><bean:define id="stemPic" value="8" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="115"><bean:define id="stemPic" value="10" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="125"><bean:define id="stemPic" value="5" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="128"><bean:define id="stemPic" value="10" /></logic:greaterThan>
                      <img src="@images_link@stem_<bean:write name='stemPic' />sq.png" alt="stem" title="<bean:write name='onerowofstemcount' property='stemdiameter' /> cm" height="<bean:write name='onerowofstemcount' property='stemdiameter' />" width="<bean:write name='onerowofstemcount' property='stemdiameter' />" />
                      <bean:define id="themaxstems"><bean:write name="onerowofstemcount" property="stemcount" /></bean:define>
                      <logic:greaterThan name="onerowofstemcount" property="stemcount" value="1">
                        <!-- do again! -->
                        <!-- only iterates if more than one, even though this causes some duplication in this file.  This part most of the time omitted. -->
                        <!-- for (int i=2; i<= new Long(themaxstems).longValue(); i++) -->
                        <%
                         for (int i=2; i<= Long.parseLong(themaxstems); i++)
                         {
                        %>
                         <img src="@images_link@stem_<bean:write name='stemPic' />sq.png" alt="stem" title="<bean:write name='onerowofstemcount' property='stemdiameter' /> cm (#<%= i %>)" height="<bean:write name='onerowofstemcount' property='stemdiameter' />" width="<bean:write name='onerowofstemcount' property='stemdiameter' />" />
                        <%
                         }
                        %>
                      </logic:greaterThan>
                    </logic:greaterThan>
                   </bean:define>
                
                  </logic:iterate>
                 </table>
                </logic:notEmpty>
                
              
                <!-- end stems table -->
              </td>
              <!-- graphically display stems: -->
              <td class="graphic_stemsize">
                <logic:notEqual name="graphicalStems" value="<!-- init -->">
                 <table bgcolor="#FFFFFF" class="thinlines">
                   <tr><td>
                     <bean:write name="graphicalStems" filter="false" />
                   </td></tr>
                 </table>
                </logic:notEqual>
              </td>
            </tr><!-- end stems graphically-->
         </logic:iterate><!-- through taxonimportance-->
       </table>
      </TD></TR>  
     </logic:notEmpty>
   <bean:define id="stratawritten" value="yes" />
 
   <TR><TD colspan="9" class="bright sizetiny">This table is SORTABLE.  Click the headers to sort ascending and descending.</TD></TR>
</TABLE><!-- end of main table -->
  
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

