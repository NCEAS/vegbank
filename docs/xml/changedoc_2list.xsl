<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="model_pieces_toSQL.xsl" />

  <xsl:output method="html"/>
  <xsl:param name="getID">24</xsl:param>
  <xsl:param name="apos">'</xsl:param>
  <xsl:param name="replc">`</xsl:param>
  <xsl:param name="level">1</xsl:param><!-- 1 is for postgres, 2 is for access -->
  <xsl:template match="* "/>
<xsl:template match="/">
<html>
<head>
  <link rel="stylesheet" type="text/css" href="http://vegbank.org/vegbank/includes/default.css" />
</head>
<body>    <xsl:apply-templates/>
</body>
</html>

  </xsl:template>

  <xsl:template match="modelChangeDocument">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="modelChange">
    <!-- <xsl:if test="modelChangeID=$getID"> -->
      <xsl:if test="modelChangeVersion='1.0.2'"> 
      <xsl:apply-templates/>
    </xsl:if>
  </xsl:template>
  <xsl:template match="change">
      <xsl:choose>
      <xsl:when test="@type='create table'">
<h2>--------------------------new tables:---------------------------</h2>
<p><xsl:value-of select="../modelChangeComments" /></p>
<table><tr><td bgcolor="#FFFFCC" valign="middle">NEW</td><td><xsl:apply-templates /></td></tr></table>      </xsl:when>
      <xsl:when test="@type='drop table'">
<h2>----------------------removing tables:-----------------------------</h2>
<p><xsl:value-of select="../modelChangeComments" /></p>
<table><tr><td bgcolor="#CCCCCC" valign="middle">DEL</td><td><xsl:apply-templates /></td></tr></table>

      </xsl:when>
      <xsl:when test="@type='create field'">
<h2>--------------------creating new fields on extant tables----------------------</h2>
<p><xsl:value-of select="../modelChangeComments" /></p>
<table><tr><td bgcolor="#FFFFCC" valign="middle">NEW</td><td><xsl:apply-templates /></td></tr></table>
      </xsl:when>
      <xsl:when test="@type='drop field'">
<h2>----------------removing fields -------------------</h2>
<p><xsl:value-of select="../modelChangeComments" /></p>
<table><tr><td bgcolor="#CCCCCC" valign="middle">DEL</td><td><xsl:apply-templates /></td></tr></table>


      </xsl:when>
      <xsl:when test="@type='alter field'">
<h2>----altering fields new definitions--------</h2>
<p><xsl:value-of select="../modelChangeComments" /></p>
<table><tr><td bgcolor="#CCCCFF" valign="middle">Chng</td><td><xsl:apply-templates /></td></tr></table>

      </xsl:when>

    </xsl:choose>

  
  </xsl:template>
<xsl:template match="entity">
<a name="{concat(translate(../../change/@type,' ','_'),'-',entityName)}" >
<h4><xsl:value-of select="entityName" /></h4></a>
<p><xsl:value-of select="entitySummary" /></p>
   <table border="1" cellpadding="0" cellspacing="0">
   <xsl:for-each select="attribute">
   <tr>
      <td class="category"><p><a name="{concat(translate(../../../change/@type,' ','_'),'-',../entityName,'-',attName)}" ><xsl:value-of select="attName" /></a><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;</xsl:text></p></td> <td class="item"><xsl:value-of select="attType" /></td><td class="itemsmall"><xsl:value-of select="attDefinition" />    </td>
   </tr>
   </xsl:for-each>
   </table>
</xsl:template> 

</xsl:stylesheet>
