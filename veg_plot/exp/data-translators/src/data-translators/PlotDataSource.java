import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

/**
 * this class represents an 'exeternal' data source -- a data source which
 * is not the vegbank databases or the vegbank native xml type document
 * at the inception of this class exteranl sources include: tnc plots, 
 * turbo veg plots and the structured data set.  In the future this class
 * will also contain local 'vegbank' data sources.  This class facilitates
 * the access to vegbank-alternative data sources via both public variables
 * and methods
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2002-02-03 19:24:31 $'
 * 	'$Revision: 1.9 $'
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
	 */
	public PlotDataSource(String pluginClass )
	{
		try
		{ 
			//create a generic object of the type specified 
			pluginObj = createObject(pluginClass);
		}
		catch(Exception e)
		{
			System.out.println("PlotDataSource > Error getting plugin: " + e.getMessage());
		}
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

			projectName = ((PlotDataSourceInterface)pluginObj).getProjectName(plotName);
			projectDescription = ((PlotDataSourceInterface)pluginObj).getProjectDescription(plotName);
			projectContributors = ((PlotDataSourceInterface)pluginObj).getProjectContributors(plotName);
			projectStartDate = ((PlotDataSourceInterface)pluginObj).getProjectStartDate(plotName);
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
			
			
			confidentialityReason = ((PlotDataSourceInterface)pluginObj).getConfidentialityReason(plotName);
			confidentialityStatus = ((PlotDataSourceInterface)pluginObj).getConfidentialityStatus(plotName);
			authorLocation
			= ((PlotDataSourceInterface)pluginObj).getAuthorLocation(plotName);
			landForm = ((PlotDataSourceInterface)pluginObj).getLandForm(plotName);
			elevation = ((PlotDataSourceInterface)pluginObj).getElevation(plotName);
			elevationAccuracy
			= ((PlotDataSourceInterface)pluginObj).getElevationAccuracy(plotName);
			
			uniqueStrataNames = ((PlotDataSourceInterface)pluginObj).getUniqueStrataNames(plotName);
			plantTaxaNames = ((PlotDataSourceInterface)pluginObj).getPlantTaxaNames(plotCode);
			uniquePlantTaxaNumber =  plantTaxaNames.size();
			System.out.println("PlotDataSource >  end");
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
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
        Class classDefinition = Class.forName(className);
        object = classDefinition.newInstance();
    	} 
    	catch (InstantiationException e) 
    	{
     	   throw new InstantiationException("Error instantiating plugin: " + e);
    	} 
    	catch (IllegalAccessException e) 
   	 {
   	     throw new IllegalAccessException("Error accessing plugin: " + e);
   	 } 
    	catch (ClassNotFoundException e) 
    	{
        throw new ClassNotFoundException("Plugin " + className + " not " +
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
	String getXCoord(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getXCoord(plotName);
		return(s);
	}
	//returns the northing
	String getYCoord(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getYCoord(plotName);
		return(s);
	}
	
	//returns the latitude
	String getLatitude(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLatitude(plotName);
		return(s);
	}
	
	//returns the longitude
	String getLongitude(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLongitude(plotName);
		return(s);
	}
	
	//returns the geographic zone
	String getUTMZone(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getUTMZone(plotName);
		return(s);
	}
	
	//returns the plot shape
	String getPlotShape(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPlotShape(plotName);
		return(s);
	}
	
	//returns the plot area
	String getPlotArea(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getPlotArea(plotName);
		return(s);
	}
	
	//returns the state for the current plot
	String getCommunityName(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCommunityName(plotName);
		return(s);
	}
	
	

	//returns the state in which the plot exists
	String getState(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getState(plotName);
		return(s);
	}
	
	//retuns the hydrologic regime
	String getHydrologicRegime(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getHydrologicRegime(plotName);
		return(s);
	}
	
	//returns the topo position
	String getTopoPosition(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getTopoPosition(plotName);
		return(s);
	}
	
	//returns the slope aspect
	String getSlopeAspect(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSlopeAspect(plotName);
		return(s);
	}
	
	//returns yje slope gradient
	String getSlopeGradient(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSlopeGradient(plotName);
		return(s);
	}
	
	//returns the surficial geology
	String getSurfGeo(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getSurfGeo(plotName);
		return(s);
	}
	
	//retuns the country
	String getCountry(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getCountry(plotName);
		return(s);
	}
	
	//returns the size of the stand -- extensive etc..
	String getStandSize(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getStandSize(plotName);
		return(s);
	}
	
	//returns the location as described by the author
	String getAuthorLocation(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getAuthorLocation(plotName);
		return(s);
	}
	
	//returns the landForm
	String getLandForm(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getLandForm(plotName);
		return(s);
	}
	
	//retuns the elevation
	String getElevation(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getElevation(plotName);
		return(s);
	}
	
	//returns the elevation accuracy
	String getElevationAccuracy(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getElevationAccuracy(plotName);
		return(s);
	}
	
	//return the confidentiality reason -- not null
	String	getConfidentialityReason(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getConfidentialityReason(plotName);
		return(s);
	}
	
	//return the confidentiality status -- not null 0-6
	String getConfidentialityStatus(String plotName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getConfidentialityStatus(plotName);
		return(s);
	}
	

	
	/**
	 * method that prints the variables for a given plot to the 
	 * sys out
	 */
	private void printDBVariables(String pluginClass, String plotName)
	{
		System.out.println("PlotDataSource > getting info for: " + plotName );
		try
		{
			//rectify tis damn variable -- use a consistent name
			plotCode = plotName;
			
			//define the plugin
			PlotDataSource source = new PlotDataSource(pluginClass);
			//get all the variables for the given plot
			source.getPlot(plotName);
			
			System.out.println(" \n ---------------------project info------------------------");
			System.out.println("project name: " + source.projectName );
			System.out.println("project description: " + source.projectDescription );
			System.out.println("project contributors: " + source.projectContributors.toString() );
			System.out.println("project start date: " + source.projectStartDate );
			System.out.println("project stop date: " + source.projectStopDate );
			//the project contributor info goes here
			System.out.println("number of project contributors: " + source.projectContributors.size() );
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
				
				System.out.println("project contributor whole name: "+ wholeName);
				System.out.println("project contributor salutation: "+ salutation);
				System.out.println("project contributor given name: "+ givenName );
				System.out.println("project contributor middle name: "+ middleName);
				System.out.println("project contributor sur name: "+ surName);
				System.out.println("project contributor org name: "+ organizationName);
				System.out.println("project contributor contactInstructions: "+ contactInstructions);
				System.out.println("project contributor phoneNumber: "+ phoneNumber);
				System.out.println("project contributor orgPosition: "+ orgPosition);
				System.out.println("project contributor email: "+ email);
				System.out.println("project contributor deliveryPoint: "+ deliveryPoint);
				System.out.println("project contributor city: "+ city);
				System.out.println("project contributor administrativeArea: "+ administrativeArea);
				System.out.println("project contributor postalCode: "+ postalCode);
				System.out.println("project contributor country: "+ country);
				System.out.println("project contributor currentFlag: "+ currentFlag);
				System.out.println("project contributor addressStartDate: "+ addressStartDate+"\n");
			}
			
			
			System.out.println(" \n ----------------------plot site info-----------------------------");
			System.out.println("authorplotcode: " + source.plotCode );
			System.out.println("latitude: " + source.latitude );
			System.out.println("longitude: " + source.longitude );
			System.out.println("x coord: " + source.xCoord );
			System.out.println("y coord: " + source.yCoord );
			System.out.println("utm zone: " + source.utmZone );
			System.out.println("datum: " + source.datum );
			System.out.println("state: "+ source.state);
			System.out.println("country: "+ source.country);
			System.out.println("plot shape: " + source.plotShape );
			System.out.println("geology: " + source.surfGeo );
			System.out.println("topo position: " + source.topoPosition );
			System.out.println("hydrologic regime: " + source.hydrologicRegime );
			System.out.println("plot area(m): " + source.plotArea );
			System.out.println("elevation(m): " + source.elevation );
			System.out.println("elevation accuracy (%): " + source.elevationAccuracy );
			System.out.println("landform : " + source.landForm );
			System.out.println("author location: " + source.authorLocation);
			System.out.println("stand size: " + source.standSize );
			System.out.println("slope gradient: " + source.slopeGradient);
			System.out.println("slope aspect: " + source.slopeAspect);
			System.out.println("community name: " + source.communityName);
			System.out.println("number of named places: " + source.namedPlaces.size() );
			for (int i=0; i<source.namedPlaces.size(); i++)
			{
				String placeName = source.namedPlaces.elementAt(i).toString(); 
				String placeDescription = getPlaceDescription(placeName);
				String placeCode = getPlaceCode(placeName);
				String placeSystem = getPlaceSystem(placeName);
				String placeOwner = getPlaceOwner(placeName);
				System.out.println("place name: " + placeName);
				System.out.println("place description: " + placeDescription);
				System.out.println("place code: " + placeCode);
				System.out.println("place owner: " + placeOwner);
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
				System.out.println("strata: " + curstrata+" cover: "+cover+" base: "+base+" height: "+ height);
			}
			
			System.out.println(" \n ----------------------plant taxa info-----------------------------");
			System.out.println("number of unique plant names: " + source.plantTaxaNames.size() );
			//System.out.println("unique plant names: " + source.plantTaxaNames.toString() );
			
			//get the coverages for the plants for each strata
			for (int i=0; i<source.plantTaxaNames.size(); i++)
			{
				String name = source.plantTaxaNames.elementAt(i).toString();
				//System.out.println("calling plotName : " + plotCode);
				Vector strata = getTaxaStrataExistence(name, plotCode);
				System.out.println("name:  " + name  );
				for (int ii=0; ii<strata.size(); ii++)
				{
					String curStrata = strata.elementAt(ii).toString();
					System.out.println(" strata: " + curStrata );
					System.out.println(" cover: " + getTaxaStrataCover(name, 
						plotCode, curStrata ));
				}
			}
			
			
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	
	
/**
 * main method for testing --
 */
	public static void main(String[] args)
	{
		if (args.length == 2) 
		{
			//assume that the two args are 1] plugin and 2] plotName
			String plotName = args[1];
			String pluginClass = args[0];
			//should make sure that the plugin exists
			PlotDataSource source = new PlotDataSource(pluginClass);
			source.printDBVariables(pluginClass, plotName);
		}
		else
		{
			//use the plugin with the test data
			PlotDataSource source = new PlotDataSource("TestPlotSourcePlugin");
			source.printDBVariables("TestPlotSourcePlugin" , "test-plot");
		}
	}
	
}
	
