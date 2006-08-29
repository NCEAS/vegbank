@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@
 
<TITLE>VegBank Plant Concept Detail</TITLE>
<script type="text/javascript">
function getHelpPageId() {
  return "view-plant-detail";
}

</script>

 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@ 

<h2>Plant Concept Detail</h2>
  <vegbank:get id="concept" select="plantconcept" beanName="map" pager="true" xwhereEnable="true"
   allowOrderBy="true" orderBy="xorderby_plantname"
    />


<logic:empty name="concept-BEANLIST">
             <p>Sorry, no Plant concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<!-- if special first letter query, then show that it is and show other options: -->
<bean:parameter id="prm_where" name="where" value="n/a" />
 <div id="tut_plantcriteriamessage">
   <logic:present parameter="criteriaAsText">
     <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
     <logic:notEmpty name="bean_criteriaAsText">
       <p class="psmall" >You searched for plants: <bean:write name="bean_criteriaAsText" /></p>
   
   
     </logic:notEmpty>
  </logic:present>  
 
<logic:equal name="prm_where" value="where_plantconcept_firstletter">
  <!-- tell what we are showing: -->
  <bean:parameter id="prm_wparam" name="wparam" value="?" />
  <p>
  <%@ include file="../includes/menu-plants-byletter.jsp" %>
 <br/> Showing Plants starting with "<bean:write name="prm_wparam" />"
  </p>

</logic:equal>
  </div>
  <logic:present parameter="where"> <!-- only add these in custom where situations -->
  
     <!--data cart -->
      <!-- add all results -->
  
          <a href="javascript:addAllResults('plantconcept');"
              title="add all query results to datacart" class="nobg"><img src="/vegbank/images/cart_star_on_blue2.gif" border="0" id="datacart-addallresults-icon" /></a>
  
          <a href="javascript:addAllResults('plantconcept');"
              title="add all results to datacart">add all query results</a> to datacart,&nbsp;&nbsp;
  
          <a href="javascript:addAllOnPage()" title="add all on page to datacart" class="nobg"><img src="/vegbank/images/cart_add_one.gif" border="0" /></a>
          <a href="javascript:addAllOnPage()" title="add all on page">add plots on page</a> to datacart,&nbsp;&nbsp;
                                                                                                                                                                                                    
      <!-- drop page -->
  
          <a href="javascript:dropAllOnPage()" title="drop all on page from datacart" class="nobg"><img src="/vegbank/images/cart_drop_one.gif" border="0" /></a>
          <a href="javascript:dropAllOnPage()" title="drop all on page">drop plots on page</a> from datacart
   </logic:present>
<%@ include file="../includes/setup_rowindex.jsp" %>
      <logic:equal parameter="delta" value="findadd-accessioncode">
          <vegbank:datacart delta="findadd:plantconcept:plantconcept:plantconcept_id:accessioncode" deltaItems="getQuery" display="false" />
    </logic:equal>
  
 <vegbank:pager />
  
  
<form action="" method="GET" id="cartable">
<table class="outsideborder" width="100%" cellpadding="0" cellspacing="0"><!--each field, only write when HAS contents-->
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->

<!-- tutorial note: here, id's are defined for first iteration only.  After first iteration _iterated is appened to id, those will NOT BE UNIQUE -->
<tr id="tut_fullplant<bean:write name='iterated' ignore='true'/>"><th class="major_smaller" colspan="4"><bean:write name="onerow" property="plantname_id_transl"/> [<bean:write name="onerow" property="reference_id_transl"/>]</th></tr>

<bean:define id="delta_ac" name="onerow" property="accessioncode" />
<tr><td colspan="4">
       <%@ include file="../includes/datacart_checkbox.jsp" %> click to update datacart
</td></tr>

<tr id="tut_plantuniversal<bean:write name='iterated' ignore='true'/>">
<td colspan="4">
<span class="datalabelsmall">Name: </span><bean:write name="onerow" property="plantname_id_transl"/><br/>
<span class="datalabelsmall">Reference: </span><a href='@get_link@std/reference/<bean:write name="onerow" property="reference_id"/>'><bean:write name="onerow" property="reference_id_transl"/></a><br/>
<logic:notEmpty name="onerow" property="plantdescription">
<span class="datalabelsmall">Description: </span><span class="largefield"><bean:write name="onerow" property="plantdescription"/>&nbsp;</span>
</logic:notEmpty>
<span class="datalabelsmall">Accession Code: </span><span class="largefield"><bean:write name="onerow" property="accessioncode"/></span>
<br/>

