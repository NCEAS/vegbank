/*
 *	'$RCSfile: Query.java,v $'
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class Query
{
	
	DatabaseAccess da = new DatabaseAccess();
	
	public Query ()
	{
	}
	
	/**
	 * Find the conceptId of a plant using the plantname to search
	 * This is a case insensitive search.
	 * 
	 * @param String -- plantName
	 * @return int -- plantConcept PrimaryKey
	 * @throws SQLException
	 */
	public int getPlantConceptId(String plantName)
		throws Exception
	{
		int plantConceptId = 0;
		
		// Case insensitive search for plantName
		String query = " select plantconcept_id from plantusage, plantname where" 
				+ " upper(plantname.plantname) = '" + plantName.toUpperCase() +"' and " 
				+ " plantname.plantname_id = plantusage.plantname_id";
		
		ResultSet rs = da.issueSelect(query);
		if ( rs.next() )
		{
			plantConceptId = rs.getInt(1);
            da.closeStatement();
            rs.close();
		}
		else
		{
			throw new Exception("Could not find a concept with the name '" + plantName +"'");
		}
		return plantConceptId;
	}
	
	/**
	 * Find the statustId of a plant using the plantname to search
	 * 
	 * @param String -- plantName
	 * @return int -- plantStatus PrimaryKey
	 * @throws SQLException
	 */
	public int getPlantStatusId(String plantName)
		throws Exception
	{
		int plantStatusId = 0;
		
		// Case insensitive search for plantNames status
		String query = " select plantstatus_id from plantstatus, plantusage, plantname where" 
				+ " plantname.plantname = '" + plantName +"' and " 
				+ " plantname.plantname_id = plantusage.plantname_id and "
				+ " plantusage.plantconcept_id = plantstatus.plantconcept_id";
		
		ResultSet rs = da.issueSelect(query);
		
		if ( rs.next() )
		{
			plantStatusId = rs.getInt(1);
            da.closeStatement();
            rs.close();
		}
		else
		{
			throw new Exception("Could not find a status with the name '" + plantName +"'");
		}
		return plantStatusId;
	}
	
	/**
	 * Find the conceptId of a community using the commname to search
	 * This is a case insensitive search.
	 * 
	 * @param String -- commName
	 * @return int -- commConcept PrimaryKey
	 * @throws SQLException
	 */
	public int getCommConceptId(String commName)
		throws Exception
	{
		int commConceptId = 0;
		
		// Case insensitive search for plantName
		String query = " select commconcept_id from commusage, commname where" 
				+ " upper(commname.plantname) = '" + commName.toUpperCase() +"' and " 
				+ " commname.commname_id = commusage.commname_id";
		
		ResultSet rs = da.issueSelect(query);
		if ( rs.next() )
		{
			commConceptId = rs.getInt(1);
            da.closeStatement();
            rs.close();
		}
		else
		{
			throw new Exception("Could not find a concept with the name '" + commName +"'");
		}
		return commConceptId;
	}
	
	/**
	 * Find the statustId of a community using the plantname to search
	 * Case insensitive.
	 * 
	 * @param String -- plantName
	 * @return int -- plantStatus PrimaryKey
	 * @throws SQLException
	 */
	public int getCommStatusId(String commName)
		throws Exception
	{
		int commStatustId = 0;
		
		// Case insensitive search for commNames status
		String query = " select commstatus_id from commstatus, commusage, commname where" 
				+ " commname.commname = '" + commName +"' and " 
				+ " commname.commname_id = commusage.commname_id and "
				+ " commusage.commconcept_id = commstatus.commconcept_id";
		
		ResultSet rs = da.issueSelect(query);
		
		if ( rs.next() )
		{
			commStatustId = rs.getInt(1);
            da.closeStatement();
            rs.close();
		}
		else
		{
			throw new Exception("Could not find a status with the name '" + commName +"'");
		}
		return commStatustId;
	}

	/**
	 * @param synonymnName
	 * @param statusId
	 * @param conceptId
	 * @param plantConvergence
	 * @param startDate
	 * @param stopDate
	 */
	public void insertPlantCorrelation(
		String synoymnName,
		int statusId,
		String plantConvergence,
		String startDate,
		String stopDate)
		throws SQLException
	{
		DBConnection conn =DBConnectionPool.getInstance().getDBConnection("Need connection for inserting PlantCorrelation");
		
		int correlationId =
			(int) Utility.dbAdapter.getNextUniqueID(
				conn,
				"PLANTCORRELATION",
				"plantstatus_id");
		
		try
		{
			int conceptId = this.getPlantConceptId(synoymnName);
			PreparedStatement pstmt =
				conn.prepareStatement(
			" insert into PLANTCORRELATION ( PLANTSTATUS_ID, PLANTCONCEPT_ID, "
			+ "plantConvergence,  correlationstart, correlationstop, PLANTCORRELATION_ID	)"
			+ " values (?,?,?,?,?,?)");		
			
			pstmt.setInt(1, statusId);
			pstmt.setInt(2, conceptId);
			pstmt.setString(3, plantConvergence);
			Utility.insertDateField(startDate, pstmt, 4);  
			Utility.insertDateField(stopDate, pstmt, 5);  
			pstmt.setInt(6, correlationId);
		
			pstmt.execute();
		}
		catch (SQLException se)
		{
			throw se;
		}
		catch (Exception e)
		{
			System.out.println("Could not find synonym, indicates a sorting problem maybe");
			e.printStackTrace();
		}
	}

}
