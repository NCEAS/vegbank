@stdvegbankget_jspdeclarations@

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HEAD>@defaultHeadToken@
 
<TITLE>Summary of Data in VegBank</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />


<meta http-equiv="Content-Type" content="text/html; charset=" />
</HEAD>
<body>@vegbank_header_html_normal@

<h1>Data Currently in VegBank </h1>
<div align="center">
<table class="leftrightborders" cellpadding="2">
<% String rowClass = "evenrow"; %>
<vegbank:get select="observation_summ" beanName="map" />
 <logic:empty name="BEANLIST">
  <tr class="@nextcolorclass@"><td colspan="2">ERROR! There are no observations!</td></tr>
 </logic:empty>
 <logic:notEmpty name="BEANLIST">
   <tr class="@nextcolorclass@">
     <td colspan="2" >Plot-Observations:</td>
     <td  align="right">&nbsp;<bean:write name="BEAN" property="count_observation"/></td>
   </tr> 
 </logic:notEmpty>

<vegbank:get select="plantconcept_summ" beanName="map"/>
 <logic:notEmpty name="BEANLIST">
   <tr class="@nextcolorclass@">
     <td colspan="2">Plant Concepts:</td>
     <td align="right">&nbsp;<bean:write name="BEAN" property="count_plantconcept"/></td>
   </tr> 
 </logic:notEmpty> 


<vegbank:get select="commconcept_summ" beanName="map"/>
 <logic:notEmpty name="BEANLIST">
   <tr class="@nextcolorclass@">
     <td colspan="2">Community Concepts:</td>
     <td align="right">&nbsp;<bean:write name="BEAN" property="count_commconcept"/></td>
   </tr> 
 </logic:notEmpty> 

<vegbank:get select="party_summ" beanName="map"/>
 <logic:notEmpty name="BEANLIST">
   <tr class="@nextcolorclass@">
     <td colspan="2">Parties:</td>
     <td align="right">&nbsp;<bean:write name="BEAN" property="count_party"/></td>
   </tr> 
 </logic:notEmpty>

<vegbank:get select="taxonobservation_summ" beanName="map"/>
 <logic:notEmpty name="BEANLIST">
   <tr class="@nextcolorclass@">
     <td colspan="2">Taxa observed on plots:</td>
     <td align="right">&nbsp;<bean:write name="BEAN" property="count_taxonobservation"/></td>
   </tr> 
 </logic:notEmpty>
 <!--- <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr> -->
</table>



 <vegbank:get id="namedplace" select="place_summ" beanName="map" perPage="-1" pager="false" where="where_group_place_summ" wparam="1"/>
 <logic:notEmpty name="BEANLIST">
<h2>Geographical Distribution of Observations</h2>

   <table class="thinlines" cellpadding="2"> 
   <%@ include file="../views/includeviews/sub_namedplace_states.jsp" %>
  
  
 </logic:notEmpty> 
<%@ include file="plot-map-northamerica.html" %>



</div>

@vegbank_footer_html_tworow@
</BODY>
</html>
