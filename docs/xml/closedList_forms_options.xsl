<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://nosuchaddress.com"><xsl:output method="html"/><xsl:template match="/dataModel">
##junk_token=<xsl:element name="html:rootCatchJunkAtts"><xsl:for-each select="entity/attribute[attListType!='no']">
VB_INSERT_CLOSEDLIST_<xsl:value-of select="translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>.<xsl:value-of select="translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />= <xsl:for-each select="attList/attListItem"><xsl:sort select="attListSortOrd" data-type="number"/><xsl:element name="option"><xsl:attribute name="value"><xsl:value-of select="attListValue" /></xsl:attribute><xsl:value-of select="attListValue" /></xsl:element></xsl:for-each>
VB_INSERT_STRUTS_CLOSEDLIST_<xsl:value-of select="translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>.<xsl:value-of select="translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />= <xsl:for-each select="attList/attListItem"><xsl:sort select="attListSortOrd" data-type="number"/><xsl:element name="html:option"><xsl:attribute name="value"><xsl:value-of select="attListValue" /></xsl:attribute><xsl:value-of select="attListValue" /></xsl:element>\
</xsl:for-each> 
# comment #
</xsl:for-each>
##junk_token2=</xsl:element>
</xsl:template>
</xsl:stylesheet>
