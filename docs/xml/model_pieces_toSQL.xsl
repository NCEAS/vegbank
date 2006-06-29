<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

<xsl:template name="createfield">
<xsl:for-each select="attribute">
<xsl:call-template name="createonefield" /></xsl:for-each>
</xsl:template>

<xsl:template name="createonefield">
ALTER TABLE <xsl:value-of select="../entityName" /> ADD COLUMN <xsl:value-of select="attName" /> <xsl:text> </xsl:text><xsl:call-template name="handleType" /> <xsl:if test="string-length(attDefaultValue)&gt;0"> DEFAULT <xsl:value-of select="attDefaultValue" /></xsl:if>;
</xsl:template>



<xsl:template name="alterfield">
<xsl:for-each select="attribute"><!-- THIS DOES NOT CHANGE THE DATA TYPE FOR YOU! IT will only update required/not required and default values -->
ALTER TABLE <xsl:value-of select="../entityName" /> ALTER COLUMN <xsl:value-of select="attName" /> <xsl:text> </xsl:text>  <xsl:choose>
  <xsl:when test="attNulls='no'"> SET NOT NULL</xsl:when>
  <xsl:otherwise> DROP NOT NULL</xsl:otherwise>
</xsl:choose> ;
ALTER TABLE <xsl:value-of select="../entityName" /> ALTER COLUMN <xsl:value-of select="attName" /> <xsl:text> </xsl:text>  <xsl:choose>
  <xsl:when test="string-length(attDefaultValue&gt;0)"> SET DEFAULT <xsl:value-of select="attDefaultValue" /></xsl:when>
  <xsl:otherwise> DROP DEFAULT</xsl:otherwise>
</xsl:choose> ;



</xsl:for-each>

</xsl:template>

<xsl:template name="dropfield">
<xsl:for-each select="attribute">
<xsl:call-template name="droponefield" />
</xsl:for-each>
</xsl:template>

<xsl:template name="droponefield">

ALTER TABLE <xsl:value-of select="../entityName" /> DROP COLUMN <xsl:value-of select="attName" />  ;
</xsl:template>

<xsl:template name="droptable">
DROP TABLE <xsl:value-of select="entityName" /> ;
</xsl:template>

<xsl:template name="removeRels">
---create tbl temp----
CREATE TABLE temp_<xsl:value-of select="entityName" /> AS SELECT * FROM <xsl:value-of select="entityName" />;
DROP TABLE <xsl:value-of select="entityName" />;
 CREATE TABLE <xsl:value-of select="entityName" />  AS SELECT * FROM temp_<xsl:value-of select="entityName" />;
 DROP TABLE temp_<xsl:value-of select="entityName" />;
</xsl:template>

<xsl:template name="createtable">
   <!-- write create table script -->
CREATE TABLE <xsl:value-of select="entityName" />
(
<xsl:for-each select="attribute">
<xsl:value-of select="attName" /><xsl:text> </xsl:text> <xsl:call-template name="handleType" /> <xsl:if test="attNulls='no'"> NOT NULL</xsl:if> <xsl:if test="string-length(attDefaultValue)&gt;0"> DEFAULT <xsl:value-of select="attDefaultValue" /></xsl:if> ,
</xsl:for-each>
PRIMARY KEY ( <xsl:value-of  select="attribute[attKey='PK']/attName" /> )
);
</xsl:template>
<xsl:template name="addrels">
<!-- relationships -->
<!--  CREATE RELATIONSHIPS -->
<xsl:for-each select="attribute[attReferences!='n/a']">
<xsl:call-template name="addonerel" />
</xsl:for-each>
</xsl:template>

<xsl:template name="addonerel">
<!-- relationships -->
<!--  CREATE RELATIONSHIPS -->
<xsl:if test="attReferences!='n/a'">
ALTER TABLE<xsl:text> </xsl:text><xsl:value-of select="../entityName" />   ADD CONSTRAINT R<xsl:value-of select="position()" /><xsl:value-of select="../entityName" />_<xsl:value-of select="attName" /><xsl:text> </xsl:text>FOREIGN KEY (<xsl:value-of select="attName" />)    REFERENCES <xsl:value-of select="substring-before(attReferences,'.')" /> (<xsl:value-of select="substring-after(attReferences,'.')" /> );
</xsl:if>
</xsl:template>

<xsl:template name="makeClosedListTbl">
<xsl:for-each select="attribute[attListType!='no']">
<xsl:variable name="currauxTbl">aux_<xsl:value-of select="../entityName" />_<xsl:value-of select="attName" /></xsl:variable>
-------- MAKE CLOSED LIST TABLE--------------
CREATE TABLE <xsl:value-of select="$currauxTbl" />
(
values<xsl:text> </xsl:text><xsl:call-template name="handleType" /> NOT NULL,
valueDescription text,
sortOrd integer,
PRIMARY KEY (values)
);
<!-- <xsl:if test="attListType='closed'">
- - - *closed* list- - -
ALTER TABLE<xsl:text> </xsl:text><xsl:value-of select="../entityName" />
  ADD CONSTRAINT RelNum_aux<xsl:value-of select="position()"></xsl:value-of><xsl:text> </xsl:text>FOREIGN KEY (<xsl:value-of select="attName" />)
  REFERENCES <xsl:value-of select="$currauxTbl" /> ( values );
</xsl:if> -->


------------POPULATE CLOSED LISTS------------------
<xsl:variable name="apos">'</xsl:variable>
<xsl:variable name="repl">`</xsl:variable>
<xsl:for-each select="attList/attListItem">
INSERT INTO <xsl:value-of select="$currauxTbl" /> (values,valueDescription,sortOrd) select '<xsl:value-of select="translate(attListValue,$apos,$repl)" />', <xsl:choose> <xsl:when test="string-length(attListValueDesc)&gt;0">'<xsl:value-of select="translate(attListValueDesc,$apos,$repl)" />'</xsl:when>  <xsl:otherwise>null</xsl:otherwise></xsl:choose>, <xsl:choose>  <xsl:when test="string-length(attListSortOrd)&gt;0"><xsl:value-of select="attListSortOrd" /></xsl:when>  <xsl:otherwise>null</xsl:otherwise></xsl:choose> ;
</xsl:for-each>
</xsl:for-each>

</xsl:template>
<!-- handle data type -->
  <xsl:template name="handleType">
    <xsl:choose>
      <xsl:when test="attType='Date'"> timestamp with time zone </xsl:when>
      <xsl:otherwise><xsl:value-of select="attType" /></xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
