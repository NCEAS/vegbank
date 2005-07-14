@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>View VegBank Data: Taxon Observations - Detail</TITLE>
<script type="text/javascript">
function getHelpPageId() {
  return "interpret-taxon-on-plot";
}

</script>



      @webpage_masthead_html@
      @possibly_center@
        <h2>View VegBank Taxon Observations</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
         <% String rowClassReset = "evenrow"; %>
        <vegbank:get id="taxonobservation" select="taxonobservation" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="taxonobservation-BEANLIST">
<p>  Sorry, no Taxon Observations found.</p>
</logic:empty>
<logic:notEmpty name="taxonobservation-BEANLIST">
<logic:iterate id="onerowoftaxonobservation" name="taxonobservation-BEANLIST">
<bean:define id="taxonobservation_pk" name="onerowoftaxonobservation" property="taxonobservation_id" />

<!-- iterate over all records in set : new table for each -->
<TABLE cellpadding="2" class="outsideborder" >
<TR><TH colspan="5" class="major">Taxon:<bean:write name='onerowoftaxonobservation' property='authorplantname' /></TH></TR>
<TR><TD colspan="5">

<!-- taxon Obs -->
<table class="leftrightborders" cellpadding="2" width="100%">
        <%@ include file="autogen/taxonobservation_detail_data.jsp" %>
    </table>
</TD>
</TR>


<TR>
<!-- 2 HUGE CAPITALIZED CELLS: TaxInterpret | taxonObs | taxonImportance (w/ stems too) -->
<TD valign="top"><!-- taxoninterpret -->

<vegbank:get id="taxoninterpretation" select="taxoninterpretation_nostem" beanName="map" pager="false" where="where_taxonobservation_pk" wparam="taxonobservation_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2">
<tr><th colspan="19">Interpretations of this Taxon:</th></tr>
<logic:empty name="taxoninterpretation-BEANLIST">
<tr><td class="@nextcolorclass@">  ERROR! no Taxon Interpretations found.</td></tr>
</logic:empty>
<logic:notEmpty name="taxoninterpretation-BEANLIST">
<!--<tr>
<th>More</th>
<%@ include file="autogen/taxoninterpretation_summary_head.jsp" %>
</tr>-->
<logic:iterate id="onerowoftaxoninterpretation" name="taxoninterpretation-BEANLIST">
<tr class="@nextcolorclass@">
<td class="largefield">
<a href="@get_link@detail/taxoninterpretation/@subst_lt@bean:write name='onerowoftaxoninterpretation' property='taxoninterpretation_id' /@subst_gt@">
                            Details
                            </a>
</td>
<td><!-- all of this in one cell -->
<%@ include file="autogen/taxoninterpretation_notblshort_data.jsp" %>
</td>
</tr>
</logic:iterate>
</logic:notEmpty>

<!-- wanna interpret this differently? -->
<!-- <br/><br/>
<table class="noborder"> -->
<tr>
<td colspan="19" class="useraction">ACTION:
<a href="@web_context@InterpretTaxonObservation.do?tobsAC=<bean:write name='onerowoftaxonobservation' property='accessioncode' />">
Interpret This Plant</a>
</td></tr>
<!-- </table> -->


</table>




</TD>

        



