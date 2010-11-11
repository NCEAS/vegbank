<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:vegbank="http://vegbank.org" xmlns:logic="http://vegbank.org" xmlns:bean="http://vegbank.org" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:jsp="http://vegbank.org" extension-element-prefixes="redirect">
  <!--  <xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/> -->
  <!-- comment out extenstion-elemtn ... though this comment to run locally without xalan -->
  <!--<xsl:param name="pathToWrite"/> -->
  <xsl:param name="viewsBuildFile">was not specified 1234567890</xsl:param>
  
  <xsl:variable name="pathToWrite"><xsl:value-of select="substring($viewsBuildFile,1,number(string-length($viewsBuildFile)-9))" />/autogen/</xsl:variable>
  <xsl:param name="quot">"</xsl:param>
  <xsl:param name="apos">'</xsl:param>
  <xsl:output method="html"/>
  <xsl:param name="alphalow">abcdefghijklmnopqrstuvwxyz</xsl:param>
  <xsl:param name="alphahigh">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:param>
  <xsl:template name="doViews">
    <xsl:param name="viewNum"/>
    <xsl:variable name="view_init" select="document('uniqueformlist.xml')/allforms/oneForm[position()=$viewNum]"/>
    <xsl:variable name="view" select="translate($view_init,$alphahigh,$alphalow)"/>
    <xsl:comment>Starting view <xsl:value-of select="$view"/> with number:<xsl:value-of select="$viewNum"/>
    </xsl:comment>
    <xsl:if test="string-length($view)&gt;0">
      <xsl:value-of select="$view"/>IS VIEW:
