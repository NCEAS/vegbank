<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text file which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="xml"/>
<xsl:template match="/vegPlot">

<!--Information about the toplevel project information -->
<xsl:value-of select="project/projectName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!--project contributor information-->
<xsl:value-of select="project/projectContributor/role"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!--party information-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/salutation "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/givenName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/surName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/organizationName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/positionName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/hoursOfService"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/contactInstructions"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>



<!--Address information about the project contributor-->
<xsl:value-of select="project/projectContributor/party/address/deliveryPoint"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/address/city"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/address/administrativeArea"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/address/postalCode"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/address/country"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/address/currentFlag"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!-- telephone related information about the project contributor-->
<xsl:value-of select="project/projectContributor/party/telephone/phoneNumber"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/telephone/phoneType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!-- email related information about the project contributor-->
<xsl:value-of select="project/projectContributor/party/email/emailAddress"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!-- online resource related information about the project contributor-->
<xsl:value-of select="project/projectContributor/party/onlineResource/linkage"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/onlineResource/protocol"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/onlineResource/name"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/onlineResource/applicationProfile"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/projectContributor/party/onlineResource/description"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>



<!-- Next block corresponds to the plot table-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/authorPlotCode"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/parentPlot"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/samplingMethod "/>
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
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
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

<!-- next block transforms the attributes for the plotObservationTable-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/plotStartDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/plotStopDate "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/dateAccuracy "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/effortLevel "/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!-- Next block corresponds to the strata table -->


<xsl:for-each select="project/plot/plotObservation/strata">

<xsl:value-of select="stratumType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="stratumCover"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="stratumHeight"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

</xsl:for-each>


<!-- Next block corresponds to the taxonObservation -->

<xsl:for-each select="project/plot/plotObservation/taxonObservations">
 
	<xsl:value-of select="authNameId"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="originalAuthority"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>

<!--select the strata information for that specific species - there may be ocurrences of this plant in multiple starta--> 
	<xsl:value-of select="strataComposition/strataType"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="strataComposition/percentCover"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	
<!-- select any interpretation information-->
	<xsl:value-of select="interpretation/party"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="interpretation/taxonName"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="interpretation/authority"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="interpretation/usageStartDate"/>	
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>	
	<xsl:value-of select="interpretation/usageStopDate"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	
</xsl:for-each>

<!-- select the community type information - this will have to be changed to allow for 'lookups' from the NVC database-->

<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/communityType/classAssociation"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/communityType/classQuality"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/communityType/startDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/communityType/stopDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>


<!--retrieve the information about any possible citation-->

<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/title"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/altTitle"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/pubDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/edition"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/editionDate"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/seriesName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/issueIdentification"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/otherCredentials"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/page"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/isbn"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/plotObservation/citation/issn"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>


<!--Information retrieval for the named place data -->
  
  <xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/namedPlace/placeName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/namedPlace/placeDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/namedPlace/gazeteerRef"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>  
 

<!--Information retrieval for the browsedata data -->
  
  <xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/graphic/browseName"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/graphic/browseDescription"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:value-of select="project/plot/graphic/browseType"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> 
<xsl:text>  graphic.graphicData </xsl:text> <xsl:value-of select="project/plot/graphic/browseData"/>
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text> 


 

		
	</xsl:template>
</xsl:stylesheet>
