package org.vegbank.plots.datasource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * This class represents the TNC Plots DB and will be used as a
 * source for the transformation of the TNC plots database into 
 * the native XML format used by the vegbank system -- this class 
 * will be one of a number of plugins for access to a data source
 * so the many of the varaibles and methods will be general and 
 * consistent with many other plugins <br> <br>
 *
 *	
 *  '$Author: farrell $' <br>
 *  '$Date: 2003-06-03 21:41:33 $' <br>
 * 	'$Revision: 1.4 $' <br>
 */
public class TNCPlotsDB implements PlotDataSourceInterface
//public class TNCPlotsDB
{
	private String dbUrl = "jdbc:odbc:test_access"; 
	private Connection con = null;
	public String projectName = "null";
	public String projectDescription = "null";
	public String projectContributor = null;
	public String communityName = null;
	public String communityCode = null;
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
  	public Vector observationContributors = new Vector();
  	// Rules for assigning role
  	public static final String roleFirst = "Primary Observer";
  	public static final String roleRest = "Co-Observer";  
  	public static final int roleFirstIndex = 0;
	
	//this is used by the get location method
	private String locationCode = null;
	
	//use vectors to store those data that may occurr 
	//multiple times within a given plant
	Vector scientificNames = new Vector();
	
	/**
	 * constructor for this class -- will update the public variables
	 * that will be used to generate the vegbank xml document
	 */
	public TNCPlotsDB() 
	{
		try 
		{
			// Load the sun jdbc-odbc bridge driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// connect to the jdbc-odbc bridge driver
			System.out.println("TNCPlotsDB > connecting to the ODBC datasource url: " + dbUrl );
			con = DriverManager.getConnection(dbUrl, "user", "pass");

			// Get the DatabaseMetaData object and display
			// some information about the connection
			DatabaseMetaData dma = con.getMetaData();

			System.out.println("TNCPlotsDB > Connected to " + dma.getURL() );
			System.out.println("TNCPlotsDB > Driver       " + dma.getDriverName() );
			System.out.println("TNCPlotsDB > Version      " + dma.getDriverVersion() );
			System.out.println("TNCPlotsDB > Catalog      " + con.getCatalog() );
			
		}
		catch (SQLException ex) 
		{
			while (ex != null)
			{
				System.out.println ("TNCPlotsDB > ErrorCode: " + ex.getErrorCode () + "<BR>");
				System.out.println ("TNCPlotsDB > SQLState:  " + ex.getSQLState () + "<BR>");
				System.out.println ("TNCPlotsDB > Message:   " + ex.getMessage () + "<BR>");
				ex = ex.getNextException();
			}
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("TNCPlotsDB > Exception: " + ex + "<BR>");
		}
	}
	
	/** 
	 * method that returns the accession number for a given plot.  This method 
	 * must be in this class eventhough it is not entirely relevant; because
	 * the TNC plots database does not use an accession number this method 
	 * will always return null
	 * @param plotId -- the RDBMS unique plot ID
	 */
	public String getAccessionValue(String plotId)
	{
		return("null");
	}
	
	/** 
	 * method that returns the accession number for a given plot observation.  
	 * This method must be in this class eventhough it is not  relevant; because
	 * the TNC plots database does not use an accession number this method 
	 * will always return null
	 * @param plotId -- the RDBMS unique plot ID
	 */
	public String getObservationAccessionNumber(String plotId)
	{
		return("null");
	}
	
	
	
	//returns all the plots stored in the access file
	public Vector getPlotNames()
	{
		Vector v = new Vector();
		Statement stmt = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select Distinct "
				+" ([Plot Code]) "
				+" from plots ");
				while (rs.next()) 
				{
					v.addElement( rs.getString(1) );
				}
				rs.close();
				stmt.close();
			}
			catch (Exception x) 
			{
				System.out.println("TNCPlotsDB > Exception: " + x.getMessage() );
			}
		return(v);
	}

	
	/**
	 * method that retuns the unique names of all the strata in a given plot
	 */
	public Vector getUniqueStrataNames(String plotName)
	{
		Vector v = new Vector();
		//watch the case sensetivity
		v.addElement("H");
		v.addElement("T1");
		v.addElement("T2");
		v.addElement("T3");
		v.addElement("S1");
		v.addElement("S2");
		v.addElement("S3");
		v.addElement("N");
		v.addElement("V");
		v.addElement("E");
		return(v);
	}
	
	
	/**
	 *
	 */
	 private void getProjectData()
	 {
		 this.projectName = "tncplots"+this.placeName;
		 this.projectDescription = "tncplotsDes"+this.placeName;
	 }
	
	/**
	 * method that returns the project name as it is viewed by tnc
	 * this is basically the location code plus the string: 
	 * "Vegetation Mapping Project"
	 */
	public String getProjectName(String plotName)
	{
		String s = null;
		Statement stmt = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Location Code]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					s = rs.getString(1);
					s = s.trim()+" Vegetation Mapping Project -- NATURESERVE";
				}
				rs.close();
			stmt.close();
			}
			catch (Exception x) 
			{
				System.out.println("TNCPlotsDB > Exception: " + x.getMessage() );
			}
		return(s);
		
	}
	
	/**
	 * method that returns the project description for a plot 
	 * which is basically the string: " Plots were collected in the national
	 * park as part of the USGS - NPS Vegetation Mapping Program (Grossman 
	 * et al. 1994) Further details are available at:
	 * http://biology.usgs.gov/npsveg/
	 *
	 */
	public String getProjectDescription(String plotName)
	{
		String s = " Plots were collected in the national "
		+ " park as part of the USGS - NPS Vegetation Mapping Program (Grossman " 
	  + "et al. 1994) Further details are available at: "
	  + " http://biology.usgs.gov/npsveg/ ";
		return(s);
	}
	
	// see the interface for method descriptions
	public Vector getProjectContributors(String plotName)
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			v.addElement("Jim Drake");
			v.addElement("Dennis Grossman");
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
	
	
	//retuns the person's salutation based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSalutation(String contributorWholeName)
	{ 
		String salutation = "";
		if ( contributorWholeName.trim().toUpperCase().startsWith("JIM") )
		{
			salutation = "Mr.";
		}
		else if ( contributorWholeName.trim().toUpperCase().startsWith("DEN") )
		{
			salutation = "Dr.";
		}
		return(salutation);
	}
	
	//retuns the person's given based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorGivenName(String contributorWholeName)
	{
		String givenName = null;
		try
		{
			StringTokenizer t = new StringTokenizer(contributorWholeName, " ");
		 	String buf = t.nextToken();
			givenName = buf;
		}
		catch (Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(givenName);
	}
	
	
	//retuns the person's middle name based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorMiddleName(String contributorWholeName)
	{
			return("");
	}
	//retuns the person's surName based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSurName(String contributorWholeName)
	{
		String surName = null;
		try
		{
			StringTokenizer t = new StringTokenizer(contributorWholeName, " ");
		 	String buf = t.nextToken();
		 	buf = t.nextToken().trim();
			surName = buf;
		}
		catch (Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(surName);
	}
	//retuns the name of an org. that a person is associated with based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorOrganizationName(String contributorWholeName)
	{
		return("The Nature Conservancy");
	}
	
	//retuns the person's contactinstructions based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
		return("contact the contributor at specified email:");
	}
	//retuns the person's phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
		String s = "";
		if ( contributorWholeName.trim().toUpperCase().startsWith("JIM") )
		{
			s = "303-444-1060";
		}
		else if ( contributorWholeName.trim().toUpperCase().startsWith("DEN") )
		{
			s = "703-908-1800";
		}
		return(s);
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
		return("Ecologist");
	}
	//retuns the person's email based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorEmailAddress(String contributorWholeName)
	{
		String email = "";
		if ( contributorWholeName.trim().toUpperCase().startsWith("JIM") )
		{
			email = "jim_drake@natureserve.org";
		}
		else if ( contributorWholeName.trim().toUpperCase().startsWith("DEN") )
		{
			email = "denny_grossman@natureserve.org";
		}
		return(email);
	}
	//retuns the person's address line based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorDeliveryPoint(String contributorWholeName)
	{
		String s = "";
		if ( contributorWholeName.trim().toUpperCase().startsWith("JIM") )
		{
			s = "1313 Fifth Street, SE";
		}
		else if ( contributorWholeName.trim().toUpperCase().startsWith("DEN") )
		{
			s = "1101 Wilson Boulevard, 15th Floor";
		}
		return(s);
	}
	//retuns the person's city based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCity(String contributorWholeName)
	{
		String s = "";
		if ( contributorWholeName.trim().toUpperCase().startsWith("JIM") )
		{
			s = "Minneapolis";
		}
		else if ( contributorWholeName.trim().toUpperCase().startsWith("DEN") )
		{
			s = "Arlington";
		}
		return(s);
	}
	
	//returns the administrative area, or state that a party is from
	public String getProjectContributorAdministrativeArea(String contributorWholeName)
	{
		String state = "";
		if ( contributorWholeName.trim().toUpperCase().startsWith("JIM") )
		{
			state = "MN";
		}
		else if ( contributorWholeName.trim().toUpperCase().startsWith("DEN") )
		{
			state = "VA";
		}
		return(state);
	}
	//retuns the zip code for a party
	public String getProjectContributorPostalCode(String contributorWholeName)
	{
		String s = "";
		if ( contributorWholeName.trim().toUpperCase().startsWith("JIM") )
		{
			s = "55414";
		}
		else if ( contributorWholeName.trim().toUpperCase().startsWith("DEN") )
		{
			s = "22209";
		}
		return(s);
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
		return("");
	}
	
	
	// see the interface for method descriptions
	public String getProjectStartDate(String plotName)
	{
		return("01-JAN-1996");
	}
	
	// see the interface for method descriptions
	public String getProjectStopDate(String plotName)
	{
		return("01-JAN-2003");
	}
	
	/**
	 * this method returns the plotName.  The plotName is the 
	 * unique identifier for a plot, akin to the plot_id in vegbank
	 * and so the code and the name in this class and database 
	 * are the same
	 */
	 public String getPlotCode(String plotName)
	{
		return(plotName);
	}
	
	//returns the placeNames each name can be used to retrieve
	//other information about a place
	public Vector getPlaceNames(String plotName)
	{
		Vector placeNames = new Vector();
		return(placeNames);
	}
	//retuns a description of a place based on a placeName
	public String getPlaceDescription(String placeName)
	{
		return("getPlaceDescription not implemented");
	}
	//returns a placeCode based on a placeName
	public String getPlaceCode(String placeName)
	{
		return("getPlaceCode not implemented");
	}
	//returns a place system based on a placeName
	public String getPlaceSystem(String placeName)
	{
		return("getPlaceSystem not implemented");
	}
	//returns the owner of a place based on the name of a place
	public String getPlaceOwner(String placeName)
	{
		return("getPlaceOwner not implemented");
	}



	/**
	 * this method looks up the y coordiante in the 
	 * access database ased on a plot.  Because many 
	 * plots seem not to have the 'Corrected UTM Y' 
	 * this method will return the 'Field UTM Y' if the 
	 * corrected value does not exist 
	 *
	 * @param plotName -- the name of the plot
	 */
	public String getYCoord(String plotName)
	{
		String y = null;
		try
		{
			y = getCorrectedYCoord( plotName);
			if ( ( y != null ) && (! y.equals("0.0") ) )
			{
				System.out.println("TNCPlotsDB > using corrected y coordinate");
				this.yCoord = y;
			}
			else
			{	
				y = getFieldYCoord(plotName);
				if ( y != null )
				{
					System.out.println("TNCPlotsDB > using field y coordinate");
					this.yCoord = y;
				}
			}
		}
		catch (Exception e)                                                                                                                              
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );                                                                                           
			e.printStackTrace();
		}
		return(this.yCoord);
	}



	/**
	 * method for looking up the 'Field UTM Y' and if it 
	 * does not exist a null is returned
	 *
	 * 
	 * @param plotName -- the name of the plot
	 */
	public String getFieldYCoord(String plotName)
	{
		Statement stmt = null;
		String y = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Field UTM Y]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					y= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(y);
	}
	


	/**
	 * method for looking up the 'Corrected UTM Y' and if it 
	 * does not exist a null is returned
	 *
	 * 
	 * @param plotName -- the name of the plot
	 */
	public String getCorrectedYCoord(String plotName)
	{
		Statement stmt = null;
		String y = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Corrected UTM Y]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					y = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(y);
	}
	

