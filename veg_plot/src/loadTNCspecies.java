import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

/*JHH NCEAS 8/2000 - will load ascii legacy data in format used by the TNC plots database
 * (Plots-Species Table) into a prototype Vegetation Plots Database
 */

public class loadTNCspecies {
public static void main(String[] args) {

/*print error meassge if zero arguments passed to app*/
if (args.length < 1) {
System.out.println("Wrong number of arguments passed to asciiLoader\n");
System.out.println("USAGE: java asciiLoader <flag> <load.file>\n");
System.out.println("FLAGS: 	-Q   Prints out all Tables and Fields in Veg Plot DB\n"+
"	-species 	Loads an input file consistent with TNC plot table\n");
System.exit(0);}


String flag=(args[0]);
String infile="null";

/*assign input file*/
if (args.length==2) {infile=(args[1]);}


/*next lines are the DB login variables*/

/*
* use these settings for a mysql database
String driver_class = "org.gjt.mm.mysql.Driver";
String connect_string = "jdbc:mysql://128.111.220.109:3306/test2";
String login = "kern";
String passwd = "kern";
*/


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
	System.out.println("Trying to connect to the database");
	if( conn == null)
	conn = DriverManager.getConnection (connect_string, login, passwd);
	query = conn.createStatement ();
	stmt = conn.createStatement ();
	System.out.println("Connected...");
   }
catch(Exception e) {System.out.println("did not connect "+e.getMessage());}




/* Figure out what flag is sent to the script*/

if (flag.equals("-Q")) {


/*Selects the tables in the DB*/
try{


ResultSet resultTable =query.executeQuery("select TABLE_NAME from USER_TABLES");
	while (resultTable.next()) {
		String Table = resultTable.getString(1);
		System.out.println(Table);
		} //end while
} //end try
catch (Exception ex) {System.out.println("error querying the DB. "+ex);System.exit(1);}
}  //end if


/*loads the plot table to the DB*/
if (flag.equals("-species")) {

try {
BufferedReader in = new BufferedReader(new FileReader(infile), 8192);
System.out.println("reading the input file");
String infileArray[]=new String[100000];
int lineNum=0;
String s=null;

/*read the input file into the array*/
while ( (s=in.readLine()) != null ){

	if (s.startsWith("#")) {System.out.println("reading header");}

	else {
	infileArray[lineNum]=s;
	lineNum++;
	} //end else
	}  //end while

	

/* Read back the data and load to the respective table */

int sysPrinter=0; //every time this gets to 100 print the line
for (int i=0; i<lineNum; i++) {

		StringTokenizer t = new StringTokenizer(infileArray[i], "|");
		/*authorPlotCode: 1:*/ String authorPlotCode=t.nextToken().replace('"',' ').trim();

		String buf=t.nextToken().replace('"',' ').trim();
		/*plantSpeciesCounter: 2:*/ String plotSpeciesCounter=buf;

		/*take out later*/
		buf=t.nextToken().replace('"',' ').trim();
		buf=t.nextToken().replace('"',' ').trim();

		buf=t.nextToken().replace('"',' ').trim();
		/*plantSymbol:3*/ String plantSymbol=buf;


		buf=t.nextToken().replace('"',' ').trim();
		/*scientificName:4*/ String scientificName=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*commonName:6*/ String commonName=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*family:7*/ String family=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*specimenName:8*/ String specimenName=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*PLANTS:9*/ String PLANTS=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*source:10*/ String source=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*withInPlot:11*/ String withInPlot=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*stratumSort:12*/ String startumSort=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*stratum:13*/ String stratum=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*diagnostic:14*/ String diagnostic=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*rangeCover:15*/ String rangeCover=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*realCover:16*/ String realCover=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*other1:17*/ String other1=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*other2:18*/ String other2=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*dbh:19*/ String dbh=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*update:20*/ String update=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*user:21*/ String user=buf;


/*get the taxon_id associated with the last taxon entry in the speciesTaxon table so that the next row will have correct taxon_id*/
ResultSet lastSpecies =query.executeQuery("SELECT count(*) from speciesTaxon");
int lastSpeciesNum = 0;
	while (lastSpecies.next()) {
		lastSpeciesNum = lastSpecies.getInt(1);
		//System.out.println(lastSpeciesNum);
			} //end while

/*increment the plot number by one*/
lastSpeciesNum++;


/*get the correct plot_id from the plotmaster table*/


ResultSet plot_id=query.executeQuery("SELECT plot_id from plotMaster where authorPlotNum like '%"+authorPlotCode+"%'");
	int plotId = 0;
	while (plot_id.next()) {
		plotId = plot_id.getInt(1);
			} //end while

sysPrinter++;
if (sysPrinter==100) { 
System.out.println(lastSpeciesNum+" "+plotId+" "+plantSymbol+"    "+scientificName+" "+stratum+" "+realCover);
sysPrinter=0;
}


/* insert the data  into the plotShape */
//System.out.println(lastSpeciesNum+" "+scientificName+" "+plotId);
try {
stmt.executeUpdate("INSERT into speciesTaxon (taxon_id,plot_id,originalTaxonName,originalTaxonSymbol,stratumType,percentCover) values ("+lastSpeciesNum+", "+plotId+", '"+
scientificName+"', '"+plantSymbol+"', '"+stratum+"', "+realCover+")");

} catch (Exception ex) {System.out.println("Loading the data failed"+ex);System.exit(1);}





	} //end for loop


} //end try
catch (Exception ex) {System.out.println("Error reading input data"+ex);System.exit(1);}

} //end if

}
}
