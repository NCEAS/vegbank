/*
 *	'$RCSfile: DropPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-10-17 01:23:58 $'
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DatabaseUtility;

/**
 * @author anderson
 */

public class DropPlotAction extends Action
{


	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		ActionErrors errors = new ActionErrors();

		// Get the form
		DynaActionForm dpform = (DynaActionForm) form;

		String fwd = "success";
		String strPlotIdList = (String)dpform.get("plotIdList");

		if (strPlotIdList == null || strPlotIdList.equals("") ) {
			fwd = "failure";
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.required", "At least one plot_id value"));
		} else {
			
			DatabaseUtility du = new DatabaseUtility(); 

			String[] arrPlotIdList = strPlotIdList.split("\\s"); 
			for (int i=0; i < arrPlotIdList.length; i++) {
				if (arrPlotIdList[i] != null && !arrPlotIdList[i].equals("")) {
					try {
						System.out.println("Deleting plot_id: " + arrPlotIdList[i]);
						du.dropPlot(Integer.parseInt(arrPlotIdList[i]), "localhost");
					} catch (SQLException ex) {
						System.out.println("DropPlotAction problem while deleting plot_id: " + 
								arrPlotIdList[i] + ", " + ex.getMessage());
						errors.add(Globals.ERROR_KEY, new ActionMessage(
									"errors.database", ex.getMessage(),"<delete plot by ID>"));
						fwd = "failure";
						break;
					}
				}
			}
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return mapping.findForward(fwd);
	}

}
