@stdvegbankget_jspdeclarations@


<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=">
@defaultHeadToken@
 
<TITLE>View VegBank Community Concepts - Detail</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@ 

<h2>View Community Concepts - Detail</h2>

<vegbank:get id="concept" select="commconcept" beanName="map" pager="true" xwhereEnable="true"/>
<vegbank:pager />
<logic:empty name="concept-BEANLIST">
             <p>Sorry, no community concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<table class="outsideborder" width="100%" cellpadding="0" cellspacing="0"><!--each field, only write when HAS contents-->
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->
<tr><th class="major_smaller" colspan="4"><bean:write name="onerow" property="commname_id_transl"/> | <bean:write name="onerow" property="reference_id_transl"/></th></tr>
<tr>
<td colspan="4">
<span class="datalabelsmall">Name: </span><bean:write name="onerow" property="commname_id_transl"/><br/>
<span class="datalabelsmall">Reference: </span><a href='@get_link@std/reference/<bean:write name="onerow" property="reference_id"/>'><bean:write name="onerow" property="reference_id_transl"/></a><br/>
<logic:notEmpty name="onerow" property="commdescription">
<span class="datalabelsmall">Description: </span><span class="largefield"><bean:write name="onerow" property="commdescription"/>&nbsp;</span><br/>
<span class="datalabelsmall">Accession Code: </span><span class="largefield"><bean:write name="onerow" property="accessioncode"/></span>
</logic:notEmpty>
</td>
</tr>


<bean:define id="concId" name="onerow" property="commconcept_id"/>

<tr><td colspan="4"><span class="datalabelsmall">Plot-observations of this Community Concept:</span>
<vegbank:get id="observation" select="observation_count" 
  where="where_commconcept_observation_complex" 
  wparam="concId" perPage="-1" pager="false" beanName="map"  />
<logic:empty name="observation-BEAN">
-none-
</logic:empty>
<logic:notEmpty name="observation-BEAN">
<bean:write name="observation-BEAN" property="count_observations" />
<logic:notEqual name="observation-BEAN" property="count_observations" value="0">
<a href="@get_link@simple/observation/<bean:write name='concId' />?where=where_commconcept_observation_complex">View 
  observation(s)</a>
</logic:notEqual>
</logic:notEmpty>


</td></tr>


<vegbank:get id="commstatus" select="commstatus" where="where_commconcept_pk" beanName="map" wparam="concId" perPage="-1" pager="false"/>
<logic:notEmpty name="commstatus-BEANLIST">
<logic:iterate id="statusbean" name="commstatus-BEANLIST">
<bean:define id="thispartyacccode" name="statusbean" property="party_accessioncode" /> <!-- get accCode of this party as bean -->
<bean:define id="thispartyid" name="statusbean" property="party_id" />
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="grey" colspan="3">
<!-- party perspective -->
<span class="datalabelsmall">Party Perspective according to: </span>
<a href='@get_link@std/party/<bean:write name="thispartyid" />'><bean:write name="statusbean" property="party_id_transl" /></a>
</td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

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
      
     <span class="datalabelsmall"> <a href="javascript:showorhidediv('commchildren-for-<bean:write name="statusbean" property="commstatus_id" />')">Show/Hide</a> </span> </br>
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
<td/><td colspan="3" bgcolor="#222222"><img src="@images_link@transparent.gif" height="1"/></td>
</tr>
</logic:iterate>
</logic:notEmpty> <!-- status -->



<tr><td colspan="4">&nbsp;<!--<hr />--></td></tr>
</logic:iterate>
</table>

 <!-- concept -->


</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @vegbank_footer_html_tworow@
