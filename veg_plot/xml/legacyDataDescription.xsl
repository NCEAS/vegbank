<?xml version="1.0"?> 

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"/>
<xsl:template match="/plotDataPackage">

	<xsl:for-each select="plotDataFile">
		<!-- file name-->
		<xsl:text>fileName|</xsl:text>
		<xsl:value-of select="fileName"/>
		<xsl:text>&#010;</xsl:text>
		
		<!--attribute delimeter -->
		<xsl:text>attributeDelimeter|</xsl:text>
		<xsl:value-of select="attributeDelimeter"/>
		<xsl:text>&#010;</xsl:text>
		
		<!--file theme -->
		<xsl:text>fileTheme|</xsl:text>
		<xsl:value-of select="fileTheme"/>
		<xsl:text>&#010;</xsl:text>
		
		<!--file constraint -->
		<xsl:for-each select="constraint">
			<!-- constraining file-->
			<xsl:text>constraint.fileName|</xsl:text> 
			<xsl:value-of select="fileName"/>
			<xsl:text>&#010;</xsl:text> 
			
			<!-- constraining file theme-->
			<xsl:text>constraint.themeName|</xsl:text> 
			<xsl:value-of select="themeName"/>
			<xsl:text>&#010;</xsl:text> 
			
			<!-- constraining file attribute-->
			<xsl:text>constraint.attributeName|</xsl:text> 
			<xsl:value-of select="attributeName"/>
			<xsl:text>&#010;</xsl:text> 
			
			<!-- constraining file cardality -->
			<xsl:text>constraint.cardnality|</xsl:text> 
			<xsl:value-of select="cardnality"/>
			<xsl:text>&#010;</xsl:text> 
			
		</xsl:for-each>
		
		<!-- file attributes-->
		<xsl:for-each select="attribute">
			<!-- attribute name  in the file-->
			<xsl:text>attributeName|</xsl:text> 
			<xsl:value-of select="attributeName"/>
			<xsl:text>|</xsl:text>
			<xsl:value-of select="plotDBAttribute"/>
			<xsl:text>|</xsl:text>
			<xsl:value-of select="attributePosition"/>
			<xsl:text>&#010;</xsl:text> 
			
			<!-- 
			<xsl:text>plotDBAttribute|</xsl:text> 
			<xsl:value-of select="plotDBAttribute"/>
			<xsl:text>&#010;</xsl:text> 
			-->
		</xsl:for-each>
	
	<xsl:text>&#010;</xsl:text>
	</xsl:for-each>
	

</xsl:template>
</xsl:stylesheet>

