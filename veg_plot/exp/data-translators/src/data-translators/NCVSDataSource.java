import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


/**
 * plugin to read the NCVS dataset
 *  Authors: 
 *  Release: 
 *	
 *  '$Author: harris $'
 *  '$Date: 2002-01-10 21:48:47 $'
 * 	'$Revision: 1.1 $'
 */
public class NCVSDataSource implements PlotDataSourceInterface
//public class NCVSDataSource
{
	private String dbUrl = "jdbc:odbc:ncvs-datasource"; 
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
	public NCVSDataSource() 
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

			System.out.println("\nConnected to " + dma.getURL() );
			System.out.println("Driver       " + dma.getDriverName() );
			System.out.println("Version      " + dma.getDriverVersion() );
			System.out.println("Catalog      " + con.getCatalog() );
			
		}
		catch (SQLException ex) 
		{
			// Error, a SQLException was generated. Display the error information
			System.out.println ("<BR><B>*** SQLException caught ***</B><BR>");
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
			ex.printStackTrace();
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
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
				x.printStackTrace();
			}
		return(v);
	}

	//returns the placeNames each name can be used to retrieve
	//other information about a place
	public Vector getPlaceNames(String plotName)
	{
		Vector placeNames = new Vector();
		Statement stmt = null;
		try 
		{
			// Create a Statement so we can submit SQL statements to the driver
			stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select Distinct ([NamedPlace]) "
			+" from PlotPlace where PlotID like '"+plotName+"'");
			while (rs.next()) 
			{
				//what is really being returned here is the place id number and not
				//really the place name so this has to be resolved
				String placeId = rs.getString(1);
				String placeName = getTranslatedPlaceId(placeId);
				placeNames.addElement( placeName );
			}
		}
		catch (Exception x) 
		{
			System.out.println("Exception: " + x.getMessage() );
			x.printStackTrace();
		}
		return(placeNames);
	}
	
	//this is a utility method that returns a place name from a place
	// id 
	private String getTranslatedPlaceId(String placeId)
	{
		String placeName = null;
		Statement stmt = null;
		try 
		{
			// Create a Statement so we can submit SQL statements to the driver
			stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select  ([PlaceName]) "
			+" from PlaceNames where PlaceID like '"+placeId+"'");
			while (rs.next()) 
			{
				placeName = rs.getString(1);
			}
		}
		catch (Exception x) 
		{
			System.out.println("Exception: " + x.getMessage() );
			x.printStackTrace();
		}
		return(placeName);
	}
	
	//utility method that returns a placeId based on an input place name
	private String getTranslatedPlaceName(String placeName)
	{
		String placeId = null;
		Statement stmt = null;
		try 
		{
			// Create a Statement so we can submit SQL statements to the driver
			stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select  ([PlaceID]) "
			+" from PlaceNames where PlaceName like '"+placeName+"'");
			while (rs.next()) 
			{
				placeId = rs.getString(1);
			}
		}
		catch (Exception x) 
		{
			System.out.println("Exception: " + x.getMessage() );
			x.printStackTrace();
		}
		return(placeId);
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
	
	
	
	//retuns the unique names of all the strata in a given plot -- as of 20010102,
	//I do not understand how bob lists the unique plants.  It appears that he
	// recognizes the plants in the tree and herb layers b/c those are the names
	// of the tables where the data are stored
	public Vector getUniqueStrataNames(String plotName)
	{
		Statement stmt = null;
		Vector v = new Vector();
			try 
			{
				System.out.println("hit!");
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([StratumTypeID]) "
				+" from StratumPlot where ([PlotID]) like '"+plotName+"'");
				
				while (rs.next()) 
				{
					String stratumId = rs.getString(1);
					String stratumName = getTranslatedStratumID( stratumId);
					v.addElement( stratumName );
					System.out.println("strata: " + stratumName);
				}
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
				x.printStackTrace();
			}
		return(v);
	}
	

	//utility method that returns the translated name for a stratumTypeId, I think
	//that these are fixed but am not sure so I am querying the tables in the
	// access database
		 private String getTranslatedStratumID( String stratumId)
		 {
			 Statement stmt = null;
				String stratumName = null;
				try 
				{
					// Create a Statement so we can submit SQL statements to the driver
					stmt = con.createStatement();
				
					//create the result set
					ResultSet rs = stmt.executeQuery(" select ([StratumName]) "
					+" from StratumType where ([StratumTypeId]) like '"+stratumId+"'" );
				
					while (rs.next()) 
					{
						stratumName = rs.getString(1); 
					}
				}
				catch (Exception x) 
				{
					System.out.println("Exception: " + x.getMessage() );
					x.printStackTrace();
				}
			return(stratumName);
		 }
	
	//utility method that returns the translated strataId based on a stratumName
	// so that the stratumid can be used to query for heights and coverages for
	// that stratum
	 private String getTranslatedStratumName(String stratumName)
		 {
			 Statement stmt = null;
				String stratumId = null;
				try 
				{
					// Create a Statement so we can submit SQL statements to the driver
					stmt = con.createStatement();
				
					//create the result set
					ResultSet rs = stmt.executeQuery(" select ([StratumTypeId]) "
					+" from StratumType where ([StratumName]) like '"+stratumName+"'" );
				
					while (rs.next()) 
					{
						stratumId = rs.getString(1); 
					}
				}
				catch (Exception x) 
				{
					System.out.println("Exception: " + x.getMessage() );
					x.printStackTrace();
				}
			return(stratumId);
		 }
	
	
	
	/**
	 *
	 */
	 private void getProjectData()
	 {
		 this.projectName = "tncplots"+this.placeName;
		 this.projectDescription = "tncplotsDes"+this.placeName;
	 }
	
	// see the interface for method descriptions
	public String getProjectName(String plotName)
	{
		 Statement stmt = null;
		 String projectName = null;
		try 
		{
		 
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([ProjectName]) "
			+" from ([Project]) where ([Project_ID]) = "
			+" (select project from All_Plots where Project_Team_Plot like '"+plotName+"')" );
			
			while (rs.next()) 
			{
				projectName = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(projectName);
	}
	
	// see the interface for method descriptions
	public String getProjectDescription(String plotName)
	{
		 Statement stmt = null;
		 String projectDescription = null;
		try 
		{
		 
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([description]) "
			+" from ([Project]) where ([Project_ID]) = "
			+" (select project from All_Plots where Project_Team_Plot like '"+plotName+"')" );
			
			while (rs.next()) 
			{
				projectDescription = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(projectDescription);
	}
	
	
	
	
	// see the interface for method descriptions
	public Vector getProjectContributors(String plotName)
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			
			String query = " select ([PartyID])  "
			+" from ([PlotContributor])  where ([Project-Team-Plot]) = '"+plotName+"'";
			//System.out.println(query);
			ResultSet rs = stmt.executeQuery( query);
			while (rs.next()) 
			{
				String partyId = rs.getString(1);
				String partyName = getTranslatedPartyID(partyId);
				//System.out.println( partyName );
				v.addElement(  partyName );
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
	
	//utility method that translates a PartyID into a fullName
	private String getTranslatedPartyID(String partyId)
	{
		 Statement stmt = null;
		 String partyName  = null;  //the full name of a party
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
			
				//create the result set
				ResultSet rs = stmt.executeQuery(" select ([FullName]) "
				+" from Party where ([PartyID]) like '"+partyId+"'" );
			
				while (rs.next()) 
				{
					partyName = rs.getString(1); 
				}
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
				x.printStackTrace();
			}
		return(partyName);
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
			StringTokenizer t = new StringTokenizer(contributorWholeName, ",");
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
			StringTokenizer t = new StringTokenizer(contributorWholeName, ",");
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
		String orgName = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			
			String query = " select ([OrganizationNameAtPartic])  "
			+" from ([Party]) where ([FullName]) like '"+contributorWholeName+"'";
			//System.out.println(query);
			ResultSet rs = stmt.executeQuery( query);
			while (rs.next()) 
			{
				orgName = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(orgName);
	}
	
	//retuns the person's contactinstructions based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
		return("Contact Robert Peet for contact instructions: uniola@email.unc.edu");
	}
	//retuns the person's phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
		return("Contact Robert Peet for Phone Number: uniola@email.unc.edu");
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
		String email = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			String query = " select ([E-mail])  "
			+" from ([Party]) where ([FullName]) like '"+contributorWholeName+"'";
			//System.out.println(query);
			ResultSet rs = stmt.executeQuery( query);
			while (rs.next()) 
			{
				email = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(email);
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
		 Statement stmt = null;
		 String startDate = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([startDate]) "
			+" from ([Project]) where ([Project_ID]) = "
			+" (select project from All_Plots where Project_Team_Plot like '"+plotName+"')" );
			
			while (rs.next()) 
			{
				startDate = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(startDate);
	}
	
	// see the interface for method descriptions
	public String getProjectStopDate(String plotName)
	{
		Statement stmt = null;
		String stopDate = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([stopDate]) "
			+" from ([Project]) where ([Project_ID]) = "
			+" (select project from All_Plots where Project_Team_Plot like '"+plotName+"')" );
			
			while (rs.next()) 
			{
				stopDate = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(stopDate);
	}
	
	// see the interface for method descriptions
	public String getPlotCode(String plotName)
	{
		return(plotName);
	}
	
	// see the interface for method descriptions
	public String getXCoord(String plotName)
	{
		Statement stmt = null;
			try 
			{
				// Create a Statement so we can submit SQL statements to the driver
				stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery("select "
				+" ([UTM-E]) "
				+" from File1_Plot_Summary where ([Project-Team-Plot]) like '"+plotName+"'");
				while (rs.next()) 
				{
					xCoord= rs.getString(1);
				}
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
				x.printStackTrace();
			}
		return(this.xCoord);
	}
	
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
				+" ([UTM-N]) "
				+" from File1_Plot_Summary where ([Project-Team-Plot]) like '"+plotName+"'");
				while (rs.next()) 
				{
					yCoord= rs.getString(1);
				}
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
				x.printStackTrace();
			}
		return(this.yCoord);
	}
	
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
				+" from File1_Plot_Summary where ([Project-Team-Plot]) like '"+plotName+"'");
				while (rs.next()) 
				{
					this.utmZone = rs.getString(1);
				}
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
				x.printStackTrace();
			}
		return(this.utmZone);
	}
	
	// see the interface for method descriptions
	public String getPlotShape(String plotName)
	{
		this.plotShape = "rectangular";
		return(this.plotShape);
	}
	
