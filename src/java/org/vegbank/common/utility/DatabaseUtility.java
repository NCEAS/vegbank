/**
 *  '$RCSfile: DatabaseUtility.java,v $'
 *  Purpose: A utility class for the VegBank database access module
 *  Copyright: 2002 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-10-20 22:59:34 $'
 *	'$Revision: 1.6 $'
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
package org.vegbank.common.utility;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
/**
 * This class contains multi-purpose utilities for dealing with plot data, 
 * and the VegBank database at this point these may include utilities to 
 * convert various data types, access database properties files and drop
 * plots from the database.
 * 
 */

public class DatabaseUtility 
{

	public Vector outVector;
	public int vecElementCnt;
	public String driverClass;
	public String connectionString;
	public String login;
	public String passwd;
	public String logFile;
	public int minConnections;
	public int maxConnections;
	public String maxConnectionTime;
	public int maxConnectionUses;

	public String outArray[]=new String[100000];
	public int outArrayNum;

	public String outString[] =new String[100000];
	public int outStringNum;
	
	private static PropertyResourceBundle options = null;
	private static String propertiesFile = "database";

	private ResourceBundle defaultPropFile = ResourceBundle.getBundle("database");
	private Connection conn = null;
	
	
	/**
	 * due to problems connecting to the various vegbank databases and vegbank 
	 * url-based services at runtime  'test' connection methods have been 
	 * implemented to connect to all these sources upon startup of the system.
	 * This method will make connections to:
	 * 1] the vegbank plots database
	 * 2] the vegbank plants database
	 * 3] the vegbank community database
	 * 4] the vegbank community data-retrieval servlet
	 * 5] the vegbank plant data-retriveal servlet
	 * 6] the vegbank user-authentication servlet
	 * 7] the vegbank coordinate transform servlet
	 * This method will print to stdout the results of each of these connections 
	 * and will return true if all attempts are successful
	 */
	 public boolean testVegBankConnections()
	 {
		 System.out.println("DatabaseUtility > testing the VegBank network connections");
		 boolean result = true;
		 
		 try
		 {
			 // fisrt test the database connections
			 Class.forName("org.postgresql.Driver");
			 DriverManager.setLoginTimeout(2);
			 int timeout = DriverManager.getLoginTimeout();
			 System.out.println("DatabaseUtility > database connection timeout: " + timeout);
			 String plotConnectString = defaultPropFile.getString("connectString");
			 System.out.println("DatabaseUtility > plotConnectString: " + plotConnectString);
			 String plantConnectString = defaultPropFile.getString("plantdbconnectstring");
			 System.out.println("DatabaseUtility > plantConnectString: " + plantConnectString);
			 String communityConnectString = defaultPropFile.getString("communitydbconnectstring");
			 System.out.println("DatabaseUtility > communityConnectString: " + communityConnectString);
			 System.out.println("DatabaseUtility > testing the plots database connection");
			 
			 try { Connection testC = DriverManager.getConnection(plotConnectString, "datauser", ""); }
			 catch(Exception e) { e.printStackTrace(); result = false;}
			 System.out.println("DatabaseUtility > testing the plants database connection");
			 try { Connection testC = DriverManager.getConnection(plantConnectString, "datauser", ""); }
			 catch(Exception e) { e.printStackTrace(); result = false;}
			 System.out.println("DatabaseUtility > testing the community database connection");
			 try{Connection testC = DriverManager.getConnection(communityConnectString, "datauser", ""); }
			 catch(Exception e) { e.printStackTrace(); result = false;}
			 
			 // next test the servlet connections
			 // create a vector to store the urls for testing
			 Vector testUrls = new Vector();
			 String authenticationServletHost = defaultPropFile.getString("authenticationServletHost");
			 testUrls.addElement("http://"+authenticationServletHost+"/vegbank/servlet/usermanagement");
			 
			 String plantRequestServletHost = defaultPropFile.getString("plantRequestServletHost");
			 testUrls.addElement("http://"+plantRequestServletHost+"/vegbank/servlet/DataRequestServlet");
			 String communityRequestServletHost = defaultPropFile.getString("communityRequestServletHost");
			 testUrls.addElement("http://"+communityRequestServletHost+"/vegbank/servlet/DataRequestServlet");
			 String geoCoordRequestServletHost = defaultPropFile.getString("geoCoordRequestServletHost");
			 testUrls.addElement("http://"+geoCoordRequestServletHost+"/vegbank/servlet/framework");
			 
			 for (int ii = 0; ii < testUrls.size(); ii++) 
			 {
				 String url = (String)testUrls.elementAt(ii);
				 System.out.println("DatabaseUtility > testing url: " + url);
				 URL u;
				 URLConnection uc;
				 u = new URL(url);
				 uc = u.openConnection();
				 String header = uc.getHeaderField("date");
				 System.out.println("DatabaseUtility date: > " + header);
				 HttpURLConnection hcon = (HttpURLConnection) u.openConnection();
				 int resCode = 0;
				 String resMessage = null;
				 try
				 {
					 resCode = hcon.getResponseCode();
					 resMessage = hcon.getResponseMessage();
				 }
				 catch(Exception e) { e.printStackTrace(); result = false;}
				 System.out.println("DatabaseUtility http status code: > " + resCode);
				 System.out.println("DatabaseUtility http status message : > " + resMessage);
			 }
			 
	
			 
			 
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
			 result = false;
		 }
		 
		 return (result);
	 }


