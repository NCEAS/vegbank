import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public class circumQuery
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


public void circumQuery()
{	
	outCircumId=null;
	outCircumNum=0;


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
	results = query.executeQuery("select circum_id, ref_id from CIRCUMSCRIPTION  where circum_id  = "+input_s);
	int i=0;
		while (results.next()) {
			int CircumId = results.getInt("circum_id");
			outCircumId[i]=CircumId;
			i++;
		} //end while
		outCircumNum=i;
	

	//System.out.println("closed DB connections: usageQuery.getNameId");
	query.close();
	conn.close();

	} // end try 
	catch ( Exception e ){System.out.println("Query failed circumQuery.getCircumId  "+e.getMessage());}

}

  // these variables can be accessed from the calling program
public int outCircumId[]=new int[1000];
public int outCircumNum;

}
