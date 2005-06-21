<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- this file reads in a common model NatureServe file and spits out all names and references along with random numbers as IDs.  Then this file is read to get a unique ID for each name and reference.

********
PLEASE NOTE: This does NOT create Unique ID's actually.  But the first value that matches when read by the other stylesheet will always have the same number 
**********
Should be run before running: commonModelXML_2vegbankXML.xsl
 -->
<xsl:output method="xml" />
  <xsl:template match="/">
    <xsl:element name="iddoc">
    <xsl:apply-templates select="*" />
    </xsl:element>
      </xsl:template>
  <xsl:template match="*">

       <xsl:apply-templates select="*" />
  </xsl:template>

  <xsl:template match="entity_name">
    <xsl:element name="NAME">
     <xsl:element name="id">
    <xsl:number count="*" level="any" />
      </xsl:element>
      <xsl:element name="value">
      <xsl:value-of select="entity_name.entity_name" />
      </xsl:element>
    </xsl:element>
   <xsl:apply-templates select="*" />
  </xsl:template>
  <xsl:template match="entity.ns_uid">
    <xsl:element name="NAME">
     <xsl:element name="id">
    <xsl:number count="*" level="any" />
      </xsl:element>
      <xsl:element name="value">
      <xsl:value-of select="current()" />
      </xsl:element>
    </xsl:element>
   <xsl:apply-templates select="*" />
  </xsl:template>  
  
  
   <xsl:template match="reference">
    <xsl:element name="REF">
     <xsl:element name="id">
    <xsl:number count="*" level="any" />
      </xsl:element>
      <xsl:element name="value">
      <xsl:value-of select="reference.full_citation" />
      </xsl:element>
    </xsl:element>
   <xsl:apply-templates select="*" />
  </xsl:template> 
  
</xsl:stylesheet>