	 /**
	 * Method to read a database properties file and return the salient attributes 
	 * to be passed to the connection pooling class   including:
	 *
	 * dbDriver:        JDBC driver. e.g. 'oracle.jdbc.driver.OracleDriver'<br>
	 * dbServer:        JDBC connect string. e.g. 'jdbc:oracle:thin:@203.92.21.109:1526:orcl'<br>
	 * dbLogin:         Database login name.  e.g. 'Scott'<br>
	 * dbPassword:      Database password.    e.g. 'Tiger'<br>
	 * minConns:        Minimum number of connections to start with.<br>
	 * maxConns:        Maximum number of connections in dynamic pool.<br>
	 * logFileString:   Absolute path name for log file. e.g. 'c:/temp/mylog.log' <br>
	 * maxConnTime:     Time in days between connection resets. (Reset does a basic cleanup)<br>
	 *
	 *  @param	propFile  the database properties file
	 *  @param	accessType - can include insert or query - min/max conns will vary.
	 *  @exception   MissingResourceException
	 */
	 public void getDatabaseParameters(String propFile, String accessType)
	 {
		 String s = null;
		 try 
		 {
			 ResourceBundle rb = ResourceBundle.getBundle(propFile);
			 driverClass=rb.getString("driverClass");
			 connectionString=rb.getString("connectString");
			 login=rb.getString("user");
			 passwd=rb.getString("password");
			 logFile=rb.getString("logFile");
			 maxConnectionUses = (new Double(rb.getString("maxConnectionUses"))).intValue();
			 
			 if (accessType.equals("query")) 
			 {	
				minConnections = (new Double(rb.getString("query.minConnections"))).intValue();
				maxConnections = (new Double(rb.getString("query.maxConnections"))).intValue();
			 } //end if
			 
			 if (accessType.equals("insert")) 
			 {
				minConnections = (new Double(rb.getString("insert.minConnections"))).intValue();
				maxConnections = (new Double(rb.getString("insert.maxConnections"))).intValue();
			 } //end if
		 }
		 catch (MissingResourceException e) 
		 {
			 System.out.println("DatabaseUtility > failed in getDatabaseParameters " +e.getMessage());
       e.printStackTrace();
		 }
	}



	/**
	*  this method will take, as input a StringWriter object and return a String Array 
	* and the number of vertical elements in the array structure
	*/
	public void convertStringWriter  (StringWriter inputStringWriter)
	{
		try 
		{
			/*convert the stringwriter to a string*/
			String transformedString=null;  // a string inwhich to convert the String Writer to
			PrintWriter pw = new PrintWriter(inputStringWriter);
			transformedString  = inputStringWriter.toString().trim();  //do the conversion to the string

			int stringContentsNum = 0;  // number of vertical elements in the array
			String stringContents[] =new String[100000];  //the array to contain the contents of the string
			BufferedReader br = new BufferedReader(new StringReader (transformedString)); //speed up the string parsing with a buffered reader

			//read each line
			String line; // temporary string to contain the lines from the transformedData 
			int lineCnt=0; //running line counter
			while ((line = br.readLine()) !=null ) 
			{
				stringContents[lineCnt]=line.trim();  //write the lines to the local array
				outString[lineCnt]=line.trim();  //write the lines directly to the public array
				lineCnt++;  //increment the line
			} //end while

			stringContentsNum=lineCnt;  //number of elements in the array
			//System.out.println("number of lines transformed: "+lineCnt);
			outStringNum=stringContentsNum;	
		} //end try
		catch( Exception e ) 
		{
			System.out.println(" failed in: DatabaseUtility "+e.getMessage());
		}
	}//end method


