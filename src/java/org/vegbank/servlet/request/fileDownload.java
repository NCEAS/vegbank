package org.vegbank.servlet.request;

/*
 *  '$RCSfile: fileDownload.java,v $'
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-07-02 17:26:49 $'
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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.servlet.util.GetURL;
import org.vegbank.servlet.util.ServletUtility;

import xmlresource.utils.transformXML;

/**
 * Servlet to perform file downloading operations 
 *
 * <p>Valid parameters are:<br><br>
 * action=actionDownload -- initiates the file download process using the
 *	attributes listed below <br>
 * debugLevel -- if this value is one then debugging/processing info is written
 *	to the client browser <br>
 * fileName -- the name that the user is applying to the downloaded file <br>
 * formatType -- the downloaded file format type (incl: xml, html, ascii) <br>
 * userNotes -- short note text that will be included in downloaded file header<br>
 * dataType -- type of plot information desired in the downloaded file - may
 * 	include entire plot, species list, or environmental data<br>
 * aggregationType -- describes how the downloaded data is to be clustered
 * 	- including gzipped single file, zipped multiple files, or both<br>
 * compression -- if no aggregation type is specifed then use this compression<br>
 * fileNamePath -- the downloadPath plus fileName <br>
 * plotRequestList -- the list of plotId numbers that the user is requesting
 * 	data for <br>
 * atomicResultSet -- the file that the results for each individual plot is
 * 	stored in before being concatenated or zipped into the download file <br>
 * cummulativeResultsSet -- the file that the results for the entire list of
 * 	plots are contained within before being compressed/zipped <br>
 *
 * <p>action=upload -- intitiates a file upload to the server<br>
 *
 * @author John Harris
 *
 */

public class fileDownload extends HttpServlet
{

	private ServletUtility sutil = new ServletUtility();
	private GetURL gurl = new GetURL();
	private transformXML transformer = new transformXML();

	private static final String htmlStyleSheet =
		"/usr/local/devtools/jakarta-tomcat/webapps/vegbank/WEB-INF/lib/ascii-treeview.xsl";

	//below are the stylesheets for the flat ascii file tarnsformation process 
	private static final String asciiSitesStyleSheet =
		"/usr/local/devtools/jakarta-tomcat/webapps/vegbank/WEB-INF/lib/plotsites-flatascii.xsl";
	private static final String asciiSpeciesStyleSheet =
		"/usr/local/devtools/jakarta-tomcat/webapps/vegbank/WEB-INF/lib/plotspecies-flatascii.xsl";
	private static final String condensedAsciiSpeciesStyleSheet =
		"/usr/local/devtools/jakarta-tomcat/webapps/vegbank/WEB-INF/lib/plotspecies-condensedascii.xsl";

	/**
	 * constructor method -- just to show status on startup
	 */
	public fileDownload()
	{
		System.out.println("fileDownload: init");
	}

