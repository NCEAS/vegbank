<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:param name="currtbl">Z_USER_B_specList</xsl:param>
<xsl:param name="showFlds">2</xsl:param>
<xsl:param name="jumpMenu">no</xsl:param>

  <xsl:output method="html"/>
  <xsl:template match="/LoadModDoc">
  <xsl:variable name="suffix"><xsl:if test="$jumpMenu!='yes'">_simple</xsl:if></xsl:variable>
    <html>
      <head>@defaultHeadToken@
        <link rel="stylesheet" href="@stylesheet@" type="text/css"/> 
<style type="text/css">
          
          td.color9 {background-color: FFFFFF}
          td.color1 {background-color: FF6666}
          td.color2 {background-color: FFCC99}
          td.color3 {background-color: FFFF99}
          td.color4 {background-color: 99FFCC}
        </style>
<xsl:if test="$jumpMenu='yes'">
        <script type="text/javascript">
<xsl:comment>
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}
//</xsl:comment>

</script>
</xsl:if>
      </head>
      <body>
    @vegbank_header_html_normal@

      <h2 class="VegBank">
          <a name="top"/> VegBranch Loading Module - tables
      </h2>
            <xsl:if test="$currtbl='index'">
      <h4>index</h4>
            <p class="sansserif">
Select a table to find out more about it below.  Once you have opened one of these links,
a table will show you the different fields for that table.  Several examples are given,
some comments on the examples are provided, and the database structure of each field is given,
including a definition.<br/>
<xsl:choose>
  <xsl:when test="$jumpMenu='yes'">This page requires a javascript enabled browser.  If this is not possible for your browser, use the version <a  href="loadtbl_menu_simple.html">here</a>. </xsl:when>
  <xsl:otherwise>A version of this page is available <a  href="loadtbl_menu.html">here</a> that uses JavaScript, if your browser supports it. </xsl:otherwise>
</xsl:choose>


</p>
            </xsl:if>
          <p class="sansserif">
        <xsl:for-each select="table">
           <a href="{TableName}~{$showFlds}{$suffix}.html"><xsl:value-of select="tableNickName" /></a><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text>
           <xsl:if test="position() mod 6=0"><br /></xsl:if>
        </xsl:for-each>
        </p>
          <xsl:if test="$currtbl!='index'">

        <form name="FieldsToShow">
        <p>Show Fields: 
<xsl:choose>
  <xsl:when test="$jumpMenu='yes'">
  <select name="pickFields" onChange="MM_jumpMenu('document',this,0)">
    <option value="" selected="selected">--Select which fields to show--</option>
    <option value="{$currtbl}~1.html" ><xsl:value-of select="translate(translate(number($showFlds=1),'1','*'),'0',' ')" />Required Fields Only</option>
    <option value="{$currtbl}~2.html" ><xsl:value-of select="translate(translate(number($showFlds=2),'1','*'),'0',' ')" />add commonly used fields</option>
    <option value="{$currtbl}~3.html" ><xsl:value-of select="translate(translate(number($showFlds=3),'1','*'),'0',' ')" />add sometimes used fields</option>
    <option value="{$currtbl}~4.html" ><xsl:value-of select="translate(translate(number($showFlds=4),'1','*'),'0',' ')" />add advanced fields</option>
    <option value="{$currtbl}~9.html" ><xsl:value-of select="translate(translate(number($showFlds=9),'1','*'),'0',' ')" />all fields</option>
  </select>
  A* appears before the current setting of how many fields are shown in this picklist.
  </xsl:when>
  <xsl:otherwise>
  <br/>
    <a href="{$currtbl}~1{$suffix}.html" ><xsl:value-of select="translate(translate(number($showFlds=1),'1','*'),'0',' ')" />Required Fields Only</a><br />
    <a href="{$currtbl}~2{$suffix}.html" ><xsl:value-of select="translate(translate(number($showFlds=2),'1','*'),'0',' ')" />add commonly used fields</a><br />
    <a href="{$currtbl}~3{$suffix}.html" ><xsl:value-of select="translate(translate(number($showFlds=3),'1','*'),'0',' ')" />add sometimes used fields</a><br />
    <a href="{$currtbl}~4{$suffix}.html" ><xsl:value-of select="translate(translate(number($showFlds=4),'1','*'),'0',' ')" />add advanced fields</a><br />
    <a href="{$currtbl}~9{$suffix}.html" ><xsl:value-of select="translate(translate(number($showFlds=9),'1','*'),'0',' ')" />all fields</a><br />
  A* appears before the current setting of how many fields are shown in this picklist.
  </xsl:otherwise>
</xsl:choose>

  
  </p>
</form>
        
        <xsl:for-each select="table[TableName=$currtbl]">
<p><b><font size="+1"><xsl:value-of select="tableNickName" /></font></b> <br />
<b>Description: </b><xsl:value-of select="TableDescription" /> <br />
<b>Am I required to use this table? </b><xsl:value-of select="TableReqd" /><br />
<b>Database table name: </b><xsl:value-of select="TableName" /></p>
          <table border="1" cellspacing="0" class="sansserif">
<xsl:variable name="strLen">25</xsl:variable>
          <tr><th>FldNm:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><b><xsl:value-of select="FieldName" /></b><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
          <tr><th>exmpl1:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example1" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
          <tr><th>exmpl2:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example2" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
          <tr><th>exmpl3:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example3" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
        <tr><th>exmpl4:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}"><xsl:value-of select="example4" /><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
         <tr><th>comment:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}">

      <xsl:choose>
        <xsl:when test="string-length(exampleComments)&gt;0">
          <textarea rows="3" readonly="readonly"><xsl:value-of select="exampleComments" /></textarea>
        </xsl:when>
        <xsl:otherwise>
         <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>        
        </xsl:otherwise>
      </xsl:choose>
         </td></xsl:for-each></tr>                    
          <tr><th><!--<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>--></th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td><!-- <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>--></td></xsl:for-each></tr>         
           <tr><th>defn:</th><xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]"><td class="{concat('color',fieldUseGroup)}">
           <textarea rows="3" readonly="readonly">
                 <xsl:value-of select="FieldDefinition" />            
</textarea>           <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></xsl:for-each></tr>
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
       </xsl:if>
<br/>
Color Key:
<table border="1" cellspacing="0">
<tr><td class="color1">req'd fld</td>
<td class="color2">often used</td>
<td class="color3">some times</td>
<td class="color4">expert user</td>
<td colspan="2" class="color9">internal field</td></tr>
</table>

<br />
    @vegbank_footer_html_onerow@
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
