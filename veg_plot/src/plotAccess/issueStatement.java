import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

/**
 * This class will take, as input, a sql statement string and an action,
 * either select, insert or update and issue the query to the database 
 *
 *
 * @author John Harris
 */


public class  issueStatement
{

/*Global variables for the database connection*/
Connection conn=null;
Statement query = null;
ResultSet results = null;


public String outValueString; 
public String outReturnFields[] = new String[10000]; //array containg returned vals
public int outReturnFieldsNum;  //number of lines returned
public Vector returnedValues = new Vector(); //vector containg the same as above array



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
	for (int ii=0;ii<i.outReturnFieldsNum; ii++) {
		System.out.println(i.outReturnFields[ii]);
	}
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
 * This method will take a database action, target table, number of satatements and 
 * the variable name/data as input and compose statements to be passed to 
 * the database.  This method is nearly identical to the next which is an
 * overloaded version of this one that also uses as input a connection.  Thus
 * care should be taken when using this one - for if a connection cannot be made
 * the method fails
 *
 * @param inputStatement
 * @param inputAction
 * @param inputReturnFields
 * @param inputReturnFieldLength 
 * @param pconn - database connection
 *
 */

public void issueSelect(String inputStatement, String inputAction, 
	String[] inputReturnFields, int inputReturnFieldLength)
{

/**
* establish the database connection and statement
*/
try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	System.out.println("Opening db connection by calling dbConnect from \n"
	+"issueStatement.issueSelect");
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect.makeConnection call" + e.getMessage());}

try {

/**
* compose and issue a prepared statement for loading a table
*/	
if (inputAction.equals("select")) {

//execute the query
results = query.executeQuery(inputStatement);

outReturnFieldsNum=0;
//make a matrix to store the returned values because storing them directly
//in a string was giving a jdbc error that couldn't be fixed
String verticalStore[] = new String[inputReturnFieldLength];

//get all the levels returned
while (results.next()) {
	
	StringBuffer resultLine = new StringBuffer();
	//match return elements with correct column name
	for (int i=0;i<inputReturnFieldLength; i++) {
		
		//if the results is null then handle it below
		if (results.getString(inputReturnFields[i])==null) {
			resultLine=resultLine.append(" nullValue");
			verticalStore[i]="nullValue"; //populate the vertical storage array
		}

		else {			
			String buf =(results.getString(inputReturnFields[i]));
			resultLine.append(" ").append(buf.trim());
			verticalStore[i]=buf;	//populate the vertical storage array
		}
	
	}

	//grab the values out of the vertical store and stick them into a string
	//that is separated by pipes, this way all the data associated with a 
	//plot is on a sigle line which can be tokenized later and positioned
	//into an xml structure
	
	String returnedRow="";
	for (int ii=0;ii<verticalStore.length; ii++) {
			returnedRow=returnedRow+"|"+verticalStore[ii];
	}
	
	outReturnFields[outReturnFieldsNum]=returnedRow; //add to the array
	outReturnFieldsNum++;
	returnedValues.addElement(returnedRow); //add to the vector
	
}  //end while

/*assume that b/c a database connection was made here that it needs to be closed*/
query.close();
conn.close();

} //end if			
} //end try 
catch ( Exception e ){System.out.println("failed at issueStatement.issueSelect: "
+e.getMessage());e.printStackTrace();}
} //end method



/**
 * This method is the same as above except that it needs a db connection passed 
 * to it and it will not close the connection - it should be closed by the calling 
 * class where connection management should be accomplished.
 *
 * @param inputStatement
 * @param inputAction
 * @param inputReturnFields
 * @param inputReturnFieldLength 
 * @param pconn - database connection
 */

public void issueSelect(String inputStatement, String inputAction, 
	String[] inputReturnFields, int inputReturnFieldLength, Connection pconn)
{

/**
* transfer the database connection
*/
try {
 conn=pconn;
 query = conn.createStatement ();;	
} 

catch (Exception e) {
 System.out.println("failed at issueStatement.issueselect transfering "
	+" db connections" + e.getMessage());
}


try {

/**
* compose and issue a prepared statement for loading a table
*/	
if (inputAction.equals("select")) {

	//execute the query
	results = query.executeQuery(inputStatement);

	outReturnFieldsNum=0;
	//make a matrix to store the returned values because storing them directly
	//in a string was giving a jdbc error that couldn't be fixed
	String verticalStore[] = new String[inputReturnFieldLength];

	//get all the levels returned
	while (results.next()) {
	
		StringBuffer resultLine = new StringBuffer();
		//match return elements with correct column name
		for (int i=0;i<inputReturnFieldLength; i++) {
		
			//if the results is null then handle it below
			if (results.getString(inputReturnFields[i])==null) {
				resultLine=resultLine.append(" nullValue");
				verticalStore[i]="nullValue"; //populate the vertical storage array
			}

			else {			
				String buf =(results.getString(inputReturnFields[i]));
				resultLine.append(" ").append(buf.trim());
				verticalStore[i]=buf;	//populate the vertical storage array
			}
	
		}

		//grab the values out of the vertical store and stick them into a string
		//that is separated by pipes, this way all the data associated with a 
		//plot is on a sigle line which can be tokenized later and positioned
		//into an xml structure
	
		String returnedRow="";
		for (int ii=0;ii<verticalStore.length; ii++) {
			returnedRow=returnedRow+"|"+verticalStore[ii];
		}
		
		outReturnFields[outReturnFieldsNum]=returnedRow; //add to array
		outReturnFieldsNum++;
		returnedValues.addElement(returnedRow); //add to the vector
	
	}  //end while

/*don't close b/c connection was passed into the method*/
//query.close();
//conn.close();

} //end if			
} // end try 
catch ( Exception e ){System.out.println("failed at issueStatement.issueSelect: "
+e.getMessage());e.printStackTrace();}
} //end method

	

	
	
	
/**
 * This method will take an insert statement and related attributes
 * and will issue an insert statement to the database after making a database
 * connection using the dbConnect class.  This method is overloaded - see the next
 * method.  Use of this method should be considered carefully because the db
 * connection is made directly from the method and often the connection cannot
 * be made the next method uses a connection that is passed to it allowing for
 * connection pooling and connection reuse - a good thing
 *
 * @param insertString
 * @param attributeString
 * @param valueString
 * @param inputValueNum
 * @param inputValue 
 *
 */	

public void issueInsert(String insertString , String attributeString, 
	String valueString, int inputValueNum, String[] inputValue)
{

/**
* establish the database connection and statement
*/
try {
dbConnect m = new dbConnect();
m.makeConnection(conn, query);
	System.out.println("Opening db connection by calling dbConnect from \n"
	+"issueStatement.issueInsert");
	conn=m.outConn;
	query=m.outStmt;	
} catch (Exception e) {System.out.println("failed in the  dbConnect.makeConnection call" 
+e.getMessage());}
	
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
				//System.out.println(i+1+" "+inputValue[i]);
				pstmt.setString(i+1, inputValue[i]);
			}
			
