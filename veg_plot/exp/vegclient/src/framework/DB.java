/** 
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:41 $'
 * 	'$Revision: 1.1 $'
 */
package vegclient.framework;

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

import org.w3c.dom.Node;

import vegclient.framework.*;

/**
 * this is a class that is used to load the database by parsing an xml document
 * that is consistent with the  veg plot project
 */
public class DB
{

	// constructor -- define as static the LocalDbConnectionBroker
	// so that methods called by this class can access the 'local' 
	// pool of database connections
	static LocalDbConnectionBroker connectionBroker = new LocalDbConnectionBroker(); 

	public Node plotNode;
	public Connection conn;
	public Statement query = null;
	private PrintWriter out;
	private String logFile = "loadlog.txt";
	
	//refers to the project for which the data is to be used in the db class
	public VegProject project;
	//refers to the given plot, a sub-set of data of the above project, for use in
	//the class
	public Plot plot;

	// variable that are quite general to the class and are going to be used by a
	// number of the methods
	String projectName = null;
	String projectDescription = null;
	String plotName = null;
	Vector plotNameList = new Vector();
	
	//these variables are returned from the database and are used throughout the
	// class
	int plotId;
	int namedPlaceId;
	int plotObservationId;
	int strataId;
	int taxonObservationId;


	// the name of the plots project xml file containing the plot information
	// to be stored in the database
	public String filename = new String();
	
	//debug string buffer
	StringBuffer debug = new StringBuffer();


