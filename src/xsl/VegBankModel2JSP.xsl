<?xml version="1.0" encoding="utf-8"?>
<!--
 * '$RCSfile: VegBankModel2JSP.xsl,v $'
 *  Authors: @author@
 *  Release: @release@
 *
 *  '$Author: anderson $'
 *  '$Date: 2004-04-30 13:07:53 $'
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
  xmlns:bean="dummy"
  xmlns:html="dummy"
  xmlns:logic="dummy"
  xmlns:jsp="dummy"
  exclude-result-prefixes="bean html logic"
  xmlns:redirect="http://xml.apache.org/xalan/redirect"
  extension-element-prefixes="redirect">

  <!-- ***************************************************************************** -->
  <!-- 
       Generates jsp source code from the data schema definition file
       This uses the xalan "redirect" extension to generate many outputs 
       When the XSL standard comes up with a standard way of doing this
       the method should be changed, right now this XSL depends on Xalan.
  -->
  <!-- ***************************************************************************** -->

  <xsl:import href="utility/common.xsl"/>

  <xsl:output method="html" indent="yes"/>

  <xsl:param name="outdir"/>

   <!-- Match the root element and apply all templates -->
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>


  <!-- ********************************************************************* -->
  <!--  Match all the entities in the XML                                    -->
  <!-- ********************************************************************* -->
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

     <!-- Generated files need access to some resources, defined here -->
     <xsl:text disable-output-escaping="yes">
