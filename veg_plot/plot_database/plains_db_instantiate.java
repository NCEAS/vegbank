import java.sql.*; 
import java.awt.*; 
import java.io.*; 
import java.util.*;
import java.math.*; 
import java.net.URL;
                              
public class plains_db_instantiate { 
public static void main(String[] args) {

String driver_class = "org.gjt.mm.mysql.Driver"; 
 

/*next lines are the DB login variables*/
String connect_string = "jdbc:mysql://128.111.220.109:3306/test2";
String login = "kern";
String passwd = "kern";

Connection conn=null; 
Statement query = null;
Statement stmt = null;  
                     
try{ 
	Class.forName(driver_class); 
	System.out.println("Trying to connect..."); 
	if( conn == null) 
	conn = DriverManager.getConnection (connect_string, login, passwd);
	query = conn.createStatement ();
	stmt = conn.createStatement ();	 
	System.out.println("Connected."); 
   } 
catch( Exception e ) {System.out.println("did not connect "+e.getMessage());}

ResultSet results = null;


/*Do the Instantiating*/
try{

/*input file*/
BufferedReader in = new BufferedReader(new FileReader(args[0]), 8192);
String s = null;
String property_string="";
	while ((s=in.readLine()) != null) {
		property_string=property_string+s;
	}

System.out.println(property_string);

/*now, that read to string, instantiate the DB*/

query.executeUpdate(property_string);
//query.executeUpdate("create table well_master (WELL_ID decimal(10,3))");
	

}
catch (Exception ex) {
      System.out.println("Error retrieving data from database. "+ex);
      System.exit(1);
    }

} 
} 
