<?xml version="1.0" encoding="UTF-8"?>
<!-- takes a db_model_vegbank.xml file and creates a new build file that distributes the datadictionary -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <!-- params to be passed into this stylesheet with ant -->
  <xsl:param name="infile" />
  <xsl:param name="style" />
    <xsl:param name="styleField" />
  <xsl:param name="htmlPrefix" />
  
  <xsl:output method="xml" omit-xml-declaration="no" encoding="UTF-8"/>
  <xsl:template match="/dataModel">
    <project name="vegbank_web" default="init" basedir=".">
      <target name="init">
        <xsl:for-each select="entity">
          <style 
             out="{$htmlPrefix}~table~{translate(entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html" 
             in="{$infile}" extension=".html" style="{$style}" force="true">
            <param name="CurrentTable" expression="{entityName}"/>
            <param name="CurrentField" expression=""/>
            <param name="htmlPrefix" expression="{$htmlPrefix}"/>
          </style>
          <xsl:for-each select="attribute">
           <!-- <xsl:if test="attModel='logical'"> -->
              <style 
                out="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html" 
                in="{$infile}" extension=".html" style="{$style}" force="true">
                <param name="CurrentTable" expression="{../entityName}"/>
                <param name="CurrentField" expression="{attName}"/>
                <param name="htmlPrefix" expression="{$htmlPrefix}"/>
              </style>
              <!-- could insert a second style trans for full field html -->
              <style 
                out="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html" 
                in="{$infile}" extension=".html" style="{$styleField}" force="true">
                <param name="CurrentTable" expression="{../entityName}"/>
                <param name="CurrentField" expression="{attName}"/>
                <param name="htmlPrefix" expression="{$htmlPrefix}"/>                
              </style>              
            <!-- </xsl:if> -->
          </xsl:for-each>
        </xsl:for-each>
      </target>
    </project>
  </xsl:template>
</xsl:stylesheet>
