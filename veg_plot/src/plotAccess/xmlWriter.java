import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;



/**
* This class will write out an xml file corresponding with the vegPlot.dtd
*/



public class  xmlWriter
{

public void writePlotSummary(String plotSummary[], int plotSummaryNum, String outFile)
{
try {
String nullValue="nullValue";
	
//set up the output query file called query.xml	using append mode 
PrintStream out  = new PrintStream(new FileOutputStream(outFile, false)); 	
	
//print the header stuff
out.println("<?xml version=\"1.0\"?>       ");
out.println("<!DOCTYPE vegPlot SYSTEM \"vegPlot.dtd\">     ");
out.println("<vegPlot>");
	
//tokenize the incomming string array into respective elements
//make the token map and put into cvs
for (int i=0;i<=plotSummaryNum; i++) {	
	 StringTokenizer t = new StringTokenizer(plotSummary[i], "|");
	 String plotId=t.nextToken();
	 String authorPlotCode=t.nextToken();
	 String project_id=t.nextToken();
	 String surfGeo=t.nextToken();
	 String plotType=t.nextToken();
	 String origLat=t.nextToken();
	 String origLong=t.nextToken();
	 //the remaining elements are species so grab the species names and 
	 //stick into a array
	 String buf=null;
	 String speciesArray[]=new String[100000];
	 int speciesArrayNum=0;
	 while (t.hasMoreTokens()) {
		buf=t.nextToken();
		//System.out.println("writer "+buf);
		speciesArray[speciesArrayNum]=buf;
		speciesArrayNum++;
	 }
	 
//print to elements in the correct order
out.println("<project>");
out.println("	<projectName>"+authorPlotCode+"</projectName>");
out.println("	<projectDescription>TNC PROJECT:</projectDescription>");
out.println("	<projectContributor>");
out.println("		<role>"+nullValue+"</role>");
out.println("		<party>");
out.println("			<salutation>"+nullValue+" </salutation>");
out.println("			<givenName>"+nullValue+"  </givenName>");
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
out.println("				<phoneNumber>nullValue</phoneNumber>        ");
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
out.println("		<plotType>"+plotType+"</plotType>       ");
out.println("		<samplingMethod>"+nullValue+"</samplingMethod>       ");
out.println("		<coverScale>"+nullValue+"</coverScale>       ");
out.println("		<plotOriginLat>"+origLat+" </plotOriginLat>       ");
out.println("		<plotOriginLong>"+origLong+"</plotOriginLong>       ");
out.println("		<plotShape>"+nullValue+" </plotShape>       ");
out.println("		<plotSize>"+nullValue+"</plotSize>       ");
out.println("		<plotSizeAcc>"+nullValue+"</plotSizeAcc>       ");
out.println("		<altValue>"+nullValue+" </altValue>       ");
out.println("		<altPosAcc>"+nullValue+"</altPosAcc>       ");
out.println("		<slopeAspect>"+nullValue+"</slopeAspect>       ");
out.println("		<slopeGradient>"+nullValue+"</slopeGradient>       ");
out.println("		<slopePosition>"+nullValue+" </slopePosition>       ");
out.println("		<hydrologicRegime>"+nullValue+" </hydrologicRegime>       ");
out.println("		<soilDrainage>"+nullValue+" </soilDrainage>       ");
out.println("		<surfGeo>"+surfGeo+" </surfGeo>       ");

out.println("		<plotObservation>        ");
out.println("			<previousPlot>"+nullValue+"</previousPlot>       ");
out.println("			<plotStartDate>"+nullValue+" </plotStartDate>       ");
out.println("			<plotStopDate>"+nullValue+"</plotStopDate>       ");
out.println("			<dateAccuracy>"+nullValue+"</dateAccuracy>       ");
out.println("			<effortLevel>"+nullValue+"</effortLevel>       ");

out.println("			<strata>        ");
out.println("				<stratumType>"+nullValue+"</stratumType>       ");	
out.println("				<stratumCover>"+nullValue+"</stratumCover>       ");
out.println("				<stratumHeight>"+nullValue+"</stratumHeight>       ");
out.println("			</strata>        ");




/*this is where to print out the list of different species found in the resultset
* for now just print the unique ones*/
utility m = new utility();
m.getUniqueArray(speciesArray, speciesArrayNum);

//print the unique species types
for (int h=0; h<m.outArrayNum; h++) {
	
out.println("                   <taxonObservations>        ");
out.println("                           <authNameId>"+m.outArray[h]+"</authNameId>       ");
out.println("                           <originalAuthority>"+nullValue+"</originalAuthority>       ");
out.println("                           <strataComposition>        ");
out.println("                                   <strataType>"+nullValue+"</strataType>       ");
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
out.println("				<classAssociation>"+nullValue+"</classAssociation>       ");
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


}
//print the end of the file
out.println("</vegPlot>");
	
	
}
catch (Exception e) {System.out.println("failed in xmlWriter.writePlotSummary" + 
	e.getMessage());}

}	
	
}
