package servlet.stylesheet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.sql.*;

import org.apache.tools.mail.MailMessage;
import servlet.util.ServletUtility;
//import servlet.plugin.*;
//import org.w3c.dom.Document;
//import xmlresource.utils.XMLparse;
//import servlet.util.ServletUtility;


/**
 *
 */

public class StyleSheetGenerator extends HttpServlet 
{
	
	//this is the tmp file that will be written
	private String fileName = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/test.xsl";
	private ServletUtility util = new ServletUtility();
	private String userEmail = null;
	
	/**
	 * constructor method
	 */
	public StyleSheetGenerator()
	{
		System.out.println("init: StyleSheetGenerator");
	}
	
	
	/** Handle "POST" method requests from HTTP clients */
	public void doPost(HttpServletRequest request,
		HttpServletResponse response)
 	 throws IOException, ServletException 	
		{
			doGet(request, response);
		}

		
		
	/** Handle "GET" method requests from HTTP clients */ 
	public void doGet(HttpServletRequest request, 
		HttpServletResponse response)
		throws IOException, ServletException  
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			try 
			{
				
				//first get the cookie value etc
				this.userEmail = this.getCookieValue(request);
				System.out.println("user: " + this.userEmail );
				
				
			//enumeration is needed for the attributes
				Enumeration enum =request.getParameterNames();
				Hashtable params = this.parameterHash( request );
				
				//the temp file name
				StringBuffer fileContents = new StringBuffer();
				
				//print the xslt file
				fileContents.append( getHeader().toString() );
				fileContents.append( getBody(params).toString() );
				fileContents.append( getFooter().toString() );
				printFile( fileContents );
				
				//register the document
				this.registerDocument(this.fileName, this.userEmail, "stylesheet");
				
				//sendResponse();
			}
		catch( Exception e ) 
		{
			System.out.println("Exception:  "
			+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * method that registers the stylesheet document with 
	 * the datafile database as the user's default stylesheet
	 * for viewing database results through
	 *
	 * @param file -- the file to register 
	 * @param user -- the email address of the user
	 * @param fileType -- the type of file specified in the database, which 
	 *	will be used for querying
	 */
	 private void registerDocument(String file, String user, String fileType )
	 {
		 try
		 {
		 	System.out.println("StyleSheetGenerator > registering the stylesheet document ###");
		 	util.uploadFileDataExcahgeServlet(file, user, fileType);
		 }
		 catch(Exception e )
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		}
	
	/**
	 * method that returns the cookie value associated with the 
	 * current browser
	 */
	private String getCookieValue(HttpServletRequest req)
	{
		
		//get the cookies - if there are any
		String cookieName = null;
		String cookieValue = null;
		try
		{
			Cookie[] cookies = req.getCookies();
			//determine if the requested page should be shown
    	if (cookies.length > 0) 
			{
				for (int i = 0; i < cookies.length; i++) 
				{
      		Cookie cookie = cookies[i];
					//out.print("Cookie Name: " +cookie.getName()  + "<br>");
       	 cookieName=cookie.getName();
					//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
					cookieValue=cookie.getValue();
					System.out.println("cleint passing the cookie name: "+cookieName+" value: "
					+cookieValue);
				}
  		}
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(cookieValue);
	}
	
	
	/**
	 * method that returns the head for the style  sheet
	 */
	private StringBuffer getHeader()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<?xml version=\"1.0\"?> \n");
		sb.append("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"> \n");
		sb.append("<xsl:output method=\"html\"/> \n");
		sb.append("<xsl:template match=\"/vegPlot\"> \n");

		sb.append("<html> \n");
		sb.append("<head> \n");
		sb.append("</head> \n");
		sb.append("<body bgcolor=\"FFFFFF\"> \n");

		sb.append("<br></br> \n");
		sb.append("<xsl:number value=\"count(project/plot)\" /> DOCUMENTS FOUND \n");
	
		sb.append("<!-- THE PROJECT LEVEL INFORMATION --> \n");
		sb.append("<br></br><a> <b> Project name: </b> <xsl:value-of select=\"project/projectName\"/> <br></br> \n");
		sb.append("<b> Project description: </b> <xsl:value-of select=\"project/projectDescription\"/> </a> \n");


		sb.append("<!-- set up a table --> \n");
		sb.append("<table width=\"100%\"> \n");

		//correct the widths for the 3 collumns
    sb.append("       <tr colspan=\"1\" bgcolor=\"CCCCFF\" align=\"left\" valign=\"top\"> \n");
    sb.append("         <th width=\"25%\" class=\"tablehead\">Site Data</th> \n");
    sb.append("         <th width=\"25%\" class=\"tablehead\">Observation Data</th> \n");
    sb.append("         <th width=\"50%\" class=\"tablehead\">Vegetation Data</th> \n");
    sb.append("       </tr> \n");

		sb.append("<!-- Header and row colors --> \n");
  	sb.append("      <xsl:variable name=\"evenRowColor\">#C0D3E7</xsl:variable> \n");
	  sb.append("      <xsl:variable name=\"oddRowColor\">#FFFFFF</xsl:variable> \n");
	
	   
		sb.append("	<xsl:for-each select=\"project/plot\"> \n");
		sb.append("	<xsl:sort select=\"authorPlotCode\"/> \n");
	
		sb.append("	<tr valign=\"top\"> \n");

		return(sb);
	}
	
	
	/**
	 * method that returns the body of the stylesheet
	 * @param params -- the hashtable that contains the common name and the
	 * attribute name
	 */
	private StringBuffer getBody(Hashtable params)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("    <!--if even row --> \n");
		sb.append("    <xsl:if test=\"position() mod 2 = 1\"> \n");
		
		sb.append("		<td colspan=\"1\" bgcolor=\"{$evenRowColor}\" align=\"left\" valign=\"top\"> \n ");
		
		sb.append("	<!-- Site data here --> \n ");
		sb.append("	<xsl:variable name=\"PLOT\"> \n ");
  	sb.append("		<xsl:value-of select=\"authorPlotCode\"/> \n");
		sb.append("	</xsl:variable> \n");
		
		sb.append("	<xsl:variable name=\"PLOTID\"> \n");
  	sb.append("		<xsl:value-of select=\"plotId\"/> \n");
		sb.append("	</xsl:variable> \n");

            
		sb.append("	<xsl:number value=\"position()\"/> \n");
		
		
		//get the attribute xsl lines
		sb.append( getSiteAttributes(params).toString()  );

		//replace this with a method that returns the lines for processing 
		// the observational data
		sb.append("	<!--Observation data hera --> \n ");
	  sb.append("   <td colspan=\"1\"  bgcolor=\"{$evenRowColor}\" align=\"left\" valign=\"top\"> \n");
    sb.append("   	<a> <b>observation code: </b><br></br> <font color=\"FF0066\"> <xsl:value-of select=\"plotObservation/authorObsCode\"/> </font> <br> </br></a> \n");
		sb.append("			<b>community Name:</b> <br></br> <font color=\"FF0066\"> <xsl:value-of select=\"plotObservation/communityAssignment/communityName\"/> </font>\n");
    sb.append("   </td> ");
	 

	 sb.append("<!-- Species data here --> \n");
	 sb.append("   	<td colspan=\"1\" bgcolor=\"{$evenRowColor}\" align=\"left\" valign =\"top\">  \n");
   sb.append("     <i><FONT color=\"green\" SIZE=\"-1\" FACE=\"arial\"> \n");
	 sb.append("			<b>Top species:</b> <xsl:for-each select=\"plotObservation/taxonObservation\"> \n");
	 sb.append("    	 <xsl:value-of select=\"authNameId\"/>; </xsl:for-each> \n");
	 sb.append("				</FONT></i> \n"); 
   sb.append("    	  </td> \n");
	 sb.append("				</xsl:if> \n");
	 
	 
	 //IF ODD ROW
	sb.append("    <!--if odd row --> \n");
	sb.append("    <xsl:if test=\"position() mod 2 = 0\"> \n");
		
	sb.append("		<td colspan=\"1\" bgcolor=\"{$oddRowColor}\" align=\"left\" valign=\"top\"> \n ");
	
		sb.append("	<!-- Site data here --> \n ");
		sb.append("	<xsl:variable name=\"PLOT\"> \n ");
  	sb.append("		<xsl:value-of select=\"authorPlotCode\"/> \n");
		sb.append("	</xsl:variable> \n");
		
		sb.append("	<xsl:variable name=\"PLOTID\"> \n");
  	sb.append("		<xsl:value-of select=\"plotId\"/> \n");
		sb.append("	</xsl:variable> \n");

            
		sb.append("	<xsl:number value=\"position()\"/> \n");
		
		
		//get the attribute xsl lines
		sb.append( getSiteAttributes(params).toString()  );
	
		sb.append("	<!--Observation data hera --> \n ");
	  sb.append("   <td colspan=\"1\"  bgcolor=\"{$oddRowColor}\" align=\"left\" valign=\"top\"> \n");
    sb.append("   	<a> <b>observation code: </b><br></br> <font color=\"FF0066\"> <xsl:value-of select=\"plotObservation/authorObsCode\"/> </font> <br> </br></a> \n");
		sb.append("			<b>community Name:</b> <br></br> <font color=\"FF0066\"> <xsl:value-of select=\"plotObservation/communityAssignment/communityName\"/> </font>\n");
    sb.append("   </td> ");
	 

	 	sb.append("<!-- Species data here --> \n");
	 	sb.append("   	<td colspan=\"1\" bgcolor=\"{$oddRowColor}\" align=\"left\" valign =\"top\">  \n");
   	sb.append("     <i><FONT color=\"green\" SIZE=\"-1\" FACE=\"arial\"> \n");
	 	sb.append("			<b>Top species:</b> <xsl:for-each select=\"plotObservation/taxonObservation\"> \n");
	 	sb.append("    	 <xsl:value-of select=\"authNameId\"/>; </xsl:for-each> \n");
	 	sb.append("				</FONT></i> \n"); 
   	sb.append("    	  </td> \n");
	 	sb.append("				</xsl:if> \n");
	 
	 
		return(sb);
	}
	
	
	
	private StringBuffer getSiteAttributes( Hashtable params )
	{
		StringBuffer sb = new StringBuffer();
		
		System.out.println("site params: "+params.toString() );
		//get all the keys
	 	Enumeration paramlist = params.keys();
 
 		//everyone needs a plot -- so return the plot code
		sb.append("	<a><br></br> <b>plot code: </b> <br></br> ");
		sb.append("<font color = \"red\"> <xsl:value-of select=\"authorPlotCode\"/> </font><br> </br></a> \n");

 		while (paramlist.hasMoreElements()) 
 		{
			String element = (String)paramlist.nextElement();
   		String value  = (String)params.get(element);
			sb.append(" <a><b>"+element+":</b> <br></br> <font color=\"FF0066\"> <xsl:value-of select=\""+value+"\"/> </font> <br></br> </a> \n");
		}
		
		sb.append("	</td> \n");

		return(sb);
	}
	
	
	
	private StringBuffer getFooter()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("	</tr>  \n "); 
		sb.append("</xsl:for-each> \n ");  
		sb.append("</table> \n"); 
		sb.append("</body> \n"); 
		sb.append("</html> \n"); 
		sb.append("</xsl:template> \n"); 
		sb.append("</xsl:stylesheet> \n"); 
		return(sb);
	}
	
	
	/**
	 * this is a method that prints the stylesheet, whose contents are stored
	 * in a string buffer
	 *
	 */
	private void printFile(StringBuffer sb)
	{
	  try
     {
      PrintWriter out = new PrintWriter(new FileWriter(this.fileName));
     		out.println(sb.toString() );
     		out.close(); 
				System.out.println("done printing to the file system");
		 }
     catch(Exception e)
     {
				System.out.println( "Caught Exception: "+ e.getMessage() ); 
     }
		}	 
	 

	 /**
	 * method to stick the parameters from the client 
	 * into a hashtable - where each attribute is stored 
	 * as a string in the hashtable -- maybe change this in
	 * the future to a vector for each attribute so that multiple
	 * attribute vales can be stored
	 */
	private Hashtable parameterHash(HttpServletRequest request) 
	{
		Hashtable params = new Hashtable();
		try 
		{
			Enumeration enum =request.getParameterNames();
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
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(params);
	}
}