<xsl:call-template name="doEnts">
        <xsl:with-param name="view" select="$view"/>
      </xsl:call-template>
      <!-- call next view -->
      <xsl:call-template name="doViews">
        <xsl:with-param name="viewNum" select="number(1+$viewNum)"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  <xsl:template name="doEnts">
    
    <xsl:param name="view"/>
    <xsl:for-each select="/dataModel/entity">
      <xsl:variable name="countAttsInView" select="count(attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view])"/>
      <xsl:comment>
        <xsl:value-of select="entityName"/> has <xsl:value-of select="$countAttsInView"/> fields in view: <xsl:value-of select="$view"/>
      </xsl:comment>
      <xsl:if test="$countAttsInView&gt;0 or ((entityName='plot' or entityName='observation') and (substring($view,1,4)='plot'))">
        <!-- write those where there is a view for this ent, and  write plot and obs for all views starting with 'plot' -->
        <xsl:variable name="currEnt" select="translate(entityName,$alphahigh,$alphalow)"/>
        <!-- begin writing data here -->
        <xsl:choose>
          <xsl:when test="contains($view,'summary')">
            <!-- summary view: table with headers -->
            <xsl:comment>WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_head.jsp</xsl:comment>
            <redirect:write file="{$pathToWrite}{$currEnt}_{$view}_head.jsp">
              <xsl:for-each select="attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view]">
                <xsl:sort select="node()" data-type="number"/>
                <!-- add sort ? -->
                <xsl:choose>
                  <xsl:when test="@addSort='true'">
                    <!-- when sorting, just pass info to include -->
                        <xsl:element name="bean:define">
                          <xsl:attribute name="id">thisfield</xsl:attribute>
                          <xsl:attribute name="value"><xsl:value-of select="translate(../../attName,$alphahigh,$alphalow)" /><xsl:if test="string-length(../../attFKTranslationSQL)&gt;0">_transl</xsl:if></xsl:attribute>
                        </xsl:element>
                      
                        <bean:define id="fieldlabel"><xsl:value-of  select="../../attLabel"/></bean:define>
                      @subst_lt@%@ include file="/includes/orderbythisfield.jsp"  %@subst_gt@
                  </xsl:when>
                  <xsl:otherwise><!-- no sort -->

                  <xsl:call-template name="labelField">
                     <xsl:with-param name="container">th</xsl:with-param>
                    <xsl:with-param name="currEnt" select="$currEnt"/>
                    <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                    <xsl:with-param name="currLbl" select="../../attLabel"/>
                    <xsl:with-param name="currDefn" select="../../attDefinition" />
                    <xsl:with-param name="noDDLink" select="@noDDLink" />
                  </xsl:call-template>
                  </xsl:otherwise>
                </xsl:choose>
                
              </xsl:for-each>
            </redirect:write>
            <xsl:comment>END WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_head.jsp</xsl:comment>
            <xsl:comment>WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
            <redirect:write file="{$pathToWrite}{$currEnt}_{$view}_data.jsp">
              <xsl:for-each select="attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view]">
                <xsl:sort select="node()" data-type="number"/>
                <xsl:call-template name="writeField">
                  <xsl:with-param name="currEnt" select="$currEnt"/>
                  <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                  <xsl:with-param name="currentAtt" select="ancestor::attribute"/>
                  <xsl:with-param name="container">td</xsl:with-param>
                  <xsl:with-param name="view" select="$view" />
                  <xsl:with-param name="linkToPK" select="@linkToPK" /><!-- special link for summary to detail -->
                </xsl:call-template>
              </xsl:for-each>
            </redirect:write>
            <xsl:comment>END WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
          </xsl:when>
          <xsl:when test="contains($view,'notbl')">
            <!-- notbl view: just a <p>smalllabel:data <br/></p>-->
            <xsl:comment>WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
            <redirect:write file="{$pathToWrite}{$currEnt}_{$view}_data.jsp">
              <xsl:for-each select="attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view]">
                <xsl:sort select="node()" data-type="number"/>
                     <xsl:call-template name="labelField">
                        <xsl:with-param name="container">span</xsl:with-param>
                        <xsl:with-param name="containerClass">datalabelsmall</xsl:with-param>
                        <xsl:with-param name="suffix">: </xsl:with-param>
                        <xsl:with-param name="currEnt" select="$currEnt"/>
                        <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                        <xsl:with-param name="currLbl" select="../../attLabel"/>
                        <xsl:with-param name="currDefn" select="../../attDefinition" />
                        <xsl:with-param name="noDDLink" select="@noDDLink" />
                      </xsl:call-template>
                <xsl:call-template name="writeField">
                  <xsl:with-param name="currEnt" select="$currEnt"/>
                  <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                  <xsl:with-param name="currentAtt" select="ancestor::attribute"/>
                  <xsl:with-param name="container">span</xsl:with-param>
                  <xsl:with-param name="view" select="$view" />
                </xsl:call-template><br/>
              </xsl:for-each>
            </redirect:write>
            <xsl:comment>END WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
          </xsl:when>
          <xsl:otherwise>
            <!-- default view -->
            <xsl:comment>WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
            <redirect:write file="{$pathToWrite}{$currEnt}_{$view}_data.jsp">
              <xsl:for-each select="attribute/attForms/formShow[translate(@name,$alphahigh,$alphalow)=$view]">
                <xsl:sort select="node()" data-type="number"/>
                <logic:notEmpty name="onerowof{$currEnt}" property="{translate(../../attName,$alphahigh,$alphalow)}">
                  <tr class="@nextcolorclass@">
                      <xsl:call-template name="labelField">
                        <xsl:with-param name="container">td</xsl:with-param>
                        <xsl:with-param name="containerClass">datalabel</xsl:with-param>
                        <xsl:with-param name="currEnt" select="$currEnt"/>
                        <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                        <xsl:with-param name="currLbl" select="../../attLabel"/>
                        <xsl:with-param name="currDefn" select="../../attDefinition" />
                        <xsl:with-param name="noDDLink" select="@noDDLink" />
                      </xsl:call-template>
                    <xsl:call-template name="writeField">
                      <xsl:with-param name="currEnt" select="$currEnt"/>
                      <xsl:with-param name="currFld" select="translate(../../attName,$alphahigh,$alphalow)"/>
                      <xsl:with-param name="currentAtt" select="ancestor::attribute"/>
                     <xsl:with-param name="container">td</xsl:with-param>
                     <xsl:with-param name="view" select="$view" />
                     <xsl:with-param name="linkToPK" select="@linkToPK" />
                    </xsl:call-template>
                  </tr>
                  <bean:define id="hadData" value="true"/>
                </logic:notEmpty>
              </xsl:for-each>
            </redirect:write>
            <xsl:comment>END WRITE FILE: <xsl:value-of select="$currEnt"/>_<xsl:value-of select="$view"/>_data.jsp</xsl:comment>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:if>
    </xsl:for-each>
    <!--entity -->
  </xsl:template>
  <xsl:template match="/dataModel">
    <html>
      <xsl:call-template name="doViews">
        <xsl:with-param name="viewNum">1</xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
  <xsl:template name="writeField">
    <xsl:param name="currEnt"/>
    <xsl:param name="currFld"/>
    <xsl:param name="currentAtt"/>
    <xsl:param name="container" /><!-- p or td -->
    <xsl:param name="view" /><!-- name of this view -->
    <xsl:param name="linkToPK" /><!-- if true, then encapsulate in <a></a> -->
    <xsl:param name="writeSuffix" /><!-- write something after the field value -->
    <xsl:param name="altField" /><!-- true if writing an alternate field -->
    <!--    <xsl:comment>WRITE FIELD: <xsl:value-of select="$currFld"/> and att is: <xsl:value-of select="$currentAtt/attName"/>
    </xsl:comment>-->
          <!-- this is what gets written on cell -->
          <xsl:variable name="currFldMaybeTransl"><xsl:value-of select="$currFld"/><xsl:if test="string-length($currentAtt/attFKTranslationSQL)&gt;0">_transl</xsl:if><xsl:if test="$currentAtt/attType='Date'">_datetrunc</xsl:if></xsl:variable>    
             <xsl:element name="{$container}"><xsl:if test="$altField!='true'"><xsl:attribute name="class"><xsl:value-of select="translate(concat($currEnt,'_',$currFld),$alphahigh,$alphalow)" /></xsl:attribute></xsl:if>
