@stdvegbankget_jspdeclarations@


<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=">
@defaultHeadToken@
 
<TITLE>View VegBank Plant Concepts</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>@vegbank_header_html_normal@ 

<h2>View Plant Concepts</h2>
  <vegbank:get id="concept" select="plantconcept" beanName="map" where="where_plantconcept_pk" pager="true"/>


<logic:empty name="concept-BEANLIST">
             <p>Sorry, no Plant concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->
<table width="95%" border="0" cellpadding="0" cellspacing="0" class="item"><!--each field, only write when HAS contents-->
<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->

<tr>
<td colspan="4"><p>
<span class="itemsmall">Name: </span><span class="item"><bean:write name="onerow" property="plantname_id_transl"/></span><br/>
<span class="itemsmall">Reference: </span><span class="item"><a href='@web_context@get/reference/<bean:write name="onerow" property="reference_id_transl"/>'><bean:write name="onerow" property="reference_id_transl"/></a></span><br/>
<logic:notEmpty name="onerow" property="plantdescription">
<span class="itemsmall">Description: </span><span class="item"><bean:write name="onerow" property="plantdescription"/>&nbsp;</span>
</logic:notEmpty>
</p></td>
</tr>
<logic:equal value="1" parameter="showdtl">
<bean:define id="concId" name="onerow" property="plantconcept_id"/>
<vegbank:get id="plantstatus" select="plantstatus" where="where_plantconcept_pk" beanName="map" wparam="concId" perPage="-1" pager="false"/>
<logic:notEmpty name="plantstatus-BEANLIST">
<logic:iterate id="statusbean" name="plantstatus-BEANLIST">
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td bgcolor="#CCCCCC" colspan="3">
<!-- party perspective -->
<p><span class="item"><i>Party Perspective according to: </i>
<bean:write name="statusbean" property="party_id_transl" /></span></p>
</td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td valign="top" bgcolor="#CCCCCC" ><p><span class="item">
&nbsp;&nbsp;&nbsp;
<b><bean:write name="statusbean" property="plantconceptstatus" /></b>
<logic:notEmpty name="statusbean" property="plantparent_id">
<br/>
&nbsp;&nbsp;&nbsp;
Parent:<a href='@web_context@get/plantconcept/<bean:write name="statusbean" property="plantparent_id" />'><bean:write name="statusbean" property="plantparent_id_transl" /></a>
</logic:notEmpty><!-- has parent-->
</span></p>
<!-- end status -->
</td>
<td bgcolor="#CCCCCC" valign="top"><!--usage --><p><span class="item">
  <!-- nested :get #3 -->
  <bean:define id="statId" name="statusbean" property="plantstatus_id"/>
  <vegbank:get id="plantusage" select="plantusage_std" where="where_plantstatus_pk" beanName="map" wparam="statId" perPage="-1" pager="false"/>  
  <logic:notEmpty name="plantusage-BEANLIST">
    <logic:iterate id="usagebean" name="plantusage-BEANLIST">
      <bean:write name="usagebean" property="classsystem" />:<bean:write name="usagebean" property="plantname_id_transl" /><br/>
    </logic:iterate>
  </logic:notEmpty>
  
  <!-- </span></p> 
</td>-->
<!-- get correlations -->
<!--<td bgcolor="#CCCCCC" valign="top"><p><span class="item">-->
  <!-- nested :get #4 -->
  <!-- current status already defined -->
  <vegbank:get id="plantcorrelation" select="plantcorrelation" where="where_plantstatus_pk" beanName="map" wparam="statId" perPage="-1" pager="false"/>
  <logic:notEmpty name="plantcorrelation-BEANLIST">
      
      <logic:iterate id="corrbean" name="plantcorrelation-BEANLIST">
        Synonym:(<bean:write name="corrbean" property="plantconvergence" />) 
        <a href='@web_context@get/plantconcept/<bean:write name="corrbean" property="plantconcept_id" />'>
        <bean:write name="corrbean" property="plantconcept_id_transl" /></a><br/>
      </logic:iterate>
  </logic:notEmpty>
  
</span></p></td>
</tr>
<!-- spacer b/t perspectives -->
<tr>
<td/><td colspan="3" bgcolor="#222222"><img src="@images_link@transparent.gif" height="1"/></td>
</tr>
</logic:iterate>
</logic:notEmpty> <!-- status -->


</logic:equal><!-- show detail -->
<tr><td colspan="4"><hr /></td></tr>
</logic:iterate>
</table>

 <!-- concept -->


</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @vegbank_footer_html_tworow@
