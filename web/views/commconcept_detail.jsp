@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@




 
<TITLE>View VegBank Community Concepts - Detail</TITLE>
<script type="text/javascript">
function getHelpPageId() {
  return "view-comm-detail";
}

</script>



  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@ 

<h2>View Community Concepts - Detail</h2>

 <div id="tut_commcriteriamessage">
  <logic:present parameter="criteriaAsText">
    <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
    <logic:notEmpty name="bean_criteriaAsText">
      <p class="psmall">You searched for communities: 
      <bean:write name="bean_criteriaAsText" /></p>
    </logic:notEmpty>
  </logic:present>  
 </div>

<vegbank:get id="concept" select="commconcept" beanName="map" pager="true" xwhereEnable="true"/>
<vegbank:pager />
<logic:empty name="concept-BEANLIST">
             <p>Sorry, no community concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->

  <logic:present parameter="where"> <!-- only add these in custom where situations -->
  
     <!--data cart -->
      <!-- add all results -->
  
          <a href="javascript:addAllResults('commconcept');"
              title="add all query results to datacart" class="nobg"><img src="/vegbank/images/cart_star_on_blue2.gif" border="0" id="datacart-addallresults-icon" /></a>
  
          <a href="javascript:addAllResults('commconcept');"
              title="add all results to datacart">add all query results</a> to datacart,&nbsp;&nbsp;
  
          <a href="javascript:addAllOnPage()" title="add all on page to datacart" class="nobg"><img src="/vegbank/images/cart_add_one.gif" border="0" /></a>
          <a href="javascript:addAllOnPage()" title="add all on page">add plots on page</a> to datacart,&nbsp;&nbsp;
                                                                                                                                                                                                    
      <!-- drop page -->
  
          <a href="javascript:dropAllOnPage()" title="drop all on page from datacart" class="nobg"><img src="/vegbank/images/cart_drop_one.gif" border="0" /></a>
          <a href="javascript:dropAllOnPage()" title="drop all on page">drop plots on page</a> from datacart
   </logic:present>
<%@ include file="../includes/setup_rowindex.jsp" %>
      <logic:equal parameter="delta" value="findadd-accessioncode">
          <vegbank:datacart delta="findadd:commconcept:commconcept:commconcept_id:accessioncode" deltaItems="getQuery" display="false" />
    </logic:equal>
    
<form action="" method="GET" id="cartable">
<table class="outsideborder" width="100%" cellpadding="0" cellspacing="0"><!--each field, only write when HAS contents-->
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->

<!-- tutorial note: here, id's are defined for first iteration only.  After first iteration _iterated is appened to id, those will NOT BE UNIQUE -->
<tr id="tut_fullcomm<bean:write name='iterated' ignore='true'/>"><th class="major_smaller" colspan="4"><bean:write name="onerow" property="commname_id_transl"/> | <bean:write name="onerow" property="reference_id_transl"/></th></tr>

<logic:notEmpty name="onerow" property="accessioncode">
  <bean:define id="delta_ac" name="onerow" property="accessioncode" />
  <tr><td colspan="4">
       <%@ include file="../includes/datacart_checkbox.jsp" %> click to update datacart
  </td></tr>
</logic:notEmpty>  

