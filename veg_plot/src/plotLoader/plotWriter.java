import java.io.*;
import java.util.*;
import java.sql.*;




/**
* This class will insert a plot into the database the string passed to this class is the transformed (via XSLT)
*  XML data document and the string has both a database address and the data which wil have to be converted to
*  the correct type and then bound to one of the prepared statements
*/

public class plotWriter {

	
	
	/**
	* This method takes the input stream from the xslt transform and inserts the data into their
	* respective cells
	*/
	

public void insertPlot (String[] transformedString, int transformedStringNum) {

	/**
	* the ordering of events is critical in this method and will be in the following order:  validate if project exists,
	*  update project, validate if plot exists, update plots
	*/
	
	//call the setAttributeAddress method to separate the database address from the data
	
	plotWriter a =new plotWriter();
	a.setAttributeAddress(transformedString, transformedStringNum);
	
	String address[]=a.dbAddress;  // the returned database address string
	String dataString[]=a.attributeData;  // the returened data string
	int addressNum=a.dbAddressNum; //the number of addresses and attributes in the returned set
	
	
	//Define the variable that will be reused in this method
	Connection conn=null;
	Statement query = null;
	ResultSet results = null;
	
	int projectId=0;  //this is the global variable b/c it will be needed for inserting the plots etc.
	int plotId=0;  //this is the global variable b/c it will be needed for inserting the plot observations etc.
	int plotObservationId=0; //this is the global variable b/c it will be needed for inserting taxon observations etc.
	int strataId=0; //this is the global variable b/c it will be needed for inserting taxon observations etc.
	int taxonObservationId=0;
	
	
	
	//get a database connection
	
	try {
	dbConnect m = new dbConnect();
	m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
	} catch (Exception e) {System.out.println("failed calling dbConnect.makeConnection call" + e.getMessage());}

	
	
	
	PreparedStatement pstmt=null;
	for (int ii=0; ii<addressNum; ii++) 
	{
		//check for the project name and insert project info to the database
		
		if (address[ii] != null && address[ii].startsWith("project.projectName")) {
		System.out.println(dataString[ii]);	
		
		
			try {
	
		//grab the next project_id in the project table in a future version check to see if this value
		//already exists and if so do an update instead of an insert
		plotWriter g =new plotWriter();  
		g.getNextId(dataString[ii], "project");
	
		projectId=g.outNextId;  //grab the returned value for the project id number
		
		
		//insert the data into the project table;
			pstmt=conn.prepareStatement(
			"INSERT INTO project "+
			" (project_ID, projectName, Description) " +
			"VALUES (?, ?, ?)");
			//bind the values to the statement
			pstmt.setInt(1, projectId);
			pstmt.setString(2, dataString[ii]);
			pstmt.setString(3, dataString[ii+1]);
			
			
			//do the insertion
			pstmt.execute();
			pstmt.close();
			//conn.close();
			
			} //end try
		catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to insert project data: " + e.getMessage());}
		
		
		}//end if
		
		
		//insert the plot information
		if (address[ii] != null && address[ii].startsWith("plot.authorPlotCode")) {
			
			try {	
				
			//grab the next value in the plot table
			plotWriter g =new plotWriter();  
			g.getNextId(dataString[ii], "plot");
	
			
			plotId=g.outNextId;  //grab the returned value
		
			
			
			pstmt=conn.prepareStatement(
			"INSERT INTO plot "+
			" (PLOT_ID, project_id, authorPlotCode, parentPlot) " +
			"VALUES (?, ?, ?, ?)");
			//bind the values to the statement
			pstmt.setInt(1, plotId);
			pstmt.setInt(2, projectId);
			pstmt.setString(3, dataString[ii]);
			pstmt.setString(4, dataString[ii+1]);
			
			
			//do the insertion
			pstmt.execute();
			pstmt.close();
			
			System.out.println(dataString[ii]);
		
			}//end try
			catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to insert plot data: " + e.getMessage());}
		
		}//end if
		
		
		//insert the plotObservationData
		if (address[ii] != null && address[ii].startsWith("plotObservation.previousObs")) {
			
			try {	
				
			//grab the next value in the plotObservation table
			plotWriter g =new plotWriter();  
			g.getNextId(dataString[ii], "plotObservation");
	
			
			plotObservationId=g.outNextId;  //grab the returned value
		
			
			
			pstmt=conn.prepareStatement(
			"INSERT INTO plotObservation "+
			" (Obs_ID, parentPlot, authorObsCode) " +
			"VALUES (?, ?, ?)");
			//bind the values to the statement
			pstmt.setInt(1, plotObservationId);
			pstmt.setInt(2, plotId);
			pstmt.setString(3, dataString[ii+1]);
			
			
			//do the insertion
			pstmt.execute();
			pstmt.close();
			
			System.out.println(dataString[ii]);
		
			}//end try
			catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to insert plot observation data: " + e.getMessage());}
		
		}//end if
		
		
		//insert the strata
		if (address[ii] != null && address[ii].startsWith("strata.stratumType")) {
			
			try {	
				
			//grab the next value in the strata table
			plotWriter g =new plotWriter();  
			g.getNextId(dataString[ii], "strata");
	
			
			strataId=g.outNextId;  //grab the returned value
		
			
			
			pstmt=conn.prepareStatement(
			"INSERT INTO strata "+
			" (strata_id, OBS_ID, stratumType, stratumCover, stratumHeight) " +
			"VALUES (?, ?, ?, ?, ?)");
			//bind the values to the statement
			pstmt.setInt(1, strataId);
			pstmt.setInt(2, plotObservationId);
			pstmt.setString(3, dataString[ii]);  
			pstmt.setString(4, dataString[ii+1]); //i'll have to take a look at how this works because this should be a integer value
			pstmt.setString(5, dataString[ii+1]);
			
			
			//do the insertion
			pstmt.execute();
			pstmt.close();
			
			System.out.println(dataString[ii]);
		
			}//end try
			catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to insert plot observation data: " + e.getMessage());}
		
		}//end if
		
		
		
		
		//insert the taxonObservation
		if (address[ii] != null && address[ii].startsWith("taxonObservation.authNameId")) {
			
			try {	
				
			//grab the next value in the strata table
			plotWriter g =new plotWriter();  
			g.getNextId(dataString[ii], "taxonObservation");
	
			
			taxonObservationId=g.outNextId;  //grab the returned value
		
			
			
			pstmt=conn.prepareStatement(
			"INSERT INTO taxonObservation "+
			" (taxonobservation_id, OBS_ID, authorNameId, originalAuthority) " +
			"VALUES (?, ?, ?, ?)");
			//bind the values to the statement
			pstmt.setInt(1, taxonObservationId);
			pstmt.setInt(2, plotObservationId);
			pstmt.setString(3, dataString[ii]);  
			pstmt.setString(4, dataString[ii+1]); 
			
			
			//do the insertion
			pstmt.execute();
			pstmt.close();
			
			
			//in this try statement the strataComposition table should be updated
			//because in this version evrytime a taxon is recognized in a different strata it should 
			//be recorded so every taxon has beneath it a strataType and percentCoverage which should be put in
			//the strataCompositionTable
			
			System.out.println("the starta type for the above taxon: "+ dataString[ii+2]);
			System.out.println(dataString[ii+3]);
			
			//get the required information for updating the stratumComposition table
			plotWriter gs =new plotWriter();  
			gs.getStrataComposition(plotObservationId,  dataString[ii+2]);
			
			int returnStrataId=gs.outStrataId;
			int strataCompositionId=gs.outStrataCompositionId;
			
			System.out.println(returnStrataId+" * "+strataCompositionId);
			
			pstmt=conn.prepareStatement(
			"INSERT INTO strataComposition "+
			" (strataComposition_Id, taxonobservation_id, strata_Id, cheatStratumType, percentcover) " +
			"VALUES (?, ?, ?, ?, ?)");
			//bind the values to the statement
			pstmt.setInt(1, strataCompositionId);
			pstmt.setInt(2, taxonObservationId);
			pstmt.setInt(3, returnStrataId);
			pstmt.setString(4, dataString[ii+2]);
			pstmt.setString(5, dataString[ii+3]); 
			
			
			//do the insertion
			pstmt.execute();
			pstmt.close();
			
		
			}//end try
			catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to insert plot observation data: " + e.getMessage());}
		
		}//end if
		
		
		
		
		
		
		
		
	//remeber to close the connection here
	}  //end for

	
	
	}



