package org.vegbank.plots.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface DataSourceServerInterface extends Remote 
{
	
	 
	 /**
	  * this method returns the validation report for the plot
		* @return -- returns the xml receipt for the plot validation report 
		*/
		public String getValidationReport()
		throws Exception;

  /**
   * this metod uses the  plotvalidator to test that the input plot is valid.
   * @param plotName -- the name of the plot to test for validity 
   * @return valid  -- true if the plot is valid, else false 
	 */
   public boolean isPlotValid(String plot)
   throws Exception;
	
	//method for inserting a plot into the database
	public String insertPlot(String plot, String fileType, String emailAddress)
	throws Exception;
		
		//method for validating that the uploaded file is indeed an mdb file
		public boolean isMDBFileValid( String location )
		throws Exception;
		
   //method for uploading a file to the server
	 public boolean getMDBFile(String fileName, String fileType,  byte buffer[], String location )
	 throws Exception;
	 
	 public byte[] downloadFile(String fileName) throws
   RemoteException;
	 
	//this method retrives all the attributes for a gine plotname
  void getPlotData(String plotName)
	throws Exception;
	
	//method that returns all the plot names, as a vector, stored in a given
	//source
	public Vector getPlotNames()
	throws Exception;
	
	//method that returns all the plot names, as a vector, stored in a given
	//source
	public Vector getPlotNames(String fileType)
	throws Exception;
	
	
	//returns the project name 
	public String getProjectName(String plotName)
	throws Exception;
	
	//returns the project name 
	String getProjectDescription(String plotName)
	throws Exception;
	
	//returns the easting
	String getXCoord(String plotName)
	throws Exception;

	//returns the northing
	String getYCoord(String plotName)
	throws Exception;

	//returns the latitude
	String getLatitude(String plotName)
	throws Exception;

	//returns the longitude
	String getLongitude(String plotName)
	throws Exception;	
	
	//returns the geographic zone
	String getUTMZone(String plotName)
	throws Exception;

	//returns the plot shape
	String getPlotShape(String plotName)
	throws Exception;

	//returns the plot area
	String getPlotArea(String plotName)
	throws Exception;

	//returns the state for the current plot
	String getCommunityName(String plotName)
	throws Exception;

	//returns the state in which the plot exists
	String getState(String plotName)
	throws Exception;

	//retuns the hydrologic regime
	String getHydrologicRegime(String plotName)
	throws Exception;

	//returns the topo position
	String getTopoPosition(String plotName)
	throws Exception;

	//returns the slope aspect
	String getSlopeAspect(String plotName)
	throws Exception;

	//returns yje slope gradient
	String getSlopeGradient(String plotName)
	throws Exception;

	//returns the surficial geology
	String getSurfGeo(String plotName)
	throws Exception;

	//retuns the country
	String getCountry(String plotName)
	throws Exception;

	//returns the size of the stand -- extensive etc..
	String getStandSize(String plotName)
	throws Exception;

	//returns the location as described by the author
	String getAuthorLocation(String plotName)
	throws Exception;

	//returns the landForm
	String getLandForm(String plotName)
	throws Exception;

	//retuns the elevation
	String getElevation(String plotName)
	throws Exception;

	//returns the elevation accuracy
	String getElevationAccuracy(String plotName)
	throws Exception;

	//return the confidentiality reason -- not null
	String	getConfidentialityReason(String plotName)
	throws Exception;

	//return the confidentiality status -- not null 0-6
	String getConfidentialityStatus(String plotName)
	throws Exception;
	
	//#START EDITIONS
	//retuns the input plot name
	public String getAuthorPlotCode(String plotName)
	throws Exception;
	
	
	
		/**
	 * method that retuns the start date of the project that corresponds
	 * to the input plot
	 */
	public String getProjectStartDate(String plot)
	throws Exception;

	/**
	 * method that retuns the stop date of the project that corresponds
	 * to the input plot
	 */
	public String getProjectStopDate(String plot)
	throws Exception;
		
	public String getObservationCode(String plotName)
	throws Exception;
	
	public String getSamplingMethod(String plotName)
	throws Exception;
	
	public String getObservationStartDate(String plotName)
	throws Exception;
	
	public String getObservationDateAccuracy(String plotName)
	throws Exception;
	
	public String getSoilDepth(String plotName)
	throws Exception;
	
	public String getPercentRock(String plotName)
	throws Exception;
	
	public String getPercentLitter(String plotName)
	throws Exception;
	
	public String getPercentWater(String plotName)
	throws Exception;
	
	public Vector getUniqueStrataNames(String plotName)
	throws Exception;
	
	public Vector getPlantNames(String plotName)
	throws Exception;
	
	public void setPluginClassName(String pluginClassName)
	throws Exception;

	/**
	 * @return  -- the location the file can be saved
	 */
	public String getFileUploadLocation()
		throws Exception;

	/**
	 * @param location
	 */
	public void releaseFileUploadLocation(String location)
		throws Exception;
	
//#STOP EDDITIONS
	

	
}
