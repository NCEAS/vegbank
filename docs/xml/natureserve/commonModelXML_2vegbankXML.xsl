<?xml version="1.0" encoding="UTF-8"?>
<!-- THIS File styles NatureServe - VegBank "Common Model" data into VegBank Package.  Currently only the initial load is handled, not updates.

*****************REQUIRED ***************************
You must first style the doc with writeStringsAndID.xsl and output to UniqueID.xml.  This will be read to create IDs for names and references
****************************************************
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml"/>
  <xsl:param name="alphahigh">QWERTYUIOPASDFGHJKLZXCVBNM</xsl:param>
  <xsl:param name="alphalow">qwertyuiopasdfghjklzxcvbnm</xsl:param>
  <!-- rules for filling in ID's: generated ID's from this doc will always be odd, incoming from NS will be even!  (names only) -->
  <xsl:template match="/VegBankPackage">
    <xsl:text disable-output-escaping="yes">&lt;VegBankPackage&gt;</xsl:text>
    <!-- allow to add an xsd automatically:   <xsl:text disable-output-escaping="yes">&lt;VegBankPackage xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                    xsi:noNamespaceSchemaLocation="cp_vegbank-data-ver1.0.2usage.xsd"&gt;</xsl:text> -->
    <doc-VegBankVersion>
      <xsl:value-of select="doc-VegBankVersion"/>
    </doc-VegBankVersion>
    <doc-date>
      <xsl:value-of select="doc-date"/>
    </doc-date>
    <doc-author>
      <xsl:value-of select="doc-author"/>
    </doc-author>
    <doc-authorSoftware>
      <xsl:value-of select="doc-authorSoftware"/>
    </doc-authorSoftware>
    <doc-comments>
      <xsl:value-of select="doc-comments"/>
    </doc-comments>
    <xsl:for-each select="entity[entity.entity_type_cd='C']">
      <xsl:call-template name="writeCommConcept"/>
    </xsl:for-each>
    <xsl:for-each select="entity[entity.entity_type_cd='P']">
      <xsl:call-template name="writePlantConcept"/>
    </xsl:for-each>
    <xsl:text disable-output-escaping="yes">&lt;/VegBankPackage&gt;</xsl:text>
  </xsl:template>
  <xsl:template match="entity[entity.entity_type_cd='P']" name="writePlantConcept">
    <plantConcept>
      <xsl:if test="string-length(entity.entity_id)&gt;0">
        <plantConcept.PLANTCONCEPT_ID>
          <xsl:value-of select="entity.entity_id"/>
        </plantConcept.PLANTCONCEPT_ID>
      </xsl:if>
      <plantConcept.PLANTNAME_ID>
        <xsl:call-template name="writePlantName">
          <xsl:with-param name="NameEnt" select="entity.circ_name_id/entity_name"/>
        </xsl:call-template>
      </plantConcept.PLANTNAME_ID>
      <plantConcept.reference_ID>
        <xsl:call-template name="writeReference">
          <xsl:with-param name="RefEnt" select="entity.circ_ref_id/reference"/>
        </xsl:call-template>
      </plantConcept.reference_ID>
      <plantConcept.plantDescription>
        <xsl:value-of select="entity.entity_desc"/>
      </plantConcept.plantDescription>
      <!-- start plant status -->
      <plantStatus>
        <plantStatus.PLANTSTATUS_ID>
          <xsl:value-of select="entity.entity_id"/>
        </plantStatus.PLANTSTATUS_ID>
        <plantStatus.plantConceptStatus>
          <xsl:value-of select="entity.classification_status"/>
        </plantStatus.plantConceptStatus>
        <xsl:if test="string-length(entity.classification_start_date)&gt;0">
          <plantStatus.startDate>
            <xsl:value-of select="entity.classification_start_date"/>
          </plantStatus.startDate>
        </xsl:if>
        <xsl:if test="string-length(entity.classification_stop_date)&gt;0">
          <plantStatus.stopDate>
            <xsl:value-of select="entity.classification_stop_date"/>
          </plantStatus.stopDate>
        </xsl:if>
        <plantStatus.plantPartyComments>
          <xsl:value-of select="entity.classification_com"/>
        </plantStatus.plantPartyComments>
        <xsl:for-each select="entity.classification_parent_id">
          <plantStatus.plantParent_ID>
            <xsl:apply-templates/>
          </plantStatus.plantParent_ID>
        </xsl:for-each>
        <plantStatus.plantLevel>
          <xsl:value-of select="entity.classification_level"/>
        </plantStatus.plantLevel>
        <plantStatus.PARTY_ID>
          <xsl:call-template name="writeParty"/>
        </plantStatus.PARTY_ID>
        <xsl:for-each select="entity_lineage">
          <plantCorrelation>
            <xsl:for-each select="entity_lineage.successor_id/entity">
              <plantCorrelation.PLANTCONCEPT_ID>
                <xsl:call-template name="writePlantConcept"/>
              </plantCorrelation.PLANTCONCEPT_ID>
            </xsl:for-each>
            <plantCorrelation.plantConvergence>
              <xsl:value-of select="entity_lineage.convergence"/>
            </plantCorrelation.plantConvergence>
            <plantCorrelation.correlationStart>
              <xsl:value-of select="entity_lineage.lineage_start_date"/>
            </plantCorrelation.correlationStart>
          </plantCorrelation>
        </xsl:for-each>
        <!-- ready!!-->
        <xsl:for-each select="entity_usage">
          <plantUsage>
            <plantUsage.PLANTUSAGE_ID>
              <xsl:value-of select="../entity.entity_id"/>
              <xsl:value-of select="number(1+position())"/>
            </plantUsage.PLANTUSAGE_ID>
            <!-- only for new data! -->
            <plantUsage.PLANTNAME_ID>
              <xsl:call-template name="writePlantName">
                <xsl:with-param name="NameEnt" select="entity_usage.entity_name_id/entity_name"/>
              </xsl:call-template>
            </plantUsage.PLANTNAME_ID>
            <xsl:if test="string-length(entity_usage.usage_start_date)&gt;0">
              <plantUsage.usageStart>
                <xsl:value-of select="entity_usage.usage_start_date"/>
              </plantUsage.usageStart>
            </xsl:if>
            <xsl:if test="string-length(entity_usage.usage_stop_date)&gt;0">
              <plantUsage.usageStop>
                <xsl:value-of select="entity_usage.usage_stop_date"/>
              </plantUsage.usageStop>
            </xsl:if>
            <plantUsage.plantNameStatus>
              <xsl:call-template name="writeUsageStatus">
                <xsl:with-param name="statusCode" select="entity_usage.name_status_cd"/>
              </xsl:call-template>
            </plantUsage.plantNameStatus>
            <xsl:if test="string-length(entity_usage.entity_name_id/entity_name/entity_name.name_system)&gt;0">
              <!-- dig to get system -->
              <plantUsage.classSystem>
                <xsl:value-of select="entity_usage.entity_name_id/entity_name/entity_name.name_system"/>
              </plantUsage.classSystem>
            </xsl:if>
            <!-- <plantUsage.PARTY_ID>
            <xsl:call-template name="writeParty"/>
          </plantUsage.PARTY_ID> -->
          </plantUsage>
          <!-- ready -->
        </xsl:for-each>
        <!-- ready write also the UID as a usage. -->
        <plantUsage>
          <plantUsage.PLANTUSAGE_ID>
            <xsl:value-of select="entity.entity_ID"/>0</plantUsage.PLANTUSAGE_ID>
          <plantUsage.PLANTNAME_ID>
            <plantName>
              <plantName.PLANTNAME_ID>
                <xsl:variable name="thisPUID" select="entity.ns_uid"/>
                <xsl:value-of select="document('UniqueID.xml')/iddoc/NAME[value=$thisPUID]/id"/>
              </plantName.PLANTNAME_ID>
              <plantName.plantName>
                <xsl:value-of select="entity.ns_uid"/>
              </plantName.plantName>
            </plantName>
          </plantUsage.PLANTNAME_ID>
          <plantUsage.usageStart>
            <xsl:value-of select="/VegBankPackage/doc-date"/>
          </plantUsage.usageStart>
          <plantUsage.plantNameStatus>
            <xsl:call-template name="writeUsageStatus">
              <xsl:with-param name="statusCode">S</xsl:with-param>
              <!-- hard code standard name status for NS UID -->
            </xsl:call-template>
          </plantUsage.plantNameStatus>
          <plantUsage.classSystem>UID</plantUsage.classSystem>
        </plantUsage>
      </plantStatus>
    </plantConcept>
    <!-- ready -->
  </xsl:template>
  <xsl:template name="writePlantName">
    <xsl:param name="NameEnt"/>
    <plantName>
      <plantName.PLANTNAME_ID>
        <xsl:variable name="thisPNm" select="$NameEnt/entity_name.entity_name"/>
        <xsl:value-of select="document('UniqueID.xml')/iddoc/NAME[value=$thisPNm]/id"/>
      </plantName.PLANTNAME_ID>
      <plantName.plantName>
        <xsl:value-of select="$NameEnt/entity_name.entity_name"/>
      </plantName.plantName>
      <xsl:if test="count($NameEnt/entity_name.ref_id/reference)&gt;0">
        <plantName.reference_ID>
          <xsl:call-template name="writeReference">
            <xsl:with-param name="RefEnt" select="$NameEnt/entity_name.ref_id/reference"/>
          </xsl:call-template>
        </plantName.reference_ID>
      </xsl:if>
    </plantName>
    <!-- plantName is done -->
  </xsl:template>
  <xsl:template name="writeReference">
    <xsl:param name="RefEnt"/>
    <reference>
      <reference.reference_ID>
        <xsl:variable name="thisRefNm" select="$RefEnt/reference.full_citation"/>
        <xsl:value-of select="document('UniqueID.xml')/iddoc/REF[value=$thisRefNm]/id"/>
      </reference.reference_ID>
      <reference.fulltext>
        <xsl:value-of select="$RefEnt/reference.full_citation"/>
      </reference.fulltext>
    </reference>
  </xsl:template>
  <xsl:template name="writeParty">
    <party>
      <party.accessionCode>VB.Py.512.NATURESERVE</party.accessionCode>
    </party>
  </xsl:template>
  <xsl:template match=" doc-VegBankVersion | doc-date |	
