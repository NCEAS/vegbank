package servlet.util;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.*;


import servlet.util.*;


/**
 * Class that acts as a utility to the plotQuery servlet
 *
 * @version 
 * @author 
 *
 */



public class ServletUtility 
{

	private GetURL gurl = new GetURL();
	public Vector outVector;
	public int vecElementCnt;
	
  /**
   * creates an object of a type className. this is used for instantiating
   * plugins.
   */
  public static Object createObject(String className) 
                throws InstantiationException, 
                       IllegalAccessException,
                       ClassNotFoundException
  {
    Object object = null;
    try 
    {
        Class classDefinition = Class.forName(className);
        object = classDefinition.newInstance();
    } 
    catch (InstantiationException e) 
    {
        throw new InstantiationException("Error instantiating plugin: " + e);
    } 
    catch (IllegalAccessException e) 
    {
        throw new IllegalAccessException("Error accessing plugin: " + e);
    } 
    catch (ClassNotFoundException e) 
    {
        throw new ClassNotFoundException("Plugin " + className + " not " +
                                         "found: " + e);
    }
    return object;
  }
	
	
	/**
   * method that will retrive the authentication results
	 * from an 'AuthenticationServlet' using as input a 
	 * username and password -- it is expected that a number
	 * of the inter-servlet communication practices will require
	 * authentication and this utility method will be used 
	 *
	 * @param userName
	 * @param passWord
	 * @param authType -- this is the desired authentication type desired
	 * 	{ uploadFile, loginUser }
	 * @return -- a string containing either:
	 * 	<authentication>true</auentication>
	 * 	<cookieName>name</cookieName>
	 * 	<cookieValue>value</cookieValue>
	 * 	or
	 * 	<authentication>false</auentication>
	 *
   */
  public String httpAuthenticationHandler( String userName, String passWord, String authType)
  {
    String htmlResults = null;
    try
    {
      //create the parameter string to be passed to the DataRequestServlet -- 
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?userName="+userName+"&password="+passWord+"&authType="+authType);
			
      //connect to the authentication servlet
			String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/authenticate"+sb.toString().trim();
			System.out.println("OUT PARAMETERS: "+uri);
      int port=80;
      String requestType="POST";
      htmlResults = gurl.requestURL(uri);
    }
    catch( Exception e )
    {
      System.out.println("** failed :  "
      +e.getMessage());
    }
    return(htmlResults);
  }
	
	
	
	
	/**
	 * method to stick the parameters from the client 
	 * into a hashtable and then pass it back to the 
	 * calling method
	 */
	public Hashtable parameterHash (HttpServletRequest request) 
	{
		Hashtable params = new Hashtable();
		try 
		{
			Enumeration enum =request.getParameterNames();
			//System.out.println("servletUtility 'parameterHash' contacted ");
 			while (enum.hasMoreElements()) 
			{
				String name = (String) enum.nextElement();
				String values[] = request.getParameterValues(name);
				if (values != null) 
				{
					for (int i=0; i<values.length; i++) 
					{
						params.put(name,values[i]);
					}
				}
 			}
		}
		catch( Exception e ) 
		{
			System.out.println("** failed in:  "
			+" first try - reading parameters "
			+e.getMessage());
		}
		return(params);
	}
	
	
	
	


/**
 *  Method to copy a file
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 */
public void flushFile (String inFile) 
{
	try
	{
		(new File(inFile)).delete();
		//inFile.delete(); 
		//PrintStream out  = new PrintStream(new FileOutputStream(inFile, true));
	}
	catch(Exception e) 
	{
		System.out.println("failed: servletUtility.flushFile");
		e.printStackTrace();
	}
}




/**
 *  Method to copy a file
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 */

public void fileCopy (String inFile, String outFile, String appendFlag) 
{
	try
	{
		BufferedReader in = new BufferedReader(new FileReader(inFile));

		/** Define out by default */
		PrintStream out  = new PrintStream(new FileOutputStream(outFile, true));
		if (appendFlag.equals("append")) 
		{
			out  = new PrintStream(new FileOutputStream(outFile, false)); 
		}
		if (appendFlag.equals("concat")) 
		{
			out  = new PrintStream(new FileOutputStream(outFile, true)); 
		}
		System.out.println("servletUtility.fileCopy copying a file");
		int c;
		while((c = in.read()) != -1)
        out.write(c);
		in.close();
		out.close();
	}
	catch(Exception e) 
	{
		System.out.println("failed: servletUtility.fileCopy");
		e.printStackTrace();
	}
}



/**
 *  Method to copy a file
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 */

public void fileCopy (String inFile, String outFile) 
{
	try
	{
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		PrintStream out  = new PrintStream(new FileOutputStream(outFile, true));
		
		System.out.println("servletUtility.fileCopy copying a file");
		
		int c;
		while((c = in.read()) != -1)
		{
        out.write(c);
		}
		
		in.close();
		out.close();
	}
	catch(Exception e) 
	{
		System.out.println("Exception: " + e.getMessage() );
		e.printStackTrace();
	}
}





/**
 *  Method to store html code that can be accessed based on the requests
 * made by the user 
 */

public void htmlStore() 
{
	ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

	//compose a very simple html page to dispaly that the user query is being
	//handled by the servlet engine
	String mainPage="<html> \n"
	//+"<body> \n"
	+"<head> \n"
	+"	<title> plotServe - Query Engine for the National Plots Database </title> \n"
	+"</head>  \n"
	+" 	<body bgcolor=\"white\"> \n"
	+" 	<table border=\"0\" width=\"100%\"> \n"
	+" 	<tr bgcolor=\"#9999CC\"><td>&nbsp;<B><FONT FACE=\"arial\" COLOR=\"FFFFFF\" SIZE=\"-1\"> "
	+" 	plotServe - Query Engine for the National Plots Database "
	+" 	</FONT></B></td></tr> \n"
	+"</table> \n"
	+" \n"
	+"<br><i><small> \n"
	+rb.getString("requestparams.servletPosition")+"<br> \n"
	+"<br></i></small> \n"
	+"<p> \n"
	+"<A HREF="+rb.getString("requestparams.servletAccessPosition")+"> "
	+"<B><FONT SIZE=\"-1\" FACE=\"arial\">Back to the query mechanism</FONT></B></A> \n"
	+"<br></i> \n"
	+"<P> \n"
	+"<table border=\"0\" width=\"100%\"> \n"
	+"<tr bgcolor=\"#9999CC\"><td>&nbsp;<B><FONT FACE=\"arial\" COLOR=\"FFFFFF\" SIZE=\"-1\"> "
	+" "
	+"</FONT></B></td></tr> \n"
	+"</table> \n"
	+"<br><br> \n";
	//don't close the html b/c more will be written by the servlet
	//+"</body> \n"
	//+"</html> \n";
	outString = mainPage;
}


	
/**
 * Method to store html code that can be accessed based on the requests
 * made by the user that will allow the user to access the summary viewer class
 * called 'viewData' and depending on the input parameter passed will show the 
 * user the summary of the selected plots, veg vommunities, or plant taxonmies.
 *
 * @param summaryViewType -- the type of summary to view, can include vegPlot, 
 *	vegCommunity, or plantTaxa 
 */
public void getViewOption (String summaryViewType) 
{
	System.out.println("accessing the getViewOptionMethod");
	ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
	StringBuffer responseBuf=null; //use this string buffer instead
	String response=
	" "
	//+"<html> \n"
	//+"<body> \n"
	//+"<head> \n"
	+"<form action=\"http://vegbank.nceas.ucsb.edu/framework/servlet/viewData\" method=\"GET\"> \n"
	+"<input type=\"hidden\" name=\"resultType\" value=\"summary\" > \n"
	+"<input type=\"hidden\" name=\"summaryViewType\" value=\""+summaryViewType+"\"> \n"
	+"<input type=\"submit\" name=\"submitButton\" value=\"view data\" > \n"
	+"</form> \n"
	//+"</body> \n"
	+"</html> \n";
	outString=response;	
}



	
/**
 * Method to store html code showing the end user the
 * options for interacting with the data that was in 
 * the result set
 */

public void getViewMethod () 
{
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
	//+"<A HREF=\"http://beta.nceas.ucsb.edu:8080/examples/servlet/plotQuery\">"
	+"<A HREF=\"http://"+rb.getString("server")+""+rb.getString("servlet-path")+"plotQuery\">"
	
	+"<br>"
	+"<B><FONT SIZE=\"-1\" FACE=\"arial\">Back to Query</FONT></B></A>"
	+"</form> \n"
	+"</body> \n"
	+"</html> \n";
	outString=response;
}
public String outString = null;	



/**
 *  Method to compress a file using GZIP compression using as input both the
 *  input file name and the output file name
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 */

public void gzipCompress (String inFile, String outFile) 
{
	try
	{
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(
			new FileOutputStream(outFile)));

		System.out.println("servletUtility.gzipCompress Writing a compressed file");
		int c;
		while((c = in.read()) != -1)
		out.write(c);
		in.close();
		out.close();
	} 
catch(Exception e) 
{
	e.printStackTrace();
}
}



