@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: Taxon Observations - Detail</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>
      @vegbank_header_html_normal@
      @possibly_center@
        <h2>View VegBank Taxon Observations</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="taxonobservation" select="taxonobservation" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="taxonobservation-BEANLIST">
<p>  Sorry, no Taxon Observations found.</p>
</logic:empty>
<logic:notEmpty name="taxonobservation-BEANLIST">
<logic:iterate id="onerowoftaxonobservation" name="taxonobservation-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table cellpadding="2" class="outsideborder" width="799">
<tr>
<td width="80%"><table class="leftrightborders" width="100%">
        <%@ include file="autogen/taxonobservation_detail_data.jsp" %>
    </table>
</td>
<td class="useraction">ACTION:<br/>
<a href="@web_context@InterpretTaxonObservation.do?tobsAC=<bean:write name='onerowoftaxonobservation' property='accessioncode' />">
Interpret This Plant</a>
</td>
        <bean:define id="taxonobservation_pk" name="onerowoftaxonobservation" property="taxonobservation_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_taxonobservation_pk" wparam="taxonobservation_pk" />-->
<TR><TD COLSPAN="3">
<vegbank:get id="taxoninterpretation" select="taxoninterpretation_nostem" beanName="map" pager="false" where="where_taxonobservation_pk" wparam="taxonobservation_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="19">Current Taxon Interpretations:</th></tr>
<logic:empty name="taxoninterpretation-BEANLIST">
<tr><td class="@nextcolorclass@">  ERROR! no Taxon Interpretations found.</td></tr>
</logic:empty>
<logic:notEmpty name="taxoninterpretation-BEANLIST">
<tr>
<%@ include file="autogen/taxoninterpretation_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowoftaxoninterpretation" name="taxoninterpretation-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/taxoninterpretation_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
</TD></TR>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@