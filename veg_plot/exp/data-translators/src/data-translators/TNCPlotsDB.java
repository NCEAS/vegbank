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
 *  '$Date: 2001-12-06 02:07:58 $'
 * 	'$Revision: 1.3 $'
 */
public class TNCPlotsDB implements PlotDataSourceInterface
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
		}
	}
	
	//returns the project name 
	public String getProjectName()
	{ return(projectName); }
	//returns the project description
	public String getProjectContributor()
	{ return(projectContributor); }
	//returns the project contributor
	public String getProjectDescription()
	{ return(projectDescription); }
	//returns the plot code for the current plot
	public String getPlotCode()
	{ return(plotCode); }
	//returns the easting 
	public String getXCoord()
	{ return(yCoord); }
	//returns the northing
	public String getYCoord()
	{ return(xCoord); }
	//returns the geographic zone
	public String getUTMZone()
	{ return(utmZone); }
	//returns the plot shape
	public String getPlotShape()
	{ return(plotShape); }
	//returns the plot area
	public String getPlotArea()
	{ return(plotArea); }
	//returns the state for the current plot
	public String getState()
	{ return(state); }
	
	//returns the state for the current plot
	public String getCommunityName()
	{ return(communityName); }
	
	//retuns the topo position
	public String getTopoPosition()
	{ return(topoPosition); }
	//returns the hydrologic regime
	public String getHydrologicRegime()
	{ return(hydrologicRegime); }
	//returns the surface geology
	public String getSurfGeo()
	{ return(surfGeo); }
	//returns the country
	public String getCountry()
	{ return("USA"); }
	//returns the slope aspect
	public String getSlopeAspect()
	{ return(slopeAspect); }
	//returns the slope gradient
	public String getSlopeGradient()
	{ return(slopeGradient); }
	//retuns the unique names of all the strata in a given plot
	public Vector getUniqueStrataNames(String plotName)
	{
		return(strataNames);
	}
	
	
	/**
	 * method that returns the project-level information including the 
	 * project name and the project description, two variables that are
	 * not really incledued in the tnc plot database access file
	 * and for this reason this method creates a project name based on the 
	 * location of the plots
	 */
	 private void getProjectData()
	 {
		 this.projectName = "tncplots"+this.placeName;
		 this.projectDescription = "tnc project descripion";
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
				System.out.println( uniquePlantNameNumber + " unique plant names");
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
				v.addElement( rs.getString(1));
			}
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
		 Vector v = new Vector();
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
				v.addElement( rs.getString(1));
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
		}
		 return(v.elementAt(0).toString());
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
