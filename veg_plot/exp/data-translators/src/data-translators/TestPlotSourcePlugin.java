import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


/**
 * Test plugin class for the PlotDataSource Interface this class contains a 
 * plot called 'test-plot'
 *
 *  Authors:
 *  Release:
 *	
 *  '$Author: harris $'
 *  '$Date: 2002-03-27 22:52:41 $'
 * 	'$Revision: 1.5 $'
 */
public class TestPlotSourcePlugin implements PlotDataSourceInterface
{
	private String dbUrl = "jdbc:odbc:test_access"; 
	private Connection con = null;
	
	public String projectName = "test project";
	public String projectDescription = "to collect plot data from california";
	public String projectContributor = null;
	
	public String communityName = "ice plant community";
	public String communityCode = "CEGL_ICE_PLANT";
	public String	xCoord= null;
	public String yCoord= null;
	public String utmZone = null;
	public String date = null;
	public String elevation = null;
	public String slope = null;
	public String aspect = null;
	public String surfGeo = null;
	public String placeName = null;
	
	public String state = "null";
	public String plotCode = "null";
	public String plotShape = "null";
	public String test = "null";
	
	public String authorObsCode = "null";
	public String soilDepth = "null";
	
	public String t1Height = "n/a";
	public String t1Cover = "n/a";
	public String t2Height = "n/a";
	public String t2Cover= "n/a";
	public String t3Height= "n/a";
	public String t3Cover = "n/a";
	public String s1Height = "n/a";
	public String	s1Cover = "n/a";
	
	public int strataNumber = 4; //always 4 strata
	public Vector strataNames = new Vector();
	public Vector strataMinHeight = new Vector();
	public Vector strataMaxHeight = new Vector();
	public Vector strataCover = new Vector();
	
	public int uniquePlantNameNumber;
	public String plotArea = null;
	public String slopeAspect = null;
	public String slopeGradient = null;
	public String hydrologicRegime = null;
	public String topoPosition = null;
	
	//this is used by the get location method
	private String locationCode = null;
	
	//use vectors to store those data that may occurr 
	//multiple times within a given plant
	Vector scientificNames = new Vector();
	
	/**
	 * constructor for this class -- will update the public variables
	 * that will be used to generate the vegbank xml document
	 */
	public TestPlotSourcePlugin()
	{
		try 
		{
			System.out.println("TestPlotSourcePlugin init");
		}
		catch (Exception e) 
		{
			System.out.println( e.getMessage() );
		}
	}
	


	/**
 	 * returns the soil drainage of a plot
 	 * @param plotName -- the plot
 	 */
 	public String getSoilDrainage(String plotName)
 	{
 		return("");
 	}
 	
 	/**
 	 * returns the author's observation code
 	 * @param plotName -- the plot
 	 */
 	public String getAuthorObsCode(String plotName)
 	{
 		return("");
 	}
 	
 	/**
 	 * returns the start date for the plot observation
 	 * @param plotName -- the plot
 	 */
 	public String getObsStartDate(String plotName)
 	{
 		return("");
 	}
 	
 	/**
 	 * returns the stop date for the plot observation
 	 * @param plotName -- the plot
 	 */
 	public String getObsStopDate(String plotName)
 	{
 		return("");
 	}



	/**
 	 * returns the soil depth of a plot
 	 * @param plotName -- the plot
	 */
 	public String getSoilDepth(String plotName) 
 	{
 		return("");
 	}
 
 	/**
 	 * returns true if the plot is a permanent plot or false if not
 	 * @param plotName -- the plot
 	 */
 	 public boolean isPlotPermanent(String plotName)
 	 {
 	 	return(true);
 	 }
  
  /**
 	* returns the soil taxon for the plot -- this is the USDA class
 	* heirarchy (eg., Order, Group, Family, Series etc..)
 	* @param plotName -- the plot
 	*/
 	public String getSoilTaxon(String plotName)
 	{
 		return("");
 	}
 	
 	/**
 	 * returns how the soil taxon was determined (eg., field observation
 	 * mapping, other ...)
 	 * @param plotName -- the plot
 	 */
 	public String getSoilTaxonSource(String plotName)
 	{
 		return("");
 	}





	
	//returns all the plots stored in the access file
	public Vector getPlotNames()
	{
		Vector v = new Vector();
		v.addElement("test-plot");
		return(v);
	}
	
	//returns the project name 
	public String getProjectName(String plotName)
	{ return(projectName); }
	
	//returns the project description
	public Vector getProjectContributors(String plotName)
	{
		Vector v = new Vector();
		v.addElement("Bob Peet");
		v.addElement("John Harris");
		return(v);
	}
	
