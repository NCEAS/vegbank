/*
 *	'$RCSfile: QueryJournals.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-05-02 11:11:05 $'
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
 
package org.vegbank.common.command;

	
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.utility.DatabaseAccess;

/**
 * @author farrell
 */

public class QueryJournals  implements VegbankCommand
{	
	
	/**
	 * @author farrell
	 *
	 * JavaBean to hold the journal summary.
	 */
	public class JournalSummary
	{
		private String name;
		private String id;
		
		public JournalSummary()
		{
		}

		/**
		 * @return
		 */
		public String getId()
		{
			return id;
		}

		/**
		 * @return
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * @param string
		 */
		public void setId(String string)
		{
			id = string;
		}

		/**
		 * @param string
		 */
		public void setName(String string)
		{
			name = string;
		}

	}
	
	private static final String[] fields = {"referencejournal_id", "journal"};
	private static final String query = "select referencejournal_id, journal from referencejournal";
	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Collection execute(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		// Query the database for references
		Collection collection = this.execute();
		request.setAttribute("journals", collection);
		return collection;
	}

	public Collection execute()
		throws Exception
	{
		// Query the database for references
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect(query);
		
		Collection col = new Vector();
		while ( rs.next())
		{
			JournalSummary jorSum = new JournalSummary();
			jorSum.setId(rs.getString(1));
			jorSum.setName(rs.getString(2));
			col.add(jorSum);
		}
        da.closeStatement();
        rs.close();
		return col;
	}

}
