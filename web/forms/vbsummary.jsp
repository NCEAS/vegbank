<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HEAD>@defaultHeadToken@
 
<TITLE>Summary of Data in VegBank</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />


<meta http-equiv="Content-Type" content="text/html; charset=" />
</HEAD>
<BODY >@vegbank_header_html_normal@

<h3>Data Currently in VegBank </h3>
<table border="0" cellpadding="0" cellspacing="0" class="item">

<vegbank:get select="observation_summ" beanName="map"/>
 <logic:empty name="map">
  <tr><td colspan="2">ERROR! There are no observations!</td></tr>
 </logic:empty>
 <logic:notEmpty name="map">
   <tr>
     <td colspan="2" >Observations:</td>
     <td  align="right">&nbsp;<bean:write name="map" property="count_observation"/></td>
   </tr> 
 </logic:notEmpty>
 <vegbank:get select="place_summ" beanName="map" />
 <logic:notEmpty name="map">
 <!-- <tr><td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
  <td  colspan="2">
   <table class="item" border="1" cellpadding="0" cellspacing="0"> --><tr><TD/><th>Place</th><th># Obs</th></tr>
     <logic:iterate id="onerec" name="map" >
       <!-- loop over list of states -->
       <tr><TD/><td ><bean:write name="onerec" property="region_name"/>&nbsp;</td><td  align="right">&nbsp;<bean:write name="onerec" property="count_obs"/></td></tr>
     </logic:iterate>
   <!--</table></td>
  </tr> -->
 </logic:notEmpty> 
 <tr><td colspan="3"><hr/></td></tr>
<vegbank:get select="plantconcept_summ" beanName="map"/>
 <logic:notEmpty name="map">
   <tr>
     <td colspan="2">Plant Concepts:</td>
     <td align="right">&nbsp;<bean:write name="map" property="count_plantconcept"/></td>
   </tr> 
 </logic:notEmpty> 


<vegbank:get select="commconcept_summ" beanName="map"/>
 <logic:notEmpty name="map">
   <tr>
     <td colspan="2">Community Concepts:</td>
     <td align="right">&nbsp;<bean:write name="map" property="count_commconcept"/></td>
   </tr> 
 </logic:notEmpty> 

<vegbank:get select="party_summ" beanName="map"/>
 <logic:notEmpty name="map">
   <tr>
     <td colspan="2">Parties:</td>
     <td align="right">&nbsp;<bean:write name="map" property="count_party"/></td>
   </tr> 
 </logic:notEmpty>

<vegbank:get select="taxonobservation_summ" beanName="map"/>
 <logic:notEmpty name="map">
   <tr>
     <td colspan="2">Taxa observed on plots:</td>
     <td align="right">&nbsp;<bean:write name="map" property="count_taxonobservation"/></td>
   </tr> 
 </logic:notEmpty>
 <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
</table>

@vegbank_footer_html_tworow@
</BODY>
</html>
