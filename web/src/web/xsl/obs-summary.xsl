<?xml version="1.0"?>
<!--
  * obs-summary.xsl : writes a brief Observation summary to html from XML
  *
  *      Authors: Michael Lee, John Harris
  *    Copyright: 2000 Regents of the University of California and the 
  *               National Center for Ecological Analysis and Synthesis
  *  For Details: http://www.nceas.ucsb.edu/
  *      Created: 2002 August
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
	<xsl:template match="/">  
		  <html>
	             <xsl:variable name="pageTitle">View Observation - Brief Summary</xsl:variable>
			<head>
				<title>VegBank -- <xsl:copy-of select="$pageTitle" />
                           </title>
				<link rel="stylesheet" type="text/css" href="http://vegbank.nceas.ucsb.edu/vegbank/includes/default.css"/>
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
			
			
			<!--VEGBANK HEADER -->
			@vegbank_header_html_normal@
			<!--END OF VEGBANK HEADER -->

				<blockquote>
					<p>
						<font face="Arial, Helvetica, sans-serif" size="5"><xsl:copy-of select="$pageTitle" /></font>
					</p>
				</blockquote>
				<table width="799" cellpadding="0" cellspacing="0" border="1">
					<tr vAlign="top" align="left" colspan="1">
						<th class="tablehead" colspan="3" height="25">
							<font face="Arial, Helvetica, sans-serif">
												observation count: <xsl:number value="count(/vegPlotPackage/project/plot/observation)"/> <br/>
							</font>
						</th>
					</tr>
				</table>
				<table width="799" cellpadding="0" cellspacing="0" border="1">
					<!-- set up the form which is required by netscape 4.x browsers -->
					<form name="myform" action="viewData" method="post">
						<!-- Header and row colors -->
						<xsl:variable name="evenRowColor">#ffffcc</xsl:variable>
						<xsl:variable name="oddRowColor">#FFFFFF</xsl:variable>

				<!-- EDIT		<hr/> -->
						<xsl:for-each select="/vegPlotPackage/project/plot/observation">
						<xsl:sort select="/vegPlotPackage/project/plot/observation/authorObsCode"/>
					<tr valign="center" align="left" colspan="3">
						<th class="tablehead" width="55%" bgcolor="#336633" colspan="3">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Plot and Observation Details</font>
						</th>
					</tr>
		<!-- EDIT				/vegPlotPackage/project/plot/observation <xsl:number value="position()"/> <br/> -->
							  <xsl:if test="position() mod 2 = 0"> 
								<tr vAlign="top">
									<td vAlign="top" align="left" width="60%" bgcolor="{$oddRowColor}" height="106" colspan="3">
										<p>
											<font size="-1" face="Arial, Helvetica, sans-serif">
												<b>
												<span class="category">
												projectName: <span class="item"><xsl:value-of select="../../projectName"/>
												</span>
												<br/>
												</span>
												<span class="category">
												projectDescription: <span class="item"><xsl:value-of select="../../projectDescription"/>
												</span>
												<br/>
												</span>
												<span class="category">
												startDate: <span class="item"><xsl:value-of select="../../startDate"/>
												</span>
												<br/>
												</span>
												<span class="category">
												plotId: <span class="item"><xsl:value-of select="../plotId"/>
												</span>
												<br/>
												</span>
												<span class="category">
												plotAccessionNumber: <span class="item"><xsl:value-of select="../plotAccessionNumber"/>
												</span>
												<br/>
												</span>
												<span class="category">
												authorPlotCode: <span class="item"><xsl:value-of select="../authorPlotCode"/>
												</span>
												<br/>
												</span>
												<span class="category">
												latitude: <span class="item"><xsl:value-of select="../latitude"/>
												</span>
												<br/>
												</span>
												<span class="category">
												longitude: <span class="item"><xsl:value-of select="../longitude"/>
												</span>
												<br/>
												</span>
												<span class="category">
												area: <span class="item"><xsl:value-of select="../area"/>
												</span>
												<br/>
												</span>
												<span class="category">
												permanence: <span class="item"><xsl:value-of select="../permanence"/>
												</span>
												<br/>
												</span>
												<span class="category">
												elevation: <span class="item"><xsl:value-of select="../elevation"/>
												</span>
												<br/>
												</span>
												<span class="category">
												slopeAspect: <span class="item"><xsl:value-of select="../slopeAspect"/>
												</span>
												<br/>
												</span>
												<span class="category">
												slopeGradient: <span class="item"><xsl:value-of select="../slopeGradient"/>
												</span>
												<br/>
												</span>
												<span class="category">
												topoPosition: <span class="item"><xsl:value-of select="../topoPosition"/>
												</span>
												<br/>
												</span>
												<span class="category">
												landform: <span class="item"><xsl:value-of select="../landform"/>
												</span>
												<br/>
												</span>
												<span class="category">
												geology: <span class="item"><xsl:value-of select="../geology"/>
												</span>
												<br/>
												</span>
												<span class="category">
												state: <span class="item"><xsl:value-of select="../state"/>
												</span>
												<br/>
												</span>
												<span class="category">
												country: <span class="item"><xsl:value-of select="../country"/>
												</span>
												<br/>
												</span>
												<span class="category">
												authorObsCode: <span class="item"><xsl:value-of select="authorObsCode"/>
												</span>
												<br/>
												</span>
												<span class="category">
												obsStartDate: <span class="item"><xsl:value-of select="obsStartDate"/>
												</span>
												<br/>
												</span>
												<span class="category">
												obsEndDate: <span class="item"><xsl:value-of select="obsEndDate"/>
												</span>
												<br/>
												</span>
												<span class="category">
												taxonObservationArea: <span class="item"><xsl:value-of select="taxonObservationArea"/>
												</span>
												<br/>
												</span>
												<span class="category">
												autotaxonCover: <span class="item"><xsl:value-of select="autotaxonCover"/>
												</span>
												<br/>
												</span>
												<span class="category">
												observationNarrative: <span class="item"><xsl:value-of select="observationNarrative"/>
												</span>
												<br/>
												</span>
												<span class="category">
												basalArea: <span class="item"><xsl:value-of select="basalArea"/>
												</span>
												<br/>
												</span>
												<span class="category">
												hydrologicRegime: <span class="item"><xsl:value-of select="hydrologicRegime"/>
												</span>
												<br/>
												</span>
												<span class="category">
												soilDepth: <span class="item"><xsl:value-of select="soilDepth"/>
												</span>
												<br/>
												</span>
												<span class="category">
												className: <span class="item"><xsl:value-of select="communityClassification/className"/>
												</span>
												<br/>
												</span>
						<hr/>
						<xsl:for-each select="observationContributor">
						<xsl:sort select="/"/>
						observationContributor <xsl:number value="position()"/> <br/>
												<span class="category">
												wholeName: <span class="item"><xsl:value-of select="wholeName"/>
												</span>
												<br/>
												</span>
						</xsl:for-each>
						<hr/>
												</b>
											</font>
										</p>
									</td>
						<tr>
						<th class="tablehead" width="45%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Taxon Name</font>
						</th>
						<th class="tablehead" width="15%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Taxon Code</font>
						</th>
						<th class="tablehead" width="40%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Taxon Cover</font>
						</th>	
						</tr>
						<xsl:for-each select="taxonObservation">
						<xsl:sort order="descending" select="taxonCover"/>
						<tr colspan="3">
										<p>
											<font size="-1" face="Arial, Helvetica, sans-serif">
												<b>
									<td vAlign="top" align="left" width="40%" bgcolor="{$oddRowColor}" height="10">

					<!-- EDIT	taxonObservation <xsl:number value="position()"/> <br/> -->
											
										       	<span class="item">  <xsl:value-of select="authorNameId"/> </span> </td>
											<td vAlign="top" align="left" width="40%" bgcolor="{$oddRowColor}" height="10">
