<?xml version="1.0"?> 
<!--
  * showSummary.xsl
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


<!--Style sheet for transforming vegetation community xml files, specifically
	for the servlet transformation to show summary results
	to the web browser-->
	
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:output method="html"/>
  <xsl:template match="/vegCommunity">
  

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
<xsl:number value="count(community)" /> documents found.

<!-- set up the form which is required by netscape 4.x browsers -->
<form name="myform" action="viewData" method="post">

<!-- set up a table -->
<table width="100%">

	<tr colspan="1" bgcolor="CCCCFF" align="left" valign="top">
             <th class="tablehead">Community Name</th>
             <th class="tablehead">Community Concept</th>
        </tr>

	<!-- Header and row colors -->
        <xsl:variable name="evenRowColor">#C0D3E7</xsl:variable>
        <xsl:variable name="oddRowColor">#FFFFFF</xsl:variable>
	
	   
	<xsl:for-each select="community">
	<xsl:sort select="commName"/>	

	<tr valign="top">


<!--if even row -->
<xsl:if test="position() mod 2 = 1">
			
	<td colspan="1" bgcolor="{$evenRowColor}" align="left" valign="top">
		<xsl:variable name="COMMNAME">
  		<xsl:value-of select="commSummaryId"/>
		</xsl:variable>
  	<a>Community Name:<b> <xsl:value-of select="commName"/> </b> </a> <br></br>
		<a>Parent Name: <xsl:value-of select="parentComm/commName"/>;  </a><br></br>
		<a>Date Entered: <xsl:value-of select="conceptOriginDate"/>; </a><br></br>
	</td>
		
	<td colspan="1" bgcolor="{$evenRowColor}" align="left" valign="top">
		<a>Code: <b> <xsl:value-of select="classCode"/>; </b>  </a><br></br>
		<a>Level: <xsl:value-of select="classLevel"/>;  </a><br></br>
		<a>Parent Code: <xsl:value-of select="parentComm/classCode"/>;  </a><br></br>
		<a>Recognizing Party: <xsl:value-of select="recognizingParty"/>; </a> <br></br>
	</td>
	
</xsl:if>
	 

<!--if odd row-->
<xsl:if test="position() mod 2 = 0">
	
	<td colspan="1" bgcolor="{$oddRowColor}" align="left" valign="top">
		<xsl:variable name="COMMNAME">
  		<xsl:value-of select="commSummaryId"/>
		</xsl:variable>
  	<a>Community Name:<b> <xsl:value-of select="commName"/> </b> </a> <br></br>
		<a>Parent Name: <xsl:value-of select="parentComm/commName"/>;  </a><br></br>
		<a>Date Entered: <xsl:value-of select="conceptOriginDate"/>; </a><br></br>
	</td>
		
	<td colspan="1" bgcolor="{$oddRowColor}" align="left" valign="top">
		<a>Code: <b> <xsl:value-of select="classCode"/>; </b>  </a><br></br>
		<a>Level: <xsl:value-of select="classLevel"/>;  </a><br></br>
		<a>Parent Code: <xsl:value-of select="parentComm/classCode"/>;  </a><br></br>
		<a>Recognizing Party: <xsl:value-of select="recognizingParty"/>; </a> <br></br>
	</td>
    
</xsl:if>
	 
	 </tr>    
	</xsl:for-each>



<!--
<input type="button" value="Check All" onClick="this.value=check(this.form.plotName)">
</input> 
-->
</table>

<!--
<input type="submit" name="downLoadAction" value="start downLoad" /> 
-->

</form>
	

</body>
</html> 
</xsl:template>
</xsl:stylesheet>

