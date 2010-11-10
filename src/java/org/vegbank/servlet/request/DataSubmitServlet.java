package org.vegbank.servlet.request;

/*
 *  '$RCSfile: DataSubmitServlet.java,v $'
 *
 *	'$Author: mlee $'
 *  '$Date: 2005-05-30 17:17:13 $'
 *  '$Revision: 1.31 $'
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

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.Constants;
import org.vegbank.common.command.Query;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.communities.datasource.VegCommunityLoader;
import org.vegbank.databaseAccess.CommunityQueryStore;
import org.vegbank.plots.datasource.PlotDataSource;
import org.vegbank.plots.rmi.DataSourceClient;
import org.vegbank.common.utility.datafileexchange.DataFileExchange;
import org.w3c.dom.Document;

import org.vegbank.xmlresource.XMLparse;
import org.vegbank.xmlresource.transformXML;


public class DataSubmitServlet extends HttpServlet implements Constants
{

	private String submitDataType = null;
	// FIXME: Should be in properties
	private static String commUpdateScript = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/update_community_summary.sql";
	private static String communityValidationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/community-submit_valid.html";
	//private static String communityValidationForm = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/valid.html";

	private static String plotSelectTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/plot-submit-select.html";
	private static String plotSelectForm  = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/plot_select.html";
	private static String genericTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/generic_form.html";
	//this is the file that has the updated tokens and should be shown to the client
	private static String plotSubmittalInitForm = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/plot-valid.html";
	private static String plotSubmittalInitTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/plot-submit.html";
	// END FIXME: Should be in properties

	// Construct Objects
	private static final ServletUtility su = new ServletUtility();
	private static final XMLparse parser = new XMLparse();

	// ResourceBundle properties
	private static ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private static final String rmiServer= rb.getString("rmiserver");
	private static final String mailHost = rb.getString("mailHost");
	private static final String cc = rb.getString("systemEmail");
	private static final int rmiServerPort = 1099;
	private static String uploadDir = rb.getString("uploadDir");
	private static String plotsArchiveFile =  uploadDir + "/input.data";



	//private String browserType = "";
	// THESE VARIBLES ARE USED BY THE VARIOUS SUBMITTAL ROUTINES


	/**
	 * constructor method
	 */
	public DataSubmitServlet()
	{
		try
		{
			System.out.println("init: DataSubmitServlet");

			// If the name of the RMI Server is false then don't try to connect ...
			if ( rmiServer.equals("false") )
			{
				System.out.println("DataSumbitServlet >This System is configured"
					+" to run without an RMIServer, any functions dependant on"
					+ " RMI will fail. The \"vegbank/build.properties file can be"
					+ " used to set an RMIServer\"");

			}
			else
			{
				System.out.println("DataSumbitServlet > init rmiserver: " + rmiServer);
				//construct a new instance of the rmi client
				DataSourceClient rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}



	/** Handle "POST" method requests from HTTP clients */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
 	 throws IOException, ServletException
		{
			System.out.println("IN DoPost");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			try
			{
				Long usrId = ServletUtility.getUsrIdFromSession(request);
				//Long usrId = (Long)request.getSession().getAttribute(Constants.USER_KEY);
				String userName = null;
				String salutation = null;
				String surName = null;
				String givenName = null;
				String organizationname = null;
				long permissionType = 0;
				WebUser user = null;

				if (usrId != null && usrId.longValue() != 0)
				{
					user = (new UserDatabaseAccess()).getUser(usrId);
					userName = user.getUsername();
					salutation = user.getSalutation();
					surName = user.getSurname();
					givenName = user.getGivenname();
					organizationname = user.getOrganizationname();
					permissionType = user.getPermissiontype();
				}

				System.out.println("DataSubmitServlet > current user email: " + user);
				System.out.println("DataSubmitServlet > current user salutation: " + salutation);
				System.out.println("DataSubmitServlet > current user surName: " + surName);
				System.out.println("DataSubmitServlet > current user givenName: " + givenName);
				System.out.println("DataSubmitServlet > current user organizationname: " + organizationname);
				System.out.println("DataSubmitServlet > current user permission lev: " + permissionType);
				if ( permissionType <= 1)
				{
					// don't let the user any further into the loading process
					System.out.println("DataSubmitServlet > ## this user does not have the appropriate permissions");
					this.handleInvalidPermissions(response, userName);
				}
				else
				{
					String contentType =  request.getContentType();
					System.out.println("DataSubmitServlet >contentType == " + contentType);
					if (contentType != null && contentType.startsWith("multi") )
					{
						DataFileExchange dfe = new DataFileExchange(request);

            String action = dfe.getMultiPartParameter("action");
            String submitDataType = dfe.getMultiPartParameter("submitDataType");
            System.out.println("DataSubmitServlet > action: " + action);
            System.out.println("DataSubmitServlet > submitDataType " + submitDataType);

            if (action.equals("upload") && submitDataType.equals("vegPlot") )
            {
						  if ( dfe.uploadMultiPartFile() )
						  {
							  // Success
							  System.out.println("DataSubmitServlet > file is LOADED");
						  }
						  else
						  {
							  // FAIL
							  System.out.println("DataSubmitServlet > file  FAILED to load ... oh oh");
						  }
              // Get the plotsArchiveType from the session
              String plotsArchiveType = (String) request.getSession().getAttribute("plotsArchiveType");
              System.out.println("DataSubmitServlet > plotsArchiveType: " + plotsArchiveType);


              StringBuffer sb = handleVegPlotUpload(plotsArchiveType, request);
						  out.println( sb.toString() );
            }
            else
            {
              // I do not expext this
              System.out.println(
                "DataSubmitServlet > a mutipart request with action: " + action
                +" and submitDataType: " + submitDataType);
            }
					}
					else
					{
						//Hashtable params = new Hashtable();
						//params = su.parameterHash(request);
						// there will be cases where there will be multiple parameters
						// with the same name and they all need to be accessed so
						// capture an ennumeration here
						Enumeration anenum = request.getParameterNames();

						//get the browser type
						String browserType = su.getBrowserType(request);


						System.out.println("DataSubmitServlet > IN PARAMETERS: "+ su.printParameters(request) );
						System.out.println("DataSubmitServlet > IN ATTRIBUTES: "+ su.printAttributes(request) );
						submitDataType = request.getParameter("submitDataType");
						String action = request.getParameter("action");
						System.out.println("DataSubmitServlet > browserType: " + browserType);
						System.out.println("DataSubmitServlet > submit data type: " + submitDataType);
						System.out.println("DataSubmitServlet >> action : '" + action + "'");



						// FIGURE OUT WHAT TO DO WITH THE REQUEST
						if ( submitDataType.trim().equals("vegCommunity") )
						{
							AddCommunity ac = new AddCommunity();
							StringBuffer sb = ac.execute(request, response);
							out.println( sb.toString() );
						}
						else if ( submitDataType.trim().equals("vegCommunityCorrelation")  )
						{
							submitDataType.trim().equals("vegCommunityCorrelation");
							StringBuffer sb = handleVegCommunityCorrelation(request, response);
							out.println( sb.toString() );
						}
						else if ( submitDataType.trim().equals("vegPlot")  )
						{
							//out.println("DataSubmitServlet > action unknown!");
							StringBuffer sb = handleVegPlotSubmittal(anenum, request, response, userName, user);
							out.println( sb.toString() );
						}
						else if ( submitDataType.trim().toUpperCase().equals("PLANTTAXA")  )
						{
							AddPlant ap = new AddPlant();
							StringBuffer sb = ap.execute(request, response, user);
							out.println( sb.toString() );
						}
						else
						{
							out.println("DataSubmitServlet > action unknown!");
						}
					}
				}
			}
			catch( Exception e )
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
		}


	/** Handle "GET" method requests from HTTP clients */
	public void doGet(HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException
	{
		System.out.println("GET Method called !!!");
		doPost(request, response);
	}


	/**
	 * method that retuns a page to the browser that explains to the user
	 * that he/she does not have the appropritate priveleges to load data
	 * to the system and that they should fill in the certification page
	 * and submit that
	 *
	 * @param response -- the http response object
	 */
	 private void handleInvalidPermissions(HttpServletResponse response, String user)
	 {
		 StringBuffer message = new StringBuffer();
		 try
		 {
			  message.append("<span class=\"category\"> INVALID PERMISSIONS ERROR </span> <br> <br> \n");
				message.append("<span class=\"item\"> User: "+user+" Does Not Have Permission to Load Data To Vegbank <br> \n");
				message.append("In order to attain this privilege, please fill out the certification form at: \n");
				message.append("<a href=\"/vegbank/LoadCertification.do\">Certification Form</a>");
				message.append("</span>");

				Hashtable replaceHash = new Hashtable();
				// party-related attributes
				replaceHash.put("messages", message.toString() );

				StringWriter output = new StringWriter();
				su.filterTokenFile(genericTemplate, output, replaceHash);

				PrintWriter out = response.getWriter();
				out.println( output.toString() );

		 }
		 catch( Exception e )
		 {
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		 }
	 }

	 /**
	  * mrthod to read, format and return as a string the plot submittal
		* initaition page.
		*/
		private String updatePlotInitPage(
			String user,
			String salutation,
			String givenName,
			String surName,
			String organizationname)
		{
			StringWriter output = new StringWriter();
			try
			{
				// set up the filter tokens
				Hashtable replaceHash = new Hashtable();
				replaceHash.put("emailAddress", ""+user);
				replaceHash.put("salutation", ""+salutation);
				replaceHash.put("givenName", ""+givenName);
				replaceHash.put("surName", ""+surName);
				replaceHash.put("organizationname", ""+organizationname);
				su.filterTokenFile(plotSubmittalInitTemplate, output, replaceHash);
			}
			catch( Exception e )
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return(output.toString());
		}

	/**
	 * this method is used to handle the submittal of a vegplot into the
	 * VegBank database using for the insertion of either an MS access file
	 * or an XML document, using the rmi datasource client / server system
	 * where the server is running on a  win-nt machine.  Steps handeled by
	 * this method include <br> <br>
	 *
	 * 1] validating the user <br>
	 * 2] accepting an uploaded data file <br>
	 * 3] passing the uploaded archive file to the RMI server <br>
	 * 4] issueing the request for a plot(s) upload at the RMI server <br>
	 * 5] passing the receipt back to the client <br>
	 * 6] emailing the client the receipt for the insertion of their data <br>
	 * <br> <br>
	 * @see DataSourceClient
	 * @param anenum -- the input parameters to the parent servlet
	 * @param params -- a hashtable with the unique parameters sent to the servlet
	 * @param request -- http request sent to the servlet
	 * @param response -- http reponse sent to the servlet
	 *
	 */
	 private StringBuffer handleVegPlotSubmittal(
	 	Enumeration anenum,
	 	HttpServletRequest request,
	 	HttpServletResponse response,
	 	String user,
	 	WebUser userBean)
	{
		StringBuffer sb = new StringBuffer();

		String salutation = userBean.getSalutation();
		String surName = userBean.getSurname();
		String givenName = userBean.getGivenname();
		String organizationname = userBean.getOrganizationname();

		try
		{
			String inputEmail = "";
			String action = request.getParameter("action");
			System.out.println("DataSubmitServlet > action: " + action);

			PrintWriter out = response.getWriter();
			String htmlContents = "";

			// THE FIRST STEP IS THE PREINIT STEP WHICH TAKES PLACE WHEN THE USER
			// HITS THE LINK TO UPLOAD PLOT DATA, THIS STEP LOOKS UP THE
			// USERS INFO AND RETURNS A FORM WITH THEIR INFO AND THE FORM
			// TO SELECT THE PLOT TYPE
			if ( action.equals("preinit") )
			{
				System.out.println("DataSubmitServlet > loading plots preinit ");
				// GET THE FORM WITH THE UPDATED ATTRIBUTES AND SEND IT TO THE USER
				htmlContents = updatePlotInitPage(user, salutation, givenName, surName, organizationname);
				out.println(htmlContents);
			}

			else if ( action.equals("init") )
			{
				//get the paramter refering to the plot archive type (ie tnc, vbaccess, nativexml)
				String plotFileType = request.getParameter("plotFileType");

				//quick hack to cahnge the type of plots archive
				String plotsArchiveType = plotFileType;
				System.out.println("DataSubmitServlet > plotsArchiveType: " + plotsArchiveType);
			  // put this into the users session for retrival latter
        request.getSession().setAttribute("plotsArchiveType", plotsArchiveType);

				// check that the uer has valid priveleges to load a data
				// file to the database and if so give the use a window
				// to upload some data
				if ( request.getParameter("emailAddress") != null )
				{
					inputEmail = request.getParameter("emailAddress");
					//make sure that the login email matches the passed here
					if ( inputEmail.equals(user) )
					{
						//redirect the user to the data selection form to either
						//upload or choose from a previosly uploaded file
						response.sendRedirect("/vegbank/forms/plot-upload.html");
					}
					// if not then send the user to a login
					else
					{
						//if the user did not pass in an email send them back
						if (inputEmail.length() < 2)
						{
							sb.append("<b>Please Go Back and enter a valid email login</b> ");
						}
						//they are just not logged in
						else
						{
							sb.append("<a href=\"\\\">You are not logged in! </a> ");
						}
					}
				}
			}
			// this is the actual submittal of one or many plots from the
			// uploaded archive
			else if ( action.equals("submit") )
			{
				String receiptType  = request.getParameter("receiptType");
				//the plot file can only be tnc, vbaccess, nativexml
				//String plotFileType = request.getParameter("plotFileType");

				System.out.println("DataSubmitServlet > requesting a receipt type: "+receiptType );
				//sleep so that admin can see the debugging

				DataSourceClient rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);

				while (anenum.hasMoreElements())
				{
					String name = (String) anenum.nextElement();
					if ( name.toUpperCase().equals("PLOTID") )
					{
						String values[] = request.getParameterValues(name);
						if (values != null)
						{
							for (int i = 0; i < values.length; i++)
							{
								String thisPlot = values[i];
								System.out.println(
									"DataSubmitServlet > requesting an rmi plot insert: '"
										+ values[i]
										+ "'");
								//insert the plot over the rmi system -- first test that the plot is valid
								System.out.println(
									"DataSubmitServlet > plot being loaded by: "
										+ user);

								// get the plotsArchiveType from the session
								String plotsArchiveType =
									(String) request.getSession().getAttribute(
										"plotsArchiveType");
								System.out.println(
									"DataSubmitServlet > About to insert Plot");
								String result =
									rmiClient.insertPlot(
										thisPlot,
										plotsArchiveType,
										user);
								//System.out.println("DataSubmitServlet > Plot insert result = '" + result + "'");
								String receipt =
									getPlotInsertionReceipt(
										thisPlot,
										result,
										receiptType,
										i,
										values.length,
										su.getBrowserType(request));
								sb.append(receipt);
								System.out.println(
									"DataSubmitServlet > requesting a receipt: '"
										+ i
										+ "' out of: "
										+ values.length);
							}
						}
					}
				}
				System.out.println("DataSubmitServlet > requesting an rmi insert ");
				// email the receipt to the user
				System.out.println("DataSubmitServlet > emailing the receipt to the user: " + user);
				this.emailPlotSubmitalReceipt(user, sb.toString() );
				// Remove the lock
				String location =
					(String) request.getSession().getAttribute("rmiFileUploadLocation");
				rmiClient.releaseFileUploadLocation(location);
			}
			else
			{
				System.out.println("DataSubmitServlet > unknown plot submital task ");
			}
		}
		catch( Exception e )
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(sb);
	}


  private StringBuffer handleVegPlotUpload(String plotsArchiveType, HttpServletRequest request)
  {
    StringBuffer sb = new StringBuffer();
		DataSourceClient rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);

	  System.out.println("DataSubmitServlet > file type: " +  plotsArchiveType);
	  Vector plots = new Vector();

	  //take the file that was just deposited via the data exchange servlet
		//and pass it onto the winnt machine if it is an mdb file
		if ( ! plotsArchiveType.equals("nativexml") )
		{
		  System.out.println("DataSubmitServlet > using RMI client to pass file: " + plotsArchiveFile );
			System.out.println("DataSubmitServlet > instantiating an rmi client on: " + rmiServer);

      // Need to use the RMIServer to read a mdb file
      rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);

      // Is the RMI Server free
      String location = rmiClient.getFileUploadLocation();


      if (location == null)
      {
      	System.out.println("DataSubmitServlet > The RMI server is busy, no location to save file");
      	// Display error message and end
				StringWriter sw = new StringWriter();
				Hashtable replaceHash = new Hashtable();
				replaceHash.put("messages","Sorry the system is busy, try again later");
				su.filterTokenFile(genericTemplate, sw, replaceHash);
				sb.append(sw.toString());
				return sb;
      }
      else
      {
				boolean sendResults = rmiClient.putMDBFile(plotsArchiveFile, plotsArchiveType, location);

				System.out.println("DataSubmitServlet > RMI file send results: " + sendResults);

				//validate that the plot archive is real
				boolean fileValidityResults = rmiClient.isMDBFileValid(location);
				System.out.println("DataSubmitServlet > file validity at RMI server: " + fileValidityResults);

				// get the name of the plots and update the selection form and redircet
				// the browser there
				System.out.println("DataSubmitServlet > instantiating an rmi client on: " + rmiServer);
				rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);
				plots = rmiClient.getPlotNames(plotsArchiveType);

				request.getSession().setAttribute("rmiFileUploadLocation", location );
      }

		}
		else
		{
			// TODO: Remove hard coding
			PlotDataSource pds = new PlotDataSource("VegbankXMLPlugin");
			plots = pds.getPlotNames();
		}

		System.out.println("DataSubmitServlet > number of plots in archive: " + plots.size() );
		// prepare the plots element
		StringBuffer sb2 = new StringBuffer();
		for (int i=0; i<plots.size(); i++)
		{
			sb2.append("<option>" + (String)plots.elementAt(i) + "</option> \n");
			System.out.println("DataSubmitServlet > add plot: " + (String)plots.elementAt(i) );
		}

		//create the form
		StringWriter output = new StringWriter();
		try
    {
      FileReader inFile = new FileReader(plotSelectTemplate);
		  su.filterTokenFile(inFile, output, "plots", sb2.toString() );
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
		sb.append( output.toString() );
    return sb;
  }

  /**
	  * this method takes the xml report from the from the plot validation module
		* and then returns an html receipt that can be presented to the user attemplting
		* to load the plot
		* @param report -- the plot validation report from the plot validation module:
		*  <plotValidationReport>
		*			<failedValidationAttribute>
		*				<dbTable>
		* 			<dbAttribute>
		*				<methodName>
		*				<methodParams>
		*				<failedTarget>
		*				<constraints>
		*					<constraint>
		*				</constraints>
		* @return html -- the html receipt
		*/
		private String getPlotValidationReceipt(String plotValidationRept)
		{
			StringBuffer sb = new StringBuffer();
			try
			{
				System.out.println("receipt: " + plotValidationRept);
				transformXML trans  = new transformXML();
				String tr = trans.getTransformedFromString(plotValidationRept.replace('&', '_' ), "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/validation-report.xsl");
				sb.append(tr);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return(sb.toString() );
		}



	/**
	 * this method will email the plot submital receipt form to the
	 * user who has uploaded the plot archive file to the server
	 *
	 * @param inputEmail -- the email address of the user
	 * @param receipt -- string representation of the insertion receipt
	 * @see ServletUtility -- the class that handles the email process.  the
	 * method is called 'sendEmail'
	 */
	 private void emailPlotSubmitalReceipt(String inputEmail, String receipt) throws MessagingException
	 {
		String from = "vegbank";
		String to = inputEmail;
		String subject = "VEGBANK PLOT INSERTION RECEIPT";
	 	String body = receipt;
		//su.sendEmail(this.mailHost, from, to, this.cc, subject, body);
		su.sendHTMLEmail(mailHost, from, to, cc, subject, body);
	 }


	/**
	 * method that for a given plot will grab some of the salient stats for that
	 * plot into an html table that can be used elsehere.  Also the loading results
	 * from the rmi client are passed into this method so that they can elso be
	 * embedded into the table the table will look somethink like what is below for each
	 * plot:
	 *
	 *  <table>
	 * 		<tr> <td> plot name </td> <td> $plotname </td>
	 * 		<tr> <td> state </td> <td> $state </td>
	 * 		<tr> <td> named place </td> <td> $named place </td>
	 * 		<tr> <td> community  </td> <td> $community </td>
	 *	</table>
	 *
	 * @see DataSourceClient
	 * @param plot  -- the string name of the plot as it used in the users archive
	 * @param results -- the string results that have been returnd by the loader
	 *		which is the rmi client -- this is strictly an XML document
	 * @param receiptType -- 'minimal' or 'extensive'
	 * @param curPlotNumber -- the current number of the plots that to be inserted
	 * 	- if this is the first then the xml 1.0 header will be attached here
	 * 		this pllt should start at zero
	 * @param totalPlotNumber -- the total number of plots for which a receipt
	 *  will be requested for
	 */
	 private String getPlotInsertionReceipt(
	 	String plot,
	 	String results,
	 	String receiptType,
	 	int curPlotNumber,
	 	int totalPlotNumber,
	 	String browserType)
	 {
	 		StringBuffer sb = new StringBuffer();
			System.out.println("DataSubmitServlet > currentPlot: " + curPlotNumber +" total number: "+ totalPlotNumber );
	 		try
			{
				if (receiptType.equals("extensive") )
				{
					if ( browserType.equals("msie") )
					{
						//if there is only one plot for which a receipt will be prepared
						if ( curPlotNumber == 0 && (totalPlotNumber == 1) )
						{
							sb.append("<?xml version=\"1.0\"?> \n");
							sb.append("<plotInsertionReceipt> \n");
							sb.append(results);
							sb.append("</plotInsertionReceipt>");
						}
						else if ( curPlotNumber == 0 && (totalPlotNumber > 1)  )
						{
							sb.append("<?xml version=\"1.0\"?> \n");
							sb.append("<plotInsertionReceipt> \n");
							sb.append(results);
						}
						else if ( (curPlotNumber+1) == (totalPlotNumber) )
						{
							System.out.println("DataSubmitServlet > closing the receipt tags");
							sb.append(results);
							sb.append("</plotInsertionReceipt>");
						}
						else
						{
							sb.append( results );
						}
					}
					else
					{
						//the loading results
						// first transform the xml that is returned as the results string
						// into an html body
						transformXML trans  = new transformXML();
						String tr = trans.getTransformedFromString(results, "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/ascii-treeview.xsl");
						sb.append("<table> \n");
						sb.append(" <tr> \n");
						sb.append(" 	<td> Loading Results </td> \n");
						sb.append(" </tr> \n");
						sb.append("</table> \n");
						sb.append(tr);
						sb.append("<br>");
					}
				}
				else if (receiptType.equals("minimal") )
				{
					//System.out.println("DataSubmitServlet > Get minimal receipt from results ='" + results + "'");
					String s = getMinimalInsertionReceipt(plot, results);
					sb.append( s );
				}
				else
				{
					System.out.println("DataSubmitServlet > unrecognized receipt type: " + receiptType );
				}
			}
			catch( Exception e )
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return( sb.toString() );
	 }

	 /**
	  * method that returns the most salient attributes of a plot that were
		* loaded to the database as an html table -- this method will be called when
		* the user request a 'minimal' receipt for the plots that they are attempting
		* to load to the vegbank
		* @param plot  -- the string name of the plot as it used in the users archive
	  * @param results -- the string results that have been returnd by the loader
	  *		which is the rmi client -- this is strictly an XML document
		*
		*/
		private String getMinimalInsertionReceipt(String plot, String results)
		{
			StringBuffer sb = new StringBuffer();
			try
			{
				Document doc = parser.getDocumentFromString(results);
				Vector accessionNumber = parser.getValuesForPath(doc, "/plotInsertion/accessionNumber");
				Vector authorPlotCode = parser.getValuesForPath(doc, "/plotInsertion/authorPlotCode");
				Vector latitude = parser.getValuesForPath(doc, "/plotInsertion/latitude");
				Vector longitude = parser.getValuesForPath(doc, "/plotInsertion/longitude");
				Vector state =  parser.getValuesForPath(doc, "/plotInsertion/state");
				Vector exceptions = parser.getValuesForPath(doc, "/plotInsertion/exceptionMessage");
				Vector insertion = parser.getValuesForPath(doc, "/plotInsertion/insert");

			//	String plotName = rmiClient.getAuthorPlotCode(plot);
			//	String state = rmiClient.getState(plot);
			//	String community= rmiClient.getCommunityName(plot);

				//plot name
				sb.append("<table> \n");
			//	sb.append(" <tr bgcolor=\"#DFE5FA\"> \n");
			//	sb.append(" 	<td> Plot Name: </td> \n");
			//	sb.append(" 	<td> "+plotName+" </td> \n");
			//	sb.append(" </tr> \n");

				//accession number
				sb.append(" <tr bgcolor=\"#DFE5FA\"> \n");
				sb.append(" 	<td> VegBank Identifier: </td> \n");
				sb.append(" 	<td> "+accessionNumber.toString()+" </td> \n");
				sb.append(" </tr> \n");

				//Authors Identifier
				sb.append(" <tr bgcolor=\"#DFE5FA\"> \n");
				sb.append(" 	<td> Author Identifier: </td> \n");
				sb.append(" 	<td> "+authorPlotCode.toString()+" </td> \n");
				sb.append(" </tr> \n");

				//the state
				sb.append(" <tr> \n");
				sb.append(" 	<td> State </td> \n");
				sb.append(" 	<td> "+state+" </td> \n");
				sb.append(" </tr> \n");

				//the locations
				sb.append(" <tr> \n");
				sb.append(" 	<td> Latitude:  </td> \n");
				sb.append(" 	<td> "+latitude+" </td> \n");
				sb.append(" </tr> \n");
				sb.append(" <tr> \n");
				sb.append(" 	<td> Longitude:  </td> \n");
				sb.append(" 	<td> "+longitude+" </td> \n");
				sb.append(" </tr> \n");

				//the community
			//	sb.append(" <tr> \n");
			//	sb.append(" 	<td> Commmunity: </td> \n");
			//	sb.append(" 	<td> "+community+" </td> \n");
			//	sb.append(" </tr> \n");

				// the exceptions
				sb.append(" <tr> \n");
				sb.append("   <td> Errors: </td> \n");
				sb.append("   <td> "+exceptions+" </td> \n");
				sb.append(" </tr> \n");

				// insertions results
				sb.append(" <tr> \n");
				sb.append("   <td> Insert: </td> \n");
				sb.append("   <td> "+insertion+" </td> \n");
				sb.append(" </tr> \n");

				sb.append("</table> \n");
			}
			catch( Exception e )
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return( sb.toString() );
		}


	/**
	 * this method is used to handle the coorrelation of communities
	 *
	 */
	 private StringBuffer handleVegCommunityCorrelation(
	 	HttpServletRequest request,
	 	HttpServletResponse response)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String action = request.getParameter("action");
			if ( action.equals("init") )
			{
				sb.append("<html>");

				System.out.println("DataSubmitServlet > init vegCommunityCorrelation");
				String salutation = request.getParameter("salutation");;
				String firstName =  request.getParameter("firstName");
				String lastName =  request.getParameter("lastName");
				String emailAddress = request.getParameter("emailAddress");
				String orgName =  request.getParameter("orgName");
				String commName = request.getParameter("communityName");
				String correlationTaxon = request.getParameter("correlationTaxon");
				String correlationTaxonLevel = request.getParameter("correlationTaxonLevel");
				String status = "accepted";
				String nameRefAuthor = salutation+" "+firstName+" "+lastName;
				String nameRefTitle = "vegbank";
				sb.append("<b> communityName: " + commName + " </b> <br>" );
				//HERE NEED TO GET THE STATUS ID ASSOCIATED WITH THIS Community

				Query query = new Query();
				int statusId = query.getCommStatusId(commName);


				//get a vector that contains hashtables with all the possible
				//correlation ( name, recognizing party, system, status, level)
				CommunityQueryStore qs = new CommunityQueryStore();
				Vector correlationTargets =
					qs.getCorrelationTargets(
						correlationTaxon,
						"natureserve",
						correlationTaxonLevel);

				for (int i=0; i<correlationTargets.size(); i++)
				{
					//get the hashtables form the vector
					Hashtable hash = (Hashtable)correlationTargets.elementAt(i);
					String correlationName = (String)hash.get("commName");
					String recognizingParty  = (String)hash.get("recognizingParty");
					String level  = (String)hash.get("level");
					String conceptStatus  = (String)hash.get("conceptStatus");
					String commConceptId  = (String)hash.get("commConceptId");
					String usageId  = (String)hash.get("usageId");

					sb.append("<form action=\"/vegbank/servlet/DataSubmitServlet\" method=\"get\" >");
					sb.append(" <input type=hidden name=submitDataType value=vegCommunityCorrelation> ");
					sb.append(" <input type=hidden name=action value=submit> ");
					sb.append(" <input type=hidden name=statusId value="+statusId+"> ");
					sb.append(" <input type=hidden name=conceptId value="+commConceptId+"> ");

					sb.append(" <input type=hidden name=commName value=\""+commName+"\"> ");
					sb.append(" <input type=hidden name=correlationTargetName value="+correlationName+"> ");
					sb.append(" <input type=hidden name=correlationTargetLevel value="+level+"> ");

					sb.append("commName: " +correlationName + "<br> ");
					sb.append("party: " +recognizingParty + "<br> ");
					sb.append("level: " +level + "<br> ");
					sb.append("status: "+conceptStatus + "<br> ");
					sb.append("concept: "+commConceptId + "<br> ");

					sb.append("<select class=item name=correlation multiple size=3>");
					sb.append("<option selected>unknown");
					sb.append("<option>gt");
					sb.append("<option>lt");
					sb.append("<option>overlap");
					sb.append("</select>");
					sb.append("<br> <input type=\"submit\" VALUE=\"submit correlation\">");
					sb.append("<br>");

					sb.append("</form>");
				}
				sb.append("</html>");
			}
			else if ( action.equals("submit") )
			{
				//GET THE ATTRIBUTES TO LOAD TO THE DATABASE
				String conceptId = request.getParameter("conceptId");
				int concept = Integer.parseInt(conceptId);
				String statusId =  request.getParameter("statusId");
				int status = Integer.parseInt(statusId);
				String correlation =  request.getParameter("correlation");

				String communityName = request.getParameter("commName");
				String correlationTargetName = request.getParameter("correlationTargetName");
				String correlationTargetLevel = request.getParameter("correlationTargetLevel");

				String startDate = null;
				String stopDate = null;
				VegCommunityLoader commLoader = new VegCommunityLoader();
				commLoader.insertCommunityCorrelation(
					status,
					concept,
					correlation,
					startDate,
					stopDate);

				//assume that all went ok and return a recipt
				System.out.println("DataSubmitServlet > preparing results page");

				String resultPage = getCorrelationResultsPage(true, communityName,
				correlationTargetName, correlationTargetLevel, correlation);

				sb.append( resultPage );
			}
		}
		catch( Exception e )
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
			sb.append( e.getMessage() );
		}
		return(sb);
	}


	/**
	 * method to prepare the results page
	 */
	 private String getCorrelationResultsPage(boolean result, String communityName,
	 String correlationTargetName, String correlationTargetLevel, String correlation)
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html> \n");
		 sb.append("<b> CORRELATION SUBMITAL <b> \n");
		 sb.append("<br> \n");
		 sb.append("Community Name: " + communityName +" <br> \n");
		 sb.append("Correlation Target Name: " + correlationTargetName +" <br> \n");
		 sb.append("Correlation Target Level: " + correlationTargetLevel +" <br> \n");
		 sb.append("Convergence: " + correlation +" <br> \n");
		 sb.append("<a href=/vegbank/forms/community-submit.html> Add a new community  </a> \n");
		 sb.append("");
		 sb.append("");
		 sb.append("");
		 sb.append("</html> \n");
		 return(sb.toString() );
	 }





}
