import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

/**
* This class will take, as input, a sql statement string and an action,
* either select, insert or update and issue the query to the database 
* In the future this, or a related, class will issue the sql statements
* for the plots database query and load modules 
*/


public class  issueStatement
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


/**
* Main method for testing - sends a rather generic statement to the
* plots database
*/

public static void main(String[] args) 
{

	
//Below are the input formats required by the setStatement method

String action="select";
String statement="select PLOT_ID, AUTHORPLOTCODE, Project_ID from PLOT where PLOT_ID in"+
	"(select PARENTPLOT  from PLOTOBSERVATION  where OBS_ID in"+
	"(select OBS_ID from TAXONOBSERVATION where AUTHORNAMEID like '%Pinus%'))";
String returnFields[]=new String[3];	
returnFields[0]="PLOT_ID";
returnFields[1]="AUTHORPLOTCODE";
returnFields[2]="Project_id";
int returnFieldLength=3;



/**
* Call the issueSelect method which will return an arry with the return
* values
*/

issueStatement i = new issueStatement();
i.issueSelect(statement, action, returnFields, returnFieldLength);	


//grab the returned array and print to the screen
for (int ii=0;ii<i.outReturnFieldsNum; ii++) {System.out.println(i.outReturnFields[ii]);}

}  //end main method


/**
* This method will take the action, target table, number of satatements and the variable name/data as input and compose 
* statements to be passed to the database.
*/

public void issueSelect(String inputStatement, String inputAction, String[] inputReturnFields, int inputReturnFieldLength)
{

/**
* establish the database connection and statement
*/
try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect.makeConnection call" + e.getMessage());}

	
try {
/**
* compose and issue a prepared statement for loading a table
*/	

if (inputAction.equals("select")) {

//statement query=null;

results = query.executeQuery(inputStatement);

outReturnFieldsNum=0;
while (results.next()) {
	
	String resultLine="";
	for (int i=0;i<inputReturnFieldLength; i++) {			
			String buf = results.getString(inputReturnFields[i]);
			resultLine=resultLine.trim()+" "+buf.trim();
	}

	//System.out.println(resultLine);
	
	outReturnFields[outReturnFieldsNum]=resultLine;
	outReturnFieldsNum++;
	
}  //end while

query.close();
conn.close();

} //end if			
} // end try 
	catch ( Exception e ){System.out.println("failed at issueStatement.issueSelect "+e.getMessage());}
}	
	

	
/**
*  This method will take an insert statement and related attributes
*  and will issue an insert statement  - currently just a skeleton
*/	

public void issueInsert(String inputStatement, String inputAction)
{

/**
* establish the database connection and statement
*/
try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect.makeConnection call" + e.getMessage());}
	
	
	
try {

if (inputAction.equals("insert")) {
PreparedStatement pstmt=null;
String stringPiece="INSERT INTO test";
String attributeName="col1";
String tableName="test";
String attributeList="col1, col2";
String attribute="col";

//create the insertion string
String insertionString=inputAction;
//insertionString=insertionString+" INTO "+inputTargetTable;

//assume there to be only one statement to begin with
			pstmt=conn.prepareStatement(
			//"INSERT INTO "+tableName+" "+
			""+insertionString+" "+
			" ("+attribute+") " +
			"VALUES (?)");
			//bind the values to the statement
			pstmt.setString(1, "value");
			
			
			//do the insertion
			pstmt.execute();			
	
	
	query.close();
	conn.close();

} //end if	
	} // end try 
	catch ( Exception e ){System.out.println("Query failed sqlToDB.setStatement "+e.getMessage());}

}


public String outReturnFields[] = new String[10000];
public int outReturnFieldsNum;


}
