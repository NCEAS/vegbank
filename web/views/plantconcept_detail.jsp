@stdvegbankget_jspdeclarations@


<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=">
@defaultHeadToken@
 
<TITLE>View VegBank Plant Concepts</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@ 

<h2>View Plant Concepts</h2>
  <vegbank:get id="concept" select="plantconcept" beanName="map" pager="true" xwhereEnable="true"/>

<vegbank:pager />
<logic:empty name="concept-BEANLIST">
             <p>Sorry, no Plant concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<table class="outsideborder" width="100%" cellpadding="0" cellspacing="0"><!--each field, only write when HAS contents-->
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->
<tr><th class="major_smaller" colspan="4"><bean:write name="onerow" property="plantname_id_transl"/> | <bean:write name="onerow" property="reference_id_transl"/></th></tr>

<tr>
<td colspan="4">
<span class="datalabelsmall">Name: </span><bean:write name="onerow" property="plantname_id_transl"/><br/>
<span class="datalabelsmall">Reference: </span><a href='@get_link@std/reference/<bean:write name="onerow" property="reference_idl"/>'><bean:write name="onerow" property="reference_id_transl"/></a><br/>
<logic:notEmpty name="onerow" property="plantdescription">
<span class="datalabelsmall">Description: </span><span class="largefield"><bean:write name="onerow" property="plantdescription"/>&nbsp;</span>
</logic:notEmpty>
<span class="datalabelsmall">Accession Code: </span><span class="largefield"><bean:write name="onerow" property="accessioncode"/></span>
</td>
</tr>

<bean:define id="concId" name="onerow" property="plantconcept_id"/>
<tr><td colspan="4"><span class="datalabelsmall">Plot-observations with this plant Concept:</span>
<vegbank:get id="observation" select="observation_count" 
  where="where_plantconcept_observation_complex" beanName="map" 
  wparam="concId" perPage="-1" pager="false" />
<logic:empty name="observation-BEAN">
-none-
</logic:empty>
<logic:notEmpty name="observation-BEAN">
<bean:write name="observation-BEAN" property="count_observations" />
<logic:notEqual name="observation-BEAN" property="count_observations" value="0">
<a href="@get_link@simple/observation/<bean:write name='concId' />?where=where_plantconcept_observation_complex">View 
  observation(s)</a>
</logic:notEqual>
</logic:notEmpty>


</td></tr>

<vegbank:get id="plantstatus" select="plantstatus" where="where_plantconcept_pk" beanName="map" wparam="concId" perPage="-1" pager="false"/>

<logic:notEmpty name="plantstatus-BEANLIST">
<logic:iterate id="statusbean" name="plantstatus-BEANLIST">
<bean:define id="thispartyacccode" name="statusbean" property="party_accessioncode" />
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="grey" colspan="3">
<!-- party perspective -->
<span class="datalabelsmall">Party Perspective according to: </span>
<a href="@get_link@std/party/<bean:write name='statusbean' property='party_id' />"><bean:write 
  name="statusbean" property="party_id_transl" /></a>
</td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td valign="top" class="grey" >
&nbsp;&nbsp;&nbsp;
<b><bean:write name="statusbean" property="plantconceptstatus" /></b>
<logic:notEmpty name="statusbean" property="plantlevel">
<br/>
&nbsp;&nbsp;&nbsp;
<span class="datalabelsmall">Level:</span>
<bean:write name="statusbean" property="plantlevel" />
</logic:notEmpty><!-- has level-->
<logic:notEmpty name="statusbean" property="plantparent_id">
<br/>
&nbsp;&nbsp;&nbsp;
<span class="datalabelsmall">Parent:</span><a href='@get_link@std/plantconcept/<bean:write name="statusbean" property="plantparent_id" />'><bean:write name="statusbean" property="plantparent_id_transl" /></a>
</logic:notEmpty><!-- has parent-->

<!-- end status -->
</td>
<!-- spacer grey -->
<td width="1px" bgcolor="#222222"><img src="@images_link@transparent.gif" width="1px" height="1px"/></td>
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
      href="http://plants.usda.gov/cgi_bin/plant_search.cgi?mode=Symbol&go=go&keywordquery=<bean:write name='usagebean' property='plantname_id_transl' />"
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
