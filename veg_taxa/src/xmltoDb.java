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
String flag=(args[1]);

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

String refType = null;
String errorArray[]=new String[10000];
FileOutputStream file_output;


try {
BufferedReader in = new BufferedReader(new FileReader(infile), 8192);
System.out.println("reading the input file");
String s=null;

	/*read the input file into the array*/
	while ( (s=in.readLine()) !=null){
	if (s.indexOf("#")>0) {blankSwitch="off";}

	else{
	infileArray[lineNum]=s.replace('\'',' ').trim();;
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


/*parse the variables from the input file*/
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


if (flag.startsWith("name")) {
System.out.println("loading the name into the database but not performing the correlation update");
try {
PrintStream out = new PrintStream(new FileOutputStream("error.log", true));


/*determine if the name entry already exists*/
ResultSet testQuery =query.executeQuery("SELECT name_id from  name where name like '%"+taxon+"%'");
int redundantName=-999;
while (testQuery.next()) {redundantName=testQuery.getInt(1);}
	/*if this name is redundant print to log*/
		if (redundantName !=-999) {
			System.out.println("REDUNDANT NAME: "+taxon);
			String dateString=null;
			Date date = new Date();
			dateString=(date.toString());
			out.println("REDUNDANT NAME: "+taxon+" date: "+dateString);	
		
			}


else {
/*get the number associated with the last reference in the reference table*/
ResultSet lastRef =query.executeQuery("SELECT count(*) from reference");
int lastRefNum = 0;
	while (lastRef.next()) {
		lastRefNum = lastRef.getInt(1);
		} //end while

/*increment the reference number by one*/
lastRefNum++;

/*insert the name reference information for each document*/
refType="reference";
stmt.executeUpdate("INSERT into reference (ref_id, author, speciesName, circumType) values ("+lastRefNum+",'"+nameAuthor+"','"+label+"','"+refType+"')");

	


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




/*insert the circumscription information into the reference table*/
lastRefNum++;
refType="circumscription";
stmt.executeUpdate("INSERT into reference (ref_id, author, speciesName, circumType) values ("+lastRefNum+",'"+circumAuthor+"','"+circumLabel+"','"+refType+"')");



/*get the number associated with the circumscription record*/

ResultSet lastCircum=query.executeQuery("SELECT count(*) from CIRCUMSCRIPTION");
int lastCircumNum = 0;
        while (lastCircum.next()) {
                lastCircumNum = lastCircum.getInt(1);
                } //end while

/*insert the circumscription information into the circumscription table*/
lastCircumNum++;
stmt.executeUpdate("INSERT into CIRCUMSCRIPTION (CIRCUM_ID, REF_ID) values ("+lastCircumNum+","+lastRefNum+")");



/*get the number associated with the last name_usage record*/
ResultSet lastNameUsage  =query.executeQuery("SELECT count(*) from NAME_USAGE");
int lastNameUsageNum = 0;
        while (lastNameUsage.next()) {
                lastNameUsageNum = lastNameUsage.getInt(1);
                } //end while


/*update that name_usage table*/
lastNameUsageNum++;
stmt.executeUpdate("INSERT into NAME_USAGE (NAMEUSAGEID, CIRCUM_ID, NAME_ID, PARTY_ID ) values ("+lastNameUsageNum+","+lastCircumNum+","+lastNameNum+",1"+")");


/*update the status table   - currentStatus - */
ResultSet lastStatus  =query.executeQuery("SELECT count(*) from STATUS");
int lastStatusNum = 0;
        while (lastStatus.next()) {
                lastStatusNum = lastStatus.getInt(1);
                } //end while
stmt.executeUpdate("INSERT into STATUS (PARTY_CIRCUM_ID, CIRCUM_ID, STATUS, PARTY_ID) values ("+lastStatusNum+","+lastCircumNum+",'"+currentStatus+"',1)");


}  //end else
} catch (Exception ex) {System.out.println("Loading the data failed"+ex);System.exit(1);}
} //end if





/*this is where the correlation updating takes place*/
if (flag.startsWith("correlation")) {
try {

System.out.println("updating the correlation");

/*circumscription number for the plant defined in the input - currently only using one criteria thie will have to be added to later*/
System.out.println(taxon);
ResultSet nameQuery= query.executeQuery("select PARTY_CIRCUM_ID from status where (select circum_id from name_usage where (select name_id from name where name like '%"+taxon+"%')=name_id)=circum_id");
int  nameResult=0;
while (nameQuery.next()) {
                nameResult=nameQuery.getInt(1);
                System.out.println("this taxon has a  circumscription number: "+nameResult);
                } //end while


/*circumscription number for the correlation plant - currently only using one criteria thie will have to be added to later*/
ResultSet relatedNameQuery= query.executeQuery("select PARTY_CIRCUM_ID from status where (select circum_id from name_usage where (select name_id from name where name like '%"+correlationLabel+"%')=name_id)=circum_id");

int  relatedNameResult=0;
while (relatedNameQuery.next()) {
                relatedNameResult=relatedNameQuery.getInt(1);
		System.out.println("this taxon is related to the taxon with circumscription number: "+relatedNameResult+" by: "+congruence);
                } //end while



/*update the correlation table*/

ResultSet lastCorrelation  =query.executeQuery("SELECT count(*) from CORRELATION");
int lastCorrelationNum = 0;
        while (lastCorrelation.next()) {
                lastCorrelationNum = lastCorrelation.getInt(1);
                } //end while

stmt.executeUpdate("INSERT into CORRELATION (CORRELATION_ID, PARTY_CIRCUM1, PARTY_CIRCUM2, CONVERGENCE) values ("+lastCorrelationNum+","+nameResult+","+relatedNameResult+",'"+congruence+"')");


} //end try
catch (Exception ex) {System.out.println("Loading the data failed at the correlation level"+ex);System.exit(1);}
} //end if

else {System.out.println("unrecognized flag - use name or correlation only");}
}
}

