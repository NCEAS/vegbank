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
<vegbank:get id="commstatus" select="commstatus" where="where_commconcept_pk" beanName="map" wparam="concId" perPage="-1" pager="false"/>
<logic:notEmpty name="commstatus-BEANLIST">
<logic:iterate id="statusbean" name="commstatus-BEANLIST">
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="grey" colspan="3">
<!-- party perspective -->
<span class="datalabelsmall">Party Perspective according to: </span>
<bean:write name="statusbean" property="party_id_transl" />
</td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td valign="top" class="grey" >
&nbsp;&nbsp;&nbsp;
<span class="datalabelsmall">status: </span><strong><bean:write name="statusbean" property="commconceptstatus" /></strong>
<logic:notEmpty name="statusbean" property="commlevel">
<br/>
&nbsp;&nbsp;&nbsp;
<span class="datalabelsmall">Level:</span>
<bean:write name="statusbean" property="commlevel" />
</logic:notEmpty><!-- has level-->
<logic:notEmpty name="statusbean" property="commparent_id">
<br/>
&nbsp;&nbsp;&nbsp;
<span class="datalabelsmall">Parent: </span><a href='@get_link@std/commconcept/<bean:write name="statusbean" property="commparent_id" />'><bean:write name="statusbean" property="commparent_id_transl" /></a>
</logic:notEmpty><!-- has parent-->


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
      &nbsp;&nbsp;<span class="datalabelsmall"><bean:write name="usagebean" property="classsystem" />:</span> <bean:write name="usagebean" property="commname_id_transl" /><br/>
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
