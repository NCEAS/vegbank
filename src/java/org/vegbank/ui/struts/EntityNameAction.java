/*
 *	'$RCSfile: EntityNameAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-10-30 19:46:22 $'
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;

/**
 * @author anderson
 */

public class EntityNameAction extends Action
{


	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In action EntityNameAction");
		ActionErrors errors = new ActionErrors();
		DoubleMetaphone phone;
		String priMPC, secMPC;

		// Get the form
		EntityNameForm enForm = (EntityNameForm) form;


		String searchText = enForm.getSearchText();
		if (searchText != null) {

			// get the search text's primary and alternate metaphonetic codings
			phone = new DoubleMetaphone();
			priMPC = phone.doubleMetaphone(searchText);
			secMPC = phone.doubleMetaphone(searchText, true);

			Collection searchResults = new Vector();
			searchResults.add(priMPC);
			if (!priMPC.equals(secMPC)) {
				searchResults.add(secMPC);
			}
			request.setAttribute("searchResults", searchResults);
		}

		// Use parameterized queries
		// (and not StringBuffer)
		StringBuffer query = new StringBuffer(1024);

		/*
		try
		{
			//Vector resultSets = new Vector();
			//DatabaseAccess da = new DatabaseAccess();
			
			// Get all resultsets
			//da.issueSelect(query.toString());
			
		}
		catch (SQLException e1)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.database", e1.getMessage()));
			System.out.println(query.toString());
		}

		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}
		*/

		System.out.println("Leaving EntityNameAction");
		return mapping.findForward("EntityName");
	}

	/**
	 * @author anderson
	 *
	 * JavaBean to hold the search results.
	 */
	public class SearchResult
	{
		private String result;

		public SearchResult()
		{
		}

		/**
		 * @return
		 */
		public String getResult()
		{
			return result;
		}

		/**
		 * @param string
		 */
		public void setResult(String string)
		{
			result = string;
		}

	}
}
