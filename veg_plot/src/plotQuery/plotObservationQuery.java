import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public class plotObservationQuery
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


public void plotObservationQuery()
{	
	outPlotId=null;
	outPlotNum=0;
	
	//outObservationId=null;
	//outAuthorTaxonName=null;
	//outOriginalAuthority=null;
	//outCumStrataCoverage=null;


}

public void getPlotObservationId(int input_i)
{



try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect call" + e.getMessage());}

	

try {

	results = query.executeQuery("select  PARENTPLOT, OBS_ID  from plotObservation  where  OBS_ID  ="+input_i);
	int i=0;
		while (results.next()) {
			int plotId = results.getInt("PARENTPLOT");
			outPlotId[i]=plotId;
			System.out.println(plotId);

		
			//int observationId=results.getInt("OBS_ID");
			//outObservationId[i]=observationId;

			//String authorTaxonName=results.getString("AUTHORNAMEID");
			//outAuthorTaxonName[i]=authorTaxonName;
			
			//String originalAuthority=results.getString("ORIGINALAUTHORITY");
			//outOriginalAuthority[i]=originalAuthority;

			//String cumStrataCoverage=results.getString("CUMSTRATACOVERAGE");
			//outCumStrataCoverage[i]=cumStrataCoverage;
			
			i++;
		} //end while
		outPlotNum=i;
	

	//System.out.println("closed DB connections: usageQuery.getNameId");
	query.close();
	conn.close();

	} // end try 
	catch ( Exception e ){System.out.println("Query failed circumQuery.gettaxonObservationId  "+e.getMessage());}

}

  // these variables can be accessed from the calling program
public int outPlotId[]=new int[1000];
public int outPlotNum;

//public int outObservationId[]=new int[1000];
//public String outAuthorTaxonName[]=new String[1000];
//public String outOriginalAuthority[]=new String[1000];
//public String outCumStrataCoverage[]=new String[1000];


}
