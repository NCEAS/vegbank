package servlet.usermanagement;
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


import servlet.util.ServletUtility;
import servlet.util.GetURL;
import org.apache.tools.ant.taskdefs.Copy;



public class UserManagementServlet extends HttpServlet 
{
	
	private ServletUtility util = new ServletUtility();
  private GetURL gurl = new GetURL();
	
	//the cookie value is the same as the user name
	//which is the same as the user's email addy
	private String cookieValue;
	
	/** Handle "GET" method requests from HTTP clients */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
  	throws IOException, ServletException 	
		{
    	doPost(request, response);
		}

	/** Handle "POST" method requests from HTTP clients */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
  	throws ServletException, IOException 
		{
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			try 
			{
				//get the parameters
				Hashtable params = util. parameterHash(req);
				System.out.println("UserManagementServlet > in params: " + params  );
				
				//the cookie value is the same as the user name and email addy
				cookieValue = getCookieValue(req);
				String action = getAction( params );
				if (action == null)
				{
					System.out.println("UserManagementServlet > action parameter required: ");
				}
				
				if ( action.equals("options") )
				{
					//copy the actions file and then displat to the browser
					String actionFile = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/general/actions.html";
					String outFile ="/tmp/actions.html";
					String token = "user";
					String value = cookieValue;
					util.filterTokenFile(actionFile, outFile, token, value);
					String s = util.fileToString(outFile);
					out.println(s);
				}
				else if ( action.equals("showfiles") )
				{
					this.showUserFiles(req, res, out);
				}
				else
				{
					System.out.println("UserManagementServlet > unrecognized action: " + action);
				}
		}
			catch (Exception e)
			{
				System.out.println("Exception: "+ e.getMessage() );
				e.printStackTrace();
			}
		}
	
		
	/**
	 * method that returns the desired action requested by the user
	 *
	 * @param params - hashtable with all the servlet parameters
	 *
	 */
	 private String getAction(Hashtable params)
	 {
		 String s = null;
		 try
		 {
		 	s=(String)params.get("action");
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		  return(s);
	 }

	
	
	/**
	 * method to show the user the files that they
	 * have stored in the profile on the server file
	 * system
	 */
	 private void showUserFiles(HttpServletRequest req, HttpServletResponse res,
	 	PrintWriter out)
	 {
		 try
		 {
			 	
				out.println("<html> \n");
				out.println("<head> \n");
				out.println("<body class=\"BODY\" >");
				out.println("<title> Database User Manager: "+ cookieValue+" </title> \n");
				out.println("<link rel=\"STYLESHEET\" href=\"http://numericsolutions.com/includes/default.css\" type=\"text/css\">");
				
				//get the java-script functions into the html here -- later remove this
				//and add a link to an external js file
				out.println( getJavaScriptFunctions() );
				out.println("</head> \n");
				
				//out.println("<br class=\"category\"> Welcome Vegbank User: " + cookieValue +"<br> \n");
				out.println( getNavigationHeader() );
				
				//this is the table that has all the registered queries
				out.println( getUserRegisteredQuerySummary(cookieValue) );
				
				//some space
				out.println("<br> <br>");
				
				//out.println("<br> you have uploaded "+getUserRegisteredFileNum(cookieValue)+"files registered on the server <br>");
				out.println( getUserRegisteredFileSummary(cookieValue) );
				out.println("</body>");
				out.println("</html>");
			 
		 }
		 catch (Exception e)
			{
				System.out.println("Exception: "+ e.getMessage() );
				e.printStackTrace();
			}
	 }
	
	
	/** 
	 * method that returns the navigation header -- which is the template that
	 * is consistent with the content that we have been developing for vegbank
	 */
	private String getNavigationHeader()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<table width=\"87%\" class=\"navigation\"> \n");
		sb.append("<tr>");
		sb.append("<td width=\"10%\"> <a href=\"http://vegbank.nceas.ucsb.edu/forms/uploadFile.html\"> UPLOAD FILE </a></td> \n");
		sb.append("<td  width=\"10%\"> <a href=\"http://vegbank.nceas.ucsb.edu/forms/utilities.html\"> UTILITIES </a></td> \n");
		sb.append("<td  width=\"10%\"> <a href=\"http://vegbank.nceas.ucsb.edu/forms/create_user.html\"> CREATE USER ACCOUNT </a></td> \n");
		sb.append("<td  width=\"10%\"> <a href=\"http://vegbank.org/\"> VEGBANK DOCUMENTATION </a></td> \n");
		//next image is an exclamation
		sb.append("<td  width=\"14%\"> <img src=\"/vegbank/images/icon_cat31.gif\"> <a href=\"http://vegbank.nceas.ucsb.edu/vegbank/sampledata/\"> SAMPLE DATA</a></td> \n");
		sb.append("</tr> \n");
		sb.append("</table>");
		
