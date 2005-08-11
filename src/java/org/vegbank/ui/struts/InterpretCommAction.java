/*
 *	'$RCSfile: InterpretCommAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-11 01:30:05 $'
 *	'$Revision: 1.4 $'
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
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.ui.struts.VegbankAction;
import org.vegbank.plots.datasource.DBModelBeanReader;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.utility.AccessionCode;
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

		log.debug("In action InterpretCommAction as " + actionParam);
		ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();

		String obsId = request.getParameter("obsId");
        if (Utility.isStringNullOrEmpty(obsId)) {
            log.error("No obsId param found in request");
            prepareError("Request parameter 'obsId' is required.", errors, request);
            return mapping.findForward("edit");
        }

		long lObsId = Long.parseLong(obsId);
        //DynaActionForm icForm = (DynaActionForm)form;
        DynaBean icForm = (DynaBean)form;

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

/*
                commclass = new Commclass();
                commclass.setObservation_id(lObsId);
                comminterpArray = new Comminterpretation[1];
                comminterpArray[0] = new Comminterpretation();
                commconcept_ac = new String[1];
                commconcept_ac[0] = "";

                icForm.set("commclass", commclass);
                //icForm.set("comminterp", comminterpArray);
                icForm.set("commconcept_ac", commconcept_ac);
                request.setAttribute("commclass", commclass);
                //request.setAttribute("comminterp", comminterpArray);
                request.setAttribute("commconcept_ac", commconcept_ac);
*/

				return mapping.findForward("edit");


			} else if (actionParam.equals("save")) {

				// get the taxonobservation_id
				log.debug("SAVING");

                //
                // STRATEGY:
                // Populate classcontributor
                // Populate comminterp list
                // Populate commclass
                // Insert commclass bean
                //

                // Initialize variables
				ResultSet rs = null;
				DatabaseAccess da = new DatabaseAccess();
                commclass = (Commclass)icForm.get("commclass");
                classcontrib = new Classcontributor();
                comminterpArray = (Comminterpretation[])icForm.get("comminterp");
                commconcept_ac = (String[])icForm.get("commconcept_ac");
                Long commconcept_id = null;


                if (commclass == null) {
					log.error("No commclass object found in post");
                    prepareError("No commclass object found in post", errors, request);
					return mapping.findForward("vberror");
                }


                //
                // COMMINTERPRETATIONS
                //
                log.debug("Handling comm. interpretations: " + comminterpArray.length );
                List comminterpList = new ArrayList();
                if (comminterpArray.length == 0) {
                    //error
					log.error("No comm interps entered");
                    prepareError("Please choose at least one community for your interpretation of this plot.", errors, request);
					return mapping.findForward("vberror");

                } else {
                    for (int i=0; i< comminterpArray.length; i++) {
                        Comminterpretation comminterp = (Comminterpretation)comminterpArray[i];
                        comminterp.setComminterpretation_id(-1);

                        // set commconcept_id
                        AccessionCode ac = new AccessionCode(commconcept_ac[i]);
                        if (Utility.isStringNullOrEmpty(ac.toString())) {
                            log.error("ERROR: Bad comm. concept accession code given: " + commconcept_ac[i]);
                            prepareError("Bad comm. concept accession code given: " + commconcept_ac[i], errors, request);
                            return mapping.findForward("vberror");
                        }

                        commconcept_id = ac.getEntityId();
                        log.debug("adding commconcept_id #" + commconcept_id);
                        comminterp.setCommconcept_id(commconcept_id.longValue());

                        // get the commname
                        try {
                            rs = da.issueSelect("SELECT commname FROM commconcept WHERE commconcept_id='" + commconcept_id.toString() + "'");
                            if (rs.next()) {
                                comminterp.setCommname(rs.getString(1));
                            } else {
                                log.error("Problem finding commconcept.commname for commconcept_id: " + commconcept_id.toString());
                            }
                            rs.close();
                        } catch (Exception ex) {
                            log.error("Problem finding commconcept.commname for commconcept_id: " +
                                    commconcept_id.toString() + ", " + ex.toString());
                        }

                        // embargo
                        comminterp.setEmb_comminterpretation("0");

                        // add comminterp to list
                        comminterpList.add(comminterp);
                        log.debug("COMMINTERP: \n" + comminterp.toXML());
                    }
                    // save all comminterps to commclass
                    commclass.setcommclass_comminterpretations(comminterpList);
                    log.debug("added " + comminterpList.size() + " comm. interps");
                }


                //
                // CLASSCONTRIBUTORS
                //

				// Set party
				classcontrib.setParty_id(new Long(getUser(request.getSession()).getPartyid()).longValue());
				
				// Set role
				rs = da.issueSelect("SELECT role_id FROM aux_role WHERE LOWER(rolecode)='classifier'");
				if (rs.next()) {
					classcontrib.setRole_id(rs.getLong(1));
				} else {
					log.error("Can't find role_id for rolecode: 'Classifier'");
				    rs.close();
                    prepareError("Bad rolecode, 'not specified'", errors, request);
					return mapping.findForward("vberror");
				}
                rs.close();

                // Set embargo
                classcontrib.setEmb_classcontributor("0");

                log.debug("adding one Classcontributor");
                ArrayList classcontributorList = new ArrayList();
                classcontributorList.add(classcontrib);
                commclass.setcommclass_classcontributors(classcontributorList);




				// 
				// COMMCLASS
				//
                commclass.setObservation_id(lObsId);
				commclass.setClassstartdate("now()");
                commclass.setEmb_commclass("0");

                // TODO: set commclass.accessioncode
				
				// save new commclass record
				log.debug("inserting new commclass");
				VBModelBeanToDB bean2db = new VBModelBeanToDB();
				long newId = bean2db.insert(commclass);
                log.debug("Done inserting new commclass #" + newId);
				

				messages.add("saved", new ActionMessage(
							"errors.general", 
							"Thank you.  Your interpretation has been saved."));
				saveMessages(request, messages);

				request.getSession().removeAttribute("obsId");

				// tell the next action to clear out the form
				//log.debug("Newing up the form");
				//icForm = new InterpretCommForm();
				//request.setAttribute("formBean", icForm);

				log.debug("Leaving InterpretCommAction (save)");
				return mapping.findForward("edit");
			}
		
		} catch (Exception ex) {
			log.error("ERROR InterpretCommAction: FAILURE", ex);
            prepareError(ex.toString(), errors, request);
			return mapping.findForward("vberror");
		}


		// error
		log.error("Leaving InterpretCommAction: no parameter set in struts-config!");
		return null;
	}

}
