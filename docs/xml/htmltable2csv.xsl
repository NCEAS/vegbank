<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"><xsl:import href="csvtools.xsl" />
 <xsl:output method="text" encoding="ISO-8859-1" />
 <xsl:template match="/table"><xsl:apply-templates /></xsl:template>
 <xsl:template match="tr"><xsl:apply-templates /><xsl:value-of select="$LF" /></xsl:template> 
 
 <xsl:template match="td|th">"<xsl:call-template name="replace-string"><xsl:with-param name="text"><xsl:value-of select="normalize-space(node())"  /></xsl:with-param><xsl:with-param name="from">"</xsl:with-param><xsl:with-param name="to">""</xsl:with-param></xsl:call-template><!--<xsl:value-of select="node()" />-->"<xsl:if test="position()!=last()">,</xsl:if></xsl:template>
</xsl:stylesheet>
