<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<!--
 *	'$RCSfile: VegBankModel2SQL.xsl,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 21:34:35 $'
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
 Generate the vegbank sql create scripts from the xml schema for each module.
 Expects the module name to be passed in as a parameter called 'module'

 Assumes the following naming convention.
  commXXX   => community module table
  plantXXX  => plant module table
  all other => plot module table.
-->

  <xsl:output method="text" indent="no"/>
  
  <!-- Hack to handle the different sql scripts we want generated -->
  <xsl:param name="module"/>
  
  <xsl:template match="/dataModel">
    -- This is a generated SQL script for postgresql
    --
    <xsl:for-each select="entity">
      <xsl:call-template name="StepOne"/>
    </xsl:for-each>
    
    <xsl:for-each select="entity">
      <xsl:call-template name="StepTwo"/>
    </xsl:for-each>
    
  </xsl:template>

  <!--
    Logic to decide what entities to process
  -->
  <xsl:template name="StepOne">
    <!-- Which Module are we dealing with -->
    <xsl:choose>
      
      <!-- COMMUNITY -->
      <xsl:when test="$module='community'">
        <xsl:if test="module='community'">
          <xsl:call-template name="createTable"/>
        </xsl:if>  
      </xsl:when>
      
      <!-- PLANT -->
      <xsl:when test="$module='plant'">
        <xsl:if test="module='plant'">
          <xsl:call-template name="createTable"/>
        </xsl:if>  
      </xsl:when>
      
      <!-- PLOT -->
      <xsl:when test="$module='plot'">
        <xsl:if test="module='plot'">
          <xsl:call-template name="createTable"/>
        </xsl:if>  
      </xsl:when>
      
      <!-- ?????? -->
      <xsl:otherwise>
        <xsl:message>
          Please choose a valid module i.e. community, plot, plant
        </xsl:message>
      </xsl:otherwise>
      
    </xsl:choose>
    
  </xsl:template>

  <!--
    Logic to decide what entities to process
  -->
  <xsl:template name="StepTwo">
    <!-- Which Module are we dealing with -->
    <xsl:choose>
      
      <!-- COMMUNITY -->
      <xsl:when test="$module='community'">
        <xsl:if test="module='community'">
          <xsl:call-template name="alterTable"/>
        </xsl:if>  
      </xsl:when>
      
      <!-- PLANT -->
      <xsl:when test="$module='plant'">
        <xsl:if test="module='plant'">
          <xsl:call-template name="alterTable"/>
        </xsl:if>  
      </xsl:when>
      
      <!-- PLOT -->
      <xsl:when test="$module='plot'">
        <xsl:if test="module='plot'">
          <xsl:call-template name="alterTable"/>
        </xsl:if>  
      </xsl:when>
      
      <!-- ?????? -->
      <xsl:otherwise>
        <xsl:message>
          Please choose a valid module i.e. community, plot, plant
        </xsl:message>
      </xsl:otherwise>
      
    </xsl:choose>
    
  </xsl:template>


  <!--
    Most work done here for handling entities/tables
  -->
  <xsl:template name="createTable">

    <xsl:variable name="tableName" select="entityName"/>
    <xsl:variable name="sequenceName"><xsl:value-of select="attribute[attKey='PK']/attName"/>_seq</xsl:variable>
    
----------------------------------------------------------------------------
-- CREATE <xsl:value-of select="$tableName"/>
----------------------------------------------------------------------------


CREATE SEQUENCE <xsl:value-of select="$sequenceName"/>;

CREATE TABLE <xsl:value-of select="entityName"/>
(
    <xsl:apply-templates>
      <xsl:with-param name="sequenceName" select="$sequenceName"/> 
    </xsl:apply-templates>
      
PRIMARY KEY ( <xsl:value-of select="attribute[attKey='PK']/attName"/>  )
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
        <xsl:value-of select="attName"/> integer <xsl:call-template name="handleNotNull"/>
          default nextval('<xsl:value-of select="$sequenceName"/>'),
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="attName"/> <xsl:text> </xsl:text> <xsl:value-of select="attType"/> <xsl:call-template name="handleNotNull"/>,
      </xsl:otherwise>
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
