<?xml version="1.0" encoding="UTF-8"?>
<!--
 * '$RCSfile: manual2html.xsl,v $'
 *  Authors: @author@
 *  Release: @release@
 *
 *  '$Author: mlee $'
 *  '$Date: 2004-07-29 09:07:16 $'
 *  '$Revision: 1.4 $'
 * 
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
 *
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect">
  <xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/>
  <xsl:param name="anchorOneFile"/>
  <!-- # if one file made and linking to other areas with # links -->
  <xsl:param name="directory"/>
  <!-- dir to put these files, temporarily -->
  <xsl:param name="menu_url">manual-index.html</xsl:param>
  <xsl:param name="heightPerc">96</xsl:param>
  <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
  <xsl:template match="/ | manualHTML">
    <p>
      <xsl:apply-templates/>
    </p>
  </xsl:template>
  <xsl:template match="manualItem"/>
  <xsl:template match="manualDoc">
    <hr/>
    <redirect:write file="{$directory}{$menu_url}">
      <html>
        <head>
          <link rel="stylesheet" href="@stylesheet@" type="text/css"/>
        </head>
        <body>
          <table width="100%" height="100%">
            <tr>
              <td valign="top" align="center">
                <font size="+1">
                  <b>VegBank</b>
                </font> -- tutorial</td>
            </tr>
<tr><td valign="top">          
          <p>
            <xsl:call-template name="writeMenu">
              <xsl:with-param name="level">0</xsl:with-param>
            </xsl:call-template>
          </p>
</td></tr>
<tr><td valign="bottom">
<p>
            <font color="red">
              <xsl:call-template name="closeManual" />
            </font>
