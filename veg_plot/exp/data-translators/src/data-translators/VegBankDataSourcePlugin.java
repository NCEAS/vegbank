import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


/**
 * plugin to allow access to plot data stored in the VegBank - plots 
 * database <br> <br>
 *
 *	
 *  '$Author: harris $' <br>
 *  '$Date: 2002-07-23 21:42:54 $' <br>
 * 	'$Revision: 1.17 $' <br>
 */
 
//public class VegBankDataSourcePlugin
public class VegBankDataSourcePlugin implements PlotDataSourceInterface
{
	//private String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
	//private String dbUrl = "jdbc:odbc:vegbank_access";
	
	//variables below are for VEGBANK on postgresql
	private String driver = "org.postgresql.Driver";
	private String dbUrl = "jdbc:postgresql://vegbank.nceas.ucsb.edu/plots_dev";
	private String dbUser = "datauser";
	public Connection con = null;
	
	/**
	 * constructor -- assume that if this constructor is being called then the 
	 * default database is to be accessed.  There may be times when the calling
	 * class wants to specify the database type -- like the class that supports
	 * access to the Access VegBank access version where the class wants to 
	 * specify the database type as an access database type
	 */
	public VegBankDataSourcePlugin()
	{
		//use the default database -- postgresql
		this("postgresql");
	}
	
