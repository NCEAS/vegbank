import java.io.IOException;
import java.io.*;
import java.util.*;




public class dbLoader {


public static void main(String[] args) {
        if (args.length != 3) {
		//later expand the input arguments to include at least an action for either update or load
		//curently action can be insert or verify where insert actually inserts the dataset to the database
		//and verify actually writes the db address and datastring to the screen
            System.out.println("Usage: java transformWrapper  [dataXML] [dataXSL] [action]");
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



if (action.equals("insert")) {

/**
* pass the array to the plot writer to be inserted into the database
*/

plotWriter w =new plotWriter();
w.insertPlot(transformedString, transformedStringNum);

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


