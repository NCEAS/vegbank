/**
* '$RCSfile: DBinsertPlotSource.java,v $'
* Purpose: A Class that loads plot data to the vegbank database system
* Copyright: 2000 Regents of the University of California and the
*            National Center for Ecological Analysis and Synthesis
* Authors: John Harris
* Release: @release@-t
*
*   '$Author: farrell $'
*   '$Date: 2003-10-17 22:09:14 $'
*   '$Revision: 1.6 $'
*
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
package org.vegbank.databaseAccess;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.AbstractList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.GetURL;
import org.vegbank.common.utility.Utility;
import org.vegbank.plots.datasource.PlotDataSource;
import org.vegbank.plots.datasource.RectificationUtility;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.vegbank.xmlresource.*;


/**
 * this is a class that is used to load the vegbank plots database
 * by using as input data data abctracted by the 'PlotDataSource' 
 * class and the plugins used by that system 
 */
public class DBinsertPlotSource {

	public Node plotNode;
	public DBConnection conn;
	private PrintWriter out;
	private String logFile = "loadlog.txt";


	//public VegProject project;
	private XMLparse parser;

	// variables that are quite general to the class
	String projectName = null;
	String projectDescription = null;
	private String projectStartDate = null;
	private String projectStopDate = null;
	String plotName = null;
	Vector plotNameList = new Vector();
	// instance variables describing the person that is loading 
	private String submitterSurName = "";
	private String submitterGivenName = "";
	private String submitterEmail = "";

	//these variables are returned from the database and are used throughout the
	// class
	int projectId;
	int plotId;
	int namedPlaceId;
	int plotObservationId;
	int strataId;
	//int taxonObservationId;
	int coverMethodId;
	int stratumMethodId;
	Hashtable stratumIds = new Hashtable();

	//private String loaderEmail = "vegbank@" + this.getHostname();
	//the email of the loader
	// the name of the plots project xml file containing the plot information
	// to be stored in the database
	public String filename = new String();
	//debug string buffer
	StringBuffer debug = new StringBuffer();
	//the data source that will be used for loading the db
	public PlotDataSource source;
	private GetURL gurl = new GetURL();
	private String hostname = "localhost";
	
	// the properties file and the associated properties 
	private ResourceBundle rb;
	private String authenticationServletHost;
	private String plantRequestServletHost;
	private String communityRequestServletHost;
	private String geoCoordRequestServletHost;
	
	
	/**
		* constructor -- input the name if the plugin and plot
		* @param plugin -- the name of the plugin that corresponds to the type 
		*  of plot datasource 
		* @param -- the name of the plot to load
		*/
	public DBinsertPlotSource(String plugin, String plot)
		throws FileNotFoundException {
		try {
			System.out.println("DBinsertPlotSource > init");
			// read the paramters from the properties file
			rb = ResourceBundle.getBundle("database");
			this.authenticationServletHost = rb.getString("authenticationServletHost");
			this.plantRequestServletHost = rb.getString("plantRequestServletHost");
			this.communityRequestServletHost = rb.getString("communityRequestServletHost");
			this.geoCoordRequestServletHost = rb.getString("geoCoordRequestServletHost");
			System.out.println("DBinsertPlotSource > authenticationServletHost: " + authenticationServletHost);
			System.out.println("DBinsertPlotSource > plantRequestServletHost: " + plantRequestServletHost );
			System.out.println("DBinsertPlotSource > communityRequestServletHost: " + communityRequestServletHost );
			System.out.println("DBinsertPlotSource > geoCoordRequestServletHost: " + geoCoordRequestServletHost );
			
			//initialize the data source
			source = new PlotDataSource(plugin);
			System.out.println("DBinsertPlotSource > initializing the plot plugin framework: " + plugin);
			source.getPlot(plot);

			System.out.println("DBinsertPlotSource > pooling database connections");
			conn=DBConnectionPool.getDBConnection("Need connection for inserting dataset");

			//System.out.println("opening log file");
			// make a new output log file
			out = new PrintWriter(new FileWriter(logFile));
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
			System.out.println("Exiting at DBinsertPlotSource constructor");
			//System.exit(0);
		}
	}

