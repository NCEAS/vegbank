import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

/*JHH NCEAS 8/2000 - will load ascii legacy data in format used by the TNC plots database
	into a prototype Vegetation Plots Database*/

public class loadTNCplot {
public static void main(String[] args) {

/*print error meassge if zero arguments passed to app*/
if (args.length < 1) {
System.out.println("Wrong number of arguments passed to asciiLoader\n");
System.out.println("USAGE: java asciiLoader <flag> <load.file>\n");
System.out.println("FLAGS: 	-Q   Prints out all Tables and Fields in Veg Plot DB\n"+
"	-plot 	Loads an input file consistent with TNC plot table");
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
if (flag.equals("-plot")) {

try {
BufferedReader in = new BufferedReader(new FileReader(infile), 8192);
System.out.println("reading the input file");
String infileArray[]=new String[1000];
int lineNum=0;
String s=null;

	/*read the input file into the array*/
	while ( (s=in.readLine()) != null ){

		if (s.startsWith("#")) {System.out.println("reading header");}

		else {
	//System.out.println(s);
	infileArray[lineNum]=s;
	lineNum++;
	} //end else
	}

	for (int i=0; i<lineNum; i++) {



		StringTokenizer t = new StringTokenizer(infileArray[i], "|");
		String authorPlotCode=t.nextToken().replace('"',' ').trim();
		String buf=t.nextToken().replace('"',' ').trim();
		String subPlot=buf;
		buf=t.nextToken().replace('"',' ').trim();
		/*subPlotParentCode:3*/ String subPlotParentCode=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*airPhotoNum:3*/ String airPhotoNum=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*polyCode:3*/ String polyCode=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*provCommName:3*/ String provCommName=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*classCommName:3*/ String classCommName=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*tncElCode:3*/ String tncElCode=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*tncEonumSuf:3*/ String tncEonumSuf=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*locCode:3*/ String locCode=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*subLoc:3*/ String subLoc=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*quadName:3*/ String quadName=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*quadCode:3*/ String quadCode=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*coordSys:3*/ String coordSys=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*gpsFile:3*/ String gpsFile=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*gpsTech:3*/ String gpsTech=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*fieldUtmX:3*/ String fieldUtmX=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*fieldUtmY:3*/ String fieldUtmY=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*corUtmX:3*/ String corUtmX=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*corUtmY:3*/ String corUtmY=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*utmZone:3*/ String utmZone=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*fieldLat:3*/ String fieldLat=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*fieldLong:3*/ String fieldLong=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*corLat:3*/ String corLat=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*corLong:3*/ String corLong=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*surDate:3*/ String surDate=buf;
			//StringTokenizer dateToken = new StringTokenizer(surDate, " ");
			//surDate=dateToken.nextToken();

		surDate="24-AUG-2000";

/*fix the date stuff later*/
		//Date today = new Date();
        //String dateOut;
        //DateFormat shortdate = DateFormat.getDateInstance(DateFormat.SHORT);
        //today = new Date();
        //dateOut = dateFormatter.format(today);
        //System.out.println(dateOut + " " + currentLocale.toString());


		buf=t.nextToken().replace('"',' ').trim();
		/*surveyor:3*/ String surveyor=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*plotDir:3*/ String plotDir=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*Xdim:3*/ String Xdim=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*Ydim:3*/ String Ydim=buf;

		/*temporarily store the size as a string = Xdim this should be changed*/


		buf=t.nextToken().replace('"',' ').trim();
		/*plotShape:3*/ String plotShape=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*photo:3*/ String photos=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*rollNum:3*/ String rollNum=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*frameNum:3*/ String frameNum=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*perm:3*/ String perm=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*represent:3*/ String represent=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*elev:3*/ String elev=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*elevUnits:3*/ String elevUnits=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*slope:3*/ String slope=buf;

		/*make sure the the slope is a number, and if not make it a null - this needs more thought */
		char letter[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','>','<'};
		for (int ii=0; ii<28; ii++) {
			Character c= new Character(letter[ii]);  //must make a new character so that letter[] is not dereferenced
			String letterStr = c.toString();  //convert the character to a string so that it mak be used in the 'startsWith'
				if ( ( slope.indexOf(letter[ii])>0 ) || (slope.startsWith(letterStr)) ) {slope="-999.25";}  //convert to null
		} //end for


		buf=t.nextToken().replace('"',' ').trim();
		/*aspect:3*/ String aspect=buf;

		/*make sure the the slopeAspect is a number, and if not make it a null - this needs more thought */
		for (int ii=0; ii<28; ii++) {
			Character c= new Character(letter[ii]);  //must make a new character so that letter[] is not dereferenced
			String letterStr = c.toString();  //convert the character to a string so that it mak be used in the 'startsWith'
			String letterUpperCase = letterStr.toUpperCase();
				if ( ( aspect.indexOf(letter[ii])>0 ) || ( aspect.indexOf(letterUpperCase) >0 ) || (aspect.startsWith(letterUpperCase)) || aspect.startsWith(letterStr)) {aspect="-999.25";}  //convert to null
		} //end for



		buf=t.nextToken().replace('"',' ').trim();
		/*topoPos:3*/ String topoPos=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*landForm:3*/ String landForm=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*surfGeo:3*/ String surfGeo=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*cowardinSys:3*/ String cowardinSys=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*hydroRegime:3*/ String hydroRegime=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*salinity:3*/ String salinity=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*hydroEvidence:3*/ String hydroEvidence=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*environComment:3*/ String environComment=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*landscapeComment:3*/ String lanscapeComment=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*soilTaxon:3*/ String soilTaxon=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*soilTex:3*/ String soilTex=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*soilDepth:3*/ String soilDepth=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*soilDepthUnit:3*/ String soilDepthUnit=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*soilDrain:3*/ String soilDrain=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*perBedRx:3*/ String perBedRx=buf;

		buf=t.nextToken().replace('"',' ').trim();
		/*perLargeRx:3*/ String perLargeRx=buf;



/*There are many other fields that go here*/


/*get the plot_id associated with the last plot in the plotMaster table so that the next row will have correct plot_id*/
ResultSet lastPlot =query.executeQuery("SELECT count(*) from plotMaster");
int lastPlotNum = 0;
	while (lastPlot.next()) {
		lastPlotNum = lastPlot.getInt(1);
		System.out.println(lastPlotNum);
			} //end while

/*increment the plot number by one*/
lastPlotNum++;


/* insert the data  into the plotShape */

System.out.println(lastPlotNum+" "+topoPos);


try {
stmt.executeUpdate("INSERT into plotMaster (plot_id,project_id,authorPlotNum,plotDate,plotOriginLat,plotOriginLong,plotShape,plotSize,altValue,slopeAspect,slopeGradient,slopePosition,hydrologicRegime,soilDrainage,surfGeo) values ("+
lastPlotNum+", "+3+", '"+authorPlotCode+"', '"
+surDate+"', "+corUtmX+", "+corUtmY+", '"+plotShape+"', "+Xdim+", "+elev+", "+slope+", "+aspect+", '"+
topoPos+"', '"+hydroRegime+"', '"+soilDrain+"', '"+surfGeo+"')");

} catch (Exception ex) {System.out.println("Loading the data failed"+ex);System.exit(1);}



System.out.println(authorPlotCode+" "+
surDate);



	} //end for loop


} //end try
catch (Exception ex) {System.out.println("Error reading input data"+ex);System.exit(1);}

} //end if

}
}
