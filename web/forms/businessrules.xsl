<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:vegbank="none"  xmlns:logic="none"  xmlns:bean= "none" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="xml" encoding="UTF-8" />
<xsl:template match="/">
 

@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<TITLE>View VegBank Business Rules</TITLE>
  @webpage_masthead_html@

  @possibly_center@
<h1>View VegBank Business Rules: current status</h1>
<!--Get standard declaration of rowClass as string: -->
        @subst_lt@% String rowClass = "evenrow"; %@subst_gt@
  
  
  <xsl:apply-templates />

  @webpage_footer_html@

</xsl:template>
<xsl:template match="busRule">
<a name="{sqlSrc}">  </a>  
<xsl:variable name="countSQL">br_count_<xsl:value-of select="substring-after(sqlSrc,'br_')" /></xsl:variable>      
<vegbank:get id="{$countSQL}" select="{$countSQL}" beanName="map" pager="false" perPage="-1"/>

<logic:empty name="{$countSQL}-BEANLIST">
<p class="error">ERROR in trying to get <xsl:value-of select="name" /> (<xsl:value-of select="$countSQL" />).  Please check the log. </p>

</logic:empty>

<logic:notEmpty name="{$countSQL}-BEANLIST">


<table width="100%" class="thinlines" cellpadding="2"><tr class="major"><th colspan="3"><xsl:value-of select="name" /></th></tr>
<tr><td class="sizelarger">errors: <bean:write name="{$countSQL}-BEAN" property="howmany" /></td>
    <logic:notEqual name="{$countSQL}-BEAN" property="howmany" value="0"><td class="error">ERRORS! <a href="javascript:showorhidediv('{sqlSrc}')">Show/Hide Details</a></td></logic:notEqual> 
    <logic:equal name="{$countSQL}-BEAN" property="howmany" value="0"><th>NO ERRORS!</th></logic:equal> 
    <td><xsl:value-of select="notes" /></td>
    
</tr>
  <TR><TD COLSPAN="3">
        
   <vegbank:get id="{sqlSrc}" select="{sqlSrc}" beanName="map" pager="false" perPage="-1"/>

<div id='{sqlSrc}' style="display:none">
   <table width="80%" cellpadding="2" class="leftrightborders">
   <logic:iterate name="{sqlSrc}-BEANLIST" id="onerowof{sqlSrc}">
     <xsl:for-each select="detailFields/field">
 
   <tr><td><xsl:value-of select="node()" /></td><td>
   <bean:write name="onerowof{../../sqlSrc}" property="{node()}" />  <xsl:if test="@get='true'"><a href="@machine_url@/get/std/{substring-before(node(),'_')}/@subst_lt@bean:write name='onerowof{../../sqlSrc}' property='{node()}' /@subst_gt@">Details</a></xsl:if></td></tr>
 
   </xsl:for-each>
   <tr><td>Notes:</td><td>
   <xsl:value-of select="notes" /></td></tr>
         <tr><td colspan="2"><hr/></td></tr>  
   </logic:iterate>
   </table>
</div>
  </TD></TR>


</table>

<p><hr/></p>
</logic:notEmpty>

</xsl:template>
</xsl:stylesheet>
