package org.vegbank.common.utility;

/*
 * Utility class for java servltes for doing a range of utility 
 * type functions including:
 * 		emailing
 * 		figuring the the type of client browser
 *    etc.. 
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-10-10 23:37:13 $'
 *  '$Revision: 1.1 $'
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.filters.ReplaceTokens;
import org.apache.tools.mail.MailMessage;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

public class ServletUtility 
{
	private GetURL gurl = new GetURL();	
	
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
	
	
	public void sendHTMLEmail(
		String smtpServer,
		String from,
		String to,
		String cc,
		String subject,
		String body)
	{
		try
		{
			Properties props =new Properties();
			props.put("mail.smtp.host", smtpServer);
			Session session = Session.getDefaultInstance(props, null);
			
			//	-- Create a new message --
			Message msg = new MimeMessage(session);
			
			// -- Set the FROM and TO fields --
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			
			// -- Include CC recipients too --
		 	if (cc != null)
		 	{
				msg.setRecipients(Message.RecipientType.CC ,InternetAddress.parse(cc, false));
		 	}

			// -- Set the subject and body text --
			msg.setSubject(subject);
			//msg.setText(body);
			msg.setContent(body, "text/html");

			// -- Set some other header information --
			msg.setSentDate(new Date());

			// -- Send the message --
			Transport.send(msg);

			System.out.println("Message sent OK.");
		}
		catch ( Exception e)
		{
			System.out.println("ServletUtility > Problem sending mail message");
			e.printStackTrace();
		}
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
		 * 
		 * @deprecated use sendHTMLMail instead 
		 */
		public void sendEmail(
			String mailHost,
			String from,
			String to,
			String cc,
			String subject,
			String body)
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
			String uri = "/vegbank/servlet/authenticate"+sb.toString().trim();
			System.out.println("OUT PARAMETERS: "+uri);
      int port=80;
      String requestType="POST";
      htmlResults = GetURL.requestURL(uri);
    }
    catch( Exception e )
    {
      System.out.println("** failed :  "
      +e.getMessage());
    }
    return(htmlResults);
  }
	

	/**
	 *  Method to store html code that can be accessed based on the requests
	 * made by the user
	 */

	public String htmlStore()
	{
		ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

		//compose a very simple html page to dispaly that the user query is being
		//handled by the servlet engine
		String mainPage="<html> \n"
		//+"<body> \n"
		+"<head> \n"
		+"  <title> VEGBANK - QUERY ENGINE </title> \n"
		+"</head>  \n"
		+"  <body bgcolor=\"white\"> \n"
		+"  <table border=\"0\" width=\"100%\"> \n"
		+"  <tr bgcolor=\"#9999CC\"><td>&nbsp;<B><FONT FACE=\"arial\" COLOR=\"FFFFFF\" SIZE=\"-1\"> "
		+"  VegBank - Query Engine  "
		+"  </FONT></B></td></tr> \n"
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
	
		return mainPage;
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
			System.out.println(
					"** failed in:  " + " first try - reading parameters " + e.getMessage()
			);
		}
		return(params);
	}
	
	
	
	
	


