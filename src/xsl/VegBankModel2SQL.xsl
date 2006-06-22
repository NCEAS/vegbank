<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<!--
 *	'$RCSfile: VegBankModel2SQL.xsl,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-06-22 18:32:13 $'
 *	'$Revision: 1.3 $'
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
 Generate the vegbank sql create scripts from the xml schema for each module.
 Expects the module name to be passed in as a parameter called 'module'

 Assumes the following naming convention.
  commXXX   => community module table
  plantXXX  => plant module table
  all other => plot module table.
-->

  <xsl:output method="text" indent="no"/>
  
  <xsl:template match="/dataModel">
    -- This is a generated SQL script for postgresql
    --
   
    <xsl:for-each select="entity">
      <xsl:call-template name="createTable"/>
    </xsl:for-each>
    <xsl:for-each select="entity">
      <xsl:call-template name="alterTable"/>
    </xsl:for-each>
  </xsl:template>

  <!--
    Most work done here for handling entities/tables
  -->
  <xsl:template name="createTable">

    <xsl:variable name="tableName" select="entityName"/>
    <xsl:variable name="sequenceName"><xsl:value-of select="entityName" />__<xsl:value-of select="attribute[attKey='PK']/attName"/>_seq</xsl:variable>
    
----------------------------------------------------------------------------
-- CREATE <xsl:value-of select="$tableName"/>
----------------------------------------------------------------------------


CREATE SEQUENCE <xsl:value-of select="$sequenceName"/>;

CREATE TABLE <xsl:value-of select="entityName"/>
(
    <xsl:apply-templates>
      <xsl:with-param name="sequenceName" select="$sequenceName"/> 
    </xsl:apply-templates>
);    
    
  
  </xsl:template>

  <!--
    Most work done here for handling  altering tables
  -->
  <xsl:template name="alterTable">
----------------------------------------------------------------------------
-- ALTER <xsl:value-of select="entityName"/>
----------------------------------------------------------------------------
    
    <xsl:for-each select="attribute[attKey='FK']">
ALTER TABLE  <xsl:value-of select="../entityName"/>
  ADD CONSTRAINT <xsl:value-of select="attName"/> FOREIGN KEY ( <xsl:value-of select="attName"/> )
  REFERENCES  <xsl:value-of select="substring-before(attReferences, '.')"/> (<xsl:value-of select="substring-after(attReferences, '.')"/>);
    </xsl:for-each>
  
  </xsl:template>

  <!--
    Handle creation of attribute of table/entity
  -->
  <xsl:template match="attribute">
    <xsl:param name="sequenceName"/>
    <xsl:choose>
      <xsl:when test="attKey='PK'">
        <xsl:choose>
          <xsl:when test="attType='serial'">
          <xsl:value-of select="attName"/> integer 
          NOT NULL PRIMARY KEY default nextval('<xsl:value-of select="$sequenceName"/>')
          </xsl:when>
          <xsl:otherwise>
            <!-- pk is not a serial -->
            <xsl:value-of select="attName"/> <xsl:value-of select="attType" /> 
          NOT NULL PRIMARY KEY )

          </xsl:otherwise>
        </xsl:choose>
        </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="attName"/> <xsl:text> </xsl:text> <xsl:value-of select="attType"/> <xsl:call-template name="handleNotNull"/>
      </xsl:otherwise>
    </xsl:choose>


    <!-- Comma unless it is the last attribute -->
    <xsl:choose>
      <!-- I wish I knew why I need to decrement last() in this case ??? well it works -->
      <xsl:when test="position() = last()"></xsl:when>
      <xsl:otherwise>, </xsl:otherwise>
    </xsl:choose>

  </xsl:template>


  <!--
    Handle Not Null
  -->
  <xsl:template name="handleNotNull">
    <xsl:if test="attNulls = 'no'"> NOT NULL</xsl:if>
  </xsl:template>
 

 <!--
    Default match rule is shut up
 -->
 <xsl:template match="*"/>

</xsl:stylesheet>