	/**
	* method to tokenize elements from a string object
	* @param pipeString -- string to tokenize
	* @param tokenPosition -- the location of the desired token
	*/
	public String positionStringTokenizer(String pipeString, int tokenPosition)
	{
		//System.out.println("%%%%% "+pipeString+" "+tokenPosition);
		String token="nullToken";
		if (pipeString != null)
		{
			StringTokenizer t = new StringTokenizer(pipeString.trim(), " \t");
			int i=1;
			while (i<=tokenPosition)
			{
				if ( t.hasMoreTokens() )
				{
					token=t.nextToken();
					i++;
				}
				else
				{
					token="nullToken";
					i++;
				}
			}
			return(token);
		}
		else
		{
			return(token);
		}
	}


	/**
	*  Method that takes as input a name of a file and writes the file contents 
	*  to a vector and then makes the vector and number of vector elements access
	*  to the public 
	*
	* @param fileName name of the file that whose contents should be written to a vector
	*/
	public void fileVectorizer (String fileName) 
	{
		try 
		{
			vecElementCnt=0;
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			Vector localVector = new Vector();
			String s;
			while((s = in.readLine()) != null) 
			{
				//System.out.println(s);	
				localVector.addElement(s);
				vecElementCnt++;
			}
			outVector=localVector;
		}
		catch (Exception e) 
		{
			System.out.println("failed in DatabaseUtility" + 
			e.getMessage());
		}
	}
	
	/**
	 * This method will take an array as input and check to see if there are any
	 * redundant levels in the array and if there are redundancies will remove them
	 * returning to the calling class a unique array
	 */
	 public void getUniqueArray (String[] inputArray, int inputArrayNum)
	 {
		 try 
		 { 
			 int uniqueSwitch=0;  //switch is one if redundant otherwise unique is zero
			 String uniqueArray[]=new String[100000];
			 String inputArrayValue=null;
			 String uniqueArrayValue=null;
			 int uniqueArrayNum=0;

			 for (int i=0; i<=inputArrayNum; i++) 
			 { 
				 //load the first value into the unique array
				 if (i==0) 
				 {
					 uniqueArray[uniqueArrayNum]=inputArray[i];
					 uniqueArrayNum++;
					 //System.out.println("first value: "+inputArray[i]);
				 }
	
				//check for redundancies
				if (i>0) 
				{
					//make sure that the array value is not null then grab the value
					if (inputArray[i] != null) 
					{
						inputArrayValue=inputArray[i].trim();
					}
					uniqueSwitch=0;  //make swich 0 for new level
					for (int ii=0; ii<uniqueArrayNum; ii++) 
					{  //pass thru all uniq vals
						uniqueArrayValue=uniqueArray[ii].trim();
						if (uniqueArrayValue.equals(inputArrayValue)) 
						{
							uniqueSwitch=1;
							break;
						}//end if
					}//end for
					if (uniqueSwitch==0) 
					{
						uniqueArray[uniqueArrayNum]=inputArrayValue;
						uniqueArrayNum++;
					}
				}//end if
			 } //end for 

			 //pass to calling class
			 outArray=uniqueArray;
			 outArrayNum=uniqueArrayNum;
		 } //end try
		 catch( Exception e ) 
		 {
			 System.out.println(" failed in: DatabaseUtility.getUniqueArray: "+e.getMessage());
		 	e.printStackTrace();
		 }
	 } //end method
	 
	


