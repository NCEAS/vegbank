/*
 *	'$RCSfile: InterpretPlantAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-07-27 21:55:07 $'
 *	'$Revision: 1.5 $'
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

package org.vegbank.ui.struts;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.ui.struts.VegbankAction;
import org.vegbank.plots.datasource.DBModelBeanReader;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.model.Taxoninterpretation;
import org.vegbank.common.model.Taxonobservation;


/**
 * InterpretPlant
 * 1: Present an empty form for interpretation
 * 2: Add a new taxon interpretation, display result page
 * 
 * @author anderson
 */
public class InterpretPlantAction extends VegbankAction {

	private static Log log = LogFactory.getLog(InterpretPlantAction.class); 
	private static final String CANCEL_URL = Utility.SERVER_ADDRESS + "/get/std/observation/";

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {


		String actionParam = mapping.getParameter();
		if (actionParam == null) {
			actionParam = "n/a";
		}

		log.debug("In action InterpretPlantAction as " + actionParam);
		ActionErrors errors = new ActionErrors();
		InterpretPlantForm ipForm = (InterpretPlantForm)form; 
		String tobsAC = request.getParameter("tobsAC");

		// for validation,
		// store the tobsAC in the session
		if (Utility.isStringNullOrEmpty(tobsAC)) {
			log.debug("Getting tobsAC from session");
			tobsAC = (String)request.getSession().getAttribute("tobsAC");
		} else {
			log.debug("Putting tobsAC in session");
			request.getSession().setAttribute("tobsAC", tobsAC);
		}


		/*
		String clear = request.getParameter("clear");
		if (!Utility.isStringNullOrEmpty(clear)) {
			log.debug("clearing out form");
			ipForm = new InterpretPlantForm();
		}
		*/


		try {
			if (isCancelled(request)) {
				log.debug("CANCELLING");
				return new ActionForward(CANCEL_URL + ipForm.getObservation_id(), true);
			}

			log.debug("Interpreting tobs " + tobsAC);

			// these come in handy
			request.setAttribute("tobsAC", tobsAC);
			ipForm.setTaxonInterpretation(new Taxoninterpretation());
			request.setAttribute("formBean", ipForm);

			////////////////////////////////////////////////////
			// ACTION
			////////////////////////////////////////////////////
			if (actionParam.equals("edit")) {

				log.debug("Leaving InterpretPlantAction (edit)");
				return mapping.findForward("edit");


			} else if (actionParam.equals("save")) {

				// get the taxonobservation_id
				log.debug("Calling ipForm.getTaxonInterpretation()");
				Taxoninterpretation tint = ipForm.getTaxonInterpretation();

				ResultSet rs = null;
				DatabaseAccess da = new DatabaseAccess();
				
				// set the tobsId (from the form) in the T-Int
				tint.setTaxonobservation_id(Long.parseLong(ipForm.getTobsId()));

				//
				// Get plantname_id
				//
				log.debug("checking for plantName: " + 
							Utility.encodeForDB(ipForm.getPlantName()));
				rs = da.issueSelect(
						"SELECT plantname_id FROM plantname WHERE lower(plantname)='" +
							Utility.encodeForDB(ipForm.getPlantName()) + "'");

				// can find plantName?
				if (rs.next()) {
					// use the extant plantName 
					log.debug("using extant plantName");
					tint.setPlantname_id(rs.getLong(1));
                    da.closeStatement();

				} else {
					// plantName does not yet exist, so store it
					String ins = "INSERT INTO plantname (plantname,dateentered) VALUES ('" + 
							Utility.encodeForDB(ipForm.getPlantName()) + "', now())";
					log.debug("adding new plantName: " + ins);
					da.issueUpdate(ins);
				}
				
				//
				// Get plantconcept_id
				// 
				log.debug("getting plantconcept_id with: " + ipForm.getPcAC());
				String query = "SELECT plantconcept_id FROM plantconcept WHERE lower(accessioncode)='" +
						ipForm.getPcAC().toLowerCase().trim() + "'";
				rs = da.issueSelect(query);


				if (rs.next()) {
					log.debug("setting plantconcept_id");
					tint.setPlantconcept_id(rs.getLong(1));
                    da.closeStatement();

				} else {
					log.error("Can't find plantconcept_id for given AC: " + ipForm.getPcAC());
					errors.add(Globals.ERROR_KEY, new ActionMessage(
								"errors.general", 
								"The plant concept accession code you entered could not be found."));
					saveErrors(request, errors);
					return mapping.findForward("edit");
				}
				
				// 
				// Set the interpretationdate
				//
				tint.setInterpretationdate("now()");
				
				// 
				// Set party_id
				//
				tint.setParty_id(new Long(getUser(request.getSession()).getPartyid()).longValue());
				
				// 
				// Set role_id
				//
				rs = da.issueSelect("SELECT role_id FROM aux_role WHERE LOWER(rolecode)='not specified'");
				if (rs.next()) {
					tint.setRole_id(rs.getLong(1));
                    da.closeStatement();
				} else {
					log.error("Can't find role_id for rolecode: 'Not specified'");
					errors.add(Globals.ERROR_KEY, new ActionMessage(
								"errors.general", 
								"Bad rolecode, 'not specified'"));
					saveErrors(request, errors);
					return mapping.findForward("vberror");
				}

				
				// 
				// Set other tint fields
				//
				tint.setOriginalinterpretation("false");
				tint.setCurrentinterpretation("false");
				tint.setCurrentinterpretation("false");
				tint.setEmb_taxoninterpretation("0");

				//log.debug("tint:\n" + tint.toXML());
				rs.close();
				
				// save new taxonInterpretation record
				log.debug("inserting new taxoninterpretation");
				VBModelBeanToDB db = new VBModelBeanToDB();
				db.insert(tint);
				

				ActionMessages messages = new ActionMessages();
				messages.add("saved", new ActionMessage(
							"errors.general", 
							"Thank you.  Your interpretation has been saved."));
				saveMessages(request, messages);

				request.getSession().removeAttribute("tobsAC");

				// tell the next action to clear out the form
				log.debug("Newing up the form");
				ipForm = new InterpretPlantForm();
				request.setAttribute("formBean", ipForm);

				log.debug("Leaving InterpretPlantAction (save)");
				return mapping.findForward("edit");
			}
		
		} catch (Exception ex) {
			log.error("ERROR InterpretPlantAction: FAILURE: " + ex.toString());
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("edit");
		}


		// error
		log.error("Leaving InterpretPlantAction: no parameter set in struts-config!");
		return null;
	}

}
