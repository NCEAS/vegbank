<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:param name="htmlPrefix">datadictionary</xsl:param>
  <xsl:output method="html"/>
  <xsl:template match="/dataDictionaryKey">
@webpage_top_html@
  @webpage_head_html@
  <title>VegBank Data Dictionary Key</title>
  @webpage_masthead_html@ 


      <h2>
      <a name="top"/> VegBank data dictionary - guide
      </h2>
    <p>This page briefly describes the different pieces of information found in the <a href="{$htmlPrefix}-index.html">data dictionary</a>.<br/>
Jump to : 
      <xsl:for-each select="att">
        <a href="#{translate(name,' ABCDEFGHIJKLMNOPQRSTUVWXYZ','-abcdefghijklmnopqrstuvwxyz')}">
          <xsl:value-of select="name"/>
        </a>
        <xsl:if test="last()!=position()">
          <xsl:text> | </xsl:text>
        </xsl:if>
      </xsl:for-each>
    </p>
    <xsl:for-each select="att">
      <!-- bookmark for this att name -->
      <a name="{translate(name,' ABCDEFGHIJKLMNOPQRSTUVWXYZ','-abcdefghijklmnopqrstuvwxyz')}"/>
      <table class="thinlines">
        <tr>
          <th>Describing what:</th>
          <td>
            
                <xsl:value-of select="name"/>
              
          </td>
        </tr>
        <tr>
          <th>Description:</th>
          <td>
            <xsl:value-of select="defn"/>
          </td>
        </tr>
        <xsl:if test="string-length(possValue/value)&gt;0">
          <tr>
            <th>possible values:</th>
            <td>
              <table class="thinlines">
                <tr>
                  <th>value</th>
                  <th>value meaning</th>
                </tr>
                <xsl:for-each select="possValue">
                  <tr>
                    <td>
                      <xsl:value-of select="value"/>
                    </td>
                    <td>
                      <xsl:value-of select="valueMeaning"/>
                    </td>
                  </tr>
                </xsl:for-each>
              </table>
            </td>
          </tr>
        </xsl:if>
      </table>
      <p>
        <a href="#top">top...</a>
      </p>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p>
      <p>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
      </p>

    </xsl:for-each>
    <p>
      <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    </p>
@webpage_footer_html@


</xsl:template>
</xsl:stylesheet>
