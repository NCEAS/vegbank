<?xml version="1.0"?>
<!--
 * '$RCSfile: UtilityXMLToJavaBeans.xsl,v $'
 *  Authors: @author@
 *  Release: @release@
 *
 *  '$Author: anderson $'
 *  '$Date: 2005-01-19 00:07:22 $'
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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0"
  xmlns:redirect="http://xml.apache.org/xalan/redirect"
  extension-element-prefixes="redirect">

  <!-- ***************************************************************************** -->
  <!-- 
	Used to create utility javabeans for getting lists of countries and states 
	initially.
  -->
  <!-- ***************************************************************************** -->
  
  <xsl:import href="utility/common.xsl"/>
  
  <xsl:output method="text" indent="no"/>

  <xsl:param name="outdir"/>


  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="entity">

   <xsl:variable name="entityName" select="entityName"/>
   <xsl:variable name="CappedEntityName">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text" select="$entityName"/>
     </xsl:call-template>
   </xsl:variable>
   <xsl:variable name="aFile" select="concat($outdir, $CappedEntityName, '.java')"/>
  <!--xsl:if test="module!='extra'"-->
   <redirect:write select="$aFile">
/*
 * This is an auto-generated javabean 
 */

package org.vegbank.common.model.utility;
 
import java.util.Vector;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import org.apache.struts.util.LabelValueBean;
import org.vegbank.common.utility.LogUtility;

/**
 * &lt;p&gt;<xsl:value-of select="entitySummary"/>&lt;/p&gt;
 *
 * &lt;p&gt;<xsl:value-of select="entityDescription"/>&lt;/p&gt;
 *
 */
public class <xsl:value-of select="$CappedEntityName"/> implements Serializable
{

  public <xsl:value-of select="$CappedEntityName"/>()
  {
  }

  <xsl:variable name="Lists" select="attribute[attList]"/>

  <xsl:apply-templates select="$Lists" mode="Lists"/>

  /* Lookup the name(label) using the value, this is not the most preformant implementation but it allows
   * a message to be logged if duplicates found.
   * Returns null if nothing found.
   */
  public String getName(String value)
  {
    String result = null;
    Iterator lvbeans = <xsl:value-of select="attribute/attName"/>LabelValueBeans.iterator();
    while ( lvbeans.hasNext() )
    {
      LabelValueBean lvb = (LabelValueBean) lvbeans.next();
      String beanValue = lvb.getValue();
      if ( beanValue.equalsIgnoreCase(value) )
      {
        if ( result == null )
        {
          result = lvb.getLabel();
        }
        else
        {
          LogUtility.log("WARNING: Non unique value found in <xsl:value-of select="$CappedEntityName"/>");
        }
      }   
    }
    return result;
  }

  /* Lookup the value using the name, this is not the most preformant implementation but it allows
   * a message to be logged if duplicates found.
   * Returns null if nothing found.
   */
  public String getValue(String name)
  {
    String result = null;
    Iterator lvbeans = <xsl:value-of select="attribute/attName"/>LabelValueBeans.iterator();
    while ( lvbeans.hasNext() )
    {
      LabelValueBean lvb = (LabelValueBean) lvbeans.next();
      String beanLabel = lvb.getLabel();
      if ( beanLabel.equalsIgnoreCase(name) )
      {
        if ( result == null )
        {
          result = lvb.getValue();
        }
        else
        {
          LogUtility.log("WARNING: Non unique name found in <xsl:value-of select="$CappedEntityName"/>");
        }
      }   
    }
    return result;
  }

}

    </redirect:write>
 <!--/xsl:if-->
 </xsl:template>

 <xsl:template match="attribute" mode="Lists">

   <xsl:variable name="cappedVariableName">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text" select="attName"/>
     </xsl:call-template>
   </xsl:variable>
  
   private static List <xsl:value-of select="attName"/>LabelValueBeans = new ArrayList();

   // populate the List statically
   {
     <xsl:for-each select="attList/attListItem">
       <xsl:value-of select="../../attName"/>LabelValueBeans.add( new LabelValueBean("<xsl:value-of select="attListName"/>", "<xsl:value-of select="attListValue"/>" ) );
     </xsl:for-each>

     // Sort these alphabetically
     // This comparator is not availible yet ( in the newer struts code )
     //Collections.sort( <xsl:value-of select="attName"/>LabelValueBeans, LabelValueBean.CASE_INSENSITIVE_ORDER);
   }

   public Collection getAll<xsl:value-of select="$cappedVariableName"/>Names()
   {
     return  <xsl:value-of select="attName"/>LabelValueBeans;
   }
   </xsl:template>

</xsl:stylesheet>
