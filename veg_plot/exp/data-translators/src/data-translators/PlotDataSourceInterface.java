/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-12-06 02:07:58 $'
 * '$Revision: 1.1 $'
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
	
	//this method retrives all the attributes for a gine plotname
  void getPlotData(String plotName)
		throws Exception;
		
	//returns the project name 
	String getProjectName(String plotName);
	
	//returns the project description
	Vector getProjectContributors(String plotName);
	
	String getProjectStartDate(String plotName);
	String getProjectStopDate(String plotName);
	
	//returns the project contributor
	String getProjectDescription(String plotName);
	//returns the current plot code
	String getPlotCode(String plotName);
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
	String getlandForm(String plotName);
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