/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then makes the vector and number of vector elements access
 *  to the public 
 *
 * @param fileName name of the file that whose contents should be written to a vector
 */
public void fileVectorizer(String fileName) 
{
	try 
	{
		vecElementCnt=0;
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		Vector localVector = new Vector();
		String s;
		while((s = in.readLine()) != null) 
		{
			//System.out.println(s);	
			localVector.addElement(s);
			vecElementCnt++;
		}
		outVector=localVector;
	}
	catch (Exception e) 
	{
		System.out.println("failed in servletUtility.fileVectorizer" + 
		e.getMessage());
	}
}



/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then makes the vector and number of vector elements access
 *  to the public 
 *
 * @param fileName name of the file that whose contents should be written to a vector
 */
public Vector fileToVector(String fileName) 
{
	Vector v = new Vector();
	try 
	{
		vecElementCnt=0;
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		Vector localVector = new Vector();
		String s;
		while((s = in.readLine()) != null) 
		{
			v.addElement(s);
		}
	}
	catch (Exception e) 
	{
		System.out.println("Exception: " + 
		e.getMessage());
		e.printStackTrace();
	}
	return(v);
}



/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then makes the vector and number of vector elements access
 *  to the public 
 *
 * @param fileName name of the file that whose contents should be written to a vector
 */
