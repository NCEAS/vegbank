import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public class taxonObservationQuery 
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


public void taxonObservationQuery()
{	
	outTaxonObservationId=null;
	outTaxonObservationNum=0;
	
	outObservationId=null;
	outAuthorTaxonName=null;
	outOriginalAuthority=null;
	outCumStrataCoverage=null;


}

public void getTaxonName(String input_s)
{



try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect call" + e.getMessage());}

	

try {
System.out.println(input_s);
	results = query.executeQuery("select TAXONOBSERVATION_ID, OBS_ID,  AUTHORNAMEID, ORIGINALAUTHORITY, CUMSTRATACOVERAGE from taxonObservation  where  AUTHORNAMEID like  '%"+input_s+"%'");
	int i=0;
		while (results.next()) {
			int taxonObservationId = results.getInt("TAXONOBSERVATION_ID");
			outTaxonObservationId[i]=taxonObservationId;
		
			int observationId=results.getInt("OBS_ID");
			outObservationId[i]=observationId;

			String authorTaxonName=results.getString("AUTHORNAMEID");
			outAuthorTaxonName[i]=authorTaxonName;
			
			String originalAuthority=results.getString("ORIGINALAUTHORITY");
			outOriginalAuthority[i]=originalAuthority;

			String cumStrataCoverage=results.getString("CUMSTRATACOVERAGE");
			outCumStrataCoverage[i]=cumStrataCoverage;
			
			i++;
		} //end while
		outTaxonObservationNum=i;
	

	//System.out.println("closed DB connections: usageQuery.getNameId");
	query.close();
	conn.close();

	} // end try 
	catch ( Exception e ){System.out.println("Query failed circumQuery.gettaxonObservationId  "+e.getMessage());}

}

  // these variables can be accessed from the calling program
public int outTaxonObservationId[]=new int[1000];
public int outTaxonObservationNum;

public int outObservationId[]=new int[1000];
public String outAuthorTaxonName[]=new String[1000];
public String outOriginalAuthority[]=new String[1000];
public String outCumStrataCoverage[]=new String[1000];


}
