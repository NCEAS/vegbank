import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


/**
 * This class represents the TNC Plots DB and will be used as a
 * source for the transformation of the TNC plots database into 
 * the native XML format used by the vegbank system -- this class 
 * will be one of a number of plugins for access to a data source
 * so the many of the varaibles and methods will be general and 
 * consistent with many other plugins
 *
 *  Authors: 
 *  Release: 
 *	
 *  '$Author: harris $'
 *  '$Date: 2002-04-03 17:07:56 $'
 * 	'$Revision: 1.18 $'
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
		{   // All other types of exceptions
			System.out.println("Exception: " + ex + "<BR>");
		}
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
				System.out.println("Exception: " + x.getMessage() );
			}
		return(v);
	}

	
	//retuns the unique names of all the strata in a given plot
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
					s = s.trim()+" Vegetation Mapping Project";
				}
				rs.close();
			stmt.close();
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
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
			
			
			// USE THE CODE BELOW FOR THE OBSERVATION CONTRIBUTOR
			/*
			stmt = con.createStatement();
			String query = " select ([Surveyors])  "
			+" from ([Plots])  where ([Plot Code]) = '"+plotName+"'";
			//System.out.println(query);
			ResultSet rs = stmt.executeQuery( query);
			while (rs.next()) 
			{
				String party = rs.getString(1);
				if (party.indexOf(",") >= 0 || party.indexOf("&") >= 0 )
				{
					if (party.indexOf(",") >= 0)
					{
						StringTokenizer t = new StringTokenizer(party, ",");
						while ( t.hasMoreTokens() )
						{
							String buf = t.nextToken();
							v.addElement( buf );
						}
					}
					else if ( party.indexOf("&") >= 0 )
					{
						StringTokenizer t = new StringTokenizer(party, "&");
						while ( t.hasMoreTokens() )
						{
							String buf = t.nextToken();
							v.addElement( buf );
						}
					}
					else
					{
						System.out.println("cannot tokenize the surveyors");
					}
				}
				else
				{
					//add the single surveyor
					v.addElement(party);
				}
			}
			rs.close();
			stmt.close();
		*/
		
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
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
		String givenName = null;
		try
		{
			StringTokenizer t = new StringTokenizer(contributorWholeName, " ");
		 	String buf = t.nextToken();
			givenName = buf;
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
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
			System.out.println("Exception: " + e.getMessage() );
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
		return("Ecologist");
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
		return("02-JAN-1998");
	}
	
	// see the interface for method descriptions
	public String getProjectStopDate(String plotName)
	{
		return("02-JAN-1998");
	}
	
	// see the interface for method descriptions
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
			System.out.println("Exception: " + e.getMessage() );                                                                                           
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
				System.out.println("Exception: " + e.getMessage() );
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
				System.out.println("Exception: " + e.getMessage() );
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
			System.out.println("Exception: " + e.getMessage() );                                                                                           
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
				System.out.println("Exception: " + e.getMessage() );
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
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		return(x);
	}
	

/*
	
	// see the interface for method descriptions
	public String getYCoord(String plotName)
	{
		Statement stmt = null;
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
					yCoord= rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
			}
		return(this.yCoord);
	}
	
*/

	
	// see the interface for method descriptions
	public String getLatitude(String plotName)
	{
		return("34");
	}
	
	// see the interface for method descriptions
	public String getLongitude(String plotName)
	{
		return("-116");
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
				System.out.println("Exception: " + x.getMessage() );
			}
		return(this.utmZone);
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
				System.out.println("Exception: " + x.getMessage() );
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
					System.out.println("Exception: " + x.getMessage() );
					System.out.println("Exception:  could not parse the integers " );
					return("");
				}
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
			}
		return(area);
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
			System.out.println("Exception: " + e.getMessage() );
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
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(code);
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
			System.out.println("Exception: " + e.getMessage() );
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
				System.out.println("Exception: " + e.getMessage() );
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
				System.out.println("Exception: " + e.getMessage() );
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
			System.out.println("Exception: " + e.getMessage() );
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
			System.out.println("Exception: " + e.getMessage() );
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
		return("");
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
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(date);
	}
	
	
	
	/**
	 * returns the soil depth of a plot
	 * @param plotName -- the plot
	 */
	public String getSoilDepth(String plotName) 
	{
		return("");
	}

	


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
			System.out.println("Exception: " + e.getMessage() );
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
				else if (s.startsWith("N"))
					this.slopeAspect = "0";
				else if (s.startsWith("S"))
					this.slopeAspect = "180";
				else if (s.startsWith("NE"))
					this.slopeAspect = "45";
				else if (s.startsWith("SE"))
					this.slopeAspect = "230";
				else
					this.slopeAspect ="-1";
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
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
				if (s.startsWith("Gentle"))
					this.slopeGradient = "3";
				else if (s.startsWith("Flat"))
					this.slopeGradient = "0";
				else if (s.startsWith("Steep"))
					this.slopeGradient = "20";
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(this.slopeGradient );
	}
	
	// see the interface for method descriptions
	public String getSurfGeo(String plotName)
	{
		 Statement stmt = null;
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
				this.surfGeo = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(this.surfGeo);
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
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		 return(s);
	}
	
	// see the interface for method descriptions
	public String getLandForm(String plotName)
	{
		return("land form");
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
				this.elevation = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
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
		return("land form");
	}
	
	// see the interface for method descriptions
	public String getConfidentialityStatus(String plotName)
	{
		return("land form");
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
				System.out.println("Exception: " + e.getMessage() );
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

		 
	 /**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String strataName)
	 {
		 return("100");
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
		 	System.out.println("TNCPlotsDB > strata height lookup: " + strataName );
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
					System.out.println("TNCPlotsDB > strata height: '" + height +"'" );
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
						trueHeight = "";
					}
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		 	return(trueHeight);
	 }
	 
	 
	 /**
	  * method that returns the max height for a stratume 
		* as observerd in a specific plot using as inputs a plot name 
		* and a stratumName
		*
		* @param plot -- the plot
		* @param stratumName -- the stratum
	  */
	 public String getStrataHeight(String plotName, String strataName)
	 {
		 	System.out.println("TNCPlotsDB > strata height lookup: " + strataName );
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
					System.out.println("TNCPlotsDB > strata height: '" + height +"'" );
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
					else 
					{
						trueHeight = "";
					}
				}
				rs.close();
				stmt.close();
			}
			catch( Exception e)
			{
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}
		 	return(trueHeight);
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
				System.out.println("Exception: " + ex + "<BR>");
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
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
			}
			return(code);
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
					//System.out.println(rs.getString(2) );
					scientificNames.addElement(s);
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
				System.out.println("Exception: " + ex + "<BR>");
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
					System.out.println("Exception: " + ex + "<BR>");
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
			System.out.println("Exception: " + ex + "<BR>");
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
			System.out.println("Exception: " + e.getMessage() );
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
			System.out.println("Exception: " + e.getMessage() );
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
					System.out.println("Exception: " + x.getMessage() );
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
			{   // All other types of exceptions
				System.out.println("Exception: " + ex + "<BR>");
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
	//		for (int ii=0; ii<strata.size(); ii++)
	//		System.out.println(" cover: " + db.getSpeciesStrataCover(name, plotName, strata.elementAt(ii).toString() ) );
		}
	
	}
	else
	{
		TNCPlotsDB db = new TNCPlotsDB();
		System.out.println( db.getPlotCodes().toString() );
	}
}
	
	



}
