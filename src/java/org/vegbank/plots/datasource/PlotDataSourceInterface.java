package org.vegbank.plots.datasource;
import java.util.Hashtable;
import java.util.Vector;

/**
 * '$RCSfile: PlotDataSourceInterface.java,v $'
 *
 * Purpose:  This class provides an interface for the plugins to be used for
 * accessing vegplot data.  For the Summer 2002 release there will be 
 * the following plugins to implement this class: <br>
 * NativeXmlPlugin -- a plugin to allow access to data stored in the 
 * Native VegBank XML format. <br> <br>
 * TNCPlotsDB -- a plugin to allow access to data stored in The Nature 
 * Conservancy's (TNC) Plots database. <br>
 * VegBankDataSourcePlugin -- to access data on the VegBank System running
 * either on Oracle 8i, or Postgres 7.1
 * VBAccessDataSourcePlugin -- to access mdb files (via ODBC) written by the 
 * Client tool. <br> <br>
 *
 * '$Author: farrell $'
 * '$Date: 2003-08-21 21:16:45 $'
 * '$Revision: 1.5 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */  

public interface PlotDataSourceInterface
{
	
	//method that returns all the plot names, as a vector, stored in a given
	//source
	Vector getPlotNames();
	
	
	//this method retrives all the attributes for a given plotname
	void getPlotData(String plotName) throws Exception;
		
		
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
	 
	/**
	 * this method takes the primary plot identifier and returns the 
	 * name of the plot as assigned by the author.  In the case of the 
	 * vegbank databases, this will return the authorPlotCode for a given 
	 * 'plot_id' (the primary-key value for the plot table), whereas for 
	 * the TNC plots database, the the unique plotid is the name of the 
	 * plot
	 */
	String getPlotCode(String plotName);
	
	/** 
	 * method that returns the accession number associated with a plot id
	 * the input plot id is the unique identifier of the plot as used by 
	 * the RDBMS
	 * @param plotId -- the RDBMS unique plot ID
	 */
	 String getAccessionValue(String plotId);
	 
	 /** 
	 * method that returns the accession number associated with an observation
	 * the input plot id is the unique identifier of the plot as used by 
	 * the RDBMS.  This was added late in August 2002, and this method will
	 * have to be overloaded by one that accepts the plotId and the vbsequence
	 * number, for this method will only work where there is a single observation
	 * per plot.
	 * @param plotId -- the RDBMS unique plot ID
	 */
	 String getObservationAccessionNumber(String plotId);
	 
	 
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
	
	/**
	 * method returns the geographic datum (eg., NAD27, WS84)
	 *@param plotName -- the plot 
	 */
	String getDatumType(String plotName);
	
	
	//returns the azimuth? --> The horizontal angle of the observer's bearing in surveying
	String getAzimuth(String plotName);
	// returns the dsgploy? --> coordinates defining the outline of an area covered by a data set. 
	String getDSGPoly(String plotName);
	// returns the plot narrative
	String getLocationNarrative(String plotName);
	// returns the layout narative
	String getLayoutNarrative( String plotName );

	
	
	String getPlotShape(String plotName);
	//returns the plot area
	String getPlotArea(String plotName);	
	//returns the state in which the plot exists
	String getState(String plotName);
	//returns the topo position
	String getTopoPosition(String plotName);
	/**
	 * returns the slope aspect in degrees between 
	 * 0 - 360.  Becase this value is 'numeric' in 
	 * the database, this method should never return 
	 * a 'null' or non-numeric value
	 * @param plotName -- the plot 
	 */
	String getSlopeAspect(String plotName);
	
	/**
	 * returns the gradient of the slope inb degrees
	 * and if the inclination of the slope is too 
	 * irregular then the method should return a '-1.0'
	 * @param plotName -- the plot 
	 */
	String getSlopeGradient(String plotName);
	
	/**
	 * returns the surficial geology
	 * @param plotName -- the plot
	 * @deprecated
	 */
	String getSurfGeo(String plotName);
	
	/**
	 * returns the rockType
	 * @param plotName -- the plot
	 */
	String getRockType(String plotName);
	
