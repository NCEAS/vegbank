<?xml version="1.0" encoding="UTF-8"?>
<!-- purpose of this file is to copy xml elements to html directly, except for tableToStyle elements, which get grey/yellow pattern alternating rows and columns to increase legibility -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
<xsl:output method="html" omit-xml-declaration="yes" />
<xsl:template match="*">
  
   <xsl:element name="{name()}">
     <xsl:for-each select="attribute::*">
         <xsl:attribute name="{name()}"><xsl:value-of select="current()" /></xsl:attribute>
     </xsl:for-each>
    
  <xsl:apply-templates />
  </xsl:element> 

</xsl:template>
<xsl:template match="tableToStyle">
  <xsl:element name="table">
     <xsl:for-each select="attribute::*">
         <xsl:attribute name="{name()}"><xsl:value-of select="current()" /></xsl:attribute>
     </xsl:for-each>
  <xsl:for-each select="tr">
    <xsl:element name="{name()}">
         <xsl:for-each select="attribute::*">
         <xsl:attribute name="{name()}"><xsl:value-of select="current()" /></xsl:attribute>
     </xsl:for-each>
     <xsl:variable name="currRow" select="number((position() mod 2)+1)" />
     <xsl:for-each select="td|th">
        <xsl:variable name="currCol" select="number((position() mod 2)+1)" />
            <xsl:element name="{name()}">
            <xsl:attribute name="class"><xsl:value-of select="concat('row',$currRow,'col',$currCol)"/><xsl:if test="string-length(@class)&gt;0"><xsl:value-of select="concat(' ',@class)" /></xsl:if></xsl:attribute>
           <xsl:for-each select="attribute::*">
             <xsl:if test="name()!='class'"><xsl:attribute name="{name()}"><xsl:value-of select="current()" /></xsl:attribute></xsl:if>
           </xsl:for-each>

  <xsl:apply-templates /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
</xsl:element> <!-- td -->
     </xsl:for-each><!-- td | th -->
    </xsl:element>  
  </xsl:for-each><!-- tr-->
  </xsl:element> <!-- talbe -->
</xsl:template>


</xsl:stylesheet>
