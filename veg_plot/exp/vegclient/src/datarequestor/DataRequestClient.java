/**
 *
 *
 * 	Purpose: This class acts as a client for requesting data from either a 
 *	servlet (for remote databases) on in the case of a local database this 
 *	client will take part in the data request mechanism
 *
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-09 23:22:15 $'
 * 	'$Revision: 1.1 $'
 *
 *
 */
package vegclient.datarequestor;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.sql.*;
import java.net.*;

import vegclient.datarequestor.*;

public class DataRequestClient 
{
	//this is the hash table that is to be passed to the DataRequestHandler for
	// local queries on the embedded database 
	public Hashtable queryElementHash = new Hashtable();

	//this is a string buffer that will be used to pass back the results to the
	// interface
	public StringBuffer results = new StringBuffer();


	/**
	 * main method for testing
	 */
	public static void main (String args[]) 
	{
		if (args.length !=1 )
		{
			System.out.println( "USAGE: java DataRequestClient [databaseType] {local, remote}");
		}
		else 
		{
			String databaseType = args[0];
			if (databaseType.equals("remote"))
			{
				System.out.println("contacting DataRequestServlet: ");
				
				//this is a test to request data from a servlet at NCEAS
				String servlet = "/harris/servlet/DataRequestServlet";
				String protocol = "http://";
				String host = "dev.nceas.ucsb.edu";
				
				//set the required parameters for the remotely hosted servlet
				Properties parameters = new Properties();
				parameters.setProperty("taxon", "166");
				parameters.setProperty("requestDataType", "vegPlot");
				parameters.setProperty("queryType", "simple");
				parameters.setProperty("resultType", "summary");

				//mimic the above properties in a hash for the local query
				Hashtable queryHash = new Hashtable();
				queryHash.put("taxon","Fraxinus dipetala");
				queryHash.put("requestDataType","vegPlot");
				queryHash.put("resultType","summary");
				//make the blank stringbuffer
				StringBuffer sb = new StringBuffer();
				
				//request the data from the servlet and print the results to the system
				System.out.println( requestURL(servlet, protocol, host, parameters) );
				
			}
			else if ( databaseType.equals("local") )
			{
				//this is a test to request data from a servlet at NCEAS
				//make the url components
				String servlet = ":8080/examples/servlet/DataRequestServlet";
				String protocol = "http://";
				String host = "127.0.0.1";

				Properties parameters = new Properties();
				parameters.setProperty("taxon", "Fraxinus dipetala");
				parameters.setProperty("requestDataType", "vegPlot");

				//mimic the above properties in a hash for the local query
				Hashtable queryHash = new Hashtable();
				queryHash.put("taxon","Fraxinus dipetala");
				queryHash.put("requestDataType","vegPlot");
				queryHash.put("resultType","summary");
				//make the blank stringbuffer
				StringBuffer sb = new StringBuffer();


				//request the data from the servlet and print the results to the system
				System.out.println( requestURL(servlet, protocol, host, parameters) );
		
				//now make the same request of the local embedded database
				DataRequestHandler drh = new DataRequestHandler();
				drh.doGet(queryHash, sb);
				}
				else
				{
					System.out.println( "USAGE: java DataRequestClient [databaseType] {local, remote}");
				}
			}
	}


	/**
	 *  Method to create a hash table used to store the the input parametes that will 
	 *  be passed the DataRequestHandler which mimics those data that are passed as 
	 *  input parameters to the DataRequestServlet so like: taxonName Pinus etc
	 *
	 *
	 * @param element -- this can be any elemnt that can be passed to the 
	 *		DataRequestHandler in the hash table including queryElements (like
	 *		taxonName, state etc..) or resultType (like summary or full), outFile etc
	 * @param value -- this is the value that corresponds to the above element
	 *
	 */
	public void  createQueryElementHash (String element , String value ) 
	{
		try 
		{ 
			//put the input into a hash table
			queryElementHash.put(element,value);
		} 
		catch (Exception e) 
		{
			System.out.println("failed in " 
			+"DataRequestClient.createQueryElementHash");
			System.err.println(e);
		}
	}



	/**
	 *  Method to request data from the local embedded database through the 
	 * 	DataRequestHandler
	 *
	 * @param parms -- the hash table with the parameters need for querying the
	 * 		databse by the DataRequesthandler
	 * @param results -- the results in the form of a string buffer
	 *
	 */
	public void  queryLocalDatabase (Hashtable params) 
	{
		try 
		{ 
			//pass the paramters to the DataRequestHandler
			DataRequestHandler drh = new DataRequestHandler();
  	  drh.doGet(params, results);
			//grab the results from the query (DataRequestHandler 
			//which has stored it as a string buffer
			results = drh.results;
		} 
		catch (Exception e) 
		{
			System.out.println("failed in " 
			+"DataRequestClient.queryLocalDatabase");
			System.err.println(e);
		}
	}

	
	
