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
      <!--
             <link rel="stylesheet" type="text/css" 
              href="@web-base-url@/default.css" />
        -->
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

<br></br>
<xsl:number value="count(taxon)" /> matches found.

<!-- set up the form which is required by netscape 4.x browsers -->
<form name="myform" action="viewData" method="post">

<!-- set up a table -->
<table width="100%">

	<tr colspan="1" bgcolor="CCCCFF" align="left" valign="top">
             <th class="tablehead">Name Attributes</th>
             <th class="tablehead">Concept Attributes</th>
	     <th class="tablehead">Usage Attributes</th>
        </tr>

	<!-- Header and row colors -->
        <xsl:variable name="evenRowColor">#C0D3E7</xsl:variable>
        <xsl:variable name="oddRowColor">#FFFFFF</xsl:variable>
	
	   
	<xsl:for-each select="taxon">
	<xsl:sort select="name"/>	

	<tr valign="top">


<!--if even row -->
<xsl:if test="position() mod 2 = 1">			
	<td colspan="1" bgcolor="{$evenRowColor}" align="left" valign="middle">
  		<a>Plant Name: <xsl:value-of select="name/plantName"/></a> <br></br>
		<a>Name ID: <xsl:value-of select="name/plantNameId"/></a> <br></br>
		<a>Name Status: <xsl:value-of select="name/status"/></a> <br></br>
		<a>Parent Name: <xsl:value-of select="name/parentName"/></a> <br></br>
	</td>
	<td colspan="1" bgcolor="{$evenRowColor}" align="left" valign="top">
		<a>Class System: <xsl:value-of select="name/classSystem"/>; </a> <br></br>
		<a>Plant Level: <xsl:value-of select="name/plantLevel"/>; </a> <br></br>
		<a>Concept ID: <xsl:value-of select="name/plantConceptId"/>; </a> <br></br>
		<a>Concept Desc: <xsl:value-of select="name/plantDescription"/>; </a> <br></br>
		
	</td>
	<td colspan="1" bgcolor="{$evenRowColor}" align="left" valign="middle">
		<a>Start Date: <xsl:value-of select="name/startDate"/>; </a> <br></br>
		<a>Stop Date: <xsl:value-of select="name/stopDate"/>; </a> <br></br>
		<a>Synonym: <xsl:value-of select="name/acceptedSynonym"/>; </a> <br></br>
	</td>
</xsl:if>
	 
	 
<!--if odd row-->
<xsl:if test="position() mod 2 = 0">
	
	<td colspan="1" bgcolor="{$oddRowColor}" align="left" valign="middle">
  		<a>Plant Name: <xsl:value-of select="name/plantName"/></a> <br></br>
		<a>Name ID: <xsl:value-of select="name/plantNameId"/></a> <br></br>
		<a>Name Status: <xsl:value-of select="name/status"/></a> <br></br>
		<a>Parent Name: <xsl:value-of select="name/parentName"/></a> <br></br>
	</td>
	<td colspan="1" bgcolor="{$oddRowColor}" align="left" valign="top">
		<a>Class System: <xsl:value-of select="name/classSystem"/>; </a> <br></br>
		<a>Plant Level: <xsl:value-of select="name/plantLevel"/>; </a> <br></br>
		<a>Concept ID: <xsl:value-of select="name/plantConceptId"/>; </a> <br></br>
		<a>Concept Desc: <xsl:value-of select="name/plantDescription"/>; </a> <br></br>
		
	</td>
	<td colspan="1" bgcolor="{$oddRowColor}" align="left" valign="middle">
		<a>Start Date: <xsl:value-of select="name/startDate"/>; </a> <br></br>
		<a>Stop Date: <xsl:value-of select="name/stopDate"/>; </a> <br></br>
		<a>Synonym: <xsl:value-of select="name/acceptedSynonym"/>; </a> <br></br>
	</td>


</xsl:if>


</tr>    
</xsl:for-each>




</table>
</form>
	

</body>
</html> 
</xsl:template>
</xsl:stylesheet>

