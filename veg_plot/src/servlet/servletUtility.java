import java.io.IOException;
import java.io.*;
import java.util.*;


/**
* Class that acts as a utility to the plotQuery servlet
*/


public class servletUtility {
	
	

/**
*  Method to store html code that can be accessed based on the requests
* made by the user 
*/

public void htmlStore () {
ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

//compose a very simple html page to dispaly that the user query is being
//handled by the servlet engine
String mainPage="<html> \n"
+"<body> \n"
+"<head> \n"
+"<title> plotServe - Query Engine for the National Plots Database </title> \n"
+"</head>  \n"
+"<body bgcolor=\"white\"> \n"
+"<table border=\"0\" width=\"100%\"> \n"
+"<tr bgcolor=\"#9999CC\"><td>&nbsp;<B><FONT FACE=\"arial\" COLOR=\"FFFFFF\" SIZE=\"-1\"> "
+"plotServe - Query Engine for the National Plots Database "
+"</FONT></B></td></tr> \n"
+"</table> \n"
+" \n"
+"<br><i><small> \n"
+rb.getString("requestparams.servletPosition")+"<br> \n"
+"<br></i></small> \n"
+"<p> \n"
+"<A HREF=\"http://www.nceas.ucsb.edu/collab/2180/docs/plotQuery.html\">"
	+"<B><FONT SIZE=\"-1\" FACE=\"arial\">Back to the query mechanism</FONT></B></A> \n"
+"<br></i> \n"
+"<P> \n"
+"<table border=\"0\" width=\"100%\"> \n"
+"<tr bgcolor=\"#9999CC\"><td>&nbsp;<B><FONT FACE=\"arial\" COLOR=\"FFFFFF\" SIZE=\"-1\"> "
+" "
+"</FONT></B></td></tr> \n"
+"</table> \n"
+"<br><br> \n"
+"</body> \n"
+"</html> \n";
outString = mainPage;

}


	
/**
*  Method to store html code that can be accessed based on the requests
*  made by the user 
*/

public void getViewOption () {
ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
String response="<html> \n"
+"<body> \n"
+"<head> \n"
+"<form action=\""  //these should be together
+"viewData\"  \n"  //this is the servlet name
+"method=POST> \n"
+"<input type=\"submit\" value=\"view plots\" /> \n"
+"</form> \n"
+"</body> \n"
+"</html> \n";
outString=response;
}



	
/**
*  Method to store html code showing the end user the options for interacting
*  with the data that was in the result set
*/

public void getViewMethod () {
ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
String response="<html> \n"
+"<body> \n"
+"<head> \n"
+"<form action=\""  //these should be together
+"viewData\"  \n"
+"method=POST> \n"
+"<input type=\"submit\" name=\"formatType\" value=\"view Summary\" /> \n"
+"<input type=\"submit\" name=\"formatType\" value=\"view Species List\" /> \n"
+"<input type=\"submit\" name=\"formatType\" value=\"download data\" /> \n"
+"<A HREF=\"http://beta.nceas.ucsb.edu:8080/examples/servlet/plotQuery\">"
+"<br>"
+"<B><FONT SIZE=\"-1\" FACE=\"arial\">Back to Query</FONT></B></A>"
+"</form> \n"
+"</body> \n"
+"</html> \n";
outString=response;
}
public String outString = null;	



} //end class	