//end edit





	/**
	 * this method looks up the x coordiante in the 
	 * access database ased on a plot.  Because many 
	 * plots seem not to have the 'Corrected UTM X' 
	 * this method will return the 'Field UTM X' if the 
	 * corrected value does not exist 
	 *
	 * @param plotName -- the name of the plot
	 */
	public String getXCoord(String plotName)
	{
		String x = null;
		try
		{
			x = getCorrectedXCoord( plotName);
			if ( ( x != null ) && (! x.equals("0.0") ) )
			{
				System.out.println("TNCPlotsDB > using corrected x coordinate");
				this.xCoord = x;
			}
			else
			{	
				x = getFieldXCoord(plotName);
				if ( x != null )
				{
					System.out.println("TNCPlotsDB > using field x coordinate");
					this.xCoord = x;
				}
			}
		}
		catch (Exception e)                                                                                                                              
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );                                                                                           
			e.printStackTrace();
		}
		return(this.xCoord);
	}



	/**
	 * method for looking up the 'Field UTM X' and if it 
	 * does not exist a null is returned
	 *
	 * 
	 * @param plotName -- the name of the plot
	 */
	public String getFieldXCoord(String plotName)
	{
		Statement stmt = null;
		String x = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Field UTM X]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					x= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(x);
	}
	


	/**
	 * method for looking up the 'Corrected UTM X' and if it 
	 * does not exist a null is returned
	 *
	 * 
	 * @param plotName -- the name of the plot
	 */
	public String getCorrectedXCoord(String plotName)
	{
		Statement stmt = null;
		String x = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Corrected UTM X]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					x= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(x);
	}

	/**
	 * this is a method for retrieving the corrected latitude
	 * @param plotName -- the name of the plot.
	 * @return correctedLat -- the corrected latitude value.
	 */
	 private String getCorrectedLatitude(String plotName )
	 {
		 Statement stmt = null;
		 String x = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Corrected Lat]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					x= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(x);
	 }
	 
	 /**
	 * this is a method for retrieving the corrected latitude
	 * @param plotName -- the name of the plot.
	 * @return correctedLong -- the corrected longitude value.
	 */
	 private String getCorrectedLongitude(String plotName)
	 {
		 Statement stmt = null;
		 String x = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Corrected Long]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					x= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(x);
	 }
	 
	 /**
	 * this is a method for retrieving the corrected latitude
	 * @param plotName -- the name of the plot.
	 * @return fieldLat -- the field latitude value.
	 */
	 private String getFieldLatitude(String plotName )
	 {
		 Statement stmt = null;
		 String x = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Field Lat]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					x= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(x);
	 }
	 
	 /**
	 * this is a method for retrieving the corrected latitude
	 * @param plotName -- the name of the plot.
	 * @return fieldLong -- the corrected longitude value.
	 */
	 private String getFieldLongitude(String plotName)
	 {
		 Statement stmt = null;
		 String x = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Field Long]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					x= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(x);
	 }
	
	// see the interface for method descriptions
	public String getLatitude(String plotName)
	{
		String fl = this.getFieldLatitude(plotName);
		String cl = this.getCorrectedLatitude(plotName);
		// return the field corrdinates only if the corrected are null
		if ( cl != null )
		{
			return(cl);
		}
		else
		{
			return(fl);
		}
	}
	
	// see the interface for method descriptions
	public String getLongitude(String plotName)
	{
		String fl = this.getFieldLongitude(plotName);
		String cl = this.getCorrectedLongitude(plotName);
		// return the field corrdinates only if the corrected are null
		if ( cl != null )
		{
			return(cl);
		}
		else
		{
			return(fl);
		}
	}
	
	// see the interface for method descriptions
	public String getUTMZone(String plotName)
	{
		Statement stmt = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([UTM Zone]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					this.utmZone = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception x) 
			{
				System.out.println("TNCPlotsDB > Exception: " + x.getMessage() );
			}
		return(this.utmZone);
	}
	
	public String getDatumType(String plotName)
	{
		return("NAD27");
	}
	
	// see the interface for method descriptions
	public String getPlotShape(String plotName)
	{
		Statement stmt = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Plot Shape]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					this.plotShape = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception x) 
			{
				System.out.println("TNCPlotsDB > Exception: " + x.getMessage() );
			}
			return(this.plotShape);
	}
	