<tr id="tut_communiversal<bean:write name='iterated' ignore='true'/>">
<td colspan="4">
  <span class="datalabelsmall">Name: </span>
  <bean:write name="onerow" property="commname_id_transl"/><br/>
  <span class="datalabelsmall">Reference: </span>
  <a href='@get_link@std/reference/<bean:write name="onerow" property="reference_id"/>'><bean:write name="onerow" property="reference_id_transl"/></a><br/>
 <logic:notEmpty name="onerow" property="commdescription">
   <span class="datalabelsmall">Description: </span>
   <span class="largefield"><bean:write name="onerow" property="commdescription"/>&nbsp;</span><br/>
 </logic:notEmpty>
  <span class="datalabelsmall">Accession Code: </span>
  <span class="largefield"><bean:write name="onerow" property="accessioncode"/></span><br/>


 <bean:define id="concId" name="onerow" property="commconcept_id"/>
 <span class="datalabelsmall">Plot-observations of this Community Concept:</span>
 <logic:equal name="onerow" property="d_obscount" value="0">
   <bean:write name="onerow" property="d_obscount" />
 </logic:equal>
 <logic:notEqual name="onerow" property="d_obscount" value="0">
   <bean:define id="newCritAsTxt">
     Interpreted as the community: <bean:write name="onerow" property="commname_id_transl"/> [<bean:write name="onerow" property="reference_id_transl"/>]
   </bean:define>
   <%  
    /* create a map of parameters to pass to the new link: */
    java.util.HashMap params = new java.util.HashMap();
    params.put("wparam", concId);
    params.put("where", "where_commconcept_observation_complex");
    params.put("criteriaAsText", newCritAsTxt);
    pageContext.setAttribute("paramsName", params);
   %>

   <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" > 
     <bean:write name="onerow" property="d_obscount" />
   </html:link>

  </logic:notEqual>
  
</td></tr>

<!-- check for only one status requested! -->
<logic:equal parameter="where" value="where_commstatus_ac">
  <!-- get only one status -->
  
  <vegbank:get id="commstatus" select="commstatus" where="where_ac" beanName="map" perPage="-1" pager="false"/> <!-- same wparam as URL -->
  <!-- show user that this is what is cited -->
  <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
  <td class="highlight" colspan="3">
  <strong>This perspective was requested: </strong> <a href="@get_link@detail/commconcept/<bean:write name='concId' />">show all party perspectives for this concept</a><br/>
  </td></tr>
</logic:equal>
<logic:notEqual parameter="where" value="where_commstatus_ac">
  <!-- get all statuses -->
  <vegbank:get id="commstatus" select="commstatus" where="where_commconcept_pk" beanName="map" wparam="concId" perPage="-1" pager="false"/>
</logic:notEqual>  
<logic:notEmpty name="commstatus-BEANLIST">
<logic:iterate id="statusbean" name="commstatus-BEANLIST">
<bean:define id="thispartyacccode" name="statusbean" property="party_accessioncode" /> <!-- get accCode of this party as bean -->
<bean:define id="thispartyid" name="statusbean" property="party_id" />
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="grey" colspan="3">
<!-- party perspective -->
<span class="datalabelsmall">Party Perspective according to: </span>
<a href='@get_link@std/party/<bean:write name="thispartyid" />'><bean:write name="statusbean" property="party_id_transl" /></a>
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
<tr id="tut_commpartyperspective<bean:write name='iterated' ignore='true'/>">
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

<td valign="top" class="grey" >
<ul class="compact">
 <li><span class="datalabelsmall">status: </span><strong><bean:write name="statusbean" property="commconceptstatus" /></strong>
 
 </li>
 <logic:notEmpty name="statusbean" property="commparent_id">
 <li>
   <img src="@images_link@uparr.gif" /><span class="datalabelsmall">Community's Parent: </span><a href='@get_link@std/commconcept/<bean:write name="statusbean" property="commparent_id" />'><bean:write name="statusbean" property="commparent_id_transl" /></a>
 </li>
 </logic:notEmpty><!-- has parent-->

 <logic:notEmpty name="statusbean" property="commlevel">
 <li>
  <span class="datalabelsmall">This Community's Level:</span>
  <bean:write name="statusbean" property="commlevel" />
 </li></logic:notEmpty><!-- has level-->

 <li>
    
    <!-- now show any children, which is inverted lookup -->
    <img src="@images_link@downarr.gif" /><span class="datalabelsmall">This Community's Children: </span>
    
    <!-- create complex wparam, using concept_ID ; party_id -->
    
    <vegbank:get id="commchildren" select="commchildren" where="where_commchildren" 
      wparam="<%= concId + Utility.PARAM_DELIM + thispartyid %>" 
      perPage="-1" pager="false" beanName="map" />
    <logic:notEmpty name="commchildren-BEANLIST">  
      <bean:define id="thisdivid">commchildren-for-<bean:write name="statusbean" property="commstatus_id" /></bean:define>
     <span class="datalabelsmall"> <a href="javascript:showorhidediv('<%= thisdivid %>')">Show/Hide</a> </span> <br/>
   <div id='commchildren-for-<bean:write name="statusbean" property="commstatus_id" />'>
    <logic:iterate id="onerowofcommchildren" name="commchildren-BEANLIST">
      <ul class="compact">
      <li>&nbsp;&nbsp;<a href='@get_link@std/commconcept/<bean:write name="onerowofcommchildren" property="commconcept_id" />'><bean:write name="onerowofcommchildren" property="commconcept_id_transl" /></a></li>
      </ul>
    </logic:iterate>

   </div>
   </logic:notEmpty>
   <logic:empty name="commchildren-BEANLIST">
    [none]
   </logic:empty>
  </li>
  </ul>
