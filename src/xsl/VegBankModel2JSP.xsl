<?xml version="1.0" encoding="utf-8"?>
<!--
 * '$RCSfile: VegBankModel2JSP.xsl,v $'
 *  Authors: @author@
 *  Release: @release@
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-12-05 23:15:29 $'
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
  version="1.0"
  xmlns:bean="dummy"
  xmlns:html="dummy"
  xmlns:logic="dummy"
  xmlns:jsp="dummy"
  exclude-result-prefixes="bean html logic"
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

  <xsl:import href="utility/common.xsl"/>

  <xsl:output method="html" indent="yes"/>

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

   <xsl:variable name="lcEntityName">
     <xsl:call-template name="to-lower">
       <xsl:with-param name="text" select="$entityName"/>
     </xsl:call-template>     
   </xsl:variable>

   <xsl:variable name="aFile" select="concat($outdir, 'Display', $CappedEntityName, '.jsp')"/>

   <!-- Create a new file for this entity -->
   <redirect:write select="$aFile">

     <xsl:text disable-output-escaping="yes">
<![CDATA[
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
]]>
</xsl:text>

     <!-- Define the bean for this entity -->
     <!-- If passed a bean the same name as this entity use that otherwise use genericBean -->
     <logic:present name="{$lcEntityName}">
       <bean:define 
         id='{$lcEntityName}' 
         name="{$lcEntityName}" 
         type='org.vegbank.common.model.{$CappedEntityName}'
       />
     </logic:present>

     <logic:notPresent name="{$lcEntityName}">
       <bean:define 
         id='{$lcEntityName}' 
         name="genericBean" 
         type='org.vegbank.common.model.{$CappedEntityName}'
       />
     </logic:notPresent>
     

     <!-- Simple value attributes and relational key values -->
     <xsl:variable name="primativeAttribs" select="attribute[attRelType/@type != 'inverted']"/>
     <!-- One to one children -->
     <xsl:variable name="simpleChildObjects" select="attribute[attKey='FK' and attRelType/@type = 'normal']"/> 
     <!-- One to many children -->
     <xsl:variable name="childrenObjects"
       select="../entity/attribute[starts-with(attReferences, concat($entityName,'.') ) and attRelType/@type = 'inverted']"/>

   <table border="1">
     <xsl:apply-templates select="$primativeAttribs">
       <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
       <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
     </xsl:apply-templates>

     <xsl:apply-templates mode="ChildObject" select="$simpleChildObjects">
       <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
       <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
     </xsl:apply-templates>

     <xsl:apply-templates mode="ChildrenObjects" select="$childrenObjects">
       <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
       <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
       <xsl:with-param name="childEntityName" select="$CappedEntityName"/>
     </xsl:apply-templates>
   </table>
 
   </redirect:write>
 </xsl:template>

 <xsl:template match="attribute">
   <xsl:param name="lcEntityName"/>
   <xsl:param name="CappedEntityName"/>
   
   <xsl:variable name="lcAttName">
     <xsl:call-template name="to-lower">
       <xsl:with-param name="text">
         <xsl:value-of select="attName"/>
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>

   <tr>
     <td>
       <span class="category">
         <xsl:value-of select="attLabel"/>
       </span>       
     </td>
     <td> 
       <span class="item">
         <bean:write 
           name='{$lcEntityName}' 
           property='{$lcAttName}'
         />
       </span>
     </td>
   </tr>
 </xsl:template>


 <xsl:template match="attribute" mode="ChildObject">
   <xsl:param name="lcEntityName"/>
   <xsl:param name="CappedEntityName"/>
   
   <xsl:variable name="lcAttName">
     <xsl:call-template name="to-lower">
       <xsl:with-param name="text">
         <xsl:value-of select="attName"/>
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>

   <xsl:variable name="javaType">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text">
         <xsl:value-of select="substring-before(attReferences, '.')"/>
       </xsl:with-param>
     </xsl:call-template>     
   </xsl:variable>

   <xsl:variable name="lcJavaType">
     <xsl:call-template name="to-lower">
       <xsl:with-param name="text">
         <xsl:value-of select="$javaType"/>
       </xsl:with-param>
     </xsl:call-template>     
   </xsl:variable>

   <!-- Do not do self recursive -->
   <xsl:if test="$CappedEntityName != $javaType">
     <tr>
       <td><xsl:value-of select="attLabel"/></td>
       <td>
         <logic:present name="{$lcEntityName}" property="{$lcJavaType}object">
           Has as child ob
           <bean:define name="{$lcEntityName}" property="{$lcJavaType}object" id="{$lcJavaType}"/>

           <!-- put the object into the request -->
           <xsl:call-template name="jspExpression">
             <xsl:with-param 
               name="expression" 
               select="concat('request.setAttribute(&quot;',$lcJavaType ,'&quot;',',',$lcJavaType,');')"
             /> 
           </xsl:call-template>

           <jsp:include page="Display{$javaType}.jsp" flush="true"/>

         </logic:present>
       </td>
     </tr>   
   </xsl:if>

 </xsl:template>

 <xsl:template match="attribute" mode="ChildrenObjects">
   <xsl:param name="lcEntityName"/>
   <xsl:param name="CappedEntityName"/>

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

   <xsl:variable name="lcJavaType">
     <xsl:call-template name="to-lower">
       <xsl:with-param name="text">
         <xsl:value-of select="../entityName"/>
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>
   
   <tr>
     <td><xsl:value-of select="attLabel"/></td>
     <td>

       <logic:iterate id="{$lcJavaType}" name="{$lcEntityName}" property="{$variableName}s">
         
         <!-- put the object into the request -->
         <xsl:call-template name="jspExpression">
           <xsl:with-param 
             name="expression" 
             select="concat('request.setAttribute(&quot;',$lcJavaType ,'&quot;',',',$lcJavaType,');')"
           /> 
         </xsl:call-template>

         <jsp:include page="Display{$javaType}.jsp" flush="true"/>
         
       </logic:iterate>

     </td>
   </tr>

 </xsl:template>



 <!-- ********************************************************************* -->
 <!--  Put the ugliness of getting a jsp expression in a template           -->
 <!-- ********************************************************************* -->
 <xsl:template name="jspExpression">
   <xsl:param name="expression"/>
   <xsl:param name="modifier" select="''"/>
   
   <xsl:text disable-output-escaping="yes"> <![CDATA[ <%]]></xsl:text> <xsl:value-of select="$modifier"/>
   <xsl:value-of select="$expression"/>
   <xsl:text disable-output-escaping="yes"> <![CDATA[%>]]> </xsl:text>

 </xsl:template>

</xsl:stylesheet>
