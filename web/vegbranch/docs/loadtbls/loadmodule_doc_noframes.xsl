<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:param name="currtbl">Z_USER_B_specList</xsl:param>
<xsl:param name="showFlds">3</xsl:param>

  <xsl:output method="html"/>
  <xsl:template match="/LoadModDoc">
    <html>
      <head>
        <link rel="stylesheet" href="@stylesheet@" type="text/css"/> 
        <style type="text/css">
          
          td.color9 {background-color: FFFFFF}
          td.color1 {background-color: FF6666}
          td.color2 {background-color: FFCC99}
          td.color3 {background-color: FFFF99}
          td.color4 {background-color: 99FFCC}
        </style>
<title>VegBranch Loading Module Documentation (no frames)</title>
      </head>
      <body>
    @vegbank_header_html_normal@

      <h2 class="VegBank">
          <a name="top"/> VegBranch Loading Module - tables (no frames version)
      </h2>
      <xsl:if test="$currtbl='index'">
       <h4>index</h4>
      <p class="sansserif">
Select a table to find out more about it below.  Once you have opened one of these links,
a table will show you the different fields for that table.  Several examples are given,
some comments on the examples are provided, and the database structure of each field is given,
including a definition.</p>
      </xsl:if>
      
          <p class="sansserif">
        <xsl:for-each select="table">
           <a href="{TableName}~{$showFlds}_noframes.html"><xsl:value-of select="tableNickName" /></a><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text>
           <xsl:if test="position() mod 6=0"><br /></xsl:if>
        </xsl:for-each>
        </p>
<xsl:if test="$currtbl!='index'">
        <form name="FieldsToShow">
        <p>Show Fields: <br />
<xsl:value-of select="translate(translate(number($showFlds=1),'1','*'),'0',' ')" /><a href="{$currtbl}~1_noframes.html" >Required Fields Only</a><br />
<xsl:value-of select="translate(translate(number($showFlds=2),'1','*'),'0',' ')" /><a href="{$currtbl}~2_noframes.html" >add commonly used fields</a><br />
<xsl:value-of select="translate(translate(number($showFlds=3),'1','*'),'0',' ')" /><a href="{$currtbl}~3_noframes.html" >add sometimes used fields</a><br />
<xsl:value-of select="translate(translate(number($showFlds=4),'1','*'),'0',' ')" /><a href="{$currtbl}~4_noframes.html" >add advanced fields</a><br />
<xsl:value-of select="translate(translate(number($showFlds=9),'1','*'),'0',' ')" /><a href="{$currtbl}~9_noframes.html" >all fields</a><br />
<br />
  A* appears before the current setting of how many fields are shown.
  </p>
</form>
        
        <xsl:for-each select="table[TableName=$currtbl]">
<p><b><font size="+1"><xsl:value-of select="tableNickName" /></font></b> <br />
<b>Description:</b><xsl:value-of select="TableDescription" /> <br />
<b>Am I required to use this table?</b><xsl:value-of select="TableReqd" /><br />
<b>Database table name:</b><xsl:value-of select="TableName" /></p>
          <table border="1" cellspacing="0" class="sansserif">
<xsl:variable name="strLen">25</xsl:variable>
           <xsl:variable name="strApos">'</xsl:variable>
           <xsl:variable name="strOthr">`</xsl:variable>
          <tr><th>FldNm:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><b><xsl:value-of select="FieldName" /></b><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
          <tr><th>exmpl1:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example1" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
          <tr><th>exmpl2:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example2" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
          <tr><th>exmpl3:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example3" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
        <tr><th>exmpl4:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example4" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
         <tr><th>comment:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}">
         <font size="-1">
      <i><xsl:value-of select="exampleComments" /></i><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></font></td></xsl:for-each></tr>                    
          <tr><th><!--<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>--></th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td><!-- <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>--></td></xsl:for-each></tr>         
           <tr><th>defn:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}">
           <font size="-1"><xsl:value-of select="FieldDefinition" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></font></td></xsl:for-each></tr>
           <tr><th>type:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="dataType" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
        <tr><th>size:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="FieldSize" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
       <tr><th>nulls:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="Nulls" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>                    
          <tr><th>key:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="key" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
          <tr><th>dest. field:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}">
            <xsl:choose>
              <xsl:when test="contains(DestinationField,'.')">  <xsl:value-of select="substring-before(DestinationField,'.')"/>. <xsl:value-of select="substring-after(DestinationField,'.')"/>                </xsl:when>
                    <xsl:otherwise><xsl:value-of select="DestinationField"/></xsl:otherwise>
             </xsl:choose>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
        
          </table>
        </xsl:for-each>
       <p>
       Color Key:
       <br/>
       <table  border="1" cellspacing="0">
<tr><td class="color1">req'd fld</td>
<td class="color2">often used</td>
<td class="color3">some times</td>
<td class="color4">expert user</td>
<td colspan="2" class="color9">internal field</td></tr>
</table>
       </p>
</xsl:if>
    @vegbank_footer_html_onerow@
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