</p>
</td></tr>
</table>          
          
        </body>
      </html>
    </redirect:write>
    <xsl:for-each select="manualItem">
      <xsl:call-template name="runManualItem"/>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="runManualItem">
    <xsl:variable name="aFile" select="concat($directory,@name,'.html')"/>
    <redirect:write select="$aFile">
      <html>
        <head>
          <title>VegBank Tutorial -- <xsl:value-of select="@topic"/>
          </title>
          <link rel="stylesheet" href="@stylesheet@" type="text/css"/>
          <meta http-equiv="Content-Type" content="text/html; charset="/>
          <script language="JavaScript">
            <xsl:comment>
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}
//</xsl:comment>
          </script>
        </head>
        <body>
          <a name="{@name}.html"/>
          <!-- top row to link places -->
          <table height="{$heightPerc}%" width="100%">
            <tr>
              <td valign="top">
                <table width="100%">
                  <tr>
                    <td>
                      <table width="100%">
                        <tr>
                          <td align="center">
                            <font size="+1">
                              <b>VegBank</b>
                            </font> -- tutorial</td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr valign="top">
              <td>
                <table>
                  <tr>
                    <td>
                      <h2>
                        <xsl:element name="a">
                          <xsl:if test="string-length(@refURL)&gt;0">
                            <xsl:attribute name="target">upperframe</xsl:attribute>
                            <xsl:attribute name="href"><xsl:value-of select="@refURL"/></xsl:attribute>
                          </xsl:if>
                          <xsl:value-of select="@topic"/>
                        </xsl:element>
                      </h2>
                      <xsl:apply-templates/>
                      <!-- bottom navigation -->
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td valign="bottom">
                <table width="100%">
                  <tr>
                    <td>
                      <xsl:variable name="empty_url">
                        <!-- vriable that holds name of link that should be disabled -->
                        <xsl:call-template name="link-to-man">
                          <xsl:with-param name="page"/>
                        </xsl:call-template>
                      </xsl:variable>
                      <xsl:variable name="next_url">
                        <xsl:call-template name="link-to-man">
                          <xsl:with-param name="page">
                            <xsl:call-template name="get-nextURL"/>
                          </xsl:with-param>
                        </xsl:call-template>
                      </xsl:variable>
                      <xsl:variable name="prev_url">
                        <xsl:call-template name="link-to-man">
                          <xsl:with-param name="page">
                            <xsl:call-template name="get-prevURL"/>
                          </xsl:with-param>
                        </xsl:call-template>
                      </xsl:variable>
                      <table width="100%">
                        <tr>
                          <td align="left">
                            <!-- long method allows disabling the button -->
                            <xsl:element name="input">
                              <xsl:attribute name="value">&lt;&lt;</xsl:attribute>
                              <xsl:attribute name="type">button</xsl:attribute>
                              <xsl:attribute name="onclick">window.location='<xsl:value-of select="$prev_url"/>'</xsl:attribute>
                              <xsl:if test="$empty_url=$prev_url">
                                <!-- add disabled att -->
                                <xsl:attribute name="disabled">disabled</xsl:attribute>
                              </xsl:if>
                            </xsl:element>
                            <br/>
                            <font size="-2">prev</font>
                          </td>
                          <td align="center">
                            <input value="MENU" type="button" onclick="window.location='{$menu_url}'"/><br />
                            <font size="-2" color="red"> <xsl:call-template name="closeManual" /></font></td>
                          <td align="right">
                            <!--                            <input value="&gt;&gt;" type="button" onclick="window.location='{$next_url}'"/> -->
                            <xsl:element name="input">
                              <xsl:attribute name="value">&gt;&gt;</xsl:attribute>
                              <xsl:attribute name="type">button</xsl:attribute>
                              <xsl:attribute name="onclick">window.location='<xsl:value-of select="$next_url"/>'</xsl:attribute>
                              <xsl:if test="$empty_url=$next_url">
                                <!-- add disabled att -->
                                <xsl:attribute name="disabled">disabled</xsl:attribute>
                              </xsl:if>
                            </xsl:element>
                            <br/>
                            <font size="-2">next</font>
                          </td>
                        </tr>
                        <tr>
                          <td colspan="3" align="center">
                            <xsl:variable name="charsPickList">30</xsl:variable>
                            <select name="nav_picklist" onChange="MM_jumpMenu('window',this,0)">
                              <xsl:if test="string-length(../@name)&gt;0">
                                <option value="{$anchorOneFile}{../@name}.html">
                                  <xsl:value-of select="substring(../@topic,1,$charsPickList)"/>
                                </option>
                              </xsl:if>
                              <option value="#" selected="selected">[current]-<xsl:value-of select="substring(@topic,1,number(($charsPickList) -10))"/>
                              </option>
                              <xsl:for-each select="manualItem">
                                <option value="{$anchorOneFile}{@name}.html">--<xsl:value-of select="substring(@topic,1,number(($charsPickList) -2))"/>
                                </option>
                              </xsl:for-each>
                            </select>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </body>
      </html>
      <!-- end of file -->
    </redirect:write>
    <hr/>
    <xsl:for-each select="manualItem">
      <xsl:call-template name="runManualItem"/>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="*">
    <!-- copy element and all atts -->
    <xsl:element name="{name()}">
      <xsl:if test="name()='a'">
        <xsl:if test="string-length(@target)&lt;1">
          <xsl:attribute name="target">upperframe</xsl:attribute>
        </xsl:if>
      </xsl:if>
      <xsl:for-each select="attribute::*">
        <xsl:attribute name="{name()}"><xsl:value-of select="current()"/></xsl:attribute>
      </xsl:for-each>
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>
  <xsl:template name="get-nextURL">
    <!-- check for children:- -->
    <xsl:choose>
      <xsl:when test="string-length(manualItem/@name)&gt;0">
        <xsl:value-of select="manualItem/@name"/>
      </xsl:when>
      <xsl:otherwise>
        <!-- following -->
        <xsl:value-of select="following::manualItem/@name"/>
      </xsl:otherwise>
    </xsl:choose>
    <!-- <xsl:value-of select="manualItem/@name" /> -->
    <!--(I am number: <xsl:value-of select="number(position())" /> of <xsl:value-of  select="count(../node())" /> ; next is: )-->
  </xsl:template>
  <xsl:template name="get-prevURL">
    <!-- check for children:- -->
    <xsl:if test="position()=1">
      <xsl:for-each select="ancestor::manualItem">
        <xsl:if test="position()=last()">
          <xsl:value-of select="@name"/>
        </xsl:if>
      </xsl:for-each>
    </xsl:if>
    <xsl:if test="position()!=1">
      <xsl:for-each select="preceding::manualItem">
        <xsl:if test="position()=last()">
          <xsl:value-of select="@name"/>
        </xsl:if>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>
  <xsl:template name="link-to-man">
    <xsl:param name="page"/>
    <xsl:value-of select="$anchorOneFile"/>
    <xsl:value-of select="$page"/>.html</xsl:template>
  <xsl:template name="writeMenu">
    <xsl:param name="level"/>
    <xsl:if test="count(manualItem)&gt;0">
      <!--  <ul class="constsize"> -->
      <xsl:for-each select="manualItem">
        <xsl:variable name="currHREF">
          <xsl:call-template name="link-to-man">
            <xsl:with-param name="page" select="@name"/>
          </xsl:call-template>
        </xsl:variable>
        <!--<li class="constsize">-->
        <xsl:call-template name="nbsp">
          <xsl:with-param name="count" select="number(2*($level))"/>
        </xsl:call-template>
        <xsl:if test="$level&gt;0">-</xsl:if>
        <a href="{$currHREF}">
          <xsl:value-of select="@topic"/>
        </a>
        <br/>
        <!--</li>-->
        <xsl:call-template name="writeMenu">
          <xsl:with-param name="level" select="number(($level)+1)"/>
        </xsl:call-template>
      </xsl:for-each>
      <!--  </ul>  -->
    </xsl:if>
  </xsl:template>
  <xsl:template name="nbsp">
    <xsl:param name="count"/>
    <xsl:if test="$count&gt;0">
      <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      <xsl:call-template name="nbsp">
        <xsl:with-param name="count" select="number(($count)-1)"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
<xsl:template name="closeManual">
<a style="COLOR: #ff0000" href="javascript:document.top.location=window.upperframe.location">close tutorial</a>
</xsl:template>

</xsl:stylesheet>
