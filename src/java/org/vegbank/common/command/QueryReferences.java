/*
 *	'$RCSfile: QueryReferences.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-06-30 20:02:59 $'
 *	'$Revision: 1.3 $'
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

public class QueryReferences  implements VegbankCommand
{
	
	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		Collection collection = this.execute();
		request.setAttribute("references", collection);
	}
	
	public Collection execute()
		throws Exception
	{
		// Query the database for references
		//String[] fields = {"reference_id", "title", "shortname", "referencetype"}; 
		
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect("select reference_id, title, shortname, referencetype from reference");
		
		Collection col = new Vector();
		while ( rs.next())
		{
			ReferenceSummary refsum = new ReferenceSummary(this);
			refsum.setId(rs.getString(1));
			refsum.setTitle(rs.getString(2));
			refsum.setShortname( rs.getString(3));
			refsum.setReferenceType( rs.getString(4));
			col.add(refsum);
		}
		return col;
	}

}