<xsl:value-of select="authorCodeId"/> </td>
                                                                           <td vAlign="top" align="left" width="40%" bgcolor="{$oddRowColor}" height="10">
     <xsl:value-of select="taxonCover"/> </td>
												
											<!--	<br/> -->
												</b>
											</font>
											
										</p>
						</tr>
						</xsl:for-each>

									
									</tr>
							  </xsl:if>
							  <!--  ODD ROW:: -->
							  <xsl:if test="position() mod 2 = 1"> 
								<tr vAlign="top">
									<td vAlign="top" align="left" width="60%" bgcolor="{$evenRowColor}" height="106" colspan="3">
										<p>
											<font size="-1" face="Arial, Helvetica, sans-serif">
												<b>
												<span class="category">
												projectName: <span class="item"><xsl:value-of select="../../projectName"/>
												</span>
												<br/>
												</span>
												<span class="category">
												projectDescription: <span class="item"><xsl:value-of select="../../projectDescription"/>
												</span>
												<br/>
												</span>
												<span class="category">
												startDate: <span class="item"><xsl:value-of select="../../startDate"/>
												</span>
												<br/>
												</span>
												<span class="category">
												plotId: <span class="item"><xsl:value-of select="../plotId"/>
												</span>
												<br/>
												</span>
												<span class="category">
												plotAccessionNumber: <span class="item"><xsl:value-of select="../plotAccessionNumber"/>
												</span>
												<br/>
												</span>
												<span class="category">
												authorPlotCode: <span class="item"><xsl:value-of select="../authorPlotCode"/>
												</span>
												<br/>
												</span>
												<span class="category">
												latitude: <span class="item"><xsl:value-of select="../latitude"/>
												</span>
												<br/>
												</span>
												<span class="category">
												longitude: <span class="item"><xsl:value-of select="../longitude"/>
												</span>
												<br/>
												</span>
												<span class="category">
												area: <span class="item"><xsl:value-of select="../area"/>
												</span>
												<br/>
												</span>
												<span class="category">
												permanence: <span class="item"><xsl:value-of select="../permanence"/>
												</span>
												<br/>
												</span>
												<span class="category">
												elevation: <span class="item"><xsl:value-of select="../elevation"/>
												</span>
												<br/>
												</span>
												<span class="category">
												slopeAspect: <span class="item"><xsl:value-of select="../slopeAspect"/>
												</span>
												<br/>
												</span>
												<span class="category">
												slopeGradient: <span class="item"><xsl:value-of select="../slopeGradient"/>
												</span>
												<br/>
												</span>
												<span class="category">
												topoPosition: <span class="item"><xsl:value-of select="../topoPosition"/>
												</span>
												<br/>
												</span>
												<span class="category">
												landform: <span class="item"><xsl:value-of select="../landform"/>
												</span>
												<br/>
												</span>
												<span class="category">
												geology: <span class="item"><xsl:value-of select="../geology"/>
												</span>
												<br/>
												</span>
												<span class="category">
												state: <span class="item"><xsl:value-of select="../state"/>
												</span>
												<br/>
												</span>
												<span class="category">
												country: <span class="item"><xsl:value-of select="../country"/>
												</span>
												<br/>
												</span>
												<span class="category">
												authorObsCode: <span class="item"><xsl:value-of select="authorObsCode"/>
												</span>
												<br/>
												</span>
												<span class="category">
												obsStartDate: <span class="item"><xsl:value-of select="obsStartDate"/>
												</span>
												<br/>
												</span>
												<span class="category">
												obsEndDate: <span class="item"><xsl:value-of select="obsEndDate"/>
												</span>
												<br/>
												</span>
												<span class="category">
												taxonObservationArea: <span class="item"><xsl:value-of select="taxonObservationArea"/>
												</span>
												<br/>
												</span>
												<span class="category">
												autotaxonCover: <span class="item"><xsl:value-of select="autotaxonCover"/>
												</span>
												<br/>
												</span>
												<span class="category">
												observationNarrative: <span class="item"><xsl:value-of select="observationNarrative"/>
												</span>
												<br/>
												</span>
												<span class="category">
												basalArea: <span class="item"><xsl:value-of select="basalArea"/>
												</span>
												<br/>
												</span>
												<span class="category">
												hydrologicRegime: <span class="item"><xsl:value-of select="hydrologicRegime"/>
												</span>
												<br/>
												</span>
												<span class="category">
												soilDepth: <span class="item"><xsl:value-of select="soilDepth"/>
												</span>
												<br/>
												</span>
												<span class="category">
												className: <span class="item"><xsl:value-of select="communityClassification/className"/>
												</span>
												<br/>
												</span>
						<hr/>
						<xsl:for-each select="observationContributor">
						<xsl:sort select="/"/>
						observationContributor <xsl:number value="position()"/> <br/>
												<span class="category">
												wholeName: <span class="item"><xsl:value-of select="wholeName"/>
												</span>
												<br/>
												</span>
						</xsl:for-each>
						<hr/>
												</b>
											</font>
										</p>
									</td>
						<tr>
						<th class="tablehead" width="45%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Taxon Name</font>
						</th>
						<th class="tablehead" width="15%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Taxon Code</font>
						</th>
						<th class="tablehead" width="40%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Taxon Cover</font>
						</th>	
						</tr>
						<xsl:for-each select="taxonObservation">
						<xsl:sort order="descending" select="taxonCover"/>
						<tr colspan="3">
										<p>
											<font size="-1" face="Arial, Helvetica, sans-serif">
												<b>
									<td vAlign="top" align="left" width="40%" bgcolor="{$evenRowColor}" height="10">

					<!-- EDIT	taxonObservation <xsl:number value="position()"/> <br/> -->
											
										       	<span class="item">  <xsl:value-of select="authorNameId"/> </span> </td>
											<td vAlign="top" align="left" width="40%" bgcolor="{$evenRowColor}" height="10">
<xsl:value-of select="authorCodeId"/> </td>
                                                                           <td vAlign="top" align="left" width="40%" bgcolor="{$evenRowColor}" height="10">
     <xsl:value-of select="taxonCover"/> </td>
												

												</b>
											</font>
											
										</p>
						</tr>
						</xsl:for-each>

									
									</tr>
							  </xsl:if>
						</xsl:for-each>
				<!--		<hr/> -->
					</form>
				</table>
			<!-- VEGBANK FOOTER -->
			@vegbank_footer_html_tworow@
			<!-- END OF FOOTER -->
		</body>
	</html>
	</xsl:template>
</xsl:stylesheet>
