<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!-- 
*   '$RCSfile: TaxonInterpretationAnnotateData.jsp,v $'
*   Purpose: Edit a taxon interpretation record.
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: anderson $'
*  '$Date: 2004-06-29 06:51:57 $'
*  '$Revision: 1.1 $'
*
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-->
<head>
<script language="javascript">
function doLookup(ac) {
	window.open('@web_context@GenericDispatcher.do?command=RetrieveVBModelBean&jsp=GenericDisplay.jsp&rootEntity=PlantConcept&accessionCode=' + ac, '', 'width=830,height=550,location,status,scrollbars,resizable');
}
</script>
@defaultHeadToken@

<title>Interpret Plant Taxon</title>
<link rel="stylesheet" href="@stylesheet@" type="text/css"/> 
  <meta http-equiv="Content-Type" content="text/html; charset=">
  </head>

  <body>

  <!--xxx -->
  @vegbank_header_html_normal@ 
  <!--xxx -->
  
  <br/>

  <html:errors/>

  <h2>Interpret Plant Taxon</h2>

<bean:define id="tobsAC" name="tobsAC"/>
<%
// Set up genericBean in the request scope with a list of Taxoninterpretation objects
// execute() PARAMS:
//   1: the HTTP request
//   2: the SQL select key in SQLStore.properties
//   3: the SQL where key in SQLStore.properties
//   4: the name of the model bean to generate
//   5: any SQL where parameters; can be an array too
org.vegbank.common.command.GenericCommand.execute(
		request, "taxonobservation", "where_accessioncode", 
		"Taxonobservation", tobsAC);
%>

<logic:empty name="Taxonobservation">
	The given taxon observation accession code was not found: 
	<bean:write name="tobsAC"/>
</logic:empty>

<logic:notEmpty name="Taxonobservation">
	<bean:define id="tobsId" name="Taxonobservation" property="taxonobservation_id"/>
<%-- 
String tobsId = request.getAttribute("Taxonobservation").getTaxonobservation_id();
--%>


<html:form action="/SaveTaxonInterpretation.do">
	<html:hidden name="formBean" property="tobsId" value="<%= tobsId.toString() %>"/>
	<html:hidden name="formBean" property="taxonInterpretation.interpretationtype" value="Other"/>
	<html:hidden name="Taxonobservation" property="observation_id"/>
	<hidden name="tobsAC" value="<bean:write name="tobsAC"/>">

<blockquote>

 <table border="0" cellspacing="1" cellpadding="1" bgcolor="#333333"><tr><td>
  <table border="0" cellspacing="1" cellpadding="4" bgcolor="#FFFFFF">
<tr>
	<td class="listhead">Author Plant Name</td>
	<td><span class="item"><bean:write name="Taxonobservation" property="authorplantname"/></span></td>
</tr>

<tr>
	<td bgcolor="#666666" class="whitetext" align="right" colspan="2">
		Enter your interpretation below &nbsp; &nbsp;</td>
</tr>

<tr>
	<td class="listhead">Name you call this concept</td>
	<!-- check table plantName; if exists use, else create -->
	<td><html:text property="plantName" size="45"/></td>
</tr>
<tr>
	<td class="listhead">Fit</td>
	<td>
	    <html:select property="taxonInterpretation.taxonfit" size="1">
			<html:option value="">choose...</html:option>
			<html:option value="Absolutely wrong">Absolutely wrong</html:option>
			<html:option value="Understandable but wrong">Understandable but wrong</html:option>
			<html:option value="Reasonable or acceptable an...">Reasonable or acceptable an...</html:option>
			<html:option value="Good answer">Good answer</html:option>
			<html:option value="Absolutely correct">Absolutely correct</html:option>
		</html:select>
	</td>
	</tr>
	<tr>
		<td class="listhead">Confidence</td>
		<td> 
	    	<html:select property="taxonInterpretation.taxonconfidence" size="1">
				<html:option value="">choose...</html:option>
				<html:option value="High">High</html:option>
				<html:option value="Medium">Medium</html:option>
				<html:option value="Low">Low</html:option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="listhead">Notes</td>
		<td><html:textarea property="taxonInterpretation.notes" rows="8" cols="45"/></td>
	</tr>

