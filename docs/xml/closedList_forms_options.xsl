<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output method="html"/><xsl:template match="/dataModel"><xsl:for-each select="entity/attribute[attListType!='no']">
VB_INSERT_CLOSEDLIST_<xsl:value-of select="translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>.<xsl:value-of select="translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />= <xsl:for-each select="attList/attListItem"><xsl:sort select="attListSortOrd" data-type="number"/><xsl:element name="option"><xsl:value-of select="attListValue" /></xsl:element></xsl:for-each></xsl:for-each>
</xsl:template>
</xsl:stylesheet>
