<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect">
<xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/>
  <xsl:param name="htmlPrefix" />

  <xsl:output method="html"/>

   <xsl:template match="/dataModel">
  <xsl:for-each select="entity" >
      <!-- table view nothing highlighted -->
      <xsl:for-each select="attribute[position()=1]"> <!-- get one attribute, which will be ignored : position at att node is important -->
      <xsl:call-template name="StartTable"><xsl:with-param name="CurrentField"></xsl:with-param></xsl:call-template>
      </xsl:for-each>
      <xsl:for-each select="attribute">
            <xsl:call-template name="StartTable"><xsl:with-param name="CurrentField" select="attName" /></xsl:call-template>
      </xsl:for-each>
    </xsl:for-each>
</xsl:template>
<xsl:template name="StartTable"><xsl:param name="CurrentField" />
   <xsl:variable name="CurrEnt" select="../entityName" />
   <xsl:variable name="fieldhtmlpart"><xsl:if test="string-length($CurrentField)&gt;0">~field~<xsl:value-of select="translate($CurrentField,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" /></xsl:if></xsl:variable>
  <redirect:write file="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}{$fieldhtmlpart}~type~tableview.html"> 
@webpage_top_html@

  @webpage_head_html@
   <title>VegBank Data Dictionary - table view: <xsl:value-of select="../entityName" /></title>
     @webpage_masthead_html@
      <h2>
        <a  href="{$htmlPrefix}-index.html">VegBank data dictionary</a>
      </h2>
          <p>Table:<xsl:value-of select="../entityName"/>
            <br/>
            <blockquote>
              <p>
                <xsl:value-of select="../entitySummary"/>
              </p>
            </blockquote>
          </p>
          <xsl:if test="string-length($CurrentField)&gt;0"><p>The field:<xsl:value-of select="$CurrentField" /> is highlighted below.  Click <a href="#this_field">here</a> to jump there.</p></xsl:if>
          <table border="1" cellpadding="0" cellspacing="0" width="100%">
            <tr class="greenbkgrd">
              <th>
                <b>
                  <a href="dd-guide.html#field-name">field name</a>
                </b>
              </th>
              <th>
                <b>
                  <a   href="dd-guide.html#nulls">nulls</a>
                </b>
              </th>
              <th>
                <b>
                  <a  href="dd-guide.html#type">type</a>
                </b>
              </th>
              <th>
                <b>
                  <a  href="dd-guide.html#key">key</a>
                </b>
              </th>
              <th>
                <b>
                  <a   href="dd-guide.html#references">references</a>
                </b>
              </th>
              <th>
                <b>
                  <a href="dd-guide.html#list">list</a>
                </b>
              </th>
              <!-- <td class="normal"><b><font size="+1">List Values</font></b></td> -->
              <th>
                <b>
                  <a   href="dd-guide.html#field-notes">field notes</a>
                </b>
              </th>
              <th>
                <b>
                  <a   href="dd-guide.html#field-definition">field definition</a>
                </b>
              </th>
            </tr>
            <xsl:for-each select="../../entity[entityName=$CurrEnt]/attribute">
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
                <td class="normal">
                  <xsl:if test="$RowType=0">
                  <a name="this_field" />
                  </xsl:if>
                  <xsl:choose>
                    <xsl:when test="attNulls='no'">
                      <b>
                        <a  href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">
                          <xsl:value-of select="attName"/>
                        </a>
                      </b>
                    </xsl:when>
                    <xsl:otherwise>
                      <a  href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">
                        <xsl:value-of select="attName"/>
                      </a>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
                <td class="normal">
                  <xsl:value-of select="attNulls"/>
                </td>
                <td class="normal">
                  <xsl:value-of select="attType"/>
                </td>
                <td class="normal">
                  <xsl:value-of select="attKey"/>
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
                <td class="normal">
                  <xsl:choose>
                    <xsl:when test="attListType='no'">
                      <xsl:value-of select="attListType"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <a  href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">
                        <xsl:value-of select="attListType"/>
                      </a>
                      <font size="-1">: 
    <select>
                          <option selected="yes">See values</option>
                          <xsl:for-each select="attList/attListItem">
                          <xsl:sort data-type="number" select="attListSortOrd"/>
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
                <td class="normal">
                  <xsl:value-of select="attNotes"/>
                </td>
                <td class="normal">
                  <xsl:value-of select="attDefinition"/>
                </td>
              </tr>


              <!-- </xsl:if>  -->
            </xsl:for-each>
          </table>

      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p> 
@webpage_footer_html@


</redirect:write> 

  </xsl:template>

     
</xsl:stylesheet>
