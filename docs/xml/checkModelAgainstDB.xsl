<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="alphaHigh">QWERTYUIOPASDFGHJKLZXCVBNM</xsl:param>
<xsl:param name="alphaLow">qwertyuiopasdfghjklzxcvbnm</xsl:param>
<!-- Purpose: style the main vegbank_model.xml file with this xsl.  The output should be compared to the following SQL output:

\a 
\t

see vegbank/src/sql/verifyActualModel.sql for SQL to run on database

then diff case INSENsitive and ignoring spaces -->

<xsl:output method="text" />
<xsl:template match="/ | dataModel">
  <xsl:for-each select="dataModel/entity/attribute" >
    <xsl:sort select="../entityName" />
      <!-- replace _ with a for sorting purposes. -->
      <xsl:sort select="translate(attName,concat($alphaHigh,'_'),concat($alphaLow,'a'))" lang="en-us" case-order="upper-first" />

<xsl:value-of select="../entityName" />|<xsl:value-of select="attName" />|<xsl:choose>
  <xsl:when test="attType='Integer' or attType='serial'">int4</xsl:when>
  <xsl:when test="attType='Float'">float8</xsl:when>
  <xsl:when test="attType='Boolean'">bool</xsl:when>
  <xsl:when test="attType='Date'">timestamptz</xsl:when>
  <xsl:otherwise><xsl:value-of select="attType" /></xsl:otherwise>
</xsl:choose> |<xsl:choose>
  <xsl:when test="attKey='PK'">no nulls</xsl:when><xsl:otherwise><xsl:value-of select="attNulls" /> nulls</xsl:otherwise>
</xsl:choose>|<xsl:if test="attReferences!='n/a'"><xsl:value-of select="attReferences" /></xsl:if>|<xsl:choose>
<xsl:when test="attModel='implementation'">dIl</xsl:when>
    <xsl:when test="attModel='denorm'">Dil</xsl:when>
    <xsl:when test="attModel='logical'">diL</xsl:when>
  </xsl:choose>|IF I'M LOWERCASE, I'M FROM DATABASE, ELSE FROM MODEL XML
</xsl:for-each>
  <xsl:for-each select="dataModel/view"><xsl:sort select="viewName" />view|<xsl:value-of select="viewName" />|IF I'M LOWERCASE, I'M FROM DATABASE, ELSE FROM MODEL XML
</xsl:for-each>
  <xsl:for-each select="dataModel/sequence"><xsl:sort select="sequenceName" />sequence|<xsl:value-of select="sequenceName" />||IF I'M LOWERCASE, I'M FROM DATABASE, ELSE FROM MODEL XML
</xsl:for-each>
  <xsl:for-each select="dataModel/entity/attribute[attKey='PK'][attType='serial']">
    <xsl:sort select="concat(../entityName,'_',attName,'_seq')" />sequence|<xsl:value-of select="concat(../entityName,'_',attName,'_seq')" />|1|IF I'M LOWERCASE, I'M FROM DATABASE, ELSE FROM MODEL XML
</xsl:for-each>
</xsl:template>

<xsl:template name="writeModel" >

</xsl:template>

</xsl:stylesheet>
<!-- Boolean
Float
Integer
serial

-->
<!-- target : 
bool     
float8   
int4     
  
-->  
  
