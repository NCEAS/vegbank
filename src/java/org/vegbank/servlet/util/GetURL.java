package org.vegbank.servlet.util;
/**
 *	Name: GetUrl.java
 *  Purpose: Used for Java servlet/applet/application communication
 *    with servlet. Based on code given in the book
 *		"Java Servlet Programming" by Hunter & crawford and
 *
 *     '$Author: farrell $'
 *     '$Date: 2003-02-26 19:16:35 $'
 *     '$Revision: 1.1 $'
 *
 */

import java.net.*;
import java.util.*;
import java.io.*;
//import servlet.util.HttpMessage;

public class GetURL 
{ 
	public Hashtable responseVec = new Hashtable();
	public Vector responseVector = new Vector();
	
	/** Main method for testing etc */
	public static void main(String[] args) 
	{
		
	GetURL g =new GetURL();
	
	//if zero arguments then get the response 
	//from the default url
	if (args.length == 0) 
	{
		//THIS USES THE GET POST METHOD
		String urlString="/framework/servlet/framework?action=coordinateTransform&returnformattype=html&x=4555&y=23553&zone=12";
		int port = 80;
		String requestType = "GET";
		g.getPost(urlString, port, requestType);
	
	
		//THIS USES THE REQUESTURL METHOD
		String servlet = "/framework/servlet/framework?action=coordinateTransform&returnformattype=html&x=4555&y=23553&zone=12";
		String protocol = "http://";
	  String host = "vegbank.nceas.ucsb.edu";
		String s = GetURL.requestURL(protocol+host+servlet);
		System.out.println("GetURL > output: " + s);
		
		//THIS USES THE OTHER REQUEST URL METHOD
		servlet = "/framework/servlet/framework";
		Properties parameters = new Properties();
		parameters.setProperty("action", "coordinateTransform");
		parameters.setProperty("returnformattype", "xml");
		parameters.setProperty("x", "4555");
		parameters.setProperty("y", "23553");
		parameters.setProperty("zone", "12");
		s = GetURL.requestURL(servlet,  protocol, host, parameters);
		System.out.println("GetURL > output: " + s);
		
		
	}
	else
	{
		String urlString = new String(args[0]); //url starting with http://
		int port = Integer.parseInt(args[1]); //port number
		String requestType=new String(args[2]); //GET or POST  
		//g.getPost(urlString, port, requestType);
		
		//this uses the requestURL class
		String servlet = "/harris/servlet/DataRequestServlet?requestDataType=vegPlot&queryType=extended&"
		 +"resultType=summary&operator=eq&criteria=state&value=WY&operator=gt&"
		 +"criteria=elevation&value=10500";
		String protocol = "http://";
	  String host = "dev.nceas.ucsb.edu";
		
		Properties parameters = new Properties();
		parameters.setProperty("test", "test");
		
		//request the data from the servlet and print the results to the system
		System.out.println( requestURL(servlet, protocol, host, parameters) );
	}
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
	* EXAMPLES: 
 	* String servlet = ":8080/examples/servlet/DataRequestServlet";
	* String protocol = "http://";
	* String host = "127.0.0.1";
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
				//System.out.println("GetURL > url header: "+uc.getHeader() );
				System.out.println("GetURL > url encoding:"+uc.getContentEncoding());
	  
				//now turn the URLConnection into a BufferedReader
				BufferedReader theHTML = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		  
				try 
				{
					while ((thisLine = theHTML.readLine()) != null) 
					{
						buf.append(thisLine+" ");
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
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	//	System.out.println(buf.toString() );
	return buf.toString();
	}



/**
 * Method to make a url connection and return the contents using as input the 
 * url, port number and requestType
 *
 */
	public void getPost (String uri, int port, String requestType) 
	{
		StringTokenizer tok = new StringTokenizer(uri); 
		String protocol = tok.nextToken(":"); 
		String host = tok.nextToken(":/"); 
		//Hashtable responseVec = new Hashtable();
		System.out.println("GetURL > uri originally passed: "+uri);
		if (tok.hasMoreTokens()) 
		{  
			uri =  tok.nextToken(" ") + "/";
		}
		else 
		{
			uri = "/"; 
		}
		//System.out.println("original uri passed: "+uri);
		//  JHH - removed the last forward slash if one exists with at least one element in 
		//  URI  - marked by a '?' b/c can't figure why would it would be needed
		if ( (uri.indexOf("?") > 0) && (uri.endsWith("/")) ) 
		{
			int uriLength=uri.length();
			int newLength=uriLength-1;
			String correctedUri=uri.substring(0, newLength);
			uri=correctedUri;
			System.out.println("GetURL > abnormal forward slash at "
			+"the end of the uri - trimmed");
		}
		System.out.println("GetURL > Protocol=" + protocol + ", host=" + host + ", uri=" + uri); 
		// send the request using GET or post depending on the input
		try 
		{ 
			Socket clientSocket = new Socket(host, port); 
			PrintStream outStream = new PrintStream(clientSocket.getOutputStream()); 
			
			BufferedReader buffReader
				= new BufferedReader( new InputStreamReader( clientSocket.getInputStream()) );
			
			outStream.println(requestType+" " + uri + " HTTP/1.0\n"); 
			String line; 
			int lineCnt=1;
			while ((line = buffReader.readLine()) != null) 
			{
				//add the results to the vector
      	responseVector.addElement(line);
      	//System.out.println("GetURL > server response: " + line);
				//and add to the hashtable too
      	responseVec.put(new Integer(lineCnt), line);
      	lineCnt++;
			}
		} //end try 
		catch(IOException ioe) 
		{ 
			System.out.println("IOException:" + ioe); 
		} 
		catch(Exception e) 
		{ 
			System.out.println("Exception: " + e.getMessage()); 
			e.printStackTrace(); 
		} 
	}

	/**
	 * overloaded method from above
	 */
	 
	 




}


