<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="text"/>
<xsl:template match="/vegPlotPackage">

<xsl:text>----------SPECIES DATA ----------  &#xA; </xsl:text> 
<xsl:text>PLOT ID, PLANT NAME</xsl:text> 
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:for-each select="project">
		<xsl:for-each select="plot/observation/taxonObservation/stratumComposition">
	 		<xsl:value-of select="../../../../plot/plotId"/> <xsl:text>,&#x20;</xsl:text>
	 		<xsl:value-of select="../authorNameId"/> <xsl:text> &#x20;</xsl:text> 
			<xsl:value-of select="stratumName"/> <xsl:text> &#x20;</xsl:text>
			<xsl:value-of select="taxonStratumCover"/>
			<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
		</xsl:for-each>
</xsl:for-each>
			<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	</xsl:template>
</xsl:stylesheet>

