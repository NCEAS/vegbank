<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:vegbank="http://vegbank.org" xmlns:logic="http://vegbank.org" xmlns:bean="http://vegbank.org" >
  <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes" />

  <xsl:param name="view">Detail</xsl:param>
  <xsl:param name="oneTbl">reference</xsl:param>
  <xsl:param name="detailAdd"></xsl:param>
  <xsl:param name="more">yes</xsl:param><!-- yes if you want a link to details for each summary row -->
  <xsl:param name="alphalow">abcdefghijklmnopqrstuvwxyz</xsl:param>
  <xsl:param name="alphahigh">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:param>
  <xsl:template match="entity">
    <xsl:if test="string-length($oneTbl)&lt;1 or entityName=$oneTbl">
<xsl:comment>
This file is a template that should be edited to create jsp files that will be accessed in /get/ views of vegbank data.  The SQL is generated with this file, and should be edited and added to SQLStore as needed.

Copy from after the START: comment to the END: comment for contents of the file!
</xsl:comment>
      
      <xsl:comment> ____________________________START  SQL for:  <xsl:value-of select="entityName"/> _______________________________________ 
<xsl:value-of select="translate(entityName,$alphahigh,$alphalow)"/>=SELECT <xsl:for-each select="attribute">
          <xsl:value-of select="translate(attName,$alphahigh,$alphalow)"/>
          <xsl:if test="string-length(attFKTranslationSQL)&gt;0">, <xsl:value-of select="attFKTranslationSQL"/> AS <xsl:value-of select="translate(attName,$alphahigh,$alphalow)"/>_transl </xsl:if>
          <xsl:if test="position()!=last()">,</xsl:if> \
      </xsl:for-each>           FROM <xsl:value-of select="translate(entityName,$alphahigh,$alphalow)"/>
      </xsl:comment>
      <xsl:comment> ____________________________START:  <xsl:value-of select="entityName"/> _______________________________________ 
</xsl:comment> 

@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: <xsl:value-of select="entityName"/>s - <xsl:value-of select="$view"/>
        </TITLE>
        <link rel="stylesheet" href="@stylesheet@" type="text/css"/>

      </HEAD>
      <body>@vegbank_header_html_normal@  <br/>
        <h2>View VegBank <xsl:value-of select="entityLabel"/>s</h2>
        <xsl:comment>Get standard declaration of rowClass as string: </xsl:comment>
        
        <xsl:text disable-output-escaping="yes">
        &lt;% String rowClass = "evenrow"; %&gt;
        </xsl:text>
        <xsl:variable name="currEnt" select="translate(entityName,$alphahigh,$alphalow)"/>
        <xsl:variable name="currPK" select="translate(attribute[attKey='PK']/attName,$alphahigh,$alphalow)"/>
       
       
        <xsl:element name="vegbank:get">
          <xsl:attribute name="id"><xsl:value-of select="$currEnt"/></xsl:attribute>
          <xsl:attribute name="select"><xsl:value-of select="$currEnt"/></xsl:attribute>
          <xsl:attribute name="beanName">map</xsl:attribute>
          <xsl:attribute name="pager">true</xsl:attribute>
        </xsl:element> <xsl:comment>Where statement removed from preceding: </xsl:comment>
        


