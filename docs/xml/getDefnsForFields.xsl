<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <xsl:output method="xml" />
  <xsl:template match="dataModel">
    <xsl:for-each select="entity">
        <xsl:for-each select="attribute">
getDDDefnID_<xsl:value-of select="attID" />=<xsl:value-of select="attDefinition"/> @subst_lt@a target="_top" href="/ddfull/<xsl:value-of select="translate(concat(../entityName,'/'
,attName),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />"@subst_gt@more @subst_lt@/a@subst_gt@ <br/><img src="@images_link@transparent.gif" height="1" width="140"/><!-- end of defn -->
       </xsl:for-each>
    </xsl:for-each>
      </xsl:template>
</xsl:stylesheet>
