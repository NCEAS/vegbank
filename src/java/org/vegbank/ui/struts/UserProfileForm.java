/**	
 *  $ Id: $
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-03-01 01:05:48 $'
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

import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.common.model.WebUser;

public class UserProfileForm extends ValidatorForm 
			implements java.io.Serializable
{


	// prepopulated fields

	private String action;
	private WebUser webuser;


	/**
	 * Constructor
	 */
	public UserProfileForm() {
		super();
	}

	/**
	 * Get value of 'action' property.
	 */
	public String getAction() {
		return this.action;
	}

	/**
	 * Get value of 'webuser' property.
	 */
	public WebUser getWebuser() {
		return this.webuser;
	}

	/**
	 * Set the 'webuser' property.
	 */
	public void setWebuser(WebUser webuser) {
		this.webuser = webuser;
	}

	/**
	 * Set the 'action' property.
	 */
	public void setAction(String action) {
		this.action = action;
	}


}
