import java.net.*;
import java.util.*;
import java.io.*;

/**
 *        Name: getUrl.java
 *     Purpose: Used for Java servlet/applet/application communication
 *              with servlet. Based on code given in the book
 *              "Java Servlet Programming" by Hunter & crawford and
 * 		8/96 Marty Hall, hall@apl.jhu.edu  
 *		http://www.apl.jhu.edu/~hall/java/ 
 *     Authors: John Harris
 *
 *  Fix:
 * port numbers, ending forward slash, cookies
 *
 */

//============================================================================
// Takes a URL as input and retrieves the file. Only handles the http 
// protocol. Does a direct connection rather than using the URLConnection 
// object in order to illustrate the use of sockets. 
// 
// 8/96 Marty Hall, hall@apl.jhu.edu 
// http://www.apl.jhu.edu/~hall/java/ 
//============================================================================

public class GetURL { 

/** Main method for testing etc*/
public static void main(String[] args) {

String urlString = new String(args[0]); //url starting with http://
int port =  Integer.parseInt(args[1]); //port number
String requestType=new String(args[2]); //GET or POST

GetURL g =new GetURL();  
g.getPost(urlString, port, requestType);

//print to sys out the results from the request
int hashSize=g.responseVec.size();
for (int i=0; i<=hashSize; i++) {
	
	String n= (String)g.responseVec.get(new Integer(i)); 
   	if (n != null) {
        	 System.out.println("* "+n);
	}

}
}//end method

/**
 * Method to make a url connection and return the contents using as input the 
 * url, port number and requestType
 *
*/

public void getPost (String uri, int port, String requestType) {
StringTokenizer tok = new StringTokenizer(uri); 
String protocol = tok.nextToken(":"); 
String host = tok.nextToken(":/"); 
//Hashtable responseVec = new Hashtable();

if (tok.hasMoreTokens()) { 
	uri = "/" + tok.nextToken(" ") + "/"; 
}

else 
	uri = "/"; 


/** 
*  JHH - removed the last forward slash if one exists with at least one element in 
*  URI  - marked by a '?' b/c can't figure why would it would be needed
*/

if ( (uri.indexOf("?")>0) && (uri.endsWith("/")) ) {
	int uriLength=uri.length();
	int newLength=uriLength-1;
	String correctedUri=uri.substring(0, newLength);
	uri=correctedUri;
	System.out.println("abnormal forward slash at the end of the uri - trimmed");
}


System.out.println("Protocol=" + protocol + ", host=" + host + ", uri=" + uri); 

/**
* send the request using GET or post depending on the input
*/

try { 
Socket clientSocket = new Socket(host, port); 
PrintStream outStream = new PrintStream(clientSocket.getOutputStream()); 
DataInputStream inStream = 
new DataInputStream(clientSocket.getInputStream()); 
outStream.println(requestType+" " + uri + " HTTP/1.0\n"); 
String line; 
int lineCnt=1;

while ((line = inStream.readLine()) != null) {
	//responseVec.put(line, new Integer(lineCnt));
	responseVec.put(new Integer(lineCnt), line);
	lineCnt++;
	//System.out.println("* " + line); 
}

} //end try 


catch(IOException ioe) { 
	System.out.println("IOException:" + ioe); 
} 
catch(Exception e) { 
	System.out.println(e.fillInStackTrace()); 
	System.out.println(e.getMessage()); 
	e.printStackTrace(); 
	System.out.println("Error! " + e); 
} 
}
public Hashtable responseVec = new Hashtable();

} 


