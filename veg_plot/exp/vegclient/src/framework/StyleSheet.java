/**
 * Aclass that prodeces a style sheet based on the attributes that are
 * defineable by a user
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:42 $'
 * 	'$Revision: 1.1 $'
 */
package vegclient.framework;

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

import vegclient.framework.*;

public class StyleSheet
{
	
	StringBuffer fileName = new StringBuffer();
	//the attribute names and  xml path
	Hashtable params = new Hashtable();
	
	public StyleSheet(StringBuffer fileName, Hashtable params)
	{
		fileName.append( getHeader().toString() );
		fileName.append( getBody(params).toString() );
		fileName.append( getFooter().toString() );
		printFile( fileName);
		//System.out.println( fileName.toString() );
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
      PrintWriter out = new PrintWriter(new FileWriter("autoGenerate.xsl"));
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
   * the main routine used to test t
   */
	 
  static public void main(String[] args) 
	{
  	try 
		{
			
			Hashtable attributes = new Hashtable();
			attributes.put("Elevation:", "elevationValue");
			attributes.put("Slope Aspect:", "slopeAspect");
			attributes.put("Slope Gradient:", "slopeGradient");
			attributes.put("Latitude:", "origLat");
			attributes.put("Longitude:", "origLong");
			//try some observation stuff
			attributes.put("observation code:", "plotObservation/authorObsCode");
			attributes.put("start date:", "plotObservation/plotStartDate");
			
			StringBuffer fileBuffer = new StringBuffer();
			StyleSheet stylesheet = new StyleSheet(fileBuffer, attributes);
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	

	
}

