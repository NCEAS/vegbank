<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl takes the vegbank database model xml and transforms into xsd that can be used to validate xml that is compliant with our model.  Written by Michael Lee (mikelee@unc.edu) 05-JUN-2003 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" >
  <xsl:output method="xml"/>
  <xsl:param name="validLevel">1</xsl:param>
  <xsl:template match="/">
<xsl:comment>
 this document validates VegBank data xml documents, for version 1.0.0 

  *
  *     '$Author: mlee $'
  *     '$Date: 2003-10-19 05:42:18 $'
  *     '$Revision: 1.4 $'
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
    <xsl:element name="xs:schema">
      <!--<xsl:attribute name="xmlns:xs">http://www.w3.org/2001/XMLSchema</xsl:attribute>-->
      <xsl:attribute name="elementFormDefault">qualified</xsl:attribute>
      <xsl:element name="xs:element">
        <xsl:attribute name="name">VegBankPackage</xsl:attribute>
        <xsl:element name="xs:complexType">
          <xsl:element name="xs:sequence">
        <!-- manually added fields to allow some versioning of xml docs -->
        <xs:element ref="doc-VegBankVersion"/>
        <xs:element ref="doc-date"/>
        <xs:element ref="doc-author"/>
        <xs:element ref="doc-authorSoftware" minOccurs="0"/>
        <xs:element ref="doc-comments" minOccurs="0" maxOccurs="unbounded"/>

            <xsl:for-each select="/dataModel/entity">
              <xsl:if test="attribute/attRelType/attribute::type='root'">
                <xsl:element name="xs:element">
                  <xsl:attribute name="ref"><xsl:value-of select="entityName"/></xsl:attribute>
                  <xsl:attribute name="minOccurs">0</xsl:attribute>
                  <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                </xsl:element>
              </xsl:if>
            </xsl:for-each>
            <xsl:for-each select="/dataModel/entity">
              <xsl:if test="attribute/attRelType/attribute::type='allfields'">
                <xsl:element name="xs:element">
                  <xsl:attribute name="ref"><xsl:value-of select="entityName"/></xsl:attribute>
                  <xsl:attribute name="minOccurs">0</xsl:attribute>
                  <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                </xsl:element>
              </xsl:if>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:element>
<!-- manually added element types -->
  <xs:element name="doc-VegBankVersion" type="xs:string"/>
  <xs:element name="doc-date" type="xs:dateTime"/>
  <xs:element name="doc-author" type="xs:string"/>
  <xs:element name="doc-authorSoftware" type="xs:string"/>
  <xs:element name="doc-comments" type="xs:string"/>
 
  <xs:simpleType name="nonEmptyString">
    <xs:restriction base="xs:string">
      <xs:minLength value="1"/>
      <xs:whiteSpace value="collapse"/>
    </xs:restriction>
  </xs:simpleType>



      <!-- first desclare simple types for closed lists -->
      <xsl:for-each select="dataModel/entity/attribute[attListType='closed'][attModel='logical']">
        <xsl:element name="xs:simpleType">
          <xsl:attribute name="name"><xsl:call-template name="closedlistType"/></xsl:attribute>
          <xsl:element name="xs:restriction">
            <xsl:attribute name="base"><xsl:call-template name="handleType"/></xsl:attribute>
<xsl:if test="$validLevel&gt;1">
            <xsl:for-each select="attList/attListItem">
              <xsl:element name="xs:enumeration">
                <xsl:attribute name="value"><xsl:value-of select="attListValue"/></xsl:attribute>
              </xsl:element>
            </xsl:for-each>
