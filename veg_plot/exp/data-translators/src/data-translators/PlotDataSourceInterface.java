/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-01-05 19:11:15 $'
 * '$Revision: 1.2 $'
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
	String getProjectContributorCountry(String contributorWholeName);
	String getProjectContributorCurrentFlag(String contributorWholeName);
	String getProjectContributorAddressStartDate(String contributorWholeName);
	
	String getProjectStartDate(String plotName);
	String getProjectStopDate(String plotName);
	
	//returns the project contributor
	String getProjectDescription(String plotName);
	//returns the current plot code
	String getPlotCode(String plotName);
	
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
	//returns the state for the current plot
	String getCommunityName(String plotName);
	//returns the state in which the plot exists
	String getState(String plotName);
	//retuns the hydrologic regime
	String getHydrologicRegime(String plotName);
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
