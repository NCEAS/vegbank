/*
 *	'$RCSfile: PlantQueryAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-12-05 22:45:31 $'
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.model.Plantusage;

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
		LogUtility.log(" In PlantQueryAction ");
		ActionErrors errors = new ActionErrors();

		DynaActionForm thisForm = (DynaActionForm) form;

		// Get the properties of the form
		String plantname = (String) thisForm.get("plantname");
		String nameType = (String) thisForm.get("nameType");
		String taxonLevel = (String) thisForm.get("taxonLevel");
		String nameExistsBeforeDate =
			(String) thisForm.get("nameExistsBeforeDate");
		String acordingToParty = (String) thisForm.get("accordingToParty");		
		
		
		// Get sci name, code and common name for this plant
		String sharedGetNameType = 
			" ( SELECT plantname.plantname FROM plantusage NATURAL JOIN plantname where plantusage.plantconcept_id = plantconcept.plantconcept_id AND plantusage.classsystem ="; 

		// TODO: Use Constants for the classsystem and the label to display
		String selectCodeName = sharedGetNameType + "'Code' ) AS Code"; 
		String selectCommonName = sharedGetNameType + "'English Common' ) AS \"English Common Name\""; 
		String selectSciName = sharedGetNameType + "'Scientific' ) AS \"Scientific Name\""; 
		
		// Query Construct
		StringBuffer query = new StringBuffer();
		query.append(
				" SELECT plantconcept.accessioncode, " + selectCodeName + "," + selectCommonName + "," + selectSciName
				+ " FROM "
				+ "  (plantconcept "
				+ "    JOIN "
				+ "     ( plantname JOIN plantusage ON plantname.plantname_id = plantusage.plantname_id ) " 
				+ "    ON plantusage.plantconcept_id = plantconcept.plantconcept_id "
				+ "  ) "
				+ "  JOIN plantstatus "
				+ "    ON plantstatus.plantconcept_id = plantconcept.plantconcept_id "
				+ " WHERE plantname.plantname like '" + plantname + "'");

		// AND classsytem is
		if ( ! Utility.isStringNullOrEmpty(nameType) )
			query.append(" AND plantusage.classsystem = '" + nameType + "'");

		// AND classsytem is
		if ( ! Utility.isStringNullOrEmpty(nameType) )
			query.append(" AND plantusage.classsystem = '" + nameType + "'");

		// AND taxonLevel is
		if ( ! Utility.isStringNullOrEmpty(taxonLevel) )
			query.append(" AND plantstatus.plantlevel = '" + taxonLevel + "'");


		// TODO: Wire up the Party and dates searches


    try
    {
      DatabaseAccess da = new DatabaseAccess();
      ResultSet rs = da.issueSelect( query.toString() );
      
      RowSetDynaClass rsdc = new RowSetDynaClass(rs);    
      request.setAttribute("PlantQueryResults", rsdc);
/*    	
			// DEBUGGING 
      while(rs.previous())
      {
				LogUtility.log(rs.getString(1) + rs.getString(2) + rs.getString(3) + rs.getString(4) );
				LogUtility.log("#############################################");
      }
*/
    }
    catch ( Exception e )
    {
      errors.add(
        ActionErrors.GLOBAL_ERROR,
        new ActionError("errors.database", e.getMessage() )
       );
       e.printStackTrace();
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
