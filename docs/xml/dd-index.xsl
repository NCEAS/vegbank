<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:param name="htmlPrefix">datadictionary</xsl:param>
  <xsl:output method="xml" omit-xml-declaration="no" encoding="UTF-8"/>
  <xsl:template match="/dataModel">
    <html>
    <head>
<title>VegBank Data Dictionary</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script language="JavaScript">

function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}

</script>
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
@vegbank_header_html_normal@
    <h2>VegBank Data Dictionary Index</h2>
    <p>See the <a href="{$htmlPrefix}-guide.html">data dictionary guide</a> for more information on how to interpret the information in the data dictionary.</p>
    <table border="1">
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

            <xsl:for-each select="entity">
            <tr>
<td>   <a href="{$htmlPrefix}~table~{translate(entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                  <xsl:value-of select="entityName"/> </a> </td>
      <td>
        <select onChange="MM_jumpMenu('parent',this,0)">
                <option>--select field--</option>
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
