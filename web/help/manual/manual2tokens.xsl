<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" />
<xsl:template match="*" />
<xsl:template match="/manualDoc">
  <xsl:apply-templates />
</xsl:template>
<xsl:template match="manualItem">manualFrameFor-<xsl:value-of select="@name" />=@jspHelpPage@?<xsl:if test="string-length(@refURL)&gt;0">mainPage=<xsl:value-of select="@refURL" /><xsl:text disable-output-escaping="yes">&amp;</xsl:text></xsl:if>helpPage=@manual_link@<xsl:value-of select="@name" />.html
<xsl:apply-templates />
</xsl:template>
</xsl:stylesheet>
