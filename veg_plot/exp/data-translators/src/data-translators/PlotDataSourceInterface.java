/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-04-10 18:42:55 $'
 * '$Revision: 1.7 $'
 *
 *
 */
 
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class provides an interface
 */
public interface PlotDataSourceInterface
{
	
	//method that returns all the plot names, as a vector, stored in a given
	//source
	Vector getPlotNames();
	
	
	//this method retrives all the attributes for a gine plotname
  void getPlotData(String plotName)
		throws Exception;
		
		
		
	/**
	 **
	 ** THE PROJECT - RELATED DATA
	 **
	 **/
	
	//returns the project name 
	String getProjectName(String plotName);
	//returns a vector containing full name of the project contributors
	// such as 'bob peet' think about using, in the future a method that 
	// returns some other unique identifier for a contributor
	Vector getProjectContributors(String plotName);
	String getProjectContributorSalutation(String contributorWholeName);
	String getProjectContributorGivenName(String contributorWholeName);
	String getProjectContributorMiddleName(String contributorWholeName);
	String getProjectContributorSurName(String contributorWholeName);
	String getProjectContributorOrganizationName(String contributorWholeName);
	String getProjectContributorContactInstructions(String contributorWholeName);
	String getProjectContributorPhoneNumber(String contributorWholeName);
	String getProjectContributorCellPhoneNumber(String contributorWholeName);
	String getProjectContributorFaxPhoneNumber(String contributorWholeName);
	String getProjectContributorOrgPosition(String contributorWholeName);
	String getProjectContributorEmailAddress(String contributorWholeName);
	String getProjectContributorDeliveryPoint(String contributorWholeName);
	String getProjectContributorCity(String contributorWholeName);
	String getProjectContributorAdministrativeArea(String contributorWholeName);
	String getProjectContributorPostalCode(String contributorWholeName);
	String getProjectContributorCountry(String contributorWholeName);
	String getProjectContributorCurrentFlag(String contributorWholeName);
	String getProjectContributorAddressStartDate(String contributorWholeName);
	String getProjectStartDate(String plotName);
	String getProjectStopDate(String plotName);
	
	
	//returns the project contributor
	String getProjectDescription(String plotName);
	
		
	/**
	 **
	 ** THE PLOT - RELATED DATA
	 **
	 **/
	//returns the current plot name -- the name given to the plot by the author
	String getPlotCode(String plotName);
	
	/** 
	 * method that returns the accession number associated with a plot id
	 * the input plot id is the unique identifier of the plot as used by 
	 * the RDBMS
	 * @param plotId -- the RDBMS unique plot ID
	 */
	 String getAccessionValue(String plotId);
	//returns the placeNames each name can be used to retrieve
	//other information about a place
	Vector getPlaceNames(String plotName);
	//retuns a description of a place based on a placeName
	String getPlaceDescription(String placeName);
	//returns a placeCode based on a placeName
	String getPlaceCode(String placeName);
	//returns a place system based on a placeName
	String getPlaceSystem(String placeName);
	//returns the owner of a place based on the name of a place
	String getPlaceOwner(String placeName);
	//returns the easting
	String getXCoord(String plotName);
	//returns the northing
	String getYCoord(String plotName);
	//returns the latitude
	String getLatitude(String plotName);
	//returns the longitude
	String getLongitude(String plotName);
	//returns the geographic zone
	String getUTMZone(String plotName);
	//returns the plot shape
	String getPlotShape(String plotName);
	//returns the plot area
	String getPlotArea(String plotName);	
	//returns the state in which the plot exists
	String getState(String plotName);
	//returns the topo position
	String getTopoPosition(String plotName);
	//returns the slope aspect
	String getSlopeAspect(String plotName);
	//returns yje slope gradient
	String getSlopeGradient(String plotName);
	//returns the surficial geology
	String getSurfGeo(String plotName);
	//retuns the country
	String getCountry(String plotName);
	//returns the size of the stand -- extensive etc..
	String getStandSize(String plotName);
	//returns the location as described by the author
	String getAuthorLocation(String plotName);
	//returns the landForm
	String getLandForm(String plotName);
	//retuns the elevation
	String getElevation(String plotName);
	//returns the elevation accuracy
	String getElevationAccuracy(String plotName);
	//return the confidentiality reason -- not null
	String	getConfidentialityReason(String plotName);
	//return the confidentiality status -- not null 0-6
	String getConfidentialityStatus(String plotName);
	/**
	 * returns true if the plot is a permanent plot or false if not
	 * @param plotName -- the plot
	 */
	 boolean isPlotPermanent(String plotName);
 /**
	* returns the soil taxon for the plot -- this is the USDA class
	* heirarchy (eg., Order, Group, Family, Series etc..)
	* @param plotName -- the plot
	*/
	String getSoilTaxon(String plotName);
	/**
	 * returns how the soil taxon was determined (eg., field observation
	 * mapping, other ...)
	 * @param plotName -- the plot
	 */
	String getSoilTaxonSource(String plotName); 
	
	
	/**
	 **
	 ** THE COMMUNITY - RELATED DATA
	 **
	 **/
	
