<?xml version="1.0"?>
<!--
  * showSummary.xsl
  *
  *      Authors: John Harris, Michael Lee
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
  <xsl:template match="/">
    <html>
      <xsl:variable name="pageTitle">Observation, full details</xsl:variable>
      <head>
        <title>VegBank -- <xsl:copy-of select="$pageTitle"/>
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
        <!-- br/ -->
    
	<!--VEGBANK HEADER -->
	@vegbank_header_html_normal@
	<!--END OF VEGBANK HEADER -->

        <blockquote>
          <p>
            <font face="Arial, Helvetica, sans-serif" size="5">
              <xsl:copy-of select="$pageTitle"/>
            </font>
          </p>
        </blockquote>
        <table width="799" cellpadding="0" cellspacing="0" border="1">
          <tr vAlign="top" align="left" colspan="1">
            <th class="tablehead" colspan="3" height="25">
              <font face="Arial, Helvetica, sans-serif">
												Project Name: <xsl:value-of select="/vegPlotPackage/project/projectName"/>
                <br/>
												Observation count: <xsl:number value="count(/vegPlotPackage/project/plot/observation)"/>
                <br/>
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
            <!-- EDIT						<hr/> -->
            <xsl:for-each select="/vegPlotPackage/project/plot/observation">
              <xsl:sort select="/vegPlotPackage/project/plot/observation/authorObsCode"/>
              <!-- EDIT	/vegPlotPackage/project/plot/observation <xsl:number value="position()"/> -->
              <!-- EDIT			<br/> -->
              <xsl:if test="position() mod 2 = 0">
<tr valign="center" align="left"><td colspan="2" class="category">
      <p>
        <font size="-1" face="Arial, Helvetica, sans-serif">
          <b>
   Jump to: 
  <a href="#{position()}plotobs">Plot/Obs</a> | 
  <a href="#{position()}cover">Cover/Strata</a> |
  <a href="#{position()}comm">Community</a> |
  <a href="#{position()}stratumdefn">Strata Defn</a> |
  <a href="#{position()}proj">Project</a> |
  <a href="#{position()}ObsContrib">ObsContrib</a>
  </b></font></p>
</td></tr>
<tr valign="center" align="left" colspan="1">
              <td class="tablehead" width="35%" bgcolor="#336633">
                <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1"><b>
           <a name="{position()}plotobs">Plot and Observation Data</a></b></font>
              </td>
              <td class="tablehead" width="65%" bgcolor="#336633">
                <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1"><b>
                <a name="{position()}cover">Plant Cover/Stratum Data</a></b></font>
              </td>