	//filename is the xml file that contains the project data and the plots
	public DB(String filename) throws FileNotFoundException
  {
		try
    {
			//construct the project class with the project file
			project = new VegProject(filename);
		
			//initialize the general project parameters
			projectName = project.getProjectName();
			projectDescription = project.getProjectDescription();
			plotNameList = project.getPlotNames();
		
			//initialize the database connection manager
			connectionBroker.manageLocalDbConnectionBroker("initiate");
			conn = connectionBroker.manageLocalDbConnectionBroker("getConn");
		
		
    	out = new PrintWriter(new FileWriter(logFile));
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		 
	}
	
	/**
   * the main routine used to test the DB class which interacts with the 
	 * vegclass database.
   * <p>
   * Usage: java DB <filename plotName action>
   *
   * @param filename the filename to be loaded into the database
   * @param plotName the name of the plot as it is recorded in the
	 * authorPlotCode tag
   * @param action
   */
  static public void main(String[] args) 
	{
  	try 
		{
			//for now just allow the user to insert the plot
			if (args.length == 0) 
			{
				System.out.println("Usage: java DB [XML projectFile] [plotName] [action] \n"
				+"actions: insertPlot ");
				System.exit(0);
			}
			//insert an individual plot
			else if ( args.length == 3 )
			{
				String projectFile=args[0];
				String plotName=args[1];
				String action=args[2];
				
				DB db = new DB(projectFile);
				db.insertPlot(plotName);
			}
			//load the entire package
			else if (  args.length == 2 )
			{
				String projectFile=args[0];
				String action=args[1];
				
				DB db = new DB(projectFile);
				db.insertPlotPackage();
			}
		  //unrecognized 
			else
			{
				System.out.println( );
			}
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * method that loads an entire plot package
	 *
	 * @param plotNames -- a vector that holds all the plotNames
	 */
	 public void insertPlotPackage()
	{
		try 
		{
			//for (int i =0; i < plotNameList.size(); i++)
			for (int i =0; i < 3; i++)
			{
				//insert one plot at a time
				insertPlot( plotNameList.elementAt(i).toString() );
			}
			//close the connections and destroy the pooled connections
			conn.close();
			connectionBroker.manageLocalDbConnectionBroker("destroy");
			
			//print the logfile
			out.println( debug.toString() );
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
	
	//method to initiate the insertion of a plot, there are a series of private
	//methods that will be called to actually get the data into the database
	public void insertPlot(String plotName)
	{
		try 
		{
			//this boolean determines if the plot should be commited or rolled-back
			boolean commit = true;
			
			//separate this plot from the project file into a separate file
			plotNode=project.getPlot(plotName);
			
			//fail if no not get anything back
			if (plotNode == null) 
			{
				System.out.println("no plot returned from praser: exiting");
				System.exit(0);
			}
			
			//save it to a file
			project.savePlot(plotNode, "foo.xml");
			plot = new Plot("foo.xml");
			
			//set the auto commit option on the connection to false after getting a
			// new connection from the pool
			conn = connectionBroker.manageLocalDbConnectionBroker("getConn");
			conn.setAutoCommit(false);
			
			
			//see if the project in which this plot is a member exists in the database
			if (projectExists(projectName) == true )
			{
				//get the project id value
				int projectId = getProjectId(projectName);
				insertNamedPlace();
				if (	insertStaticPlotData() == false ) 
				{	
					System.out.println("static data: "+commit);
					commit = false;
				}
				else
				{
					if ( insertPlotObservation() ==false )
					{
						System.out.println("obs data: "+commit);
						commit = false;
					}
					else
					{
						if ( insertStrata() == false )
						{	
							System.out.println("strata data: "+commit);
							commit = false;
						}
						else
						{
							if ( insertTaxonObservations() == false )
							{
								System.out.println("taxonObse data: "+commit);
								commit = false;
							}
						}
					}
				}
				
			}
			//else insert a new project and then the plot information
			else 
			{
				//insert the basis for the project, the project id is auto updated in 
				//the insert project method 			
				insertProject(projectName, projectDescription);
				int projectId = getProjectId(projectName);
				insertNamedPlace();
				if (	insertStaticPlotData() == false ) 
				{	
					System.out.println("static data: "+commit);
					commit = false;
				}
				else
				{
					if( insertPlotObservation() == false )
					{
						System.out.println("obs data>: "+commit);
						commit = false;
					}
					else
					{
						if ( insertStrata() == false )
						{
							System.out.println("strata data: "+commit);
							commit = false;
						}
						else
						{
							if ( insertTaxonObservations() == false )
							{
								System.out.println("taxon obs data: "+commit);
								commit = false;
							}
						}
					}
				}
			}
			
			System.out.println("success?: "+ commit);
			
			//close the connections
			//conn.close();
			if ( commit == true) 
			{
				conn.commit();
				//conn.close();
				debug.append( "INSERTION SUCCESS: \n" );
			}
			else
			{
				conn.rollback();
				debug.append( "INSERTION FAILURE: \n" );
				//conn.close();
			}
			
			
			
			//connectionBroker.manageLocalDbConnectionBroker("destroy");
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	//method to update the strata composition tables -- this method should only
	// ever be called from the 'insertTaxonObservation' method
	private int insertStrataComposition(String stratumType, String percentCover)
	{
		try 
		{
			StringBuffer sb = new StringBuffer();
			
			//get the correct strataId from the database corresponding to the
			// 'stratumType' and the observation id
			int strataId = getStrataId(plotObservationId, stratumType );
			//System.out.println("CORRECT STRATA ID: "+ strataId );
			
			sb.append("INSERT INTO STRATACOMPOSITION (TAXONOBSERVATION_ID, "
			+" CHEATSTRATUMTYPE, STRATA_ID, percentCover) "
			+" values("+taxonObservationId+", '"+stratumType+"', "+strataId
			+", "+percentCover+" )" );
			
			//do the insertion
			Statement insertStatement = conn.createStatement();
			insertStatement.executeUpdate(sb.toString());
		
		}
		catch (SQLException sqle)
		{
			System.out.println("Caught SQL Exception: "+sqle.getMessage() ); 
			sqle.printStackTrace();
		}
		
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		return(namedPlaceId);
	}
	
	
/**
 *  Method that returns a strataId value that corresponds to a given stratumType
 * and an observation id value 
 *
 * @param obseravtionId - the taxonObservation value for a given recognition of 
 *	a taxon 
 * @param strataType - the type of strata in which the taxon is found
 *
 */
	private int getStrataId (int plotObservationId, 
		String stratumType ) 
	{
		int strataId = -999;
		try 
		{		
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("SELECT STRATA_ID from STRATA where OBS_ID = "
			+plotObservationId+" and stratumType like '%"
			+stratumType+"%'");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			while ( rs.next() ) 
			{
				strataId = rs.getInt(1);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Caught Exception  "
			+" " 
			+ e.getMessage());
		}
		return(strataId);
	}


	
	//method to add the taxonObservation data to the database
	// if the method fails it will return false and the plot will be rolled 
	//backe
	private boolean insertTaxonObservations()
	{
		boolean successfulCommit = true;
		try 
		{
			//get the number of taxonObservations
			int numberOfTaxonObservations = plot.getNumberOfTaxonObservations();
			for (int i =0; i < numberOfTaxonObservations; i++)
			{
				StringBuffer sb = new StringBuffer();
				//get the strataId number
				taxonObservationId = getNextId("taxonObservation");
				//get the hashtable that contains all  the starta info for the given
				// strata
				Hashtable ht = plot.getTaxonObservation( i );
				String authorNameId = (String)ht.get("authorNameId");
				String strataType = (String)ht.get("strataType");
				String percentCover = (String)ht.get("percentCover");
				percentCover ="0";  //a hack to get data to load
				//insert the strata values
				sb.append("INSERT into TAXONOBSERVATION (taxonObservation_id, obs_id, "
					+" authorNameId, cumStrataCoverage) "
					+" values("+taxonObservationId+", "+plotObservationId+", '"
					+authorNameId+"', "+percentCover+" )" );
				Statement insertStatement = conn.createStatement();
				insertStatement.executeUpdate(sb.toString());
				
				//now update the strata composition table
				insertStrataComposition( strataType, percentCover );
				
				//System.out.println("inserted taxonObservation: " + ht);
			}			
		
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
			//System.exit(0);
		}
		return( successfulCommit );
	}
	
	
	/**
	 * method that returns false if the strataElements cannot be loaded to the
	 * database
	 *
	 */
	private boolean insertStrata()
	{
		try 
		{
			
			//get the names of the recognized strata
			Vector strataTypes = plot.getStrataTypes();
			for (int i =0; i < strataTypes.size(); i++)
			{
				StringBuffer sb = new StringBuffer();
				//get the strataId number
				strataId = getNextId("strata");
				//get the hashtable that contains all  the starta info for the given
				// strata
				Hashtable ht = plot.getStrata( strataTypes.elementAt(i).toString() );
				String type = (String)ht.get("stratumType");
				String cover = (String)ht.get("stratumCover");
				String height = (String)ht.get("stratumHeight");
				
				//set up the debugging
				debug.append("stratumType: "+ type+"\n");
				debug.append("stratumCover: "+cover+"\n");
				debug.append("stratumHeight: "+height+"\n");
			
				//System.out.println( debug.toString() );
				
				//insert the strata values
				sb.append("INSERT into STRATA (strata_id, obs_id, stratumType, " 
					+" stratumCover, stratumHeight) "
					+" values("+strataId+", "+plotObservationId+", '"+type+"', '"+cover
					+"','"+height+"' )" );
				Statement insertStatement = conn.createStatement();
				insertStatement.executeUpdate(sb.toString());
			
				System.out.println("inserted Strata: " + ht);
			}			
		
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
			//System.exit(0);
			return(false);
		}
		return(true);
	}
	
	
	
		/**
		 * method that returns the prepared statement for inserting data
		 * into the plotObservation table
		 *
		 * @param conn -- the current connection
		 * @return pstmt -- the prepared statement
		 *
		 */
		private boolean insertPlotObservation()
		{
			StringBuffer sb = new StringBuffer();
			try 
			{
				//get the plotid number
				plotObservationId = getNextId("plotObservation");
				//the variables from the plot file
				String observationCode = plot.getPlotObservationCode();
				String startDate = plot.getStartDate();
				String stopDate = plot.getStopDate();
			
				//set up the debugging
//				StringBuffer debug = new StringBuffer();
				debug.append("plotId: "+ plotId+"\n");
				debug.append("observationCode: "+ observationCode+"\n");
				debug.append("startDate: "+ startDate+"\n");
				debug.append("stopDate: "+ stopDate +"\n");
			
				//System.out.println( debug.toString() );
			
				System.out.println("PARENT PLOT: "+plotId);
				sb.append("INSERT into PLOTOBSERVATION (obs_id, parentPlot, authorObsCode,"
				+" obsStartDate, obsStopDate) "
				+" values(?,?,?,?,?)" );
	
			
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  		  // Bind the values to the query and execute it
  		  pstmt.setInt(1, plotObservationId);
  		  pstmt.setInt(2, plotId);
  		  pstmt.setString(3, observationCode);
				pstmt.setString(4, startDate);
				pstmt.setString(5, stopDate);
			
				//execute the p statement
  		  pstmt.execute();
  		 // pstmt.close();
			}
			catch (Exception e)
			{
				System.out.println("Caught Exception: "+e.getMessage() ); 
				e.printStackTrace();
				//return false so that the calling method knows to roll-back
				return(false);
				//System.exit(0);
			}
		return(true);
	}
	
	
	
	//method that inserts the named place data for a plot and then returns the
	//named place id
	private int insertNamedPlace()
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			//get the plotid number
			namedPlaceId = getNextId("namedPlace");
			//the variables from the plot file
			String placeName = plot.getPlaceName();
		
			sb.append("INSERT into NAMEDPLACE (namedplace_id, placeName) "
				+"values("+namedPlaceId+", '"+placeName+"')" );
			Statement insertStatement = conn.createStatement();
			insertStatement.executeUpdate(sb.toString());
			System.out.println("inserted named place ");
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		return(namedPlaceId);
	}
	
	
	
	
	
	/**
	 *	method to insert the static plot data like names and locations
	 * and if it catches an exception then false is returned to the calling 
	 * class so that a roll-back can be issued
	 *
	 */
	private boolean insertStaticPlotData()
	{
	//	int plotId = -999;
		StringBuffer sb = new StringBuffer();
		try 
		{
			//get the plotid number
			plotId = getNextId("plot");
			int projectId = getProjectId(projectName);
			
			//the variables from the plot file
			plotName = plot.getPlotName();
			String surfGeo = plot.getSurfGeo();
			String parentPlot = plot.getParentPlotName();
			//parentPlot = "99";
			String plotType = plot.getPlotType();
			String placeName = plot.getPlaceName();
			String samplingMethod = plot.getSamplingMethod();
			String latitude = plot.getOriginLatitude();
			String longitude = plot.getOriginLongitude();
			String coverScale = plot.getCoverScale();
			String plotSize = plot.getPlotSize();
			double altValue = plot.getAltValue();
			String slopeAspect = plot.getSlopeAspect();
			String slopeGradient = plot.getSlopeGradient();
			String slopePosition = plot.getSlopePosition();
			String hydrologicRegime = plot.getHydrologicRegime();
			String soilDrainage = plot.getSoilDrainage();
			String plotShape = plot.getPlotShape();
			
			//print the variables to the screen for debugging
			//StringBuffer debug = new StringBuffer();
			debug.append("authorPlotCode: "+ plotName+"\n");
			debug.append("plotId: "+ plotId+"\n");
			debug.append("projectId: "+ projectId+"\n");
			debug.append("namedPlace: "+ namedPlaceId+"\n");
			debug.append("surfGeo: "+ surfGeo+"\n");
			debug.append("parentPlot: " + parentPlot+"\n");
			debug.append("plotType: " +plotType +"\n");
			debug.append("placeName: " +placeName +"\n");
			debug.append("samplingMethod: " + samplingMethod+"\n");
			debug.append("latitude: " + latitude+"\n");
			debug.append("longitude: " +longitude +"\n");
			debug.append("coverScale: " +coverScale +"\n");
			debug.append("plotSize: " + plotSize+"\n");
			debug.append("altValue: " + altValue +"\n");
			debug.append("slopeAspect: " + slopeAspect +"\n");
			debug.append("slopeGradient: " + slopeGradient+"\n");
			debug.append("slopePosition: " + slopePosition +"\n");
			debug.append("hydrologicRegime: " + hydrologicRegime +"\n");
			debug.append("soilDrainage: " + soilDrainage +"\n");
			debug.append("plotShape: " + plotShape+"\n");
			//System.out.println( debug.toString() );

			sb.append("INSERT into PLOT (project_id, authorPlotCode, plot_id, "
				+"surfGeo, plotType, parentPlot, namedPlace, samplingMethod "
				+", plotOriginLat, plotOriginLong, coverScale, plotSize, altValue,"
				+"slopeAspect, slopeGradient, slopePosition, hydrologicRegime, "
				+"soilDrainage, plotShape) "
				+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			double test = 1222.00;
			
  	  // Bind the values to the query and execute it
  	  pstmt.setInt(1, projectId);
  	  pstmt.setString(2, plotName);
			pstmt.setInt(3, plotId);
			pstmt.setString(4, surfGeo);
			pstmt.setString(5, plotType);
			pstmt.setString(6, parentPlot);
			pstmt.setInt(7, namedPlaceId);
			pstmt.setString(8, samplingMethod);
			pstmt.setDouble(9, test);
			pstmt.setDouble(10, test);
			pstmt.setString(11, coverScale);
			pstmt.setString(12, plotSize);
			pstmt.setDouble(13, altValue);
			pstmt.setString(14, slopeAspect);
			pstmt.setString(15, slopeGradient);
			pstmt.setString(16, slopePosition);
			pstmt.setString(17, hydrologicRegime);
			pstmt.setString(18, soilDrainage);
			pstmt.setString(19, plotShape);
			
			pstmt.getWarnings();
  	  pstmt.execute();
  	  pstmt.close();
		}
		catch (SQLException sqle)
		{
			System.out.println("Caught SQL Exception: "+sqle.getMessage() ); 
			sqle.printStackTrace();
			//System.exit(0);
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
			return(false);
			//System.exit(0);
		}
		return(true);
	}
	
	
	
	//method that returns true if the project with this name exists in the 
	//database
	private boolean projectExists(String projectName)
	{
		int rows = 0;
		try 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT count(*) from PROJECT where projectName like '"+projectName+"'" );
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			while ( rs.next() ) 
			{
				rows = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		if (rows == 0)
		{
			return(false);
		}
		else
		{
			return(true);
		}
	}
	
	
	
	//method to return the projectId given as input the 
	//name of a project -- before this method is to be 
	//called make sure that the project does actually exist
	//using the method 'projectExists'
	private int getProjectId(String projectName)
	{
		int projectId = 0;
		try 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT project_id from PROJECT where projectName like '"+projectName+"'" );
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			while ( rs.next() ) 
			{
				projectId = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		return(projectId);
	}
	
	
	
	
	/**
	 * method that returns the number of row in a given table
	 */
	private int getNextId(String tableName)
	{
		int rows = -1;
		try 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT count(*) from "+tableName );
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			while ( rs.next() ) 
			{
				rows = rs.getInt(1);
				//System.out.println("grabbed "+rows );
				//query = conn.createStatement();
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
			//System.exit(0);
		}
		return(rows);
	}
	
	//method that inserts a new project into the database
	public void insertProject(String projectName, String projectDescription)
	{
		try 
		{
			System.out.println("Project Primary Key: "+getNextId("project"));
			StringBuffer sb = new StringBuffer();
			int projectId = getNextId("project");
			sb.append("INSERT into PROJECT (project_id, projectName, description) "
			 +"values("+projectId+", '"+projectName+"', '"+projectDescription+"')" );
			Statement insertStatement = conn.createStatement();
			insertStatement.executeUpdate(sb.toString());
			System.out.println("inserted PROJECT");
			
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		
	}


}
