import java.sql.*; 
import java.awt.*; 
import java.io.*; 
import java.util.*;
import java.math.*; 
import java.net.URL;

//import org.enhydra.instantdb.jdbc.*;
                               
public class instantDB_connect_jdbc { 
public static void main(String argv[]) {
String driver_class = "org.enhydra.instantdb.jdbc.idbDriver"; 
String connect_string = "jdbc:oracle:thin:harris/harris@128.111.220.60:1521:oralin"; 
 
Connection conn=null; 
Statement query = null;
Statement stmt = null;                       
try{ 
	Class.forName(driver_class); 
        System.out.println("Trying to connect..."); 
        	if( conn == null) 
                	conn = DriverManager.getConnection("jdbc:idb:/home/computer/harris/idb/Examples/sample.prp");
			//conn = DriverManager.getConnection("jdbc:idb:F:/instantDB/Examples/sample.prp");
			query = conn.createStatement ();
			stmt = conn.createStatement ();	 
                        System.out.println("Connected."); 
		} 
                        catch( Exception e ) 
                                      {  
                          System.out.println("did not connect "+e.getMessage());  
}

                                    
//first make a table

try{
ResultSet results = null;
System.out.println("making a temporary table");
String resultString = null;

results = query.executeQuery("DROP TABLE plots");
results = query.executeQuery("CREATE TABLE plots (plotName char(100)) ");

}
catch (Exception ex) {
      System.out.println("Error creating the table. "+ex);
      System.exit(1);
    }



//second load some data into the database

try{
ResultSet results = null;
System.out.println("loading some data to the temp table");
String resultString = null;

results = query.executeQuery("INSERT INTO plots (plotName) VALUES('YOSEMITE') ");


}
catch (Exception ex) {
      System.out.println("Error loading the table. "+ex);
      System.exit(1);
    }





//last make a query
try{
ResultSet results = null;
System.out.println("making a test query");
String resultString = null;

results = query.executeQuery("SELECT plotName FROM plots");

while (results.next()) {
	String buf = results.getString("plotName");
	System.out.println(buf);
}

//close the resultSet -- important with instant db
results.close();
conn.close();
System.out.println("ending now");
//System.exit(1);

}
catch (Exception ex) {
      System.out.println("Error retrieving data from database. "+ex);
      System.exit(1);
    }

                                 } 
                                 } 
