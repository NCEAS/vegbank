<?xml version="1.0"?>




<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes" />
  <xsl:template match="/dataModel">
	
<app-data>
<database name="nvc">
	

<!-- GO THRU THE ATTRIBUTES AND COLOR IN A STANDARD WAY -->

<table>
	<name><xsl:value-of select="entity/entityName"/></name>
	<xsl:for-each select="entity/attribute">
	<collumn>
		<name><xsl:value-of select="attName"/></name>
		<required><xsl:value-of select="attNulls"/></required>
		<type><xsl:value-of select="attType"/></type>
		
		<xsl:variable name="type">
                	<xsl:value-of select="attType"/>
                </xsl:variable>
                <xsl:if test="substring-before($type,'AR')">
               		<type>VARCHAR</type>
                </xsl:if>

            	
		<size> <xsl:value-of select="attNulls"/> </size>
	</collumn>

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