	/**
	 * constructor for this class.  The input parameter is the database type
	 * so that the class can determine the type of database parameters to 
	 * use
	 * @param databaseType -- includes: postgresql, oracle, msaccess -- for now 
	 */
	public VegBankDataSourcePlugin(String databaseType)
	{
		System.out.println("VegBankDataSourcePlugin > using database type: " + databaseType);
		try 
		{
			if (databaseType.equals("msaccess"))
			{
				driver = "sun.jdbc.odbc.JdbcOdbcDriver";
				dbUrl = "jdbc:odbc:vegbank_access";
			}
			//else use the default database settings
			con = this.getConnection();
			DatabaseMetaData dma = con.getMetaData();
			System.out.println("VegBankDataSourcePlugin > Connected to " + dma.getURL() );
			System.out.println("VegBankDataSourcePlugin > Driver       " + dma.getDriverName() );
			System.out.println("VegBankDataSourcePlugin > Version      " + dma.getDriverVersion() );
			System.out.println("VegBankDataSourcePlugin > Catalog      " + con.getCatalog() );
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
	 * method that returns the accession number associated with a plot id
	 * the input plot id is the unique identifier of the plot as used by 
	 * the RDBMS
	 * @param plotId -- the RDBMS unique plot ID
	 */
	public String getAccessionValue(String plotId)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  accession_number "
			+" from PLOT where PLOT_ID = " + plotId );
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
	 * method to return the taxa code from a data source using as input
	 * the scientific plant name -- or the plant name that comes from
	 * the 'getPlantTaxaNames' method
	 *
	 * @param plantName -- the scientific plantName
	 */
	public String getPlantTaxonCode(String plantName)
	{
		String code = "";
		StringBuffer sb = new StringBuffer();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			sb.append("select distinct(AUTHORPLANTCODE) from PLOTSPECIESSUM where AUTHORNAMEID LIKE '"+plantName+"'"); 
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) 
			{
				code = rs.getString(1);
			}
		}
	//	catch (SQLException ex) 
	//	{
	//		this.handleSQLException( ex );
	//	}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("Exception: " + ex );
			System.out.println("sql: " + sb.toString() );
			ex.printStackTrace();
		}
	 	return(code);
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
		String s = null;
		StringBuffer sb = new StringBuffer();
		Statement stmt = null;
		try 
		{
			sb.append("select OBSSTARTDATE from OBSERVATION where PLOT_ID = " + plotName);
			stmt = con.createStatement();
			System.out.println("VegBankDataSourcePlugin > query: " + sb.toString() );
			ResultSet rs = stmt.executeQuery(sb.toString());
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
 	 * returns the stop date for the plot observation
 	 * @param plotName -- the plot
 	 */
 	public String getObsStopDate(String plotName)
 	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select obsenddate "
			+" from OBSERVATION where PLOT_ID = " + plotName );
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
 	 * returns the soil depth of a plot
 	 * @param plotName -- the plot
 	 */
 	public String getSoilDepth(String plotName) 
 	{
 		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select soilDepth "
			+" from OBSERVATION where PLOT_ID = " + plotName );
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
	
	
	//START
	/**
	 * utlility method for querying the observation table and returning 
	 * each of the elements as a string
	 * @param plotName -- the name of the plot 
	 * @param elementname -- the attribute name of the desired attribute 
	 * @return elementValue -- the value of the desired attribute 
	 */
	 private String getObservationElement(String plotName, String elementName )
	 {
		String s = null;
		Statement stmt = null;
		StringBuffer sb = new StringBuffer();
		try 
		{
			stmt = con.createStatement();
			
			sb.append("select ");
			sb.append(elementName);
			sb.append(" from OBSERVATION where PLOT_ID = ");
			sb.append(plotName);
			
			ResultSet rs = stmt.executeQuery(  sb.toString() );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			System.out.println("query > " + sb.toString() );
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	 }
	 
	 
	/**
	*/
	public String getObsDateAccuracy(String plotName)
	{
		String s = this.getObservationElement(plotName, "dateaccuracy");
		return(s);
	}
	 
	/**
	*/
	public Hashtable getCoverMethod(String plotName)
	{
		Hashtable  s = new Hashtable();
		return(s);
	}
	
	/**
	 */
	public Hashtable getStratumMethod(String plotName)
	{
		Hashtable s = new Hashtable();
		return(s);
	}
	
	/**
	 */
	public String getStemSizeLimit(String plotName)
	{
		String s = "";
		return(s);
	}

	/**
	 */
	public String getMethodNarrative(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getTaxonObservationArea(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getCoverDispersion(String plotName )
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public boolean getAutoTaxonCover(String plantName)
	{
		boolean s = true;
		return(s);
	}
	
	/**
	 */
	public String getStemObservationArea(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getStemSampleMethod(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getOriginalData(String plotName )
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getEffortLevel( String plotName )
	{
		String s = "";
		return(s);
	}
//END


	/**
 	 * returns true if the plot is a permanent plot or false if not
 	 * @param plotName -- the plot
 	 */
 	 public boolean isPlotPermanent(String plotName)
 	 {
		boolean b = true; //default is true
 	 	String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select permanence "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
				 if ( s.toUpperCase().equals("false") )
				 {
					 b = false;
				 }
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(b);
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
	 * @deprecated -- this element has been removed from the database -- 20020717
 	 */
 	public String getSoilTaxonSource(String plotName)
 	{
 		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select SOILTAXONSOURCE from PLOT where PLOT_ID = "+plotName);
			ResultSet rs = stmt.executeQuery( sb.toString() );			
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
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
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select  projectname from PROJECT where PROJECT_ID = (");
			sb.append(" select project_id from PLOT where PLOT_ID = "+plotName+")");
			ResultSet rs = stmt.executeQuery( sb.toString() );			
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//returns the project description
	public Vector getProjectContributors(String plotName)
	{
		Vector contributors = new Vector();
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select givenName, surName from PARTY where PARTY_ID = (");
			sb.append(" select PARTY_ID from PROJECTCONTRIBUTOR where PROJECT_ID = (");
			sb.append(" select PROJECT_ID from PLOT where PLOT_ID = "+plotName+"))");
			ResultSet rs = stmt.executeQuery( sb.toString() );			
			while (rs.next()) 
			{
				 s = rs.getString(1);
				 s = s+" "+rs.getString(2);
				 contributors.addElement(s);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(contributors);
	}
	
	//retuns the person's salutation based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSalutation(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's given based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorGivenName(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's middle name based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorMiddleName(String contributorWholeName)
	{
		if (contributorWholeName.startsWith("Bob"))
			return("");
		else
			return("");
	}
	//retuns the person's surName based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSurName(String contributorWholeName)
	{
			return("");
	}
	//retuns the name of an org. that a person is associated with based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorOrganizationName(String contributorWholeName)
	{
			return("");
	}
	
	//retuns the person's contactinstructions based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's cellPhone based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCellPhoneNumber(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's fax phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorFaxPhoneNumber(String contributorWholeName)
	{
			return("");
	}
	//retuns the party's position within an organization based on their full 
	// name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorOrgPosition(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's email based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorEmailAddress(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's address line based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorDeliveryPoint(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's city based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCity(String contributorWholeName)
	{
			return("");
	}
	
	//returns the administrative area, or state that a party is from
	public String getProjectContributorAdministrativeArea(String contributorWholeName)
	{
			return("");
	}
	//retuns the zip code for a party
	public String getProjectContributorPostalCode(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's country based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCountry(String contributorWholeName)
	{
			return("");
	}
	//retuns a boolean 'true' if it is a party's current address based on their 
	//full name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCurrentFlag(String contributorWholeName)
	{
		return("");
	}
	//retuns the date that the address became current for a party based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorAddressStartDate(String contributorWholeName)
	{
			return("");
	}
	
	//returns the start date for the project
	public String getProjectStartDate(String plotName )
	{
		return("");
	}
	
	//returns the stop date for the project
	public String getProjectStopDate(String plotName )
	{
		return("");
	}
	
	//returns the placeNames each name can be used to retrieve
	//other information about a place
	public Vector getPlaceNames(String plotName)
	{
		Vector placeNames = new Vector();
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select  placeName from NAMEDPLACE where NAMEDPLACE_ID = (");
			sb.append(" select NAMEDPLACE_ID from PLACE where PLOT_ID = "+plotName+")");
			ResultSet rs = stmt.executeQuery( sb.toString() );			
			while (rs.next()) 
			{
				 s = rs.getString(1);
				 placeNames.addElement(s);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
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
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  projectdescription  "
			+" from PROJECT where PROJECT_ID = 0 "  );			
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	
	/**
	 * private method that returns the project ids in the 
	 * form of a vector for a given plot
	 * 
	 */
	//private Vector 
	
	
	
	//returns the plot code for the current plot
	public String getPlotCode(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  authorPlotCode "
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
	
	//returns the easting 
	public String getXCoord(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  authore "
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
	
	//returns the northing
	public String getYCoord(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  authorn "
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
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  authorzone "
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
	
	public String getDatumType(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select AUTHORDATUM from PLOT where PLOT_ID = " +plotName);
			ResultSet rs = stmt.executeQuery( sb.toString() );
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	
	//returns the plot shape
	public String getPlotShape(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  shape "
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
	
	//returns the plot area -n m^2
	public String getPlotArea(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  area "
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
	
	//returns the state for the current plot
	public String getState(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		System.out.println("VegBankDataSourcePlugin > getState for plot: " + plotName);
		try 
		{
			stmt = con.createStatement();
			String query = "select state from PLOT where PLOT_ID = " + plotName;
			System.out.println("VegBankDataSourcePlugin > query: " + query);
			ResultSet rs = stmt.executeQuery(query);				
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
		System.out.println("VegBankDataSourcePlugin > state: " + s);
		return(s);
	}
	
	//returns the state for the current plot
	public String getCommunityName(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select COMMNAME from COMMCLASS where OBSERVATION_ID = ");
			sb.append(" ( select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+")");
			ResultSet rs = stmt.executeQuery( sb.toString() );			
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	
	/**
	 * returns the community code for the named plot
	 */
	public String getCommunityCode(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select COMMCODE from COMMCLASS where OBSERVATION_ID = ");
			sb.append(" ( select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+")");
			ResultSet rs = stmt.executeQuery( sb.toString() );			
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
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
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  topoposition "
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
	
	//returns the hydrologic regime
	public String getHydrologicRegime(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  hydrologicregime "
			+" from observation where  PLOT_ID = " + plotName );
							
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
				//remove the apersand so that the xml can be parsed
				 String buf  = rs.getString(1);
				 if ( buf != null)
				 {
				 	s = buf.replace('&', 'a');
				 }
			}
		}
		catch (java.lang.Exception ex) 
		{   
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
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  slopeaspect "
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
	
	//returns the slope gradient
	public String getSlopeGradient(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  slopegradient "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//returns the size of the stand -- extensive etc..
	public String getStandSize(String plotName)
	{
		return("extensive");
	}
	
	//returns the location as described by the author
	public String getAuthorLocation(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select authorlocation "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	//returns the landForm
	public String getLandForm(String plotName)
	{
		return("aluvial fan");
	}
	
	//retuns the elevation
	public String getElevation(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select elevation "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//returns the elevation accuracy
	public String getElevationAccuracy(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select elevationAccuracy "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//return the confidentiality reason -- not null
	public String	getConfidentialityReason(String plotName)
	{  
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select confidentialityReason "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//return the confidentiality status -- not null 
	public String getConfidentialityStatus(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select confidentialitystatus "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
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
			ResultSet rs = stmt.executeQuery("select OBSERVATION_ID from OBSERVATION "
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
	 * @param plantName 
	 * @param plotName
	 */
	 public Vector getTaxaStrataExistence(String plantName, String plotName)
	 {
		 StringBuffer sb = new StringBuffer();
		 Vector v = new Vector();
		try
		{
			sb.append( " SELECT distinct(STRATUMTYPE) FROM PLOTSPECIESSUM where  " );
			sb.append(" PLOT_ID = " + plotName +" and AUTHORNAMEID LIKE '"+plantName+"'");
			System.out.println("sql: " + sb.toString() );
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery( sb.toString() );
			while (rs.next()) 
			{
				String buf = rs.getString(1);
				v.addElement(buf);		
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
		}
		 return(v);
	 }
	 
	 
	 /**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String strataName)
	 {
		 String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMCOVER from STRATUM where STRATUMNAME like '"+strataName+"' and OBSERVATION_ID = (");
				sb.append(" select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+")");
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("Exception: " + ex );
				ex.printStackTrace();
			}
			return(s);
	 }
	 
	 /** 
	  * method the returns the base height of a strata based on the 
		* name of that starta and the plot for which that strata is included
		*/
		public String getStrataBase(String plotName, String strataName)
		{
			String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMBASE from STRATUM where STRATUMNAME like '"+strataName+"' and OBSERVATION_ID = (");
				sb.append(" select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+")");
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("Exception: " + ex );
				ex.printStackTrace();
			}
			return(s);
		}
		
		/**
		 * method that returns the upper height of a starata based on 
		 * the name of the strata and the plot inwhich the strata exists
		 */
		public String getStrataHeight(String plotName, String strataName)
		{
			String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMHEIGHT from STRATUM where STRATUMNAME like '"+strataName+"' and OBSERVATION_ID = (");
				sb.append(" select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+")");
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("Exception: " + ex );
				ex.printStackTrace();
			}
			return(s);
		}
		
	 
	 /**
	 * method that returns the strata cover for a given plant, strata, and 
	 * plot
	 */
	 public String getTaxaStrataCover(String plantName, String plotName, String stratum)
	 {
		 String cover = "";
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 	// SELECT THE COVER VALUE DIRECTLY FROM THE DENORMALIZED TABLES
				sb.append("SELECT PERCENTCOVER from PLOTSPECIESSUM WHERE ");
				sb.append(" PLOT_ID = "+plotName+" and STRATUMTYPE like '"+stratum+"' and ");
				sb.append(" AUTHORNAMEID like '"+plantName+"'");
				//System.out.println("sql: " + sb.toString() );
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery( sb.toString() );
				while (rs.next()) 
				{
					cover = rs.getString(1);
				}
		 }
		 catch (java.lang.Exception ex) 
			{   
				System.out.println("Exception: " + ex );
				ex.printStackTrace();
			}
			return(cover);
	 }
	 
	/**
	 * method that retuns the cummulative cover accoss all strata for a given 
	 * plant taxa in a given plot
	 */
	public String getCummulativeStrataCover( String plantName, String plotName )
	{
		System.out.println("VegBankDataSourcePlugin > looking cum. strata cover for plot: " + plotName);
		String s = null;
		Statement stmt = null;
		StringBuffer sb = new StringBuffer();
		try 
		{
			stmt = con.createStatement();
			sb.append("select TAXONCOVER from TAXONOBSERVATION where OBSERVATION_ID = ");
			sb.append(" (select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+" ) ");
			sb.append(" and CHEATPLANTNAME like '"+plantName+"' ");
			ResultSet rs = stmt.executeQuery( sb.toString() );
			while (rs.next()) 
			{
				 s = rs.getString(1);
				 // IF WE GET A NULL VALUE THEN TAKE THE MAX OF THE SPECIES
				 if ( s == null )
				 {
					 sb = new StringBuffer();
					 sb.append(" select max(PERCENTCOVER) from PLOTSPECIESSUM where ");
					 sb.append(" AUTHORNAMEID like '"+plantName+"' and PLOT_ID = "+ plotName  );
					 ResultSet rs1 = stmt.executeQuery( sb.toString() );
					 while (rs1.next()) 
					 {
						 s = rs1.getString(1);
						 if (s == null )
						 {
							 s = "";
						 }
					 }
				 }
			}
			stmt.close();
			rs.close();
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("Exception: " + ex );
			ex.printStackTrace();
			System.out.println("VegBankDataSource > query which failed: " + sb.toString() );
		}
		return(s);
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
				System.out.println ("VegBankDataSourcePlugin > ErrorCode: " + ex.getErrorCode () );
				System.out.println ("VegBankDataSourcePlugin > SQLState:  " + ex.getSQLState () );
				System.out.println ("VegBankDataSourcePlugin > Message:   " + ex.getMessage () );
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
		//	VegBankDataSourcePlugin db = new VegBankDataSourcePlugin("postgresql");
		//	System.out.println(" VegBankDataSourcePlugin > getting info for: " + plotName );
		}
		else
		{
		//	VegBankDataSourcePlugin db = new VegBankDataSourcePlugin("postgresql");
		//	System.out.println( db.getPlotCodes().toString() );
		//	System.out.println( db.getPlotIDs().toString() );
		}
	}
	
	
}