</tr>

  <tr vAlign="top">
   
    <td vAlign="top" align="left" width="35%" bgcolor="{$oddRowColor}" height="106">
      <p>
        <font size="-1" face="Arial, Helvetica, sans-serif">
          <b>
            <span class="category">
												plotId: <span class="item">
                <xsl:value-of select="../plotId"/>
              </span>
              <br/>
            </span>
            <span class="category">
												plotAccessionNumber: <span class="item">
                <xsl:value-of select="../plotAccessionNumber"/>
              </span>
              <br/>
            </span>
            <span class="category">
												authorPlotCode: <span class="item">
                <xsl:value-of select="../authorPlotCode"/>
              </span>
              <br/>
            </span>
            <span class="category">
												latitude: <span class="item">
                <xsl:value-of select="../latitude"/>
              </span>
              <br/>
            </span>
            <span class="category">
												longitude: <span class="item">
                <xsl:value-of select="../longitude"/>
              </span>
              <br/>
            </span>
            <span class="category">
												authorE: <span class="item">
                <xsl:value-of select="../authorE"/>
              </span>
              <br/>
            </span>
            <span class="category">
												authorN: <span class="item">
                <xsl:value-of select="../authorN"/>
              </span>
              <br/>
            </span>
            <span class="category">
												authorZone: <span class="item">
                <xsl:value-of select="../authorZone"/>
              </span>
              <br/>
            </span>
            <span class="category">
												authorDatum: <span class="item">
                <xsl:value-of select="../authorDatum"/>
              </span>
              <br/>
            </span>
            <span class="category">
												authorLocation: <span class="item">
                <xsl:value-of select="../authorLocation"/>
              </span>
              <br/>
            </span>
            <span class="category">
												locationNarrative: <span class="item">
                <xsl:value-of select="../locationNarrative"/>
              </span>
              <br/>
            </span>
            <span class="category">
												confidentialityStatus: <span class="item">
                <xsl:value-of select="../confidentialityStatus"/>
              </span>
              <br/>
            </span>
            <span class="category">
												confidentialityReason: <span class="item">
                <xsl:value-of select="../confidentialityReason"/>
              </span>
              <br/>
            </span>
            <span class="category">
												azimuth: <span class="item">
                <xsl:value-of select="../azimuth"/>
              </span>
              <br/>
            </span>
            <span class="category">
												dsgPoly: <span class="item">
                <xsl:value-of select="../dsgPoly"/>
              </span>
              <br/>
            </span>
            <span class="category">
												shape: <span class="item">
                <xsl:value-of select="../shape"/>
              </span>
              <br/>
            </span>
            <span class="category">
												area: <span class="item">
                <xsl:value-of select="../area"/>
              </span>
              <br/>
            </span>
            <span class="category">
												standSize: <span class="item">
                <xsl:value-of select="../standSize"/>
              </span>
              <br/>
            </span>
            <span class="category">
												placementMethod: <span class="item">
                <xsl:value-of select="../placementMethod"/>
              </span>
              <br/>
            </span>
            <span class="category">
												permanence: <span class="item">
                <xsl:value-of select="../permanence"/>
              </span>
              <br/>
            </span>
            <span class="category">
												layoutNarative: <span class="item">
                <xsl:value-of select="../layoutNarative"/>
              </span>
              <br/>
            </span>
            <span class="category">
												elevation: <span class="item">
                <xsl:value-of select="../elevation"/>
              </span>
              <br/>
            </span>
            <span class="category">
												elevationAccuracy: <span class="item">
                <xsl:value-of select="../elevationAccuracy"/>
              </span>
              <br/>
            </span>
            <span class="category">
												slopeAspect: <span class="item">
                <xsl:value-of select="../slopeAspect"/>
              </span>
              <br/>
            </span>
            <span class="category">
												slopeGradient: <span class="item">
                <xsl:value-of select="../slopeGradient"/>
              </span>
              <br/>
            </span>
            <span class="category">
												topoPosition: <span class="item">
                <xsl:value-of select="../topoPosition"/>
              </span>
              <br/>
            </span>
            <span class="category">
												landform: <span class="item">
                <xsl:value-of select="../landform"/>
              </span>
              <br/>
            </span>
            <span class="category">
												geology: <span class="item">
                <xsl:value-of select="../geology"/>
              </span>
              <br/>
            </span>
            <span class="category">
												soilTaxon: <span class="item">
                <xsl:value-of select="../soilTaxon"/>
              </span>
              <br/>
            </span>
            <span class="category">
												soilTaxonSource: <span class="item">
                <xsl:value-of select="../soilTaxonSource"/>
              </span>
              <br/>
            </span>
            <span class="category">
												notesPublic: <span class="item">
                <xsl:value-of select="../notesPublic"/>
              </span>
              <br/>
            </span>
            <span class="category">
												notesMgt: <span class="item">
                <xsl:value-of select="../notesMgt"/>
              </span>
              <br/>
            </span>
            <span class="category">
												revisions: <span class="item">
                <xsl:value-of select="../revisions"/>
              </span>
              <br/>
            </span>
            <span class="category">
												state: <span class="item">
                <xsl:value-of select="../state"/>
              </span>
              <br/>
            </span>
            <span class="category">
												country: <span class="item">
                <xsl:value-of select="../country"/>
              </span>
              <br/>
            </span>
           <span class="category">
												authorObsCode: <span class="item">
                <xsl:value-of select="authorObsCode"/>
              </span>
              <br/>
            </span>
            <span class="category">
												obsStartDate: <span class="item">
                <xsl:value-of select="obsStartDate"/>
              </span>
              <br/>
            </span>
            <span class="category">
												obsEndDate: <span class="item">
                <xsl:value-of select="obsEndDate"/>
              </span>
              <br/>
            </span>
            <span class="category">
												dateAccuracy: <span class="item">
                <xsl:value-of select="dateAccuracy"/>
              </span>
              <br/>
            </span>
            <span class="category">
												sampleMethodId: <span class="item">
                <xsl:value-of select="sampleMethodId"/>
              </span>
              <br/>
            </span>
            <span class="category">
												coverMethodId: <span class="item">
                <xsl:value-of select="coverMethodId"/>
              </span>
              <br/>
            </span>
            <span class="category">
												stratumMethodId: <span class="item">
                <xsl:value-of select="stratumMethodId"/>
              </span>
              <br/>
            </span>
            <span class="category">
												stemSizeLimit: <span class="item">
                <xsl:value-of select="stemSizeLimit"/>
              </span>
              <br/>
            </span>
            <span class="category">
												methodNarrative: <span class="item">
                <xsl:value-of select="methodNarrative"/>
              </span>
              <br/>
            </span>
            <span class="category">
												taxonObservationArea: <span class="item">
                <xsl:value-of select="taxonObservationArea"/>
              </span>
              <br/>
            </span>
            <span class="category">
												coverDispersion: <span class="item">
                <xsl:value-of select="coverDispersion"/>
              </span>
              <br/>
            </span>
            <span class="category">
												autotaxonCover: <span class="item">
                <xsl:value-of select="autotaxonCover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												stemObservationArea: <span class="item">
                <xsl:value-of select="stemObservationArea"/>
              </span>
              <br/>
            </span>
            <span class="category">
												stemSampleMethod: <span class="item">
                <xsl:value-of select="stemSampleMethod"/>
              </span>
              <br/>
            </span>
            <span class="category">
												originalData: <span class="item">
                <xsl:value-of select="originalData"/>
              </span>
              <br/>
            </span>
            <span class="category">
												effortLevel: <span class="item">
                <xsl:value-of select="effortLevel"/>
              </span>
              <br/>
            </span>
            <span class="category">
												plotValidationLevel: <span class="item">
                <xsl:value-of select="plotValidationLevel"/>
              </span>
              <br/>
            </span>
            <span class="category">
												floristicQuality: <span class="item">
                <xsl:value-of select="floristicQuality"/>
              </span>
              <br/>
            </span>
            <span class="category">
												bryophyteQuality: <span class="item">
                <xsl:value-of select="bryophyteQuality"/>
              </span>
              <br/>
            </span>
            <span class="category">
												lichenQuality: <span class="item">
                <xsl:value-of select="lichenQuality"/>
              </span>
              <br/>
            </span>
            <span class="category">
												observationNarrative: <span class="item">
                <xsl:value-of select="observationNarrative"/>
              </span>
              <br/>
            </span>
            <span class="category">
												landscapeNarrative: <span class="item">
                <xsl:value-of select="landscapeNarrative"/>
              </span>
              <br/>
            </span>
            <span class="category">
												homogeneity: <span class="item">
                <xsl:value-of select="homogeneity"/>
              </span>
              <br/>
            </span>
            <span class="category">
												phenologicalAspect: <span class="item">
                <xsl:value-of select="phenologicalAspect"/>
              </span>
              <br/>
            </span>
            <span class="category">
												representativeness: <span class="item">
                <xsl:value-of select="representativeness"/>
              </span>
              <br/>
            </span>
            <span class="category">
												basalArea: <span class="item">
                <xsl:value-of select="basalArea"/>
              </span>
              <br/>
            </span>
            <span class="category">
												hydrologicRegime: <span class="item">
                <xsl:value-of select="hydrologicRegime"/>
              </span>
              <br/>
            </span>
            <span class="category">
												soilMoistureRegime: <span class="item">
                <xsl:value-of select="soilMoistureRegime"/>
              </span>
              <br/>
            </span>
            <span class="category">
												soilDrainage: <span class="item">
                <xsl:value-of select="soilDrainage"/>
              </span>
              <br/>
            </span>
            <span class="category">
												waterSalinity: <span class="item">
                <xsl:value-of select="waterSalinity"/>
              </span>
              <br/>
            </span>
            <span class="category">
												waterDepth: <span class="item">
                <xsl:value-of select="waterDepth"/>
              </span>
              <br/>
            </span>
            <span class="category">
												shoreDistance: <span class="item">
                <xsl:value-of select="shoreDistance"/>
              </span>
              <br/>
            </span>
            <span class="category">
												soilDepth: <span class="item">
                <xsl:value-of select="soilDepth"/>
              </span>
              <br/>
            </span>
            <span class="category">
												organicDepth: <span class="item">
                <xsl:value-of select="organicDepth"/>
              </span>
              <br/>
            </span>
            <span class="category">
												percentBedrock: <span class="item">
                <xsl:value-of select="percentBedrock"/>
              </span>
              <br/>
            </span>
            <span class="category">
												percentRockGravel: <span class="item">
                <xsl:value-of select="percentRockGravel"/>
              </span>
              <br/>
            </span>
            <span class="category">
												percentWood: <span class="item">
                <xsl:value-of select="percentWood"/>
              </span>
              <br/>
            </span>
            <span class="category">
												percentLitter: <span class="item">
                <xsl:value-of select="percentLitter"/>
              </span>
              <br/>
            </span>
            <span class="category">
												percentBareSoil: <span class="item">
                <xsl:value-of select="percentBareSoil"/>
              </span>
              <br/>
            </span>
            <span class="category">
												percentWater: <span class="item">
                <xsl:value-of select="percentWater"/>
              </span>
              <br/>
            </span>
            <span class="category">
												percentOther: <span class="item">
                <xsl:value-of select="percentOther"/>
              </span>
              <br/>
            </span>
            <span class="category">
												nameOther: <span class="item">
                <xsl:value-of select="nameOther"/>
              </span>
              <br/>
            </span>
            <span class="category">
												standMaturity: <span class="item">
                <xsl:value-of select="standMaturity"/>
              </span>
              <br/>
            </span>
            <span class="category">
												successionalStatus: <span class="item">
                <xsl:value-of select="successionalStatus"/>
              </span>
              <br/>
            </span>
            <span class="category">
												treeHt: <span class="item">
                <xsl:value-of select="treeHt"/>
              </span>
              <br/>
            </span>
            <span class="category">
												shrubHt: <span class="item">
                <xsl:value-of select="shrubHt"/>
              </span>
              <br/>
            </span>
            <span class="category">
												fieldHt: <span class="item">
                <xsl:value-of select="fieldHt"/>
              </span>
              <br/>
            </span>
            <span class="category">
												nonvascularHt: <span class="item">
                <xsl:value-of select="nonvascularHt"/>
              </span>
              <br/>
            </span>
            <span class="category">
												submergedHt: <span class="item">
                <xsl:value-of select="submergedHt"/>
              </span>
              <br/>
            </span>
            <span class="category">
												treeCover: <span class="item">
                <xsl:value-of select="treeCover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												shrubCover: <span class="item">
                <xsl:value-of select="shrubCover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												fieldCover: <span class="item">
                <xsl:value-of select="fieldCover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												nonvascularCover: <span class="item">
                <xsl:value-of select="nonvascularCover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												floatingCover: <span class="item">
                <xsl:value-of select="floatingCover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												submergedCover: <span class="item">
                <xsl:value-of select="submergedCover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												dominantStratum: <span class="item">
                <xsl:value-of select="dominantStratum"/>
              </span>
              <br/>
            </span>
            <span class="category">
												growthform1type: <span class="item">
                <xsl:value-of select="growthform1type"/>
              </span>
              <br/>
            </span>
            <span class="category">
												growthform2type: <span class="item">
                <xsl:value-of select="growthform2type"/>
              </span>
              <br/>
            </span>
            <span class="category">
												growthform3type: <span class="item">
                <xsl:value-of select="growthform3type"/>
              </span>
              <br/>
            </span>
            <span class="category">
												growthform1cover: <span class="item">
                <xsl:value-of select="growthform1cover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												growthform2cover: <span class="item">
                <xsl:value-of select="growthform2cover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												growthform3cover: <span class="item">
                <xsl:value-of select="growthform3cover"/>
              </span>
              <br/>
            </span>
            <span class="category">
												notesPublic: <span class="item">
                <xsl:value-of select="notesPublic"/>
              </span>
              <br/>
            </span>
            <span class="category">
												notesMgt: <span class="item">
                <xsl:value-of select="notesMgt"/>
              </span>
              <br/>
            </span>
            <span class="category">
												revisions: <span class="item">
                <xsl:value-of select="revisions"/>
              </span>
              <br/>
            </span>
          </b>
        </font>
      </p>
    </td>
    
    <td vAlign="top" align="left" width="65%" bgcolor="{$oddRowColor}" height="106">
      <p>
        <font size="-1" face="Arial, Helvetica, sans-serif">
          <b>
            
            <table cellpadding="0" cellspacing="0" border="1">
              <tr vAlign="top">
                <td vAlign="top" align="left" width="8%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                  Count</span></b></font></p></td>
                <td vAlign="top" align="left" width="65%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                   Plant Name</span></b></font></p></td>
                <td vAlign="top" align="left" width="10%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                  Code
                  </span></b></font></p></td>
                <td vAlign="top" align="left" width="7%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                  Cover
                  </span></b></font></p></td>
                <td vAlign="top" align="left" width="10%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                  Stratum
                  </span></b></font></p></td>
              </tr>  
              
                <xsl:for-each select="taxonObservation">
                 <xsl:sort select="/"/>
              <tr vAlign="top">
              <td>
               <span class="item">

                 <xsl:number value="position()"/>
               </span>
              </td>
              <td>
               <span class="item">
                  <xsl:value-of select="authorNameId"/>
                </span>
              </td>
              <td>
                <span class="item">
                    <xsl:value-of select="authorCodeId"/>
                </span>
              </td>
              <td>
                <span class="item">
                  <xsl:value-of select="taxonCover"/>
                </span>
              </td>
              <td><span class="item">--all--</span></td>
              </tr>
            
              <xsl:for-each select="stratumComposition">
                <xsl:sort select="/"/>
               <tr>
               <td colspan="3" />
               

							 <td>					
									 <span class="item">
                    <xsl:value-of select="taxonStratumCover"/>
                  </span>
                </td>
                <td>
									<span class="item">
                    <xsl:value-of select="stratumName"/>
                  </span>
                </td>
                </tr>
              </xsl:for-each>
            </xsl:for-each>
            </table>
            
          </b>
        </font>
      </p>
    </td>
    </tr>
    <tr valign="center" align="left" colspan="1">
        <td class="tablehead" bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1"><b>
                    <a name="{position()}comm">Community Classification</a></b></font>
                  </td>
                  <td class="tablehead"  bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1"><b>
                    <a name="{position()}stratumdefn">Strata Definitions</a></b></font>
              </td>
    </tr>          
    
    <tr vAlign="top">
    <td vAlign="top" align="left" width="35%" bgcolor="{$oddRowColor}" height="106">
          <p>
            <font size="-1" face="Arial, Helvetica, sans-serif">
              <b>
                <xsl:for-each select="communityClassification">
                  <xsl:sort select="/"/>
                communityClassification <xsl:number value="position()"/>
                  <br/>
                  <span class="category">
                            className: <span class="item">
                      <xsl:value-of select="className"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            classCode: <span class="item">
                      <xsl:value-of select="classCode"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            classStartDate: <span class="item">
                      <xsl:value-of select="classStartDate"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            classStopDate: <span class="item">
                      <xsl:value-of select="classStopDate"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            classInspection: <span class="item">
                      <xsl:value-of select="classInspection"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            tableAnalysis: <span class="item">
                      <xsl:value-of select="tableAnalysis"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            multivariateAnalysis: <span class="item">
                      <xsl:value-of select="multivariateAnalysis"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            expertSystem: <span class="item">
                      <xsl:value-of select="expertSystem"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            classNotes: <span class="item">
                      <xsl:value-of select="classNotes"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            classPublication: <span class="item">
                      <xsl:value-of select="classPublication"/>
                    </span>
                    <br/>
                  </span>
                </xsl:for-each>
              </b>
            </font>
          </p>
    </td>
    <td vAlign="top" align="left" width="65%" bgcolor="{$oddRowColor}" height="106">
          <p>
            <font size="-1" face="Arial, Helvetica, sans-serif">
              <b>
                <table cellpadding="0" cellspacing="0" border="1">
                              <tr vAlign="top">
                                <td vAlign="top" align="left" width="8%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                                  Count</span></b></font></p></td>
                                <td vAlign="top" align="left" width="25%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                                   Stratum Name</span></b></font></p></td>
                                <td vAlign="top" align="left" width="10%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                                   Stratum Base</span></b></font></p></td>
                                <td vAlign="top" align="left" width="7%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                                   Stratum Height
                                  </span></b></font></p></td>
                                <td vAlign="top" align="left" width="10%" bgcolor="{$oddRowColor}"><p><font size="-1" face="Arial, Helvetica, sans-serif"><b><span class="category">
                                  Stratum Cover
                                  </span></b></font></p></td>
                    </tr>  
                <xsl:for-each select="stratum">
                  <xsl:sort select="/"/>
                  <tr vAlign="top">
                  <td>
                  <span class="item"><xsl:number value="position()"/></span>
                  </td>
                  <td>
                    <span class="item">
                      <xsl:value-of select="stratumName"/>
                    </span>
                  </td>
                  <td>
                    <span class="item">
                      <xsl:value-of select="stratumBase"/>
                    </span>
                  </td>
                  <td>
                    <span class="item">
                      <xsl:value-of select="stratumHeight"/>
                    </span>
                  </td>
                  <td>
                    <span class="item">
                      <xsl:value-of select="stratumCover"/>
                    </span>
                  </td>
                  </tr>
                </xsl:for-each>
                </table>
              </b>
            </font>
          </p>
    </td>
    </tr>
