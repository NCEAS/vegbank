<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl takes the vegbank database model xml and transforms into xsl that transforms the data xml into vegbranch csv import files. Written by Michael Lee (mikelee@unc.edu) 23-APR-2004 -->
<!-- attempting to fix weirdities of xml: ie stratumType without method as parent, have to go look it up elsewhere, see http://tekka.nceas.ucsb.edu/~lee/xml/guide/all-conflicts.html -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <xsl:output method="xml" encoding="UTF-8"/>
  <xsl:param name="txtdelim">"</xsl:param>
  <xsl:param name="maxFields"><xsl:for-each select="/dataModel/entity[module='plot' or module='plant' or module='community']"><xsl:sort select="count(attribute[attModel='logical'])" order="descending" data-type="number" /><xsl:if test="position()=1" ><xsl:value-of select="number(count(attribute[attModel='logical'])+1)" /></xsl:if></xsl:for-each></xsl:param>
  <xsl:template name="DoApplyTempl">
     <xsl:element name="xsl:apply-templates" />
  </xsl:template>
  
  <xsl:template name="DoCSV"><xsl:param name="nodeName" /><xsl:param name="literalText" /><xsl:param name="nodeAlt" /><xsl:param name="nodeAlt2" />
    <xsl:element name="xsl:call-template">
      <xsl:attribute name="name">csvIt</xsl:attribute>
      <xsl:element name="xsl:with-param">
        <xsl:attribute name="name">text</xsl:attribute>
        <xsl:choose>
          <xsl:when test="string-length($literalText)&gt;0"><xsl:value-of select="$literalText" /></xsl:when>
          <xsl:when test="string-length($nodeAlt)&gt;0">
            <!-- two places to pull from or more may apply -->
            <xsl:element name="xsl:choose">
               <xsl:element name="xsl:when">
                 <xsl:attribute name="test">string-length(<xsl:value-of select="$nodeName" />)&gt;0</xsl:attribute><!-- have normal att -->
                   <xsl:element name="xsl:value-of">
                     <xsl:attribute name="select"><xsl:value-of select="$nodeName" /></xsl:attribute>
                   </xsl:element>
               </xsl:element><!-- when normal one is there -->
                   <!-- possible second element location to check: -->
<xsl:if test="string-length($nodeAlt2)&gt;0">
<!-- add another when statement -->
               <xsl:element name="xsl:when">
                 <xsl:attribute name="test">string-length(<xsl:value-of select="$nodeAlt2" />)&gt;0</xsl:attribute><!-- have normal att -->
                   <xsl:element name="xsl:value-of">
                     <xsl:attribute name="select"><xsl:value-of select="$nodeAlt2" /></xsl:attribute>
                   </xsl:element>
               </xsl:element><!-- /when -->
</xsl:if>
               <xsl:element name="xsl:otherwise"><!-- get alt --><xsl:element name="xsl:value-of">
                    <xsl:attribute name="select"><xsl:value-of select="$nodeAlt" /></xsl:attribute>
                  </xsl:element>
               </xsl:element><!-- /otherwise -->
            </xsl:element><!-- /choose -->
            <!-- targ:
            <xsl:choose>

        <xsl:when test="string-length(../observation.OBSERVATION_ID)&gt;0"><xsl:value-of select="../observation.OBSERVATION_ID" /></xsl:when>
        <xsl:otherwise><xsl:value-of select="../../../../observation.OBSERVATION_ID" /></xsl:otherwise>

      </xsl:choose>
            
            -->
            
          </xsl:when>
          <xsl:otherwise><!-- normal get value from doc -->
                  <xsl:attribute name="select">
                  <xsl:value-of select="$nodeName" /></xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="DoGetName"><xsl:call-template name="DoCSV"><xsl:with-param name="nodeName" >name()</xsl:with-param></xsl:call-template></xsl:template>

<xsl:template name="writeMaxFields"><xsl:param name="currCount" /><xsl:param name="writeWhat" />
  <xsl:if test="$currCount&lt;=$maxFields"><xsl:value-of select="$writeWhat" /><xsl:if test="$writeWhat='field'"><xsl:value-of select="$currCount" /></xsl:if><xsl:if test="$currCount&lt;$maxFields">,</xsl:if><xsl:call-template name="writeMaxFields"><xsl:with-param name="currCount" select="number($currCount+1)" /><xsl:with-param name="writeWhat" select="$writeWhat" /></xsl:call-template></xsl:if>
</xsl:template>

  <xsl:template name="writeSomeFields"><xsl:param name="currCount" /><xsl:param name="type"/>
    <xsl:if test="$currCount&lt;$maxFields">
    <xsl:choose>
      <xsl:when test="$type='1'">field<xsl:value-of select="$currCount" /></xsl:when>
      <xsl:otherwise>null</xsl:otherwise>
    </xsl:choose><xsl:if test="number($currCount+1)!=$maxFields">,</xsl:if><xsl:call-template name="writeSomeFields"><xsl:with-param name="currCount" select="number($currCount+1)" /><xsl:with-param name="type" select="$type" /></xsl:call-template></xsl:if>
    <xsl:if test="$currCount=$maxFields">null</xsl:if>
  </xsl:template>
  <xsl:template match="/">
