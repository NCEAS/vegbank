<?xml version="1.0"?>
<!--
 * '$RCSfile: VegBankModel2Java.xsl,v $'
 *  Authors: @author@
 *  Release: @release@
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-10-25 01:48:41 $'
 *  '$Revision: 1.10 $'
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
       Generated java source code from the data schema definition file
       This uses the xalan "redirect" extension to generate many outputs 
       When the XSL standard comes up with a standard way of doing this
       the method should be changed, right now this XSL depends on Xalan.
  -->
  <!-- ***************************************************************************** -->

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

   <redirect:write select="$aFile">
/*
 * This is an auto-generated javabean 
 */

package org.vegbank.common.model;

import java.util.Vector;
import java.util.Collection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Iterator;

public class <xsl:value-of select="$CappedEntityName"/> implements Serializable
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
   
   <xsl:variable name="primativeAttribs" select="attribute[attRelType/@type = 'n/a' or attKey='PK']"/>
   <xsl:variable name="FKAttribs" select="attribute[attRelType/@type = 'normal' or attRelType/@type='inverted']"/>
   <!--<xsl:variable name="simpleRelationalAttribs" select="attribute[javaType/@type='Object' and javaType/@relation='one']"/>-->
   <!--   <xsl:variable name="simpleRelationalAttribs" select="attribute[javaType/@type='int' and javaType/@relation='one']"/>-->
    <!--    <xsl:variable name="complexRelationalAttribs" select="../entity/attribute/attReferences[starts-with(text(),$entityName)]"/> -->

    <xsl:apply-templates mode="declareAttrib" select="$primativeAttribs"/>
    <xsl:apply-templates mode="declareAttrib" select="$FKAttribs"/>
    <xsl:apply-templates mode="declareObjectAttrib" select="$FKAttribs"/>
    <!--<xsl:apply-templates mode="declareObjectAttrib" select="$simpleRelationalAttribs"/>-->
    
    <!--<xsl:apply-templates mode="declareComplexAttrib" select="$complexRelationalAttribs"/>-->

    <xsl:apply-templates mode="get-setSimpleAttrib" select="$primativeAttribs"/>
    <xsl:apply-templates mode="get-setSimpleAttrib" select="$FKAttribs"/>
    <xsl:apply-templates mode="get-setObjectAttrib" select="$FKAttribs"/>
    <!--<xsl:apply-templates mode="get-setObjectAttrib" select="$simpleRelationalAttribs"/>-->
    <!--<xsl:apply-templates mode="get-setComplexAttrib" select="$complexRelationalAttribs"/>-->

    <!-- choose all entities that have an inverted FK relationship to this entity -->
    <xsl:for-each select="../entity/attribute[starts-with(attReferences, concat(current()/entityName,'.') ) and attRelType/@type = 'inverted']">
      
      <xsl:variable name="javaType">
        <xsl:call-template name="UpperFirstLetter">
          <xsl:with-param name="text">
            <xsl:value-of select="../entityName"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="variableName">
        <xsl:call-template name="to-lower">
          <xsl:with-param name="text">
            <xsl:value-of select="substring-before(attName, '_ID')"/>_<xsl:value-of select="$javaType"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:variable>

    private List <xsl:value-of select="$variableName"/>s = new ArrayList();

    public void set<xsl:value-of select="$variableName"/>s ( List <xsl:value-of select="$variableName"/>s )
    {
      this.<xsl:value-of select="$variableName"/>s = <xsl:value-of select="$variableName"/>s;
    }

    public List get<xsl:value-of select="$variableName"/>s()
    {
      return this.<xsl:value-of select="$variableName"/>s;
    }

    public void add<xsl:value-of select="$variableName"/> ( <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> <xsl:value-of select="$variableName"/> )
    {
      this.<xsl:value-of select="$variableName"/>s.add( <xsl:value-of select="$variableName"/> );
    }
    
    
    </xsl:for-each>

 <!-- ***************************************************************************** -->
 <!-- Generate toXML() method  -->
 <!-- ***************************************************************************** -->
    public String toXML(int indent)
    {
      this.indent = indent; 
      return this.toXML();
    }   
 
    private int indent = 0;
    
    public String toXML()
    {
      StringBuffer xml = new StringBuffer();
      xml.append(getIdent( indent ) + "&lt;<xsl:value-of select="$entityName"/>&gt;\n");
      indent = indent +1 ;
    <xsl:for-each select="attribute">

      <xsl:variable name="javaType">
        <xsl:call-template name="UpperFirstLetter">
         <xsl:with-param name="text">
           <xsl:value-of select="substring-before(attReferences, '.')"/>
         </xsl:with-param>
        </xsl:call-template>     
      </xsl:variable>
      

      <xsl:variable name="XMLElementName" select="concat($entityName, '.', ./attName )"/>
      
      <xsl:variable name="cappedVariableName">
        <xsl:choose>
          <xsl:when test="attKey='FK'">
            <xsl:call-template name="UpperFirstLetter">
              <xsl:with-param name="text" select="concat(substring-before(attName, '_ID'), 'object')"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="UpperFirstLetter">
              <xsl:with-param name="text" select="attName"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:choose>
        <xsl:when test="./attRelType/@type ='n/a' and attKey!='PK'">
      if ( this.get<xsl:value-of select="$cappedVariableName"/>() != null)
      {
        xml.append(getIdent( indent ) + "&lt;<xsl:value-of select="$XMLElementName"/>&gt;");
        xml.append(escapeXML(this.get<xsl:value-of select="$cappedVariableName"/>()) );
        xml.append("&lt;/<xsl:value-of select="$XMLElementName"/>&gt;\n");
      }
        </xsl:when>
        <xsl:when test="attKey='PK'">
      if ( this.get<xsl:value-of select="$cappedVariableName"/>() != 0)
      {
        xml.append(getIdent( indent ) + "&lt;<xsl:value-of select="$XMLElementName"/>&gt;");
        xml.append(this.get<xsl:value-of select="$cappedVariableName"/>() );
        xml.append("&lt;/<xsl:value-of select="$XMLElementName"/>&gt;\n");
      }
        </xsl:when>
        <xsl:when test="./attRelType/@type ='normal'">
      if ( this.get<xsl:value-of select="$cappedVariableName"/>() != null)
      {
        xml.append(getIdent( indent ) + "&lt;<xsl:value-of select="$XMLElementName"/>&gt;\n");
        <xsl:value-of select="$javaType"/> object = this.get<xsl:value-of select="$cappedVariableName"/>();
        xml.append( object.toXML(indent + 1) );
        xml.append(getIdent( indent ) + "&lt;/<xsl:value-of select="$XMLElementName"/>&gt;\n");
      }
        </xsl:when>
        <xsl:otherwise>
          // Got <xsl:value-of select="./javaType/@type"/> <xsl:value-of select="./attName"/>
          xml.append(getIdent( indent ) + "&lt;!-- Ignoring <xsl:value-of select="$XMLElementName"/>  --&gt;\n");
        </xsl:otherwise>
      </xsl:choose>

    </xsl:for-each>
    
     <!-- ***************************************************************************** -->
     <!-- Handle one to many's   -->
     <!-- ***************************************************************************** -->
     <!-- choose all entities that have an inverted FK relationship to this entity -->
    <xsl:for-each select="../entity/attribute[starts-with(attReferences, concat(current()/entityName,'.') ) and attRelType/@type = 'inverted'] ">
      
      <xsl:variable name="javaType">
        <xsl:call-template name="UpperFirstLetter">
          <xsl:with-param name="text">
            <xsl:value-of select="../entityName"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:variable>
      
      <xsl:variable name="variableName">
        <xsl:call-template name="to-lower">
          <xsl:with-param name="text">
            <xsl:value-of select="substring-before(attName, '_ID')"/>_<xsl:value-of select="$javaType"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:variable>

      if ( this.get<xsl:value-of select="$variableName"/>s() != null)
      {
        List list = this.get<xsl:value-of select="$variableName"/>s();
         Iterator iterator = list.iterator();
        while ( iterator.hasNext() )
        {
          <xsl:value-of select="$javaType"/> object = (<xsl:value-of select="$javaType"/>) iterator.next();
          xml.append( object.toXML( indent + 1) );
        };
      }
    </xsl:for-each>

      xml.append(getIdent( indent -1 ) +"&lt;/<xsl:value-of select="$entityName"/>&gt;\n");
      return xml.toString();
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    // These should  be of in a Utility class, need to sort out build order first
    ////////////////////////////////////////////////////////////////////////////////
        
    // this should only exist once, but need to set classpath of compile to do that
    // org.vegbank.commom.utility.Utility has this method also
    private static String getIdent(int indent)
    {
      StringBuffer sb = new StringBuffer();
      for ( int i=0; i&lt;indent; i++)
      {
        sb.append("\t");
      }
      return sb.toString();
    }
    
    // The insanity ... having to escape the escapes .. see above
    private static String escapeXML(String str)
    {
      str = str.replaceAll("&amp;","&amp;amp;");
      str = str.replaceAll("&lt;","&amp;lt;");
      str = str.replaceAll("&gt;","&amp;gt;");
      str = str.replaceAll("\&quot;","&amp;quot;");
      str = str.replaceAll("&apos;","&amp;apos;");
      return str;
    }
}
    </redirect:write>
 </xsl:template>

 <!-- ***************************************************************************** -->
 <!-- Declare members  -->
 <!-- ***************************************************************************** -->

 <xsl:template match="attribute" mode="declareObjectAttrib">

   <xsl:variable name="javaType">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text">
         <xsl:value-of select="substring-before(attReferences, '.')"/>                
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>

   <xsl:variable name="memberName">
     <xsl:choose>
       <xsl:when test="attKey='FK' or attKey='PK'">
         <xsl:call-template name="to-lower">
           <xsl:with-param name="text">
             <xsl:value-of select="concat(substring-before(attName, '_ID'), 'object')"/>
           </xsl:with-param>
         </xsl:call-template>
       </xsl:when>
       <xsl:otherwise>
         <xsl:call-template name="to-lower">
           <xsl:with-param name="text">
             <xsl:value-of select="substring-before(attName, '_ID')"/>
           </xsl:with-param>
         </xsl:call-template>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
  private <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> <xsl:value-of select="$memberName"/>; 
 </xsl:template>

 <xsl:template match="attribute" mode="declareAttrib">
   <xsl:variable name="javaType">
     <xsl:choose>
       <xsl:when test="attKey='FK' or attKey='PK'">int</xsl:when>
       <xsl:otherwise>
         <xsl:value-of select="javaType/@type"/>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:variable>

  private <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> <xsl:value-of select="attName"/>; 
 </xsl:template>


 <!-- ***************************************************************************** -->
 <!-- Generate get and set methods -->
 <!-- ***************************************************************************** -->

 <!-- simple attributes -->
 <xsl:template match="attribute" mode="get-setSimpleAttrib">

   <xsl:variable name="javaType">
     <xsl:choose>
       <xsl:when test="attKey='FK' or attKey='PK'">int</xsl:when>
       <xsl:otherwise>
         <xsl:value-of select="javaType/@type"/>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:variable>

   <xsl:variable name="cappedVariableName">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text" select="attName"/>
     </xsl:call-template>
   </xsl:variable>
   <xsl:variable name="uncappedVariableName">
     <xsl:value-of select="attName"/>
   </xsl:variable>

   <xsl:call-template name="GenerateGetSet">
     <xsl:with-param name="javaType" select="$javaType"/>
     <xsl:with-param name="cappedVariableName" select="$cappedVariableName"/>
     <xsl:with-param name="uncappedVariableName" select="$uncappedVariableName"/>
   </xsl:call-template>

   <!-- Has a closed list of values generate a get${variable}PickList method-->
   <xsl:if test="attListType='closed'">
  private static Collection <xsl:value-of select="$uncappedVariableName"/>PickList;

  {
    <xsl:value-of select="$uncappedVariableName"/>PickList = new Vector();
    
    <xsl:for-each select="attList/attListItem">
      <xsl:sort select="attListSortOrd" data-type="number"/>
    <xsl:value-of select="$uncappedVariableName"/>PickList.add("<xsl:value-of select="attListValue"/>");
    </xsl:for-each>
  }
  
  public Collection get<xsl:value-of select="$cappedVariableName"/>PickList()
  {
    return <xsl:value-of select="$uncappedVariableName"/>PickList;
  }
   </xsl:if>

 </xsl:template>

 <!-- Generate gets and sets for Object members -->
 <xsl:template match="attribute" mode="get-setObjectAttrib">
   <xsl:variable name="javaType">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text">
         <xsl:value-of select="substring-before(attReferences, '.')"/>
       </xsl:with-param>
     </xsl:call-template>     
   </xsl:variable>

   <xsl:variable name="cappedVariableName">
     <xsl:choose>
       <xsl:when test="attKey='FK' or attKey='PK'">
         <xsl:call-template name="UpperFirstLetter">
           <xsl:with-param name="text">
             <xsl:value-of select="concat(substring-before(attName, '_ID'), 'object')"/>
           </xsl:with-param>
         </xsl:call-template>	     
       </xsl:when>
       <xsl:otherwise>
         <xsl:call-template name="UpperFirstLetter">
           <xsl:with-param name="text">
             <xsl:value-of select="substring-before(attName, '_ID')"/>
           </xsl:with-param>
         </xsl:call-template>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:variable>

   <xsl:variable name="uncappedVariableName">
   <!--
     <xsl:choose>
       <xsl:when test="attKey='FK' or attKey='PK'"><xsl:value-of select="$cappedVariableName"/></xsl:when>
       <xsl:otherwise>
   -->
         <xsl:call-template name="to-lower">
           <xsl:with-param name="text">
             <xsl:value-of select="$cappedVariableName"/>
           </xsl:with-param>
         </xsl:call-template>
       <!--
       </xsl:otherwise>
     </xsl:choose>
     -->
   </xsl:variable>

   <xsl:call-template name="GenerateGetSet">
     <xsl:with-param name="javaType" select="$javaType"/>
     <xsl:with-param name="cappedVariableName" select="$cappedVariableName"/>
     <xsl:with-param name="uncappedVariableName" select="$uncappedVariableName"/>
   </xsl:call-template>
   
 </xsl:template>

 <!-- Generate gets and sets for Forgien Keys -->
 <xsl:template match="attribute" mode="get-setFKAttrib">
   <xsl:variable name="javaType">
     <xsl:value-of select="substring-before(attReferences, '.')"/>
   </xsl:variable>

   <xsl:variable name="cappedVariableName">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text">
         <xsl:value-of select="substring-before(attName, '_ID')"/>
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>

   <xsl:variable name="uncappedVariableName">
     <xsl:value-of select="substring-before(attName, '_ID')"/>
   </xsl:variable>

   <xsl:call-template name="GenerateGetSet">
     <xsl:with-param name="javaType" select="$javaType"/>
     <xsl:with-param name="cappedVariableName" select="$cappedVariableName"/>
     <xsl:with-param name="uncappedVariableName" select="$uncappedVariableName"/>
   </xsl:call-template>
   
 </xsl:template>


 <!-- Set Complex Attributes , not used yet -->
 <xsl:template match="attribute" mode="get-setComplexAttrib">
   <xsl:variable name="CappedAttName">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text" select="attName"/>
     </xsl:call-template>
   </xsl:variable>

   <xsl:variable name="javaType">
     <xsl:call-template name="UpperFirstLetter">
      <xsl:with-param name="text" select="substring-before(attReferences, '.')"/>
     </xsl:call-template>
   </xsl:variable>

  /**
   * Set the value for <xsl:value-of select="attName"/>
   */
   public void set<xsl:value-of select="$CappedAttName"/>s( List <xsl:value-of select="attName"/>)
  {
    this.<xsl:value-of select="attName"/>s = <xsl:value-of select="attName"/>;
  }

  /**
   * Get the <xsl:value-of select="attName"/>s
   */
  public List get<xsl:value-of select="$CappedAttName"/>s()
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

 <!-- ***************************************************************************** -->
 <!-- Utility templates                                                             -->
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


 <xsl:template name="GenerateGetSet">
   <xsl:param name="javaType"/>
   <xsl:param name="cappedVariableName"/>
   <xsl:param name="uncappedVariableName"/>

  /**
   * Set the value for <xsl:value-of select="$uncappedVariableName"/>
   */
   public void set<xsl:value-of select="$cappedVariableName"/>( <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> <xsl:value-of select="$uncappedVariableName"/>)
  {
    this.<xsl:value-of select="$uncappedVariableName"/> = <xsl:value-of select="$uncappedVariableName"/>;
  }

  /**
   * Get the value for <xsl:value-of select="$uncappedVariableName"/>
   */
  public <xsl:value-of select="$javaType"/> <xsl:text> </xsl:text> get<xsl:value-of select="$cappedVariableName"/>()
  {
    return this.<xsl:value-of select="$uncappedVariableName"/>;
  }


 </xsl:template>

</xsl:stylesheet>
