<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:import href="model_pieces_toSQL.xsl" />
<xsl:param name="whichType">1</xsl:param><!-- 1 for postgresql, 2 for access -->
<xsl:param name="specialCase">0</xsl:param><!-- 0 for normal: tables and reltionships; 1 for just tables ,2 for just relationships, 3 for adding implementation fields only (useful in ERD), 4 to drop implementation fields -->
<xsl:output method="text"/>
<xsl:template match="/dataModel">
<xsl:if test="$whichType='2'">
----CREATE TABLE TO DESCRIBE THIS VEGBANK VERSION----
CREATE TABLE zvegbank_version
(
verID serial,
versionNumber varchar (100),
versionDescription text,
PRIMARY KEY (verID)
);
INSERT INTO zvegbank_version (versionNumber, versionDescription) SELECT  '<xsl:value-of select="version" />' , '<xsl:value-of select="versionDesc" />' ;

----CREATE CLOSED LIST TABLE-----------
CREATE TABLE fieldList
(
fieldList_ID serial,
tableName varchar (50),
fieldName varchar (50),
listValue varchar (255),
valueDesc text,
sortOrd float,
PRIMARY KEY (fieldList_ID)
);

</xsl:if>
   <!-- write create table script -->
   <xsl:if test="$specialCase='0' or $specialCase='1'">
---------------------CREATE TABLES ----------------------------
<xsl:for-each select="entity">
<xsl:call-template name="createtable" />
</xsl:for-each>
</xsl:if>


<xsl:if test="$specialCase='0' or $specialCase='2'">
--------------RELATIONSHIPS ----------------------------------------
<xsl:for-each select="entity">
<xsl:call-template name="addrels" />
</xsl:for-each>
</xsl:if>

<xsl:if test="$whichType='2'">
----------POPULATE  CLOSED LIST TABLE-----------------
<xsl:variable name="apos">'</xsl:variable>
<xsl:variable name="repl">`</xsl:variable>
<xsl:for-each select="entity/attribute[attListType!='no']/attList/attListItem">
<!--<xsl:variable name="currauxTbl">aux_<xsl:value-of select="../entityName" />_<xsl:value-of select="attName" /></xsl:variable>-->
<!--CREATE TABLE <xsl:value-of select="$currauxTbl" />
(
values<xsl:text> </xsl:text><xsl:value-of select="attType" /> NOT NULL,
valueDescription text,
sortOrd integer,
PRIMARY KEY (values)
);-->
INSERT INTO fieldList (tableName, fieldName, listValue, valueDesc, sortOrd) values ('<xsl:value-of select="../../../entityName" />', '<xsl:value-of select="../../attName" />',  '<xsl:value-of select="translate(attListValue,$apos,$repl)" />', <xsl:choose> <xsl:when test="string-length(attListValueDesc)&gt;0">'<xsl:value-of select="translate(attListValueDesc,$apos,$repl)" />'</xsl:when>  <xsl:otherwise>null</xsl:otherwise></xsl:choose>, <xsl:choose>  <xsl:when test="string-length(attListSortOrd)&gt;0"><xsl:value-of select="attListSortOrd" /></xsl:when>  <xsl:otherwise>null</xsl:otherwise></xsl:choose>) ;
</xsl:for-each>


<!--
<xsl:for-each select="entity/attribute/attList/attListItem">
INSERT INTO aux_<xsl:value-of select="../../../entityName" />_<xsl:value-of select="../../attName" /> (values,valueDescription,sortOrd) select '<xsl:value-of select="translate(attListValue,$apos,$repl)" />', <xsl:choose> <xsl:when test="string-length(attListValueDesc)&gt;0">'<xsl:value-of select="translate(attListValueDesc,$apos,$repl)" />'</xsl:when>  <xsl:otherwise>null</xsl:otherwise></xsl:choose>, <xsl:choose>  <xsl:when test="string-length(attListSortOrd)&gt;0"><xsl:value-of select="attListSortOrd" /></xsl:when>  <xsl:otherwise>null</xsl:otherwise></xsl:choose> ;</xsl:for-each> -->
</xsl:if>
<xsl:if test="$specialCase='3' or $specialCase='4'">
<xsl:for-each select="entity/attribute[attModel='implementation']">
  <xsl:choose>
    <xsl:when test="$specialCase='3'">
      <xsl:call-template name="createonefield" />
      <xsl:call-template name="addonerel" />
    </xsl:when>
    <xsl:when test="$specialCase='4'">
      <xsl:call-template name="droponefield" />
    </xsl:when>
  </xsl:choose>

</xsl:for-each>
</xsl:if>
</xsl:template>


</xsl:stylesheet>