<TD valign="top"><!-- importance values -->
  
  <vegbank:get id="taxonimportance" select="taxonimportance" beanName="map" pager="false" where="where_taxonimportance_taxonobservation_fk" wparam="taxonobservation_pk" perPage="-1" />
  
  <table class="leftrightborders" cellpadding="2">
  <tr><th colspan="9">Taxon Importance Values:</th></tr>
  <logic:empty name="taxonimportance-BEANLIST">
  <tr><td class="@nextcolorclass@">  Sorry, no Taxon Importances found.</td></tr>
  </logic:empty>
  <logic:notEmpty name="taxonimportance-BEANLIST">
  <tr>
  <%@ include file="autogen/taxonimportance_summarynotaxon_head.jsp" %>
  <th>Stems:</th><th>Stem Diameters (graphically):</th>
  </tr>
  <logic:iterate id="onerowoftaxonimportance" name="taxonimportance-BEANLIST">
  <bean:define id="taxonimportance_pk" name="onerowoftaxonimportance" property="taxonimportance_id" />
  <% rowClass = rowClassReset ; %> <!-- reset rowClass, which got off in stems -->
  <tr class="@nextcolorclass@">
  
  <%@ include file="autogen/taxonimportance_summarynotaxon_data.jsp" %>
  <% rowClassReset = rowClass ; %> <!-- remember where to reset this -->
  <td>
    <!-- start stems -->
    <!-- THIS token 'bumps' the rowClass var to start stems off in same color as last stuff: @nextcolorclass@ -->
    <vegbank:get id="stemcount" select="stemcount" beanName="map" pager="false" 
     where="where_stemcount_taxonimportance_fk" wparam="taxonimportance_pk" perPage="-1" 
     allowOrderBy="true" orderBy="xorderby_stemdiameter_asc" />
  <bean:define id="graphicalStems" value="<!-- init -->" />
  <table class="leftrightborders" cellpadding="2">
	
	<logic:empty name="stemcount-BEANLIST">
	<tr  class="@nextcolorclass@"><td>-none-</td></tr>
	</logic:empty>
	<logic:notEmpty name="stemcount-BEANLIST">
	<tr>
	<%@ include file="autogen/stemcount_summary_head.jsp" %>
	</tr>
	
	<logic:iterate id="onerowofstemcount" name="stemcount-BEANLIST">
	
	<tr class="@nextcolorclass@">
	<%@ include file="autogen/stemcount_summary_data.jsp" %>
	</tr>
  <!-- store graphical stems for later: -->
  <bean:define id="graphicalStems"><bean:write name="graphicalStems" filter="false" />
    <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="0">
      <!-- define value for stem image: this is done because the scaling of round objects is a little funny.  these sizes are (somewhat) optimal -->
      <bean:define id="stemPic" value="1" /> <!-- default for small ones -->
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="18"><bean:define id="stemPic" value="3" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="23"><bean:define id="stemPic" value="4" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="24"><bean:define id="stemPic" value="2" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="29"><bean:define id="stemPic" value="1" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="32"><bean:define id="stemPic" value="4" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="37"><bean:define id="stemPic" value="3" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="50"><bean:define id="stemPic" value="4" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="53"><bean:define id="stemPic" value="2" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="56"><bean:define id="stemPic" value="5" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="69"><bean:define id="stemPic" value="6" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="77"><bean:define id="stemPic" value="3" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="80"><bean:define id="stemPic" value="7" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="94"><bean:define id="stemPic" value="8" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="101"><bean:define id="stemPic" value="4" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="104"><bean:define id="stemPic" value="8" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="115"><bean:define id="stemPic" value="10" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="125"><bean:define id="stemPic" value="5" /></logic:greaterThan>
      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="128"><bean:define id="stemPic" value="10" /></logic:greaterThan>

      
      <img src="@images_link@stem_<bean:write name='stemPic' />sq.png" alt="stem" title="<bean:write name='onerowofstemcount' property='stemdiameter' /> cm" height="<bean:write name='onerowofstemcount' property='stemdiameter' />" width="<bean:write name='onerowofstemcount' property='stemdiameter' />" />
        <bean:define id="themaxstems"><bean:write name="onerowofstemcount" property="stemcount" /></bean:define>
                
      <logic:greaterThan name="onerowofstemcount" property="stemcount" value="1">
        <!-- do again! -->
        
        <!-- only iterates if more than one, even though this causes some duplication in this file.  This part most of the time omitted. -->
        <!-- for (int i=2; i<= new Long(themaxstems).longValue(); i++) -->
         <%
           for (int i=2; i<= Long.parseLong(themaxstems); i++)
           {
          %>
           <img src="@images_link@stem_<bean:write name='stemPic' />sq.png" alt="stem" title="<bean:write name='onerowofstemcount' property='stemdiameter' /> cm (#<%= i %>)" height="<bean:write name='onerowofstemcount' property='stemdiameter' />" width="<bean:write name='onerowofstemcount' property='stemdiameter' />" />
         <%
           }
         %>
      </logic:greaterThan>
    </logic:greaterThan>
  </bean:define>
  
	</logic:iterate>
	</logic:notEmpty>
	</table>

  <!-- end stems -->
  </td>
  <!-- graphically display stems: -->
  <td>
    <logic:notEqual name="graphicalStems" value="<!-- init -->">
     <table bgcolor="#FFFFFF" class="thinlines"><tr><td>
      <bean:write name="graphicalStems" filter="false" />
      </td></tr>
     </table>
    </logic:notEqual>
  </td>
  
  </tr>
  </logic:iterate>
  </logic:notEmpty>
  </table>

</TD>
</TR>
</TABLE>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
