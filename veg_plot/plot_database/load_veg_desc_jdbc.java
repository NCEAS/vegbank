import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;

/* JHH for NCEAS 7/2000 - this code will read a file into a MYSQL
database.  The code can be quickly modified to work on another RDBMS*/

public class load_veg_desc_jdbc {
public static void main(String argv[]) {

/*Set up the MYSQL connection*/
String driver_class = "org.gjt.mm.mysql.Driver";
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
		   	stmt = conn.createStatement();
                        System.out.println("Connected.");
                    } // end try

catch( Exception e ) {System.out.println("did not connect "+e.getMessage());}

ResultSet results = null;

try{

String s = null;
String buf = "null";
BufferedReader in = new BufferedReader(new FileReader("/plot_database/xml_edit.flat"), 8192);

      while ( (s=in.readLine()) != null ){

StringTokenizer t = new StringTokenizer(s, "|");
String plot_id = t.nextToken();
String orig_species = t.nextToken();
String stratum_type = t.nextToken();
String percent_cover = t.nextToken();


buf = s;

//System.out.println(buf);
query.executeUpdate("INSERT INTO vegDescription (plot_id, orig_species,stratum_type,percent_cover)"+
"VALUES ('"+plot_id+"','"+orig_species+"','"+stratum_type+"','"+percent_cover+"')");
} //clse first while loop


}
catch (Exception ex) {
      System.out.println("Error retrieving data from database. "+ex);
      System.exit(1);
    }

                                 }
                                 }
