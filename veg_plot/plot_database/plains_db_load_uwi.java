import java.sql.*; 
import java.awt.*; 
import java.io.*; 
import java.util.*;
import java.math.*; 
import java.net.URL;
                              
public class plains_db_load_uwi { 
public static void main(String[] args) {


 

/*next lines are the DB login variables*/
String driver_class = "org.gjt.mm.mysql.Driver"; 
String connect_string = "jdbc:mysql://linux:3306/test2";
String login = "kern_hacker";
String passwd = "kern_hacker";
Connection conn=null; 
Statement query = null;
Statement stmt = null;  

/*Print out the hard-wired attribute variables*/
System.out.println("This program will load and check a list of UWI's to a database "); 

String fixed_attributes = "COUNRTY=USA,WELL_UWI_TYPE=UWI,CURRENT_COMPANY=PLAINS RESOURCES,STATE=CALIFORNIA,ON_OFF_SHORE=OFF";    
StringTokenizer t1 = new StringTokenizer(fixed_attributes, ",");
while (t1.hasMoreTokens()) {
	String buf1 = t1.nextToken();
	System.out.println(buf1);
	}




/*Make the connection to the JDBC compliant database*/
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


/*Read the file and load it*/

try{

/*input file*/
BufferedReader in = new BufferedReader(new FileReader(args[0]), 8192);
String s = null;
int count =0;
	while ((s=in.readLine()) != null) {
	
		if (s.indexOf("#")>= 0) {System.out.println("reading EV header");}
		else {
			count++;
			StringTokenizer t = new StringTokenizer(s, " \t");
			String buf = t.nextToken();

		/*now, load the table*/
		//query.executeUpdate("INSERT into WELL_MASTER (WELL_UWI, WELL_ID) values('"+buf+"')");
		//query.executeUpdate("INSERT into WELL_MASTER (WELL_UWI, WELL_ID) values('"+buf+"','"+count+"')");
query.executeUpdate
("INSERT into WELL_MASTER (WELL_UWI,WELL_ID,COUNTRY,STATE,WELL_UWI_TYPE,CURRENT_COMPANY,ON_OFF_SHORE) values('"+buf+"','"+count+"','USA','CALIFORNIA','UWI','PLAINS_RESOURCEES','OFF')");
		//System.out.println(buf);
		} //end else
		
	}

//System.out.println(property_string);


//query.executeUpdate("create table well_master (WELL_ID decimal(10,3))");
	

}
catch (Exception ex) {
      System.out.println("Error retrieving data from database. "+ex);
      System.exit(1);
    }

} 
} 
