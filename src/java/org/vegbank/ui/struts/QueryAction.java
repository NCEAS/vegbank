/*
 * '$RCSfile: QueryAction.java,v $' 
 * 
 *  Authors: @author@ Release: @release@
 * 
 * '$Author: farrell $' 
 * '$Date: 2004-03-05 22:45:16 $' 
 * '$Revision: 1.1 $'
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
import org.vegbank.common.utility.LogUtility;
import org.vegbank.ui.struts.helpers.QueryHelper;


/**
 * @author farrell
 */
public class QueryAction
		extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		LogUtility.log(" In QueryAction " + LogUtility.DEBUG);
		ActionErrors errors = new ActionErrors();
		
		// Get SQL depend on queryType
		StringBuffer query = null;
		if ( form instanceof PlantQueryForm )
		{
			PlantQueryForm plantForm = (PlantQueryForm) form;
			query = QueryHelper.getPlantQuerySQL(plantForm); 
		}
		else if ( form instanceof CommQueryForm )
		{
			CommQueryForm commForm = (CommQueryForm) form;
			query = QueryHelper.getCommQuerySQL(commForm); 
		}
		else
		{
			errors.add(ActionErrors.GLOBAL_ERROR, 
						new ActionError("errors.action.failed", "Invalid Form type: " + form.getClass() ));
		}

		if ( query != null )
		{
			LogUtility.log("SQL: "+ query.toString(), LogUtility.INFO);
			try
			{
				DatabaseAccess da = new DatabaseAccess();
				ResultSet rs = da.issueSelect(query.toString());
				RowSetDynaClass rsdc = new RowSetDynaClass(rs);
				request.setAttribute("QueryResults", rsdc);
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
		}
		// Report any errors we have discovered back to the original form
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}
		return mapping.findForward("success");
	}
}