/**
 *  Method to copy a file
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 */
public void flushFile(String inFile) 
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
		System.out.println("ServletUtility > appendFlag: " + appendFlag);
		// CHECK THE FILE 
		File inf = new File(inFile);
		File outf = new File(outFile);
		boolean readable =  inf.canRead();
		boolean writeable =  outf.canWrite();
		System.out.println("ServletUtility > in readable " + inf.canRead() );
		System.out.println("ServletUtility > out writeable " + outf.canWrite() );

		int c;
		while((c = in.read()) != -1)
		{
    	//System.out.println("## " + c );
			out.write(c);
		}

		in.close();
		out.close();
		System.out.println("ServletUtility > file size: " + c);	
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
		
		System.out.println("ServletUtility > file size: " + c);	
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

	public void removeCookie(HttpServletRequest req, HttpServletResponse response )
	{
		//get the cookies - if there are any
		Cookie[] cookies = this.getVegbankCookies(req);
		
		Cookie delcookie = null;
		
		// Remove all the cookies
		for (int i = 0; i < cookies.length; i++) 
		{
			Cookie cookie = cookies[i];
			
			// clone the existing cookie
			delcookie = (Cookie) cookie.clone();
			// negative value means the cookie lives for the lifetime of the browser
			// delcookie.setMaxAge(-1);
			// 0 tells the browser to delete immediately
			delcookie.setPath("/");
			delcookie.setMaxAge(1);
			delcookie.setValue("-1");
			response.addCookie(delcookie);
			
			System.out.println("ServletUtility > Deleting the vegbank cookie!!!");
		} 		
	}
	
 	/**
	 * method that returns the name and value of a valid cookie
	 * assocaited with the servlet that the request is made to
	 * @param req -- the servlet request
	 */
	 public String getCookieValue( HttpServletRequest req)
	 {
		String s = null;
		String cookieValue = "";

		//get the cookies - if there are any
		Cookie[] cookies = this.getVegbankCookies(req);
		
		// This always used the last cookie found to use as the value
		// Should work but not ideal.
		for (int i = 0; i < cookies.length; i++) 
		{
	      	Cookie cookie = cookies[i];
			cookieValue=cookie.getValue(); 
			s = cookieValue.trim();
		} 

		return(s);
	}


	/**
	 * Get all the vegbank cookies
	 * 
	 * @param req
	 * @return Cookie[] -- all the vegbank cookies
	 */
	private Cookie[]  getVegbankCookies( HttpServletRequest req )
	{
		Vector vegbankCookies = new Vector();
		// TODO: Move this into config options
		String defaultCookieName = "framework";
		
		Cookie[] cookies = req.getCookies();
		if ( cookies != null  &&  cookies.length >= 0 )
		{
			for (int i = 0; i < cookies.length; i++) 
			{
				Cookie cookie = cookies[i];
				System.out.println("Cookie Name: " +cookie.getName());
				String cookieName=cookie.getName();

				if ( cookieName.equals(defaultCookieName) )
				{
					vegbankCookies.add(cookie);
				}
			}
		} 
		else 
		{
			System.out.println("ServletUtility > No valid cookies found");
		}
		
		// Cast into Cookie Array 
		Object[] objects = vegbankCookies.toArray();
		Cookie[] results = new Cookie[ objects.length ];
		for ( int i=0; i < objects.length; i++ )
		{
			results[i] = (Cookie) objects[i];
		}
		
		return results;
	}
	
/**
 *  Method to compress a file using GZIP compression using as input both the
 *  input file name and the output file name
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 */

public void gzipCompress(String inFile, String outFile)
{
	try
	{
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		BufferedOutputStream out =
			new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(outFile)));

		System.out.println("ServletUtility > gzipCompress");
		System.out.println("ServletUtility > inFile: " + inFile);
		System.out.println("ServletUtility > outFile: " + outFile);

		//System.out.println("servletUtility.gzipCompress Writing a compressed file");
		int c;
		while ((c = in.read()) != -1)
			out.write(c);
		in.close();
		out.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
}

/**
 *  Method to compress an inputstream using GZIP compression
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 */

public static byte[] gzipCompress(byte[]  ba) throws IOException
{
	ByteArrayOutputStream compressed = new ByteArrayOutputStream();
	GZIPOutputStream gzout = new GZIPOutputStream(compressed);
	gzout.write( ba );
	gzout.flush();
	gzout.close();
	return compressed.toByteArray();
}


/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then makes the vector and number of vector elements access
 *  to the public 
 *
 * @param fileName name of the file that whose contents should be written to a vector
 */
