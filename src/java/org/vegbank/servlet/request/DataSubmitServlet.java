package org.vegbank.servlet.request;

/*
 *  '$RCSfile: DataSubmitServlet.java,v $'
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-05-07 01:37:27 $'
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

import VegCommunityLoader;

import org.vegbank.plots.datasource.PlotDataSource;
import org.vegbank.plots.rmi.DataSourceClient;
//import org.vegbank.communities.datasink.DBCommunityWriter;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.AbstractList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vegbank.common.Constants;
//import org.vegbank.common.model.Community;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Plant;
import org.vegbank.common.model.PlantUsage;
import org.vegbank.common.model.Reference;
import org.vegbank.plants.datasink.DBPlantWriter;
import org.vegbank.servlet.authentication.UserDatabaseAccess;
import org.vegbank.servlet.datafileexchange.DataFileExchange;
import org.vegbank.servlet.util.ServletUtility;
import org.w3c.dom.Document;

import xmlresource.utils.XMLparse;
import xmlresource.utils.transformXML;
import databaseAccess.CommunityQueryStore;
import databaseAccess.SqlFile;
import databaseAccess.TaxonomyQueryStore;

/**
 * REQUIRED PARAMETERS
 * @param submitDataType -- 
 * 
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-05-07 01:37:27 $'
 *  '$Revision: 1.6 $'
 */


public class DataSubmitServlet extends HttpServlet implements Constants
{