// see the interface for method descriptions
	public String getPlotArea(String plotName)
	{
		Statement stmt = null;
		String xDim = null;
		String yDim = null;
		String area = "";
			try 
			{
				
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([X Dimension]), ([Y Dimension]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					xDim = rs.getString(1);
					yDim = rs.getString(2);
				}
				rs.close();
				stmt.close();
				//multiply the x and y dimensions
				try
				{
					double x = (new Double(xDim)).doubleValue();
					double y = (new Double(yDim)).doubleValue();
					double a = x * y;
					area = ""+a;
				}
				catch (Exception x) 
				{
					System.out.println("TNCPlotsDB > Exception: " + x.getMessage() );
					System.out.println("TNCPlotsDB > Exception:  could not parse the integers " );
					return("");
				}
			}
			catch (Exception x) 
			{
				System.out.println("TNCPlotsDB > Exception: " + x.getMessage() );
			}
		return(area);
	}
	
	public String getAzimuth(String plotName) 
	{
		return null;
	}	
 
	public String getDSGPoly(String plotName)
	{
		return null;
	}
	
	public String getLocationNarrative(String plotName)
	{
		return null;
	}
	
	public String getLayoutNarrative( String plotName )
	{
		return null;
	}
	
	/**
	 * this is the method that returns the community 
	 * name that the plot is associated with -- not the 
	 * community code.  If there is no 'Classified community 
	 * name' then the 'provisional name' is returned 
	 *
	 * @param plotName -- the plot
	 *
	 */
	public String getCommunityName(String plotName)
	{
		String cn = null;
		try
		{
			String className = getClassifiedCommunityName(plotName);
			System.out.println("TNCPlotsDB > class community name: " + className);
			if ( className != null )
			{
				cn = className ;
			}
			else
			{
				String provisionalName = getProvisionalName(plotName);
				System.out.println("TNCPlotsDB > provisional community name: " + provisionalName);
				if (provisionalName != null )
				{
					cn = provisionalName;
				}
				else
				{
					cn = "unknown";
				}
			}
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	return(cn);	
	}
	
	
	/**
	 * returns the community code for the named plot.  In this
	 * case this is the tnc elcode
	 */
	public String getCommunityCode(String plotName)
	{
			Statement stmt = null;
			String code = null;
			try
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([TNC Elcode])  "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					code = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(code);
	}
	
	/**
	 * returns the community classification notes for the named plot
	 */
	public String getClassNotes(String plotName)
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
		String cf = null;
		try
		{
			String className = getClassifiedCommunityName(plotName);
			if ( className != null )
			{
				cf = "nvc" ;
			}
			else
			{
				String provisionalName = getProvisionalName(plotName);
				if (provisionalName != null )
				{
					cf = "provisional";
				}
				else
				{
					cf = "";
				}
			}
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(cf);
	}
	
	
	
	/**
	 * method that returns the provisional community 
	 * name based on a plot
	 * 
	 * @param plotName -- the plot
	 */
		private String getProvisionalName(String plotName)
		{
			Statement stmt = null;
			String name = null;
			try
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Provisional Community Name])  "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					name = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(name);
		}

	/**
	 * method that returns the classified  community 
	 * name based on a plot
	 * 
	 * @param plotName -- the plot
	 */
		private String getClassifiedCommunityName(String plotName)
		{
			Statement stmt = null;
			String name = null;
			try
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Classified Community Name])  "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					name = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(name);
		}



		
	 

	// see the interface for method descriptions
	public String getState(String plotName)
	{
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Location Code]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				String locationCode = rs.getString(1);
				System.out.println("TNCPlotsDB > locationCode: " + locationCode);
				Statement stmt2 = null;
				stmt2 = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select ([Jurisdiction Code]) from ([Locations]) where ([Location Code]) like '"+locationCode+"'");
				System.out.println("TNCPlotsDB > query: " + sb.toString() );
				ResultSet rs2 = stmt2.executeQuery( sb.toString() );
				while (rs2.next())
				{
						this.state = rs2.getString(1);
				}
				rs2.close();					
				stmt2.close();
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
			return("unknown");
		}
		 return(this.state);
	}
	
	// see the interface for method descriptions
	public String getHydrologicRegime(String plotName)
	{
		Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Hydro Regime]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				this.hydrologicRegime = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		//System.out.println("hydroregime: "+ this.hydrologicRegime);
		return(this.hydrologicRegime);
	}

	/**
	 * returns the soil drainage of a plot
	 * @param plotName -- the plot
	 */
	public String getSoilDrainage(String plotName)
	{
		String depth = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Soil Drainage]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				depth = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(depth);
	}

	public Vector getObservationContributors(String plotName)
	{
		//Vector v = new Vector();
		Statement stmt = null;
		
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select ([Surveyors]) "
				+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
		
			System.out.println("TNCPlotsDB > Trying to get surveyors for " + plotName);
			
			while (rs.next()) 
			{
				String s = rs.getString(1);
				// split into contributors 
				StringTokenizer st = new StringTokenizer( s, ",");
				while ( st.hasMoreTokens() ) {
					String contributorFullName = st.nextToken();
					observationContributors.addElement( contributorFullName );
					System.out.println("TNCPlotsDB > Found Contributor " + contributorFullName );
				}
			}
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(observationContributors);	
	}

	public String getObservationContributorSalutation(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorGivenName(String contributorWholeName) 
	{
			String givenName = null;
			try
			{
				StringTokenizer t = new StringTokenizer(contributorWholeName, " ");
			 	String buf = t.nextToken();
				givenName = buf;
			}
			catch (Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(givenName);
	}
	
	public String getObservationContributorMiddleName(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorSurName(String contributorWholeName)
	{
			String surName = null;
			StringBuffer surNameSb = new StringBuffer("");
			try
			{
				// Rule: Surname is all Text after first space
				StringTokenizer t = new StringTokenizer(contributorWholeName, " ");
				String firstName = t.nextToken();
	
				while ( t.hasMoreTokens() ) 
				{
					surNameSb.append( t.nextToken() + " " );
				}
				
				surName = surNameSb.toString().trim();
				
			 	//String buf = t.nextToken();
			 	//buf = t.nextToken().trim();
				//surName = buf;
			}
			catch (Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(surName);
	}
	
	public String getObservationContributorOrganizationName(String contributorWholeName){
		return null;
	}
	
	public String getObservationContributorContactInstructions(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorPhoneNumber(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCellPhoneNumber(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorFaxPhoneNumber(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorOrgPosition(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorEmailAddress(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorDeliveryPoint(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCity(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorAdministrativeArea(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorPostalCode(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCountry(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCurrentFlag(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorAddressStartDate(String contributorWholeName)
	{
		return null;
	}

  
	public String getObservationContributorRole(String contributorWholeName)
  {
  		int index = 666;   // Should throw an exception if not initialized 
  		String role = "";
  		
		// Use name to find out if name first, second, etc
		for (int i =0; i < observationContributors.size(); i++)
		{
			String nameToCheck = (String) observationContributors.elementAt(i);
			if ( contributorWholeName.equals(nameToCheck) )
				index = i;
		}
		
		
		if ( index == 666 )
		{
				System.out.println("TNCPlotsDB > Error: Could not find index for  '" + contributorWholeName + 
																			"' observationContributor in Vector  observationContributors" );
		}
		else if (  index == roleFirstIndex )
		{
			role =  roleFirst;
		}
		else 
		{	
			role = roleRest;
		}
		return role;
    
  }
  
	/**
	 * returns the author's observation code
	 * @param plotName -- the plot
	 */
	public String getAuthorObsCode(String plotName)
	{
		// there is always one observation per plot
		// so make a simple string that represents this
		String obsCode = plotName+".001";
		return( obsCode );
	}
	
	/**
	 * returns the start date for the plot observation
	 * @param plotName -- the plot
	 */
	public String getObsStartDate(String plotName)
	{
		//because these are all one day sampling sessions 
		// the start date is the same as the end date
		String date = this.getObsStopDate(plotName);
		return( date );
	}
	
	/**
	 * returns the stop date for the plot observation
	 * @param plotName -- the plot
	 */
	public String getObsStopDate(String plotName)
	{
		String date = "";
		Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Survey Date]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				date = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(date);
	}
	
	
	// returns the soil depth
	public String getSoilDepth(String plotName) 
	{
		String depth = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Soil Depth]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				depth = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(depth);
	}
		//START
	/**
	*/
	public String getObsDateAccuracy(String plotName)
	{
		String s = "day";
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
		String s = "Using the TNC PLOTS DB for NPS Mapping";
		return(s);
	}
	
	/**
	 */
	public String getTaxonObservationArea(String plotName)
	{
		String s = "";
		s = this.getPlotArea(plotName);
		return(s);
	}
	
	/**
	 * Were cover values for the total taxon list collected from one 
	 * contiguous area or dispersed subplots (e.g., continguous, 
	 * dispersed-regular, dispered-random)
	 */
	public String getCoverDispersion(String plotName )
	{
		String s = "contiguous";
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
		String s = this.getPlotArea(plotName);
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
		String s = "TNC Plots DMS";
		return(s);
	}
	
	/**
	 */
	public String getEffortLevel( String plotName )
	{
		String s = "AVERAGE";
		return(s);
	}
	
	
	//START

public String getPlotValidationLevel(String plotName)
{
	return("1");
}

public String  getFloristicQuality(String plotName)
{
	return("");
}

public String  getBryophyteQuality(String plotName)
{
	return("");
}

public String  getLichenQuality(String plotName)
{
	return("");
}

public String  getObservationNarrative(String plotName)
{
		String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([other comments]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				String buf = "consolidated "+rs.getString(1);
				s = buf;
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getHomogeneity(String plotName)
{
	return("");
}

public String  getRepresentativeness(String plotName)
{
		String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([Representativeness]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getBasalArea(String plotName)
{
	return("");
}

public String  getSoilMoistureRegime(String plotName)
{
	return("");
}

public String  getWaterSalinity(String plotName)
{
		String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([salinity/halinity]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getShoreDistance(String plotName)
{
	return("");
}

public String  getOrganicDepth(String plotName)
{
	return("");
}

public String  getPercentBedRock(String plotName)
{
		String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Bedrock]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getPercentRockGravel(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Small Rocks]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getPercentWood(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Wood]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}


public String  getPercentLitter(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Litter, Duff]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getPercentBareSoil(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Bare Soil]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getPercentWater(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Water]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getPercentOther(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Other]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getNameOther(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([% Other Description]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getStandMaturity(String plotName)
{
	return("");
}

public String  getLandscapeNarrative(String plotName)
{		
	return("");
}

public String  getPhenologicalAspect(String plotName)
{
	return("");
}

public String  getWaterDepth(String plotName)
{
	return("");
}

public String  getFieldHt(String plotName)
{
	return("");
}

public String  getSubmergedHt(String plotName)
{
	return("");
}

public String  getTreeCover(String plotName)
{
	return("");
}

public String  getShrubCover(String plotName)
{
	return("");
}

public String  getFieldCover(String plotName)
{
	return("");
}

public String  getNonvascularCover(String plotName)
{
	return("");
}

public String  getSuccessionalStatus(String plotName)
{
	return("");
}

public String  getTreeHt(String plotName)
{
	return("");
}

public String  getShrubHt(String plotName)
{
	return("");
}

public String  getNonvascularHt(String plotName)
{
	return("");
}

public String  getFloatingCover(String plotName)
{
	return("");
}

public String  getSubmergedCover(String plotName)
{
	return("");
}

public String  getDominantStratum(String plotName)
{
	String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([Leaf Type]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(s);
}

public String  getGrowthform1Type(String plotName)
{
	return("");
}

public String  getGrowthform2Type(String plotName)
{
	return("");
}

public String  getGrowthform3Type(String plotName)
{
	return("");
}

public String  getGrowthform1Cover(String plotName)
{
	return("");
}

public String  getGrowthform2Cover(String plotName)
{
	return("");
}

public String  getGrowthform3Cover(String plotName)
{
	return("");
}

public boolean  getNotesPublic(String plotName)
{
	return(false);
}

public boolean  getNotesMgt(String plotName)
{
	return(false);
}

public boolean  getRevisions(String plotName)
{
	return(false);
}
//END
//END
	


		// see the interface for method descriptions
	public String getTopoPosition(String plotName)
	{
		Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Topo Position]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				this.topoPosition = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(this.topoPosition);
	}
	
	// see the interface for method descriptions
	public String getSlopeAspect(String plotName)
	{
		 Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Aspect]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				String s = rs.getString(1);
				if (s.startsWith("Flat"))
					this.slopeAspect = "0";
				else if (s.toUpperCase().equals("N"))
					this.slopeAspect = "0";
				else if (s.toUpperCase().equals("S"))
					this.slopeAspect = "180";
				else if (s.toUpperCase().equals("NE"))
					this.slopeAspect = "45";
				else if (s.toUpperCase().equals("SE"))
					this.slopeAspect = "125";
				else if (s.toUpperCase().equals("SW"))
					this.slopeAspect = "225";
				else if (s.toUpperCase().equals("NW"))
					this.slopeAspect = "315";
				else if (s.toUpperCase().equals("E"))
					this.slopeAspect = "90";
				else if (s.toUpperCase().equals("W"))
					this.slopeAspect = "270";
					
				else
					this.slopeAspect ="-1";
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(this.slopeAspect );
	}
	
	// see the interface for method descriptions
	public String getSlopeGradient(String plotName)
	{
		 Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Slope]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				String s = rs.getString(1);
				if (s.toUpperCase().startsWith("GENTLE"))
					this.slopeGradient = "3";
				else if (s.toUpperCase().startsWith("FLAT"))
					this.slopeGradient = "0";
				else if (s.toUpperCase().startsWith("STEEP"))
					this.slopeGradient = "36";
				else if (s.toUpperCase().startsWith("SOMEWHAT"))
					this.slopeGradient = "20.5";
				else if (s.toUpperCase().startsWith("VERY"))
					this.slopeGradient = "57.5";
				else if (s.toUpperCase().startsWith("MODERATE"))
					this.slopeGradient = "10";
				else if (s.toUpperCase().startsWith("ABRUPT"))
					this.slopeGradient = "85";
				else 
					this.slopeGradient = "";
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(this.slopeGradient );
	}
	
	
	// see the interface for method descriptions
	public String getSurfGeo(String plotName)
	{
		return this.getRockType(plotName);
	}
	
	// see the interface for method descriptions
	public String getCountry(String plotName)
	{
		return("USA");
	}
	
	// see the interface for method descriptions
	public String getStandSize(String plotName)
	{
		return("1200");
	}
	
	/**
	 * returns the origional location as described by the 
	 * author -- basically the location code and the sublocation
	 * together
	 *
	 * @param plotName
	 */
	public String getAuthorLocation(String plotName)
	{
		String s = "";
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Sublocation]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(s);
	}
	
	// see the interface for method descriptions
	public String getLandForm(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Landform]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				s = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(s);
	}
	
	// see the interface for method descriptions
	public String getElevation(String plotName)
	{
		Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Elevation]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				// Assuming that this is an double of the elevation in feet
				String elevationFeet = rs.getString(1);
				double elevationMeters = feetToMeters( Double.valueOf(elevationFeet).doubleValue() );
				System.out.println("TNCPlotsDB > Elevation is " + elevationFeet +  " feet (" + elevationMeters + " meters)" );				
				this.elevation = Double.toString(elevationMeters);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(this.elevation);
	}
	
	// see the interface for method descriptions
	public String getElevationAccuracy(String plotName)
	{
		return("20");
	}
	
	// see the interface for method descriptions
	public String getConfidentialityReason(String plotName)
	{
		return("no endangered species");
	}
	
	// see the interface for method descriptions
	public String getConfidentialityStatus(String plotName)
	{
		return("0");
	}

	/**
	 * returns true if the plot is a permanent plot or false if not
	 * @param plotName -- the plot
	 */
	 public boolean isPlotPermanent(String plotName)
	 {
		Statement stmt = null;
		String p = null;
		boolean r = true;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Permanent]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					p=rs.getString(1);
				}
				if ( p.toUpperCase().equals("Yes") )
				{
				 	r = true;
				}
				else
				{	
					r = false;
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e) 
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
	 	return(r);
	 }
 
 
 /**
	* returns the soil taxon for the plot -- this is the USDA class
	* heirarchy (eg., Order, Group, Family, Series etc..)
	* @param plotName -- the plot
	*/
	public String getSoilTaxon(String plotName)
	{
		Statement stmt = null;
		String x = null;
		try 
		{
			// Create a Statement so we can submit SQL statements to the driver
			stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select "
			+" ([Soil Taxon/Description]) "
			+" from plots where ([Plot Code]) like '"+plotName+"'");
			while (rs.next()) 
			{
				x= rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e) 
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(x);
	}
	
	/**
	 * returns how the soil taxon was determined (eg., field observation
	 * mapping, other ...)
	 * @param plotName -- the plot
	 */
	public String getSoilTaxonSource(String plotName)
	{
		String t = this.getSoilTaxon(plotName);
		if ( t != null )
		{
			return("field observation");
		}
		else
		{
			return("");
		}
	}

		 
	 /**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String strataName)
	 {
		 System.out.println("TNCPlotsDB > strata cover lookup: '"+strataName+"'" );
		 
		 String coverAttribute = ""; // this will become the attribute name like 'H Cover'
		 String cover = ""; // the cover 
		 coverAttribute = strataName+" Cover";
		 try 
		 {
			 Statement stmt = con.createStatement();
			 StringBuffer sb = new StringBuffer();
			 sb.append("select  (["+coverAttribute+"])");
			 sb.append(" from ([Plots]) where ([Plot Code]) like '"+plotName+"'" );
			 ResultSet rs = stmt.executeQuery(sb.toString() );

				while (rs.next()) 
				{
					cover = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		 	return(cover);
	 }
	 
	 
	 /**
	  * method that returns the min height for a stratum
		* as observerd in a specific plot using as inputs a plot name 
		* and a stratumName
		*
		* @param plot -- the plot
		* @param stratumName -- the stratum
	  */
	 public String getStrataBase(String plotName, String strataName)
	 {
		 	System.out.println("TNCPlotsDB > strata height lookup: '"+strataName+"'" );
		 	String height = "";
			String trueHeight = "";
			Statement stmt = null;
			try 
			{
				//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select ");
				sb.append(" (["+strataName+" Hgt]) ");
				sb.append(" from ([Plots]) where ([Plot Code]) like '"+plotName+"'" );
				ResultSet rs = stmt.executeQuery(sb.toString() );

				while (rs.next()) 
				{
					height = rs.getString(1);
					if ( height != null )
					{
						height = height.trim();
						if ( height.startsWith("<0.5"))
						{
							trueHeight = "0.0";
						}
						else if  ( height.startsWith("0.5 -"))
						{
							trueHeight = "0.5";
						}
						else if  ( height.startsWith("1 -"))
						{
							trueHeight = "1.0";
						}
						else if  ( height.startsWith("2 -"))
						{
							trueHeight = "2.0";
						}
						else if  ( height.startsWith("5 -"))
						{
							trueHeight = "5.0";
						}
						else if  ( height.startsWith("10 -"))
						{
							trueHeight = "10.0";
						}
						else if  ( height.startsWith("15 -"))
						{
							trueHeight = "15.0";
						}
						else if  ( height.startsWith("20 -"))
						{
							trueHeight = "20";
						}
						else if  ( height.startsWith("35 -"))
						{
							trueHeight = "35";
						}
						else if  ( height.startsWith(">50"))
						{
							trueHeight = "50";
						}
						else
						{
							trueHeight = "";
						}
					}
					else 
					{
						trueHeight = getDefaultStrataBaseHeight(strataName);
						//trueHeight = "";
					}
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			//System.out.println("TNCPlotsDB > strum true base: '" + trueHeight +"'" );
		 	return(trueHeight);
	 }
	 
	 
	 /**
	  * method that returns the max height for a stratume 
		* as observerd in a specific plot using as inputs a plot name 
		* and a stratumName -- if the levels are not defined in the 
		* Access table then a default is returned
		*
		* @param plot -- the plot
		* @param stratumName -- the stratum
	  */
	 public String getStrataHeight(String plotName, String strataName)
	 {
		 	//System.out.println("TNCPlotsDB > strata height lookup: " + strataName );
		 	String height = "";
			String trueHeight = "";
			Statement stmt = null;
			try 
			{
				//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select ");
				sb.append(" (["+strataName+" Hgt]) ");
				sb.append(" from ([Plots]) where ([Plot Code]) like '"+plotName+"'" );
				ResultSet rs = stmt.executeQuery(sb.toString() );

				while (rs.next()) 
				{
					height = rs.getString(1);
					//System.out.println("TNCPlotsDB > strata height: '" + height +"'" );
					if ( height != null )
					{
						height = height.trim();
						if ( height.startsWith("<0.5"))
						{
							trueHeight = "0.5";
						}
						else if  ( height.startsWith("0.5 -"))
						{
							trueHeight = "1.0";
						}
						else if  ( height.startsWith("1 -"))
						{
							trueHeight = "2.0";
						}
						else if  ( height.startsWith("2 -"))
						{
							trueHeight = "5.0";
						}
						else if  ( height.startsWith("5 -"))
						{
							trueHeight = "10.0";
						}
						else if  ( height.startsWith("10 -"))
						{
							trueHeight = "15.0";
						}
						else if  ( height.startsWith("15 -"))
						{
							trueHeight = "20.0";
						}
						else if  ( height.startsWith("20 -"))
						{
							trueHeight = "35";
						}
						else if  ( height.startsWith("35 -"))
						{
							trueHeight = "50";
						}
						else if  ( height.startsWith(">50"))
						{
							trueHeight = "100";
						}
						else
						{
							trueHeight = "";
						}
					}
					//if there is no height listed in the Access table then get the default height
					else 
					{
						//trueHeight = "";
						trueHeight = getDefaultStrataHeight(strataName);
					}
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			//System.out.println("TNCPlotsDB > strum true height: '" + trueHeight +"'" );
		 	return(trueHeight);
	 }
	 
	 /**
	  * this method will return the default height for a given strata,  The
		* inputs are the strata name and the term top or bottom so that the 
		* the correct elevation is returned
		* @param strataName --  the name of the stratum
		*/
		private String getDefaultStrataHeight(String strataName)
		{
			if ( strataName.equals("N") ) {
				return ("0.5");
			} 
			else if ( strataName.equals("H") ) 
			{
				return ("2");
			} 
			else if ( strataName.equals("S3") ) 
			{
				return ("1");
			} 
			else if ( strataName.equals("S2") ) 
			{
				return ("2");
			} 
			else if ( strataName.equals("S1") ) 
			{
				return ("5");
			}
			 else if ( strataName.equals("T3") ) 
			 {
				return ("10");
			} 
			else if ( strataName.equals("T2") ) 
			{
				return ("30");
			} 
			else if ( strataName.equals("T1") ) 
			{
				return ("100");
			}
			else 
			{
				return ("0");
			} 
		}
		
		/**
		 *  Return the default StrataBase Height
		 * @param strataName -- the name of the strata
		 */
		private String getDefaultStrataBaseHeight ( String strataName) 
		{
			if ( strataName.equals("N") ) 
			{
				return ("0");
			} 
			else if ( strataName.equals("H") ) 
			{
				return ("0");
			} 
			else if ( strataName.equals("S3") )
			{
				return ("0.5");
			} 
			else if ( strataName.equals("S2") ) 
			{
				return ("1");
			} 
			else if ( strataName.equals("S1") ) 
			{
				return ("2");
			} 
			else if ( strataName.equals("T3") ) 
			{
				return ("5");
			} 
			else if ( strataName.equals("T2") ) 
			{
				return ("10");
			} 
			else if ( strataName.equals("T1") ) 
			{
				return ("30");
			} 
			else 
			{
				return ("0");
			} 		
		
		}
	 
	
	/**
	 * method that takes a plot code and populates the public varaibles with
	 * data related to that specific plot
	 *
	 * @param plotName -- the name of a plot for which to retrieve data for
	 */
	 public void getPlotData(String plotName)
	 {
		
		 //get the data from the plots access table
		 getSiteInfo( plotName);
		 //get the data from the plots-species table
		 getSpeciesInfo( plotName );
		 //get all the data from the location info
		 getLocationInfo( locationCode );
		 //get the cover-class values etc..
		 getStrataInfo( plotName );
		 //the top-level information for the plots database this is 
		 //last because it requires variables that have been updated 
		 //elsewhere
		 getProjectData();
	 }
	 
	 /**
	  * method that updates the publicly accessible variables with the 
		* site specific data
		* 
		* @param plotName -- string representation of the plotCode
		*/
		private void getSiteInfo(String plotName)
		{
			Statement stmt = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Plot Code]), "
				+" SubPlot, "
				+" ([Air Photo Number]), "
				+" ([Provisional Community Name]), "
				+" ([TNC Elcode]), "
				+" ([Corrected UTM X]), "
				+" ([Corrected UTM Y]), "
				+" ([UTM Zone]), "
				+" ([Survey Date]), "
				+" ([Elevation]), "
				+" ([Slope]), "
				+" ([Aspect]), "
				+" ([Surficial Geology]), "
				+" ([Location Code]), "
				+" ([Soil Depth]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
			
				//there should only be one
				while (rs.next()) 
				{
					plotCode = rs.getString(1);
					communityName = rs.getString(4);
					communityCode = rs.getString(5);
					xCoord= rs.getString(6);
					yCoord= rs.getString(7);
					utmZone = rs.getString(8);
					date = rs.getString(9);
					elevation = rs.getString(10);
					slopeGradient = rs.getString(11);
					System.out.println("TNCPlotsDB > slope: " + slope);
					slopeAspect = rs.getString(12);
					surfGeo = rs.getString(13);
					locationCode = rs.getString(14);
					soilDepth = rs.getString(15);
				}
				//create the obscode by combining the plot with the date
				this.authorObsCode = plotCode+date;
			rs.close();
			stmt.close();
			}
			catch (SQLException ex) 
			{
				// Error, a SQLException was generated. Display the error information
				System.out.println ("<BR><B>*** SQLException caught ***</B><BR>");
				try 
				{  
					System.out.println("Warning =   " + stmt.getWarnings() ); 
				}
				catch (Exception x) {}
				// get all sql error messages in a loop
				while (ex != null)
				{
					System.out.println ("ErrorCode: " + ex.getErrorCode () + "<BR>");
					System.out.println ("SQLState:  " + ex.getSQLState () + "<BR>");
					System.out.println ("Message:   " + ex.getMessage () + "<BR>");
					System.out.println ("&nbsp;<BR>");
					ex = ex.getNextException();
				}
			}
			catch (java.lang.Exception ex) 
			{   // All other types of exceptions
				System.out.println("TNCPlotsDB > Exception: " + ex + "<BR>");
			}		
		}
		
		
		/**
		 * method that returns a vector with the unique plant 
		 * taxa names for a given plot -- this method is defined
		 * in the plugin interface
		 */
		 public Vector getPlantTaxaNames(String plotName)
		 {
			 //call the private getSpeciesInfo method
			 scientificNames = new Vector();
			 getSpeciesInfo(plotName);
			 System.out.println("TNCPlotsDB > The plants in this plot: " +scientificNames);
			 return( scientificNames );
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
			Statement stmt = null;
			String code = null;
			try
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Plant Symbol])  "
				+" from ([Plots-Species]) where ([Scientific Name]) like '"+plantName+"'");
				while (rs.next()) 
				{
					code = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(code);
	 	}
		
    /**
		 * method to return the taxa code from a data source using as input 
		 * the scientific plant name -- or the plant name that comes from 
		 * the 'getPlantTaxaNames' method
	 	 *
	 	 * @param plantName -- the scientific plantName
	 	 */
		public String getPlantTaxonCover(String plantName, String plotName)
	 	{
			Statement stmt = null;
			String taxonCover = "";
      
			try
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([Real Cover])  "
				+" from ([Plots-Species]) where ([Scientific Name]) = '"+plantName+"'"
				+ " AND ([Plot Code]) like '"+plotName+"'");
				while (rs.next()) 
				{
					taxonCover = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
				e.printStackTrace();
			}
      
      System.out.println("TNCPlotsDB > cover: " + taxonCover);
			return(taxonCover);
	 	}		
	 
	 /**
	  * method that updates the publicly accessible variables with the 
		* site specific data
		* 
		* @param plotName -- string representation of the plotCode
		*/
		private void getSpeciesInfo(String plotName)
		{
			Statement stmt = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select distinct"
				+" ([Plot Code]), "
				+" ([Scientific Name]) "
				+" from ([Plots-Species]) where ([Plot Code]) like '"+plotName+"'");
				int cnt = 0;
				//there should only be one
				while (rs.next()) 
				{
					String s = rs.getString(2);
					if ( s != null  &&  s.length() >=2 )
					{
						scientificNames.addElement(s);
					}
					cnt++;
				}
				//update the public variable representing the number of 
				//unique scientific names
				uniquePlantNameNumber = cnt;
				System.out.println("TNCPlotsDB > " + uniquePlantNameNumber + " unique plant names for plot: " + plotName);
				rs.close();
				stmt.close();
			}
			catch (SQLException ex) 
			{
				// Error, a SQLException was generated. Display the error information
				System.out.println ("<BR><B>*** SQLException caught ***</B><BR>");
				try 
				{  
					System.out.println("Warning =   " + stmt.getWarnings() ); 
				}
				catch (Exception x) {}
				// get all sql error messages in a loop
				while (ex != null)
				{
					System.out.println ("ErrorCode: " + ex.getErrorCode () + "<BR>");
					System.out.println ("SQLState:  " + ex.getSQLState () + "<BR>");
					System.out.println ("Message:   " + ex.getMessage () + "<BR>");
					System.out.println ("&nbsp;<BR>");
					ex = ex.getNextException();
				}
			}
			catch (java.lang.Exception ex) 
			{   // All other types of exceptions
				System.out.println("TNCPlotsDB > Exception: " + ex + "<BR>");
			}	
		}
		
	
	 /**
	  * method that updates the publicly accessible variables with the 
		* site location data like state and place name
		* 
		* @param locationCode -- string representation of the location code
		*/
		private void getLocationInfo(String locationCode)
		{
			if (locationCode == null)
			{
				System.out.println("Location Code is null -- this should not be");
			}
			else
			{
				Statement stmt = null;
				try 
				{
					//System.out.println("locationCode: " + locationCode);
					// Create a Statement so we can submit SQL statements to the driver
					stmt = con.createStatement();
					//create the result set
					ResultSet rs = stmt.executeQuery("select "
					+" ([Location Name]), "
					+" ([Jurisdiction Code]) "
					+" from Locations where ([Location Code]) like '"+locationCode+"'");
			
					
					//there should only be one
					while (rs.next()) 
					{
						placeName = rs.getString(1);
						state = rs.getString(2);
						//String s = rs.getString(2);
						//System.out.println(rs.getString(2) );
						//scientificNames.addElement(s);
					}
					rs.close();
					stmt.close();
				}
				catch (SQLException ex) 
				{
					// Error, a SQLException was generated. Display the error information
					System.out.println (" SQLException caught ");
					try 
					{  
						System.out.println("Warning =   " + stmt.getWarnings() ); 
					}
					catch (Exception x) { }
					// get all sql error messages in a loop
					while (ex != null)
					{
						System.out.println ("ErrorCode: " + ex.getErrorCode () + "<BR>");
						System.out.println ("SQLState:  " + ex.getSQLState () + "<BR>");
						System.out.println ("Message:   " + ex.getMessage () + "<BR>");
						System.out.println ("&nbsp;<BR>");
						ex = ex.getNextException();
					}
				}
				catch (java.lang.Exception ex) 
				{   // All other types of exceptions
					System.out.println("TNCPlotsDB > Exception: " + ex + "<BR>");
				}
			}//end else
			}
		
		
	/**
	 * method that returns a vector of plot codes from the target
	 * tnc plots database
	 */
	public Vector getPlotCodes()
	{
		Vector v = new Vector();
//		String dbUrl = "jdbc:odbc:test_access"; 
//		Connection con = null; 
		Statement stmt = null;
		try 
		{
			// Create a Statement so we can submit SQL statements to the driver
			stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select ([Plot Code]), "
			+"SubPlot, ([Air Photo Number]), ([Provisional Community Name]) from plots");
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				//System.out.println(rs.getString(1) );
				v.addElement(s);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) 
		{
			// Error, a SQLException was generated. Display the error information
			System.out.println ("<BR><B>*** SQLException caught ***</B><BR>");
			try 
			{  
				System.out.println("Warning =   " + stmt.getWarnings() ); 
			}
			catch (Exception x) {}
			// get all sql error messages in a loop
			while (ex != null)
			{
				System.out.println ("ErrorCode: " + ex.getErrorCode () + "<BR>");
				System.out.println ("SQLState:  " + ex.getSQLState () + "<BR>");
				System.out.println ("Message:   " + ex.getMessage () + "<BR>");
				System.out.println ("&nbsp;<BR>");
				ex = ex.getNextException();
			}
		}
		catch (java.lang.Exception ex) 
		{   // All other types of exceptions
			System.out.println("TNCPlotsDB > Exception: " + ex + "<BR>");
		}
		return(v);
}


	/**
	 * method that returns the strata inwhich the input plant
	 * exists within the input plot
	 */
	 public Vector getTaxaStrataExistence(String plantName, String plotName)
	 {
		 Vector v = new Vector();
		 Statement stmt = null;
		 ResultSet rss = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			rss = stmt.executeQuery("select "
			+" ([Stratum]) "
			+" from ([Plots-Species]) where ([Plot Code]) like '"+plotName+"' and ([Scientific Name]) like '"+plantName+"'");
			//there should only be one
			while (rss.next()) 
			{
				String curStrata = rss.getString(1);
				//System.out.println( curStrata );
				v.addElement( curStrata );
			}
			rss.close();
			stmt.close();
			
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
		}
		 return(v);
	 }
	 
	 
	 /**
	 * method that returns the strata cover for a given plant, strata, and 
	 * plot
	 */
	 public String getTaxaStrataCover(String plantName, String plotName, String stratum)
	 {
		 //System.out.println( "plnat name: " + plantName + "plot: " + plotName +" strata:"+ stratum);
		 Vector v = new Vector();
		 String s = null;
		 Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Real Cover]) "
			+" from ([Plots-Species]) where ([Plot Code]) like '"+plotName+"' "
			+" and ([Scientific Name]) like '"+plantName+"' "
			+" and Stratum like '"+stratum+"'");
			//there should only be one
			while (rs.next()) 
			{
				s = rs.getString(1);
				v.addElement(s);
			}
			//make sure that there are not too many values in the vector
			if ( v.size() > 1)
			{
				System.out.println("TNCPlotsDB > warning - more than one cover value found for a taxa strata pair");
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(s);
	 }
	 
	 
	 
	 /**
	  * method that updates the publicly accessible variables with the 
		* site specific data
		* 
		* @param plotName -- string representation of the plotCode
		*/
		private void getStrataInfo(String plotName)
		{
			Statement stmt = null;
			try 
			{
				System.out.println( " plotName " + plotName);
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([T1 Hgt]), "
				+" ([T1 Cover]), "
				+" ([T2 Hgt]), "
				+" ([T1 Cover]), "
				+" ([T2 Hgt]), "
				+" ([T3 Cover]), "
				+" ([T3 Hgt]), "
				+" ([S1 Cover]), "
				+" ([S1 Hgt]) "
				+" from plots where ([Plot Code]) like '"+plotName+"'");
			
				//there should only be one
				while (rs.next()) 
				{
					t1Height = rs.getString(1);
					t1Cover = rs.getString(2);
					t2Height = rs.getString(3);
					t2Cover= rs.getString(4);
					t3Height= rs.getString(5);
					t3Cover = rs.getString(6);
					s1Height = rs.getString(7);
					s1Cover = rs.getString(8);
				}
			
				//update the publicly accessible variables
				strataNames.addElement("t1");
				strataMinHeight.addElement(t1Height);
				strataMaxHeight.addElement(t1Height);
				strataCover.addElement(t1Cover);
				strataNames.addElement("t2");
				strataMinHeight.addElement(t2Height);
				strataMaxHeight.addElement(t2Height);
				strataCover.addElement(t2Cover);
				strataNames.addElement("t3");
				strataMinHeight.addElement(t3Height);
				strataMaxHeight.addElement(t3Height);
				strataCover.addElement(t3Cover);
				strataNames.addElement("s1");
				strataMinHeight.addElement(s1Height);
				strataMaxHeight.addElement(s1Height);
				strataCover.addElement(s1Cover);
			
				//make sure that the strata codes do not have null values
				//create the obscode by combining the plot with the date
				this.authorObsCode = plotCode+date.replace(' ','_');
				stmt.close();
				rs.close();
			
			}
			catch (SQLException ex) 
			{
				// Error, a SQLException was generated. Display the error information
				System.out.println (" SQLException caught ");
				try 
				{  
					System.out.println("Warning =   " + stmt.getWarnings() ); 
				}
				catch (Exception x) 
				{
					System.out.println("TNCPlotsDB > Exception: " + x.getMessage() );
				}
				// get all sql error messages in a loop
				while (ex != null)
				{
					System.out.println ("ErrorCode: " + ex.getErrorCode () + "<BR>");
					System.out.println ("SQLState:  " + ex.getSQLState () + "<BR>");
					System.out.println ("Message:   " + ex.getMessage () + "<BR>");
					System.out.println ("&nbsp;<BR>");
					ex.printStackTrace();
					ex = ex.getNextException();
				}
			}
			catch (java.lang.Exception ex) 
			{
				System.out.println("TNCPlotsDB > Exception: " + ex + "<BR>");
			}
		}
		
	/**
	 * method that retuns the cummulative cover accoss all strata for a given 
	 * plant taxa in a given plot
	 */
	public String getCummulativeStrataCover(String plantName, String plotName)
	{
		return("7");
	}

	/**
	 * This method should be in a utility class. 
	 * Convert number feet into number of meters
	 * 
	 * @param feet the number of feet to convert to meters.
     * @return the number of meters in the feet. 
	 * */
	public double feetToMeters(double feet) 
	{	
		double meters = feet  *  0.305f;
		return meters;
	}	

/**
 * main method for testing --
 */
public static void main(String[] args)
{
	if (args.length == 1) 
	{
		String plotName = args[0];
		TNCPlotsDB db = new TNCPlotsDB();
		System.out.println(" getting info for: " + plotName );
		
		//populate the publicly accessible variables
		db.getPlotData(plotName);
		System.out.println("project name: " + db.projectName);
		System.out.println("project description: " + db.projectDescription);
		
		System.out.println("Plot Name: " + plotName);
		
		System.out.println( "community name: " + db.communityName );
		System.out.println( "community code: "+db.communityCode );
		
		
		System.out.println( "X: "+db.xCoord );
		System.out.println( "Y: "+db.yCoord );
		System.out.println( "UTM Zone: "+db.utmZone );
		System.out.println( "date: "+db.date );
		
		System.out.println("Elevation: "+ db.elevation );
		System.out.println("Slope Gradient: " +db.slope );
		System.out.println("Slope Aspect: "+ db.aspect );
		System.out.println("Surf Geo: "+ db.surfGeo );
		System.out.println("Place Name: " + db.placeName);
		System.out.println("State:  " + db.state);
		System.out.println("Plot Shape: " + db.plotShape );
		
		System.out.println("author obs code: " + db.authorObsCode);
		System.out.println("Soil Depth: " + db.soilDepth );
		
		System.out.println(" ");
		System.out.println("Number of Strata " + db.strataNumber);
		for (int i=0; i<db.strataNumber; i++)
		{
			System.out.println("Strata " + i + " " + (String)db.strataNames.elementAt(i)  );
			System.out.println(" min height " + (String)db.strataMinHeight.elementAt(i));
			System.out.println(" max height " + (String)db.strataMaxHeight.elementAt(i));
			System.out.println(" cover " + (String)db.strataCover.elementAt(i));
		}
		
		System.out.println("\n" +"number of species: " + db.uniquePlantNameNumber );
		
		for (int i=0; i<db.uniquePlantNameNumber; i++)
		{
			String name = db.scientificNames.elementAt(i).toString();
			System.out.println( "name: " + name );
			Vector strata = db.getTaxaStrataExistence(name, plotName);
			System.out.println( "strata: " + strata.toString() );
		}
	
	}
	else
	{
		TNCPlotsDB db = new TNCPlotsDB();
		System.out.println( db.getPlotCodes().toString() );
	}
}



	
	





	/**
	 * @see PlotDataSourceInterface#getCommunityInspection(java.lang.String)
	 */
	public String getCommunityInspection(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityPublication(java.lang.String)
	 */
	public String getCommunityPublication(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityStartDate(java.lang.String)
	 */
	public String getCommunityStartDate(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityStopDate(java.lang.String)
	 */
	public String getCommunityStopDate(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityExpertSystem(java.lang.String)
	 */
	public String getCommunityExpertSystem(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityMultiVariateAnalysis(java.lang.String)
	 */
	public String getCommunityMultiVariateAnalysis(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityTableAnalysis(java.lang.String)
	 */
	public String getCommunityTableAnalysis(String plotName)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getRockType(java.lang.String)
	 */
	public String getRockType(String plotName)
	{
		Statement stmt = null;
		String rockType = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Surficial Geology]) "
			+" from ([Plots]) where ([Plot Code]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				rockType = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("TNCPlotsDB > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(rockType);
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSurficialDeposits(java.lang.String)
	 */
	public String getSurficialDeposits(String plotName)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStratumMethodName(java.lang.String)
	 */
	public String getStratumMethodName(String plotName)
	{
		return "NPS StratumMethod"; 
	}
	
	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCoverMethodName(java.lang.String)
	 */
	public String getCoverMethodName(String plotName)
	{
		return "NPS CoverMethod"; 
	}

}
