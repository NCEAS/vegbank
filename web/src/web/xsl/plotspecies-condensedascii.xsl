<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd
  *   Authors: John Harris
  *   Copyright: 2000 Regents of the University of California and the 
  *               National Center for Ecological Analysis and Synthesis
  *  	For Details: http://www.nceas.ucsb.edu/
  *   Created: 2002 May
	*  '$Author: farrell $'
	*  '$Date: 2003-02-14 20:51:34 $'
	*  '$Revision: 1.1 $'		 
	-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="text"/>
<xsl:template match="/vegPlotPackage">

<xsl:text>----------SPECIES DATA ----------  &#xA; </xsl:text> 
<xsl:text>PLOT ID,PLANT CODE,COVER</xsl:text> 
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:for-each select="project">
		<xsl:for-each select="plot/observation/taxonObservation">
	 		<xsl:value-of select="../../../plot/plotId"/> <xsl:text>,</xsl:text>
	 		<xsl:value-of select="authorCodeId"/> <xsl:text>,</xsl:text> 
	 		<xsl:value-of select="taxonCover"/> <xsl:text>&#x20;</xsl:text> 
			<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
		</xsl:for-each>
</xsl:for-each>
			<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	</xsl:template>
</xsl:stylesheet>

