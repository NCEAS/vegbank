<?xml version="1.0"?> 

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes" />
<xsl:template match="/Plot_package">
<!-- Start the veg plot project-->
<vegPlot> <xsl:text>&#xA;</xsl:text>
	<project> <xsl:text>&#xA;</xsl:text>
	<projectName>TurboVeg Project</projectName> <xsl:text>&#xA;</xsl:text>
	<projectDescription>TurboVeg Data From Europe</projectDescription> <xsl:text>&#xA;</xsl:text>
<!-- SELECT EACH PLOT-->	   
<xsl:for-each select="Plot">

	<!-- THE SITE ATTRIBUTES-->
	<plot> <xsl:text>&#xA;</xsl:text>
		<authorPlotCode>
		<xsl:value-of select="releve_nr"/> 
		</authorPlotCode> <xsl:text>&#xA;</xsl:text>
		
		<importSource>turboVeg</importSource> <xsl:text>&#xA;</xsl:text>
		
		<elevationValue>
		<xsl:value-of select="altitude"/> 
		</elevationValue> <xsl:text>&#xA;</xsl:text>
		
		<country>
		<xsl:value-of select="country"/> 
		</country> <xsl:text>&#xA;</xsl:text>
		
		<!--there are many of these user defined fields - this will 
		cycle thru and select the correct value for 'state', below this
		are other atributes that are arrived at the same way-->
		<state>
		<xsl:for-each select="UDFH_data">
			<xsl:variable name="type">
				<xsl:value-of select="udfh_name"/>
			</xsl:variable>
			<xsl:if test="$type='state'">
				<xsl:value-of select="udfh_value"/>
			</xsl:if>
		</xsl:for-each>	
		</state> <xsl:text>&#xA;</xsl:text>
		
		
		<quadName>
		<xsl:for-each select="UDFH_data">
			<xsl:variable name="type">
				<xsl:value-of select="udfh_name"/>
			</xsl:variable>
			<xsl:if test="$type='quad_name'">
				<xsl:value-of select="udfh_value"/>
			</xsl:if>
		</xsl:for-each>	
		</quadName> <xsl:text>&#xA;</xsl:text>
		
		<utmZone>
		<xsl:for-each select="UDFH_data">
			<xsl:variable name="type">
				<xsl:value-of select="udfh_name"/>
			</xsl:variable>
			<xsl:if test="$type='utm_zone'">
				<xsl:value-of select="udfh_value"/>
			</xsl:if>
		</xsl:for-each>	
		</utmZone> <xsl:text>&#xA;</xsl:text>
		
		<xCoord>
		<xsl:for-each select="UDFH_data">
			<xsl:variable name="type">
				<xsl:value-of select="udfh_name"/>
			</xsl:variable>
			<xsl:if test="$type='utm_e'">
				<xsl:value-of select="udfh_value"/>
			</xsl:if>
		</xsl:for-each>	
		</xCoord> <xsl:text>&#xA;</xsl:text>
		
		<yCoord>
		<xsl:for-each select="UDFH_data">
			<xsl:variable name="type">
				<xsl:value-of select="udfh_name"/>
			</xsl:variable>
			<xsl:if test="$type='utm_n'">
				<xsl:value-of select="udfh_value"/>
			</xsl:if>
		</xsl:for-each>	
		</yCoord> <xsl:text>&#xA;</xsl:text>
		
		<datum>
		<xsl:for-each select="UDFH_data">
			<xsl:variable name="type">
				<xsl:value-of select="udfh_name"/>
			</xsl:variable>
			<xsl:if test="$type='datum'">
				<xsl:value-of select="udfh_value"/>
			</xsl:if>
		</xsl:for-each>	
		</datum> <xsl:text>&#xA;</xsl:text>
		
		
		<placeName><xsl:value-of select="country"/>
		</placeName><xsl:text>&#xA;</xsl:text>
		
		<plotSize>
		<xsl:value-of select="surf_area"/> 
		</plotSize> <xsl:text>&#xA;</xsl:text>
		
		<slopeAspect>
		<xsl:value-of select="exposition"/> 
		</slopeAspect> <xsl:text>&#xA;</xsl:text>
		
		<slopeGradient>
		<xsl:value-of select="inclinatio"/> 
		</slopeGradient> <xsl:text>&#xA;</xsl:text>
		
		
		<!-- OBSERVATION DATA -->
		<plotObservation> <xsl:text>&#xA;</xsl:text>
		
			<plotStartDate>
			<xsl:value-of select="date"/> 
			</plotStartDate> <xsl:text>&#xA;</xsl:text>
			
			<plotStopDate>
			<xsl:value-of select="date"/> 
			</plotStopDate> <xsl:text>&#xA;</xsl:text>
			
			<authorObsCode>
			<xsl:value-of select="date"/>
			<xsl:value-of select="releve_nr"/>
			</authorObsCode> <xsl:text>&#xA;</xsl:text>
			
			<percentWater>
			<xsl:value-of select="cov_water"/> 
			</percentWater> <xsl:text>&#xA;</xsl:text>
			
			<percentRock>
			<xsl:value-of select="cov_rock"/> 
			</percentRock> <xsl:text>&#xA;</xsl:text>
			
			<!-- STRATA DATA -->
			<strata>        
				<stratumCode>1</stratumCode>
				<stratumName>high tree</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>2</stratumCode>
				<stratumName>middle tree</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>3</stratumCode>
				<stratumName>low tree</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>4</stratumCode>
				<stratumName>high shrub</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>5</stratumCode>
				<stratumName>low shrub</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>6</stratumCode>
				<stratumName>herb</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>7</stratumCode>
				<stratumName>juvenile</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>8</stratumCode>
				<stratumName>seedling</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			
			<strata>        
				<stratumCode>9</stratumCode>
				<stratumName>moss</stratumName>
				<stratumCover>0</stratumCover>       
				<stratumHeight>0</stratumHeight>
			</strata>
			


	<!-- SPECIES DATA-->
	<xsl:for-each select="Species_data">
	<xsl:text>&#xA;</xsl:text>
		
		<taxonObservation> <xsl:text>&#xA;</xsl:text>
				<authNameId> 
					<xsl:value-of select="species_name"/> 
				</authNameId> <xsl:text>&#xA;</xsl:text>
				<cumStrataCover></cumStrataCover> <xsl:text>&#xA;</xsl:text>
				
				
				<strataComposition> <xsl:text>&#xA;</xsl:text>
					<strataType> 
					<xsl:value-of select="Species_observations/species_layer"/> 
					</strataType>			<xsl:text>&#xA;</xsl:text>
					<percentCover> 
					<xsl:value-of select="Species_observations/species_cover_code"/> 
					</percentCover>			<xsl:text>&#xA;</xsl:text>
				</strataComposition><xsl:text>&#xA;</xsl:text>
				
				
			</taxonObservation> <xsl:text>&#xA;</xsl:text>
		
	</xsl:for-each>	
<xsl:text>&#xA;</xsl:text>

			</plotObservation> <xsl:text>&#xA;</xsl:text>
		<!-- end the plot -->
		</plot> <xsl:text>&#xA;</xsl:text>

</xsl:for-each>					

	
	</project>
</vegPlot>

</xsl:template>



</xsl:stylesheet>
