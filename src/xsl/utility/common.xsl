<?xml version="1.0"?>
<!--
 * '$RCSfile: common.xsl,v $'
 *  Authors: @author@
 *  Release: @release@
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-11-25 19:39:58 $'
 *  '$Revision: 1.1 $'
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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

 <!-- ***************************************************************************** -->
 <!-- Utility templates that can be imported  by other XSLs                         -->
 <!-- ***************************************************************************** -->

 <xsl:template name="UpperFirstLetter">
   <xsl:param name="text"/>
   <xsl:call-template name="to-upper">
     <xsl:with-param name="text">
       <xsl:value-of select="substring($text,1,1)"/>
     </xsl:with-param>
   </xsl:call-template>
   <xsl:call-template name="to-lower">
    <xsl:with-param name="text">
      <xsl:value-of select="substring($text,2)"/>
    </xsl:with-param>
  </xsl:call-template>
 </xsl:template>

  <xsl:template name="to-upper">
    <xsl:param name="text"/>
    <xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
    <xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>

    <xsl:value-of select="translate($text,$lcletters,$ucletters)"/>
  </xsl:template>

  <xsl:template name="to-lower">
    <xsl:param name="text"/>
    <xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
    <xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>

    <xsl:value-of select="translate($text,$ucletters,$lcletters)"/>
  </xsl:template>

</xsl:stylesheet>
