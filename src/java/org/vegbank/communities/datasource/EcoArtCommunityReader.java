/*
 *	'$RCSfile: EcoArtCommunityReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-04-16 00:16:45 $'
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
 
package org.vegbank.communities.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractList;
import java.util.Vector;

import org.vegbank.common.Constants;
import org.vegbank.common.model.Community;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Reference;

/**
 * @author farrell
 */

public class EcoArtCommunityReader implements Constants
{
	
	// Reference Information
	private static final String authors = "NatureServe";
	private static final String title = 
		"International Classification of Ecological Communities: Terrestrail Vegetation. EcoArt version 2.65";
	private static final String pubdate = "13-FEB-2002";
	private static final String edition = "Version 2.65";
	private static final String otherCitationDetails =
		"Natural Heritage Central Databases. NatureServe, Arlington, VA";
	// Party Information
	private static final String organization = "NatureServe";
	private static final String contactInstructions = "http://www.natureserve.org/";
	
	private static Reference ref = null;
	private static Party party = null;
	
	private static final String CODE_CLASSSYSTEM = "CEGL";
	private static final String NAME_CLASSSYSTEM = "NVC";	
	
	private Connection conn = null;
	private AbstractList communities = new Vector();
	
	public EcoArtCommunityReader(Connection conn)
	{
		this.conn = conn;
		
		// Initialize the refence if needed
		if (ref == null)
		{
			EcoArtCommunityReader.initializeReference();
		}
		if ( party == null)
		{
			EcoArtCommunityReader.initializeParty();
		}
		
		try
		{
			// Get Info from each table in EcoArt Databse !!!
			this.readClassTable();
			this.readSubclassTable();
			this.readGroupTable();
			this.readSubgroupTable();
			this.readFormationTable();
			this.readAllianceTable();
			this.readAssociationTables();

			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	private void readClassTable() throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "select ClassCode, ClassName, ClassDesc, Update from Class";
		ResultSet rs = stmt.executeQuery(query);
		while ( rs.next() )
		{
			String classLevel = "Class";
			String code = rs.getString(1);
			String name = rs.getString(2);
			String description= rs.getString(3);
			String date = rs.getString(4);					
			
			this.createCommmunity(
				classLevel,
				code,
				name,
				null,
				CONCEPT_STATUS_ACCEPTED,
				date,
				date,
				null,
				null,
				description,
				date,
				null);	

		}
	}
	
	private void readSubclassTable() throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = 
			"select SubclassCode, SubclassName, SubclassDesc, subclass.Update, classCode from "
			+ "subclass, class  where class.classkey = subclass.classkey";
		ResultSet rs = stmt.executeQuery(query);
		while ( rs.next() )
		{
			String classLevel = "Subclass";
			String code = rs.getString(1);
			String name = rs.getString(2);
			String description = rs.getString(3);
			String date = rs.getString(4);					
			String parentCode = rs.getString(5);
			
			this.createCommmunity(
				classLevel,
				code,
				name,
				null,
				CONCEPT_STATUS_ACCEPTED,
				date,
				date,
				null,
				null,
				description,
				date,
				parentCode);	

		}
	}	

