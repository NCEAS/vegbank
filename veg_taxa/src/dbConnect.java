import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;



/**
 * This class will pass to a calling class a database connection and statement 
 * after making a database connection.  Recomend that a connection pooling class
 * be used instead of this class 
 *
 * @version 11 Jan 2001
 * @author John Harris
 */

public class dbConnect
{

/*Global variables for the database connection*/

Connection conn=null;
Statement query = null;
Statement query1=null;
ResultSet results = null;
ResultSet results1=null;


/**Main route for testing**/
static public void main(String[] args) {
	
System.out.println("This class reads a file called \"database.properties\""
	+"and connects to the database"+
	"using the parameters within the file \n");
	
try {
	
Connection testConn=null;
Statement testQuery = null;
	
dbConnect m = new dbConnect();
m.makeConnection(testConn, testQuery);
testConn=m.outConn;
testQuery=m.outStmt;	
} 
catch (Exception e) {System.out.println("failed calling dbConnect.makeConnection"
	+" call" + e.getMessage());}

}  //end method	




/**
 * Method for making an active database connection and statement
 *
 * @param conn - database connection
 * @param query - statement
 *
 */
public void makeConnection(Connection conn, Statement query)
{

/**
* call the method: getParameters to get the database login parameters  
*/


dbConnect m = new dbConnect();
m.getParameters("database.properties"); //assume that the parameters file is local to the class

/*make the database connections*/
try {
Class.forName(m.driverClass);
	
if( conn == null)
	//point to the conncstion parameters determined in getParameters
        conn = DriverManager.getConnection (m.connectString, m.user, m.password);
        query = conn.createStatement ();
	//System.out.println("Connected to database via string: "+m.connectString);
	outConn=conn;
	outStmt=query;
	} //end try
catch ( Exception e ){System.out.println("failed making db connection: "
	+"dbConnect.makeConnection: "+e.getMessage());e.printStackTrace();}
}


/**
 * This method reads a file called: database properties and grabs database 
 * parameters like login, passwd, for database connectivity - use a resource 
 * bundle instead
 *
 * @param inputFile - a file containg the parameters
 *
 */

private void getParameters(String inputFile)
{

try {	

BufferedReader in = new BufferedReader(new FileReader(inputFile), 8192);
String s=null;

while ( (s=in.readLine()) != null ){

	if (s.startsWith("connectString")) {
		StringTokenizer t = new StringTokenizer(s, "=");
		String buf=t.nextToken();
		connectString=t.nextToken().trim();
			//System.out.println("connectString: "+connectString);
	}//end if
	
	if (s.startsWith("user")) {
		StringTokenizer t = new StringTokenizer(s, "=");
		String buf=t.nextToken();
		user=t.nextToken().trim();
	}//end if
		 
	 if (s.startsWith("password")) {
		StringTokenizer t = new StringTokenizer(s, "=");
	 	String buf=t.nextToken();
		password=t.nextToken().trim();
	 }//end if
		 
	 if (s.startsWith("driverClass")) {
		StringTokenizer t = new StringTokenizer(s, "=");
	 	String buf=t.nextToken();
		driverClass=t.nextToken().trim();
	 }//end if
	
	
	
} //end while 

}  //end try

catch ( Exception e ){System.out.println("failed in class: dbConnect.getParameters, trying "+
	"to read the database parameter file: database.properties "+e.getMessage());}
	

}  //end method


public String user;
public String password;  
public String connectString;
public String driverClass;

public Connection outConn;
public Statement outStmt;

}
