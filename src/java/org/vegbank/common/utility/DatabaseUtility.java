/**
 *  '$RCSfile: DatabaseUtility.java,v $'
 *  Purpose: A utility class for the VegBank database access module
 *  Copyright: 2002 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-10-05 02:12:24 $'
 *	'$Revision: 1.12 $'
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

import org.apache.struts.util.LabelValueBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class contains multi-purpose utilities for dealing with plot data, 
 * and the VegBank database at this point these may include utilities to 
 * convert various data types, access database properties files and drop
 * plots from the database.
 * 
 */

public class DatabaseUtility 
{
	private static Log log = LogFactory.getLog(DatabaseUtility.class);

	public Vector outVector;
	public int vecElementCnt;
	public String outArray[]=new String[100000];
	public int outArrayNum;

	public String outString[] =new String[100000];
	public int outStringNum;
	
	private static PropertyResourceBundle options = null;
	private static String propertiesFile = "database";

	private ResourceBundle defaultPropFile = ResourceBundle.getBundle("database");
	private Connection conn = null;

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
			//log.debug("number of lines transformed: "+lineCnt);
			outStringNum=stringContentsNum;	
		} //end try
		catch( Exception e ) 
		{
			log.error(" failed in: DatabaseUtility "+e.getMessage());
		}
	}//end method


	/**
	* method to tokenize elements from a string object
	* @param pipeString -- string to tokenize
	* @param tokenPosition -- the location of the desired token
	*/
	public String positionStringTokenizer(String pipeString, int tokenPosition)
	{
		//log.debug("%%%%% "+pipeString+" "+tokenPosition);
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
				//log.debug(s);	
				localVector.addElement(s);
				vecElementCnt++;
			}
			outVector=localVector;
		}
		catch (Exception e) 
		{
			log.error("failed in DatabaseUtility" + 
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
					 //log.debug("first value: "+inputArray[i]);
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
			 log.error(" failed in: DatabaseUtility.getUniqueArray: "+e.getMessage());
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
	private Connection getDBConnection(String host)
	{
		Connection c = null;
		try 
 		{
			String dbConnectString = defaultPropFile.getString("connectString");
			String driverClass = defaultPropFile.getString("driverClass");
			String user = defaultPropFile.getString("user");
			String password = defaultPropFile.getString("password");
			
			Class.forName(driverClass);
			log.error("CommunityQueryStore > db connect string: " + dbConnectString);
			c = DriverManager.getConnection(dbConnectString, user, password);			

		}
		catch ( Exception e )
		{
			log.error("DatabaseUtility > Exception: " + e.getMessage());
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
	
		this.conn = this.getDBConnection("localhost");
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

		this.conn = this.getDBConnection(dbHost);
		this.conn.setAutoCommit(false);

		dropSinglePlot(plotId);

		this.conn.commit();
		this.conn.close();
	}

	/**
	 *
	 */
	private void dropSinglePlot(String plotId) throws java.sql.SQLException {

		log.error("DatabaseUtility.dropSinglePlot > ID=" + plotId);
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
			log.error("DatabaseUtility.dropSinglePlot > no plot");
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
		String stmtMulti11 = "DELETE FROM ? WHERE ? IN ( SELECT ? from ? where ? IN " +
			"( SELECT ? from ? where ? IN " +
			"( SELECT ? from ? where observation_id IN (?) ) ) )";
		
		if (obsIds != null && !obsIds.equals("")) {
			log.debug("DatabaseUtility.dropSinglePlot > got obs IDs: " + obsIds);
			
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
			executeStatement(values, stmtMulti8);

			// STEMLOCATION
			values = new ArrayList(11);
			values.add("stemlocation");
			values.add("stemcount_id");  
			values.add("stemcount_id");  
			values.add("stemcount");
			values.add("taxonimportance_id");
			values.add("taxonimportance_id");
			values.add("taxonimportance");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			executeStatement(values, stmtMulti11);
			
			// STEMCOUNT
			values = new ArrayList(8);
			values.add("stemcount");
			values.add("taxonimportance_id");
			values.add("taxonimportance_id");
			values.add("taxonimportance");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			executeStatement(values, stmtMulti8);

			// TAXONINTERPRETATION
			values = new ArrayList(5);
			values.add("taxoninterpretation");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			executeStatement(values, stmtMultiO);

			// TAXONIMPORTANCE
			values = new ArrayList(5);
			values.add("taxonimportance");
			values.add("taxonobservation_id");  
			values.add("taxonobservation_id");  
			values.add("taxonobservation");  
			values.add(obsIds);
			executeStatement(values, stmtMultiO);

			// STRATUM
			values = new ArrayList(3);
			values.add("stratum"); 
			values.add("observation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);

			// TAXONOBSERVATION
			values = new ArrayList(3);
			values.add("taxonobservation"); 
			values.add("observation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);

			// CLASSCONTRIBUTOR
			values = new ArrayList(5);
			values.add("classcontributor"); 
			values.add("commclass_id"); 
			values.add("commclass_id"); 
			values.add("commclass"); 
			values.add(obsIds);
			executeStatement(values, stmtMultiO);

			// COMMINTERPRETATION
			values = new ArrayList(5);
			values.add("comminterpretation");
			values.add("commclass_id"); 
			values.add("commclass_id"); 
			values.add("commclass"); 
			values.add(obsIds);
			executeStatement(values, stmtMultiO);

			// COMMCLASS
			values = new ArrayList(3);
			values.add("commclass"); 
			values.add("observation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);

			// DISTURBANCEOBS
			values = new ArrayList(3);
			values.add("disturbanceobs"); 
			values.add("observation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);

			// SOILOBS
			values = new ArrayList(3);
			values.add("soilobs"); 
			values.add("observation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);

			// OBSERVATIONSYNONYM
			values = new ArrayList(3);
			values.add("observationsynonym"); 
			values.add("primaryobservation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);

			// OBSERVATIONSYNONYM
			values = new ArrayList(3);
			values.add("observationsynonym"); 
			values.add("synonymobservation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);

			// OBSERVATIONCONTRIBUTOR
			values = new ArrayList(3);
			values.add("observationcontributor"); 
			values.add("observation_id"); 
			values.add(obsIds);
			executeStatement(values, stmtSingle);
		}

		// PLACE
		values = new ArrayList(3);
		values.add("place"); 
		values.add("plot_id"); 
		values.add(plotId);
		executeStatement(values, stmtSingle);

		// OBSERVATION
		values = new ArrayList(3);
		values.add("observation"); 
		values.add("plot_id"); 
		values.add(plotId);
		executeStatement(values, stmtSingle);

		// PLOT
		values = new ArrayList(3);
		values.add("plot"); 
		values.add("plot_id"); 
		values.add(plotId);
		executeStatement(values, stmtSingle);

		log.debug("DatabaseUtility.dropSinglePlot: DONE");
	}

	private void executeStatement(List values, String sql) throws SQLException
	{
		//log.debug( "DatabaseUtility.dropSinglePlot > SQL:\n" + constructQuery(sql, values) );
		conn.createStatement().executeUpdate( constructQuery(sql, values) );
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
			log.error("DatabaseUtility.constructQuery: bad syntax, " 
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
			 this.conn = this.getDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 log.debug("DatabaseUtility >>> dropping user profile for: " + email );
			 
			 long usrId = getUserId(email);
			 
			 
			 sb.append("delete from usr where usr_id = '"+usrId+"';");
			 sb.append("delete from usercertification where usr_id = '"+usrId+"';");
			 sb.append("delete from usernotify where usr_id = '"+usrId+"';");
			 sb.append("delete from userdataset where usr_id = '"+usrId+"';");
			 sb.append("delete from userpermission where usr_id = '"+usrId+"';");
			 sb.append("delete from userpreference where usr_id = '"+usrId+"';");
			 sb.append("delete from userquery where usr_id = '"+usrId+"';");
			 sb.append("delete from userrecordowner where usr_id = '"+usrId+"';");
			 
			 //log.debug(sb.toString());
			 
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			log.error("DatabaseUtility > Exception: " + e.getMessage());
			e.printStackTrace();
		 }
	 }
	 
	 private long getUserId(String email) throws SQLException
	{
		// Get the usr_id
		 long usrId = 0;
		 PreparedStatement getUserId = conn.prepareStatement("select usr_id from usr where email_address = '" + email + "'");
		 ResultSet rs = getUserId.executeQuery();
		 while ( rs.next() )
		 {
			usrId = rs.getLong(1);
		 }
		return usrId;
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
			 this.conn = this.getDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 log.debug("DatabaseUtility > updating user: " + email +" to permission type: " + level );
			 sb.append("update  usr set permission_type = '"+level+"' where email_address like '"+email+"';");
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			log.error("DatabaseUtility > Exception: " + e.getMessage());
			e.printStackTrace();
		 }
	 }
	
		/**
		 * This fills a vector with Party name and id stored in a LabelValues bean
		 * for the results of a query.
		 * 
		 * @param partyIdNames
		 * @param partyQuery
		 */
		public static void getPartyValueLabelBeans(Vector partyIdNames, StringBuffer partyQuery)
		{
			try
			{
				DatabaseAccess da = new DatabaseAccess();
		
				// hit the DB, plot
				ResultSet rs = da.issueSelect(partyQuery.toString());		
		
				while (rs.next())
				{		
					String partyId  = rs.getString(1);
					String salutation = rs.getString(2);
					String givenName = rs.getString(3);
					String middleName = rs.getString(4);
					String surName = rs.getString(5);
					String organizationName = rs.getString(6);
					
					StringBuffer partyName = new StringBuffer();
					
					if ( Utility.isStringNullOrEmpty(surName) )
					{
						partyName.append(organizationName);
					}
					else
					{
						// Construct the party name 
						// e.g. 'Dr. James William Smith (NCEAS)'							
						if ( Utility.isStringNullOrEmpty(salutation) )
						{
							partyName.append(salutation + " ");
						}
						if ( Utility.isStringNullOrEmpty(givenName) )
						{
							partyName.append(givenName + " ");
						}
						if ( Utility.isStringNullOrEmpty(middleName) )
						{
							partyName.append(middleName + " ");
						}
						if ( Utility.isStringNullOrEmpty(surName) )
						{
							partyName.append(surName + " ");
						}
						if ( Utility.isStringNullOrEmpty(organizationName) )
						{
							partyName.append("(" + organizationName + ")");
						}
					}
					
					LabelValueBean partyNameId = new LabelValueBean(partyName.toString(), partyId);
					partyIdNames.add(partyNameId);
				}
			}
			catch (SQLException e1)
			{
				LogUtility.log("org.vegbank.ui.struts.CommQueryForm:: " +
					"findPartyNameIdsInDB() ERROR: " + e1.getMessage(), e1);
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
			 this.conn = this.getDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 log.debug("DatabaseUtility > updating user's : " + email +" password to  : " + password );
			 sb.append("update usr set password = '"+password+"' where email_address like '"+email+"';");
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			log.error("DatabaseUtility > Exception: " + e.getMessage());
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
					log.error(sex.getMessage());
					sex.printStackTrace();
				}
			}
			else if ( action.equals("dropuser") )
			{
				String email = args[1];
				String host = args[2];
				log.debug("Dropuser > " + email + " on db " + host);
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
				log.debug("unknown action: " + action);
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


	/**
	 * Generate the sql for max, min date range comparisons 
	 * 
	 * @param maxDate
	 * @param minDate
	 * @param endDateFieldName
	 * @param endDateFieldName
	 * @param allowNulls
	 * @param conjunctionToUse
	 * @return String -- SQL fragment
	 */
	public static String handleMaxMinNullDateRange(
		String minDate,
		String maxDate,
		String startDateFieldName,
		String endDateFieldName,
		boolean allowNulls,
		String conjunctionToUse)
	{
		StringBuffer sb = new StringBuffer(256);
	
		if (Utility.isStringNullOrEmpty(maxDate) && Utility.isStringNullOrEmpty(minDate))
		{
			// Both empty nothing to be done
		}
		else
		{
	
			sb.append(conjunctionToUse);
			sb.append(" ( ");
	
			if (!Utility.isStringNullOrEmpty(minDate))
			{
				sb.append(" ( ").append(startDateFieldName).append(" >= '").append(makeSQLSafe(minDate)).append("' ");
				if ( allowNulls)
				{
					sb.append("  OR ").append(startDateFieldName).append(" IS NULL ");
				}
				sb.append(" ) ");
			}
	
			if (!Utility.isStringNullOrEmpty(maxDate))
			{
				sb.append(" AND ( ").append(endDateFieldName).append(" <= '").append(makeSQLSafe(maxDate)).append("' ");
				if ( allowNulls)
				{
					sb.append("  OR ").append(startDateFieldName).append(" IS NULL ");
				}
				sb.append(" ) ");
			}
			
			sb.append(" ) ");
		}
		return sb.toString();
	}


	/**
	 * Generate the sql for max, min comparisons 
	 * 
	 * @param max
	 * @param min
	 * @param nullsAllowed
	 * @param fieldName
	 * @return
	 */
	public static String handleMaxMinNull(
		String max,
		String min,
		boolean nullsAllowed,
		String fieldName,
		String conjunctionToUse)
	{
		StringBuffer sb = new StringBuffer(256);
	
		if (Utility.isStringNullOrEmpty(max) && Utility.isStringNullOrEmpty(min))
		{
			// Both empty nothing to be done
		}
		else
		{
	
			sb.append(conjunctionToUse);
	
			if (nullsAllowed)
			{
				sb.append(" ( ");
			}
	
			sb.append(" ( ");
			if (!Utility.isStringNullOrEmpty(max))
			{
				sb.append(" ").append(fieldName).append(" <= ").append(makeSQLSafe(max)).append(" ");
	
				if (!Utility.isStringNullOrEmpty(min))
				{
					sb.append(" AND ");
				}
			}
	
			if (!Utility.isStringNullOrEmpty(min))
			{
				sb.append(" ").append(fieldName).append(" >= ").append(makeSQLSafe(min)).append(" ");
			}
			sb.append(" ) ");
	
			if (nullsAllowed)
			{
				sb.append("  OR (").append(fieldName).append(" IS NULL )  )");
			}
		}
		return sb.toString();
	}


	/**
	 * Is the ANYVALUE present or is this a list of nulls
	 * which is functionally the same.
	 * 
	 * @param values
	 * @return
	 */
	public static boolean isAnyValueAllowed(String[] values, String ANYVALUE)
	{
		boolean allNulls = true;
	
		for (int i = 0; i < values.length; i++)
		{
			if ( values[i] != null )
			{
				allNulls = false;
				// Is ANYVALUE present
				if ( values[i].equals(ANYVALUE))
				{
					return true;
				}
			}
		}
		return allNulls;
	}


	/**
	 * Generated SQL for option boxs where many options can be selected
	 * 
	 * Special handling for values ANY, IS NULL and IS NOT NULL 
	 * Needs to handle a list of nulls 
	 * 
	 * @param values
	 * @param fieldName
	 * @return
	 */
	public static String handleValueList(String[] values, String fieldName, String conjunction)
	{
		StringBuffer sb = new StringBuffer(1024);
		String ANYVALUE = "ANY";
	
		// Are there any constraints
		if (values != null && values.length != 0 && !isAnyValueAllowed(values, ANYVALUE) )
		{
			sb.append(conjunction)
				.append(" ( ").append(fieldName).append(" = '").append(makeSQLSafe(values[0])).append("'");
	
			for (int i = 1; i < values.length; i++)
			{
				if ( values[i] == null)
				{
					// Do nothing
				}
				else if (values[i].trim().equals("IS NOT NULL")
					|| values[i].trim().equals("IS NULL"))
				{
					sb.append(" OR ").append(fieldName).append(" ").append(values[i]).append(" ");
				}
				else
				{
					sb.append(" OR ").append(fieldName).append(" ='").append(makeSQLSafe(values[i])).append("'");
				}
			}
			sb.append(" ) ");
		}
		return sb.toString();
	}
	

	/**
	 * Duplicates apostophes, removes semicolons.
	 */
	public static String makeSQLSafe(String unsafe) {
		if (unsafe == null) {
			return null;
		}

		log.debug("making SQL safe: " + unsafe);
		if (unsafe.indexOf(";") != -1) {
			log.debug(";;;;;;;;;;;;;;;;;;;;;; removing ;");
			unsafe = unsafe.replaceAll(";", "");
		}

		if (unsafe.indexOf("'") != -1) {
			log.debug("fixing single quotes");
			unsafe = unsafe.replaceAll("\'", "\'\'");
		}
		return unsafe;
	}

	/**
	 * Singlates apostrophes.
	 */
	public static String makeSQLUnsafe(String safe) {
		if (safe == null) {
			return null;
		}

		return safe.replaceAll("\'\'", "\'");
	}

}


