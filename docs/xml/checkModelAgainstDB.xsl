<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="alphaHigh">QWERTYUIOPASDFGHJKLZXCVBNM</xsl:param>
<xsl:param name="alphaLow">qwertyuiopasdfghjklzxcvbnm</xsl:param>
<!-- Purpose: style the main vegbank_model.xml file with this xsl.  The output should be compared to the following SQL output:

\a 
\t

select (select relname from pg_statio_all_tables where attrelid=relid) as mtl_tableName, attname, (select typname from pg_type where pg_type.oid=atttypid) || CASE WHEN atttypmod>4 THEN ' (' || atttypmod-4 || ') ' ELSE ' ' END as mtl_dataType, CASE WHEN attnotnull THEN 'no' ELSE 'yes' END as mtl_nulls, mtl_FTbl || '.' || mtl_fFldNm as MTL_references, 'dil' as unknownModel from pg_attribute LEFT JOIN (select conname, (select relname from pg_statio_all_tables where pg_constraint.conrelid=pg_statio_all_tables.relid) as mtl_tblNm, (select relname from pg_statio_all_tables where pg_constraint.confrelid=pg_statio_all_tables.relid) as mtl_FTbl, (select attname from pg_attribute where attrelid=conrelid and attnum =conkey[1]) as mtl_fldNm, (select attname from pg_attribute where attrelid=confrelid and attnum =confkey[1]) as mtl_fFldNm from pg_constraint where conrelid>0 and confrelid>0) as mtl_cons ON (select relname from pg_statio_all_tables where attrelid=relid)=mtl_tblNm and pg_attribute.attname=mtl_fldNm where attstattarget=-1 and attrelid in (select relid from pg_statio_all_tables where schemaname='public') ORDER BY (select relname from pg_statio_all_tables where attrelid=relid), replace(attname,'_','a')

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
  <xsl:otherwise><xsl:value-of select="attType" /></xsl:otherwise>
</xsl:choose> |<xsl:choose>
  <xsl:when test="attKey='PK'">no</xsl:when><xsl:otherwise><xsl:value-of select="attNulls" /></xsl:otherwise>
</xsl:choose>|<xsl:if test="attReferences!='n/a'"><xsl:value-of select="attReferences" /></xsl:if>|<xsl:choose>
    <xsl:when test="attModel='implementation'">dIl
</xsl:when>
    <xsl:when test="attModel='denorm'">Dil
</xsl:when>
    <xsl:when test="attModel='logical'">diL
</xsl:when>
  </xsl:choose>
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
  