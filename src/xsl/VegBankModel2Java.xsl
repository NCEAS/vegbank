<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0"
  xmlns:redirect="http://xml.apache.org/xalan/redirect"
  xmlns:str="http://xsltsl.org/string"
  extension-element-prefixes="redirect">

  <xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/>
  <xsl:output method="text" indent="no"/>

  <xsl:param name="outdir"/>


  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="entity">
   <xsl:variable name="CappedEntityName">
     <xsl:call-template name="str:to-upper">
       <xsl:with-param name="text" select="substring(entityName, 1, 1)"/>
     </xsl:call-template>
     <xsl:value-of select="substring(entityName, 2)"/>
   </xsl:variable>
   <xsl:variable name="aFile" select="concat($outdir, $CappedEntityName, '.java')"/>

   <xsl:message>
     <xsl:value-of select="$aFile"/>
   </xsl:message>
   <redirect:write select="$aFile">
/*
 * This is an auto-generated file
 */

package org.vegbank.common.model;

import java.util.Vector;
import java.util.AbstractList;

public class <xsl:value-of select="$CappedEntityName"/> 
{
   <xsl:choose>
     <xsl:when test="javaType/@relation= 'many'">
       <xsl:variable name="javaType" select="AbstractList"/>
     </xsl:when>
     <xsl:otherwise>
       <xsl:value-of select="javaType/@name"/>
       <xsl:variable name="javaType" select="javaType/@name"/>       
     </xsl:otherwise>
   </xsl:choose>

    <xsl:variable name="primativeAttribs" select="attribute[javaType/@type = 'primative']"/>
    <xsl:variable name="simpleRelationalAttribs" select="attribute[javaType/@type='object' and javaType/@relation='one']"/>
    <xsl:variable name="complexRelationalAttribs" select="attribute[javaType/@type='object' and javaType/@relation='many']"/>

    <xsl:apply-templates mode="declareAttrib" select="$primativeAttribs"/>
    <xsl:apply-templates mode="declareAttrib" select="$simpleRelationalAttribs"/>
    
    <!-- ????? -->
    <xsl:apply-templates mode="declareAttrib" select="$complexRelationalAttribs"/>
    <xsl:apply-templates mode="declareComplexAttrib" select="$complexRelationalAttribs"/>

    <xsl:apply-templates mode="get-setAttrib" select="$primativeAttribs"/>
    <xsl:apply-templates mode="get-setAttrib" select="$simpleRelationalAttribs"/>
    <xsl:apply-templates mode="get-setComplexAttrib" select="$complexRelationalAttribs"/>

}
    </redirect:write>
 </xsl:template>

 <xsl:template match="attribute" mode="declareAttrib">
   <xsl:variable name="javaType">
     <xsl:call-template name="str:to-upper">
       <xsl:with-param name="text" select="substring(javaType/@name, 1, 1)"/>
     </xsl:call-template>
     <xsl:value-of select="substring(javaType/@name, 2)"/>
   </xsl:variable>
  private <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> <xsl:value-of select="attName"/>; 
 </xsl:template>

 <xsl:template match="attribute" mode="declareComplexAttrib">
   <xsl:variable name="javaType">
     <xsl:call-template name="str:to-upper">
       <xsl:with-param name="text" select="substring(javaType/@name, 1, 1)"/>
     </xsl:call-template>
     <xsl:value-of select="substring(javaType/@name, 2)"/>
   </xsl:variable>   
  private Vector <xsl:text> </xsl:text> <xsl:value-of select="concat(attName,'s')"/>; 
 </xsl:template>


 <xsl:template match="attribute" mode="get-setAttrib">
   <xsl:variable name="CappedAttName">
     <xsl:call-template name="str:to-upper">
       <xsl:with-param name="text" select="substring(attName, 1, 1)"/>
     </xsl:call-template>
     <xsl:value-of select="substring(attName, 2)"/>
   </xsl:variable>

   <xsl:variable name="javaType">
     <xsl:call-template name="str:to-upper">
       <xsl:with-param name="text" select="substring(javaType/@name, 1, 1)"/>
     </xsl:call-template>
     <xsl:value-of select="substring(javaType/@name, 2)"/>
   </xsl:variable>
  /**
   * Set the value for <xsl:value-of select="attName"/>
   */
   public void set<xsl:value-of select="$CappedAttName"/>( <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> <xsl:value-of select="attName"/>)
  {
    this.<xsl:value-of select="attName"/> = <xsl:value-of select="attName"/>;
  }

  /**
   * Get the value for <xsl:value-of select="attName"/>
   */
  public <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> get<xsl:value-of select="$CappedAttName"/>()
  {
    return this.<xsl:value-of select="attName"/>;
  }
 </xsl:template>

 <xsl:template match="attribute" mode="get-setComplexAttrib">
   <xsl:variable name="CappedAttName">
     <xsl:call-template name="str:to-upper">
       <xsl:with-param name="text" select="substring(attName, 1, 1)"/>
     </xsl:call-template>
     <xsl:value-of select="substring(attName, 2)"/>
   </xsl:variable>

   <xsl:variable name="javaType">
     <xsl:call-template name="str:to-upper">
       <xsl:with-param name="text" select="substring(javaType/@name, 1, 1)"/>
     </xsl:call-template>
     <xsl:value-of select="substring(javaType/@name, 2)"/>
   </xsl:variable>

  /**
   * Set the value for <xsl:value-of select="attName"/>
   */
   public void set<xsl:value-of select="$CappedAttName"/>s( Vector <xsl:value-of select="attName"/>)
  {
    this.<xsl:value-of select="attName"/>s = <xsl:value-of select="attName"/>;
  }

  /**
   * Get the <xsl:value-of select="attName"/>s
   */
  public AbstractList get<xsl:value-of select="$CappedAttName"/>s()
  {
    return this.<xsl:value-of select="attName"/>s;
  }

  /**
   * Add a <xsl:value-of select="attName"/>
   */
  public void add<xsl:value-of select="$CappedAttName"/>(<xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> <xsl:value-of select="attName"/> )
  {
    this.<xsl:value-of select="attName"/>s.add(<xsl:value-of select="attName"/>);
  }
 </xsl:template>


</xsl:stylesheet>