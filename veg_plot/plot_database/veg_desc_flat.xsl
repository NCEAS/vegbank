<?xml version="1.0"?> 
<xsl:stylesheet 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="vegetation_description/species">
	
	<xsl:value-of select="../plot_id"/>
	<xsl:text>|</xsl:text>
	<xsl:value-of select="orig_species"/>
	<xsl:text>|</xsl:text>
	<xsl:value-of select="stratum/stratum_type"/>
	<xsl:text>|</xsl:text>
	<xsl:value-of select="stratum/percent_cover"/>
	
	</xsl:template>
</xsl:stylesheet>