public Vector fileVectorizer(String fileName) 
{
	Vector vector = new Vector();
	try 
	{
		System.out.println("ServletUtility > vectorizing file: " + fileName);
		int vecElementCnt=0;
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String s;
		while((s = in.readLine()) != null) 
		{
			//System.out.println(s);	
			vector.addElement(s);
			vecElementCnt++;
		}
	}
	catch (Exception e) 
	{
		System.out.println("failed in servletUtility.fileVectorizer" + 
		e.getMessage());
	}
	return vector;
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
	System.out.println("ServletUtility > file to vector: " + fileName);
	Vector v = new Vector();
	try 
	{
		int vecElementCnt=0;
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		Vector localVector = new Vector();
		String s;
		while((s = in.readLine()) != null) 
		{
			v.addElement(s);
		}
		in.close();
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
	System.out.println("ServletUtility > fileToString");
	StringBuffer sb = new StringBuffer();
	try 
	{
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String s;
		while((s = in.readLine()) != null) 
		{
			//System.out.println("##> " + s +" \n");
			sb.append(s + "\n");
		}
	}
	catch (Exception e) 
	{
		System.out.println("Exception: " + e.getMessage());
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
	public boolean filterTokenFile( String inFile, Writer out,  Hashtable values)
	{
		boolean result = true;
		try
		{
			filterTokens( new FileReader(inFile), out, values);
		}
		catch (Exception e )
		{
			System.out.println(e.getMessage() );
			e.printStackTrace();
			result=false;
		}
		return(result);
	}	
	
	
	private void filterTokens ( Reader in, Writer out, Hashtable tokenVals)
	{	
		
		ReplaceTokens rtReader = 	new ReplaceTokens( in);
		Enumeration enum = tokenVals.keys();
		while ( enum.hasMoreElements() )
		{
			String tokenName = (String) enum.nextElement();
			String value = (String) tokenVals.get(tokenName);
			// do shit with this
			ReplaceTokens.Token token = new ReplaceTokens.Token();
			token.setKey(tokenName);
			token.setValue(value);
			
			// Add token to Reader
			rtReader.addConfiguredToken(token);
		}
		
		BufferedReader br = new BufferedReader(rtReader);
		String s = "";
		try
		{
			while (( s = br.readLine() ) != null)
			{
				out.write(s);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
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
	public boolean filterTokenFile( Reader in, Writer out, String token, String value)
	{
		boolean result = true;
		try
		{
			Hashtable hash = new Hashtable();
			hash.put(token, value);
			filterTokens(in, out, hash);
		}
		catch (Exception e )
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
     	String servlet = "/vegbank/servlet/dataexchange";
     	String protocol = "http://";
     	String host = "vegbank.nceas.ucsb.edu";
     	String server = protocol + host + servlet;
     	String filename = fileName;
			
	 		// String parameterString = "?action=uploadFile&user=harris02@hotmail.com&exchangeType=upload&submitter=harris&"
	  	//	+"password=jasmine&file=test.dat";
	 
			DataExchangeClient.uploadFile(servlet, protocol, host, server, filename, userName, fileType);
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }

	/**
	 * @param request
	 * @return
	 */
	public String printParameters(HttpServletRequest request)
	{
		StringBuffer sb = new StringBuffer();
		 Enumeration enum = request.getParameterNames();
		 
		 while ( enum.hasMoreElements() )
		 {
		 	String parameterName = (String) enum.nextElement();
			sb.append( parameterName + " --> " + request.getParameter(parameterName) );
			sb.append(",");
		 }
		
		return sb.toString();
	}
	
	/**
	 * @param request
	 * @return
	 */
	public String printAttributes(HttpServletRequest request)
	{
		StringBuffer sb = new StringBuffer();
		 Enumeration enum = request.getAttributeNames();
		 
		 while ( enum.hasMoreElements() )
		 {
				String attributeName = (String) enum.nextElement();
			sb.append( attributeName + " --> " + request.getAttribute(attributeName) );
			sb.append(",");
		 }
		
		return sb.toString();
	}
	
	/**
	 * method that retuns the date in a format that can be accepted 
	 * by the RDBMS and that is like: Aug 9, 2002
	 */
	 public String getCurrentDate()
	 {
		String s = "";
		 try
		 {
			Date now = new Date();  //Set Date variable now to the current date and time
			DateFormat med = DateFormat.getDateInstance (DateFormat.MEDIUM);
			s = med.format(now);
			//System.out.println ("MEDIUM: " + s);
		 }
			catch(Exception e )
			{
				e.printStackTrace();
			}
			return(s);
	 }
	 
	 

}	

