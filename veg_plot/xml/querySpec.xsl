<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="xml"/>
<xsl:template match="/dbQuery">

<xsl:for-each select="query">

<!--Query element(S) -->
<xsl:text>queryElement|</xsl:text> <xsl:value-of select="queryElement"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>elementString|</xsl:text> <xsl:value-of select="elementString"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>


</xsl:for-each>

<!--desired requested data type-->
<xsl:text>requestDataType|</xsl:text> <xsl:value-of select="requestDataType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>


<!--desired output type-->
<xsl:text>resultType|</xsl:text> <xsl:value-of select="resultType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!--desired path and filename-->
<xsl:text>outFile|</xsl:text> <xsl:value-of select="outFile"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

		
	</xsl:template>
</xsl:stylesheet>

