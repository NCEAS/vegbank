@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<% String rowClass = "evenrow"; %>

 
<TITLE>VegBank Data</TITLE>



<meta http-equiv="Content-Type" content="text/html; charset=" />

@webpage_masthead_html@
 

  <vegbank:get id="namedplace" select="place_summ" beanName="map" 
    pager="true" where="where_group_place_summ_desccount" wparam="1"/>
  <logic:notEmpty name="BEANLIST">

  <TABLE cellpadding="5">
  <TR><TD colspan="3"><h3>Where Plots Are:</h3></TD></TR>
  <TR valign="top"><TD>

  
  <table class="thinlines" cellpadding="2"> 
   <%@ include file="includeviews/sub_namedplace_states.jsp" %>
  
     
   </table>
   <p class="psmall"><a href="@forms_link@vbsummary.jsp">More Places...</a></p>
   <p class="psmall"><a href="notdoneyet">Add plots to VegBank</a></p>
 </logic:notEmpty> 
 </TD><TD>
 <bean:include id="mapfile" href="@forms_link@plot-map-northamerica-sml.html" />
 <bean:write name="mapfile" filter="false" />
 </TD></TR>
 
  <TR><TD><h3>Who contributed plots:</h3></TD><TD><h3>Data Projects:</h3></TR>
   <TR valign="top"><TD>
     <vegbank:get id="browseparty" select="browseparty" 
         beanName="map" pager="true"  xwhereEnable="false" />
     <bean:define id="onlytotalplots" value="yes" />
     <%@ include file="includeviews/sub_party_plotcount.jsp" %>
   <p class="psmall"><a href="@get_link@plotcount/party">More People...</a></p> 
   
  </logic:notEmpty> 
  </TD><TD>
  <!-- projects -->
    <vegbank:get id="project" select="project" beanName="map" pager="true" xwhereEnable="false" /> 
    <table class="thinlines" cellpadding="2">
      <tr><th>Project</th><th>Plots</th></tr>
      <logic:iterate id="onerowofproject" name="project-BEANLIST">
        <tr  class='@nextcolorclass@'>
          <td><a href='@get_link@std/project/<bean:write name="onerowofproject" property="project_id" />'><bean:write name="onerowofproject" property="projectname" /></a></td>
          <td class="numeric"><a href="@get_link@simple/observation/<bean:write name='onerowofproject' property='project_id' />?where=where_project_pk"><bean:write name="onerowofproject" property="countobs" /></a></td>
        </tr>
      </logic:iterate>
      
    </table>
  <p class="psmall"><a href="@get_link@std/project">More Projects...</a></p> 
 </TD></TR>
 <TR><TD><h3>Common Species:</h3></TD><TD><h3>Common Communities:</h3></TR>
  <TR valign="top"><TD><!-- spp -->
      <vegbank:get id="browsecommonplants" select="browsecommonplants" 
          beanName="map" pager="true"  xwhereEnable="false" />
     <table class="thinlines" cellpadding="2">
      <tr><th>Plant</th><th>Plots</th></tr>
      <logic:iterate id="onerowofbrowsecommonplants" name="browsecommonplants-BEANLIST">
	          <tr class='@nextcolorclass@'>
	            <td><a href="@get_link@std/plantconcept/<bean:write name='onerowofbrowsecommonplants' property='plantconcept_id' />"><bean:write name="onerowofbrowsecommonplants" property="plantconcept_id_transl" /></td>
	            <td class="numeric"><a href="@get_link@simple/observation/<bean:write name='onerowofbrowsecommonplants' property='plantconcept_id' />?where=where_plantconcept_observation_complex"><bean:write name="onerowofbrowsecommonplants" property="countobs" /></a></td>
	          </tr>
      </logic:iterate>
     
     </table>
     
    <!-- <p class="psmall"><a href="@get_link@plotcount/party">More...</a></p> -->
    
   
   </TD><TD>
   <!-- comms -->
   <vegbank:get id="browsecommoncomms" select="browsecommoncomms" 
             beanName="map" pager="true"  xwhereEnable="false" />
        <table class="thinlines" cellpadding="2">
         <tr><th>Community</th><th>Plots</th></tr>
         <logic:iterate id="onerowofbrowsecommoncomms" name="browsecommoncomms-BEANLIST">
   	          <tr class='@nextcolorclass@'>
   	            <td><a href="@get_link@std/commconcept/<bean:write name='onerowofbrowsecommoncomms' property='commconcept_id' />"><bean:write name="onerowofbrowsecommoncomms" property="commconcept_id_transl" /></a></td>
   	            <td class="numeric"><a href="@get_link@simple/observation/<bean:write name='onerowofbrowsecommoncomms' property='commconcept_id' />?where=where_commconcept_observation_complex"><bean:write name="onerowofbrowsecommoncomms" property="countobs" /></a></td>
   	          </tr>
         </logic:iterate>
        
        </table>
        
    <!-- <p class="psmall"><a href="@get_link@plotcount/party">More...</a></p> -->
   
   
 </TD></TR>
 
 
 </TABLE>


</body>