	//retuns the person's salutation based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSalutation(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("Dr.");
		else
			return("Mr.");
	}
	//retuns the person's given based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorGivenName(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("Robert");
		else
			return("John");
	}
	//retuns the person's middle name based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorMiddleName(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("");
		else
			return("Howland");
	}
	//retuns the person's surName based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSurName(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("Peet");
		else
			return("Harris");
	}
	//retuns the name of an org. that a person is associated with based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorOrganizationName(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("University of North Carolina Chapel Hill");
		else
			return("University of California Santa Barbara");
	}
	
	//retuns the person's contactinstructions based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("email at peet@unc.edu");
		else
			return("email at harris@nceas.ucsb.edu");
	}
	//retuns the person's phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("919 945-0788");
		else
			return("805 123-4523");
	}
	//retuns the person's cellPhone based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCellPhoneNumber(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("949 222-4555");
		else
			return("805 222-3434");
	}
	//retuns the person's fax phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorFaxPhoneNumber(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("949 456-7856");
		else
			return("805 456-6576");
	}
	//retuns the party's position within an organization based on their full 
	// name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorOrgPosition(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("Professor");
		else
			return("Software Developer");
	}
	//retuns the person's email based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorEmailAddress(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("peet@unc.edu");
		else
			return("harris@nceas.ucsb.edu");
	}
	//retuns the person's address line based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorDeliveryPoint(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("Department of Biology, CB#3280");
		else
			return("735 State Street, Suite 300");
	}
	//retuns the person's city based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCity(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("Chapel Hill");
		else
			return("Santa Barbara");
	}
	
	//returns the administrative area, or state that a party is from
	public String getProjectContributorAdministrativeArea(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("NC");
		else
			return("CA");
	}
	//retuns the zip code for a party
	public String getProjectContributorPostalCode(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("27599-3280");
		else
			return("93101");
	}
	//retuns the person's country based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCountry(String contributorWholeName)
	{
			return("USA");
	}
	//retuns a boolean 'true' if it is a party's current address based on their 
	//full name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCurrentFlag(String contributorWholeName)
	{
		return("true");
	}
	//retuns the date that the address became current for a party based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorAddressStartDate(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("01-JAN-1977");
		else
			return("01-JAN-2000");
	}
	
	//returns the start date for the project
	public String getProjectStartDate(String plotName )
	{
		return("01-JAN-1999");
	}
	
	//returns the stop date for the project
	public String getProjectStopDate(String plotName )
	{
		return("01-JAN-2002");
	}
	
	//returns the placeNames each name can be used to retrieve
	//other information about a place
	public Vector getPlaceNames(String plotName)
	{
		Vector placeNames = new Vector();
		placeNames.addElement("Marina Park");
		placeNames.addElement("Ventura Harbor Area");
		return(placeNames);
	}
	//retuns a description of a place based on a placeName
	public String getPlaceDescription(String placeName)
	{
		if ( placeName.startsWith("Marina") )
			return("marina park by the sunken ship imitation");
		else if (placeName.startsWith("Ventura"))
			return("the ventura harbor on the north side");
		else return("");
	}
	//returns a placeCode based on a placeName
	public String getPlaceCode(String placeName)
	{
		if ( placeName.startsWith("Marina") )
			return("MarinePrk");
		else if (placeName.startsWith("Ventura"))
			return("VenHbr");
		else return("");
	}
	//returns a place system based on a placeName
	public String getPlaceSystem(String placeName)
	{
		return("unknown system");
	}
	//returns the owner of a place based on the name of a place
	public String getPlaceOwner(String placeName)
	{
		if ( placeName.startsWith("Marina") )
			return("Ventura county");
		else if (placeName.startsWith("Ventura"))
			return("State of California");
		else return("");
	}
	
	
	//returns the project description
	public String getProjectDescription(String plotName )
	{ return(projectDescription); }
	
	
	
	
	
	//returns the plot code for the current plot
	public String getPlotCode(String plotName)
	{ return(plotName); }
	
	//returns the easting 
	public String getXCoord(String plotName)
	{ return("325307"); }
	
	//returns the northing
	public String getYCoord(String plotName)
	{ return("3884137"); }
	
	//retuns the latitude -- not null
	public String getLatitude(String plotName)
	{ return("38"); }
	
	//returns the longitude -- not null
	public String getLongitude(String plotName)
	{ return("-118"); }
	
	//returns the geographic zone
	public String getUTMZone(String plotName)
	{ return("17"); }
	
	//returns the plot shape
	public String getPlotShape(String plotName)
	{ return("rectangular"); }
	
	//returns the plot area -n m^2
	public String getPlotArea(String plotName)
	{ return("10"); }
	
	//returns the state for the current plot
	public String getState(String plotName)
	{ return("CA"); }
	
	//returns the state for the current plot
	public String getCommunityName(String plotName)
	{ return("ice-plant community"); }
	
	
	/**
	 * returns the community code for the named plot
	 */
	public String getCommunityCode(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 * returns the community level of the framework for the named plot
	 */
	public String getCommunityLevel(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 * returns the community framework for the named plot
	 */
	public String getCommunityFramework(String plotName)
	{
		String s = "";
		return(s);
	}
	
	
	
	//retuns the topo position
	public String getTopoPosition(String plotName)
	{ return("toe"); }
	
	//returns the hydrologic regime
	public String getHydrologicRegime(String plotName)
	{ return("moist"); }
	
	//returns the surface geology
	public String getSurfGeo(String plotName)
	{ return("metamorphic"); }
	
	//returns the country
	public String getCountry(String plotName)
	{ return("USA"); }
	
	//returns the slope aspect
	public String getSlopeAspect(String plotName)
	{ return("320"); }
	
	//returns the slope gradient
	public String getSlopeGradient(String plotName)
	{ return("12"); }
	
	//returns the size of the stand -- extensive etc..
	public String getStandSize(String plotName)
	{
		return("extensive");
	}
	//returns the location as described by the author
	public String getAuthorLocation(String plotName)
	{
		return("by the ventura river mouth");
	}
	//returns the landForm
	public String getLandForm(String plotName)
	{
		return("aluvial fan");
	}
	//retuns the elevation
	public String getElevation(String plotName)
	{
		return("10");
	}
	//returns the elevation accuracy
	public String getElevationAccuracy(String plotName)
	{
		return("20"); //20%
	}
	
	//return the confidentiality reason -- not null
	public String	getConfidentialityReason(String plotName)
	{  return("no real reason");  }
	
	//return the confidentiality status -- not null 
	public String getConfidentialityStatus(String plotName)
	{ return("0"); }
	
	//retuns the unique names of all the strata in a given plot
	public Vector getUniqueStrataNames(String plotName)
	{
		Vector v = new Vector();
		v.addElement("tree");
		v.addElement("shrub");
		v.addElement("herb");
		return(v);
	}
	
	
	
	
	/**
	 * method that takes a plot code and populates the public varaibles with
	 * data related to that specific plot
	 *
	 * @param plotName -- the name of a plot for which to retrieve data for
	 */
	 public void getPlotData(String plotName)
	 {
		 //there is only one plot here  so this method should be blank
	 }
	 
	
		
		/**
		 * method that returns a vector with the unique plant 
		 * taxa names for a given plot -- this method is defined
		 * in the plugin interface
		 */
		 public Vector getPlantTaxaNames(String plotName)
		 {
			 Vector v = new Vector();
			 v.addElement("Viola rotundifola");
			 v.addElement("Trautvetteria caroliniensis");
			 v.addElement("Arsaema triphyllum");
			 v.addElement("Polygonatum");
			 v.addElement("Aconitum reclinatum");
			 return( v );
		 }
		 
	
	 
		
	/**
	 * method that returns a vector of plot codes from the target
	 * tnc plots database
	 */
	public Vector getPlotCodes()
	{
		Vector v = new Vector();
		v.addElement("test-plot");
		return(v);
}


	/**
	 * method that returns the strata in which the input plant
	 * exists within the input plot
	 */
	 public Vector getTaxaStrataExistence(String plantName, String plotName)
	 {
		 //kinda randomize the results here so that all the plant are not 
		 //all in the same strata
		 Vector v = new Vector();
		 if (plantName.startsWith("A") )
		 {
			 v.addElement("shrub");
		 }
		 else
		 {
			 v.addElement("tree");
			 v.addElement("shrub");
			 v.addElement("herb");
		 }
		 return(v);
	 }
	 
	 
	 /**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String strataName)
	 {
		 return("100");
	 }
	 
	 /** 
	  * method the returns the base height of a strata based on the 
		* name of that starta and the plot for which that strata is included
		*/
		public String getStrataBase(String plotName, String strataName)
		{
			return("12");
		}
		
		/**
		 * method that returns the upper height of a starata based on 
		 * the name of the strata and the plot inwhich the strata exists
		 */
		public String getStrataHeight(String plotName, String strataName)
		{
			return("100");
		}
		
	 
	 /**
	 * method that returns the strata cover for a given plant, strata, and 
	 * plot
	 */
	 public String getTaxaStrataCover(String plantName, String plotName, String stratum)
	 {
		 if (plantName.startsWith("T"))
		 {
			return("10");
		 }
		 else 
		 {
			 return("12");
		 }
	 }
	 
	/**
	 * method that retuns the cummulative cover accoss all strata for a given 
	 * plant taxa in a given plot
	 */
	public String getCummulativeStrataCover( String plantName, String plotName )
	{
		return("7");
	}



}
