<?xml version="1.0"?> 
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/XSL/Transform/1.0">

	<xsl:template match="/vegetation_description">
		<xsl:value-of select="plot_id"/>
		<xsl:text>	</xsl:text>
		<xsl:value-of select="species/orig_species"/>	
  		<xsl:text>End if first formatting</xsl:text>
	</xsl:template>


</xsl:stylesheet>
