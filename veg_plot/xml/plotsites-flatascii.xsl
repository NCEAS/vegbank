<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd
 	*   Authors: John Harris
  *   Copyright: 2000 Regents of the University of California and the 
  *               National Center for Ecological Analysis and Synthesis
  *  	For Details: http://www.nceas.ucsb.edu/
  *   Created: 2002 May
	*  '$Author: harris $'
	*  '$Date: 2002-07-16 22:28:57 $'
	*  '$Revision: 1.2 $'		 
	-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="text"/>
<xsl:template match="/vegPlotPackage">

<xsl:text>---------- SITE DATA ---------- &#xA; </xsl:text> 
<xsl:text>PLOT ID, ACCESSION_NUMBER, VEGCOMMUNITY, COLLECTION DATE, LATITUDE, LONGITUDE</xsl:text> 
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:for-each select="project">
		<xsl:for-each select="plot/observation">
	 		<xsl:value-of select="../../plot/plotId"/> <xsl:text>,&#x20;</xsl:text>
	 		<xsl:value-of select="../../plot/plotAccessionNumber"/> <xsl:text>,&#x20;</xsl:text>
	 		<xsl:value-of select="../../plot/observation/communityClassification/className"/> <xsl:text>,&#x20;</xsl:text>
	 		<xsl:value-of select="obsStartDate"/> <xsl:text>,&#x20;</xsl:text>
	 		<xsl:value-of select="../../plot/latitude"/> <xsl:text>,&#x20;</xsl:text>
	 		<xsl:value-of select="../../plot/longitude"/> <xsl:text> &#x20;</xsl:text>
			<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
		</xsl:for-each>
</xsl:for-each>
			<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	</xsl:template>
</xsl:stylesheet>

