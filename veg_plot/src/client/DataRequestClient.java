/**
 *
 *
 * Purpose: A class that acts as a client to a servlet by requesting data
 * from that servlet
 * Copyright: 
 * Authors: John Harris
 *
 */

package client;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.sql.*;
import java.net.*;

public class DataRequestClient {

//this is the typical url to be sent
//http://64.160.26.222:8080/examples/servlet/wellQuery?
//requestDataType=wellCurve&wellName=1&assetName=any&minDate=&maxDate=&curveName=any&multipleObs=any&wellType=any&resultType=summary

//main method for testing
public static void main (String args[]) {
	
//make a url
//String url ="http://64.160.26.222:8080/examples/servlet/wellQuery?"
//	+"requestDataType=wellCurve&wellName=1&assetName=any&minDate=&maxDate="
//	+"&curveName=any&multipleObs=any&wellType=any&resultType=summary";
	
//request the data 
//System.out.println( requestURL(url) );


//grab the url components
String servlet = ":8080/examples/servlet/wellQuery";
String protocol = "http://";
String host = "64.160.26.222";

Properties parameters = new Properties();
parameters.setProperty("wellName", "%");
parameters.setProperty("requestDataType", "productionData");

//String parameterString = "?exchangeType=upload&submitter=harris&"
//	  	+"password=jasmine&file=../test.dat";

//request the data
System.out.println( requestURL(servlet, protocol, host, parameters) );

}


/**
 *  Method to request data from a servlet
 *
 *  @param url a string that represents the entire url like:
 *   http://64.160.26.222:8080/examples/servlet/wellQuery?minDate=1998;
 *
 */
public static String requestURL(String servlet, String protocol, String host, 
	Properties parameters) {



StringBuffer buf = new StringBuffer();
Enumeration names = parameters.propertyNames();
while (names.hasMoreElements()) {
	String name = (String)names.nextElement();
	String value = parameters.getProperty(name);
    buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
    if (names.hasMoreElements()) buf.append("&");
}

String server = protocol + host + servlet +"?"+ buf.toString();

//System.out.println(requestURL(server));
String output = requestURL(server);
return output;


}




/**
 *  Method to request data from a servlet
 *
 *  @param url a string that represents the entire url like:
 *   http://64.160.26.222:8080/examples/servlet/wellQuery?minDate=1998;
 *
 */
public static String requestURL(String url) {

StringBuffer buf = new StringBuffer();
String thisLine ="";
URL u;
URLConnection uc;

//Open the URL for reading
try {
	u = new URL(url); //convert the string to a url
	
	try {
		uc = u.openConnection();
		//uc.setRequestProperty("ENCTYPE", "multipart/form-data");
		//System.out.println("***"+uc.getHeader());
		//System.out.println("***"+uc.getContentEncoding());
	  
		//now turn the URLConnection into a BufferedReader
		BufferedReader theHTML = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		  
		try {
			while ((thisLine = theHTML.readLine()) != null) {
				buf.append(thisLine+"\n");
				//System.out.println(thisLine);
			}
          
		} catch (Exception e) {System.err.println(e);}
	
	} catch (Exception e) {System.err.println(e);}

} catch (MalformedURLException e) {System.err.println(url + " is not a parseable URL");
	System.err.println(e);}

return buf.toString();
}


}
