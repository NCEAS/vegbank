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


<!--Style sheet for transforming plot xml files, specifically
	for the servlet transformation to show summary results
	to the web browser
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:output method="html"/>
  <xsl:template match="/vegPlotPackage">

<html>
	<head>
  	<link rel="stylesheet" type="text/css" href="http://numericsolutions.com/includes/default.css" />
	
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


<span class="copyright">
	<xsl:number value="count(plot)" /> documents found.
</span>

<!--NAVIGATION TO THE NEXT AND PREVIOUS-->
<span class="navigation">
PREV || NEXT 
</span>
<br> </br>
<!-- SOME NOTES ABOUT THE USE OF ICONS-->
<br> </br>
<span class="intro" >Available Reports: 
<img src="/vegbank/images/report_sm.gif"></img>=Summary
<img src="/vegbank/images/small_globe.gif"></img>=Location 
<img src="/vegbank/images/comprehensive_sm.gif"></img>=Comprehensive </span>

<!-- set up the form which is required by netscape 4.x browsers -->
<form name="myform" action="viewData" method="post">

<input type="submit" name="downLoadAction" value="Continue to Download Wizard" /> 
<!-- set up a table -->
<table width="85%">


<tr colspan="1" bgcolor="CCCCFF" align="left" valign="top">
   <th class="tablehead">Identification</th>
</tr>

	<!-- Header and row colors -->
        <xsl:variable name="evenRowColor">#C0D3E7</xsl:variable>
        <xsl:variable name="oddRowColor">#FFFFFF</xsl:variable>
	
	   
	<xsl:for-each select="plot">
	<xsl:sort select="authorPlotCode"/>
	
	<tr valign="top">
             
     
    <!--if even row -->
    <xsl:if test="position() mod 2 = 1">	
		
		<!-- First Cell-->
		<td  width="20%" colspan="1" bgcolor="{$evenRowColor}" align="left" valign="middle">
			
			<!--grab the plot name and store as a varibale for later-->
			<xsl:variable name="PLOT">
  			<xsl:value-of select="authorPlotCode"/>
			</xsl:variable>
			
			<xsl:variable name="PLOTID">
  			<xsl:value-of select="plotId"/>
			</xsl:variable>
			
			<a> <span class="category">Vegbank code: </span> 	
				<span class="itemsmall">
					<xsl:value-of select="plotAccessionNumber"/> <br> </br>
				</span>
			</a>
			<a> <span class="category">Author code: </span> 
					<span class="itemsmall">
					<xsl:value-of select="authorPlotCode"/> <br> </br>
					</span>
			</a>
			<!-- GET THE LATS AND LONGS INTO A VARIABLE -->
			<xsl:variable name="LATITUDE">
  			<xsl:value-of select="latitude"/>
			</xsl:variable>
			<xsl:variable name="LONGITUDE">
  			<xsl:value-of select="longitude"/>
			</xsl:variable>
			<!-- THE LINK TO THE SUMMARY REPORT-->
			<a href="/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&amp;longitude={$LONGITUDE}&amp;latitude={$LATITUDE}"> 
				<img align="center" border="0" src="/vegbank/images/small_globe.gif" alt="Location"> </img> 
			</a>
			&#160;
			<!-- THE LINK TO THE COMPREHENSIVE REPORT-->
			<a href="/framework/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=full&amp;queryType=simple&amp;plotId={$PLOTID}"> 
				<img align="center" border="0" src="/vegbank/images/comprehensive_sm.gif" alt="Comprehensive view"> </img> 
			</a>
			&#160;
			<!-- THE LINK TO THE SUMMARY-->
			<a href="/framework/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=summary&amp;queryType=simple&amp;plotId={$PLOTID}"> 
				<img align="center" border="0" src="/vegbank/images/report_sm.gif" alt="Comprehensive view"> </img> 
			</a>
			<br> </br>
			<input name="plotName" type="checkbox" value="{$PLOTID}" checked="yes"> 
				<span class="item"> <font color="#333333"> download <xsl:number value="position()"/> </font> </span> 
			</input>
	</td>		 
	 </xsl:if>
	 
	 
	

  
  <!--if odd row -->
	<xsl:if test="position() mod 2 = 0">
		  	
		<!-- First Cell-->
		<td  width="20%" colspan="1" bgcolor="{$oddRowColor}" align="left" valign="middle">

	<!--grab the plot name and store as a varibale for later-->
			<xsl:variable name="PLOT">
  			<xsl:value-of select="authorPlotCode"/>
			</xsl:variable>
			
			<xsl:variable name="PLOTID">
  			<xsl:value-of select="plotId"/>
			</xsl:variable>
			
			<a> <span class="category">Vegbank code: </span> 	
				<span class="itemsmall">
					<xsl:value-of select="plotAccessionNumber"/> <br> </br>
				</span>
			</a>
			<a> <span class="category">Author code: </span> 
					<span class="itemsmall">
					<xsl:value-of select="authorPlotCode"/> <br> </br>
					</span>
			</a>
			<!-- GET THE LATS AND LONGS INTO A VARIABLE -->
			<xsl:variable name="LATITUDE">
  			<xsl:value-of select="latitude"/>
			</xsl:variable>
			<xsl:variable name="LONGITUDE">
  			<xsl:value-of select="longitude"/>
			</xsl:variable>
			<!-- THE LINK TO THE SUMMARY REPORT-->
			<a href="/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&amp;longitude={$LONGITUDE}&amp;latitude={$LATITUDE}"> 
				<img align="center" border="0" src="/vegbank/images/small_globe.gif" alt="Location"> </img> 
			</a>
			&#160;
			<!-- THE LINK TO THE COMPREHENSIVE REPORT-->
			<a href="/framework/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=full&amp;queryType=simple&amp;plotId={$PLOTID}"> 
				<img align="center" border="0" src="/vegbank/images/comprehensive_sm.gif" alt="Comprehensive view"> </img> 
			</a>
			&#160;
			<!-- THE LINK TO THE SUMMARY-->
			<a href="/framework/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=summary&amp;queryType=simple&amp;plotId={$PLOTID}"> 
				<img align="center" border="0" src="/vegbank/images/report_sm.gif" alt="Comprehensive view"> </img> 
			</a>
			<br> </br>
			<input name="plotName" type="checkbox" value="{$PLOTID}" checked="yes"> 
				<span class="item"> <font color="#333333"> download <xsl:number value="position()"/> </font> </span> 
			</input>
	</td>		 
	 </xsl:if>

	 </tr>    
	</xsl:for-each>

</table>
<input type="submit" name="downLoadAction" value="Continue to Download Wizard" /> 
</form>
	


</body>
</html> 

</xsl:template>


</xsl:stylesheet>
