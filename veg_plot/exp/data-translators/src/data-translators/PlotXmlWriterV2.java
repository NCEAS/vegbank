import java.io.*;
import java.util.*;
import PlotDataSource;

/**
 * 
 * Purpose: To write plot data as xml documents that can then be loaded into
 *  the plots database 
 * Copyright: 2000 Regents of the University of California and the
 *  National Center for Ecological Analysis and Synthesis <br> <br>
 *  
 *  This class handles the conversion from vegbank and other <br>
 *  legacy data sources to the native vegbank XML format <br> <br>
 *     
 *  '$Author: farrell $' <br>
 *  '$Date: 2003-01-08 02:00:10 $' <br>
 *  '$Revision: 1.15 $' <br>
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
			System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
			System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
			System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
		}
	}
	
	/**
	 * method that writes an XML Identification document for a vector of 
	 * plots.  This identification is the most fundamental identification 
	 * attributes of a plot (ie., the name(s) of the plots, the location of the 
	 * plots, the origional community associated with the plot, and the 
	 * some information correponding to the party responsible for the plotCode
	 * this identification is to be displayed to the user who may then choose
	 * to either view a summary of the plot or download the plot 
	 *
	 * @param plotIdVec -- the vector containing the plot id's 
	 * @param idnetificationFile -- the file to which the plot should be written
	 */
	public void writeMultiplePlotIdentifcation(Vector plotNameVec, String fileName)
	 {
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			//String buffer to store the plot
			StringBuffer sb = new StringBuffer();

			sb.append( getPlotHeader() );			
			//get each of the plots in the vector
			for (int i=0; i< plotNameVec.size(); i++)
			{
				String plotName = plotNameVec.elementAt(i).toString();
				//initialize the instance variable, plotCode, with this plot name
				this.plotCode = plotName;
				datasrc.getPlot(plotName);
				// get the plotid which is the unique ID for a plot as used by the RDBMS
				String plotId = datasrc.getPlotId(plotCode);
				// get the accession number associtated with this 
				String accession = datasrc.getAccessionValue(plotId);
				sb.append(" <plot> \n");
				sb.append("		<plotId>"+plotId+"</plotId> \n");
				sb.append("		<plotAccessionNumber>"+accession+"</plotAccessionNumber> \n");
				sb.append("		<authorPlotCode>"+datasrc.plotCode+"</authorPlotCode> \n");
				sb.append("		<state>"+datasrc.state+"</state> \n");
				sb.append("		<latitude>"+datasrc.latitude+"</latitude> \n");
				sb.append("		<longitude>"+datasrc.longitude+"</longitude> \n");
				sb.append(" </plot> \n");
				
			}
			sb.append( getPlotFooter() );
			out.println( sb.toString() );
			out.close();
			//System.out.println("datasrc: " + datasrc.state);
		}
		catch (Exception e) 
		{
			System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
				 System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
			 }
		 return( sb.toString() );
	 } 
	 
	 /**
	  * method that returns a string that contains the community classification
		* content for a specific plot
		*/
		private String getVegCommunityContent()
		{
			String name = datasrc.getCommunityName(plotCode);
			String code = datasrc.getCommunityCode(plotCode);
			String classNotes = datasrc.getClassNotes(plotCode);
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			sb.append("<communityClassification> \n");
			sb.append("	<className>"+name+"</className> \n");
			sb.append("	<classCode>"+code+"</classCode> \n");
			sb.append("	<classStartDate></classStartDate> \n");
			sb.append("	<classStopDate></classStopDate> \n");
			sb.append("	<classInspection></classInspection> \n");
			sb.append("	<classInspection></classInspection> \n");
			sb.append("	<tableAnalysis></tableAnalysis> \n");
			sb.append("	<multivariateAnalysis></multivariateAnalysis> \n");
			sb.append("	<expertSystem></expertSystem> \n");
			sb.append("	<classNotes>" + classNotes + "</classNotes> \n");
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
				+"  <projectName>"+datasrc.projectName+"</projectName> \n"
				+"  <projectDescription>"+datasrc.projectDescription+"</projectDescription> \n"
				+"  <startDate>"+datasrc.projectStartDate+"</startDate> \n"
				+"  <stopDate>"+datasrc.projectStopDate+"</stopDate> \n"
				//get the project contributor information
				+ this.getProjectContributorContent() 
				//+"</project> \n"
				+"\n"
			);
		 }
		 catch (Exception e)
		 {
			 System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
					+"  <projectContributor> \n"
					+"    <wholeName>"+contributorName+"</wholeName> \n"
					+"    <salutation>"+datasrc.getProjectContributorSalutation(contributorName)+"</salutation> \n"
					+"    <givenName>"+datasrc.getProjectContributorGivenName(contributorName)+"</givenName> \n"
					+"    <middleName>"+datasrc.getProjectContributorMiddleName(contributorName)+"</middleName> \n"
					+"    <surName>"+datasrc.getProjectContributorSurName(contributorName)+"</surName> \n"
					+"    <organizationName>"+datasrc.getProjectContributorOrganizationName(contributorName)+"</organizationName> \n"
					+"    <contactInstructions>"+datasrc.getProjectContributorContactInstructions(contributorName)+"</contactInstructions> \n"
					+"    <telephone> \n"
					+"      <phoneNumber>"+datasrc.getProjectContributorPhoneNumber(contributorName)+"</phoneNumber> \n"
					+"      <phoneType></phoneType> \n"
					+"    </telephone> \n"
					+"    <address> \n"
					+"      <orgPosition>"+datasrc.getProjectContributorOrgPosition(contributorName)+"</orgPosition> \n"
					+"      <email>"+datasrc.getProjectContributorEmailAddress(contributorName)+"</email> \n"
					+"      <deliveryPoint>"+datasrc.getProjectContributorDeliveryPoint(contributorName)+"</deliveryPoint> \n"
					+"      <city>"+datasrc.getProjectContributorCity(contributorName)+"</city> \n"
					+"      <administrativeArea>"+datasrc.getProjectContributorAdministrativeArea(contributorName)+" </administrativeArea> \n"
					+"      <postalCode>"+datasrc.getProjectContributorPostalCode(contributorName)+"</postalCode> \n"
					+"      <country>"+datasrc.getProjectContributorCountry(contributorName)+"</country> \n"
					+"      <currentFlag>"+datasrc.getProjectContributorCurrentFlag(contributorName)+"</currentFlag> \n"
					+"      <addressStartDate>"+datasrc.getProjectContributorAddressStartDate(contributorName)+"</addressStartDate> \n"
					+"    </address> \n"
					+"  </projectContributor>"
					+"\n"
					);
				}
		 }
		 catch (Exception e)
		 {
			 System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
			 System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
				sb.append("  <plot> \n");
				// get the plotid which is the unique ID for a plot as used by the 
				// RDBMS
				String plotId = datasrc.getPlotId(plotCode);
				// get the accession number associtated with this 
				String accession = datasrc.getAccessionValue(plotId);
				
				sb.append("    <plotId>"+plotId+"</plotId> \n");
				sb.append("    <plotAccessionNumber>"+accession+"</plotAccessionNumber> \n");
				sb.append("    <authorPlotCode>"+datasrc.plotCode+"</authorPlotCode> \n");
				//citationId
				//parentId
				//sampleMethodId
				sb.append("    <latitude>"+datasrc.latitude+"</latitude> \n");
				sb.append("    <longitude>"+datasrc.longitude+"</longitude> \n");
				sb.append("    <authorE>"+datasrc.xCoord+"</authorE> \n");
				sb.append("    <authorN>"+datasrc.yCoord+"</authorN> \n");
				sb.append("    <authorZone>"+datasrc.utmZone+"</authorZone> \n");
				sb.append("    <authorDatum></authorDatum> \n");
				sb.append("    <authorLocation>"+datasrc.getAuthorLocation(plotCode)+"</authorLocation> \n");
				sb.append("    <locationNarrative>" + datasrc.locationNarrative +"</locationNarrative> \n");
				sb.append("    <layoutNarrative>" + datasrc.layoutNarrative +"</layoutNarrative> \n");				
				sb.append("    <confidentialityStatus>"+datasrc.confidentialityStatus+"</confidentialityStatus> \n");
				sb.append("    <confidentialityReason>"+datasrc.confidentialityReason+"</confidentialityReason> \n");
				sb.append("    <azimuth>" + datasrc.azimuth +"</azimuth> \n");
				sb.append("    <dsgPoly>" + datasrc.dsgPoly + "</dsgPoly> \n");
				sb.append("    <shape>"+datasrc.plotShape+"</shape> \n");
				sb.append("    <area>"+datasrc.plotArea+"</area> \n");
				sb.append("    <standSize>"+datasrc.getStandSize(plotCode)+"</standSize> \n");
				sb.append("    <placementMethod></placementMethod> \n");
				sb.append("    <permanence>"+datasrc.isPlotPermanent(plotCode)+"</permanence> \n");
				sb.append("    <layoutNarative>"+datasrc.getLayoutNarrative(plotCode)+"</layoutNarative> \n");
				sb.append("    <elevation>"+datasrc.getElevation(plotCode)+"</elevation> \n");
				sb.append("    <elevationAccuracy>"+datasrc.getElevationAccuracy(plotCode)+"</elevationAccuracy> \n");
				sb.append("    <slopeAspect>"+datasrc.slopeAspect.trim()+" </slopeAspect> \n");
				sb.append("    <slopeGradient>"+datasrc.slopeGradient.trim()+"</slopeGradient> \n");
				sb.append("    <topoPosition>"+datasrc.topoPosition+"</topoPosition> \n");
				sb.append("    <landform>"+datasrc.getLandForm(plotCode)+"</landform> \n");
				sb.append("    <geology>"+datasrc.surfGeo+"</geology> \n");
				sb.append("    <soilTaxon>"+datasrc.getSoilTaxon(plotCode)+"</soilTaxon> \n");
				// THIS ELEMENT HAS BEEN REMOVED FROM THE DATABASE 20020717
				sb.append("    <soilTaxonSource>"+datasrc.getSoilTaxonSource(plotCode)+"</soilTaxonSource> \n");
				sb.append("    <notesPublic>"+datasrc.getNotesPublic(plotCode)+"</notesPublic> \n");
				sb.append("    <notesMgt>"+datasrc.getNotesMgt(plotCode)+"</notesMgt> \n");
				sb.append("    <revisions>"+datasrc.getRevisions(plotCode)+"</revisions> \n");
				sb.append("    <state>"+datasrc.state+"</state> \n");
				sb.append("    <country>"+datasrc.country+"</country> \n");
				sb.append(" \n");
				
			}
			catch (Exception e)
			{
				System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
		 	sb.append("    <observation> \n");
		 	
			sb.append("      <authorObsCode>"+datasrc.getAuthorObsCode(plotCode)+"</authorObsCode> \n");
  		sb.append("      <obsStartDate>"+datasrc.getObsStartDate(plotCode)+"</obsStartDate> \n");
   		sb.append("      <obsEndDate>"+datasrc.getObsStopDate(plotCode)+"</obsEndDate> \n");
   		sb.append("      <dateAccuracy>"+datasrc.getObsDateAccuracy(plotCode)+"</dateAccuracy> \n");
   		sb.append("      <observationSequence>"+datasrc.getObservationAccessionNumber(plotCode)+"</observationSequence> \n");
			//sb.append("      <sampleMethodId></sampleMethodId>  \n");
    	//sb.append("      <coverMethodId></coverMethodId> \n");
   		//sb.append("      <stratumMethodId></stratumMethodId> \n");
    	sb.append("      <stemSizeLimit>"+datasrc.getStemSizeLimit(plotCode)+"</stemSizeLimit> \n");
    	sb.append("      <methodNarrative>"+datasrc.getMethodNarrative(plotCode)+"</methodNarrative> \n");
    	sb.append("      <taxonObservationArea>"+datasrc.getTaxonObservationArea(plotCode)+"</taxonObservationArea> \n");
    	sb.append("      <coverDispersion>"+datasrc.getCoverDispersion(plotCode)+"</coverDispersion> \n");
    	sb.append("      <autotaxonCover>"+datasrc.getAutoTaxonCover(plotCode)+"</autotaxonCover> \n");
    	sb.append("      <stemObservationArea>"+datasrc.getStemObservationArea(plotCode)+"</stemObservationArea> \n");
    	sb.append("      <stemSampleMethod></stemSampleMethod> \n");
    	sb.append("      <originalData>"+datasrc.getOriginalData(plotCode)+"</originalData> \n");
    	sb.append("      <effortLevel>"+datasrc.getEffortLevel(plotCode)+"</effortLevel> \n");
    	sb.append("      <plotValidationLevel>"+datasrc.getPlotValidationLevel(plotCode)+"</plotValidationLevel> \n");
    	sb.append("      <floristicQuality>"+datasrc.getFloristicQuality(plotCode)+"</floristicQuality> \n");
    	sb.append("      <bryophyteQuality>"+datasrc.getBryophyteQuality(plotCode)+"</bryophyteQuality> \n");
    	sb.append("      <lichenQuality>"+datasrc.getLichenQuality(plotCode)+"</lichenQuality> \n");
    	sb.append("      <observationNarrative>"+datasrc.getObservationNarrative(plotCode)+"</observationNarrative> \n");
    	sb.append("      <landscapeNarrative>"+datasrc.getLandscapeNarrative(plotCode)+"</landscapeNarrative> \n");
    	sb.append("      <homogeneity>"+datasrc.getHomogeneity(plotCode)+"</homogeneity> \n");
    	sb.append("      <phenologicalAspect>"+datasrc.getPhenologicalAspect(plotCode)+"</phenologicalAspect> \n");
    	sb.append("      <representativeness>"+datasrc.getRepresentativeness(plotCode)+"</representativeness> \n");
    	sb.append("      <basalArea>"+datasrc.getBasalArea(plotCode)+"</basalArea> \n");
			sb.append("      <hydrologicRegime>"+datasrc.hydrologicRegime+"</hydrologicRegime> \n");
    	sb.append("      <soilMoistureRegime>"+datasrc.getSoilMoistureRegime(plotCode)+"</soilMoistureRegime> \n");
    	sb.append("      <soilDrainage>"+datasrc.getSoilDrainage(plotCode)+"</soilDrainage> \n");
    	sb.append("      <waterSalinity>"+datasrc.getWaterSalinity(plotCode)+"</waterSalinity> \n");
    	sb.append("      <waterDepth>"+datasrc.getWaterDepth(plotCode)+"</waterDepth> \n");
    	sb.append("      <shoreDistance>"+datasrc.getShoreDistance(plotCode)+"</shoreDistance> \n");
			sb.append("      <soilDepth>"+datasrc.soilDepth+"</soilDepth> \n");
    	sb.append("      <organicDepth>"+datasrc.getOrganicDepth(plotCode)+"</organicDepth> \n");
    	sb.append("      <percentBedrock>"+datasrc.getPercentBedRock(plotCode)+"</percentBedrock>  \n");
    	sb.append("      <percentRockGravel>"+datasrc.getPercentRockGravel(plotCode)+"</percentRockGravel>  \n");
    	sb.append("      <percentWood>"+datasrc.getPercentWood(plotCode)+"</percentWood> \n");
    	sb.append("      <percentLitter>"+datasrc.getPercentLitter(plotCode)+"</percentLitter> \n");
    	sb.append("      <percentBareSoil>"+datasrc.getPercentBareSoil(plotCode)+"</percentBareSoil> \n");
    	sb.append("      <percentWater>"+datasrc.getPercentWater(plotCode)+"</percentWater> \n");
    	sb.append("      <percentOther>"+datasrc.getPercentOther(plotCode)+"</percentOther> \n");
    	sb.append("      <nameOther>"+datasrc.getNameOther(plotCode)+"</nameOther> \n");
    	sb.append("      <standMaturity>"+datasrc.getStandMaturity(plotCode)+"</standMaturity> \n"); 
    	sb.append("      <successionalStatus>"+datasrc.getSuccessionalStatus(plotCode)+"</successionalStatus> \n"); 
    	sb.append("      <treeHt>"+datasrc.getTreeHt(plotCode)+"</treeHt> \n");
    	sb.append("      <shrubHt>"+datasrc.getShrubHt(plotCode)+"</shrubHt> \n");
    	sb.append("      <fieldHt>" + datasrc.getFieldHt(plotCode) + "</fieldHt> \n");
    	sb.append("      <nonvascularHt>" + datasrc.getNonvascularHt(plotCode) + "</nonvascularHt> \n");
    	sb.append("      <submergedHt>" + datasrc.getSubmergedHt(plotCode) + "</submergedHt> \n");
    	sb.append("      <treeCover>" + datasrc.getTreeCover(plotCode) + "</treeCover> \n");
    	sb.append("      <shrubCover>" + datasrc.getShrubCover(plotCode) + "</shrubCover> \n");
    	sb.append("      <fieldCover>" + datasrc.getFieldCover(plotCode)+ "</fieldCover> \n");
    	sb.append("      <nonvascularCover>" + datasrc.getNonvascularCover(plotCode) + "</nonvascularCover> \n");
    	sb.append("      <floatingCover>"+datasrc.getFloatingCover(plotCode)+"</floatingCover> \n");
    	sb.append("      <submergedCover>"+datasrc.getSubmergedCover(plotCode)+"</submergedCover> \n");
    	sb.append("      <dominantStratum>"+datasrc.getDominantStratum(plotCode)+"</dominantStratum> \n");
    	sb.append("      <growthform1type>"+datasrc.getGrowthform1Type(plotCode)+"</growthform1type> \n");
			sb.append("      <growthform2type>"+datasrc.getGrowthform2Type(plotCode)+"</growthform2type>  \n");
    	sb.append("      <growthform3type>"+datasrc.getGrowthform3Type(plotCode)+"</growthform3type> \n");
    	sb.append("      <growthform1cover>"+datasrc.getGrowthform1Cover(plotCode)+"</growthform1cover>  \n");
    	sb.append("      <growthform2cover>"+datasrc.getGrowthform2Cover(plotCode)+"</growthform2cover> \n");
   		sb.append("      <growthform3cover>"+datasrc.getGrowthform3Cover(plotCode)+"</growthform3cover>  \n");
    	sb.append("      <notesPublic>"+datasrc.getNotesPublic(plotCode)+"</notesPublic> \n");
    	sb.append("      <notesMgt>"+datasrc.getNotesMgt(plotCode)+"</notesMgt>  \n");
			sb.append("      <revisions>"+datasrc.getRevisions(plotCode)+"</revisions>  \n");
			
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
				sb.append( getTaxonObservation(plantName, plotCode ) );
				//System.out.println(">> " + datasrc.plotCode);
			}
			//sb.append("	</observation> \n");
		 
		 }
		 catch (Exception e)
		 {
			 System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
			 System.out.println( "PlotXmlWriterV2 > Exception: " + e.getMessage() );
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
				// GET THE ASSOCIATED PLANT CODE
				String code = datasrc.getPlantTaxonCode(plantName);
				sb.append("  <authorCodeId>"+ code +"</authorCodeId> \n" );
				
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
				System.out.println("PlotXmlWriterV2 > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return( sb.toString() );
		}
		
		/**
		 * method that returns the strata composition for a given plot, plant taxa
		 * and strata
		 * @param plotCode -- the plotCode, which is the PK value for a 'PLOT'
		 * table in vegbank
		 * @param plantName -- the name of the plant for which the coverage is 
		 * desired
		 * @param stratumName -- the name of the corresponding stratum
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
		//	plotIdVec.addElement("0");
		//	plotIdVec.addElement("1");
			plotIdVec.addElement("382");
		//	plotIdVec.addElement("3");
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(plugin);
			//writer.writeSinglePlot("test-plot");
			writer.writeMultiplePlot(plotIdVec, "test.xml");
			writer.writeMultiplePlotIdentifcation(plotIdVec, "plot-identification.xml");
		}
	}
	
}
