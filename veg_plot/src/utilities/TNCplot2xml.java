import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

/*JHH NCEAS 9/2000 - will load ascii legacy data in format used by the TNC plots database
	into a prototype Vegetation Plots Database*/

public class TNCplot2xml {
public static void main(String[] args) {

/*print error meassge if zero arguments passed to app*/
if (args.length < 2) {
System.out.println("Wrong number of arguments passed to TNCplot2xml\n");
System.out.println("USAGE: java TNCplot2xml <TNCplot.file> <TNCspecies.file> \n");
System.out.println("The output from this will be a file called outPlotX.xml \n"+
	"where X corresponds to the plot number in the TNC file");
System.exit(0);}


String plotFile=args[0];
String speciesFile=args[1];


String s=null;
int lineNum=0;
int speciesLineNum=0;
String infileArray[]=new String[100000];
String speciesArray[]=new String[100000];
String speciesInfoArray[]=new String[100000];  //this is storage for species
String strataInfoArray[]=new String[100000]; //this is the strata for the species
String coverInfoArray[]=new String[100000];  //this is the percent coverage
int speciesCount=0;

/*Below are all the variables that are going to be required for writing out teh xml file*/
String nullValue="-999.25";
String authorPlotCode=null;
String buf=null;
String subPlot=null;
/*subPlotParentCode:3*/ String subPlotParentCode=null;
/*airPhotoNum:3*/ String airPhotoNum=null;
/*polyCode:3*/ String polyCode=null;
/*provCommName:3*/ String provCommName=null;
/*classCommName:3*/ String classCommName=null;
/*tncElCode:3*/ String tncElCode=null;
/*tncEonumSuf:3*/ String tncEonumSuf=null;
/*locCode:3*/ String locCode=null;
/*subLoc:3*/ String subLoc=null;
/*quadName:3*/ String quadName=null;
/*quadCode:3*/ String quadCode=null;
/*coordSys:3*/ String coordSys=null;
/*gpsFile:3*/ String gpsFile=null;
/*gpsTech:3*/ String gpsTech=null;
/*fieldUtmX:3*/ String fieldUtmX=null;
/*fieldUtmY:3*/ String fieldUtmY=null;
/*corUtmX:3*/ String corUtmX=null;
/*corUtmY:3*/ String corUtmY=null;
/*utmZone:3*/ String utmZone=null;
/*fieldLat:3*/ String fieldLat=null;
/*fieldLong:3*/ String fieldLong=null;
/*corLat:3*/ String corLat=null;
/*corLong:3*/ String corLong=null;
/*surDate:3*/ String surDate=null;
/*surveyor:3*/ String surveyor=null;
/*plotDir:3*/ String plotDir=null;
/*Xdim:3*/ String Xdim=null;
/*Ydim:3*/ String Ydim=null;
/*plotShape:3*/ String plotShape=null;
/*photo:3*/ String photos=null;
/*rollNum:3*/ String rollNum=null;
/*frameNum:3*/ String frameNum=null;
/*perm:3*/ String perm=null;
/*represent:3*/ String represent=null;
/*elev:3*/ String elev=null;
/*elevUnits:3*/ String elevUnits=null;
/*slope:3*/ String slope=null;
/*topoPos:3*/ String topoPos=null;
/*landForm:3*/ String landForm=null;
/*surfGeo:3*/ String surfGeo=null;
/*cowardinSys:3*/ String cowardinSys=null;
/*hydroRegime:3*/ String hydroRegime=null;
/*salinity:3*/ String salinity=null;
/*hydroEvidence:3*/ String hydroEvidence=null;
/*environComment:3*/ String environComment=null;
/*landscapeComment:3*/ String lanscapeComment=null;
/*soilTaxon:3*/ String soilTaxon=null;
/*soilTex:3*/ String soilTex=null;
/*soilDepth:3*/ String soilDepth=null;
/*soilDepthUnit:3*/ String soilDepthUnit=null;
/*soilDrain:3*/ String soilDrain=null;
/*perBedRx:3*/ String perBedRx=null;
/*perLargeRx:3*/ String perLargeRx=null;




/**
* First try block will read the species and plot tables into memory
*/

try {
BufferedReader plotIn = new BufferedReader(new FileReader(plotFile), 8192);
BufferedReader speciesIn = new BufferedReader(new FileReader(speciesFile), 8192);


/*read the plot-specific input file into the array*/
	while ( (s=plotIn.readLine()) != null ){

		if (s.startsWith("#")) {System.out.println("reading header");}

		else {
	infileArray[lineNum]=s;
	lineNum++;
	} //end else
	} //end while 

/*read the species-specific input file into the array*/
while ( (s=speciesIn.readLine()) != null ){

		if (s.startsWith("#")) {System.out.println("reading header");}

		else {
	speciesArray[speciesLineNum]=s;
	speciesLineNum++;
	} //end else
	} //end while 


	} //end  try
catch (Exception ex) {System.out.println("Error reading input data"+ex);System.exit(1);}



/**
* The next block will tokenize the plot data into respective attributes
*/
try {

	for (int i=0; i<lineNum; i++) {



		StringTokenizer t = new StringTokenizer(infileArray[i], "|");
		authorPlotCode=t.nextToken().replace('"',' ').trim();
		buf=t.nextToken().replace('"',' ').trim();
		subPlot=buf;
		buf=t.nextToken().replace('"',' ').trim();
		subPlotParentCode=buf;
		buf=t.nextToken().replace('"',' ').trim();
		buf=t.nextToken().replace('"',' ').trim();
		polyCode=buf;
		buf=t.nextToken().replace('"',' ').trim();
		provCommName=buf;
		buf=t.nextToken().replace('"',' ').trim();
		classCommName=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 tncElCode=buf;
		buf=t.nextToken().replace('"',' ').trim();
		tncEonumSuf=buf;
		buf=t.nextToken().replace('"',' ').trim();
		locCode=buf;
		buf=t.nextToken().replace('"',' ').trim();
		subLoc=buf;
		buf=t.nextToken().replace('"',' ').trim();
		quadName=buf;
		buf=t.nextToken().replace('"',' ').trim();
		quadCode=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 coordSys=buf;
		buf=t.nextToken().replace('"',' ').trim();
		gpsFile=buf;
		buf=t.nextToken().replace('"',' ').trim();
		gpsTech=buf;
		buf=t.nextToken().replace('"',' ').trim();
		fieldUtmX=buf;
		buf=t.nextToken().replace('"',' ').trim();
		fieldUtmY=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 corUtmX=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 corUtmY=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 utmZone=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 fieldLat=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 fieldLong=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 corLat=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 corLong=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 surDate=buf;
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
		 surveyor=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 plotDir=buf;
		buf=t.nextToken().replace('"',' ').trim();
		Xdim=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 Ydim=buf;
		/*temporarily store the size as a string = Xdim this should be changed*/
		buf=t.nextToken().replace('"',' ').trim();
		plotShape=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 photos=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 rollNum=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 frameNum=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 perm=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 represent=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 elev=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 elevUnits=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 slope=buf;

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
		 topoPos=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 landForm=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 surfGeo=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 cowardinSys=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 hydroRegime=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 salinity=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 hydroEvidence=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 environComment=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 lanscapeComment=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 soilTaxon=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 soilTex=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 soilDepth=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 soilDepthUnit=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 soilDrain=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 perBedRx=buf;
		buf=t.nextToken().replace('"',' ').trim();
		 perLargeRx=buf;
/*There are many other fields that go here*/



/*read the species-related elements*/
for (int ii=0; ii<speciesLineNum; ii++) {
	
	//determine if the species related record is the refers to this plot
	if (speciesArray[ii].indexOf(authorPlotCode)>0 || speciesArray[ii].startsWith(authorPlotCode))
		{
		StringTokenizer speciesTok = new StringTokenizer(speciesArray[ii], "|");
		String speciesBuf= speciesTok.nextToken();
		String strataBuf= null;
		speciesBuf= speciesTok.nextToken();
		int bufCount=0;
		
		//this counts out to the position where the species is stored
		while (speciesTok.hasMoreTokens() && bufCount<4 ) {
			speciesBuf= speciesTok.nextToken();
			bufCount++;
		} //end while
		
		
		//count out to the strata name and capture
		while (speciesTok.hasMoreTokens() && bufCount<9 ) {
			strataBuf= speciesTok.nextToken();
			bufCount++;
		} //end while
		
			strataInfoArray[speciesCount]=strataBuf;
			speciesInfoArray[speciesCount]=speciesBuf.replace('&','_').trim();;
			speciesCount++;
				//System.out.println(speciesBuf);
		} //end if
} //end for


/*print the xml file*/
try {

PrintStream out = new PrintStream(new FileOutputStream("outfile"+i+".xml"));

out.println("<?xml version=\"1.0\"?>       ");
out.println("<!DOCTYPE vegPlot SYSTEM \"vegPlot.dtd\">     ");

out.println("<vegPlot>");
out.println("<project>");
out.println("	<projectName>"+ authorPlotCode+"</projectName>");
out.println("	<projectDescription>TNC PROJECT:  </projectDescription>");
out.println("	<projectContributor>");
out.println("		<role>"+nullValue+"</role>");
out.println("		<party>");
out.println("			<salutation>"+nullValue+" </salutation>");
out.println("			<givenName>"+surveyor+"  </givenName>");
out.println("			<surName>"+nullValue+" </surName>");
out.println("			<organizationName>"+nullValue+" </organizationName>");
out.println("			<positionName>"+nullValue+" </positionName>");
out.println("			<hoursOfService>"+nullValue+" </hoursOfService>");
out.println("			<contactInstructions>"+nullValue+" </contactInstructions>");
out.println("			<onlineResource>        ");
out.println("				<linkage>"+nullValue+"</linkage>       ");
out.println("				<protocol>"+nullValue+"</protocol>       ");
out.println("				<name>"+nullValue+"</name>       ");
out.println("				<applicationProfile>"+nullValue+"</applicationProfile>       ");
out.println("				<description>"+nullValue+"</description>       ");
out.println("			</onlineResource>        ");
out.println("			<telephone>        ");
out.println("				<phoneNumber></phoneNumber>        ");
out.println("				<phoneType>"+nullValue+"</phoneType>       ");
out.println("			</telephone>        ");
out.println("			<email>        ");
out.println("				<emailAddress>"+nullValue+"</emailAddress>       ");
out.println("			</email>        ");
out.println("			<address>        ");
out.println("				<deliveryPoint>"+nullValue+"</deliveryPoint>       ");
out.println("				<city>"+nullValue+"</city>       ");
out.println("				<administrativeArea>"+nullValue+"</administrativeArea>       ");
out.println("				<postalCode>"+nullValue+"</postalCode>       ");
out.println("				<country>"+nullValue+"</country>       ");
out.println("				<currentFlag>"+nullValue+"</currentFlag>       ");
out.println("			</address>        ");
out.println("		</party>");
out.println("	</projectContributor>        ");
out.println("	<plot>        ");
out.println("		<authorPlotCode>"+ authorPlotCode+" </authorPlotCode>       ");
out.println("		<parentPlot>"+nullValue+"</parentPlot>       ");
out.println("		<plotType>vegetation</plotType>       ");
out.println("		<samplingMethod>"+nullValue+"</samplingMethod>       ");
out.println("		<coverScale>"+nullValue+"</coverScale>       ");
out.println("		<plotOriginLat>"+corLat+" </plotOriginLat>       ");
out.println("		<plotOriginLong>"+corLong+"</plotOriginLong>       ");
out.println("		<plotShape>"+plotShape+" </plotShape>       ");
out.println("		<plotSize>"+Xdim+" "+Ydim+"</plotSize>       ");
out.println("		<plotSizeAcc>"+nullValue+"</plotSizeAcc>       ");
out.println("		<altValue>"+elev+" </altValue>       ");
out.println("		<altPosAcc>"+nullValue+"</altPosAcc>       ");
out.println("		<slopeAspect>"+nullValue+"</slopeAspect>       ");
out.println("		<slopeGradient>"+slope+"</slopeGradient>       ");
out.println("		<slopePosition>"+topoPos+" </slopePosition>       ");
out.println("		<hydrologicRegime>"+cowardinSys+" </hydrologicRegime>       ");
out.println("		<soilDrainage>"+soilDrain+" </soilDrainage>       ");
out.println("		<surfGeo>"+surfGeo+" </surfGeo>       ");
out.println("		<state>Minnesota </state>       ");
out.println("		<currentCommunity>"+provCommName+" </currentCommunity>       ");
out.println("		<plotObservation>        ");
out.println("			<previousPlot>"+nullValue+"</previousPlot>       ");
out.println("			<plotStartDate>"+surDate+" </plotStartDate>       ");
out.println("			<plotStopDate>"+nullValue+"</plotStopDate>       ");
out.println("			<dateAccuracy>"+nullValue+"</dateAccuracy>       ");
out.println("			<effortLevel>"+nullValue+"</effortLevel>       ");


/*this is where to print out the list of different strata - firts grab the unique*/
 
utility m = new utility();
m.getUniqueArray(strataInfoArray, speciesCount);

//print the unique strata types
for (int ii=0; ii<m.outArrayNum; ii++) {System.out.println(m.outArray[ii]); 
out.println("			<strata>        ");
out.println("				<stratumType>"+m.outArray[ii]+"</stratumType>       ");	
out.println("				<stratumCover>"+nullValue+"</stratumCover>       ");
out.println("				<stratumHeight>"+nullValue+"</stratumHeight>       ");
out.println("			</strata>        ");
}


/*this is where to print out the species list*/
 for (int iii=0; iii<speciesCount; iii++) { 

out.println("                   <taxonObservations>        ");
out.println("                           <authNameId>"+speciesInfoArray[iii]+"</authNameId>       ");
out.println("                           <originalAuthority>"+nullValue+"</originalAuthority>       ");
out.println("                           <strataComposition>        ");
out.println("                                   <strataType>"+strataInfoArray[iii]+"</strataType>       ");
out.println("                                   <percentCover>"+nullValue+"</percentCover>       ");
out.println("                           </strataComposition>        ");
out.println("                           <interptretation>        ");
out.println("                                   <circum_id>"+nullValue+"</circum_id>       ");
out.println("                                   <role>"+nullValue+"</role>       ");
out.println("                                   <date>"+nullValue+"</date>       ");
out.println("                                   <notes>"+nullValue+"</notes>       ");
out.println("                           </interptretation>        ");
out.println("                   </taxonObservations>        ");





	}



out.println("			<communityType>        ");
out.println("				<classAssociation>"+provCommName+"</classAssociation>       ");
out.println("				<classQuality>"+nullValue+"</classQuality>       ");
out.println("				<startDate>"+nullValue+"</startDate>       ");
out.println("				<stopDate>"+nullValue+"</stopDate>       ");
out.println("			</communityType>        ");

out.println("			<citation>        ");
out.println("				<title>"+nullValue+"</title>       ");
out.println("				<altTitle>"+nullValue+"</altTitle>       ");
out.println("				<pubDate>"+nullValue+"</pubDate>       ");
out.println("				<edition>"+nullValue+"</edition>       ");
out.println("				<editionDate>"+nullValue+"</editionDate>       ");
out.println("				<seriesName>"+nullValue+"</seriesName>       ");
out.println("				<issueIdentification>"+nullValue+"</issueIdentification>       ");
out.println("				<otherCredentials>"+nullValue+"</otherCredentials>       ");
out.println("				<page>"+nullValue+"</page>       ");
out.println("				<isbn>"+nullValue+"</isbn>       ");
out.println("				<issn>"+nullValue+"</issn>       ");

out.println("				<citationContributor>        ");
out.println("				</citationContributor>        ");

out.println("			</citation>        ");
out.println("		</plotObservation>        ");

out.println("		<namedPlace>        ");
out.println("			<placeName>"+nullValue+"</placeName>       ");
out.println("			<placeDescription>"+nullValue+"</placeDescription>       ");
out.println("			<gazeteerRef>"+nullValue+"</gazeteerRef>       ");
out.println("		</namedPlace>        ");

out.println("		<stand>        ");
out.println("			<standSize>"+nullValue+"</standSize>       ");
out.println("			<standDescription>"+nullValue+"</standDescription>       ");
out.println("			<standName>"+nullValue+"</standName>       ");
out.println("		</stand>        ");

out.println("		<graphic>        ");
out.println("			<browseName>"+nullValue+"</browseName>       ");
out.println("			<browseDescription>"+nullValue+"</browseDescription>       ");
out.println("			<browseType>"+nullValue+"</browseType>       ");
out.println("			<browseData>"+nullValue+"</browseData>       ");
out.println("		</graphic>        ");

out.println("		<plotContributor>        ");
out.println("			<role>"+nullValue+"</role>       ");
out.println("			<party>"+nullValue+"</party>       ");
out.println("		</plotContributor>        ");
out.println("	</plot>");
out.println("</project>");
out.println("</vegPlot>");
} //end try

catch (Exception ex) {System.out.println("Error printing the xml file "+ex);System.exit(1);}




} //end for loop - looping though the plot array
} //end try
catch (Exception ex) {System.out.println("Error reading input data"+ex);System.exit(1);}


///this is where the printer used to be



}
}
