<?xml version='1.0' encoding='ISO-8859-1'?>

<!--
 '$RCSfile: compareXML.xsl,v $'
 Purpose: A Class that loads plot data to the vegbank database system
 Copyright: 2000 Regents of the University of California and the
            National Center for Ecological Analysis and Synthesis
 Release: @release@

   '$Author: farrell $'
   '$Date: 2003-01-16 00:14:49 $'
   '$Revision: 1.3 $'

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-->

<!--
  DESCRIPTION:
  Compares two xml files and generates a junit like test report indicated
  missing nodes, mismatched values etc.
  This is not a completely generic comparison as it assumes that position in the
  document order is meaningfull for nodes that are not unique in name on there
  level of the tree. I cannot see a better way around this, it could be done
  using a piece of information other than position like the value of a attribute
  or child text node value  to key between the input and output XMLs. 

  RUNNING:
  xslprocessor inputXML XSL PARAM output [outputXML]
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">

  <xsl:output method="xml"
    version="1.0"
    encoding="ISO-8859-1"/>

  <xsl:param name="output"/>
  <xsl:param name="testName"/>
  <xsl:variable name="doc2node" select="document($output)" />
  <xsl:variable name="in" select="$output" />

  <xsl:template match="/">
    <testsuite errors="0" failures="0" name="INTEGATION.{$testName}" tests="1" time="">
      <xsl:for-each select="*">
        <xsl:variable name="elementName" select="name()"/>
        <xsl:apply-templates select=".">
          <xsl:with-param name="doc2node" 
            select="$doc2node/*[name() = $elementName]" /> 
        </xsl:apply-templates>
      </xsl:for-each>
    </testsuite>
  </xsl:template>

  <xsl:template match="*">
    <xsl:param name="doc2node" />
    <xsl:variable name="elementName" select="name()"/>
    
    <testcase name="test-{name()}"> 
    
    <!-- do your element comparison here -->
      <xsl:choose>
        <xsl:when test="normalize-space(text()) = normalize-space($doc2node/text())">
          <!-- No error to report -->
        </xsl:when>
        <xsl:when test="not($doc2node/node())">
          <failure 
            message="This node does not exist in expected position in output"
            type="junit.framework.AssertionFailedError">
            Node does not exist in output xml
          </failure>
        </xsl:when>
        <xsl:otherwise>
          <failure 
            message="input:&lt;{normalize-space(text())}&gt; but output was:&lt;{normalize-space($doc2node/text())}&gt;" 
            type="junit.framework.AssertionFailedError">
            Mismatch here
          </failure>
        </xsl:otherwise>
      </xsl:choose>
    </testcase> 
    <xsl:for-each select="@*">
      <xsl:variable name="name" select="name()" />
      <xsl:apply-templates select=".">
        <xsl:with-param name="doc2node" select="$doc2node/@*[name() = $name]" />
      </xsl:apply-templates>
    </xsl:for-each>

    <xsl:for-each select="*">
      <xsl:variable name="elementName" select="name()"/>
      <xsl:variable name="index"
          select="count(preceding-sibling::node()[name()=$elementName]) + 1" />
      <xsl:apply-templates select=".">
        <xsl:with-param name="doc2node" 
            select="$doc2node/*[name() = $elementName][$index]" /> 
        <!--<xsl:with-param name="doc2node" select="$doc2node/*[position() = $index]" />-->
      </xsl:apply-templates>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:param name="doc2node" />
    <!-- do your attribute comparison here -->
    <xsl:value-of select="name()"/> 
    <xsl:choose>
      <xsl:when test="normalize-space(text()) = normalize-space($doc2node/text())">
      </xsl:when>
      <xsl:otherwise>
        <li>
          <!--  Attribute <b><xsl:value-of select="./name()"/> </b>
          in the element <b><xsl:value-of select="../name()"/></b>-->
          the input value &quot;<xsl:value-of select="text()"/>&quot; 
          is not equal to output value &quot;<xsl:value-of select="$doc2node/text()"/>&quot;
        </li>
      </xsl:otherwise>
    </xsl:choose>


    <!--
         <xsl:value-of select="name()"/> Vs. <xsl:value-of select="name($doc2node)"/> 
         <xsl:value-of select="."/> and <xsl:value-of select="$doc2node/."/> 
         -->
  </xsl:template>
  

</xsl:stylesheet>