<!-- end status -->
</td>
<!-- spacer grey -->
<td bgcolor="#222222"><img src="@images_link@transparent.gif" width="1" height="1"/></td>
<td class="grey" valign="top"><!--usage -->
  <!-- nested :get #3 -->
  <bean:define id="statId" name="statusbean" property="commstatus_id"/>
  <vegbank:get id="commusage" select="commusage_std" where="where_commstatus_pk" beanName="map" wparam="statId" perPage="-1" pager="false"/>  
  <logic:notEmpty name="commusage-BEANLIST">
    <strong>Names: </strong>
    <logic:iterate id="usagebean" name="commusage-BEANLIST">
      &nbsp;&nbsp;<span class="datalabelsmall"><bean:write name="usagebean" property="classsystem" />:</span> <bean:write name="usagebean" property="commname_id_transl" />
      <logic:equal name="thispartyacccode" value="VB.Py.512.NATURESERVE">
	          <!-- only link if party is NatureServe -->
	          <logic:equal name="usagebean" property="classsystem" value="UID">
	             <!-- and this is a UID -->  <!-- and is accepted -->
	             <logic:equal name="statusbean" property="commconceptstatus" value="accepted">
	               <a target="_new" 
	  href="http://www.natureserve.org/explorer/servlet/NatureServe?searchCommunityUid=<bean:write name='usagebean' property='commname_id_transl' />"
	  title="An authoritative source for information on the plants, animals, and ecological communities of the United States and Canada. 	      NatureServe Explorer is a product of NatureServe and its network of natural heritage member programs.">NatureServe 
	  Explorer<img border="0" src="@image_server@natureserveexplorer.png" alt="NatureServe Explorer logo" /></a>
	             </logic:equal>     
	          </logic:equal>  
      </logic:equal>
      
      <br/>
    </logic:iterate>
  </logic:notEmpty>
   <!-- nested :get #4 -->
    <!-- current status already defined -->
    <vegbank:get id="commcorrelation" select="commcorrelation" where="where_commstatus_pk" beanName="map" wparam="statId" perPage="-1" pager="false"/>
    <logic:notEmpty name="commcorrelation-BEANLIST">
        <strong>(convergence) and Synonyms: </strong><br/>
        <logic:iterate id="corrbean" name="commcorrelation-BEANLIST">
          <span class="datalabelsmall">(<bean:write name="corrbean" property="commconvergence" />)</span> 
          <a href='@get_link@std/commconcept/<bean:write name="corrbean" property="commconcept_id" />'>
          <bean:write name="corrbean" property="commconcept_id_transl" /></a><br/>
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
<bean:define id='iterated' value='_iterated'/> <!-- for tutorial -->
</logic:iterate>
</table>
</form>
@mark_datacart_items@


 <!-- concept -->


</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @webpage_footer_html@
