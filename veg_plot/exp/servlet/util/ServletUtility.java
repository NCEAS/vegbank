package servlet.util;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.*;
import org.apache.tools.mail.MailMessage;
import servlet.util.GetURL;
//import servlet.util.*;


/**
 * Utility class for java servltes for doing a range of utility 
 * type functions including:
 * 		emailing
 * 		figuring the the type of client browser
 *    etc.. 
 *
 *	'$Author: harris $'
 *  '$Date: 2002-06-13 17:37:03 $'
 *  '$Revision: 1.12 $'
 *
 */



public class ServletUtility 
{
	private GetURL gurl = new GetURL();
	public Vector outVector;
	public int vecElementCnt;


	
	/**
	 * method that takes a vector containing a number of file names
	 * and a string that represents the output file and creates a 
	 * zip file with the specified name containing the specified 
	 * files
	 *
	 * @param fileVec -- the vector containing the files
	 * @param outFile -- the name of the zip file
	 *
	 */
	public void setZippedFile(Vector fileVec, String outFile)
	{
		try 
		{
		 	String outFilename = outFile;
     	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
			for (int i =0; i < fileVec.size(); i++)
			{
				String inFilename = (String)fileVec.elementAt(i);
     		FileInputStream in = new FileInputStream(inFilename);
     		out.putNextEntry(new ZipEntry(inFilename));
     		byte[] buf = new byte[1024];
     		int len;
      	while ((len = in.read(buf)) > 0) 
				{
         	out.write(buf, 0, len);
       	}
     		out.closeEntry();
				in.close();
     	}
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+ e.getMessage() );
			e.printStackTrace();
		}
	}

	/**
	 * method that unzips the contents of a zip file
	 * 
	 * @param zipFile -- the zip file
	 *
	 */
	 public void getZippedFileContents(String zipFile)
	 {
	  try 
		{
			String inFilename = "infile.zip";
      String outFilename = "outfile";
      ZipInputStream in = new ZipInputStream(new FileInputStream(inFilename));
      OutputStream out = new FileOutputStream(outFilename);
     
      ZipEntry entry;
      byte[] buf = new byte[1024];
      int len;
      if ((entry = in.getNextEntry()) != null) 
			{
				String name = entry.getName();
				System.out.println("ServletUtility > file name: " + name);
				while ((len = in.read(buf)) > 0) 
				{
					out.write(buf, 0, len);
      	}
     	}
     	out.close();
     	in.close();
     } 
		 catch (IOException e) 
		 {
     }
	 }
 



	




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
		 * method to mail the a message to an email address
		 *
		 * @param mailhost -- like nceas.ucsb.edu
		 * @param from -- like harris
		 * @param to -- like harris02@hotmail.com
		 * @param cc -- like vegbank@nceas.ucsb.edu
		 * @param subject -- like 'hi'
		 * @param body -- the main body
		 */
		 public void sendEmail( String mailHost, String from, String to, String cc,
		 String subject, String body)
		 {
			 try
			 {
			 	//String mailhost = "nceas.ucsb.edu";  // or another mail host
 				//String from = "vegbank";
 				//String to = this.emailAddress;
 				//String cc1 = "vegbank@nceas.ucsb.edu";
 				//String cc2 = "lori@esa.org";
 				//String bcc = "bcc@you.com";
  
 				MailMessage msg = new MailMessage(mailHost);
 				msg.from(from);
 				msg.to(to);
 				msg.cc(cc);
 				msg.setSubject(subject);
 				PrintStream out = msg.getPrintStream();
  			out.println(body);
			 	msg.sendAndClose();
 			}
			 catch (Exception e)
		 	 {
				 System.out.println("Exception: " + e.getMessage());
				 e.printStackTrace();
			 }
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
		
		System.out.println("ServletUtility > fileCopy");
		System.out.println("ServletUtility > inFile: " + inFile);
		System.out.println("ServletUtility > outFile: " + outFile);
		
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
		PrintStream out  = new PrintStream(new FileOutputStream(outFile, false));
		
		System.out.println("ServletUtility > fileCopy");
		System.out.println("ServletUtility > inFile: " + inFile);
		System.out.println("ServletUtility > outFile: " + outFile);
		
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
	+"	<title> VEGBANK - QUERY ENGINE </title> \n"
	+"</head>  \n"
	+" 	<body bgcolor=\"white\"> \n"
	+" 	<table border=\"0\" width=\"100%\"> \n"
	+" 	<tr bgcolor=\"#9999CC\"><td>&nbsp;<B><FONT FACE=\"arial\" COLOR=\"FFFFFF\" SIZE=\"-1\"> "
	+" 	VegBank - Query Engine  "
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
 * 
 * @deprecated -- this code has been moved to the data request servlet 20020415
 *
 */
public void getViewOption(String summaryViewType) 
{
	System.out.println("ServletUtility > accessing the getViewOptionMethod");
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
 * utility method to return the name of the browser type that 
 * a client is using given as input an http request
 * @param request -- the http request
 *
 */
 public String getBrowserType(HttpServletRequest request)
 {
	 String s = "";
	 try
	 {
		 Enumeration headerNames = request.getHeaderNames();
		 while(headerNames.hasMoreElements()) 
		{
      String headerName = (String)headerNames.nextElement();
      String value = request.getHeader(headerName);
			//System.out.println("ServletUtility > headerName: " + headerName+ " value: " + value);
      if ( headerName.toUpperCase().startsWith("USER") )
			{
				String ua = headerName.toUpperCase();
				//System.out.println("ServletUtility > UA: "+ value );
				if ( value.toUpperCase().indexOf("MSIE") >= 1 )
				{
					s = "msie";
				}
				else if ( value.toUpperCase().indexOf("NETSCAPE") >= 1 )
				{
					 s = "netscape";
				}
				else 
				{
					s = "unknown";
				}
			}
    }
		System.out.println("ServletUtility > browserType: " + s); 
	 }
	 catch(Exception e) 
	{
		System.out.println("Exception: " + e.getMessage() );
		e.printStackTrace();
	}
	 return(s);
 }

 /**
	 * method that returns the name and value of a valid cookie
	 * assocaited with the servlet that the request is made to
	 * @param req -- the servlet request
	 */
	 public String getCookieValue( HttpServletRequest req)
	 {
		String s = null;
		 //get the cookies - if there are any
		String cookieName = null;
		String cookieValue = null;

		Cookie[] cookies = req.getCookies();
		//	System.out.println("cookie dump: " + cookies.toString() );
		//	Cookie cook = cookies[0];
		//	String name =cook.getName();
		//	System.out.println("cookie name > : " + name );
		
		//determine if the requested page should be shown
    if (cookies.length >= 0) 
		{
			for (int i = 0; i < cookies.length; i++) 
			{
      	Cookie cookie = cookies[i];
				//out.print("Cookie Name: " +cookie.getName()  + "<br>");
        cookieName=cookie.getName();
				//System.out.println("DataRequestServlet > cookie name: " + cookieName);
				//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
				cookieValue=cookie.getValue();
				s = cookieValue.trim(); 
			}
  	} 
		else 
		{
			System.out.println("DataRequestServlet > No valid cookies found");
		}
		return(s);
	}
 
 
	
/**
 * Method to store html code showing the end user the
 * options for interacting with the data that was in 
 * the result set
 */

public void getViewMethod() 
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
		
		System.out.println("ServletUtility > gzipCompress");
		System.out.println("ServletUtility > inFile: " + inFile);
		System.out.println("ServletUtility > outFile: "  + outFile );
		
		//System.out.println("servletUtility.gzipCompress Writing a compressed file");
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
	 *
	 */
	public boolean filterTokenFile( String inFile, String outFile,  Hashtable values)
	{
		boolean result = true;
		try
		{
			//get the keys 
			Vector keyVec = new Vector();
			Enumeration enum = values.keys();
			//System.out.println("servletUtility 'parameterHash' contacted ");
 			while (enum.hasMoreElements()) 
			{
				String key = (String)enum.nextElement();
				//System.out.println("ServletUtility > filter key: " + key  );
				keyVec.addElement(key);
 			}
			
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
		
			//System.out.println("ServletUtility > owning target: " + copy.getOwningTarget() );
			//System.out.println("ServletUtility > targets in proj: " + proj.getTargets().toString() );

			org.apache.tools.ant.taskdefs.Filter filter = new org.apache.tools.ant.taskdefs.Filter();
			//CREATE A FILTER SET
			org.apache.tools.ant.types.FilterSet fset = copy.createFilterSet();
			
			//put all the filters in the set
			for (int i=0; i<keyVec.size(); i++) 
			{
				String token = (String)keyVec.elementAt(i);
				String val = (String)values.get(token);
				//System.out.println("ServletUtility > applying filter: " + token +" "+ val );
				fset.addFilter(token, val);
			}
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
		
			//System.out.println("ServletUtility > owning target: " + copy.getOwningTarget() );
			//System.out.println("ServletUtility > targets in proj: " + proj.getTargets().toString() );

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

