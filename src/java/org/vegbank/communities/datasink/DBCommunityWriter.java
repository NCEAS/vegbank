/*
 *	'$RCSfile: DBCommunityWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-22 01:31:08 $'
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
 
package org.vegbank.communities.datasink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Iterator;

import org.vegbank.common.model.Community;
import org.vegbank.common.model.CommunityUsage;
import org.vegbank.common.model.DBPartyWriter;
import org.vegbank.common.model.DBReferenceWriter;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Reference;
import org.vegbank.common.utility.Utility;


/**
 * Responsible for writing a Community Object to the database.
 * 
 * @author farrell
 */

public class DBCommunityWriter
{

		private Connection conn = null;
		private boolean commit = true;
		private boolean writeSuccess = false;

	public DBCommunityWriter(Community comm, Connection conn)
	{
		System.out.println("DBCommunityWriter > inserting '" + comm.getName() + "'");
		// This is going to know about the Data Model the database has 
		try
		{
			this.conn = conn;
			conn.setAutoCommit(false);

			// Need to get the referenceId;
			Reference ref = comm.getNameReference();
			DBReferenceWriter  dbrw = 
				new DBReferenceWriter(ref, conn, "CommReference", "commreference_id");
			int commRefId = dbrw.getReferenceId();
			
			// Need to get the partyId
			Party party = comm.getParty();
			DBPartyWriter dbpw =
				new DBPartyWriter(party, conn, "commparty", "commparty_id");
			int partyId = dbpw.getPartyId();

			// Insert the  Name
			int commNameId =
				this.insertName(
					commRefId,
					comm.getName(),
					Utility.dbAdapter.getDateTimeFunction() );

			// Insert the Code
			int commCodeId =
				this.insertName(
					commRefId,
					comm.getCode(),
					Utility.dbAdapter.getDateTimeFunction() );
					
			// Insert the CommonName
			int commCommonId =
				this.insertName(
					commRefId,
					comm.getCommonName(),
					Utility.dbAdapter.getDateTimeFunction() );
			
			// Insert Other Names ???


			// Insert the Concept using the code as the FK to the name table
			int conceptId =
				this.insertCommConcept(
					commCodeId,
					commRefId,
					comm.getDescription());


			// Insert the Status
			int statusId = 
				this.insertStatus(
					conceptId,
					commRefId,
					partyId,
					comm.getStatus(),
					comm.getParentCode(),
					comm.getStatusStartDate(),
					comm.getClassLevel()
				);
		

	
			// Insert all the usages
			AbstractList commUsages = comm.getUsages();
			
			Iterator i = commUsages.iterator();
			while( i.hasNext()) 
			{
				CommunityUsage cu = (CommunityUsage) i.next();
				int usageId = 
					this.insertUsage(
						commNameId,
						conceptId,
						cu.getName(),
						partyId,				
						comm.getStatusStartDate(),
						cu.getNameStatus(),
						cu.getClassSystem()
				);
			
			}
		}
		catch (SQLException e)
		{
			// If any step fails the transaction is void
			commit = false;
			System.out.println("Could not write this plant to the database");
			e.printStackTrace();
		}

		try
		{
			// decide on the transaction
			if (commit == true)
			{
				System.out.println("DBPlantWriter > commiting transaction");
				conn.commit();
				writeSuccess = true;
			}
			else
			{
				System.out.println("DBPlantWriter >  not commiting transaction");
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
		 * @return int Primary Key
		 */
		private int insertStatus(
			int conceptId, 
			int referenceId, 
			int partyId, 
			String conceptStatus,
			String parentCode,
			String startDate,
			String level) 
			throws SQLException
		{
		
			int parentConceptId = this.getParentConceptId(parentCode);
		
			int statusId =
				(int) Utility.dbAdapter.getNextUniqueID(
					conn,
					"COMMSTATUS",
					"commstatus_id");
						
			PreparedStatement pstmt =
						conn.prepareStatement(
				" insert into COMMSTATUS (COMMCONCEPT_ID, COMMREFERENCE_ID,  "
			+ " COMMPARTY_ID, COMMCONCEPTSTATUS,  startdate, COMMLEVEL, " 
			+ "commstatus_id, commparent)"
			+ " values (?,?,?,?,?,?,?,?)");
		
			pstmt.setInt(1, conceptId);
			pstmt.setInt(2, 	referenceId);
			pstmt.setInt(3, partyId);
			pstmt.setString(4, conceptStatus);
			Utility.insertDateField(startDate, pstmt, 5);  
			pstmt.setString(6, level);		
			pstmt.setInt(7, statusId);		
			
			// 0 => that no plantconcept_id was found
			if (parentConceptId == 0)
			{
				pstmt.setNull(8, java.sql.Types.INTEGER);
			}
			else
			{
				pstmt.setInt(8, parentConceptId);		
			}
				
			pstmt.executeUpdate();
			pstmt.close();
					
			return statusId;
		}

		private int insertUsage(
			int nameId, 
			int conceptId, 
			String name,
			int partyId, 
			String startDate,
			String nameStatus,
			String classSystem) 		
			throws SQLException
		{
			int usageId =
				(int) Utility.dbAdapter.getNextUniqueID(
					conn,
					"COMMUSAGE",
					"commusage_id");	
				
					
			PreparedStatement pstmt =
					conn.prepareStatement(
						" insert into COMMUSAGE (COMMNAME_ID, COMMCONCEPT_ID, COMMNAME, "
						+ "COMMPARTY_ID,  USAGESTART,  commnamestatus, "
						+ "classsystem, commusage_id)  values (?,?,?,?,?,?,?,?)"
					);
				
			pstmt.setInt(1,nameId);
			pstmt.setInt(2,conceptId);		
			pstmt.setString(3, name);
			pstmt.setInt(4,partyId);			
			Utility.insertDateField(startDate, pstmt, 5);  		
			pstmt.setString(6, nameStatus);
			pstmt.setString(7,classSystem);
			pstmt.setInt(8,usageId);
				
			pstmt.executeUpdate();
			pstmt.close();
		
			return usageId;
		}

		/**
		* method that loads a new instance of a 
		* name and then returns the allocated primary
		* key value of that name to the calling
		* method
		*
		* @param refId -- the reference Id
		* @param name -- the name
		* @param dateEntered -- the data thate the plant name is to be entered
		* @return nameId -- the PK for this name
		*
		*/
		private int insertName(
			int refId,
			String name,
			String dateEntered)
			throws SQLException
		{
			int nameId = 0;
			
			if ( name == null || name.equals(""))
			{
				// Nothing to do here
			}
			else
			{
				// Get Primary key for table		
				nameId =
					(int) Utility.dbAdapter.getNextUniqueID(
						conn,
						"COMMNAME",
						"commname_id");
	
				PreparedStatement pstmt =
					conn.prepareStatement(
						"insert into COMMNAME (commreference_id, commName,  dateEntered, "
							+ "commname_id) values(?,?,?,?) ");
	
				//bind the values
				pstmt.setInt(1, refId);
				pstmt.setString(2, name);
				pstmt.setString(3, dateEntered);
				pstmt.setInt(4, nameId);
				pstmt.executeUpdate();
				pstmt.close();
			}
			return (nameId);
		}

		private int insertCommConcept(
			int nameId,
			int refId,
			String description)
			throws SQLException
		{
			int nextId =
				(int) Utility.dbAdapter.getNextUniqueID(
					conn,
					"commconcept",
					"commconcept_id");

			PreparedStatement pstmt =
				conn.prepareStatement(
					" insert into COMMCONCEPT (COMMNAME_ID, COMMREFERENCE_ID, "
						+ " CONCEPTDESCRIPTION, commconcept_id)   values (?,?,?,?)");

			pstmt.setInt(1, nameId);
			pstmt.setInt(2, refId);
			pstmt.setString(3, description);
			pstmt.setInt(4, nextId);
			pstmt.execute();
			pstmt.close();

			return nextId;
		}

		private int getParentConceptId(String code)
			throws SQLException
		{
			int conceptId = 0;
		
			// This is referring to a different plant
			PreparedStatement pstmt =
				conn.prepareStatement(
					" select commconcept_id from commconcept where commname_id = "
					+ "( select distinct commname_id from commname where commname = '" + code +"')" 
				);
		
			ResultSet rs = pstmt.executeQuery();
			while ( rs.next() )
			{
				conceptId = rs.getInt(1);
			}
		
			return conceptId;
		}
		/**
		 * @return boolean
		 */
		public boolean isWriteSuccess()
		{
			return writeSuccess;
		}

		/**
		 * Sets the writeSuccess.
		 * @param writeSuccess The writeSuccess to set
		 */
		public void setWriteSuccess(boolean writeSuccess)
		{
			this.writeSuccess = writeSuccess;
		}

	}

