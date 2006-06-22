<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
<!-- compare output of this stylesheet from vegbank_model.xml doc with query Z_fieldDefn_maintainWithXML in VegBranch to assure that contents of the VegBranch data dictionary tables match vegbank's exactly. @BR@ should be replaced with null first!-->
<xsl:output method="text" />
<xsl:param name="apos">'</xsl:param>
<xsl:param name="replc">`</xsl:param>
<xsl:template match="dataModel">TableName|FieldName|typeData|Nulls|key|References|relType|relName|FieldNotes|FieldDefinition|model|ClosedList
<xsl:for-each select="entity[module!='user']">
<xsl:sort select="entityName" />
<xsl:for-each select="attribute">

<xsl:value-of select="../entityName" />|<xsl:value-of select="attName" />|<xsl:value-of select="attType" />|<xsl:choose>
  <xsl:when test="attKey='PK'">no</xsl:when><xsl:otherwise><xsl:value-of select="attNulls" /></xsl:otherwise>
</xsl:choose>|<xsl:value-of select="attKey" />|<xsl:value-of select="attReferences" />|<xsl:value-of select="attRelType/@type"/>|<xsl:value-of select="attRelType/@name"/>|<xsl:value-of select="attNotes" />|<xsl:value-of select="attDefinition" />|<xsl:value-of select="attModel" />|<xsl:value-of select="attListType" /> @BR@
</xsl:for-each></xsl:for-each>
</xsl:template>

<xsl:template match="*" />

<xsl:template name="SQLVal">
<xsl:param name="currFld"/>
<xsl:choose>
  <xsl:when test="string-length($currFld)&gt;0">'<xsl:value-of select="translate($currFld,$apos,$replc)"/>'</xsl:when>
  <xsl:otherwise>null</xsl:otherwise>
</xsl:choose>
</xsl:template>

</xsl:stylesheet>
