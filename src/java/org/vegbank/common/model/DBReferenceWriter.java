/*
 *	'$RCSfile: DBReferenceWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-21 17:52:12 $'p
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
 
package org.vegbank.common.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author farrell
 */

public class DBReferenceWriter
{
	
	private Connection conn = null;
	boolean commit = true;
	public int referenceId = 0;
	private String pKName = null;
	private String tableName = null;
	 
	public DBReferenceWriter(
		Reference ref, 
		Connection conn,
		String tableName,	// quick hack to handle plants and communities
		String pKName
	)
	{
		this.conn = conn;
		this.pKName = pKName;
		this.tableName = tableName;
		
		try
		{

			if ( !this.referenceExists( ref.getTitle(), ref.getPubdate() ))
			{
				referenceId =
					this.insertReference(
						ref.getTitle(),
						ref.getPubdate(),
						ref.getEdition(),
						null,
						null,
						null,
						null,
						null);
			}
			else
			{
				referenceId = this.getReferenceId( ref.getTitle(), ref.getPubdate() );
			}

		}
		catch (SQLException e)
		{
			// If any step fails the transaction is void
			commit = false;
			System.out.println("Could not write this reference to the database");
			e.printStackTrace();
		}

		try
		{
			// decide on the transaction
			if (commit == true)
			{
				System.out.println("DBReferenceWriter > commiting transaction");
				conn.commit();
			}
			else
			{
				System.out.println("DBReferenceWriter >  not commiting transaction");
				conn.rollback();
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

		/** 
		 * Does the reference already exist?
		 */
		private boolean referenceExists(
			String title,
			String date)
		{
			boolean exists = false;
			StringBuffer sb = new StringBuffer();
			try
			{
				sb = new StringBuffer();
				sb.append("SELECT " + pKName + " from " + tableName + " where ");
				sb.append("  title = '" + title + "'");
				// IF THE DATE IS A VALID DATE THEN QUERY BY IT TOO
				if (date.length() > 4)
				{
					sb.append(" and PUBDATE = '" + date + "'");
				}
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				int cnt = 0;
				while (rs.next())
				{
					cnt++;
				}
				if (cnt > 0)
				{
					exists = true;
				}
				else
				{
					exists = false;
				}
			}
			catch (Exception e)
			{
				commit = false;
				System.out.println("DBReferenceWriter > Exception: " + e.getMessage());
				System.out.println("sql: " + sb.toString());
				e.printStackTrace();
			}
			return (exists);
		}
		
	/** 
	 * method that grabs and retuns the referenceId based on the authors' name and
	 * the otherCitationDetails
	 */
	private int getReferenceId(String title, String date)
	{
		StringBuffer sb = new StringBuffer();
		int refId = 0;
		try
		{
			sb.append("SELECT " + pKName + " from " + tableName + " where title");
			sb.append(" like '" + title + "'");
			// IF THE DATE IS VALID USE IT ELSE DONT
			if (date.length() > 4)
			{
				sb.append(" and pubdate = '" + date + "'");
			}
			//System.out.println("getPlantReferenceId sql: " + sb.toString() );
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			int cnt = 0;
			while (rs.next())
			{
				refId = rs.getInt(1);
				cnt++;
			}
		}
		catch (Exception e)
		{
			commit = false;
			System.out.println("DBReferenceWriter > Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString());
			e.printStackTrace();
		}
		return (refId);
	}
	
	/** 
	 * method that inserts the reference data into the 'reference' table, if it
	 * does not already exist and it it does it returns the referenece id
	 */
	private int insertReference(
		String title,
		String date,
		String edition,
		String seriesName,
		String page,
		String isbn,
		String issn,
		String conceptDescription)
		throws SQLException
	{
		StringBuffer sb = new StringBuffer();
		PreparedStatement pstmt;
		int refId = 0;
		
		//first see if the reference already exists
		boolean refExists = referenceExists(title, date);
		System.out.println("DBReferenceWriter > ref exists: " + refExists);
		if (refExists == true)
		{
			refId = getReferenceId(title, date);
		}
		else
		{
			// IF THE DATE IS VALID THEN USE ONE QUERY ELSE USE ANOTHER
			if (date.length() > 4)
			{
				sb.append("INSERT into " + tableName + " ( TITLE, PUBDATE) ");
				sb.append(" values(?,?)");
				pstmt = conn.prepareStatement(sb.toString());
				// Bind the values to the query and execute it
				pstmt.setString(1, title);
				pstmt.setString(2, date);
			}
			else
			{
				sb.append("INSERT into " + tableName + " (TITLE) ");
				sb.append(" values(?,?)");
				pstmt = conn.prepareStatement(sb.toString());
				// Bind the values to the query and execute it
				pstmt.setString(1, title);
			}
			pstmt.execute();

			//now get the reference id to return
			refId = getReferenceId(title, date);
			System.out.println("DBReferenceWriter > new refId: " + refId);
		}
		return (refId);
	}
	
	/**
	 * @return int
	 */
	public int getReferenceId()
	{
		return referenceId;
	}


}
