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
*  '$Date: 2004-12-13 06:38:15 $'
*  '$Revision: 1.7 $'
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
	window.open('@get_link@std/plantconcept/' + ac, '', 'width=700,height=400,location,status,scrollbars,toolbar,resizable');

	////////////OLD
	//window.open('@web_context@GenericDispatcher.do?command=RetrieveVBModelBean&jsp=GenericDisplay.jsp&rootEntity=PlantConcept&accessionCode=' + ac, '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

function popupPlantQuery() {
	window.open('@web_context@forms/PlantQuery.jsp', '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
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

  <h2>Interpret Plant Taxon</h2>

<logic:messagesPresent message="false">
<table border="0"><tr><td>
<h3><font color="red">Please Try Again</font></h3>
	<ul>
	<html:messages id="error" message="false">
		<li><bean:write name="error"/></li>
	</html:messages>
	</ul>
	<hr noshade>
</td></tr></table>
<br>
</logic:messagesPresent>

<logic:messagesPresent property="saved" message="true">
<table border="0"><tr><td>
	<ul>
	<html:messages id="msg" property="saved" message="true">
		<li><bean:write name="msg"/></li>
	</html:messages>
	</ul>
	<hr noshade>
</td></tr></table>
<br>
</logic:messagesPresent>


<bean:define id="tobsAC" name="tobsAC"/>
<%
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
	<html:hidden name="formBean" property="interpretationtype" value="Other"/>
	<html:hidden name="Taxonobservation" property="observation_id"/>
	<input type="hidden" name="tobsAC" value="<bean:write name="tobsAC"/>">


<p>Please enter your re-interpretation of the author's plant choice.
<br>

 <table border="0" cellspacing="15" cellpadding="5" bgcolor="#FFFFFF" width="750"><tr><td>

 <table border="0" cellspacing="1" cellpadding="1" bgcolor="#333333"><tr><td>
  <table border="0" cellspacing="1" cellpadding="3" bgcolor="#FFFFFF">
<tr>
	<td class="listhead">Author Plant Name</td>
	<td><span class="item"><bean:write name="Taxonobservation" property="authorplantname"/></span></td>
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
	</td></tr></table>

	</td>
	<td valign="top">
		<br/>
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
		<td bgcolor="#AAAAAA"><img src="@image_server@pix_clear.gif"></td>
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
		<td bgcolor="#AAAAAA"><img src="@image_server@pix_clear.gif"></td>
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
		<td bgcolor="#AAAAAA"><img src="@image_server@pix_clear.gif"></td>
		</tr>
			
		<tr class="sizetiny">
		<td valign="top">&raquo;</td>
		<td>
			You are REQUIRED to fill in the Notes section with a good 
			explanation for why you are interpreting this plant the way you are.
		</td></tr>

		</table>
	</td></tr></table>


	<br/>
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:submit property="submit" value="Submit Interpretation" />
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:cancel/>

  </html:form>



  <!-- OTHER TAXON INTERPRETATIONS -->

<%
org.vegbank.common.command.GenericCommandStatic.execute(
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
	org.vegbank.common.command.GenericCommandStatic.execute(
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
org.vegbank.common.command.GenericCommandStatic.execute(request, "reference", "where_reference_pk", "Reference", refId);
%>

<logic:notEmpty name="genericVariable">
	<bean:write name="genericVariable" property="shortname"/>
</logic:notEmpty>
	   </td>
	   <td>
		<a href="javascript:void doLookup('<bean:write name="Plantconcept" property="accessioncode"/>');">
			lookup</a> 

	   </td>
	   <td>
		<logic:empty name="tint" property="interpretationtype">n/a</logic:empty>
	    <bean:write name="tint" property="interpretationtype"/>
	   </td>
	   <td>
		<logic:empty name="tint" property="taxonfit">n/a</logic:empty>
		<bean:write name="tint" property="taxonfit"/>
	   </td>
	   <td>
		<logic:empty name="tint" property="taxonconfidence">n/a</logic:empty>
	   	<bean:write name="tint" property="taxonconfidence"/>
	   </td>
	   <td>
<!-- translate the PK -->
<bean:define id="partyId" name="tint" property="party_id"/>

<%
// Set up genericVariable in the request scope
org.vegbank.common.command.GenericCommandStatic.execute(request, "party_simple", "where_party_pk", "Party", partyId);
%>

<logic:notEmpty name="Party">
	<logic:notEmpty name="Party" property="surname">
		<bean:write name="Party" property="surname"/>,
	</logic:notEmpty>
	<logic:notEmpty name="Party" property="givenname"> 
		<bean:write name="Party" property="givenname"/>
	</logic:notEmpty>

	<logic:notEmpty name="Party" property="organizationname">
		org: <bean:write name="Party" property="organizationname"/>
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