public String fileToString(String fileName) 
{
	StringBuffer sb = new StringBuffer();
	try 
	{
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String s;
		while((s = in.readLine()) != null) 
		{
			sb.append(s+"\n");
		}
	}
	catch (Exception e) 
	{
		System.out.println("Exception: " + 
		e.getMessage());
		e.printStackTrace();
	}
	return(sb.toString() );
}

	/**
	 * method that allows a class (in this case a servlet) to upload
	 * a file to the dataexcahnge servlet and onto the data file database
	 * 
	 * @param fileName -- the name of the file
	 * @param userName -- actually the email address of the user
	 * 
	 */
	 public void uploadFileDataExcahgeServlet(String fileName, String userName)
	 {
		 try
		 {
		 	this.uploadFileDataExcahgeServlet(fileName, userName, "unknown");
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
	 
	 /**
 	 * method to copy a file and filter the tokens, this method uses the 
 	 * jakarta ant library 1.4.1 and above
 	 *
	 * @param infile -- the file to be copied that contains the token
	 * @param outfile -- the output file with the replaced token
	 * @param  token -- the token to replace in the file
	 * @param value -- the value to replace the token with
	 */
	public boolean filterTokenFile( String inFile, String outFile, String token, String value)
	{
		boolean result = true;
		try
		{
			System.out.println("ServletUtility > in file: " + inFile );
			System.out.println("ServletUtility > out file: " + outFile );

			//SET UP THE PROJECT
	  	Project proj = new Project();
			System.out.println("ServletUtility > base directory:  " + proj.getBaseDir() );
	
	
			//SET UP THE COPY TASK
			Copy copy = new Copy();
			copy.setVerbose(true);
			File in = new File(inFile);
			File outf = new File(outFile);
			//File outf = new File("./out1");
			File dir = new File("./");

			boolean readable =  in.canRead();
			boolean writeable =  outf.canWrite();
			System.out.println("ServletUtility > in readable " + in.canRead() );
			System.out.println("ServletUtility > out writeable " + outf.canWrite() );
			if (readable == false )
			{
				System.out.println("ServletUtility > cannot perfor copy " );
				result=false;
			}

			copy.setFiltering(true);
			copy.setFile(in);
			copy.setOverwrite(true);
			copy.setTofile(outf);
		
			//SET THE COPY TASK AS A TASK IN THE PROJECT
			copy.setProject(proj);
		
			System.out.println("ServletUtility > owning target: " + copy.getOwningTarget() );
			System.out.println("ServletUtility > targets in proj: " + proj.getTargets().toString() );

			org.apache.tools.ant.taskdefs.Filter filter = new org.apache.tools.ant.taskdefs.Filter();
			//CREATE A FILTER SET
			org.apache.tools.ant.types.FilterSet fset = copy.createFilterSet();
			fset.addFilter(token, value);
			fset.setProject(proj);
			System.out.println("ServletUtility > task has filters: " + fset.hasFilters() );
		
			//EXECUTE THE COPY TASK
			copy.execute();

		}
		catch (BuildException e )
		{
			System.out.println(e.getMessage() );
			e.printStackTrace();
			result=false;
		}
		return(result);
	}		

	 
	 /**
	 * method that allows a class (in this case a servlet) to upload
	 * a file to the dataexcahnge servlet and onto the data file database
	 * 
	 * @param fileName -- the name of the file
	 * @param userName -- actually the email address of the user
	 * @param fileType -- the type of file as to be specified in the 
	 * 	user profile database
	 */
	 public void uploadFileDataExcahgeServlet(String fileName, String userName, 
	 String fileType)
	 {
		 try
		 {
		 	String temp = null;
     	String servlet = "/framework/servlet/dataexchange";
     	String protocol = "http://";
     	String host = "vegbank.nceas.ucsb.edu";
     	String server = protocol + host + servlet;
     	String filename = fileName;
			
	 		// String parameterString = "?action=uploadFile&user=harris02@hotmail.com&exchangeType=upload&submitter=harris&"
	  	//	+"password=jasmine&file=test.dat";
	 
			DataExchangeClient dec = new DataExchangeClient();
			dec.uploadFile(servlet, protocol, host, server, filename, userName, fileType);
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
	

}	

