import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


/**
 * This class represents the TNC Plots DB and will be used as a
 * source for the transformation of the TNC plots database into 
 * the native XML format used by the vegbank system
 */
public class  TNCPlotsDB
{
	private String dbUrl = "jdbc:odbc:test_access"; 
	private Connection con = null;
	
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
	public String projectName = "null";
	public String projectDescription = "null";
	public String plotCode = "null";
	public String plotShape = "null";
	public String test = "null";
	
	public String authorObsCode = "null";
	public String soilDepth = "null";
	
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
				slope = rs.getString(11);
				aspect = rs.getString(12);
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
			ResultSet rs = stmt.executeQuery("select "
			+" ([Plot Code]), "
			+" ([Scientific Name]) "
			+" from ([Plots-Species]) where ([Plot Code]) like '"+plotName+"'");
			
			System.out.println("OK");
			//there should only be one
			while (rs.next()) 
			{
				String s = rs.getString(2);
				//System.out.println(rs.getString(2) );
				scientificNames.addElement(s);
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
					System.out.println("locationCode: " + locationCode);
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
		System.out.println( "community name: " + db.communityName );
		System.out.println( " community code: "+db.communityCode );
		
		System.out.println( "X: "+db.xCoord );
		System.out.println( "Y: "+db.yCoord );
		System.out.println( "UTM Zone: "+db.utmZone );
		System.out.println( "date: "+db.date );
		
		System.out.println("Elevation: "+ db.elevation );
		System.out.println("Slope Gradient: " +db.slope );
		System.out.println("Slope Aspect: "+ db.aspect );
		System.out.println("Surf Geo: "+ db.surfGeo );
		
		System.out.println("scientific names: " + db.scientificNames.toString() );
		
	}
	else
	{
		TNCPlotsDB db = new TNCPlotsDB();
		System.out.println( db.getPlotCodes().toString() );
	}
}
	
	



}
