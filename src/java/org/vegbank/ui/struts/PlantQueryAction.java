/*
 *	'$RCSfile: PlantQueryAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-06-30 20:08:17 $'
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vegbank.common.utility.DatabaseAccess;

/**
 * @author farrell
 */

public class PlantQueryAction extends Action
{


	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In action PlantAction");
		ActionErrors errors = new ActionErrors();

		StringBuffer query = new StringBuffer();
		query.append(
			" SELECT plantname, plantname_id "
				+ " FROM plantname"
				+ " WHERE plantname like "); 
	
    // Handle the request 
    String plantName = request.getParameter("plantName");
    query.append("'" + plantName + "'");

    try
    {
      DatabaseAccess da = new DatabaseAccess();
      ResultSet rs = da.issueSelect( query.toString() );
    
      Collection col = new Vector();
      while(rs.next())
      {
        PlantsSummary ps = new PlantsSummary();
        
        col.add(ps);
      }
      request.setAttribute("PlantSummary", col);
    }
    catch ( SQLException e )
    {
      errors.add(
        ActionErrors.GLOBAL_ERROR,
        new ActionError("errors.database", e.getMessage() )
       );
       e.printStackTrace();
    }
    return null;
  }

  public class PlantsSummary
  {
    private String plantName;
    private String plantNameId;
  }
}
