<?xml version="1.0" encoding="UTF-8"?>
<!--  * '$RCSfile: manual2html.xsl,v $'  *  Authors: @author@  *  Release: @release@  *  *  '$Author: mlee $'  *  '$Date: 2007-09-05 20:02:40 $'  *  '$Revision: 1.17 $'  *   * This program is free software; you can redistribute it and/or modify  * it under the terms of the GNU General Public License as published by  * the Free Software Foundation; either version 2 of the License, or  * (at your option) any later version.  *  * This program is distributed in the hope that it will be useful,  * but WITHOUT ANY WARRANTY; without even the implied warranty of  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  * GNU General Public License for more details.  *  * You should have received a copy of the GNU General Public License  * along with this program; if not, write to the Free Software  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA  * -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect">
  <xsl:param name="anchorOneFile"/>
  <!-- # if one file made and linking to other areas with # links -->
  <xsl:param name="directory"/>
  <!-- dir to put these files, temporarily -->
  <xsl:param name="menu_url_full">manual-index-full.html</xsl:param>
  <xsl:param name="menu_url_basic">manual-index-basic.html</xsl:param>

  <xsl:param name="heightPerc">96</xsl:param>
  <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
  <xsl:template match="/ | manualHTML">
    
      <xsl:apply-templates/>
    
  </xsl:template>
  <xsl:template match="manualItem"/>
  <xsl:template match="manualDoc">
    <xsl:call-template name="writeIndex">
      <xsl:with-param name="menu_url" select="$menu_url_full" />
      <xsl:with-param name="maxLevel">100</xsl:with-param>
      <xsl:with-param name="offerMenuName">short</xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="writeIndex">
      <xsl:with-param name="menu_url" select="$menu_url_basic" />
      <xsl:with-param name="maxLevel">0</xsl:with-param>
      <xsl:with-param name="offerMenuName">full</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <xsl:template name="writeIndex">
    <xsl:param name="menu_url" />
    <xsl:param name="maxLevel" />
    <xsl:param name="offerMenuURL" />
    <xsl:param name="offerMenuName" />
    <hr/>
    <redirect:write file="{$directory}{$menu_url}">
     @webpage_top_html@
  
  @webpage_head_html@
<title>VegBank--tutorial</title>

  @webpage_masthead_small_html@    
          <table  height="100%" cellpadding="0" cellspacing="1" border="0">
        <tr>
          <td valign="top" align="center">
            <h1>VegBank Tutorial Menu</h1>
            <span class="sizetiny">
            <xsl:choose>
              <xsl:when test="$offerMenuName='full'"><!-- link to full-->short menu | <a class="tut_link" href="{$menu_url_full}">full menu</a></xsl:when>
              <xsl:otherwise><a class="tut_link" href="{$menu_url_basic}">short menu</a> | full menu</xsl:otherwise>
            </xsl:choose>
            </span>
          </td>
        </tr>
        <tr>
          <td valign="top">
            
              <xsl:call-template name="writeMenu">
                <xsl:with-param name="level">0</xsl:with-param>
                <xsl:with-param name="maxLevel" select="$maxLevel" />
              </xsl:call-template>
            
          </td>
        </tr>
        <tr>
          <td valign="bottom">
            <xsl:call-template name="closeManual"/>
          </td>
        </tr>
      </table>
       @webpage_footer_small_html@    </redirect:write>
    <xsl:for-each select="manualItem">
      <xsl:call-template name="runManualItem"/>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="runManualItem">
    <xsl:variable name="aFile" select="concat($directory,@name,'.html')"/>
    <redirect:write file="$aFile">
      @webpage_top_html@
  
  @webpage_head_html@
