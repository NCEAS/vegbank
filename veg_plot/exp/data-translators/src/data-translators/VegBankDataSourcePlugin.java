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
 *  '$Date: 2002-01-17 19:08:58 $'
 * 	'$Revision: 1.2 $'
 */
public class VegBankDataSourcePlugin
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
	 * method that returns a vector of plot codes (Plot table Primary keys) 
	 * from the target VegBank Database
	 */
	public Vector getPlotIDs()
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			// Create a Statement so we can submit SQL statements to the driver
			stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select distinct PLOT_ID  from PLOT");
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
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
		System.out.println( db.getPlotIDs().toString() );
	}
	
}
	
	
}
