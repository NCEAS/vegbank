<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <!-- params to be passed into this stylesheet with ant -->
  <xsl:param name="infile"/>
  <xsl:param name="style"/>
  <xsl:param name="style_noframes"/>
  <xsl:output method="xml" omit-xml-declaration="no" encoding="UTF-8"/>
  <xsl:template match="/LoadModDoc">
    <project name="vegbranch - loading module doc" default="init" basedir=".">
      <target name="init">
        <xsl:for-each select="table">
          <style out="{TableName}~9.html" in="{$infile}" extension=".html" style="{$style}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="9"/>
          </style>
          <style out="{TableName}~1.html" in="{$infile}" extension=".html" style="{$style}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="1"/>
          </style>
          <style out="{TableName}~2.html" in="{$infile}" extension=".html" style="{$style}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="2"/>
          </style>
          <style out="{TableName}~3.html" in="{$infile}" extension=".html" style="{$style}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="3"/>
          </style>
          <style out="{TableName}~4.html" in="{$infile}" extension=".html" style="{$style}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="4"/>
          </style>
<!-- no frames version -->
<style out="{TableName}~9_noframes.html" in="{$infile}" extension=".html" style="{$style_noframes}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="9"/>
          </style>
          <style out="{TableName}~1_noframes.html" in="{$infile}" extension=".html" style="{$style_noframes}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="1"/>
          </style>
          <style out="{TableName}~2_noframes.html" in="{$infile}" extension=".html" style="{$style_noframes}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="2"/>
          </style>
          <style out="{TableName}~3_noframes.html" in="{$infile}" extension=".html" style="{$style_noframes}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="3"/>
          </style>
          <style out="{TableName}~4_noframes.html" in="{$infile}" extension=".html" style="{$style_noframes}" force="true">
            <param name="currtbl" expression="{TableName}"/>
            <param name="showFlds" expression="4"/>
          </style>          
        </xsl:for-each>
        <!-- perform for index pages, too -->
        <style out="loadtbl_menu.html" in="{$infile}" extension=".html" style="{$style}" force="true">
            <param name="currtbl" expression="index"/>
            <param name="showFlds" expression="3"/>
          </style>          
        <style out="loadtbl_menu_noframes.html" in="{$infile}" extension=".html" style="{$style_noframes}" force="true">
            <param name="currtbl" expression="index"/>
            <param name="showFlds" expression="3"/>
          </style>          

      </target>
    </project>
  </xsl:template>
</xsl:stylesheet>
