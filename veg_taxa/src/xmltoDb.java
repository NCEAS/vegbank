import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

public class xmltoDb {
public static void main(String[] args) {

String infile=(args[0]);
/*global variables*/
String infileArray[]=new String[1000];
int lineNum=0;

String symbol = null;
String taxon = null;
String commonName = null;
String family = null;
String entryDate = null;
String nameAuthor = null;
String dateEntered = null;
String citation = null;
String label = null;
String type = null;
String circumAuthor = null; 
String circumDateEntered = null;
String circumCitation = null;
String circumLabel = null;
String orgName = null;
String currentStatus =null; 
String statusParty = null;
String startDate = null;
String stopDate = null;
String correlationParty = null;
String correlationAuthor = null;
String correlationDateEntered = null;
String correlationCitation = null;
String correlationLabel = null;
String congruence =null;
String blankSwitch=null; 

try {
BufferedReader in = new BufferedReader(new FileReader(infile), 8192);
System.out.println("reading the input file");
String s=null;

	/*read the input file into the array*/
	while ( (s=in.readLine()) !=null){
	if (s.indexOf("#")>0) {blankSwitch="off";}

	else{
	infileArray[lineNum]=s;
	lineNum++;
	//System.out.println(lineNum);
	} //end else	

	} //end while




} catch(Exception e) {System.out.println("error loading the xml file to memory"+e.getMessage());}


String driver_class = "oracle.jdbc.driver.OracleDriver";
String connect_string = "jdbc:oracle:thin:@128.111.220.63:1521:exp";
String login = "harris";
String passwd = "use4dev";


Connection conn=null;
Statement query = null;
Statement stmt = null;

/* connect to the database*/
try{
Class.forName(driver_class);
System.out.println("connecting to the database");
if( conn == null)
	conn = DriverManager.getConnection (connect_string, login, passwd);
	query = conn.createStatement ();
	stmt = conn.createStatement ();
	System.out.println("Connected...");
 }
catch(Exception e) {System.out.println("did not connect "+e.getMessage());}


try {
symbol = infileArray[1];
taxon = infileArray[2];
commonName = infileArray[3];
family = infileArray[4];
entryDate = infileArray[5];
nameAuthor = infileArray[6];
dateEntered = infileArray[7];
citation = infileArray[8];
label = infileArray[9];
type = infileArray[10];
circumAuthor = infileArray[11];
circumDateEntered = infileArray[12];
circumCitation = infileArray[13];
circumLabel = infileArray[14];
orgName = infileArray[15];
currentStatus = infileArray[16];
statusParty = infileArray[17];
startDate = infileArray[18];
stopDate = infileArray[19];
correlationParty = infileArray[20];
correlationAuthor = infileArray[21];
correlationDateEntered = infileArray[22];
correlationCitation = infileArray[23];
correlationLabel = infileArray[24];
congruence = infileArray[25];
} catch (Exception ex) {System.out.println("errors assigning variables"+ex);System.exit(1);}


try {
/*get the number associated with the last reference in the reference table*/
ResultSet lastRef =query.executeQuery("SELECT count(*) from reference");
int lastRefNum = 0;
	while (lastRef.next()) {
		lastRefNum = lastRef.getInt(1);
		} //end while

/*increment the reference number by one*/
lastRefNum++;

/*insert the name reference information for each document*/
stmt.executeUpdate("INSERT into reference (ref_id, author, speciesName) values ("+lastRefNum+",'"+nameAuthor+"','"+label+"')");

	


/*get the number associated with the last name in the name table*/
ResultSet lastName =query.executeQuery("SELECT count(*) from name");
int lastNameNum = 0;
	while (lastName.next()) {
		lastNameNum = lastName.getInt(1);
		} //end while

/*increment the plot number by one*/
lastNameNum++;

/*insert the name and name information for each document*/
stmt.executeUpdate("INSERT into name (name_id, symbol, name, ref_id, commonName, family, authorName) values ("+lastNameNum+", '"+symbol+"','"+taxon+"',"+lastRefNum+",'"+commonName+"','"+family+"','"+nameAuthor+"')");




/*insert the circumscription information*/
lastRefNum++;
stmt.executeUpdate("INSERT into reference (ref_id, author, speciesName) values ("+lastRefNum+",'"+circumAuthor+"','"+circumLabel+"')");

} catch (Exception ex) {System.out.println("Loading the data failed"+ex);System.exit(1);}


}
}