<bean:define id="concId" name="onerow" property="plantconcept_id"/>
<span class="datalabelsmall">Plot-observations with this plant Concept:</span>
  <logic:equal name="onerow" property="d_obscount" value="0">
    <bean:write name="onerow" property="d_obscount" />
  </logic:equal>

  <logic:notEqual name="onerow" property="d_obscount" value="0">
    <bean:define id="newCritAsTxt">
    With the plant: <bean:write name="onerow" property="plantname_id_transl"/> [<bean:write name="onerow" property="reference_id_transl"/>]
    </bean:define>
    <%  
        /* create a map of parameters to pass to the new link: */
        java.util.HashMap params = new java.util.HashMap();
        params.put("wparam", concId);
        params.put("where", "where_plantconcept_observation_complex");
        params.put("criteriaAsText", newCritAsTxt);
        pageContext.setAttribute("paramsName", params);
    %>
    
    <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page">
      <bean:write name="onerow" property="d_obscount" />
    </html:link>

  </logic:notEqual>

</td></tr>

<!-- check for only one status requested! -->
<logic:equal parameter="where" value="where_plantstatus_ac">
  <!-- get only one status -->
  
  <vegbank:get id="plantstatus" select="plantstatus" where="where_ac" beanName="map" perPage="-1" pager="false"/> <!-- same wparam as URL -->
  <!-- show user that this is what is cited -->
  <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
  <td class="highlight" colspan="3">
  <strong>This perspective was requested: </strong> <a href="@get_link@detail/plantconcept/<bean:write name='concId' />">show all party perspectives for this concept</a><br/>
  </td></tr>
</logic:equal>
<logic:notEqual parameter="where" value="where_plantstatus_ac">
  <!-- get all statuses -->
  <vegbank:get id="plantstatus" select="plantstatus" where="where_plantconcept_pk" beanName="map" wparam="concId" perPage="-1" pager="false"/>
</logic:notEqual> 

<logic:notEmpty name="plantstatus-BEANLIST">
<logic:iterate id="statusbean" name="plantstatus-BEANLIST">
<bean:define id="thispartyacccode" name="statusbean" property="party_accessioncode" />
<bean:define id="thispartyid" name="statusbean" property="party_id" />

<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="grey" colspan="3">
<!-- party perspective -->
<span class="datalabelsmall">Party Perspective according to: </span>
<a href="@get_link@std/party/<bean:write name='statusbean' property='party_id' />"><bean:write 
  name="statusbean" property="party_id_transl" /></a>
  <!-- state dates of this perspective -->
  <br/><span class="datalabelsmall">Perspective from:</span>
     <strong>
      <span title="<bean:write name='statusbean' property='startdate' />">
        <dt:format pattern="dd-MMM-yyyy">
          <dt:parse pattern="yyyy-MM-dd">
            <bean:write name='statusbean' property='startdate_datetrunc' />
          </dt:parse>
        </dt:format>
      </span>
     </strong>
     <span class="datalabelsmall">to:</span>
     <strong>
         <logic:empty name='statusbean' property='stopdate'>
           ongoing
         </logic:empty>
         <logic:notEmpty name='statusbean' property='stopdate'>
         <span title="<bean:write name='statusbean' property='stopdate' />">
           <dt:format pattern="dd-MMM-yyyy">
             <dt:parse pattern="yyyy-MM-dd">
               <bean:write name='statusbean' property='stopdate_datetrunc' />
             </dt:parse>
           </dt:format>
         </span>
         </logic:notEmpty>
   </strong>
  
</td></tr>
<tr id="tut_plantpartyperspective<bean:write name='iterated' ignore='true'/>"><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td valign="top" class="grey" >
<ul class="compact">
<li><span class="datalabelsmall">Status:</span>
<b><bean:write name="statusbean" property="plantconceptstatus" /></b>
</li>
<logic:notEmpty name="statusbean" property="plantparent_id">
<li>

<img src="@images_link@uparr.gif" /><span class="datalabelsmall">Plant's Parent:</span><a href='@get_link@std/plantconcept/<bean:write name="statusbean" property="plantparent_id" />'><bean:write name="statusbean" property="plantparent_id_transl" /></a>
</li>
</logic:notEmpty><!-- has parent-->


<logic:notEmpty name="statusbean" property="plantlevel">
<li>

<span class="datalabelsmall">This Plant's Level:</span>
<bean:write name="statusbean" property="plantlevel" />
</li>
</logic:notEmpty><!-- has level-->

