import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;

/**
This utility will read a file containing a sql statement and issue that statemet 
to a database
JHH 8/2000 - pass a file containing sql command(s) to a database
        -currently this works with Oracle 8i
	-currently this only allows for update commands (add function for query)
*/

        
public class sql2db {
public static void main(String args[]) {



Connection conn=null;
Statement query = null;
Statement stmt = null;


/**
* User the dbConnect class to create a database connection and statement
*/


try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect.makeConnection call" + e.getMessage());}

	

/**read the input file, replace the semi-colons and run the commands on the database
	-the semi-colons at the end of each command must be removed to work with oracle*/

ResultSet results = null;

try{
BufferedReader in = new BufferedReader(new FileReader(args[0]), 8192);
System.out.println("loading the sql file\n");

String s = null;
String property_string="";
String command_array[]=new String[1000];
int array_cnt=0;

/**
*read all the lines contained within the sql file and parse the individual commands into
*an array structure
*/

while ((s=in.readLine()) != null) {
	if (s.indexOf(";")>0) {
		
		//strip out all the database settings
		
		if (s.trim().startsWith("set")) {System.out.println("reading a setting value");}

		else {
	property_string=property_string+s.replace(';',' ');
	command_array[array_cnt]=property_string;
	array_cnt++;
	property_string="";
	
		} //end else

	}  //end if

	else {property_string=property_string+s;}

	} //end while

/*print out the individual commands being sent to the DB to the screen*/
for (int i=0;i<array_cnt; i++) {System.out.println(command_array[i]+"\n");}

/*send the individual commands to the DB*/
for (int i=0;i<array_cnt; i++) {query.executeUpdate(command_array[i]);}

}
catch (Exception ex) {
  System.out.println("Error retrieving data from database. "+ex);
  System.exit(1);
}



}
}
