<?xml version="1.0"?> 

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"/>
<xsl:template match="/">

	<xsl:for-each select="plantTaxa/taxon">
	
		<!--taxonUnit-->
		<xsl:text>tsnValue|</xsl:text>
		<xsl:value-of select="taxonUnit"/>
		<xsl:text>&#010;</xsl:text>
		
		<!--concatenated name-->
		<xsl:text>concatenatedLongName|</xsl:text>
		<xsl:value-of select="concatenatedLongName"/>
		<xsl:text>&#010;</xsl:text>
		
		<!--concatenated name-->
		<xsl:text>concatenatedName|</xsl:text>
		<xsl:value-of select="concatenatedName"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- Rank -->
		<xsl:text>rank|</xsl:text>
		<xsl:value-of select="classLevel"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- Initial Date -->
		<xsl:text>initialDate|</xsl:text>
		<xsl:value-of select="InitialTimeStamp"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- Update Date -->
		<xsl:text>updateDate|</xsl:text>
		<xsl:value-of select="TaxonUpdateDate"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- Parent concat name -->
		<xsl:text>parentName|</xsl:text>
		<xsl:value-of select="parentName"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- Author name -->
		<xsl:text>authorName|</xsl:text>
		<xsl:value-of select="authorName"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- Author update date -->
		<xsl:text>authorUpdateDate|</xsl:text>
		<xsl:value-of select="Author/AuthorUpdateDate"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- Author update date -->
		<xsl:text>itisUsage|</xsl:text>
		<xsl:value-of select="status"/>
		<xsl:text>&#010;</xsl:text>
		
		<!-- THERE MAY BE MULTIPLE SYNONYM NAMES FOR A GIVEN TAXON -->
		<!--
		<xsl:for-each select="Synonym">
		-->
		
			<!-- Synonomy name -->
			<xsl:text>synonymousName|</xsl:text>
			<xsl:value-of select="synonymName"/>
			<xsl:text>&#010;</xsl:text>
			
				<!-- Plant Code  -->
			<xsl:text>plantCode|</xsl:text>
			<xsl:value-of select="plantCode"/>
			<xsl:text>&#010;</xsl:text>
			
			<!-- common name -->
			<xsl:text>commonName|</xsl:text>
			<xsl:value-of select="commonName"/>
			<xsl:text>&#010;</xsl:text>
			
			<!-- familyName  -->
			<xsl:text>familyName|</xsl:text>
			<xsl:value-of select="familyName"/>
			<xsl:text>&#010;</xsl:text>
			
		
		<!--
		</xsl:for-each>
		-->
		
		<!-- publication Name - add more later -->
		<xsl:text>publicationName|</xsl:text>
		<xsl:value-of select="Publication/PublicationName"/>
		<xsl:text>&#010;</xsl:text>		
		
	

		<xsl:text>&#010;</xsl:text>
		
		</xsl:for-each>
			

</xsl:template>
</xsl:stylesheet>

