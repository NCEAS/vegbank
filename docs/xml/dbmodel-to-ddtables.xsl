<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="csvtools.xsl" />
   <xsl:param name="LF1">|
|</xsl:param><!-- has line feed in string -->
   <xsl:param  name="LF" select="substring($LF1,2,1)" /><!-- get just LF -->

<xsl:output method="text" indent="no" encoding="UTF-8"/>
  <xsl:template match="/dataModel">
    --
    -- This is a generated SQL script for postgresql
    --
    
    -- first delete any info out of the tables
    DELETE FROM dba_tableDescription;
    DELETE FROM dba_fieldDescription;
    DELETE FROM dba_fieldList;
   
    <xsl:for-each select="entity[module='plant' or module='community' or module='plot']">
       SELECT '<xsl:value-of select="entityName" />';
        INSERT INTO dba_tableDescription (  tableName , tableLabel , tableNotes , tableDescription  ) values (
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="entityName" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="entityLabel" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="entitySummary" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="entityDescription" /></xsl:call-template>
        );
        <!-- while we're here, add info about fields: -->
        <xsl:for-each select="attribute">
             SELECT '<xsl:value-of select="../entityName" />.<xsl:value-of select="attName" />';
            INSERT INTO dba_fieldDescription ( tableName, fieldName , fieldLabel , fieldModel , fieldNulls , fieldType , fieldKey , fieldReferences , fieldList , fieldNotes , fieldDefinition )
               values (
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="../entityName" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attName" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attLabel" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attModel" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attNulls" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attType" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attKey" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attReferences" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attListType" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attNotes" /></xsl:call-template>,
        <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attDefinition" /></xsl:call-template>
               );
                
               <!-- ok, while we're HERE, lets add any list values -->
               
               <!-- store field and table name -->
               <xsl:variable name="thisTableName" select="../entityName" />
               <xsl:variable name="thisFieldName"  select="attName" />
               <xsl:for-each select="attList/attListItem">
                 <xsl:sort select="attListSortOrd" data-type="number" />
                      SELECT '<xsl:value-of select="$thisTableName" />.<xsl:value-of select="$thisFieldName" />  : '<xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attListValue" /></xsl:call-template>'';
                     INSERT INTO dba_fieldlist ( tableName , fieldName , listValue , listValueDescription , listValueSortOrder )
                       values (
                             <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="$thisTableName" /></xsl:call-template>,
                              <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="$thisFieldName" /></xsl:call-template>,
                              <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attListValue" /></xsl:call-template>,
                              <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attListValueDesc" /></xsl:call-template>,
                              <xsl:call-template name="field-to-csv"><xsl:with-param name="text" select="attListSortOrd" /></xsl:call-template>
                       );
               </xsl:for-each>
        </xsl:for-each>
    </xsl:for-each>
  </xsl:template>
   <!--
    Default match rule is shut up
 -->
 <xsl:template match="*"/>
  <xsl:template name="field-to-csv">
    <xsl:param name="text" />
    <xsl:choose>
      <xsl:when test="string-length($text)=0">null</xsl:when>
      <xsl:otherwise>
    <!-- first, replace ' with '' -->
      <xsl:variable name="rep1"><xsl:call-template name="replace-string"><xsl:with-param name="text" select="$text" /><xsl:with-param name="from"><xsl:text>'</xsl:text></xsl:with-param><xsl:with-param name="to"><xsl:text>''</xsl:text></xsl:with-param></xsl:call-template></xsl:variable>
<!-- replace LF too -->'<xsl:call-template name="replace-string"><xsl:with-param name="text" select="$rep1" /><xsl:with-param name="from" select="$LF" /><xsl:with-param name="to"><xsl:text> </xsl:text></xsl:with-param></xsl:call-template>'</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
