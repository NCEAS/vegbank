<?xml version="1.0" encoding="UTF-8"?>
<!--

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

-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="csvtools.xsl"/>
  <xsl:output method="text" encoding="UTF-16"/>
  <xsl:template match="doc-VegBankVersion | doc-date | doc-author | doc-authorSoftware | doc-comments"/>
  <xsl:template match="VegBankPackage">HD,tableName,field1,field2,field3,field4,field5,field6,field7,field8,field9,field10,field11,field12,field13,field14,field15,field16,field17,field18,field19,field20,field21,field22,field23,field24,field25,field26,field27,field28,field29,field30,field31,field32,field33,field34,field35,field36,field37,field38,field39,field40,field41,field42,field43,field44,field45,field46,field47,field48,field49,field50,field51,field52,field53,field54,field55,field56,field57,field58,field59,field60,field61,field62,field63,field64,field65,field66,field67,field68,field69,field70,field71,field72,field73,field74,field75<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commConcept</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMNAME_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commDescription</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commCorrelation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCORRELATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMSTATUS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commConvergence</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">correlationStart</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">correlationStop</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commLineage</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMLINEAGE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">parentCommStatus_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">childCommStatus_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMNAME_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">dateEntered</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMSTATUS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commConceptStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commParent_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commLevel</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">startDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stopDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commPartyComments</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commUsage</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMUSAGE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMNAME_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">usageStart</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">usageStop</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commNameStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classSystem</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMSTATUS_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantConcept</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTNAME_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantDescription</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantCorrelation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTCORRELATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTSTATUS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantConvergence</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">correlationStart</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">correlationStop</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantLineage</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTLINEAGE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">childPlantStatus_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">parentPlantStatus_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTNAME_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">dateEntered</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTSTATUS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantConceptStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">startDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stopDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantPartyComments</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantParent_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantLevel</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantUsage</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTUSAGE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTNAME_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">usageStart</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">usageStop</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantNameStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classSystem</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTSTATUS_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">address</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ADDRESS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">party_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">organization_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">orgPosition</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">email</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">deliveryPoint</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">city</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">administrativeArea</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">postalCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">country</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">currentFlag</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">addressStartDate</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">aux_Role</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ROLE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">roleCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">roleDescription</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">shortName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">fulltext</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">title</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">titleSuperior</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">pubDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">conferenceDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceJournal_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">volume</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">issue</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">pageRange</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">totalPages</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">publisher</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">publicationPlace</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">isbn</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">edition</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">numberOfVolumes</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">chapterNumber</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reportNumber</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">communicationType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">degree</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">url</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">doi</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">additionalInfo</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceAltIdent</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceAltIdent_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">system</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">identifier</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceContributor</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceContributor_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceParty_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">roleType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">position</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceParty</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceParty_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">type</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">positionName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">salutation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">givenName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">surname</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">suffix</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">organizationName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">currentParty_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceJournal</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">referenceJournal_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">journal</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">issn</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">abbreviation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classContributor</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">CLASSCONTRIBUTOR_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCLASS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ROLE_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commClass</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCLASS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classStartDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classStopDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">inspection</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableAnalysis</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">multivariateAnalysis</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">expertSystem</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classPublication_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classNotes</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commInterpretation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMINTERPRETATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCLASS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COMMCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classFit</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classConfidence</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">commAuthority_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notes</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">type</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">nomenclaturalType</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">coverIndex</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COVERINDEX_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COVERMETHOD_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">coverCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">upperLimit</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">lowerLimit</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">coverPercent</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">indexDescription</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">coverMethod</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COVERMETHOD_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">coverType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">coverEstimationMethod</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">definedValue</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">DEFINEDVALUE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">USERDEFINED_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableRecord_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">definedValue</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">disturbanceObs</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">disturbanceObs_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">disturbanceType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">disturbanceIntensity</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">disturbanceAge</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">disturbanceExtent</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">disturbanceComment</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">graphic</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">GRAPHIC_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">graphicName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">graphicLocation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">graphicDescription</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">graphicType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">graphicDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">graphicData</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">namedPlace</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">NAMEDPLACE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">placeSystem</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">placeName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">placeDescription</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">placeCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">owner</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">namedPlaceCorrelation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">NAMEDPLACECORRELATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARENTPLACE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">CHILDPLACE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">placeConvergence</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">note</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">NOTE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">NOTELINK_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ROLE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">noteDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">noteType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">noteText</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">noteLink</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">NOTELINK_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">attributeName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableRecord</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">observation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PREVIOUSOBS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLOT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PROJECT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorObsCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">obsStartDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">obsEndDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">dateAccuracy</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">dateEntered</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">COVERMETHOD_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">coverDispersion</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">autoTaxonCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STRATUMMETHOD_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">methodNarrative</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonObservationArea</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemSizeLimit</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemObservationArea</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemSampleMethod</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">originalData</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">effortLevel</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plotValidationLevel</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">floristicQuality</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">bryophyteQuality</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">lichenQuality</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">observationNarrative</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">landscapeNarrative</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">homogeneity</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">phenologicAspect</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">representativeness</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">standMaturity</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">successionalStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">numberOfTaxa</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">basalArea</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">hydrologicRegime</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilMoistureRegime</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilDrainage</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">waterSalinity</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">waterDepth</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">shoreDistance</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilDepth</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">organicDepth</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">SOILTAXON_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilTaxonSrc</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">percentBedRock</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">percentRockGravel</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">percentWood</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">percentLitter</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">percentBareSoil</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">percentWater</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">percentOther</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">nameOther</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">treeHt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">shrubHt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">fieldHt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">nonvascularHt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">submergedHt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">treeCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">shrubCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">fieldCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">nonvascularCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">floatingCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">submergedCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">dominantStratum</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">growthform1Type</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">growthform2Type</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">growthform3Type</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">growthform1Cover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">growthform2Cover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">growthform3Cover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">totalCover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notesPublic</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notesMgt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">revisions</xsl:with-param>
    </xsl:call-template>,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">observationContributor</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATIONCONTRIBUTOR_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ROLE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">contributionDate</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">observationSynonym</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATIONSYNONYM_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">synonymObservation_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">primaryObservation_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ROLE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classStartDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">classStopDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">synonymComment</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">party</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">salutation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">givenName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">middleName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">surName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">organizationName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">currentName_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">contactInstructions</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">partyType</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">partyMember</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">partyMember_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">parentParty_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">childParty_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">role_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">memberStart</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">memberStop</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">place</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLOTPLACE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLOT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">calculated</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">NAMEDPLACE_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plot</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLOT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorPlotCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARENT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">realLatitude</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">realLongitude</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">locationAccuracy</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">confidentialityStatus</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">confidentialityReason</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">latitude</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">longitude</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorE</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorN</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorZone</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorDatum</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorLocation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">locationNarrative</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plotRationaleNarrative</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">azimuth</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">dsgpoly</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">shape</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">area</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">standSize</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">placementMethod</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">permanence</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">layoutNarrative</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">elevation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">elevationAccuracy</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">elevationRange</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">slopeAspect</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">minSlopeAspect</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">maxSlopeAspect</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">slopeGradient</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">minSlopeGradient</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">maxSlopeGradient</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">topoPosition</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">landform</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">surficialDeposits</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">rockType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notesPublic</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notesMgt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">revisions</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">project</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PROJECT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">projectName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">projectDescription</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">startDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stopDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">projectContributor</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PROJECTCONTRIBUTOR_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PROJECT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ROLE_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">revision</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">REVISION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableAttribute</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableRecord</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">revisionDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">previousValueText</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">previousValueType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">previousRevision_ID</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilObs</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">SOILOBS_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilHorizon</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilDepthTop</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilDepthBottom</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilColor</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilOrganic</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilTexture</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilSand</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilSilt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilClay</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilCoarse</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilPH</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">exchangeCapacity</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">baseSaturation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilDescription</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilTaxon</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">SOILTAXON_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilLevel</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">SOILPARENT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">soilFramework</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemCount</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STEMCOUNT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">TAXONIMPORTANCE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemDiameter</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemDiameterAccuracy</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemHeight</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemHeightAccuracy</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemCount</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemTaxonArea</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemLocation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STEMLOCATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STEMCOUNT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemCode</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemXPosition</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemYPosition</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemHealth</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratum</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STRATUM_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STRATUMTYPE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumHeight</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumBase</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumCover</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumMethod</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STRATUMMETHOD_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumMethodName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumMethodDescription</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumAssignment</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STRATUMTYPE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">STRATUMMETHOD_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumIndex</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratumDescription</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonImportance</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonImportance_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonObservation_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stratum_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">cover</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">basalArea</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">biomass</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">inferenceArea</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonInterpretation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">TAXONINTERPRETATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">TAXONOBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">stemLocation_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTCONCEPT_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">interpretationDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PLANTNAME_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">ROLE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">interpretationType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">originalInterpretation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">currentInterpretation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonFit</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonConfidence</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">collector_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">collectionNumber</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">collectionDate</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">museum_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">museumAccessionNumber</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">groupType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notes</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notesPublic</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">notesMgt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">revisions</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonObservation</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">TAXONOBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">OBSERVATION_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">authorPlantName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">reference_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonInferenceArea</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonAlt</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonAlt_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonInterpretation_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plantConcept_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonAltFit</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonAltConfidence</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">taxonAltNotes</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">telephone</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">TELEPHONE_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">PARTY_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">phoneNumber</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">phoneType</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">userDefined</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">USERDEFINED_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">userDefinedName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">userDefinedMetadata</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">userDefinedCategory</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">userDefinedType</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">tableName</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">accessionCode</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:value-of select="$LF"/>"H",<xsl:call-template name="csvIt">
      <xsl:with-param name="text">embargo</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">embargo_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">plot_ID</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">embargoReason</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">embargoStart</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">embargoStop</xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">defaultStatus</xsl:with-param>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commConcept">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commConcept.COMMCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commConcept.COMMNAME_ID/commName/commName.COMMNAME_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commConcept.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commConcept.commDescription"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commConcept.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commConcept.COMMCONCEPT_ID"/>
  <xsl:template match="commConcept.COMMNAME_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commConcept.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commConcept.commDescription"/>
  <xsl:template match="commConcept.accessionCode"/>
  <xsl:template match="commCorrelation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commCorrelation.COMMCORRELATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../commStatus.COMMSTATUS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commCorrelation.COMMCONCEPT_ID/commConcept/commConcept.COMMCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commCorrelation.commConvergence"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commCorrelation.correlationStart"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commCorrelation.correlationStop"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commCorrelation.COMMCORRELATION_ID"/>
  <xsl:template match="commCorrelation.COMMSTATUS_ID"/>
  <xsl:template match="commCorrelation.COMMCONCEPT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commCorrelation.commConvergence"/>
  <xsl:template match="commCorrelation.correlationStart"/>
  <xsl:template match="commCorrelation.correlationStop"/>
  <xsl:template match="commLineage">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commLineage.COMMLINEAGE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commLineage.parentCommStatus_ID/commStatus/commStatus.COMMSTATUS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../commStatus.COMMSTATUS_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commLineage.COMMLINEAGE_ID"/>
  <xsl:template match="commLineage.parentCommStatus_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commLineage.childCommStatus_ID"/>
  <xsl:template match="commName">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commName.COMMNAME_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commName.commName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commName.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commName.dateEntered"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commName.COMMNAME_ID"/>
  <xsl:template match="commName.commName"/>
  <xsl:template match="commName.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commName.dateEntered"/>
  <xsl:template match="commStatus">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.COMMSTATUS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../commConcept.COMMCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.commConceptStatus"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.commParent_ID/commConcept/commConcept.COMMCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.commLevel"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.startDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.stopDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.commPartyComments"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commStatus.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commStatus.COMMSTATUS_ID"/>
  <xsl:template match="commStatus.COMMCONCEPT_ID"/>
  <xsl:template match="commStatus.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commStatus.commConceptStatus"/>
  <xsl:template match="commStatus.commParent_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commStatus.commLevel"/>
  <xsl:template match="commStatus.startDate"/>
  <xsl:template match="commStatus.stopDate"/>
  <xsl:template match="commStatus.commPartyComments"/>
  <xsl:template match="commStatus.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commStatus.accessionCode"/>
  <xsl:template match="commUsage">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commUsage.COMMUSAGE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commUsage.COMMNAME_ID/commName/commName.COMMNAME_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commUsage.usageStart"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commUsage.usageStop"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commUsage.commNameStatus"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commUsage.classSystem"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../commStatus.COMMSTATUS_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commUsage.COMMUSAGE_ID"/>
  <xsl:template match="commUsage.COMMNAME_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commUsage.usageStart"/>
  <xsl:template match="commUsage.usageStop"/>
  <xsl:template match="commUsage.commNameStatus"/>
  <xsl:template match="commUsage.classSystem"/>
  <xsl:template match="commUsage.COMMSTATUS_ID"/>
  <xsl:template match="plantConcept">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantConcept.PLANTCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantConcept.PLANTNAME_ID/plantName/plantName.PLANTNAME_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantConcept.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantConcept.plantDescription"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantConcept.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantConcept.PLANTCONCEPT_ID"/>
  <xsl:template match="plantConcept.PLANTNAME_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantConcept.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantConcept.plantDescription"/>
  <xsl:template match="plantConcept.accessionCode"/>
  <xsl:template match="plantCorrelation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantCorrelation.PLANTCORRELATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../plantStatus.PLANTSTATUS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantCorrelation.PLANTCONCEPT_ID/plantConcept/plantConcept.PLANTCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantCorrelation.plantConvergence"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantCorrelation.correlationStart"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantCorrelation.correlationStop"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantCorrelation.PLANTCORRELATION_ID"/>
  <xsl:template match="plantCorrelation.PLANTSTATUS_ID"/>
  <xsl:template match="plantCorrelation.PLANTCONCEPT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantCorrelation.plantConvergence"/>
  <xsl:template match="plantCorrelation.correlationStart"/>
  <xsl:template match="plantCorrelation.correlationStop"/>
  <xsl:template match="plantLineage">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantLineage.PLANTLINEAGE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../plantStatus.PLANTSTATUS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantLineage.parentPlantStatus_ID/plantStatus/plantStatus.PLANTSTATUS_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantLineage.PLANTLINEAGE_ID"/>
  <xsl:template match="plantLineage.childPlantStatus_ID"/>
  <xsl:template match="plantLineage.parentPlantStatus_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantName">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantName.PLANTNAME_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantName.plantName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantName.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantName.dateEntered"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantName.PLANTNAME_ID"/>
  <xsl:template match="plantName.plantName"/>
  <xsl:template match="plantName.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantName.dateEntered"/>
  <xsl:template match="plantStatus">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.PLANTSTATUS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../plantConcept.PLANTCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.plantConceptStatus"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.startDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.stopDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.plantPartyComments"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.plantParent_ID/plantConcept/plantConcept.PLANTCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.plantLevel"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantStatus.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantStatus.PLANTSTATUS_ID"/>
  <xsl:template match="plantStatus.PLANTCONCEPT_ID"/>
  <xsl:template match="plantStatus.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantStatus.plantConceptStatus"/>
  <xsl:template match="plantStatus.startDate"/>
  <xsl:template match="plantStatus.stopDate"/>
  <xsl:template match="plantStatus.plantPartyComments"/>
  <xsl:template match="plantStatus.plantParent_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantStatus.plantLevel"/>
  <xsl:template match="plantStatus.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantStatus.accessionCode"/>
  <xsl:template match="plantUsage">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantUsage.PLANTUSAGE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantUsage.PLANTNAME_ID/plantName/plantName.PLANTNAME_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantUsage.usageStart"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantUsage.usageStop"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantUsage.plantNameStatus"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plantUsage.classSystem"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../plantStatus.PLANTSTATUS_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantUsage.PLANTUSAGE_ID"/>
  <xsl:template match="plantUsage.PLANTNAME_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plantUsage.usageStart"/>
  <xsl:template match="plantUsage.usageStop"/>
  <xsl:template match="plantUsage.plantNameStatus"/>
  <xsl:template match="plantUsage.classSystem"/>
  <xsl:template match="plantUsage.PLANTSTATUS_ID"/>
  <xsl:template match="address">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.ADDRESS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.organization_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.orgPosition"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.email"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.deliveryPoint"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.city"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.administrativeArea"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.postalCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.country"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.currentFlag"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="address.addressStartDate"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="address.ADDRESS_ID"/>
  <xsl:template match="address.party_ID"/>
  <xsl:template match="address.organization_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="address.orgPosition"/>
  <xsl:template match="address.email"/>
  <xsl:template match="address.deliveryPoint"/>
  <xsl:template match="address.city"/>
  <xsl:template match="address.administrativeArea"/>
  <xsl:template match="address.postalCode"/>
  <xsl:template match="address.country"/>
  <xsl:template match="address.currentFlag"/>
  <xsl:template match="address.addressStartDate"/>
  <xsl:template match="aux_Role">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="aux_Role.ROLE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="aux_Role.roleCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="aux_Role.roleDescription"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="aux_Role.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="aux_Role.ROLE_ID"/>
  <xsl:template match="aux_Role.roleCode"/>
  <xsl:template match="aux_Role.roleDescription"/>
  <xsl:template match="aux_Role.accessionCode"/>
  <xsl:template match="reference">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.shortName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.fulltext"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.referenceType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.title"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.titleSuperior"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.pubDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.accessDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.conferenceDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.referenceJournal_ID/referenceJournal/referenceJournal.referenceJournal_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.volume"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.issue"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.pageRange"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.totalPages"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.publisher"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.publicationPlace"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.isbn"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.edition"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.numberOfVolumes"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.chapterNumber"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.reportNumber"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.communicationType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.degree"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.url"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.doi"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.additionalInfo"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="reference.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="reference.reference_ID"/>
  <xsl:template match="reference.shortName"/>
  <xsl:template match="reference.fulltext"/>
  <xsl:template match="reference.referenceType"/>
  <xsl:template match="reference.title"/>
  <xsl:template match="reference.titleSuperior"/>
  <xsl:template match="reference.pubDate"/>
  <xsl:template match="reference.accessDate"/>
  <xsl:template match="reference.conferenceDate"/>
  <xsl:template match="reference.referenceJournal_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="reference.volume"/>
  <xsl:template match="reference.issue"/>
  <xsl:template match="reference.pageRange"/>
  <xsl:template match="reference.totalPages"/>
  <xsl:template match="reference.publisher"/>
  <xsl:template match="reference.publicationPlace"/>
  <xsl:template match="reference.isbn"/>
  <xsl:template match="reference.edition"/>
  <xsl:template match="reference.numberOfVolumes"/>
  <xsl:template match="reference.chapterNumber"/>
  <xsl:template match="reference.reportNumber"/>
  <xsl:template match="reference.communicationType"/>
  <xsl:template match="reference.degree"/>
  <xsl:template match="reference.url"/>
  <xsl:template match="reference.doi"/>
  <xsl:template match="reference.additionalInfo"/>
  <xsl:template match="reference.accessionCode"/>
  <xsl:template match="referenceAltIdent">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceAltIdent.referenceAltIdent_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceAltIdent.system"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceAltIdent.identifier"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="referenceAltIdent.referenceAltIdent_ID"/>
  <xsl:template match="referenceAltIdent.reference_ID"/>
  <xsl:template match="referenceAltIdent.system"/>
  <xsl:template match="referenceAltIdent.identifier"/>
  <xsl:template match="referenceContributor">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceContributor.referenceContributor_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceContributor.referenceParty_ID/referenceParty/referenceParty.referenceParty_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceContributor.roleType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceContributor.position"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="referenceContributor.referenceContributor_ID"/>
  <xsl:template match="referenceContributor.reference_ID"/>
  <xsl:template match="referenceContributor.referenceParty_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="referenceContributor.roleType"/>
  <xsl:template match="referenceContributor.position"/>
  <xsl:template match="referenceParty">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.referenceParty_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.type"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.positionName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.salutation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.givenName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.surname"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.suffix"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.organizationName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.currentParty_ID/referenceParty/referenceParty.referenceParty_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceParty.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="referenceParty.referenceParty_ID"/>
  <xsl:template match="referenceParty.type"/>
  <xsl:template match="referenceParty.positionName"/>
  <xsl:template match="referenceParty.salutation"/>
  <xsl:template match="referenceParty.givenName"/>
  <xsl:template match="referenceParty.surname"/>
  <xsl:template match="referenceParty.suffix"/>
  <xsl:template match="referenceParty.organizationName"/>
  <xsl:template match="referenceParty.currentParty_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="referenceParty.accessionCode"/>
  <xsl:template match="referenceJournal">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceJournal.referenceJournal_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceJournal.journal"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceJournal.issn"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceJournal.abbreviation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="referenceJournal.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="referenceJournal.referenceJournal_ID"/>
  <xsl:template match="referenceJournal.journal"/>
  <xsl:template match="referenceJournal.issn"/>
  <xsl:template match="referenceJournal.abbreviation"/>
  <xsl:template match="referenceJournal.accessionCode"/>
  <xsl:template match="classContributor">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="classContributor.CLASSCONTRIBUTOR_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../commClass.COMMCLASS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="classContributor.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="classContributor.ROLE_ID/aux_Role/aux_Role.ROLE_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="classContributor.CLASSCONTRIBUTOR_ID"/>
  <xsl:template match="classContributor.COMMCLASS_ID"/>
  <xsl:template match="classContributor.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="classContributor.ROLE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commClass">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.COMMCLASS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.classStartDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.classStopDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.inspection"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.tableAnalysis"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.multivariateAnalysis"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.expertSystem"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.classPublication_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.classNotes"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commClass.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commClass.COMMCLASS_ID"/>
  <xsl:template match="commClass.OBSERVATION_ID"/>
  <xsl:template match="commClass.classStartDate"/>
  <xsl:template match="commClass.classStopDate"/>
  <xsl:template match="commClass.inspection"/>
  <xsl:template match="commClass.tableAnalysis"/>
  <xsl:template match="commClass.multivariateAnalysis"/>
  <xsl:template match="commClass.expertSystem"/>
  <xsl:template match="commClass.classPublication_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commClass.classNotes"/>
  <xsl:template match="commClass.accessionCode"/>
  <xsl:template match="commInterpretation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.COMMINTERPRETATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../commClass.COMMCLASS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.COMMCONCEPT_ID/commConcept/commConcept.COMMCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.classFit"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.classConfidence"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.commAuthority_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.notes"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.type"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="commInterpretation.nomenclaturalType"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commInterpretation.COMMINTERPRETATION_ID"/>
  <xsl:template match="commInterpretation.COMMCLASS_ID"/>
  <xsl:template match="commInterpretation.COMMCONCEPT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commInterpretation.classFit"/>
  <xsl:template match="commInterpretation.classConfidence"/>
  <xsl:template match="commInterpretation.commAuthority_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="commInterpretation.notes"/>
  <xsl:template match="commInterpretation.type"/>
  <xsl:template match="commInterpretation.nomenclaturalType"/>
  <xsl:template match="coverIndex">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverIndex.COVERINDEX_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../coverMethod.COVERMETHOD_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverIndex.coverCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverIndex.upperLimit"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverIndex.lowerLimit"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverIndex.coverPercent"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverIndex.indexDescription"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="coverIndex.COVERINDEX_ID"/>
  <xsl:template match="coverIndex.COVERMETHOD_ID"/>
  <xsl:template match="coverIndex.coverCode"/>
  <xsl:template match="coverIndex.upperLimit"/>
  <xsl:template match="coverIndex.lowerLimit"/>
  <xsl:template match="coverIndex.coverPercent"/>
  <xsl:template match="coverIndex.indexDescription"/>
  <xsl:template match="coverMethod">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverMethod.COVERMETHOD_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverMethod.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverMethod.coverType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverMethod.coverEstimationMethod"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="coverMethod.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="coverMethod.COVERMETHOD_ID"/>
  <xsl:template match="coverMethod.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="coverMethod.coverType"/>
  <xsl:template match="coverMethod.coverEstimationMethod"/>
  <xsl:template match="coverMethod.accessionCode"/>
  <xsl:template match="definedValue">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="definedValue.DEFINEDVALUE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="definedValue.USERDEFINED_ID/userDefined/userDefined.USERDEFINED_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="definedValue.tableRecord_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="definedValue.definedValue"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="definedValue.DEFINEDVALUE_ID"/>
  <xsl:template match="definedValue.USERDEFINED_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="definedValue.tableRecord_ID"/>
  <xsl:template match="definedValue.definedValue"/>
  <xsl:template match="disturbanceObs">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="disturbanceObs.disturbanceObs_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="disturbanceObs.disturbanceType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="disturbanceObs.disturbanceIntensity"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="disturbanceObs.disturbanceAge"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="disturbanceObs.disturbanceExtent"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="disturbanceObs.disturbanceComment"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="disturbanceObs.disturbanceObs_ID"/>
  <xsl:template match="disturbanceObs.OBSERVATION_ID"/>
  <xsl:template match="disturbanceObs.disturbanceType"/>
  <xsl:template match="disturbanceObs.disturbanceIntensity"/>
  <xsl:template match="disturbanceObs.disturbanceAge"/>
  <xsl:template match="disturbanceObs.disturbanceExtent"/>
  <xsl:template match="disturbanceObs.disturbanceComment"/>
  <xsl:template match="graphic">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.GRAPHIC_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.graphicName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.graphicLocation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.graphicDescription"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.graphicType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.graphicDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.graphicData"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="graphic.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="graphic.GRAPHIC_ID"/>
  <xsl:template match="graphic.OBSERVATION_ID"/>
  <xsl:template match="graphic.graphicName"/>
  <xsl:template match="graphic.graphicLocation"/>
  <xsl:template match="graphic.graphicDescription"/>
  <xsl:template match="graphic.graphicType"/>
  <xsl:template match="graphic.graphicDate"/>
  <xsl:template match="graphic.graphicData"/>
  <xsl:template match="graphic.accessionCode"/>
  <xsl:template match="namedPlace">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.NAMEDPLACE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.placeSystem"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.placeName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.placeDescription"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.placeCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.owner"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlace.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="namedPlace.NAMEDPLACE_ID"/>
  <xsl:template match="namedPlace.placeSystem"/>
  <xsl:template match="namedPlace.placeName"/>
  <xsl:template match="namedPlace.placeDescription"/>
  <xsl:template match="namedPlace.placeCode"/>
  <xsl:template match="namedPlace.owner"/>
  <xsl:template match="namedPlace.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="namedPlace.accessionCode"/>
  <xsl:template match="namedPlaceCorrelation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlaceCorrelation.NAMEDPLACECORRELATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../namedPlace.NAMEDPLACE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlaceCorrelation.CHILDPLACE_ID/namedPlace/namedPlace.NAMEDPLACE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="namedPlaceCorrelation.placeConvergence"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="namedPlaceCorrelation.NAMEDPLACECORRELATION_ID"/>
  <xsl:template match="namedPlaceCorrelation.PARENTPLACE_ID"/>
  <xsl:template match="namedPlaceCorrelation.CHILDPLACE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="namedPlaceCorrelation.placeConvergence"/>
  <xsl:template match="note">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="note.NOTE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../noteLink.NOTELINK_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="note.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="note.ROLE_ID/aux_Role/aux_Role.ROLE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="note.noteDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="note.noteType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="note.noteText"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="note.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="note.NOTE_ID"/>
  <xsl:template match="note.NOTELINK_ID"/>
  <xsl:template match="note.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="note.ROLE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="note.noteDate"/>
  <xsl:template match="note.noteType"/>
  <xsl:template match="note.noteText"/>
  <xsl:template match="note.accessionCode"/>
  <xsl:template match="noteLink">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="noteLink.NOTELINK_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="noteLink.tableName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="noteLink.attributeName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="noteLink.tableRecord"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="noteLink.NOTELINK_ID"/>
  <xsl:template match="noteLink.tableName"/>
  <xsl:template match="noteLink.attributeName"/>
  <xsl:template match="noteLink.tableRecord"/>
  <xsl:template match="observation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.PREVIOUSOBS_ID/observation/observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.PLOT_ID/plot/plot.PLOT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.PROJECT_ID/project/project.PROJECT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.authorObsCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.obsStartDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.obsEndDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.dateAccuracy"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.dateEntered"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.COVERMETHOD_ID/coverMethod/coverMethod.COVERMETHOD_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.coverDispersion"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.autoTaxonCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.STRATUMMETHOD_ID/stratumMethod/stratumMethod.STRATUMMETHOD_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.methodNarrative"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.taxonObservationArea"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.stemSizeLimit"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.stemObservationArea"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.stemSampleMethod"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.originalData"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.effortLevel"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.plotValidationLevel"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.floristicQuality"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.bryophyteQuality"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.lichenQuality"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.observationNarrative"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.landscapeNarrative"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.homogeneity"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.phenologicAspect"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.representativeness"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.standMaturity"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.successionalStatus"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.numberOfTaxa"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.basalArea"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.hydrologicRegime"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.soilMoistureRegime"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.soilDrainage"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.waterSalinity"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.waterDepth"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.shoreDistance"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.soilDepth"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.organicDepth"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.SOILTAXON_ID/soilTaxon/soilTaxon.SOILTAXON_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.soilTaxonSrc"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.percentBedRock"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.percentRockGravel"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.percentWood"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.percentLitter"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.percentBareSoil"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.percentWater"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.percentOther"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.nameOther"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.treeHt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.shrubHt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.fieldHt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.nonvascularHt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.submergedHt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.treeCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.shrubCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.fieldCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.nonvascularCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.floatingCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.submergedCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.dominantStratum"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.growthform1Type"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.growthform2Type"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.growthform3Type"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.growthform1Cover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.growthform2Cover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.growthform3Cover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.totalCover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.accessionCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.notesPublic"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.notesMgt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observation.revisions"/>
    </xsl:call-template>,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observation.OBSERVATION_ID"/>
  <xsl:template match="observation.PREVIOUSOBS_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observation.PLOT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observation.PROJECT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observation.authorObsCode"/>
  <xsl:template match="observation.obsStartDate"/>
  <xsl:template match="observation.obsEndDate"/>
  <xsl:template match="observation.dateAccuracy"/>
  <xsl:template match="observation.dateEntered"/>
  <xsl:template match="observation.COVERMETHOD_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observation.coverDispersion"/>
  <xsl:template match="observation.autoTaxonCover"/>
  <xsl:template match="observation.STRATUMMETHOD_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observation.methodNarrative"/>
  <xsl:template match="observation.taxonObservationArea"/>
  <xsl:template match="observation.stemSizeLimit"/>
  <xsl:template match="observation.stemObservationArea"/>
  <xsl:template match="observation.stemSampleMethod"/>
  <xsl:template match="observation.originalData"/>
  <xsl:template match="observation.effortLevel"/>
  <xsl:template match="observation.plotValidationLevel"/>
  <xsl:template match="observation.floristicQuality"/>
  <xsl:template match="observation.bryophyteQuality"/>
  <xsl:template match="observation.lichenQuality"/>
  <xsl:template match="observation.observationNarrative"/>
  <xsl:template match="observation.landscapeNarrative"/>
  <xsl:template match="observation.homogeneity"/>
  <xsl:template match="observation.phenologicAspect"/>
  <xsl:template match="observation.representativeness"/>
  <xsl:template match="observation.standMaturity"/>
  <xsl:template match="observation.successionalStatus"/>
  <xsl:template match="observation.numberOfTaxa"/>
  <xsl:template match="observation.basalArea"/>
  <xsl:template match="observation.hydrologicRegime"/>
  <xsl:template match="observation.soilMoistureRegime"/>
  <xsl:template match="observation.soilDrainage"/>
  <xsl:template match="observation.waterSalinity"/>
  <xsl:template match="observation.waterDepth"/>
  <xsl:template match="observation.shoreDistance"/>
  <xsl:template match="observation.soilDepth"/>
  <xsl:template match="observation.organicDepth"/>
  <xsl:template match="observation.SOILTAXON_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observation.soilTaxonSrc"/>
  <xsl:template match="observation.percentBedRock"/>
  <xsl:template match="observation.percentRockGravel"/>
  <xsl:template match="observation.percentWood"/>
  <xsl:template match="observation.percentLitter"/>
  <xsl:template match="observation.percentBareSoil"/>
  <xsl:template match="observation.percentWater"/>
  <xsl:template match="observation.percentOther"/>
  <xsl:template match="observation.nameOther"/>
  <xsl:template match="observation.treeHt"/>
  <xsl:template match="observation.shrubHt"/>
  <xsl:template match="observation.fieldHt"/>
  <xsl:template match="observation.nonvascularHt"/>
  <xsl:template match="observation.submergedHt"/>
  <xsl:template match="observation.treeCover"/>
  <xsl:template match="observation.shrubCover"/>
  <xsl:template match="observation.fieldCover"/>
  <xsl:template match="observation.nonvascularCover"/>
  <xsl:template match="observation.floatingCover"/>
  <xsl:template match="observation.submergedCover"/>
  <xsl:template match="observation.dominantStratum"/>
  <xsl:template match="observation.growthform1Type"/>
  <xsl:template match="observation.growthform2Type"/>
  <xsl:template match="observation.growthform3Type"/>
  <xsl:template match="observation.growthform1Cover"/>
  <xsl:template match="observation.growthform2Cover"/>
  <xsl:template match="observation.growthform3Cover"/>
  <xsl:template match="observation.totalCover"/>
  <xsl:template match="observation.accessionCode"/>
  <xsl:template match="observation.notesPublic"/>
  <xsl:template match="observation.notesMgt"/>
  <xsl:template match="observation.revisions"/>
  <xsl:template match="observationContributor">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationContributor.OBSERVATIONCONTRIBUTOR_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationContributor.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationContributor.ROLE_ID/aux_Role/aux_Role.ROLE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationContributor.contributionDate"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observationContributor.OBSERVATIONCONTRIBUTOR_ID"/>
  <xsl:template match="observationContributor.OBSERVATION_ID"/>
  <xsl:template match="observationContributor.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observationContributor.ROLE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observationContributor.contributionDate"/>
  <xsl:template match="observationSynonym">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.OBSERVATIONSYNONYM_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.synonymObservation_ID/observation/observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.ROLE_ID/aux_Role/aux_Role.ROLE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.classStartDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.classStopDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.synonymComment"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="observationSynonym.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observationSynonym.OBSERVATIONSYNONYM_ID"/>
  <xsl:template match="observationSynonym.synonymObservation_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observationSynonym.primaryObservation_ID"/>
  <xsl:template match="observationSynonym.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observationSynonym.ROLE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="observationSynonym.classStartDate"/>
  <xsl:template match="observationSynonym.classStopDate"/>
  <xsl:template match="observationSynonym.synonymComment"/>
  <xsl:template match="observationSynonym.accessionCode"/>
  <xsl:template match="party">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.salutation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.givenName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.middleName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.surName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.organizationName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.currentName_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.contactInstructions"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.accessionCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="party.partyType"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="party.PARTY_ID"/>
  <xsl:template match="party.salutation"/>
  <xsl:template match="party.givenName"/>
  <xsl:template match="party.middleName"/>
  <xsl:template match="party.surName"/>
  <xsl:template match="party.organizationName"/>
  <xsl:template match="party.currentName_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="party.contactInstructions"/>
  <xsl:template match="party.accessionCode"/>
  <xsl:template match="party.partyType"/>
  <xsl:template match="partyMember">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="partyMember.partyMember_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="partyMember.childParty_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="partyMember.role_ID/aux_Role/aux_Role.ROLE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="partyMember.memberStart"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="partyMember.memberStop"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="partyMember.partyMember_ID"/>
  <xsl:template match="partyMember.parentParty_ID"/>
  <xsl:template match="partyMember.childParty_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="partyMember.role_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="partyMember.memberStart"/>
  <xsl:template match="partyMember.memberStop"/>
  <xsl:template match="place">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="place.PLOTPLACE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../plot.PLOT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="place.calculated"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="place.NAMEDPLACE_ID/namedPlace/namedPlace.NAMEDPLACE_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="place.PLOTPLACE_ID"/>
  <xsl:template match="place.PLOT_ID"/>
  <xsl:template match="place.calculated"/>
  <xsl:template match="place.NAMEDPLACE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plot">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.PLOT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.authorPlotCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.PARENT_ID/plot/plot.PLOT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.realLatitude"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.realLongitude"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.locationAccuracy"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.confidentialityStatus"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.confidentialityReason"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.latitude"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.longitude"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.authorE"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.authorN"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.authorZone"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.authorDatum"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.authorLocation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.locationNarrative"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.plotRationaleNarrative"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.azimuth"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.dsgpoly"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.shape"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.area"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.standSize"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.placementMethod"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.permanence"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.layoutNarrative"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.elevation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.elevationAccuracy"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.elevationRange"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.slopeAspect"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.minSlopeAspect"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.maxSlopeAspect"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.slopeGradient"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.minSlopeGradient"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.maxSlopeGradient"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.topoPosition"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.landform"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.surficialDeposits"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.rockType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.accessionCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.notesPublic"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.notesMgt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="plot.revisions"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plot.PLOT_ID"/>
  <xsl:template match="plot.authorPlotCode"/>
  <xsl:template match="plot.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plot.PARENT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="plot.realLatitude"/>
  <xsl:template match="plot.realLongitude"/>
  <xsl:template match="plot.locationAccuracy"/>
  <xsl:template match="plot.confidentialityStatus"/>
  <xsl:template match="plot.confidentialityReason"/>
  <xsl:template match="plot.latitude"/>
  <xsl:template match="plot.longitude"/>
  <xsl:template match="plot.authorE"/>
  <xsl:template match="plot.authorN"/>
  <xsl:template match="plot.authorZone"/>
  <xsl:template match="plot.authorDatum"/>
  <xsl:template match="plot.authorLocation"/>
  <xsl:template match="plot.locationNarrative"/>
  <xsl:template match="plot.plotRationaleNarrative"/>
  <xsl:template match="plot.azimuth"/>
  <xsl:template match="plot.dsgpoly"/>
  <xsl:template match="plot.shape"/>
  <xsl:template match="plot.area"/>
  <xsl:template match="plot.standSize"/>
  <xsl:template match="plot.placementMethod"/>
  <xsl:template match="plot.permanence"/>
  <xsl:template match="plot.layoutNarrative"/>
  <xsl:template match="plot.elevation"/>
  <xsl:template match="plot.elevationAccuracy"/>
  <xsl:template match="plot.elevationRange"/>
  <xsl:template match="plot.slopeAspect"/>
  <xsl:template match="plot.minSlopeAspect"/>
  <xsl:template match="plot.maxSlopeAspect"/>
  <xsl:template match="plot.slopeGradient"/>
  <xsl:template match="plot.minSlopeGradient"/>
  <xsl:template match="plot.maxSlopeGradient"/>
  <xsl:template match="plot.topoPosition"/>
  <xsl:template match="plot.landform"/>
  <xsl:template match="plot.surficialDeposits"/>
  <xsl:template match="plot.rockType"/>
  <xsl:template match="plot.accessionCode"/>
  <xsl:template match="plot.notesPublic"/>
  <xsl:template match="plot.notesMgt"/>
  <xsl:template match="plot.revisions"/>
  <xsl:template match="project">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="project.PROJECT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="project.projectName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="project.projectDescription"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="project.startDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="project.stopDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="project.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="project.PROJECT_ID"/>
  <xsl:template match="project.projectName"/>
  <xsl:template match="project.projectDescription"/>
  <xsl:template match="project.startDate"/>
  <xsl:template match="project.stopDate"/>
  <xsl:template match="project.accessionCode"/>
  <xsl:template match="projectContributor">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="projectContributor.PROJECTCONTRIBUTOR_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../project.PROJECT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="projectContributor.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="projectContributor.ROLE_ID/aux_Role/aux_Role.ROLE_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="projectContributor.PROJECTCONTRIBUTOR_ID"/>
  <xsl:template match="projectContributor.PROJECT_ID"/>
  <xsl:template match="projectContributor.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="projectContributor.ROLE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="revision">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.REVISION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.tableName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.tableAttribute"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.tableRecord"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.revisionDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.previousValueText"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.previousValueType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="revision.previousRevision_ID/revision/revision.REVISION_ID"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="revision.REVISION_ID"/>
  <xsl:template match="revision.tableName"/>
  <xsl:template match="revision.tableAttribute"/>
  <xsl:template match="revision.tableRecord"/>
  <xsl:template match="revision.revisionDate"/>
  <xsl:template match="revision.previousValueText"/>
  <xsl:template match="revision.previousValueType"/>
  <xsl:template match="revision.previousRevision_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="soilObs">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.SOILOBS_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilHorizon"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilDepthTop"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilDepthBottom"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilColor"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilOrganic"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilTexture"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilSand"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilSilt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilClay"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilCoarse"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilPH"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.exchangeCapacity"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.baseSaturation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilObs.soilDescription"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="soilObs.SOILOBS_ID"/>
  <xsl:template match="soilObs.OBSERVATION_ID"/>
  <xsl:template match="soilObs.soilHorizon"/>
  <xsl:template match="soilObs.soilDepthTop"/>
  <xsl:template match="soilObs.soilDepthBottom"/>
  <xsl:template match="soilObs.soilColor"/>
  <xsl:template match="soilObs.soilOrganic"/>
  <xsl:template match="soilObs.soilTexture"/>
  <xsl:template match="soilObs.soilSand"/>
  <xsl:template match="soilObs.soilSilt"/>
  <xsl:template match="soilObs.soilClay"/>
  <xsl:template match="soilObs.soilCoarse"/>
  <xsl:template match="soilObs.soilPH"/>
  <xsl:template match="soilObs.exchangeCapacity"/>
  <xsl:template match="soilObs.baseSaturation"/>
  <xsl:template match="soilObs.soilDescription"/>
  <xsl:template match="soilTaxon">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilTaxon.SOILTAXON_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilTaxon.soilCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilTaxon.soilName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilTaxon.soilLevel"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilTaxon.SOILPARENT_ID/soilTaxon/soilTaxon.SOILTAXON_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilTaxon.soilFramework"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="soilTaxon.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="soilTaxon.SOILTAXON_ID"/>
  <xsl:template match="soilTaxon.soilCode"/>
  <xsl:template match="soilTaxon.soilName"/>
  <xsl:template match="soilTaxon.soilLevel"/>
  <xsl:template match="soilTaxon.SOILPARENT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="soilTaxon.soilFramework"/>
  <xsl:template match="soilTaxon.accessionCode"/>
  <xsl:template match="stemCount">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemCount.STEMCOUNT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../taxonImportance.taxonImportance_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemCount.stemDiameter"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemCount.stemDiameterAccuracy"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemCount.stemHeight"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemCount.stemHeightAccuracy"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemCount.stemCount"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemCount.stemTaxonArea"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="stemCount.STEMCOUNT_ID"/>
  <xsl:template match="stemCount.TAXONIMPORTANCE_ID"/>
  <xsl:template match="stemCount.stemDiameter"/>
  <xsl:template match="stemCount.stemDiameterAccuracy"/>
  <xsl:template match="stemCount.stemHeight"/>
  <xsl:template match="stemCount.stemHeightAccuracy"/>
  <xsl:template match="stemCount.stemCount"/>
  <xsl:template match="stemCount.stemTaxonArea"/>
  <xsl:template match="stemLocation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemLocation.STEMLOCATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../stemCount.STEMCOUNT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemLocation.stemCode"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemLocation.stemXPosition"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemLocation.stemYPosition"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stemLocation.stemHealth"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="stemLocation.STEMLOCATION_ID"/>
  <xsl:template match="stemLocation.STEMCOUNT_ID"/>
  <xsl:template match="stemLocation.stemCode"/>
  <xsl:template match="stemLocation.stemXPosition"/>
  <xsl:template match="stemLocation.stemYPosition"/>
  <xsl:template match="stemLocation.stemHealth"/>
  <xsl:template match="stratum">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratum.STRATUM_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">
        <xsl:choose>
          <xsl:when test="string-length(../observation.OBSERVATION_ID)&gt;0">
            <xsl:value-of select="../observation.OBSERVATION_ID"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="../../../../observation.OBSERVATION_ID"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratum.STRATUMTYPE_ID/stratumType/stratumType.STRATUMTYPE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratum.stratumHeight"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratum.stratumBase"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratum.stratumCover"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="stratum.STRATUM_ID"/>
  <xsl:template match="stratum.OBSERVATION_ID"/>
  <xsl:template match="stratum.STRATUMTYPE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="stratum.stratumHeight"/>
  <xsl:template match="stratum.stratumBase"/>
  <xsl:template match="stratum.stratumCover"/>
  <xsl:template match="stratumMethod">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumMethod.STRATUMMETHOD_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumMethod.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumMethod.stratumMethodName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumMethod.stratumMethodDescription"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumMethod.stratumAssignment"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumMethod.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="stratumMethod.STRATUMMETHOD_ID"/>
  <xsl:template match="stratumMethod.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="stratumMethod.stratumMethodName"/>
  <xsl:template match="stratumMethod.stratumMethodDescription"/>
  <xsl:template match="stratumMethod.stratumAssignment"/>
  <xsl:template match="stratumMethod.accessionCode"/>
  <xsl:template match="stratumType">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumType.STRATUMTYPE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">
        <xsl:choose>
          <xsl:when test="string-length(../stratumMethod.STRATUMMETHOD_ID)&gt;0">
            <xsl:value-of select="../stratumMethod.STRATUMMETHOD_ID"/>
          </xsl:when>
          <xsl:when test="string-length(../../../observation.STRATUMMETHOD_ID/stratumMethod/stratumMethod.STRATUMMETHOD_ID)&gt;0">
            <xsl:value-of select="../../../observation.STRATUMMETHOD_ID/stratumMethod/stratumMethod.STRATUMMETHOD_ID"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="../../../../../../observation.STRATUMMETHOD_ID/stratumMethod/stratumMethod.STRATUMMETHOD_ID"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumType.stratumIndex"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumType.stratumName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="stratumType.stratumDescription"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="stratumType.STRATUMTYPE_ID"/>
  <xsl:template match="stratumType.STRATUMMETHOD_ID"/>
  <xsl:template match="stratumType.stratumIndex"/>
  <xsl:template match="stratumType.stratumName"/>
  <xsl:template match="stratumType.stratumDescription"/>
  <xsl:template match="taxonImportance">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonImportance.taxonImportance_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../taxonObservation.TAXONOBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonImportance.stratum_ID/stratum/stratum.STRATUM_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonImportance.cover"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonImportance.basalArea"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonImportance.biomass"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonImportance.inferenceArea"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonImportance.taxonImportance_ID"/>
  <xsl:template match="taxonImportance.taxonObservation_ID"/>
  <xsl:template match="taxonImportance.stratum_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonImportance.cover"/>
  <xsl:template match="taxonImportance.basalArea"/>
  <xsl:template match="taxonImportance.biomass"/>
  <xsl:template match="taxonImportance.inferenceArea"/>
  <xsl:template match="taxonInterpretation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.TAXONINTERPRETATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text">
        <xsl:choose>
          <xsl:when test="string-length(../taxonObservation.TAXONOBSERVATION_ID)&gt;0">
            <xsl:value-of select="../taxonObservation.TAXONOBSERVATION_ID"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="../../../../taxonObservation.TAXONOBSERVATION_ID"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:with-param>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../stemLocation.STEMLOCATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.PLANTCONCEPT_ID/plantConcept/plantConcept.PLANTCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.interpretationDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.PLANTNAME_ID/plantName/plantName.PLANTNAME_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.PARTY_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.ROLE_ID/aux_Role/aux_Role.ROLE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.interpretationType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.originalInterpretation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.currentInterpretation"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.taxonFit"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.taxonConfidence"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.collector_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.collectionNumber"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.collectionDate"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.museum_ID/party/party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.museumAccessionNumber"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.groupType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.notes"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.notesPublic"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.notesMgt"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.revisions"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonInterpretation.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.TAXONINTERPRETATION_ID"/>
  <xsl:template match="taxonInterpretation.TAXONOBSERVATION_ID"/>
  <xsl:template match="taxonInterpretation.stemLocation_ID"/>
  <xsl:template match="taxonInterpretation.PLANTCONCEPT_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.interpretationDate"/>
  <xsl:template match="taxonInterpretation.PLANTNAME_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.PARTY_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.ROLE_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.interpretationType"/>
  <xsl:template match="taxonInterpretation.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.originalInterpretation"/>
  <xsl:template match="taxonInterpretation.currentInterpretation"/>
  <xsl:template match="taxonInterpretation.taxonFit"/>
  <xsl:template match="taxonInterpretation.taxonConfidence"/>
  <xsl:template match="taxonInterpretation.collector_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.collectionNumber"/>
  <xsl:template match="taxonInterpretation.collectionDate"/>
  <xsl:template match="taxonInterpretation.museum_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonInterpretation.museumAccessionNumber"/>
  <xsl:template match="taxonInterpretation.groupType"/>
  <xsl:template match="taxonInterpretation.notes"/>
  <xsl:template match="taxonInterpretation.notesPublic"/>
  <xsl:template match="taxonInterpretation.notesMgt"/>
  <xsl:template match="taxonInterpretation.revisions"/>
  <xsl:template match="taxonInterpretation.accessionCode"/>
  <xsl:template match="taxonObservation">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonObservation.TAXONOBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../observation.OBSERVATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonObservation.authorPlantName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonObservation.reference_ID/reference/reference.reference_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonObservation.taxonInferenceArea"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonObservation.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonObservation.TAXONOBSERVATION_ID"/>
  <xsl:template match="taxonObservation.OBSERVATION_ID"/>
  <xsl:template match="taxonObservation.authorPlantName"/>
  <xsl:template match="taxonObservation.reference_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonObservation.taxonInferenceArea"/>
  <xsl:template match="taxonObservation.accessionCode"/>
  <xsl:template match="taxonAlt">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonAlt.taxonAlt_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../taxonInterpretation.TAXONINTERPRETATION_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonAlt.plantConcept_ID/plantConcept/plantConcept.PLANTCONCEPT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonAlt.taxonAltFit"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonAlt.taxonAltConfidence"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="taxonAlt.taxonAltNotes"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonAlt.taxonAlt_ID"/>
  <xsl:template match="taxonAlt.taxonInterpretation_ID"/>
  <xsl:template match="taxonAlt.plantConcept_ID">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="taxonAlt.taxonAltFit"/>
  <xsl:template match="taxonAlt.taxonAltConfidence"/>
  <xsl:template match="taxonAlt.taxonAltNotes"/>
  <xsl:template match="telephone">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="telephone.TELEPHONE_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../party.PARTY_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="telephone.phoneNumber"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="telephone.phoneType"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="telephone.TELEPHONE_ID"/>
  <xsl:template match="telephone.PARTY_ID"/>
  <xsl:template match="telephone.phoneNumber"/>
  <xsl:template match="telephone.phoneType"/>
  <xsl:template match="userDefined">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="userDefined.USERDEFINED_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="userDefined.userDefinedName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="userDefined.userDefinedMetadata"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="userDefined.userDefinedCategory"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="userDefined.userDefinedType"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="userDefined.tableName"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="userDefined.accessionCode"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="userDefined.USERDEFINED_ID"/>
  <xsl:template match="userDefined.userDefinedName"/>
  <xsl:template match="userDefined.userDefinedMetadata"/>
  <xsl:template match="userDefined.userDefinedCategory"/>
  <xsl:template match="userDefined.userDefinedType"/>
  <xsl:template match="userDefined.tableName"/>
  <xsl:template match="userDefined.accessionCode"/>
  <xsl:template match="embargo">
    <xsl:value-of select="$LF"/>"d",<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="name()"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="embargo.embargo_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="../plot.PLOT_ID"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="embargo.embargoReason"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="embargo.embargoStart"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="embargo.embargoStop"/>
    </xsl:call-template>,<xsl:call-template name="csvIt">
      <xsl:with-param name="text" select="embargo.defaultStatus"/>
    </xsl:call-template>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null<xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="embargo.embargo_ID"/>
  <xsl:template match="embargo.plot_ID"/>
  <xsl:template match="embargo.embargoReason"/>
  <xsl:template match="embargo.embargoStart"/>
  <xsl:template match="embargo.embargoStop"/>
  <xsl:template match="embargo.defaultStatus"/>
</xsl:stylesheet>
