/*
 *	'$RCSfile: QueryReferences.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 02:46:58 $'
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

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.utility.DatabaseAccess;

import databaseAccess.issueStatement;


/**
 * @author farrell
 */

public class QueryReferences  implements VegbankCommand
{
	
	

	/**
	 * @author farrell
	 *
	 * JavaBean to hold the reference summary.
	 */
	public class ReferenceSummary  implements Serializable
	{
		private String title;
		private String id;

		public ReferenceSummary()
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
		public String getTitle()
		{
			return title;
		}

		/**
		 * @param key
		 */
		public void setId(String key)
		{
			id = key;
		}

		/**
		 * @param string
		 */
		public void setTitle(String string)
		{
			title = string;
		}

	}
	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		// Query the database for references
		String[] fields = {"reference_id", "title"}; 
		
		Hashtable references = new Hashtable();
		issueStatement is = new issueStatement();
		is.issueSelect("select reference_id, title from reference", fields, references);
		
		return null;
	}
	
	public Collection execute()
		throws Exception
	{
		// Query the database for references
		String[] fields = {"reference_id", "title"}; 
		
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect("select reference_id, title from reference");
		
		Collection col = new Vector();
		while ( rs.next())
		{
			ReferenceSummary refsum = new ReferenceSummary();
			refsum.setId(rs.getString(1));
			refsum.setTitle(rs.getString(2));
			col.add(refsum);
		}
		return col;
	}

}
