/**
 *
 *
 * 	Purpose: The class acts as a the installer of the vegclient software
 *
 * @author 
 * @version 
 *
 */
package vegclient.installer;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.sql.*;
import java.net.*;

import vegclient.installer.*;

public class Installer 
{
	//this is the hash table that is to be passed to the DataRequestHandler for
	// local queries on the embedded database 
	public Hashtable queryElementHash = new Hashtable();

	//this is a string buffer that will be used to pass back the results to the
	// interface
	public StringBuffer results = new StringBuffer();
	private ClientFramework framework = new ClientFramework();
	
	//variables used in multiple loactions in this class
		String servlet = "/vegclass/servlet/InstallerServlet";
		String protocol = "http://";
		String host = "beta.nceas.ucsb.edu";

	/**
	 * the constructor method
	 */
	public Installer()
	{
		
	}
	
	
	/**
	 * main method for testing
	 */
	public static void main (String args[]) 
	{
		Installer install = new Installer();
		if (args.length !=1 )
		{
			System.out.println( "USAGE: java Installer [source] {local, remote}");
		}
		else 
		{
			String databaseType = args[0];
			if (databaseType.equals("remote"))
			{
				System.out.println("contacting InstallerServlet: ");
				install.submitEmailAddress("harris@nceas.ucsb.edu");
				install.getCygwinPackage();
				install.getVegClientPackage();
			}
				else
				{
					//install.extractPackage("cygwin");
					//install.extractPackage("vegClient");
					//install.initCygwinInstall();
					//install.initDatabase();
					//System.load("C://cygwin/home/harris/installer/cygwin1.dll");
					install.cygwinCopy();
				}
			}
	}

	
	
	/**
	 * method that initiates the database system
	 */
	 public void initDatabase()
	 {
		 try 
		{
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("DatabaseManager.exe init");
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			while ((response=in.readLine()) !=null) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				String buf = t.nextToken();
				System.out.println(buf);
		}

		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
		}
	 }
	
	/**
	 * method to submit an email address to the InstallServlet
	 */
	 public void submitEmailAddress(String emailAddress)
	 {
			Properties parameters = new Properties();
			parameters.setProperty("emailAddress", emailAddress);
			requestURL(servlet, protocol, host, parameters);
			System.out.println("submitting to server email address: "+emailAddress);
	 }
	
	/**
	 * method that executes the cygwins startup -- using the setup.exe installer
	 * that can be found at redhat.com
	 *
	 */
	public void initCygwinInstall()
	{
		try 
		{
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("./setup.exe");
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			while ((response=in.readLine()) !=null) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				String buf = t.nextToken();
				System.out.println(buf);
				//out.println(response);
		} //end while

		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
		}
	}
	
	
	
	
	
	
	/**
	 * method that allows retrieves the cygwin installer and associated 
	 * packages from the InstallerServlet
	 */
	public void getCygwinPackage()
	{
		//set the required parameters for the remotely hosted servlet
		Properties parameters = new Properties();
		parameters.setProperty("packageType", "cygwin");
		writeURLResponseToFile(servlet, protocol, host, parameters, "cygwin.jar");
	}
	
		
	/**
	 * method that extracts the contents of a package -- either
	 * the vegClientPackage of the cygwin package
	 * @param packageName -- can be vegClient or cygwin
	 */
	 public void extractPackage(String packageName, String directory)
	 {
		 System.out.println("extracting: "+packageName+" in directory: "+directory);
	 		try 
			{
				System.out.println("Experimental");
				Process listener;
				DataInputStream listenerStream;
			
				String envp[] = {""}; 
			  String s = directory;
			  File path = new File(s);
			
				listener = Runtime.getRuntime().exec("jar.exe -xvf vegClient.jar", envp, path);
			
				//listener = Runtime.getRuntime().load("cygwin1.dll").exec("cp test bad");
				System.out.println(" issued ");
				listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
				BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
				String response = null;
	
				while ((response=in.readLine()) !=null) 
				{
					StringTokenizer t = new StringTokenizer(response, "	\t");
					String buf = t.nextToken();
					System.out.println(buf);
				}

			}
			catch (Exception e) 
			{
				System.out.println("Exception " 
				+ e.getMessage());
			}
	 }
	
	
	/**
	 * method that extracts the contents of a package -- either
	 * the vegClientPackage of the cygwin package
	 * @param packageName -- can be vegClient or cygwin
	 */
	 public void extractPackage(String packageName)
	 {
		 String fileName = null;
		 if (packageName.equals("cygwin") )
		 {
			  fileName = "cygwin.jar";
		 }
		 else if (packageName.equals("vegClient") )
		 {
			 fileName = "vegClient.jar";
		 }
		 else
		 {
			 System.out.println("unkown package type: "+ packageName);
		 }
			
		try 
		{
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("jar -xvf "+fileName);
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			while ((response=in.readLine()) !=null) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				String buf = t.nextToken();
				System.out.println(buf);
				//out.println(response);
		} //end while

		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
		}
	 }
	
	/**
	 * method that allows retrieves the vegClient software
	 */
	public void getVegClientPackage()
	{
		//set the required parameters for the remotely hosted servlet
		Properties parameters = new Properties();
		parameters.setProperty("packageType", "vegClient");
		writeURLResponseToFile(servlet, protocol, host, parameters, "vegClient.jar");
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
	 * method that hadles the copying of an installation package to a directory
	 * on the file system
	 *
	 * @param packageName -- the package name
	 * @param directory -- the directory
	 */
	 public void copyPackage(String packageName , String directory)
	 {
		 if ( packageName.trim().equals("vegClient") )
		 {
			 framework.fileCopy( "vegClient.jar", directory+"/vegClient.jar", "append");
		 }
		 System.out.println("copying package: "+ packageName +" to directory: "+ directory);
	 }
	 
	 
	 
	 
	 /**
	  * method that copies a file using the cygwin library -- this is a test
		* method and will not be used and will be deleted soon
		*/
	 public void cygwinCopy()
	 {
	 
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
				BufferedInputStream in = new BufferedInputStream((uc.getInputStream()));
				//set up the output to file
				FileOutputStream out = new FileOutputStream(fileName, false);
		  	
				try 
				{
						byte[] buf1 = new byte[4 * 1024]; //4K buffer
						int i = in.read(buf1);
						while( i  != -1 ) 
						{
							out.write(buf1, 0, i);
							i = in.read(buf1);
						}
						out.close();
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
