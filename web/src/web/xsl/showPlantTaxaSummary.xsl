<?xml version="1.0"?> 
<!--
  *
  *      Authors: John Harris
  *    Copyright: 2000 Regents of the University of California and the 
  *               National Center for Ecological Analysis and Synthesis
  *  For Details: http://www.nceas.ucsb.edu/
  *      Created: 2000 December
  * 
  * This is an XSLT (http://www.w3.org/TR/xslt) stylesheet designed to
  * convert an XML file showing the resultset of a query
  * into an HTML format suitable for rendering with modern web browsers.
-->


<!--Style sheet for transforming plant taxonomy xml files, specifically
	for the servlet transformation to show summary results
	to the web browser-->
	
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:output method="html"/>
  <xsl:template match="/plantTaxa">
  

<html>
<head>
		<title> VegBank -- Plant concept query results </title>
		<link rel="stylesheet" type="text/css" href="http://vegbank.nceas.ucsb.edu/vegbank/includes/default.css" />
	<script LANGUAGE="JavaScript">
<!-- Modified By:  Steve Robison, Jr. (stevejr@ce.net) -->
<!-- This script and many more are available free online at -->
<!-- The JavaScript Source!! http://javascript.internet.com -->
<xsl:text disable-output-escaping="yes">
&lt;!-- Begin
var checkflag = "false";
function check(field) {
if (checkflag == "false") {
for (i = 0; i &lt; field.length; i++) {
field[i].checked = true;}
checkflag = "true";
return "Uncheck All"; }
else {
for (i = 0; i &lt; field.length; i++) {
field[i].checked = false; }
checkflag = "false";
return "Check All"; }
}
//  End -->
</xsl:text>
</script>

</head>
<body bgcolor="FFFFFF">

<!--VEGBANK HEADER -->
@vegbank_header_html_normal@
<!--END OF VEGBANK HEADER -->

<blockquote> 
<p><font face="Arial, Helvetica, sans-serif" size="5"> Results of Plant Concept Lookup</font></p>
</blockquote>
<table width="799" cellpadding="0" cellspacing="0" border="1">
<tr vAlign="top" align="left" colspan="1"> 
<th class="tablehead" colspan="3" height="87">
<font face="Arial, Helvetica, sans-serif"> 
Search Name:  <xsl:value-of select="query/taxonName"/> <br></br>
Search Name Type:  <xsl:value-of select="query/taxonNameType"/> <br></br>
Search Level:  <xsl:value-of select="query/taxonNameLevel"/> <br></br>
Search Party:<br></br>
Search Date:<br></br>
Matches found: <xsl:number value="count(taxon)" /> </font></th>
</tr>
</table>
<!-- TABLE HEADER -->

<table>
<tr valign="center" align="left" colspan="1"> <th class="tablehead" width="34%" bgcolor="#336633">
<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">PLANT CONCEPT</font></th>
<th class="tablehead" width="34%" bgcolor="#336633">
<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">STANDARD NAMES</font></th>
<th class="tablehead" width="32%" bgcolor="#336633">
<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">NONSTANDARD NAMES</font></th>
</tr>


<!-- set up the form which is required by netscape 4.x browsers -->
<form name="myform" action="viewData" method="post">

	<!-- Header and row colors -->
<xsl:variable name="evenRowColor">#ffffcc</xsl:variable>
<xsl:variable name="oddRowColor">#FFFFFF</xsl:variable> 
<xsl:for-each select="taxon">
<xsl:sort select="name"/>	
	
<!-- ATTRIBUTES TABLE --> 


<xsl:if test="position() mod 2 = 1">			
<tr vAlign="top"> 
<td vAlign="top" align="left" width="34%" bgcolor="{$evenRowColor}"  height="106"> 
<p>
<font size="-1" face="Arial, Helvetica, sans-serif"><b><a><span class="category">
Ref. author: <span class="item"> <xsl:value-of select="name/plantConceptRefAuthor"/> </span> <br> </br>
Ref. date:  <span class="item"> <xsl:value-of select="name/plantConceptRefDate"/> </span> <br> </br>
Level: <span class="item"> <xsl:value-of select="name/plantLevel"/> </span> <br></br>
Description: <span class="item"> <xsl:value-of select="name/plantDescription"/> </span> <br></br>
Party: <span class="item"> <xsl:value-of select="name/plantUsagePartyOrgName"/> </span><br></br>
Start date: <span class="item"> <xsl:value-of select="name/startDate"/> </span> 
</span></a></b></font></p>
</td>

<td vAlign="top" align="left" width="32%" bgcolor="{$evenRowColor}"  height="106"> 
<p><b><font face="Arial, Helvetica, sans-serif" size="-1">
Scientific: <span class="item"><xsl:value-of select="name/scientificName"/> </span>  <br></br>
Scientific + authors: <span class="item"><xsl:value-of select="name/scientificNameWithAuthors"/> </span> <br> </br>
Common: <span class="item"> <xsl:value-of select="name/commonName"/> </span>  <br> </br>
Code: <span class="item"> <xsl:value-of select="name/plantCode"/> </span>  <br> </br>
Parent: <span class="item"> <xsl:value-of select="name/parentName"/> </span> </font></b><b>
<font face="Arial, Helvetica, sans-serif" size="-1"><br> </br>
</font></b></p>
</td>



<!-- SETUP THE LINK TO THE SYNONYM -->
<xsl:variable name="SYNONYM">
	<xsl:value-of select="name/acceptedSynonym"/>
</xsl:variable>																
<td vAlign="top" align="left" width="34%" bgcolor="{$evenRowColor}"  height="106">
<b><font face="Arial, Helvetica, sans-serif" size="-1">
Synonym:  <span class="item"> 
<a href="/framework/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonName={$SYNONYM}&amp;taxonNameType=%25&amp;taxonLevel=%25&amp;party=%25">  <xsl:value-of select="name/acceptedSynonym"/> </a> </span> <br> </br>
</font></b>
</td>

</tr>
</xsl:if>

<xsl:if test="position() mod 2 = 0">			
<tr vAlign="top"> 
<td vAlign="top" align="left" width="34%" bgcolor="{$oddRowColor}"  height="106"> 
<p>
<font size="-1" face="Arial, Helvetica, sans-serif"><b><a><span class="category">
Ref. author: <span class="item"> <xsl:value-of select="name/plantConceptRefAuthor"/> </span> <br> </br>
Ref. date:  <span class="item"> <xsl:value-of select="name/plantConceptRefDate"/> </span> <br> </br>
Level: <span class="item"> <xsl:value-of select="name/plantLevel"/> </span> <br></br>
Description: <span class="item"> <xsl:value-of select="name/plantDescription"/> </span> <br></br>
Party: <span class="item"> <xsl:value-of select="name/plantUsagePartyOrgName"/> </span><br></br>
Start date: <span class="item"> <xsl:value-of select="name/startDate"/> </span> 
</span></a></b></font></p>
</td>

<td vAlign="top" align="left" width="32%" bgcolor="{$oddRowColor}"  height="106"> 
<p><b><font face="Arial, Helvetica, sans-serif" size="-1">
Scientific: <span class="item"><xsl:value-of select="name/scientificName"/> </span>  <br></br>
Scientific + authors: <span class="item"><xsl:value-of select="name/scientificNameWithAuthors"/> </span> <br> </br>
Common: <span class="item"> <xsl:value-of select="name/commonName"/> </span>  <br> </br>
Code: <span class="item"> <xsl:value-of select="name/plantCode"/> </span>  <br> </br>
Parent: <span class="item"> <xsl:value-of select="name/parentName"/> </span> </font></b><b>
<font face="Arial, Helvetica, sans-serif" size="-1"><br> </br>
</font></b></p>
</td>



<!-- SETUP THE LINK TO THE SYNONYM -->
<xsl:variable name="SYNONYM">
	<xsl:value-of select="name/acceptedSynonym"/>
</xsl:variable>																
<td vAlign="top" align="left" width="34%" bgcolor="{$oddRowColor}"  height="106">
<b><font face="Arial, Helvetica, sans-serif" size="-1">
Synonym:  <span class="item"> 
<a href="/framework/servlet/DataRequestServlet?requestDataType=plantTaxon&amp;requestDataFormatType=html&amp;clientType=browser&amp;taxonName={$SYNONYM}&amp;taxonNameType=%25&amp;taxonLevel=%25&amp;party=%25">  <xsl:value-of select="name/acceptedSynonym"/> </a> </span> <br> </br>
</font></b>
</td>


</tr>
</xsl:if>




</xsl:for-each>
</form>
</table>

<!-- VEGBANK FOOTER -->
@vegbank_footer_html_tworow@
<!-- END OF FOOTER -->

</body>
</html> 
</xsl:template>
</xsl:stylesheet>

