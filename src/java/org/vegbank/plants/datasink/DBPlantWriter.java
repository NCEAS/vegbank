/*
 *	'$RCSfile: DBPlantWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-29 18:42:18 $'
 *	'$Revision: 1.9 $'
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
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Hashtable;
import java.util.Iterator;

import org.vegbank.common.Constants;
import org.vegbank.common.command.Query;
import org.vegbank.common.model.Plant;
import org.vegbank.common.model.PlantParty;
import org.vegbank.common.model.PlantUsage;
import org.vegbank.common.utility.ObjectToDB;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class DBPlantWriter implements Constants
{

	private Query query = new Query();
	private Connection conn = null;
	private boolean commit = true;
	private boolean writeSuccess = false;
	
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
	
	
			// Need to get the referenceIds;
			int conceptRefId = this.getIntFromString(plant.getConceptReferenceId());
			int snRefId =  this.getIntFromString(plant.getScientificNameReferenceId());
			int codeRefId =  this.getIntFromString(plant.getCodeNameReferenceId());
			int commonRefId =  this.getIntFromString(plant.getCommonNameReferenceId());
							
			// Need to get the partyId
			PlantParty party = plant.getPlantParty();
			int partyId;
			if (party == null)
			{
				String partyIdString = plant.getPlantPartyId();
				partyId = new Integer(partyIdString).intValue();
			}
			else
			{
				ObjectToDB pp2db = new ObjectToDB(party);
				pp2db.insert();
				partyId = pp2db.getPrimaryKey();
			}


			Hashtable plantNameIds = new Hashtable();
			AbstractList plantUsages = plant.getPlantUsages();
			
			Iterator i = plantUsages.iterator();
			while(i.hasNext())
			{
				PlantUsage pu = (PlantUsage) i.next();
				//System.out.println( " My Name >>> " + pu.getPlantName() + "and classsystem is " + pu.getClassSystem() );
				
				if (pu.getPlantName() == null || pu.getPlantName().trim().equals(""))
				{
					// No need to load this
				}
				else
				{
					// Get the correct ReferenceId
					int refId = 0;
					
					if (pu.getClassSystem().equals( USAGE_NAME_CODE) )
					{
						refId = codeRefId;
					}
					else if ( pu.getClassSystem().equals( USAGE_NAME_COMMON) )
					{
						refId = commonRefId;
					}
					else if ( pu.getClassSystem().equals( USAGE_NAME_SCIENTIFIC) )
					{
						refId = snRefId;
					}
					else
					{
						System.out.println("DBPlantWriter: Name classsystem not recoginized");
					}
					
					
					int plantNameId =
						this.insertPlantName(
							refId,
							pu.getPlantName(),
							Utility.dbAdapter.getDateTimeFunction() );
						
					plantNameIds.put(pu.getClassSystem(), new Integer(plantNameId) );
				}
			}
	
			//System.out.println("Get a PlantParty Id of " + partyId + " " + refId + " ---> " + plantNameIds);
			//System.out.println(">>>> Loaded Names and " + plantNameIds.get("Scientific") );
			
			int sciNameId = ( (Integer) plantNameIds.get("Scientific")).intValue(); 
			
			System.out.println("sciNameId: " + sciNameId);
			// Insert the Concept
			int conceptId =
				this.insertPlantConcept(
					sciNameId,
					conceptRefId,
					plant.getScientificName(),
					plant.getPlantDescription(),
					plant.getCode());
	
			//System.out.println(">>>> Loaded Concept");
			//System.out.println(">>>>  " + plant.getStatusStopDate());
			
			// Insert the Status
			int statusId = 
				this.insertPlantStatus(
					conceptId,
					conceptRefId,
					partyId,
					plant.getStatus(),
					plant.getParentName(),
					plant.getStatusStartDate(),
					plant.getStatusStopDate(),
					plant.getClassLevel(),
					plant.getStatusPartyComments()
				);
			
			//System.out.println(">>>>  " + plant.getStatusStopDate());
			//System.out.println("Got  StatusId:  " + statusId + "Got  ConceptId:  " + conceptId + "Got  ConceptId:  " + conceptId);
			
			// Insert all the usages
			Iterator it = plantUsages.iterator();
			while( it.hasNext()) 
			{
				PlantUsage pu = (PlantUsage) it.next();
				//System.out.println("'" + pu.getPlantName() + "'" + "  is a " + pu.getClassSystem());
				if ( pu.getPlantName().trim().equals("") || pu.getPlantName() == null )
				{
					//System.out.println("NOT LOADING");
				}
				else
				{
					//System.out.println("LOADING");
					int usageId = 
						this.insertPlantUsage(
							( (Integer) plantNameIds.get(pu.getClassSystem())).intValue(),
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
		}
		catch (Exception e)
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
	 * Wrapper for getting int from string,
	 * suppresses expections and returns a 0
	 * @return
	 */
	private int getIntFromString( String string )
	{
		int result = 0;
		try
		{
			result = new Integer(string).intValue();
		}
		catch ( Exception e)
		{
			// just return a 0
			result = 0;
		}
		return result;
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
		String stopDate,
		String plantLevel,
		String partyComments) 
		throws SQLException
	{
		int statusId = 0;
		int plantParentConceptId = 0;
		
		try
		{
			plantParentConceptId = query.getPlantConceptId(plantParentName);
		} 
		catch (Exception e)
		{
			System.out.println("Could not find parent: " + e.getMessage() );
			}	
			
		statusId =
			(int) Utility.dbAdapter.getNextUniqueID(
				conn,
				"PLANTSTATUS",
				"plantstatus_id");
						
		PreparedStatement pstmt =
					conn.prepareStatement(
			" insert into PLANTSTATUS (PLANTCONCEPT_ID, REFERENCE_ID,  "
		+ " PLANTPARTY_ID, PLANTCONCEPTSTATUS,  startdate, stopdate, PLANTLEVEL, " 
		+ "plantstatus_id, plantparent_id, plantparentname, plantpartycomments)"
		+ " values (?,?,?,?,?,?,?,?,?,?,?)");
		
		pstmt.setInt(1, plantConceptId);
		if ( referenceId == 0)
		{
			pstmt.setNull(2, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setInt(2, referenceId);
		} 
		pstmt.setInt(3, plantPartyId);
		pstmt.setString(4, plantConceptStatus);
		Utility.insertDateField(startDate, pstmt, 5);  
		Utility.insertDateField(stopDate, pstmt, 6);  
		pstmt.setString(7, plantLevel);		
		pstmt.setInt(8, statusId);		
		// 0 => that no plantconcept_id was found
		if (plantParentConceptId == 0)
		{
			pstmt.setNull(9, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setInt(9, plantParentConceptId);		
		}
		pstmt.setString(10, plantParentName);
		pstmt.setString(11, partyComments);
				
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
		if ( refId == 0)
		{
			pstmt.setNull(1, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setInt(1, refId);
		} 
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
		if ( refId == 0)
		{
			pstmt.setNull(2, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setInt(2, refId);
		} 
		pstmt.setString(3, plantName);
		pstmt.setString(4, plantDescription);
		pstmt.setString(5, plantCode);
		pstmt.setInt(6, nextId);
		pstmt.execute();
		pstmt.close();

		return nextId;
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