<xsl:comment>

******** This stylesheet was generated automatically from dbmodel-to-vbrxsl.xsl ************
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
    <!-- get some xsl functions for the output xsl by importing from file -->
    <xsl:element name="xsl:import">
      <xsl:attribute name="href">csvtools.xsl</xsl:attribute>
    </xsl:element>

    <xsl:element name="xsl:output">
      <xsl:attribute name="method">text</xsl:attribute>
      <xsl:attribute name="encoding">UTF-16</xsl:attribute>
    </xsl:element><!-- /xsl:output -->
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">doc-VegBankVersion | doc-date | doc-author | doc-authorSoftware | doc-comments</xsl:attribute><!-- do nothing for these preset elements -->
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">VegBankPackage</xsl:attribute><!-- header line for whole file -->HD,tableName,<xsl:call-template name="writeMaxFields">
      <xsl:with-param name="currCount">1</xsl:with-param><xsl:with-param name="writeWhat">field</xsl:with-param>
      </xsl:call-template>
            <xsl:for-each select="/dataModel/entity[module='plot' or module='plant' or module='community']">
<!-- write header lines --><xsl:element name="xsl:value-of"><xsl:attribute name="select">$LF</xsl:attribute></xsl:element><xsl:value-of select="$txtdelim" />H<xsl:value-of select="$txtdelim" />,<xsl:call-template name="DoCSV"><xsl:with-param name="literalText" select="entityName" /></xsl:call-template>,<xsl:for-each select="attribute[attModel='logical']"><xsl:call-template name="DoCSV"><xsl:with-param name="literalText" select="attName" /></xsl:call-template>,</xsl:for-each>
<xsl:call-template name="writeMaxFields">
      <xsl:with-param name="currCount" select="number(count(attribute[attModel='logical'])+1)"/><xsl:with-param name="writeWhat">null</xsl:with-param>
      </xsl:call-template>

            </xsl:for-each> <!-- ent -->
      <xsl:call-template name="DoApplyTempl" />
    </xsl:element>


<!-- loop through tables, getting values to write for that table -->
     <xsl:for-each select="/dataModel/entity[module='plot' or module='plant' or module='community']">

<xsl:element name="xsl:template">
  <xsl:attribute name="match"><xsl:value-of select="entityName" /></xsl:attribute>
  <!-- label this row with element name -->
<xsl:element name="xsl:value-of"><xsl:attribute name="select">$LF</xsl:attribute></xsl:element><xsl:value-of select="$txtdelim" />d<xsl:value-of select="$txtdelim" />,<!-- data row --><xsl:call-template name="DoGetName" />,<xsl:for-each select="attribute[attModel='logical']">
    <!-- get att Value for this entity -->

    <xsl:call-template name="DoCSV">
    
<!-- special cases exist?? these are FKs not abiding by normal rules, having conflicts, need to look elsewhere for value -->
<xsl:with-param name="nodeAlt">
 <xsl:choose>
  <xsl:when test="(attName='OBSERVATION_ID') and (../entityName='stratum')">../../../../observation.OBSERVATION_ID</xsl:when>
  <xsl:when test="(attName='STRATUMMETHOD_ID') and (../entityName='stratumType')">../../../../../../observation.STRATUMMETHOD_ID/stratumMethod/stratumMethod.STRATUMMETHOD_ID</xsl:when>
  <xsl:when test="(attName='TAXONOBSERVATION_ID') and (../entityName='taxonInterpretation')">../../../../taxonObservation.TAXONOBSERVATION_ID</xsl:when>
</xsl:choose>
</xsl:with-param>
<xsl:with-param name="nodeAlt2">
 <xsl:choose>
  <xsl:when test="(attName='STRATUMMETHOD_ID') and (../entityName='stratumType')">../../../observation.STRATUMMETHOD_ID/stratumMethod/stratumMethod.STRATUMMETHOD_ID</xsl:when>
</xsl:choose>

</xsl:with-param>
    <xsl:with-param name="nodeName">
    <!-- 3 cases to consider: normal data (easy), normal FK (downstream), inverted FK (upstream) --><!-- can add parenthetical field name here (<xsl:value-of select="attName" />) -->
        <xsl:choose>
      <xsl:when test="attKey!='FK'"><xsl:value-of select="../entityName" />.<xsl:value-of select="attName"/></xsl:when>
      <xsl:otherwise><!-- noraml or inverted FK -->
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

  </xsl:with-param></xsl:call-template>,<!-- value-of -->
    
  </xsl:for-each> <!-- looping thru atts -->
  <xsl:call-template name="writeMaxFields">
      <xsl:with-param name="currCount" select="number(count(attribute[attModel='logical'])+1)"/><xsl:with-param name="writeWhat">null</xsl:with-param>
      </xsl:call-template>


  <!-- continue through elements -->
  <xsl:call-template name="DoApplyTempl" />
</xsl:element>
<xsl:for-each select="attribute[attModel='logical']"> <!-- empty for each att -->
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
