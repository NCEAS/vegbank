import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public class nameQuery
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


public void nameQuery()
{
	outName = null;	
	outNameId=null;
	outNameNum=0;

}

public void getName(String input_s)
{



try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect.makeConnection call" + e.getMessage());}

	

try {
	results = query.executeQuery("select name_id, name, symbol, commonname, family, AUTHORNAME  from name where name like '%"+input_s+"%'");
	int i=0;
		while (results.next()) {
			String name = results.getString("name");
			int name_id = results.getInt("name_id");
			outName[i]=name.trim();
			outNameId[i]=name_id;
			i++;
		} //end while
	outNameNum=i;
	

	//System.out.println("closed DB connections: nameQuery.getName");
	query.close();
	conn.close();

	} // end try 
	catch ( Exception e ){System.out.println("Query failed nameQuery.getName "+e.getMessage());}

}



public void getNameId(int  input_s)
{



try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
        conn=m.outConn;
        query=m.outStmt;
} catch (Exception e) {System.out.println("failed in the  dbConnect.makeConnection call" + e.getMessage());}



try {
        results = query.executeQuery("select name_id, name, symbol, commonname, family, AUTHORNAME  from name where name_id  = "+input_s);
        int i=0;
                while (results.next()) {
                        String name = results.getString("name");
                        int name_id = results.getInt("name_id");
                        outName[i]=name.trim();
                        outNameId[i]=name_id;
                        i++;
                } //end while
        outNameNum=i;


        //System.out.println("closed DB connections: nameQuery.getName");
        query.close();
        conn.close();

        } // end try
        catch ( Exception e ){System.out.println("Query failed nameQuery.getName "+e.getMessage());}

}







  // these variables can be accessed from the calling program
public String outName[]=new String[1000];
public int outNameId[]=new int[1000];
public int outNameNum;

}
