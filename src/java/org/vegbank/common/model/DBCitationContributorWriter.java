/*
 *	'$RCSfile: DBCitationContributorWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 19:34:15 $'
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
 
package org.vegbank.common.model;

import java.sql.Connection;

/**
 * @author farrell
 */

public class DBCitationContributorWriter
{
	
	private Connection conn;
	private String pKName;
	private String tableName;
		
	public DBCitationContributorWriter(
		CitationContributor cc, 
		Connection conn,
		String tableName,	// quick hack to handle plants and communities
		String pKName
	)
	{
		this.conn = conn;
		this.pKName = pKName;
		this.tableName = tableName;
		
		this.insertCitationContributor();
		this.insertCitationParty();
		
	}
	/**
	 * 
	 */
	private void insertCitationParty()
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	private void insertCitationContributor()
	{
		// TODO Auto-generated method stub
		
	}

}