</xsl:if>
                        <!-- add empty string option for values where nulls are allowed : -->
   <!--         <xsl:if test="attNulls='yes'">
              <xsl:element name="xs:enumeration">
                <xsl:attribute name="value"/>
              </xsl:element>
            </xsl:if>  -->
          </xsl:element>
        </xsl:element>
        <!-- end simpleContent and restriction -->
      </xsl:for-each>
      <xsl:for-each select="dataModel/entity">
        <!-- complex type: table (entity) -->
        <xsl:element name="xs:element">
          <xsl:attribute name="name"><xsl:value-of select="entityName"/></xsl:attribute>
          <xsl:element name="xs:complexType">
            <xsl:element name="xs:sequence">
              <xsl:for-each select="attribute[attModel='logical']">
                <xsl:choose>
                  <xsl:when test="attKey='FK'">
                    <!-- FK field -->
                    <xsl:choose>
                      <xsl:when test="attRelType[attribute::type='normal']">
                        <xsl:element name="xs:element">
                          <xsl:attribute name="ref"><xsl:value-of select="concat(../entityName,'.',attName)"/></xsl:attribute>
                          <xsl:call-template name="handleNullz"/>
                        </xsl:element>
                      </xsl:when>
                      <xsl:when test="attRelType[attribute::type='inverted']">
                        <!-- inverted FK according to schema, that is FK is not listed here, but referenced from the primary table -->
                        <xsl:comment>
                          <xsl:value-of select="attName"/> is inverted rel</xsl:comment>
                      </xsl:when>
                      <xsl:otherwise>@@#@ CANNOT deal with attRelType @@#@: <xsl:value-of select="attName"/>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:when>
                  <!-- not a FK field -->
                  <xsl:otherwise>
                    <xsl:element name="xs:element">
                      <xsl:attribute name="ref"><xsl:value-of select="concat(../entityName,'.',attName)"/></xsl:attribute>
                      <xsl:call-template name="handleNullz"/>
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:for-each>
              <!-- check for inverted fks to link that object as child of this one -->
              <xsl:variable name="CurrEntity">
                <xsl:value-of select="entityName"/>
              </xsl:variable>
              <xsl:for-each select="../entity/attribute[attKey='FK'][attModel='logical'][attRelType/attribute::type='inverted'][substring-before(attReferences,'.')=$CurrEntity]">
              <xsl:sort select="../entityName" />
                <xsl:choose>
                  <xsl:when test="attRelType/attribute::name!='n/a'">
                    <xsl:element name="xs:element">
                      <xsl:attribute name="name"><xsl:value-of select="attRelType/attribute::name"/></xsl:attribute>
                      <xsl:attribute name="minOccurs">0</xsl:attribute>
                      <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                      <xsl:element name="xs:complexType">
                        <xsl:element name="xs:sequence">
                          <xsl:call-template name="getInvertedElement"/>
                        </xsl:element>
                      </xsl:element>
                    </xsl:element>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:call-template name="getInvertedElement"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:for-each>
              <!-- add all entities that are to be attached to each table, i.e. userdefined -->
              <xsl:call-template name="addTableEnts"/>
            </xsl:element>
          </xsl:element>
        </xsl:element>
        <xsl:for-each select="attribute[attModel='logical'][attRelType/attribute::type!='inverted']">
          <!-- add each field as its own element -->
          <xsl:element name="xs:element">
            <xsl:attribute name="name"><xsl:value-of select="concat(../entityName,'.',attName)"/></xsl:attribute>
            <xsl:element name="xs:complexType">
              <xsl:choose>
                <xsl:when test="attKey='FK'">
                  <!--    <xsl:element name="xs:sequence"> -->
                  <!--                      <xsl:attribute name="base"><xsl:call-template name="handleType"/></xsl:attribute>-->
                  <xsl:element name="xs:sequence">
                    <xsl:element name="xs:element">
                      <xsl:attribute name="ref"><xsl:value-of select="substring-before(attReferences,'.')"/></xsl:attribute>
                    </xsl:element>
                  </xsl:element>
                  <!--                  </xsl:element> end of  -->
                  <xsl:call-template name="addFieldAtts"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:element name="xs:simpleContent">
                    <xsl:element name="xs:extension">
                      <xsl:attribute name="base"><xsl:choose><xsl:when test="attListType='closed'"><xsl:call-template name="closedlistType"/></xsl:when><xsl:otherwise><!-- this is not a closed list, just get the regular type --><xsl:call-template name="handleType"/></xsl:otherwise></xsl:choose></xsl:attribute>
                      <xsl:call-template name="addFieldAtts"/>
                    </xsl:element>
                  </xsl:element>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:element>
            <!-- complex type -->
          </xsl:element>
        </xsl:for-each>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template name="getInvertedElement">
    <xsl:element name="xs:element">
      <xsl:attribute name="ref"><xsl:value-of select="../entityName"/></xsl:attribute>
      <xsl:attribute name="minOccurs">0</xsl:attribute>
      <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
    </xsl:element>
  </xsl:template>
  <xsl:template match="*"/>
  <xsl:template name="handleType">
    <xsl:choose>
      <xsl:when test="attType='BLOB'">xs:hexBinary</xsl:when>
      <xsl:when test="attType='oid'">xs:hexBinary</xsl:when>
      <xsl:when test="attType='Boolean'">xs:boolean</xsl:when>
      <xsl:when test="attType='Date'">xs:dateTime</xsl:when>
      <xsl:when test="attType='Float'">xs:decimal</xsl:when>
      <xsl:when test="attType='Integer'">xs:long</xsl:when>
      <xsl:when test="attType='serial'">xs:long</xsl:when>
      <xsl:when test="attType='text'"><xsl:choose><xsl:when test="attNulls='no'">nonEmptyString</xsl:when><xsl:otherwise>xs:string</xsl:otherwise></xsl:choose></xsl:when>
      <xsl:when test="starts-with(attType,'varchar')"><xsl:choose><xsl:when test="attNulls='no'">nonEmptyString</xsl:when><xsl:otherwise>xs:string</xsl:otherwise></xsl:choose></xsl:when>
      <xsl:otherwise>@SOMETHINGELSE:<xsl:value-of select="attType"/>@</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="handleNullz">
    <xsl:attribute name="minOccurs"><xsl:choose><xsl:when test="attNulls='no'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:attribute>
  </xsl:template>
  <xsl:template name="addFieldAtts">
    <xsl:for-each select="/dataModel/entity/attribute[attRelType/attribute::type='allfields'][attModel='logical']">
    <xsl:sort select="../entityName"/>
      <xsl:element name="xs:attribute">
        <xsl:attribute name="name"><xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/></xsl:attribute>
        <xsl:attribute name="type"><xsl:call-template name="handleType"/></xsl:attribute>
        <xsl:attribute name="use">optional</xsl:attribute>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="addTableEnts">
    <xsl:for-each select="/dataModel/entity[attribute/attRelType/attribute::type='alltables']">
    <xsl:sort select="entityName"/>
      <xsl:element name="xs:element">
        <xsl:attribute name="ref"><xsl:value-of select="entityName"/></xsl:attribute>
        <xsl:attribute name="minOccurs">0</xsl:attribute>
        <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="closedlistType">
    <xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/>__closedlistType</xsl:template>
</xsl:stylesheet>