	/**
	* general method to tokenize a string of DB table.address|data strings into two seperate strings called dbAddress and 
	* attributeData.  There is a bug here: there are some strings included in the output that have nulls and have to be stripped 
	* in the above method - I can not figure out what is the problem
	*/
	
	private void setAttributeAddress (String[] combinedString, int combinedStringNum) {
	
	
	try {
		int count=0;
		for (int ii=0; ii<combinedStringNum; ii++) { 
	
			if (combinedString[ii].indexOf("|")>0 && combinedString[ii].trim() != null ) {  //make sure to tokenize an appropriate string
			//System.out.println(combinedString[ii]);
			StringTokenizer t = new StringTokenizer(combinedString[ii].trim(), "|");  //tokenize the string to get the requyired address
			//count++;
				if (t.hasMoreTokens())	{	//make sure that there are more tokens or else replace with the null value 
					dbAddress[count]=t.nextToken();
				}				//capture the required address
				else {dbAddress[count]="-999.25";}
				
				
				if (t.hasMoreTokens() )	{	//make sure that there are more tokens or else replace with the null value (-999)
					attributeData[count]=t.nextToken();
				} 				//capture the attributeString
				else {attributeData[count]="-999.25";}  //do the replacement
			
			
			//System.out.println(dbAddress[count]+" "+attributeData[count]+" "+ii+" "+count);
			
			}//end if
			count++;
	dbAddressNum=count;
			
		}//end for

	
	} //end try
	catch ( Exception e ){System.out.println("failed at: plotWriter.setAttributeAddress  "+e.getMessage());}
	
	}  //end method
	
	
	/**
	* method to return the next integer for an index from a table
	* in future versions this should be lumped with a switch that 
	* will warn if there is a repetative value
	*/
	
	
private void getNextId (String projectName, String tableName) {
	
	try {
	//Define the variable that will be reused in this method
	Connection conn=null;
	Statement query = null;
	ResultSet results = null;
	
	//get a database connection
	
	try {
	dbConnect m = new dbConnect();
	m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
	}//end try 
	catch (Exception e) {System.out.println("failed calling dbConnect.makeConnection call" + e.getMessage());}

	
	
	
	//this is a skeleton, the method just returns the next id value but in a future 
	//revision will look to see if the project exists and will return some warning
	
	try {	
		//results =query.executeQuery("SELECT project_id from  project where projectName like '%"+projectName+"%'");
		
		ResultSet lastValue=query.executeQuery("SELECT count(*) from "+tableName+"");
			
			int lastNum=0;
                        while (lastValue.next()) {
				lastNum = lastValue.getInt(1);
				}
                        lastNum++;
                        outNextId=lastNum;
			
			conn.close();
			query.close();
			

	} //end try
	catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to query the next id value in a table data: " + e.getMessage());}
	
		
		
	} //end try
	catch ( Exception e ){System.out.println("failed at: plotWriter.getProjectId  "+e.getMessage());}
		


}  //end method
		

		
/**
* a method to get the required information for the strataComposition table which is an intersection between
* that strata and the taxonObservation take as input the taxonObservationId, and stratumType to gain the output
* containing strataCompositionId, strataId - this table should be denormalized to include stratum type
*/
		
private void getStrataComposition (int observationId, String stratumType) {
	
	
	try {
	//Define the variable that will be reused in this method
	Connection conn=null;
	Statement query = null;
	ResultSet results = null;
	
	//get a database connection
	
	try {
	dbConnect m = new dbConnect();
	m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
	}//end try 
	catch (Exception e) {System.out.println("failed calling dbConnect.makeConnection call" + e.getMessage());}

	//query the starta table to get the strata_id
	try {	
		int strataId=0;
		System.out.println("passed values: "+observationId+" "+stratumType);
		ResultSet strataIdResult=query.executeQuery("SELECT strata_id from strata where OBS_ID = "+observationId+"and stratumType like '%"+stratumType+"%'");
			
			
                        while (strataIdResult.next()) {
				strataId = strataIdResult.getInt(1);
				}
                        
                        
			System.out.println("returned startaId: "+strataId);
			
			outStrataId=strataId;  
			
			//now get the last index/integer in the strataComposition table
			
			plotWriter g =new plotWriter();  
			g.getNextId("strataComposition", "strataComposition");
	
			
			
			outStrataCompositionId=g.outNextId;  //grab the returned value
			System.out.println("strataCompositionId: "+outStrataCompositionId);
		
			
			
			conn.close();
			query.close();
			

	} //end try
	catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to query the next id value in a table data: " + e.getMessage());}
	
		
		
	} //end try
	catch ( Exception e ){System.out.println("failed at: plotWriter.getProjectId  "+e.getMessage());}
		
	
	
}//end method	
	
	
	
	
	public String dbAddress[] =new String[100000];
	public String attributeData[] =new String[100000];
	public int dbAddressNum;

	public int outNextId;  // this is the value returned from teh getProjectId method
	
	public int outStrataId; //this is the return value from the getStrataCampositionId method
	public int outStrataCompositionId; //this is the return value from the getStrataCampositionId method
	
	
}

