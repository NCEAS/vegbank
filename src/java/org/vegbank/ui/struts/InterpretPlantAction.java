/*
 *	'$RCSfile: InterpretPlantAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-06-29 06:57:51 $'
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
	private static final String TOBS_LIST_URL =
			"/GenericDispatcherFwd.do?prop=taxonobservation_v_annotate_obs&param=";

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {


		String actionParam = mapping.getParameter();
		log.debug("In action InterpretPlantAction as " + actionParam);
		ActionErrors errors = new ActionErrors();
		InterpretPlantForm ipForm = (InterpretPlantForm)form; 


		try {
			if (isCancelled(request)) {
				log.debug("CANCELLING");
				return new ActionForward(TOBS_LIST_URL + 
						request.getParameter("observation_id"), true);
			}


			////////////////////////////////////////////////////
			// ACTION
			////////////////////////////////////////////////////
			if (actionParam.equals("edit")) {

				String tobsAC = request.getParameter("tobsAC");
				log.debug("Interpreting tobs " + tobsAC);

				/*
				// get the taxonObservation in question
				log.debug("getting all taxon interpretations for tobs=" + tobsAC);
				DBModelBeanReader mbReader = new DBModelBeanReader();
				Taxonobservation tobs = (Taxonobservation)mbReader.getVBModelBean(tobsAC);

				if (tobs == null) {
					log.error("Problem getting taxonObservation");
				}

				mbReader.releaseConnection();
				*/

				/*
				// get the plantConcept AC
				DatabaseAccess da = new DatabaseAccess();
				log.debug("Getting plantConcept AC");
				log.debug("plant code = " + tobs.getAuthorplantname());
				ResultSet rs = da.issueSelect(
						"SELECT accessioncode FROM plantconcept WHERE plantcode='" +
						tobs.getAuthorplantname() + "'");
				if (rs.next()) {
					ipForm.setPlantConceptAC(rs.getString(1));
				}
				*/

				/////ipForm.setTaxonObservation(tobs);
				ipForm.setTaxonInterpretation(new Taxoninterpretation());

				request.setAttribute("formBean", ipForm);
				request.setAttribute("tobsAC", tobsAC);

				log.debug("Leaving InterpretPlantAction (edit)");
				return mapping.findForward("edit");


			} else if (actionParam.equals("save")) {

				// save new taxonInterpretation record
				log.debug("inserting new taxoninterpretation");
				VBModelBeanToDB db = new VBModelBeanToDB();
				db.insert(ipForm.getTaxonInterpretation());
				
				// can find plantName?
				DatabaseAccess da = new DatabaseAccess();
				ResultSet rs = da.issueSelect("SELECT plantname_id FROM plantname WHERE lower(plantName)='" +
						ipForm.getPlantName().toLowerCase() + "'");
				if (!rs.next()) {
					// plantName does not yet exist, so store it
					/*
					da.issueInsert("INSERT plantname SET plant ");
						// maybe make a new Plantname bean instead?
					Plantname plantname = new Plantname();
					plantname.setPlantname(ipForm.getPlantName());
					log.debug("inserting new plantName");
					*/
				}

				log.debug("Leaving InterpretPlantAction (save)");
				return mapping.findForward("list");
			}
		
		} catch (Exception ex) {
			log.error("ERROR InterpretPlantAction: FAILURE: " + ex.toString());
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("failure");
		}

		// error
		log.error("Leaving InterpretPlantAction: no parameter set in struts-config!");
		return null;
	}

}