	private String submitDataType = null;
	// FIXME: Should be in properties
	private static String communityValidationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/community-submit_valid.html";
	private static String communityValidationForm = "/usr/local/devtools/jakarta-tomcat/webapps/forms/valid.html";
	private static String commUpdateScript = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/update_community_summary.sql";
	private static String plotsArchiveFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/input.data";
	private static String plotSelectTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/plot-submit-select.html";
	private static String plotSelectForm  = "/usr/local/devtools/jakarta-tomcat/webapps/forms/plot_select.html";
	private static String genericTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/generic_form.html";
	// this is the pre-transformed init template 
	private static String plantNameRectificationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/submit-plantname-rectification.html";
	private static String plantNameReferenceTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/submit-plantname-reference.html";
	private static String plantConceptTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/submit-plantconcept.html";
	private static String plantStatusUsageTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/submit-plantstatususage.html";
	private static String plantSubmittalReceiptTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/submit-plantsubmittal-receipt.html";
	//this is the file that has the updated tokens and should be shown to the client
	private static String plantValidationForm = "/usr/local/devtools/jakarta-tomcat/webapps/forms/plant-valid.html";
	private static String plotSubmittalInitTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/plot-submit.html";
	//this is the file that has the updated tokens and should be shown to the client
	private static String plotSubmittalInitForm = "/usr/local/devtools/jakarta-tomcat/webapps/forms/plot-valid.html";
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
			System.out.println("DataSumbitServlet > init rmiserver: " + rmiServer);
			//construct a new instance of the rmi client
			DataSourceClient rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);
			
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
				// GET THE COOKIE FOR THE CURRENT USER WHICH WILL BE REFERENCED 
				// ELSEWHERE IN THE CLASS AND WILL BE STORED WITH THE DATA THAT 
				// IS LOADED TO VEGBANK AND THEN GET THE REST OF THE USER-RELATED 
				// ATTRIBUTES
				String user = su.getCookieValue(request);
				Hashtable userAtts = this.getUserIdParameters(user);
				String salutation = (String)userAtts.get("salutation");
				String surName = (String)userAtts.get("surName");
				String givenName = (String)userAtts.get("givenName");
				String institution = (String)userAtts.get("institution");
				String permissionType = (String)userAtts.get("permissionType");
				int permissionLevel = 0;
				try
				{
					permissionLevel = Integer.parseInt(permissionType);
				}
				catch ( Exception e1 )
				{
					System.out.println("Exception: could not parse the permission level: " + e1.getMessage() );
				}
				System.out.println("DataSubmitServlet > current user email: " + user);
				System.out.println("DataSubmitServlet > current user salutation: " + salutation);
				System.out.println("DataSubmitServlet > current user surName: " + surName);
				System.out.println("DataSubmitServlet > current user givenName: " + givenName);
				System.out.println("DataSubmitServlet > current user institution: " + institution);
				System.out.println("DataSubmitServlet > current user permission lev: " + permissionLevel);
				if ( permissionLevel <= 1)
				{
					// don't let the user any further into the loading process
					System.out.println("DataSubmitServlet > ## this user does not have the appropriate permissions");
					this.handleInvalidPermissions(response, user);
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
						Enumeration enum = request.getParameterNames();
				
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
							StringBuffer sb = handleVegCommunitySubmittal(request, response);
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
							StringBuffer sb = handleVegPlotSubmittal(enum, request, response, user, userAtts);
							out.println( sb.toString() );
						}
						else if ( submitDataType.trim().toUpperCase().equals("PLANTTAXA")  )
						{
							StringBuffer sb = handlePlantTaxaSubmittal(request, response,userAtts);
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
				message.append("<a href=\"/forms/certification.html\">Certification Form");
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
		 * Handles the submittal of a new plant into the plant taxonomy 
		 * database.  The proccesses here very closely mimic those in the submittal
		 * of a community.  This method represents the wizard for loading new plant taxa
		 * and the steps included in the wizard are:
		 * 1] init -- submit the new plant name(s)
		 *
		 * @param params -- a hashtable with all the parameter name, value pairs
		 * @param response -- the http response object
		 * @return sb -- stringbuffer with any errors or warnings etc.
		 *
		 */
		private StringBuffer handlePlantTaxaSubmittal(
			HttpServletRequest request,
			HttpServletResponse response,
			Hashtable userAtts)
		{
			StringBuffer sb = new StringBuffer();
			PrintWriter out = null;
			//getSession
			HttpSession session = request.getSession();
			Plant plant = (Plant) session.getAttribute("Plant");
			
			if (plant == null)
			{
				// CONSTRUCT A NEW INSTANCE OF A PLANT
				plant = new Plant();
				session.setAttribute("Plant", plant);			
			}
			
			try
			{
				out = response.getWriter();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
					
			TaxonomyQueryStore tqs = new TaxonomyQueryStore();
			try
			{
				String taxonDescription= "";
				String salutation= "";
				String firstName = "";
				String lastName = "";
				String emailAddress = "";
				String orgName = "";
				
				String action = request.getParameter("action");
				// if the wizard is initiated the plant names will be checked against the 
				// vegbank database for near matches and a form with these matches will be 
				// sent to the client
				if ( action.equals("init") )
				{
					
					System.out.println("DataSubmitServlet > init plantTaxa for " );
	
					Party party = new Party();
					
					party.setGivenName( (String) userAtts.get("givenName") );
					party.setSurname( (String) userAtts.get("surName") );
					party.setOrganizationName( (String) userAtts.get("institution") );
					
					plant.setParty(party);
					
					
					
					// the next 3 attributes refer to the plant name that the 
					// user is trying to insert into the database
					String longName = request.getParameter("longName");
					String shortName = request.getParameter("shortName");
					String code = request.getParameter("code");
			
					System.out.println("DataSubmitServlet > longName: " + longName);
					System.out.println("DataSubmitServlet > shortName: " + shortName);
					System.out.println("DataSubmitServlet > code: " + code);
					
					// get the data already stored in the database with corresponding to the names
					String longNameMessage = "";
					String shortNameMessage = "";
					String codeMessage = "";

					Vector lv = tqs.getPlantTaxonSummary(longName, "%" );
					Vector sv = tqs.getPlantTaxonSummary(shortName, "%" );
					Vector cv = tqs.getPlantTaxonSummary(code, "%" );
					if ( lv.size() > 0)
						{	longNameMessage = "Currently Exists in VegBank"; }
					else 
						{ longNameMessage = "Not Currently in VegBank"; }
					if ( sv.size() > 0)
						{	shortNameMessage = "Currently Exists in VegBank"; }
					else 
						{ shortNameMessage = "Not Currently in VegBank"; }
					if ( cv.size() > 0)
						{ codeMessage = "Currently Exists in VegBank"; }
					else 
						{ codeMessage = "Not Currently in VegBank"; }
					
					Vector longNameNearMatches = tqs.getNameNearMatches(longName);
					Vector shortNameNearMatches = tqs.getNameNearMatches(shortName);
					Vector codeNearMatches = tqs.getNameNearMatches(code);
					//System.out.println("DataSubmitServlet > longName match: " + lv.toString() );
					
					//update the validation page that is returned to the user
					updatePlantRectificationPage(
						emailAddress,
						longName,
						shortName,
						code,
						longNameMessage,
						shortNameMessage,
						codeMessage,
						longNameNearMatches,
						shortNameNearMatches,
						codeNearMatches,
						out);			
				}
				// THIS WHERE THE RECTIFIED NAMES ARE SUBMITTED TO THE SERVLET
				// AND WHERE THOSE ATTRIBUTES SHOULD BE IDENTIFIED AND STORED IN THE INSTANCE
				else if ( action.equals("namerectification") )
				{
					System.out.println("DataSubmitServlet > performing the name rectification ");
					// GET THE NAME REFERENCES FOR THE INPUT NAMES OR GIVE THE 
					// USER THE FORM TO FILL OUT
					String longNameMatch = request.getParameter("longNameMatches");
					String shortNameMatch = request.getParameter("shortNameMatches");
					String codeMatch = request.getParameter("codeMatches");
					
					
					// UPDATE THE DATA CLASS
					plant.setScientificNameNoAuthors(longNameMatch);
					plant.setCommonName(shortNameMatch);
					plant.setCode(codeMatch);
					
					System.out.println("longName: " +  " match: " +  longNameMatch );
					Hashtable longNameRef = tqs.getPlantNameReference(longNameMatch);
					System.out.println("longNameRef: " + longNameRef.toString() );
					System.out.println("shortName: " + " match: " +  shortNameMatch);
					Hashtable shortNameRef = tqs.getPlantNameReference(shortNameMatch);
					System.out.println("code: " + " match: " + codeMatch );
					Hashtable codeNameRef = tqs.getPlantNameReference(codeMatch);
					
					updatePlantNameReferencePage(
						emailAddress, 
						longNameRef, 
						shortNameRef,  
						codeNameRef,
						out
					);
	
				}
				// THIS IS WHERE THE NAME REFERENCES INFORMATION IS SUBMITTED TO THE SERVLET
				// AND WHERE THOSE ATTRIBUTES SHOULD BE STORED
				else if ( action.equals("namereference") )
				{
					System.out.println("DataSubmitServlet > getting the name reference ");
					
					// LOAD THE NAME REFERENCE ATTRIBUTES 
					String longNameRefAuthors = request.getParameter("longNameRefAuthors");
					String longNameRefTitle = request.getParameter("longNameRefTitle");
					String longNameRefDate = request.getParameter("longNameRefDate");
					String longNameRefEdition = request.getParameter("longNameRefEdition");
					String longNameRefSeriesName = request.getParameter("longNameRefSeriesName");
					String longNameRefVolume = request.getParameter("longNameRefVolume");
					String longNameRefPage = request.getParameter("longNameRefPage");
					String longNameRefISSN = request.getParameter("longNameRefISSN");
					String longNameRefISBN = request.getParameter("longNameRefISBN");
					String longNameRefOtherCitDetails = request.getParameter("longNameRefOtherCitDetails");
					
					String shortNameRefAuthors = request.getParameter("shortNameRefAuthors");
					String shortNameRefTitle = request.getParameter("shortNameRefTitle");
					String shortNameRefDate = request.getParameter("shortNameRefDate");
					String shortNameRefEdition = request.getParameter("shortNameRefEdition");
					String shortNameRefSeriesName = request.getParameter("shortNameRefSeriesName");
					String shortNameRefVolume = request.getParameter("shortNameRefVolume");
					String shortNameRefPage = request.getParameter("shortNameRefPage");
					String shortNameRefISSN = request.getParameter("shortNameRefISSN");
					String shortNameRefISBN = request.getParameter("shortNameRefISBN");
					String shortNameRefOtherCitDetails = request.getParameter("shortNameRefOtherCitDetails");
					
					String codeRefAuthors = request.getParameter("codeRefAuthors");
					String codeRefTitle = request.getParameter("codeRefTitle");
					String codeRefDate = request.getParameter("codeRefDate");
					String codeRefEdition = request.getParameter("codeRefEdition");
					String codeRefSeriesName = request.getParameter("codeRefSeriesName");
					String codeRefVolume = request.getParameter("codeRefVolume");
					String codeRefPage = request.getParameter("codeRefPage");
					String codeRefISSN = request.getParameter("codeRefISSN");
					String codeRefISBN = request.getParameter("codeRefISBN");
					String codeRefOtherCitDetails = request.getParameter("codeRefOtherCitDetails");
					
					Reference scientificNameNoAuthorsRef = new Reference();
					Reference codeNameRef = new Reference(); 
					Reference commonNameRef = new Reference(); 
					 // assign these references to plant
					plant.setScientificNameNoAuthorsReference(scientificNameNoAuthorsRef);
					plant.setCommonNameReference(commonNameRef);
					plant.setCodeNameReference(codeNameRef);
					
	//				scientificNameNoAuthorsRef.setAuthors(longNameRefAuthors);
					scientificNameNoAuthorsRef.setTitle(longNameRefTitle);
					scientificNameNoAuthorsRef.setPubDate(longNameRefDate);
					scientificNameNoAuthorsRef.setEdition(longNameRefEdition);
	//				scientificNameNoAuthorsRef.setSeriesName(longNameRefSeriesName);
					scientificNameNoAuthorsRef.setVolume(longNameRefVolume);
					scientificNameNoAuthorsRef.setPageRange(longNameRefPage);
					scientificNameNoAuthorsRef.setIssn(longNameRefISBN);
					scientificNameNoAuthorsRef.setIsbn(longNameRefISSN);
	//				scientificNameNoAuthorsRef.setOtherCitationDetails(longNameRefOtherCitDetails);
					
	//				commonNameRef.setAuthors(shortNameRefAuthors);
					commonNameRef.setTitle(shortNameRefTitle);
					commonNameRef.setPubDate(shortNameRefDate);
					commonNameRef.setEdition(shortNameRefEdition);
	//				commonNameRef.setSeriesName(shortNameRefSeriesName);
					commonNameRef.setVolume(shortNameRefVolume);
					commonNameRef.setPageRange(shortNameRefPage);
					commonNameRef.setIssn(shortNameRefISBN);
					commonNameRef.setIsbn(shortNameRefISSN);
	//				commonNameRef.setOtherCitationDetails(shortNameRefOtherCitDetails);
					
	//				codeNameRef.setAuthors(codeRefAuthors);
					codeNameRef.setTitle(codeRefTitle);
					codeNameRef.setPubDate(codeRefDate);
					codeNameRef.setEdition(codeRefEdition);
	//				codeNameRef.setSeriesName(codeRefSeriesName);
					codeNameRef.setVolume(codeRefVolume);
					codeNameRef.setPageRange(codeRefPage);
					codeNameRef.setIssn(codeRefISBN);
					codeNameRef.setIsbn(codeRefISSN);
	//				codeNameRef.setOtherCitationDetails(codeRefOtherCitDetails);
					
					updatePlantConceptPage(
						emailAddress,
						plant.getScientificNameNoAuthors(),
						plant.getCommonName(),
						plant.getCode(),
						out);
				}
				else if ( action.equals("plantconcept") )
				{
					System.out.println("DataSubmitServlet > getting the name reference ");
					String conceptDescription = ""+request.getParameter("conceptDescription");
					String conceptRefAuthors = ""+request.getParameter("conceptRefAuthors");
					String conceptRefTitle  = ""+request.getParameter("conceptRefTitle");
					String conceptRefDate  = ""+request.getParameter("conceptRefDate");
					String conceptRefEdition  = ""+request.getParameter("conceptRefEdition");
					String conceptRefSeriesName  = ""+request.getParameter("conceptRefSeriesName");
					String conceptRefVolume  = ""+request.getParameter("conceptRefVolume");
					String conceptRefPage  = ""+request.getParameter("conceptRefPage");
					String conceptRefISSN  = ""+request.getParameter("conceptRefISSN");
					String conceptRefISBN  = ""+request.getParameter("conceptRefISBN");
					String conceptRefOtherCitDetails = ""+request.getParameter("conceptRefOtherCitDetails");
					
	//				plantParentName = request.getParameter("plantParentName");
	//				plantParentRefTitle =request.getParameter("plantParentRefTitle");
	//				plantParentRefAuthors = request.getParameter("plantParentRefAuthors");
					
	
					Reference conceptReference = new Reference();
					plant.setConceptReference(conceptReference);
					
					plant.setDescription(conceptDescription);
	//				conceptReference.setAuthors(conceptRefAuthors);
					conceptReference.setTitle(conceptRefTitle);
					conceptReference.setPubDate(conceptRefDate);
					conceptReference.setEdition(conceptRefEdition);
	//				conceptReference.setSeriesName(conceptRefSeriesName);
					conceptReference.setVolume(conceptRefVolume);
					conceptReference.setPageRange(conceptRefPage);
					conceptReference.setIssn(conceptRefISSN);
					conceptReference.setIsbn(conceptRefISBN);
	//				conceptReference.setOtherCitationDetails(conceptRefOtherCitDetails);
					
					// send the user the attributes related to the plant status/usage
					// this is where the instance variables (surName,givenName etc..) are updated
					updatePlantStatusUsagePage(  
						emailAddress,
						plant.getScientificNameNoAuthors(),
						plant.getCommonName(),
						plant.getCode(),
						out,
						userAtts);
				}
				// STEP WHEREBY THE USER SUBMITS THE STATUS USAGE DATA AND 
				// THE RECIPT IS RETURNED
				else if ( action.equals("plantstatususage") )
				{
					System.out.println("DataSubmitServlet > getting the status-usage data, returnig recipt");
					String conceptStatus = request.getParameter("conceptStatus");
					String  statusStartDate = request.getParameter("statusStartDate");
					String  statusStopDate = request.getParameter("statusStopDate");
					// ASSUME THAT THE ABOVE DATES ARE THE SAME FOR THE USAGE
					String  usageStartDate = request.getParameter("statusStartDate");
					String  usageStopDate = request.getParameter("statusStopDate");
					String statusDescription = request.getParameter("statusDescription");
					String taxonLevel = request.getParameter("taxonLevel");
					String plantParentName = request.getParameter("plantParentName");
					String plantParentRefAuthors = request.getParameter("plantParentRefAuthors");
					String plantParentRefTitle = request.getParameter("plantParentRefTitle");
					
					// TODO: Need to handle setting a reference for the parent
					// Need to set a reference for the parent ????
					//plantTaxon.setPlantParentName(plantParentName);
					//plantTaxon.setPlantParentRefTitle(plantParentRefTitle);
					//plantTaxon.setPlantParentRefAuthors(plantParentRefAuthors);
					
					plant.setStatus(conceptStatus);
					plant.setStatusStartDate(statusStartDate);
					plant.setStatusStopDate(statusStopDate);
					plant.setDescription(statusDescription);    // FIXME: This may be the party comments field ???
					plant.setClassLevel(taxonLevel);
					
					// Create New Usage Objects
					PlantUsage commonUsage = new PlantUsage();
					PlantUsage scientificNameNoAuthorsUsage = new PlantUsage();
					PlantUsage codeNameUsage = new PlantUsage();
					
					// Set up Common Usage
					commonUsage.setClassSystem(USAGE_NAME_COMMON);
					commonUsage.setPlantName( plant.getCommonName() );
					commonUsage.setStartDate(statusStartDate);
					commonUsage.setStopDate(statusStopDate);				
					// Set up scientificNameNoAuthorsUsage Usage
					commonUsage.setClassSystem(USAGE_NAME_SCIENTIFIC_NOAUTHORS);
					commonUsage.setPlantName( plant.getScientificNameNoAuthors() );
					commonUsage.setStartDate(statusStartDate);
					commonUsage.setStopDate(statusStopDate);						
					// Set up codeName Usage
					commonUsage.setClassSystem(USAGE_NAME_CODE);
					commonUsage.setPlantName( plant.getCode() );
					commonUsage.setStartDate(statusStartDate);
					commonUsage.setStopDate(statusStopDate);		
					
					updatePlantSubmittalRecipt(emailAddress, plant, out, userAtts);
				}
				// STEP WHERE THE PLANT ACTUALLY GETS LOADED TO THE DATABASE
				else if ( action.equals("plantsubmittalreceipt") )
				{
					System.out.println("submittal to the database taking place ");
	
					DBPlantWriter dbw = new DBPlantWriter( plant, null );		
					boolean results = dbw.isWriteSuccess();
	
					// WRITE THE RESULTS BACK TO THE BROWSER
					String receipt = this.getPlantInsertionReceipt(results, plant);
					out.println(receipt);
					
				}
				else
				{
					System.out.println("DataSubmitServlet > unknown step in the plant taxa process " + action);
				}
			}
			catch( Exception e ) 
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return(sb);
		}
	
	/**
	 * method that composes the plant-insert receipt from the hashtable 
	 * which is passed to the plant taxonomy loader
	 * 
	 * @param plantAtts -- the hashtable that contains the plant attributes and that
	 * conforms to the hashtable to be loaded via the 'PlantTaxaLoader'
	 * @see PlantTaxaLoade
	 * @param results -- true if the plant was successfully loaded, otherwise false
	 * @return s -- an html-encoded string containing the receipt 
	 *
	 */
	
	 private String getPlantInsertionReceipt( boolean results, Plant plant)
	 {
		 StringWriter output = new StringWriter();
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 
			 //get the required elements from the planttaxonobject
			 String email = "";
			 String name = "";
			 String institution = "";
			 String sciName  = plant.getScientificName();
			 String commonName = plant.getCommonName();
			 String code = plant.getCode();
			 String conceptDescription = plant.getDescription();
			 
			 AbstractList plantUsageList = plant.getPlantUsages();
				// Just use the first element -- they all have same dates
			 PlantUsage pu = (PlantUsage) plantUsageList.get(0);
			 String usageStartDate = pu.getStartDate();
			 String usageStopDate = pu.getStopDate();
			 
			 String status = plant.getStatus();
			 String level = plant.getClassLevel();

			 
			 // create a table to be put into the generic form 
			 sb.append("<span class=\"itemsmall\">");
			 sb.append("Insertion Results: " + results + "<br> \n");
			 sb.append("Email: " + email+ "<br> \n");
			 sb.append("Name: " + name + "<br> \n");
			 sb.append("Institution: " + institution + "<br> \n");
			 sb.append("Scientific Name: " + sciName + "<br> \n");
			 sb.append("Common Name: " + commonName + "<br> \n");
			 sb.append("Code: " + code + "<br> \n");
			 sb.append("Concept Description: " +  conceptDescription + "<br> \n");
			 sb.append("Usage Start: " +  usageStartDate + "<br> \n");
			 sb.append("Usage Stop: " + usageStopDate  + "<br> \n");
			 sb.append("Status: " + status  + "<br> \n");
			 sb.append("Taxonomic Level: " + level  + "<br> \n");
			 
			 sb.append("</span>");
			 // set up the filter tokens
			 Hashtable replaceHash = new Hashtable();
			 replaceHash.put("messages", sb.toString() );
			 su.filterTokenFile(genericTemplate, output, replaceHash);

			
		 }
		 catch (Exception e )
		 {
			 System.out.println("Exception > " + e.getMessage() );
		 }
		 return( output.toString() );
	 }

	
	/**
	 * method that updates the plant submittal receipt page --
	 * this is the last sub-step in the plant submittal process
	 */
	private void updatePlantSubmittalRecipt(
		String emailAddress,
		Plant plant,
		Writer out,
		Hashtable userAtts)
	 {
	 	Reference snRef = plant.getScientificNameNoAuthorsReference();
		Reference codeRef = plant.getCodeNameReference();
		Reference cnRef = plant.getCommonNameReference();
		Reference conceptRef = plant.getConceptReference();
	 	
	 	Reference ref = new Reference();
		 try
		 {
			 Hashtable replaceHash = new Hashtable();
			 
			 // THE INFORMATION ABOUT THE SUBMITTER
			 replaceHash.put("emailAddress", ""+emailAddress);
			 replaceHash.put("givenName", ""+ userAtts.get("givenName") );
			 replaceHash.put("surName", ""+ userAtts.get("surName"));
			 
			 // THE NAMES OF THE PLANT
			  replaceHash.put("longName", ""+ plant.getScientificNameNoAuthors());
				replaceHash.put("shortName", ""+plant.getCommonName());
				replaceHash.put("code", ""+ plant.getCode());
				
			 
			 // THE NAME REFERENCES
			 //replaceHash.put("longNameRefAuthors", ""+snRef.getAuthors() );
			 replaceHash.put("longNameRefTitle", ""+ snRef.getTitle() );
			 replaceHash.put("longNameRefDate", ""+ snRef.getPubDate() );
			 replaceHash.put("longNameRefEdition", ""+ snRef.getEdition() );
			 //replaceHash.put("longNameRefSeriesName", ""+ snRef.getSeriesName() );
			 replaceHash.put("longNameRefVolume", ""+ snRef.getVolume() );
			 replaceHash.put("longNameRefPage", ""+ snRef.getPageRange() );
			 replaceHash.put("longNameRefISSN", ""+ snRef.getIssn() );
			 replaceHash.put("longNameRefISBN", ""+ snRef.getIsbn());
			 //replaceHash.put("longNameRefOtherCitDetails", ""+ snRef.getOtherCitationDetails() );
			 
			 //replaceHash.put("shortNameRefAuthors", ""+cnRef.getAuthors() );
			 replaceHash.put("shortNameRefTitle", ""+cnRef.getTitle()  );
			 replaceHash.put("shortNameRefDate", ""+ cnRef.getPubDate() );
			 replaceHash.put("shortNameRefEdition", ""+ cnRef.getEdition() );
			 //replaceHash.put("shortNameRefSeriesName", ""+ cnRef.getSeriesName() );
			 replaceHash.put("shortNameRefVolume", ""+ cnRef.getVolume() );
			 replaceHash.put("shortNameRefPage", ""+cnRef.getPageRange() );
			 replaceHash.put("shortNameRefISSN", ""+cnRef.getIssn() );
			 replaceHash.put("shortNameRefISBN", ""+ cnRef.getIsbn() );
			 //replaceHash.put("shortNameRefOtherCitDetails", ""+cnRef.getOtherCitationDetails() );
			 
			 //replaceHash.put("codeRefAuthors", ""+codeRef.getAuthors() );
			 replaceHash.put("codeRefTitle", ""+ codeRef.getTitle() );
			 replaceHash.put("codeRefDate", ""+ codeRef.getPubDate() );
			 replaceHash.put("codeRefEdition", ""+ codeRef.getEdition() );
			 //replaceHash.put("codeRefSeriesName", ""+ codeRef.getSeriesName() );
			 replaceHash.put("codeRefVolume", ""+codeRef.getVolume() );
			 replaceHash.put("codeRefPage", ""+codeRef.getPageRange() );
			 replaceHash.put("codeRefISSN", ""+ codeRef.getIssn());
			 replaceHash.put("codeRefISBN", ""+codeRef.getIsbn() );
			 //replaceHash.put("codeRefOtherCitDetails", ""+codeRef.getOtherCitationDetails() );
			 
			 // CONCEPT REFERENCE
			 replaceHash.put("conceptDescription", ""+plant.getDescription() );
			 //replaceHash.put("conceptRefAuthors", ""+conceptRef.getAuthors()  );
			 replaceHash.put("conceptRefTitle", ""+ conceptRef.getTitle()  );
			 replaceHash.put("conceptRefDate", ""+conceptRef.getPubDate() );
			 replaceHash.put("conceptRefEdition", ""+conceptRef.getEdition() );
			 //replaceHash.put("conceptRefSeriesName", ""+ conceptRef.getSeriesName() );
			 replaceHash.put("conceptRefVolume", ""+conceptRef.getVolume() );
			 replaceHash.put("conceptRefPage", ""+conceptRef.getPageRange() );
			 replaceHash.put("conceptRefISSN", ""+conceptRef.getIssn() );
			 replaceHash.put("conceptRefISBN", ""+conceptRef.getIsbn() );
			 //replaceHash.put("conceptRefOtherCitDetails", ""+conceptRef.getOtherCitationDetails() );
			 
			 // STATUS AND USAGE
			 replaceHash.put("conceptStatus", ""+plant.getStatus() );
			 replaceHash.put("statusStartDate", ""+plant.getStatusStartDate());
			 replaceHash.put("statusStopDate", ""+plant.getStatusStopDate());
			 replaceHash.put("statusDescription", ""+plant.getDescription()); // FIXME: I think this is wrong
			 replaceHash.put("taxonLevel", ""+plant.getClassLevel());
			
				//	FIXME: Ignoring plantparent  for now
//			 replaceHash.put("plantParentName", ""+plantParentName);
//			 replaceHash.put("plantParentRefTitle", ""+plantParentRefTitle);
//			 replaceHash.put("plantParentRefAuthors", ""+plantParentRefAuthors);
			 
			 
			 su.filterTokenFile(plantSubmittalReceiptTemplate, out, replaceHash);
		 }
		 catch( Exception e ) 
		 {
			 System.out.println("Exception:  " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }

	 
	 /**
	  * Method that uses the user database class to access the parameters 
		* for a given user so that those attributes can be used in the 
		* forms.
		* 
		* @param emailAddress -- the email address of the user
		* @return userInfo -- a hashtable with the following attributes <br> <br>
		* surName
		*	givenName
		*	password 
		*	address
		*	city
		*	state
		*	country
		*	zipCode
		*	dayPhone
		*	ticketCount
		*	permissionType
		*	institution
		*/
		private Hashtable getUserIdParameters(String emailAddress)
		{
			Hashtable h = new Hashtable();
			try
			{
				UserDatabaseAccess userdb = new UserDatabaseAccess();
				h = userdb.getUserInfo(emailAddress);
			}
			catch( Exception e ) 
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return(h);
		}
	
	
	
	
	/**
	 * method that updates the plant usage / status page 
	 * @param emailAddress -- the email of the vegbank user
	 * @param longName -- the log, or scientific name of the plant
	 * @param shortName -- the short or common name of the plant
	 * @param code -- the code of the plant
	 */
	private void updatePlantStatusUsagePage(
		String emailAddress,
		String longName,
		String shortName,
		String code,
		Writer out,
		Hashtable userAtts)
	 {
		try
		{		
			// set up the filter tokens
			Hashtable replaceHash = new Hashtable();
			// to this form add the names and the concept description and the 
			// party names for perspectives
			
			replaceHash.put("longName", ""+longName);
			replaceHash.put("shortName", ""+shortName);
			replaceHash.put("code", ""+code);
			
			replaceHash.put("emailAddress", ""+emailAddress);
			replaceHash.put("plantPartyGivenName", ""+userAtts.get("givenName") );
			replaceHash.put("plantPartySurName", ""+userAtts.get("surName"));
			replaceHash.put("plantPartyInstitution", ""+userAtts.get("institution") );
			replaceHash.put("plantPartyEmailAddress", ""+emailAddress );

			// UPDATE THE DATES B/C THEY ARE REQUIRED BY THE LOADER
			String curDate = this.getCurrentDate();
			replaceHash.put("statusStartDate", curDate);
			replaceHash.put("statusStopDate", curDate);
			
			
			su.filterTokenFile(plantStatusUsageTemplate, out, replaceHash);
			// the calling method will redirect the browser
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
	 }

	/*
	 * method that retuns the date in a format that can be accepted 
	 * by the RDBMS and that is like: Aug 9, 2002
	 */
	 private String getCurrentDate()
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
	 
	
	/**
	 * method that handles the updating of the plant concept page
	 */
	private void updatePlantConceptPage(
		String emailAddress,
		String longName,
		String shortName,
		String code,
		Writer out)
	{
		try
		{		
			// set up the filter tokens
			Hashtable replaceHash = new Hashtable();
			replaceHash.put("longName", ""+longName);
			replaceHash.put("shortName", ""+shortName);
			replaceHash.put("code", ""+code);
			
			replaceHash.put("emailAddress", ""+emailAddress);
			su.filterTokenFile(plantConceptTemplate, out, replaceHash);
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	/**
	 * method that handles the plant name reference page
	 */
	private void updatePlantNameReferencePage(
		String emailAddress,
		Hashtable longNameRef,
		Hashtable shortNameRef,
		Hashtable codeNameRef,
		Writer out)
	{
		try
		{
			// get the paramters from the hashtables
			String longName = (String)longNameRef.get("plantName");
			String shortName = (String)shortNameRef.get("plantName");
			String code = (String)codeNameRef.get("plantName");
			
			String longNameReferenceId = (String)longNameRef.get("plantReferenceId");
			String shortNameReferenceId = (String)shortNameRef.get("plantReferenceId");
			String codeNameReferenceId = (String)codeNameRef.get("plantReferenceId");
			
			System.out.println("ref id: " + longNameReferenceId);
			
			// set up the filter tokens
			Hashtable replaceHash = new Hashtable();
			replaceHash.put("emailAddress", ""+emailAddress);
			replaceHash.put("longName", ""+longName);
			replaceHash.put("shortName", ""+shortName);
			replaceHash.put("code", ""+code);
			
			// long name
			if ( longNameReferenceId != null && ! longNameReferenceId.trim().equals("null") )
			{
				replaceHash.put("longNameReference", ""+longNameRef.toString() );
			}
			else
			{
				replaceHash.put("longNameReference", ""+ this.getReferenceForm("longName") );
			}
			// short name 
			if ( shortNameReferenceId != null && ! shortNameReferenceId.trim().equals("null") )
			{
				replaceHash.put("shortNameReference", ""+shortNameRef.toString() );
			}
			else
			{
				replaceHash.put("shortNameReference", ""+ this.getReferenceForm("shortName") );
			}
			// code
			if ( codeNameReferenceId != null && ! codeNameReferenceId.trim().equals("null") )
			{
				replaceHash.put("codeNameReference", ""+codeNameRef.toString() );
			}
			else
			{
				replaceHash.put("codeNameReference", ""+ this.getReferenceForm("code") );
			}
			
			su.filterTokenFile(plantNameReferenceTemplate, out, replaceHash);

		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	/** 
	 * this method stores the html code for collecting reference data 
	 * and is intended to be used to add to a template to create the 
	 * from that is sent to the browser
	 *
	 * @param plantNameType -- the type of plantName and may contain: <br>
	 * longName <br>
	 * shortName <br>
	 * codeName <br>
	 * @return s - the string with the fields top fill out for submitting 
	 * 	an entry in the plantReference table that looks like:
	 * Title: <input type=text size=25 name=shortNameRefTitle> <br> 
	 * Date: <input type=text size=25 name=shortNameRefDate> <br> 
	 * Edition: <input type=text size=25 name=shortNameRefEdition> <br> 
	 * Series name: <input type=text size=25 name=shortNameRefSeriesName> <br> 
	 * Volume: <input type=text size=25 name=shortNameRefVolume> <br> 
	 * Page: <input type=text size=25 name=shortNameRefPage> <br> 
	 * ISSN: <input type=text size=25 name=shortNameRefISSN> <br> 
	 * ISBN: <input type=text size=25 name=shortNameRefISBN> <br> 
	 * Other details: <input type=text size=25 name=shortNameRefOtherCitationDetails> <br> 
	 * 
	 */
	private String getReferenceForm(String plantNameType)
	{
		StringBuffer sb = new StringBuffer();
		
		//TODO: Expose the new Reference Contributor fields
		sb.append("Not Connected - Authors: <input type=text size=25 name="+plantNameType+"RefAuthors> <br> \n");
		sb.append("Title: <input type=text size=25 name="+plantNameType+"RefTitle> <br> \n");
		sb.append("Date: <input type=text size=25 name="+plantNameType+"RefDate> <br> \n");
		sb.append("Edition: <input type=text size=25 name="+plantNameType+"RefEdition> <br> \n");
		sb.append("Not Connected -  Series name: <input type=text size=25 name="+plantNameType+"RefSeriesName> <br> \n");
		sb.append("Volume: <input type=text size=25 name="+plantNameType+"RefVolume> <br> \n");
		sb.append("Page Range: <input type=text size=25 name="+plantNameType+"RefPage> <br> \n");
		sb.append("ISSN: <input type=text size=25 name="+plantNameType+"RefISSN> <br> \n");
		sb.append("ISBN: <input type=text size=25 name="+plantNameType+"RefISBN> <br> \n");
		sb.append("Not Connected -  Other details: <input type=text size=25 name="+plantNameType+"RefOtherCitDetails> <br> \n");
		
		return( sb.toString() );
	}

	/**
	 * method that handles the updating of the plant submittal form with 
	 * a combination of the input attributes and the input.
	 *
	 * @return -- retuns false if there are any exceptions thrown while
	 * 	genetaing the html form
	 */
	private boolean updatePlantRectificationPage(
		String emailAddress,
		String longName,
		String shortName,
		String code,
		String longNameMessage,
		String shortNameMessage,
		String codeMessage,
		Vector longNameMatches,
		Vector shortNameMatches,
		Vector codeMatches,
		Writer out)
	 {
		 try
		 {
			 Hashtable replaceHash = new Hashtable();
			 // party-related attributes
			 replaceHash.put("emailAddress", ""+emailAddress);
			 
			 //plant name-related attributes
			 replaceHash.put("longName", longName );
			 replaceHash.put("shortName", shortName );
			 replaceHash.put("code", code);
			 replaceHash.put("longNameMessage", longNameMessage );
			 replaceHash.put("shortNameMessage", shortNameMessage );
			 replaceHash.put("codeMessage", codeMessage);
			 
			 // drop down with near matches
			 StringBuffer sb = new StringBuffer();
			 sb.append("<select name=\"longNameMatches\">");
			 sb.append("<option selected>  "+ longName +" </option>");
			 for (int i=0; i < longNameMatches.size(); i++) 
			 {
				 sb.append("<option> "+(String)longNameMatches.elementAt(i)+" </option>");
			 }
			 sb.append("</select>");
			 replaceHash.put("longNameNearMatches", sb.toString() );
			 
			 sb = new StringBuffer();
			 sb.append("<select name=\"shortNameMatches\">");
			 sb.append("<option selected>  "+ shortName +" </option>");
			 for (int i=0; i < shortNameMatches.size(); i++) 
			 {
				 sb.append("<option> "+(String)shortNameMatches.elementAt(i)+" </option>");
			 }
			 sb.append("</select>");
			 replaceHash.put("shortNameNearMatches", sb.toString() );
			 
			 sb = new StringBuffer();
			 sb.append("<select name=\"codeMatches\">");
			 sb.append("<option selected>  "+ code +" </option>");
			 for (int i=0; i < codeMatches.size(); i++) 
			 {
				 sb.append("<option> "+(String)codeMatches.elementAt(i)+" </option>");
			 }
			 sb.append("</select>");
			 replaceHash.put("codeNearMatches", sb.toString() );
			 
			 //the message telling the client that there are near matches
			 replaceHash.put("longNameNearMatchMessages", "VegBank has " +longNameMatches.size()+ " near matches" );
			 replaceHash.put("shortNameNearMatchMessages", "VegBank has " +shortNameMatches.size()+ " near matches" );
			 replaceHash.put("codeNearMatchMessages", "VegBank has " +codeMatches.size()+ " near matches" );
				
			 su.filterTokenFile(plantNameRectificationTemplate, out, replaceHash);
		 }
		 catch( Exception e ) 
		 {
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
			return(false);
		 }
		 return(true);
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
			String institution)
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
				replaceHash.put("institution", ""+institution);
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
	 * @param enum -- the input parameters to the parent servlet
	 * @param params -- a hashtable with the unique parameters sent to the servlet
	 * @param request -- http request sent to the servlet
	 * @param response -- http reponse sent to the servlet
	 * 
	 */
	 private StringBuffer handleVegPlotSubmittal(
	 	Enumeration enum,
	 	HttpServletRequest request, 
	 	HttpServletResponse response, 
	 	String user,
	 	Hashtable userAtts)
	{
		StringBuffer sb = new StringBuffer();
		
		String salutation = (String)userAtts.get("salutation");
		String surName = (String)userAtts.get("surName");
		String givenName = (String)userAtts.get("givenName");
		String institution = (String)userAtts.get("institution");
		
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
				htmlContents = updatePlotInitPage(user, salutation, givenName, surName, institution);
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
						response.sendRedirect("/forms/plot-upload.html"); 
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
				
				while (enum.hasMoreElements()) 
				{
					String name = (String) enum.nextElement();
					if ( name.toUpperCase().equals("PLOTID") )
					{
						String values[] = request.getParameterValues(name);
						if (values != null) 
						{
							for (int i=0; i < values.length; i++) 
							{
								String thisPlot = values[i];
								System.out.println("DataSubmitServlet > requesting an rmi plot insert: '"+values[i]+"'" );
								//insert the plot over the rmi system -- first test that the plot is valid
								System.out.println("DataSubmitServlet > plot being loaded by: " + user );
								
								//JHH 20030108
								boolean valid = rmiClient.isPlotValid(thisPlot);
								System.out.println("DataSubmitServlet > plot is valid: " + valid);
								if ( valid == false )
								{
									// get the validation report and proccess the xml that looks like
									String report = rmiClient.getValidationReport();
									// create a document 
									System.out.println("DataSubmitServlet > validation report length: " + report.length() );
									String htmlReceipt = this.getPlotValidationReceipt(report) ;
									sb.append(htmlReceipt);
								}
								// if the plot is valid then load it
								else
								{
                  // get the plotsArchiveType from the session
                  String plotsArchiveType =
                    (String) request.getSession().getAttribute("plotsArchiveType");
									System.out.println("DataSubmitServlet > About to insert Plot");
									String result = rmiClient.insertPlot(thisPlot, plotsArchiveType, user);
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
									System.out.println("DataSubmitServlet > requesting a receipt: '"+i+"' out of: " + values.length );
								}
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
	
	
  public StringBuffer handleVegPlotUpload(String plotsArchiveType, HttpServletRequest request)
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
	 private void emailPlotSubmitalReceipt(String inputEmail, String receipt)
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
				//HERE NEED TO GET THE STATUS ID ASSOCIATED WITH THIS PLANT
				VegCommunityLoader commLoader = new VegCommunityLoader();
				int statusId = commLoader.getStatusId(nameRefTitle, nameRefAuthor, 
				status, commName, salutation, firstName, lastName, emailAddress,
				orgName );
				
				
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
				commLoader.insertCommunityCorrelation(status, concept, correlation
				, startDate, stopDate);
				//UPDATE THE DATABASE SUMMARY TABLE
				new SqlFile().issueSqlFile(commUpdateScript);
				
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
		 sb.append("<a href=/forms/community-submit.html> Add a new community  </a> \n");
		 sb.append("");
		 sb.append("");
		 sb.append("");
		 sb.append("</html> \n");
		 return(sb.toString() );
	 }
	 
	 
	/**
	 * this method handles the submital of community data to the 
	 * vegbank database.  The return from this method is a StringBuffer 
	 * containing the response from the application and / or database in
	 * relation to the submittal proccess of a new community.  Here the 
	 * action may be either init ( which is the first stage in a two step 
	 * proccess), or submit ( which is the second stage ) where the data 
	 * are submitted to the database
	 * 
	 * @param params -- a hashtable storing the input parameters
	 * @param resposnse -- the http response object
	 * @return sb -- A string buffer with the response by the database
	 * and application about the success of the submittal to the database
	 */
	private StringBuffer handleVegCommunitySubmittal(
		HttpServletRequest request, 
		HttpServletResponse response)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String action = request.getParameter("action");
			if ( action.equals("init") )
			{
				
				System.out.println("DataSubmitServlet > init vegCommunity");
				String salutation = request.getParameter("salutation");;
				String firstName =  request.getParameter("firstName");
				String lastName =  request.getParameter("lastName");
				String emailAddress =  request.getParameter("emailAddress");
				String orgName =  request.getParameter("orgName");
				String commName = request.getParameter("communityName");
				String dateEntered = "11-MAR-2002";
				String authors = salutation.trim()+" "+firstName+" "+lastName ;
				String title = "VegBank2002";
				String pubDate ="11-MAR-2002";
				String edition ="--";
				String seriesName ="--";
				String issueId ="--";
				String otherCitationDetails ="--";
				String pageNumber ="--";
				String isbn ="--";
				String issn ="--";
				String classSystem ="--";
				String commLevel ="--";
				String conceptStatus = "accepted";
				String nameStatus = "standard";
			
				//CREATE A COMMUNITY CODE
				String commCode = "";
				StringTokenizer t = new StringTokenizer(commName);
	  		while (t.hasMoreTokens() )
				{
					String buf = t.nextToken();
					buf = buf.substring(0, 1);
					commCode = commCode+buf.toUpperCase();
				}
					
				//SEE IF THE NAME ALREADY EXISTS IN THE DATABASE
				CommunityQueryStore qs = new CommunityQueryStore();
				qs = new CommunityQueryStore();
				Vector v = qs.getCommunityNames(commName);
				if ( v.size() < 1)
				{
					//assume that the name does not already exist in the db
					//so update the validation page and return it to the user
					String message = "You have entered a unique name; VegBank will be the reference";
					updateCommunityValidationPage(salutation, firstName, lastName, 
					emailAddress, orgName, commName, commCode, commLevel, conceptStatus, 
					nameStatus, authors, title, pubDate, edition, seriesName,
					issueId, otherCitationDetails, pageNumber, isbn, issn, classSystem, 
					message);
					//redirect the browser
					response.sendRedirect("/forms/valid.html");
				}
				else
				{
					String curName = v.elementAt(0).toString();
					qs = new CommunityQueryStore();
					Hashtable refHash = qs.getNameReference(curName);
					String nameRefTitle = (String)refHash.get("title");
					String nameRefAuthors = (String)refHash.get("authors");
					String nameRefPubDate = (String)refHash.get("pubDate");
					String nameRefEdition = (String)refHash.get("edition");
					String nameRefSeriesName = (String)refHash.get("seriesName");
					String nameRefIssueId = (String)refHash.get("issueId");
					String nameRefOtherCitationDetails = (String)refHash.get("otherCitationDetails");
					String nameRefPageNumber = (String)refHash.get("pageNumber");
					String nameRefISBN = (String)refHash.get("isbn");
					String nameRefISSN = (String)refHash.get("issn");
					String message =" This name already exists in the vegbank system and was <br>"
					+"has a refernce to: "+nameRefAuthors;
					
					sb.append("nameRefTitle: " + nameRefTitle);
					sb.append("nameRefAuthors: " + nameRefAuthors);
					sb.append("nameRefPubDate: " + nameRefPubDate);
					sb.append("nameRefPubEdition: " + nameRefEdition);
					sb.append("nameRefSeriesName: " + nameRefSeriesName);
					sb.append("nameRefIssueId: " + nameRefIssueId);
					sb.append("nameRefOtherCitationDetails: " + nameRefOtherCitationDetails);
					sb.append("nameRefPageNumber: " + nameRefPageNumber);
					sb.append("nameRefISBN: " + nameRefISBN);
					sb.append("nameRefISSN: " + nameRefISSN);
					System.out.println("DataSubmitServlet > " + sb.toString());
					
					
					//update the html page with the name refrence equal to the 
					//data retrieved from the database
					updateCommunityValidationPage(salutation, firstName, lastName, 
					emailAddress, orgName, commName, commCode, commLevel, conceptStatus, 
					nameStatus, nameRefAuthors, nameRefTitle, nameRefPubDate, nameRefEdition, 
					nameRefSeriesName, nameRefIssueId, nameRefOtherCitationDetails, 
					nameRefPageNumber, nameRefISBN, nameRefISSN, 
					authors, title, pubDate, edition, seriesName,
					issueId, otherCitationDetails, pageNumber, isbn, issn, 
					classSystem, message);
					
					//redirect the browser
					response.sendRedirect("/forms/valid.html");
				}
				
			}
			//THIS WHERE THE ACTUAL SUBMITTAL OF THE NEW COMMUNITY TAKES PLACE
			else if ( action.equals("submit") )
			{
				
				String salutation = request.getParameter("salutation");
				String givenName = request.getParameter("firstName");
				String surName = request.getParameter("lastName");
				String middleName = "";
				String orgName = request.getParameter("orgName");
				String contactInstructions = request.getParameter("emailAddress");
				
				String conceptReferenceTitle = request.getParameter("conceptRefTitle");
				String conceptReferenceAuthor = request.getParameter("conceptRefAuthors");
				String conceptReferenceDate = "12-MAR-2002";

				String nameReferenceTitle = request.getParameter("nameRefTitle");
				String nameReferenceAuthor = request.getParameter("nameRefAuthors");
				String nameReferenceDate = "12-MAR-2002";
				
				String communityCode = request.getParameter("communityCode");
				String communityLevel = request.getParameter("communityLevel");
				String communityName = request.getParameter("communityName");
				String dateEntered = "12-MAR-2002";
				String parentCommunity = "";
				String partyName = "vegbank";
				String otherName = "";
				System.out.println("DataSubmitServlet > submit vegCommunity");
				
				//SUBMIT THE DATA TO THE DATABASE
				VegCommunityLoader commLoader = new VegCommunityLoader();
				StringBuffer sbr = commLoader.insertGenericCommunity( salutation,  givenName, surName,
				middleName, orgName, contactInstructions,conceptReferenceTitle, 
				conceptReferenceAuthor, conceptReferenceDate, nameReferenceTitle,
				nameReferenceAuthor, nameReferenceDate, communityCode,communityLevel,
				communityName,  dateEntered, parentCommunity,  otherName  );
				
				String resultString = sbr.toString();
				
				System.out.println("DataSubmitServlet > submittal result: " + resultString);
				//IF SUCCESS THEN PREPARE AND RETURN A PAGE TO THE USER
				if ( resultString.toUpperCase().indexOf("TRUE") > 0 )
				{
					System.out.println("DataSubmitServlet > preparing results page");
					String resultPage = getSubmittalResultsPage(true, communityName, givenName, 
					surName, nameReferenceAuthor, conceptReferenceAuthor );
					sb.append( resultPage );
				}
				//UPDATE THE DATABASE SUMMARY TABLE
				new SqlFile().issueSqlFile(commUpdateScript);
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(sb);
	}
	
	
	/**
	 * method that returns a result page as a string based on a true or false
	 * 
	 * @param result -- true or false
	 * @param commName -- the name of the community
	 * @param givenName -- the given Name of the system user
	 * @param surName -- the surName of the system user
	 * @param nameReferenceAuthor -- the author of the name reference
	 * @param conceptReferenceAuthor -- the name of the concept reference auth.
	 */
	 private String getSubmittalResultsPage( boolean result, String commName, 
	 String givenName, String surName, String nameReferenceAuthor, 
	 String conceptReferenceAuthor)
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html> \n");
		 sb.append("<b> SUCCESSFUL SUBMITAL <b> \n");
		 sb.append("<br> \n");
		 sb.append("VegBank User: " + givenName + " " + surName + " <br> \n");
		 sb.append("Community Name: " + commName +" <br> \n");
		 sb.append("Name Reference: " + nameReferenceAuthor +" <br> \n");
		 sb.append("Concept Reference: " + conceptReferenceAuthor +" <br> \n");
		 sb.append("<a href=/forms/community-correlate.html> correlate communities  </a> \n");
		 sb.append("");
		 sb.append("");
		 sb.append("");
		 sb.append("</html> \n");
		 return(sb.toString() );
	 }
	
	
	
	/**
	 * method that handles updating the validation form for the community 
	 * submittal process
	 */
	 private boolean updateCommunityValidationPage(String salutation, 
	 String firstName, String lastName, String emailAddress, String orgName, 
	 String commName, String commCode, String commLevel, String conceptStatus, 
	 String nameStatus, String authors, String title, String pubDate, 
	 String edition,
	 String seriesName, String issueId, String otherCitationDetails, 
	 String pageNumber, String isbn, String issn, String classSystem, String message)
	 {
	 	boolean results = true;
		Hashtable replaceHash = new Hashtable();
		try
		{
			System.out.println("DataSubmitServlet > commName: " + commName);
			System.out.println("DataSubmitServlet > commLevel: " + commCode);
			System.out.println("DataSubmitServlet > commCode: " + commLevel);
			System.out.println("DataSubmitServlet > salutation: " + salutation);
			System.out.println("DataSubmitServlet > firstname: " + firstName);
			System.out.println("DataSubmitServlet > lastName: " + lastName );
			System.out.println("DataSubmitServlet > emailAddress: " + emailAddress);
			System.out.println("DataSubmitServlet > orgname: " + orgName);
			System.out.println("DataSubmitServlet > authors: " + authors);
			System.out.println("DataSubmitServlet > titlke: " + title);
			System.out.println("DataSubmitServlet > pubDate: " + pubDate);
			System.out.println("DataSubmitServlet > ");
			System.out.println("DataSubmitServlet > ");
			
			replaceHash.put("communityName", ""+commName);
			replaceHash.put("communityLevel", ""+commLevel);
			replaceHash.put("communityCode", ""+commCode);
			replaceHash.put("conceptStatus", ""+conceptStatus);
			replaceHash.put("nameStatus", ""+nameStatus);
			
			replaceHash.put("level", "unknown");
			replaceHash.put("salutation", ""+salutation);
			replaceHash.put("firstName", ""+firstName);
			replaceHash.put("lastName", ""+lastName);
			replaceHash.put("emailAddress", ""+emailAddress);
			replaceHash.put("orgName" , ""+orgName);
			
			
			replaceHash.put("nameRefAuthors" , ""+authors);
			replaceHash.put("nameRefTitle" , ""+ title);
			replaceHash.put("nameRefPubDate" , ""+pubDate);
			replaceHash.put("nameRefEdition", ""+edition);
			replaceHash.put("nameRefSeriesName", ""+seriesName);
			replaceHash.put("nameRefIssueId", ""+ issueId);
			replaceHash.put("nameRefOtherCitationDetails", ""+otherCitationDetails);
			replaceHash.put("nameRefPageNumber", ""+pageNumber );
			replaceHash.put("nameRefISBN", ""+ isbn );
			replaceHash.put("nameRefISSN", ""+issn);
			
			replaceHash.put("conceptRefAuthors" , ""+authors);
			replaceHash.put("conceptRefTitle" , ""+ title);
			replaceHash.put("conceptRefPubDate" , ""+pubDate);
			replaceHash.put("conceptRefEdition", ""+edition);
			replaceHash.put("conceptRefSeriesName", ""+seriesName);
			replaceHash.put("conceptRefIssueId", ""+issueId);
			replaceHash.put("conceptRefOtherCitationDetails", ""+otherCitationDetails);
			replaceHash.put("conceptRefPageNumber", ""+pageNumber );
			replaceHash.put("conceptRefISBN", ""+isbn );
			replaceHash.put("conceptRefISSN", ""+issn);
			
			
			replaceHash.put("authors" , ""+authors);
			replaceHash.put("title" , ""+title);
			replaceHash.put("pubDate" , ""+pubDate);
			replaceHash.put("edition", ""+edition);
			replaceHash.put("seriesName", ""+seriesName);
			replaceHash.put("issueId", ""+issueId );
			replaceHash.put("otherCitationDetails", ""+otherCitationDetails);
			replaceHash.put("pageNumber", ""+pageNumber );
			replaceHash.put("isbn", ""+isbn );
			replaceHash.put("issn", ""+issn );
			replaceHash.put("classSystem", ""+classSystem );
			
			replaceHash.put("message", message );
			//replaceHash.put("", );
			//replaceHash.put("", );
			//replaceHash.put("", );
			//su.filterTokenFile(communityValidationTemplate, communityValidationForm, replaceHash);
		}
		catch( Exception e)
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(results);
	 }
	 
	 
	 
	 private boolean updateCommunityValidationPage(String salutation, 
	 String firstName, String lastName, String emailAddress, String orgName, 
	 String commName, String commCode, String commLevel, String conceptStatus, 
	 String nameStatus, 
	 
	 String nameRefAuthors, String nameRefTitle, String nameRefPubDate, 
	 String nameRefEdition,
	 String nameRefSeriesName, String nameRefIssueId, String nameRefOtherCitationDetails, 
	 String nameRefPageNumber, String nameRefISBN, String nameRefISSN, 
	 
	 String conceptRefAuthors, String conceptRefTitle, String conceptRefPubDate, 
	 String conceptRefEdition,
	 String conceptRefSeriesName, String conceptRefIssueId, String conceptRefOtherCitationDetails, 
	 String conceptRefPageNumber, String conceptRefISBN, String conceptRefISSN, 
	 
	 String classSystem, String message)
	 {
	 	boolean results = true;
		Hashtable replaceHash = new Hashtable();
		try
		{
			System.out.println("DataSubmitServlet > commName: " + commName);
			System.out.println("DataSubmitServlet > commLevel: " + commCode);
			System.out.println("DataSubmitServlet > commCode: " + commLevel);
			
			
			System.out.println("DataSubmitServlet > salutation: " + salutation);
			System.out.println("DataSubmitServlet > firstname: " + firstName);
			System.out.println("DataSubmitServlet > lastName: " + lastName );
			System.out.println("DataSubmitServlet > emailAddress: " + emailAddress);
			System.out.println("DataSubmitServlet > orgname: " + orgName);
			
			System.out.println("DataSubmitServlet > nameRefAuthors: " + nameRefAuthors);
			System.out.println("DataSubmitServlet > nameRefTitle: " + nameRefTitle);
			System.out.println("DataSubmitServlet > nameRefPubDate: " + nameRefPubDate);
			
			System.out.println("DataSubmitServlet > conceptRefAuthors: " + conceptRefAuthors);
			System.out.println("DataSubmitServlet > conceptRefTitle: " + conceptRefTitle);
			System.out.println("DataSubmitServlet > conceptRefPubDate: " + conceptRefPubDate);
			
			
			replaceHash.put("communityName", ""+commName);
			replaceHash.put("communityLevel", ""+commLevel);
			replaceHash.put("communityCode", ""+commCode);
			replaceHash.put("conceptStatus", ""+conceptStatus);
			replaceHash.put("nameStatus", ""+nameStatus);
			
			replaceHash.put("level", "unknown");
			replaceHash.put("salutation", ""+salutation);
			replaceHash.put("firstName", ""+firstName);
			replaceHash.put("lastName", ""+lastName);
			replaceHash.put("emailAddress", ""+emailAddress);
			replaceHash.put("orgName" , ""+orgName);
			
			replaceHash.put("nameRefAuthors" , ""+nameRefAuthors);
			replaceHash.put("nameRefTitle" , ""+ nameRefTitle);
			replaceHash.put("nameRefPubDate" , ""+nameRefPubDate);
			replaceHash.put("nameRefEdition", ""+nameRefEdition);
			replaceHash.put("nameRefSeriesName", ""+nameRefSeriesName);
			replaceHash.put("nameRefIssueId", ""+ nameRefIssueId);
			replaceHash.put("nameRefOtherCitationDetails", ""+nameRefOtherCitationDetails);
			replaceHash.put("nameRefPageNumber", ""+nameRefPageNumber );
			replaceHash.put("nameRefISBN", ""+nameRefISBN );
			replaceHash.put("nameRefISSN", ""+nameRefISSN);
			
			replaceHash.put("conceptRefAuthors" , ""+conceptRefAuthors);
			replaceHash.put("conceptRefTitle" , ""+ conceptRefTitle);
			replaceHash.put("conceptRefPubDate" , ""+conceptRefPubDate);
			replaceHash.put("conceptRefEdition", ""+conceptRefEdition);
			replaceHash.put("conceptRefSeriesName", ""+conceptRefSeriesName);
			replaceHash.put("conceptRefIssueId", ""+conceptRefIssueId);
			replaceHash.put("conceptRefOtherCitationDetails", ""+conceptRefOtherCitationDetails);
			replaceHash.put("conceptRefPageNumber", ""+conceptRefPageNumber );
			replaceHash.put("conceptRefISBN", ""+conceptRefISBN );
			replaceHash.put("conceptRefISSN", ""+conceptRefISSN);
			replaceHash.put("message", ""+message);
			replaceHash.put("classSystem", ""+classSystem );
			//su.filterTokenFile(communityValidationTemplate, communityValidationForm, replaceHash);
		}
		catch( Exception e)
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(results);
	 }
}
