<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl takes the vegbank database model xml and transforms into xsl that transforms the data xml into vegbranch csv import files. Written by Michael Lee (mikelee@unc.edu) 23-APR-2004 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" >
  <xsl:output method="xml" encoding="UTF-8"/>
  <xsl:template name="DoApplyTempl">
     <xsl:element name="xsl:apply-templates" />
  </xsl:template>
  <xsl:template name="DoGetName">
,<xsl:element name="xsl:value-of">
<xsl:attribute name="select">name()</xsl:attribute>
    </xsl:element>,</xsl:template>
  <xsl:template match="/">
<xsl:comment>

  *
  *     '$Author: mlee $'
  *     '$Date: 2004-04-23 11:39:26 $'
  *     '$Revision: 1.1 $'
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

</xsl:comment>
  <xsl:element name="xsl:stylesheet"><!-- = root -->
    <xsl:attribute name="version">1.0</xsl:attribute>
 <!--   <xsl:attribute name="xmlns:xsl">http://www.w3.org/1999/XSL/Transform</xsl:attribute> -->
    <xsl:element name="xsl:output">
      <xsl:attribute name="method">text</xsl:attribute>
      <xsl:attribute name="encoding">UTF-8</xsl:attribute>
    </xsl:element><!-- /xsl:output -->
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">doc-VegBankVersion | doc-date | doc-author | doc-authorSoftware | doc-comments</xsl:attribute><!-- do nothing for these preset elements -->
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">VegBankPackage</xsl:attribute>
      <xsl:call-template name="DoApplyTempl" />
    </xsl:element>
<!-- loop through tables, getting values to write for that table -->
            <xsl:for-each select="/dataModel/entity">
<xsl:element name="xsl:template">
  <xsl:attribute name="match"><xsl:value-of select="entityName" /></xsl:attribute>
  <!-- label this row with element name -->
  <xsl:call-template name="DoGetName" />
  <xsl:for-each select="attribute">
    <!-- get att Value for this entity -->
    <!-- 3 cases to consider: normal data (easy), normal FK (downstream), inverted FK (upstream) -->(<xsl:value-of select="attName" />)<xsl:element name="xsl:value-of"> <xsl:attribute name="select">
    <xsl:choose>
      <xsl:when test="attKey!='FK'"><xsl:value-of select="../entityName" />.<xsl:value-of select="attName"/></xsl:when>
      <xsl:otherwise>
         <!-- noraml or inverted -->
         <xsl:choose>
          <xsl:when test="attRelType/@type='inverted'">
             <!-- get value from upstream -->../<xsl:value-of select="attReferences" />
          </xsl:when> <!-- inverted -->
          <xsl:otherwise>
            <!-- normal FK, downstream -->
            <xsl:value-of select="../entityName" />.<xsl:value-of select="attName"/>/<xsl:value-of select="substring-before(attReferences,'.')"/>/<xsl:value-of select="attReferences" />
          </xsl:otherwise> <!-- not inverted -->
        </xsl:choose> <!-- inverted or not -->
      </xsl:otherwise> 
      
    </xsl:choose> <!-- not FK or FK -->
    </xsl:attribute></xsl:element>,<!-- value-of -->
    
  </xsl:for-each> <!-- looping thru atts -->
  
  <!-- continue through elements -->
  <xsl:call-template name="DoApplyTempl" />
</xsl:element>
<xsl:for-each select="attribute"> <!-- empty for each att -->
<xsl:element name="xsl:template">
  <xsl:attribute name="match"><xsl:value-of select="../entityName" />.<xsl:value-of select="attName" /></xsl:attribute>
  <xsl:if test="attRelType/@type='normal'"> <!-- have a normal FK: make sure we get elements inside it! -->
     <xsl:call-template name="DoApplyTempl" />
  </xsl:if>
</xsl:element>


</xsl:for-each>
           </xsl:for-each>



  </xsl:element><!-- stylesheet = root-->
  </xsl:template>
  </xsl:stylesheet>
