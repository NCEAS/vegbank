<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect"> 
  <xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/> 
  <!-- <xsl:param name="CurrentTable" />
<xsl:param name="CurrentField" /> -->
  <!-- not needed : <xsl:param name="CurrentTable">observation</xsl:param>
  <xsl:param name="CurrentField">PLOT_ID</xsl:param> -->

    <xsl:param name="htmlPrefix" />

  <xsl:output method="html"/>
  <xsl:template match="/dataModel">
  <xsl:for-each select="entity/attribute" >
 <redirect:write file="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">
    <head>@defaultHeadToken@
 <title>VegBank Data Dictionary - field view : <xsl:value-of select="../entityName" />.<xsl:value-of select="attName" /></title>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
    </head>
    <body>
    @vegbank_header_html_normal@

      <h2>
        <a  href="{$htmlPrefix}-index.html">VegBank data dictionary</a>
      </h2>
            <p>Table:<xsl:value-of select="../entityName"/>  (<a href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">Click for table view</a>)
              <br/>
              <blockquote>
                <p>
                  <xsl:value-of select="../entitySummary"/>
                </p>
              </blockquote>
             
            </p>
            <table border="1" cellpadding="0" cellspacing="0" width="100%">
              <tr bgcolor="#FFFFFF">
                <td class="normal">
                  <b>
                    <a href="dd-guide.html#field-name">field name:</a>
                  </b>
                </td>
                <td class="normal">
                  <xsl:choose>
                    <xsl:when test="attNulls='no'">
                      <b>
                        <xsl:value-of select="attName"/>
                      </b>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attName"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <tr bgcolor="#FFFFCC">
                <td class="normal">
                  <b>
                    <a  href="dd-guide.html#nulls">nulls:</a>
                  </b>
                </td>
                <td class="normal">
                  <xsl:value-of select="attNulls"/>
                </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td class="normal">
                  <b>
                    <a  href="dd-guide.html#type">type:</a>
                  </b>
                </td>
                <td class="normal">
                  <xsl:value-of select="attType"/>
                </td>
              </tr>
              <tr bgcolor="#FFFFCC">
                <td class="normal">
                  <b>
                    <a  href="dd-guide.html#key">key:</a>
                  </b>
                </td>
                <td class="normal">
                  <xsl:value-of select="attKey"/>
                </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td class="normal">
                  <b>
                   <a  href="dd-guide.html#references">references:</a>
                  </b>
                </td>
                <td class="normal">
                  <!-- references can be split so that it doesn't make the column too wide in output -->
<xsl:choose>
                    <xsl:when test="contains(attReferences,'.')">
                      <a  href="{$htmlPrefix}~table~{translate(substring-before(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(substring-after(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                        <xsl:value-of select="substring-before(attReferences,'.')"/>. <xsl:value-of select="substring-after(attReferences,'.')"/>
                      </a>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attReferences"/>
                    </xsl:otherwise>
                  </xsl:choose>

                </td>
              </tr>
              <tr bgcolor="#FFFFCC">
                <td class="normal">
                  <b>
                    <a  href="dd-guide.html#list">list:</a>
                  </b>
                </td>
                <td class="normal">
                  <xsl:value-of select="attListType"/>
                </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td class="normal">
                  <b>
                <a  href="dd-guide.html#field-notes">field notes:</a>
                  </b>
                </td>
                <td class="normal">
                  <xsl:value-of select="attNotes"/>
                </td>
              </tr>
              <tr bgcolor="#FFFFCC">
                <td class="normal">
                  <b>
                 <a  href="dd-guide.html#field-definition">field definition:</a>
                  </b>
                </td>
                <td class="normal">
                  <xsl:value-of select="attDefinition"/>
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <xsl:if test="attListType!='no'">
                    <b>
             list values:
                  </b>
                    <br/>
                    <table border="1" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="normal">
                          <b>value</b>
                        </td>
                        <td class="normal">
                          <b>description</b>
                        </td>
                        <td class="normal">
                          <b>sorting</b>
                        </td>
                      </tr>
                      <xsl:for-each select="attList/attListItem">
                        <xsl:sort data-type="number" select="attListSortOrd"/>
                        <!-- <xsl:if test="attribute/attModel='logical'"> -->
                        <!-- the following variable is 0 for the matching field, and otherwise alternates 1 and 2 - these will be converted into colors orange, yellow, and white -->
                        <!-- the variables are made uppercase by the translate function so that plot_ID = PLOT_ID -->
                        <xsl:variable name="RowType" select="string((1+number((position() mod 2) = 0)))"/>
                        <!-- this will be the green portion of the color -->
                        <xsl:variable name="GreenAmt" select="translate(translate($RowType,'0','9'),'12','FF')"/>
                        <!-- this will be the blue portion of the color -->
                        <xsl:variable name="BlueAmt" select="translate(translate($RowType,'1','C'),'2','F')"/>
                        <!-- the following variable will be FF9900 or FFFFCC or FFFFFF -->
                        <xsl:variable name="RowColor">FF<xsl:value-of select="$GreenAmt"/>
                          <xsl:value-of select="$GreenAmt"/>
                          <xsl:value-of select="$BlueAmt"/>
                          <xsl:value-of select="$BlueAmt"/>
                        </xsl:variable>
                        <tr bgcolor="#{$RowColor}">
                          <td class="normal">
                            <xsl:value-of select="attListValue"/>
                          </td>
                          <td class="normal">
                            <xsl:choose>
                              <xsl:when test="string-length(attListValueDesc)&gt;0">
                                <xsl:value-of select="attListValueDesc"/>
                              </xsl:when>
                              <xsl:otherwise>--</xsl:otherwise>
                            </xsl:choose>
                          </td>
                          <td class="normal">
                            <xsl:value-of select="attListSortOrd"/>
                          </td>
                        </tr>
                      </xsl:for-each>
                    </table>
                  </xsl:if>
                </td>
              </tr>
              <!--        <tr bgcolor="#{$RowColor}"> -->
            </table>
      <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
@vegbank_footer_html_tworow@

    </body>
</redirect:write>
</xsl:for-each> <!-- table -->
  </xsl:template>
</xsl:stylesheet>