</table>
	</td></tr></table>

	<br/>
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:submit property="submit" value="Submit Interpretation" />
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:cancel/>

</blockquote>
  
  </html:form>



  <!-- OTHER TAXON INTERPRETATIONS -->

<%
org.vegbank.common.command.GenericCommand.execute(
		request, "taxoninterpretation_summary", "where_taxonobservation_pk", 
		"Taxoninterpretation", tobsId);
%>

<logic:notEmpty name="genericBean">

<br/>
 <blockquote>
	<h3>Other Taxon Interpretations</h3>

 <table border="0" cellspacing="1" cellpadding="0"><tr bgcolor="#333333"><td>
  <table border="0" cellspacing="1" cellpadding="4">
	 <tr>
	 	<td class="whitetext" colspan="3" bgcolor="#666666" align="center">Plant Concept</td>
		<td class="whitetext" colspan="4" align="center">Details</td>
	 </tr>

	 <tr class="listhead">
		  <td>Plant Name</td>
		  <td>Reference</td>
		  <td>LOOKUP</td>
		  <td>Type</td>
		  <td>Fit</td>
		  <td>Confidence</td>
		  <td>Party</td>
	 </tr>

<%
String bgColor = "#FFFFF";
%>

<logic:iterate id="tint" name="genericBean" type="org.vegbank.common.model.Taxoninterpretation">

<%
	bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";

	// get the plant concept for this T-int
	org.vegbank.common.command.GenericCommand.execute(
			request, "plantconcept", "where_plantconcept_pk", 
			"Plantconcept", new Long(tint.getPlantconcept_id()));
%>
	 <tr class="item" bgcolor="<%= bgColor %>">
	   <td><bean:write name="Plantconcept" property="plantname"/></td>
	   <td>
<!-- translate the PK -->
<bean:define id="refId" name="Plantconcept" property="reference_id"/>
<%
// Set up genericVariable in the request scope
org.vegbank.common.command.GenericCommand.execute(request, "reference", "where_reference_pk", "Reference", refId);
%>

<logic:notEmpty name="genericVariable">
	<bean:write name="genericVariable" property="shortname"/>
</logic:notEmpty>
	   </td>
	   <td>
		<a href="javascript:doLookup('<bean:write name="Plantconcept" property="accessioncode"/>');">
			lookup</a> 

	   </td>
	   <td><bean:write name="tint" property="interpretationtype"/></td>
	   <td><bean:write name="tint" property="taxonfit"/></td>
	   <td><bean:write name="tint" property="taxonconfidence"/></td>
	   <td>
<!-- translate the PK -->
<bean:define id="partyId" name="tint" property="party_id"/>

<%
// Set up genericVariable in the request scope
org.vegbank.common.command.GenericCommand.execute(request, "party_simple", "where_party_pk", "Party", partyId);
%>

<logic:notEmpty name="Party">
	<logic:notEmpty name="Party" property="surname">
		<bean:write name="Party" property="surname"/>,
	</logic:notEmpty>
	<logic:notEmpty name="Party" property="givenname">
		, <bean:write name="Party" property="givenname"/>
	</logic:notEmpty>

	<logic:notEmpty name="Party" property="organizationname">
		ORG: <bean:write name="Party" property="organizationname"/>
	</logic:notEmpty>
</logic:notEmpty>
	</td>
   </tr>

 </logic:iterate>
 </table>
 </td></tr></table>
 </blockquote>
 <br/>

 </logic:notEmpty>

</logic:notEmpty>
 <br/>
 <br/>




  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
