<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:vegbank="http://vegbank.org" xmlns:logic="http://vegbank.org" xmlns:bean="http://vegbank.org">
  <xsl:output method="html"/>
  <xsl:param name="view">Summary</xsl:param>
  <xsl:param name="oneTbl">commInterpretation</xsl:param>
  <xsl:param name="alphalow">abcdefghijklmnopqrstuvwxyz</xsl:param>
  <xsl:param name="alphahigh">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:param>
  <xsl:template match="entity">
    <xsl:if test="string-length($oneTbl)&lt;1 or entityName=$oneTbl">
      <br/>
      <br/>
      <br/>
      <br/>
      <xsl:comment> ____________________________START  SQL for:  <xsl:value-of select="entityName"/> _______________________________________ 
<xsl:value-of select="translate(entityName,$alphahigh,$alphalow)"/>=SELECT <xsl:for-each select="attribute">
 <xsl:text>           </xsl:text>    <xsl:value-of select="translate(attName,$alphahigh,$alphalow)"/><xsl:if test="string-length(attFKTranslationSQL)&gt;0">  , <xsl:value-of select="attFKTranslationSQL" /> AS <xsl:value-of select="translate(attName,$alphahigh,$alphalow)"/>_transl </xsl:if>
          <xsl:if test="position()!=last()">,</xsl:if> \
</xsl:for-each>           FROM <xsl:value-of select="translate(entityName,$alphahigh,$alphalow)"/>
      </xsl:comment>
      <xsl:comment> ____________________________START:  <xsl:value-of select="entityName"/> _______________________________________ 
</xsl:comment> 


@stdvegbankget_jspdeclarations@


<HEAD>
@defaultHeadToken@
 
<TITLE>View Data in VegBank : <xsl:value-of select="entityName"/>(s) : <xsl:value-of select="$view"/>
        </TITLE>
        <link rel="stylesheet" href="@stylesheet@" type="text/css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      </HEAD>
      <body>@vegbank_header_html_normal@  <br/>
        <h2>View VegBank <xsl:value-of select="entityLabel"/>s</h2>
        <xsl:text disable-output-escaping="yes">&lt;% String rowClass = "evenrow"; %&gt;</xsl:text>
        <xsl:variable name="currEnt" select="translate(entityName,$alphahigh,$alphalow)"/>
        <xsl:variable name="currPK" select="translate(attribute[attKey='PK']/attName,$alphahigh,$alphalow)"/>
        <xsl:element name="vegbank:get">
          <xsl:attribute name="id"><xsl:value-of select="$currEnt"/></xsl:attribute>
          <xsl:attribute name="select"><xsl:value-of select="$currEnt"/></xsl:attribute>
          <xsl:attribute name="beanName">map</xsl:attribute>
          <xsl:attribute name="where">where_<xsl:value-of select="$currEnt"/>_pk</xsl:attribute>
          <xsl:attribute name="pager">true</xsl:attribute>
        </xsl:element>
        <logic:empty name="{$currEnt}-BEANLIST">
          <p>  Sorry, no <xsl:value-of select="entityName"/>s found.</p>
        </logic:empty>
        <logic:notEmpty name="{$currEnt}-BEANLIST">
          <!-- begin writing data here -->
          <xsl:choose>
            <xsl:when test="$view='Summary'">
              <!-- summary view: table with headers -->
              <table class="leftrightborders" cellpadding="2">
                <tr>
                  <th>More</th>
                  <xsl:for-each select="attribute[attForms/formShow/@name=$view  and attForms/formShow&gt;0]">
                    <xsl:sort select="attForms/formShow" data-type="number"/>
                    <th>
                      <xsl:call-template name="labelField">
                        <xsl:with-param name="currEnt" select="$currEnt"/>
                        <xsl:with-param name="currFld" select="translate(attName,$alphahigh,$alphalow)"/>
                      </xsl:call-template>
                    </th>
                  </xsl:for-each>
                </tr>
                <logic:iterate id="onerowof{$currEnt}" name="{$currEnt}-BEANLIST">
                  <tr class="@nextcolorclass@">
                    <td class="sizetiny">
                      <xsl:element name="a">
                        <xsl:attribute name="href">@get_link@detail/<xsl:value-of select="$currEnt"/>/<xsl:text disable-output-escaping="yes">&lt;</xsl:text>bean:write name='onerowof<xsl:value-of select="$currEnt"/>' property='<xsl:value-of select="translate($currPK,$alphahigh,$alphalow)"/>' /<xsl:text disable-output-escaping="yes">&gt;</xsl:text></xsl:attribute>
                            Details
                            </xsl:element>
                      <!-- a -->
                    </td>
                    <xsl:for-each select="attribute[attForms/formShow/@name=$view  and attForms/formShow&gt;0]">
                      <xsl:sort select="attForms/formShow" data-type="number"/>
                      <xsl:call-template name="writeField">
                        <xsl:with-param name="currEnt" select="$currEnt"/>
                        <xsl:with-param name="currFld" select="translate(attName,$alphahigh,$alphalow)"/>
                      </xsl:call-template>
                    </xsl:for-each>
                  </tr>
                </logic:iterate>
              </table>
            </xsl:when>
            <xsl:otherwise>
              <!-- default view -->
              <logic:iterate id="onerowof{$currEnt}" name="{$currEnt}-BEANLIST">
                <xsl:comment> iterate over all records in set : new table for each </xsl:comment>
                <table class="leftrightborders" cellpadding="0">
                  <xsl:comment>each field, only write when field HAS contents</xsl:comment>
                  <xsl:for-each select="attribute[attForms/formShow/@name=$view  and attForms/formShow&gt;0]">
                    <xsl:sort select="attForms/formShow" data-type="number"/>
                    <logic:notEmpty name="onerowof{$currEnt}" property="{translate(attName,$alphahigh,$alphalow)}">
                      <tr class="@nextcolorclass@">
                        <td class="datalabel">
                          <xsl:call-template name="labelField">
                            <xsl:with-param name="currEnt" select="$currEnt"/>
                            <xsl:with-param name="currFld" select="translate(attName,$alphahigh,$alphalow)"/>
                          </xsl:call-template>
                        </td>
                        <xsl:call-template name="writeField">
                          <xsl:with-param name="currEnt" select="$currEnt"/>
                          <xsl:with-param name="currFld" select="translate(attName,$alphahigh,$alphalow)"/>
                        </xsl:call-template>
                      </tr>
                    </logic:notEmpty>
                  </xsl:for-each>
                </table>
                <p>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </p>
              </logic:iterate>
            </xsl:otherwise>
          </xsl:choose>
        </logic:notEmpty>
        <br/>
        <vegbank:pager/>
          @vegbank_footer_html_tworow@
