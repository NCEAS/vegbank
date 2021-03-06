<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
<!-- this file styles exported xml from excel to normal html table -->
<xsl:output method="html" />
  <xsl:template match="/">
    <TagToGetNameSpace>
    <!-- comment for file, so people know where it came from -->
    <xsl:comment>This file is generated by excelXml2html.xsl by saving a downloaded .csv as .xml from Excel.</xsl:comment>
    <table class="thinlines" border="1">
    <xsl:apply-templates select="*" />
    </table >
    </TagToGetNameSpace>
  </xsl:template>
  <xsl:template name="row">
    
    <tr>
        <xsl:for-each select="*"><!-- Cell -->
          <xsl:if test="string-length(@ss:Index)&gt;0">
          <!-- get the acutal position of this cell, which is not position, but position + the number of skipped cells -->
          <xsl:variable name="thisPos" select="position()" />
          <xsl:variable name="adjustCells">0<xsl:for-each select="../*[position()&lt;$thisPos]"><xsl:if test="string-length(@ss:Index)=0">+1</xsl:if><xsl:if test="string-length(@ss:Index)&gt;0">*<xsl:value-of select="@ss:Index" /></xsl:if></xsl:for-each></xsl:variable>
          <xsl:variable name="reallyAdjust"><xsl:call-template name="getReallyAdjust"><xsl:with-param name="rawInfo" select="$adjustCells" /></xsl:call-template></xsl:variable>
           <!--<xsl:value-of select="$reallyAdjust"></xsl:value-of>-->
              <!--<xsl:comment><xsl:value-of select="position()" /> + <xsl:value-of select="$reallyAdjust" /> and index: <xsl:value-of select="@ss:Index" /></xsl:comment>-->
              <xsl:call-template name="addNullCells">
             <xsl:with-param name="currentPos" select="$reallyAdjust" />
              <xsl:with-param name="currentIndex" select="@ss:Index" /> 
           </xsl:call-template>
         </xsl:if>
          <xsl:call-template name="cell">
            <xsl:with-param name="data" select="node()" />
          </xsl:call-template>
          
        </xsl:for-each>
    </tr>

  </xsl:template>
  <xsl:template name="cell">
    <xsl:param name="data" />
    <td><xsl:value-of select="$data" /></td>
  </xsl:template>

  <xsl:template match="*">
 <xsl:choose>
  <xsl:when test="name()='Row'">
    <xsl:call-template name="row" />
  </xsl:when>
<xsl:when test="name()='Data'">

    <xsl:call-template name="cell">
      <xsl:with-param name="data" select="node()" />
    </xsl:call-template>
  </xsl:when>
  <xsl:otherwise>
         <xsl:apply-templates select="*" />
  </xsl:otherwise>
</xsl:choose>

  

  </xsl:template>
<xsl:template name="addNullCells">
  <xsl:param name="currentPos" />
  <xsl:param name="currentIndex" />
  <!-- <xsl:comment>calling addNullCells: <xsl:value-of select="$currentPos" /> index: <xsl:value-of select="$currentIndex" /></xsl:comment> -->
  <xsl:if test="number(-1+$currentIndex)&gt;$currentPos">
    <td>null</td>
    <xsl:call-template name="addNullCells">
      <xsl:with-param name="currentPos" select="number(1+$currentPos)" />
      <xsl:with-param name="currentIndex" select="$currentIndex" />
    </xsl:call-template>
  </xsl:if>
</xsl:template>
  <xsl:template name="getReallyAdjust">
    <xsl:param name="rawInfo" />
  <!-- pass this function something like this: 0+1+1*5*7+1*10+1+1+1*15*17+1*22+1*27+1*35+1
   and it will return the sum from the last * on -->
   <!--<xsl:comment>Passed: <xsl:value-of select="$rawInfo" /></xsl:comment> -->
   <xsl:choose>
    <xsl:when test="contains($rawInfo,'*')"><!-- strip --><xsl:call-template name="getReallyAdjust"><xsl:with-param name="rawInfo" select="substring-after($rawInfo,'*')" /></xsl:call-template></xsl:when>
<xsl:otherwise>
  <!-- see if + in string, if so take before it, if not take # -->
  <xsl:choose>
    <xsl:when test="contains($rawInfo,'+')">
      <!-- here, get first number and add half the string length of rest -->
      <xsl:value-of select="number(substring-before($rawInfo,'+')+0.5*( (string-length($rawInfo) - (string-length(substring-before($rawInfo,'+')))) ))" />
    </xsl:when>
    <xsl:otherwise>
      <xsl:choose>
        <xsl:when test="string-length($rawInfo)&gt;0"><xsl:value-of select="$rawInfo" /></xsl:when>
        <xsl:otherwise>0</xsl:otherwise>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:otherwise>
  </xsl:choose>

  </xsl:template>

</xsl:stylesheet>