	private void readGroupTable() throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = 
			"select GroupCode, GroupName, group_.Update, SubclassCode from group_, subclass "
			+ "where subclass.SubclassKey =group_.SubclassKey ";
		ResultSet rs = stmt.executeQuery(query);
		while ( rs.next() )
		{
			String classLevel = "Group";
			String code =rs.getString(1);
			String name = rs.getString(2);
			String date = rs.getString(3);					
			String parentCode =rs.getString(4);	
			
			this.createCommmunity(
				classLevel,
				code,
				name,
				null,
				CONCEPT_STATUS_ACCEPTED,
				date,
				date,
				null,
				null,
				null,
				date,
				parentCode);	

		}
	}	

	private void readSubgroupTable() throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "select SubgroupCode, SubgroupName, Subgroup.Update, GroupCode from "
			+ "Subgroup, Group_ where Group_.GroupKey = Subgroup.GroupKey";
		ResultSet rs = stmt.executeQuery(query);
		while ( rs.next() )
		{
			String classLevel = "Subgroup";
			String code = rs.getString(1);
			String name = rs.getString(2);
			String date = rs.getString(3);					
			String parentCode =rs.getString(4);	
			
			this.createCommmunity(
				classLevel,
				code,
				name,
				null,
				CONCEPT_STATUS_ACCEPTED,
				date,
				date,
				null,
				null,
				null,
				date,
				parentCode);	

		}
	}	
	
	private void readFormationTable() throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = 
			"select FormationCode, FormationName, Formation.Update, SubGroupCode from "
			+ "Formation, SubGroup where SubGroup.subgroupkey = Formation.subgroupkey";
		ResultSet rs = stmt.executeQuery(query);
		while ( rs.next() )
		{
			String classLevel = "Formation";
			String code = rs.getString(1);
			String name = rs.getString(2);
			String date = rs.getString(3);					
			String parentCode = rs.getString(4);	
	
			this.createCommmunity(
				classLevel,
				code,
				name,
				null,
				CONCEPT_STATUS_ACCEPTED,
				date,
				date,
				null,
				null,
				null,
				date,
				parentCode);			
		}
	}	
	
	private void readAllianceTable() throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = 
			"select AllianceKey, AllianceName, AllianceDesc, AllianceOriginDate, "
			+ "FormationCode, AllianceNameTrans from Alliance";
		ResultSet rs = stmt.executeQuery(query);
		while ( rs.next() )
		{
			String classLevel = "Alliance" ;
			String code = rs.getString(1);
			String name = rs.getString(2);
			String description = rs.getString(3);	
			String date = rs.getString(4);					
			String parentCode = rs.getString(5);	
			String commonName = rs.getString(6);	
			
			this.createCommmunity(
				classLevel,
				code,
				name,
				commonName,
				CONCEPT_STATUS_ACCEPTED,
				date,
				date,
				null,
				null,
				description,
				date,
				parentCode);			
		}
	}	
		
	private void readAssociationTables() throws SQLException
	{
		// There are three tables that contain this information:
		
		Statement stmt = conn.createStatement();
		String query = 
			"select ELCODE, GNAME, CLASSIFKEY, AssocOriginDate, GnameTrans, ClassifUsed "
			+ "from ETC";
		ResultSet rs = stmt.executeQuery(query);
		while ( rs.next() )
		{
			String classUsed = rs.getString(6);
			if ( classUsed.trim().equals("GC"))  // Ignore all others
			{
				String classLevel = "Association";
				String code = rs.getString(1);
				String name =	rs.getString(2);
				String parentCode = rs.getString(3);	
				String date = rs.getString(4);					
				String commonName = rs.getString(5);	
				
				this.createCommmunity(
					classLevel,
					code,
					name,
					commonName,
					CONCEPT_STATUS_ACCEPTED,
					date,
					date,
					null,
					null,
					null,
					date,
					parentCode);
			}
		}
		rs.close();
		
		// The ETC_HISTORIC table has associations
//		query = 
//			"select ELCODE, GNAME, CLASSIFKEY, AssocOriginDate, Archive_date, "
//			+ "Archive_reason from ETC_HISTORIC";
//		rs = stmt.executeQuery(query);
//		while ( rs.next() )
//		{
//			Community comm = new Community();
//			String setClassLevel("Association");
//			String setCode(rs.getString(1), CODE_CLASSSYSTEM);
//			String setName(rs.getString(2), NAME_CLASSSYSTEM);
//			String setParentCode(rs.getString(3));	
//			String date = rs.getString(4);			
//			String setDateEntered(date);		
//			String setStatusStartDate(date);	
//			String setStatusStopDate(rs.getString(5));	
//						
//			String setStatus(CONCEPT_STATUS_NOT_ACCEPTED);				
//			this.communities.add(comm);
//		}
//		rs.close();
		
		// The ETC_OBSOLETE table has associations ( that are obsolete )
//		query = "select ELCODE, OBSOLETE, OBSOLETE_DATE from ETC_OBSOLETE";
//		rs = stmt.executeQuery(query);
//		while ( rs.next() )
//		{
//			Community comm = new Community();
//			String setClassLevel("Association");
//			String setCode(rs.getString(1), CODE_CLASSSYSTEM);
//			String setName(rs.getString(2), NAME_CLASSSYSTEM);
//			// Date it became obsolete
//			//String setDateEntered(rs.getString(3);		
//			String setStatus(CONCEPT_STATUS_NOT_ACCEPTED);				
//			this.communities.add(comm);
//		}	
//		rs.close();			
	}	
	
	public AbstractList getAllCommunities() 
	{
		return this.communities;
	}
	
	/**
	 * Creates a new community from the parameters. Its a horrifically large 
	 * method but captures some class specific defaults and assists in 
	 * abstracting the of gathering the resultset values. 
	 * 
	 * @param classLevel
	 * @param code
	 * @param name
	 * @param commonName
	 * @param status
	 * @param dateEntered
	 * @param statusStartDate
	 * @param statusStopDate
	 * @param statusComment
	 * @param description
	 * @param usageStartDate
	 * @param parentCode
	 */
	private void createCommmunity (
		String classLevel,
		String code,
		String name,
		String commonName,
		String status,
		String dateEntered,
		String statusStartDate,
		String statusStopDate,
		String statusComment,
		String description,
		String usageStartDate,
		String parentCode)
	{
		
		//	 Filter out CAVE & COMPLEX rows and outer invalids
		if ( code == null || code.equals("") )
		{
			return;
		}
		
		Community comm = new Community();
		
		// Set all the attributes
		comm.setDateEntered(dateEntered);
		comm.setStatusStartDate(statusStartDate);
		comm.setStatusStopDate(statusStopDate);
		comm.setUsageStartDate(usageStartDate);
		comm.setClassLevel(classLevel);
		comm.setCode(code, CODE_CLASSSYSTEM);
		comm.setName(name, NAME_CLASSSYSTEM);
		if ( commonName != null )
		{
			comm.setCommonName(commonName);  // Community knows the classSystem here
		}
		comm.setStatus(status);
		comm.setStatusComment(statusComment);
		comm.setDescription(description);
		comm.setParentCode(parentCode);
		
		comm.setAllReferences(ref);
		comm.setParty(party);
		
		this.communities.add(comm);
	}
	
	private static void initializeReference()
	{
			// Initialize the Reference
			ref = new Reference();
			ref.setEdition(edition);
			ref.setPubDate(pubdate);
			ref.setTitle(title);
	}
	
	/**
	 * Create a Party from static class variables
	 */
	private static void initializeParty() 
	{
		// Initialize the Party
		party = new Party();
		party.setOrganizationName(organization);
		party.setContactInstructions(contactInstructions);
	}

}
