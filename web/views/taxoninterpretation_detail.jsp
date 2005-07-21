@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>View VegBank Data: Taxon Interpretations - Detail</TITLE>



      <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@
      @possibly_center@
        <h2>View VegBank Taxon Interpretations</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="taxoninterpretation" select="taxoninterpretation" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="taxoninterpretation-BEANLIST">
<p>  Sorry, no Taxon Interpretations found.</p>
</logic:empty>
<logic:notEmpty name="taxoninterpretation-BEANLIST">
<logic:iterate id="onerowoftaxoninterpretation" name="taxoninterpretation-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">

  <bean:define id="manual_taxonobservation_pk" name="onerowoftaxoninterpretation" property="taxonobservation_id" />
  <vegbank:get select="taxonobservation" id="taxonobservation" beanName="map" pager="false" where="where_taxonobservation_pk" wparam="manual_taxonobservation_pk" 
    perPage="-1" />
<logic:iterate id="onerowoftaxonobservation" name="taxonobservation-BEANLIST">
<tr>
  <th colspan="2" class="major_smaller">
  <!-- get observation name and taxonobservation name + links -->

  <logic:notEmpty name="onerowoftaxonobservation">
    <bean:write name="onerowoftaxonobservation" property="observation_id_transl" /> | <bean:write name="onerowoftaxonobservation" property="authorplantname" />
  </logic:notEmpty>
  
  </th>
</tr>
<tr>
  <th><a href="@get_link@std/observation/<bean:write name='onerowoftaxonobservation' property='observation_id' />">view observation</a></th>
  <th><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonobservation' property='taxonobservation_id' />">view taxon-observation</a></th>
</tr>

</logic:iterate><!-- taxonOBs -->
        <%@ include file="autogen/taxoninterpretation_detail_data.jsp" %>
        <bean:define id="taxoninterpretation_pk" name="onerowoftaxoninterpretation" property="taxoninterpretation_id" />

<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_taxoninterpretation_pk" wparam="taxoninterpretation_pk" />-->
<logic:notEmpty name="onerowoftaxoninterpretation" property="grouptype"><!-- only show taxonAlt when group type is filled in -->
<TR><TD COLSPAN="2">
<vegbank:get id="taxonalt" select="taxonalt" beanName="map" pager="false" where="where_taxoninterpretation_pk" wparam="taxoninterpretation_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="9">Taxon Alts:</th></tr>
<logic:empty name="taxonalt-BEANLIST">
<tr><td class="@nextcolorclass@">  Sorry, no Taxon Alts found.</td></tr>
</logic:empty>
<logic:notEmpty name="taxonalt-BEANLIST">
<tr>
<%@ include file="autogen/taxonalt_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowoftaxonalt" name="taxonalt-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/taxonalt_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
</TD></TR>
</logic:notEmpty>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@