	/**
	 * returns the surficial surficial Deposits
	 * @param plotName -- the plot
	 */
	String getSurficialDeposits(String plotName);
	
	/**
	 * retuns the country
	 * @param plotName -- the plot 
	 */
	String getCountry(String plotName);
	
	/**
	 * returns the size of the stand -- extensive etc..
	 * @param plotName -- the plot 
	 */
	String getStandSize(String plotName);
	
	/**
	 * returns the location as described by the author
	 * @param plotName -- the plot 
	 */
	String getAuthorLocation(String plotName);
	
	/**
	 * returns the landForm
	 * @param plotName -- the plot 
	 */
	String getLandForm(String plotName);
	
	/**
	 * retuns the elevation and must be either a numeric 
	 * value or an empty string and cannot be 'null'
	 * The retruned value should be in Meters
	 * @param plotName -- the plot
	 */
	String getElevation(String plotName);
	
	/**
	 * returns the elevation accuracy and must be either a numeric
	 * value or an empty string and cannot be 'null'. The value
	 * must be a percentage of the elevation
	 * @param plotName -- the plot
	 */
	String getElevationAccuracy(String plotName);
	
 /**
	* return the confidentiality reason -- not null
	* @param plotName -- the plot
	*/
	String	getConfidentialityReason(String plotName);
	
	/**
	 * return the confidentiality status -- not null 0-6
	 * Acceptable values to be returned from this function are: <br> <br>
	 * 0 = no confidentaility <br>
	 * 1 = 1km radius <br>
	 * 2=10km radius <br>
	 * 3=100km radius <br>
	 * 4 = location embargo <br> 
	 * 5 = public embargo on all plot data <br> 
	 * 6 = full embargo on all plot data <br>
	 * <br> <br>
	 * @param plotName -- the plot
	 */
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
	 * returns the community classification notes for the named plot
	 */
	String getClassNotes(String plotName);
	
	String getCommunityStartDate(String plotName);
	String getCommunityStopDate(String plotName);
	String getCommunityInspection(String plotName);
	String getCommunityPublication(String plotName);
	String getCommunityTableAnalysis(String plotName);
	String getCommunityMultiVariateAnalysis(String plotName);
	String getCommunityExpertSystem(String plotName);
	
	
	/**
	 **
	 ** THE OBSERVATION - RELATED DATA
	 **
	 **/
	 
	/**
	* Observation Contributors 
	*/
	 
	Vector getObservationContributors( String plotName );
	String getObservationContributorSalutation(String contributorWholeName);
	String getObservationContributorGivenName(String contributorWholeName);
	String getObservationContributorMiddleName(String contributorWholeName);
	String getObservationContributorSurName(String contributorWholeName);
	String getObservationContributorOrganizationName(String contributorWholeName);
	String getObservationContributorContactInstructions(String contributorWholeName);
	String getObservationContributorPhoneNumber(String contributorWholeName);
	String getObservationContributorCellPhoneNumber(String contributorWholeName);
	String getObservationContributorFaxPhoneNumber(String contributorWholeName);
	String getObservationContributorOrgPosition(String contributorWholeName);
	String getObservationContributorEmailAddress(String contributorWholeName);
	String getObservationContributorDeliveryPoint(String contributorWholeName);
	String getObservationContributorCity(String contributorWholeName);
	String getObservationContributorAdministrativeArea(String contributorWholeName);
	String getObservationContributorPostalCode(String contributorWholeName);
	String getObservationContributorCountry(String contributorWholeName);
	String getObservationContributorCurrentFlag(String contributorWholeName);
	String getObservationContributorAddressStartDate(String contributorWholeName);
	String getObservationContributorRole(String contributorWholeName);
	 
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
	
	
	//START
	/**
	* method to return the accuracy of the observation date <br>
	* These results may include the following list: <br>
	* exact <br>
	* one month <br>
	* three months <br>
	* one year <br>
	* three years <br>
	* ten years <br>
	* greater than ten years <br>
	* @param plotName -- the plot
	* @return accuracy -- the accuracy as described above
	*/
	String getObsDateAccuracy(String plotName);
	 
