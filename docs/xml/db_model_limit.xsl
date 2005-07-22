<?xml version="1.0"?>
<!-- this xsl file is passed the param whichModel, which is either logical or implementation - if both are wanted, skip this xsl step -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:param name="whichModel" />
<xsl:output method="xml" version="1.0" encoding="utf-8" indent="yes" />

<xsl:template match="@* | node()">
<xsl:copy><xsl:apply-templates select="@* | node()" /></xsl:copy>
</xsl:template>

<xsl:template match="dataModel/entity/attribute">
  <xsl:if test="attModel=$whichModel or attModel='logical'"><!-- always do logical fields -->
     <xsl:copy><xsl:apply-templates select="@* | node()" /></xsl:copy>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>