<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:param name="htmlPrefix">datadictionary</xsl:param>
  <xsl:output method="xml" omit-xml-declaration="no" encoding="UTF-8"/>
  <xsl:template match="/dataModel">
@webpage_top_html@

  @webpage_head_html@
  <title>VegBank Data Dictionary Index</title>
  @webpage_masthead_html@
    <h2>VegBank Data Dictionary Index</h2>
    <p>See the <a href="{$htmlPrefix}-guide.html">data dictionary guide</a> for more information on how to interpret the information in the data dictionary.</p>
    <table class="thinlines">
    <tr>
            <th>Select table</th><th>or a field</th>
    </tr>
<!--             <select>
              <option>-select table-</option>
              <xsl:for-each select="entity">
                <option value="{$htmlPrefix}~table~{translate(entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                  <xsl:value-of select="entityName"/>
                </option>
              </xsl:for-each>
            </select> -->

            <xsl:for-each select="entity[count(attribute)&gt;0]">
            <xsl:sort select="entityName" />
            <tr>
<td>   <a href="{$htmlPrefix}~table~{translate(entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                  <xsl:value-of select="entityName"/> </a> </td>
      <td>
        <select onChange="MM_jumpMenu('parent',this,0)">
                <option>---------------------select field---------------------</option> <!-- wider select field so that combo boxes are same width -->
                <xsl:for-each select="attribute">
                  <xsl:if test="attModel='logical'">
                    <option value="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                      <xsl:value-of select="attName"/>
                    </option>
                  </xsl:if>
                </xsl:for-each>
              </select>
             </td>
             </tr>
            </xsl:for-each>
</table>

@webpage_footer_html@
  </xsl:template>
</xsl:stylesheet>
