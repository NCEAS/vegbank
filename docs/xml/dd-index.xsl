<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:param name="htmlPrefix">datadictionary</xsl:param>
  <xsl:output method="xml" omit-xml-declaration="no" encoding="UTF-8"/>
  <xsl:template match="/dataModel">
    <html>
    <head>@defaultHeadToken@
<title>VegBank Data Dictionary -- Index</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script language="JavaScript">

function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}

</script>
 
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</head>
@vegbank_header_html_normal@
    <h2>VegBank Data Dictionary Index</h2>
    <p>See the <a href="{$htmlPrefix}-guide.html">data dictionary guide</a> for more information on how to interpret the information in the data dictionary.</p>
    <table border="1" cellpadding="0" cellspacing="0">
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
<td class="normal">   <a href="{$htmlPrefix}~table~{translate(entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                  <xsl:value-of select="entityName"/> </a> </td>
      <td class="normal">
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
 <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p> 
@vegbank_footer_html_onerow@
    </html>
  </xsl:template>
</xsl:stylesheet>