<tr valign="center" align="left" colspan="1">
                  <td class="tablehead"  bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1"><b>
                    <a name="{position()}proj">Project Information</a></b></font>
                  </td>
                  <td class="tablehead"  bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1"><b>
                    <a name="{position()}ObsContrib">Observation Contributors</a></b></font>
                  </td>                  
 </tr>
    <tr vAlign="top">
     <td vAlign="top" align="left" width="35%" bgcolor="{$oddRowColor}" height="106">
          <p>
            <font size="-1" face="Arial, Helvetica, sans-serif">
              <b>
                <span class="category">
                            projectName: <span class="item">
                    <xsl:value-of select="../../projectName"/>
                  </span>
                  <br/>
                </span>
                <span class="category">
                            projectDescription: <span class="item">
                    <xsl:value-of select="../../projectDescription"/>
                  </span>
                  <br/>
                </span>
                <span class="category">
                            startDate: <span class="item">
                    <xsl:value-of select="../../startDate"/>
                  </span>
                  <br/>
                </span>
                <span class="category">
                            stopDate: <span class="item">
                    <xsl:value-of select="../../stopDate"/>
                  </span>
                  <br/>
                </span>
                <hr/>
                <xsl:for-each select="../../projectContributor">
                  <xsl:sort select="surName"/>
                projectContributor <xsl:number value="position()"/>
                  <br/>
                  <span class="category">
                            wholeName: <span class="item">
                      <xsl:value-of select="wholeName"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            salutation: <span class="item">
                      <xsl:value-of select="salutation"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            givenName: <span class="item">
                      <xsl:value-of select="givenName"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            middleName: <span class="item">
                      <xsl:value-of select="middleName"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            surName: <span class="item">
                      <xsl:value-of select="surName"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            organizationName: <span class="item">
                      <xsl:value-of select="organizationName"/>
                    </span>
                    <br/>
                  </span>
                  <span class="category">
                            contactInstructions: <span class="item">
                      <xsl:value-of select="contactInstructions"/>
                    </span>
                    <br/>
                  </span>
                  <blockquote>
                  <xsl:for-each select="telephone">
                  <xsl:sort select="/"/>
                  
                telephone <xsl:number value="position()"/>
                    <br/>
                    <span class="category">
                            phoneNumber: <span class="item">
                        <xsl:value-of select="phoneNumber"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            phoneType: <span class="item">
                        <xsl:value-of select="phoneType"/>
                      </span>
                      <br/>
                    </span>
                  
                  </xsl:for-each>
                  </blockquote>
                  <hr/>
                  <blockquote>
                  <xsl:for-each select="address">
                    <xsl:sort select="/"/>
                  
                address <xsl:number value="position()"/>
                    <br/>
                    <span class="category">
                            orgPosition: <span class="item">
                        <xsl:value-of select="orgPosition"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            email: <span class="item">
                        <xsl:value-of select="email"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            deliveryPoint: <span class="item">
                        <xsl:value-of select="deliveryPoint"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            city: <span class="item">
                        <xsl:value-of select="city"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            administrativeArea: <span class="item">
                        <xsl:value-of select="administrativeArea"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            postalCode: <span class="item">
                        <xsl:value-of select="postalCode"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            country: <span class="item">
                        <xsl:value-of select="country"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            currentFlag: <span class="item">
                        <xsl:value-of select="currentFlag"/>
                      </span>
                      <br/>
                    </span>
                    <span class="category">
                            addressStartDate: <span class="item">
                        <xsl:value-of select="addressStartDate"/>
                      </span>
                      <br/>
                    </span>
                  </xsl:for-each>
                  </blockquote>
                  <hr/>
                </xsl:for-each>
                <hr/>
              </b>
            </font>
          </p>
    </td>
     
        <td vAlign="top" align="left" width="35%" bgcolor="{$oddRowColor}" height="106">
              <p>
                <font size="-1" face="Arial, Helvetica, sans-serif">
                  <b>
                      
                    <xsl:for-each select="observationContributor">
                      <xsl:sort select="surName"/>
                    observationContributor <xsl:number value="position()"/>
                      <br/>
                      <span class="category">
                                wholeName: <span class="item">
                          <xsl:value-of select="wholeName"/>
                        </span>
                        <br/>
                      </span>
                      <span class="category">
                                salutation: <span class="item">
                          <xsl:value-of select="salutation"/>
                        </span>
                        <br/>
                      </span>
                      <span class="category">
                                givenName: <span class="item">
                          <xsl:value-of select="givenName"/>
                        </span>
                        <br/>
                      </span>
                      <span class="category">
                                middleName: <span class="item">
                          <xsl:value-of select="middleName"/>
                        </span>
                        <br/>
                      </span>
                      <span class="category">
                                surName: <span class="item">
                          <xsl:value-of select="surName"/>
                        </span>
                        <br/>
                      </span>
                      <span class="category">
                                organizationName: <span class="item">
                          <xsl:value-of select="organizationName"/>
                        </span>
                        <br/>
                      </span>
                      <span class="category">
                                contactInstructions: <span class="item">
                          <xsl:value-of select="contactInstructions"/>
                        </span>
                        <br/>
                      </span>
                      <hr/>
                      <blockquote>
                      <xsl:for-each select="telephone">
                        <xsl:sort select="/"/>
                    telephone <xsl:number value="position()"/>
                        <br/>
                        <span class="category">
                                phoneNumber: <span class="item">
                            <xsl:value-of select="phoneNumber"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                phoneType: <span class="item">
                            <xsl:value-of select="phoneType"/>
                          </span>
                          <br/>
                        </span>
                      </xsl:for-each>
                      </blockquote>
                      <hr/>
                      <blockquote>
                      <xsl:for-each select="address">
                        <xsl:sort select="/"/>
                    address <xsl:number value="position()"/>
                        <br/>
                        <span class="category">
                                orgPosition: <span class="item">
                            <xsl:value-of select="orgPosition"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                email: <span class="item">
                            <xsl:value-of select="email"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                deliveryPoint: <span class="item">
                            <xsl:value-of select="deliveryPoint"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                city: <span class="item">
                            <xsl:value-of select="city"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                administrativeArea: <span class="item">
                            <xsl:value-of select="administrativeArea"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                postalCode: <span class="item">
                            <xsl:value-of select="postalCode"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                country: <span class="item">
                            <xsl:value-of select="country"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                currentFlag: <span class="item">
                            <xsl:value-of select="currentFlag"/>
                          </span>
                          <br/>
                        </span>
                        <span class="category">
                                addressStartDate: <span class="item">
                            <xsl:value-of select="addressStartDate"/>
                          </span>
                          <br/>
                        </span>
                      </xsl:for-each>
                      </blockquote>
                      <hr/>
                    </xsl:for-each>
                    <hr/>
                  </b>
                </font>
              </p>
    </td>
  </tr>
