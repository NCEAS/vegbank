package org.vegbank.plots.datasource;
import java.util.*;

/**
 * this class represents an 'external' data source -- a data source which is not
 * the vegbank databases or the vegbank native xml type document at the
 * inception of this class exteranl sources include: tnc plots, turbo veg plots
 * and the structured data set.  In the future this class will also contain
 * local 'vegbank' data sources.  This class facilitates the access to vegbank-
 * alternative data sources via both public variables and methods
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: farrell $'
 *  '$Date: 2003-03-20 20:50:14 $'
 *  '$Revision: 1.1 $'
 */
public class PlotDataSource 
{
	private Object pluginObj = null;
	
	//BELOW ARE THE PLOT VARIABLES WHICH CAN BE ACCESSED BY THE CALLING CLASSES
	public String projectName = null;
	public String projectDescription = null;
	public String projectStartDate = null;
	public String projectStopDate = null;
	//This vector contains the  concatenated given name and sur name for a 
	//project contributor and each element is used to retrive the data associated
	//with that specific user explicitly through method calls 
	public Vector projectContributors = new Vector();
	public Vector observationContributors = new Vector();
	public String latitude = null;
	public String longitude = null;
	public Vector namedPlaces = new Vector();
	public String datum = null;
	public String state = null;
	public String communityName = null;
	public String plotShape = null;
	public String plotId = null;
	public String plotCode = null;
	public String plotArea = null;
	public String surfGeo = null;
	public String elevation = null;
	public String elevationAccuracy = null;
	public String landForm = null;
	public String authorLocation = null;
	public String standSize = null;
	public String topoPostion = null;
	public String country = null;
	public String utmZone = null;
	public String slopeAspect = null;
	public String slopeGradient = null;
	public String hydrologicRegime = null;
	public String xCoord = null;
	public String yCoord = null;
	public String topoPosition = null;
	public String soilDepth = null;
	public String confidentialityReason = null;
	public String confidentialityStatus = null;
	public Vector uniqueStrataNames = new Vector();
	public int uniquePlantTaxaNumber = 0;
	public Vector plantTaxaNames = new Vector();
	public String azimuth = null;
	public String dsgPoly = null;
	public String locationNarrative = null;
	public String layoutNarrative = null;
	public String stemSizeLimit  = null;
	public String landscapeNarative= null;
	public String phenologicalAspect= null;
  public String waterDepth= null;
  public String fieldHt= null;
  public String submergedHt= null;
  public String treeCover= null;
  public String shrubCover= null;
  public String fieldCover= null;
  public String nonvascularCover= null;
  
  
  
