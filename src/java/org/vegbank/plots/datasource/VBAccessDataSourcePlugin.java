package org.vegbank.plots.datasource;

import java.util.*;
import java.sql.*;


/**
 * plugin to allow access to plot data stored in the VegBank - plots 
 * database 
 *
 *  Release: @release@
 *	
 * 	'$Author: mlee $'  
 * 	'$Date: 2006-06-30 20:12:22 $' 
 * 	'$Revision: 1.4 $'
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 
//public class VBAccessDataSourcePlugin
public class VBAccessDataSourcePlugin extends VegBankDataSourcePlugin implements PlotDataSourceInterface 
{
	private String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
	private String dbUrl = "jdbc:odbc:vegbank_access";
	private String dbUser = "vegbank";
	private Connection con = null;
	
	
	public VBAccessDataSourcePlugin()
	{
		super("msaccess");
		try 
		{
			con = super.con;
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex + "<BR>");
			ex.printStackTrace();
		}
	}


 	
	
  /**
	 * method to return the taxa code from a data source using as input
	 * the scientific plant name -- or the plant name that comes from
	 * the 'getPlantTaxaNames' method
	 *
	 * @param plantName -- the scientific plantName
	 * @see DBinsertPlotSource.insertTaxonObservation() -- calls this 
	 * 	method
	 */
	public String getPlantTaxonCode(String plantName)
	{
		String code = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append(" select PLANTCODE from PLANTCONCEPT where PLANTNAME like '"+plantName+"'");
			System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
			ResultSet rs = stmt.executeQuery( sb.toString() );	
			while (rs.next()) 
			{
				code = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
		catch (java.lang.Exception ex) 
		{   
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
	}

	/**
	 * this method returns the plotid's of all the plots stored 
	 * in the VegBank MDB file.  The name of this class seems
	 * strange because one would expect that the name would be 
	 * the name of the plot that the author used to describe the 
	 * plot, but this gives the 'unique' names from the perspective 
	 * of the database
	 */
	public Vector getPlotNames()
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
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
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			+" from STRATUMTYPE " );
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
					Vector observationIDs = this.getObservationIDs(plotName);
					stmt = con.createStatement();
					StringBuffer sb = new StringBuffer();
					sb.append("select plantname from plantname where plantname_id in ( ");
					sb.append("select distinct plantname_id from TAXONOBSERVATION where OBSERVATION_ID = ");
					sb.append((String)observationIDs.elementAt(0)+" )" );
					System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
					ResultSet rs = stmt.executeQuery( sb.toString() );	
					while (rs.next()) 
					{
						String s = rs.getString(1);
						v.addElement(s);
					}
				}
				catch (java.lang.Exception ex) 
				{   
					System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
					ex.printStackTrace();
				}
				return(v);
		 }
		 


	/**
	 * method that returns the strata in which the input plant
	 * exists within the input plot
	 * @param plantName -- the name of the plant
	 * @param plotName -- the name of the plot
	 * @return v -- a vector containing the names of the strata for
	 * 	which this plant was collected
	 */
	 public Vector getTaxaStrataExistence(String plantName, String plotName)
	 {
		 Vector v = new Vector();
		 	String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMNAME from STRATUMTYPE WHERE STRATUMTYPE_ID in ( ");
				sb.append("select STRATUMTYPE_ID FROM STRATUM WHERE OBSERVATION_ID in ( ");
				sb.append("select observation_id from OBSERVATION where PLOT_ID = " + plotName+" ) and STRATUM_ID in ");
				sb.append(" ( select STRATUM_ID from stratumcomposition where TAXONOBSERVATION_ID in ( ");
				sb.append(" select TAXONOBSERVATION_ID from TAXONOBSERVATION where PLANTNAME_ID = ( ");
				sb.append(" select PLANTNAME_ID from PLANTNAME where PLANTNAME LIKE '"+plantName+"' ) ) ) ) ");
				System.out.println("VBAceessDataSource > query: " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );
				
				int cnt = 0;
				while (rs.next()) 
				{
					cnt++;
					s = rs.getString(1);
					v.addElement(s);
				}
				System.out.println("VBAceessDataSource > result set size: " + cnt);
				System.out.println("VBAceessDataSource > result set contents: " + v.toString());
				rs.close();
				stmt.close();
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
				sb.append("select STRATUMCOVER from STRATUM where STRATUMTYPE_ID in (");
				sb.append(" select DISTINCT STRATUMTYPE_ID from STRATUMTYPE where STRATUMNAME LIKE '"+strataName+"') and OBSERVATION_ID = (");
				sb.append(" select observation_id from OBSERVATION where PLOT_ID = " + plotName+" )");
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
				sb.append("select STRATUMBASE from STRATUM where STRATUMTYPE_ID in (");
				sb.append(" select DISTINCT STRATUMTYPE_ID from STRATUMTYPE where STRATUMNAME LIKE '"+strataName+"') and OBSERVATION_ID = (");
				sb.append(" select observation_id from OBSERVATION where PLOT_ID = " + plotName+" )");
				//System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
						// IF THERE IS A NULL PASS A NUMERIC VALUE, BC IT HAS TO BE ONE
				if ( s == null )
				{
						s = "0.0";
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
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
				sb.append("select STRATUMHEIGHT from STRATUM where STRATUMTYPE_ID in (");
				sb.append(" select DISTINCT STRATUMTYPE_ID from STRATUMTYPE where STRATUMNAME LIKE '"+strataName+"') and OBSERVATION_ID = (");
				sb.append(" select observation_id from OBSERVATION where PLOT_ID = " + plotName+" )");
				//System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
				// IF THERE IS A NULL PASS A NUMERIC VALUE, BC IT HAS TO BE ONE
				if ( s == null )
				{
						s = "0.0";
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
				ex.printStackTrace();
			}
			return(s);
		}
		
	 
	 /**
	 * method that returns the strata cover for a given plant, strata, and 
	 * plot.  Notice that the inputs into this method include the plantName, plotName
	 * (the plotid in this case) and the stratum(name).  The MS Access version allows
	 * a stratum with the same name to be used multiple times in the database which 
	 * causes a problem with this method.  The current fix is to edit the mdb file 
	 * and increment any stratum whose name has already been used; there should be 
	 * a method that can fix this so that the user does not have to do it by hand.
	 */
	 public String getTaxaStrataCover(String plantName, String plotName, String stratum)
	 {
		 	StringBuffer sb = new StringBuffer();
			String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				System.out.println("VBAccessDataSourcePlugin > querying cover for: " + plantName+" "+plotName+" "+stratum );
				
				sb.append("select TAXONSTRATUMCOVER from STRATUMCOMPOSITION where TAXONOBSERVATION_ID in ( ");
				sb.append(" select TAXONOBSERVATION_ID from TAXONOBSERVATION where PLANTNAME_ID = ( ");
				sb.append(" select PLANTNAME_ID from PLANTNAME where PLANTNAME LIKE '"+plantName+"' ) )");
				sb.append(" and ");
				sb.append(" STRATUM_ID in ( select STRATUM_ID from STRATUM where STRATUMTYPE_ID = ( ");
				sb.append(" select STRATUMTYPE_ID from STRATUMTYPE WHERE STRATUMNAME like '"+stratum+"') and OBSERVATION_ID = ");
				sb.append(" (select observation_id from OBSERVATION where PLOT_ID = " + plotName+" ) )");
				//System.out.println("VBAccessDataSourcePlugin > query " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
				rs.close();
				stmt.close();
				System.out.println("VBAccessDataSourcePlugin > getTaxaStrataCover result: " + s);
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
				System.out.println("sql: " + sb.toString() );
				ex.printStackTrace();
			}
			return(s);
	 }
	 
	/**
	 * method that retuns the cummulative cover accoss all strata for a given 
	 * plant taxa in a given plot
	 */
	public String getCummulativeStrataCover( String plantName, String plotName )
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select TAXONCOVER from TAXONOBSERVATION where OBSERVATION_ID = ");
			sb.append(" (select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+" ) ");
			sb.append(" and PLANTNAME_ID = ");
			sb.append(" ( select PLANTNAME_ID from PLANTNAME where PLANTNAME like '"+plantName+"') ");
			
			ResultSet rs = stmt.executeQuery( sb.toString() );
			while (rs.next()) 
			{
				 s = rs.getString(1);
				 //if we get null returned then make the value = '999.99'
				 if ( s == null )
				 {
					 s = "999.99";
				 }
			}
			stmt.close();
			//rs.close();
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
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
				System.out.println ("VBAccessDataSourcePlugin > ErrorCode: " + ex.getErrorCode () );
				System.out.println ("VBAccessDataSourcePlugin > SQLState:  " + ex.getSQLState () );
				System.out.println ("VBAccessDataSourcePlugin > Message:   " + ex.getMessage () );
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
		catch (java.lang.Exception x) 
		{   
			// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + x + "<BR>");
			x.printStackTrace();
		}
	}


	/*
	 *  Convert a String to a Boolean
	 */
	 private boolean getBooleanFromString(String s) 
	 {
			boolean retVal = false; 
			if (s ==  "-1") 
			{
				retVal = true;
			} 
			else if ( s == "0")
			{
					retVal = false;
			}
	 		
			return retVal;
	 }	
	
	
	
	
/**
 * main method for testing --
 */
	public static void main(String[] args)
	{
		if (args.length == 1) 
		{
			String plotName = args[0];
		//	VBAccessDataSourcePlugin db = new VBAccessDataSourcePlugin("msaccess");
		//	System.out.println(" VBAccessDataSourcePlugin > getting info for: " + plotName );
		}
		else
		{
		//	VBAccessDataSourcePlugin db = new VBAccessDataSourcePlugin("msaccess");
		//	System.out.println( db.getPlotCodes().toString() );
		//	System.out.println( db.getPlotIDs().toString() );
		}
	}
	
	
}
