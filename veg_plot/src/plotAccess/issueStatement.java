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
* plots database depending on the action provided at the command line
*/


public static void main(String[] args) 
{


	
if (args.length != 1) {
            System.out.println("Usage: java issueStatement  [action] <--Either select or insert");
            System.exit(0);
        }  //end if

String action=args[0];	

if (action.trim().equals("select")) {
	
//Below are the input formats required by the setStatement method

//String action="select";
String statement="select PLOT_ID, AUTHORPLOTCODE, Project_ID from PLOT where PLOT_ID in"+
	"(select PARENTPLOT  from PLOTOBSERVATION  where OBS_ID in"+
	"(select OBS_ID from TAXONOBSERVATION where AUTHORNAMEID like '%a%'))";
String returnFields[]=new String[3];	
returnFields[0]="PLOT_ID";
returnFields[1]="AUTHORPLOTCODE";
returnFields[2]="Project_id";
int returnFieldLength=3;



/**
* Call the issueSelect method which will return an array with the return
* values
*/

issueStatement i = new issueStatement();
i.issueSelect(statement, action, returnFields, returnFieldLength);	


//grab the returned array and print to the screen
for (int ii=0;ii<i.outReturnFieldsNum; ii++) {System.out.println(i.outReturnFields[ii]);}

} // end if


if (action.trim().equals("insert")) {

System.out.println("sending a test insert statement to database");

String insertString="INSERT INTO NAMEDPLACE";
String attributeString="namedplace_id, placeName";
String valueString="VALUES (?, ?)";
int inputValueNum=2;
String inputValue[]=new String[2];	
inputValue[0]="30";
inputValue[1]="VENTURA";

//String insertString , String attributeString, String valueString int inputValueNum, String[] inputValue)

issueStatement j = new issueStatement();
j.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue);	
	
} //end if	

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
*  and will issue an insert statement
*/	

public void issueInsert(String insertString , String attributeString, String valueString, int inputValueNum, String[] inputValue)
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

PreparedStatement pstmt=null;


//assume there to be only one statement to begin with
			pstmt=conn.prepareStatement(
			
			""+insertString+" "+
			" ("+attributeString+") " +
			""+valueString+"");
			
			
			//bind the values to the statement
			//pstmt.setString(1, "005");
			
			//bind the values to the statement	
			for (int i=0; i<inputValueNum; i++) {			
				System.out.println(i+1+" "+inputValue[i]);
				pstmt.setString(i+1, inputValue[i]);
			}
			
			//do the insertion
			pstmt.execute();			
	
	
	query.close();
	conn.close();
	
	} // end try 
	catch ( Exception e ){System.out.println("Query failed issueStatement.insert method "+e.getMessage());}

}



/**
* method to return a valueString from an input of an integer representing
* the number of database attributes that are being used in the insertion
* prepared statemet 
*/
	
public void getValueString (int valueNum) {
	
String valueString="VALUES (";
	for (int i=0; i<valueNum-1; i++) {
		valueString=valueString+"?, ";
	}
valueString =valueString+"?)";

outValueString=valueString;

	
}  //end method

public String outValueString;
public String outReturnFields[] = new String[10000];
public int outReturnFieldsNum;


}