// see the interface for method descriptions
	public String getPlotArea(String plotName)
	{
/*
		Statement stmt = null;
		String xDim = null;
		String yDim = null;
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
			}
			catch (Exception x) 
			{
				System.out.println("Exception: " + x.getMessage() );
				x.printStackTrace();
			}
*/
		return("1000");
	}
	
		// see the interface for method descriptions
	public String getCommunityName(String plotName)
	{
		return("vegcommunity");
	}
	
		// see the interface for method descriptions
	public String getState(String plotName)
	{
		return("CA");
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
			+" ([Hydrologic Regime]) "
			+" from ([File2_Site_Attributes]) where ([Project-Team-Plot]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				this.hydrologicRegime = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
			//System.out.println("hydroregime: "+ this.hydrologicRegime);
		 return(this.hydrologicRegime);
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
			+" ([Topographic Position Class]) "
			+" from ([File2_Site_Attributes]) where ([Project-Team-Plot]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				this.topoPosition = rs.getString(1);
			}
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
			+" from ([File2_Site_Attributes]) where ([Project-Team-Plot]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				String s = rs.getString(1);
			//	System.out.println("asp: " + s);
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
			+" from ([File2_Site_Attributes]) where ([Project-Team-Plot]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				String s = rs.getString(1);
				this.slopeGradient = s;
			}
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
			+" ([SurfaceGeology]) "
			+" from ([File2_Site_Attributes]) where ([Project-Team-Plot]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				this.surfGeo = rs.getString(1);
				//System.out.println("geo: " + this.surfGeo);
			}
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
	
	// see the interface for method descriptions
	public String getAuthorLocation(String plotName)
	{
		 Statement stmt = null;
		 String location = null;
		try 
		{
		 
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([General Location]) "
			+" from ([File1_Plot_Summary]) where ([Project-Team-Plot]) like '"+plotName+"'");
			while (rs.next()) 
			{
				location = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(location);
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
			+" from ([File2_Site_Attributes]) where ([Project-Team-Plot]) like '"+plotName+"'");
			//there should only be one
			while (rs.next()) 
			{
				this.elevation = rs.getString(1);
			}
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
	 * method to return the cover for a given stratum for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String stratumName)
	 {
		Statement stmt = null;
		String percentCover = null;
		try 
		{
			//obtain the correct strataId for use in the query for the coverage
		 	String stratumId = getTranslatedStratumName(stratumName);
		 
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([StratumPlotCoverPercent]) "
			+" from ([StratumPlot]) where ([PlotID]) like '"+plotName+"'"
			+" and StratumTypeId = "+stratumId+"");
			
			while (rs.next()) 
			{
				percentCover = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(percentCover);
	 }
	 
	  /**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataBase(String plotName, String stratumName)
	 {
		 Statement stmt = null;
		 String base = null;
		try 
		{
			//obtain the correct strataId for use in the query for the coverage
		 	String stratumId = getTranslatedStratumName(stratumName);
		 
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([StratumPlotMinHt]) "
			+" from ([StratumPlot]) where ([PlotID]) like '"+plotName+"'"
			+" and StratumTypeId = "+stratumId+"");
			
			while (rs.next()) 
			{
				base = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(base);
	 }
	 
	/**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataHeight(String plotName, String stratumName)
	 {
		  Statement stmt = null;
		  String top = null;
		try 
		{
			//obtain the correct strataId for use in the query for the coverage
		 	String stratumId = getTranslatedStratumName(stratumName);
		 
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  ([StratumPlotMaxHt]) "
			+" from ([StratumPlot]) where ([PlotID]) like '"+plotName+"'"
			+" and StratumTypeId = "+stratumId+"");
			
			while (rs.next()) 
			{
			top = rs.getString(1);
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(top);
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
//		 getSpeciesInfo( plotName );
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
					System.out.println("slope: " + slope);
					slopeAspect = rs.getString(12);
					surfGeo = rs.getString(13);
					locationCode = rs.getString(14);
					soilDepth = rs.getString(15);
				}
				//create the obscode by combining the plot with the date
				this.authorObsCode = plotCode+date;
			}
			catch (SQLException ex) 
			{
				// Error, a SQLException was generated. Display the error information
				System.out.println ("<BR><B>*** SQLException caught ***</B><BR>");
				try 
				{  
					System.out.println("Warning =   " + stmt.getWarnings() ); 
				}
				catch (Exception x) 
				{
					x.printStackTrace();
				}
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
				ex.printStackTrace();
			}		
		}
		
		
		/**
		 * method that returns a vector with the unique plant 
		 * taxa names for a given plot -- this method is defined
		 * in the plugin interface
		 */
		 public Vector getPlantTaxaNames(String plotName)
		 {
			 
			 	Statement stmt = null;
				Vector v = new Vector();
				try 
				{
					// Create a Statement so we can submit SQL statements to the driver
					stmt = con.createStatement();
				
					//create the result set using both the herb data
					ResultSet rs = stmt.executeQuery(" select distinct ([SppID]) "
					+" from HerbData where ([PlotID]) like '"+plotName+"'" );
				
					while (rs.next()) 
					{
						String sppId = rs.getString(1); //call this one a herb
						String binomial = getTranslatedSppID( sppId );
						//System.out.println("herb: " + binomial );
						v.addElement( binomial );
					}
				}
				catch (Exception x) 
				{
					System.out.println("Exception: " + x.getMessage() );
					x.printStackTrace();
				}
			return(v);
		 }
		 
		 //utility method that will translate the sppID into a latin binomial.  This
		 //should be done as a sub-query but I cannot get subqueries to work with ms
		 //access
		 private String getTranslatedSppID( String sppId)
		 {
			 Statement stmt = null;
			String binomial = null;
				try 
				{
					// Create a Statement so we can submit SQL statements to the driver
					stmt = con.createStatement();
				
					//create the result set
					ResultSet rs = stmt.executeQuery(" select ([Genus]), ([Species])  "
					+" from CarSpList where ([SppID]) like '"+sppId+"'" );
				
					while (rs.next()) 
					{
						String genus = rs.getString(1); //the genus
						String species = rs.getString(2); //the genus
						binomial = genus+" "+species;
					}
				}
				catch (Exception x) 
				{
					System.out.println("Exception: " + x.getMessage() );
					x.printStackTrace();
				}
			return(binomial);
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
					ex.printStackTrace();
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
			ResultSet rs = stmt.executeQuery("select distinct ([Project_team_plot]) "
			+" from all_plots");
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				//System.out.println(rs.getString(1) );
				v.addElement(s);
			}
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
			ex.printStackTrace();
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
		 /*
		 Statement stmt = null;
		try 
		{
			//System.out.println("plant name: "+ plantName +" plot name: " + plotName);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select "
			+" ([Stratum]) "
			+" from ([Plots-Species]) where ([Plot Code]) like '"+plotName+"' and ([Scientific Name]) like '"+plantName+"'");
			//there should only be one
			while (rs.next()) 
			{
				String curStrata = rs.getString(1);
				//System.out.println( curStrata );
				v.addElement( curStrata );
			}
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		*/
		 return(v);
	 }
	 
	 
	 /**
	 * method that returns the strata cover for a given plant, strata, and 
	 * plot
	 */
	 public String getTaxaStrataCover(String plantName, String plotName, String stratum)
	 {
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
				v.addElement( s);
			}
			//make sure that there are not too many values in the vector
			if ( v.size() > 1)
			{
				System.out.println( " warning - more than one cover value found for a taxa strata pair");
			}
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
		NCVSDataSource db = new NCVSDataSource();
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
		NCVSDataSource db = new NCVSDataSource();
		System.out.println( db.getPlotCodes().size() );
	}
}
	
	



}
