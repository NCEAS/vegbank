package org.vegbank.common.utility;

/*
 * Utility class for java servltes for doing a range of utility 
 * type functions including:
 * 		emailing
 * 		figuring the the type of client browser
 *    etc.. 
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-11-12 22:27:31 $'
 *  '$Revision: 1.6 $'
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.filters.ReplaceTokens;

import com.Ostermiller.util.LineEnds;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

public class ServletUtility 
{
	private GetURL gurl = new GetURL();	
	
	static final int BUFFER = 2048; 
	
	/**
	 * method that takes a Hashtable containing fileContent and desired fileName 
	 * and an Outputstrean that represents the created  zip file plus the LineEnd 
	 * style desired if downloading file to different OS.
	 * 
	 * See ( or use ) com.Ostermiller.util>LineEnds for the Constants for each system
	 *
	 * @param nameContent -- the Hashtable containing the filesContent (as String ) and the file names
	 * @param outStream -- OutputStream to  take zipped up files
	 * @param style line separator style.
	 *
	 */
	public static void  zipFiles(Hashtable nameContent, OutputStream outStream, int style ) throws IOException
	{
		ZipOutputStream zipstream = new ZipOutputStream(outStream);
		
		Enumeration names = nameContent.keys();
		while ( names.hasMoreElements() )
		{
			String fileName = (String) names.nextElement();
			String fileContent = (String) nameContent.get(fileName);
		
			ZipEntry zipentry = new ZipEntry(fileName);
			zipstream.putNextEntry(zipentry);
			
			int count;
			byte data[] = new byte[BUFFER];
			
			ByteArrayInputStream origin = new ByteArrayInputStream(fileContent.getBytes());
			OutputStream originToConvert = new ByteArrayOutputStream();

			LineEnds.convert(origin, zipstream, LineEnds.STYLE_DOS);
			
			origin.close();	
		}
		zipstream.close();
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
	 * @deprecated
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
//	 public void uploadFileDataExcahgeServlet(String fileName, String userName, 
//	 String fileType)
//	 {
//		 try
//		 {
//		 	String temp = null;
//	     	String servlet = "/vegbank/servlet/dataexchange";
//	     	String protocol = "http://";
//	     	String host = "vegbank.nceas.ucsb.edu";
//	     	String server = protocol + host + servlet;
//	     	String filename = fileName;
//			
//	 		// String parameterString = "?action=uploadFile&user=harris02@hotmail.com&exchangeType=upload&submitter=harris&"
//	  		//	+"password=jasmine&file=test.dat";
//	 
//			DataExchangeClient.uploadFile(servlet, protocol, host, server, filename, userName, fileType);
//		 }
//		 catch(Exception e)
//		 {
//			 System.out.println("Exception: " + e.getMessage() );
//			 e.printStackTrace();
//		 }
//	 }

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
}	

