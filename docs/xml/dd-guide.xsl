<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:param name="htmlPrefix">datadictionary</xsl:param>
  <xsl:output method="html"/>
  <xsl:template match="/dataDictionaryKey">
    <head>@defaultHeadToken@
      <style type="text/css">
  td , th , p
  {
  font-family: verdana, arial, sans-serif  ;
  font-size:smaller
  }
  h2
  {
  font-family:georgia, Times New Roman,  times, serif 
  }
</style>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
    </head>
    <body>
    @vegbank_header_html_normal@

      <h2>
     <a name="top" /> VegBank data dictionary - guide
      </h2>
      <p>This page briefly describes the different pieces of information found in the <a href="{$htmlPrefix}-index.html">data dictionary</a>.<br />
Jump to : 
      <xsl:for-each select="att">
             <a href="#{translate(name,' ABCDEFGHIJKLMNOPQRSTUVWXYZ','-abcdefghijklmnopqrstuvwxyz')}"><xsl:value-of select="name" /></a>
               <xsl:if test="last()!=position()"><xsl:text> | </xsl:text></xsl:if>
      </xsl:for-each>
      </p>
      <xsl:for-each select="att">
      
        <!-- bookmark for this att name -->
        <a name="{translate(name,' ABCDEFGHIJKLMNOPQRSTUVWXYZ','-abcdefghijklmnopqrstuvwxyz')}" />
            <table border="1" cellpadding="0" cellspacing="0"> 
                        <tr bgcolor="#FFFFCC">
            <th>Describing what:</th><td><font size="+1"><b><xsl:value-of select="name"/></b></font></td>
            </tr>
            <tr>
            <th>Description:</th>
            <td>       <xsl:value-of select="defn"/></td>
            </tr>
            <xsl:if test="string-length(possValue/value)&gt;0">
<tr>
              <th>possible values:</th>
              <td>
              <table border="1" cellpadding="0" cellspacing="0">  
              <tr>
                <th>value</th>
                <th>value meaning</th>
              </tr>
            <xsl:for-each select="possValue">
               <tr>
                 <td><xsl:value-of select="value" /></td>
                 <td><xsl:value-of select="valueMeaning" /></td>
               </tr>
            </xsl:for-each>
            </table>
            </td>
          </tr>
            </xsl:if>
            </table>
            <p><a href="#top">top...</a></p>
        <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>        <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>              
 <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>  <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p> </xsl:for-each>
      <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
@vegbank_footer_html_onerow@

</body>
</xsl:template>
</xsl:stylesheet>
