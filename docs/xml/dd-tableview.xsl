<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <!-- <xsl:param name="CurrentTable" />
<xsl:param name="CurrentField" /> -->
  <xsl:param name="CurrentTable">plot</xsl:param>
  <xsl:param name="CurrentField"></xsl:param>
  <xsl:param name="htmlPrefix">ddr</xsl:param>
  <xsl:param name="positionAdj">-1</xsl:param>
  <xsl:output method="html"/>
  <xsl:template match="/dataModel">
    <head>
      <style type="text/css">
  td , th , p
  {
  font-family: verdana, arial, sans-serif  ;
  font-size:smaller
  }
  tr.greenbkgrd {
  font-color : #FFFF80 ; background-color: #336633
  }
  h2
  {
  font-family:georgia, Times New Roman,  times, serif 
  }

</style>
    <link rel="stylesheet" href="@stylesheet@" type="text/css"/> 
       <title>VegBank Data Dictionary - table view</title>
    </head>
    <body>
    @vegbank_header_html_normal@
      <h2>
        <a  href="{$htmlPrefix}-index.html">VegBank data dictionary</a>
      </h2>
      <xsl:for-each select="entity">
        <xsl:if test="entityName=$CurrentTable">
          <p>Table:<xsl:value-of select="entityName"/>
            <br/>
            <blockquote>
              <p>
                <xsl:value-of select="entitySummary"/>
              </p>
            </blockquote>
          </p>
          <xsl:if test="string-length($CurrentField)&gt;0"><p>The field:<xsl:value-of select="$CurrentField" /> is highlighted below.  Click <a href="#this_field">here</a> to jump there.</p></xsl:if>
          <table border="1" cellpadding="0" cellspacing="0" width="100%">
            <tr class="greenbkgrd">
              <td>
                <b>
                  <a class="brightyellow" href="dd-guide.html#field-name">field name</a>
                </b>
              </td>
              <td>
                <b>
                  <a  class="brightyellow" href="dd-guide.html#nulls">nulls</a>
                </b>
              </td>
              <td>
                <b>
                  <a  class="brightyellow"  href="dd-guide.html#type">type</a>
                </b>
              </td>
              <td>
                <b>
                  <a  class="brightyellow" href="dd-guide.html#key">key</a>
                </b>
              </td>
              <td>
                <b>
                  <a  class="brightyellow" href="dd-guide.html#references">references</a>
                </b>
              </td>
              <td>
                <b>
                  <a class="brightyellow" href="dd-guide.html#list">list</a>
                </b>
              </td>
              <!-- <td><b><font size="+1">List Values</font></b></td> -->
              <td>
                <b>
                  <a  class="brightyellow" href="dd-guide.html#field-notes">field notes</a>
                </b>
              </td>
              <td>
                <b>
                  <a  class="brightyellow" href="dd-guide.html#field-definition">field definition</a>
                </b>
              </td>
            </tr>
            <xsl:for-each select="attribute">
              <!-- <xsl:if test="attribute/attModel='logical'"> -->
              <!-- the following variable is 0 for the matching field, and otherwise alternates 1 and 2 - these will be converted into colors orange, yellow, and white -->
              <!-- the variables are made uppercase by the translate function so that plot_ID = PLOT_ID -->
              <xsl:variable name="RowType" select="string(-1*((number(translate(attName,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')=translate($CurrentField,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'))-1)*(1+number((position() mod 2) = 0))))"/>
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
                <td>
                  <xsl:if test="$RowType=0">
                  <a name="this_field" />
                  </xsl:if>
                  <xsl:choose>
                    <xsl:when test="attNulls='no'">
                      <b>
                        <a class="blk" href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">
                          <xsl:value-of select="attName"/>
                        </a>
                      </b>
                    </xsl:when>
                    <xsl:otherwise>
                      <a class="blk" href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">
                        <xsl:value-of select="attName"/>
                      </a>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
                <td>
                  <xsl:value-of select="attNulls"/>
                </td>
                <td>
                  <xsl:value-of select="attType"/>
                </td>
                <td>
                  <xsl:value-of select="attKey"/>
                </td>
                <td>
                  <!-- references can be split so that it doesn't make the column too wide in output -->
                  <xsl:choose>
                    <xsl:when test="contains(attReferences,'.')">
                      <a class="blk" href="{$htmlPrefix}~table~{translate(substring-before(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(substring-after(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                        <xsl:value-of select="substring-before(attReferences,'.')"/>. <xsl:value-of select="substring-after(attReferences,'.')"/>
                      </a>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attReferences"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
                <td>
                  <xsl:choose>
                    <xsl:when test="attListType='no'">
                      <xsl:value-of select="attListType"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <a class="blk" href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">
                        <xsl:value-of select="attListType"/>
                      </a>
                      <font size="-1">: 
    <select>
                          <option selected="yes">See values</option>
                          <xsl:for-each select="attList/attListItem">
                            <option>
                              <xsl:value-of select="substring(attListValue,1,27)"/>
                              <xsl:if test="string-length(attListValue)>27">...</xsl:if>
                            </option>
                          </xsl:for-each>
                        </select>
                      </font>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
                <td>
                  <xsl:value-of select="attNotes"/>
                </td>
                <td>
                  <xsl:value-of select="attDefinition"/>
                </td>
              </tr>
              <!-- </xsl:if>  -->
            </xsl:for-each>
          </table>
        </xsl:if>
      </xsl:for-each>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p> 
@vegbank_footer_html_onerow@

    </body>
  </xsl:template>
</xsl:stylesheet>
