/*
 *	'$RCSfile: DBPlantWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-25 19:40:56 $'
 *	'$Revision: 1.14 $'
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Hashtable;
import java.util.Iterator;

import org.vegbank.common.Constants;
import org.vegbank.common.command.Query;
import org.vegbank.common.model.Plant;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Plantusage;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class DBPlantWriter implements Constants
{

	private Query query = new Query();
	private DBConnection conn = null;
	private boolean commit = true;
	private boolean writeSuccess = false;
	
	public DBPlantWriter(Plant plant, DBConnection conn)
	{
		if ( plant.getScientificName() != null )
		{
			System.out.println("DBPlantWriter > inserting '" + plant.getScientificName() + "'");
		}
		else if ( plant.getScientificNameNoAuthors() != null )
		{
			System.out.println("DBPlantWriter > inserting '" + plant.getScientificNameNoAuthors() + "'");
		}
		else 
		{
			System.out.println("Cannot insert this plant because name cannot be found");
			return;
		}
		
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
			long conceptRefId = this.getLongFromString(plant.getConceptReferenceId());
			long  snRefId =  this.getLongFromString(plant.getScientificNameReferenceId());
			long  codeRefId =  this.getLongFromString(plant.getCodeNameReferenceId());
			long  commonRefId =  this.getLongFromString(plant.getCommonNameReferenceId());
			long  statusRefId =  this.getLongFromString(plant.getStatusReferenceId());
			long  snNoAutRefId =  this.getLongFromString(plant.getScientificNameNoAuthorsReferenceId());
										
			// Need to get the partyId
			Party party = plant.getParty();
			long partyId;
			if (party == null)
			{
				String partyIdString = plant.getPartyId();
				partyId = new Integer(partyIdString).intValue();
			}
			else
			{
				VBModelBeanToDB pp2db = new VBModelBeanToDB();
				partyId =pp2db.insert(party);
			}


			Hashtable plantNameIds = new Hashtable();
			AbstractList plantUsages = plant.getPlantUsages();
			
			Iterator i = plantUsages.iterator();
			while(i.hasNext())
			{
				Plantusage pu = (Plantusage) i.next();
				//System.out.println( " My Name >>> " + pu.getPlantName() + "and classsystem is " + pu.getClassSystem() );
				
				if (pu.getPlantname() == null || pu.getPlantname().trim().equals(""))
				{
					// No need to load this
				}
				else
				{
					// Get the correct ReferenceId
					long  refId = 0;
					
					if (pu.getClasssystem().equals( USAGE_NAME_CODE) )
					{
						refId = codeRefId;
					}
					else if ( pu.getClasssystem().equals( USAGE_NAME_COMMON) )
					{
						refId = commonRefId;
					}
					else if ( pu.getClasssystem().equals( USAGE_NAME_SCIENTIFIC) )
					{
						refId = snRefId;
					}
					else if ( pu.getClasssystem().equals( USAGE_NAME_SCIENTIFIC_NOAUTHORS) )
					{
						refId = snNoAutRefId;
					}
					else
					{
						System.out.println("DBPlantWriter: Name classsystem not recoginized");
					}
					
					// Does it have this kind  of  name
					if ( pu.getPlantname() != null )
					{
						long  plantNameId =
							this.insertPlantName(
								refId,
								pu.getPlantname(),
								Utility.dbAdapter.getDateTimeFunction() );
						
						System.out.println("===" + pu.getClasssystem() + " AND " +plantNameId);
						plantNameIds.put(pu.getClasssystem(), new Long(plantNameId) );
					}
				}
			}
	
			//System.out.println("Get a PlantParty Id of " + partyId + " " + refId + " ---> " + plantNameIds);
			//System.out.println(">>>> Loaded Names and " + plantNameIds.get("Scientific") );
			
			// The Scienticfic name is prefered for linking but scientic name without authors 
			// can also be used	
			Long sciNameId = (Long) plantNameIds.get(USAGE_NAME_SCIENTIFIC);
			Long sciNameWAId = (Long) plantNameIds.get(USAGE_NAME_SCIENTIFIC_NOAUTHORS);
			
			long  conceptId = 0;
			
			System.out.println( ">>>> " +sciNameId + " **** " + sciNameWAId + " " +plantNameIds);
			if ( sciNameId == null || sciNameId.equals("") )
			{
				if ( sciNameWAId != null || !sciNameWAId.equals("") )
				{
					long  longSciNameWAId =  sciNameWAId.longValue();
				
					System.out.println("sciNameId: " + sciNameId);
					// Insert the Concept
					conceptId =
						this.insertPlantConcept(
							longSciNameWAId,
							conceptRefId,
							plant.getScientificNameNoAuthors(),
							plant.getPlantDescription(),
							plant.getCode());
				}
				else
				{
					System.out.println("Cannot find a nameId to use");
				}
			}
			else
			{
				// Use the scientific Name Id
				long longSciNameId =  sciNameId.longValue(); 
			
				System.out.println("sciNameId: " + sciNameId);
				// Insert the Concept
				conceptId =
					this.insertPlantConcept(
						longSciNameId,
						conceptRefId,
						plant.getScientificName(),
						plant.getPlantDescription(),
						plant.getCode());
			}

	
			//System.out.println(">>>> Loaded Concept");
			//System.out.println(">>>>  " + plant.getStatusStopDate());
			
			// Insert the Status
			long statusId = 
				this.insertPlantStatus(
					conceptId,
					statusRefId,
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
				Plantusage pu = (Plantusage) it.next();
				//System.out.println("'" + pu.getPlantName() + "'" + "  is a " + pu.getClassSystem());
				if ( pu.getPlantname().trim().equals("") || pu.getPlantname() == null )
				{
					//System.out.println("NOT LOADING");
				}
				else
				{
					//System.out.println("LOADING");
					long usageId = 
						this.insertPlantUsage(
							( (Long) plantNameIds.get(pu.getClasssystem())).longValue(),
							conceptId,
							pu.getPlantname(),
							partyId,				
							plant.getStatusStartDate(),
							plant.getSynonymName(),
							pu.getPlantnamestatus(),
							pu.getClasssystem()
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
	 * Wrapper for getting long  from string,
	 * suppresses expections and returns a 0
	 * @return
	 */
	private long getLongFromString( String string )
	{
		long result = 0;
		try
		{
			result = new Long(string).longValue();
		}
		catch ( Exception e)
		{
			// just return a 0
			result = 0;
		}
		return result;
	}

	/**
	 * @return long  Primary Key
	 */
	private long insertPlantStatus(
		long plantConceptId, 
		long referenceId, 
		long plantPartyId, 
		String plantConceptStatus,
		String plantParentName,
		String startDate,
		String stopDate,
		String plantLevel,
		String partyComments) 
		throws SQLException
	{
		long statusId = 0;
		long plantParentConceptId = 0;
		
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
		
		pstmt.setLong(1, plantConceptId);
		if ( referenceId == 0 )
		{
			pstmt.setNull(2, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setLong(2, referenceId);
		} 
		pstmt.setLong(3, plantPartyId);
		pstmt.setString(4, plantConceptStatus);
		Utility.insertDateField(startDate, pstmt, 5);  
		Utility.insertDateField(stopDate, pstmt, 6);  
		pstmt.setString(7, plantLevel);		
		pstmt.setLong(8, statusId);		
		// 0 => that no plantconcept_id was found
		if (plantParentConceptId == 0)
		{
			pstmt.setNull(9, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setLong(9, plantParentConceptId);		
		}
		pstmt.setString(10, plantParentName);
		pstmt.setString(11, partyComments);
				
		pstmt.executeUpdate();
		pstmt.close();

		return statusId;
	}

	private long insertPlantUsage(
		long plantNameId, 
		long plantConceptId, 
		String plantName,
		long plantPartyId, 
		String startDate,
		String synonymName,
		String plantNameStatus,
		String classSystem) 		
		throws SQLException
	{
		long usageId =
			Utility.dbAdapter.getNextUniqueID(
				conn,
				"PLANTUSAGE",
				"plantusage_id");	
				
					
		PreparedStatement pstmt =
				conn.prepareStatement(
					" insert into PLANTUSAGE (PLANTNAME_ID, PLANTCONCEPT_ID, PLANTNAME, "
					+ "PLANTPARTY_ID,  USAGESTART, acceptedsynonym, plantnamestatus, "
					+ "classsystem, plantusage_id)  values (?,?,?,?,?,?,?,?,?)"
				);
				
		pstmt.setLong(1,plantNameId);
		pstmt.setLong(2,plantConceptId);		
		pstmt.setString(3, plantName);
		pstmt.setLong(4,plantPartyId);			
		Utility.insertDateField(startDate, pstmt, 5);  		
		pstmt.setString(6, synonymName);
		pstmt.setString(7, plantNameStatus);
		pstmt.setString(8,classSystem);
		pstmt.setLong(9,usageId);
				
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
	private long insertPlantName(
		long refId,
		String plantName,
		String dateEntered)
		throws SQLException
	{
		// Get Primary key for table		
		long plantNameId =
			Utility.dbAdapter.getNextUniqueID(
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
			pstmt.setLong(1, refId);
		} 
		pstmt.setString(2, plantName);
		pstmt.setString(3, dateEntered);
		pstmt.setLong(4, plantNameId);
		pstmt.executeUpdate();
		pstmt.close();

		return (plantNameId);
	}

	/**
	 * method that will return a database connection for use with the database
	 *
	 * @return conn -- an active connection
	 */
	private DBConnection getConnection() throws SQLException
	{
		return DBConnectionPool.getInstance().getDBConnection("Need connection for inserting plants");
	}
	
	private long insertPlantConcept(
		long plantNameId,
		long refId,
		String plantName,
		String plantDescription,
		String plantCode)
		throws SQLException
	{
		long nextId =
			Utility.dbAdapter.getNextUniqueID(
				conn,
				"plantconcept",
				"plantconcept_id");

		PreparedStatement pstmt =
			conn.prepareStatement(
				" insert into PLANTCONCEPT (PLANTNAME_ID, REFERENCE_ID, PLANTNAME, "
					+ " PLANTDESCRIPTION,  PLANTCODE, plantconcept_id)   values (?,?,?,?,?,?)");

		pstmt.setLong(1, plantNameId);
		if ( refId == 0)
		{
			pstmt.setNull(2, java.sql.Types.INTEGER);
		}
		else
		{
			pstmt.setLong(2, refId);
		} 
		pstmt.setString(3, plantName);
		pstmt.setString(4, plantDescription);
		pstmt.setString(5, plantCode);
		pstmt.setLong(6, nextId);
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
