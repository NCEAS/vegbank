/* *	'$RCSfile: EntityNameForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-10-06 20:50:23 $'
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

import java.util.*;
import java.sql.*;

import org.apache.struts.action.ActionForm;
import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.model.Covermethod;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Project;
import org.vegbank.common.model.Stratummethod;
import org.vegbank.common.utility.DatabaseAccess;

/**
 * @author anderson
 */

public class EntityNameForm extends ActionForm implements java.io.Serializable
{
	private String searchText;
	private String entityType;

	/**
	 * Constructor
	 */
	public EntityNameForm()
	{
		super();
		System.out.println("constructing EntityNameForm");
	}
	
	/**
	 * @return
	 */
	public String getSearchText()
	{
		System.out.println("Getting searchText: " + searchText);
		return searchText;
	}

	/**
	 * @param string
	 */
	public void setSearchText(String searchText)
	{
		System.out.println("Setting searchText to " + searchText);
		this.searchText = searchText;
	}

	/**
	 * @return
	 */
	public String getEntityType()
	{
		System.out.println("Getting entityType: " + entityType);
		return entityType;
	}

	/**
	 * @param string
	 */
	public void setEntityType(String entityType)
	{
		System.out.println("Setting entityType to " + entityType);
		this.entityType = entityType;
	}

}