<logic:notEmpty name="onerowof{$currEnt}" property="{$currFldMaybeTransl}"><xsl:element name="span"><xsl:choose>
            <xsl:when test="string-length(@useClass)=0">
              <!-- no specific class for this data -->
              <!-- fill in different class for long text fields: -->
              <xsl:if test="$currentAtt/attType='text' or (contains($currentAtt/attType,'varchar (') and string-length($currentAtt/attType)&gt;12)">
                <xsl:attribute name="class">largefield</xsl:attribute>
              </xsl:if>
              <xsl:if test="$currentAtt/attType='Date'"><!-- write exact date in a title att -->
                <xsl:attribute name="title"><xsl:text disable-output-escaping="yes">&lt;</xsl:text>bean:write name='onerowof<xsl:value-of select="$currEnt"/>' property='<xsl:value-of select="$currFld"/>' /<xsl:text disable-output-escaping="yes">&gt;</xsl:text></xsl:attribute>
              </xsl:if>
            </xsl:when>
            <xsl:otherwise>
               <xsl:attribute name="class"><xsl:value-of select="@useClass"/></xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
        <!-- parse date: -->
        <xsl:if test="$currentAtt/attType='Date'">
          @subst_lt@dt:format pattern="dd-MMM-yyyy"@subst_gt@
          @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
        </xsl:if>
                  <!-- extra stuff before something : -->
        <xsl:if test="string-length($currentAtt/attFormsHTMLpre)&gt;0">
          <!-- insert extra stuff -->
          <!--logic:notEqual parameter="textoutput" value="true"-->
            <xsl:value-of select="$currentAtt/attFormsHTMLpre"/>
          <!--/logic:notEqual-->
        </xsl:if>

          <xsl:choose>
            <xsl:when test="string-length($currentAtt/attFKTranslationSQL)&gt;0 and $currentAtt/attKey='FK' and ($currentAtt/attFKTranslationSQL/@addLink='true' or string-length($currentAtt/attFKTranslationSQL/@addLink)=0)">
              <!-- only do this if translation criteria AND FK! -->
              <xsl:element name="a"><xsl:attribute name="href">@get_link@std/<xsl:value-of select="translate(substring-before($currentAtt/attReferences,'.'),$alphahigh,$alphalow)"/>/<xsl:text disable-output-escaping="yes">&lt;</xsl:text>bean:write name='onerowof<xsl:value-of select="$currEnt"/>' property='<xsl:value-of select="$currFld"/>' /<xsl:text disable-output-escaping="yes">&gt;</xsl:text></xsl:attribute><!-- no space between elements here! --><xsl:call-template name="writeOneFieldValue">
                  <xsl:with-param name="beanPropName" select="$currFldMaybeTransl"/>
                  <xsl:with-param name="currEnt" select="$currEnt"/>
                </xsl:call-template>
              </xsl:element>
              <!-- </a> -->
            </xsl:when>
            <!-- 2nd repeat of this  test -->
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$linkToPK='true'">
                  <!-- add link to PK -->
                  <xsl:element name="a">
                    <xsl:attribute name="href">@get_link@detail/<xsl:value-of select="$currEnt" />/@subst_lt@bean:write name='onerowof<xsl:value-of select="$currEnt"/>' property='<xsl:value-of select="translate($currentAtt/../attribute[attKey='PK']/attName,$alphahigh,$alphalow)" />' /@subst_gt@</xsl:attribute>
                       <xsl:call-template name="writeOneFieldValue">
                         <xsl:with-param name="beanPropName" select="$currFldMaybeTransl"/>
                         <xsl:with-param name="currEnt" select="$currEnt"/>
                     </xsl:call-template>
                  </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                                 <!-- just write data -->
                     <xsl:call-template name="writeOneFieldValue">
                         <xsl:with-param name="beanPropName" select="$currFldMaybeTransl"/>
                         <xsl:with-param name="currEnt" select="$currEnt"/>
                     </xsl:call-template>
                </xsl:otherwise>
              </xsl:choose>

            </xsl:otherwise>
          </xsl:choose>

          <!-- units -->
        <xsl:if test="string-length($currentAtt/attUnits)&gt;0"><xsl:if test="contains($view,'csv')=false"><!-- do not write units to csv data views -->
          <xsl:comment>only write units if there is something here:</xsl:comment>
          <logic:notEqual parameter="textoutput" value="true">
                    <span class="units"><xsl:value-of select="$currentAtt/attUnits"/></span></logic:notEqual>
        </xsl:if></xsl:if>
        <!-- extra -->

         <!-- date extra stuff -->
         <xsl:if test="$currentAtt/attType='Date'">
           @subst_lt@/dt:parse@subst_gt@
           @subst_lt@/dt:format@subst_gt@
         </xsl:if>      
        <xsl:if test="string-length($currentAtt/attFormsHTMLpost)&gt;0">
          <!-- insert extra stuff -->
          <!--logic:notEqual parameter="textoutput" value="true"-->          
             <xsl:value-of select="$currentAtt/attFormsHTMLpost"/>
          <!--/logic:notEqual-->
        </xsl:if>
        
        </xsl:element>
        <!--</span>-->
      
      </logic:notEmpty>
        <!-- add something to view if alternate field is specified and field is null -->
        <!-- alternate field if null? -->
         <xsl:if test="string-length($currentAtt/attAltIfNull)&gt;0 and $altField!='true'"><!-- second condition prevents infinite loops -->
           <!-- yes there is an alt field if null, display it -->
           <logic:empty	 name="onerowof{$currEnt}" property="{$currFldMaybeTransl}">
             <xsl:comment>The field is empty, but there is an alternate, display the alternate field:</xsl:comment>
               <xsl:call-template name="writeField">
                  <xsl:with-param name="currEnt" select="$currEnt"/>
                  <xsl:with-param name="currFld" select="translate($currentAtt/attAltIfNull,$alphahigh,$alphalow)"/>
                  <xsl:with-param name="currentAtt" select="$currentAtt"/><!-- not ideal to pass this att again, but doing it anyway -->
                  <xsl:with-param name="container">span</xsl:with-param><!-- p or td -->
                  <xsl:with-param name="view" select="$view" /><!-- name of this view -->
                  <xsl:with-param name="linkToPK" select="$linkToPK" /><!-- if true, then encapsulate in <a></a> -->
                  <xsl:with-param name="writeSuffix" select="$currentAtt/attAltIfNull/@addSuffixTxt" /><!-- write something after the field value -->              
                  <xsl:with-param name="altField">true</xsl:with-param><!-- true if writing an alternate field -->
               </xsl:call-template>
           </logic:empty>
         </xsl:if>  
      
        <xsl:if test="$container!='span'">
      <logic:empty name="onerowof{$currEnt}" property="{$currFld}">
        <!-- only extra bit if empty and container is not p-->
        <xsl:if test="contains($view,'csv')=false"><logic:notEqual parameter="textoutput" value="true">
