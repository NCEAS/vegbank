/**
 * 
 *    Purpose: To write plot data as xml documents that can then be loaded into
 *			the plots database 
 *    Copyright: 2000 Regents of the University of California and the
 *			National Center for Ecological Analysis and Synthesis
 * 		@author @author@ 
 * 		@version @release@ 
 *
 *     '$Author: harris $'
 *     '$Date: 2002-04-09 16:09:53 $'
 *     '$Revision: 1.3 $'
 *
 *
 */
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

import PlotDataSource;


/** 
 * this class handles the conversion from vegbank and other 
 * legacy data sources to the native vegbank XML format
 */
public class PlotXmlWriterV2 
{
	//the plot data source interface
	private PlotDataSource datasrc;
	
	// name of the plot used in this class
	private String plotCode = null;
	
	
	//constructor method
	public PlotXmlWriterV2()
	{
		//construct a new instance of the data source class
		datasrc = new PlotDataSource("TestPlotSourcePlugin");
	}
	
	//second constructor method
	public PlotXmlWriterV2(String pluginClass)
	{
		//construct a new instance of the data source class
		datasrc = new PlotDataSource(pluginClass);
	}
	
	
	/**
	 * method that will handle writing a single plot from
	 * a data source
	 */
	public void writeSinglePlot(String plotName)
	{
		try
		{
			this.writeSinglePlot(plotName, "out.xml");
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage() );
		}
	}
	
	
	/**
	 * overloaded method of the same name above
	 *
	 * @param plotName -- the name (or in some cases) the id of a plot
	 * @param fileName -- the output fully qualified filename with absolute path
	 *
	 */
	 public void writeSinglePlot(String plotName, String fileName)
	 {
		try
		{
			//initialize the class variable, plotCode, with this plot name
			this.plotCode = plotName;
			//the printWriter
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			//String buffer to store the plot
			StringBuffer sb = new StringBuffer();
			//load the public variables in the data source class with those 
			//from the plotName
			datasrc.getPlot(plotName);
			
			sb.append( getPlotHeader() );
			sb.append( getPlotProjectContent() );
			sb.append( getPlotSiteContent() );
				//this is a child of the site content
				sb.append( getPlotPlaceContent() );
			
				sb.append( getPlotObservationContent() );
				
				
			//end tags
				sb.append( getPlotObservationEndTag() );
			sb.append( getPlotSiteEndTag() );
			sb.append( getPlotProjectEndTag() );
			sb.append( getPlotFooter() );
			
			out.println( sb.toString() );
			out.close();
			//System.out.println("datasrc: " + datasrc.state);
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage() );
		}
	}
	
	
	
	
	/**
	 * overloaded method of the same name above
	 *
	 * @param plotNameVec -- a vector containg the names 
	 *	(or in some cases) the ids of a plot
	 * @param fileName -- the output fully qualified filename with absolute path
	 *
	 */
	 public void writeMultiplePlot(Vector plotNameVec, String fileName)
	 {
		try
		{
			//initialize the class variable, plotCode, with this plot name
			//this.plotCode = plotName;
			//the printWriter
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			//String buffer to store the plot
			StringBuffer sb = new StringBuffer();
			//load the public variables in the data source class with those 
			//from the plotName
			
			sb.append( getPlotHeader() );
			
			//get each of the plots in the vector
			for (int i=0; i< plotNameVec.size(); i++)
			{
				String plotName = plotNameVec.elementAt(i).toString();
				datasrc.getPlot(plotName);
				
				//initialize the class variable, plotCode, with this plot name
				this.plotCode = plotName;
			
				sb.append( getPlotProjectContent() );
				sb.append( getPlotSiteContent() );
				//this is a child of the site content
				sb.append( getPlotPlaceContent() );
			
				sb.append( getPlotObservationContent() );
				
				
				//end tags
				sb.append( getPlotObservationEndTag() );
				sb.append( getPlotSiteEndTag() );
				sb.append( getPlotProjectEndTag() );
				
			}
			sb.append( getPlotFooter() );
			
			out.println( sb.toString() );
			out.close();
			//System.out.println("datasrc: " + datasrc.state);
		
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage() );
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * method that returns the content for the named places recognized within a 
	 * plot
	 */
	 private String getPlotPlaceContent()
	 {
	 	StringBuffer sb = new StringBuffer();
			try 
			 {
				//get each of the unique place names associated with the plot
			 for (int i=0; i< datasrc.namedPlaces.size(); i++)
			 {
				 String name = datasrc.namedPlaces.elementAt(i).toString();
			 		sb.append(" \n"
					+"<namedPlace> \n"
					+"	<placeName>"+name+"</placeName> \n"
					+"	<placeDescription>"+datasrc.getPlaceDescription(name)+"</placeDescription> \n"
					+"	<placeCode>"+datasrc.getPlaceCode(name)+"</placeCode> \n"
					+"	<placeSystem>"+datasrc.getPlaceSystem(name)+"</placeSystem> \n"
					+"	<owner>"+datasrc.getPlaceOwner(name)+"</owner> \n"
					+"</namedPlace> \n"
					+"\n"
				);
				//System.out.println();
			 }
			 }
			 catch (Exception e)
			 {
				 System.out.println("Exception: " + e.getMessage() );
			 }
		 return( sb.toString() );
	 } 
	 
	 /**
	  * method that returns a string that contains the community classification
		* content for a specific plot
		*/
		private String getVegCommunityContent()
		{
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			sb.append("<communityClassification> \n");
			sb.append("	<className></className> \n");
			sb.append("	<classStartDate></classStartDate> \n");
			sb.append("	<classStopDate></classStopDate> \n");
			sb.append("	<classInspection></classInspection> \n");
			sb.append("	<classInspection></classInspection> \n");
			sb.append("	<tableAnalysis></tableAnalysis> \n");
			sb.append("	<multivariateAnalysis></multivariateAnalysis> \n");
			sb.append("	<expertSystem></expertSystem> \n");
			sb.append("	<classNotes></classNotes> \n");
			sb.append("	<classPublication></classPublication> \n");
			return( sb.toString() );
		}
	 
	 /**
	  * end tag for the community classification stuff
		*/
		private String getVegCommunityContentEndTag()
		{
			return("</communityClassification> \n \n");
		}
	
	
	
	/**
	 * Method to write a plot xml header which defihe the root node 
	 * and dtd
	 */
	private String getPlotHeader()
	{
		StringBuffer output = new StringBuffer(); 
		output.append("<?xml version=\"1.0\"?> \n"
		//removed the doc type for testing
		//+"<!DOCTYPE vegPlotPackage SYSTEM \"plot-standard1.dtd\"> \n"
		+"<vegPlotPackage> \n");
		return( output.toString() );
	}
	
	/**
	 * method that returns xml content for the plot project data
	 *
	 */
	 private String getPlotProjectContent()
	 {
		 StringBuffer sb = new StringBuffer();
		 try 
		 {
		 	sb.append(" \n"
				+"<project> \n"
				+"	<projectName>"+datasrc.projectName+"</projectName> \n"
				+"	<projectDescription>"+datasrc.projectDescription+"</projectDescription> \n"
				+"	<startDate>"+datasrc.projectStartDate+"</startDate> \n"
				+"	<stopDate>"+datasrc.projectStopDate+"</stopDate> \n"
				//get the project contributor information
				+ this.getProjectContributorContent() 
				//+"</project> \n"
				+"\n"
			);
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
		 }
		 return( sb.toString() );
	 }
	
	
	//method that returns the xml end-tag for the project element
	private String getPlotProjectEndTag()
	{
		return(" </project> \n");
	}
	
	
	/**
	 * method that returns the project contributor information
	 */
	 private String getProjectContributorContent()
	 {
		  StringBuffer sb = new StringBuffer();
		 try 
		 {
			 //there might be multiple contributors so get the the vector containing 
			 //all the contributors and handle each explicitly
			 	for (int i=0; i< datasrc.projectContributors.size(); i++)
			 	{
					String contributorName=datasrc.projectContributors.elementAt(i).toString();
		 			sb.append(" \n"
					+"<projectContributor> \n"
					+"	<wholeName>"+contributorName+"</wholeName> \n"
					+"	<salutation>"+datasrc.getProjectContributorSalutation(contributorName)+"</salutation> \n"
					+"	<givenName>"+datasrc.getProjectContributorGivenName(contributorName)+"</givenName> \n"
					+"	<middleName>"+datasrc.getProjectContributorMiddleName(contributorName)+"</middleName> \n"
					+"	<surName>"+datasrc.getProjectContributorSurName(contributorName)+"</surName> \n"
					+"	<organizationName>"+datasrc.getProjectContributorOrganizationName(contributorName)+"</organizationName> \n"
					+"	<contactInstructions>"+datasrc.getProjectContributorContactInstructions(contributorName)+"</contactInstructions> \n"
					+"	<telephone> \n"
					+"		<phoneNumber>"+datasrc.getProjectContributorPhoneNumber(contributorName)+"</phoneNumber> \n"
					+"		<phoneType></phoneType> \n"
					+"	</telephone> \n"
					+"	<address> \n"
					+"		<orgPosition>"+datasrc.getProjectContributorOrgPosition(contributorName)+"</orgPosition> \n"
					+"		<email>"+datasrc.getProjectContributorEmailAddress(contributorName)+"</email> \n"
					+"		<deliveryPoint>"+datasrc.getProjectContributorDeliveryPoint(contributorName)+"</deliveryPoint> \n"
					+"		<city>"+datasrc.getProjectContributorCity(contributorName)+"</city> \n"
					+"		<administrativeArea>"+datasrc.getProjectContributorAdministrativeArea(contributorName)+" </administrativeArea> \n"
					+"		<postalCode>"+datasrc.getProjectContributorPostalCode(contributorName)+"</postalCode> \n"
					+"		<country>"+datasrc.getProjectContributorCountry(contributorName)+"</country> \n"
					+"		<currentFlag>"+datasrc.getProjectContributorCurrentFlag(contributorName)+"</currentFlag> \n"
					+"		<addressStartDate>"+datasrc.getProjectContributorAddressStartDate(contributorName)+"</addressStartDate> \n"
					+"	</address> \n"
					+"</projectContributor>"
					+"\n"
					);
				}
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
		 }
		 return( sb.toString() );
	 }
	
	
	/** 
	 * method that is very similar to the 'getProjectContributorContent()' method
	 * returning the content for an observation contributor these methods could
	 * probably be combined in the future but for now I am keeping them seperate
	 */
	 private String getObservationContributorContent()
	 {
		  StringBuffer sb = new StringBuffer();
		 try 
		 {
			 //there might be multiple contributors so get the the vector containing 
			 //all the contributors and handle each explicitly
			 	for (int i=0; i< datasrc.projectContributors.size(); i++)
			 	{
					String contributorName=datasrc.projectContributors.elementAt(i).toString();
		 			sb.append(" \n"
					+"<observationContributor> \n"
					+"	<wholeName>"+contributorName+"</wholeName> \n"
					+"	<salutation>"+datasrc.getProjectContributorSalutation(contributorName)+"</salutation> \n"
					+"	<givenName>"+datasrc.getProjectContributorGivenName(contributorName)+"</givenName> \n"
					+"	<middleName>"+datasrc.getProjectContributorMiddleName(contributorName)+"</middleName> \n"
					+"	<surName>"+datasrc.getProjectContributorSurName(contributorName)+"</surName> \n"
					+"	<organizationName>"+datasrc.getProjectContributorOrganizationName(contributorName)+"</organizationName> \n"
					+"	<contactInstructions>"+datasrc.getProjectContributorContactInstructions(contributorName)+"</contactInstructions> \n"
					+"	<telephone> \n"
					+"		<phoneNumber>"+datasrc.getProjectContributorPhoneNumber(contributorName)+"</phoneNumber> \n"
					+"		<phoneType></phoneType> \n"
					+"	</telephone> \n"
					+"	<address> \n"
					+"		<orgPosition>"+datasrc.getProjectContributorOrgPosition(contributorName)+"</orgPosition> \n"
					+"		<email>"+datasrc.getProjectContributorEmailAddress(contributorName)+"</email> \n"
					+"		<deliveryPoint>"+datasrc.getProjectContributorDeliveryPoint(contributorName)+"</deliveryPoint> \n"
					+"		<city>"+datasrc.getProjectContributorCity(contributorName)+"</city> \n"
					+"		<administrativeArea>"+datasrc.getProjectContributorAdministrativeArea(contributorName)+" </administrativeArea> \n"
					+"		<postalCode>"+datasrc.getProjectContributorPostalCode(contributorName)+"</postalCode> \n"
					+"		<country>"+datasrc.getProjectContributorCountry(contributorName)+"</country> \n"
					+"		<currentFlag>"+datasrc.getProjectContributorCurrentFlag(contributorName)+"</currentFlag> \n"
					+"		<addressStartDate>"+datasrc.getProjectContributorAddressStartDate(contributorName)+"</addressStartDate> \n"
					+"	</address> \n"
					+"</observationContributor>"
					+"\n"
					);
				}
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
		 }
		 return( sb.toString() );
	 }
	
	
	/**
	 * method that retuns xml content for the plot site data
	 */
	 private String getPlotSiteContent( )
	 {
		  StringBuffer sb = new StringBuffer();
			try
			{
				sb.append(" \n");
				sb.append(" <plot> \n");
				sb.append("		<plotId>"+datasrc.plotId+"</plotId> \n");
				sb.append("		<plotAccessionNumber></plotAccessionNumber> \n");
				sb.append("		<authorPlotCode>"+datasrc.plotCode+"</authorPlotCode> \n");
				//citationId
				//parentId
				//sampleMethodId
				sb.append("		<latitude>"+datasrc.latitude+"</latitude> \n");
				sb.append("		<longitude>"+datasrc.longitude+"</longitude> \n");
				sb.append("		<authorE>"+datasrc.xCoord+"</authorE> \n");
				sb.append("		<authorN>"+datasrc.yCoord+"</authorN> \n");
				sb.append("		<authorZone>"+datasrc.utmZone+"</authorZone> \n");
				sb.append("		<authorDatum></authorDatum> \n");
				sb.append("		<authorLocation></authorLocation> \n");
				
				sb.append("		<locationNarrative></locationNarrative> \n");
				sb.append("		<confidentialityStatus>"+datasrc.confidentialityStatus+"</confidentialityStatus> \n");
				sb.append("		<confidentialityReason>"+datasrc.confidentialityReason+"</confidentialityReason> \n");
				sb.append("		<azimuth></azimuth> \n");
				sb.append("		<dsgPoly></dsgPoly> \n");
				sb.append("		<shape>"+datasrc.plotShape+"</shape> \n");
				sb.append("		<area>"+datasrc.plotArea+"</area> \n");
				sb.append("		<standSize></standSize> \n");
				sb.append("		<placementMethod></placementMethod> \n");
				sb.append("		<permanence></permanence> \n");
				sb.append("		<layoutNarative></layoutNarative> \n");
				sb.append("		<elevation></elevation> \n");
				sb.append("		<elevationAccuracy></elevationAccuracy> \n");
				sb.append("		<slopeAspect>"+datasrc.slopeAspect+" </slopeAspect> \n");
				sb.append("		<slopeGradient>"+datasrc.slopeGradient+"</slopeGradient> \n");
				sb.append("		<topoPosition>"+datasrc.topoPosition+"</topoPosition> \n");
				sb.append("		<landform></landform> \n");
				sb.append("		<geology>"+datasrc.surfGeo+"</geology> \n");
				sb.append("		<soilTaxon></soilTaxon> \n");
				sb.append("		<soilTaxonSource></soilTaxonSource> \n");
				sb.append("		<notesPublic></notesPublic> \n");
				sb.append("		<notesMgt></notesMgt> \n");
				sb.append("		<revisions></revisions> \n");
				
				sb.append("		<state>"+datasrc.state+"</state> \n");
				sb.append("		<country>"+datasrc.country+"</country> \n");
				sb.append(" \n");
				
			}
			catch (Exception e)
			{
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}	
			return( sb.toString() );
	 }
	 
	 //method that returns the xml end-tag for the project element
	private String getPlotSiteEndTag()
	{
		return(" </plot> \n");
	}
	 
	 
	 /**
	  * method that returns xml content as a string for the plot 
		* observational data which includes those data that are considered
		* to be transitional wrt time 
		*/
		private String getPlotObservationContent( )
	  {
		 StringBuffer sb = new StringBuffer();
		 try 
		 {
		 	sb.append("	<observation> \n");
		 	
			sb.append("	<authorObsCode></authorObsCode> \n");
  		sb.append("	<obsStartDate></obsStartDate> \n");
   		sb.append("	<obsEndDate>	</obsEndDate> \n");
   		sb.append("	<dateAccuracy> </dateAccuracy> \n");
   		sb.append("	<sampleMethodId> </sampleMethodId>  \n");
    	sb.append("	<coverMethodId></coverMethodId> \n");
   		sb.append("	<stratumMethodId> </stratumMethodId> \n");
    	sb.append("	<stemSizeLimit> </stemSizeLimit> \n");
    	sb.append("	<methodNarrative> </methodNarrative> \n");
    	sb.append("	<taxonObservationArea> </taxonObservationArea> \n");
    	sb.append("	<coverDispersion></coverDispersion> \n");
    	sb.append("	<autotaxonCover></autotaxonCover> \n");
    	sb.append("	<stemObservationArea></stemObservationArea> \n");
    	sb.append("	<stemSampleMethod></stemSampleMethod> \n");
    	sb.append("	<originalData></originalData> \n");
    	sb.append("	<effortLevel></effortLevel> \n");
    	sb.append("	<plotValidationLevel></plotValidationLevel> \n");
    	sb.append("	<floristicQuality></floristicQuality> \n");
    	sb.append("	<bryophyteQuality></bryophyteQuality> \n");
    	sb.append("	<lichenQuality></lichenQuality> \n");
    	sb.append("	<observationNarrative></observationNarrative> \n");
    	sb.append("	<landscapeNarrative> </landscapeNarrative> \n");
    	sb.append("	<homogeneity> </homogeneity> \n");
    	sb.append("	<phenologicalAspect> </phenologicalAspect> \n");
    	sb.append("	<representativeness> </representativeness> \n");
    	sb.append("	<basalArea> </basalArea> \n");
			sb.append("		<hydrologicRegime>"+datasrc.hydrologicRegime+"</hydrologicRegime> \n");
    	sb.append("	<soilMoistureRegime> </soilMoistureRegime> \n");
    	sb.append("	<soilDrainage> </soilDrainage> \n");
    	sb.append("	<waterSalinity> </waterSalinity> \n");
    	sb.append("	<waterDepth> 	</waterDepth> \n");
    	sb.append("	<shoreDistance> </shoreDistance> \n");
			sb.append("		<soilDepth>"+datasrc.soilDepth+"</soilDepth> \n");
    	sb.append("	<organicDepth> </organicDepth> \n");
    	sb.append("	<percentBedrock> </percentBedrock>  \n");
    	sb.append("	<percentRockGravel> </percentRockGravel>  \n");
    	sb.append("	<percentWood></percentWood> \n");
    	sb.append("	<percentLitter> </percentLitter> \n");
    	sb.append("	<percentBareSoil> </percentBareSoil> \n");
    	sb.append("	<percentWater></percentWater> \n");
    	sb.append("	<percentOther></percentOther> \n");
    	sb.append("	<nameOther></nameOther> \n");
    	sb.append("	<standMaturity> </standMaturity> \n"); 
    	sb.append("	<successionalStatus> </successionalStatus> \n"); 
    	sb.append("	<treeHt></treeHt> \n");
    	sb.append("	<shrubHt> </shrubHt> \n");
    	sb.append("	<fieldHt>	</fieldHt> \n");
    	sb.append("	<nonvascularHt></nonvascularHt> \n");
    	sb.append("	<submergedHt></submergedHt> \n");
    	sb.append("	<treeCover></treeCover> \n");
    	sb.append("	<shrubCover></shrubCover> \n");
    	sb.append("	<fieldCover> </fieldCover> \n");
    	sb.append("	<nonvascularCover> 	</nonvascularCover> \n");
    	sb.append("	<floatingCover></floatingCover> \n");
    	sb.append("	<submergedCover> 	</submergedCover> \n");
    	sb.append("	<dominantStratum> </dominantStratum> \n");
    	sb.append("	<growthform1type> </growthform1type> \n");
			sb.append("	<growthform2type> </growthform2type>  \n");
    	sb.append("	<growthform3type> </growthform3type> \n");
    	sb.append("	<growthform1cover> 	</growthform1cover>  \n");
    	sb.append("	<growthform2cover> </growthform2cover> \n");
   		sb.append("	<growthform3cover> </growthform3cover>  \n");
    	sb.append("	<notesPublic> </notesPublic> \n");
    	sb.append("	<notesMgt> 	</notesMgt>  \n");
			sb.append("	<revisions> </revisions>  \n");
			
			//add the skeleton for the community classification stuff
			sb.append( getVegCommunityContent() );
			sb.append( getVegCommunityContentEndTag() );
			
			//append the observation contributor information
			sb.append( getObservationContributorContent() );
			
			
			
			// get the categorical data related to the strata, including the 
			// cover, base, height etc.
			sb.append( getStrataContent() );
			
			//write the taxon observation data to the string buffer
			for (int i=0; i< datasrc.uniquePlantTaxaNumber; i++)
			{	
				String plantName = datasrc.plantTaxaNames.elementAt(i).toString();
				//sb.append( plantName );
				//Vector strataPresence = datasrc.getSpeciesStrataExistence();
				sb.append( getTaxonObservation(plantName, datasrc.plotCode ) );
				//System.out.println(">> " + datasrc.plotCode);
			}
			//sb.append("	</observation> \n");
		 
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
		 }
		 return( sb.toString() );
	  }
		
		//method that returns the xml end-tag for the observation element
		private String getPlotObservationEndTag()
		{
			return(" </observation> \n");
		}
	
	 
	 /**
	  * method that returns the strata height, and coverage
		* for each of the unique strata in a given plot
		*/
	 private String getStrataContent()
	 {
		 StringBuffer sb = new StringBuffer();
		 try 
		 {
			
			 //get the data for each of the unique strata
			 for (int i=0; i< datasrc.uniqueStrataNames.size() ; i++)
			 {
				  sb.append("<stratum> \n");
				//get the attributes
				String name = datasrc.uniqueStrataNames.elementAt(i).toString();
		 		String base = datasrc.getStrataBase(this.plotCode, name);
				String height = datasrc.getStrataHeight(this.plotCode, name);
				String cover = datasrc.getStrataCover(this.plotCode, name);
				
				//write the attributes
				sb.append("	<stratumName>"+name+"</stratumName> \n");
				sb.append("	<stratumHeight>"+height+"</stratumHeight> \n");
				sb.append("	<stratumBase>"+base+"</stratumBase> \n");
				sb.append("	<stratumCover>"+cover+"</stratumCover> \n");
			 	sb.append("</stratum> \n \n");
			 }
			 
		 }
		 catch (Exception e) 
		 {
			 System.out.println( "Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(sb.toString() );
	 }
	 
	 
	 /**
	  * method that returns xml content as a string for a taxon 
		* observation
		*/
		private String getTaxonObservation(String plantName, String plotCode)
		{
			StringBuffer sb = new StringBuffer();
			try
			{
				sb.append("<taxonObservation> \n" );
				sb.append("  <authorNameId>"+ plantName.replace('&', ' ') +"</authorNameId> \n" );
				
				//this is the cover of this taxon accross all strata
				sb.append("   <taxonCover>"+datasrc.getCummulativeStrataCover(plantName, plotCode)+"</taxonCover> \n");
				
				//the vector containing the strata names inwhich the plant exists in
				// this plot
				Vector strataExistence = datasrc.getTaxaStrataExistence(plantName, plotCode);
				for (int i=0; i< strataExistence.size(); i++)
				{	
					String strataName = strataExistence.elementAt(i).toString();
					//get the strata composition -- the cover of an individual plant
					// within a given strata
					sb.append( getStrataCompositionContent(plotCode, plantName, strataName));
				}
				sb.append("</taxonObservation> \n \n" );
			}
			catch (Exception e )
			{
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return( sb.toString() );
		}
		
		/**
		 * method that returns the strata composition for a given plot, plant taxa
		 * and strata 
		 */
		 private String getStrataCompositionContent(String plotCode, String plantName, 
		 String stratumName)
		 {
			 StringBuffer sb = new StringBuffer();
			 sb.append("    <stratumComposition> \n");
			 //get the cover value for that plant in that strata
			 String cover = datasrc.getTaxaStrataCover(plantName, plotCode, stratumName);
			 sb.append("      <taxonStratumCover>"+cover+"</taxonStratumCover> \n");
			 sb.append("      <stratumName>"+stratumName+"</stratumName> \n");
			 sb.append("    </stratumComposition> \n");
			 return(sb.toString());
		 }
		 
		 
		/**
		 * method that returns an end tag for a plot observation
		 */
		 private String getObservationEndTag()
		 {
			 return("</observation>");
		 }
		
	 
	/**
	 * Method to write a plot xml footer
	 */
	private String getPlotFooter()
	{
		return("</vegPlotPackage> \n");
	}
	
	
	/**
	 * main method for testing --
 	 */
	public static void main(String[] args)
	{
		
		//allow the user to access a specific plugin and a specific plot
		if (args.length == 2) 
		{
			//assume that the two args are 1] plugin and 2] plotName
			String pluginClass = args[0];
			String plotName = args[1];
			System.out.println("PlotXmlWriterV2 > plugin: " + pluginClass +" \n " + " plot: " + plotName);
			
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(pluginClass);
			writer.writeSinglePlot(plotName);
		}
		
		//the default
		else
		{
			//use the vegbank plugin
			String plugin = "VegBankDataSourcePlugin";
			//test the multiple plot writer
			Vector plotIdVec = new Vector();
			plotIdVec.addElement("0");
			plotIdVec.addElement("1");
			plotIdVec.addElement("2");
			plotIdVec.addElement("3");
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(plugin);
			//writer.writeSinglePlot("test-plot");
			writer.writeMultiplePlot(plotIdVec, "test.xml");
		}
	}
	
}
