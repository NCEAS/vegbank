package org.vegbank.common.utility;

/*
 * Utility class for java servltes for doing a range of utility 
 * type functions including:
 * 		emailing
 * 		figuring the the type of client browser
 *    etc.. 
 *
 *	'$Author: anderson $'
 *  '$Date: 2004-08-27 23:28:22 $'
 *  '$Revision: 1.14 $'
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


import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;
import org.apache.tools.ant.filters.ReplaceTokens;
import org.vegbank.common.Constants;

import com.Ostermiller.util.LineEnds;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

public class ServletUtility 
{
	private static Log log = LogFactory.getLog(ServletUtility.class); 
	private GetURL gurl = new GetURL();	
	
	static final int BUFFER = 2048; 
	static final String SMTP_SERVER = "hyperion.nceas.ucsb.edu"; 
	
	/**
	 * method that takes a Hashtable containing fileContent and desired fileName 
	 * and an Outputstrean that represents the created  zip file plus the LineEnd 
	 * style desired if downloading file to different OS.
	 * 
	 * See ( or use ) com.Ostermiller.util>LineEnds for the Constants for each system
	 *
	 * @param nameContent -- the Hashtable containing the filesContent (as String ) and the file names
	 * @param outStream -- OutputStream to  take zipped up files
	 * @param style -- line separator style.
	 * @param encoding -- charset (e.g. UTF-8, UTF-16)
	 *
	 */
	public static void zipFiles(Hashtable nameContent, OutputStream outStream, int style) throws IOException {
		zipFiles(nameContent, outStream, style, null);
	}

	public static void zipFiles(Hashtable nameContent, OutputStream outStream, int style, String encoding) throws IOException
	{
		ZipOutputStream zipstream = new ZipOutputStream(outStream);
		
		Enumeration names = nameContent.keys();
		while ( names.hasMoreElements() )
		{
			String fileName = (String) names.nextElement();
			String fileContent = (String) nameContent.get(fileName);
		
			ZipEntry zipentry = new ZipEntry(fileName);
			zipstream.putNextEntry(zipentry);
			
			ByteArrayInputStream origin;
			try {
				// Set up an input stream with the given character encoding
				if (Utility.isStringNullOrEmpty(encoding)) {
					origin = new ByteArrayInputStream(fileContent.getBytes());
				} else {
					origin = new ByteArrayInputStream(fileContent.getBytes(encoding));
				}

			} catch(UnsupportedEncodingException uex) {
				// Use the platform's default charset
				origin = new ByteArrayInputStream(fileContent.getBytes());
			}


			// handle line endings, or not if binary data
			// -- TODO: make better check for binary data
			// -- other than whether encoding is set or not
			if (Utility.isStringNullOrEmpty(encoding)) {
				LineEnds.convert(origin, zipstream, LineEnds.STYLE_DOS);

			} else {
				
				// handle as binary data
				int c = origin.read();
				while (c != -1) {
					zipstream.write(c);
					c = origin.read();
				}
			}

			origin.close();	
		}
		zipstream.close();
	}
	
	/**
	 * Send a HTML email message.
	 * 
	 * @param smtpServer
	 * @param from
	 * @param to
	 * @param cc
	 * @param subject
	 * @param body
	 * @throws MessagingException
	 */
	public static void sendHTMLEmail(
		String smtpServer,
		String from,
		String to,
		String cc,
		String subject,
		String body) throws MessagingException
	{
		Message msg = getEmailMessage(smtpServer, from, to, cc, subject);

		// Set the body content
		msg.setContent(body, "text/html");
		// -- Send the message --
		Transport.send(msg);
	}
	
	/**
	 * Get the usrId from the session.
	 *
	 * @param request -- to get at session object
	 * @return Long the usrid ( or null if not found)
	 */
	public static Long getUsrIdFromSession(HttpServletRequest request)
	{
		Long usrId = null;
		HttpSession session = request.getSession();
		if ( session != null )
		{
			usrId = (Long) session.getAttribute(Constants.USER_KEY);	
		}
		return usrId;
	}

	/**
	 * Send an email from a template.
	 * 
	 * @param templateName
	 * @param tagTable
	 * @param from
	 * @param to
	 * @param cc
	 * @param subject
	 * @param plainText=true
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendEmailTemplate(String templateName, Map tagTable, 
			String from, String to, String cc,
			String subject, boolean plainText) 
			throws AddressException, MessagingException
	{
		// load the email template
		VelocityParser velo = new VelocityParser(templateName);
		velo.putAll(tagTable);

		// set up the email header
		String body = velo.processTemplate(); 

		if (to == null || to.equals("")) {
			throw new AddressException("no email address given");
		}

		log.debug("Sending email from " + from + " to " + to);
		if (plainText) {
			sendPlainTextEmail(SMTP_SERVER, from, to, cc, subject, body);
		} else {
			sendHTMLEmail(SMTP_SERVER, from, to, cc, subject, body);
		}
	}

	/**
	 * Send a plain text email.
	 * 
	 * @param smtpServer
	 * @param from
	 * @param to
	 * @param cc
	 * @param subject
	 * @param body
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendPlainTextEmail(
		String smtpServer,
		String from,
		String to,
		String cc,
		String subject,
		String body) throws AddressException, MessagingException
	{
		Message msg = getEmailMessage(smtpServer, from, to, cc, subject);

		// Set the body text
		msg.setText(body);
		// -- Send the message --
		Transport.send(msg);
	}


	private static Message getEmailMessage(
		String smtpServer,
		String from,
		String to,
		String cc,
		String subject) throws AddressException, MessagingException
	{
		// Get default SMTP_SERVER if none given
		if ( Utility.isStringNullOrEmpty(smtpServer) )
		{
			smtpServer = Utility.SMTP_SERVER;
		}
		
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
		
		// -- Set some other header information --
		msg.setSentDate(new Date());
		
		return msg;
	}
	
	/**
	 * method to stick the parameters from the client 
	 * into a hashtable and then pass it back to the 
	 * calling method
	 */
	public static Hashtable parameterHash (HttpServletRequest request) 
	{
		Hashtable params = new Hashtable();
		try 
		{
			Enumeration enum =request.getParameterNames();
			//log.debug("servletUtility 'parameterHash' contacted ");
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
			log.debug(
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
			log.debug("failed: servletUtility.flushFile");
		}
	}
	
	/**
	 *  Method to copy a file
	 *
	 * @param  inFile  a string representing the input file
	 * @param  outFile a string representing the output, compressed, file
	 */

	public void fileCopy(String inFile, String outFile)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(inFile));
			PrintStream out =
				new PrintStream(new FileOutputStream(outFile, false));

			log.debug("ServletUtility > fileCopy");
			log.debug("ServletUtility > inFile: " + inFile);
			log.debug("ServletUtility > outFile: " + outFile);

			int c;
			while ((c = in.read()) != -1)
			{
				out.write(c);
			}

			log.debug("ServletUtility > file size: " + c);
			in.close();
			out.close();
		}
		catch (Exception e)
		{
			log.debug("Exception: " + e.getMessage());
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
				//log.debug("ServletUtility > headerName: " + headerName+ " value: " + value);
	      if ( headerName.toUpperCase().startsWith("USER") )
				{
					String ua = headerName.toUpperCase();
					//log.debug("ServletUtility > UA: "+ value );
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
			log.debug("ServletUtility > browserType: " + s); 
		 }
		 catch(Exception e) 
		{
			log.debug("Exception: " + e.getMessage() );
		}
		 return(s);
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
			log.debug("ServletUtility > vectorizing file: " + fileName);
			int vecElementCnt=0;
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String s;
			while((s = in.readLine()) != null) 
			{
				//log.debug(s);	
				vector.addElement(s);
				vecElementCnt++;
			}
		}
		catch (Exception e) 
		{
			log.debug("failed in servletUtility.fileVectorizer" + 
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
			log.debug(e.getMessage() );
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
			log.debug(e.getMessage() );
			result=false;
		}
		return(result);
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
	 *
	 */
	public static String buildQueryString(Map params) {
		StringBuffer qs = new StringBuffer(params.size() * 17)
			.append("?");

		String p;
		boolean first = true;
		Iterator pit = params.keySet().iterator();
		try {

			while (pit.hasNext()) {
				if (first) {
					first = false;
				} else {
					qs.append("&");
				}

				p = (String)pit.next();
				qs.append(p).append("=");
				qs.append( (java.net.URLEncoder.encode((String)params.get(p), "UTF-8")) );
			}
		} catch (Exception ex) {
			log.error("problem building query string", ex);
		}


		return qs.toString();
	}

	/**
	 * Takes a File and  unzips it 
	 * 
	 */
	public static Collection unZip(FormFile inFile) throws Exception 
	{
		Vector fileList = new Vector();
		InputStream in = inFile.getInputStream();
		ZipInputStream zis = new ZipInputStream(in);
		ZipEntry e;
		// Loop over every ZipEntry in ZIP file
		while( ( e = zis.getNextEntry()) != null)
		{
			File outFile = new File(e.getName());
			FileOutputStream out = new FileOutputStream(outFile);
			
			byte[] b = new byte[512];
			int len = 0;
			while ( ( len=zis.read(b) ) != -1)
			{
				out.write(b,0,len);
			}
			fileList.add( new FileWrapper(outFile) );
		}
		zis.close();
		
		return fileList;
	}

}	

