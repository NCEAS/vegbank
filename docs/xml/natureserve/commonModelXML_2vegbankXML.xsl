<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output omit-xml-declaration="yes" method="xml"/>
  <xsl:param name="alphahigh">QWERTYUIOPASDFGHJKLZXCVBNM</xsl:param>
  <xsl:param name="alphalow">qwertyuiopasdfghjklzxcvbnm</xsl:param>
  <xsl:template match="/VegBankPackage">
    <!-- <VegBankPackage> -->
    <!--  <doc-VegBankVersion>1.0.2</doc-VegBankVersion>
<doc-date>2004-04-23T03:04:31</doc-date>
<doc-author>Michael  Lee</doc-author>
<doc-authorSoftware>VegBranch, version: 1.2b</doc-authorSoftware>
<doc-comments>This is an example dataset for the 1.0.2. vegbank version xml.</doc-comments> -->
    <xsl:for-each select="entity[entity.entity_type_cd='C']">
      <xsl:call-template name="writeCommConcept"/>
    </xsl:for-each>
    <xsl:for-each select="entity[entity.entity_type_cd='P']">
      <xsl:call-template name="writePlantConcept"/>
    </xsl:for-each>
    <!--  </VegBankPackage>-->
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
      </plantStatus>
      <!-- ready!!-->
      <xsl:for-each select="entity_usage">
        <plantUsage>
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
          <xsl:if test="string-length(entity_usage.usage_start_date)&gt;0">
            <plantUsage.usageStop>
              <xsl:value-of select="entity_usage.usage_start_date"/>
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
          <plantUsage.PARTY_ID>
            <xsl:call-template name="writeParty"/>
          </plantUsage.PARTY_ID>
        </plantUsage>
        <!-- ready -->
      </xsl:for-each>
    </plantConcept>
    <!-- ready -->
  </xsl:template>
  <xsl:template name="writePlantName">
    <xsl:param name="NameEnt"/>
    <plantName>
      <xsl:if test="string-length($NameEnt/entity_name.entity_name_id)&gt;0">
        <plantName.PLANTNAME_ID>
          <xsl:value-of select="$NameEnt/entity_name.entity_name_id"/>
        </plantName.PLANTNAME_ID>
      </xsl:if>
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
        <xsl:value-of select="$RefEnt/reference.reference_id"/>
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
      </commStatus>
      <!-- ready!!-->
      <xsl:for-each select="entity_usage">
        <commUsage>
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
          <xsl:if test="string-length(entity_usage.usage_start_date)&gt;0">
            <commUsage.usageStop>
              <xsl:value-of select="entity_usage.usage_start_date"/>
            </commUsage.usageStop>
          </xsl:if>
          <commUsage.commNameStatus>
            <xsl:call-template name="writeUsageStatus">
              <xsl:with-param name="statusCode" select="entity_usage.name_status_cd"/>
            </xsl:call-template>
          </commUsage.commNameStatus>
          <xsl:if test="string-length(entity_usage.entity_name_id/entity_name/entity_name.name_system)&gt;0">
            <!-- dig to get system -->
            <commUsage.classSystem>
              <xsl:value-of select="entity_usage.entity_name_id/entity_name/entity_name.name_system"/>
            </commUsage.classSystem>
          </xsl:if>
          <commUsage.PARTY_ID>
            <xsl:call-template name="writeParty"/>
          </commUsage.PARTY_ID>
        </commUsage>
        <!-- ready -->
      </xsl:for-each>
    </commConcept>
    <!-- ready -->
  </xsl:template>
  <xsl:template name="writeCommName">
    <xsl:param name="NameEnt"/>
    <commName>
      <xsl:if test="string-length($NameEnt/entity_name.entity_name_id)&gt;0">
        <commName.COMMNAME_ID>
          <xsl:value-of select="$NameEnt/entity_name.entity_name_id"/>
        </commName.COMMNAME_ID>
      </xsl:if>
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
