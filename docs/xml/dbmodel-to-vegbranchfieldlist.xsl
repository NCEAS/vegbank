<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:import href="csvtools.xsl" /> <xsl:output method="text" encoding="UTF-16" />
  <xsl:param name="doubleQuot">"</xsl:param>

  <xsl:template match="/dataModel">tableName,fieldName,listValue,valueDesc,sortOrd<xsl:value-of select="$LF" /><xsl:for-each select="entity/attribute/attList/attListItem">"<xsl:value-of select="../../../entityName"/>","<xsl:value-of select="../../attName" />",<xsl:call-template name="csvIt"><xsl:with-param name="text" select="attListValue" /></xsl:call-template>,<xsl:call-template name="csvIt"><xsl:with-param name="text" select="attListValueDesc" /></xsl:call-template>,<xsl:call-template name="csvIt"><xsl:with-param name="text" select="attListSortOrd" /></xsl:call-template><xsl:value-of select="$LF" /></xsl:for-each>
  </xsl:template>
  
</xsl:stylesheet>
