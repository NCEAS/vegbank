<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:vegbank="http://vegbank.org" xmlns:logic="http://vegbank.org" xmlns:bean="http://vegbank.org">
<xsl:output method="html" />
  <xsl:param name="view">Summary</xsl:param>
  <xsl:param name="oneTbl">userDatasetItem</xsl:param>
 <xsl:param name="alphalow">abcdefghijklmnopqrstuvwxyz</xsl:param>
  <xsl:param name="alphahigh">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:param>
<xsl:template match="entity">
<xsl:if test="string-length($oneTbl)&lt;1 or entityName=$oneTbl" >
<br/><br/><br/><br/>



<xsl:comment> ____________________________START  SQL for:  <xsl:value-of select="entityName" /> _______________________________________ 
SELECT <xsl:for-each select="attribute"><xsl:value-of select="translate(attName,$alphahigh,$alphalow)" /><xsl:if test="position()!=last()">,</xsl:if> \
</xsl:for-each>FROM <xsl:value-of select="translate(entityName,$alphahigh,$alphalow)" />
</xsl:comment> 


<xsl:comment> ____________________________START:  <xsl:value-of select="entityName" /> _______________________________________ 
</xsl:comment> 


@stdvegbankget_jspdeclarations@


<HEAD>@defaultHeadToken@
 
<TITLE>View Data in VegBank : <xsl:value-of select="entityName" />(s) : <xsl:value-of select="$view" /></TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />


<meta http-equiv="Content-Type" content="text/html; charset=" />
</HEAD>
<body>@vegbank_header_html_normal@  <br/>
<h2>View VegBank <xsl:value-of select="entityLabel" />s</h2>
  <xsl:element name="vegbank:get">
    <xsl:attribute name="select"><xsl:value-of select="translate(entityName,$alphahigh,$alphalow)" /></xsl:attribute>
    <xsl:attribute name="beanName">map</xsl:attribute>
  </xsl:element>
          <logic:empty name="BEANLIST">
                Sorry, no <xsl:value-of select="entityName" />(s) are available in the database!
          </logic:empty>
          <logic:notEmpty name="BEANLIST">
       <!-- begin writing data here -->
       <xsl:choose>
        <xsl:when test="$view='Summary'">
           <!-- summary view: table with headers -->
           <table border="1" cellpadding="0" cellspacing="0">
             <tr>
               <xsl:for-each select="attribute[attForms/formShow/@name=$view  and attForms/formShow&gt;0]">
             <xsl:sort select="attForms/formShow" data-type="number"/>
           <th><xsl:choose>
                    <xsl:when test="string-length(attLabel)&gt;0"><xsl:value-of select="attLabel" /></xsl:when>
                    <xsl:otherwise><xsl:comment>WARNING:no label in XML!</xsl:comment><xsl:value-of select="attName" /></xsl:otherwise>
                  </xsl:choose></th>
                  </xsl:for-each>
                        </tr>

                       <logic:iterate id="onerow" name="BEANLIST">
                            <tr>
               <xsl:for-each select="attribute[attForms/formShow/@name=$view  and attForms/formShow&gt;0]">
             <xsl:sort select="attForms/formShow" data-type="number"/>
           <td><p><span class="item"><bean:write name="onerow" property="{translate(attName,$alphahigh,$alphalow)}" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></span></p></td>

</xsl:for-each>                            
                            </tr>
                       </logic:iterate>   
           </table>
        </xsl:when>
        <xsl:otherwise><!-- default view -->
            <logic:iterate id="onerow" name="BEANLIST"> <xsl:comment> iterate over all records in set : new table for each </xsl:comment>
             <table border="1" cellpadding="0" cellspacing="0" class="item">
              <xsl:comment>each field, only write when HAS contents</xsl:comment>
             <xsl:for-each select="attribute[attForms/formShow/@name=$view  and attForms/formShow&gt;0]">
             <xsl:sort select="attForms/formShow" data-type="number"/>
                 <logic:notEmpty name="onerow" property="{translate(attName,$alphahigh,$alphalow)}">
                   <tr><td><xsl:comment>label:</xsl:comment><p><span class="category"><xsl:choose>
                    <xsl:when test="string-length(attLabel)&gt;0"><xsl:value-of select="attLabel" /></xsl:when>
                    <xsl:otherwise><xsl:comment>WARNING:no label in XML!</xsl:comment><xsl:value-of select="attName" /></xsl:otherwise>
                  </xsl:choose></span></p></td>
                  <td><p><span class="item"><bean:write name="onerow" property="{translate(attName,$alphahigh,$alphalow)}" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></span></p></td>
                  </tr>
                 </logic:notEmpty>
             </xsl:for-each>
              </table>
            </logic:iterate>
        </xsl:otherwise>
      </xsl:choose>    
            </logic:notEmpty>
          <br/>
          @vegbank_footer_html_tworow@
<xsl:comment> ____________________________@END:  <xsl:value-of select="entityName" /> _______________________________________ 


</xsl:comment>  
<br/><br/><br/><br/><br/><br/>  

</body>
</xsl:if>
</xsl:template>
</xsl:stylesheet>