<vegbank:pager /><!-- top copy of pager -->
        <logic:empty name="{$currEnt}-BEANLIST">
          <p>  Sorry, no <xsl:value-of select="entityName"/>s found.</p>
        </logic:empty>
        <logic:notEmpty name="{$currEnt}-BEANLIST">
          <!-- begin writing data here -->
          <xsl:choose>
            <xsl:when test="contains(translate($view,$alphahigh,$alphalow),'summary')">
              <!-- summary view: table with headers -->
              <table class="leftrightborders" cellpadding="2">
                <tr>
                  <xsl:if test="$more='yes'">
                  <th>More</th>
                  </xsl:if>
                  <xsl:text disable-output-escaping="yes">
                  &lt;</xsl:text>%@ include file="autogen/<xsl:value-of select="$currEnt" />_<xsl:value-of select="translate($view,$alphahigh,$alphalow)" />_head.jsp" %<xsl:text disable-output-escaping="yes">&gt;</xsl:text>                </tr>
                <logic:iterate id="onerowof{$currEnt}" name="{$currEnt}-BEANLIST">
                  <tr class="@nextcolorclass@">
                    <xsl:if test="$more='yes'">
                    <td class="sizetiny">
                      <xsl:element name="a">
                        <xsl:attribute name="href">@get_link@detail/<xsl:value-of select="$currEnt"/>/@subst_lt@bean:write name='onerowof<xsl:value-of select="$currEnt"/>' property='<xsl:value-of select="translate($currPK,$alphahigh,$alphalow)"/>' /@subst_gt@</xsl:attribute>
                            Details
                            </xsl:element>
                      <!-- a -->
                    </td>
                    </xsl:if>
                       <xsl:text disable-output-escaping="yes">
                       &lt;</xsl:text>%@ include file="autogen/<xsl:value-of select="$currEnt" />_<xsl:value-of select="translate($view,$alphahigh,$alphalow)" />_data.jsp" %<xsl:text disable-output-escaping="yes">&gt;
                       </xsl:text>

                  </tr>
                   <xsl:call-template name="storePK"><xsl:with-param name="currEnt" select="$currEnt" /><xsl:with-param name="currPK" select="$currPK" /></xsl:call-template>
                </logic:iterate>
              </table>
            </xsl:when>
            <xsl:otherwise>
              <!-- default view -->
              <logic:iterate id="onerowof{$currEnt}" name="{$currEnt}-BEANLIST">
                <xsl:comment> iterate over all records in set : new table for each </xsl:comment>
                <table class="leftrightborders" cellpadding="2">
        <xsl:text disable-output-escaping="yes">
        &lt;</xsl:text>%@ include file="autogen/<xsl:value-of select="$currEnt" />_<xsl:value-of select="translate($view,$alphahigh,$alphalow)" />_data.jsp" %<xsl:text disable-output-escaping="yes">&gt;
        </xsl:text>
                   <xsl:call-template name="storePK"><xsl:with-param name="currEnt" select="$currEnt" /><xsl:with-param name="currPK" select="$currPK" /></xsl:call-template>
                   
                   <xsl:if test="string-length($detailAdd)&gt;0">
                   <xsl:variable name="subtbl" select="translate($detailAdd,$alphahigh,$alphalow)" />
                     <!-- add a inside table -->

                      <TR><TD COLSPAN="2">
  
  <vegbank:get id="{$subtbl}" select="{$subtbl}" beanName="map"  pager="false" where="where_{$currEnt}_pk" wparam="{$currEnt}_pk" perPage="-1"/>
  <table class="leftrightborders" cellpadding="2" >
<tr><th colspan="9"><xsl:value-of select="$detailAdd"/>s:</th></tr>  
<logic:empty name="{$subtbl}-BEANLIST">
<tr><td  class="@nextcolorclass@">  Sorry, no <xsl:value-of select="$subtbl"/>s found.</td></tr>
</logic:empty>

<logic:notEmpty name="{$subtbl}-BEANLIST">

<tr>    <xsl:text disable-output-escaping="yes">
&lt;</xsl:text>%@ include file="autogen/<xsl:value-of select="$subtbl"/>_summary_head.jsp" %<xsl:text disable-output-escaping="yes">&gt;
</xsl:text> </tr>
    <logic:iterate id="onerowof{$subtbl}" name="{$subtbl}-BEANLIST">
 <tr class="@nextcolorclass@">
     <xsl:text disable-output-escaping="yes">
&lt;</xsl:text>%@ include file="autogen/<xsl:value-of select="$subtbl"/>_summary_data.jsp" %<xsl:text disable-output-escaping="yes">&gt;
</xsl:text> </tr>        
    </logic:iterate>

</logic:notEmpty>
  </table>
  </TD></TR>
  
                   </xsl:if>
                   
                </table>
                <p>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </p>
              </logic:iterate>
            </xsl:otherwise>
          </xsl:choose>
        </logic:notEmpty>
        <br/>
        <vegbank:pager />
      </body></html>
          @vegbank_footer_html_tworow@
<xsl:comment> ____________________________@END:  <xsl:value-of select="entityName"/> _______________________________________ 


</xsl:comment>
      

    </xsl:if>
  </xsl:template>
  <xsl:template match="dataModel">
    <rootThatGetsXMLNSBits>
    <xsl:apply-templates/>
    </rootThatGetsXMLNSBits>
  </xsl:template>
  <xsl:template match="*"/>
 <xsl:template name="storePK">
   <xsl:param name="currEnt"/>
   <xsl:param name="currPK" />
   
   <bean:define id="{$currEnt}_pk" name="onerowof{$currEnt}" property="{$currPK}"/>  
   <xsl:comment>Insert a nested get statement here:
   example:   
<xsl:text disable-output-escaping="yes">
&lt;</xsl:text>vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_<xsl:value-of select="$currEnt"/>_pk" wparam="<xsl:value-of select="$currEnt"/>_pk" /<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
   
   </xsl:comment>
 </xsl:template>
 <xsl:template name="htmlbreak" ><!-- not needed anymore --></xsl:template>
</xsl:stylesheet>