</xsl:if>

              <xsl:if test="position() mod 2 = 1">
                <tr valign="center" align="left">
                  <td colspan="2" class="category">
                    <p>
                      <font size="-1" face="Arial, Helvetica, sans-serif">
                        <b>
   Jump to: 
  <a href="#{position()}plotobs">Plot/Obs</a> | 
  <a href="#{position()}cover">Cover/Strata</a> |
  <a href="#{position()}comm">Community</a> |
  <a href="#{position()}stratumdefn">Strata Defn</a> |
  <a href="#{position()}proj">Project</a> |
  <a href="#{position()}ObsContrib">ObsContrib</a>
                        </b>
                      </font>
                    </p>
                  </td>
                </tr>
                <tr valign="center" align="left" colspan="1">
                  <td class="tablehead" width="35%" bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">
                      <b>
                        <a name="{position()}plotobs">Plot and Observation Data</a>
                      </b>
                    </font>
                  </td>
                  <td class="tablehead" width="65%" bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">
                      <b>
                        <a name="{position()}cover">Plant Cover/Stratum Data</a>
                      </b>
                    </font>
                  </td>
                </tr>
                <tr vAlign="top">
                  <td vAlign="top" align="left" width="35%" bgcolor="{$evenRowColor}" height="106">
                    <p>
                      <font size="-1" face="Arial, Helvetica, sans-serif">
                        <b>
                          <span class="category">
												plotId: <span class="item">
                              <xsl:value-of select="../plotId"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												plotAccessionNumber: <span class="item">
                              <xsl:value-of select="../plotAccessionNumber"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												authorPlotCode: <span class="item">
                              <xsl:value-of select="../authorPlotCode"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												latitude: <span class="item">
                              <xsl:value-of select="../latitude"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												longitude: <span class="item">
                              <xsl:value-of select="../longitude"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												authorE: <span class="item">
                              <xsl:value-of select="../authorE"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												authorN: <span class="item">
                              <xsl:value-of select="../authorN"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												authorZone: <span class="item">
                              <xsl:value-of select="../authorZone"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												authorDatum: <span class="item">
                              <xsl:value-of select="../authorDatum"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												authorLocation: <span class="item">
                              <xsl:value-of select="../authorLocation"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												locationNarrative: <span class="item">
                              <xsl:value-of select="../locationNarrative"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												confidentialityStatus: <span class="item">
                              <xsl:value-of select="../confidentialityStatus"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												confidentialityReason: <span class="item">
                              <xsl:value-of select="../confidentialityReason"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												azimuth: <span class="item">
                              <xsl:value-of select="../azimuth"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												dsgPoly: <span class="item">
                              <xsl:value-of select="../dsgPoly"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												shape: <span class="item">
                              <xsl:value-of select="../shape"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												area: <span class="item">
                              <xsl:value-of select="../area"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												standSize: <span class="item">
                              <xsl:value-of select="../standSize"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												placementMethod: <span class="item">
                              <xsl:value-of select="../placementMethod"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												permanence: <span class="item">
                              <xsl:value-of select="../permanence"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												layoutNarative: <span class="item">
                              <xsl:value-of select="../layoutNarative"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												elevation: <span class="item">
                              <xsl:value-of select="../elevation"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												elevationAccuracy: <span class="item">
                              <xsl:value-of select="../elevationAccuracy"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												slopeAspect: <span class="item">
                              <xsl:value-of select="../slopeAspect"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												slopeGradient: <span class="item">
                              <xsl:value-of select="../slopeGradient"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												topoPosition: <span class="item">
                              <xsl:value-of select="../topoPosition"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												landform: <span class="item">
                              <xsl:value-of select="../landform"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												geology: <span class="item">
                              <xsl:value-of select="../geology"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												soilTaxon: <span class="item">
                              <xsl:value-of select="../soilTaxon"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												soilTaxonSource: <span class="item">
                              <xsl:value-of select="../soilTaxonSource"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												notesPublic: <span class="item">
                              <xsl:value-of select="../notesPublic"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												notesMgt: <span class="item">
                              <xsl:value-of select="../notesMgt"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												revisions: <span class="item">
                              <xsl:value-of select="../revisions"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												state: <span class="item">
                              <xsl:value-of select="../state"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												country: <span class="item">
                              <xsl:value-of select="../country"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												authorObsCode: <span class="item">
                              <xsl:value-of select="authorObsCode"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												obsStartDate: <span class="item">
                              <xsl:value-of select="obsStartDate"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												obsEndDate: <span class="item">
                              <xsl:value-of select="obsEndDate"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												dateAccuracy: <span class="item">
                              <xsl:value-of select="dateAccuracy"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												sampleMethodId: <span class="item">
                              <xsl:value-of select="sampleMethodId"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												coverMethodId: <span class="item">
                              <xsl:value-of select="coverMethodId"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												stratumMethodId: <span class="item">
                              <xsl:value-of select="stratumMethodId"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												stemSizeLimit: <span class="item">
                              <xsl:value-of select="stemSizeLimit"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												methodNarrative: <span class="item">
                              <xsl:value-of select="methodNarrative"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												taxonObservationArea: <span class="item">
                              <xsl:value-of select="taxonObservationArea"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												coverDispersion: <span class="item">
                              <xsl:value-of select="coverDispersion"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												autotaxonCover: <span class="item">
                              <xsl:value-of select="autotaxonCover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												stemObservationArea: <span class="item">
                              <xsl:value-of select="stemObservationArea"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												stemSampleMethod: <span class="item">
                              <xsl:value-of select="stemSampleMethod"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												originalData: <span class="item">
                              <xsl:value-of select="originalData"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												effortLevel: <span class="item">
                              <xsl:value-of select="effortLevel"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												plotValidationLevel: <span class="item">
                              <xsl:value-of select="plotValidationLevel"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												floristicQuality: <span class="item">
                              <xsl:value-of select="floristicQuality"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												bryophyteQuality: <span class="item">
                              <xsl:value-of select="bryophyteQuality"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												lichenQuality: <span class="item">
                              <xsl:value-of select="lichenQuality"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												observationNarrative: <span class="item">
                              <xsl:value-of select="observationNarrative"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												landscapeNarrative: <span class="item">
                              <xsl:value-of select="landscapeNarrative"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												homogeneity: <span class="item">
                              <xsl:value-of select="homogeneity"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												phenologicalAspect: <span class="item">
                              <xsl:value-of select="phenologicalAspect"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												representativeness: <span class="item">
                              <xsl:value-of select="representativeness"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												basalArea: <span class="item">
                              <xsl:value-of select="basalArea"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												hydrologicRegime: <span class="item">
                              <xsl:value-of select="hydrologicRegime"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												soilMoistureRegime: <span class="item">
                              <xsl:value-of select="soilMoistureRegime"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												soilDrainage: <span class="item">
                              <xsl:value-of select="soilDrainage"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												waterSalinity: <span class="item">
                              <xsl:value-of select="waterSalinity"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												waterDepth: <span class="item">
                              <xsl:value-of select="waterDepth"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												shoreDistance: <span class="item">
                              <xsl:value-of select="shoreDistance"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												soilDepth: <span class="item">
                              <xsl:value-of select="soilDepth"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												organicDepth: <span class="item">
                              <xsl:value-of select="organicDepth"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												percentBedrock: <span class="item">
                              <xsl:value-of select="percentBedrock"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												percentRockGravel: <span class="item">
                              <xsl:value-of select="percentRockGravel"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												percentWood: <span class="item">
                              <xsl:value-of select="percentWood"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												percentLitter: <span class="item">
                              <xsl:value-of select="percentLitter"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												percentBareSoil: <span class="item">
                              <xsl:value-of select="percentBareSoil"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												percentWater: <span class="item">
                              <xsl:value-of select="percentWater"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												percentOther: <span class="item">
                              <xsl:value-of select="percentOther"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												nameOther: <span class="item">
                              <xsl:value-of select="nameOther"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												standMaturity: <span class="item">
                              <xsl:value-of select="standMaturity"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												successionalStatus: <span class="item">
                              <xsl:value-of select="successionalStatus"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												treeHt: <span class="item">
                              <xsl:value-of select="treeHt"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												shrubHt: <span class="item">
                              <xsl:value-of select="shrubHt"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												fieldHt: <span class="item">
                              <xsl:value-of select="fieldHt"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												nonvascularHt: <span class="item">
                              <xsl:value-of select="nonvascularHt"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												submergedHt: <span class="item">
                              <xsl:value-of select="submergedHt"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												treeCover: <span class="item">
                              <xsl:value-of select="treeCover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												shrubCover: <span class="item">
                              <xsl:value-of select="shrubCover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												fieldCover: <span class="item">
                              <xsl:value-of select="fieldCover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												nonvascularCover: <span class="item">
                              <xsl:value-of select="nonvascularCover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												floatingCover: <span class="item">
                              <xsl:value-of select="floatingCover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												submergedCover: <span class="item">
                              <xsl:value-of select="submergedCover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												dominantStratum: <span class="item">
                              <xsl:value-of select="dominantStratum"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												growthform1type: <span class="item">
                              <xsl:value-of select="growthform1type"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												growthform2type: <span class="item">
                              <xsl:value-of select="growthform2type"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												growthform3type: <span class="item">
                              <xsl:value-of select="growthform3type"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												growthform1cover: <span class="item">
                              <xsl:value-of select="growthform1cover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												growthform2cover: <span class="item">
                              <xsl:value-of select="growthform2cover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												growthform3cover: <span class="item">
                              <xsl:value-of select="growthform3cover"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												notesPublic: <span class="item">
                              <xsl:value-of select="notesPublic"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												notesMgt: <span class="item">
                              <xsl:value-of select="notesMgt"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
												revisions: <span class="item">
                              <xsl:value-of select="revisions"/>
                            </span>
                            <br/>
                          </span>
                        </b>
                      </font>
                    </p>
                  </td>
                  <td vAlign="top" align="left" width="65%" bgcolor="{$evenRowColor}" height="106">
                    <p>
                      <font size="-1" face="Arial, Helvetica, sans-serif">
                        <b>
                          <table cellpadding="0" cellspacing="0" border="1">
                            <tr vAlign="top">
                              <td vAlign="top" align="left" width="8%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                  Count</span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="65%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                   Plant Name</span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="10%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                  Code
                  </span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="7%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                  Cover
                  </span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="10%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                  Stratum
                  </span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                            </tr>
                            <xsl:for-each select="taxonObservation">
                              <xsl:sort select="/"/>
                              <tr vAlign="top">
                                <td>
                                  <span class="item">
                                    <xsl:number value="position()"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">
                                    <xsl:value-of select="authorNameId"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">
                                    <xsl:value-of select="authorCodeId"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">
                                    <xsl:value-of select="taxonCover"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">--all--</span>
                                </td>
                              </tr>
                              <xsl:for-each select="stratumComposition">
                                <xsl:sort select="/"/>
                                <tr>
                                  <td colspan="3"/>
                                  <td>
                                    <span class="item">
                                      <xsl:value-of select="taxonStratumCover"/>
                                    </span>
                                  </td>
                                  <td>
                                    <span class="item">
                                      <xsl:value-of select="stratumName"/>
                                    </span>
                                  </td>
                                </tr>
                              </xsl:for-each>
                            </xsl:for-each>
                          </table>
                        </b>
                      </font>
                    </p>
                  </td>
                </tr>
                <tr valign="center" align="left" colspan="1">
                  <td class="tablehead" bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">
                      <b>
                        <a name="{position()}comm">Community Classification</a>
                      </b>
                    </font>
                  </td>
                  <td class="tablehead" bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">
                      <b>
                        <a name="{position()}stratumdefn">Strata Definitions</a>
                      </b>
                    </font>
                  </td>
                </tr>
                <tr vAlign="top">
                  <td vAlign="top" align="left" width="35%" bgcolor="{$evenRowColor}" height="106">
                    <p>
                      <font size="-1" face="Arial, Helvetica, sans-serif">
                        <b>
                          <xsl:for-each select="communityClassification">
                            <xsl:sort select="/"/>
                communityClassification <xsl:number value="position()"/>
                            <br/>
                            <span class="category">
                            className: <span class="item">
                                <xsl:value-of select="className"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            classCode: <span class="item">
                                <xsl:value-of select="classCode"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            classStartDate: <span class="item">
                                <xsl:value-of select="classStartDate"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            classStopDate: <span class="item">
                                <xsl:value-of select="classStopDate"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            classInspection: <span class="item">
                                <xsl:value-of select="classInspection"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            tableAnalysis: <span class="item">
                                <xsl:value-of select="tableAnalysis"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            multivariateAnalysis: <span class="item">
                                <xsl:value-of select="multivariateAnalysis"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            expertSystem: <span class="item">
                                <xsl:value-of select="expertSystem"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            classNotes: <span class="item">
                                <xsl:value-of select="classNotes"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            classPublication: <span class="item">
                                <xsl:value-of select="classPublication"/>
                              </span>
                              <br/>
                            </span>
                          </xsl:for-each>
                        </b>
                      </font>
                    </p>
                  </td>
                  <td vAlign="top" align="left" width="65%" bgcolor="{$evenRowColor}" height="106">
                    <p>
                      <font size="-1" face="Arial, Helvetica, sans-serif">
                        <b>
                          <table cellpadding="0" cellspacing="0" border="1">
                            <tr vAlign="top">
                              <td vAlign="top" align="left" width="8%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                                  Count</span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="25%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                                   Stratum Name</span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="10%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                                   Stratum Base</span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="7%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                                   Stratum Height
                                  </span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                              <td vAlign="top" align="left" width="10%" bgcolor="{$evenRowColor}">
                                <p>
                                  <font size="-1" face="Arial, Helvetica, sans-serif">
                                    <b>
                                      <span class="category">
                                  Stratum Cover
                                  </span>
                                    </b>
                                  </font>
                                </p>
                              </td>
                            </tr>
                            <xsl:for-each select="stratum">
                              <xsl:sort select="/"/>
                              <tr vAlign="top">
                                <td>
                                  <span class="item">
                                    <xsl:number value="position()"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">
                                    <xsl:value-of select="stratumName"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">
                                    <xsl:value-of select="stratumBase"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">
                                    <xsl:value-of select="stratumHeight"/>
                                  </span>
                                </td>
                                <td>
                                  <span class="item">
                                    <xsl:value-of select="stratumCover"/>
                                  </span>
                                </td>
                              </tr>
                            </xsl:for-each>
                          </table>
                        </b>
                      </font>
                    </p>
                  </td>
                </tr>
                <tr valign="center" align="left" colspan="1">
                  <td class="tablehead" bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">
                      <b>
                        <a name="{position()}proj">Project Information</a>
                      </b>
                    </font>
                  </td>
                  <td class="tablehead" bgcolor="#336633">
                    <font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">
                      <b>
                        <a name="{position()}ObsContrib">Observation Contributors</a>
                      </b>
                    </font>
                  </td>
                </tr>
                <tr vAlign="top">
                  <td vAlign="top" align="left" width="35%" bgcolor="{$evenRowColor}" height="106">
                    <p>
                      <font size="-1" face="Arial, Helvetica, sans-serif">
                        <b>
                          <span class="category">
                            projectName: <span class="item">
                              <xsl:value-of select="../../projectName"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
                            projectDescription: <span class="item">
                              <xsl:value-of select="../../projectDescription"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
                            startDate: <span class="item">
                              <xsl:value-of select="../../startDate"/>
                            </span>
                            <br/>
                          </span>
                          <span class="category">
                            stopDate: <span class="item">
                              <xsl:value-of select="../../stopDate"/>
                            </span>
                            <br/>
                          </span>
                          <hr/>
                          <xsl:for-each select="../../projectContributor">
                            <xsl:sort select="surName"/>
                projectContributor <xsl:number value="position()"/>
                            <br/>
                            <span class="category">
                            wholeName: <span class="item">
                                <xsl:value-of select="wholeName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            salutation: <span class="item">
                                <xsl:value-of select="salutation"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            givenName: <span class="item">
                                <xsl:value-of select="givenName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            middleName: <span class="item">
                                <xsl:value-of select="middleName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            surName: <span class="item">
                                <xsl:value-of select="surName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            organizationName: <span class="item">
                                <xsl:value-of select="organizationName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                            contactInstructions: <span class="item">
                                <xsl:value-of select="contactInstructions"/>
                              </span>
                              <br/>
                            </span>
                            <blockquote>
                              <xsl:for-each select="telephone">
                                <xsl:sort select="/"/>
                  
                telephone <xsl:number value="position()"/>
                                <br/>
                                <span class="category">
                            phoneNumber: <span class="item">
                                    <xsl:value-of select="phoneNumber"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            phoneType: <span class="item">
                                    <xsl:value-of select="phoneType"/>
                                  </span>
                                  <br/>
                                </span>
                              </xsl:for-each>
                            </blockquote>
                            <hr/>
                            <blockquote>
                              <xsl:for-each select="address">
                                <xsl:sort select="/"/>
                  
                address <xsl:number value="position()"/>
                                <br/>
                                <span class="category">
                            orgPosition: <span class="item">
                                    <xsl:value-of select="orgPosition"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            email: <span class="item">
                                    <xsl:value-of select="email"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            deliveryPoint: <span class="item">
                                    <xsl:value-of select="deliveryPoint"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            city: <span class="item">
                                    <xsl:value-of select="city"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            administrativeArea: <span class="item">
                                    <xsl:value-of select="administrativeArea"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            postalCode: <span class="item">
                                    <xsl:value-of select="postalCode"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            country: <span class="item">
                                    <xsl:value-of select="country"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            currentFlag: <span class="item">
                                    <xsl:value-of select="currentFlag"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                            addressStartDate: <span class="item">
                                    <xsl:value-of select="addressStartDate"/>
                                  </span>
                                  <br/>
                                </span>
                              </xsl:for-each>
                            </blockquote>
                            <hr/>
                          </xsl:for-each>
                          <hr/>
                        </b>
                      </font>
                    </p>
                  </td>
                  <td vAlign="top" align="left" width="35%" bgcolor="{$evenRowColor}" height="106">
                    <p>
                      <font size="-1" face="Arial, Helvetica, sans-serif">
                        <b>
                          <xsl:for-each select="observationContributor">
                            <xsl:sort select="surName"/>
                    observationContributor <xsl:number value="position()"/>
                            <br/>
                            <span class="category">
                                wholeName: <span class="item">
                                <xsl:value-of select="wholeName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                                salutation: <span class="item">
                                <xsl:value-of select="salutation"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                                givenName: <span class="item">
                                <xsl:value-of select="givenName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                                middleName: <span class="item">
                                <xsl:value-of select="middleName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                                surName: <span class="item">
                                <xsl:value-of select="surName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                                organizationName: <span class="item">
                                <xsl:value-of select="organizationName"/>
                              </span>
                              <br/>
                            </span>
                            <span class="category">
                                contactInstructions: <span class="item">
                                <xsl:value-of select="contactInstructions"/>
                              </span>
                              <br/>
                            </span>
                            <hr/>
                            <blockquote>
                              <xsl:for-each select="telephone">
                                <xsl:sort select="/"/>
                    telephone <xsl:number value="position()"/>
                                <br/>
                                <span class="category">
                                phoneNumber: <span class="item">
                                    <xsl:value-of select="phoneNumber"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                phoneType: <span class="item">
                                    <xsl:value-of select="phoneType"/>
                                  </span>
                                  <br/>
                                </span>
                              </xsl:for-each>
                            </blockquote>
                            <hr/>
                            <blockquote>
                              <xsl:for-each select="address">
                                <xsl:sort select="/"/>
                    address <xsl:number value="position()"/>
                                <br/>
                                <span class="category">
                                orgPosition: <span class="item">
                                    <xsl:value-of select="orgPosition"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                email: <span class="item">
                                    <xsl:value-of select="email"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                deliveryPoint: <span class="item">
                                    <xsl:value-of select="deliveryPoint"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                city: <span class="item">
                                    <xsl:value-of select="city"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                administrativeArea: <span class="item">
                                    <xsl:value-of select="administrativeArea"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                postalCode: <span class="item">
                                    <xsl:value-of select="postalCode"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                country: <span class="item">
                                    <xsl:value-of select="country"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                currentFlag: <span class="item">
                                    <xsl:value-of select="currentFlag"/>
                                  </span>
                                  <br/>
                                </span>
                                <span class="category">
                                addressStartDate: <span class="item">
                                    <xsl:value-of select="addressStartDate"/>
                                  </span>
                                  <br/>
                                </span>
                              </xsl:for-each>
                            </blockquote>
                            <hr/>
                          </xsl:for-each>
                          <hr/>
                        </b>
                      </font>
                    </p>
                  </td>
                </tr>
              </xsl:if>
            </xsl:for-each>
            <!-- EDIT						<hr/> -->
          </form>
        </table>
	<!-- VEGBANK FOOTER -->
	@vegbank_footer_html_tworow@
	<!-- END OF FOOTER -->

      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