	/**
	* method to return the name of the cover method used 
	* for the collection of the plot attributes (eg., Braun Blanquet)
	* Name of the coverclass method 
	* (e.g., Braun-Blanquet, Barkman, Domin, Daubenmire, Carolina Vegetation Survey, etc.)
	*
	* @param plotName -- the plot
	* @return methodtype -- the methodtype as described above 
	*/
	Hashtable getCoverMethod(String plotName);
	
	/**
	 * method that returns the stratum method name and a 
	 * Link to the definitions of strata used in recording 
	 * taxon-specific values of cover.
	 * @param plotName -- the plot
	 * @return limit -- the lower limit in centimeters
	 */
	 Hashtable getStratumMethod(String plotName);
	 
	/**
	 * method to return the stem size limit, which is the lower 
	 * diameter limit in centimeters for inclusion of a tree in 
	 * the stem count (stemCount)
	 * @param plotName -- the plot
	 * @return limit -- the lower limit in centimeters
	 */
	 String getStemSizeLimit(String plotName);
		 
		 
	/**
	 * method to return the method narrative which is 
	 * additional metadata helpful for understanding how the 
	 * data were collected during the observation event.
	 * @param plotName -- the plot
	 * @return methodnarrative -- the narative described above
	 */
	 String getMethodNarrative(String plotName);
	
	/**
	 * method that returns the TaxonObservationArea which is the:
	 * total surface area (in m2) used for cover estimates and for 
	 * which a complete species list is provided. If subplots were used, 
	 * this would be the total area of the subplots without intersticial space.
	 *
	 * @param plotName -- the plot
	 * @return area -- the taxon observation area
	 */
	 String getTaxonObservationArea(String plotName);
	/**
	 * method that returns the cover dispersion for a plot observation,
	 * which is the: cover values for the total taxon list collected 
	 * from one contiguous area or dispersed subplots (e.g., continguous, 
	 * dispersed-regular, dispered-random) <br> <br>
	 *	contiguous <br>
	 *	dispersed-regular <br>
	 *	dispersed-random <br>
	 *
	 * @param plotName -- the plot
	 * @return coverdispersion -- the cover dispersion values in the above list:
	 *
	 */
	 String getCoverDispersion(String plotName );
		
	/**
	 * method that returns the auto taxon cover which is a boolean where:
	 * TRUE indicates that taxonObservation.taxonCover was automatically 
	 * calculated from the values of all stratumObservation.taxonStratumCover
	 *
	 * @param plotName -- the plot
	 * @return autotaxoncover -- the autotaxoncover
	 */
	boolean getAutoTaxonCover(String plantName);
	
	/**
	 * method that returns the the total surface area (in m2) observed 
	 * for recording woody stem data
	 *
	 * @param plotName -- the plot
	 * @return area -- the observation area
	 */
	String getStemObservationArea(String plotName);
	
	/**
	 * method that returns the method used to obtain basal area or tree 
	 * stem data (e.g., full census, point quarter, random pairs, Bitterlich, other).
	 * List: <br> <br>
	 * full census <br>
	 * point quarter <br>
	 * random pairs <br>
	 * bitterlich <br>
	 * other (explained in methodNarrative) <br>
	 *
	 * @param plotName -- the plot
	 * @return autotaxoncover -- the autotaxoncover
	 */
	String getStemSampleMethod(String plotName);
	
	/**
	 * method that returns the location where the hard data reside and 
	 * any access instructions.
	 *
	 * @param plotName -- the plot
	 * @return autotaxoncover -- the autotaxoncover
	 */
	String getOriginalData(String plotName );
	
	/**
	 * method to return the This is the effort spent 
	 * making the observations as estimated by the party 
	 * that submitted the data (e.g., Very thorough, Average, Hurried 
	 * description).
	 * List: <br> <br>
	 * very thorough <br>
	 * accurate <br>
	 * hurried <br>
	 * @param plotName -- the plot
	 * @return autotaxoncover -- the autotaxoncover
	 */
	String getEffortLevel( String plotName );	 

