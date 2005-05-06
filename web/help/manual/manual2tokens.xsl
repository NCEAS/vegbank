<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" />
<xsl:template match="*" />
<xsl:template match="/manualDoc">
  <xsl:apply-templates />
</xsl:template>
<xsl:template match="manualItem">
<xsl:variable name="currURLwithoutDotHTML">@jspHelpPage@?<xsl:if test="string-length(@refURL)&gt;0">mainPage=<xsl:value-of select="@refURL" /><xsl:text disable-output-escaping="yes">&amp;</xsl:text></xsl:if>helpPage=@manual_link@<xsl:value-of select="@name" /></xsl:variable>
manualFrameFor-<xsl:value-of select="@name" />=<xsl:value-of select="$currURLwithoutDotHTML" />.html
<xsl:apply-templates />
</xsl:template>
</xsl:stylesheet>
