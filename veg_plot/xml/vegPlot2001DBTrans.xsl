<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="xml"/>
<xsl:template match="/vegPlot">

<!--Information about the toplevel project information -->
<xsl:text> project.projectName|</xsl:text> <xsl:value-of select="project/projectName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text> project.projectDescription|</xsl:text> <xsl:value-of select="project/projectDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>



<!-- There may be multiple project contributors so do a for-each select-->
<xsl:for-each select="project/projectContributor">

<!--project contributor information-->
<!--
<xsl:text>projectContributor.role|</xsl:text> <xsl:value-of select="role"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
-->

<!--party information-->
<!--	
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>party.salutation|</xsl:text> <xsl:value-of select="party/salutation "/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>party.givenName|</xsl:text> <xsl:value-of select="party/givenName"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>party.surName|</xsl:text> <xsl:value-of select="party/surName"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>party.organizationName|</xsl:text> <xsl:value-of select="party/organizationName"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>party.positionName|</xsl:text> <xsl:value-of select="party/positionName"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>party.hoursOfService|</xsl:text> <xsl:value-of select="party/hoursOfService"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>party.contactInstructions|</xsl:text> <xsl:value-of select="party/contactInstructions"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
-->


<!--Address information about the project contributor-->
<!--
	<xsl:text>address.deliveryPoint|</xsl:text> <xsl:value-of select="party/address/deliveryPoint"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>address.city|</xsl:text> <xsl:value-of select="party/address/city"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>address.administrativeArea|</xsl:text> <xsl:value-of select="party/address/administrativeArea"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>adddress.postalCode|</xsl:text> <xsl:value-of select="party/address/postalCode"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>address.country|</xsl:text> <xsl:value-of select="party/address/country"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>address.currentFlag|</xsl:text> <xsl:value-of select="party/address/currentFlag"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
-->


<!-- telephone related information about the project contributor-->
<!--
	<xsl:text>telephone.phoneNumber|</xsl:text> <xsl:value-of select="party/telephone/phoneNumber"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>telephone.phoneType|</xsl:text> <xsl:value-of select="party/telephone/phoneType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
-->

<!-- email related information about the project contributor-->
<!--
	<xsl:text>email.emailAddress|</xsl:text> <xsl:value-of select="party/email/emailAddress"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
-->

</xsl:for-each>


<!-- This next block isolates the information to be stored in the plot table 
	which is basically the site information-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text> plot.authorPlotCode|</xsl:text> <!-- author plot code-->
	<xsl:value-of select="project/plot/authorPlotCode"/> 
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.topoPosition|</xsl:text> <!-- topo position-->
	<xsl:value-of select="project/plot/topoPosition"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotType|</xsl:text> <!-- plot type-->
	<xsl:value-of select="project/plot/plotType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.coverScale|</xsl:text> <!-- cover scale-->
	<xsl:value-of select="project/plot/coverScale "/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.origLat|</xsl:text> <!-- latitude -->
	<xsl:value-of select="project/plot/origLat"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.origLong|</xsl:text> <!-- longitude -->
	<xsl:value-of select="project/plot/origLong"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotShape|</xsl:text> <!--plot shape -->
	<xsl:value-of select="project/plot/plotShape"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotArea|</xsl:text> <!-- plot area -->
	<xsl:value-of select="project/plot/plotArea"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.elevationValue|</xsl:text> <!-- elevation value -->
	<xsl:value-of select="project/plot/elevationValue"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.slopeAspect|</xsl:text> <!-- slope aspect-->
	<xsl:value-of select="project/plot/slopeAspect "/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.slopeGradient|</xsl:text> <!-- slope gradient-->
	<xsl:value-of select="project/plot/slopeGradient "/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.topoPosition|</xsl:text> <!-- topo position -->
	<xsl:value-of select="project/plot/topoPosition"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.hydrologicRegime|</xsl:text> <!-- hydologic regime-->
	<xsl:value-of select="project/plot/hydrologicRegime"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.soilDrainage|</xsl:text> <!-- soil drainage-->
	<xsl:value-of select="project/plot/soilDrainage"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.surfGeo|</xsl:text> <!-- surface geology-->
	<xsl:value-of select="project/plot/surfGeo"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.state|</xsl:text> <!-- state -->
	<xsl:value-of select="project/plot/state"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>



