/*
 * '$RCSfile: PlantQueryAction.java,v $' Authors: @author@ Release: @release@
 * 
 * '$Author: farrell $' '$Date: 2004-02-27 21:39:57 $' '$Revision: 1.3 $'
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.vegbank.ui.struts;

import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;


/**
 * @author farrell
 */
public class PlantQueryAction
		extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		LogUtility.log(" In PlantQueryAction ");
		ActionErrors errors = new ActionErrors();
		PlantQueryForm thisForm = (PlantQueryForm) form;

		// Get the properties of the form
		String plantname = thisForm.getPlantname();
		String nameType = thisForm.getNameType();
		String[] taxonLevel = thisForm.getTaxonLevel();
		String nameExistsBeforeDate = thisForm.getNameExistsBeforeDate();
		String acordingToParty = thisForm.getAccordingToParty();

		// Get sci name, code and common name for this plant
		String sharedGetNameType = " ( SELECT plantname.plantname FROM plantusage NATURAL JOIN plantname where plantusage.plantconcept_id = plantconcept.plantconcept_id AND plantusage.classsystem =";
		String selectCodeName = sharedGetNameType + "'Code' ) AS Code";
		String selectCommonName = sharedGetNameType
				+ "'English Common' ) AS \"English Common Name\"";
		String selectSciName = sharedGetNameType
				+ "'Scientific' ) AS \"Scientific Name\"";

		// Query Construct
		StringBuffer query = new StringBuffer();
		query
				.append(" SELECT plantconcept.accessioncode, "
						+ selectCodeName
						+ ","
						+ selectCommonName
						+ ","
						+ selectSciName
						+ " FROM "
						+ "  (plantconcept "
						+ "    JOIN "
						+ "     ( plantname JOIN plantusage ON plantname.plantname_id = plantusage.plantname_id ) "
						+ "    ON plantusage.plantconcept_id = plantconcept.plantconcept_id "
						+ "  ) "
						+ "  JOIN plantstatus "
						+ "    ON plantstatus.plantconcept_id = plantconcept.plantconcept_id "
						+ " WHERE UPPER(plantname.plantname) like '" + plantname.toUpperCase() + "'");

		// AND classsytem is
		if (!Utility.isStringNullOrEmpty(nameType))
			query.append(" AND plantusage.classsystem = '" + nameType + "'");

		// AND taxonLevel is
		query.append(DatabaseUtility.handleValueList(taxonLevel,
				"plantstatus.plantlevel", " AND "));

		// AND date is
		if (!Utility.isStringNullOrEmpty(nameExistsBeforeDate))
		{
			query.append(isDateInRange(nameExistsBeforeDate));
		} else
		{
			nameExistsBeforeDate = Utility.dbAdapter.getDateTimeFunction();
			query.append(isDateInRange(nameExistsBeforeDate));
		}

		// Wire up the Party
		if (!Utility.isStringNullOrEmpty(acordingToParty))
			query.append(" AND plantstatus.party_id = " + acordingToParty);

		try
		{
			DatabaseAccess da = new DatabaseAccess();
			ResultSet rs = da.issueSelect(query.toString());
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			request.setAttribute("PlantQueryResults", rsdc);
			/*
			 * // DEBUGGING while(rs.previous()) { LogUtility.log(rs.getString(1) +
			 * rs.getString(2) + rs.getString(3) + rs.getString(4) );
			 * LogUtility.log("#############################################"); }
			 */
		} catch (Exception e)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.database",
					e.getMessage(), query.toString()));
			LogUtility.log(e.getMessage(), e);
		}
		// Report any errors we have discovered back to the original form
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}
		return mapping.findForward("success");
	}

	/**
	 * @param nameExistsBeforeDate
	 * @return
	 */
	private String isDateInRange(String nameExistsBeforeDate)
	{
		return "AND '" + nameExistsBeforeDate + "' >= plantstatus.startdate AND "
				+ "( plantstatus.stopdate <= '" + nameExistsBeforeDate
				+ "' OR plantstatus.stopdate IS NULL)";
	}
}
