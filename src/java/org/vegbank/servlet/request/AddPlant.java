/*
 *	'$RCSfile: AddPlant.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-30 18:01:59 $'
 *	'$Revision: 1.3 $'
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vegbank.common.Constants;
import org.vegbank.common.command.QueryReferences;
import org.vegbank.common.command.QueryReferences.ReferenceSummary;
import org.vegbank.common.model.Plant;
import org.vegbank.common.model.PlantParty;
import org.vegbank.common.model.Reference;
import org.vegbank.plants.datasink.DBPlantWriter;
import org.vegbank.servlet.util.HTMLUtil;
import org.vegbank.servlet.util.ServletUtility;

import databaseAccess.TaxonomyQueryStore;

/**
 * @author farrell
 */

public class AddPlant implements Constants
{

	private static final ServletUtility su = new ServletUtility();
	// this is the pre-transformed init template 
	private static String formsDir =
		"/usr/local/devtools/jakarta-tomcat/webapps/vegbank/forms/";
	private static String plantNameRectificationTemplate =
		formsDir + "submit-plantname-rectification.html";
	private static String plantReferenceTemplate =
		formsDir + "submit-plant-references.html";
	private static String plantStatusUsageTemplate =
		formsDir + "submit-plantstatususage.html";
	private static String plantSubmittalReceiptTemplate =
		formsDir + "submit-plantsubmittal-receipt.html";
	//this is the file that has the updated tokens and should be shown to the client
	private static String genericTemplate = formsDir + "generic_form.html";

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
	public StringBuffer execute(
		HttpServletRequest request,
		HttpServletResponse response,
		Hashtable userAtts) throws IOException
	{
		StringBuffer sb = new StringBuffer();
		PrintWriter out = response.getWriter();

		//getSession
		HttpSession session = request.getSession();
		Plant plant = (Plant) session.getAttribute("Plant");

		if (plant == null)
		{
			// CONSTRUCT A NEW INSTANCE OF A PLANT
			plant = new Plant();
			session.setAttribute("Plant", plant);
		}

		TaxonomyQueryStore tqs = new TaxonomyQueryStore();
		try
		{
			String taxonDescription = "";
			String salutation = "";
			String firstName = "";
			String lastName = "";
			String emailAddress = "";
			String orgName = "";

			String action = request.getParameter("action");
			// if the wizard is initiated the plant names will be checked against the 
			// vegbank database for near matches and a form with these matches will be 
			// sent to the client
			if (action.equals("init"))
			{

				System.out.println("DataSubmitServlet > init plantTaxa");

				PlantParty party = new PlantParty();

				party.setGivenName((String) userAtts.get("givenName"));
				party.setSurName((String) userAtts.get("surName"));
				party.setOrganizationName((String) userAtts.get("institution"));

				plant.setPlantParty(party);

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

				Vector lv = tqs.getPlantTaxonSummary(longName, "%");
				Vector sv = tqs.getPlantTaxonSummary(shortName, "%");
				Vector cv = tqs.getPlantTaxonSummary(code, "%");
				if (lv.size() > 0)
				{
					longNameMessage = "Currently Exists in VegBank";
				}
				else
				{
					longNameMessage = "Not Currently in VegBank";
				}
				if (sv.size() > 0)
				{
					shortNameMessage = "Currently Exists in VegBank";
				}
				else
				{
					shortNameMessage = "Not Currently in VegBank";
				}
				if (cv.size() > 0)
				{
					codeMessage = "Currently Exists in VegBank";
				}
				else
				{
					codeMessage = "Not Currently in VegBank";
				}

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
			else if (action.equals("namerectification"))
			{
				System.out.println(
					"DataSubmitServlet > performing the name rectification ");
				// GET THE NAME REFERENCES FOR THE INPUT NAMES OR GIVE THE 
				// USER THE FORM TO FILL OUT
				String longNameMatch = request.getParameter("longNameMatches");
				String shortNameMatch = request.getParameter("shortNameMatches");
				String codeMatch = request.getParameter("codeMatches");

				// UPDATE THE DATA CLASS -- implicitly creates plantusages
				plant.setScientificName(longNameMatch);
				plant.setCommonName(shortNameMatch);
				plant.setCode(codeMatch);

				System.out.println("longName: " + " match: " + longNameMatch);
				Hashtable longNameRef = tqs.getPlantNameReference(longNameMatch);
				System.out.println("longNameRef: " + longNameRef.toString());
				System.out.println("shortName: " + " match: " + shortNameMatch);
				Hashtable shortNameRef = tqs.getPlantNameReference(shortNameMatch);
				System.out.println("code: " + " match: " + codeMatch);
				Hashtable codeNameRef = tqs.getPlantNameReference(codeMatch);

				updatePlantNameReferencePage(
					emailAddress,
					longNameRef,
					shortNameRef,
					codeNameRef,
					out);

			}
			// THIS IS WHERE THE NAME REFERENCES INFORMATION IS SUBMITTED TO THE SERVLET
			// AND WHERE THOSE ATTRIBUTES SHOULD BE STORED
			else if (action.equals("references"))
			{
				System.out.println("DataSubmitServlet > getting the  references ");

				String commonReferenceId = request.getParameter("common.reference_id");
				String conceptReferenceId =
					request.getParameter("concept.reference_id");
				String scientificReferenceId =
					request.getParameter("scientific.reference_id");
				String codeReferenceId = request.getParameter("code.reference_id");
				String conceptDescription = request.getParameter("conceptDescription");


			  String NOREFERENCE = "none";
        
        if (! commonReferenceId.equals(NOREFERENCE)) 
        {
          System.out.println("Accepting Reference: " + commonReferenceId);
          plant.setCommonNameReferenceId(commonReferenceId);
        }
				
        plant.setConceptReferenceId(conceptReferenceId);
				plant.setPlantDescription(conceptDescription);
	      
        if (! scientificReferenceId.equals(NOREFERENCE)) 
        {
				  plant.setScientificNameReferenceId(scientificReferenceId);
        }
        
        if (! codeReferenceId.equals(NOREFERENCE)) 
        {
          System.out.println("-->" + codeReferenceId);
			    plant.setCodeNameReferenceId(codeReferenceId);
        }

				updatePlantStatusUsagePage(
					emailAddress,
					plant.getScientificName(),
					plant.getCommonName(),
					plant.getCode(),
					out,
					userAtts);
			}
			// STEP WHEREBY THE USER SUBMITS THE STATUS USAGE DATA AND 
			// THE RECIPT IS RETURNED
			else if (action.equals("plantstatususage"))
			{
				System.out.println(
					"DataSubmitServlet > getting the status-usage data, returning recipt");
				String conceptStatus = request.getParameter("conceptStatus");
				
				// Dates automatically allocated
				//String statusStartDate = request.getParameter("statusStartDate");
				//String statusStopDate = request.getParameter("statusStopDate");
				//String usageStartDate = request.getParameter("statusStartDate");
				//String usageStopDate = request.getParameter("statusStopDate");
				// UPDATE THE DATES B/C THEY ARE REQUIRED BY THE LOADER
				
				String statusDescription = request.getParameter("statusDescription");
				String taxonLevel = request.getParameter("taxonLevel");
				String plantParentName = request.getParameter("plantParentName");

				plant.setParentName(plantParentName);
				plant.setStatus(conceptStatus);
				plant.setStatusStartDate( su.getCurrentDate() );
				plant.setStatusPartyComments(statusDescription);
				plant.setClassLevel(taxonLevel);
				
				updatePlantSubmittalRecipt(emailAddress, plant, out, userAtts);
			}
			// STEP WHERE THE PLANT ACTUALLY GETS LOADED TO THE DATABASE
			else if (action.equals("plantsubmittalreceipt"))
			{
				System.out.println("submittal to the database taking place ");

				DBPlantWriter dbw = new DBPlantWriter(plant, null);
				boolean results = dbw.isWriteSuccess();

				// WRITE THE RESULTS BACK TO THE BROWSER
				String receipt = this.getPlantInsertionReceipt(results, plant, userAtts);
				out.println(receipt);
			}
			else
			{
				System.out.println(
					"DataSubmitServlet > unknown step in the plant taxa process "
						+ action);
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception:  " + e.getMessage());
			e.printStackTrace();
		}
		return (sb);
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

	private String getPlantInsertionReceipt(boolean results, Plant plant, Hashtable userAtts)
	{
		StringWriter output = new StringWriter();
		StringBuffer sb = new StringBuffer();
		try
		{

			//get the required elements from the planttaxonobject
			String email = (String) userAtts.get("emailAddress");
			String name = (String) userAtts.get("surName");
			String institution = (String) userAtts.get("institution");
			String sciName = plant.getScientificName();
			String commonName = plant.getCommonName();
			String code = plant.getCode();
			String conceptDescription = plant.getPlantDescription();
			String status = plant.getStatus();
			String level = plant.getClassLevel();

			// create a table to be put into the generic form 
			sb.append("<span class=\"itemsmall\">");
			sb.append("Insertion Results: " + results + "<br> \n");
			sb.append("Email: " + email + "<br> \n");
			sb.append("Name: " + name + "<br> \n");
			sb.append("Institution: " + institution + "<br> \n");
			sb.append("Scientific Name: " + sciName + "<br> \n");
			sb.append("Common Name: " + commonName + "<br> \n");
			sb.append("Code: " + code + "<br> \n");
			sb.append("Concept Description: " + conceptDescription + "<br> \n");
			sb.append("Status Start: " + plant.getStatusStartDate() + "<br> \n");
			sb.append("Status Stop: " + plant.getStatusStopDate() + "<br> \n");
			sb.append("Status: " + status + "<br> \n");
			sb.append("Taxonomic Level: " + level + "<br> \n");

			sb.append("</span>");
			// set up the filter tokens
			Hashtable replaceHash = new Hashtable();
			replaceHash.put("messages", sb.toString());
			su.filterTokenFile(genericTemplate, output, replaceHash);

		}
		catch (Exception e)
		{
			System.out.println("Exception > " + e.getMessage());
		}
		return (output.toString());
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
			replaceHash.put("emailAddress", "" + emailAddress);

			//plant name-related attributes
			replaceHash.put("longName", longName);
			replaceHash.put("shortName", shortName);
			replaceHash.put("code", code);
			replaceHash.put("longNameMessage", longNameMessage);
			replaceHash.put("shortNameMessage", shortNameMessage);
			replaceHash.put("codeMessage", codeMessage);

			// drop down with near matches
			StringBuffer sb = new StringBuffer();
			sb.append("<select name=\"longNameMatches\">");
			sb.append("<option selected>  " + longName + " </option>");
			for (int i = 0; i < longNameMatches.size(); i++)
			{
				sb.append(
					"<option> " + (String) longNameMatches.elementAt(i) + " </option>");
			}
			sb.append("</select>");
			replaceHash.put("longNameNearMatches", sb.toString());

			sb = new StringBuffer();
			sb.append("<select name=\"shortNameMatches\">");
			sb.append("<option selected>  " + shortName + " </option>");
			for (int i = 0; i < shortNameMatches.size(); i++)
			{
				sb.append(
					"<option> " + (String) shortNameMatches.elementAt(i) + " </option>");
			}
			sb.append("</select>");
			replaceHash.put("shortNameNearMatches", sb.toString());

			sb = new StringBuffer();
			sb.append("<select name=\"codeMatches\">");
			sb.append("<option selected>  " + code + " </option>");
			for (int i = 0; i < codeMatches.size(); i++)
			{
				sb.append(
					"<option> " + (String) codeMatches.elementAt(i) + " </option>");
			}
			sb.append("</select>");
			replaceHash.put("codeNearMatches", sb.toString());

			//the message telling the client that there are near matches
			replaceHash.put(
				"longNameNearMatchMessages",
				"VegBank has " + longNameMatches.size() + " near matches");
			replaceHash.put(
				"shortNameNearMatchMessages",
				"VegBank has " + shortNameMatches.size() + " near matches");
			replaceHash.put(
				"codeNearMatchMessages",
				"VegBank has " + codeMatches.size() + " near matches");

			su.filterTokenFile(plantNameRectificationTemplate, out, replaceHash);
		}
		catch (Exception e)
		{
			System.out.println("Exception:  " + e.getMessage());
			e.printStackTrace();
			return (false);
		}
		return (true);
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
		Reference ref = new Reference();
		try
		{
			Hashtable replaceHash = new Hashtable();

			// THE INFORMATION ABOUT THE SUBMITTER
			replaceHash.put("emailAddress", "" + emailAddress);
			replaceHash.put("givenName", "" + userAtts.get("givenName"));
			replaceHash.put("surName", "" + userAtts.get("surName"));

			// THE NAMES OF THE PLANT
			replaceHash.put("longName", "" + plant.getScientificName());
			replaceHash.put("shortName", "" + plant.getCommonName());
			replaceHash.put("code", "" + plant.getCode());

			// THE CONCEPT
			replaceHash.put("conceptDescription", plant.getPlantDescription());

			Collection references = getReferencesCollection();
			
			// THE REFERENCES
			
			replaceHash.put(
				"conceptReferenceName",
				this.getReferenceShortName( plant.getConceptReferenceId(), references )
			);
			replaceHash.put(
				"scientificReferenceName",
				this.getReferenceShortName( plant.getScientificNameReferenceId(), references )
			);
			replaceHash.put(
				"codeReferenceName",
				this.getReferenceShortName( plant.getCodeNameReferenceId(), references )
			);
			replaceHash.put(
				"commonReferenceName",
				this.getReferenceShortName( plant.getCommonNameReferenceId(), references )
			);

			// STATUS AND USAGE
			replaceHash.put("conceptStatus", "" + plant.getStatus());
			replaceHash.put("statusDescription", "" + plant.getStatusPartyComments());
			replaceHash.put("taxonLevel",  plant.getClassLevel());
			
			replaceHash.put("plantParentName", ""+ plant.getParentName());
			//	FIXME: Ignoring plantparent reference for now
			//			 replaceHash.put("plantParentRefTitle", ""+plantParentRefTitle);
			//			 replaceHash.put("plantParentRefAuthors", ""+plantParentRefAuthors);

			su.filterTokenFile(plantSubmittalReceiptTemplate, out, replaceHash);
		}
		catch (Exception e)
		{
			System.out.println("Exception:  " + e.getMessage());
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
			String longName = (String) longNameRef.get("plantName");
			String shortName = (String) shortNameRef.get("plantName");
			String code = (String) codeNameRef.get("plantName");

			String longNameReferenceId = (String) longNameRef.get("plantReferenceId");
			String shortNameReferenceId =
				(String) shortNameRef.get("plantReferenceId");
			String codeNameReferenceId = (String) codeNameRef.get("plantReferenceId");

			System.out.println("ref id: " + longNameReferenceId);

			// set up the filter tokens
			Hashtable replaceHash = new Hashtable();
			replaceHash.put("emailAddress", "" + emailAddress);
			replaceHash.put("longName", "" + longName);
			replaceHash.put("shortName", "" + shortName);
			replaceHash.put("code", "" + code);

			// long name
			if (longNameReferenceId != null
				&& !longNameReferenceId.trim().equals("null"))
			{
				replaceHash.put("longNameReference", "" + longNameRef.toString());
			}
			else
			{
				replaceHash.put("longNameReference", "" + HTMLUtil.getReferencesDropDown());
			}
			// short name 
			if (shortNameReferenceId != null
				&& !shortNameReferenceId.trim().equals("null"))
			{
				replaceHash.put("shortNameReference", "" + shortNameRef.toString());
			}
			else
			{
				replaceHash.put(
					"shortNameReference",
					"" + HTMLUtil.getReferencesDropDown());
			}
			// code
			if (codeNameReferenceId != null
				&& !codeNameReferenceId.trim().equals("null"))
			{
				replaceHash.put("codeNameReference", "" + codeNameRef.toString());
			}
			else
			{
				replaceHash.put("codeNameReference", "" + HTMLUtil.getReferencesDropDown());
			}

			replaceHash.put("referencesDropDown", HTMLUtil.getReferencesDropDown());

			su.filterTokenFile(plantReferenceTemplate, out, replaceHash);

		}
		catch (Exception e)
		{
			System.out.println("Exception:  " + e.getMessage());
			e.printStackTrace();
		}
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

			replaceHash.put("longName", "" + longName);
			replaceHash.put("shortName", "" + shortName);
			replaceHash.put("code", "" + code);

			replaceHash.put("emailAddress", "" + emailAddress);
			replaceHash.put("plantPartyGivenName", "" + userAtts.get("givenName"));
			replaceHash.put("plantPartySurName", "" + userAtts.get("surName"));
			replaceHash.put(
				"plantPartyInstitution",
				"" + userAtts.get("institution"));
			replaceHash.put("plantPartyEmailAddress", "" + emailAddress);

			su.filterTokenFile(plantStatusUsageTemplate, out, replaceHash);
			// the calling method will redirect the browser
		}
		catch (Exception e)
		{
			System.out.println("Exception:  " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String getReferenceShortName( String pKey, Collection references )
	{
		String noReferenceMessage = "No Reference Selected";
		// Check for obviously nonvalid pKey
			try
			{
				int pK = new Integer(pKey).intValue();
			}
			catch (NumberFormatException e)
			{
				// This is not an int so return no reference
				return noReferenceMessage;
			}

		
		Iterator refIt = references.iterator();
		while (refIt.hasNext())
		{
			ReferenceSummary refSumm = (ReferenceSummary) refIt.next();
			if ( refSumm.getId().equals(pKey) )
			{
				return refSumm.getTitle();
			}
		}
		return noReferenceMessage;
	}

	private Collection getReferencesCollection()
	{
		Collection references = null;
		
		QueryReferences qr = new QueryReferences();
		try
		{
			references = qr.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return references;
	}
}
