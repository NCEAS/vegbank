import java.sql.*; 
import java.awt.*; 
import java.io.*; 
import java.util.*;
import java.math.*; 
import java.net.URL;
                              
public class plains_db_load_survey { 
public static void main(String[] args) {

String driver_class = "org.gjt.mm.mysql.Driver"; 
 

/*next lines are the DB login variables*/
String connect_string = "jdbc:mysql://linux:3306/test2";
String login = "kern_hacker";
String passwd = "kern_hacker";
Connection conn=null; 
Statement query = null;
Statement stmt = null;  
 
/*Global array variables*/                    
int cnt=0;
String UWI_array[]=new String[12000];  //the unique identifier for the well
double well_id[] = new double[10000];  //the database assgined unique well identifier
double meas_depth[] = new double[10000];
double true_depth[] = new double[10000];
double x_off[] = new double[10000];
double y_off[] = new double[10000];
double x_loc[] = new double[10000];
double y_loc[] = new double[10000];

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

/*READ THE INPUT FILES AND SEPERATE THE DATA INTO ARRAYS*/
BufferedReader in = new BufferedReader(new FileReader(args[0]), 8192);
String s = null;

	while ((s=in.readLine()) != null) {
	
		if (s.indexOf("#")>= 0) {System.out.println("reading EV header");}
		else {
			StringTokenizer t = new StringTokenizer(s, " \t");
			String buf = t.nextToken();
			UWI_array[cnt]=buf;
			meas_depth[cnt]=(new Double(t.nextToken())).doubleValue();
			true_depth[cnt]=(new Double(t.nextToken())).doubleValue();
			x_off[cnt]=(new Double(t.nextToken())).doubleValue();
			y_off[cnt]=(new Double(t.nextToken())).doubleValue();
			x_loc[cnt]=(new Double(t.nextToken())).doubleValue();
			y_loc[cnt]=(new Double(t.nextToken())).doubleValue();
			cnt++;
		
			
		} //end else
		
	}  //end while

} //end try
catch (Exception ex) {
      System.out.println("Error reading the input file: "+ex);
      System.exit(1);}


/*DO THE DATABASE TRANSACTIONS*/
try {
System.out.println("Validating all UWI's in the input file with the database and retreiving the system assigned ID's");
for (int i=0; i<cnt; i++)  { 
	//get the WELL_ID from the master table and stick it into an array
	results = query.executeQuery("SELECT WELL_ID FROM WELL_MASTER WHERE WELL_UWI = '"+UWI_array[i]+"'"); 
	//System.out.println(UWI_array[i]);
	String well_id_num = results.getString("WELL_ID");
	well_id[i] = (new Double(well_id_num)).doubleValue();
		
	}

for (int i=0; i<cnt; i++)  {
query.executeQuery("INSERT into DIR_SURVEY_PT (WELL_ID,MEASURED_DEPTH,X_LOCATION,Y_LOCATION,TRUE_VERT_DEPTH,X_OFFSET,Y_OFFSET) VALUES("+well_id[i]+","+meas_depth[i]+","+x_loc[i]+","+y_loc[i]+","+true_depth[i]+","+x_off[i]+","+y_off[i]+")");
System.out.println(well_id[i]+" "+UWI_array[i]+" "+meas_depth[i]+" "+true_depth[i]+" "+x_loc[i]+" "+y_loc[i]);}

} //end try
catch (Exception ex) {
      System.out.println("Error with database transaction: "+ex);
      System.exit(1);}

//query.executeUpdate("create table well_master (WELL_ID decimal(10,3))");
	



} 
} 
