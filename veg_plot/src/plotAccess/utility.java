import java.io.IOException;
import java.io.*;
import java.util.*;


/**this class will contain multi-purpose utilities for dealing with plot data, at this point these 
* may include a utility to convert a StringWriter to a string array, and number of elements 
* in the array structure
*/

public class utility {

public void utility() 
{
/*variable to make available to the calling class -- the string containg the 
db location (table.attribute) and the data*/
        
outString  = null;  //string containg the contents from a converted StringWriter
outStringNum = 0;  //integer representing the number of elements in the above string array
}



/**
*  this method will take, as input a StringWriter object and return a String Array 
* and the number of vertical elements in the array structure
*/

public void convertStringWriter  (StringWriter inputStringWriter)
{


try {


/*convert the stringwriter to a string*/
String transformedString=null;  // a string inwhich to convert the String Writer to
PrintWriter pw = new PrintWriter(inputStringWriter);
transformedString  = inputStringWriter.toString().trim();  //do the conversion to the string


int stringContentsNum = 0;  // number of vertical elements in the array
String stringContents[] =new String[100000];  //the array to contain the contents of the string
BufferedReader br = new BufferedReader(new StringReader (transformedString)); //speed up the string parsing with a buffered reader


//read each line
String line; // temporary string to contain the lines from the transformedData 
int lineCnt=0; //running line counter
while ((line = br.readLine()) !=null ) {
	stringContents[lineCnt]=line.trim();  //write the lines to the local array
	outString[lineCnt]=line.trim();  //write the lines directly to the public array
	lineCnt++;  //increment the line
} //end while

stringContentsNum=lineCnt;  //number of elements in the array
System.out.println("number of lines transformed: "+lineCnt);


outStringNum=stringContentsNum;


} //end try

catch( Exception e ) {System.out.println(" failed in: utility "+e.getMessage());}
}//end method


    
    
/**
* This method will take an array as input and check to see if there are any
* redundant levels in the array and if there are redundancies will remove them
* returning to the calling class a unique array
*/

public void getUniqueArray (String[] inputArray, int inputArrayNum)
{

try { 
int uniqueSwitch=0;  //switch is one if redundant otherwise unique is zero
String uniqueArray[]=new String[100000];
String inputArrayValue=null;
String uniqueArrayValue=null;
int uniqueArrayNum=0;

for (int i=0; i<=inputArrayNum; i++) { 
	
	
	//load the first value into the unique array
	if (i==0) {
			uniqueArray[uniqueArrayNum]=inputArray[i];
			uniqueArrayNum++;
			//System.out.println("first value: "+inputArray[i]);
	}
	
	//check for redundancies
	if (i>0) {
		
		//make sure that the array value is not null then grab the value
		if (inputArray[i] != null) {
			inputArrayValue=inputArray[i].trim();
		}
		uniqueSwitch=0;  //make swich 0 for new level
	
		for (int ii=0; ii<uniqueArrayNum; ii++) {  //pass thru all uniq vals
			uniqueArrayValue=uniqueArray[ii].trim();
						
			if (uniqueArrayValue.equals(inputArrayValue)) {
				uniqueSwitch=1;
				break;
			}//end if
	
		}//end for
		if (uniqueSwitch==0) {
			uniqueArray[uniqueArrayNum]=inputArrayValue;
			uniqueArrayNum++;
		}
	}//end if
	
} //end for 

//pass to calling class
outArray=uniqueArray;
outArrayNum=uniqueArrayNum;
	

} //end try
catch( Exception e ) {System.out.println(" failed in: utility.getUniqueArray: "+
	e.getMessage());e.printStackTrace();}
} //end method
public String outArray[]=new String[100000];
public int outArrayNum;

    
  
  
    
public String outString[] =new String[100000];
public int outStringNum;

}


