<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect"> 
  <xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/> 

<xsl:output method="html"/>
<xsl:param name="htmlsuffix">.html</xsl:param>
<xsl:template name="tempNAme"><xsl:comment> [<xsl:value-of select="name" />]</xsl:comment></xsl:template>
<xsl:template match="/vegbranchHelp/version">
WRITING TO:<xsl:value-of select="concat(versionNumber,'/index.html')" /><br/> 
  <redirect:write file="{versionNumber}/index.html">
 <html>
  <xsl:call-template name="writehtmlhead" />
<body>
<h2>Index of help forms in VegBranch.</h2>
<p>Select one of the following forms to see help about it:</p>
<p>
  <xsl:for-each select="form">
   <xsl:sort select="formType" />
   <xsl:sort select="caption" />

       <xsl:variable name="currPos" select="position()" />
       <xsl:call-template name="dontDuplTypes"><xsl:with-param name="thisType" select="formType" /><xsl:with-param name="pos" select="position()" /> </xsl:call-template>

  </xsl:for-each>
</p>
</body></html>
  </redirect:write>
  <xsl:apply-templates />
</xsl:template>

<xsl:template name="dontDuplTypes">
  <xsl:param name="thisType" />
  <xsl:param name="pos" />
  <xsl:variable name="lastType">
    <xsl:for-each select="../form">
      <xsl:sort select="formType" />
      <xsl:sort select="caption" />
      <xsl:if test="position()=(($pos)-1)">
        <xsl:value-of select="formType" />
      </xsl:if>
    </xsl:for-each>
  </xsl:variable>
  
  <xsl:if test="$thisType!=$lastType"> 
    <br/>----<b><xsl:value-of select="$thisType" /></b>----<br/><br/>
  </xsl:if>
  <xsl:if test="subfrm!=1">
   <a href="Help.form.{name}{$htmlsuffix}"><xsl:value-of select="caption" /></a><br /></xsl:if>
</xsl:template>

<xsl:template match="form">
<!-- write one Help. for the form itself -->
WRITING TO:<xsl:value-of select="concat(../versionNumber,'/Help.form.',name,$htmlsuffix)" /><br/> 
     <redirect:write file="{../versionNumber}/Help.form.{name}{$htmlsuffix}">
        <frameset cols="*,220">
           <frame name="upperframe" src="form.{name}{$htmlsuffix}" />
           <frame name="lowerframe" src="" />
        </frameset>
     </redirect:write>
WRITING TO:<xsl:value-of select="concat(../versionNumber,'/form.',name,$htmlsuffix)" /><br/> 
 <redirect:write file="{../versionNumber}/form.{name}{$htmlsuffix}">
<html>
  <xsl:call-template name="writehtmlhead" />
<body>
  <h2>Help for '<xsl:value-of select="caption" /> <xsl:call-template name="tempNAme" />'</h2>
  <img src="screenshots/{name}.png" alt="screen shot of '{caption}'" />
  <p><xsl:copy-of select="help" />
  <xsl:call-template name="getCtls"><xsl:with-param name="pathSoFar">form.<xsl:value-of select="name" /></xsl:with-param></xsl:call-template>
  </p>
  <p>Show me an <a href="index.html" target="_top">index of forms</a>.</p>
</body></html>
</redirect:write>
  <xsl:call-template name="getCtls_rightframe"><xsl:with-param name="pathSoFar">form.<xsl:value-of select="name" /></xsl:with-param>
  <xsl:with-param name="path4form">form.<xsl:value-of select="name" /></xsl:with-param><xsl:with-param name="vers"><xsl:value-of select="../versionNumber" />/</xsl:with-param>
  </xsl:call-template>
  </xsl:template>

 <xsl:template name="getCtls_rightframe">
   <xsl:param name="pathSoFar" />
   <xsl:param name="path4form" />
   <xsl:param name="vers" />
   <xsl:for-each select="control">
WRITING TO:<xsl:value-of select="concat($vers,$pathSoFar,'.',name,$htmlsuffix)" /><br/> 
     <redirect:write file="{$vers}{$pathSoFar}.{name}{$htmlsuffix}">
<html>
  <xsl:call-template name="writehtmlhead" />
<body>
       <xsl:call-template name="getCtlRep"><xsl:with-param name="ctlType" select="ctlType" /><xsl:with-param name="caption" select="caption" /></xsl:call-template>
       <hr/>
       <p>
       <xsl:choose>
        <xsl:when test="string-length(help)&gt;0"><xsl:copy-of select="help" /></xsl:when>
        <xsl:otherwise>Sorry, no help for this yet.</xsl:otherwise>
      </xsl:choose>
       </p>
</body></html>
     </redirect:write>