			//do the insertion
			pstmt.execute();			
	/*assume that because made the connection above that it should be closed*/
	query.close();
	conn.close();
	} // end try 
	catch ( Exception e ){System.out.println("Query failed issueStatement.insert method "+e.getMessage());e.printStackTrace();}
	//catch ( Exception e ){System.out.println("Query failed issueStatement.insert method "+e.getMessage());}
} //end method


	
 /**
 * This method is the same as above except that it needs as input a database
 * connection - typically passed from a pool/cache and for this reason it is 
 * anticipated that the connection will be used again by the calling class and
 * thus the connection will not be clused in this method.
 *
 * @param insertString
 * @param attributeString
 * @param valueString
 * @param inputValueNum
 * @param inputValue 
 * @param conn - database connection
 */	

public void issueInsert(String insertString , String attributeString, 
	String valueString, int inputValueNum, String[] inputValue, Connection conn)
{
	
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
				//System.out.println(i+1+" "+inputValue[i]);
				pstmt.setString(i+1, inputValue[i]);
			}
			
			//do the insertion
			pstmt.execute();
			pstmt.close();
	//query.close();
	//conn.close();
	} // end try 
	catch ( Exception e ){System.out.println("Query failed issueStatement.insert method "+e.getMessage());e.printStackTrace();}
	//catch ( Exception e ){System.out.println("Query failed issueStatement.insert method "+e.getMessage());}
} //end method





/**
 * method to return a valueString from an input of an integer representing
 * the number of database attributes that are being used in the insertion
 * prepared statemet 
 *
 * @param valueNum - number of values
 *
 */
public void getValueString (int valueNum) {
	
String valueString="VALUES (";
for (int i=0; i<valueNum-1; i++) {
	valueString=valueString+"?, ";
}
valueString =valueString+"?)";
outValueString=valueString;
}  //end method




}
