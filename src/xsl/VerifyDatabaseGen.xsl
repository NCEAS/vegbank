<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<!--
 *	'$RCSfile: VerifyDatabaseGen.xsl,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-17 19:20:20 $'
 *	'$Revision: 1.1 $'
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

<!--
 DESCRIPTION:
  Generates a perl script that compares the db_model_vegbank.xml with the 
  actual structure of the database.
-->

  <xsl:output method="text" indent="no"/>
  <xsl:template match="/dataModel">#!/usr/bin/perl
# This file was generated from vegbank/src/xsl/VerifyDatabaseGen.xsl

    <xsl:for-each select="entity">
      <xsl:call-template name="checkTable"/>
    </xsl:for-each>
  </xsl:template>

  <!--
    Most work done here for handling entities/tables
  -->
  <xsl:template name="checkTable">
    <xsl:variable name="tableName" select="entityName"/>
    <xsl:variable name="sequenceName"><xsl:value-of select="attribute[attKey='PK']/attName"/>_seq</xsl:variable>
# Use shell redirection to capture stderr
<!-- at line end are characters that must be escaped, thus ugliness, on top of redirection syntax ugliness ;) -->
$result = `psql vegbank -t -c  "select <xsl:apply-templates/> from <xsl:value-of select="$tableName"/> LIMIT 0" 2&gt;&amp;1`;
if ( $result =~ m/^ERROR: / )
{
  chomp($result);  # remove any trailing newline 
  print "Problem: '" . $result . "' on table:  <xsl:value-of select="$tableName"/>.\n";
}  
  </xsl:template>

  <!--
    Handle each attribute of table/entity
  -->
  <xsl:template match="attribute">
<xsl:value-of select="attName"/>
    <!-- Comma unless it is the last attribute -->
    <xsl:choose>
      <!-- I wish I knew why I need to decrement last() in this case ??? well it works -->
      <xsl:when test="position() = last()-1"></xsl:when>
      <xsl:otherwise>,</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

 <!--
    Default match rule is shut up
 -->
 <xsl:template match="*"/>

</xsl:stylesheet>