<li>
<!-- now show any children, which is inverted lookup -->
 	     <img src="@images_link@downarr.gif" /><span class="datalabelsmall">This Plant's Children: </span>
 	     
 	     <!-- create complex wparam, using concept_ID ; party_id -->
 	     
 	     <vegbank:get id="plantchildren" select="plantchildren" where="where_plantchildren"
 	       wparam="<%= concId + Utility.PARAM_DELIM + thispartyid %>"
 	       perPage="-1" pager="false" beanName="map" />
 	     <logic:notEmpty name="plantchildren-BEANLIST">  
 	      
          <bean:define id="thisdivid">plantchildren-for-<bean:write name="statusbean" property="plantstatus_id" /></bean:define>
          <span class="datalabelsmall"> <a href="javascript:showorhidediv('<%= thisdivid %>')">Show/Hide</a> </span> <br/>
 	    <div id='plantchildren-for-<bean:write name="statusbean" property="plantstatus_id" />'>
 	     <logic:iterate id="onerowofplantchildren" name="plantchildren-BEANLIST">
 	       <ul class="compact">
 	       <li>&nbsp;&nbsp;<a href='@get_link@std/plantconcept/<bean:write name="onerowofplantchildren" property="plantconcept_id" />'><bean:write name="onerowofplantchildren" property="plantconcept_id_transl" /></a></li>
 	       </ul>
 	     </logic:iterate>
 	 
 	    </div>
 	    </logic:notEmpty>
 	    <logic:empty name="plantchildren-BEANLIST">
 	     [none]
 	    </logic:empty>
</li>
</ul>



<!-- end status -->
</td>
<!-- spacer grey -->
<td width="1" bgcolor="#222222"><img src="@images_link@transparent.gif" width="1" height="1"/></td>
<td class="grey" valign="top"><!--usage -->
  <!-- nested :get #3 -->
  <bean:define id="statId" name="statusbean" property="plantstatus_id"/>
  <vegbank:get id="plantusage" select="plantusage_std" where="where_plantstatus_pk" beanName="map" wparam="statId" perPage="-1" pager="false"/>  
  <logic:notEmpty name="plantusage-BEANLIST">
    <strong>Names: </strong>
    <logic:iterate id="usagebean" name="plantusage-BEANLIST">
      <span class="datalabelsmall"><bean:write name="usagebean" property="classsystem" />:</span> 
      <bean:write name="usagebean" property="plantname_id_transl" />
      <logic:equal name="thispartyacccode" value="VB.Py.511.USDANRCSPLANTS2">
        <!-- only link if party is USDA -->
        <logic:equal name="usagebean" property="classsystem" value="Code">
           <!-- and this is a code -->  
           <a target="_new" 
      href="http://plants.usda.gov/java/nameSearch?mode=Symbol&keywordquery=<bean:write name='usagebean' property='plantname_id_transl' />"
      title="See this plant in the online USDA PLANTS database in a new window">USDA 
      PLANTS Profile<img border="0" src="@image_server@leaficon.gif" alt="USDA PLANTS logo" /></a>
           
        </logic:equal>  
      </logic:equal>
      <br/>
    </logic:iterate>
  </logic:notEmpty>
  
  <!--  
</td>-->
<!-- get correlations -->
<!--<td class="grey" valign="top">-->
  <!-- nested :get #4 -->
  <!-- current status already defined -->
  <vegbank:get id="plantcorrelation" select="plantcorrelation" where="where_plantstatus_pk" beanName="map" wparam="statId" perPage="-1" pager="false"/>
  <logic:notEmpty name="plantcorrelation-BEANLIST">
      <strong>(convergence) and Synonyms: </strong><br/>
      <logic:iterate id="corrbean" name="plantcorrelation-BEANLIST">
        <span class="datalabelsmall">(<bean:write name="corrbean" property="plantconvergence" />)</span> 
        <a href='@get_link@std/plantconcept/<bean:write name="corrbean" property="plantconcept_id" />'>
        <bean:write name="corrbean" property="plantconcept_id_transl" /></a><br/>
      </logic:iterate>
  </logic:notEmpty>
  
</td>
</tr>
<!-- spacer b/t perspectives -->
<tr>
<td></td><td colspan="3" bgcolor="#222222"><img src="@images_link@transparent.gif" height="1"/></td>
</tr>
<bean:define id='iterated' value='_iterated'/> <!-- for tutorial -->
</logic:iterate>
</logic:notEmpty> <!-- status -->



<tr><td colspan="4">&nbsp;<!--<hr />--></td></tr>
<!-- for tutorial -->
<bean:define id='iterated' value='_iterated'/>
</logic:iterate> <!-- plant -->
</table>
</form>
  @mark_datacart_items@

 <!-- concept -->


</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @webpage_footer_html@