WRITING TO:<xsl:value-of select="concat($vers,'Help.',$pathSoFar,'.',name,$htmlsuffix)" /><br/> 
     <redirect:write file="{$vers}Help.{$pathSoFar}.{name}{$htmlsuffix}">
        <frameset cols="*,220">
           <frame name="upperframe" src="{$path4form}{$htmlsuffix}" />
           <frame name="lowerframe" src="{$pathSoFar}.{name}{$htmlsuffix}" />
        </frameset>
     </redirect:write>
     <xsl:call-template name="getCtls_rightframe"><xsl:with-param name="pathSoFar"><xsl:value-of select="$pathSoFar"/>.<xsl:value-of select="name" /></xsl:with-param>
       <xsl:with-param name="path4form" select="$path4form" /><xsl:with-param name="vers" select="$vers" />
     </xsl:call-template>
   </xsl:for-each>


 </xsl:template>
  
  <xsl:template name="writehtmlhead">
    <head>
      <title>VegBranch help</title>
      <link rel="stylesheet" type="text/css" href="http://vegbank.org/vegbank/includes/default.css" />
    </head>
  </xsl:template>
  
  <xsl:template name="getCtls">
  <xsl:param name="pathSoFar" />
  
  <xsl:if test="count(control)&gt;0">
  <table border="0" cellpadding="0" cellspacing="0"><tr><td><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text></td><td><p>
  <xsl:for-each select="control">
    <!-- write help for each control, min: links -->
      <a href="{$pathSoFar}.{name}{$htmlsuffix}" target="lowerframe"><img border="0" src="http://vegbank.org@images_link@question.gif" alt="help?" /></a> <xsl:call-template name="getCtlRep"><xsl:with-param name="ctlType" select="ctlType" /><xsl:with-param name="caption" select="caption" /></xsl:call-template><xsl:call-template name="tempNAme" /><br/>
        <xsl:call-template name="getCtls"><xsl:with-param name="pathSoFar"><xsl:value-of select="$pathSoFar"/>.<xsl:value-of select="name" /></xsl:with-param></xsl:call-template>
 </xsl:for-each>
</p></td></tr></table> 
</xsl:if>
</xsl:template>
<xsl:template name="getCtlRep">
  <xsl:param name="ctlType"/>
  <xsl:param name="caption" />
  <xsl:choose>
    <xsl:when test="$ctlType=104"><!-- button --><input type="button" value="{$caption}" /> <i> button</i></xsl:when>
    <xsl:when test="$ctlType=111"><!-- picklist --><xsl:value-of select="$caption" /> <select><option>picklist</option></select></xsl:when>
    <xsl:when test="$ctlType=106"><!--check --><xsl:value-of select="$caption" /> <input type="checkbox" /></xsl:when>
    <xsl:when test="$ctlType=109"><!-- field --><xsl:value-of select="$caption" /> <input value="field" /></xsl:when>
    <xsl:when test="$ctlType=112"><!-- usubform --><xsl:value-of select="$caption"/> <i> panel or subform</i></xsl:when>
    <xsl:when test="$ctlType=107"><!-- option group --><xsl:value-of select="$caption" /> <i> options box</i></xsl:when>
    <xsl:when test="$ctlType=105"><!-- radio --><input type="radio" /><xsl:value-of select="$caption" /> <i> option</i></xsl:when>
    <xsl:when test="$ctlType=110"><!-- list box --><xsl:value-of select="$caption" /><select size="2"><option>list of</option><option>options</option></select></xsl:when>
    <xsl:when test="$ctlType=123"><!-- tab control --><xsl:value-of select="$caption" /><i> Tabbed pages: </i><table class="vegbank_tiny"  border="0" cellpadding="0" cellspacing="0" >
       <tr><td bgcolor="#CCCCCC">Page 1</td><td bgcolor="#FFFFFF"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td><td bgcolor="#999999">Page 2</td><td bgcolor="#FFFFFF"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td><td bgcolor="#999999">Page 3</td></tr>
       <tr><td bgcolor="#CCCCCC" colspan="5"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></tr>
       </table> </xsl:when>
    <xsl:when test="$ctlType=124"><!--page --><table class="vegbank_tiny" border="0" cellpadding="0" cellspacing="0" >
       <tr><td bgcolor="#CCCCCC"><xsl:value-of select="$caption" /></td><td colspan="2" bgcolor="#FFFFFF"><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text></td></tr>
       <tr><td bgcolor="#CCCCCC" colspan="3"> (page contents)</td></tr>
       </table></xsl:when>
    <xsl:otherwise><xsl:value-of select="$caption" /><i> [another control type: <xsl:value-of select="$ctlType" />] </i> <xsl:value-of select="$caption" /></xsl:otherwise>
  </xsl:choose><!-- acBoundObjectFrame,108
acCheckBox,106
acComboBox,111
acCommandButton,104
acImage,103
acLabel,100
acLine,102
acListBox,110
acObjectFrame,114
acOptionButton,105
acOptionGroup,107
acPage,124
acPageBreak,118
acRectangle,101
acSubform,112
acTabCtl,123
acTextBox,109
acToggleButton,122 --></xsl:template>


<xsl:template match="name|caption|help|ctlType" />
</xsl:stylesheet>
