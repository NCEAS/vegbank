<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:vegbank="http://vegbank.org" xmlns:logic="http://vegbank.org" xmlns:bean="http://vegbank.org" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect">
<!--  <xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/> -->
  <!-- comment out extenstion-elemtn ... though this comment to run locally without xalan -->
  <xsl:param name="pathToWrite"/>
  <xsl:output method="html"/>
  <xsl:param name="alphalow">abcdefghijklmnopqrstuvwxyz</xsl:param>
  <xsl:param name="alphahigh">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:param>
  <xsl:template name="doViews">
    <xsl:param name="viewNum"/>
    <xsl:variable name="view_init" select="document('uniqueformlist.xml')/allforms/oneForm[position()=$viewNum]"/>
    <xsl:variable name="view" select="translate($view_init,$alphahigh,$alphalow)"/>
    <xsl:comment>Starting view <xsl:value-of select="$view"/> with number:<xsl:value-of select="$viewNum"/>
    </xsl:comment>
    <xsl:if test="string-length($view)&gt;0">
      <xsl:value-of select="$view"/>IS VIEW:
<xsl:call-template name="doEnts">
        <xsl:with-param name="view" select="$view"/>
      </xsl:call-template>
      <!-- call next view -->
      <xsl:call-template name="doViews">
        <xsl:with-param name="viewNum" select="number(1+$viewNum)"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  <xsl:template name="doEnts">
    <xsl:param name="view"/>
    <xsl:for-each select="/dataModel/entity">
      <xsl:variable name="countAttsInView" select="count(attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view])"/>
      <xsl:comment>
        <xsl:value-of select="entityName"/> has <xsl:value-of select="$countAttsInView"/> fields in view: <xsl:value-of select="$view"/>
      </xsl:comment>
      <xsl:if test="$countAttsInView&gt;0 or ((entityName='plot' or entityName='observation') and (substring($view,1,4)='plot'))"><!-- write those where there is a view for this ent, and  write plot and obs for all views starting with 'plot' -->
        <xsl:variable name="currEnt" select="translate(entityName,$alphahigh,$alphalow)"/>
        <!-- begin writing data here -->
        <xsl:choose>
          <xsl:when test="contains($view,'summary')">
            <!-- summary view: table with headers -->
            <xsl:comment>WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_head.jsp</xsl:comment>
            <redirect:write file="{$pathToWrite}{$currEnt}_{$view}_head.jsp">
              <xsl:for-each select="attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view]">
                <xsl:sort select="node()" data-type="number"/>
                <th>
                  <xsl:call-template name="labelField">
                    <xsl:with-param name="currEnt" select="$currEnt"/>
                    <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                    <xsl:with-param name="currLbl" select="../../attLabel"/>
                  </xsl:call-template>
                </th>
              </xsl:for-each>
            </redirect:write>
            <xsl:comment>END WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_head.jsp</xsl:comment>
            <xsl:comment>WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
            <redirect:write file="{$pathToWrite}{$currEnt}_{$view}_data.jsp">
              <xsl:for-each select="attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view]">
                <xsl:sort select="node()" data-type="number"/>
                <xsl:call-template name="writeField">
                  <xsl:with-param name="currEnt" select="$currEnt"/>
                  <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                  <xsl:with-param name="currentAtt" select="ancestor::attribute"/>
                </xsl:call-template>
              </xsl:for-each>
            </redirect:write>
            <xsl:comment>END WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
          </xsl:when>
          <xsl:otherwise>
            <!-- default view -->
            <xsl:comment>WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
            <redirect:write file="{$pathToWrite}{$currEnt}_{$view}_data.jsp">
              <xsl:for-each select="attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view]">
                <xsl:sort select="node()" data-type="number"/>
                <logic:notEmpty name="onerowof{$currEnt}" property="{translate(../../attName,$alphahigh,$alphalow)}">
                  <tr class="@nextcolorclass@">
                    <td class="datalabel">
                      <xsl:call-template name="labelField">
                        <xsl:with-param name="currEnt" select="$currEnt"/>
                        <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                        <xsl:with-param name="currLbl" select="../../attLabel"/>
                      </xsl:call-template>
                    </td>
                    <xsl:call-template name="writeField">
                      <xsl:with-param name="currEnt" select="$currEnt"/>
                      <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                      <xsl:with-param name="currentAtt" select="ancestor::attribute"/>
                    </xsl:call-template>
                  </tr>
                  <bean:define id="hadData" value="true"/>
                </logic:notEmpty>
              </xsl:for-each>
            </redirect:write>
            <xsl:comment>END WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:if>
    </xsl:for-each>
    <!--entity -->
  </xsl:template>
  <xsl:template match="/dataModel">
    <html>
      <xsl:call-template name="doViews">
        <xsl:with-param name="viewNum">1</xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
  <xsl:template name="writeField">
    <xsl:param name="currEnt"/>
    <xsl:param name="currFld"/>
    <xsl:param name="currentAtt"/>
    <xsl:comment>WRITE FIELD: <xsl:value-of select="$currFld"/> and att is: <xsl:value-of select="$currentAtt/attName"/>
    </xsl:comment>
    <td>
      <logic:notEmpty name="onerowof{$currEnt}" property="{$currFld}">
        <xsl:element name="span">
          <xsl:choose>
            <xsl:when test="string-length(@useClass)=0">
              <!-- no specific class for this data -->
              <!-- fill in different class for long text fields: -->
              <xsl:if test="$currentAtt/attType='text' or (contains($currentAtt/attType,'varchar (') and string-length($currentAtt/attType)&gt;12)">
                <xsl:attribute name="class">sizetiny</xsl:attribute>
              </xsl:if>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="class"><xsl:value-of select="@useClass"/></xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>

          <!-- this is what gets written on cell -->
          <xsl:variable name="currFldMaybeTransl">
            <xsl:value-of select="$currFld"/>
            <xsl:if test="string-length($currentAtt/attFKTranslationSQL)&gt;0">_transl</xsl:if>
          </xsl:variable>
          <xsl:choose>
            <xsl:when test="string-length($currentAtt/attFKTranslationSQL)&gt;0 and $currentAtt/attKey='FK' and ($currentAtt/attFKTranslationSQL/@addLink='true' or string-length($currentAtt/attFKTranslationSQL/@addLink)=0)">
              <!-- only do this if translation criteria AND FK! -->
              <xsl:element name="a">
                <xsl:attribute name="href">@get_link@std/<xsl:value-of select="translate(substring-before($currentAtt/attReferences,'.'),$alphahigh,$alphalow)"/>/<xsl:text disable-output-escaping="yes">&lt;</xsl:text>bean:write name='onerowof<xsl:value-of select="$currEnt"/>' property='<xsl:value-of select="$currFld"/>' /<xsl:text disable-output-escaping="yes">&gt;</xsl:text></xsl:attribute>
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
        </xsl:element>
        <!--</span>-->

          <!-- units -->
          <xsl:if test="string-length($currentAtt/attUnits)&gt;0">
            <xsl:comment>only write units if there is something here:</xsl:comment>
            <logic:notEmpty name="onerowof{$currEnt}" property="{$currFld}">
              <span class="units">
                <xsl:value-of select="$currentAtt/attUnits"/>
              </span>
            </logic:notEmpty>
          </xsl:if>
      </logic:notEmpty>
      <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    </td>
  </xsl:template>
  <xsl:template name="writeOneFieldValue">
    <xsl:param name="currEnt"/>
    <xsl:param name="beanPropName"/>
    <bean:write name="onerowof{$currEnt}" property="{$beanPropName}"/>
  </xsl:template>
  <xsl:template name="labelField">
    <xsl:param name="currEnt"/>
    <xsl:param name="currFld"/>
    <xsl:param name="currLbl"/>
    <xsl:choose>
      <xsl:when test="string-length($currLbl)&gt;0">
        <xsl:value-of select="$currLbl"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:comment>WARNING:no label in XML!</xsl:comment>
        <xsl:value-of select="$currFld"/>
      </xsl:otherwise>
    </xsl:choose>
    <!-- <a href="@get_link@dd/{$currEnt}/{$currFld}">
      <img src="@images_link@question.gif" alt="?" border="0"/>
    </a>-->
  </xsl:template>
</xsl:stylesheet>
