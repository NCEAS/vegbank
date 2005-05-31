<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="manualDoc">
    <xsl:apply-templates />
  </xsl:template>
  <xsl:template match="manualItem">
    <xsl:value-of select="@refURL" />-:topage:-<xsl:value-of select="@name" /><br/>
    <xsl:apply-templates />
  </xsl:template>
  <xsl:template match="manualHTML" />
</xsl:stylesheet>