<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></logic:notEqual></xsl:if>
      </logic:empty>
        </xsl:if>
        <xsl:value-of select="$writeSuffix" /><!-- write this.  if null or empty, won't affect view -->
    </xsl:element><!-- td or p -->
  </xsl:template>
  <xsl:template name="writeOneFieldValue">
    <xsl:param name="currEnt"/>
    <xsl:param name="beanPropName"/>
    <xsl:param name="ignore" /><xsl:element name="bean:write">
      <xsl:attribute name="name">onerowof<xsl:value-of select="translate($currEnt,$alphahigh,$alphalow)" /></xsl:attribute>
      <xsl:attribute name="property"><xsl:value-of select="$beanPropName" /></xsl:attribute>
      <xsl:if test="$ignore='true'"><xsl:attribute name="ignore">true</xsl:attribute></xsl:if>
    </xsl:element>

  </xsl:template>
  <xsl:template name="labelField">
    <xsl:param name="container" />
    <xsl:param name="containerClass" />
    <xsl:param name="suffix" /><!-- something to add after the label -->
    <xsl:param name="currEnt"/>
    <xsl:param name="currFld"/>
    <xsl:param name="currLbl"/>
    <xsl:param name="currDefn" />
    <xsl:param name="noDDLink" />
    <xsl:element name="{$container}">
        <xsl:attribute name="class"><xsl:value-of select="translate(concat($currEnt,'_',$currFld),$alphahigh,$alphalow)" /><xsl:value-of select="concat(' ',$containerClass)" /></xsl:attribute>
        <xsl:attribute name="title"><xsl:value-of select="translate($currDefn,$quot,' ')" /></xsl:attribute>
    <xsl:choose>
      <xsl:when test="string-length($currLbl)&gt;0">
        <xsl:value-of select="$currLbl"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:comment>WARNING:no label in XML!</xsl:comment>
        <xsl:value-of select="$currFld"/>
      </xsl:otherwise>
    </xsl:choose>
     
     <xsl:text> </xsl:text><xsl:choose>
      <xsl:when test="$noDDLink='true'"><!-- no data dictionary link, as per db_model specs --></xsl:when>
      <xsl:otherwise>
    <a title="Click here for definition of this field." target="_blank" class="image" href="/dd/{$currEnt}/{$currFld}" onclick="popupDD('/dd/{$currEnt}/{$currFld}'); return false;"><span class="ddlink">
      <img src="@images_link@question.gif" alt="?" border="0"/>
    </span></a></xsl:otherwise></xsl:choose>
    
    <xsl:value-of select="$suffix" />
    </xsl:element>

  </xsl:template>
</xsl:stylesheet>