<!-- next block transforms the attributes for the plotObservationTable-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.soilDepth|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/soilDepth"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.soilType|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/soilType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.percentRockGravel|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/percentRockGravel"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<xsl:text>plotObservation.authorObsCode|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/authorObsCode"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.perSmallRx|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/perSmallRx"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.perSand|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/perSand "/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	
<xsl:text>plotObservation.perLitter|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/perLitter"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	
<xsl:text>plotObservation.perWood|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/perWood"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.perBareSoil|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/perBareSoil"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.leafPhenology|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/leafPhenology "/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.leafType|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/leafType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.physioClass|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/physioClass"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	
<!-- Next block corresponds to the strata table -->


<xsl:for-each select="project/plot/plotObservation/strata">

<xsl:text>plotObservation.strata.stratumName|</xsl:text> 
	<xsl:value-of select="strataName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.strata.stratumCover|</xsl:text> 
	<xsl:value-of select="strataCover"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.strata.stratumHeight|</xsl:text> 
	<xsl:value-of select="strataHeight"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

</xsl:for-each>


<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> <!-- new line -->

<!-- Next block corresponds to the taxonObservation -->

<xsl:for-each select="project/plot/plotObservation/taxonObservation">
<xsl:text>taxonObservation.authNameId|</xsl:text> 
	<xsl:value-of select="authNameId"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>taxonObservation.originalAuthority|</xsl:text> 
	<xsl:value-of select="originalAuthority"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!--select the strata information for that specific species - 
there may be ocurrences of this plant in multiple starta--> 
<xsl:text>taxonObservation.strataComposition.strataType|</xsl:text> 
	<xsl:value-of select="strataComposition/strataType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>taxonObservation.strataComposition.percentCover|</xsl:text> 
	<xsl:value-of select="strataComposition/percentCover"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> <!-- new line -->

<!-- select any interpretation information-->
<!--
<xsl:text>interpretationParty|</xsl:text> <xsl:value-of select="interpretation/party"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>interpretation.taxonName|</xsl:text> <xsl:value-of select="interpretation/taxonName"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>interpretation.authority|</xsl:text> <xsl:value-of select="interpretation/authority"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>interpretation.start|</xsl:text> <xsl:value-of select="interpretation/usageStartDate"/>	
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>	
<xsl:text>interpretation.stop|</xsl:text> <xsl:value-of select="interpretation/usageStopDate"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
-->
	
	
</xsl:for-each>

<!-- select the community type information - this will have to be changed to allow for 'lookups' from the NVC database-->

<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> <!-- new line -->
<xsl:text>communityAssignment.classAssociation|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/communityAssignment/communityName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>communityAssignment.CEGLCode|</xsl:text> 
	<xsl:value-of select="project/plot/plotObservation/communityAssignment/CEGLCode"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>


<!--retrieve the information about any possible citation-->
<!--
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.title|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/title"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.altTitle|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/altTitle"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.pubDate|</xsl:text><xsl:value-of select="project/plot/plotObservation/citation/pubDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.edition|</xsl:text><xsl:value-of select="project/plot/plotObservation/citation/edition"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.editionName|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/editionDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.seriesName|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/seriesName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.issueIdentification|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/issueIdentification"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.otherCitationDetails|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/otherCredentials"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.page|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/page"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.ISBN|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/isbn"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>citation.ISSN|</xsl:text> <xsl:value-of select="project/plot/plotObservation/citation/issn"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
-->


<!--Information retrieval for the named place data -->
<!--  
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>namedPlace.placeName|</xsl:text> <xsl:value-of select="project/plot/namedPlace/placeName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>namedPlace.placeDesc|</xsl:text> <xsl:value-of select="project/plot/namedPlace/placeDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>namedPlace.gazateerRef|</xsl:text> <xsl:value-of select="project/plot/namedPlace/gazeteerRef"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>  
-->

<!--Information retrieval for the browsedata data -->
<!--  
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>graphic.graphicName|</xsl:text> <xsl:value-of select="project/plot/graphic/browseName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>graphic.graphicDescription|</xsl:text> <xsl:value-of select="project/plot/graphic/browseDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>graphic.graphicType|</xsl:text>  <xsl:value-of select="project/plot/graphic/browseType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> 
<xsl:text>graphic.graphicData|</xsl:text> <xsl:value-of select="project/plot/graphic/browseData"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> 
-->


	</xsl:template>
</xsl:stylesheet>
