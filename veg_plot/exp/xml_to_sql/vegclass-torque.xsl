<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" indent="yes" />
  <xsl:template match="/dataModel">
	
<app-data>
<database name="nvc">
	

<!-- GO THRU THE ATTRIBUTES and stick them into the table -->

<table>
	<name><xsl:value-of select="entity/entityName"/></name>
	<xsl:for-each select="entity/attribute">
		"collumn nme="<xsl:value-of select="attName"/> "/>"
	</xsl:for-each>


</table>

</database>

<!--
 <plot> <xsl:text>&#xA;</xsl:text>
                <authorPlotCode>
                <xsl:value-of select="releve_nr"/>
                </authorPlotCode> <xsl:text>&#xA;</xsl:text>
-->



</app-data> 
</xsl:template>
</xsl:stylesheet>
