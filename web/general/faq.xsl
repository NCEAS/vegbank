<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!--capture the information to be put in the name table-->
  <xsl:output method="html"/>
<xsl:template match="/faq">
<HTML>
  <HEAD>@defaultHeadToken@
 <style type="text/css">
 .local_yellow {background-color : #FFFFCC }	
  .local_white {background-color : #FFFFFF }	
 </style>
 <script type="text/javascript">

 function highlightDivYellow(divId)
 {
	document.getElementById(divId).className="local_yellow";	
}
 function unHighlightALL()
 {
	<xsl:for-each select="category/question">
	document.getElementById('<xsl:value-of select="qname" />').className="local_white";	
	</xsl:for-each>
}
 </script>
<TITLE>VegBank FAQ</TITLE>
    <link rel="stylesheet" href="http://vegbank.org/vegbank/includes/default.css" type="text/css"/>
    <link rel="stylesheet" href="@stylesheet@" type="text/css"/>
  </HEAD>
  <BODY>
@vegbank_header_html_normal@
<table width="799"><tr><td>
<h1 align="center">VegBank FAQ</h1>
    <h4 align="center">Frequently Asked Questions about the VegBank database and website</h4>
 <a name="topoffaq"><h3>Menu of Questions</h3></a>
 <p>Please select from the following questions, or browse through this file: <br/>
 (<a href="#" onclick="unHighlightALL()">clear highlighting on this page</a>.)</p>
 
  <xsl:for-each select="category">
    
      <p><b><a href="#catg_{@name}"><xsl:value-of select="@label" /></a>: </b></p>
      <blockquote>
      
      <p>
       
      <xsl:for-each select="question">
        <a href="#{qname}" onclick="javascript:highlightDivYellow('{qname}');"><xsl:value-of select="qtxt" /></a><xsl:if test="position()!=last()"> <br/> </xsl:if></xsl:for-each>
      
      </p>      </blockquote> 
  </xsl:for-each>

  <hr/>

<xsl:apply-templates />

@vegbank_footer_html_tworow@
<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p>
<p> <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p>

<h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><!-- extra space at bottom so # targets work -->

<h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><h1><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </h1><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p>
<p>This space is intentionally left blank so that bookmarks on this page will take you to the correct part of the FAQ.</p>
</td></tr></table>
</BODY>
</HTML>
</xsl:template> <!-- root -->

<xsl:template match="question">
  <div id="{qname}">
  <p class="vegbank_normal">
  <a name="{qname}"><xsl:copy-of select="qtxt" /></a></p>
  <blockquote>
  <p class="vegbank_small">
    <xsl:copy-of select="atxt" />
  </p>
  </blockquote>
  <p><a href="#topoffaq" onclick="javascript:unHighlightALL()">back to top...</a></p>
  </div><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
  <hr/>
</xsl:template>
<xsl:template match="qname" />
<xsl:template match="category">
  <h4> <a name="catg_{@name}"><xsl:value-of select="@label" /></a></h4>
  <xsl:apply-templates />
</xsl:template>

</xsl:stylesheet>