doc-author	|
doc-authorSoftware |
doc-comments	"/>
  <xsl:template name="writeUsageStatus">
    <xsl:param name="statusCode"/>
    <xsl:choose>
      <xsl:when test="$statusCode='S'">standard</xsl:when>
      <xsl:when test="$statusCode='N'">nonstandard</xsl:when>
      <xsl:when test="$statusCode='U'">undetermined</xsl:when>
      <xsl:otherwise>UNDERTERMINED</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!-- ####################### COMM -->
  <xsl:template match="entity[entity.entity_type_cd='C']" name="writeCommConcept">
    <commConcept>
      <xsl:if test="string-length(entity.entity_id)&gt;0">
        <commConcept.COMMCONCEPT_ID>
          <xsl:value-of select="entity.entity_id"/>
        </commConcept.COMMCONCEPT_ID>
      </xsl:if>
      <commConcept.COMMNAME_ID>
        <xsl:call-template name="writeCommName">
          <xsl:with-param name="NameEnt" select="entity.circ_name_id/entity_name"/>
        </xsl:call-template>
      </commConcept.COMMNAME_ID>
      <commConcept.reference_ID>
        <xsl:call-template name="writeReference">
          <xsl:with-param name="RefEnt" select="entity.circ_ref_id/reference"/>
        </xsl:call-template>
      </commConcept.reference_ID>
      <commConcept.commDescription>
        <xsl:value-of select="entity.entity_desc"/>
      </commConcept.commDescription>
      <!-- start comm status -->
      <commStatus>
        <commStatus.COMMSTATUS_ID>
          <xsl:value-of select="entity.entity_id"/>
        </commStatus.COMMSTATUS_ID>
        <!-- temp only for NEW data -->
        <commStatus.commConceptStatus>
          <xsl:value-of select="entity.classification_status"/>
        </commStatus.commConceptStatus>
        <xsl:for-each select="entity.classification_parent_id">
          <commStatus.commParent_ID>
            <xsl:apply-templates/>
          </commStatus.commParent_ID>
        </xsl:for-each>
        <commStatus.commLevel>
          <xsl:value-of select="entity.classification_level"/>
        </commStatus.commLevel>
        <xsl:if test="string-length(entity.classification_start_date)&gt;0">
          <commStatus.startDate>
            <xsl:value-of select="entity.classification_start_date"/>
          </commStatus.startDate>
        </xsl:if>
        <xsl:if test="string-length(entity.classification_stop_date)&gt;0">
          <commStatus.stopDate>
            <xsl:value-of select="entity.classification_stop_date"/>
          </commStatus.stopDate>
        </xsl:if>
        <commStatus.commPartyComments>
          <xsl:value-of select="entity.classification_com"/>
        </commStatus.commPartyComments>
        <commStatus.PARTY_ID>
          <xsl:call-template name="writeParty"/>
        </commStatus.PARTY_ID>
        <xsl:for-each select="entity_lineage">
          <commCorrelation>
            <xsl:for-each select="entity_lineage.successor_id/entity">
              <commCorrelation.COMMCONCEPT_ID>
                <xsl:call-template name="writeCommConcept"/>
              </commCorrelation.COMMCONCEPT_ID>
            </xsl:for-each>
            <commCorrelation.commConvergence>
              <xsl:value-of select="entity_lineage.convergence"/>
            </commCorrelation.commConvergence>
            <commCorrelation.correlationStart>
              <xsl:value-of select="entity_lineage.lineage_start_date"/>
            </commCorrelation.correlationStart>
          </commCorrelation>
        </xsl:for-each>
        <!-- ready!!-->
        <xsl:for-each select="entity_usage">
          <commUsage>
            <commUsage.COMMUSAGE_ID>
              <xsl:value-of select="../entity.entity_id"/>
              <xsl:value-of select="number(1+position())"/>
            </commUsage.COMMUSAGE_ID>
            <!-- only for new data! -->
            <commUsage.COMMNAME_ID>
              <xsl:call-template name="writeCommName">
                <xsl:with-param name="NameEnt" select="entity_usage.entity_name_id/entity_name"/>
              </xsl:call-template>
            </commUsage.COMMNAME_ID>
            <xsl:if test="string-length(entity_usage.usage_start_date)&gt;0">
              <commUsage.usageStart>
                <xsl:value-of select="entity_usage.usage_start_date"/>
              </commUsage.usageStart>
            </xsl:if>
            <xsl:if test="string-length(entity_usage.usage_stop_date)&gt;0">
              <commUsage.usageStop>
                <xsl:value-of select="entity_usage.usage_stop_date"/>
              </commUsage.usageStop>
            </xsl:if>
            <commUsage.commNameStatus>
              <xsl:call-template name="writeUsageStatus">
                <xsl:with-param name="statusCode" select="entity_usage.name_status_cd"/>
              </xsl:call-template>
            </commUsage.commNameStatus>
            <!-- dig to get system -->
            <commUsage.classSystem>
              <xsl:choose>
                <!-- special rules to help these values match our lists -->
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='CEGL'">Code</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='NVC'">Scientific</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='English Common'">Common</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='key'">Code</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='CODE'">Code</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='Code'">Code</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='code'">Code</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='elcode'">Code</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='scientific name without author'">Scientific</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='English common name'">Common</xsl:when>
                <xsl:when test="entity_usage.entity_name_id/entity_name/entity_name.name_system='translated name'">Translated</xsl:when>
                <xsl:when test="string-length(entity_usage.entity_name_id/entity_name/entity_name.name_system)=0">Other</xsl:when>
                <xsl:otherwise>
                  <!-- default -->
                  <xsl:value-of select="entity_usage.entity_name_id/entity_name/entity_name.name_system"/>
                </xsl:otherwise>
              </xsl:choose>
            </commUsage.classSystem>
            <!--    <commUsage.PARTY_ID>
            <xsl:call-template name="writeParty"/>
          </commUsage.PARTY_ID> -->
          </commUsage>
          <!-- ready -->
        </xsl:for-each>
        <!-- ready -->
        <!-- ready write also the UID as a usage. -->
        <commUsage>
          <commUsage.COMMUSAGE_ID>
            <xsl:value-of select="entity.entity_id"/>0</commUsage.COMMUSAGE_ID>
          <commUsage.COMMNAME_ID>
            <commName>
              <commName.COMMNAME_ID>
                <xsl:variable name="thisCUID" select="entity.ns_uid"/>
                <xsl:value-of select="document('UniqueID.xml')/iddoc/NAME[value=$thisCUID]/id"/>
              </commName.COMMNAME_ID>
              <commName.commName>
                <xsl:value-of select="entity.ns_uid"/>
              </commName.commName>
            </commName>
          </commUsage.COMMNAME_ID>
          <commUsage.usageStart>
            <xsl:value-of select="/VegBankPackage/doc-date"/>
          </commUsage.usageStart>
          <commUsage.commNameStatus>
            <xsl:call-template name="writeUsageStatus">
              <xsl:with-param name="statusCode">S</xsl:with-param>
              <!-- hard code standard name status for NS UID -->
            </xsl:call-template>
          </commUsage.commNameStatus>
          <commUsage.classSystem>UID</commUsage.classSystem>
        </commUsage>
      </commStatus>
    </commConcept>
    <!-- ready -->
  </xsl:template>
  <xsl:template name="writeCommName">
    <xsl:param name="NameEnt"/>
    <commName>
      <commName.COMMNAME_ID>
        <xsl:variable name="thisCNm" select="$NameEnt/entity_name.entity_name"/>
        <xsl:value-of select="document('UniqueID.xml')/iddoc/NAME[value=$thisCNm]/id"/>
      </commName.COMMNAME_ID>
      <commName.commName>
        <xsl:value-of select="$NameEnt/entity_name.entity_name"/>
      </commName.commName>
      <xsl:if test="count($NameEnt/entity_name.ref_id/reference)&gt;0">
        <commName.reference_ID>
          <xsl:call-template name="writeReference">
            <xsl:with-param name="RefEnt" select="$NameEnt/entity_name.ref_id/reference"/>
          </xsl:call-template>
        </commName.reference_ID>
      </xsl:if>
    </commName>
    <!-- commName is done -->
  </xsl:template>
</xsl:stylesheet>
