<?xml version="1.0"?><!-- set up RDF main element -->
<!DOCTYPE rdf:RDF [<!ENTITY xsd "http://www.w3.org/2001/XMLSchema#">]>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
            xmlns:vegbank="http://vegbank.org/rdf/v1/"
            xmlns:observation="http://vegbank.org/rdf/v1/observation"
            xmlns:plot="http://vegbank.org/rdf/v1/plot"
            xmlns:comminterpretation="http://vegbank.org/rdf/v1/comminterpretation"
            >
<!--@ webpage_top_html @-->
  @stdvegbankget_jspdeclarations@

  <!--@ webpage_head_html @-->
  <!--@ ajax_js_include @ -->
  <!--@ datacart_js_include @ -->

  <!-- include script for sorting tables -->
  <!--script language="javascript" src="@includes_link@sort_table.js"></script-->


<!--@ webpage_masthead_html @-->

<vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk"
    whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true"
    perPage="1000"
    allowOrderBy="false" />  <!-- save="plot-search-results" /-->


<bean:define id="vbTag">vegbank:</bean:define>
<bean:define id="obsTag">observation:</bean:define>
<bean:define id="plotTag">plot:</bean:define>

<logic:notEmpty name="plotobs-BEANLIST"><!-- set up file -->
<!-- iterate over all records in set : new table for each -->
<logic:iterate id="onerowofobservation" name="BEANLIST">
  <<bean:write name="vbTag"/>observation rdf:ID="<bean:write name="onerowofobservation" property="observationaccessioncode"/>" rdf:resource="http://vegbank.org/cite/<bean:write name="onerowofobservation" property="observationaccessioncode"/>">
   <<bean:write name="obsTag" />dateentered rdf:datatype="&xsd;date"><bean:write name="onerowofobservation" property="observationdateentered_datetrunc" /></<bean:write name="obsTag" />dateentered>
   <<bean:write name="obsTag" />startdate rdf:datatype="&xsd;date"><bean:write name="onerowofobservation" property="obsstartdate_datetrunc"/></<bean:write name="obsTag" />startdate>
   <<bean:write name="obsTag" />taxonobservationarea vegbank:units="http://vegbank.org/rdf/units/square-meters" rdf:datatype="&xsd;decimal"><bean:write name="onerowofobservation" property="taxonobservationarea"/></<bean:write name="obsTag" />taxonobservationarea>
   <<bean:write name="plotTag" />latitude rdf:datatype="&xsd;decimal"><bean:write name="onerowofobservation" property="latitude"/></<bean:write name="plotTag" />latitude>
   <<bean:write name="plotTag" />longitude rdf:datatype="&xsd;decimal"><bean:write name="onerowofobservation" property="longitude"/></<bean:write name="plotTag" />longitude>
   <<bean:write name="plotTag" />stateprovince><bean:write name="onerowofobservation" property="stateprovince"/></<bean:write name="plotTag" />stateprovince>
   <<bean:write name="plotTag" />country><bean:write name="onerowofobservation" property="country"/></<bean:write name="plotTag" />country>
  </<bean:write name="vbTag"/>observation>
</logic:iterate><!-- plot -->
</logic:notEmpty>


</rdf:RDF> <!-- end rdf -->
<!-- @webpage_footer_html  @-->


