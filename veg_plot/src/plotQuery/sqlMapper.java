import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

/**
* This class will appear as a 'black box' class where inputs 
* will be a transformed xml document string with some query/insert/update
* attributes and possibly a second xml transformed xml document string 
* with some criteria and SQL statements, which if some or all
* of the criteria are 'met' by the first document the SQL
* will be passed to another class to be issued to the database
*
*  This current version just takes the first query xml document
* and determines the sql code which is passed to the issueSQL
* class to be issued to the database 
*
* desire is to have a relatively universal query engine
* where the user can make an xml document that specifies 
* the development of a sql query in this class
*/

public class  sqlMapper
{

/**
*
* This method will take as input a queryElement (like taxonName, namedPlace, communityType etc)
* or a grouping of these elements, and depending on these elements will build a sql query which
* will be passed to another class and issued to the database which will pass back a result set
* The design of this class has not yet been finished and at this point this method is just 
* a skeleton that allows the plot query module to work.  It is envisioned that a future version
* of this class will take the aforementioned input plus a transformed xml string containing a set
* of criteria and sql statements, that if the criteria are met then the sql statements are
* passed to sql issuing class
*/


//accept the transformed xml document as a string and number of rows in the array
public void developPlotQuery(String[] transformedString, int transformedStringNum)
{

//pass the inputString to the tokenizer method below
sqlMapper a =new sqlMapper();
a.setAttributeAddress(transformedString, transformedStringNum);	
	
//grab the results
String element[]=a.element;  // the returned database address string
String value[]=a.value;  // the returened data string
int elementNum=a.elementNum; //the number of addresses and attributes in the returned set
		
	
	
//check to see the type of the query attributes passed 
//and depending, assign a sql query to the query
for (int i=0;i<transformedStringNum; i++) {
	
	if (element[i] != null && element[i].startsWith("queryElement")) {
		
		if (value[i] != null && value[i].startsWith("taxonName")) { //if this and the above are true then trigger sql stmt below
		System.out.println("bad");
		
		
		String action="select";
		String statement="select PLOT_ID, AUTHORPLOTCODE, Project_ID from PLOT where PLOT_ID in"+
		"(select PARENTPLOT  from PLOTOBSERVATION  where OBS_ID in"+
		"(select OBS_ID from TAXONOBSERVATION where AUTHORNAMEID like '%"+value[i+1]+"%'))";
		String returnFields[]=new String[3];	
		returnFields[0]="PLOT_ID";
		returnFields[1]="AUTHORPLOTCODE";
		returnFields[2]="Project_id";
		int returnFieldLength=3;


		/**
		* Call the issueSelect method which will return an arry with the return
		* values
		*/

		issueStatement j = new issueStatement();
		j.issueSelect(statement, action, returnFields, returnFieldLength);	


		//grab the returned array and print to the screen
		for (int ii=0;ii<j.outReturnFieldsNum; ii++) {System.out.println(j.outReturnFields[ii]);}
		
		}  //end if
	} //end if
	
	
	//System.out.println(transformedString[i]);

	
} //end for
} //end method



/**
* general method to tokenize a string of type: col1|col2 into two seperate strings called element and 
* value
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
					element[count]=t.nextToken();
				}				//capture the required element
				else {element[count]="-999.25";}
				
				
				if (t.hasMoreTokens() )	{	//make sure that there are more tokens or else replace with the null value (-999)
					value[count]=t.nextToken();
				} 				//capture the attributeString
				else {value[count]="-999.25";}  //do the replacement
			
			
			System.out.println(element[count]+" "+value[count]+" "+ii+" "+count);
			
			}//end if
			count++;
	elementNum=count;
			
	}//end for

	
} //end try
catch ( Exception e ){System.out.println("failed at: sqlMapper.setAttributeAddress  "+e.getMessage());}
	
}  //end method


public String queryOutput[] = new String[10000];  //the output from the issue sql can be mapped to this varaiable
int queryOutputNum; //the number of output rows from the issue sql is mapped to this varable

public String element[] = new String[100];  //store the element  - like "queryElement" and "elementString"
public String value[] = new String[100];  //store the values associated with the element like "taxonName" and "Pinus"
public int elementNum=0;
		

} //end class
