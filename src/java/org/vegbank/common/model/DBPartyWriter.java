/*
 *	'$RCSfile: DBPartyWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-05-02 11:11:05 $'
 *	'$Revision: 1.10 $'
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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class DBPartyWriter
{

 
	
		private DBConnection conn = null;
		boolean commit = true;
		public int partyId = 0;
		private String pKName = null;
		private String tableName = null;
	 
	 
		public DBPartyWriter(
			Party party, 
			DBConnection conn,
			String tableName,	// quick hack to handle plants and communities
			String pKName
		)
		{
			this.conn = conn;
			this.pKName = pKName;
			this.tableName = tableName;
		
			try
			{

				if ( !this.partyExists( party.getOrganizationname() ))
				{
					// FIXME: the ids bellow 
					partyId =
						this.insertParty(
							party.getGivenname(),
							party.getSurname(),
							party.getSalutation(),
							party.getMiddlename(),
							new Long(party.getCurrentname_id() ).toString(),
							party.getOrganizationname(),
							party.getContactinstructions());
				}
				else
				{
					partyId = this.getPartyId( party.getOrganizationname() );
				}

			}
			catch (SQLException e)
			{
				// If any step fails the transaction is void
				commit = false;
				System.out.println("Could not write this party to the database");
				e.printStackTrace();
			}

			try
			{
				// decide on the transaction
				if (commit == true)
				{
					System.out.println("DBPartyWriter > commiting transaction");
					conn.commit();
				}
				else
				{
					System.out.println("DBPartyWriter >  not commiting transaction");
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
			 * Does the party already exist?
			 */
			private boolean partyExists(String organization)
			{
				boolean exists = false;
				
				String query = 
					"select  " + pKName + " from " + tableName + " where " 
					+ "ORGANIZATIONNAME like '" + organization + "'";
				
				try
				{
					// get the party information 
					PreparedStatement pstmt = conn.prepareStatement(query);
					
					ResultSet rs = pstmt.executeQuery();
					// check to see that we got one row at least 
					if (rs.first() == true)
					{
						partyId = rs.getInt(1);
                        rs.close();
						exists = true;
					}	
				}
				catch (SQLException e)
				{
					commit = false;
					System.out.println("DBPartyWriter > Exception: " + e.getMessage());
					System.out.println("sql: " + query);
					e.printStackTrace();
				}		
				return (exists);
			}
		
		/** 
		 * method that grabs and retuns the partyId based on the organization name 
		 */
		private int getPartyId(String organization)
		{
			StringBuffer sb = new StringBuffer();
			int partyId = 0;
			try
			{
				sb.append("SELECT " + pKName + " from " + tableName + " where organizationName");
				sb.append(" like '" + organization + "'");
	
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				int cnt = 0;
				while (rs.next())
				{
					partyId = rs.getInt(1);
					cnt++;
				}
                rs.close();
			}
			catch (Exception e)
			{
				commit = false;
				System.out.println("DBPartyWriter > Exception: " + e.getMessage());
				System.out.println("sql: " + sb.toString());
				e.printStackTrace();
			}
			return (partyId);
		}
	
		/** 
		 * method that inserts the party data into the 'party' table, if it
		 * does not already exist and it it does it returns the party id
		 */
		private int insertParty(
			String givenName,
			String surName,
			String salutation,
			String middleName,
			String currentParty,
			String organization,
			String contactInstructions)
			throws SQLException
		{
			System.out.println("DBPartyWriter > attempting to insert party ");
			partyId =
				(int) Utility.dbAdapter.getNextUniqueID(
					conn,
					this.tableName,
					this.pKName);
					
			StringBuffer sb = new StringBuffer();
			PreparedStatement pstmt;

			sb.append("INSERT into " + tableName + " (givenName, surName, middleName, salutation,");
			sb.append(" currentName_id, organizationName, contactInstructions, owner_id, ");
			sb.append( pKName + ") values(?,?,?,?,?,?,?,?)");
			
			//System.out.println("====> '" + currentParty + "' ==== '" + owner + "'");
			
			pstmt = conn.prepareStatement(sb.toString());
			// Bind the values to the query and execute it
			pstmt.setString(1, givenName);
			pstmt.setString(2, surName);
			pstmt.setString(3, salutation);
			pstmt.setString(4, middleName);
			if ( currentParty != null && ! currentParty.trim().equals("0"))
			{
				pstmt.setString(5, currentParty);
			}
			else
			{
				pstmt.setNull(5, java.sql.Types.INTEGER);
			}
			pstmt.setString(6, organization);
			pstmt.setString(7, contactInstructions);
			pstmt.setInt(8,partyId);
			pstmt.execute();

			System.out.println("DBPartyWriter > new partyId: " + partyId);

			return (partyId);
		}
	
		/**
		 * @return int
		 */
		public int getPartyId()
		{
			return partyId;
		}

}

