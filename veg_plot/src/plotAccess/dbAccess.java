import java.io.IOException;
import java.io.*;
import java.util.*;



/**
* This class will take an xml file containg either query attributes
* or plot data and depending on the document type, either
* insert, update or query the database - see the diagram 
* "plotAccessArchitecture.vsd" to better understand the 
* relation of this class to database access module.
*
* 1] For database queries:
* queryXML should be the xml document containing the query attributes
* queryXSL is the XSLT sheet for transforming the queryXML into attributeType and 
* attributeString action is, at this point, verify - to transform the document, 
* and query which will formulate the query and pass it to the database
* 
* 2] for database insertion:
* dataXML should be the xml document containing plot and/or plant
* data.  dataXSL is the XSLT sheet for transforming the dataXML file into 
* databaseAddress and dataValue and the current usable action is: verify
* (same as above) and insert which will insert the plot data to the 
* database
*
* 3] for database update:
* at this point there is no update capabilities, although it will
* allow the user to update the database, based on a plot xml 
* document containing only partial data from a plot 
*/
public class dbAccess {

//main method for testing
public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java dbQuery  [XML] [XSL] [action]");
            System.exit(0);
        }  //end if


//input xml file for loading to the database
String inputXml=args[0];
String inputXSL=args[1];
String action=args[2];

//call the public method

dbAccess g =new dbAccess();  
g.accessDatabase(inputXml, inputXSL, action);


} //end main method




/**
*  Public interface for running the plotAccess module, this is how the
* interfaces and servlets will load and query the database
* input: xml file, may be datafile or query file, xsl sheet to
* transform the document and action, either insert or query
*/
public void accessDatabase (String inputXml, String inputXSL, String action) {

try {

/**
* call the method to transform the data xml document and pass back a string writer  
*/

transformXML m = new transformXML();
m.getTransformed(inputXml, inputXSL);  //call the method

StringWriter transformedData=m.outTransformedData;  //the stringwriter containg all the transformed data


/**
* pass the String writer to the utility class to convert the StringWriter to an array
*/

utility u =new utility();
u.convertStringWriter(transformedData);

String transformedString[]=u.outString;  // the string from the utility.convertStringWriter
int transformedStringNum=u.outStringNum; // the number of vertical elements contained in the string array




//query action
if (action.equals("query")) {

/**
* pass the array to the sql mapping class
*/

	sqlMapper w =new sqlMapper();
	w.developPlotQuery(transformedString, transformedStringNum);

}  //end if

//insert action
if (action.equals("insert")) {

/**
* pass the array to the plot writer to be inserted into the database
*/

	plotWriter w =new plotWriter();
	w.insertPlot(transformedString, transformedStringNum);

}  //end if


//verify action
if (action.equals("verify")) {


	for (int ii=0; ii<transformedStringNum; ii++) 
	{
		System.out.println(transformedString[ii]);	
	}

}
} //end try

catch( Exception e ) {System.out.println(" failed in: dbLoader.main  "+e.getMessage());}

} //end method



}


