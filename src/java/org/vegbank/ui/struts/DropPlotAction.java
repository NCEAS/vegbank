/*
 *	'$RCSfile: DropPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-06-16 19:37:08 $'
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

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
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
    VegbankActionMapping vbMapping = (VegbankActionMapping)mapping;

    String fwd = "success";
		
		// param specifies which element (pwd, user)
		String param = vbMapping.getParameter();
		if (param == null || param.equals("")) { param = "user"; }

		// find the action in the mapping, then request
		String action = vbMapping.getAction();
    
		ActionErrors errors = new ActionErrors();

    if(action.equals("confirm"))
    { //dont actually delete, just ask the user if that's what they really
      //want to do.
      String plotId = (String)request.getParameter("plotIdList");
      //request.setAttribute("plotpk", "plotId")
      request.setAttribute("plotIdList", plotId); 
      return mapping.findForward("success");
    }
    else
    { //delete the plot
      // Get the form
      DynaActionForm dpform = (DynaActionForm) form;
  
      String strPlotIdList = (String)dpform.get("plotIdList");
  
      if (strPlotIdList == null || strPlotIdList.equals("") ) {
        fwd = "failure";
        errors.add(Globals.ERROR_KEY, new ActionMessage(
              "errors.required", "At least one plot_id value"));
      } else {
        
        DatabaseUtility du = new DatabaseUtility(); 
  
        //String[] arrPlotIdList = strPlotIdList.split("\\s"); 
        try {
          du.dropPlots( strPlotIdList.split("\\s") );
        } catch (SQLException ex) {
          System.out.println("DropPlotAction problem while deleting plots: " + 
            ex.getMessage());
          errors.add(Globals.ERROR_KEY, new ActionMessage(
                "errors.database", ex.getMessage()," "));
          fwd = "failure";
        }
      }
  
      if (!errors.isEmpty()) {
        saveErrors(request, errors);
      }
    }

		return mapping.findForward(fwd);
	}

}
