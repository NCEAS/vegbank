import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;


/**
 * This class allows the user to view the summary of the resultset produced by
 * the queries run by the query servlet(s).  These summaries are currently
 * produced by an xsl transform of the results set which exists as an xml file. 
 * These xsl sheets are currently static but in the near future the end user
 * will be able to manipulate the sheets to view the data the he/she desires.
 *
 * <p>Valid parameters are:<br><br>
 * 
 * summaryViewType - type of data to summarize, can include community, 
 	vegPlot, plantTaxa <br>
 * resultType - type of summary result - this is not really currently used <br>
 * downLoadAction - if this is not null then the user wants to download a file
 *
 * @version 12 Feb 2001
 * @author John Harris
 *
 */
public class viewData extends HttpServlet 
{

ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

private String downLoadAction=null;
private String summaryViewType=null;
private String resultType=null;
private String servletDir= null; //like: /opt/jakarta/harris/servlet
private String servletPath=null; //like: /harris/servlet


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

response.setContentType("text/html");
PrintWriter out = response.getWriter();

//grab all the input parameters
downLoadAction=request.getParameter("downLoadAction");
summaryViewType=request.getParameter("summaryViewType");
resultType=request.getParameter("resultType");
//print the input to the system
System.out.println("viewData.doGet - input params > \n downLoadAction: "+
	downLoadAction+" \n summaryViewType: "+summaryViewType+" \n"
	+" resultType: "+resultType);

/**
 * Determine if the user wants to download the data or just to view the summary
 * table  - in the future these options will increase
 *
 * Check to see if the user wants to download the data and if so do below - which
 * is to write a list of the selected plots (checked check-boxes) to the local
 * (lib) directory and then call the fileDownload servlet to seperate the desied
 * plots form the undesired plots and then transform the data into the format
 * specified by the form (download.html) calling the fileDownload servlet
 *
 */

//handle a download request
if (downLoadAction != null) 
{
	try 
	{
	//print out to file the names of the plots and their 
	//plotId's for use by the fileDownload servlet
	
	servletDir = rb.getString("requestparams.servletDir");
	servletPath = rb.getString("servlet-path");
	
 	PrintStream outFile  = new PrintStream(
		new FileOutputStream(servletDir+"plotDownloadList", false));
		
	//use this function to figure out the type and number of inputs
	//it is a temporary function - comment out later
	Enumeration enum =request.getParameterNames();
	while (enum.hasMoreElements()) 
	{
		String name = (String) enum.nextElement();
		String values[] = request.getParameterValues(name);
		if (values != null) 
		{
			for (int i=0; i<values.length; i++) 
			{
				if (name.equals("plotName")) 
				{
					System.out.println(name+" "+values[i]);
					outFile.println(values[i]);	
					out.println(name +" ("+ i + "): "
					+values[i]+"; <br>");
				}
			}
		}
	}
 	//now call the download page
 	response.sendRedirect(servletPath+"pageDirector?pageType=download");	
	}
	catch( Exception e ) 
	{
		System.out.println("servlet failed in: viewData.main"
		+e.getMessage());
	}
}
//If the download page is not requested then request then show the summary
else 
{
	viewResultsSummary(response, out, "fileName", summaryViewType.trim());
}

}



/**
 *  Method to print to screen the results set returned by the query servlet in
 * summary format using an xsl style sheet
 *
 * @param response - the response object linked to the client
 * @param out - the output stream to the client
 * @param fileName - name of the xml file for translation/viewing
 * @param summaryViewType - the type of summary that should be displayed to the
 * browser including: vegPlot, community, plantTaxa
 * 
 */
	private void viewResultsSummary(HttpServletResponse response, 
	PrintWriter out, String fileName, String summaryViewType) 
	{

		String styleSheet=null; //the stylesheet to use
		//get the servlet directory
	//	ResourceBundle rbun = ResourceBundle.getBundle("plotQuery");
		servletDir = rb.getString("requestparams.servletDir");
		try 
		{
			//determine the style sheet to use for the xsl transform
			if (summaryViewType.equals("community")) 
			{
  			styleSheet=servletDir+"showCommunitySummary.xsl";
			}
			if (summaryViewType.equals("plantTaxa")) 
			{
  			styleSheet=servletDir+"showPlantTaxaSummary.xsl";
			}
			//the default (plots) stylesheet
			if (summaryViewType.equals("vegPlot")) 
			{
				///styleSheet="/jakarta-tomcat/webapps/examples/WEB-INF/lib/showSummary.xsl";
				styleSheet=servletDir+"transformMultiPlotSummary.xsl";
			}
			//let the user know that there is a problem with the request
			else 
			{
				out.println("viewData.viewResultsSummary: unknown request for xsl sheet");
			}

			//access the method to transfor the xml document and retrieve the string writer
			transformXML m = new transformXML();
			m.getTransformed(servletDir+"summary.xml", 
			styleSheet);
			StringWriter transformedData=m.outTransformedData;

			//pass the String writer to the utility class to convert the StringWriter to an array
			utility u =new utility();
			u.convertStringWriter(transformedData);
			String transformedString[]=u.outString;  
			int transformedStringNum=u.outStringNum; 
			//print the list of plots to the browser as a summary and then
			//read the further requests such as expanded species, or download
			for (int ii=0;ii<u.outStringNum; ii++) 
			{
				out.println(u.outString[ii]+"");
			}
		}  //end try
		catch( Exception e ) 
		{
				System.out.println("servlet failed in: "
			+"viewData.viewResultsSummary: "
			+e.getMessage());
		}
	}

}
