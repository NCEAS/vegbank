import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public class usageQuery
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


public void usageQuery()
{	
	outNameUsageId=null;
	outNameUsageNum=0;
	outCircumId=null;
	
	outNameId=null;
	outNameNum=0;
	outPartyId=null;


	outStartDate=null;
	outStopDate=null;

	outAcceptence=null;
}

public void getNameId(int input_s)
{



try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect call" + e.getMessage());}

	

try {
	results = query.executeQuery("select NAMEUSAGEID, circum_id, name_id, party_id, startDate, stopdate, acceptence  from name_usage where name_id = "+input_s);
	int i=0;
		while (results.next()) {
			int name_usage_id = results.getInt("nameUsageid");
			outNameUsageId[i]=name_usage_id;
			
			int CircumId = results.getInt("circum_id");
                        outCircumId[i]=CircumId;

			int partyId=results.getInt("party_id");
                        outPartyId[i]=partyId;


			String startDate=results.getString("startDate");
                        outStartDate[i]=startDate;

                        String stopDate=results.getString("stopDate");
                        outStopDate[i]=stopDate;

			String accptence=results.getString("acceptence");
			outAcceptence[i]=accptence;

			i++;
		} //end while
	outNameUsageNum=i;
	

	//System.out.println("closed DB connections: usageQuery.getNameId");
	query.close();
	conn.close();

	} // end try 
	catch ( Exception e ){System.out.println("Query failed usageQuery.getNameId "+e.getMessage());}

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
        results = query.executeQuery("select  NAMEUSAGEID, circum_id, name_id, party_id, startDate, stopdate, acceptence  from name_usage where circum_id = "+input_s);
        int i=0;
                while (results.next()) {
                        int name_usage_id = results.getInt("nameUsageid");
                        outNameUsageId[i]=name_usage_id;

                        int CircumId = results.getInt("circum_id");
                        outCircumId[i]=CircumId;

			int nameId=results.getInt("name_id");
			outNameId[i]=nameId;

                        int partyId=results.getInt("party_id");
			outPartyId[i]=partyId;

			String startDate=results.getString("startDate");
			outStartDate[i]=startDate;

			String stopDate=results.getString("stopDate");
                        outStopDate[i]=stopDate;
			
			String accptence=results.getString("acceptence");
                        outAcceptence[i]=accptence;

			i++;
                } //end while
        outNameNum=i;


        //System.out.println("closed DB connections: usageQuery.getCircumId");
        query.close();
        conn.close();

        } // end try
        catch ( Exception e ){System.out.println("Query failed usageQuery.getCircumId "+e.getMessage());}

}




  // these variables can be accessed from the calling program
public int outNameUsageId[]=new int[1000];
public int outNameUsageNum;
public int outCircumId[]=new int[1000];


public int outNameId[]=new int[1000];
public int outNameNum;

public int outPartyId[]=new int[1000];

public String outStartDate[]=new String[1000];
public String outStopDate[]=new String[1000];

public String outAcceptence[]=new String[1000];


}
