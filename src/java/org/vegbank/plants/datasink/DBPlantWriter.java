/*
 *	'$RCSfile: DBPlantWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-04-22 18:52:09 $'
 *	'$Revision: 1.7 $'
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

package org.vegbank.plants.datasink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Iterator;

import org.vegbank.common.model.DBPartyWriter;
import org.vegbank.common.model.DBReferenceWriter;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Plant;
import org.vegbank.common.model.PlantUsage;
import org.vegbank.common.model.Reference;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class DBPlantWriter
{

	private Connection conn = null;
	private boolean commit = true;
	private boolean writeSuccess = false;

//	public DBPlantWriter(Plant plant)
//	{
//
//			this(plant, this.getConnection());
//	}
	
	public DBPlantWriter(Plant plant, Connection conn)
	{
		System.out.println("DBPlantWriter > inserting '" + plant.getScientificName() + "'");
		// This is going to know about the Data Model the database has 
		try
		{
			if (conn == null)
			{
				conn = this.getConnection();
			}
			this.conn = conn;
			conn.setAutoCommit(false);
	
	
			// Need to get the referenceId;
			Reference ref = plant.getScientificNameNoAuthorsReference();
			DBReferenceWriter  dbrw = 
				new DBReferenceWriter(ref, conn, "reference", "reference_id");
			int refId = dbrw.getReferenceId();
				
	
			// Need to get the partyId
			Party party = plant.getParty();
			DBPartyWriter dbpw =
				new DBPartyWriter(party, conn, "plantparty", "plantparty_id");
			int partyId = dbpw.getPartyId();
	
			// Insert the Scientific Name
			int plantNameId =
				this.insertPlantName(
					refId,
					plant.getScientificName(),
					Utility.dbAdapter.getDateTimeFunction() );
	
			// Insert Other Names ???
	
			// Insert the Concept
			int conceptId =
				this.insertPlantConcept(
					plantNameId,
					refId,
					plant.getScientificNameNoAuthors(),
					plant.getDescription(),
					plant.getCode());
	
			// Insert the Status
			int statusId = 
				this.insertPlantStatus(
					conceptId,
					refId,
					partyId,
					plant.getStatus(),
					plant.getParentName(),
					plant.getStatusStartDate(),
					plant.getClassLevel()
				);
			
				
			// Insert all the usages
			// Usage may need to be a separate class,  for now this
				
			AbstractList plantUsages = plant.getPlantUsages();
				
			Iterator i = plantUsages.iterator();
			while( i.hasNext()) 
			{
				PlantUsage pu = (PlantUsage) i.next();
				int usageId = 
					this.insertPlantUsage(
						plantNameId,
						conceptId,
						pu.getPlantName(),
						partyId,				
						plant.getStatusStartDate(),
						plant.getSynonymName(),
						pu.getPlantNameStatus(),
						pu.getClassSystem()
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
	private int insertPlantStatus(
		int plantConceptId, 
		int referenceId, 
		int plantPartyId, 
		String plantConceptStatus,
		String plantParentName,
		String startDate,
		String plantLevel) 
		throws SQLException
	{
		
		int plantParentConceptId = this.getPlantParentConceptId(plantParentName);
		
		int statusId =
			(int) Utility.dbAdapter.getNextUniqueID(
				conn,
				"PLANTSTATUS",
				"plantstatus_id");
						
		PreparedStatement pstmt =
					conn.prepareStatement(
			" insert into PLANTSTATUS (PLANTCONCEPT_ID, REFERENCE_ID,  "
		+ " PLANTPARTY_ID, PLANTCONCEPTSTATUS,  startdate, PLANTLEVEL, " 
		+ "plantstatus_id, plantparent_id, plantparentname)"
		+ " values (?,?,?,?,?,?,?,?,?)");
		
		pstmt.setInt(1, plantConceptId);
		pstmt.setInt(2, 	referenceId);
		pstmt.setInt(3, plantPartyId);
		pstmt.setString(4, plantConceptStatus);
		Utility.insertDateField(startDate, pstmt, 5);  
		pstmt.setString(6, plantLevel);		
		pstmt.setInt(7, statusId);		
		// 0 => that no plantconcept_id was found
		if (plantParentConceptId == 0)
		{
			pstmt.setNull(8, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setInt(8, plantParentConceptId);		
		}
		pstmt.setString(9, plantParentName);
				
		pstmt.executeUpdate();
		pstmt.close();
					
		return statusId;
	}

	private int insertPlantUsage(
		int plantNameId, 
		int plantConceptId, 
		String plantName,
		int plantPartyId, 
		String startDate,
		String synonymName,
		String plantNameStatus,
		String classSystem) 		
		throws SQLException
	{
		int usageId =
			(int) Utility.dbAdapter.getNextUniqueID(
				conn,
				"PLANTUSAGE",
				"plantusage_id");	
				
					
		PreparedStatement pstmt =
				conn.prepareStatement(
					" insert into PLANTUSAGE (PLANTNAME_ID, PLANTCONCEPT_ID, PLANTNAME, "
					+ "PLANTPARTY_ID,  USAGESTART, acceptedsynonym, plantnamestatus, "
					+ "classsystem, plantusage_id)  values (?,?,?,?,?,?,?,?,?)"
				);
				
		pstmt.setInt(1,plantNameId);
		pstmt.setInt(2,plantConceptId);		
		pstmt.setString(3, plantName);
		pstmt.setInt(4,plantPartyId);			
		Utility.insertDateField(startDate, pstmt, 5);  		
		pstmt.setString(6, synonymName);
		pstmt.setString(7, plantNameStatus);
		pstmt.setString(8,classSystem);
		pstmt.setInt(9,usageId);
				
		pstmt.executeUpdate();
		pstmt.close();
		
		return usageId;
	}

	/**
	* method that loads a new instance of a 
	* plant name and then returns the primary
	* key value of that plant name to the calling
	* method
	*
	* @param refId -- the reference Id
	* @param plantName -- the plantname (w/o the author bit)
	* @param dateEntered -- the data thate the plant name is to be entered
	* @return plantNameId -- the plant name Id assocaited with this plant
	*
	*/
	private int insertPlantName(
		int refId,
		String plantName,
		String dateEntered)
		throws SQLException
	{
		// Get Primary key for table		
		int plantNameId =
			(int) Utility.dbAdapter.getNextUniqueID(
				conn,
				"PLANTNAME",
				"plantname_id");

		PreparedStatement pstmt =
			conn.prepareStatement(
				"insert into PLANTNAME (reference_id, plantName,  dateEntered, "
					+ "plantname_id) values(?,?,?,?) ");

		//bind the values
		pstmt.setInt(1, refId);
		pstmt.setString(2, plantName);
		pstmt.setString(3, dateEntered);
		pstmt.setInt(4, plantNameId);
		pstmt.executeUpdate();
		pstmt.close();

		return (plantNameId);
	}

	/**
	 * method that will return a database connection for use with the database
	 *
	 * @return conn -- an active connection
	 */
	private Connection getConnection()
	{
		Utility u = new Utility();
		Connection c = u.getConnection("vegbank");
		return c;
	}
	
	private int insertPlantConcept(
		int plantNameId,
		int refId,
		String plantName,
		String plantDescription,
		String plantCode)
		throws SQLException
	{
		int nextId =
			(int) Utility.dbAdapter.getNextUniqueID(
				conn,
				"plantconcept",
				"plantconcept_id");

		PreparedStatement pstmt =
			conn.prepareStatement(
				" insert into PLANTCONCEPT (PLANTNAME_ID, REFERENCE_ID, PLANTNAME, "
					+ " PLANTDESCRIPTION,  PLANTCODE, plantconcept_id)   values (?,?,?,?,?,?)");

		pstmt.setInt(1, plantNameId);
		pstmt.setInt(2, refId);
		pstmt.setString(3, plantName);
		pstmt.setString(4, plantDescription);
		pstmt.setString(5, plantCode);
		pstmt.setInt(6, nextId);
		pstmt.execute();
		pstmt.close();

		return nextId;
	}

	private int getPlantParentConceptId(String plantName)
		throws SQLException
	{
		int plantConceptId = 0;
		
		// This is referring to a different plant
		PreparedStatement pstmt =
			conn.prepareStatement(
				" select plantconcept_id from plantconcept where plantname = '" + plantName +"'"
			);
		
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() )
		{
			plantConceptId = rs.getInt(1);
		}
		
		return plantConceptId;
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