	/**
	 * returns the community name for the named plot
	 */
	String getCommunityName(String plotName);
	/**
	 * returns the community code for the named plot
	 */
	String getCommunityCode(String plotName);
	/**
	 * returns the community level of the framework for the named plot
	 */
	String getCommunityLevel(String plotName);
	/**
	 * returns the community framework for the named plot
	 */
	String getCommunityFramework(String plotName);
	
	
	
	/**
	 **
	 ** THE OBSERVATION - RELATED DATA
	 **
	 **/
	
	/**
	 * returns the hydrologic regime of a plot (this is a closed list
	 * that can be looked up)
	 * @param plotName -- the plot
	 */
	String getHydrologicRegime(String plotName);
	/**
	 * returns the soil drainage of a plot
	 * @param plotName -- the plot
	 */
	String getSoilDrainage(String plotName); 
	/**
	 * returns the author's observation code
	 * @param plotName -- the plot
	 */
	String getAuthorObsCode(String plotName); 
	/**
	 * returns the start date for the plot observation
	 * @param plotName -- the plot
	 */
	String getObsStartDate(String plotName);
	/**
	 * returns the stop date for the plot observation
	 * @param plotName -- the plot
	 */
	String getObsStopDate(String plotName);
	/**
	 * returns the soil depth of a plot
	 * @param plotName -- the plot
	 */
	String getSoilDepth(String plotName); 
	
	
	
	/**
	 **
	 ** THE TAXONOMY - RELATED DATA
	 **
	 **/
	//returns a list of the unique plant taxa names in the plot
	Vector getPlantTaxaNames( String plotName);
	//retuns a vector containing the names of the strata inwhich the plant taxa
	
	//exist for a given plot
	Vector getTaxaStrataExistence(String plantName, String plotName);
	
	// method that retuns the cummulative cover accross all strata for a given
	// taxa in a given plot
	String getCummulativeStrataCover( String plantName, String plotCode);
	
	//method that returns the coverage of a plant in a strata within a plot
	String getTaxaStrataCover(String plantName, String plotCode, String
	stratumName);

	/**
	 * method to return the taxa code from a data source using as input 
	 * the scientific plant name -- or the plant name that comes from 
	 * the 'getPlantTaxaNames' method
	 *
	 * @param plantName -- the scientific plantName
	 */
	 String getPlantTaxonCode(String plantName);
	
	
	/**
	 * method that retuns the names of the unique strata elements 
	 * for a given plot as a vector
	 */
	 Vector getUniqueStrataNames(String plotName);
	 
	 /**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
		String getStrataCover(String plotName, String strataName);

	 
	 /** 
	  * method the returns the base height of a strata based on the 
		* name of that starta and the plot for which that strata is included
		*/
		String getStrataBase(String plotName, String strataName);

		
		/**
		 * method that returns the upper height of a starata based on 
		 * the name of the strata and the plot inwhich the strata exists
		 */
		 String getStrataHeight(String plotName, String strataName);
		
}
