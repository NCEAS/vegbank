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
    <table cellspacing="0" cellpadding="0" border="0" width="800">
      <tr>
        <td width="41" height="54" rowspan="2" valign="top" bgcolor="#336633">
          &#160;
        </td>
        <td width="308" height="54" rowspan="2" valign="top" bgcolor="#336633">
          <div align="left">
            <font face="Georgia, Times New Roman, Times, serif" size=
            "7"><b><font color="#FFFFFF" size="6">VegBank</font></b></font>
          </div>
        </td>
        <td height="28" colspan="4" valign="top" bgcolor="#336633">
          &#160;
        </td>
      </tr>
      <tr>
        <td width="97" height="26" valign="top" bgcolor="#336633">
          <font face=
          "Georgia, Times New Roman, Times, serif"><b>&#160;</b></font> 
        </td>
        <td width="119" height="26" valign="top" bgcolor="#336633">
          <div align="center">
            <font face="Georgia, Times New Roman, Times, serif"><b><a href=
            "http://vegbank.nceas.ucsb.edu/vegbank/general/login.html" style=
            "text-decoration:none; color:#FFFF80">Login</a></b></font>
          </div>
        </td>
        <td width="114" height="26" valign="top" bgcolor="#336633">
          <div align="center">
            <font face="Georgia, Times New Roman, Times, serif"><b><a href=
            "http://vegbank.nceas.ucsb.edu/vegbank/general/info.html" style=
            "text-decoration:none; color:#FFFF80">Information</a></b></font>
          </div>
        </td>
        <td width="114" height="26" valign="top" bgcolor="#336633">
          <div align="center">
            <font face="Georgia, Times New Roman, Times, serif"><b><a href=
            "http://vegbank.nceas.ucsb.edu/vegbank/general/help.html" style=
            "text-decoration:none; color:#FFFF80">Help</a></b></font>
          </div>
        </td>
      </tr>
    </table>
<!--END OF VEGBANK HEADER -->
<span class="category"> 
  <font color="red">
    <!-- format the word plot to be plural if not 1 plot -->
    <xsl:if test="count(plot)=1">
    1 plot
    </xsl:if>
    <xsl:if test="count(plot)!=1">
    <xsl:number value="count(plot)" /> plots
    </xsl:if>
  </font>
  matched your search criteria:<br/>
</span>

<!-- NAVIGATION TO THE NEXT AND PREVIOUS -->
<!-- (PREV and NEXT are not yet linked)
<span class="navigation">
PREV || NEXT 
</span>
<br> </br>
-->
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
<table width="800">


<tr colspan="1" bgcolor="#336633" align="left" valign="top">
   <th class="tablehead"><font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Identification</font></th>
</tr>

	<!-- Header and row colors -->
        <xsl:variable name="evenRowColor">#ffffcc</xsl:variable>
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
			
			<a> <span class="category">Vegbank plot code: </span> 	
				<span class="itemsmall">
					<xsl:value-of select="plotAccessionNumber"/> <br> </br>
				</span>
			</a>
			<a> <span class="category">Author plot code: </span> 
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
				<img align="center" border="0" src="/vegbank/images/report_sm.gif" alt="Summary view"> </img> 
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
			
			<a> <span class="category">Vegbank plot code: </span> 	
				<span class="itemsmall">
					<xsl:value-of select="plotAccessionNumber"/> <br> </br>
				</span>
			</a>
			<a> <span class="category">Author plot code: </span> 
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
			<!--
			<a href="/framework/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=summary&amp;queryType=simple&amp;plotId={$PLOTID}"> 
				<img align="center" border="0" src="/vegbank/images/report_sm.gif" alt="Comprehensive view"> </img> 
			</a>
			-->
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
	
<!-- VEGBANK FOOTER -->
<table cellspacing="0" cellpadding="0" border="0">
  <tr bgcolor="#EED85B"> 
    <td colspan="6" height="8" valign="top"><font face="Georgia, Times New Roman, Times, serif" size="-7">&#160;</font></td>
  </tr>
  <tr> 
    <td width="43" height="8" valign="top"></td>
    <td width="326" height="8" valign="top"></td>
    <td width="47" height="8" valign="top"></td>
    <td width="154" height="8" valign="top"></td>
    <td width="91" height="8" valign="top"></td>
    <td width="131" height="8" valign="top"></td>
  </tr>
  <tr> 
    <td width="800" height="38" colspan="6" valign="top"> 
      <p align="center"><font size="2" face="Georgia, Times New Roman, Times, serif"><a href="http://www.vegbank.org">VegBank 
        Home</a> | <a href="http://vegbank.nceas.ucsb.edu/vegbank/general/info.html">About VegBank</a> |<a href="http://vegbank.nceas.ucsb.edu/vegbank/general/instructions.html">Instructions</a> 
        | <a href="http://vegbank.nceas.ucsb.edu/vegbank/panel/panel.html">ESA Vegetation Panel</a> |<a href="http://vegbank.nceas.ucsb.edu/vegbank/general/contact.html"> 
        Contact</a> | <a href="http://vegbank.nceas.ucsb.edu/vegbank/general/help.html">Help</a></font><font size="2" face="Georgia, Times New Roman, Times, serif"><br/>
        <font size="2" face="Georgia, Times New Roman, Times, serif"><a href="http://vegbank.nceas.ucsb.edu/vegbank/general/register.html">Register</a> 
        | <a href="http://vegbank.nceas.ucsb.edu/vegbank/general/login.html">Login</a> |<a href="http://vegbank.nceas.ucsb.edu/vegbank/general/actions.html"> 
        use VegBank</a>| <a href="http://vegbank.nceas.ucsb.edu/vegbank/general/logout.html">Logout</a> |<a href="http://vegbank.nceas.ucsb.edu/vegbank/general/account.html"> 
        My VegBank Account</a></font></font></p>
    </td>
  </tr>
  <tr> 
    <td width="800" height="24" colspan="6" valign="top"> 
      <p align="center"><font size="1" face="Georgia, Times New Roman, Times, serif" color="#808000">&#169;
        2002 Ecological Society of America<br/>
        <a href="http://vegbank.nceas.ucsb.edu/vegbank/general/terms.html">Terms of use</a> |<a href="http://vegbank.nceas.ucsb.edu/vegbank/general/privacy.html"> 
        Privacy policy</a></font></p>
    </td>
  </tr>
</table>
<!-- END OF FOOTER -->

</body>
</html> 

</xsl:template>


</xsl:stylesheet>
