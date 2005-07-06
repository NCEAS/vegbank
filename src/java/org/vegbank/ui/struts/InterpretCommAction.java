/*
 *	'$RCSfile: InterpretCommAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-07-06 01:05:19 $'
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
import org.vegbank.common.model.Commclass;
import org.vegbank.common.model.Comminterpretation;
import org.vegbank.common.model.Classcontributor;


/**
 * InterpretComm
 * 1: Present an empty form for interpretation
 * 2: Add a new commclass, one or more comminterpretation, display result page
 * 
 * @author anderson
 */
public class InterpretCommAction extends VegbankAction {

	private static Log log = LogFactory.getLog(InterpretCommAction.class); 
	private static final String CANCEL_URL = "/get/std/observation/";

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {


		String actionParam = mapping.getParameter();
		if (actionParam == null) {
			actionParam = "n/a";
		}

		log.debug("In action InterpretCommAction as " + actionParam);
		ActionErrors errors = new ActionErrors();

		String obsId = request.getParameter("obsId");
        if (Utility.isStringNullOrEmpty(obsId)) {
            log.error("Cannot proceed without obsId being passed in request");
            errors.add(Globals.ERROR_KEY, new ActionMessage(
                        "errors.general", 
                        "Request parameter 'obsId' is required."));
            saveErrors(request, errors);
            return mapping.findForward("edit");
        }

		long lObsId = Long.parseLong(obsId);
        DynaActionForm icForm = (DynaActionForm)form;

		// for validation,
		// store the obsId in the session
		if (Utility.isStringNullOrEmpty(obsId)) {
			log.debug("Getting obsId from session");
			obsId = (String)request.getSession().getAttribute("obsId");
		} else {
			log.debug("Putting obsId in session");
			request.getSession().setAttribute("obsId", obsId);
		}

        Commclass commclass = null;
        Classcontributor classcontrib = null;
        Comminterpretation[] comminterpArray = null;
        String[] commconcept_ac = null;

		try {
			if (isCancelled(request)) {
				log.debug("CANCELLING");
				return new ActionForward(CANCEL_URL + obsId, true);
			}

			log.debug("Interpreting obs " + obsId);

			// these come in handy
			request.setAttribute("obsId", obsId);
			//////icForm.setTaxonInterpretation(new Taxoninterpretation());
			request.setAttribute("formBean", icForm);



			////////////////////////////////////////////////////
			// ACTION
			////////////////////////////////////////////////////
			if (actionParam.equals("edit")) {

				log.debug("EDITING");

                commclass = new Commclass();
                commclass.setObservation_id(lObsId);
                comminterpArray = new Comminterpretation[1];
                comminterpArray[0] = new Comminterpretation();
                commconcept_ac = new String[1];
                commconcept_ac[0] = "";

                icForm.set("commclass", commclass);
                icForm.set("comminterp", comminterpArray);
                icForm.set("commconcept_ac", commconcept_ac);
                request.setAttribute("commclass", commclass);
                request.setAttribute("comminterp", comminterpArray);
                request.setAttribute("commconcept_ac", commconcept_ac);

				return mapping.findForward("edit");


			} else if (actionParam.equals("save")) {

				// get the taxonobservation_id
				log.debug("SAVING");

                commclass = (Commclass)icForm.get("commclass");
                classcontrib = new Classcontributor();
                comminterpArray = (Comminterpretation[])icForm.get("comminterp");

                // use to get commconcept_id
                commconcept_ac = (String[])icForm.get("commconcept_ac");



                // set up the commclass + comminterps
                if (commclass == null) {
                    // init the commclass
                    commclass.setObservation_id(lObsId);

                } else {

                    // add the comminterps
                    List comminterpList = new ArrayList();
                    if (comminterpArray.length == 0) {
                        for (int i=0; i< comminterpArray.length; i++) {
                            comminterpList.add((Comminterpretation)comminterpArray[i]);
                        }

                        commclass.setcommclass_comminterpretations(comminterpList);
                    }

                }

				ResultSet rs = null;
				DatabaseAccess da = new DatabaseAccess();
				
				// set the obsId (from the form) in the T-Int
				//////Taxoninterpretation tint = icForm.getTaxonInterpretation();
				//////tint.setTaxonobservation_id(Long.parseLong(icForm.getTobsId()));

				//
				// Get plantname_id
				//
				//////log.debug("checking for plantName: " + Utility.encodeForDB(icForm.getPlantName()));
				//rs = da.issueSelect(
				//		"SELECT plantname_id FROM plantname WHERE lower(plantname)='" +
				//			Utility.encodeForDB(icForm.getPlantName()) + "'");

				// can find plantName?
                /*
				if (rs.next()) {
					// use the extant plantName 
					log.debug("using extant plantName");
					tint.setPlantname_id(rs.getLong(1));

				} else {
					// plantName does not yet exist, so store it
					String ins = "INSERT INTO plantname (plantname,dateentered) VALUES ('" + 
							Utility.encodeForDB(icForm.getPlantName()) + "', now())";
					log.debug("adding new plantName: " + ins);
					da.issueUpdate(ins);
				}
                */
				
                /*
				//
				// Get plantconcept_id
				// 
				log.debug("getting plantconcept_id with: " + icForm.getPcAC());
				rs = da.issueSelect(
						"SELECT plantconcept_id FROM plantconcept WHERE lower(accessioncode)='" +
						icForm.getPcAC().toLowerCase() + "'");


				if (rs.next()) {
					log.debug("setting plantconcept_id");
					tint.setPlantconcept_id(rs.getLong(1));

				} else {
					log.error("Can't find plantconcept_id for given AC: " + icForm.getPcAC());
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
				tint.setInterpretationtype("Other");

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

				request.getSession().removeAttribute("tobsId");

				// tell the next action to clear out the form
				log.debug("Newing up the form");
				icForm = new InterpretCommForm();
				request.setAttribute("formBean", icForm);

				log.debug("Leaving InterpretCommAction (save)");
				return mapping.findForward("edit");
                */
			}
		
		} catch (Exception ex) {
			log.error("ERROR InterpretCommAction: FAILURE: " + ex.toString());
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("edit");
		}


		// error
		log.error("Leaving InterpretCommAction: no parameter set in struts-config!");
		return null;
	}

}
