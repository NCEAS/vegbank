/*
 *	'$RCSfile: AddCommunity.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 05:46:24 $'
 *	'$Revision: 1.1 $'
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
 
package org.vegbank.servlet.request;

import VegCommunityLoader;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.servlet.util.HTMLUtil;
import org.vegbank.servlet.util.ServletUtility;

import databaseAccess.CommunityQueryStore;

/**
 * @author farrell
 */

public class AddCommunity
{
	private static final ServletUtility su = new ServletUtility();
	private static String commUpdateScript = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/update_community_summary.sql";
	private static String communityValidationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/community-submit_valid.html";
	
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
	public StringBuffer execute(
		HttpServletRequest request, 
		HttpServletResponse response) throws IOException
	{
		StringBuffer sb = new StringBuffer();
		PrintWriter out = response.getWriter();
		try
		{
			String action = request.getParameter("action");
			if ( action.equals("init") )
			{
				
				System.out.println("AddCommunity > init AddCommunity");
				String salutation = request.getParameter("salutation");;
				String firstName =  request.getParameter("firstName");
				String lastName =  request.getParameter("lastName");
				String emailAddress =  request.getParameter("emailAddress");
				String orgName =  request.getParameter("orgName");
				String commName = request.getParameter("communityName");
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
					updateCommunityValidationPage(
						salutation,
						firstName,
						lastName,
						emailAddress,
						orgName,
						commName,
						commCode,
						commLevel,
						conceptStatus,
						nameStatus,
						classSystem,
						message,
						out);
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
					System.out.println("AddCommunity > " + sb.toString());
					
					
					//update the html page with the name refrence equal to the 
					//data retrieved from the database
					updateCommunityValidationPage(
						salutation,
						firstName,
						lastName,
						emailAddress,
						orgName,
						commName,
						commCode,
						commLevel,
						conceptStatus,
						nameStatus,
						classSystem,
						message,
						out);
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
				
				String conceptRefId = request.getParameter("concept.reference_id");
				String nameRefId = request.getParameter("name.reference_id");
			

				String communityCode = request.getParameter("communityCode");
				String communityLevel = request.getParameter("communityLevel");
				String communityName = request.getParameter("communityName");
				String dateEntered = "12-MAR-2002";
				String parentCommunity = "";
				String partyName = "vegbank";
				String otherName = "";
				System.out.println("AddCommunity > submit vegCommunity");
				
				//SUBMIT THE DATA TO THE DATABASE
				VegCommunityLoader commLoader = new VegCommunityLoader();
				StringBuffer sbr =
					commLoader.insertGenericCommunity(
						salutation,
						givenName,
						surName,
						middleName,
						orgName,
						contactInstructions,
						new Integer(conceptRefId).intValue(),
						new Integer(nameRefId).intValue(),
						communityCode,
						communityLevel,
						communityName,
						dateEntered,
						parentCommunity,
						otherName);
				
				String resultString = sbr.toString();
				
				System.out.println("AddCommunity > submittal result: " + resultString);
				//IF SUCCESS THEN PREPARE AND RETURN A PAGE TO THE USER
				if ( resultString.toUpperCase().indexOf("TRUE") > 0 )
				{
					System.out.println("AddCommunity > preparing results page");
					String resultPage =
						getSubmittalResultsPage(
							true,
							communityName,
							givenName,
							surName);
					sb.append( resultPage );
				}
			}
		}
		catch( Exception e ) 
		{
			System.out.println("AddCommunity > Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(sb);
	}
	
	
	

	/**
	 * method that handles updating the validation form for the community 
	 * submittal process
	 */
	private boolean updateCommunityValidationPage(
		String salutation,
		String firstName,
		String lastName,
		String emailAddress,
		String orgName,
		String commName,
		String commCode,
		String commLevel,
		String conceptStatus,
		String nameStatus,
		String classSystem,
		String message,
		Writer out)
	{
		boolean results = true;
		Hashtable replaceHash = new Hashtable();
		try
		{
			System.out.println("AddCommunity > commName: " + commName);
			System.out.println("AddCommunity > commLevel: " + commCode);
			System.out.println("AddCommunity > commCode: " + commLevel);
			System.out.println("AddCommunity > salutation: " + salutation);
			System.out.println("AddCommunity > firstname: " + firstName);
			System.out.println("AddCommunity > lastName: " + lastName);
			System.out.println("AddCommunity > emailAddress: " + emailAddress);
			System.out.println("AddCommunity > orgname: " + orgName);
			System.out.println("AddCommunity > ");
			System.out.println("AddCommunity > ");

			replaceHash.put("communityName", "" + commName);
			replaceHash.put("communityLevel", "" + commLevel);
			replaceHash.put("communityCode", "" + commCode);
			replaceHash.put("conceptStatus", "" + conceptStatus);
			replaceHash.put("nameStatus", "" + nameStatus);

			replaceHash.put("level", "unknown");
			replaceHash.put("salutation", "" + salutation);
			replaceHash.put("firstName", "" + firstName);
			replaceHash.put("lastName", "" + lastName);
			replaceHash.put("emailAddress", "" + emailAddress);
			replaceHash.put("orgName", "" + orgName);
			replaceHash.put("classSystem", "" + classSystem);
			replaceHash.put("referencesDropDown", HTMLUtil.getReferencesDropDown());

			replaceHash.put("message", message);
			su.filterTokenFile(communityValidationTemplate, out, replaceHash);
		}
		catch (Exception e)
		{
			System.out.println("AddCommunity > Exception:  " + e.getMessage());
			e.printStackTrace();
		}
		return (results);
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
	private String getSubmittalResultsPage(
		boolean result,
		String commName,
		String givenName,
		String surName)
	 {
	 	
	 	// FIXME: This is Uggly ....
	 	
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html> \n");
		 sb.append("<b> SUCCESSFUL SUBMITAL <b> \n");
		 sb.append("<br> \n");
		 sb.append("VegBank User: " + givenName + " " + surName + " <br> \n");
		 sb.append("Community Name: " + commName +" <br> \n");
		 sb.append("<a href=\"/vegbank/forms/community-correlate.html\"> correlate communities  </a> \n");
		 sb.append("");
		 sb.append("");
		 sb.append("");
		 sb.append("</html> \n");
		 return(sb.toString() );
	 }

}