	/**
	* method that will return a database connection for use with the 
	* vegetation plot's database on the machine described in the input 
	* parameter.
	*
	* @param host -- the host machine on which the plots database is running
	* @return conn -- an active connection
	*/
	private Connection getPlotDBConnection(String host)
	{
		Connection c = null;
		try 
 		{
			String dbConnectString = defaultPropFile.getString("connectString");
			String driverClass = defaultPropFile.getString("driverClass");
			String user = defaultPropFile.getString("user");
			String password = defaultPropFile.getString("password");
			
			Class.forName(driverClass);
			System.out.println("CommunityQueryStore > db connect string: " + dbConnectString);
			c = DriverManager.getConnection(dbConnectString, user, password);			

		}
		catch ( Exception e )
		{
			System.out.println("DatabaseUtility > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	
	/**
	 * Drop multiple plots.
	 * @param plotIds -- the PK values of the plots that should be deleted
	 */
	public void dropPlots( String[] plotIds ) throws java.sql.SQLException {

		if (plotIds ==  null)
			return;
	
		this.conn = this.getPlotDBConnection("localhost");
		this.conn.setAutoCommit(false);

		for (int i=0; i < plotIds.length; i++) {
			if (plotIds[i].equals("")) {
				continue;
			}
			dropSinglePlot(plotIds[i]);
			this.conn.commit();
		}

		this.conn.close();
	}

	/**
	 * method for droping a plot from the vegbank database.  The data that
	 * are deleted from the database are really the minimal that are required 
	 * for removal of the plot from the plot table -- thus there are some data
	 * left in the database that do not have foreign key relationships with 
	 * the plot table or any of the children tables.
	 * @param plotId -- the PK value of the plot that should be dropped
	 * @param dbHost -- the host machine onwhich the database is running
	 */
	public void dropPlot( String plotId ) throws java.sql.SQLException {
		dropPlot(plotId, "localhost");
	}
	public void dropPlot( String plotId, String dbHost )
				throws java.sql.SQLException {

		this.conn = this.getPlotDBConnection(dbHost);
		this.conn.setAutoCommit(false);

		dropSinglePlot(plotId);

		this.conn.commit();
		this.conn.close();
	}

	/**
	 *
	 */
	private void dropSinglePlot(String plotId) throws java.sql.SQLException {

		System.out.println("DatabaseUtility.dropSinglePlot > ID=" + plotId);
		String obsIds;
		boolean results = true;
		StringBuffer sb = new StringBuffer(256)
			.append("select count(plot_id) from plot where plot_id = ")
			.append(plotId);
		Statement query = conn.createStatement();
		ResultSet dbresults = query.executeQuery( sb.toString() );

		// check for extant plot
		int res = 0; 
		while (dbresults.next()) {
			res = dbresults.getInt(1);
		}

		if ( res == 0 ) {
			System.out.println("DatabaseUtility.dropSinglePlot > no plot");
			return; 
		}

		// get all obs. IDs for this plot; use them later 
		obsIds = getPlotObservationIds(plotId);

		// build and execute SQL statements
		List values;
		String stmtSingle = "DELETE FROM ? WHERE ? IN (?)";
		String stmtMultiP = "DELETE FROM ? WHERE ? IN ( SELECT ? from ? where plot_id = ? )";
		String stmtMultiO = "DELETE FROM ? WHERE ? IN ( SELECT ? from ? where observation_id IN (?) )";
		String stmtMulti8 = "DELETE FROM ? WHERE ? IN ( SELECT ? from ? where ? IN " +
			"( SELECT ? from ? where observation_id IN (?) ) )";

		if (obsIds != null && !obsIds.equals("")) {
			System.out.println("DatabaseUtility.dropSinglePlot > got obs IDs: " + obsIds);
			
			// TAXONALT
			values = new ArrayList(8);
			values.add("taxonalt");
			values.add("taxoninterpretation_id");  
			values.add("taxoninterpretation_id");  
			values.add("taxoninterpretation");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtMulti8, values) );

			// STEMLOCATION
			values = new ArrayList(8);
			values.add("stemlocation");
			values.add("stemcount_id");  
			values.add("stemcount_id");  
			values.add("stemcount");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtMulti8, values) );

			// STEMCOUNT
			values = new ArrayList(5);
			values.add("stemcount");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtMultiO, values) );

			// TAXONINTERPRETATION
			values = new ArrayList(5);
			values.add("taxoninterpretation");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtMultiO, values) );

			// STRATUMCOMPOSITION
			values = new ArrayList(5);
			values.add("stratumcomposition");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtMultiO, values) );

			// STRATUM
			values = new ArrayList(3);
			values.add("stratum"); 
			values.add("observation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

			// TAXONOBSERVATION
			values = new ArrayList(3);
			values.add("taxonobservation"); 
			values.add("observation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

			// CLASSCONTRIBUTOR
			values = new ArrayList(5);
			values.add("classcontributor"); 
			values.add("commclass_id"); 
			values.add("commclass_id"); 
			values.add("commclass"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtMultiO, values) );

			// COMMINTERPRETATION
			values = new ArrayList(5);
			values.add("comminterpretation");
			values.add("commclass_id"); 
			values.add("commclass_id"); 
			values.add("commclass"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtMultiO, values) );

			// COMMCLASS
			values = new ArrayList(3);
			values.add("commclass"); 
			values.add("observation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

			// DISTURBANCEOBS
			values = new ArrayList(3);
			values.add("disturbanceobs"); 
			values.add("observation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

			// SOILOBS
			values = new ArrayList(3);
			values.add("soilobs"); 
			values.add("observation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

			// OBSERVATIONSYNONYM
			values = new ArrayList(3);
			values.add("observationsynonym"); 
			values.add("primaryobservation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

			// OBSERVATIONSYNONYM
			values = new ArrayList(3);
			values.add("observationsynonym"); 
			values.add("synonymobservation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

			// OBSERVATIONCONTRIBUTOR
			values = new ArrayList(3);
			values.add("observationcontributor"); 
			values.add("observation_id"); 
			values.add(obsIds);
			conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );
		}

		// PLACE
		values = new ArrayList(3);
		values.add("place"); 
		values.add("plot_id"); 
		values.add(plotId);
		conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

		// PLOTSPECIESSUM
		values = new ArrayList(3);
		values.add("plotspeciessum"); 
		values.add("plot_id"); 
		values.add(plotId);
		conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

		// PLOTSITESUMMARY
		values = new ArrayList(3);
		values.add("plotsitesummary"); 
		values.add("plot_id"); 
		values.add(plotId);
		conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

		// OBSERVATION
		values = new ArrayList(3);
		values.add("observation"); 
		values.add("plot_id"); 
		values.add(plotId);
		conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

		// PLOT
		values = new ArrayList(3);
		values.add("plot"); 
		values.add("plot_id"); 
		values.add(plotId);
		conn.createStatement().executeUpdate( constructQuery(stmtSingle, values) );

		System.out.println("DatabaseUtility.dropSinglePlot: DONE");
	}

	/**
	 *
	 */ 
	private String constructQuery(String base, List values) {
		String tmp = base;
		try {
			Iterator vit = values.iterator();
			while (vit.hasNext()) {
				tmp = tmp.replaceFirst("\\?", (String)vit.next());
			}

		} catch (java.util.regex.PatternSyntaxException pse) {
			System.err.println("DatabaseUtility.constructQuery: bad syntax, " 
					+ pse.getMessage());
		}
		return tmp;
	}

	/**
	 *  @param plotId -- plot_id to get observation IDs on
	 *  @return Commas separated String of observation_id values in the form
	 * 	"1,2,3,4,5" which can easily be appended to SQL statements.
	 */
	private String getPlotObservationIds(String plotId) 
				throws SQLException {

		boolean first = true;
		StringBuffer plotIds = new StringBuffer(128);
		StringBuffer sb = new StringBuffer(256)
			.append("select observation_id from observation where plot_id=")
			.append(plotId);

		ResultSet dbresults = conn.createStatement().executeQuery( sb.toString() );
		while (dbresults.next()) {
			if (first) {
				first = false;
			} else { 
				plotIds.append(",");
			}

			plotIds.append(dbresults.getString(1));
		}

		return plotIds.toString();
	}
	
	
	/**
	* method that will return a database connection for use with the 
	* vegbank user database on the machine described in the input 
	* parameter.
	*
	* @param host -- the host machine on which the plots database is running
	* @return conn -- an active connection
	*/
	private Connection getUserDBConnection(String host)
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://"+host+"/framework", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("DatabaseUtility > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	
	
	/**
	 * method to drop a user from the vegbank user database 
	 * @param email -- the emailaddress of the user that should be dropped 
	 * @param host -- the host machine that the user database is running on
	 * 
	 */
	 public void dropUser(String email, String dbHost)
	 {
		 StringBuffer sb;
		 boolean results = true;
		 PreparedStatement pstmt = null;
		 try
		 {
			 sb = new StringBuffer(256);
			 this.conn = this.getUserDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 System.out.println("DatabaseUtility > dropping user profile for: " + email );
			 sb.append("delete from user_info where email_address like '"+email+"';");
			 sb.append("delete from user_certification where email_address like '"+email+"'");
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			System.out.println("DatabaseUtility > Exception: " + e.getMessage());
			e.printStackTrace();
		 }
	 }
	 
	 /**
	 * method to update a users permission type  
	 * @param email -- the emailaddress of the user that should be dropped 
	 * @param host -- the host machine that the user database is running on
	 * @param level -- the level 
	 *
	 * 1 -- registered user no upload of any data 
	 * 2 -- certified user upload plots, annotations, interpretations, plots, communities, plants
	 * 3 -- professional user
	 * 4 -- senior user
	 * 5 -- manager -- root level access
	 * 
	 */
	 public void updateUserPermission(String email, String dbHost, String level)
	 {
		 StringBuffer sb;
		 boolean results = true;
		 PreparedStatement pstmt = null;
		 try
		 {
			 sb = new StringBuffer();
			 this.conn = this.getUserDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 System.out.println("DatabaseUtility > updating user: " + email +" to permission type: " + level );
			 sb.append("update  user_info set permission_type = '"+level+"' where email_address like '"+email+"';");
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			System.out.println("DatabaseUtility > Exception: " + e.getMessage());
			e.printStackTrace();
		 }
	 }
	
	 /**
	 * method to update a users password  
	 * @param email -- the emailaddress of the user that should be dropped 
	 * @param host -- the host machine that the user database is running on
	 * @param password -- the new password  
	 * 
	 */
	 public void changeUserPassword(String email, String dbHost, String password)
	 {
		 StringBuffer sb;
		 boolean results = true;
		 PreparedStatement pstmt = null;
		 try
		 {
			 sb = new StringBuffer();
			 this.conn = this.getUserDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 System.out.println("DatabaseUtility > updating user's : " + email +" password to  : " + password );
			 sb.append("update user_info set password = '"+password+"' where email_address like '"+email+"';");
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			System.out.println("DatabaseUtility > Exception: " + e.getMessage());
			e.printStackTrace();
		 }
	 }
	
	
	
	/**
	 * main method for running the methods internal to 
	 * this class 
	 */
	public static void main(String[] args)
	{
		if (args.length > 1)
		{
			//get the plugin named
			String action = args[0];
			DatabaseUtility dplot = new DatabaseUtility();
			if ( action.equals("dropplot") )
			{
				String inPlot = args[1];
				String host = args[2];
				//int plotid = Integer.parseInt(inPlot);
				try {
					dplot.dropPlot(inPlot, host);
				} catch (java.sql.SQLException sex) {
					System.out.println(sex.getMessage());
					sex.printStackTrace();
				}
			}
			else if ( action.equals("dropuser") )
			{
				String email = args[1];
				String host = args[2];
				dplot.dropUser(email, host);
			}
			else if ( action.equals("updatepermission") )
			{
				String email = args[1];
				String host = args[2];
				String level = args[3];
				dplot.updateUserPermission(email, host, level);
			}
			else if ( action.equals("changepassword") )
			{
				String email = args[1];
				String host = args[2];
				String passwd = args[3];
				dplot.changeUserPassword(email, host, passwd);
			}
			else
			{
				System.out.println("unknown action: " + action);
			}
		}
		else if (args.length == 1) 
		{
			DatabaseUtility du = new DatabaseUtility();
			ArrayList values = new ArrayList(5);
			values.add("stratumcomposition");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add("5");
			String stmtMultiO = "DELETE FROM ? WHERE ? IN ( SELECT ? from ? where observation_id IN ? )";
			du.constructQuery(stmtMultiO, values);
		}
		else
		{
			System.out.println("usage > action parameters ");
			System.out.println("actions include: ");
			System.out.println("1] dropplot plotId host ");
			System.out.println("2] dropuser email host ");
			System.out.println("3] updatepermission email host level ");
			System.out.println(" levels: {1=registered 2=certified 3=professional 4=senior 5=manager}");
			System.out.println("3] changepassword email host newpassword ");
		}
	}
	
	/**
	 * Utility method to get an option value from the properties file
	 *
	 * @param optionName the name of the option requested
	 */
	public static String getOption(String optionName) {
			// Get the configuration file information
			if (options == null) {
				options = (PropertyResourceBundle)
					PropertyResourceBundle.getBundle(propertiesFile);
			}
			String value = (String)options.handleGetObject(optionName);
			return value;
	}
	

}


