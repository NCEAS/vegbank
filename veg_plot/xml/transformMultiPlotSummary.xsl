<?xml version="1.0"?> 
<!--
  * resultset.xsl
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


<!--Style sheet for transforming plot xml files, specifically
	for the servlet transformation to show summary results
	to the web browser-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:output method="html"/>
  <xsl:template match="/vegPlot">

<!--  
  <html>
      <head>
      
             <link rel="stylesheet" type="text/css" 
              href="@web-base-url@/default.css" />
        
	</head>
  	
	<body>


	</body>
 </html>
-->


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

  
<script language="javascript" alt="JavaScript not enabled!">
 <xsl:text disable-output-escaping="yes">
   &lt;!-- 
    today = new Date()
    document.write("(Accessed: " + today +" to: ")
    document.write(location.host.toLowerCase()+") \n")
   //--&gt;
   </xsl:text>
 </script>

 
</head>




<body bgcolor="FFFFFF">



<br></br>
<xsl:number value="count(project/plot)" /> documents found.


<!-- set up the form which is required by netscape 4.x browsers -->
<form name="myform" action="viewData" method="post">

<!-- set up a table -->
<table width="100%">


           <tr colspan="1" bgcolor="CCCCFF" align="left" valign="top">
             <th class="tablehead">Site Data</th>
             <th class="tablehead">Observation Data</th>
             <th class="tablehead">Vegetation Data</th>
           </tr>

	<!-- Header and row colors -->
        <xsl:variable name="evenRowColor">#C0D3E7</xsl:variable>
        <xsl:variable name="oddRowColor">#FFFFFF</xsl:variable>
	
	   
	<xsl:for-each select="project/plot">
	<xsl:sort select="authorPlotCode"/>
	
	<tr valign="top">
             
     
     <!--if even row -->
     <xsl:if test="position() mod 2 = 1">
		<td colspan="1" bgcolor="{$evenRowColor}" align="left" valign="top">
				
				
<!-- Site data here -->
			<xsl:variable name="PLOT">
  			<xsl:value-of select="authorPlotCode"/>
			</xsl:variable>
			
			<xsl:variable name="PLOTID">
  			<xsl:value-of select="plotId"/>
			</xsl:variable>

            <input name="plotName" type="checkbox" value="{$PLOTID}" checked="yes">download</input>
			<xsl:number value="position()"/>
			
			
			<a><br></br> <b>plot code: </b> <xsl:value-of select="authorPlotCode"/> <br> </br></a>
             	<a><b>State:</b> <xsl:value-of select="state"/>  <br></br> </a>
				<a><b>x coord:</b> <xsl:value-of select="xCoord"/>  <br></br> </a>
				<a><b>y coord:</b> <xsl:value-of select="yCoord"/>  <br></br> </a>
	     		<a><b>slopeAspect:</b> <xsl:value-of select="slopeAspect"/> <br></br> </a>
             	<a><b>slopeGradient:</b> <xsl:value-of select="slopeGradient"/>  <br></br></a>
	     		<a><b>slopePosition:</b> <xsl:value-of select="topoPosition"/> <br> </br> </a>
				<a><b>elevationValue:</b> <xsl:value-of select="elevationValue"/> <br> </br> </a>
				<a><b>surf geology:</b> <xsl:value-of select="surfGeo"/> <br> </br> </a>
				<a><b>hydrologicRegime:</b> <xsl:value-of select="hydrologicRegime"/>  </a>
			</td>

			
			<!--Observation data hera --> 
	     	<td colspan="1"  bgcolor="{$evenRowColor}" align="left" valign="top">
            	<a> <b>observation code: </b> <xsl:value-of select="plotObservation/authorObsCode"/> <br> </br></a>
				<b>community Name:</b> <xsl:value-of select="plotObservation/communityAssignment/communityName"/>
            </td>
	 
	 
	 <!-- Species data here -->
	    	<td colspan="1" bgcolor="{$evenRowColor}" align="left" valign="top">
            	<i><FONT SIZE="-1" FACE="arial">
				<b>Top species:</b> <xsl:for-each select="plotObservation/taxonObservation">
	       		<xsl:value-of select="authNameId"/>; </xsl:for-each>
	 			</FONT></i> 
            </td>
	 	</xsl:if>
	 
	 
	 
	 
	 <!--if odd row-->
	 	<xsl:if test="position() mod 2 = 0">
             	<td colspan="1" bgcolor="{$oddRowColor}" align="left" valign="top">
               		
			<!-- Site data here -->
			<xsl:variable name="PLOT">
  			<xsl:value-of select="authorPlotCode"/>
			</xsl:variable>
			
			<xsl:variable name="PLOTID">
  			<xsl:value-of select="plotId"/>
			</xsl:variable>

            <input name="plotName" type="checkbox" value="{$PLOTID}" checked="yes">download</input>
			<xsl:number value="position()"/>
			
			
			<a><br></br> <b>plot code: </b> <xsl:value-of select="authorPlotCode"/> <br> </br></a>
             	<a><b>State:</b> <xsl:value-of select="state"/>  <br></br> </a>
				<a><b>x coord:</b> <xsl:value-of select="xCoord"/>  <br></br> </a>
				<a><b>y coord:</b> <xsl:value-of select="yCoord"/>  <br></br> </a>
	     		<a><b>slopeAspect:</b> <xsl:value-of select="slopeAspect"/> <br></br> </a>
             	<a><b>slopeGradient:</b> <xsl:value-of select="slopeGradient"/>  <br></br></a>
	     		<a><b>slopePosition:</b> <xsl:value-of select="topoPosition"/> <br> </br> </a>
				<a><b>elevationValue:</b> <xsl:value-of select="elevationValue"/> <br> </br> </a>
				<a><b>surf geology:</b> <xsl:value-of select="surfGeo"/> <br> </br> </a>
				<a><b>hydrologicRegime:</b> <xsl:value-of select="hydrologicRegime"/>  </a>
			</td>
	     
		 	
			<!--Observation data hera --> 
	     	<td colspan="1"  bgcolor="{$oddRowColor}" align="left" valign="top">
            	<a> <b>observation code: </b> <xsl:value-of select="plotObservation/authorObsCode"/> <br> </br></a>
				<b>community Name:</b> <xsl:value-of select="plotObservation/communityAssignment/communityName"/>
            </td>
	 
	 
	 <!-- Species data here -->
	    	<td colspan="1" bgcolor="{$oddRowColor}" align="left" valign="top">
            	<i><FONT SIZE="-1" FACE="arial">
				<b>Top species:</b> <xsl:for-each select="plotObservation/taxonObservation">
	       		<xsl:value-of select="authNameId"/>; </xsl:for-each>
	 			</FONT></i> 
            </td>
		 
		 
	
	 	</xsl:if>
	 
	 </tr>    
	</xsl:for-each>
<input type="button" value="Check All" onClick="this.value=check(this.form.plotName)">
</input> 
</table>
<input type="submit" name="downLoadAction" value="start downLoad" /> 
</form>
	


</body>
</html> 

</xsl:template>


</xsl:stylesheet>