  //constructor method
	public PlotDataSource()
	{
		try
		{ 
		}
		catch(Exception e)
		{
			System.out.println("Error getting plugin: " + e.getMessage());
		}
	}
	
	
	/**
	 * constructor method that uses a plugin class as an 
	 * input to make a datasource available to class that
	 * call this class (e.g., plot loaders, or xml writers)
	 * @param pluginClass -- the name of the plugin to instantiate
	 */
	public PlotDataSource(String pluginClass)
	{
		try
		{ 
			// Hack to allow old code to work 
			String fullyQualifiedPluginClass = "org.vegbank.plots.datasource." + pluginClass;
			//create a generic object of the type specified 
			pluginObj = createObject(fullyQualifiedPluginClass);
		}
		catch(Exception e)
		{
			System.out.println("PlotDataSource > Error getting plugin: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
		 
	/**
	 * this method takes the primary plot identifier and returns the 
	 * name of the plot as assigned by the author.  In the case of the 
	 * vegbank databases, this will return the authorPlotCode for a given 
	 * 'plot_id' (the primary-key value for the plot table), whereas for 
	 * the TNC plots database, the the unique plotid is the name of the 
	 * plot
	 */
	 public String getPlotCode(String plotName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getPlotCode(plotName);
		 return(s);
	 }
	
	
	/** 
	 * method that returns the accession number associated with a plot id
	 * the input plot id is the unique identifier of the plot as used by 
	 * the RDBMS
	 * @param plotId -- the RDBMS unique plot ID
	 */
	public String getAccessionValue(String plotId)
	{
		String accessionValue = ((PlotDataSourceInterface)pluginObj).getAccessionValue(plotId);
		return(accessionValue);
	}
	
	/** 
	 * method that returns the accession number associated with a plot observation
	 * the input plot id is the unique identifier of the plot as used by 
	 * the RDBMS -- this will have to be modified so that if there are multiple 
	 * observation in a plot.
	 * @param plotId -- the RDBMS unique plot ID
	 */
	public String getObservationAccessionNumber(String plotId)
	{
		String accessionValue = ((PlotDataSourceInterface)pluginObj).getObservationAccessionNumber(plotId);
		return(accessionValue);
	}
	
	/**
 	 * returns the plotId for an input plot -- the plot id id the 
	 * unique value the RDBMS uses to identify the plot -- as opposed
	 * to the name that the author gives the plot or the application-assigned
	 * unique identifier
 	 */
 	public String getPlotId(String plotName)
 	{
		plotId = plotName;
		return(plotId);
 	}
	
	
	
	/**
 	 * returns the community code for the named plot
 	 */
 	public String getCommunityCode(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityCode(plotName);	
		return(s);
 	}
 	
	
	
 	/**
 	 * returns the community level of the framework for the named plot
 	 */
 	public String getCommunityLevel(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityLevel(plotName);	
		return(s);
 	}
 	
	public String getCommunityStartDate(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityStartDate(plotName);	
		return(s);
	}
	
	public String getCommunityStopDate(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityStopDate(plotName);	
		return(s);
	}
	
	public String getCommunityInspection(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityInspection(plotName);	
		return(s);
	}
	
	public String getCommunityTableAnalysis(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityTableAnalysis(plotName);	
		return(s);
	}
	
	public String getCommunityPublication(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityLevel(plotName);	
		return(s);
	}
	
	public String getCommunityMultiVariateAnalysis(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityMultiVariateAnalysis(plotName);	
		return(s);
	}
	
	public String getCommunityExpertSystem(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityExpertSystem(plotName);	
		return(s);
	}
 	
 	/**
 	 * returns the community framework for the named plot
 	 */
 	public String getCommunityFramework(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityFramework(plotName);	
		return(s);
 	}
	
		/**
 	 * returns the soil drainage of a plot
 	 * @param plotName -- the plot
 	 */
 	public String getSoilDrainage(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getSoilDrainage(plotName);	
		return(s);
 	}
 	
 	/**
 	 * returns the author's observation code
 	 * @param plotName -- the plot
 	 */
 	public String getAuthorObsCode(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getAuthorObsCode(plotName);	
		return(s);
 	}
 	
 	/**
 	 * returns the start date for the plot observation
 	 * @param plotName -- the plot
 	 */
 	public String getObsStartDate(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getObsStartDate(plotName);	
		return(s);
 	}
 	
 	/**
 	 * returns the stop date for the plot observation
 	 * @param plotName -- the plot
 	 */
 	public String getObsStopDate(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getObsStopDate(plotName);	
		return(s);
 	}
 	
 	/**
 	 * returns the soil depth of a plot
 	 * @param plotName -- the plot
 	 */
 	public String getSoilDepth(String plotName) 
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getSoilDepth(plotName);	
		return(s);
 	}

	
 	/**
	*/
	public String getObsDateAccuracy(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObsDateAccuracy(plotName);	
		return(s);
	}
	 
	/**
	*/
	public Hashtable getCoverMethod(String plotName)
	{
		Hashtable s = ((PlotDataSourceInterface)pluginObj).getCoverMethod(plotName);	
		return(s);
	}
	
	/**
	 */
	public Hashtable getStratumMethod(String plotName)
	{
		Hashtable s = ((PlotDataSourceInterface)pluginObj).getStratumMethod(plotName);	
		return(s);
	}
	
	/**
	 */
	public String getStemSizeLimit(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getStemSizeLimit(plotName);	
		return(s);
	}

	/**
	 */
	public String getMethodNarrative(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getMethodNarrative(plotName);	
		return(s);
	}
	
	/**
	 */
	public String getTaxonObservationArea(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getTaxonObservationArea(plotName);	
		return(s);
	}
	
	/**
	 */
	public String getCoverDispersion(String plotName )
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCoverDispersion(plotName);	
		return(s);
	}
	
	/**
	 */
	public boolean getAutoTaxonCover(String plotName)
	{
		boolean s = ((PlotDataSourceInterface)pluginObj).getAutoTaxonCover(plotName);	
		return(s);
	}
	
	/**
	 */
	public String getStemObservationArea(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getStemObservationArea(plotName);	
		return(s);
	}
	
	/**
	 */
	public String getStemSampleMethod(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getStemSampleMethod(plotName);	
		return(s);
	}

	/**
	*/
	public String getLandscapeNarrative(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLandscapeNarrative(plotName);	
		return(s);
	}
	/**
	*/
	public String getPhenologicalAspect(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPhenologicalAspect(plotName);	
		return(s);
	}
	/**
	*/
	public String getWaterDepth(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getWaterDepth(plotName);	
		return(s);
	}
	/**
	*/
	public String getFieldHt(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getFieldHt(plotName);	
		return(s);
	}
	/**
	*/
	public String getSubmergedHt(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSubmergedHt(plotName);	
		return(s);
	}
	/**
	*/
	public String getTreeCover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getTreeCover(plotName);	
		return(s);
	}
	/**
	*/
	public String getShrubCover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getShrubCover(plotName);	
		return(s);
	}
	/**
	*/
	public String getFieldCover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getFieldCover(plotName);	
		return(s);
	}
	/**
	*/
	public String getNonvascularCover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getNonvascularCover(plotName);	
		return(s);
  }
  
  /**
	 */
	public String getOriginalData(String plotName )
	{
		String s = ((PlotDataSourceInterface)pluginObj).getOriginalData(plotName);	
		return(s);
	}
	
	/**
	 */
	public String getEffortLevel( String plotName )
	{
		String s = ((PlotDataSourceInterface)pluginObj).getEffortLevel(plotName);	
		return(s);
	}
	
	
	/**
	 * method that returns the startDate of a project based on a plot
	 */
	 public String getProjectStartDate( String plotName )
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getProjectStartDate(plotName);	
		 return(s);
	 }
	 
	/**
	 * method that returns the ProjectDescription of a project based on a plot
	 */
	 public String getProjectDescription( String plotName )
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getProjectDescription(plotName);	
		 return(s);
	 }
	 
	/**
	 * method that returns the ProjectName of a project based on a plot
	 */
	 public String getProjectName( String plotName )
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getProjectName(plotName);	
		 return(s);
	 }	 	 
	 
	 /**
	 * method that returns the stopDate of a project based on a plot
	 */
	 public String getProjectStopDate( String plotName )
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getProjectStopDate(plotName);	
		 return(s);
	 }

	 //Observation Contributors
	 public Vector getObservationContributors(String plotName)
	{
		return new Vector();
	}
	
	public String getObservationContributorSalutation(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorSalutation(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorGivenName(String contributorWholeName) 
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorGivenName(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorMiddleName(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorMiddleName(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorSurName(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorSurName(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorOrganizationName(String contributorWholeName){
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorOrganizationName(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorContactInstructions(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorContactInstructions(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorPhoneNumber(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorPhoneNumber(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorCellPhoneNumber(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorCellPhoneNumber(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorFaxPhoneNumber(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorFaxPhoneNumber(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorOrgPosition(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorOrgPosition(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorEmailAddress(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorEmailAddress(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorDeliveryPoint(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorDeliveryPoint(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorCity(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorCity(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorAdministrativeArea(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorAdministrativeArea(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorPostalCode(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorPostalCode(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorCountry(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorCountry(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorCurrentFlag(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorCurrentFlag(contributorWholeName);	
		return(s);
	}
	
	public String getObservationContributorAddressStartDate(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationContributorAddressStartDate(contributorWholeName);	
		return(s);
	}
	
	//START
	public String getPlotValidationLevel(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPlotValidationLevel(plotName);	
		return(s);
	}
	
	public String  getFloristicQuality(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getFloristicQuality(plotName);	
			 return(s);
	}
	
	public String  getBryophyteQuality(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getBryophyteQuality(plotName);	
			 return(s);
	}
	
	public String  getLichenQuality(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLichenQuality(plotName);	
			 return(s);
	}
	
	public String  getObservationNarrative(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getObservationNarrative(plotName);	
			 return(s);
	}
	
	public String  getHomogeneity(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getHomogeneity(plotName);	
			 return(s);
	}
	
	public String  getRepresentativeness(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getRepresentativeness(plotName);	
			 return(s);
	}
	
	public String  getBasalArea(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getBasalArea(plotName);	
			 return(s);
	}
	
	public String  getSoilMoistureRegime(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSoilMoistureRegime(plotName);	
			 return(s);
	}
	
	public String  getWaterSalinity(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getWaterSalinity(plotName);	
			 return(s);
	}
	
	public String  getShoreDistance(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getShoreDistance(plotName);	
			 return(s);
	}
	
	public String  getOrganicDepth(String plotName)
	{
	 String s = ((PlotDataSourceInterface)pluginObj).getOrganicDepth(plotName);	
			 return(s);
	}
	
	public String  getPercentBedRock(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPercentBedRock(plotName);	
			 return(s);
	}
	
	public String  getPercentRockGravel(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPercentRockGravel(plotName);	
			 return(s);
	}
	
	public String  getPercentWood(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPercentWood(plotName);	
			 return(s);
	}
	public String  getPercentLitter(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPercentLitter(plotName);	
			 return(s);
	}
	
	public String  getPercentBareSoil(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPercentBareSoil(plotName);	
			 return(s);
	}
	
	public String  getPercentWater(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPercentWater(plotName);	
			 return(s);
	}
	
	public String  getPercentOther(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPercentOther(plotName);	
			 return(s);
	}
	
	public String  getNameOther(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getNameOther(plotName);	
			 return(s);
	}
	
	public String  getStandMaturity(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getStandMaturity(plotName);	
			 return(s);
	}
	
	
	public String  getSuccessionalStatus(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSuccessionalStatus(plotName);	
			 return(s);
	}
	
	public String  getTreeHt(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getTreeHt(plotName);	
			 return(s);
	}
	
	public String  getShrubHt(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getShrubHt(plotName);	
			 return(s);
	}
	
	public String  getNonvascularHt(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getNonvascularHt(plotName);	
			 return(s);
	}
	
	public String  getFloatingCover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getFloatingCover(plotName);	
			 return(s);
	}
	
	public String  getSubmergedCover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSubmergedCover(plotName);	
			 return(s);
	}
	
	public String  getDominantStratum(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getDominantStratum(plotName);	
			 return(s);
	}
	
	public String  getGrowthform1Type(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getGrowthform1Type(plotName);	
			 return(s);
	}
	
	public String  getGrowthform2Type(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getGrowthform2Type(plotName);	
			 return(s);
	}
	
	public String  getGrowthform3Type(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getGrowthform3Type(plotName);	
			 return(s);
	}
	
	public String  getGrowthform1Cover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getGrowthform1Cover(plotName);	
			 return(s);
	}
	
	public String  getGrowthform2Cover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getGrowthform2Cover(plotName);	
			 return(s);
	}
	
	public String  getGrowthform3Cover(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getGrowthform3Cover(plotName);	
			 return(s);
	}
	
	public boolean  getNotesPublic(String plotName)
	{
		boolean b = ((PlotDataSourceInterface)pluginObj).getNotesPublic(plotName);	
			 return(b);
	}
	
	public boolean  getNotesMgt(String plotName)
	{
		boolean b = ((PlotDataSourceInterface)pluginObj).getNotesMgt(plotName);	
			 return(b);
	}
	
	public boolean  getRevisions(String plotName)
	{
		boolean b = ((PlotDataSourceInterface)pluginObj).getRevisions(plotName);	
			 return(b);
	}
	//END


	/**
 	 * returns true if the plot is a permanent plot or false if not
 	 * @param plotName -- the plot
 	 */
 	 public boolean isPlotPermanent(String plotName)
 	 {
 	 	boolean b = ((PlotDataSourceInterface)pluginObj).isPlotPermanent(plotName);	
		return(b);
 	 }
  
  /**
 	* returns the soil taxon for the plot -- this is the USDA class
 	* heirarchy (eg., Order, Group, Family, Series etc..)
 	* @param plotName -- the plot
 	*/
 	public String getSoilTaxon(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getSoilTaxon(plotName);	
		return(s);
 	}
 	
 	/**
 	 * returns how the soil taxon was determined (eg., field observation
 	 * mapping, other ...)
 	 * @param plotName -- the plot
	 * @deprecated -- this element has been removed from the database -- 20020717
 	 */
 	public String getSoilTaxonSource(String plotName)
 	{
		String s = ((PlotDataSourceInterface)pluginObj).getSoilTaxonSource(plotName);	
		return(s);
 	}


	/**
	 * method that returns the names of the plots stored in this data 
	 * source 
	 */
	 public Vector getPlotNames()
	 {
			Vector v = new Vector();
			v =  ((PlotDataSourceInterface)pluginObj).getPlotNames( );
			return(v); 
	 }
	
	/**
	 * method that populates class vaiables with all the data representing a 
	 * plot using the plugin defined in the constructor method.  This is kinda
	 * a cheat because the public methods should actually be accessed to obtain 
	 * data b/c it is a more explicit direction.
	 *
	 * @param plotName -- the id of the plot desired by the caller
	 * @return result -- whether the method call was successful
	 */
	public boolean getPlot( String plotName )
	{
		try
		{
			System.out.println("PlotDataSource >  getting plot based on name: " + plotName );
			projectName = ((PlotDataSourceInterface)pluginObj).getProjectName(plotName);
			projectDescription = 
        ((PlotDataSourceInterface)pluginObj).getProjectDescription(plotName);
			projectContributors = 
        ((PlotDataSourceInterface)pluginObj).getProjectContributors(plotName);
			observationContributors = 
        ((PlotDataSourceInterface)pluginObj).getObservationContributors(plotName);
			projectStartDate = 
        ((PlotDataSourceInterface)pluginObj).getProjectStartDate(plotName);
			projectStopDate = ((PlotDataSourceInterface)pluginObj).getProjectStopDate(plotName);
			plotCode = ((PlotDataSourceInterface)pluginObj).getPlotCode(plotName);
			namedPlaces = ((PlotDataSourceInterface)pluginObj).getPlaceNames(plotName); 
			state = ((PlotDataSourceInterface)pluginObj).getState(plotName);
			plotShape =  ((PlotDataSourceInterface)pluginObj).getPlotShape(plotName);
			slopeGradient = ((PlotDataSourceInterface)pluginObj).getSlopeGradient(plotName);
			slopeAspect = ((PlotDataSourceInterface)pluginObj).getSlopeAspect(plotName);
			surfGeo = ((PlotDataSourceInterface)pluginObj).getSurfGeo(plotName);
			hydrologicRegime = ((PlotDataSourceInterface)pluginObj).getHydrologicRegime(plotName);
			topoPosition = ((PlotDataSourceInterface)pluginObj).getTopoPosition(plotName);
			plotArea = ((PlotDataSourceInterface)pluginObj).getPlotArea(plotName);
			country = ((PlotDataSourceInterface)pluginObj).getCountry(plotName);
			xCoord = ((PlotDataSourceInterface)pluginObj).getXCoord(plotName);
			yCoord = ((PlotDataSourceInterface)pluginObj).getYCoord(plotName);
			utmZone = ((PlotDataSourceInterface)pluginObj).getUTMZone(plotName);
			standSize = ((PlotDataSourceInterface)pluginObj).getStandSize(plotName);
			latitude = ((PlotDataSourceInterface)pluginObj).getLatitude(plotName);
			longitude = ((PlotDataSourceInterface)pluginObj).getLongitude(plotName);
			
			//NEXT LINES FOR DEBUGGING W/IN SERVLETS
/**
			System.out.println("PlotDataSource >   start: " + this.projectName);
			System.out.println("PlotDataSource >   desc: " + this.projectDescription);
	    System.out.println("PlotDataSource >   contr: " + this.projectContributors.toString() );
			System.out.println("PlotDataSource >   startdate: " + this.projectStartDate);
			System.out.println("PlotDataSource >   stopdate: " + this.projectStopDate);
			System.out.println("PlotDataSource >   plotcode: " + plotCode);
			System.out.println("PlotDataSource >   named: " + namedPlaces.toString() );
			System.out.println("PlotDataSource >   state: " + state);
			System.out.println("PlotDataSource >   shape: " + plotShape);
			System.out.println("PlotDataSource >   gradient: " + slopeGradient);
			System.out.println("PlotDataSource >   aspect: " + slopeAspect);
			System.out.println("PlotDataSource >   geology: " + surfGeo);
			System.out.println("PlotDataSource >   hydro: " + hydrologicRegime);
			System.out.println("PlotDataSource >   topop: " + topoPosition );
			System.out.println("PlotDataSource >   area: " + plotArea );
			System.out.println("PlotDataSource >   count: " + country );
			System.out.println("PlotDataSource >   xco: " + xCoord );
			System.out.println("PlotDataSource >   yco: " + yCoord );
			System.out.println("PlotDataSource >   utm: " + utmZone );
			System.out.println("PlotDataSource >   stan: " + standSize );
			System.out.println("PlotDataSource >   lat: " + latitude );
			System.out.println("PlotDataSource >   long: " + longitude );
*/			
			
			confidentialityReason = 
        ((PlotDataSourceInterface)pluginObj).getConfidentialityReason(plotName);
			confidentialityStatus = 
        ((PlotDataSourceInterface)pluginObj).getConfidentialityStatus(plotName);
			authorLocation = ((PlotDataSourceInterface)pluginObj).getAuthorLocation(plotName);
			landForm = ((PlotDataSourceInterface)pluginObj).getLandForm(plotName);
			elevation = ((PlotDataSourceInterface)pluginObj).getElevation(plotName);
			elevationAccuracy = 
        ((PlotDataSourceInterface)pluginObj).getElevationAccuracy(plotName);
			uniqueStrataNames = 
        ((PlotDataSourceInterface)pluginObj).getUniqueStrataNames(plotName);
			plantTaxaNames = ((PlotDataSourceInterface)pluginObj).getPlantTaxaNames(plotName);
			uniquePlantTaxaNumber =  plantTaxaNames.size();
      locationNarrative =  
        ((PlotDataSourceInterface)pluginObj).getLocationNarrative(plotName);
      layoutNarrative =
        ((PlotDataSourceInterface)pluginObj).getLayoutNarrative(plotName);
      azimuth =
        ((PlotDataSourceInterface)pluginObj).getAzimuth(plotName);
      dsgPoly =
        ((PlotDataSourceInterface)pluginObj).getDSGPoly(plotName);
      
		}
		catch (Exception e)
		{
			System.out.println("PlotDataSource > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(true);
	}
	
	//method that returns the unique strata recognized in a given plot
	public Vector getUniqueStrataNames(String plotName)
	{
		Vector v = new Vector();
		v  = ((PlotDataSourceInterface)pluginObj).getUniqueStrataNames(plotName);
		return(v);
	}

	/**
	 * method that will return a vector containg the unique plant names
	 * stored in a plot data source associated with a given plot
	 * 
	 * @param plot -- the identifier of the plot
	 * @return plantNameVector --the vector containg the names of the distinct 
	 *  plant names
	 */
	public Vector getPlantNames(String plotName)
	{
		Vector v = new Vector();	
		v  = ((PlotDataSourceInterface)pluginObj).getPlantTaxaNames(plotName);
		return(v);
	}

	//retuns the person's salutation based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSalutation(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorSalutation(contributorWholeName);	
		return(s);
	}
	//retuns the person's given based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorGivenName(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorGivenName(contributorWholeName);	
		return(s);
	}
	//retuns the person's middle name based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorMiddleName(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorMiddleName(contributorWholeName);	
		return(s);
	}
	//retuns the person's surName based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSurName(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorSurName(contributorWholeName);	
		return(s);
	}
	//retuns the name of an org. that a person is associated with based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorOrganizationName(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorOrganizationName(contributorWholeName);	
		return(s);
	}
	
	//retuns the person's contactinstructions based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorContactInstructions(contributorWholeName);	
		return(s);
	}
	//retuns the person's phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorPhoneNumber(contributorWholeName);	
		return(s);
	}
	//retuns the person's cellPhone based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCellPhoneNumber(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorCellPhoneNumber(contributorWholeName);	
		return(s);
	}
	//retuns the person's fax phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorFaxPhoneNumber(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorFaxPhoneNumber(contributorWholeName);	
		return(s);
	}
	//retuns the party's position within an organization based on their full 
	// name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorOrgPosition(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorOrgPosition(contributorWholeName);	
		return(s);
	}
	//retuns the person's email based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorEmailAddress(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorEmailAddress(contributorWholeName);	
		return(s);
	}
	//retuns the person's address line based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorDeliveryPoint(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorDeliveryPoint(contributorWholeName);	
		return(s);
	}
	//retuns the person's city based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCity(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorCity(contributorWholeName);	
		return(s);
	}
	
	public String getProjectContributorAdministrativeArea(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorAdministrativeArea(contributorWholeName);	
		return(s);
	}
	
	public String getProjectContributorPostalCode(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorPostalCode(contributorWholeName);	
		return(s);
	}
	
	//retuns the person's country based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCountry(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorCountry(contributorWholeName);	
		return(s);
	}
	//retuns a boolean 'true' if it is a party's current address based on their 
	//full name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCurrentFlag(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorCurrentFlag(contributorWholeName);	
		return(s);
	}
	//retuns the date that the address became current for a party based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorAddressStartDate(String contributorWholeName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getProjectContributorAddressStartDate(contributorWholeName);	
		return(s);
	}
	
	
	
	/**
	 * method that returns a vector containing the names of the 
	 * strata in which the input plant taxa name exists within the 
	 * input plot
	 */
	public Vector getTaxaStrataExistence(String plantName, String plotCode)
	{
		Vector v = new Vector();
		v =  ((PlotDataSourceInterface)pluginObj).getTaxaStrataExistence( plantName, plotCode );
		return(v);
	}
	
	/**
	 * method that returns the coverage of a specific class in a specific
	 * strata
	 */
	public String getTaxaStrataCover(String plantName, String plotCode, String stratumName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getTaxaStrataCover(
		plantName, plotCode, stratumName);
		//String s = "bad";	
		return(s);
	}
	/**
	 * method to return the taxon cover from a data source using as input 
	 * the scientific plant name -- or the plant name that comes from 
	 * the 'getPlantTaxaNames' method
	 *
	 * @param plantName -- the scientific plantName
	 */
	 public String getPlantTaxonCover(String plantName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getPlantTaxonCover(plantName);
		return(s);
	 }
	 
	/**
	 * method to return the taxa code from a data source using as input 
	 * the scientific plant name -- or the plant name that comes from 
	 * the 'getPlantTaxaNames' method
	 *
	 * @param plantName -- the scientific plantName
	 */
	 public String getPlantTaxonCode(String plantName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getPlantTaxonCode(plantName);
		return(s);
	 }
	 
	 
	
	 /**
   * creates an object of a type className.  this is used for instantiating
   * plugins.
   */
  private static Object createObject(String className) 
	throws InstantiationException, 
		IllegalAccessException,
		ClassNotFoundException
  	{
    	Object object = null;
   	 try 
    	{
    		System.out.println("PlotDataSource > Instantiating Class " + className);
        Class classDefinition = Class.forName(className);
        object = classDefinition.newInstance();
    	} 
    	catch (InstantiationException e) 
    	{
     	   throw new InstantiationException("PlotDataSource > Error instantiating plugin: " + e);
    	} 
    	catch (IllegalAccessException e) 
   	 {
   	     throw new IllegalAccessException("PlotDataSource > Error accessing plugin: " + e);
   	 } 
    	catch (ClassNotFoundException e) 
    	{
        throw new ClassNotFoundException("PlotDataSource > Plugin " + className + " not " +
                                         "found: " + e);
    	}
    	return object;
  	}
		
	
	/**
	 * retuns a description of a place based on a placeName
	 */
	 public String getPlaceDescription(String placeName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getPlaceDescription(placeName);	
			return(s);
	 }
	
	/**
	 * returns a placeCode based on a placeName
	 */
	 public String getPlaceCode(String placeName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getPlaceCode(placeName);	
			return(s);
	 }

	/**
	 * returns a place system based on a placeName
	 */
	 public String getPlaceSystem(String placeName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getPlaceSystem(placeName);	
			return(s);
	 }
	 
	 	/**
	 * returns a place owner based on a placeName
	 */
	 public String getPlaceOwner(String placeName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getPlaceOwner(placeName);	
			return(s);
	 }
	 
	 
	
	/**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String strataName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getStrataCover(
		  plotName, strataName);	
			return(s);
	 }
	 
	 /** 
	  * method the returns the base height of a strata based on the 
		* name of that starta and the plot for which that strata is included
		*/
		public String getStrataBase(String plotName, String strataName)
		{
			String s = ((PlotDataSourceInterface)pluginObj).getStrataBase(plotName, 
			strataName);
			return(s);
		}
		
		/**
		 * method that returns the upper height of a starata based on 
		 * the name of the strata and the plot inwhich the strata exists
		 */
		public String getStrataHeight(String plotName, String strataName)
		{
			String s = ((PlotDataSourceInterface)pluginObj).getStrataHeight(
		  plotName, strataName);	
			return(s);
		}
		
		
		/**
	 * method that retuns the cummulative cover accoss all strata for a given 
	 * plant taxa in a given plot
	 */
	public String getCummulativeStrataCover( String plantName, String plotName )
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCummulativeStrataCover(
		plantName, plotName);	
		return(s);
	}
	
	//returns the easting
	public String getXCoord(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getXCoord(plotName);
		return(s);
	}
	//returns the northing
	public String getYCoord(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getYCoord(plotName);
		return(s);
	}
	
	//returns the atitude
	public String getLatitude(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLatitude(plotName);
		System.out.println("PlotDataSource > latitude: " + s);
		return(s);
	}
	
	//returns the longitude
	public String getLongitude(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLongitude(plotName);
		return(s);
	}
	
	//returns the geographic zone
	public String getUTMZone(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getUTMZone(plotName);
		return(s);
	}
	//returns the geographic datum
	public String getDatumType(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getDatumType(plotName);
		return(s);
	}
	//returns the plot shape
	public String getPlotShape(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPlotShape(plotName);
		return(s);
	}
	
	//returns the plot area
	public String getPlotArea(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPlotArea(plotName);
		return(s);
	}
	
	//returns the community classification notes associated with a plot
	public String getClassNotes(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getClassNotes(plotName);
		return(s);
	}
  
  //returns the community name associated with a plot
	public String getCommunityName(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityName(plotName);
		return(s);
	}
	
	//returns the state in which the plot exists
	public String getState(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getState(plotName);
		return(s);
	}
	
	//retuns the hydrologic regime
	public String getHydrologicRegime(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getHydrologicRegime(plotName);
		return(s);
	}
	
	//returns the topo position
	public String getTopoPosition(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getTopoPosition(plotName);
		return(s);
	}
	
	//returns the slope aspect
	public String getSlopeAspect(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSlopeAspect(plotName);
		return(s);
	}
	
	//returns yje slope gradient
	public String getSlopeGradient(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSlopeGradient(plotName);
		return(s);
	}
	
	//returns the surficial geology
	public String getSurfGeo(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSurfGeo(plotName);
		return(s);
	}
	
	//retuns the country
	public String getCountry(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCountry(plotName);
		return(s);
	}
	
	//returns the size of the stand -- extensive etc..
	public String getStandSize(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getStandSize(plotName);
		return(s);
	}
	
	//returns the location as described by the author
	public String getAuthorLocation(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getAuthorLocation(plotName);
		return(s);
	}
	
	//returns the landForm
	public String getLandForm(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLandForm(plotName);
		return(s);
	}
	
	//retuns the elevation
	public String getElevation(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getElevation(plotName);
		return(s);
	}
	
	//returns the elevation accuracy
	public String getElevationAccuracy(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getElevationAccuracy(plotName);
		return(s);
	}
	
	//return the confidentiality reason -- not null
	public String	getConfidentialityReason(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getConfidentialityReason(plotName);
		return(s);
	}
	
	//return the confidentiality status -- not null 0-6
	public String getConfidentialityStatus(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getConfidentialityStatus(plotName);
		return(s);
	}

	public String getAzimuth(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getAzimuth(plotName);
		return(s);
	}
	
	public String getLocationNarrative(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLocationNarrative(plotName);
		return(s);
	}
	
	public String getLayoutNarrative(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLayoutNarrative(plotName);
		return(s);
	}
	
	public String getDSGPoly(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getDSGPoly(plotName);
		return(s);
	}

	
	/**
	 * method that prints the variables for a given plot to the 
	 * system.  This method is primarily to be used fro debugging
	 * the plugin system for accessing plot data sources.
	 *
	 * @param pluginClass -- the name of the pluging class (eg. NativeXmlPlugin)
	 * @param plotName -- the name of the plot
	 */
	private void printDBVariables(String pluginClass, String plotName)
	{
		System.out.println("PlotDataSource > getting info for: " + plotName );
		try
		{
			//rectify this variable -- use a consistent name
			plotCode = plotName;
			//define the plugin
			PlotDataSource source = new PlotDataSource(pluginClass);
			//get all the variables for the given plot
			source.getPlot(plotName);
			
			System.out.println(" \n ---------------------project info------------------------");
			System.out.println("PlotDataSource > project name: " + source.projectName );
			System.out.println("PlotDataSource > project description: " + source.projectDescription );
			System.out.println("PlotDataSource > project contributors: " + source.projectContributors.toString() );
			System.out.println("PlotDataSource > project start date: " + source.projectStartDate );
			System.out.println("PlotDataSource > project stop date: " + source.projectStopDate );
			//the project contributor info goes here
			System.out.println("PlotDataSource > number of project contributors: " + source.projectContributors.size() );
			for (int i=0; i<source.projectContributors.size(); i++)
			{
				String wholeName = source.projectContributors.elementAt(i).toString();
				String salutation = source.getProjectContributorSalutation(wholeName);
				String givenName = source.getProjectContributorGivenName(wholeName);
				String middleName = source.getProjectContributorMiddleName(wholeName);
				String surName = source.getProjectContributorSurName(wholeName);
				String organizationName = source.getProjectContributorOrganizationName(wholeName);
				String contactInstructions = source.getProjectContributorContactInstructions(wholeName);
				String phoneNumber = source.getProjectContributorPhoneNumber(wholeName);
				String orgPosition = source.getProjectContributorOrgPosition(wholeName);
				String email = source.getProjectContributorEmailAddress(wholeName);
				String deliveryPoint = source.getProjectContributorDeliveryPoint(wholeName);
				String city = source.getProjectContributorCity(wholeName);
				String administrativeArea = source.getProjectContributorAdministrativeArea(wholeName);
				String postalCode = source.getProjectContributorPostalCode(wholeName);
				String country = source.getProjectContributorCountry(wholeName);
				String currentFlag = source.getProjectContributorCurrentFlag(wholeName);
				String addressStartDate = source.getProjectContributorAddressStartDate(wholeName);
				
				System.out.println("PlotDataSource > project contributor whole name: "+ wholeName);
				System.out.println("PlotDataSource > project contributor salutation: "+ salutation);
				System.out.println("PlotDataSource > project contributor given name: "+ givenName );
				System.out.println("PlotDataSource > project contributor middle name: "+ middleName);
				System.out.println("PlotDataSource > project contributor sur name: "+ surName);
				System.out.println("PlotDataSource > project contributor org name: "+ organizationName);
				System.out.println("PlotDataSource > project contributor contactInstructions: "+ contactInstructions);
				System.out.println("PlotDataSource > project contributor phoneNumber: "+ phoneNumber);
				System.out.println("PlotDataSource > project contributor orgPosition: "+ orgPosition);
				System.out.println("PlotDataSource > project contributor email: "+ email);
				System.out.println("PlotDataSource > project contributor deliveryPoint: "+ deliveryPoint);
				System.out.println("PlotDataSource > project contributor city: "+ city);
				System.out.println("PlotDataSource > project contributor administrativeArea: "+ administrativeArea);
				System.out.println("PlotDataSource > project contributor postalCode: "+ postalCode);
				System.out.println("PlotDataSource > project contributor country: "+ country);
				System.out.println("PlotDataSource > project contributor currentFlag: "+ currentFlag);
				System.out.println("PlotDataSource > project contributor addressStartDate: "+ addressStartDate+"\n");
			}
			
			System.out.println(" \n ---------------------observation info------------------------");
			System.out.println("PlotDataSource > number of project contributors: " 
                          + source.observationContributors.size() );

			for (int i=0; i<source.observationContributors.size(); i++)
			{
				String wholeName = source.observationContributors.elementAt(i).toString();
				String salutation = source.getObservationContributorSalutation(wholeName);
				String givenName = source.getObservationContributorGivenName(wholeName);
				String middleName = source.getObservationContributorMiddleName(wholeName);
				String surName = source.getObservationContributorSurName(wholeName);
				String organizationName = source.getObservationContributorOrganizationName(wholeName);
				String contactInstructions = source.getObservationContributorContactInstructions(wholeName);
				String phoneNumber = source.getObservationContributorPhoneNumber(wholeName);
				String orgPosition = source.getObservationContributorOrgPosition(wholeName);
				String email = source.getObservationContributorEmailAddress(wholeName);
				String deliveryPoint = source.getObservationContributorDeliveryPoint(wholeName);
				String city = source.getObservationContributorCity(wholeName);
				String administrativeArea = source.getObservationContributorAdministrativeArea(wholeName);
				String postalCode = source.getObservationContributorPostalCode(wholeName);
				String country = source.getObservationContributorCountry(wholeName);
				String currentFlag = source.getObservationContributorCurrentFlag(wholeName);
				String addressStartDate = source.getObservationContributorAddressStartDate(wholeName);
				
				System.out.println("PlotDataSource > observation contributor whole name: "+ wholeName);
				System.out.println("PlotDataSource > observation contributor salutation: "+ salutation);
				System.out.println("PlotDataSource > observation contributor given name: "+ givenName );
				System.out.println("PlotDataSource > observation contributor middle name: "+ middleName);
				System.out.println("PlotDataSource > project contributor sur name: "+ surName);
				System.out.println("PlotDataSource > observation contributor org name: "+ organizationName);
				System.out.println("PlotDataSource > observation contributor contactInstructions: "+ contactInstructions);
				System.out.println("PlotDataSource > observation contributor phoneNumber: "+ phoneNumber);
				System.out.println("PlotDataSource > observation contributor orgPosition: "+ orgPosition);
				System.out.println("PlotDataSource > observation contributor email: "+ email);
				System.out.println("PlotDataSource > observation contributor deliveryPoint: "+ deliveryPoint);
				System.out.println("PlotDataSource > observation contributor city: "+ city);
				System.out.println("PlotDataSource > observation contributor administrativeArea: "+ administrativeArea);
				System.out.println("PlotDataSource > observation contributor postalCode: "+ postalCode);
				System.out.println("PlotDataSource > observation contributor country: "+ country);
				System.out.println("PlotDataSource > observation contributor currentFlag: "+ currentFlag);
				System.out.println("PlotDataSource > observation contributor addressStartDate: "+ addressStartDate+"\n");
			}
			
			System.out.println(" \n ----------------------plot site info-----------------------------");
			System.out.println("PlotDataSource > authorplotcode: " + source.plotCode );
			System.out.println("PlotDataSource > accession value: " + source.getAccessionValue(plotName) );
			System.out.println("PlotDataSource > latitude: " + source.getLatitude(plotName) );
			System.out.println("PlotDataSource > longitude: " + source.longitude );
			System.out.println("PlotDataSource > x coord: " + source.xCoord );
			System.out.println("PlotDataSource > y coord: " + source.yCoord );
			System.out.println("PlotDataSource > utm zone: " + source.getUTMZone(plotName) );
			System.out.println("PlotDataSource > datum: " + source.getDatumType(plotName) );
			System.out.println("PlotDataSource > state: "+ source.state);
			System.out.println("PlotDataSource > country: "+ source.country);
			System.out.println("PlotDataSource > plot shape: " + source.plotShape );
			System.out.println("PlotDataSource > geology: " + source.surfGeo );
			System.out.println("PlotDataSource > topo position: " + source.topoPosition );
			System.out.println("PlotDataSource > hydrologic regime: " + source.hydrologicRegime );
			System.out.println("PlotDataSource > plot area(m): " + source.plotArea );
			System.out.println("PlotDataSource > elevation(m): " + source.elevation );
			System.out.println("PlotDataSource > elevation accuracy (%): " + source.elevationAccuracy );
			System.out.println("PlotDataSource > landform : " + source.landForm );
			System.out.println("PlotDataSource > author location: " + source.authorLocation);
			System.out.println("PlotDataSource > stand size: " + source.standSize );
			System.out.println("PlotDataSource > slope gradient: " + source.slopeGradient);
			System.out.println("PlotDataSource > slope aspect: " + source.slopeAspect);
			System.out.println("PlotDataSource > number of named places: " + source.namedPlaces.size() );
			System.out.println("PlotDataSource > confidentialityStatus: " + source.getConfidentialityStatus(plotName) );
			System.out.println("PlotDataSource > confidentialityReason: " + source.getConfidentialityReason(plotName) );
			System.out.println("PlotDataSource > permanence: " + source.isPlotPermanent(plotName) );
			System.out.println("PlotDataSource > soil taxon: " + source.getSoilTaxon(plotName) );
			System.out.println("PlotDataSource > soil taxon source: " + source.getSoilTaxonSource(plotName) ); 
			System.out.println("PlotDataSource > azimuth: " + source.getAzimuth(plotName) );
			System.out.println("PlotDataSource > locationnarrative: " + source.getLocationNarrative(plotName) );
			System.out.println("PlotDataSource > layoutnarrative: " + source.getLayoutNarrative(plotName) );
			System.out.println("PlotDataSource > dsgpoly: " + source.getDSGPoly(plotName) );
			
			System.out.println(" \n ----------------------plot site-observational info-----------------------------");
			System.out.println("PlotDataSource > soilDepth: " + source.getSoilDepth(plotName) );
			System.out.println("PlotDataSource > obsStartDate: " + source.getObsStartDate(plotName) );
			System.out.println("PlotDataSource > obsStopDate: " + source.getObsStopDate(plotName) );
			System.out.println("PlotDataSource > community name: " + source.getCommunityName(plotName));
			System.out.println("PlotDataSource > community code: " + source.getCommunityCode(plotName));
			System.out.println("PlotDataSource > community level: " + source.getCommunityLevel(plotName));
			System.out.println("PlotDataSource > community framework: " + source.getCommunityFramework(plotName));
			System.out.println("PlotDataSource > community classNotes: " + source.getClassNotes(plotName));
			
			System.out.println("PlotDataSource > obs date acc.: " + source.getObsDateAccuracy(plotName));
			System.out.println("PlotDataSource > cover method: " + source.getCoverMethod(plotName));
			System.out.println("PlotDataSource > stratum method: " + source.getStratumMethod(plotName));
			System.out.println("PlotDataSource > stem size limit: " + source.getStemSizeLimit(plotName));
			System.out.println("PlotDataSource > method narrative: " + source.getMethodNarrative(plotName));
			System.out.println("PlotDataSource > taxon obs. area: " + source.getTaxonObservationArea(plotName));
			System.out.println("PlotDataSource > cover dispersion: " + source.getCoverDispersion(plotName));
			System.out.println("PlotDataSource > auto taxon cover: " + source.getAutoTaxonCover(plotName));
			System.out.println("PlotDataSource > stem obs. area: " + source.getStemObservationArea(plotName));
			System.out.println("PlotDataSource > stem sample method: " + source.getStemSampleMethod(plotName));
			System.out.println("PlotDataSource > original data: " + source.getOriginalData(plotName));
			System.out.println("PlotDataSource > effort level: " + source.getEffortLevel(plotName));
			//START
			System.out.println("PlotDataSource > PlotValidationLevel: " + source.getPlotValidationLevel(plotName));
			System.out.println("PlotDataSource > FloristicQuality: " + source.getFloristicQuality(plotName));
			System.out.println("PlotDataSource > BryophyteQuality: " + source.getBryophyteQuality(plotName));
			System.out.println("PlotDataSource > LichenQuality: " + source.getLichenQuality(plotName));
			System.out.println("PlotDataSource > ObservationNarrative: " + source.getObservationNarrative(plotName));
			System.out.println("PlotDataSource > Homogeneity: " + source.getHomogeneity(plotName));
			System.out.println("PlotDataSource > Representativeness: " + source.getRepresentativeness(plotName));
			System.out.println("PlotDataSource > BasalArea: " + source.getBasalArea(plotName));
			System.out.println("PlotDataSource > MoistureRegime: " + source.getSoilMoistureRegime(plotName));
			System.out.println("PlotDataSource > WaterSalinity: " + source.getWaterSalinity(plotName));
			System.out.println("PlotDataSource > ShoreDistance: " + source.getShoreDistance(plotName));
			System.out.println("PlotDataSource > OrganicDepth: " + source.getOrganicDepth(plotName));
			System.out.println("PlotDataSource > PercentBedRock: " + source.getPercentBedRock(plotName));
			System.out.println("PlotDataSource > PercentRockGravel: " + source.getPercentRockGravel(plotName));
			System.out.println("PlotDataSource > PercentWood: " + source.getPercentWood(plotName));
			System.out.println("PlotDataSource > PercentLitter: " + source.getPercentLitter(plotName));
			System.out.println("PlotDataSource > PercentBareSoil: " + source.getPercentBareSoil(plotName));
			System.out.println("PlotDataSource > PercentWater: " + source.getPercentWater(plotName));
			System.out.println("PlotDataSource > PercentOther: " + source.getPercentOther(plotName));
			System.out.println("PlotDataSource > NameOther: " + source.getNameOther(plotName));
			System.out.println("PlotDataSource > StandMaturity: " + source.getStandMaturity(plotName));
			System.out.println("PlotDataSource > SuccessionalStatus: " + source.getSuccessionalStatus(plotName));
			System.out.println("PlotDataSource > TreeHt: " + source.getTreeHt(plotName));
			System.out.println("PlotDataSource > ShrubHt: " + source.getShrubHt(plotName));
			System.out.println("PlotDataSource > NonvascularHt: " + source.getNonvascularHt(plotName));
			System.out.println("PlotDataSource > FloatingCover: " + source.getFloatingCover(plotName));
			System.out.println("PlotDataSource > SubmergedCover: " + source.getSubmergedCover(plotName));
			System.out.println("PlotDataSource > DominantStratum: " + source.getDominantStratum(plotName));
			System.out.println("PlotDataSource > Growthform1Type: " + source.getGrowthform1Type(plotName));
			System.out.println("PlotDataSource > Growthform2Type: " + source.getGrowthform2Type(plotName));
			System.out.println("PlotDataSource > Growthform3Type: " + source.getGrowthform3Type(plotName));
			System.out.println("PlotDataSource > Growthform1Cover: " + source.getGrowthform1Cover(plotName));
			System.out.println("PlotDataSource > Growthform2Cover: " + source.getGrowthform2Cover(plotName));
			System.out.println("PlotDataSource > Growthform3Cover: " + source.getGrowthform3Cover(plotName));
			System.out.println("PlotDataSource > NotesPublic: " + source.getNotesPublic(plotName));
			System.out.println("PlotDataSource > NotesMgt: " + source.getNotesMgt(plotName));
			System.out.println("PlotDataSource > Revisions: " + source.getRevisions(plotName));

//END
			System.out.println("\n " );
			
			
			for (int i=0; i<source.namedPlaces.size(); i++)
			{
				String placeName = source.namedPlaces.elementAt(i).toString(); 
				String placeDescription = getPlaceDescription(placeName);
				String placeCode = getPlaceCode(placeName);
				String placeSystem = getPlaceSystem(placeName);
				String placeOwner = getPlaceOwner(placeName);
				System.out.println("PlotDataSource > place name: " + placeName);
				System.out.println("PlotDataSource > place description: " + placeDescription);
				System.out.println("PlotDataSource > place code: " + placeCode);
				System.out.println("PlotDataSource > place owner: " + placeOwner);
			}
			
			
			System.out.println(" \n ----------------------strata info-----------------------------");
			System.out.println("unique strata names: " + source.uniqueStrataNames.toString() );
			//get the height, base cover etc for  each of the strata
			for (int i=0; i<source.uniqueStrataNames.size(); i++)
			{
				String curstrata = source.uniqueStrataNames.elementAt(i).toString();
				String cover = getStrataCover(plotCode, curstrata);
				String base = getStrataBase(plotCode, curstrata);
				String height = getStrataHeight(plotCode, curstrata);
				System.out.println("PlotDataSource > strata: " + curstrata+" cover: "+cover+" base: "+base+" height: "+ height);
			}
			
			System.out.println(" \n ----------------------plant taxa info-----------------------------");
			System.out.println("number of unique plant names: " + source.plantTaxaNames.size() );
			
			//get the coverages for the plants for each strata
			for (int i=0; i<source.plantTaxaNames.size(); i++)
			{
				String name = source.plantTaxaNames.elementAt(i).toString();
				// get the strata that the plant exists in
				Vector strata = getTaxaStrataExistence(name, plotCode);
				System.out.println("PlotDataSource > plant name: " + name  );
				String code = getPlantTaxonCode(name);
				System.out.println("PlotDataSource > plant code: " + code  );
				System.out.println("PlotDataSource > cum. strata cover: " + source.getCummulativeStrataCover(name, plotName));
				for (int ii=0; ii<strata.size(); ii++)
				{
					String curStrata = strata.elementAt(ii).toString();
					System.out.println("PlotDataSource >  strata: " + curStrata );
					System.out.println("PlotDataSource >  cover: " + getTaxaStrataCover(name, 
						plotCode, curStrata ));
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("PlotDataSource > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	
/**
 * main method for testing that the plugin classes work and work well
 * @param pluginClass -- the name of the plugin class
 * @param plotName -- the name of the plot
 */
	public static void main(String[] args)
	{
		if (args.length == 2) 
		{
			//assume that the two args are 1] plugin and 2] plotName
			String pluginClass = args[0];
			String plotName = args[1];
			//should make sure that the plugin exists
			PlotDataSource source = new PlotDataSource(pluginClass);
			source.printDBVariables(pluginClass, plotName);
			//			System.out.println("slope: " + source.getSlopeAspect(plotName) );
			//			System.out.println("state: " + source.getState(plotName) ); 
		}
		else
		{
			//use the plugin with the test data
			PlotDataSource source = new PlotDataSource("VegBankDataSourcePlugin");
			source.printDBVariables("VegBankDataSourcePlugin" , "1");
		}
	}
	
}
	
