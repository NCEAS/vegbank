<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="xml"/>
<xsl:template match="/dbQuery">

<xsl:for-each select="query">

<!--Information about the toplevel project information -->
<xsl:text>queryElement|</xsl:text> <xsl:value-of select="queryElement"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>elementString|</xsl:text> <xsl:value-of select="elementString"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

</xsl:for-each>


		
	</xsl:template>
</xsl:stylesheet>

