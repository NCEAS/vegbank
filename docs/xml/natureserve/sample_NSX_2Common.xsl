<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    
    <xsl:template match="/">
        <VegBankPackage>
                <xsl:apply-templates/>
        </VegBankPackage>
    </xsl:template>
    
    <xsl:template match="Table[@name='ELEMENT_GLOBAL']/Records/Record">
        <xsl:variable name="gnamePath" select="Fields/Field[@name='GNAME_ID']/Table/Records/Record/Fields"/>
        <xsl:variable name="typeCd" select="$gnamePath/Field[@name='D_NAME_CATEGORY_ID']/Table/Records/Record"/>
        <xsl:variable name="classLevel" select="$gnamePath/Field[@name='D_CLASSIFICATION_LEVEL_ID']/Table/Records/Record"/>
        <entity>
            <entity.entity_id><xsl:value-of select="@id"/></entity.entity_id>
            <xsl:apply-templates select="Fields/Field[@name='CONCEPT_NAME_ID']"/>
            <xsl:apply-templates select="Fields/Field[@name='CONCEPT_REFERENCE_ID']"/>
            <xsl:apply-templates select="Fields/Field[@name='G_CLASSIFICATION_COM']"/>
            <xsl:apply-templates select="$classLevel"/>
            <xsl:apply-templates select="ChildTables/Table[@name='TAXON_GLOBAL']/Records/Record/Fields/Field[@name='PARENT_SPECIES_ID']"/>
            <xsl:apply-templates select="Fields/Field[@name='HIGHER_CLASS_UNIT_ID']"/>
            <xsl:apply-templates select="Fields/Field[@name='ADD_TO_STD_DATE']"/>
            <xsl:call-template name="class_status">
                <xsl:with-param name="indicator" select="Fields/Field[@name='INACTIVE_IND']"/>
                <xsl:with-param name="cstatus" select="Fields/Field[@name='D_CLASSIFICATION_STATUS_ID']/Table/Records/Record/Fields/Field[@name='CLASSIFICATION_STATUS_DESC']"/>
            </xsl:call-template>
            <xsl:apply-templates select="ChildTables/Table[@name='COMMUNITY_GLOBAL']/Records/Record/Fields/Field[@name='G_ELEMENT_SUMMARY']"/>
            <xsl:apply-templates select="$typeCd"/>
            <xsl:comment>NOTICE: the final uid format is still pending...</xsl:comment>
            <entity.ns_uid>
                <xsl:value-of select="'ELEMENT_GLOBAL'"/>.<xsl:value-of select="Fields/Field[@name='ELEMENT_GLOBAL_OU_UID']"/>.<xsl:value-of select="Fields/Field[@name='ELEMENT_GLOBAL_SEQ_UID']"/>
            </entity.ns_uid>
            <xsl:apply-templates select="Fields/Field[@name='ELCODE_BCD']"/>
            <xsl:apply-templates select="Fields/Field[@name='GNAME_ID']">
                <xsl:with-param name="typeCd" select="$typeCd/Fields/Field[@name='NAME_TYPE_CD']"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="Fields/Field[@name='G_PRIMARY_COMMON_NAME']">
                <xsl:with-param name="typeCd" select="$typeCd/Fields/Field[@name='NAME_TYPE_CD']"/>
            </xsl:apply-templates>
        </entity>
    </xsl:template>
    
    <xsl:template match="ChildTables/Table[@name='TAXON_GLOBAL']/Records/Record/Fields/Field[@name='PARENT_SPECIES_ID']">
        <xsl:if test="@rawValue">
            <entity.classification_parent_id>
                <xsl:apply-templates/>
            </entity.classification_parent_id>
        </xsl:if>
    </xsl:template>

    <xsl:template match="Fields/Field[@name='HIGHER_CLASS_UNIT_ID']">
        <xsl:if test="@rawValue">
            <entity.classification_parent_id>
                <xsl:apply-templates/>
            </entity.classification_parent_id>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="Table[@name='HIGHER_CLASS_UNIT']/Records/Record">
        <entity>
            <xsl:variable name="alliancePath" select="ChildTables/Table[@name='HIGHER_COMMUNITY']/Records/Record/ChildTables/Table[@name='ALLIANCE']/Records/Record"/>
            <entity.entity_id><xsl:value-of select="@id"/></entity.entity_id>
            <xsl:apply-templates select="Fields/Field[@name='HIGHER_CLASS_UNIT_NAME']"/>
            <xsl:apply-templates select="$alliancePath/Fields/Field[@name='OPTIONAL_1']" mode="circ_ref"/>
            <xsl:apply-templates select="$alliancePath/Fields/Field[@name='CLASSIFICATION_COM']"/>
            <xsl:apply-templates select="Fields/Field[@name='D_CLASSIFICATION_LEVEL_ID']/Table/Records/Record"/>
            <!-- no parent classification unit for alliances -->
            <xsl:apply-templates select="$alliancePath/Fields/Field[@name='ALLIANCE_ADD_TO_STD_DATE']"/>
            <xsl:call-template name="class_status">
                <xsl:with-param name="indicator" select="$alliancePath/Fields/Field[@name='INACTIVE_IND']"/>
                <xsl:with-param name="cstatus" select="$alliancePath/Fields/Field[@name='D_CLASSIFICATION_STATUS_ID']/Table/Records/Record/Fields/Field[@name='CLASSIFICATION_STATUS_DESC']"/>
            </xsl:call-template>
            <xsl:apply-templates select="$alliancePath/Fields/Field[@name='ALLIANCE_SUMMARY']"/>
            <entity.entity_type_cd><xsl:value-of select="'C'"/></entity.entity_type_cd>            
            <xsl:comment>NOTICE: the final uid format is still pending...</xsl:comment>
            <entity.ns_uid>
                <xsl:value-of select="'HIGHER_CLASS_UNIT'"/>.<xsl:value-of select="Fields/Field[@name='HIGHER_CLASS_UNIT_OU_UID']"/>.<xsl:value-of select="Fields/Field[@name='HIGHER_CLASS_UNIT_SEQ_UID']"/>
            </entity.ns_uid>
            <xsl:apply-templates select="$alliancePath/Fields/Field[@name='ALLIANCE_KEY_BCD']"/>
            <xsl:apply-templates select="$alliancePath/Fields/Field[@name='TRANSLATED_NAME']" mode="translated_name"/>
        </entity>
    </xsl:template>
    
    <xsl:template match="Field/Table[@name='SCIENTIFIC_NAME']/Records/Record">
        <entity_name>
            <entity_name.entity_name_id><xsl:value-of select="@id"/></entity_name.entity_name_id>
            <xsl:apply-templates select="ChildTables/Table[@name='SCIENTIFIC_NAME_REF']"/>
            <xsl:apply-templates select="Fields/Field['SCIENTIFIC_NAME']"/>
            <entity_name.name_system>SCIENTIFIC_NAME</entity_name.name_system>
        </entity_name>
    </xsl:template>
    
    <xsl:template match="Field/Table[@name='SCIENTIFIC_NAME']/Records/Record" mode="with_author">
        <entity_name>
            <entity_name.entity_name_id><xsl:value-of select="@id"/></entity_name.entity_name_id>
            <xsl:apply-templates select="ChildTables/Table[@name='SCIENTIFIC_NAME_REF']"/>
            <xsl:variable name="author_name" select="Fields/Field[@name='AUTHOR_NAME']"/>
            <xsl:choose>
                <xsl:when test="string-length($author_name)>0">
                    <entity_name.entity_name>
                        <xsl:value-of select="Fields/Field[@name='SCIENTIFIC_NAME']"/>
                        <xsl:value-of select="' '"/>
                        <xsl:value-of select="Fields/Field[@name='AUTHOR_NAME']"/>
                    </entity_name.entity_name>
                    <entity_name.name_system>SCIENTIFIC_NAME AUTHOR_NAME</entity_name.name_system>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:comment>NOTICE: this SCIENTIFIC_NAME row has no AUTHOR_NAME value</xsl:comment>
                    <xsl:apply-templates select="Fields/Field['SCIENTIFIC_NAME']"/>
                    <entity_name.name_system>SCIENTIFIC_NAME</entity_name.name_system>
                </xsl:otherwise>
            </xsl:choose>
        </entity_name>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='SCIENTIFIC_NAME']">
        <entity_name.entity_name>
            <xsl:value-of select="."/>
        </entity_name.entity_name>
    </xsl:template>
    
    <xsl:template match="ChildTables/Table[@name='SCIENTIFIC_NAME_REF']">
        <entity_name.ref_id>
            <xsl:for-each select="Records/Record">
                <xsl:variable name="indicator" select="Fields/Field[@name='PRIMARY_NAME_REF_IND']"/>
                <xsl:if test="$indicator='Y'">
                    <xsl:apply-templates select="Fields/Field/Table[@name='REFERENCE']"/>
                </xsl:if>
            </xsl:for-each>
        </entity_name.ref_id>
    </xsl:template>
    
    <xsl:template match="Field/Table[@name='REFERENCE']">
        <reference>
            <reference.reference_id>
                <xsl:value-of select="Records/Record/Fields/Field[@name='REFERENCE_ID']"/>
            </reference.reference_id>
            <reference.full_citation>
                <xsl:value-of select="Records/Record/Fields/Field[@name='FULL_CITATION']"/>
            </reference.full_citation>
        </reference>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='CONCEPT_NAME_ID']">
        <entity.circ_name_id>
            <xsl:apply-templates mode="with_author"/>
        </entity.circ_name_id>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='HIGHER_CLASS_UNIT_NAME']">
        <xsl:variable name="alliancePath" select="ancestor::*[position()=2]/ChildTables/Table[@name='HIGHER_COMMUNITY']/Records/Record/ChildTables/Table[@name='ALLIANCE']/Records/Record"/>
        <entity.circ_name_id>
            <entity_name>
                <entity_name.entity_name_id><xsl:value-of select="ancestor::*[position()=2]/@id"/></entity_name.entity_name_id>
                <xsl:apply-templates select="$alliancePath/Fields/Field[@name='OPTIONAL_1']" mode="name_ref"/>
                <entity_name.entity_name>
                    <xsl:value-of select="."/>
                </entity_name.entity_name>
                <entity_name.name_system>HIGHER_CLASS_UNIT_NAME</entity_name.name_system>
            </entity_name>
        </entity.circ_name_id>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='OPTIONAL_1']" mode="circ_ref">
        <entity.circ_ref_id>
            <xsl:apply-templates/>
        </entity.circ_ref_id>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='OPTIONAL_1']" mode="name_ref">
        <entity_name.ref_id>
            <xsl:apply-templates/>
        </entity_name.ref_id>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='CONCEPT_REFERENCE_ID']">
        <entity.circ_ref_id>
            <xsl:apply-templates/>
        </entity.circ_ref_id>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='GNAME_ID']">
        <xsl:param name="name_status_cd" select="'S'"/>
        <xsl:param name="start_date" select="0=1"/>
        <xsl:param name="typeCd"/>
        <xsl:variable name="author" select="Table[@name='SCIENTIFIC_NAME']/Records/Record/Fields/Field[@name='AUTHOR_NAME']"/>
        <entity_usage>
            <xsl:comment> gname as scientific name </xsl:comment>
            <entity_usage.entity_name_id>
                <xsl:apply-templates/>
            </entity_usage.entity_name_id>
            <xsl:call-template name="usage_without_name">
                <xsl:with-param name="name_status_cd" select="$name_status_cd"/>
                <xsl:with-param name="start_date" select="$start_date"/>
            </xsl:call-template>
        </entity_usage>
        <!-- do only for plants, and maybe just those that have an author_name value to begin with -->
        <xsl:if test="$typeCd='P' and string-length($author)>0">
            <entity_usage>
                <xsl:comment> gname as scientific name + author </xsl:comment>
                <entity_usage.entity_name_id>
                    <xsl:apply-templates mode="with_author"/>
                </entity_usage.entity_name_id>
                <xsl:call-template name="usage_without_name">
                    <xsl:with-param name="name_status_cd" select="$name_status_cd"/>
                    <xsl:with-param name="start_date" select="$start_date"/>
                </xsl:call-template>
            </entity_usage>
        </xsl:if>
        <!-- do only for communities -->
        <xsl:if test="$typeCd='C'">
             <xsl:apply-templates select="Table/Records/Record/Fields/Field[@name='TRANSLATED_NAME']" mode="translated_name"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='G_CLASSIFICATION_COM']">
        <entity.classification_com>
            <xsl:apply-templates/>
        </entity.classification_com>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='CLASSIFICATION_COM']">
        <entity.classification_com>
            <xsl:apply-templates/>
        </entity.classification_com>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='G_ELEMENT_SUMMARY']">
        <entity.entity_desc>
            <xsl:apply-templates/>
        </entity.entity_desc>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='ALLIANCE_SUMMARY']">
        <entity.entity_desc>
            <xsl:apply-templates/>
        </entity.entity_desc>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='CLASSIFICATION_LEVEL_NAME']">
        <entity.classification_level>
            <xsl:apply-templates/>
        </entity.classification_level>
    </xsl:template>

    <xsl:template match="Fields/Field[@name='ADD_TO_STD_DATE']">
        <entity.classification_start_date>
            <xsl:apply-templates/>
        </entity.classification_start_date>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='ALLIANCE_ADD_TO_STD_DATE']">
        <entity.classification_start_date>
            <xsl:apply-templates/>
        </entity.classification_start_date>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='NAME_TYPE_CD']">
        <entity.entity_type_cd><xsl:apply-templates/></entity.entity_type_cd>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='ALLIANCE_KEY_BCD']">
        <xsl:call-template name="usage_with_name">
            <xsl:with-param name="name_system" select="'ALLIANCE_KEY_BCD'"/>
            <xsl:with-param name="name_value" select="."/>
        </xsl:call-template>
    </xsl:template>
    
    <!-- the "translated_name" mode is used to target which translated name field can be processed here -->
    <xsl:template match="Fields/Field[@name='TRANSLATED_NAME']" mode="translated_name">
        <xsl:call-template name="usage_with_name">
            <xsl:with-param name="name_system" select="'TRANSLATED_NAME'"/>
            <xsl:with-param name="name_value" select="."/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='ELCODE_BCD']">
        <xsl:call-template name="usage_with_name">
            <xsl:with-param name="name_system" select="'ELCODE_BCD'"/>
            <xsl:with-param name="name_value" select="."/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="Fields/Field[@name='G_PRIMARY_COMMON_NAME']">
        <xsl:param name="typeCd"/>
        <xsl:variable name="primaryCommonName" select="."/>
        <xsl:choose>
            <xsl:when test="$typeCd='C' or ancestor::*[position()=1]/Field[@name='ACCEPTED_BY_ECOLOGY_IND']='Y'">
                <xsl:if test="string-length($primaryCommonName)>0">
                    <xsl:call-template name="usage_with_name">
                        <xsl:with-param name="name_system" select="'G_PRIMARY_COMMON_NAME'"/>
                        <xsl:with-param name="name_value" select="."/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="otherCommonName">
                    <xsl:call-template name="otherGlobalCommonName"/>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="string-length($otherCommonName)>0">
                        <xsl:call-template name="usage_with_name">
                            <xsl:with-param name="name_system" select="'OTHER_GLOBAL_COMMON_NAME'"/>
                            <xsl:with-param name="name_value" select="$otherCommonName"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="string-length($primaryCommonName)>0">
                        <xsl:call-template name="usage_with_name">
                            <xsl:with-param name="name_system" select="'G_PRIMARY_COMMON_NAME'"/>
                            <xsl:with-param name="name_value" select="$primaryCommonName"/>
                        </xsl:call-template>
                    </xsl:when>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="otherGlobalCommonName">
        <xsl:param name="path" select="ancestor::*[position()=2]/ChildTables/Table[@name='OTHER_GLOBAL_COMMON_NAME']"/>
        <xsl:for-each select="$path/Records/Record">
            <xsl:variable name="indicator" select="Fields/Field[@name='ACCEPTED_BY_ECOLOGY_IND']"/>
            <xsl:if test="$indicator='Y'">
                <xsl:value-of select="Fields/Field[@name='OTHER_GLOBAL_COMMON_NAME']"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="Field"/>
    
    <xsl:template name="class_status">
        <xsl:param name="indicator"/>
        <xsl:param name="cstatus"/>
        <entity.classification_status>
            <xsl:choose>
                <xsl:when test="$indicator='Y'">not accepted</xsl:when>
                <xsl:when test="$cstatus='Standard'">accepted</xsl:when>
                <xsl:otherwise>not accepted</xsl:otherwise>
            </xsl:choose>
        </entity.classification_status>
    </xsl:template>
    
    <!-- the "entity_name" template is for non-scientific-name names -->
    <xsl:template name="entity_name">
        <xsl:param name="id" select="0=1"/>
        <xsl:param name="name_system"/>
        <xsl:param name="name_value"/>
        <xsl:param name="nameref"/>
        <entity_name>
            <xsl:if test="$id">
                <entity_name.entity_name_id><xsl:value-of select="$id"/></entity_name.entity_name_id>
            </xsl:if>
            <entity_name.name_system><xsl:value-of select="$name_system"/></entity_name.name_system>
            <entity_name.entity_name><xsl:value-of select="$name_value"/></entity_name.entity_name>
            <xsl:if test="$nameref">
                <entity_name.ref_id>
                    <xsl:apply-templates select="$nameref/Table[@name='REFERENCE']"/>
                </entity_name.ref_id>
            </xsl:if>
        </entity_name>
    </xsl:template>
    
    <!-- the "usage_with_name" template is for non-scientific-name names/usages -->
    <xsl:template name="usage_with_name">
        <xsl:param name="name_system"/>
        <xsl:param name="name_value"/>
        <xsl:param name="name_status_cd" select="'S'"/>
        <xsl:param name="start_date" select="0=1"/>
        <entity_usage>
            <entity_usage.entity_name_id>
                <xsl:call-template name="entity_name">
                    <xsl:with-param name="name_system" select="$name_system"/>
                    <xsl:with-param name="name_value" select="$name_value"/>
                </xsl:call-template>
            </entity_usage.entity_name_id>
            <xsl:call-template name="usage_without_name">
                <xsl:with-param name="name_status_cd" select="$name_status_cd"/>
                <xsl:with-param name="start_date" select="$start_date"/>
            </xsl:call-template>
        </entity_usage>
    </xsl:template>
    
    <xsl:template name="usage_without_name">
        <xsl:param name="name_status_cd" select="'S'"/>
        <xsl:param name="start_date" select="0=1"/>
        <entity_usage.name_status_cd><xsl:value-of select="$name_status_cd"/></entity_usage.name_status_cd>
        <xsl:choose>
            <xsl:when test="$start_date">
                <entity_usage.usage_start_date><xsl:value-of select="$start_date"/></entity_usage.usage_start_date>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="moddate" select="ancestor::*[position()=1]/Field[@name='REC_LAST_MOD_DATE']"/>
                <xsl:choose>
                    <xsl:when test="$moddate">
                        <entity_usage.usage_start_date><xsl:value-of select="$moddate"/></entity_usage.usage_start_date>
                    </xsl:when>
                    <xsl:otherwise>
                        <entity_usage.usage_start_date><xsl:value-of select="ancestor::*[position()=1]/Field[@name='REC_CREATE_DATE']"/></entity_usage.usage_start_date>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>