	/** Handle "POST" method requests from HTTP clients */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		doGet(request, response);
	}

	/** Handle "GET" method requests from HTTP clients */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		try
		{
			String fileName = (String) request.getParameter("fileName");
			String fileFormatType = (String) request.getParameter("formatType");
			String aggregationType = (String) request.getParameter("aggregationType");
			String userNotes = (String) request.getParameter("userNotes");
			String dataType = (String) request.getParameter("dataType");
			String plots = (String) request.getParameter("plots");

			//this is the path and filename of the download file
			//and should be passed to the file copy method the downLoad
			//path should be absolute
			System.out.println("fileDownload > dataType: " + dataType);
			System.out.println("fileDownload > userNotes: " + userNotes);
			System.out.println("fileDownload > aggregationType: " + aggregationType);
			System.out.println("fileDownload > fileFormatType: " + fileFormatType);
			System.out.println("fileDownload > fileName: " + fileName);
			System.out.println("fileDownload > plots: " + plots);

			//request the plots from the database  
			QueryResult qr = new QueryResult();
			dataRequester(plots, qr);	
			String result = this.dataTransformer( fileFormatType, qr );

			// REGISTER THE FILENAMES FOR THE REQUESTED TYPE
			String downloadFileName;
			if ( fileFormatType.equals("html") )
			{
				downloadFileName = "vegbankDownload.html";
			}
			else if ( fileFormatType.equals("flat") || fileFormatType.equals("condensed") )
			{
				downloadFileName = "vegbankDownload.txt";
			}
			else if ( fileFormatType.equals("xml") )
			{
				downloadFileName = "vegbankDownload.xml";
			}
			else
			{
				System.out.println("fileDownload > unknown fileFormatType ");
				downloadFileName = "/vegbankDownload.xml";
			}

			response.setContentType("application/stream");
			if (aggregationType.equals("compress"))
			{
				response.setHeader("Content-Disposition","attachment; filename="+downloadFileName+".gz;"); 
				//compress the file to the appropriate compression type
				byte[] gzippedBA = ServletUtility.gzipCompress( result.getBytes() );
				response.getOutputStream().write(gzippedBA);
			}
			else
			{
				response.setHeader("Content-Disposition","attachment; filename="+downloadFileName+";"); 
				response.getWriter().print(result);
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception  " + e.getMessage());
		}
	}

	/**
	 *  Method that takes as input both the name of the file containing the database 
	 *  plot identification numbers desired for downloading and the dataType, ie. 
	 *  entire plot, species list, or environmental data and returns an xml file 
	 *  containing that data which then must be transformed and compressed in 
	 *  other methods
	 *
	 * @param plotList filename containing the list of the plots desired by the user
	 * @param dataType the type of plot data desired including: species,
	 *	environmental, or entire plot
	 */

	private void dataRequester(String plotList, 	QueryResult qr)
	{
		try
		{
			new FullPlotQuery().execute(qr, plotList);

			System.out.println(
				"############"+ qr.getResultsTotal()+ "##################"
				+ qr.getXMLString()
				+ "############"+ qr.getResultsTotal()+ "##################"
			);
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 *  Method that takes as input both the name of the xml file containing the plots
	 *  desired for downloading and the formatType for the desired plots, ie. 
	 *  html, xml, flat ascii
	 *
	 * @param plotFile filename containing the plots data desired by the user
	 * @param formatType the format of the file desired by the user including
	 *	gzipped single file, zipped, aggregate files, or both
	 */
	private String dataTransformer(String formatType, QueryResult qr)
	{
		System.out.println("fileDownload > dataTransformer working");
		StringBuffer sb = new StringBuffer();
		try
		{
			// the user wants to transform to a flat file -- which is actually two files 
			// one with the species and another with the sites 
			if (formatType.equals("flat"))
			{
				System.out.println("fileDownload > transforming data to flat-ascii -- sites");
				sb.append( transformer.getTransformedFromString( qr.getXMLString(), asciiSitesStyleSheet) );
					
				System.out.println("fileDownload > transforming data to flat-ascii -- species");
				sb.append( transformer.getTransformedFromString( qr.getXMLString(), asciiSpeciesStyleSheet) );
			}
			else if (formatType.equals("html"))
			{
				System.out.println("fileDownload > transforming data to html");
				transformer.getTransformedFromString( qr.getXMLString(), htmlStyleSheet);
			}
			// THIS IS FOR THE CONDENSED ASCII FORMAT -- THE FORMAT THAT IS TO BE 
			// USED BY THE ANALYTICAL TOOLS 
			else if (formatType.equals("condensed"))
			{
				System.out.println("fileDownload > transforming data to condensed-ascii -- sites");
				sb.append( transformer.getTransformedFromString( qr.getXMLString(), asciiSitesStyleSheet) );
				
				System.out.println("fileDownload > transforming data to condensed-ascii -- species");
				sb.append( transformer.getTransformedFromString( qr.getXMLString(), condensedAsciiSpeciesStyleSheet) );
			}
			else
			{
				System.out.println("fileDownload > no transformation");
				sb.append( qr.getXMLString() );
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return sb.toString();
	}
}
