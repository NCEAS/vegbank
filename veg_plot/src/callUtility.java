import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

//test class to call the getUniqueArray function
public class callUtility {
public static void main(String[] args) {

try {
String infileArray[]=new String[100000];
BufferedReader in = new BufferedReader(new FileReader("testFile"), 8192);
int lineNum=0;
String s=null;

while ( (s=in.readLine()) != null ){

	infileArray[lineNum]=s;
	lineNum++;

} //end while 

 
utility m = new utility();
m.getUniqueArray(infileArray, lineNum); 

for (int ii=0; ii<m.outArrayNum; ii++) {System.out.println(m.outArray[ii]);}


}//end try	
catch( Exception e ) {System.out.println(" callUtility "+e.getMessage());e.printStackTrace();}
}
}

