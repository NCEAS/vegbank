/*
 *	'$RCSfile: QueryParties.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2003-07-02 18:06:58 $'
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

package org.vegbank.common.command;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.model.*;
import org.vegbank.common.utility.DatabaseAccess;



/**
 * @author farrell
 */

public class QueryParties  implements VegbankCommand
{

	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		Collection collection = this.execute();
		request.setAttribute("parties", collection);
	}

	public Collection execute()
		throws Exception
	{
		// Query the database for parties
		//String[] fields = {"reference_id", "title", "shortname", "referencetype"};

		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect("select party_id, salutation, surname, givenname, middlename, organizationname, contactinstructions from party");

		Collection col = new Vector();
		while ( rs.next())
		{
			PartySummary partysum = new PartySummary(this);
			partysum.setId(rs.getString(1));
			partysum.setSalutation(rs.getString(2));
			partysum.setSurname( rs.getString(3));
			partysum.setGivenName( rs.getString(4));
			partysum.setMiddleName( rs.getString(5));
			partysum.setOrganizationName( rs.getString(6));
			partysum.setContactInstructions( rs.getString(7));
			col.add(partysum);
		}
		return col;
	}

}
