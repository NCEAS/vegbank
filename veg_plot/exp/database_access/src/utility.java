/**
 *  '$RCSfile: utility.java,v $'
 *    Purpose: A utility class for the VegBank database access module
 *  Copyright: 2002 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Jarris
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-08-30 18:25:17 $'
 * '$Revision: 1.3 $'
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
package databaseAccess;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.sql.*;
import databaseAccess.*;

/**
 * This class contains multi-purpose utilities for dealing with plot data, 
 * and the VegBank database at this point these may include utilities to 
 * convert various data types, access database properties files and drop
 * plots from the database.
 * 
 */

public class utility 
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
	
	private Connection conn = null;



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
			 System.out.println("failed in utility.getDatabaseParameters " +e.getMessage());
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
			System.out.println(" failed in: utility "+e.getMessage());
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
			System.out.println("failed in utility" + 
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
			 System.out.println(" failed in: utility.getUniqueArray: "+e.getMessage());
		 	e.printStackTrace();
		 }
	 } //end method
	 
	


	/**
	* method that will return a database connection for use with the 
	* vegetation plots database on the machine described in the input 
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
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://"+host+"/plots_dev", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	
	/**
	 * method for droping a plot from the vegbank database.  The data that
	 * are deleted from the database are really the minimal that are required 
	 * for removal of the plot from the plot table -- thus there are some data
	 * left in the database that do not have foreign key relationships with 
	 * the plot table or any of the children tables.
	 * @param plotId -- the PK value of the plot that should be droped
	 * @param dbHost -- the host machine onwhich the database is running
	 */
	 public void dropPlot( int plotId, String dbHost  )
	 {
		 StringBuffer sb;
		 boolean results = true;
		 PreparedStatement pstmt = null;
		 try
		 {
			 sb = new StringBuffer();
			 this.conn = this.getPlotDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 
			 sb.append("delete from stratumcomposition where taxonobservation_id in ( ");
			 sb.append("select taxonobservation_id from taxonobservation where observation_id = ( ");
			 sb.append("select observation_id from observation where plot_id = ("+plotId+"))); \n");
			 pstmt = conn.prepareStatement( sb.toString() );
			 SQLWarning warning = pstmt.getWarnings();
			 if ( warning != null )
			 {
				 System.out.println("WARNINGS > " + warning.toString());
			 }
			 else
			 {
				 results = pstmt.execute();
				 System.out.println("utility  > dropped stratumcomposition: " + results);
			 }
			 
			 sb = new StringBuffer();
			 sb.append("delete from commclass where observation_id = ( ");
			 sb.append("select observation_id from observation where plot_id = "+plotId+" )");
			 pstmt = conn.prepareStatement( sb.toString() );
			 warning = pstmt.getWarnings();
			 if ( warning != null )
			 {
				 System.out.println("WARNINGS > " + warning.toString());
			 }
			 else
			 {
				 results = pstmt.execute();
				 System.out.println("utility  > dropped commclass: " + results);
			 }
			 
			 sb = new StringBuffer();
			 sb.append("delete from stratum where observation_id = (  ");
			 sb.append(" select observation_id from observation where plot_id = ("+plotId+" ))");
			 pstmt = conn.prepareStatement( sb.toString() );
			 warning = pstmt.getWarnings();
			 if ( warning != null )
			 {
				 System.out.println("WARNINGS > " + warning.toString());
			 }
			 else
			 {
				 results = pstmt.execute();
				 System.out.println("utility  > dropped stratum: " + results);
			 }
			 
			 sb = new StringBuffer();
			 sb.append("delete from taxonobservation where observation_id = ( ");
			 sb.append("select observation_id from observation where plot_id = ( "+plotId+" ))");
			 pstmt = conn.prepareStatement( sb.toString() );
			 warning = pstmt.getWarnings();
			 if ( warning != null )
			 {
				 System.out.println("WARNINGS > " + warning.toString());
			 }
			 else
			 {
				 results = pstmt.execute();
				 System.out.println("utility  > dropped taxonobservation: " + results);
			 }
			 
			 sb = new StringBuffer();
			 sb.append(" delete from observation where plot_id = "+plotId );
			 pstmt = conn.prepareStatement( sb.toString() );
			 warning = pstmt.getWarnings();
			 if ( warning != null )
			 {
				 System.out.println("WARNINGS > " + warning.toString());
			 }
			 else
			 {
				 results = pstmt.execute();
				 System.out.println("utility  > dropped observation: " + results);
			 }
			 
			 sb = new StringBuffer();
			 sb.append(" delete from plot where plot_id = "+plotId );
			 pstmt = conn.prepareStatement( sb.toString() );
			 warning = pstmt.getWarnings();
			 if ( warning != null )
			 {
				 System.out.println("WARNINGS > " + warning.toString());
			 }
			 else
			 {
				 results = pstmt.execute();
				 System.out.println("utility  > dropped plot: " + results);
			 }
			 
			 // DROP ALSO FROM THE SUMMARY TABLES
			 sb = new StringBuffer();
			 sb.append(" delete from plotspeciessum where plot_id = "+plotId +";" );
			 sb.append(" delete from plotsitesummary where plot_id = "+plotId );
			 pstmt = conn.prepareStatement( sb.toString() );
			 warning = pstmt.getWarnings();
			 if ( warning != null )
			 {
				 System.out.println("WARNINGS > " + warning.toString());
			 }
			 else
			 {
				 results = pstmt.execute();
				 System.out.println("utility  > dropped plotsitesummary: " + results);
			 }
			 
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
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
			System.out.println("Exception: " + e.getMessage());
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
			 sb = new StringBuffer();
			 this.conn = this.getUserDBConnection(dbHost);
			 this.conn.setAutoCommit(false);
			 System.out.println("utility > dropping user profile for: " + email );
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
			System.out.println("Exception: " + e.getMessage());
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
			 System.out.println("utility > updating user: " + email +" to permission type: " + level );
			 sb.append("update  user_info set permission_type = '"+level+"' where email_address like '"+email+"';");
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			System.out.println("Exception: " + e.getMessage());
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
			 System.out.println("utility > updating user's : " + email +" password to  : " + password );
			 sb.append("update user_info set password = '"+password+"' where email_address like '"+email+"';");
			 pstmt = conn.prepareStatement( sb.toString() );
			 
			 pstmt.execute();
			 conn.commit();
			 pstmt.close();
			 conn.close();
		 }
		 catch ( Exception e )
		 {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		 }
	 }
	
	
	
	/**
	 * main method for running the methods internal to 
	 * this class 
	 */
	public static void main(String[] args)
	{
		if (args.length >= 1)
		{
			//get the plugin named
			String action = args[0];
			utility dplot = new utility();
			if ( action.equals("dropplot") )
			{
				String inPlot = args[1];
				String host = args[2];
				int plotid = Integer.parseInt(inPlot);
				dplot.dropPlot(plotid, host);
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
	

}