<![CDATA[
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
]]>
</xsl:text>
     <xsl:comment>Get the accessionCode from the reqest object</xsl:comment>
     <xsl:text disable-output-escaping="yes">
	&lt;% String accessionCode = (String) request.getAttribute("accessionCode"); %&gt;
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
     

     <!-- Logic regarding to display or not -->
     <logic:equal name="DisplayProps" value="1" property="expandStatus({$lcEntityName})">

       <!-- Entire Entity -->
       <table>
         <tr>
           <td>
             <table> 
               <tr colspan="100">
                          

                 <xsl:comment>Control to contract this composite</xsl:comment>
                 <th valign="top">
                   <a name="{$lcEntityName}"/>
                   <html:link action="GenericDispatcher?command=RetrieveVBModelBean&amp;jsp=GenericDisplay.jsp&amp;contractEntity={$lcEntityName}" paramId="accessionCode" paramName="accessionCode" anchor="{$lcEntityName}" title="View {$entityName} Summary">
                     <img valign="top" src="/vegbank/images/yellow_minus.gif"/>
                   </html:link>       
                 </th>

                 <xsl:comment>The Detail Table for  <xsl:value-of select="$CappedEntityName"/> </xsl:comment>
                 <th align="left" colspan="100"><xsl:value-of select="$CappedEntityName"/></th>
               </tr>
               <tr>
                 <td><xsl:comment>Empty cell for contract/expand control</xsl:comment></td>
                 <td>
                   <xsl:comment>Contents of this entity</xsl:comment>
                   <table cellpadding="0" cellspacing="0" border="1" width="100%">
                     
                     <!-- Display all simple attributes -->
                     <xsl:apply-templates select="$primativeAttribs">
                       <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
                       <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
                     </xsl:apply-templates>
         
                     <!-- Display all Child Object attributes -->
                     <xsl:apply-templates mode="ChildObject" select="$simpleChildObjects">
                       <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
                       <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
                     </xsl:apply-templates>
                     
                     
                     <!-- Display all One to many Child Object attributes -->
                     <xsl:apply-templates mode="ChildrenObjects" select="$childrenObjects">
                       <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
                       <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
                     </xsl:apply-templates>

                   </table><xsl:comment>End of entity content table</xsl:comment>
                 </td>
               </tr>
             </table>
           </td>
         </tr>
       </table>
     </logic:equal>

     <logic:equal name="DisplayProps" value="0" property="expandStatus({$lcEntityName})">
       <xsl:comment>Summary Table</xsl:comment>
       <tr>
             
         <!-- One row summary -->
         <xsl:comment>The Summary Row for  <xsl:value-of select="$CappedEntityName"/> </xsl:comment>

         
         <th valign="top">
           <a name="{$lcEntityName}"/>
           <html:link action="GenericDispatcher?command=RetrieveVBModelBean&amp;jsp=GenericDisplay.jsp&amp;expandEntity={$lcEntityName}" anchor="{$lcEntityName}" paramId="accessionCode" paramName="accessionCode" title="View {$entityName} Detail">
             <img valign="top" src="/vegbank/images/yellow_plus.gif"/>
           </html:link>
         </th>
         
         <xsl:apply-templates mode="displaySummaryRow" select=".">
           <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
           <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
         </xsl:apply-templates>

       </tr>
       <xsl:comment>End Summary Row</xsl:comment>
     </logic:equal>
     <!-- End Display or not logic -->
     
 
   </redirect:write>
 </xsl:template>

 <!-- ********************************************************************* -->
 <!--  Display simple attributes                                            -->
 <!-- ********************************************************************* -->
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
     <xsl:call-template name="rowHeader">
       <xsl:with-param name="rowLabel" select="attLabel"/>
     </xsl:call-template>
     <td class="listRowA" width="200"> 
       <span class="item">
         <bean:write 
           name='{$lcEntityName}' 
           property='{$lcAttName}'
         />
       </span>
     </td>
   </tr>
 </xsl:template>



 <!-- ********************************************************************* -->
 <!--  Display Object attributes                                            -->
 <!-- ********************************************************************* -->
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

   <logic:present name="{$lcEntityName}" property="{$lcJavaType}object">

     <tr>
       <td colspan="100">
         <xsl:comment>The  Cell to display <xsl:value-of select="$lcJavaType"/> </xsl:comment>
         <table>

           <xsl:call-template name="SummaryTitleRow">
             <xsl:with-param name="entityName" select="$javaType"/>
           </xsl:call-template>
           <xsl:apply-templates mode="SummaryFieldsRow" select="../../entity[entityName=$lcJavaType]"/>

     
           <!-- Get the bean for this object -->
           <bean:define name="{$lcEntityName}" property="{$lcJavaType}object" id="{$lcJavaType}"/>

           <!-- put the object into the request -->
           <xsl:call-template name="jspExpression">
             <xsl:with-param 
               name="expression" 
               select="concat('request.setAttribute(&quot;',$lcJavaType ,'&quot;',',',$lcJavaType,');')"
               /> 
           </xsl:call-template>
     
           <jsp:include page="Display{$javaType}.jsp" flush="true"/>
  

           <xsl:apply-templates mode="SummaryFieldsRow" select="../../entity[entityName=$lcJavaType]"/>
           <xsl:call-template name="SummaryTitleRow">
             <xsl:with-param name="entityName" select="$javaType"/>
           </xsl:call-template>

         </table>
       </td> <xsl:comment>End cell to display <xsl:value-of select="$lcEntityName"/> </xsl:comment>
     </tr>
         
   </logic:present>
   
 </xsl:template>


 <!-- ********************************************************************* -->
 <!--  Display one to many Objects                                          -->
 <!-- ********************************************************************* -->
 <xsl:template match="attribute" mode="ChildrenObjects">
   <xsl:param name="lcEntityName"/>
   <xsl:param name="CappedEntityName"/>

   <xsl:variable name="childEntityName">
     <xsl:call-template name="UpperFirstLetter">
       <xsl:with-param name="text">
         <xsl:value-of select="../entityName"/>
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>
                       
   <xsl:variable name="lcChildEntityName">
     <xsl:call-template name="to-lower">
       <xsl:with-param name="text">
         <xsl:value-of select="../entityName"/>
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>


   <xsl:variable name="variableName">
     <xsl:call-template name="to-lower">
       <xsl:with-param name="text">
         <xsl:value-of select="substring-before(attName, '_ID')"/>_<xsl:value-of select="$childEntityName"/>
       </xsl:with-param>
     </xsl:call-template>
   </xsl:variable>
      
   <logic:notEmpty name="{$lcEntityName}" property="{$variableName}s">
     <tr>
       <td colspan="100">
         <xsl:comment>The  Cell to display <xsl:value-of select="$childEntityName"/> </xsl:comment>
         <table>
           <!-- Summary title and fields Header -->
           <xsl:call-template name="SummaryTitleRow">
             <xsl:with-param name="entityName" select="$childEntityName"/>
           </xsl:call-template>
           <xsl:apply-templates mode="SummaryFieldsRow" select=".."/>



           <xsl:comment>The view of each  <xsl:value-of select="$childEntityName"/> </xsl:comment>
           <logic:iterate id="{$lcChildEntityName}" name="{$lcEntityName}" property="{$variableName}s">
             
             <!-- put the object into the request -->
             <xsl:call-template name="jspExpression">
               <xsl:with-param 
                 name="expression" 
                 select="concat('request.setAttribute(&quot;',$lcChildEntityName ,'&quot;',',',$lcChildEntityName,');')"
                 /> 
             </xsl:call-template>
           
             <jsp:include page="Display{$childEntityName}.jsp" flush="true"/>
           
           </logic:iterate>
           <xsl:comment>End  view of each  <xsl:value-of select="$childEntityName"/> </xsl:comment>

           <!--
           <xsl:call-template name="ChildrenObjects">
             <xsl:with-param name="lcEntityName" select="$lcEntityName"/>
             <xsl:with-param name="CappedEntityName" select="$CappedEntityName"/>
             <xsl:with-param name="childEntityName" select="$childEntityName"/>
             <xsl:with-param name="lcChildEntityName" select="$lcChildEntityName"/>
           </xsl:call-template>
           -->

           <!-- Summary title and fields Footer -->
           <xsl:apply-templates mode="SummaryFieldsRow" select=".."/>
           <xsl:call-template name="SummaryTitleRow">
             <xsl:with-param name="entityName" select="$childEntityName"/>
           </xsl:call-template>

         </table>
       </td> <xsl:comment>End cell to display <xsl:value-of select="$childEntityName"/> </xsl:comment>
     </tr>
   </logic:notEmpty>
   
 </xsl:template>
   

 <!-- ********************************************************************* -->
 <!--  Display each Child Object                                            -->
 <!-- ********************************************************************* -->
 <xsl:template name="ChildrenObjects">
   <xsl:param name="lcEntityName"/>
   <xsl:param name="CappedEntityName"/>
   <xsl:param name="childEntityName"/>
   <xsl:param name="lcChildEntityName"/>


   
 </xsl:template>

 <!-- ********************************************************************* -->
 <!--  Display Summary in a row                                          -->
 <!-- ********************************************************************* -->
 <xsl:template match="entity" mode="displaySummaryRow">
   <xsl:param name="lcEntityName"/>
   <xsl:param name="CappedEntityName"/>
   
   <xsl:comment>The Values of the summary Row</xsl:comment>
   <xsl:for-each select="attribute[attForms/formShow/@name='Summary'  and attForms/formShow[@name = 'Summary'] != -1]">
     <xsl:sort data-type="number" select="attForms/formShow[@name='Summary']" />
     <!--<xsl:message><xsl:value-of select="attForms/formShow[@name='Summary']"/></xsl:message>-->

     <xsl:variable name="lcAttName">
       <xsl:call-template name="to-lower">
         <xsl:with-param name="text">
           <xsl:value-of select="attName"/>
         </xsl:with-param>
       </xsl:call-template>
     </xsl:variable>
     
     <td class="listRowA">
       <span class="item">
         <bean:write 
           name='{$lcEntityName}' 
           property='{$lcAttName}'
           />
       </span>
     </td>
   </xsl:for-each>
 </xsl:template>


 <!-- ********************************************************************* -->
 <!--  Display the Summary FieldNames                                       -->
 <!-- ********************************************************************* -->
 <xsl:template match="entity" mode="SummaryFieldsRow">
   <xsl:comment>Summary fields header row</xsl:comment>
   <tr>
     <td><xsl:comment>Empty cell for expand contract icon</xsl:comment></td>
     <xsl:for-each select="attribute[attForms/formShow/@name='Summary'  and attForms/formShow[@name = 'Summary'] != -1]">
       <xsl:sort data-type="number" select="attForms/formShow[@name='Summary']" />
       <th class="gdAttName">
         <xsl:value-of select="attName"/>
       </th>
     </xsl:for-each>
   </tr>
   <xsl:comment>End summary fields header row</xsl:comment>
 </xsl:template>

 <!-- ********************************************************************* -->
 <!--  SummaryTitle Row                                                     -->
 <!-- ********************************************************************* -->
 <xsl:template name="SummaryTitleRow">
   <xsl:param name="entityName"/>
   <tr>
     <th colspan="100" align="left" class="gdEntityName"><xsl:value-of select="$entityName"/></th>
   </tr>
 </xsl:template>

 <!-- ********************************************************************* -->
 <!--  Display the rowHeader Label                                          -->
 <!-- ********************************************************************* -->
 <xsl:template name="rowHeader">
   <xsl:param name="rowLabel"/>

   <xsl:comment>The row label <xsl:value-of select="$rowLabel"/></xsl:comment>
   <th valign="top" align="left" class="gdRowLabel" width="1">
       <xsl:value-of select="$rowLabel"/>
   </th>
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
