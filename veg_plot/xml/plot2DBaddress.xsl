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
	<xsl:text>projectContributor.role|</xsl:text> <xsl:value-of select="role"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!--party information-->
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



<!--Address information about the project contributor-->
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

<!-- telephone related information about the project contributor-->
	<xsl:text>telephone.phoneNumber|</xsl:text> <xsl:value-of select="party/telephone/phoneNumber"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>telephone.phoneType|</xsl:text> <xsl:value-of select="party/telephone/phoneType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!-- email related information about the project contributor-->
	<xsl:text>email.emailAddress|</xsl:text> <xsl:value-of select="party/email/emailAddress"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!-- online resource related information about the project contributor-->
	<xsl:text>onlineResource.linkage|</xsl:text> <xsl:value-of select="party/onlineResource/linkage"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>onlineResource.protocol|</xsl:text> <xsl:value-of select="party/onlineResource/protocol"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>onlineResource.name|</xsl:text> <xsl:value-of select="party/onlineResource/name"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>onlineResource.applicationProfile|</xsl:text> <xsl:value-of select="party/onlineResource/applicationProfile"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:text>onlineResource.description|</xsl:text>  <xsl:value-of select="party/onlineResource/description"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

</xsl:for-each>


<!-- Next block corresponds to the plot table ~ there will only be one attribute here-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text> plot.authorPlotCode|</xsl:text> <xsl:value-of select="project/plot/authorPlotCode"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.parentPlot|</xsl:text> <xsl:value-of select="project/plot/parentPlot"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotType|</xsl:text> <xsl:value-of select="project/plot/plotType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.samplingMethod|</xsl:text> <xsl:value-of select="project/plot/samplingMethod "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.coverScale|</xsl:text> <xsl:value-of select="project/plot/coverScale "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotOrigLat|</xsl:text> <xsl:value-of select="project/plot/plotOriginLat"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotOrigLong|</xsl:text> <xsl:value-of select="project/plot/plotOriginLong"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotShape|</xsl:text> <xsl:value-of select="project/plot/plotShape"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotSize|</xsl:text> <xsl:value-of select="project/plot/plotSize"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.plotSizeAcc|</xsl:text> <xsl:value-of select="project/plot/plotSizeAcc"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.altValue|</xsl:text> <xsl:value-of select="project/plot/altValue"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.altPosAcc|</xsl:text> <xsl:value-of select="project/plot/altPosAcc"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.slopeAspect|</xsl:text> <xsl:value-of select="project/plot/slopeAspect "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.slopeGradient|</xsl:text> <xsl:value-of select="project/plot/slopeGradient "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.slopePosition|</xsl:text> <xsl:value-of select="project/plot/slopePosition"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.hydrologicRegime|</xsl:text> <xsl:value-of select="project/plot/hydrologicRegime"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.soilDrainage|</xsl:text> <xsl:value-of select="project/plot/soilDrainage"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.surfGeo|</xsl:text> <xsl:value-of select="project/plot/surfGeo"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.state|</xsl:text> <xsl:value-of select="project/plot/state"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plot.currentCommunity|</xsl:text> <xsl:value-of select="project/plot/currentCommunity"/>


<!-- next block transforms the attributes for the plotObservationTable-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.previousObs|</xsl:text> <xsl:value-of select="project/plot/plotObservation/previousObs"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.authorObsCode|</xsl:text> <xsl:value-of select="project/plot/plotObservation/authorObsCode"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.plotStartDate|</xsl:text> <xsl:value-of select="project/plot/plotObservation/plotStartDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.plotStopDate|</xsl:text> <xsl:value-of select="project/plot/plotObservation/plotStopDate "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.dateAccuracy|</xsl:text> <xsl:value-of select="project/plot/plotObservation/dateAccuracy "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>plotObservation.effortLevel|</xsl:text> <xsl:value-of select="project/plot/plotObservation/effortLevel "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!-- Next block corresponds to the strata table -->


<xsl:for-each select="project/plot/plotObservation/strata">

<xsl:text>strata.stratumType|</xsl:text> <xsl:value-of select="stratumType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>strata.stratumCover|</xsl:text> <xsl:value-of select="stratumCover"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>strata.stratumHeight|</xsl:text> <xsl:value-of select="stratumHeight"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

</xsl:for-each>


<!-- Next block corresponds to the taxonObservation -->

<xsl:for-each select="project/plot/plotObservation/taxonObservations">
 
<xsl:text>taxonObservation.authNameId|</xsl:text> <xsl:value-of select="authNameId"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>taxonObservation.originalAuthority|</xsl:text> <xsl:value-of select="originalAuthority"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!--select the strata information for that specific species - there may be ocurrences of this plant in multiple starta--> 
<xsl:text>strataComposition.strataType|</xsl:text> <xsl:value-of select="strataComposition/strataType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>strataComposition.percentCover|</xsl:text> <xsl:value-of select="strataComposition/percentCover"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	
<!-- select any interpretation information-->
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
	
</xsl:for-each>

<!-- select the community type information - this will have to be changed to allow for 'lookups' from the NVC database-->

<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>communityType.classAssociation|</xsl:text> <xsl:value-of select="project/plot/plotObservation/communityType/classAssociation"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>communityType.classQuality|</xsl:text> <xsl:value-of select="project/plot/plotObservation/communityType/classQuality"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>communityType.startDate|</xsl:text> <xsl:value-of select="project/plot/plotObservation/communityType/startDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>communityType.stopDate|</xsl:text> <xsl:value-of select="project/plot/plotObservation/communityType/stopDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>


<!--retrieve the information about any possible citation-->

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


<!--Information retrieval for the named place data -->
  
  <xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>namedPlace.placeName|</xsl:text> <xsl:value-of select="project/plot/namedPlace/placeName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>namedPlace.placeDesc|</xsl:text> <xsl:value-of select="project/plot/namedPlace/placeDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>namedPlace.gazateerRef|</xsl:text> <xsl:value-of select="project/plot/namedPlace/gazeteerRef"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>  
 

<!--Information retrieval for the browsedata data -->
  
  <xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>graphic.graphicName|</xsl:text> <xsl:value-of select="project/plot/graphic/browseName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>graphic.graphicDescription|</xsl:text> <xsl:value-of select="project/plot/graphic/browseDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>graphic.graphicType|</xsl:text>  <xsl:value-of select="project/plot/graphic/browseType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> 
<xsl:text>graphic.graphicData|</xsl:text> <xsl:value-of select="project/plot/graphic/browseData"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> 




		
	</xsl:template>
</xsl:stylesheet>
