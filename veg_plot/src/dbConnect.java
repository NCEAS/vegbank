import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;


public class dbConnect
{

/*Global variables for the database connection*/
String driver_class = "oracle.jdbc.driver.OracleDriver";
String connect_string = "jdbc:oracle:thin:@128.111.220.63:1521:exp";
String login = "harris";
String passwd = "use4dev";
Connection conn=null;
Statement query = null;
Statement query1=null;
ResultSet results = null;
ResultSet results1=null;


public void dbConnect()
{
outConn=null;
outStmt=null;
   	
	
}

public void makeConnection(Connection conn, Statement query)
{

	/*make the database connections*/
	try {
	Class.forName(driver_class);
		if( conn == null)
        		conn = DriverManager.getConnection (connect_string, login, passwd);
        		query = conn.createStatement ();
		//	System.out.println("successful connection to the database");
			outConn=conn;
			outStmt=query;
	} //end try

	catch ( Exception e ){System.out.println("did not connect to the database: getName  "+e.getMessage());}

}


  // these variables can be accessed from the calling program

public Connection outConn;
public Statement outStmt;

}