	//START
	String getPlotValidationLevel(String plotName);
	String  getFloristicQuality(String plotName);
	String  getBryophyteQuality(String plotName);
	String  getLichenQuality(String plotName);
	String  getObservationNarrative(String plotName);
	String  getHomogeneity(String plotName);
	String  getRepresentativeness(String plotName);
	String  getBasalArea(String plotName);
	String  getSoilMoistureRegime(String plotName);
	String  getWaterSalinity(String plotName);
	String  getShoreDistance(String plotName);
	String  getOrganicDepth(String plotName);
	String  getPercentBedRock(String plotName);
	String  getPercentRockGravel(String plotName);
	String  getPercentWood(String plotName);
	String  getPercentLitter(String plotName);
	String  getPercentBareSoil(String plotName);
	String  getPercentWater(String plotName);
	String  getPercentOther(String plotName);
	String  getNameOther(String plotName);
	String  getStandMaturity(String plotName);
	String  getSuccessionalStatus(String plotName);
	String  getTreeHt(String plotName);
	String  getShrubHt(String plotName);
	String  getNonvascularHt(String plotName);
	String  getFloatingCover(String plotName);
	
	String getNonvascularCover(String plotName);
	String getFieldCover(String plotName);
	String getShrubCover(String plotName);
	String getTreeCover(String plotName);
	String getSubmergedHt(String plotName);
	String getFieldHt(String plotName);
	String getWaterDepth(String plotName);
	String getLandscapeNarrative(String plotName);
	String getPhenologicalAspect(String plotName);
	
	String getSubmergedCover(String plotName);
	String getDominantStratum(String plotName);
	String getGrowthform1Type(String plotName);
	String getGrowthform2Type(String plotName);
	String getGrowthform3Type(String plotName);
	String getGrowthform1Cover(String plotName);
	String getGrowthform2Cover(String plotName);
	String getGrowthform3Cover(String plotName);
	boolean  getNotesPublic(String plotName);
	boolean  getNotesMgt(String plotName);
	boolean  getRevisions(String plotName);
	
	//END
	 
	//END
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
	
	/** 
	 * method that retuns the cummulative cover accross all strata for a given
	 * taxa in a given plot.  This can be thought of as the volume of a given
	 * plant accross all strata.  
	 *
	 * @param plantName -- the name of the plant as used by the author of the plot
	 * @param plotName -- the plot
	 * @return percentCover -- the percentage cover of that plant (in percent not decimal)
	 *
	 */
	String getCummulativeStrataCover(String plantName, String plotCode);
	
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
	 * method to return the taxon cover from a data source using as input 
	 * the scientific plant name -- or the plant name that comes from 
	 * the 'getPlantTaxaNames' method
	 *
	 * @param plantName -- the scientific plantName
	 */
		String getPlantTaxonCover(String plantName, String plotName);	

	/**
	 * method that retuns the names of the unique strata elements 
	 * for a given plot as a vector.
	 *
	 * @return strataNames -- a vector containing the strata names
	 * @param plotName -- the name of the plot
	 */
	 Vector getUniqueStrataNames(String plotName);
	 
	 /**
	 * method to return the cover for a given strata for a given 
	 * plot.  This value, that is returned, should be percentage
	 * between 0 - 100.
	 * 
	 * @param plotName -- the name of the plot
	 * @param strataName -- the name of the stratum 
	 */
		String getStrataCover(String plotName, String strataName);

	 
	 /** 
	  * method the returns the base height of a strata based on the 
		* name of that starta and the plot for which that strata is included
		*
		* @param plotName -- the name of the plot
		* @param strataName -- the name of the stratum 
		*/
		String getStrataBase(String plotName, String strataName);

		
		/**
		 * method that returns the upper height of a starata based on 
		 * the name of the strata and the plot inwhich the strata exists
		 *
		 * @param plotName -- the name of the plot
		 * @param strataName -- the name of the stratum 
		 */
		 String getStrataHeight(String plotName, String strataName);
		 
	/**
	 * method that returns the name of the stratumMethod used
	 *
	 * @param plotName -- the name of the plot
	 * @param strataName -- the name of the stratum 
	 */
	 String getStratumMethodName(String plotName);
	
	/**
	 * method that returns the name of the coverMethod used
	 *
	 * @param plotName -- the name of the plot
	 * @param strataName -- the name of the stratum 
	 */
	 String getCoverMethodName(String plotName);	
}
