@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<script language="javascript">

function popupPlantQuery() {
	window.open('@web_context@forms/PlantQuery.jsp', '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

function getHelpPageId() {
  return "interpret-taxon-on-plot";
}
</script>
<title>Interpret Plant Taxon</title>
 
  
  @webpage_masthead_html@ 
  
  <h2>Interpret Plant Taxon</h2>
<logic:messagesPresent message="false">
<table border="0"><tr><td>
<h3><font color="red">Please Try Again</font></h3>
	<ul>
	<html:messages id="error" message="false">
		<li><bean:write name="error"/></li>
	</html:messages>
	</ul>
	<hr/>
</td></tr></table>
<br/>
</logic:messagesPresent>
<logic:messagesPresent property="saved" message="true">
<table border="0"><tr><td>
	<ul>
	<html:messages id="msg" property="saved" message="true">
		<li><bean:write name="msg"/></li>
	</html:messages>
	</ul>
	<hr/>
</td></tr></table>
<br/>
</logic:messagesPresent>
<bean:parameter id="tobsAC" name="tobsAC"/>
<!--%
// Set up genericBean in the request scope with a list of Taxoninterpretation objects
// execute() PARAMS:
//   1: the HTTP request
//   2: the SQL select key in SQLStore.properties
//   3: the SQL where key in SQLStore.properties
//   4: the name of the model bean to generate
//   5: any SQL where parameters; can be an array too
org.vegbank.common.command.GenericCommandStatic.execute(
		request, "taxonobservation", "where_accessioncode", 
		"Taxonobservation", tobsAC);
%-->
<vegbank:get select="taxonobservation" id="taxonobservation" beanName="map"
 pager="false" perPage="-1" wparam="tobsAC" where="where_ac"
 />

<logic:empty name="taxonobservation-BEANLIST">
	The given taxon observation accession code was not found: 
	<bean:write name="tobsAC"/>
</logic:empty>
<logic:notEmpty name="taxonobservation-BEANLIST">
<logic:iterate id="onerowoftaxonobservation" name="taxonobservation-BEANLIST">
	<bean:define id="tobsId" name="onerowoftaxonobservation" property="taxonobservation_id"/>
<!-- 
String tobsId = request.getAttribute("Taxonobservation").getTaxonobservation_id();
-->
<html:form action="/SaveTaxonInterpretation.do">
	<html:hidden name="formBean" property="tobsId" value="<%= tobsId.toString() %>"/>
	<html:hidden name="formBean" property="interpretationtype" value="Other"/>
	<html:hidden name="onerowoftaxonobservation" property="observation_id"/>
	<html:hidden name="formBean" property="emb_taxoninterpretation" value="0" /> <!-- not embargoed -->
	<input type="hidden" name="tobsAC" value='<bean:write name="tobsAC"/>' />
<p>Please enter your re-interpretation of the author's plant choice.
</p>
 <Table id="sidebysidetable"><Tr><Td>
 
  <table>
<tr>
	<td class="listhead">Author's Plant Name</td>
	<td><span class="item"><bean:write name="onerowoftaxonobservation" property="authorplantname"/></span></td>
</tr>
<tr>
	<td bgcolor="#666666" class="whitetext" align="right" colspan="2">
		Enter your interpretation below &nbsp; &nbsp;</td>
</tr>
<tr>
	<td class="listhead">Plant concept accession code</td>
	<td><html:text name="formBean" property="pcAC" size="30"/>
	&nbsp; &nbsp; &nbsp; &raquo; 
		<span class="item"><a href="javascript:void popupPlantQuery()">lookup</a></span>
	</td>
</tr>
<tr>
	<td class="listhead">Name you call this concept (optional)</td>
	<td><html:text name="formBean" property="plantName" size="45"/></td>
</tr>
<tr>
	<td class="listhead">Fit of your concept to the plant of the plot</td>
	<td>
	    <html:select name="formBean" property="taxonfit" size="1">
			<html:option value="">choose...</html:option>
			<html:option value="Absolutely wrong">Absolutely wrong</html:option>
			<html:option value="Understandable but wrong">Understandable but wrong</html:option>
			<html:option value="Reasonable or acceptable answer">Reasonable or acceptable answer</html:option>
			<html:option value="Good answer">Good answer</html:option>
			<html:option value="Absolutely correct">Absolutely correct</html:option>
		</html:select>
	</td>
	</tr>
	<tr>
		<td class="listhead">Confidence you have in what you've entered here</td>
		<td> 
	    	<html:select name="formBean" property="taxonconfidence" size="1">
				<html:option value="">choose...</html:option>
				<html:option value="High">High</html:option>
				<html:option value="Medium">Medium</html:option>
				<html:option value="Low">Low</html:option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="listhead">Notes</td>
		<td><html:textarea name="formBean" property="notes" rows="8" cols="45"/></td>
	</tr>
</table>
	
	</Td>
	<Td valign="top">
		
 		<table border="0" cellspacing="4" cellpadding="0">
		<tr>
		<td colspan="2">
			<span class="sizenormal"><b>Information</b></span>
		</td>
		</tr>
		<tr class="sizetiny">
		<td valign="top">&raquo;</td>
		<td>
			You can more narrowly define the plant in question due to your 
			knowledge of the area in which the plant is found.  
			E.g. you can identify which species the plant is, though 
			the author only knew genus (or you can specify variety, 
			but the author knew only species).
		</td>
		</tr>
		<tr>
		<td></td>
		<td bgcolor="#AAAAAA"><img src="@image_server@transparent.gif" /></td>
		</tr>
			
		<tr class="sizetiny">
		<td valign="top">&raquo;</td>
		<td>
			You disagree with the author's interpretation of the plant and 
			wish to point to a new plant.
		</td>
		</tr>
		<tr>
		<td></td>
		<td bgcolor="#AAAAAA"><img src="@image_server@transparent.gif" /></td>
		</tr>
			
			
		<tr class="sizetiny">
		<td valign="top">&raquo;</td>
		<td>
			(Rarely) you disagree with the author's interpretation and wish 
			to state only that the plant is NOT the concept mentioned (use Fit = Absolutely wrong).
		</td>
		</tr>
			
		<tr>
		<td></td>
		<td bgcolor="#AAAAAA"><img src="@image_server@transparent.gif" /></td>
		</tr>
			
		<tr class="sizetiny">
		<td valign="top">&raquo;</td>
		<td>
			You are REQUIRED to fill in the Notes section with a good 
			explanation for why you are interpreting this plant the way you are.
		</td></tr>
		</table>
	</Td></Tr></Table><!-- end of sidebysidetable -->
	<br/>
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:submit property="submit" value="Submit Interpretation" />
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:cancel/>
  </html:form>
  <!-- OTHER TAXON INTERPRETATIONS -->
<!--%
org.vegbank.common.command.GenericCommandStatic.execute(
		request, "taxoninterpretation_summary", "where_taxonobservation_pk", 
		"Taxoninterpretation", tobsId);
%-->
<!--logic:notEmpty name="genericBean"-->
<br/>
 <blockquote>
	<h3>Other Taxon Interpretations</h3>
 
  <table class="thinlines">
	 
	 <tr>
		  <th>Plant Concept</th>
		  <th>Called by name:</th>
		  <th>Type</th>
		  <th>Fit</th>
		  <th>Confidence</th>
		  <th>Party</th>
	 </tr>
<%
String rowClass = "evenrow";
%>
<vegbank:get id="taxoninterpretation" select="taxoninterpretation" beanName="map"
  where="where_taxonobservation_pk" wparam="tobsId" pager="false" perPage="-1"/>

<!--logic:iterate id="tint" name="genericBean" type="org.vegbank.common.model.Taxoninterpretation" -->
<!--%
	bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
	// get the plant concept for this T-int
	org.vegbank.common.command.GenericCommandStatic.execute(
			request, "plantconcept", "where_plantconcept_pk", 
			"Plantconcept", new Long(tint.getPlantconcept_id()));
%-->
<logic:notEmpty name="taxoninterpretation-BEANLIST">
<logic:iterate id="onerowoftaxoninterpretation" name="taxoninterpretation-BEANLIST">

	 <tr class="@nextcolorclass@">
	   <td><a target="_new" href='@get_link@detail/plantconcept/<bean:write name="onerowoftaxoninterpretation" property="plantconcept_id"/>'><bean:write name="onerowoftaxoninterpretation" property="plantconcept_id_transl"/></a></td>
	   
 
	   <td>
	   <logic:empty name="onerowoftaxoninterpretation" property="plantname_id_transl">n/a</logic:empty>
	   <bean:write name="onerowoftaxoninterpretation" property="plantname_id_transl"/></td>
	  
	   <td>
		<logic:empty name="onerowoftaxoninterpretation" property="interpretationtype">n/a</logic:empty>
	    <bean:write name="onerowoftaxoninterpretation" property="interpretationtype"/>
	   </td>
	   <td>
		<logic:empty name="onerowoftaxoninterpretation" property="taxonfit">n/a</logic:empty>
		<bean:write name="onerowoftaxoninterpretation" property="taxonfit"/>
	   </td>
	   <td>
		<logic:empty name="onerowoftaxoninterpretation" property="taxonconfidence">n/a</logic:empty>
	   	<bean:write name="onerowoftaxoninterpretation" property="taxonconfidence"/>
	   </td>
	   <td>
<!-- translate the PK -->
<bean:write  name="onerowoftaxoninterpretation" property="party_id_transl"/>

	</td>
   </tr>
 </logic:iterate><!-- tax int -->
 </logic:notEmpty>
 </table>
 
 </blockquote>
 <br/>
 </logic:iterate><!-- tax obs -->
</logic:notEmpty><!-- tax obs -->
 <br/>
 <br/>
  <!-- VEGBANK FOOTER -->
  
  <!-- END FOOTER -->
  
  @webpage_footer_html@
