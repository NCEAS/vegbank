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

public static void main(String[] args) {
if (args.length != 3) {
	System.out.println("Usage: java dbAccess  [XML] [XSL] [action] \n"
		+"version: Feb 2001 \n \n"
		+"actions:  query compoundQuery insert insertPlot verify simpleCommunityQuery");
		System.exit(0);
}  //end if


//input xml file for loading to the database
String inputXml=args[0];
String inputXSL=args[1];
String action=args[2];

//call the public method

dbAccess g =new dbAccess();  
g.accessDatabase(inputXml, inputXSL, action);

//print the results to the System out
for (int ii=0; ii<g.queryOutputNum; ii++) {
	System.out.println("printing this from dbAccess.main "+g.queryOutput[ii]);
}

} //end main method




/**
 *  Public interface for running the plotAccess module, this is how the
 * interfaces and servlets will load and query the database
 * input: xml file, may be datafile or query file, xsl sheet to
 * transform the document and action, either insert or query
 * @param inputXml - input xml file
 * @param inputXsl - input xsl transform sheet
 * @param action - database action
 *
 */
public void accessDatabase (String inputXml, String inputXSL, String action) {
try {

//call the method to transform the data xml document and pass back a string writer  
transformXML m = new transformXML();
m.getTransformed(inputXml, inputXSL);

//the stringwriter containg all the transformed data
StringWriter transformedData=m.outTransformedData;  

//pass to the utility class to convert the StringWriter to an array
utility u =new utility();
u.convertStringWriter(transformedData);

String transformedString[]=u.outString; 
int transformedStringNum=u.outStringNum; 



//query action
if (action.equals("query")) {

	
	// pass the array to the sql mapping class - single attribute query
	sqlMapper w =new sqlMapper();
	w.developPlotQuery(transformedString, transformedStringNum);
	
	//grab the results from the sqlMapper class
	queryOutput=w.queryOutput;
	queryOutputNum=w.queryOutputNum;
	
}

//compound query action
if (action.equals("compoundQuery")) {

	
	//pass the array to the sql mapping class - compound queries
	sqlMapper w =new sqlMapper();
	w.developCompoundPlotQuery(transformedString, transformedStringNum);
	
	
	//grab the results from the sqlMapper class
	queryOutput=w.queryOutput;
	queryOutputNum=w.queryOutputNum;
	
}



//insert action -- to insert a plot to the last database
if (action.equals("insert")) {

	//pass the array to the plot writer to be inserted into the database
	plotWriter w =new plotWriter();
	w.insertPlot(transformedString, transformedStringNum);

}


//insertPlot action -- this is to insert a plot to the most recent DB
if (action.equals("insertPlot")) {

	//pass the array to the plot writer to be inserted into the database
	PlotDBWriter w =new PlotDBWriter();
	w.insertPlot(transformedString, transformedStringNum, "entirePlot");
}



//verify action
if (action.equals("verify")) {

	for (int ii=0; ii<transformedStringNum; ii++) 
	{
		System.out.println(transformedString[ii]);	
	}

}

//simple community query action
if (action.equals("simpleCommunityQuery")) {

	{
		sqlMapper w =new sqlMapper();
		w.developSimpleCommunityQuery(transformedString, 
		transformedStringNum);
		
		//grab the results from the sqlMapper class
		queryOutput=w.queryOutput;
		queryOutputNum=w.queryOutputNum;
		
	}

}


//if unknown action
else {System.out.println("dbAccess.accessDatabase: unrecognized action: "
	+action);}
	
} //end try
catch( Exception e ) {System.out.println(" failed in: dbAccess.accessDatabase "
	+e.getMessage() );
	e.printStackTrace();
	}
}


public String queryOutput[] = new String[10000];  //the output from query
int queryOutputNum; //the number of output rows from the query

}


