/** 
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2002-05-20 23:11:15 $'
 * 	'$Revision: 1.22 $'
 */
package databaseAccess;

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import databaseAccess.*;
import xmlresource.datatype.Plot;
import xmlresource.datatype.VegProject;
import xmlresource.utils.XMLparse;
import PlotDataSource;
import servlet.util.GetURL;

/**
 * this is a class that is used to load the database by parsing an xml document
 * that is consistent with the  veg plot project
 */
//public class DBinsertPlotSource implements ServletPluginInterface
public class DBinsertPlotSource
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
	private XMLparse parser;

	// variable that are quite general to the class and are going to be used by a
	// number of the methods
	String projectName = null;
	String projectDescription = null;
	String plotName = null;
	Vector plotNameList = new Vector();
	// instance variables describing the person that is loading 
	private String submitterSurName = "";
	private String submitterGivenName = "";
	private String submitterEmail = "";
	
	//these variables are returned from the database and are used throughout the
	// class
	int plotId;
	int namedPlaceId;
	int plotObservationId;
	int strataId;
	int taxonObservationId;
	int coverMethodId;
	int stratumMethodId;

	private String loaderEmail = "vegbank@vegbank.org"; //the email of the loader

	// the name of the plots project xml file containing the plot information
	// to be stored in the database
	public String filename = new String();
	
	//debug string buffer
	StringBuffer debug = new StringBuffer();

	//the data source that will be used for loading the db
	public PlotDataSource source;
	private GetURL gurl = new GetURL();

 /**
	* constructor -- input the name if the plugin and plot
	*/
	public DBinsertPlotSource(String plugin, String plot) 
	throws FileNotFoundException
  {
		try
    {
			//System.out.println("start");
			//initialize the data sourcee
			source = null;
			source = new PlotDataSource(plugin);
			//System.out.println("getting the plot data");
			source.getPlot(plot);
			
			//System.out.println("init db");
			//initialize the database connection manager
			connectionBroker.manageLocalDbConnectionBroker("initiate");
			conn = connectionBroker.manageLocalDbConnectionBroker("getConn");
			
			//System.out.println("opening log file");
			// make a new output log file
    	out = new PrintWriter(new FileWriter(logFile));
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
			System.out.println("Exiting at DBinsertPlotSource constructor");
			//System.exit(0);
		}
	}
	
	
	
	/**
	 * this constructor, unlike the other is for inserting a potentially large
	 * numebe of plots stored on a given source
	 */
	public DBinsertPlotSource(String plugin) throws FileNotFoundException
  {
		try
    {
			//initialize the data source 
			source = new PlotDataSource(plugin);
			
			//initialize the database connection manager
			connectionBroker.manageLocalDbConnectionBroker("initiate");
			conn = connectionBroker.manageLocalDbConnectionBroker("getConn");
		
    	out = new PrintWriter(new FileWriter(logFile));
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
			System.out.println("Exiting at DBinsertPlotSource constructor");
			System.exit(0);
		}
	}
	
	
	
	/**
	 * this constructor -- is empty
	 */
	public DBinsertPlotSource() 
  {
		
	}
	
	
	/**
	 * this is the method that the servlet plugin interface uses
	 */
	public StringBuffer servletRequestHandler(String action, Hashtable params)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("accessed the DBinsertPlotSource class");
		return(sb);
	}
	
	
	/**
	 * utility method to get a vector containg all the plots in a specific 
	 * data source
	 */
	 private Vector getPlotNames()
	 {
		 Vector v = new Vector();
		 	try
			{
				v = source.getPlotNames();
			}
			catch (Exception e)
			{
				System.out.println("Exception: "+ e.getMessage() );
				e.printStackTrace();
			}
			return(v);
	 }
	
	
	/**
   * the main routine used to test the DB class which interacts with the 
	 * vegclass database.
   * 
   * Usage: DBinsertPlotSource pluginName plot1 plot2 ... plotn
   *
   */
  static public void main(String[] args) 
	{
  	try 
		{
			//load an individual plot aor allow the user to specify the plots to load
			if (args.length >= 2)
			{
				//get the plugin named
				String plugin = args[0];
				//if there is only one plot then handle that differently than multiple
				if (args.length == 2)
				{
					String plot = args[1];
					String emailAddress = "";
					System.out.println("loading single plot: "+plot+"\n");
					DBinsertPlotSource db = new DBinsertPlotSource(plugin, plot);
					db.insertPlot(plot, emailAddress);
				}
			}
			//load all the plots in the package
			else if ( args.length == 1 )
			{
				System.out.println("DBinsertPlotSource > loading entire source");
				String plugin = args[0];
				DBinsertPlotSource db = new DBinsertPlotSource(plugin);
				Vector v = db.getPlotNames();
				//System.out.println("DBinsertPlotSource > plots: " + v.toString() );
				for (int i=0; i < v.size(); i++)
				{

					String emailAddress = "";
					String plot = (String)v.elementAt(i);
					plot = plot.trim();
					System.out.println("DBinsertPlotSource > loading plot: " + plot +" \n");
					db = new DBinsertPlotSource(plugin, plot);
					db.insertPlot(plot, emailAddress);
					db = null;
				}
			}
				
			else
			{
				System.out.println("Usage: DBinsertPlotSource pluginName plot1 plot2 ... plotn");
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+ e.getMessage() );
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * method that loads an entire plot package
	 *
	 * @param plotNames -- a vector that holds all the plotNames
	 */
	 public void insertPlotPackage(String pluginClass)
	{
		try 
		{
				
			//use the test plugin
	//		DBinsertPlotSource db = new DBinsertPlotSource(pluginClass);
			
			//initialize the data source 
			source = new PlotDataSource(pluginClass);
			Vector plotNames = source.getPlotNames();
			
			System.out.println("plots to be inserted: " + plotNames.toString() );
			//Thread.sleep(3000);
			
			
			for (int i =0; i < plotNames.size(); i++)
			{
				
				String pName = plotNames.elementAt(i).toString();
				String emailAddress = "";
				source = new PlotDataSource(pluginClass);
				DBinsertPlotSource db = new DBinsertPlotSource(pluginClass, pName);
				db.insertPlot(pName, emailAddress);
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	/**
	 * this method is a wrapper for the insertPlot method that takes just the 
	 * plot name.  This method differs in that it if the debugging level is
	 * greater than 0 then an xml debugging string is returned to the calling
	 * class
	 * 
	 * @param plotName -- the name of the plot that exists in the archive
	 * @param debugLevel  -- the level of debugging -- right now it can be 0, 1, 2
	 * 		where 0=nothing returned; 1=commit results; 3=above and exceptions
	 */
	public String insertPlot(String plotName, int debugLevel, String emailAddress)
	{
		try
		{
			
			if (debugLevel == 0)
			{
				debug.append("<plotInsertion> \n");
				this.insertPlot(plotName, emailAddress);
				debug.append("</plotInsertion> \n");
			}
			else if (debugLevel == 1)
			{
				debug.append("<plotInsertion> \n");
				this.insertPlot(plotName, emailAddress);
				debug.append("</plotInsertion> \n");
			}
			else if (debugLevel == 2)
			{
				debug.append("<plotInsertion> \n");
				this.insertPlot(plotName, emailAddress);
				debug.append("</plotInsertion> \n");
			}
			else
			{
				System.out.println("DbinsertPlotSource > invalid debug level: " + debugLevel );
				this.insertPlot(plotName, emailAddress);
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() );
			debug.append("<exceptionMessage>"+e.getMessage()+"</exceptionMessage>\n");
			e.printStackTrace();
		}
		return( debug.toString() );
	}
	
	
	/**
	 * method to initiate the insertion of a plot, there are a series of private
	 * methods that will be called to actually get the data into the database
	 *
	 * @param plotName -- the name of the plot that exists in the archive
	 */
	public void insertPlot(String plotName, String emailAddress)
	{
		try 
		{
			// update the instance vraible with the user's email address
			// which will be used for loading the plot and for constructing the 
			// accession number
			this.submitterEmail = emailAddress;
		
			//add a line for the user that is inserting the data
			debug.append( "<vegbankUser>"+emailAddress+"</vegbankUser> \n" );
			//this boolean determines if the plot should be commited or rolled-back
			boolean commit = true;
			int projectId = 0;
			
			//set the auto commit option on the connection to false after getting a
			// new connection from the pool
			conn = connectionBroker.manageLocalDbConnectionBroker("getConn");
			conn.setAutoCommit(false);
			
			this.projectName = source.projectName;
			this.projectDescription = source.projectDescription;
			
			
			System.out.println("DBinsertPlotSource > projectName: " + this.projectName);
			System.out.println("DBinsertPlotSource > projectDesc: " + this.projectDescription);
			System.out.println("DBinsertPlotSource > projectContrib: " + source.projectContributors.toString() );
				
		
			//see if the project in which this plot is a member exists in the database
			if (projectExists(projectName) == true )
			{
				projectId = getProjectId(projectName);
			}
			// if the project is not there then load it and the project contributor
			//info
			else
			{		
				insertProject(source.projectName, source.projectDescription);
				projectId = getProjectId(projectName);
				//insert the project contributor information here
				Vector projContributors = source.projectContributors;
				for (int ii =0; ii < projContributors.size(); ii++)
				{
					String contributor = (String)projContributors.elementAt(ii);
					String salutation=source.getProjectContributorSalutation(contributor);
					String surName = source.getProjectContributorSurName( contributor );
					String givenName = source.getProjectContributorGivenName(contributor);
					String email = source.getProjectContributorEmailAddress(contributor);
					String role = "project manager";
					insertProjectContributor(salutation, givenName, surName, email, role, 
					projectId);
				}
			}
//			insertNamedPlace();
				if (insertStaticPlotData(projectId) == false ) 
				{	
					System.out.println("static data: "+commit);
					commit = false;
				}
				else
				{
					if ( insertCoverMethod() == false )
					{
						System.out.println("covermethod>: "+commit);
						commit = false;
					}
					if ( insertStratumMethod() == false )
					{
						System.out.println("stratummethod>: "+commit);
						commit = false;
					}
					
					if( insertPlotObservation() == false )
					{
						System.out.println("observation>: "+commit);
						commit = false;
					}
					if( insertCommunities() == false )
					{
						System.out.println("communities>: "+commit);
						commit = false;
					}
					if( insertStrata() == false )
					{
						System.out.println("observation>: "+commit);
						commit = false;
					}
					//both the taxon observation tables
					//and the strata composition are uypdated here
					if( insertTaxonObservations() == false )
					{
						System.out.println("observation>: "+commit);
						debug.append( "<taxaInsertion>false</taxaInsertion> \n" );
						commit = false;
					}
					else
					{
						debug.append( "<taxaInsertion>true</taxaInsertion> \n" );
					}
				}
				System.out.println("DBinsertPlotSource > insertion success: "+ commit);
			if ( commit == true) 
			{
				conn.commit();
				debug.append( "<insert>true</insert>\n" );
			}
			else
			{
				conn.rollback();
				debug.append( "<insert>false</insert>\n" );
			}
			connectionBroker.manageLocalDbConnectionBroker("destroy");
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage() ); 
			debug.append("<exceptionMessage>"+e.getMessage()+"</exceptionMessage>\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * method that handles the loading of the project contributor information
	 *
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 * @param email
	 * @param role
	 * @param projectId 
	 *
	 */
	 private void insertProjectContributor(String salutation, String givenName,
	 String surName, String email, String role, int projectId )
	 {
		 try
		 {
			 int partyId = 0;
			 //insert into the party table first
			 boolean pExists = partyExists(salutation, givenName, surName, email);
			 System.out.println("DBinsertPlotSource > party exists: " + pExists);
			 if ( pExists == false )
			 {
				 partyId = insertParty(salutation, givenName, surName, email );
			 }
			 //else get the party id for this contributor
			 else
			 {
				 partyId = getPartyId(salutation, givenName, surName, email );
			 }
			 // insert the role table
			 
			 //insert into the project contributor table
			 StringBuffer sb = new StringBuffer();
				sb.append("INSERT into PROJECTCONTRIBUTOR ");
				sb.append("( party_id, project_id, surName, role) ");
				sb.append(" values ("+partyId+", "+projectId+", '"+surName+"', '"+role+"')" );
			
				//System.out.println("query > " + sb.toString() );
				Statement insertStatement = conn.createStatement();
				insertStatement.executeUpdate(sb.toString());	
			 
		 }
		 catch( Exception e)
		 {
			 	System.out.println("Exception: "+e.getMessage() ); 
				e.printStackTrace();
				System.exit(1);
		 }
	 }
	 
	 
	/**
	 * method that the party id for an individual
	 * otherwise false is returned
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 * @param email
	 */
	private int getPartyId (String salutation, String givenName,
	 String surName, String email)
	{
		int i = 0;
		try 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT party_id from PARTY where ");
			sb.append(" upper(salutation) ");
			sb.append(" like '"+salutation.toUpperCase()+"'" );
			sb.append(" and ");
			sb.append(" upper(givenName) ");
			sb.append(" like '"+givenName.toUpperCase()+"'" );
			sb.append(" and ");
			sb.append(" upper(surName) ");
			sb.append(" like '"+surName.toUpperCase()+"'" );
			System.out.println("DBinsertPlotSource > query: " + sb.toString());
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			while ( rs.next() ) 
			{
				i = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		return(i);
	}
	
	
	
		
	/**
	 * method that returns true if the party already exists in the database
	 * otherwise false is returned
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 * @param email
	 */
	 private int insertParty(String salutation, String givenName, String surName, 
	 String email)
	 {
		 int partyId = 0;
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into PARTY ");
			sb.append("( salutation, givenName, surName, email) ");
			sb.append(" values ('"+salutation+"', '"+givenName+"', '"+surName+"', '"+email+"')" );
			
			//System.out.println("query > " + sb.toString() );
			Statement insertStatement = conn.createStatement();
			insertStatement.executeUpdate(sb.toString());	
			
			//get the partyid associated with this entry
			sb = new StringBuffer();
			sb.append("select max(party_id) from party");
			insertStatement = conn.createStatement();	
			ResultSet rs = insertStatement.executeQuery( sb.toString() );
			while ( rs.next() ) 
			{
				partyId = rs.getInt(1);
			}
		}
		 catch( Exception e)
		 {
			 	System.out.println("Exception: "+e.getMessage() ); 
				e.printStackTrace();
				//System.exit(1);
		 }
		 return(partyId);
	 }
	
	
	
	/**
	 * method that returns true if the party already exists in the database
	 * otherwise false is returned
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 * @param email
	 */
	private boolean partyExists(String salutation, String givenName,
	 String surName, String email)
	{
		int rows = 0;
		try 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT count(*) from PARTY where ");
			sb.append(" upper(salutation) ");
			sb.append(" like '"+projectName.toUpperCase()+"'" );
			sb.append(" and ");
			sb.append(" upper(givenName) ");
			sb.append(" like '"+givenName.toUpperCase()+"'" );
			sb.append(" and ");
			sb.append(" upper(surName) ");
			sb.append(" like '"+surName.toUpperCase()+"'" );
			System.out.println("DBinsertPlotSource > query: " + sb.toString());
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
			System.out.println("DBinsertPlotSource > project does not exist");
			return(false);
		}
		else
		{
			System.out.println("DBinsertPlotSource > project does exist");
			return(true);
		}
	}
	
	
	
	/**
	 * method to update the strata composition tables --should only be called
	 * from the insert taxonObservation method
	 *
	 * @param plantName -- the plantName as it is used in the data source
	 * @param taxonObservationId -- the taxonobservation for this plant
	 *
	 */
	private boolean insertStrataComposition(String plantName, int taxonObservationId)
	{
		try 
		{
			Vector strataVec = source.getTaxaStrataExistence(plantName, plotName);
			for (int ii =0; ii < strataVec.size(); ii++)
			{
				String curStrata = strataVec.elementAt(ii).toString();
				StringBuffer sb = new StringBuffer();

				// get th cover of the plant within that strata -- if this comes back
				// null then there was no observation of this plant in the strata
				String cover = source.getTaxaStrataCover(plantName, plotName, curStrata);
				if ( cover != null )
				{
					//get the strataCompId number
					int stratumCompositionId  = getNextId("stratumComposition");
					
					//get the strata ID
					int stratumId = this.getStrataId(plotObservationId, curStrata);
				
					debug.append("<stratumComposition> \n");
					debug.append("<stratumName>"+curStrata+"</stratumName> \n");
					debug.append("<taxonName>"+plantName.replace('&', '_')+"</taxonName> \n");
					debug.append("<cover>"+cover+"</cover> \n");
					debug.append("</stratumComposition> \n");

			
					//insert the strata composition values
					sb.append("INSERT into STRATUMCOMPOSITION (stratumComposition_id, "
					+" cheatPlantName, cheatStratumName, taxonStratumCover, stratum_id, " 
					+" taxonobservation_id) ");
					sb.append("values(?,?,?,?,?,?)");
				
					PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				
					// Bind the values to the query and execute it
  	  		pstmt.setInt(1, stratumCompositionId);
  	  		pstmt.setString(2, plantName);
					pstmt.setString(3, curStrata);
					pstmt.setString(4, cover);
					pstmt.setInt(5, stratumId);
					pstmt.setInt(6, taxonObservationId);
  				pstmt.execute();
				}
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Caught SQL Exception: "+sqle.getMessage() ); 
			debug.append("<exceptionMessage>"+sqle.getMessage()+" (sqle) loading strata Composition data</exceptionMessage>\n");
			sqle.printStackTrace();
			return(false);
		}
		
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			debug.append("<exceptionMessage>"+e.getMessage()+"</exceptionMessage>\n");
			e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	
	/**
	 * method to insert the cover method data
	 */
	 private boolean insertCoverMethod()
	 {
		 		
			try
			{
				StringBuffer sb = new StringBuffer();
				//get the strataId number
				coverMethodId = getNextId("covermethod");
				String coverType = "replace this";
				//insert the strata values
				sb.append("INSERT into COVERMETHOD (covermethod_id, coverType) ");
				sb.append("values(?,?)");
				
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				
				// Bind the values to the query and execute it
  	  	pstmt.setInt(1, coverMethodId);
  	  	pstmt.setString(2, coverType);
				
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
	 
	 
	 
	/**
	 * method to insert the cover method data
	 */
	 private boolean insertStratumMethod()
	 {
		 		
			try
			{
				StringBuffer sb = new StringBuffer();
				//get the strataId number
				stratumMethodId = getNextId("stratummethod");
				String stratumMethodName = "replace this";
				//insert the strata values
				sb.append("INSERT into STRATUMMETHOD (stratummethod_id, stratummethodname) ");
				sb.append("values(?,?)");
				
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				
				// Bind the values to the query and execute it
  	  	pstmt.setInt(1, stratumMethodId);
  	  	pstmt.setString(2, stratumMethodName);
				
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
			
			sb.append("SELECT STRATUM_ID from STRATUM where OBSERVATION_ID = "
			+plotObservationId+" and stratumName like '%"+stratumType+"%'");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			
			int cnt = 0;
			while ( rs.next() ) 
			{
				strataId = rs.getInt(1);
				cnt++;
			}
			//send warnings
			if ( cnt == 0)
			{
				System.out.println("warning: There were no strata matching: " + stratumType );
			}
			
		}
		catch (Exception e) 
		{
			System.out.println("Caught Exception  " + e.getMessage());
			e.printStackTrace();
		}
		return(strataId);
	}


	
	/**
	 * method to add the taxonObservation data to the database
	 * if the method fails it will return false and the plot 
	 * will be rolled back
	 */
	private boolean insertTaxonObservations()
	{
		boolean successfulCommit = true;
		try 
		{
			//get the number of taxonObservations
			Vector uniqueTaxa = source.plantTaxaNames;
			//getPlantTaxaNames(String plotName)
			System.out.println("DBinsertPlotSource > number of assocated taxa: " + uniqueTaxa.size() );
			System.out.println("DBinsertPlotSource > taxa: " + uniqueTaxa.toString() );
			
			for (int i =0; i < uniqueTaxa.size(); i++)
			{
				StringBuffer sb = new StringBuffer();
				// get the taxonObservation number which will be used in the 
				// strata composition insertion method called below too
				taxonObservationId = getNextId("taxonObservation");	
				String authorNameId = uniqueTaxa.elementAt(i).toString();
				System.out.println("DBinsertPlotSource > current taxon: " + authorNameId );
				
				// get the code associated with this name and if this code
				// that is returned is empty or null the code that should be
				// used is the plant name itself
				String code = source.getPlantTaxonCode(authorNameId);
				if ( code == null || code.length() < 1 )
				{
					System.out.println("DBinsertPlotSource > no plant code id data source " );
					code = authorNameId;
				}
				System.out.println("DBinsertPlotSource > current taxon code: " + code );
				String percentCover = "25";
				
				// get the hashtable with the plant name look up results from the 
				// web application
				Hashtable h = getPlantTaxonomyData( code );
				String level  = "";
				String conceptId  = "";
				String name = "";
				String nameId = "";
			
				// if the hashtable has a single key then it will have 
				// all the keys associated with a plant
				if ( h.containsKey("level") )
				{
					level =	(String)h.get("level");	
					conceptId = (String)h.get("conceptId");
					name = (String)h.get("name");
					nameId = (String)h.get("nameId");
				}
				
				//add the taxon name to the debugging
				debug.append("<taxonObservation> \n");
				debug.append("<authorTaxonName>"+authorNameId.replace('&', '_')+"</authorTaxonName> \n");
				debug.append("<authorTaxonCode>"+code+"</authorTaxonCode> \n");
				debug.append("<vegbankMatch> \n");
				debug.append("<vegbankLevel>"+level+"</vegbankLevel> \n");
				debug.append("<vegbankName>"+name.replace('&', '_')+"</vegbankName> \n");
				debug.append("<vegbankConceptId>"+conceptId+"</vegbankConceptId> \n");
				debug.append("<vegbankNameId>"+nameId+"</vegbankNameId> \n");
				debug.append("</vegbankMatch> \n");
				debug.append("</taxonObservation> \n");
				
				//insert the values
				sb.append("INSERT into TAXONOBSERVATION (taxonobservation_id, observation_id, "
					+" cheatplantName, plantname_id ) "
					+" values(?,?,?,?) ");
				
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  	  	pstmt.setInt(1, taxonObservationId);
  	  	pstmt.setInt(2, plotObservationId);
				pstmt.setString(3, authorNameId);
				pstmt.setString(4, nameId);
  		  pstmt.execute();
			 	
				// insert the strata composition
				boolean result = this.insertStrataComposition(authorNameId, taxonObservationId);
			 	if (result == false)
				{
					successfulCommit = false; 
				}
				debug.append("<insertStrataComp>"+result+"</insertStrataComp> \n");
			
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
			successfulCommit = false; 
		}
		return( successfulCommit );
	}
	
	
	
	
	/**
	 * method that returns false if the community cannot be stored in the 
	 * database this method uses the webservice lookup to get the community 
	 * info based on either a code, or name
	 * 
	 */
	private boolean insertCommunities()
	{
		try 
		{
				StringBuffer sb = new StringBuffer();
				
				String name = source.getCommunityName(plotName);
				String code =  source.getCommunityCode(plotName);
				String framework = source.getCommunityFramework(plotName);
				String level = source.getCommunityLevel(plotName);
				String conceptId = "";
				
			
				// get the appropriate codes for this community via the web service
				// store the data in a hashtable
				Hashtable h = new Hashtable();
				if ( code != null )
				{
					h = getCommunityData(code);
				}
				else if ( name != null )
				{
					h = getCommunityData( name );
				}
				
				// if the hashtable has a sible key then it will have 
				// all the keys associated with a community
				if ( h.containsKey("conceptId") )
				{
					level =	(String)h.get("level");	
					code = (String)h.get("code");
					conceptId = (String)h.get("conceptId");
					name = (String)h.get("name");
				}
				
				debug.append("<communityName>"+name+"</communityName> \n");
				debug.append("<communityCode>"+code+"</communityCode> \n");
				debug.append("<communityLevel>"+level+"</communityLevel> \n");
				debug.append("<communityFramework>"+framework+"</communityFramework> \n");
				debug.append("<communityConceptId>"+conceptId+"</communityConceptId> \n");
				
				//insert the values
				sb.append("INSERT into commclass (observation_id, commName, " 
				+" commCode, commFramework, commLevel) "
				+" values(?,?,?,?,?)");
				
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  		  // Bind the values to the query and execute it
  		  pstmt.setInt(1, plotObservationId);
				pstmt.setString(2, name);
  		  pstmt.setString(3, code);
				pstmt.setString(4, framework);
				pstmt.setString(5, level);
				//execute the p statement
  		  pstmt.execute();			
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
	 * method that returns false if the strataElements cannot be loaded to the
	 * database
	 *
	 */
	private boolean insertStrata()
	{
		try 
		{
			//get the names of the recognized strata
			Vector strataTypes = source.uniqueStrataNames;
			for (int i =0; i < strataTypes.size(); i++)
			{
				StringBuffer sb = new StringBuffer();
				//get the strataId number
				strataId = getNextId("stratum");
				
				String sName = strataTypes.elementAt(i).toString();
				
				String cover = source.getStrataCover(plotName, sName);
				String base =  source.getStrataBase(plotName, sName);
				String height = source.getStrataHeight(plotName, sName);
				
				if ( height != null && height.length() >= 1 )
				{
					debug.append("<stratum> \n");
					debug.append("<name>"+sName+"</name>\n");
					debug.append("<base>"+base+"</base>\n");
					debug.append("<height>"+height+"</height> \n");
					debug.append("</stratum> \n");
				
					//insert the strata values
					sb.append("INSERT into STRATUM (stratum_id, observation_id, stratumName, " 
					+" stratumCover, stratumBase ,stratumHeight) "
					+" values(?,?,?,?,?,?)");
				
					PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  		  	// Bind the values to the query and execute it
  		  	pstmt.setInt(1, strataId);
  		  	pstmt.setInt(2, plotObservationId);
					pstmt.setString(3, sName);
  		  	pstmt.setString(4, cover);
					pstmt.setString(5, base);
					pstmt.setString(6, height);
					//execute the p statement
  		  	pstmt.execute();
			 }
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
		 * method that loads the observation table in the VegBank
		 * database and then returns a sting buffer which represents 
		 * an xml document with the attribute names and values that 
		 * were loaded to the database.
		 *
		 */
		private boolean insertPlotObservation()
		{
			StringBuffer sb = new StringBuffer();
			try 
			{
				//get the plotid number
				plotObservationId = getNextId("observation");
				//the variables from the plot file
				String observationCode = source.getAuthorObsCode(plotName);
				String startDate = source.getObsStartDate(plotName);
				String stopDate = source.getObsStopDate(plotName);
				String taxonObservationArea = "999.99";
				String autoTaxonCover = " ";
				String coverDispersion = " ";
				boolean permanence = source.isPlotPermanent(plotName);
			
				// update the debugging stringbuffer
				debug.append("<plotId>"+ plotId+"</plotId> \n");
				debug.append("<observationCode>"+observationCode+"</observationCode>\n");
				debug.append("<obsStartDate>"+startDate+"</obsStartDate>\n");
				debug.append("<obsStopDate>"+stopDate+"</obsStopDate> \n");
				debug.append("<permanent>"+permanence+"</permanent> \n");
				
				
				sb.append("INSERT into OBSERVATION (observation_id, covermethod_id,  "
				+" plot_Id, authorobscode, "
				+" obsStartDate, obsEndDate, stratummethod_id, taxonObservationArea, "
				+" autoTaxonCover, coverDispersion) "
				+" values(?,?,?,?,?,?,?,?,?,?)" );
	
			
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  		  // Bind the values to the query and execute it
  		  pstmt.setInt(1, plotObservationId);
  		  pstmt.setInt(2, coverMethodId);
				pstmt.setInt(3, plotId);
  		  pstmt.setString(4, observationCode);
				pstmt.setString(5, startDate);
				pstmt.setString(6, stopDate);
				pstmt.setInt(7, stratumMethodId);
				pstmt.setString(8, taxonObservationArea);
				pstmt.setString(9, autoTaxonCover);
				pstmt.setString(10, coverDispersion);
				
  		  pstmt.execute();
			}
			catch (Exception e)
			{
				System.out.println("Caught Exception: "+e.getMessage() ); 
				e.printStackTrace();
				debug.append("<exceptionMessage>"+e.getMessage()+"</exceptionMessage>\n");
				return(false);
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
	 * method to insert the static plot data like names and locations
	 * and if it catches an exception then false is returned to the calling 
	 * class so that a roll-back can be issued
	 *
	 */
	private boolean insertStaticPlotData(int projectId)
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			//get the plotid number
			plotId = getNextId("plot");
			
			//the variables from the plot file
			plotName = source.plotCode;
			String authorPlotCode = plotName;
			String surfGeo = source.surfGeo;
			String parentPlot = "9";
			String plotArea = source.plotArea;
			String altValue = source.elevation;
			String slopeAspect = source.slopeAspect;
			String slopeGradient = source.slopeGradient;
			String topoPosition = source.topoPosition;
			String hydrologicRegime = source.hydrologicRegime;
			String plotShape = source.plotShape;
			String confidentialityStatus = source.confidentialityStatus; //not null
			String confidentialityReason = source.confidentialityReason; //not null
			String xCoord = source.xCoord;
			String yCoord = source.yCoord;
			String zone = source.utmZone;
			// if the plot data source has geocoordinates (latitude, logitude )
			// use them otherwise lookup the information from the web service
			String latitude = source.getLatitude(plotName);
			String longitude = source.getLongitude(plotName);
			if ( latitude == null || latitude.length() < 2 )
			{
				Hashtable geoCoords=getGeoCoords(xCoord, yCoord, zone);
				latitude = (String)geoCoords.get("latitude");
				longitude = (String)geoCoords.get("longitude");
			}
			
			
			String state = source.state;
			String country = source.country;
			String authorLocation = source.authorLocation;
			
			//make a temporary accession number for each unique plot
			String accessionNumber = getAccessionNumber(authorPlotCode, plotId, this.submitterEmail);
			System.out.println("DBinsertPlotSource > accession number: " + accessionNumber);
			
			//print the variables to the screen for debugging
			debug.append("<authorPlotCode>"+ plotName+"</authorPlotCode>\n");
			debug.append("<geology>"+surfGeo+"</geology>\n");
			debug.append("<plotArea>"+plotArea+"</plotArea>\n");
			debug.append("<altValue>"+altValue+"</altValue>\n");
			debug.append("<slopeAspect>"+slopeAspect+"</slopeAspect>\n");
			debug.append("<slopeGradient>"+slopeGradient+"</slopeGradient>\n");
			debug.append("<topoPosition>"+topoPosition+"</topoPosition>\n");
			debug.append("<hydrologicRegime>"+hydrologicRegime+"</hydrologicRegime>\n");
			debug.append("<plotShape>"+plotShape+"</plotShape>\n");
			debug.append("<xCoord>"+xCoord+"</xCoord>\n");
			debug.append("<yCoord>"+yCoord+"</yCoord>\n");
			debug.append("<latitude>"+latitude+"</latitude>\n");
			debug.append("<longitude>"+longitude+"</longitude>\n");
			debug.append("<zone>"+zone+"</zone>\n");
			debug.append("<accessionNumber>"+accessionNumber+"</accessionNumber>\n");
			
			debug.append("<country>"+country+"</country>\n");
			debug.append("<state>"+state+"</state>\n");
			debug.append("<authorLocation>"+authorLocation+"</authorLocation>\n");
			
			
			//this is the postgresql date function
			String sysdate = "now()";

			sb.append("INSERT into PLOT (project_id, authorPlotCode, plot_id, "
				+"geology, "
				+"latitude, longitude, area, elevation,"
				+"slopeAspect, slopeGradient, topoPosition, "
				+"shape, confidentialityStatus, confidentialityReason, "
				+"authore, authorn, state, country, authorLocation, accession_number, "
				+"dateEntered, submitter_surname, submitter_givenname, submitter_email) "
				+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			
  	  // Bind the values to the query and execute it
  	  pstmt.setInt(1, projectId);
  	  pstmt.setString(2, plotName);
			pstmt.setInt(3, plotId);
			pstmt.setString(4, surfGeo);
			pstmt.setString(5, latitude);
			pstmt.setString(6, longitude);
			pstmt.setString(7, plotArea);
			pstmt.setString(8, altValue);
			pstmt.setString(9, slopeAspect);
			pstmt.setString(10, slopeGradient);
			pstmt.setString(11, topoPosition);
			pstmt.setString(12, plotShape);
			pstmt.setString(13, confidentialityStatus);
			pstmt.setString(14, confidentialityReason);
			pstmt.setString(15, xCoord);
			pstmt.setString(16, yCoord);
			pstmt.setString(17, state);
			pstmt.setString(18, country);
			pstmt.setString(19, authorLocation);
			pstmt.setString(20, accessionNumber);
			pstmt.setString(21, sysdate);
			pstmt.setString(22, this.submitterSurName);
			pstmt.setString(23, this.submitterGivenName);
			pstmt.setString(24, this.submitterEmail);

			pstmt.getWarnings();
  	  pstmt.execute();
  	  pstmt.close();
		}
		catch (SQLException sqle)
		{
			System.out.println("Caught SQL Exception: "+sqle.getMessage() ); 
			sqle.printStackTrace();
			return(false);
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
	
	/**
	 * utility method for creating the accession number associated with the 
	 * given plot.  This string has changed content and structure during the 
	 * initial development.  Basically we are tyring to embed in the string 
	 * caharacters that would denote:
	 * 		VegBank System
	 * 		email of the loader 
	 * 		date that the plot was observed
	 * 		name of the plot
	 *		primary key of the plot in the Vegbank system
	 *
	 * @param plotName -- the name of the plot 
	 * @param plotId -- the PK of the plot in the system
	 * @param email -- the email address of the user loading the plot
	 * 
	 */
		private String getAccessionNumber( String plotName, int plotId, String
		email)
		{
			StringBuffer sb = new StringBuffer();
			try
			{
				String date = source.getObsStartDate(plotName);
				StringTokenizer tok = new StringTokenizer(date);
				String buf = tok.nextToken().replace('-', ' ');
				StringTokenizer tok2 = new StringTokenizer(buf);
				StringBuffer sb2 = new StringBuffer();
				while ( tok2.hasMoreTokens() )
				{
					sb2.append( tok2.nextToken() );
				}
				String startDate = sb2.toString();
				
				sb.append("VB");
				sb.append(".");
				sb.append( plotName.replace('.', '_') );
				sb.append(".");
				sb.append(plotId);
				sb.append(".");
				sb.append(email);
				sb.append(".");
				sb.append(startDate);
			}
			catch (Exception e)
			{
				System.out.println("Exception: "+e.getMessage() ); 
				e.printStackTrace();
			}
			return( sb.toString() );
		}
	
	
	/**
	 * utility method to get the loatitude and longitude from 
	 * the web service that serves that information
	 * 
	 * @param xCoord -- the x coordinate
	 * @param yCoord -- the y coordinate
	 * @param zone -- the utm zone
	 * @return hash -- a hashtable with keys : longitide and latitude
	 */
	private Hashtable getGeoCoords(String xCoord, String yCoord, String zone)
	{
		Hashtable h = new Hashtable();
		try
		{
			//THIS USES THE REQUESTURL METHOD
			String protocol = "http://";
	  	String host = "vegbank.nceas.ucsb.edu";
			String s = null;		
			//THIS USES THE OTHER REQUEST URL METHOD
			String servlet = "/framework/servlet/framework";
			Properties parameters = new Properties();
			parameters.setProperty("action", "coordinateTransform");
			parameters.setProperty("returnformattype", "xml");
			parameters.setProperty("x", xCoord);
			parameters.setProperty("y", yCoord);
			parameters.setProperty("zone", zone);
			s = gurl.requestURL(servlet, protocol, host, 
		 	parameters);
			
			StringTokenizer tok = new StringTokenizer(s);
			String latitude = tok.nextToken();
			String longitude = tok.nextToken();
			
			System.out.println("DBinsertPlotSource > lat : " + latitude );
			System.out.println("DBinsertPlotSource > long : " + longitude );
			
			h.put("latitude", latitude);
			h.put("longitude",longitude );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		return(h);
	}
	
	/**
	 * utility method to use a webservice to lookup the community 
	 * related attributes from the vegbank community archive
	 * 
	 * @param code -- the community code used to lookup the concept id
	 */
	private Hashtable getCommunityData(String code)
	{
		Hashtable h = new Hashtable();
		try
		{
			//THIS USES THE REQUESTURL METHOD
			String protocol = "http://";
	  	String host = "vegbank.nceas.ucsb.edu";
			String s = null;		
			//THIS USES THE OTHER REQUEST URL METHOD
			String servlet = "/framework/servlet/DataRequestServlet";
			Properties parameters = new Properties();
			parameters.setProperty("requestDataType", "vegCommunity");
			parameters.setProperty("requestDataFormatType", "xml");
			parameters.setProperty("clientType", "clientApplication");
			parameters.setProperty("communityName", code);
			parameters.setProperty("communityLevel","%" );
			s = gurl.requestURL(servlet, protocol, host, 
		 	parameters);
			System.out.println("DBinsertPlotSource > XML string from web app: \n'"+s+"'");
			
			if ( s.length() > 3 && s != null )
			{
				// parse the returned xml doc to get the relevant data and 
				// put it into the hash table
				parser = new XMLparse();	
				Document doc = parser.getDocumentFromString(s);
				Vector levelVec = parser.getValuesForPath(doc, "/vegCommunity/community/classLevel");
				Vector codeVec = parser.getValuesForPath(doc, "/vegCommunity/community/classCode");
				Vector conceptIdVec = parser.getValuesForPath(doc, "/vegCommunity/community/commConceptId");
				Vector nameVec = parser.getValuesForPath(doc, "/vegCommunity/community/commName");
				String lev = (String)levelVec.elementAt(0);
				String c = (String)codeVec.elementAt(0);
				String conceptId = (String)conceptIdVec.elementAt(0);
				String name  = (String)nameVec.elementAt(0);
				System.out.println("DBinsertPlotSource > parsed community xml: " +lev+" "+c+" "+conceptId);
			
				if ( lev != null && c != null && conceptId != null)
				{
					h.put("code", c);
					h.put("level", lev );
					h.put("conceptId", conceptId );
					h.put("name", name);
				}
				else
				{
					System.out.println("DBinsertPlotSource > parsed elements returned null: " +lev+" "+c+" "+conceptId);
				}
			}
			else
			{
				System.out.println("DBinsertPlotSource > no results from web app" );
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		return(h);
	}
	

/**
	 * utility method to use a webservice to lookup the plant 
	 * related attributes from the vegbank plant taxonomy archive.  
	 * The input into this method may be either a plant taxon code
	 * like 'ABIES' or even a plant name like 'Abies bifolia'
	 * 
	 * @param code -- the plant taxonomy code used to lookup the related data
	 * 	that will be returned to from the web service in an xml format
	 */
	private Hashtable getPlantTaxonomyData(String code)
	{
		Hashtable h = new Hashtable();
		try
		{
			//THIS USES THE REQUESTURL METHOD
			String protocol = "http://";
	  	String host = "vegbank.nceas.ucsb.edu";
			String s = null;		
			//THIS USES THE OTHER REQUEST URL METHOD
			String servlet = "/framework/servlet/DataRequestServlet";
			Properties parameters = new Properties();
			parameters.setProperty("requestDataType", "plantTaxon");
			parameters.setProperty("requestDataFormatType", "xml");
			parameters.setProperty("clientType", "clientApplication");
			parameters.setProperty("taxonName", code);
			parameters.setProperty("taxonNameType","%" );
			parameters.setProperty("taxonLevel","%" );
			s = gurl.requestURL(servlet, protocol, host, 
		 	parameters);
			System.out.println("DBinsertPlotSource > XML string from web app: \n'"+s+"'");
			
			if ( s.length() > 3 && s != null )
			{
				// parse the returned xml doc to get the relevant data and 
				// put it into the hash table
				parser = new XMLparse();	
				Document doc = parser.getDocumentFromString(s);
				Vector levelVec = parser.getValuesForPath(doc, "/plantTaxa/taxon/name/plantLevel");
				Vector conceptIdVec = parser.getValuesForPath(doc, "/plantTaxa/taxon/name/plantConceptId");
				Vector nameVec = parser.getValuesForPath(doc, "/plantTaxa/taxon/name/plantDescription");
				Vector nameIdVec = parser.getValuesForPath(doc, "/plantTaxa/taxon/name/plantNameId");
				String lev = (String)levelVec.elementAt(0);
				String conceptId = (String)conceptIdVec.elementAt(0);
				String name  = (String)nameVec.elementAt(0);
				String nameId  = (String)nameIdVec.elementAt(0);
				System.out.println("DBinsertPlotSource > parsed plant taxa xml: " +lev+" "+conceptId);
			
				if ( lev != null && conceptId != null)
				{
					h.put("level", lev );
					h.put("conceptId", conceptId );
					h.put("name", name);
					h.put("nameId", nameId);
				}
				else
				{
					System.out.println("DBinsertPlotSource > parsed elements returned null: " +lev+"  "+conceptId);
				}

			}
			else
			{
				System.out.println("DBinsertPlotSource > no results from web app" );
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
		return(h);
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
			//System.out.println("DBinsertPlotSource > query: " + sb.toString());
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
			System.out.println("DBinsertPlotSource > project does not exist");
			return(false);
		}
		else
		{
			System.out.println("DBinsertPlotSource > project does exist");
			return(true);
		}
	}
	
	
	
	/**
	 * method to return the projectId given as input the 
	 * name of a project -- before this method is to be 
	 * called make sure that the project does actually exist
	 * using the method 'projectExists'
	 */
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
	
	/**
	 * method that will insert the project data
	 */
	public void insertProject(String projectName, String projectDescription)
	{
		try 
		{
			System.out.println("DBinsertPlotSource > project primary Key: "+getNextId("project") );
			StringBuffer sb = new StringBuffer();
			int projectId = getNextId("project");
			sb.append("INSERT into PROJECT (project_id, projectName, projectdescription) "
			 +"values("+projectId+", '"+projectName+"', '"+projectDescription+"')" );
			Statement insertStatement = conn.createStatement();
			insertStatement.executeUpdate(sb.toString());
			System.out.println("DBinsertPlotSource > inserted PROJECT");	
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
}
