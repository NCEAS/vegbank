import java.io.IOException;
import java.io.*;
import java.util.*;



/**
* This class will take an xml file containg query attributes
* see plotQuery.dtd and compose/issue a query to the plots
* database based on those attributes contained within the
* input xml document
*/

public class dbQuery {


public static void main(String[] args) {
        if (args.length != 3) {
		//queryXML should be the xml document containing the query attributes
		//queryXSL is the XSLT sheet for transforming the queryXML into attribute.Name
		//action is, at this point in time, verify - to verify the document, and query
		//which will formulate the query and pass it to the databas
            System.out.println("Usage: java dbQuery  [queryXML] [queryXSL] [action ..verify or query]");
            System.exit(0);
        }  //end if


//input xml file for loading to the database
String inputXml=args[0];
String inputXSL=args[1];
String action=args[2];



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



if (action.equals("query")) {

/**
* pass the array to the sql mapping class
*/

sqlMapper w =new sqlMapper();
w.developPlotQuery(transformedString, transformedStringNum);

}  //end if

if (action.equals("verify")) {


for (int ii=0; ii<transformedStringNum; ii++) 
	{
		System.out.println(transformedString[ii]);	
	}

}


} //end try

catch( Exception e ) {System.out.println(" failed in: dbLoader.main  "+e.getMessage());}



    }

}


