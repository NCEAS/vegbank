<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="html"/>
<xsl:template match="/tableDefns">

<h2>Current VegBank closed list values</h2>
<p>(sort order, if any, is applied)</p>
<table border="1" cellpadding="0" cellspacing="0" width="100%">
<xsl:for-each select="list">

<tr><td colspan="3">
             <b><font size="+2">Table: <xsl:value-of select="TableName"/></font><br/>
              Field: <xsl:value-of select="FieldName"/></b></td></tr>
              <tr><td colspan="3" bgcolor="#FFFFCC"><i><xsl:value-of select="listcomment"/></i></td></tr>
              <tr><!-- <td width="5"><font size="-2">ord</font></td> --><td><b>Value:</b></td><td><b>Description:</b></td></tr>
		<xsl:for-each select="record">
             <xsl:sort  data-type="number" select="sortOrd" />
 <tr>
             <!--       <td><xsl:value-of select="sortOrd" /></td> -->
	 		<td><xsl:value-of select="values"/> <xsl:text></xsl:text></td>
                    <td><xsl:if test="string-length(valueDescription)&gt;'1'"> 
                           <xsl:value-of select="valueDescription"/>
	 		</xsl:if>
	 		<xsl:text>&#xA0;</xsl:text></td>
</tr>
		</xsl:for-each>
 <tr><td colspan="3"><hr/></td></tr> 

</xsl:for-each>
</table>
</xsl:template>
</xsl:stylesheet>