<xsl:comment> ____________________________@END:  <xsl:value-of select="entityName"/> _______________________________________ 


</xsl:comment>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
      </body>
    </xsl:if>
  </xsl:template>
  <xsl:template match="dataModel">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="*"/>
  <xsl:template name="writeField">
    <xsl:param name="currEnt"/>
    <xsl:param name="currFld"/>
    <xsl:element name="td">
      <!-- fill in different class for long text fields: -->
      <xsl:if test="attType='text' or (contains(attType,'varchar (') and string-length(attType)&gt;12)">
        <xsl:attribute name="class">sizetiny</xsl:attribute>
      </xsl:if>
      <!-- this is what gets written on cell -->
      <xsl:variable name="currFldMaybeTransl">
        <xsl:value-of select="$currFld"/>
        <xsl:if test="string-length(attFKTranslationSQL)&gt;0">_transl</xsl:if>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="string-length(attFKTranslationSQL)&gt;0 and attKey='FK'">
          <!-- only do this if translation criteria AND FK! -->
          <xsl:element name="a">
            <xsl:attribute name="href">@get_link@std/<xsl:value-of select="translate(substring-before(attReferences,'.'),$alphahigh,$alphalow)"/>/<xsl:text disable-output-escaping="yes">&lt;</xsl:text>bean:write name='onerowof<xsl:value-of select="$currEnt"/>' property='<xsl:value-of select="$currFld"/>' /<xsl:text disable-output-escaping="yes">&gt;</xsl:text></xsl:attribute>
            <xsl:call-template name="writeOneFieldValue">
              <xsl:with-param name="beanPropName" select="$currFldMaybeTransl"/>
            <xsl:with-param name="currEnt" select="$currEnt"/>
            </xsl:call-template>
          </xsl:element>
          <!-- </a> -->
        </xsl:when>
        <!-- 2nd repeat of this  test -->
        <xsl:otherwise>
          <!-- just write data -->
          <xsl:call-template name="writeOneFieldValue">
            <xsl:with-param name="beanPropName" select="$currFldMaybeTransl"/>
            <xsl:with-param name="currEnt" select="$currEnt"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
      <!-- units -->
      <xsl:if test="string-length(attUnits)&gt;0">
        <xsl:comment>only write units if there is something here:</xsl:comment>
        <logic:notEmpty name="onerowof{$currEnt}" property="{$currFld}">
          <span class="units">
            <xsl:value-of select="attUnits"/>
          </span>
        </logic:notEmpty>
      </xsl:if>
      <!--</td>-->
    </xsl:element>
  </xsl:template>
  <xsl:template name="writeOneFieldValue">
    <xsl:param name="currEnt"/>
    <xsl:param name="beanPropName"/>
    <bean:write name="onerowof{$currEnt}" property="{$beanPropName}"/>
    <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
  </xsl:template>
  <xsl:template name="labelField">
    <xsl:param name="currEnt"/>
    <xsl:param name="currFld"/>
    <xsl:choose>
      <xsl:when test="string-length(attLabel)&gt;0">
        <xsl:value-of select="attLabel"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:comment>WARNING:no label in XML!</xsl:comment>
        <xsl:value-of select="attName"/>
      </xsl:otherwise>
    </xsl:choose>
    <a href="@get_link@dd/{$currEnt}/{$currFld}">
      <img src="@images_link@question.gif" alt="?" border="0"/>
    </a>
  </xsl:template>
</xsl:stylesheet>