		//this is the form that uses the java script for negotiation of the query
		//forms
		sb.append("<table  width=\"50%\" class=\"navigation\"> \n");
		sb.append("<tr> \n");
		sb.append("<td> Choose a Query Option </td> \n");
		sb.append("<td>");
		sb.append("<form name=\"form\"> \n");
		sb.append("<select name=\"menu\" onChange=\"MM_jumpMenu('parent',this,0)\"> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/plot-query.html\" selected>Simple Plot Query</option> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/nested-plot-query.html\">Nested Plot Query</option> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/community-query.html\">Vegtation Community Query</option> \n");
		sb.append("<option value=\"http://vegbank.nceas.ucsb.edu/forms/plant-query.html\">Plant Taxonomy Query</option> \n");
		sb.append("</select> \n");
		sb.append("</form> \n");
		sb.append("</td> \n");
		sb.append("</tr> \n");
		sb.append("</table>");
		sb.append("<br> <br>");
		return(sb.toString());
	}
	
	/**
	 * method to return the number of files the user has registered on the 
	 * serever
	 */
	 private String getUserRegisteredFileNum(String userName )
	 {
		  String htmlResults = null;
    try
    {
      //create the parameter string to be passed to the DataRequestServlet -- 
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?action=userfilenum&username="+userName);
			
      //connect to the dataExchaneServlet
			String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange"+sb.toString().trim();
			System.out.println("UserManagementServlet > OUT PARAMETERS: "+uri);
      int port=80;
      String requestType="POST";
      htmlResults = gurl.requestURL(uri);
    }
    catch( Exception e )
    {
      System.out.println("Exception :  "
      +e.getMessage());
			e.printStackTrace();
    }
    return(htmlResults);
	 }
	
	
	
	/**
	 * method to return a summary of the query files the user has registered 
	 * on the server
	 */
	 private String getUserRegisteredQuerySummary(String userName )
	 {
		  String htmlResults = null;
    try
    {
      //create the parameter string to be passed to the DataRequestServlet -- 
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?action=userfilesummary&username="+userName);
			
      //connect to the dataExchaneServlet
			String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange"+sb.toString().trim();
			System.out.println("UserManagementServlet > OUT PARAMETERS: "+uri);
      int port=80;
      String requestType="POST";
      String s = gurl.requestURL(uri);
			
			//get all the files back for the main table -- pass the method the 
			// all flag
			htmlResults  = getQueryFileDataTable( s, "query", "Cached Queries");
    }
    catch( Exception e )
    {
      System.out.println("Exception:  "
      +e.getMessage());
    }
    return(htmlResults);
	 }
	 
		/**
	 * method that takes tabular data and retuns the data in an html table 
	 * with the appropriate markup -- this function is specific to tabularizing
	 * the files which the user uploaded to the file system
	 * @param data the data String that is returned from the database class and
	 *	kinda a funky looking fiting structure which is pipe deleimeted and each
	 * 	line is terminated by (**)
	 *
	 * @param userFileStartString the start string of the  file name assigned by
	 * the client user -- this can be used to return a table having like file
	 * names where for instance the term query can be applied  
	 */
	private String getQueryFileDataTable(String data, 
		String userFileStartString, String tableName)
	{
		//System.out.println("UserManagementServlet > tabularizing data: " + data);
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellpadding=0 cellspacing=0 width=75% class=\"filetable\"> \n");
		//put the name of the table here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" > <td> "+tableName+"</td> <td></td> <td></td> <td></td> </tr>");
		//put the table header here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" >");
		sb.append("<td>Run</td>");
		sb.append("<td>Delete</td>");
		sb.append("<td>View</td>");
		sb.append("<td>Date</td>");
		sb.append("<tr>");
		//notice the end-of-line token is two *
		StringTokenizer tok = new StringTokenizer(data, "**"); 
		while ( tok.hasMoreTokens()  ) 
		{
			String buf = tok.nextToken();
			sb.append("<tr  class=\"itemsmall\">  \n");
			
			StringTokenizer tok2 = new StringTokenizer(buf, "|"); 
			int colCnt = 0;
			String userFileName = null;
			String createDate = null;
			String accessionNumber = null;
			String fileType = null;
			String buf2 = null;
			
			//parse the columns into the cells
			while (tok2.hasMoreTokens() )
			{
				colCnt++;
				if ( colCnt == 1 )
					userFileName = tok2.nextToken();
				else if ( colCnt == 2 )
					accessionNumber = tok2.nextToken();
				else if ( colCnt == 4 )
					createDate = tok2.nextToken();
				else if ( colCnt == 3 )
					fileType = tok2.nextToken();
				else
					buf2 = tok2.nextToken();
			}
				if (accessionNumber != null && userFileName.startsWith("query") )
				{
					//first add the option to delete the file then add the file name, date and
					//type
					sb.append("	<td width=\"8%\"><input type=\"image\"  src=\"/vegbank/images/runIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					//sb.append("	<td width=\"8%\"><input type=\"image\"  src=\"/vegbank/images/deleteIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					//sb.append("	<td width=\"10%\"><input type=\"checkbox\" name=\""+accessionNumber+"\" ></td> \n");
					
					//here is the function to delet a file
					sb.append("	<td width=\"8%\"> <a href=\"http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange?action=deletefile&filenumber="+accessionNumber+"&username="+this.cookieValue+"\"> " 
					+" <img src=\"/vegbank/images/deleteIcon.gif\">  </a> </td> \n");
					
					sb.append("	<td width=\"25%\"> <a href=\"http://vegbank.nceas.ucsb.edu/uploads/"+accessionNumber+"\">"  + userFileName + "</a> </td> \n");
					sb.append("	<td>" +createDate+ "</td> \n");
					//sb.append("	<td>" +fileType+ "</td> \n");
					sb.append("</tr> \n \n");
				}
			}
		
		sb.append("</table> \n");
		return(sb.toString() );
	}
	
	
	
	
	
	
	/**
	 * method to return a summary  of files the user has registered on the 
	 * serever
	 */
	 private String getUserRegisteredFileSummary(String userName )
	 {
		  String htmlResults = null;
    try
    {
      //create the parameter string to be passed to the DataRequestServlet -- 
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?action=userfilesummary&username="+userName);
			
      //connect to the dataExchaneServlet
			String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange"+sb.toString().trim();
			System.out.println("UserManagementServlet > OUT PARAMETERS: " + uri);
      int port=80;
      String requestType="POST";
      String s = gurl.requestURL(uri);
			
			//get all the files back for the main table -- pass the method the 
			// all flag
			htmlResults  = getUploadedFileDataTable( s, "all", "Registered Files" );
    }
    catch( Exception e )
    {
      System.out.println("Exception:  "
      +e.getMessage());
    }
    return(htmlResults);
	 }
	
	/**
	 * method that takes tabular data and retuns the data in an html table 
	 * with the appropriate markup -- this function is specific to tabularizing
	 * the files which the user uploaded to the file system
	 * @param data the data String that is returned from the database class and
	 *	kinda a funky looking fiting structure which is pipe deleimeted and each
	 * 	line is terminated by (**)
	 *
	 * @param userFileStartString the start string of the  file name assigned by
	 * the client user -- this can be used to return a table having like file
	 * names where for instance the term query can be applied  
	 */
	private String getUploadedFileDataTable(String data, 
		String userFileStartString, String tableName)
	{
		//System.out.println("UserManagementServlet > tabularizing data: " + data);
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellpadding=0 cellspacing=0 width=75% class=\"filetable\"> \n");
		//put the name of the table here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" > <td> "+tableName+"</td> <td></td> <td></td> <td></td> </tr>");
		//put the table header here
		sb.append("<tr class=\"item\" bgcolor=\"#9999FF\" >");
		sb.append("<td>Load</td>");
		sb.append("<td>Delete</td>");
		sb.append("<td>File Name</td>");
		sb.append("<td>Upload Date</td>");
		sb.append("<td>Type</td>");
		sb.append("<tr>");
		//notice the end-of-line token is two *
		StringTokenizer tok = new StringTokenizer(data, "**"); 
		while ( tok.hasMoreTokens()  ) 
		{
			String buf = tok.nextToken();
			sb.append("<tr  class=\"itemsmall\">  \n");
			
			StringTokenizer tok2 = new StringTokenizer(buf, "|"); 
			int colCnt = 0;
			String userFileName = null;
			String createDate = null;
			String accessionNumber = null;
			String fileType = null;
			String buf2 = null;
			
			//parse the columns into the cells
			while (tok2.hasMoreTokens() )
			{
				colCnt++;
				if ( colCnt == 1 )
					userFileName = tok2.nextToken();
				else if ( colCnt == 2 )
					accessionNumber = tok2.nextToken();
				else if ( colCnt == 4 )
					createDate = tok2.nextToken();
				else if ( colCnt == 3 )
					fileType = tok2.nextToken();
				else
					buf2 = tok2.nextToken();
			}
			//first add the option to delete the file then add the file name, date and
			//type
			
			
				if (accessionNumber != null &&  ! userFileName.startsWith("query") )
				{
					//sb.append("	<td width=\"5%\"><input type=\"image\"  src=\"/vegbank/images/funnelIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					//sb.append("	<td width=\"8%\"><input type=\"image\"  src=\"/vegbank/images/deleteIcon.gif\" value=\"test\" name=\""+accessionNumber+"\" > </td> \n");
					
					//here is the function to init the data loading process, where by the 
					//filename, url, file type, submitter name are issued to the 
					//data loading plugin on the frame work servlet which will describe
					//the data set in a form that the user can use to upload some or all
					//of the plots
					sb.append("	<td width=\"8%\"> "
					+"<a href=\"http://guest06.nceas.ucsb.edu/framework/servlet/framework?action=initPlotLoad&filename="
					+accessionNumber+"&username="+this.cookieValue+"&plot=all&filetype=tnc&datafileurl=vegbank.nceas.ucsb.edu/framework/servlet/dataexchage\"> " 
					+" <img src=\"/vegbank/images/funnelIcon.gif\">  </a> </td> \n");
					
					
					
					//here is the function to delet a file
					sb.append("	<td width=\"8%\"> "
					+"<a href=\"http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange?action=deletefile&filenumber="
					+accessionNumber+"&username="+this.cookieValue+"\"> " 
					+" <img src=\"/vegbank/images/deleteIcon.gif\">  </a> </td> \n");
					
					sb.append("	<td> <a href=\"http://vegbank.nceas.ucsb.edu/uploads/"+accessionNumber+"\">"  + userFileName + "</a> </td> \n");
					sb.append("	<td>" +createDate+ "</td> \n");
					sb.append("	<td>" +fileType+ "</td> \n");
					sb.append("</tr> \n \n");
				}
		}
		sb.append("</table> \n");
		return(sb.toString() );
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
				System.out.println("UserManagementServlet > cleint passing the cookie name: "+cookieName+" value: "
					+cookieValue);
			}
  	}
		return(cookieValue);
	}
	
	
	//method that retuns the java script functions for this page -- in the 
	//future consider using a link to an extrenal java script file
	private String getJavaScriptFunctions()
	{
		//there is only one java script file here and it is the 
		//one that will allow a user to choose a query type
		StringBuffer sb = new StringBuffer();
		sb.append("<script language=\"JavaScript\"> \n");
		sb.append("<!-- \n");
		sb.append("function MM_jumpMenu(targ,selObj,restore){ //v3.0 \n");
  	sb.append("eval(targ+\".location='\"+selObj.options[selObj.selectedIndex].value+\"'\"); \n");
  	sb.append("if (restore) selObj.selectedIndex=0; \n");
		sb.append("} \n");
		sb.append("//--> \n");
		sb.append("</script> \n");
		
		return(sb.toString() );

	}
	
}