<title>VegBank Tutorial -- <xsl:value-of select="@topic"/>
      </title>

  @webpage_masthead_small_html@<table height="{$heightPerc}%" cellpadding="1" cellspacing="0" border="0">
        <tr>
          <td valign="top">
            <p class="major_smaller">VegBank tutorial  <a class="tut_link" href="{$menu_url_basic}">(menu)</a> <a name="{@name}.html"/><!-- not sure what this is for -->
              <br/>
              <xsl:element name="a">
                <xsl:if test="string-length(@refURL)&gt;0">
                  <xsl:attribute name="target">upperframe</xsl:attribute>
                  <xsl:attribute name="href"><xsl:value-of select="@refURL"/></xsl:attribute>
                </xsl:if>
                <xsl:value-of select="@topic"/>
              </xsl:element>
            </p>  <span class="tiny"></span>
            
            <xsl:if test="@finish='false'">
            <p><strong class="bright">WARNING:</strong> this page is not yet finished.  Sorry for the inconvenience.  Please contact us at help@vegbank.org if you need assistance.</p>
            </xsl:if>
            <xsl:apply-templates/>
            <!-- bottom navigation -->
          </td>
        </tr>
        <tr>
          <td valign="bottom" align="center">
            <table>
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
                  <table>
                    <tr>
                      <td align="left" class="sizetiny">
                          <xsl:choose>
                            <xsl:when test="$empty_url!=$prev_url">
                              <!-- do a link -->
                              <a class="tut_link" href="{$prev_url}">&lt;&lt; prev</a>
                            </xsl:when>
                            <xsl:otherwise>&lt;&lt; prev</xsl:otherwise>
                          </xsl:choose>
                      </td>
                      <td align="center" class="sizetiny">
                        <a class="tut_link" href="{$menu_url_basic}">MENU</a>
                        <br/>
                        <span class="sizetiny">
                          <xsl:call-template name="closeManual"/>
                        </span>
                      </td>
                      <td align="right" class="sizetiny">
                        <xsl:choose >
                          <xsl:when test="$empty_url!=$next_url"><a class="tut_link" href="{$next_url}">&gt;&gt; next</a></xsl:when>
                          <xsl:otherwise>&gt;&gt; next</xsl:otherwise>
                        </xsl:choose>
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
        @webpage_footer_small_html@      <!-- end of file -->
    </redirect:write>
    <hr/>
    <xsl:for-each select="manualItem">
      <xsl:call-template name="runManualItem"/>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="*">
    <!-- copy element and all atts -->
    <xsl:element name="{name()}">
      <xsl:choose>
        <xsl:when test="name()='highlight'">
          <!--special elements -->
          <xsl:variable name="pageToRedir">
            <xsl:for-each select="ancestor::manualItem">
              <xsl:if test="position()=last()">
                <xsl:value-of select="@refURL"/>
              </xsl:if>
            </xsl:for-each>
          </xsl:variable>
          <a href="javascript:tut_togglehighlightMainFrameEl('{@id}','{$pageToRedir}')"><img src="@images_link@highlight.png" alt="highlight" /></a>
        </xsl:when>
        <xsl:when test="name()='a'">
          <!-- see if its a link to manual page -->
          <xsl:variable name="internalLink" select="contains(@href,'.') or contains(@href,'@') or contains(@href,'/')" />
          <xsl:if test="string-length(@target)&lt;1">
            <!-- dont add target if there already is one -->
            <xsl:if test="$internalLink!=true">
              <!-- dont add target if there is not a . in the link (signifies help link) -->
              <xsl:attribute name="target">upperframe</xsl:attribute>
            </xsl:if>
          </xsl:if>
          <xsl:for-each select="attribute::*">
            <xsl:attribute name="{name()}"><xsl:value-of select="current()"/><xsl:if test="name()='href'"><xsl:if test="$internalLink=true and contains(current(),'#')=0"><!-- add .html as it is in the manual, but not just a # target -->.html</xsl:if></xsl:if></xsl:attribute>
          </xsl:for-each>
          <xsl:if test="$internalLink=true">
            <!-- last thing: add class for these to make green -->
            <xsl:attribute name="class">tut_link</xsl:attribute>
          </xsl:if>
          <xsl:apply-templates/>
        </xsl:when>
        <xsl:otherwise>
          <!-- normal elements: -->
          <!-- see if its a link to manual page -->
         
          <xsl:for-each select="attribute::*">
            <xsl:attribute name="{name()}"><xsl:value-of select="current()"/></xsl:attribute>
          </xsl:for-each>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
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
    <xsl:param name="maxLevel" />
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
        <a class="tut_link" href="{$currHREF}">
          <xsl:value-of select="@topic"/>
        </a>
        <br/>
        <!--</li>-->
        <xsl:if test="$maxLevel&gt;$level">
          <xsl:call-template name="writeMenu">
            <xsl:with-param name="level" select="number(($level)+1)"/>
            <xsl:with-param name="maxLevel" select="$maxLevel" />
          </xsl:call-template>
        </xsl:if>
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
    <a class="tut_link" title="Click here to close the tutorial frame (resets form on the left)" href="javascript:tut_close();">close</a> | <a  class="tut_link" href="@web_context@" target="_top" title="If closing the tutorial doesn't work, try this to exit the tutorial">exit</a> | <a href="javascript:tut_unhighlightMainFrame()"><img src="@images_link@unhighlight.png" alt="unhighlight" /></a>
  </xsl:template>
</xsl:stylesheet>
