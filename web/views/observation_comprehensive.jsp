<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>   <%     String rowClass = "evenrow";  %>
<head>@defaultHeadToken@<title>VegBank Comprehensive Plot-Observation View</title><link REL=STYLESHEET HREF="@stylesheet@" TYPE="text/css"></head>
<body>  @vegbank_header_html_normal@   @possibly_center@  
  <h2>Comprehesive View of a Plot-Observation</h2>
  <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>
<vegbank:pager />
<logic:empty name="plotobs-BEANLIST"> Sorry, no plot-observations are available. </logic:empty>
<NOPE logic:notEmpty name="plotobs-BEANLIST" ><!-- set up table -->
<NOPE logic:iterate id="onerowofobservation" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofobservation" name="plotobs-BEAN" />
<bean:define id="onerowofplot" name="plotobs-BEAN" />
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
<!-- start of plot & obs fields-->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2">
<TR><TD width="55%" valign="top"><!-- plot level info -->
<table class="leftrightborders" cellpadding="2"><!--each field, only write when HAS contents-->
<tr><th class="major" colspan="2">Plot Level Data: <bean:write name="onerowofplot" property="authorplotcode"/></th></tr>
<tr><th>Plot ID Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotidlong_data.jsp" %>         
<%@ include file="autogen/observation_plotidlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Location Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotloclong_data.jsp" %>
<%@ include file="autogen/observation_plotloclong_data.jsp" %>
<%@ include file="includeviews/sub_place.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Layout Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotlayoutlong_data.jsp" %>
<%@ include file="autogen/observation_plotlayoutlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Environment Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotenvlong_data.jsp" %>
<%@ include file="autogen/observation_plotenvlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Methods Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotmethodlong_data.jsp" %>
<%@ include file="autogen/observation_plotmethodlong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Plot quality Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotqualitylong_data.jsp" %>
<%@ include file="autogen/observation_plotqualitylong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Overall Plot Vegetation Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotoverallveglong_data.jsp" %>
<%@ include file="autogen/observation_plotoverallveglong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Misc Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> 
<%@ include file="autogen/plot_plotmisclong_data.jsp" %>
<%@ include file="autogen/observation_plotmisclong_data.jsp" %>
<%@ include file="includeviews/sub_haddata.jsp" %>
<!-- end of plot/obs fields -->
</table>
</TD><TD valign="top"><!-- plants in this plot -->
<%@ include file="includeviews/sub_taxonobservation.jsp" %>


<vegbank:get id="stemcount" select="stemcount" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<logic:notEmpty name="stemcount-BEANLIST">
<br/><br/>
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="9">Stems:</th></tr>

<tr>
<th>Plant Name</th>  <%@ include file="autogen/stemcount_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofstemcount" name="stemcount-BEANLIST">
<tr class="@nextcolorclass@">
<td><a href="@get_link@std/taxonobservation/<bean:write name='onerowofstemcount' property='taxonobservation_id' />"><bean:write name="onerowofstemcount" property="authorplantname" /></a></td>
<%@ include file="autogen/stemcount_summary_data.jsp" %>
</tr>
</logic:iterate>

</table>
</logic:notEmpty>

</TD>
</TR><TR><TD colspan="2" valign="top" align="left">
<!-- community info -->
<vegbank:get id="commclass" select="commclass" beanName="map" 
    where="where_observation_pk" wparam="observation_pk" pager="false"/>
<%@ include file="includeviews/sub_commclass_summary.jsp" %>

<vegbank:get id="soilobs" select="soilobs" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<logic:notEmpty name="soilobs-BEANLIST">
<br/><br/>
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="19">Soil Observations:</th></tr>
<tr>
<%@ include file="autogen/soilobs_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofsoilobs" name="soilobs-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/soilobs_summary_data.jsp" %>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>

<vegbank:get id="disturbanceobs" select="disturbanceobs" beanName="map" pager="false" where="where_observation_pk" wparam="observation_pk" perPage="-1" />
<logic:notEmpty name="disturbanceobs-BEANLIST">
<br/><br/>
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="9">Disturbance Data:</th></tr>
<tr>
<%@ include file="autogen/disturbanceobs_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofdisturbanceobs" name="disturbanceobs-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/disturbanceobs_summary_data.jsp" %>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>
</TD>
</TR>
<TR><TD colspan="2"><hr noshade="true"/><br/></TD></TR>
</TABLE>
<NOPE /logic:iterate NOPE>
<vegbank:pager/>
<NOPE /logic:notEmpty NOPE><br>
@vegbank_footer_html_tworow@</BODY></html>