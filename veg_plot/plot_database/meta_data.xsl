<?xml version="1.0"?> 
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/XSL/Transform/1.0">

  <xsl:template match="/veg_plot">
	<xsl:value-of select="PI"/>
	<xsl:value-of select="contact"/>
	<xsl:value-of select="author"/>


  </xsl:template>
</xsl:stylesheet>
