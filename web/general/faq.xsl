<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!--capture the information to be put in the name table-->
  <xsl:output method="html"/>
<xsl:template match="/faq">
@webpage_top_html@
  @webpage_head_html@
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
  @webpage_masthead_html@



 <a name="topoffaq"></a>
<h1>VegBank FAQ</h1>

    <p>Frequently Asked Questions about the VegBank database and website.</p>

 <p class="instructions">Please select from the following questions, or browse through this file: <br/>
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


<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p>
<p> <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p>
<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><!-- extra space at bottom so # targets work -->

<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p><p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text> </p>

<p>This space is intentionally left blank so that bookmarks on this page will take you to the correct part of the FAQ.</p>
@webpage_footer_html@

</xsl:template> <!-- root -->

<xsl:template match="question">
  <div id="{qname}">
  <p class="sizenormal">
  <a name="{qname}"><xsl:copy-of select="qtxt" /></a></p>
  <blockquote>
  <p class="sizesmall">
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