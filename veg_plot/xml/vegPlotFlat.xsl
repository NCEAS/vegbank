<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
		flat-ascii text file which will be loaded into the circumscription DB -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="xml"/>
<xsl:template match="/vegPlot">
<xsl:value-of select="project/projectName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/role"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/saluation"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/givenName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/surName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/orgName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/positionName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/address/deliveryPoint"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/address/city"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/address/administrativeArea"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/address/postalCode"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/address/country"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/address/currentFlag"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/authorPlotCode"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/parentPlot"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/amplingMethod "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/coverScale "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotOriginLat"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotOriginLong"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotShape"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotSize"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotSizeAcc"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/altValue"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/altPosAcc"/>
<xsl:value-of select="project/plot/slopeAspect "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/slopeGradient "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/slopePosition"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/hydrologicRegime"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/soilDrainage"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/surfGeo"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/plotStartDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/plotStopDate "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/plotStartDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/dateAccuracy "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/effortLevel "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/strata/stratumType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/strata/stratumCover"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/strata/stratumHeight"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	</xsl:template>
</xsl:stylesheet>
