import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public class statusQuery 
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


public void statusQuery()
{	
	outStatusCircumId=null;
	outStatusNum=0;

	outStatus=null;

	outCircumId=null;


	outPartyId=null;

	outStartDate=null;
        outStopDate=null;

}

public void getCircumId(int input_s)
{



try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect call" + e.getMessage());}

	

try {
	results = query.executeQuery("select party_circum_id, party_id, status, startDate, stopDate  from status where circum_id  = "+input_s);
	int i=0;
		while (results.next()) {
			int statusCircumId = results.getInt("party_circum_id");
			outStatusCircumId[i]=statusCircumId;
			String status = results.getString("status");
			outStatus[i]=status;
			int partyId = results.getInt("party_id");
                        outPartyId[i]=partyId;			
			
			String startDate=results.getString("startDate");
                        outStartDate[i]=startDate;

                        String stopDate=results.getString("stopDate");
                        outStopDate[i]=stopDate;


			i++;
		
		} //end while
		outStatusNum=i;
	

	//System.out.println("closed DB connections: usageQuery.getNameId");
	query.close();
	conn.close();

	} // end try 
	catch ( Exception e ){System.out.println("Query failed statusQuery.getCircumId  "+e.getMessage());}

}







public void getStatusCircumId(int input_s)
{



try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
        conn=m.outConn;
        query=m.outStmt;
} catch (Exception e) {System.out.println("failed in the  dbConnect call" + e.getMessage());}



try {
        results = query.executeQuery("select circum_id, party_id, status, startDate, stopDate  from status where party_circum_id  = "+input_s);
        int i=0;
                while (results.next()) {
                        int circumId = results.getInt("circum_id");
                        outCircumId[i]=circumId;
                        String status = results.getString("status");
                        outStatus[i]=status;

			int partyId = results.getInt("party_id");
			outPartyId[i]=partyId;
                        
			String startDate=results.getString("startDate");
                        outStartDate[i]=startDate;

                        String stopDate=results.getString("stopDate");
                        outStopDate[i]=stopDate;

			i++;
                } //end while
                outStatusNum=i;


        //System.out.println("closed DB connections: usageQuery.getNameId");
        query.close();
        conn.close();

        } // end try
        catch ( Exception e ){System.out.println("Query failed statusQuery.getStatusCircumId  "+e.getMessage());}

}


  // these variables can be accessed from the calling program
public int outStatusCircumId[]=new int[1000];
public int outStatusNum;

public String outStatus[]=new String[1000]; 

public int outCircumId[]=new int[1000];

public int outPartyId[]=new int[1000];

public String outStartDate[]=new String[1000];
public String outStopDate[]=new String[1000];

}
