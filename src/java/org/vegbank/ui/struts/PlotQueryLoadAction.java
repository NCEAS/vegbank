/*
 *	'$Id: PlotQueryLoadAction.java,v 1.2 2004-06-14 22:16:51 anderson Exp $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-06-14 22:16:51 $'
 *	'$Revision: 1.2 $'
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.ui.struts.PlotQueryForm;



/**
 * Prepopulates the plot query form with data hints and constraints.
 *
 * @author anderson
 */
public class PlotQueryLoadAction extends VegbankAction {

	private static Log log = LogFactory.getLog(PlotQueryLoadAction.class); 

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		log.debug("In action PlotQueryLoadAction");
		ActionErrors errors = new ActionErrors();

		// Get the form
		PlotQueryForm pqform = (PlotQueryForm)form;
		if (pqform == null) {
			log.debug("PlotQueryLoadAction: constructing new pqform");
			pqform = new PlotQueryForm();
		}


		try
		{
			log.debug("PlotQueryLoadAction: loading data hints & constraints");
			loadDataConstraints(pqform);
		}
		catch (SQLException e1)
		{
			log.error("PlotQueryLoadAction: ERROR: " +
				"loadDataConstraints() ERROR: " + e1.toString(), e1);
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.database", e1.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("failure"));
		}

		request.setAttribute("bean", pqform);

		log.debug("Leaving PlotQueryLoadAction");
		return mapping.findForward("plot_query");
	}

	/**
	 * Gets relevant plot data 
	 *
	 * @return void
	 */
	private void loadDataConstraints(PlotQueryForm pqform) 
			throws SQLException {
		StringBuffer plotQuery = new StringBuffer(1024)
				.append("SELECT MIN(elevation) as curMinElevation, ")
				.append("MAX(elevation) as curMaxElevation, ")
				.append("MIN(slopeAspect) as curMinSlopeAspect, ")
				.append("MAX(slopeAspect) as curMaxSlopeAspect, ")
				.append("MIN(slopeGradient) as curMinSlopeGradient, ")
				.append("MAX(slopeGradient) as curMaxSlopeGradient, ")
				.append("MIN(dateentered) as curMinDateEntered, ")
				.append("MAX(dateentered) as curMaxDateEntered, ")
				.append("MIN(area) as curMinArea, ")
				.append("MAX(area) as curMaxArea ")
				.append("FROM plot");
	
		StringBuffer obsQuery = new StringBuffer(512)
				.append("SELECT MIN(obsStartDate) as curMinObsStartDate, ")
				.append("MAX(obsEndDate) as curMaxObsEndDate ")
				.append("FROM observation");
	
		DatabaseAccess da = new DatabaseAccess();

		// hit the DB, plot
		ResultSet rs = da.issueSelect(plotQuery.toString());		

		if (rs.next())
		{
			pqform.setCurMinElevation(rs.getString(1));
			// Confirm that the value was not a NULL and do numerical processing
			if ( ! rs.wasNull())
				pqform.setCurMinElevation(Integer.toString( (int)Double.parseDouble(pqform.getCurMinElevation())));
	
			pqform.setCurMaxElevation(rs.getString(2));
			if ( ! rs.wasNull() )
				pqform.setCurMaxElevation(Integer.toString( (int)Math.ceil(Double.parseDouble(pqform.getCurMaxElevation()))));

			pqform.setCurMinSlopeAspect(rs.getString(3));
			pqform.setCurMaxSlopeAspect(rs.getString(4));
			pqform.setCurMinSlopeGradient(rs.getString(5));
			pqform.setCurMaxSlopeGradient(rs.getString(6));
			pqform.setCurMinDateEntered(rs.getString(7));
			pqform.setCurMaxDateEntered(rs.getString(8));

			pqform.setCurMinArea(rs.getString(9));
			if ( ! rs.wasNull() )
				pqform.setCurMinArea(Integer.toString( (int)Double.parseDouble(pqform.getCurMinArea())));
			
			pqform.setCurMaxArea(rs.getString(10));
			if ( ! rs.wasNull() )
				pqform.setCurMaxArea(Integer.toString( (int)Math.ceil(Double.parseDouble(pqform.getCurMaxArea()))));
		}

		// hit the DB, obs
		rs = da.issueSelect(obsQuery.toString());		

		if (rs.next())
		{
			pqform.setCurMinObsStartDate(rs.getString(1));
			pqform.setCurMaxObsEndDate(rs.getString(2));
		}
	}
}