	/**
	 *  Method to request data from a servlet and then write the data to 
	 * a file on the local file system.  This method works with the overloaded
	 * method having the same name
	 *
	 *  @param url a string that represents the entire url like:
	 *   http://64.160.26.222:8080/examples/servlet/DataRequestServlet?taxonName=a
	 *
	 * @param servlet -- name of the servlet
	 * @param protocol -- type of protocol
	 * @param host -- host name or IP address of the server
	 * @param parameters -- the parameters to be passed to the servlet
	 * @param fileName -- the name of the file in which the response is written
	 * 		include the path!
	 */
	public static String writeURLResponseToFile(String servlet, String protocol, String host, 
	Properties parameters, String fileName) 
	{
		StringBuffer buf = new StringBuffer();
		Enumeration names = parameters.propertyNames();
		while (names.hasMoreElements()) 
		{
			String name = (String)names.nextElement();
			String value = parameters.getProperty(name);
  		buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
    	if (names.hasMoreElements()) 
			{
				buf.append("&");
			}
		}
		String server = protocol + host + servlet +"?"+ buf.toString();
		//System.out.println(requestURL(server));
		String output = writeURLResponseToFile(server, fileName);
		return output;
	}
	
	
	/**
	 *  Method to request data from a servlet using an amalgamated string like :
	 * 	http://64.160.26.222:8080/examples/servlet/DataRequestServlet?taxonName=a
	 *
	 *  @param url -- the url string like protocol+host+port+servlet+properties
	 *	@param fileName -- the file on the local filesystem where the response
	 * 		should be written
	 *
	 */
	public static String writeURLResponseToFile(String url, String fileName) 
	{
		StringBuffer buf = new StringBuffer();
		String thisLine ="";
		URL u;
		URLConnection uc;
		//Open the URL for reading
		try 
		{
			u = new URL(url); //convert the string to a url
			try 
			{
				uc = u.openConnection();
				//uc.setRequestProperty("ENCTYPE", "multipart/form-data");
				//System.out.println("***"+uc.getHeader());
				//System.out.println("***"+uc.getContentEncoding());
	  
				//now turn the URLConnection into a BufferedReader
				BufferedReader theHTML = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				//set up the output to file
				PrintStream out = new PrintStream(new FileOutputStream(fileName, false));
		  	try 
				{
					String line = null;
					while ((thisLine = theHTML.readLine() ) != null) 
					{
						line = thisLine.trim();
						out.println(line+"\n");
						//buf.append(thisLine+"\n");
						//System.out.println(thisLine);
					}
         } 
				 catch (Exception e) 
				 {
				 	System.err.println(e);
				 }
				} 
				catch (Exception e) 
				{
					System.err.println(e);
				}
			} 
			catch (MalformedURLException e) 
			{
				System.err.println(url + " is not a parseable URL");
				System.err.println(e);
			}
			return buf.toString();
		}
	
	
	


	/**
	 *  Method to request data from a servlet
	 *
	 *  @param url a string that represents the entire url like:
	 *   http://64.160.26.222:8080/examples/servlet/DataRequestServlet?taxonName=a
	 *
	 * @param servlet -- name of the servlet
	 * @param protocol -- type of protocol
	 * @param host -- host name or IP address of the server
	 * @param parameters -- the parameters to be passed to the servlet
	 *
	 */
	public static String requestURL(String servlet, String protocol, String host, 
	Properties parameters) 
	{
		StringBuffer buf = new StringBuffer();
		Enumeration names = parameters.propertyNames();
		while (names.hasMoreElements()) 
		{
			String name = (String)names.nextElement();
			String value = parameters.getProperty(name);
  		buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
    	if (names.hasMoreElements()) 
			{
				buf.append("&");
			}
		}
		String server = protocol + host + servlet +"?"+ buf.toString();
		//System.out.println(requestURL(server));
		String output = requestURL(server);
		return output;
	}


	/**
	 *  Method to request data from a servlet using an amalgamated string like :
	 * 	http://64.160.26.222:8080/examples/servlet/DataRequestServlet?taxonName=a
	 *
	 *  @param url -- the url string like protocol+host+port+servlet+properties
	 *
	 */
	public static String requestURL(String url) 
	{
		StringBuffer buf = new StringBuffer();
		String thisLine ="";
		URL u;
		URLConnection uc;
		//Open the URL for reading
		try 
		{
			u = new URL(url); //convert the string to a url
			try 
			{
				uc = u.openConnection();
				//uc.setRequestProperty("ENCTYPE", "multipart/form-data");
				//System.out.println("***"+uc.getHeader());
				//System.out.println("***"+uc.getContentEncoding());
	  
				//now turn the URLConnection into a BufferedReader
				BufferedReader theHTML = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		  	try 
				{
					while ((thisLine = theHTML.readLine()) != null) 
					{
						buf.append(thisLine+"\n");
						//System.out.println(thisLine);
					}
         } 
				 catch (Exception e) 
				 {
				 	System.err.println(e);
				 }
				} 
				catch (Exception e) 
				{
					System.err.println(e);
				}
			} 
			catch (MalformedURLException e) 
			{
				System.err.println(url + " is not a parseable URL");
				System.err.println(e);
			}
			return buf.toString();
		}
}
