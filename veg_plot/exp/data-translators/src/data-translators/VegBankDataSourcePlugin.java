import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


/**
 * plugin to allow access to plot data stored in the VegBank - plots 
 * database 
 *
 *  Release: 
 *	
 *  '$Author: harris $'
 *  '$Date: 2002-01-17 22:27:02 $'
 * 	'$Revision: 1.3 $'
 */
 
//public class VegBankDataSourcePlugin
public class VegBankDataSourcePlugin implements PlotDataSourceInterface
{
	
	private String driver = "org.postgresql.Driver";
	private String dbUrl = "jdbc:postgresql://beta.nceas.ucsb.edu/vegbank";
	private String dbUser = "datauser";
	private Connection con = null;

	
	/**
	 * constructor for this class
	 */
	public VegBankDataSourcePlugin() 
	{
		try 
		{
			con = this.getConnection();
			// Get the DatabaseMetaData object and display
			// some information about the connection
			DatabaseMetaData dma = con.getMetaData();
			
			System.out.println("Connected to " + dma.getURL() );
			System.out.println("Driver       " + dma.getDriverName() );
			System.out.println("Version      " + dma.getDriverVersion() );
			System.out.println("Catalog      " + con.getCatalog() );
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   // All other types of exceptions
			System.out.println("Exception: " + ex + "<BR>");
			ex.printStackTrace();
		}
	}
	
	  
	/**
	 * utility method that will return a database connection for use with the 
	 * database
	 */
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName(this.driver);
			//the vegbank database
			c = DriverManager.getConnection(this.dbUrl, this.dbUser, "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception : " +e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
 

	
	/**
	 * method that returns a vector of PLOT ID'S (Plot table Primary keys) 
	 * from the target VegBank Database
	 */
	public Vector getPlotIDs()
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct PLOT_ID from PLOT");
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
	}
	
	
	/**
	 * method that returns a vector of plot codes which are the 
	 * VegBank Accession Numbers, for now it will be the authorPlotCode
	 * 
	 */
	public Vector getPlotCodes()
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct authorPlotCode from PLOT");
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
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
	{ return("testproject"); }
	
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
	{ return("projectDescription"); }
	
	
	
	
	
	//returns the plot code for the current plot
	public String getPlotCode(String plotName)
	{ return(plotName); }
	
	//returns the easting 
	public String getXCoord(String plotName)
	{ return("325307"); }
	
	//returns the northing
	public String getYCoord(String plotName)
	{ return("3884137"); }
	
	/**
	 * method that retuns the latitude for an input plotid
	 * @param plotName -- the VegBank PLOTID
	 * @return latitude -- the validated latitude of that plot
	 */
	public String getLatitude(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  LATITUDE "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
	return(s);
	}
	
	/**
	 * method that retuns the latitude for an input plotid
	 * @param plotName -- the VegBank PLOTID
	 * @return longitude -- the validated latitude of that plot
	 */
	public String getLongitude(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  LONGITUDE "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	
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
	
	//retuns the topo position
	public String getTopoPosition(String plotName)
	{ return("toe"); }
	
	//returns the hydrologic regime
	public String getHydrologicRegime(String plotName)
	{ return("moist"); }
	
	/**
	 * method that retuns the latitude for an input plotid
	 * @param plotName -- the VegBank PLOTID
	 * @return geology -- the validated surface geology of that plot
	 */
	public String getSurfGeo(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  GEOLOGY "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
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
	
	/**
	 * method that retuns the unique names for a plotID
	 * @param plotName -- the VegBank PLOTID
	 * @return v -- a vector containg the unique strata for that plot
	 *
	 */
	public Vector getUniqueStrataNames(String plotName)
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			Vector observationIDs = this.getObservationIDs( plotName );
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct STRATUMNAME "
			+" from STRATUM where OBSERVATION_ID = " + 
			(String)observationIDs.elementAt(0) );
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
	}
	
	
	/**
	 * method that returns the observation ids that are children
	 * of a plot id
	 * @param plotName -- the VegBank plotId
	 * @return v -- a vector containg all the observtaions for that specific
	 *		plot id provided as input
	 */
	 private  Vector getObservationIDs(String plotName)
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select OBSERVATION_ID  from OBSERVATION "
			+"where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
			
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
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
				Statement stmt = null;
				try 
				{
					Vector observationIDs = this.getObservationIDs( plotName );
					stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("select distinct CHEATPLANTNAME "
					+" from TAXONOBSERVATION where OBSERVATION_ID = " + 
					(String)observationIDs.elementAt(0) );
							
					while (rs.next()) 
					{
						String s = rs.getString(1);
						v.addElement(s);
					}
				}
				catch (SQLException ex) 
				{
					this.handleSQLException( ex );
				}
				catch (java.lang.Exception ex) 
				{   
					// All other types of exceptions
					System.out.println("Exception: " + ex );
					ex.printStackTrace();
				}
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

	
	
	/**
	 * method for handling the sql exceptions thrown 
	 */
	private void handleSQLException(SQLException ex)
	{
		try
		{
			while (ex != null)
			{
				System.out.println ("ErrorCode: " + ex.getErrorCode () );
				System.out.println ("SQLState:  " + ex.getSQLState () );
				System.out.println ("Message:   " + ex.getMessage () );
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
		catch (java.lang.Exception x) 
		{   
			// All other types of exceptions
			System.out.println("Exception: " + x + "<BR>");
			x.printStackTrace();
		}
	}
	
	
	
	
	
/**
 * main method for testing --
 */
	public static void main(String[] args)
	{
		if (args.length == 1) 
		{
			String plotName = args[0];
			VegBankDataSourcePlugin db = new VegBankDataSourcePlugin();
			System.out.println(" getting info for: " + plotName );
		}
		else
		{
			VegBankDataSourcePlugin db = new VegBankDataSourcePlugin();
			System.out.println( db.getPlotCodes().toString() );
			System.out.println( db.getPlotIDs().toString() );
		}
	}
	
	
}
