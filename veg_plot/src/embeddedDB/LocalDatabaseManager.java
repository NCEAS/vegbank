	
/**
 * this class is to be used to manage the local embedded database and will
 * perform such tasks as creating the database tables, updateing the summary
 * tables ad dropping, restarting the database
 *
 * @author John Harris
 * @version March 28, 2001
 *
 */



import java.sql.*; 
import java.awt.*; 
import java.io.*; 
import java.util.*;
import java.math.*; 
import java.net.URL;

                               
public class LocalDatabaseManager 
{

	
public static void main(String args[]) {
ResourceBundle rb = ResourceBundle.getBundle("LocalDatabaseManager");
String database=rb.getString("database");
String baseTables=rb.getString("baseTables");
String summaryTables=rb.getString("summaryTables");

if (args.length < 1) 
	{
		System.out.println("Wrong number of arguments passed to LocalDatabaseManager\n");
		System.out.println("USAGE: java LocalDatabaseManager <action>");
		System.out.println(	"where action may be createBaseTables, createSummaryTables, updateSummary \n"
		+" \n"
		+" createBaseTables: creates the base tables which consist of the normalized \n"
		+" database structure \n \n"
		+" createSummaryTables: creates the summary tables which are often used by \n"
		+" the database access module for querying \n \n"
		+" updateSummary: updates the summary tables from the base tables -- this \n"
		+" must be run before querying the embedded database b/c the query modules \n"
		+" relies heavily on the summary tables \n \n"
		+" drop: to drop the database just recreate the base tables");
		System.exit(0);
	}
String action=args[0];



if (action.trim().equals("createBaseTables"))
{
	LocalDatabaseManager cld = new LocalDatabaseManager();
	cld.createLocalDatabase(baseTables);	
}

else if (action.trim().equals("createSummaryIndexes"))
{
//	LocalDatabaseManager cld = new LocalDatabaseManager();
//	cld.indexSummaryTables("");	
}



else if (action.trim().equals("createSummaryTables"))
{
	LocalDatabaseManager cld = new LocalDatabaseManager();
	cld.createLocalDatabase(summaryTables);	
}


else if (action.trim().equals("updateSummary")) 
{
 LocalDatabaseManager us = new LocalDatabaseManager();
	us.updateSummary("someVarible1");	
}


else {System.out.println("unrecognized command");}

}	




/**
 *
 * This method will replicate the data stored on the central database to the
 * local database
 *
 */
public void updateSummary (String someVariable)
{
	ResourceBundle rb = ResourceBundle.getBundle("LocalDatabaseManager");
	String database=rb.getString("database");
	System.out.println("testing -- updateSummary");
	String driver_class = "org.enhydra.instantdb.jdbc.idbDriver"; 

	Connection conn=null; 
	Statement query = null;
	ResultSet results = null;
	Statement stmt = null;                       

	try
	{ 
		Class.forName(driver_class); 
		System.out.println("Trying to connect..."); 
		if( conn == null)
		{
			//assume that the idb database is alwas in the following relative directory
			conn = DriverManager.getConnection("jdbc:idb:"+database);
			query = conn.createStatement ();
			System.out.println("Connected.");
		}
	}
	catch( Exception e ) 
	{  
		System.out.println("did not connect -- updateSummary"+e.getMessage());  
	}


//get the site summary information
try{
Hashtable summaryResultHash = new Hashtable();
String action="select";

//String statement="select PLOT.PLOT_ID, PLOT.AUTHORPLOTCODE, PLOTOBSERVATION.AUTHOROBSCODE" 
//	+" from PLOT, PLOTOBSERVATION where PLOT_ID > 0 AND PLOTOBSERVATION.PARENTPLOT = PLOT.PLOT_ID";

String statement = " select plot.plot_id, plot.project_id, plot.plotType, "
+" plot.samplingMethod, plot.coverScale, "
+" plot.plotoriginlat, plot.plotoriginlong, plot.plotshape,  plot.altvalue, "
+" plot.slopeaspect, plot.slopegradient, plot.slopeposition, plot.hydrologicregime, "
+" plot.soildrainage, plot.currentcommunity, plot.xcoord, plot.ycoord, "
+" plot.coordtype, plotObservation.obsstartdate, plotObservation.obsstopdate, "
+" plotObservation.effortLevel, plotObservation.hardcopylocation, "
+" plotObservation.soiltype, plot.authorplotcode, plot.surfGeo, plot.state, " 
+" plotObservation.parentplot, plotObservation.authorobscode, "
+" plotObservation.soilDepth, plotObservation.leaftype "
+" from plot, plotObservation where plot_id >= 0 "
+" and plotObservation.parentplot = plot.plot_id ";

	int returnFieldLength=27;
	String returnFields[]=new String[27];	
	
	returnFields[0]="PLOT_ID";
	returnFields[1]="project_id";
	returnFields[2]="plotType";	
	returnFields[3]="samplingMethod";
	returnFields[4]="coverscale";
	returnFields[5]="plotoriginlat";
	returnFields[6]="plotoriginlong";
	returnFields[7]="plotshape";
	returnFields[8]="altvalue";
	returnFields[9]="slopeaspect";
	returnFields[10]="slopegradient";
	returnFields[11]="slopeposition";
	returnFields[12]="xcoord";
	returnFields[13]="ycoord";
	returnFields[14]="coordType";
	returnFields[15]="obsstartdate";
	returnFields[16]="obsstopdate";
	returnFields[17]="effortlevel";
	returnFields[18]="hardcopylocation";
	returnFields[19]="soiltype";
	returnFields[20]="authorplotcode"; 
	returnFields[21]="surfgeo";
	returnFields[22]="state";
	returnFields[23]="parentplot";
	returnFields[24]="authorobscode";
	returnFields[25]="soildepth";
	returnFields[26]="leafType";
	
	//issue the select statement
	issueStatement k = new issueStatement();
	k.issueSelect(statement, action, returnFields, returnFieldLength, 
	summaryResultHash, conn);
	
	System.out.println(summaryResultHash.toString());

//insert the plot species data
Statement s;
s = conn.createStatement();
for (int i=0; i<k.outReturnFieldsNum; i++) {

System.out.println( (String)summaryResultHash.get("PLOT_ID."+i));

//grab the variable from the hash
String plotId = (String)summaryResultHash.get("PLOT_ID."+i);
String  project_id = (String)summaryResultHash.get("project_id."+i);
String plotType = (String)summaryResultHash.get("plotType."+i);
String samplingMethod =(String)summaryResultHash.get("samplingMethod."+i);
String coverScale =(String)summaryResultHash.get("coverscale."+i);
String plotOriginLat =(String)summaryResultHash.get("plotoriginlat."+i);
String plotOriginLong =(String)summaryResultHash.get("plotoriginlong."+i);
String plotShape =(String)summaryResultHash.get("plotshape."+i);
String altValue =(String)summaryResultHash.get("altvalue."+i);
String slopeAspect= (String)summaryResultHash.get("slopeaspect."+i);
String slopeGradient= (String)summaryResultHash.get("slopegradient."+i);
String slopePosition =(String)summaryResultHash.get("slopeposition."+i);
String xCoord =(String)summaryResultHash.get("xcoord."+i);
String yCoord =(String)summaryResultHash.get("ycoord."+i);
String coordType= (String)summaryResultHash.get("coordType."+i);
String obsStartDate =(String)summaryResultHash.get("obsstartdate."+i);
String obsStopDate =(String)summaryResultHash.get("obsstopdate."+i);
String effortLevel =(String)summaryResultHash.get("effortlevel."+i);
String hardCopyLocation =(String)summaryResultHash.get("hardcopylocation."+i);
String soilType =(String)summaryResultHash.get("soiltype."+i);
String authorPlotCode= (String)summaryResultHash.get("authorplotcode."+i);
String surfGeo =(String)summaryResultHash.get("surfgeo."+i);
String state =(String)summaryResultHash.get("state."+i);
String parentPlot= (String)summaryResultHash.get("parentplot."+i);
String authorObsCode= (String)summaryResultHash.get("authorobscode."+i);
String soilDepth =(String)summaryResultHash.get("soildepth."+i);
String leafType  =(String)summaryResultHash.get("leafType."+i);

//check that not nulls
if (plotId == null  )  {
	plotId = "999";
	project_id = "999";
	plotType = "999";
	samplingMethod = "999";
	coverScale = "999";
	plotOriginLat = "999";
	plotOriginLong = "999";
	plotShape = "999";
	altValue = "999";
	slopeAspect= "999";
	slopeGradient= "999";
	slopePosition = "999";
	xCoord = "999";
	yCoord = "999";
	coordType= "999";
	obsStartDate = "999";
	obsStopDate = "999";
	effortLevel = "999";
	hardCopyLocation = "999";
	soilType = "999";
	authorPlotCode= "999";
	surfGeo = "999";
	state = "999";
	parentPlot=  "999";
	authorObsCode= "999";
	soilDepth = "999";
	leafType  = "999";
}


//fix this later
obsStartDate= "2000";
obsStopDate = "2000";
authorObsCode = authorObsCode.replace('.', 'A').replace('#', 'C');
authorPlotCode = authorPlotCode.replace('.', 'A').replace('#', 'C');
surfGeo = surfGeo.replace(':', '_').replace(' ', '_').replace('\\', '_').trim();
slopeAspect = slopeAspect.replace(':', '_').replace(' ', '_').trim();
slopeGradient = slopeGradient.replace(':', '_').replace(' ', '_').trim();
slopePosition = slopePosition.replace(':', '_').replace(' ', '_').trim();

//make the insertion string
String insertString = "INSERT INTO plotsitesummary (PLOT_ID, project_id, "
+" plotType, samplingMethod, coverScale, plotOriginLat, plotOriginLong, "
+" plotShape, altValue, slopeAspect, slopeGradient, slopePosition, xCoord, "
+" yCoord, coordType,  obsStartDate,  obsStopDate, effortLevel, hardCopyLocation, "
+" soilType, authorPlotCode, surfGeo, state, parentPlot, authorObsCode, soilDepth, "
+" leafType ) VALUES ("+plotId+","+ project_id +","+plotType+","+ samplingMethod+","
+coverScale+","+plotOriginLat+","+plotOriginLong+",'"+plotShape+"',"+altValue+",'"
+slopeAspect+"','"+slopeGradient+"','"+ slopePosition+"',"+ xCoord +","+yCoord+",'"
+coordType+"',"+obsStartDate+","+ obsStopDate+","+effortLevel+",'"+ hardCopyLocation+"',"
+soilType+",'"+authorPlotCode+"','" +surfGeo+"'," +state+"," + parentPlot+",'"
+authorObsCode+"',"+ soilDepth+","+ leafType  +")";

//added this to get rid of the nulls that I am not sure where they are comming
// from
if (!plotId.equals("999")) {
	s.executeUpdate(insertString);
}

}



	
//now get the species summary information
Hashtable speciesSummaryResultHash = new Hashtable();
action="select";
statement="select TAXONOBSERVATION.OBS_ID, TAXONOBSERVATION.AUTHORNAMEID, "
+" STRATACOMPOSITION.CHEATSTRATUMTYPE, STRATACOMPOSITION.PERCENTCOVER "
//+" PLOTOBSERVATION.AUTHOROBSCODE "
+" from TAXONOBSERVATION, STRATACOMPOSITION"
+" where TAXONOBSERVATION.TAXONOBSERVATION_ID = STRATACOMPOSITION.TAXONOBSERVATION_ID " ;

	int returnSpeciesFieldLength=4;
	String returnSpeciesFields[]=new String[4];	
	
	returnSpeciesFields[0]="OBS_ID";
	returnSpeciesFields[1]="AUTHORNAMEID";
	returnSpeciesFields[2]="CHEATSTRATUMTYPE";
	returnSpeciesFields[3]="PERCENTCOVER";
//	returnSpeciesFields[4]="AUTHOROBSCODE";

	
	//issue the select statement
	issueStatement k1 = new issueStatement();
	k1.issueSelect(statement, action, returnSpeciesFields, returnSpeciesFieldLength, 
	speciesSummaryResultHash, conn);
	
//	System.out.println(speciesSummaryResultHash.toString());
	
//insert these data into the appropriate table
System.out.println(">>>>"+k1.outReturnFieldsNum);
for (int i=0; i<k1.outReturnFieldsNum; i++) {
	
//fix these problems later

	
String OBS_ID = (String)speciesSummaryResultHash.get("OBS_ID."+i);
String AUTHORNAMEID = (String)speciesSummaryResultHash.get("AUTHORNAMEID."+i);
String PERCENTCOVER = (String)speciesSummaryResultHash.get("PERCENTCOVER."+i);
String AUTHOROBSCODE = (String)speciesSummaryResultHash.get("AUTHOROBSCODE."+i);

if (OBS_ID == null ) {
	OBS_ID = "999";
}

String insertString = "INSERT INTO PLOTSPECIESSUM (OBS_ID, AUTHORNAMEID, PERCENTCOVER, "
+") values ("+OBS_ID+",'"+ AUTHORNAMEID +"',"+PERCENTCOVER+")";

//added this if statement to get rid of  the nulls that I'm not sure about the
// origin of

if (!OBS_ID.equals("999")) {
	s.executeUpdate(insertString);
}
}


//now get the parent plot info

System.out.println("badspot");
Hashtable parentPlotResultHash = new Hashtable();
action="select";
statement="select PARENTPLOT, AUTHORPLOTCODE, OBS_ID"
+" from  PLOTOBSERVATION ";

	int returnParentPlotFieldLength=3;
	String returnParentPlotFields[]=new String[3];	
	
	
	returnParentPlotFields[0]="PARENTPLOT";
	returnParentPlotFields[1]="AUTHORPLOTCODE";
	returnParentPlotFields[2]="OBS_ID";
	
	//issue the select statement
	issueStatement k2 = new issueStatement();
	k2.issueSelect(statement, action, returnParentPlotFields, returnParentPlotFieldLength, 
	parentPlotResultHash, conn);
	
	System.out.println(parentPlotResultHash.toString() );
	
//update the species summary table  
for (int i=0; i<k2.outReturnFieldsNum; i++) {

String PARENTPLOT = (String)parentPlotResultHash.get("PARENTPLOT."+i);
String AUTHORPLOTCODE = (String)parentPlotResultHash.get("AUTHORPLOTCODE."+i);
String OBS_ID = (String)parentPlotResultHash.get("OBS_ID."+i);

if (OBS_ID == null  || PARENTPLOT==null || AUTHORPLOTCODE==null ) {OBS_ID = "999"; PARENTPLOT = "999";}

//badly cheated here with the parent plot plot id to save time

String insertString = "UPDATE plotSpeciesSum "
+"	set PARENTPLOT  = "+PARENTPLOT+","
+"  AUTHORPLOTCODE = "+AUTHORPLOTCODE+","
+"  PLOT_ID = "+PARENTPLOT 
+" WHERE PLOTSPECIESSUM.OBS_ID = "+OBS_ID;

//added this if statement to get rid of  the nulls that I'm not sure about the
// origin of

if (!OBS_ID.equals("999")) {
	s.executeUpdate(insertString);
}


}
	
	

//select PLOTOBSERVATION.PARENTPLOT, PLOTOBSERVATION.AUTHORPLOTCODE
//	from PLOTOBSERVATION
//	where PLOTOBSERVATION.OBS_ID = PLOTSPECIESSUM.OBS_ID);

query.close();
conn.close();
System.out.println("ending now");
//System.exit(1);

}
catch (Exception ex) {
System.out.println("Error creating the table. "+ex+" "+ex.getMessage());
ex.printStackTrace();
System.exit(1);
}

}




/**
 *
 *  This method will create a local database on the client machine (written for
 * instantDB from Enhydra.org)
 *
 */
public void createLocalDatabase (String sqlFile)
{
	ResourceBundle rb = ResourceBundle.getBundle("LocalDatabaseManager");
	String database=rb.getString("database");
	String baseTables=rb.getString("baseTables");
	String summaryTables=rb.getString("summaryTables");
	
	System.out.println("testing -- createLocalDatabase");
	String driver_class = "org.enhydra.instantdb.jdbc.idbDriver"; 
	Connection conn=null; 
	Statement query = null;
	Statement stmt = null; 
	try
	{ 
		//System.out.println(">Current conn status: ");
		System.out.println("Trying to connect...");
		Class.forName(driver_class); 
		//assume that the idb database is alwas in the following relative directory
		conn = DriverManager.getConnection("jdbc:idb:"+database);
		//conn = DriverManager.getConnection("jdbc:idb:../../../data/sample.prp");
		query = conn.createStatement ();
		System.out.println("Connected.");
	} 
	catch( Exception e ) 
	{  
		System.out.println("did not connect createBase "+e.getMessage());  
	}

//first make a table

	try
	{
		ResultSet results = null;
		//System.out.println("making a temporary table");
		String resultString = null;

		//read the sql to create the local database into a vector
		String s = null;
		StringBuffer sb = new StringBuffer();
		BufferedReader inSql = new BufferedReader(new FileReader(sqlFile));
		//BufferedReader inSql = new BufferedReader(new FileReader("buildSummary.txt"));


		while((s = inSql.readLine()) != null) 
		{
			//	System.out.println(s);
	
			if ( !s.startsWith("end") ) 
			{ 
				sb.append(s);
			}
	
			else  
			{
				query.executeUpdate(sb.toString());
				sb = new StringBuffer();
			}
		}
		//now execute the table creation statement
		query.executeUpdate(sb.toString());
	
		//results = query.executeQuery(sb.toString());
		//System.out.println(results.getWarnings() );
		//results.close();
		query.close();
		conn.close();
		System.out.println("ending now");
		//System.exit(1);
	}
	catch (Exception ex) 
	{
		System.out.println("Error creating the table. "+ex+" "+ex.getMessage());
		ex.printStackTrace();
		System.exit(1);
	}
} 

} 