	/**
	 * this constructor, unlike the other is for inserting a potentially large
	 * numeber of plots stored on a given source
	 * @deprecated
	 */
	public DBinsertPlotSource(String plugin) throws FileNotFoundException {
		try {
			//initialize the data source 
			source = new PlotDataSource(plugin);
			
			// read the paramters from the properties file
			rb = ResourceBundle.getBundle("database");
			this.authenticationServletHost = rb.getString("authenticationServletHost");
			this.plantRequestServletHost = rb.getString("plantRequestServletHost");
			this.communityRequestServletHost = rb.getString("communityRequestServletHost");
			this.geoCoordRequestServletHost = rb.getString("geoCoordRequestServletHost");
			System.out.println("DBinsertPlotSource > authenticationServletHost: " + authenticationServletHost);
			System.out.println("DBinsertPlotSource > plantRequestServletHost: " + plantRequestServletHost );
			System.out.println("DBinsertPlotSource > communityRequestServletHost: " + communityRequestServletHost );
			System.out.println("DBinsertPlotSource > geoCoordRequestServletHost: " + geoCoordRequestServletHost );
			
			//initialize the database connection manager
			System.out.println("DBinsertPlotSource > pooling database connections");
			conn=DBConnectionPool.getDBConnection("Need connection for inserting dataset");

			out = new PrintWriter(new FileWriter(logFile));
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * this constructor is called when the class is going to connect 
	 * to the vegbank database and not to any of the plugins.  This 
	 * occurs when, for example, the class attemts to create the 
	 * summary tables.
	 * @deprecated
	 */
	public DBinsertPlotSource() {
		try {
			
			// read the paramters from the properties file
			rb = ResourceBundle.getBundle("database");
			this.authenticationServletHost = rb.getString("authenticationServletHost");
			this.plantRequestServletHost = rb.getString("plantRequestServletHost");
			this.communityRequestServletHost = rb.getString("communityRequestServletHost");
			this.geoCoordRequestServletHost = rb.getString("geoCoordRequestServletHost");
			System.out.println("DBinsertPlotSource > authenticationServletHost: " + authenticationServletHost);
			System.out.println("DBinsertPlotSource > plantRequestServletHost: " + plantRequestServletHost );
			System.out.println("DBinsertPlotSource > communityRequestServletHost: " + communityRequestServletHost );
			System.out.println("DBinsertPlotSource > geoCoordRequestServletHost: " + geoCoordRequestServletHost );
			
			//initialize the database connection manager
			System.out.println("DBinsertPlotSource > pooling database connections");
			conn=DBConnectionPool.getDBConnection("Need connection for inserting dataset");

		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * servlet plugin interface uses this
	 */
	public StringBuffer servletRequestHandler(	String action,
																																					Hashtable params) 
	{
		StringBuffer sb = new StringBuffer();
		sb.append("accessed the DBinsertPlotSource class");
		return (sb);
	}

	/**
	 * utility method to get a vector containg all the plots in a specific 
	 * data source
	 */
	private Vector getPlotNames() {
		Vector v = new Vector();
		try {
			v = source.getPlotNames();
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (v);
	}

	/**
	 * this method is used to create the denormalized tables that are 
	 * used by the vegbank for querying plots data.  The sql statements 
	 * in this method are the same as those in the 'makePlotSummaryTables_postgres.sql'
	 * script in the sql directory in cvs.  This method will be called by this 
	 * class after the insertPlot tarnsaction is carried out, and this method 
	 * is considered a single transaction, so that if any exceptions are thrown the 
	 * whole thing is 'rolled-back'
	 *
	 */
	private void createSummaryTables() {
		StringBuffer sb = new StringBuffer();
		try {
			this.conn.setAutoCommit(false);

			// DESTROY THE TABLES 
			sb.append("drop table plotSiteSummary;");
			sb.append("drop SEQUENCE PLOTSITESUMMARY_ID_seq;");
			sb.append("drop table plotSpeciesSum;");
			sb.append("drop SEQUENCE PLOTSPECIESSUM_ID_seq;");

			// RE-CREATE THE TABLES
			sb.append("CREATE SEQUENCE PLOTSITESUMMARY_ID_seq;");
			sb.append("CREATE TABLE plotSiteSummary (");
			sb.append(
				"PLOTSITESUMMARY_ID NUMERIC(20) default nextval('PLOTSITESUMMARY_ID_seq'),");
			sb.append("PLOT_ID integer,");
			sb.append("OBSERVATION_ID integer,");
			sb.append("PROJECT_ID integer,");
			sb.append("PLOTTYPE VARCHAR(30),");
			sb.append("SAMPLINGMETHOD VARCHAR(45),");
			sb.append("COVERSCALE VARCHAR(30),");
			sb.append("PLOTORIGINLAT NUMERIC(30),");
			sb.append("PLOTORIGINLONG NUMERIC(30),");
			sb.append("PLOTSHAPE VARCHAR(30),");
			sb.append("PLOTAREA VARCHAR(30),");
			sb.append("ALTVALUE float,");
			sb.append("SLOPEASPECT NUMERIC(30),");
			sb.append("SLOPEGRADIENT NUMERIC(30),");
			sb.append("SLOPEPOSITION VARCHAR(100),");
			sb.append("HYDROLOGICREGIME VARCHAR(100),");
			sb.append("SOILDRAINAGE VARCHAR(100),");
			sb.append("CURRENTCOMMUNITY VARCHAR(140),");
			sb.append("CURRENTCOMMUNITYCODE VARCHAR(45),");
			sb.append("XCOORD VARCHAR(100),");
			sb.append("YCOORD VARCHAR(100),");
			sb.append("COORDTYPE VARCHAR(100),");
			sb.append("OBSSTARTDATE DATE,");
			sb.append("OBSSTOPDATE DATE,");
			sb.append("EFFORTLEVEL VARCHAR(100),");
			sb.append("HARDCOPYLOCATION VARCHAR(100),");
			sb.append("SOILTYPE VARCHAR(30),");
			sb.append("SOILDEPTH VARCHAR(100),");
			sb.append("PERCENTROCKGRAVEL VARCHAR(30),");
			sb.append("PERCENTSOIL VARCHAR(30),");
			sb.append("PERCENTLITTER VARCHAR(30),");
			sb.append("PERCENTWOOD VARCHAR(30),");
			sb.append("PERCENTWATER VARCHAR(30),");
			sb.append("PERCENTSAND VARCHAR(30),");
			sb.append("PERCENTCLAY VARCHAR(30),");
			sb.append("PERCENTORGANIC VARCHAR(120),");
			sb.append("LEAFTYPE VARCHAR(50),");
			sb.append("PHYSIONOMICCLASS VARCHAR(100),");
			sb.append("AUTHORPLOTCODE VARCHAR(100),");
			sb.append("SURFGEO  VARCHAR(70),");
			sb.append("STATE VARCHAR(50),");
			sb.append("COUNTRY VARCHAR(100),");
			sb.append("PARENTPLOT NUMERIC(12),");
			sb.append("AUTHOROBSCODE VARCHAR(100),");
			sb.append("ACCESSION_NUMBER varchar (200),");
			sb.append(
				"CONSTRAINT plotSiteSummary_pk PRIMARY KEY (PLOTSITESUMMARY_ID)");
			sb.append(");");

			sb.append("CREATE SEQUENCE PLOTSPECIESSUM_ID_seq;");
			sb.append("CREATE TABLE plotSpeciesSum (");
			sb.append(
				"PLOTSPECIESSUM_ID integer default nextval('PLOTSITESUMMARY_ID_seq'), ");
			sb.append("PLOT_ID integer,");
			sb.append("OBS_ID integer, ");
			sb.append("PARENTPLOT integer, ");
			sb.append("AUTHORNAMEID VARCHAR(300), ");
			sb.append("AUTHORPLANTCODE VARCHAR(300), ");
			sb.append("AUTHORPLOTCODE VARCHAR(100), ");
			sb.append("AUTHOROBSCODE VARCHAR(100), ");
			sb.append("TAXONOBSERVATION_ID NUMERIC(20), ");
			sb.append("STRATUMTYPE VARCHAR(32), ");
			sb.append("PERCENTCOVER NUMERIC(8), "); // Should this be double precision
			sb.append(
				"CONSTRAINT plotSpeciesSum_pk PRIMARY KEY (PLOTSPECIESSUM_ID) ");
			sb.append(");");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			SQLWarning warning = pstmt.getWarnings();
			boolean r = pstmt.execute();

			// MAKE A NEW STRING BUFFER ETC
			sb = new StringBuffer();

			// INSERT THE DATA 
			sb.append("insert into plotSiteSummary (plot_id, project_id, ");
			sb.append(
				"plotoriginlat, plotoriginlong, plotshape, altvalue, slopeaspect, ");
			sb.append(
				"slopegradient, slopeposition, hydrologicregime, soildrainage, currentcommunity, ");
			sb.append(
				"xcoord, ycoord, coordtype, obsstartdate, obsstopdate, effortlevel,  ");
			sb.append(
				"authorplotcode, surfGeo, state, country, parentplot, authorobscode, ");
			sb.append("soilDepth, leaftype, accession_number ) ");
			sb.append("select plot.plot_id, observation.project_id,  ");
			sb.append(
				"plot.latitude, plot.longitude, plot.shape,  plot.elevation, ");
			sb.append(
				"plot.slopeaspect, plot.slopegradient, plot.topoposition, observation.hydrologicregime, ");
			sb.append(
				"observation.soildrainage, null, plot.authore, plot.authorn, ");
			sb.append(
				"plot.authordatum, observation.obsstartdate, observation.obsenddate, ");
			sb.append("observation.effortLevel, ");
			sb.append("plot.authorplotcode, plot.rocktype, plot.state, ");
			sb.append("plot.country, null, observation.authorobscode,  ");
			sb.append("observation.soilDepth, null, observation.obsaccessionnumber ");
			sb.append(
				"from plot, observation where observation.plot_id = plot.plot_id; ");

			sb.append(
				"insert into plotSpeciesSum (OBS_ID, AUTHORNAMEID, STRATUMTYPE, PERCENTCOVER ) ");
			sb.append("select ");
			sb.append("TAXONOBSERVATION.OBSERVATION_ID, ");
			sb.append("TAXONOBSERVATION.cheatPlantName, ");
			sb.append("STRATUMCOMPOSITION.CHEATSTRATUMNAME, ");
			sb.append("STRATUMCOMPOSITION.TAXONSTRATUMCOVER ");
			sb.append("from ");
			sb.append("	TAXONOBSERVATION, STRATUMCOMPOSITION ");
			sb.append("where  ");
			sb.append("	TAXONOBSERVATION.TAXONOBSERVATION_ID = ");
			sb.append("	STRATUMCOMPOSITION.TAXONOBSERVATION_ID ");
			sb.append("and ");
			sb.append("	STRATUMCOMPOSITION.STRATUMCOMPOSITION_ID > 0; ");

			// UPDATE THE VALUES
			sb.append(" update PLOTSITESUMMARY set OBSERVATION_ID = ");
			sb.append(
				" (select OBSERVATION_ID from OBSERVATION where OBSERVATION.PLOT_ID = PLOTSITESUMMARY.PLOT_ID); ");
			sb.append(" update PLOTSITESUMMARY set  CURRENTCOMMUNITY = ");
			sb.append(
				" (select COMMNAME from COMMCLASS where COMMCLASS.OBSERVATION_ID = PLOTSITESUMMARY.OBSERVATION_ID); ");
			sb.append(" update PLOTSITESUMMARY set  CURRENTCOMMUNITYCODE =  ");
			sb.append(
				" (select COMMCODE from COMMCLASS where COMMCLASS.OBSERVATION_ID = PLOTSITESUMMARY.OBSERVATION_ID); ");

			sb.append(" update plotSpeciesSum ");
			sb.append("set AUTHOROBSCODE  =  ");
			sb.append("(select OBSERVATION.AUTHOROBSCODE ");
			sb.append("from OBSERVATION ");
			sb.append(
				"where OBSERVATION.OBSERVATION_ID = PLOTSPECIESSUM.OBS_ID); ");

			//--update the 'parentPlot' table
			sb.append("update plotSpeciesSum ");
			sb.append("	set PARENTPLOT  =  ");
			sb.append("	(select OBSERVATION.PLOT_ID ");
			sb.append("	from OBSERVATION ");
			sb.append(
				"	where OBSERVATION.OBSERVATION_ID = PLOTSPECIESSUM.OBS_ID); ");

			//--update the authorplot code
			sb.append("update plotSpeciesSum ");
			sb.append("	set AUTHORPLOTCODE =  ");
			sb.append("	(select PLOT.AUTHORPLOTCODE from PLOT ");
			sb.append("	where PLOT.PLOT_ID = PLOTSPECIESSUM.PARENTPLOT); ");

			//--update the PLOT_ID
			sb.append("update plotSpeciesSum ");
			sb.append("	set PLOT_ID = ");
			sb.append("	(select PLOT.PLOT_ID from PLOT ");
			sb.append("	where PLOT.PLOT_ID = PLOTSPECIESSUM.PARENTPLOT); ");

			pstmt = conn.prepareStatement(sb.toString());
			warning = pstmt.getWarnings();
			r = pstmt.execute();

			// commit if there are no warning and if the execute executed 
			if (warning == null) {
				System.out.println("DBinsertPlotSource > got no warning denormalizing tables");
				this.conn.commit();
			} else {
				System.out.println("DBinsertPlotSource > got a warning denormalizing tables: ");
				//System.out.println(  warning.toString()  );
				System.out.println(r);
				this.conn.rollback();
			}
			// CLOSE THE OBJECTS
			System.out.println("DBinsertPlotSource > closing objects");
			pstmt.close();
			// let the connection pool know that the connection pool is to be destroyed
			//System.out.println("DBinsertPlotSource > destroying the conn. pool");
			//connectionBroker.manageLocalDbConnectionBroker("destroy");
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	* the main routine used to test the DB class which interacts with the 
	 * vegclass database.
	* 
	* Usage: DBinsertPlotSource pluginName plot1 plot2 ... plotn
	*
	*/
	static public void main(String[] args) {
		try {
			//load an individual plot aor allow the user to specify the plots to load
			if (args.length >= 2) {
				//get the plugin named
				String plugin = args[0];
				//if there is only one plot then handle that differently than multiple
				if (args.length == 2) {
					String plot = args[1];
					String emailAddress = "test@test.com";
					System.out.println("loading single plot: " + plot + "\n");
					DBinsertPlotSource db =
						new DBinsertPlotSource(plugin, plot);
					String s = db.insertPlot(plot, 2, emailAddress);
					System.out.println("RECEIPT: \n" + s);
				}
			}
			//load all the plots in the package
			else if (args.length == 1) {
				System.out.println(
					"DBinsertPlotSource > loading entire source");
				String plugin = args[0];
				DBinsertPlotSource db = new DBinsertPlotSource(plugin);
				Vector v = db.getPlotNames();
				//System.out.println("DBinsertPlotSource > plots: " + v.toString() );
				for (int i = 0; i < v.size(); i++) {
					String emailAddress = "test@test.com";
					String plot = (String) v.elementAt(i);
					plot = plot.trim();
					System.out.println(
						"DBinsertPlotSource > loading plot: " + plot + " \n");
					db = new DBinsertPlotSource(plugin, plot);
					db.insertPlot(plot, 2, emailAddress);
					db = null;
				}
			} else {
				System.out.println(
					"Usage: DBinsertPlotSource pluginName plot1 plot2 ... plotn");
				// TEST THE DENORMALIZATION ROUTINE
				// no plugin needed b/c we are not connecting to the local data source
				DBinsertPlotSource db = new DBinsertPlotSource();
				db.createSummaryTables();
			}
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * method that loads an entire plot package
	 *
	 * @param plotNames -- a vector that holds all the plotNames
	 */
	public void insertPlotPackage(String pluginClass, String emailAddress) 
	{
		try {
			//initialize the data source 
			source = new PlotDataSource(pluginClass);
			Vector plotNames = source.getPlotNames();

			System.out.println("plots to be inserted: " + plotNames.toString());
			//Thread.sleep(3000);

			for (int i = 0; i < plotNames.size(); i++) {

				String pName = plotNames.elementAt(i).toString();
				source = new PlotDataSource(pluginClass);
				DBinsertPlotSource db =
					new DBinsertPlotSource(pluginClass, pName);
				db.insertPlot(pName, emailAddress);
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
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
	public String insertPlot(
		String plotName,
		int debugLevel,
		String emailAddress)
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
				System.out.println(
					"DbinsertPlotSource > invalid debug level: " + debugLevel);
				this.insertPlot(plotName, emailAddress);
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: " + e.getMessage());
			debug.append(
				"<exceptionMessage>" + e.getMessage() + "</exceptionMessage>\n");
			e.printStackTrace();
		}
		return (debug.toString());
	}

	/**
	 * method to initiate the insertion of a plot, there are a series of private
	 * methods that will be called to actually get the data into the database.  
	 * This is the main method that initiates the loading of a plot and the input
	 * parameter 'plotName' refers to the actual plotName for the tnc database and 
	 * the plot_id value for the vegbank (both central and access client)
	 *
	 * @param plotName -- the name of the plot that exists in the archive
	 * @param emailAddress -- the VegBank valid email address of the person
	 *	inserting this plot
	 */
	public void insertPlot(String plotName, String emailAddress)
	{
		//add a line for the user that is inserting the data
		debug.append("<vegbankUser>" + emailAddress + "</vegbankUser> \n");
		// before anything else is done verify if the user has appropriate 
		// priveleges 
		if (this.getUserPrivileges(emailAddress) <= 1)
		{
			debug.append("<permissionLevel>invalid</permissionLevel> \n");
			// close the connections 
			System.out.println("Returning the DBConnection to the pool");
			//Return dbconnection too pool
			DBConnectionPool.returnDBConnection(conn, conn.getCheckOutSerialNumber());
		}
		else
		{
			try
			{
				System.out.println("DBinsertPlotSource > inserting plot: " + plotName);
				
				// update the instance vraible with the user's email address
				// which will be used for loading the plot and for constructing the 
				// accession number
				this.submitterEmail = emailAddress;
				this.plotName = plotName;

				//this boolean determines if the plot should be commited or rolled-back
				boolean commit = true;

				//set the auto commit option on the connection to false after getting a				
				conn.setAutoCommit(false);

				this.projectName =
					Utility.escapeCharacters(source.getProjectName(plotName));
				this.projectDescription =
					Utility.escapeCharacters(source.getProjectDescription(plotName));
				this.projectStartDate =
					Utility.escapeCharacters(source.getProjectStartDate(plotName));
				this.projectStopDate =
					Utility.escapeCharacters(source.getProjectStopDate(plotName));

				System.out.println("DBinsertPlotSource > projectName: " + projectName);
				System.out.println(
					"DBinsertPlotSource > projectDescription: " + projectDescription);
				System.out.println(
					"DBinsertPlotSource > project start: " + projectStartDate);
				System.out.println(
					"DBinsertPlotSource > project stop: " + projectStopDate);

				// SEE IF THE PROJECT IN WHICH THIS PLOT IS A MEMBER EXISTS IN THE DATABASE
				if (projectExists(projectName) == true)
				{
					projectId = getProjectId(projectName);
				}
				else if (projectName == null)
				{
					// This is  a hack allow loading of a plot without project info
				}
				// IF THE PROJECT IS NOT THERE THEN LOAD IT AND THE PROJECT CONT. INFO
				else
				{

					projectId =insertProject(
						projectName,
						projectDescription,
						projectStartDate,
						projectStopDate);
					
					System.out.println("DBinsertPlotDataSource > ProjectId: " + projectId);
					// INSERT THE PROJECT CONTRIBUTOR INFORMATION HERE, THE PARTY, ADDRESS, 
					// TELEPHONE ALSO LOADED FROM HERE
					Vector projContributors = source.projectContributors;
					for (int ii = 0; ii < projContributors.size(); ii++)
					{
						String contributor = (String) projContributors.elementAt(ii);
						String salutation =
							Utility.escapeCharacters(
								source.getProjectContributorSalutation(contributor));
						String surName =
							Utility.escapeCharacters(
								source.getProjectContributorSurName(contributor));
						String givenName =
							Utility.escapeCharacters(
								source.getProjectContributorGivenName(contributor));
						String email =
							Utility.escapeCharacters(
								source.getProjectContributorEmailAddress(contributor));
						String role = "project manager";
						String orgName =
							Utility.escapeCharacters(
								source.getProjectContributorOrganizationName(contributor));
						String contact =
							Utility.escapeCharacters(
								source.getProjectContributorContactInstructions(contributor));

						insertProjectContributor(
							salutation,
							givenName,
							surName,
							email,
							role,
							projectId,
							orgName,
							contact,
							contributor);
					}
				}
				//insertNamedPlace();
				if (insertStaticPlotData(projectId) == false)
				{
					System.out.println("DBinsertPlotSource > insert static data: " + commit);
					commit = false;
				}
				else
				{
					// Check if the CoverMethod Exists 
					int possibleCoverMethodId = this.getCoverMethodId();
					if ( possibleCoverMethodId != 0 )
					{
						this.coverMethodId = possibleCoverMethodId;
					}
					else
					{
						if (insertCoverMethod() == false)
						{
							commit = false;
							System.out.println("DBinsertPlotSource > insert covermethod: " + commit);
						}
					}

					// Check if the StratumMethod Exists 
					int possibleStratumeMethodId = this.getStatumMethodId();
					if ( possibleStratumeMethodId != 0 )
					{
						this.stratumMethodId = possibleStratumeMethodId;
					}
					else
					{
						if (insertStratumMethod() == false)
						{
							commit = false;
							System.out.println("DBinsertPlotSource > insert stratummethod: " + commit);
						}
					}
						
					if (insertPlotObservation() == false)
					{
						commit = false;
						System.out.println("DBinsertPlotSource > insert observation: " + commit);
					}
					if (insertCommunities() == false)
					{
						commit = false;
						System.out.println("DBinsertPlotSource > insert communities: " + commit);
					}

					if (insertStrata() == false)
					{
						commit = false;
						System.out.println("DBinsertPlotSource > insert strata: " + commit);
					}
					// BOTH THE TAXON OBSERVATION TABLES
					// AND THE STRATA COMPOSITION ARE LOADED HERE
					if (insertTaxonObservations() == false)
					{
						commit = false;
						System.out.println("DBinsertPlotSource > insert  taxonobservation: " + commit);
						debug.append("<taxaInsertion>" + commit + "</taxaInsertion> \n");
					}
					else
					{
						debug.append("<taxaInsertion>true</taxaInsertion> \n");
					}
				}
				System.out.println("DBinsertPlotSource > insertion success: " + commit);
				if (commit == true)
				{
					conn.commit();
					debug.append("<insert>true</insert>\n");
					// CREATE THE DENORM TABLES
					this.createSummaryTables();
				}
				else
				{
					conn.rollback();
					debug.append("<insert>false</insert>\n");
				}
				//this.testSimpleQuery("about to finish");
				System.out.println("Returning the DBConnection to the pool");
				//Return dbconnection too pool
				DBConnectionPool.returnDBConnection(conn, conn.getCheckOutSerialNumber());;
			}
			catch (Exception e)
			{
				System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
				debug.append(
					"<exceptionMessage>" + e.getMessage() + "</exceptionMessage>\n");
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return
	 */
	private int getCoverMethodId()
	{
		int result = 0;
		try
		{
			StringBuffer sb = new StringBuffer();

			sb.append(
				"SELECT covermethod_id from COVERMETHOD where covertype = '"
					+ source.getCoverMethodName(plotName)
					+ "'");

			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			int cnt = 0;
			while (rs.next())
			{
				result = rs.getInt(1);
				cnt++;
			}
			//send warnings
			if (cnt == 0)
			{
				System.out.println(
					"warning: There were no covermethods matching: "
						+ source.getCoverMethodName(plotName));
			}
		}
		catch (SQLException se)
		{
			System.out.println("Caught SQL Exception: " + se.getMessage());
			if (!se.getMessage().equals("No results were returned by the query."))
			{
				se.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 */
	private void testSimpleQuery(String message)
	{
		System.out.println(message);
		try 
		{
				StringBuffer sb = new StringBuffer();

				sb.append("select organizationname from party"); 

				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				
				while (rs.next())
				{
							System.out.println(rs.getString(1));
			  }
		}
		catch ( Exception e )
		{
			System.out.println("########## Come Onnn there must be journals in the db");
		}
	}

	/**
	 * @return
	 */
	private int getStatumMethodId()
	{
		int result = 0;
		try {
			StringBuffer sb = new StringBuffer();

			sb.append(
				"SELECT stratummethod_id from STRATUMMETHOD where stratummethodname = '"
					+ source.getStratumMethodName(plotName) + "'"); 

			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			int cnt = 0;
			while (rs.next()) {
				result = rs.getInt(1);
				cnt++;
			}
			//send warnings
			if (cnt == 0) {
				System.out.println(
					"warning: There were no stratamethods matching: " + source.getStratumMethodName(plotName) );
			}
		}
		catch (SQLException se) 
		{
			System.out.println("Caught SQL Exception: " + se.getMessage());
			if ( !se.getMessage().equals("No results were returned by the query.") )
			{
				se.printStackTrace();
			}
		}
		return result;
	}



	/**
	 * method that returns the priveleges of a user based on their email address
	 * @param email -- the email address of the user
	 * @return privilege value -- and interger between 1 - 5
	 */
	private int getUserPrivileges(String emailAddress) {
		int privilegeLevel = 0;
		try {
			//THIS USES THE REQUESTURL METHOD
			String protocol = "http://";
			String host = this.authenticationServletHost;
			System.out.println("DBinsertPlotSource > requesting permission level from: " + this.authenticationServletHost);
			String s = null;
			//THIS USES THE OTHER REQUEST URL METHOD
			String servlet = "/vegbank/servlet/usermanagement";
			Properties parameters = new Properties();
			parameters.setProperty("action", "getpermissionlevel");
			parameters.setProperty("user", emailAddress);
			s = GetURL.requestURL(servlet, protocol, host, parameters);
			try {
				privilegeLevel = Integer.parseInt(s.trim());
			} catch (Exception e1) {
				System.out.println(
					"DBinsertPlotSource > Exception: couldn't parse privilegeLevel: '"
						+ s
						+ "' "
						+ e1.getMessage());
			}
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (privilegeLevel);
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
	 * @param orgName -- the name of the org that the contributor belongs
	 * @param contact -- the contact instructions
	 * @param contributor -- the contributors whole name which is used 
	 * to lookup via the 'PlotDataSource' object the required info
	 *
	 */
	private void insertProjectContributor(
		String salutation,
		String givenName,
		String surName,
		String email,
		String role,
		int projectId,
		String orgName,
		String contact,
		String contributor) {
		try {
			int partyId = 0;
			boolean pExists = partyExists(salutation, givenName, surName);
			System.out.println("DBinsertPlotSource > party exists: " + pExists);
			// INSERT INTO THE PARTY TABLE FIRST, ALONG WITH THE ADDRESS AND TELEPHONE
			if (pExists == false) {
				partyId =
					insertParty(
						salutation,
						givenName,
						surName,
						email,
						orgName,
						contact);
				if (partyId > 0) {
					// THESE METHODS USER THE 'PlotDataSource' Object directly
					insertProjectContributorAddress(partyId, contributor);
					insertProjectContributorTelephone(partyId, contributor);
				}
			}

			//else get the party id for this contributor
			else {
				partyId = getPartyId(salutation, givenName, surName);
			}
			// insert the role table

			//insert into the project contributor table
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into PROJECTCONTRIBUTOR ");
			sb.append("( party_id, project_id, surName, role) ");
			sb.append(
				" values ("
					+ partyId
					+ ", "
					+ projectId
					+ ", '"
					+ surName
					+ "', '"
					+ role
					+ "')");

			Statement insertStatement = conn.createStatement();
			insertStatement.executeUpdate(sb.toString());
			insertStatement.close();
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * handle the loading of the observation contributor information
	 *
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 * @param email
	 * @param roleDescription -- Decription of contributors role
	 * @param observationId
	 * @param orgName -- the name of the org that the contributor belongs
	 * @param contact -- the contact instructions
	 * @param contributor -- the contributors whole name which is used 
	 * to lookup via the 'PlotDataSource' object the required info
	 *
	 */
	private void insertObservationContributor( 	String salutation,
																																	        String givenName,
																																	        String surName,
																																	        String email,
																																	        String roleDescription,
																																	        int observationId,
																																	        String orgName,
																																	        String contact,
																																	        String contributor) 
    {
		try 
		{
			int partyId = 0;
			boolean pExists =	partyExists(salutation, givenName, surName);
			System.out.println("DBinsertPlotSource > party exists: " + pExists);
			// INSERT INTO THE PARTY TABLE FIRST, ALONG WITH THE ADDRESS AND TELEPHONE
			if (pExists == false) 
			{
				partyId = insertParty(salutation,
						                              givenName,
																			    surName,
																			    email,
																			    orgName,
																			    contact
																			    );
				if (partyId > 0) 
				{
					// THESE METHODS USER THE 'PlotDataSource' Object directly
					insertObservationContributorAddress(partyId, contributor);
					insertObservationContributorTelephone(partyId, contributor);
				}
			}
			else 
			{
				partyId = getPartyId(salutation, givenName, surName);
			}
			// insert the role table
			
			// Use roleDescription to find existing roleId or to create a new one.
			int roleId = 0;			

			//insert into the project contributor table
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into OBSERVATIONCONTRIBUTOR ");
			sb.append("( party_id, observation_id , role_id) ");
			sb.append(" values (?,?,?)");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, partyId);
			pstmt.setString(2, "" + observationId);
			pstmt.setString(3, "" + roleId);

      pstmt.execute();
      pstmt.close();

      // DEBUG
      //System.out.println("DBinsertPlotSources > " + sb.toString() + "; partyId=" + partyId 
      //+ "; observationId=" + observationId + "; roleId=" + roleId); 
      
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}	

	/**
	 * method that inserts address information for a user
		* by using the contributor ( the wholename of the user
		* and the partyId.  The contributor string is used directly
		* with the PlotDataSource object for looking up the address
		* information
		* @param partyId  -- the partyid for the party table
		* @param contributor -- the whole name of the contributor
		*/
	private void insertTelephone(int partyId, String number) 
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			String type = "work";

			sb.append("INSERT into TELEPHONE ");
			sb.append("( party_id, phonenumber, phonetype ");
			sb.append(" ) ");
			sb.append(" values (?,?,?)");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, partyId);
			pstmt.setString(2, number);
			pstmt.setString(3, type);

			pstmt.execute();
			pstmt.close();
		} 
		catch (Exception e) 
		{
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
		}
	}
	
	/**
   * Get the projectContributors Telephone number and call a method that inserts
   * it into the database
   *
   *  @param partyId      -- The foriegn key
   *  @param contributor  -- The full name of the contibutor
   **/
  private void insertProjectContributorTelephone(int partyId, String contributor)
	{
		String number = Utility.escapeCharacters( source.getProjectContributorPhoneNumber(contributor) );	
		this.insertTelephone(partyId, number);
	}
	
	/**
   * Get the observationContributors Telephone number and call a method that inserts
   * it into the database
   *
   *  @param partyId      -- The foriegn key
   *  @param contributor  -- The full name of the contibutor
   **/
	private void insertObservationContributorTelephone(int partyId, String contributor) 
  {
		String number = Utility.escapeCharacters( source.getObservationContributorPhoneNumber(contributor) );	
		this.insertTelephone(partyId, number);
  }
  
	/**
	 * method that inserts address information for a user
		* by using the contributor ( the wholename of the user
		* and the partyId.  The contributor string is used directly
		* with the PlotDataSource object for looking up the address
		* information
		* @param partyId  -- the partyid for the party table
		* @param contributor -- the whole name of the contributor
		*/
	private void insertAddress(int partyId,  String email, String street, String city, String adminArea, String postalCode, String country) 
	{

		StringBuffer sb = new StringBuffer();
		try {
			sb.append("INSERT into ADDRESS ");
			sb.append(
				"( party_id, email, deliverypoint, city, administrativearea, ");
			sb.append(" postalcode, country ) ");
			sb.append(" values (?,?,?,?,?,?,?)");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, partyId);
			pstmt.setString(2, email);
			pstmt.setString(3, street);
			pstmt.setString(4, city);
			pstmt.setString(5, adminArea);
			pstmt.setString(6, postalCode);
			pstmt.setString(7, country);

			pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
		}
	}
	
  /**
	 *  Fetch the address information observationContributor and call a method that 
	 *  inserts it into the database.
	 * 
	 * @param partyId -- Foriegn key to use 
	 * @param contributor -- Full name of contributor
	 * */
  
	private void insertObservationContributorAddress(int partyId, String contributor)
	{
		String email = Utility.escapeCharacters( source.getObservationContributorEmailAddress(contributor) );
		String street = Utility.escapeCharacters( source.getObservationContributorDeliveryPoint(contributor) );
		String adminArea = Utility.escapeCharacters( source.getObservationContributorAdministrativeArea(contributor) );
		String city = Utility.escapeCharacters( source.getObservationContributorCity(contributor) );
		String postalCode = Utility.escapeCharacters( source.getObservationContributorPostalCode(contributor) );
		String country =  Utility.escapeCharacters( source.getObservationContributorCountry(contributor) );

		this.insertAddress(partyId, email, street, city, adminArea, postalCode, country);		
	}

  /**
	 *  Fetch the address information projectContributor and call a method that 
	 *  inserts it into the database.
	 * 
	 * @param partyId -- Foriegn key to use 
	 * @param contributor -- Full name of contributor
	 * */
  
	private void insertProjectContributorAddress(int partyId, String contributor)
	{
		String email = Utility.escapeCharacters( source.getProjectContributorEmailAddress(contributor) );
		String street = Utility.escapeCharacters( source.getProjectContributorDeliveryPoint(contributor) );
		String adminArea = Utility.escapeCharacters( source.getProjectContributorAdministrativeArea(contributor) );
		String city = Utility.escapeCharacters( source.getProjectContributorCity(contributor) );
		String postalCode = Utility.escapeCharacters( source.getProjectContributorPostalCode(contributor) );
		String country =  Utility.escapeCharacters( source.getProjectContributorCountry(contributor) );
	
    this.insertAddress(partyId, email, street, city, adminArea, postalCode, country);		
	}


	/**
	 * Gets the party id for an the matching individual
	 * otherwise return code of 0 is returned ... 
   * A 0 return represents a failure to get the party id
   * 
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 */
	private int getPartyId(	String salutation,
											    String givenName,
											    String surName
                          ) 
	{
		int i = 0;
		StringBuffer sb = new StringBuffer();

		try 
		{
			sb.append("SELECT party_id from PARTY where ");
			
      if (salutation != null)   // Use salutation in search if available   
      {
        sb.append(" upper(salutation) ");
			  sb.append(" like '" + salutation.toUpperCase() + "'");
			  sb.append(" and ");
      }
      
			sb.append(" upper(givenName) ");
			sb.append(" like '" + givenName.toUpperCase() + "'");
			sb.append(" and ");
			sb.append(" upper(surName) ");
			sb.append(" like '" + surName.toUpperCase() + "'");
			System.out.println("DBinsertPlotSource > query: " + sb.toString());
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			
			while (rs.next())
			{
				i = rs.getInt(1);
			}
		} 
    catch (SQLException se) 
    {
			System.out.println("Caught SQL Exception: " + se.getMessage());
      if ( !se.getMessage().equals("No results were returned by the query.") )
      {
			  System.out.println("sql: " + sb.toString());
        se.printStackTrace();
      }
		}
	  catch (Exception e) 
		{
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (i);
	}
	
	/**
	 * Gets the party id for an the matching individual
	 * otherwise return code of 0 is returned ... 
	 * A 0 return represents a failure to get the party id
	 * 
	 * @param organizationName
	 */
	private int getPartyId(	String organizationName)
	{
		int i = 0;
		StringBuffer sb = new StringBuffer();

		try 
		{
			sb.append("SELECT party_id from PARTY where ");

      
			sb.append(" upper(organizationName) ");
			sb.append(" like '" + organizationName.toUpperCase() + "'");
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			
			rs.next();
			i = rs.getInt(1);
		} 
		catch (SQLException se) 
		{
			System.out.println("Caught SQL Exception: " + se.getMessage());
			if ( !se.getMessage().equals("No results were returned by the query.") )
			{
				System.out.println("sql: " + sb.toString());
				se.printStackTrace();
			}
		}
		catch (Exception e) 
		{
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (i);
	}

	/**
	 * method that returns true if the party already exists in the database
	 * otherwise false is returned
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 * @param email
	 * @param orgName 
	 * @param contact -- the contact instructions 
	 */
	private int insertParty(
		String salutation,
		String givenName,
		String surName,
		String email,
		String orgName,
		String contact) {
		int partyId = 0;
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("INSERT into PARTY ");
			sb.append(
				"( salutation, givenName, surName, email, organizationName,");
			sb.append(" contactinstructions) ");
			sb.append(
				" values ('"
					+ salutation
					+ "', '"
					+ givenName
					+ "', '"
					+ surName
					+ "', '");
			sb.append(email + "', '" + orgName + "', '" + contact + "')");

			Statement insertStatement = conn.createStatement();
			insertStatement.executeUpdate(sb.toString());

			//get the partyid associated with this entry
			sb = new StringBuffer();
			sb.append("select max(party_id) from party");
			insertStatement = conn.createStatement();
			ResultSet rs = insertStatement.executeQuery(sb.toString());
			while (rs.next()) {
				partyId = rs.getInt(1);
			}


		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (partyId);
	}

	/**
	 * method that returns true if the party already exists in the database
	 * otherwise false is returned
	 * @param salutation 
	 * @param surName
	 * @param givenName
	 */
	private boolean partyExists(String salutation,
		                          String givenName,
		                          String surName
                              ) 
    {
    
		int rows = 0;
    
    try 
    {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT count(*) from PARTY where ");
      
      if (salutation != null)   // Use salutation in search if available 
      {
			  sb.append(" upper(salutation) ");
			  sb.append(" like '" + salutation.toUpperCase() + "'");
			  sb.append(" and ");
      }
      
			sb.append(" upper(givenName) ");
			sb.append(" like '" + givenName.toUpperCase() + "'");
			sb.append(" and ");
			sb.append(" upper(surName) ");
			sb.append(" like '" + surName.toUpperCase() + "'");
			System.out.println("DBinsertPlotSource > query: " + sb.toString());
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
		
      while (rs.next()) 
      {
				rows = rs.getInt(1);
			}
		} 
    catch (SQLException se) 
    {
			System.out.println("Caught SQL Exception: " + se.getMessage());
      if ( !se.getMessage().equals("No results were returned by the query.") )
      {
        se.printStackTrace();
      }
		}
		if (rows == 0) 
    {
			System.out.println("DBinsertPlotSource >  party does not exist");
			return (false);
		} 
    else 
    {
			System.out.println("DBinsertPlotSource > party does exist");
			return (true);
		}
	}

	/**
	 * method to update the strata composition tables --should only be called
	 * from the insert taxonObservation method
	 *
	 * @param plantName -- the plantName as it is used in the data source
	 * @param plantCode -- a code that is associated with the name used by the author
	 * @param taxonObservationId -- the taxonobservation for this plant
	 *
	 */
	private boolean insertStrataComposition(	String plantName,
		                                        														String plantCode,
		                                      	  													int taxonObservationId ) 
  {
		StringBuffer sb = new StringBuffer();
		try 
    {
			System.out.println(
				"DBinsertPlotSource > getting the strata for plot: "
					+ plotName
					+ " "
					+ plotId);
          
			Vector strataVec = source.getTaxaStrataExistence(plantName, plotName);
			for (int ii = 0; ii < strataVec.size(); ii++) 
      {
				sb = new StringBuffer();
				String curStrata = strataVec.elementAt(ii).toString();

				// get the cover of the plant within that strata -- if this comes back
				// null then there was no observation of this plant in the strata
				String cover = Utility.escapeCharacters( source.getTaxaStrataCover(plantName, plotName, curStrata) );
				if (cover != null) 
        {
					//get the strataCompId number
					int stratumCompositionId = getNextId("stratumComposition");
					
					//get the strata ID
					int stratumId = 0;
					try
					{
						stratumId = ( (Integer) this.stratumIds.get(curStrata)).intValue();
					}
					catch (RuntimeException e1)
					{
						 stratumId = this.getStrataId(plotObservationId, curStrata);
					}
					
					debug.append("<stratumComposition> \n");
					debug.append("<stratumName>" + curStrata + "</stratumName> \n");
					debug.append("<taxonName>"+plantName.replace('&', '_')+"</taxonName>\n");
					debug.append("<cover>" + cover + "</cover> \n");
					debug.append("</stratumComposition> \n");

					//insert the strata composition values
					sb.append(
						"INSERT into STRATUMCOMPOSITION ( stratumComposition_Id, "
							+ " cheatPlantName, cheatStratumName, taxonStratumCover, stratum_id, "
							+ " taxonobservation_id) ");
					sb.append("values(?,?,?,?,?,?)");

					PreparedStatement pstmt =
						conn.prepareStatement(sb.toString());

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
			System.out.println("Caught SQL Exception: " + sqle.getMessage());
			System.out.println("sql: " + sb.toString());
			debug.append(
				"<exceptionMessage>"
					+ sqle.getMessage()
					+ " (sqle) loading strata Composition data</exceptionMessage>\n");
			sqle.printStackTrace();
			return (false);
		} 
    catch (Exception e) 
    {
			System.out.println("Caught Exception: " + e.getMessage());
			debug.append(
				"<exceptionMessage>"
					+ e.getMessage()
					+ "</exceptionMessage>\n");
			e.printStackTrace();
			return (false);
		}
		return (true);
	}

	/**
	 * method to insert the cover method data
	 */
	private boolean insertCoverMethod() {

		try {
			StringBuffer sb = new StringBuffer();
			//get the strataId number
			coverMethodId = getNextId("covermethod");
			String coverType = source.getCoverMethodName(plotName);
			//insert the strata values
			sb.append("INSERT into COVERMETHOD (covermethod_id, coverType) ");
			sb.append("values(?,?)");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());

			// Bind the values to the query and execute it
			pstmt.setInt(1, coverMethodId);
			pstmt.setString(2, coverType);

			//execute the p statement
			pstmt.execute();
			// pstmt.close();
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
			//return false so that the calling method knows to roll-back
			return (false);
			//System.exit(0);
		}
		return (true);
	}

	/**
	 * method to insert the stratum method data
	 */
	private boolean insertStratumMethod()
	{
		//this.testSimpleQuery("INSERTING STRATUMMETHOD:");
		StringBuffer sb = new StringBuffer();
		try
		{
			//get the strataId number
			stratumMethodId = getNextId("stratummethod");
			String stratumMethodName = source.getStratumMethodName(plotName);
			//insert the strata values
			sb.append(
				"INSERT into STRATUMMETHOD (stratummethod_id, stratummethodname) ");
			sb.append("values(?,?)");
			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			// Bind the values to the query and execute it
			pstmt.setInt(1, stratumMethodId);
			pstmt.setString(2, stratumMethodName);
			//execute the p statement
			pstmt.execute();
			// pstmt.close();
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
			//return false so that the calling method knows to roll-back
			return (false);
			//System.exit(0);
		}

		Vector strataTypes = source.uniqueStrataNames;
		for (int i = 0; i < strataTypes.size(); i++)
		{
			String sName = strataTypes.elementAt(i).toString();
			String description = "";

			int stratumTypeId =
				this.insertStratumType(this.stratumMethodId, sName, sName, description);
		}

		return (true);
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
	private int getStrataId(int plotObservationId, String stratumType) {
		int strataId = -999;
		try {
			StringBuffer sb = new StringBuffer();

			sb.append(
				"SELECT STRATUM_ID from STRATUM where OBSERVATION_ID = "
					+ plotObservationId
					+ " and stratumName like '%"
					+ stratumType
					+ "%'");

			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			int cnt = 0;
			while (rs.next()) {
				strataId = rs.getInt(1);
				cnt++;
			}
			//send warnings
			if (cnt == 0) {
				System.out.println(
					"warning: There were no strata matching: " + stratumType);
			}
    }
    catch (SQLException se) 
    {
			System.out.println("Caught SQL Exception: " + se.getMessage());
      if ( !se.getMessage().equals("No results were returned by the query.") )
      {
        se.printStackTrace();
      }
		}
		return (strataId);
	}

	/**
	 * method to add the taxonObservation data to the vegbank database.  This 
	 * method handles the loading of the 'taxonobservation' table and also
	 * calls the method that loads the 'stratumcomposition' table.
	 *
	 * @see insertStratumComposition -- called by this method
	 * @return successfulCommit -- true if a successfull commit
	 */
	private boolean insertTaxonObservations()
	{
		boolean successfulCommit = true;

		System.out.println("DBinsertPlotSource > inserting taxonomy data for: "+plotName);
		//this.testSimpleQuery("Inserting taxon data");
		//get the number of taxonObservations
		Vector uniqueTaxa = source.plantTaxaNames;
		// INSERT EACH OF THE PLANTS ASSOCIATED WITH THIS PLOT
		for (int i = 0; i < uniqueTaxa.size(); i++)
		{
			StringBuffer sb = new StringBuffer();
			int nameId = 0;
			int taxonObservationId = 0;
			
			// get the taxonObservation number which will be used in the 
			taxonObservationId = getNextId("taxonObservation");
			String authorNameOfPlant =
				Utility.escapeCharacters(uniqueTaxa.elementAt(i).toString());
			String code =
				Utility.escapeCharacters(source.getPlantTaxonCode(authorNameOfPlant));
			String taxonCover =
				Utility.escapeCharacters(source.getPlantTaxonCover(authorNameOfPlant, this.plotName));
			System.out.println(
				"DBinsertPlotSource > cur. tax. name: "+ authorNameOfPlant+" code: "+ code 
				+ " cover: " + taxonCover
			);

			if (code != null && code.length() > 1)
			{
				// Get the plantName_ID from the code
				nameId = RectificationUtility.getForiegnKey(conn, RectificationUtility.GETPLANTNAMEID, code);
			}
			else
			{
				// Get the plantName_ID from the scientific name
				nameId = RectificationUtility.getForiegnKey(conn, RectificationUtility.GETPLANTNAMEID, authorNameOfPlant);
			}
			
			if (nameId == 0)
			{
				// Could not find a matching name in the database
				debug.append(
					"<exceptionMessage>Could not find plant taxon '"+ authorNameOfPlant + "' in database</exceptionMessage>\n"
				);
				successfulCommit  = true;
			}
			
			if ( taxonCover == null || taxonCover.equals("") )
			{
				// Could not find a taxonCover for this plant
				debug.append(
					"<exceptionMessage>Did not find a valid plant taxon cover for  '"+ authorNameOfPlant + "' in data source</exceptionMessage>\n"
				);
				successfulCommit  = false;
			}

			//add the taxon name info to the debugging output
			debug.append("<taxonObservation>\n");
			debug.append(
				"<authorTaxonName>"
					+ authorNameOfPlant.replace('&', '_')
					+ "</authorTaxonName>\n");
			debug.append("<authorTaxonCode>" + code + "</authorTaxonCode>\n");
			debug.append("<taxonCover>" + taxonCover + "</taxonCover>\n");
			debug.append("<vegbankMatch> \n");
			debug.append("<vegbankNameId>" + nameId + "</vegbankNameId>\n");
			debug.append("</vegbankMatch> \n");
			debug.append("</taxonObservation> \n");

			//insert the values
			sb.append(
				"INSERT into TAXONOBSERVATION (TAXONOBSERVATION_ID, OBSERVATION_ID, ");
			sb.append(" PLANTNAME_ID, TAXONCOVER, CHEATPLANTNAME ) ");
			sb.append(" values(?,?,?,?,?) ");

			try
			{
				PreparedStatement pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, taxonObservationId);
				pstmt.setInt(2, plotObservationId);
				if (nameId != 0)
				{
					pstmt.setInt(3, nameId);
				}
				else
				{
					pstmt.setNull(3, Types.INTEGER);
				}
				
				if (taxonCover == null || taxonCover.trim().equals("") )
				{
					pstmt.setNull(4, Types.DOUBLE );
				}
				else
				{
					pstmt.setString(4, taxonCover);
				}
				pstmt.setString(5, authorNameOfPlant);

				pstmt.execute();

				// insert the strata composition
				boolean result =
					this.insertStrataComposition(authorNameOfPlant, code, taxonObservationId);
				if (result == false)
				{
					successfulCommit = false;
				}

				debug.append("<insertStrataComp>" + result + "</insertStrataComp>\n");
				pstmt.close();
			}
			catch (Exception e)
			{
				System.out.println("Caught Exception: " + e.getMessage());
				System.out.println("sql: " + sb.toString());
				e.printStackTrace();
				successfulCommit = false;
			}
			
			boolean result = insertTaxonInterpritation(nameId, taxonObservationId);
			if ( result == false)
			{
				successfulCommit = false;
			}
		}
		return (successfulCommit);
	}

	/**
	 * @param nameId
	 * @param taxonObservationId
	 */
	private boolean insertTaxonInterpritation(int nameId, int taxonObservationId)
	{
		boolean result = true;
		
		// Need to get the PK, the conceptId, partyId and date
		int taxonInterpretationId = getNextId("taxonInterpretation");
		int conceptId = RectificationUtility.getForiegnKey(conn, RectificationUtility.GETPLANTCONCEPTID, new Integer(nameId) );
		int partyId = this.getPartyId("NPS");   // FIXME: get from loader
		int roleId = this.getRoleId("Not specified");
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(
			"INSERT into TAXONINTERPRETATION (TAXONOBSERVATION_ID, TAXONINTERPRETATION_ID, ");
		sb.append(" PLANTNAME_ID, PARTY_ID, PLANTCONCEPT_ID, ROLE_ID, originalinterpretation, currentinterpretation, interpretationdate ) ");
		sb.append(" values(?,?,?,?,?,?,?,?,?) ");

		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, taxonObservationId);
			pstmt.setInt(2, taxonInterpretationId);
			pstmt.setInt(3, nameId);
			pstmt.setInt(4, partyId);
			pstmt.setInt(5, conceptId);
			pstmt.setInt(6, roleId); 
			pstmt.setBoolean(7, true);
			pstmt.setBoolean(8, true);	
			pstmt.setString(9, "now()");					
			pstmt.execute();
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * @param string
	 * @return
	 */
	private int getRoleId(String string)
	{
		int result = 0;
		String sql = "select role_id from aux_role where rolecode = '"+  string + "'";
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			// Just get the first match --- TODO: Check for > 1 .. this is an error
			rs.next();
			result = rs.getInt(1);
		}
		catch (Exception e)
		{ 
			System.out.println("DBinsertPlotSource > Could not find roleId for '" + string + "'" );
			System.out.println("DBinsertPlotSource > error was: " + e.getMessage() );
		}
		return result;
	}


	/**
	 * method that returns false if the community cannot be stored in the 
	 * database, looks up the commconceptId based on either a code, or name
	 * 
	 * @return -- returns false if the code is not found in the community database
	 * @see -- getCommunityData -- this is the method that contacts the web servlet
	 *  to retrieve data from the community database
	 */
	private boolean insertCommunities() {
		
		StringBuffer sb = new StringBuffer();
		
		String name = Utility.escapeCharacters( source.getCommunityName(plotName) );
		String code = Utility.escapeCharacters( source.getCommunityCode(plotName) );
		String framework = Utility.escapeCharacters( source.getCommunityFramework(plotName) );
		String level = Utility.escapeCharacters( source.getCommunityLevel(plotName) );
		String classNotes = Utility.escapeCharacters( source.getClassNotes(plotName) );
			
		String classPublication = Utility.escapeCharacters( source.getCommunityPublication(plotName) );
		String classStartDate = Utility.escapeCharacters( source.getCommunityStartDate(plotName) );
		String classStopDate = Utility.escapeCharacters( source.getCommunityStopDate(plotName) );
		String classInspection = Utility.escapeCharacters( source.getCommunityInspection(plotName) );
		String classTableAnalysis = Utility.escapeCharacters( source.getCommunityTableAnalysis(plotName) );
		String classMultiVariateAnalysis = Utility.escapeCharacters( source.getCommunityMultiVariateAnalysis(plotName) );
		String classExpertSystem = Utility.escapeCharacters( source.getCommunityExpertSystem(plotName) );

		int commClassId = 0;
		
		try {							
			//String conceptId = "";
			// get the appropriate codes for this community via the web service
			// store the data in a hashtable
			AbstractList l = new Vector();
			if (code != null) {
				l = getCommunityData(code);
			} else if (name != null) {
				l = getCommunityData(name);
			}	
			
			debug.append("<communityName>" + name + "</communityName> \n");
			debug.append("<communityCode>" + code + "</communityCode> \n");
			debug.append("<communityLevel>" + level + "</communityLevel> \n");
			debug.append("<classNotes>" + classNotes + "</classNotes> \n");
			debug.append("<communityFramework>"+framework+"</communityFramework> \n");
//			debug.append("<communityConceptId>"+conceptId+"</communityConceptId> \n");
			
			debug.append("<communityStartDate>"+classStartDate+"</communityStartDate> \n");	
			debug.append("<communityStopDate>"+classStopDate+"</communityStopDate> \n");		
			debug.append("<communityInspection>"+classInspection+"</communityInspection> \n");
			debug.append("<communityTableAnalysis>"+classTableAnalysis+"</communityTableAnalysis> \n");
			debug.append("<communityMultiVariateAnalysis>"+classMultiVariateAnalysis+"</communityMultiVariateAnalysis> \n");
			debug.append("<communityExpertSystem>"+classExpertSystem+"</communityExpertSystem> \n");	
			debug.append("<communityPublication>"+classPublication+"</communityPublication> \n");	
			
			
			// FIXME: Also add the publication_id  but more complicated as it is a foreign key
			
			commClassId = getNextId("commclass");
					
			//insert the values
			sb.append(
				"INSERT into commclass (observation_id, commName, "
					+ " commCode, commFramework, commLevel, classNotes, "
					+ " inspection, tableanalysis, multivariateanalysis, expertsystem,"
					+ " classstartdate, classstopdate, commclass_id)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			// Bind the values to the query and execute it
			pstmt.setInt(1, plotObservationId);
			pstmt.setString(2, name);
			pstmt.setString(3, code);
			pstmt.setString(4, framework);
			pstmt.setString(5, level);
			pstmt.setString(6, classNotes);
			pstmt.setString(7, classInspection);
			pstmt.setString(8, classTableAnalysis);
			pstmt.setString(9, classMultiVariateAnalysis);
			pstmt.setString(10, classExpertSystem);
			Utility.insertDateField(classStartDate, pstmt, 11);
			Utility.insertDateField(classStopDate, pstmt, 12);
			pstmt.setInt(13, commClassId);
						
			
			//execute the p statement
			pstmt.execute();
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
			return (false);
		}
		
		// Also insert the community interpretation data if we can correllate
		
		//Need to get the conceptId
	    
		int commConceptId = RectificationUtility.getForiegnKey(conn, RectificationUtility.GETCOMMCONCEPTID, name );
		
		if (commConceptId == 0)
		{
			// Could not find a matching concept in the database
			debug.append(
				"<exceptionMessage>Could not find an existing community concept for '"+ name + "'</exceptionMessage>\n"
			);
		}
		else
		{
			StringBuffer statement = new StringBuffer();
			statement.append(
				"INSERT INTO comminterpretation ( commclass_id, commconcept_id )"
				+ " values (?,?)"
			);
			
			try
			{
				PreparedStatement insertCommInt = conn.prepareStatement(statement.toString());
				
				insertCommInt.setInt(1, commClassId);
				insertCommInt.setInt(2, commConceptId);
				
				insertCommInt.execute();
			}
			catch (SQLException e1)
			{
				System.out.println("Caught Exception: " + e1.getMessage());
				e1.printStackTrace();
				return( false);
			}
		}
			
		return (true);
	}
	
	private AbstractList getCommunityData(String searchName)
	{
		Vector result = new Vector();
		String codeClassSystem = "NVC-Code";
		String nameClassSystem = "NVC-Scientific";
		
		String sql = 
			"select commstatus.commlevel, commusage.commname from commstatus, commusage "
			+ "WHERE commusage.commname = '" + searchName + "'" 
			+ " and ( commusage.classsystem = '" + codeClassSystem + "' or  commusage.classsystem = '" + nameClassSystem + "')"
			+ "and commstatus.commconcept_id = commusage.commconcept_id";
		
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next())
			{
				// Only expect 2 rows back
				result.set(0, rs.getString(1));	 	// This should be the level
				result.add(rs.getString(2));				// This is one of the names, assuming first is code second is name 		
			}	
		}
		catch ( Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * method that returns false if the strataElements cannot be loaded to the
	 * database
	 *
	 * @return b -- either a success or failure boolean 
	 * @see this.insertStratumType -- this method calls this one
	 */
	private boolean insertStrata() 
	{
		//this.testSimpleQuery("Before inserting strata");
		StringBuffer sb = new StringBuffer();
		try 
		{
			//get the names of the recognized strata
			Vector strataTypes = source.uniqueStrataNames;
			for (int i = 0; i < strataTypes.size(); i++) {

				// CREATE A NEW STRING BUFFER FOR EACH STRATUM
				sb = new StringBuffer();
				//get the strataId number
				strataId = getNextId("stratum");
				String sName = strataTypes.elementAt(i).toString();
				String cover = Utility.escapeCharacters( source.getStrataCover(plotName, sName) );
				String base = Utility.escapeCharacters( source.getStrataBase(plotName, sName) );
				String height = Utility.escapeCharacters( source.getStrataHeight(plotName, sName) );
				String description = "";
				System.out.println("DBinsertPlotSource > inserting stratum type: " + sName);
				System.out.println("DBinsertPlotSource > stratum height: "
						+ height+ " base: "+ base);

				// Store the keys so we don't have to ask the database
				stratumIds.put(sName, new Integer(strataId) );
				
				// Get the stratumTypeId
				int stratumTypeId = getStratumTypeId(sName);

				//this.testSimpleQuery("After getting stratumTypeId:" + stratumTypeId);
				if (height != null && height.length() >= 1) 
				{
					//insert the strata values
					sb.append(
						"INSERT into STRATUM (stratum_id, observation_id, stratumName, "
							+ " stratumCover, stratumBase ,stratumHeight, STRATUMTYPE_ID, "
							+ " stratummethod_id)"
							+ " values(?,?,?,?,?,?,?,?)");

					PreparedStatement pstmt =
						conn.prepareStatement(sb.toString());
					// Bind the values to the query and execute it
					pstmt.setInt(1, strataId);
					pstmt.setInt(2, plotObservationId);
					pstmt.setString(3, sName);
					pstmt.setString(4, cover);
					pstmt.setString(5, base);
					pstmt.setString(6, height);
					pstmt.setInt(7, stratumTypeId);
					pstmt.setInt(8, this.stratumMethodId);
					//execute the prepared statement
					pstmt.execute();
				}
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
			//System.exit(0);
			return (false);
		}
		return (true);
	}

	/**
	 * @param sName
	 * @return
	 */
	private int getStratumTypeId(String sName)
	{
		int strataTypeId = 0;
		try {
			StringBuffer sb = new StringBuffer();

			sb.append(
				"SELECT STRATUMTYPE_ID from STRATUMTYPE where STRATUMNAME = '"
					+ sName+ "' AND stratummethod_id = " + this.stratumMethodId);

			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			int cnt = 0;
			while (rs.next()) {
				strataTypeId = rs.getInt(1);
				cnt++;
			}
			//send warnings
			if (cnt == 0) {
				System.out.println(
					"warning: There were no stratumtype matching: " + sName);
			}
		}
		catch (SQLException se) 
		{
			System.out.println("Caught SQL Exception: " + se.getMessage());
			if ( !se.getMessage().equals("No results were returned by the query.") )
			{
				se.printStackTrace();
			}
		}
		return strataTypeId;
	}

	/**
	 * method that inserts data to the stratum type table and returns the primary key value
	 * @return stratumTypeId -- the PK value associated with this insert
	 */
	private int insertStratumType(
		int stratumMethodId,
		String stratumName,
		String stratumIndex,
		String stratumDescription) {
		int stratumTypeId = 0;
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(
				"INSERT into STRATUMTYPE (STRATUMMETHOD_ID, STRATUMNAME, "
					+ " STRATUMINDEX, STRATUMDESCRIPTION) "
					+ " values(?,?,?,?)");
			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			// Bind the values to the query and execute it
			pstmt.setInt(1, stratumMethodId);
			pstmt.setString(2, stratumName);
			pstmt.setString(3, stratumIndex);
			pstmt.setString(4, stratumDescription);
			//execute the p statement
			pstmt.execute();

			// MAKE THIS ITS OWN METHOD
			sb = new StringBuffer();
			sb.append("SELECT STRATUMTYPE_ID from STRATUMTYPE where ");
			sb.append(
				" STRATUMNAME like '"
					+ stratumName
					+ "' and STRATUMINDEX like '");
			sb.append(
				stratumIndex
					+ "' and STRATUMDESCRIPTION like '"
					+ stratumDescription
					+ "'");
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			while (rs.next()) {
				stratumTypeId = rs.getInt(1);
			}
    }
    catch (SQLException se) 
    {
			System.out.println("Caught SQL Exception: " + se.getMessage());
      if ( !se.getMessage().equals("No results were returned by the query.") )
      {
			  System.out.println("sql: " + sb.toString());
        se.printStackTrace();
      }
		}
		return (stratumTypeId);
	}

	/**
	 * method that loads the observation table in the VegBank
	 * database and then returns a sting buffer which represents 
	 * an xml document with the attribute names and values that 
	 * were loaded to the database. The attributes loaded in this 
	 * method are accessed via the 'PlotDataSource' class and the 
	 * plugins that it uses.
	 * 
	 */
	private boolean insertPlotObservation() {
		StringBuffer sb = new StringBuffer();
		try {
			//get the plotid number
			plotObservationId = getNextId("observation");
			
			Vector obsContributors = source.getObservationContributors(plotName);
			for (int i = 0; i < obsContributors.size(); i++) 
			{			
				String wholeName = source.observationContributors.elementAt(i).toString();
				String salutation = Utility.escapeCharacters( source.getObservationContributorSalutation(wholeName) );
				String givenName = Utility.escapeCharacters( source.getObservationContributorGivenName(wholeName) );
				String surName = Utility.escapeCharacters( source.getObservationContributorSurName(wholeName) );
				String email = Utility.escapeCharacters( source.getObservationContributorEmailAddress(wholeName) );
				String role = "project manager";
				String organizationName = Utility.escapeCharacters( source.getObservationContributorOrganizationName(wholeName) );
				String contactInstructions = Utility.escapeCharacters( source.getObservationContributorContactInstructions(wholeName) );
				
				insertObservationContributor(
					salutation,
					givenName,
					surName,
					email,
					role,
					plotObservationId,
					organizationName,
					contactInstructions,
					wholeName
				);
			}
			
			// get the observation accession number
			String obsAccession =
				getObservationAccessionNumber(plotObservationId);
			System.out.println("############### " + obsAccession);

			String observationCode = Utility.escapeCharacters( source.getAuthorObsCode(plotName) );
			String startDate = Utility.escapeCharacters( source.getObsStartDate(plotName) );
			String stopDate = Utility.escapeCharacters( source.getObsStopDate(plotName) );
			String taxonObservationArea =
				Utility.escapeCharacters( source.getTaxonObservationArea(plotName) );
			boolean autoTaxonCover = source.getAutoTaxonCover(plotName);
			String coverDispersion = Utility.escapeCharacters( source.getCoverDispersion(plotName) );
			boolean permanence = source.isPlotPermanent(plotName);
			String stemObservationArea =
				Utility.escapeCharacters( source.getStemObservationArea(plotName) );

			// ADDED 
			String dateAccuracy = Utility.escapeCharacters( source.getObsDateAccuracy(plotName) );
			String hydrologicRegime = Utility.escapeCharacters( source.getHydrologicRegime(plotName) );
			String stemSampleMethod = Utility.escapeCharacters( source.getStemSampleMethod(plotName) );
			String originalData = Utility.escapeCharacters( source.getOriginalData(plotName) );
			String effortLevel = Utility.escapeCharacters( source.getEffortLevel(plotName) );
			String plotValidationLevel =
				Utility.escapeCharacters( source.getPlotValidationLevel(plotName) );
			String floristicQuality = Utility.escapeCharacters( source.getFloristicQuality(plotName) );
			String bryophyteQuality = Utility.escapeCharacters( source.getBryophyteQuality(plotName) );
			String lichenQuality = Utility.escapeCharacters( source.getLichenQuality(plotName) );
			String observationNarrative =
				Utility.escapeCharacters( source.getObservationNarrative(plotName) );
			String homogeneity = Utility.escapeCharacters( source.getHomogeneity(plotName) );
			String representativeness = Utility.escapeCharacters( source.getRepresentativeness(plotName) );
			String basalArea = Utility.escapeCharacters( source.getBasalArea(plotName) );
			String soilMoistureRegime = Utility.escapeCharacters( source.getSoilMoistureRegime(plotName) );
			String soilDrainage = Utility.escapeCharacters( source.getSoilDrainage(plotName) );
			String waterSalinity = Utility.escapeCharacters( source.getWaterSalinity(plotName) );
			String shoreDistance = Utility.escapeCharacters( source.getShoreDistance(plotName) );
			String soilDepth = Utility.escapeCharacters( source.getSoilDepth(plotName) );
			String organicDepth = Utility.escapeCharacters( source.getOrganicDepth(plotName) );
			String percentBedRock = Utility.escapeCharacters( source.getPercentBedRock(plotName) );
			String percentRockGravel = Utility.escapeCharacters( source.getPercentRockGravel(plotName) );
			String percentWood = Utility.escapeCharacters( source.getPercentWood(plotName) );
			String percentLitter = Utility.escapeCharacters( source.getPercentLitter(plotName) );
			String percentBareSoil = Utility.escapeCharacters( source.getPercentBareSoil(plotName) );
			String percentWater = Utility.escapeCharacters( source.getPercentWater(plotName) );
			String percentOther = Utility.escapeCharacters( source.getPercentOther(plotName) );
			String nameOther = Utility.escapeCharacters( source.getNameOther(plotName) );
			String standMaturity = Utility.escapeCharacters( source.getStandMaturity(plotName) );
			String successionalStatus = Utility.escapeCharacters( source.getSuccessionalStatus(plotName) );
			String treeHt = Utility.escapeCharacters( source.getTreeHt(plotName) );
			String shrubHt = Utility.escapeCharacters( source.getShrubHt(plotName) );
			String nonvascularHt = Utility.escapeCharacters( source.getNonvascularHt(plotName) );
			String floatingCover = Utility.escapeCharacters( source.getFloatingCover(plotName) );
      
			String stemSizeLimit = Utility.escapeCharacters( source.getStemSizeLimit(plotName) );
			String landscapeNarrative = Utility.escapeCharacters( source.getLandscapeNarrative(plotName) );
			String phenologicalAspect = Utility.escapeCharacters( source.getPhenologicalAspect(plotName) );
			String waterDepth = Utility.escapeCharacters( source.getWaterDepth(plotName) );
			String fieldHt = Utility.escapeCharacters( source.getFieldHt(plotName) );
			String submergedHt = Utility.escapeCharacters( source.getSubmergedHt(plotName) );
			String treeCover = Utility.escapeCharacters( source.getTreeCover(plotName) );
			String shrubCover = Utility.escapeCharacters( source.getShrubCover(plotName) );
			String fieldCover = Utility.escapeCharacters( source.getFieldCover(plotName) );
			String nonvascularCover = Utility.escapeCharacters( source.getNonvascularCover(plotName) );
			
      String submergedCover = Utility.escapeCharacters( source.getSubmergedCover(plotName) );
			String dominantStratum = Utility.escapeCharacters( source.getDominantStratum(plotName) );
			String growthform1Type = Utility.escapeCharacters( source.getGrowthform1Type(plotName) );
			String growthform2Type = Utility.escapeCharacters( source.getGrowthform2Type(plotName) );
			String growthform3Type = Utility.escapeCharacters( source.getGrowthform3Type(plotName) );
			String growthform1Cover = Utility.escapeCharacters( source.getGrowthform1Cover(plotName) );
			String growthform2Cover = Utility.escapeCharacters( source.getGrowthform2Cover(plotName) );
			String growthform3Cover = Utility.escapeCharacters( source.getGrowthform3Cover(plotName) );
			boolean notesPublic = source.getNotesPublic(plotName);
			boolean notesMgt = source.getNotesMgt(plotName);
			boolean revisions = source.getRevisions(plotName);
			String methodNarrative = Utility.escapeCharacters( source.getMethodNarrative(plotName) );

			// update the debugging stringbuffer
			debug.append("<plotId>" + plotId + "</plotId> \n");
			debug.append("<projectId>" + projectId + "</projectId> \n");
			debug.append("<observationCode>" + observationCode + "</observationCode>\n");
			debug.append("<obsStartDate>" + startDate + "</obsStartDate>\n");
			debug.append("<obsStopDate>" + stopDate + "</obsStopDate> \n");
			debug.append(
				"<accessionNumber>" + obsAccession + "</accessionNumber>\n");

			sb.append(
				"INSERT into OBSERVATION (observation_id, covermethod_id,  ");
			sb.append(" plot_Id, authorobscode, obsStartDate, obsEndDate, ");
			sb.append(
				" stratummethod_id, taxonObservationArea,  autoTaxonCover,");
			sb.append(" coverDispersion, STEMOBSERVATIONAREA, dateAccuracy, ");
			sb.append(
				" HYDROLOGICREGIME, stemSampleMethod, originalData, effortLevel, ");
			sb.append(
				" plotValidationLevel, floristicQuality, bryophyteQuality, ");
			sb.append(
				" lichenquality, observationNarrative, homogeneity, phenologicAspect, ");
			sb.append(
				" representativeness, basalArea, soilMoistureRegime, soilDrainage,");
			sb.append(
				" waterSalinity, shoreDistance, soilDepth, organicDepth, ");
			sb.append(
				" percentBedRock, percentRockGravel, percentWood, percentLitter, ");
			sb.append(
				" percentBareSoil, percentWater, percentOther, nameOther, ");
			sb.append(
				" standMaturity, successionalStatus, treeHt, shrubHt, nonvascularHt, ");
			sb.append(
				" floatingCover, submergedCover, dominantStratum, growthform1Type, ");
			sb.append(
				" growthform2Type, growthform3Type, growthform1Cover, growthform2Cover, ");
			sb.append(
				" growthform3Cover, notesPublic, notesMgt, revisions, methodnarrative, ");
			sb.append("stemsizelimit, landscapenarrative, ");
      sb.append("waterdepth, fieldht, submergedht, treecover, shrubcover, ");
			sb.append(" fieldcover, nonvascularCover, obsAccessionNumber, project_Id )");

			//68 total
			sb.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,");  //15
			sb.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"); //20
			sb.append(",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");  //19
      sb.append(",?,?,?,?,?,?,?,?,?,?,?,?,?,?)");         //14
			//68

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			// Bind the values to the query and execute it
			pstmt.setInt(1, plotObservationId);
			pstmt.setInt(2, coverMethodId);
			pstmt.setInt(3, plotId);
			pstmt.setString(4, observationCode);
			Utility.insertDateField(startDate, pstmt, 5);  
			Utility.insertDateField(startDate, pstmt, 6);		
			//pstmt.setString(5, startDate);
			//pstmt.setString(6, stopDate);
			pstmt.setInt(7, stratumMethodId);
			pstmt.setString(8, taxonObservationArea);
			pstmt.setBoolean(9, autoTaxonCover);
			pstmt.setString(10, coverDispersion);
			pstmt.setString(11, stemObservationArea);
			pstmt.setString(12, dateAccuracy);
			pstmt.setString(13, hydrologicRegime);

			pstmt.setString(14, stemSampleMethod);
			pstmt.setString(15, originalData);
			pstmt.setString(16, effortLevel);
			pstmt.setString(17, plotValidationLevel);
			pstmt.setString(18, floristicQuality);
			pstmt.setString(19, bryophyteQuality);
			pstmt.setString(20, lichenQuality);
			pstmt.setString(21, observationNarrative);
			pstmt.setString(22, homogeneity);
			pstmt.setString(23, phenologicalAspect);
			pstmt.setString(24, representativeness);
			pstmt.setString(25, basalArea); //12
			pstmt.setString(26, soilMoistureRegime);
			pstmt.setString(27, soilDrainage);
			pstmt.setString(28, waterSalinity);
			pstmt.setString(29, shoreDistance);
			pstmt.setString(30, soilDepth);
			pstmt.setString(31, organicDepth);
			pstmt.setString(32, percentBedRock);
			pstmt.setString(33, percentRockGravel);
			pstmt.setString(34, percentWood);
			pstmt.setString(35, percentLitter);
			pstmt.setString(36, percentBareSoil);
			pstmt.setString(37, percentWater); //24
			pstmt.setString(38, percentOther);
			pstmt.setString(39, nameOther);
			pstmt.setString(40, standMaturity);
			pstmt.setString(41, successionalStatus);
			pstmt.setString(42, treeHt);
			pstmt.setString(43, shrubHt);
			pstmt.setString(44, nonvascularHt);
			pstmt.setString(45, floatingCover);
			pstmt.setString(46, submergedCover);
			pstmt.setString(47, dominantStratum);
			pstmt.setString(48, growthform1Type);
			pstmt.setString(49, growthform2Type); //36
			pstmt.setString(50, growthform3Type);
			pstmt.setString(51, growthform1Cover);
			pstmt.setString(52, growthform2Cover);
			pstmt.setString(53, growthform3Cover);
			pstmt.setBoolean(54, notesPublic);
			pstmt.setBoolean(55, notesMgt);
			pstmt.setBoolean(56, revisions);
			pstmt.setString(57, methodNarrative);
			pstmt.setString(58, stemSizeLimit);
			pstmt.setString(59, landscapeNarrative);
			pstmt.setString(60, waterDepth);
			pstmt.setString(61, fieldHt);
			pstmt.setString(62, submergedHt);
			pstmt.setString(63, treeCover);
			pstmt.setString(64, shrubCover);
			pstmt.setString(65, fieldCover);
			pstmt.setString(66, nonvascularCover);
			pstmt.setString(67, obsAccession);
			pstmt.setInt(68, projectId);
			
			pstmt.execute();
			Thread.sleep(2000);
			pstmt.close();
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
			debug.append(
				"<exceptionMessage>"
					+ e.getMessage()
					+ "</exceptionMessage>\n");

			return (false);
		}
		return (true);
	}



	/**
	 * method to insert the static plot data like names and locations
	 * and if it catches an exception then false is returned to the calling 
	 * class so that a roll-back can be issued
	 *
	 * @param projectId -- the projectId
	 * @return boolean -- true if successfull insert
	 *
	 */
	private boolean insertStaticPlotData(int projectId) {
		StringBuffer sb = new StringBuffer();
		try {
			//get the plotid number
			plotId = getNextId("plot");

			//the variables from the plot file

			String authorPlotCode = Utility.escapeCharacters( source.getPlotCode(plotName) );
			String rockType = Utility.escapeCharacters( source.getRockType(plotName) );
			String surficialDeposits = Utility.escapeCharacters( source.getSurficialDeposits(plotName) ); 	
			String plotArea = Utility.escapeCharacters( source.getPlotArea(plotName) );
			String elevation = Utility.escapeCharacters( source.getElevation(plotName) );
			String datumType = Utility.escapeCharacters( source.getDatumType(plotName) );
      String elevationAccuracy = Utility.escapeCharacters( source.getElevationAccuracy(plotName) );
			String slopeAspect = Utility.escapeCharacters( source.getSlopeAspect(plotName) );
			String slopeGradient = Utility.escapeCharacters( source.getSlopeGradient(plotName) );
			String topoPosition = Utility.escapeCharacters( source.getTopoPosition(plotName) );
			String hydrologicRegime = Utility.escapeCharacters( source.getHydrologicRegime(plotName) );
			String plotShape = Utility.escapeCharacters( source.getPlotShape(plotName) );
			String confidentialityStatus = Utility.escapeCharacters( source.getConfidentialityStatus(plotName) );
			String confidentialityReason = Utility.escapeCharacters( source.getConfidentialityReason(plotName) );
			String azimuth = Utility.escapeCharacters( source.getAzimuth(plotName) );
			String dsgPoly = Utility.escapeCharacters( source.getDSGPoly(plotName) );
			String xCoord = Utility.escapeCharacters( source.getXCoord(plotName) );
			String yCoord = Utility.escapeCharacters( source.getYCoord(plotName) );
			String zone = Utility.escapeCharacters( source.getUTMZone(plotName) );
			// if the plot data source has geocoordinates (latitude, logitude )
			// use them otherwise lookup the information from the web service
			String latitude = Utility.escapeCharacters( source.getLatitude(plotName) );
			String longitude = Utility.escapeCharacters( source.getLongitude(plotName) );
			if (latitude == null || latitude.length() < 2) 
			{
				Hashtable geoCoords = getGeoCoords(xCoord, yCoord, zone);
				latitude = (String) geoCoords.get("latitude");
				longitude = (String) geoCoords.get("longitude");
			}

			String state = Utility.escapeCharacters( source.getState(plotName) );
			String country = Utility.escapeCharacters( source.getCountry(plotName) );
			String authorLocation = Utility.escapeCharacters( source.getAuthorLocation(plotName) );
			boolean permanence = source.isPlotPermanent(plotName);
     	String layoutNarrative = Utility.escapeCharacters( source.getLayoutNarrative(plotName) );
     	String locationNarrative = Utility.escapeCharacters( source.getLocationNarrative(plotName) );
			String landForm = Utility.escapeCharacters( source.getLandForm(plotName) );
     	String standSize = Utility.escapeCharacters( source.getStandSize(plotName) );

			//make a temporary accession number for each unique plot
			String accessionNumber =
				getAccessionNumber(plotName, plotId, this.submitterEmail);
			System.out.println(
				"DBinsertPlotSource > accession number: " + accessionNumber);

			//print the variables to the screen for debugging
      		// Does this deal with all variables? need a method of class for this
      		// stuff
			debug.append(
				"<authorPlotCode>" + authorPlotCode + "</authorPlotCode>\n");
			debug.append("<rockType>" + rockType + "</rockType>\n");
			debug.append("<plotArea>" + plotArea + "</plotArea>\n");
			debug.append("<elevation>" + elevation + "</elevation>\n");
			debug.append(
        		"<elevationAccuracy>" + elevationAccuracy + "</elevationAccuracy>\n");
			debug.append("<slopeAspect>" + slopeAspect + "</slopeAspect>\n");
			debug.append("<authorDatum>"+datumType+"</authorDatum>\n");			
			debug.append(
				"<slopeGradient>" + slopeGradient + "</slopeGradient>\n");
			debug.append("<topoPosition>" + topoPosition + "</topoPosition>\n");
			debug.append(
				"<hydrologicRegime>" + hydrologicRegime + "</hydrologicRegime>\n");
			debug.append("<plotShape>" + plotShape + "</plotShape>\n");
			debug.append("<xCoord>" + xCoord + "</xCoord>\n");
			debug.append("<yCoord>" + yCoord + "</yCoord>\n");
			debug.append("<latitude>" + latitude + "</latitude>\n");
			debug.append("<longitude>" + longitude + "</longitude>\n");
			debug.append("<zone>" + zone + "</zone>\n");
			debug.append("<country>" + country + "</country>\n");
			debug.append("<state>" + state + "</state>\n");
			debug.append(
				"<authorLocation>" + authorLocation + "</authorLocation>\n");
			debug.append("<permanence>" + permanence + "</permanence>\n");
			debug.append("<standsize>" + standSize + "</standsize>\n");
			debug.append("<surficialDeposits>" + surficialDeposits + "</surficialDeposits>\n");			
			
      		//this is the postgresql date function
			String sysdate = "now()";

      		// The mother of all inserts ;)
			sb.append(
				"INSERT into PLOT (authorPlotCode, plot_id, "
					+ "rocktype, latitude, longitude, area, elevation,"
					+ "slopeAspect, slopeGradient, topoPosition, "
					+ "shape, confidentialityStatus, confidentialityReason, "
					+ "authore, authorn, state, country, authorLocation, accession_number, "
					+ "dateEntered, submitter_surname, submitter_givenname, submitter_email, "
					+ "authorzone, permanence, landform, layoutnarative, locationnarrative, "
          + "azimuth, elevationaccuracy, dsgpoly, standsize, authordatum, surficialDeposits) "
					+
          "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());

			// Bind the values to the query and execute it
			pstmt.setString(1, authorPlotCode);
			pstmt.setInt(2, plotId);
			pstmt.setString(3, rockType);	
			Utility.insertDoubleField(latitude, pstmt, 4);
			//pstmt.setString(4, latitude);
			Utility.insertDoubleField(longitude, pstmt, 5);
			//pstmt.setString(5, longitude);
			Utility.insertDoubleField(plotArea, pstmt, 6);
			//pstmt.setString(6, plotArea);
			Utility.insertDoubleField(elevation, pstmt, 7);
			//pstmt.setString(7, elevation);
			pstmt.setString(8, slopeAspect);
			pstmt.setString(9, slopeGradient);
			pstmt.setString(10, topoPosition);
			pstmt.setString(11, plotShape);
			pstmt.setString(12, confidentialityStatus);
			pstmt.setString(13, confidentialityReason);
			pstmt.setString(14, xCoord);
			pstmt.setString(15, yCoord);
			pstmt.setString(16, state);
			pstmt.setString(17, country);
			pstmt.setString(18, authorLocation);
			pstmt.setString(19, accessionNumber);
			pstmt.setString(20, sysdate);
			pstmt.setString(21, this.submitterSurName);
			pstmt.setString(22, this.submitterGivenName);
			pstmt.setString(23, this.submitterEmail);
			pstmt.setString(24, zone);
			pstmt.setBoolean(25, permanence);
			pstmt.setString(26, landForm);
			pstmt.setString(27, layoutNarrative);
			pstmt.setString(28, locationNarrative);
			Utility.insertDoubleField(azimuth, pstmt, 29);
			//pstmt.setString(29, azimuth);
			Utility.insertDoubleField(elevationAccuracy, pstmt, 30);
			//pstmt.setString(30, elevationAccuracy);
			pstmt.setString(31, dsgPoly);
			pstmt.setString(32, standSize);
			pstmt.setString(33, datumType);
			pstmt.setString(34, surficialDeposits);
						     
			pstmt.getWarnings();
			pstmt.execute();
			pstmt.close();
		} catch (SQLException sqle) {
			System.out.println("Caught SQL Exception: " + sqle.getMessage());
			sqle.printStackTrace();
			return (false);
			//System.exit(0);
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.getMessage());
			e.printStackTrace();
			return (false);
			//System.exit(0);
		}
		return (true);
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
	 * @see getObservationAccessionNumber
	 */
	private String getAccessionNumber(
		String plotName,
		int plotId,
		String email) 
	{
		StringBuffer sb = new StringBuffer();
		try 
    {
			String date = Utility.escapeCharacters( source.getObsStartDate(plotName) );

			// THE PLOTNAME HEAR REFERS TO THE UNIQUE PLOT IDENTIFIER FOR A
			// GIVEN CLASS SO THE CODE FOR THAT CLASS MUST BE LOOKED UP AND
			// USED 

			String plotCode = Utility.escapeCharacters( source.getPlotCode(plotName) );

			StringTokenizer tok = new StringTokenizer(date);
			String buf = tok.nextToken().replace('-', ' ');
			StringTokenizer tok2 = new StringTokenizer(buf);
			StringBuffer sb2 = new StringBuffer();
			while (tok2.hasMoreTokens()) {
				sb2.append(tok2.nextToken());
			}
			String startDate = sb2.toString();

			sb.append("VB");
			sb.append(".");
			sb.append(plotCode.replace('.', '_'));
			sb.append(".");
			sb.append(plotId);
			sb.append(".");
			sb.append(email);
			sb.append(".");
			sb.append(startDate);
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (sb.toString());
	}

	/**
	 * this is the second accession number that refers to a unique observation.
	 * This was added Aug. 2002, so based on a request by RPK.  This should 
	 * ultimately replace the accession number in the plot table, but for now 
	 * it is being added with the hope that nothing breaks
	 *
	 * @param observationId -- the PK of the observation in the system
	 * @return accession number like: VB-obsId
	 * @see getAccessionNumber
	 */
	private String getObservationAccessionNumber(int observationId) 
  {
    // This is of overkill for just this but it may change
		return ( "VB." + observationId);
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
	private Hashtable getGeoCoords(String xCoord, String yCoord, String zone) {
		System.out.println("DBinsertPlotSource > requesting coordinates -- x,y: "+xCoord+","+yCoord+" host: " + this.geoCoordRequestServletHost);
		Hashtable h = new Hashtable();
		try {
			//THIS USES THE REQUESTURL METHOD
			String protocol = "http://";
			String host = this.geoCoordRequestServletHost;
			String s = null;
			//THIS USES THE OTHER REQUEST URL METHOD
			String servlet = "/vegbank/servlet/framework";
			Properties parameters = new Properties();
			parameters.setProperty("action", "coordinateTransform");
			parameters.setProperty("returnformattype", "xml");
			parameters.setProperty("x", xCoord);
			parameters.setProperty("y", yCoord);
			parameters.setProperty("zone", zone);
			s = GetURL.requestURL(servlet, protocol, host, parameters);

			StringTokenizer tok = new StringTokenizer(s);
			String latitude = tok.nextToken();
			String longitude = tok.nextToken();

			System.out.println("DBinsertPlotSource > lat : " + latitude);
			System.out.println("DBinsertPlotSource > long : " + longitude);

			h.put("latitude", latitude);
			h.put("longitude", longitude);
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (h);
	}

	/**
	 * utility method to use a webservice to lookup the community 
	 * related attributes from the vegbank community archive
	 * 
	 * @param code -- the community code used to lookup the concept id
	 * @return hastable -- a hashtable containing the community data
	 */
//	private Hashtable getCommunityData(String code) {
//		Hashtable h = new Hashtable();
//		try {
//			//THIS USES THE REQUESTURL METHOD
//			String protocol = "http://";
//			String host = this.communityRequestServletHost;
//			String s = null;
//			//THIS USES THE OTHER REQUEST URL METHOD
//			String servlet = "/vegbank/servlet/DataRequestServlet";
//			Properties parameters = new Properties();
//			parameters.setProperty("requestDataType", "vegCommunity");
//			parameters.setProperty("requestDataFormatType", "xml");
//			parameters.setProperty("clientType", "clientApplication");
//			parameters.setProperty("communityName", code);
//			parameters.setProperty("communityLevel", "%");
//			s = GetURL.requestURL(servlet, protocol, host, parameters);
//			System.out.println(
//				"DBinsertPlotSource > XML string from web app: \n'" + s + "'");
//
//			if (s.length() > 3 && s != null) {
//				// parse the returned xml doc to get the relevant data and 
//				// put it into the hash table
//				parser = new XMLparse();
//				Document doc = parser.getDocumentFromString(s);
//				Vector levelVec =
//					parser.getValuesForPath(
//						doc,
//						"/vegCommunity/community/classLevel");
//				Vector codeVec =
//					parser.getValuesForPath(
//						doc,
//						"/vegCommunity/community/classCode");
//				Vector conceptIdVec =
//					parser.getValuesForPath(
//						doc,
//						"/vegCommunity/community/commConceptId");
//				// THIS IS A HACK TO GET AT THE COMMUNITY NAME
//				Vector nameVec =
//					parser.getValuesForPath(
//						doc,
//						"/vegCommunity/community/commDesc");
//				String lev = (String) levelVec.elementAt(0);
//				String c = (String) codeVec.elementAt(0);
//				String conceptId = (String) conceptIdVec.elementAt(0);
//				String name = (String) nameVec.elementAt(0);
//				System.out.println(
//					"DBinsertPlotSource > parsed community xml: "
//						+ lev
//						+ " "
//						+ c
//						+ " "
//						+ conceptId);
//
//				if (lev != null && c != null && conceptId != null) {
//					h.put("code", c);
//					h.put("level", lev);
//					h.put("conceptId", conceptId);
//					h.put("name", name);
//				} else {
//					System.out.println(
//						"DBinsertPlotSource > parsed elements returned null: "
//							+ lev
//							+ " "
//							+ c
//							+ " "
//							+ conceptId);
//				}
//			} else {
//				System.out.println(
//					"DBinsertPlotSource > no results from web app");
//			}
//		} catch (Exception e) {
//			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
//			e.printStackTrace();
//		}
//		return (h);
//	}

	/**
		 * utility method to use a webservice to lookup the plant 
		 * related attributes from the vegbank plant taxonomy archive.  
		 * The input into this method may be either a plant taxon code
		 * like 'ABIES' or even a plant name like 'Abies bifolia'
		 * 
		 * @param name -- the plant taxonomy code used to lookup the related data
		 * 	that will be returned to from the web service in an xml format
		 * @param nameType -- the type of name that the name paremeter is
		 * these values may only be: CODE, COMMON NAME, SCIENTIFIC NAME or ANY
		 * @return h -- a hastable with the elements for the plant that matches in the 
		 * 	vegbank database system.
		 */
	private Hashtable getPlantTaxonomyData(String name, String nameType) {
		System.out.print("DBinsertPlotSource > getting plant info  -- type: " + nameType);
		System.out.println(" name: " + name + " host:" + plantRequestServletHost);
		Hashtable h = new Hashtable();
		try {
			// TEST THAT THE NAME TYPES AR VALID
			if (nameType.equals("CODE")
				|| nameType.equals("COMMON NAME")
				|| nameType.equals("SCIENTIFIC NAME")
				|| nameType.equals("ANY")) {
				// SET THE NAMETYPE TO A WILDCARD IF IT IS 'ANY'
				if (nameType.equals("ANY")) {
					nameType = "%";
				}
				//THIS USES THE REQUESTURL METHOD
				String protocol = "http://";
				String host = this.plantRequestServletHost;
				String s = null;
				//THIS USES THE OTHER REQUEST URL METHOD
				String servlet = "/vegbank/servlet/DataRequestServlet";
				Properties parameters = new Properties();
				parameters.setProperty("requestDataType", "plantTaxon");
				parameters.setProperty("requestDataFormatType", "xml");
				parameters.setProperty("clientType", "clientApplication");
				parameters.setProperty("taxonName", name);
				parameters.setProperty("taxonNameType", nameType);
				parameters.setProperty("taxonLevel", "%");
				s = GetURL.requestURL(servlet, protocol, host, parameters);
				System.out.println("DBinsertPlotSource > plant data XML string from web app: \n'"+s+"'");

				// IF THERE ARE RESULTS THEN ADD THEM TO THE HASHTABLE
				if (s.length() > 3 && s != null) {
					// parse the returned xml doc to get the relevant data and 
					// put it into the hash table
					parser = new XMLparse();
					Document doc = parser.getDocumentFromString(s);
					Vector levelVec =
						parser.getValuesForPath(
							doc,
							"/plantTaxa/taxon/name/plantLevel");
					Vector conceptIdVec =
						parser.getValuesForPath(
							doc,
							"/plantTaxa/taxon/name/plantConceptId");
					Vector nameVec =
						parser.getValuesForPath(
							doc,
							"/plantTaxa/taxon/name/plantDescription");
					Vector nameIdVec =
						parser.getValuesForPath(
							doc,
							"/plantTaxa/taxon/name/plantNameId");
					String lev = (String) levelVec.elementAt(0);
					String conceptId = (String) conceptIdVec.elementAt(0);
					String dbName = (String) nameVec.elementAt(0);
					String nameId = (String) nameIdVec.elementAt(0);
					System.out.println(
						"DBinsertPlotSource > parsed plant taxa xml: "
							+ lev
							+ " "
							+ conceptId);
					if (lev != null && conceptId != null) {
						h.put("level", lev);
						h.put("conceptId", conceptId);
						h.put("name", dbName);
						h.put("nameId", nameId);
					} else {
						System.out.println(
							"DBinsertPlotSource > parsed elements returned null: "
								+ lev
								+ "  "
								+ conceptId);
					}
				} else {
					System.out.println(
						"DBinsertPlotSource > no results from web app");
				}
			} else {
				System.out.println(
					"DBinsertPlotSource > invalid plant name type: "
						+ nameType);
			}
		} catch (Exception e) {
			System.out.println("DBinsertPlotSource > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (h);
	}

	/**
	 * Thin wrapper around setting a date field in a PreparedStatement to handle
	 * adding nulls when needed, e.g. empty string.
	 * 
	 * @param date
	 * @param psmnt
	 * @param i
	 * @throws SQLException
	 *
	private void insertDateField ( String date, PreparedStatement psmnt, int i)
		throws SQLException
	{
		int sqlDateType = java.sql.Types.DATE;
		if ( date == "" )
		{
			psmnt.setNull(i, sqlDateType);
		}
		else
		{
			// this maybe should be setDate
			psmnt.setString(i, date);
		}
	}
	**/

	/**
	 * method that returns true if the project with this name exists in the 
	 * database
	 */ 
	private boolean projectExists(String projectName) 
  {
		int rows = 0;
		try 
    {
			StringBuffer sb = new StringBuffer();
			sb.append(
				"SELECT count(*) from PROJECT where projectName = '"+projectName+"'");
			//System.out.println("DBinsertPlotSource > query: " + sb.toString());
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			while (rs.next()) 
      {
				rows = rs.getInt(1);
			}
    }
    catch ( SQLException se ) 
    {      
	    System.out.println("Caught SQL Exception: " + se.getMessage());
      if ( !se.getMessage().equals("No results were returned by the query.") )
      {
			  se.printStackTrace();
      }
		}
		if (rows == 0) 
    {
			System.out.println("DBinsertPlotSource > project does not exist");
			return (false);
		} 
    else 
    {
			System.out.println("DBinsertPlotSource > project does exist");
			return (true);
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
		StringBuffer sb = new StringBuffer();
		try 
    {
			sb.append(
				"SELECT project_id from PROJECT where projectName = '"+projectName+"'"
      );
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			while (rs.next()) 
      {
				projectId = rs.getInt(1);
			}
    } 
    catch (SQLException se) 
    {
			System.out.println("Caught SQL Exception: " + se.getMessage());
      if ( !se.getMessage().equals("No results were returned by the query.") )
      {
			  System.out.println("sql: " + sb.toString());
        se.printStackTrace();
      }
		}
		return (projectId);
	}


   private int getNextId(String tableName) {
           int rows = -1;
           StringBuffer sb = new StringBuffer();
           try {
                   if (tableName.toUpperCase().equals("TAXONOBSERVATION")) {
                           //sb.append(
                           //        "SELECT max(taxonobservation_id)+1 from taxonobservation");
											sb.append("SELECT nextval('taxonobservation_id_seq')");
                   } else if (tableName.toUpperCase().equals("STRATUMCOMPOSITION")) {
                          // sb.append(
                           //        "SELECT max(stratumcomposition_id)+1 from stratumcomposition");
										sb.append("SELECT nextval('STRATUMCOMPOSITION_id_seq')");
                   } else if (tableName.toUpperCase().equals("PLOT")) {
                          // sb.append("SELECT max(plot_id)+1 from plot");
										sb.append("SELECT nextval('PLOT_id_seq')");
                   } else if (tableName.toUpperCase().equals("STRATUM")) {
                          //sb.append("SELECT max(stratum_id)+1 from stratum");
										sb.append("SELECT nextval('STRATUM_id_seq')");
                   } else if (tableName.toUpperCase().equals("PROJECT")) {
                          // sb.append("SELECT max(project_id)+1 from project");
										sb.append("SELECT nextval('PROJECT_id_seq')");
                   } else if (tableName.toUpperCase().equals("OBSERVATION")) {
                           //sb.append("SELECT max(observation_id)+1 from observation");
										sb.append("SELECT nextval('OBSERVATION_id_seq')");
                   } else {
                          // sb.append("SELECT count(*)+1 from " + tableName);
										sb.append("SELECT nextval('" + tableName + "_id_seq')");
                   }
                   Statement query = conn.createStatement();
                   ResultSet rs = query.executeQuery(sb.toString());
                   while (rs.next()) {
                           rows = rs.getInt(1);
                   }
     }
 		catch (SQLException se)
 		{
     	System.out.println("Caught SQL Exception: " + se.getMessage());
   		if ( !se.getMessage().equals("No results were returned by the query.") )
   		{
      	System.out.println("sql: " + sb.toString());
       	se.printStackTrace();
     	}
   	}
	 return (rows);
	}

	/**
	 * method that will insert the project data into the project table.
   * The validator should insure that the date string is ok for insertion 
   * into the database!!
   *
	 * @param projectName -- the name of the project
	 * @param projectDescription -- the description of the project
	 * @param projectStartDate -- the start date of the project
	 * @param projectStopDate -- the stop date of the project
	 */
	public int insertProject(
		String projectName,
		String projectDescription,
		String projectStartDate,
		String projectStopDate) 
  {
		StringBuffer sb = new StringBuffer();
		try 
    {
			projectId = getNextId("project");
			
				sb.append(
					"INSERT into PROJECT (project_id, projectName, projectdescription,  ");
				sb.append(" startdate, stopdate) ");
			  sb.append(" values (?,?,?,?,?)");

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, projectId);
			pstmt.setString(2, projectName);
			pstmt.setString(3, projectDescription);
			Utility.insertDateField(projectStartDate, pstmt, 4);
			Utility.insertDateField(projectStopDate, pstmt, 5);			

      pstmt.execute();
      pstmt.close();

      
			System.out.println("DBinsertPlotSource > inserted PROJECT");
		} 
    catch (Exception e) 
    {
			System.out.println("Caught Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
		}
		return projectId;
	}
	
	
  /**
  * Handle SQLExceceptions thrown by JDBC layer. 
  * In particular the "No results found" error" does not trigger a
  * printStacktrace while all other errors do.
  **/
  private void handleSQLException (SQLException se) {
		System.out.println("Caught SQL Exception: " + se.getMessage());
    if ( !se.getMessage().equals("No results were returned by the query.") )
    {
      se.printStackTrace();
    }   
  }

  /**
  * Get a string that can put into a database date field form a string input
  **/
  private java.util.Date parseDateString (String dateToParse)
  {
    java.util.Date parsedDate;
    try
    {
      DateFormat df = DateFormat.getDateInstance();
      parsedDate = df.parse(dateToParse);
    }
    catch (ParseException pe)
    {
      System.out.println("DBinsertPlotSource > Exeception parsing a date: " 
                          + dateToParse + " " + pe.getMessage() );
      pe.printStackTrace();
      
      return null;
    }
    return parsedDate;
  }
}
