<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <xsl:param name="alphahigh">QWERTYUIOPASDFGHJKLZXCVBNM</xsl:param>
  <xsl:param name="alphalow">qwertyuiopasdfghjklzxcvbnm</xsl:param>
  <xsl:output method="xml"  omit-xml-declaration="yes" />
  <xsl:template match="demo">
vegbr-demo-linksfromfile-<xsl:value-of select="translate(file,$alphahigh,$alphalow)" />=<p><xsl:text disable-output-escaping="yes">&amp;laquo;Previous: </xsl:text>
<xsl:for-each select="preceding-sibling::demo[position()=1]"><xsl:call-template name="linkTo" /></xsl:for-each>
<xsl:if test="count(preceding-sibling::demo[position()=1])=0">(none)</xsl:if> | <xsl:text disable-output-escaping="yes">&amp;raquo;Next: </xsl:text>
<xsl:for-each select="following-sibling::demo[position()=1]"><xsl:call-template name="linkTo" /></xsl:for-each>
<xsl:if test="count(following-sibling::demo[position()=1])=0">(none)</xsl:if><xsl:if test="count(parent::demo)&gt;0"><br/>Up to:<xsl:for-each select="parent::demo"><xsl:call-template name="linkTo" /><br /></xsl:for-each></xsl:if><xsl:if test="count(child::demo)&gt;0"><br/>MORE DETAIL: <br/><xsl:for-each select="demo"><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text><xsl:call-template name="linkTo" /><br/></xsl:for-each>
</xsl:if></p><xsl:if test="string-length(form)&gt;0"><p>More help available <a href="@vegbranch_link@help/{version}/Help.form.{translate(form,$alphahigh,$alphalow)}.html">here</a>.</p></xsl:if><p>Back to <a href="@vegbranch_link@vegbranch.html">VegBranch Main Page</a>.</p> <xsl:apply-templates />
  </xsl:template>
  <xsl:template name="linkTo">
  <a href="@vegbranch_demo_link@{file}"><xsl:value-of select="label" /></a>
  </xsl:template>
  <xsl:template match="/demos">
    <xsl:apply-templates />
   <xsl:call-template name="writeTokensForForms" />
  </xsl:template>
  <xsl:template match="file|label|version|form" />
  <xsl:template name="writeTokensForForms">
   <xsl:for-each select="descendant-or-self::demo" >
      <xsl:sort select="form" />
       <xsl:sort select="version" />
 <!--       'start  iterations -->
<!--      <xsl:call-template name="oneForm"><xsl:with-param name=""</xsl:call-template> -->
  <xsl:variable name="lastName"><xsl:call-template name="getLastName"><xsl:with-param name="currPos" select="number(-1+position())" /></xsl:call-template></xsl:variable>
<xsl:call-template name="oneForm" ><xsl:with-param name="IAM" select="form" /><xsl:with-param name="lastWas" select="$lastName" /></xsl:call-template>  </xsl:for-each>
  </xsl:template>
  <xsl:template name="oneForm">
    <xsl:param name="IAM"/>
    <xsl:param name="lastWas"/>
 <!--   IAM=<xsl:value-of select="$IAM" />  ; last was=<xsl:value-of select="$lastWas" /> -->
    <xsl:if test="string-length($IAM)&gt;0">
    <xsl:if test="$IAM!=$lastWas">
vegbr-demo-linksfromform-<xsl:value-of select="translate(form,$alphahigh,$alphalow)" />=<p>Video demonstration(s) available:</p></xsl:if>
<p><xsl:call-template name="linkTo" /> (version: <xsl:value-of select="version" />)</p>
</xsl:if>
  </xsl:template>
<xsl:template name="getLastName">
  <xsl:param name="currPos" />
  <xsl:for-each select="/">
    <xsl:for-each select="descendant-or-self::demo">
             <xsl:sort select="form" />
            <xsl:sort select="version" />
            <xsl:if test="position()=$currPos"><xsl:value-of select="form" /></xsl:if>
    </xsl:for-each>
  </xsl:for-each>
</xsl:template>
</xsl:stylesheet>
