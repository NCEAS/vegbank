/*	
 * '$RCSfile: LoadTreeToDatabase.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-01 17:26:29 $'
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
package org.vegbank.dataload.XML;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.vegbank.common.Constants;
import org.vegbank.common.model.Address;
import org.vegbank.common.model.Aux_role;
import org.vegbank.common.model.Commclass;
import org.vegbank.common.model.Commconcept;
import org.vegbank.common.model.Commcorrelation;
import org.vegbank.common.model.Comminterpretation;
import org.vegbank.common.model.Commlineage;
import org.vegbank.common.model.Commname;
import org.vegbank.common.model.Commstatus;
import org.vegbank.common.model.Commusage;
import org.vegbank.common.model.Disturbanceobs;
import org.vegbank.common.model.Embargo;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Observationcontributor;
import org.vegbank.common.model.Observationsynonym;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Place;
import org.vegbank.common.model.Plantconcept;
import org.vegbank.common.model.Plantcorrelation;
import org.vegbank.common.model.Plantlineage;
import org.vegbank.common.model.Plantname;
import org.vegbank.common.model.Plantstatus;
import org.vegbank.common.model.Plantusage;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Projectcontributor;
import org.vegbank.common.model.Reference;
import org.vegbank.common.model.Referencealtident;
import org.vegbank.common.model.Referencecontributor;
import org.vegbank.common.model.Referenceparty;
import org.vegbank.common.model.Soilobs;
import org.vegbank.common.model.Stemcount;
import org.vegbank.common.model.Stratum;
import org.vegbank.common.model.Stratumtype;
import org.vegbank.common.model.Taxonalt;
import org.vegbank.common.model.Taxonimportance;
import org.vegbank.common.model.Taxoninterpretation;
import org.vegbank.common.model.Taxonobservation;
import org.vegbank.common.utility.AccessionGen;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;


/**
	 * 
	 * @author farrell
	 *
	 * Takes a Hashtable representation of the XML to be loaded 
	 * and writes it to the database.
	 */
	public class LoadTreeToDatabase
	{
		public static final String TABLENAME = "TableName";
		public InputPKTracker inputPKTracker = new InputPKTracker();
		private DBConnection dbConn = null; 
		private Hashtable revisionsHash = null;
		private Hashtable noteLinksHash = null;
		private Hashtable vegbankPackage = null;
		private LoadingErrors errors = null;
		private boolean commit = false;
		// Allow no commit for testing
		private boolean doCommit = true;
		private AccessionGen ag  = null;
		private HashMap tableKeys = new HashMap();
		
		// This holds the name of the current concept
		private String currentConceptName = null;
		
		public LoadTreeToDatabase(LoadingErrors errors, boolean doCommit)
		{
			this.doCommit = doCommit;
			this.errors = errors;
		}

		public LoadTreeToDatabase(LoadingErrors errors)
		{
			this.errors = errors;
		}
		
		/**
		 * Inserts the entire dataset into the database
		 * 
		 * @param vegbankPackage -- the root of the dataset 
		 */
		public void insertVegbankPackage(Hashtable vegbankPackage)
			throws SQLException
		{
			this.vegbankPackage = vegbankPackage;
			//Utility.prettyPrintHash(vegbankPackage);

			//this boolean determines if the dataset should be commited or rolled-back
			commit = true;
			this.initDB();

			// insert commConcepts
			Enumeration commConcepts =  getChildTables(vegbankPackage, "commConcept");
			while ( commConcepts.hasMoreElements() )
			{
				Hashtable commConcept = (Hashtable) commConcepts.nextElement();
				insertCommConcept(commConcept);
			}			

			// insert plantConcepts
			Enumeration plantConcepts =  getChildTables(vegbankPackage, "plantConcept");
			while ( plantConcepts.hasMoreElements() )
			{
				Hashtable plantConcept = (Hashtable) plantConcepts.nextElement();
				insertPlantConcept(plantConcept);
			}

			// insert references
			Enumeration references =
				getChildTables(vegbankPackage, "reference");
			while (references.hasMoreElements())
			{
				Hashtable reference = (Hashtable) references.nextElement();
				this.insertReference(reference);
			}

			// insert Parties
			Enumeration parties = getChildTables(vegbankPackage, "party");
			while (parties.hasMoreElements())
			{
				Hashtable party = (Hashtable) parties.nextElement();
				this.insertParty(party);
			}

			// insert observations
			Enumeration observations = getChildTables(vegbankPackage, "observation");
			while (observations.hasMoreElements())
			{
				Hashtable observation = (Hashtable) observations.nextElement();
				insertObservation(observation);
			}

			LogUtility.log(
				"LoadTreeToDatabase:  insertion success: " + commit);

			if (commit == true && doCommit == true)
			{				
				dbConn.commit();
				LogUtility.log("LoadTreeToDatabase: Adding AccessionCodes to loaded data");
				this.addAllAccessionCodes();
				dbConn.commit();
			}
			else
			{
				dbConn.rollback();
			}
			
			LogUtility.log("LoadTreeToDatabase: Returning the DBConnection to the pool");
			//Return dbconnection to pool
			DBConnectionPool.returnDBConnection(dbConn);
		}

		private void initDB() throws SQLException
		{
			//	Get DBConnection
			dbConn=DBConnectionPool.getInstance().getDBConnection("Need connection for inserting dataset");
			dbConn.setAutoCommit(false);
		}
		
		/**
		 * Get the PK of the row in the database that has the same values as record
		 * 
		 * @param tableName
		 * @param fieldValueHash
		 * @return long -- PK of the table
		 */
		private long getTablePK( String tableName, Hashtable fieldValueHash )
		{
			Vector fieldEqualsValue = new Vector();
			StringBuffer sb = new StringBuffer();
			long PK = 0;
			try 
			{
				Enumeration fields = fieldValueHash.keys();
				while ( fields.hasMoreElements())
				{
					String field = (String) fields.nextElement();
					Object value = fieldValueHash.get(field);
					if ( this.isDatabaseReadyField(field, value, tableName) )
					{				
						fieldEqualsValue.add(field + "=" +"'" + Utility.encodeForDB(value.toString()) + "'");
					}
				}
				
				sb.append(
					"SELECT " + Utility.getPKNameFromTableName(tableName) +" from "+tableName+" where " 
					+ Utility.joinArray(fieldEqualsValue.toArray(), " and ")
				);
				
				sb.append(this.getSQLNullValues(tableName, fieldValueHash));
				
				Statement query = dbConn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				while (rs.next()) 
				{
					PK = rs.getInt(1);
				}
			}
			catch ( SQLException se ) 
			{ 
				this.filterSQLException(se, sb.toString());     
			}
			LogUtility.log("LoadTreeToDatabase:  Query: " + sb.toString());
			return PK;
		}
		
		/**
		 * Get the PK of the row in the database that has the same values as record
		 * 
		 * @param tableName
		 * @param AccessionCode
		 * @return long -- PK of the table
		 */
		private long getTablePK( String tableName, String accessionCode )
		{
			Vector fieldEqualsValue = new Vector();
			StringBuffer sb = new StringBuffer();
			long PK = 0;
			try 
			{
				sb.append(
					"SELECT " + Utility.getPKNameFromTableName(tableName) +" from "+tableName+" where " 
					+ Constants.ACCESSIONCODENAME + " = '" + accessionCode + "'"
				);

				Statement query = dbConn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				while (rs.next()) 
				{
					PK = rs.getInt(1);
				}
			}
			catch ( SQLException se ) 
			{ 
				this.filterSQLException(se, sb.toString());     
			}
			LogUtility.log("LoadTreeToDatabase:  Query: " + sb.toString());
			return PK;
		}
		
		/**
		 * @param se
		 */
		private void filterSQLException(SQLException se, String sql)
		{
				if ( se.getMessage().startsWith("ERROR:  current transaction is aborted") )
				{
					// Filter out errors like this, doing nothing
				}
				else if ( se.getMessage().equals("No results were returned by the query.") )
				{
					// Filter out errors like this, doing nothing
				}
				else
				{
					LogUtility.log("LoadTreeToDatabase: Caught SQL Exception: " + se.getMessage());
					LogUtility.log("LoadTreeToDatabase: problematic sql: '" + sql + "'");
					se.printStackTrace();
					commit = false;
					errors.AddError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
				}
		}

		/**
		 * Is this a field I want to send to the Database
		 * @param field
		 * @param value
		 * @param tableName
		 * @return
		 */
		private boolean isDatabaseReadyField(String field, Object value, String tableName)
		{ 
			boolean result = true;
			
			if (!(value instanceof String)
				|| field.equals("TableName")
				|| field.equals("REVISION_ID")
				|| (field.equals("NOTELINK_ID") && ! tableName.equalsIgnoreCase("note"))
				|| isPKName(field, tableName) )
			{
				result = false;
			}

			return result;
		}
		
		private boolean isPKName(String field, String tableName)
		{
			boolean result = false;
			if (field.equalsIgnoreCase(Utility.getPKNameFromTableName(tableName)) )
			{
				result = true;
			}
			LogUtility.log("isPKFieldOfInput tested :" + field
					+ " and decided it is the PK of " + tableName + " : " + result,
					LogUtility.TRACE);
			return result;
		}
		
		/**
		 * Get the next value from the sequence of the PK of this table
		 * 
		 * @param tableName
		 * @return long -- the next availible PK for this table
		 */
		private long getNextId(String tableName) 
		{
			long PK = -1;
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT nextval('" + tableName + "_" + Utility.getPKNameFromTableName(tableName) + "_seq')");
			try 
			{
			   Statement query = dbConn.createStatement();
			   ResultSet rs = query.executeQuery(sb.toString());
			   while (rs.next()) 
			   {
					   PK = rs.getLong(1);
			   }
			   rs.close();
			   LogUtility.log("Get next PK: SQL: " + sb.toString() + " returned PK ="
			   		+ PK ,LogUtility.TRACE);
			}
			catch (SQLException se)
			{
				this.filterSQLException(se, sb.toString());
			}
			return (PK);
		}

		/**
		 * 
		 * @param tableName
		 * @param Enumeration 
		 * @param FKName
		 * @param FKValue
		 * @return boolean -- success or not
		 */
		private boolean insertTables( String tableName, Enumeration tables, String fKName, long fKValue ) throws SQLException
		{
			boolean result = true;
			
			while( tables.hasMoreElements()  )
			{
				Hashtable table = (Hashtable) tables.nextElement();
				//LogUtility.log("LoadTreeToDatabase: " + tableName + " : " + table);
				addForeignKey(table, fKName, fKValue);
				insertTable(tableName, table);
			}
			
			return result;
		}

		/**
		 * Insert this record into the database, check if it has been add before first
		 * 
		 * Check if there exists a reference for this table and add it first.
		 * Check if there exists a note for each field and add it last.
		 * Check and insert userdefined table last.
		 * 
		 * Returns 0 if null is passed in.
		 * 
		 * @param tableName
		 * @param fieldValueHash
		 * @return long -- PK of the table
		 */
		private long insertTable( String tableName, Hashtable fieldValueHash ) throws SQLException
		{	
			//LogUtility.log("LoadTreeToDatabase : insert: " + tableName);
			
			long PK = 0;

			// Handle null being passed or empty
			if ( fieldValueHash == null )
			{
				return PK;
			}
			
			// Have we this already in the db
			PK = getExtantPK(fieldValueHash);
			if ( PK != 0 )
			{
				return PK;
			}
			
			// Is the user allowed the load this 
			// TODO: Make configurable and add user perms. 
			if ( ! this.isAllowedToLoad(fieldValueHash) )
			{
				return PK; // Even if empty
			}
			
			// Insert Reference if it exists
			long referenceId = 0;
			Hashtable reference = getFKChildTable(fieldValueHash, "reference_ID", "reference");
			if ( reference != null)
			{
				referenceId = insertReference(reference);
			}
			addForeignKey(fieldValueHash, "reference_ID", referenceId);
			
			// Maybe need a method like this to support update extant data
			// Check if this already exists in the database
//			if ( this.tableExists( tableName, fieldValueHash) )
//			{
//				return this.getTablePK(tableName, fieldValueHash);
//			}
			
			//LogUtility.log("LoadTreeToDatabase: INSERT: " + tableName);
			PK = this.getNextId(tableName);
			
			// Strip out accessionCodes before writing to database
			// AC's are now added after loadind the dataset.
			this.removeAccessionCode(fieldValueHash);
			
			Vector fieldNames = new Vector();
			Vector fieldValues = new Vector();
			StringBuffer sb = new StringBuffer();
			try 
			{
				Enumeration fields = fieldValueHash.keys();
				while ( fields.hasMoreElements())
				{		
					String field = (String) fields.nextElement();
					Object value = fieldValueHash.get(field);
					
					//LogUtility.log("Handle fieldName " + field + " with value " + value, LogUtility.TRACE);
					
					// Ignore if not a string, null or empty
					if ( value instanceof String && ! Utility.isStringNullOrEmpty((String) value) )
					{
						String stringValue = (String) fieldValueHash.get(field);
						if ( this.isDatabaseReadyField(field, stringValue, tableName) )
						{				
							fieldNames.add(field);
							fieldValues.add("'" + Utility.encodeForDB(stringValue.toString()) + "'");
						}

						// Handle dups prevention
						if ( this.isPKName(field, tableName))
						{
							// Check if this has already been added to db
							long storedPK = inputPKTracker.getTablesPK(tableName, stringValue.toString());
							if (storedPK != 0) 
							{
								LogUtility.log("Already entered record xmlPK "
										+ stringValue.toString() + " into " + tableName + " using PK of "
										+ storedPK, LogUtility.TRACE);
								return storedPK;
							}
							// Need to add this to the datastruture that prevents
							// the adding duplicate fields
							inputPKTracker.setTablesPKs(tableName, stringValue.toString(), PK);
							LogUtility.log("No record added for xmlPK :" + stringValue.toString()
									+ " for table " + tableName + " adding PK now: " + PK, LogUtility.TRACE);
						}
					}
				}
				
				fieldNames.add( Utility.getPKNameFromTableName(tableName) );
				fieldValues.add("" + PK);

				sb.append("INSERT INTO " + tableName );
				sb.append(" (" + Utility.arrayToCommaSeparatedString( fieldNames.toArray() ) + ")" );
				sb.append(" VALUES (" + Utility.arrayToCommaSeparatedString( fieldValues.toArray() ) +")" );
				
				LogUtility.log("Running SQL: " + sb.toString(), LogUtility.TRACE );
				LogUtility.log("Loaded Table : " +tableName + " with PK of " + PK, LogUtility.INFO );
				
				Statement query = dbConn.createStatement();
				int rowCount = query.executeUpdate(sb.toString());
			}
			catch ( SQLException se ) 
			{
				this.filterSQLException(se, sb.toString());
			}
			
			// All the tables can have defined value field
			insertUserDefinedTables(tableName, PK, fieldValueHash);
			// All fields can have a revision
			insertRevision(PK, fieldValueHash);
			// All fields can have a note attached
			insertNoteLink(PK, fieldValueHash);
			
			this.storeTableNameAndPK(tableName, PK);
			
			LogUtility.log("Returning with PK :" + PK + " for table " + tableName, LogUtility.TRACE);
			return PK;
		}

		/**
		 * Remove accessionCode field if it exists
		 * 
		 * @param fieldValueHash
		 */
		private void removeAccessionCode(Hashtable fieldValueHash)
		{
				fieldValueHash.remove(Constants.ACCESSIONCODENAME);
		}

		/**
		 * Store the tableName and the Pk so an accessionCode can be generated later
		 * 
		 * @param tableName
		 * @param PK
		 */
		private void storeTableNameAndPK(String tableName, long PK) throws SQLException
		{	
				Vector keys = (Vector) tableKeys.get(tableName);
				if ( keys == null )
				{
					keys = new Vector();
					tableKeys.put(tableName, keys);
				}
				keys.add(new Long(PK));
		}
		
		/**
		 * Add accessionCodes to all the rows added by this loading.
		 */
		private void addAllAccessionCodes() throws SQLException
		{
			String accessionCode = "";
			
			if ( ag == null )
			{
				// Initialize the AccessionGen
				ag = new AccessionGen(this.dbConn.getConnections(), Utility.getAccessionPrefix() );
			}
			
			if ( Utility.isLoadAccessionCodeOn() )
			{
				ag.updateSpecificRows(tableKeys);
			}
		}
		
		
		/**
		 * Get the accessionCode from the XML
		 * 
		 * @param fieldValueHash
		 * @param tableName
		 * @param PK
		 */
		private long getPKFromAccessionCode(Hashtable fieldValueHash, String tableName)
		{		
			long PK = 0;
			String accessionCode = "";
			
			accessionCode = (String) fieldValueHash.get(Constants.ACCESSIONCODENAME);
			
			if ( Utility.isStringNullOrEmpty( accessionCode ))
			{
				LogUtility.log("LoadTreeToDatabase: Found no accessionCode for " + tableName, LogUtility.TRACE);
				// do nothing
			}
			else if ( accessionCode.startsWith(Utility.getAccessionPrefix() + ".") )
			{
				// Need to get the pK of the table
				PK = this.getTablePK(tableName, accessionCode);
				
				if ( PK != 0 )
				{
					// great got a real PK
					LogUtility.log(
						"LoadTreeToDatabase: Found PK ("
							+ PK
							+ ") for "
							+ tableName
							+ " accessionCode: "
							+ accessionCode, LogUtility.INFO);
				}
				else
				{
					// Problem no accessionCode like that in database -- fail load
					String errorMessage =
						"There is no "
							+ tableName
							+ " with a accessionCode of value '"
							+ accessionCode
							+ "' in the database.";
							
					LogUtility.log("LoadTreeToDatabase:  : " + errorMessage, LogUtility.ERROR );
					commit = false;
					errors.AddError(
						LoadingErrors.DATABASELOADINGERROR,
						errorMessage);
				}
			}
			else
			{
				LogUtility.log(
					"LoadTreeToDatabase: Got an alien accessionCode in table "
						+ tableName
						+ " --> "
						+ accessionCode, LogUtility.TRACE);
						
				// Remove from hash
				fieldValueHash.remove(Constants.ACCESSIONCODENAME);
			}
			
			LogUtility.log(
					"LoadTreeToDatabase: Using accessionCode " + accessionCode + " in table "
						+ tableName + " got DB PK of " + PK,  LogUtility.TRACE);
			return PK;
		}

		/**
		 * @param FK
		 * @param fieldValueHash
		 */
		private void insertNoteLink(long FK, Hashtable fieldValueHash) throws SQLException
		{
			// Does this table have a noteLink
			Enumeration noteLinks =  getChildTables(fieldValueHash, "noteLinkList");
			while ( noteLinks.hasMoreElements() )
			{
				Hashtable noteLink = (Hashtable) noteLinks.nextElement();
				
				// Find the noteLink that matchs this
				String noteLinkId =  (String) noteLink.get("NOTELINK_ID");	
				
				if (noteLinksHash == null)
				{
					noteLinksHash = new Hashtable();
					Enumeration allNoteLinks =  getChildTables(vegbankPackage, "noteLink");
					{
						Hashtable aNoteLink = (Hashtable) allNoteLinks.nextElement();
						noteLinksHash.put(aNoteLink.get("NOTELINK_ID"), aNoteLink);
					}
				}
				
				Hashtable noteLinkDescription = (Hashtable)noteLinksHash.get(noteLinkId);
				this.addForeignKey(noteLinkDescription, "tableRecord", FK);
				
				
				// Sanity check the tablename and attribute name are equal
				if ( ! isFieldValueEqual(noteLinkDescription, noteLink, "tableName") 
					|| ! isFieldValueEqual(noteLinkDescription, noteLink, "attributeName"))
				{
					// Housten we have a problem
					errors.AddError(LoadingErrors.VALIDATIONERROR, "Mismatch on NOTELINK_ID = " + noteLinkId);
					commit = false;
				}
				else
				{
					long noteLinkPK = insertTable("noteLink", noteLinkDescription);
					// Add note tables
					Enumeration notes =  getChildTables(noteLinkDescription, "note");
					while ( notes.hasMoreElements() )
					{
						Hashtable note = (Hashtable) notes.nextElement();
						// This is basically a contributor table .. 
						insertContributor("note", note, "NOTELINK_ID", noteLinkPK);	
					}
				}
			}

		}

		/**
		 * @param FK
		 * @param fieldValueHash
		 */
		private void insertRevision(long FK, Hashtable fieldValueHash) throws SQLException
		{
			// Does this table have a revision
			Enumeration revisions =  getChildTables(fieldValueHash, "revisionList");
			while ( revisions.hasMoreElements() )
			{
				Hashtable revision = (Hashtable) revisions.nextElement();
				
				// Find the revision that matchs this
				String revisionId =  (String) revision.get("REVISION_ID");	
				
				if (revisionsHash == null)
				{
					revisionsHash = new Hashtable();
					Enumeration allRevisions =  getChildTables(vegbankPackage, "revision");
					{
						Hashtable aRevision = (Hashtable) allRevisions.nextElement();
						revisionsHash.put(aRevision.get("REVISION_ID"), aRevision);
					}
				}

				Hashtable revisionDescription = (Hashtable)revisionsHash.get(revisionId);
				this.addForeignKey(revisionDescription, "tableRecord", FK);
				
				
				// Sanity check the tablename and attribute name are equal
				if ( ! isFieldValueEqual(revisionDescription, revision, "tableName")
					|| ! isFieldValueEqual(revisionDescription, revision, "tableAttribute"))
				{
					// Housten we have a problem
					errors.AddError(LoadingErrors.VALIDATIONERROR, "Mismatch on REVISION_ID = " + revisionId);
					commit = false;
				}
				else
				{
					insertTable("revision", revisionDescription);
				}
			}
			
		}
		
		private boolean isFieldValueEqual(Hashtable a, Hashtable b, String fieldName)
		{
			boolean result = false;
			
			if ( ((String) a.get(fieldName)).equalsIgnoreCase( ((String) b.get(fieldName) ) ) )
			{
				result = true;
			}
			
			return result;
		}

		/**
		 * Convience method to add a reference and it child tables
		 * 
		 **/
		private long insertReference( Hashtable reference) throws SQLException
		{
			long referenceId = 0;
			// Handle null
			if ( reference == null )
			{
				return referenceId;
			}
			
			referenceId = getExtantPK(reference);
			if ( referenceId != 0 )
			{
				return referenceId;
			}
			
			// Insert Reference Journal
			Hashtable referenceJournal = getFKChildTable(reference, Reference.REFERENCEJOURNAL_ID, "referenceJournal");
			long referenceJournalId = insertTable("referenceJournal", referenceJournal);

			addForeignKey(reference, Reference.REFERENCEJOURNAL_ID, referenceJournalId);
			referenceId = insertTable("reference", reference);
			
			// Insert referenceAltIdent
			Enumeration referenceAltIdents =  getChildTables(reference, "referenceAltIdent");
			while ( referenceAltIdents.hasMoreElements() )
			{
				Hashtable referenceAltIdent = (Hashtable) referenceAltIdents.nextElement();
				addForeignKey(referenceAltIdent, Referencealtident.REFERENCE_ID, referenceId);
				long referenceAltIdentId = insertTable("referenceAltIdent", referenceAltIdent);
			}
			
			// Insert referenceContributor
			Enumeration referenceContributors =  getChildTables(reference, "referenceContributor");
			while ( referenceContributors.hasMoreElements() )
			{
				Hashtable referenceContributor = (Hashtable) referenceContributors.nextElement();
				
				Hashtable referenceParty = getFKChildTable(referenceContributor, Referencecontributor.REFERENCEPARTY_ID, "referenceParty");
				// Add  referenceParty
				long referencePartyId = insertReferenceParty(referenceParty);
				
				addForeignKey(referenceContributor, Referencecontributor.REFERENCE_ID, referenceId);
				addForeignKey(referenceContributor, Referencecontributor.REFERENCEPARTY_ID, referencePartyId);
				long referenceContributorId = insertTable("referenceContributor", referenceContributor);
			}		
			
			return referenceId;
		}
		
		/**
		 * @param referenceParty
		 * @return
		 */
		private long insertReferenceParty(Hashtable referenceParty) throws SQLException
		{
			long referencePartyId = 0;
			if (referenceParty != null)
			{
				referencePartyId = getExtantPK(referenceParty);
				if ( referencePartyId != 0 )
				{
					return referencePartyId;
				}
				
				Hashtable referencePartyParent = getFKChildTable(referenceParty, Referenceparty.CURRENTPARTY_ID, "referenceParty");
				long referencePartyParentId = insertReferenceParty(referencePartyParent);
				
				addForeignKey(referenceParty, Referenceparty.CURRENTPARTY_ID, referencePartyParentId);
				referencePartyId = insertTable( "referenceParty", referenceParty);
			}
			return referencePartyId;
		}

		private boolean insertUserDefinedTables( String parentTableName, long tableRecordID, Hashtable parentHash) throws SQLException
		{
			boolean result = false;
			
			Enumeration userdefineds = getChildTables( parentHash, "userDefined");
			while ( userdefineds.hasMoreElements() )
			{
				Hashtable userDefined = (Hashtable) userdefineds.nextElement();
				// Use the parentTableName as the tableName value
				userDefined.put( "tableName", parentTableName);
				long userDefinedPK = insertTable( "userDefined", userDefined );
				
				// Get the defined value
				Hashtable definedValue = getChildTable( userDefined, "definedValue");
				definedValue.put("tablerecord_id", ""+tableRecordID);
				definedValue.put("userdefined_id", ""+userDefinedPK);
				insertTable( "definedValue", definedValue );
			}
			return result;
		}
		
		/**
		 * method that returns true if the table with these values exists in the 
		 * database
		 */ 
		private boolean tableExists( String tableName, Hashtable fieldValueHash)
		{
			int rows = 0;
			
			Vector fieldEqualsValue = new Vector();
			StringBuffer sb = new StringBuffer();
			
			try 
			{		
				Enumeration fields = fieldValueHash.keys();
				while ( fields.hasMoreElements())
				{					
					String field = (String) fields.nextElement();
					//LogUtility.log("===" + field);
					Object value = fieldValueHash.get(field);
					if ( this.isDatabaseReadyField(field, value, tableName) )
					{				
						fieldEqualsValue.add(field + "=" + "'" + Utility.encodeForDB(value.toString()) + "'");
					}
				}
				
				// TODO: All fields that have no value should be placed in the search 
				
				sb.append(
					"SELECT count(*) from "+tableName+" where " 
					+ Utility.joinArray(fieldEqualsValue.toArray(), " and ")
				);

				sb.append(this.getSQLNullValues(tableName, fieldValueHash));
				
				Statement query = dbConn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				while (rs.next()) 
				{
					rows = rs.getInt(1);
				}
			}
			catch ( SQLException se ) 
			{      
				this.filterSQLException(se, sb.toString());
			}
				
			if (rows == 0) 
			{
				LogUtility.log("LoadTreeToDatabase : "+tableName+" does not exist", LogUtility.TRACE);
				return (false);
			} 
			else 
			{
				LogUtility.log("LoadTreeToDatabase : "+tableName+" does exist", LogUtility.TRACE);
				return (true);
			}
		}
		
		
		private String getSQLNullValues( String tableName, Hashtable fieldValuesHash ) throws SQLException
		{
			StringBuffer sb = new StringBuffer();
			
			ResultSet rs = dbConn.getMetaData().getColumns(null, null, tableName.toLowerCase(), "%" );
			
			//LogUtility.log("LoadTreeToDatabase : Got a Result Set");
			while ( rs.next() )
			{
				// according to api value 4 is the columnName
				String columnName = rs.getString(4);
				//LogUtility.log("LoadTreeToDatabase : ColumnName> " + columnName + " in table > " + tableName);
				
				boolean foundValueForColumn = false;
				Enumeration fields = fieldValuesHash.keys();
				while ( fields.hasMoreElements())
				{
					String fieldName = (String) fields.nextElement();
					
					// Compare with the currentColumnName
					if ( fieldName.equalsIgnoreCase(columnName))
					{
						foundValueForColumn = true;
					}
				}
				if ( ! foundValueForColumn )
				{
					// Need to add a null here
					sb.append(" and "  + columnName + " = NULL" );
				}
			}
			
			return sb.toString();
		}
		
		/**
		 * Convience method to get the a child TableHash that has is a FK
		 *  
		 * @param tableName
		 */
		private Hashtable getFKChildTable( Hashtable parentHash, String keyName ,String tableName)
		{
			Hashtable childHash = null;
			
			Hashtable keyHash =  this.getChildTable(parentHash, keyName);
			if ( keyHash != null )
			{
				childHash = this.getChildTable(keyHash, tableName);
			}
			return childHash;
		}
		
		/**
		 * Convience method to get the a child TableHash
		 * 
		 * @param tableName
		 */
		private Hashtable getChildTable( Hashtable parentHash, String tableName)
		{
			Hashtable childHash = null;
			if ( parentHash == null )
			{
				LogUtility.log("Got asked to find: '" + tableName+ "' from a null Hashtable. This shouldn't happen but hey, what do I know?", LogUtility.WARN);
				return childHash;

				//Utility.prettyPrintHash(parentHash);
			}
			
			//Utility.prettyPrintHash(parentHash);

			Object o = parentHash.get(tableName);
			if ( o instanceof java.util.Vector )
			{
				childHash = (Hashtable) ((Vector) o).elementAt(0);
			} 
			else  if (  o instanceof java.util.Hashtable  )
			{
				childHash = (Hashtable) o;
			}
			else if ( o == null)
			{
				LogUtility.log("LoadTreeToDatabase: Could not find table.. " + tableName + " as child of  table " + parentHash.get("TableName"), LogUtility.TRACE);
			}
			else
			{
				// Don't know what to do here
				LogUtility.log("LoadTreeToDatabase: Type: '" + o.getClass() + "' should not exist here in" + tableName + "?." , LogUtility.WARN);
			}
			return childHash;
		}
		
		/**
		 * Convience method to get the a child TableHashes as a vector
		 * 
		 * @param tableName
		 * @return Vector -- List of Hashtables for each child table
		 */
		private Enumeration getChildTables( Hashtable parentHash, String tableName)
		{
			Vector childVec  = new Vector();
			if ( parentHash == null )
			{
				// childVec empty	
			}
			else 
			{
				Object childObject = parentHash.get(tableName);
				if ( childObject == null)
				{
					childObject  = new Vector();
				}
				// Need to handle potential null before casting
				childVec = (Vector) childObject;
			}

			Enumeration enum = childVec.elements();
			return enum;
		}
		
		/**
		 * Insert a party
		 * 
		 * @param party
		 * @return
		 */
		private long insertParty( Hashtable party) throws SQLException
		{
			//LogUtility.log("### " +party);
			
			long pKey = 0;
			// Handle null
			if ( party == null )
			{
				return pKey;			
			}
			pKey = getExtantPK(party);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
			pKey = insertPartyBase(party, "party");
			
			// Insert Tables that depend on this PK
			Enumeration telephones =getChildTables( party, "telephone");
			insertTables("telephone", telephones, Address.PARTY_ID ,pKey );
			Enumeration addresses = getChildTables( party, "address");
			insertTables("address", addresses, Address.PARTY_ID ,pKey );	
			
			return pKey;
		}

		/**
		 * Party, PlantParty and CommParty are very similar.
		 * This method does shared functions need for insertion.
		 * 
		 * @param party
		 * @return
		 */
		private long insertPartyBase(Hashtable party, String tableName) throws SQLException
		{
			long pKey = 0;
			
			pKey = getExtantPK(party);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
			// recursivly deal with FK party's
			Hashtable ownerParty = getFKChildTable(party, "owner_ID", "party");
			Hashtable currentNameParty = getFKChildTable(party, "currentName_ID", "party");
			
			long ownerPartyPK = 0;
			long currentNamePartyPK = 0;
			
			if (ownerParty != null)
			{
				//LogUtility.log(">>>>" + ownerParty);			
				ownerPartyPK = insertParty(ownerParty);
			}
			
			if ( currentNameParty != null)
			{
				currentNamePartyPK = insertParty( currentNameParty);
			}
			
			addForeignKey(party, Party.CURRENTNAME_ID, currentNamePartyPK);
			
			pKey = insertTable(tableName, party);
							
			return pKey;
		}
		
		private long insertContributor(String tableName, Hashtable contribHash, String keyName, long keyValue) throws SQLException
		{
			long pKey = 0;
			// Handle null
			if ( contribHash == null )
			{
				return pKey;			
			}
			pKey = getExtantPK(contribHash);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
//			System.out.println("##############");
//			Utility.prettyPrintHash(contribHash);
//			System.out.println("##############");
						
			// Get the party ( required )
			Hashtable party = this.getFKChildTable(contribHash, "PARTY_ID", "party");
			long partyId = insertParty(party);
							
			// Get the Role  ( not required )
			Hashtable roleIDHash = (Hashtable) this.getChildTable(contribHash, "ROLE_ID");
			if ( roleIDHash != null )
			{
				Hashtable role = this.getChildTable( roleIDHash, "aux_Role" );
				long roleId = insertTable("aux_Role", role);
				addForeignKey(contribHash, "role_id", roleId);
			}
							
			addForeignKey(contribHash, keyName, keyValue);
			addForeignKey(contribHash, "party_id", partyId);
			insertTable(tableName, contribHash);
			
			return pKey;
		}
		
		/**
		 * method to initiate the insertion of a plot
		 *
		 * @param Hashtable -- Hashtable representing plot to load
		 */
		private long insertPlot(Hashtable plotHash) throws SQLException
		{		
			long plotId = 0;

			plotId = getExtantPK(plotHash);
			if ( plotId != 0 )
			{
				return plotId;
			}
			
			// Insert the parent plot if it exists
			Hashtable parentPlot =  this.getChildTable(plotHash, Plot.PARENT_ID);
			
			long parentPlotId = 0;
			if (parentPlot != null)
			{
				parentPlotId = insertPlot(parentPlot);
			}
			addForeignKey(plotHash,  Plot.PARENT_ID, parentPlotId);
			
			// Insert this plot
			plotId = insertTable("plot", plotHash);
			
			// Get and insert NamedPlace and place
			Enumeration places = this.getChildTables(plotHash, "place");
			while( places.hasMoreElements()  )
			{
				Hashtable place = (Hashtable) places.nextElement();
				addForeignKey(place, Place.PLOT_ID, plotId);

				// Insert NamedPlace
				Hashtable namedPlace =   this.getFKChildTable(place, Place.NAMEDPLACE_ID, "namedPlace");
				long namedPlaceId = insertTable("namedPlace",namedPlace);
					
				addForeignKey(place, Place.NAMEDPLACE_ID, namedPlaceId);
				long placeId = insertTable("place", place);
			}

			// Get and insert Embargos
			Enumeration embargos = this.getChildTables(plotHash, "embargo");
			while( embargos.hasMoreElements()  )
			{
				Hashtable embargo = (Hashtable) embargos.nextElement();
				addForeignKey(embargo, Embargo.PLOT_ID, plotId);

				long embargoId = insertTable("embargo", embargo);
			}

			return plotId;
		}

		/**
		 * Insert a project and its children
		 * 
		 * @param observationHash
		 * @throws SQLException
		 */
		private long insertProject(Hashtable projectFieldValueHash) throws SQLException {
			long projectId=0;
			// Handle null
			if ( projectFieldValueHash == null )
			{
				return projectId;			
			}
			projectId = getExtantPK(projectFieldValueHash);
			if ( projectId != 0 )
			{
				return projectId;
			}

			projectId = insertTable("project", projectFieldValueHash);
			
			Enumeration pcs = this.getChildTables( projectFieldValueHash, "projectContributor");
			while (pcs.hasMoreElements())
			{	
				this.insertContributor( "projectContributor", (Hashtable) pcs.nextElement(), Projectcontributor.PROJECT_ID,  projectId);
			}	

			return projectId;
		}

		/**
		 * Insert a observation into the database.
		 * 
		 * @param observationHash
		 * 
		 * @return long -- observationId
		 */
		private long insertObservation(Hashtable observationHash) throws SQLException
		{
			long observationId = 0;
			// Handle null
			if ( observationHash == null )
			{
				return observationId;			
			}
			
			observationId = getExtantPK(observationHash);
			if ( observationId != 0 )
			{
				return observationId;
			}
			
			//	Need to insert the plot and the project and get the FKs
			Hashtable project = this.getFKChildTable(observationHash, Observation.PROJECT_ID, "project");
			long projectId = this.insertProject(project);
			
			Hashtable plot = getFKChildTable(observationHash, Observation.PLOT_ID, "plot");
			long plotId = insertPlot(plot);

			// Continue loading as normal
			
			Hashtable previousObs = getFKChildTable(observationHash, Observation.PREVIOUSOBS_ID, "observation");
			
			long previousObsId = 0;
			if (previousObs != null)
			{
				previousObsId = insertObservation(previousObs);
			}
			
			addForeignKey(observationHash, Observation.PREVIOUSOBS_ID, previousObsId);
			addForeignKey(observationHash, Observation.PLOT_ID, plotId);
			addForeignKey(observationHash, Observation.PROJECT_ID, projectId);
			
			// CoverMethod
			Hashtable coverMethod = getFKChildTable(observationHash, Observation.COVERMETHOD_ID, "coverMethod");
			long coverMethodId = insertTable("coverMethod", coverMethod);
			this.insertTables("coverIndex",  getChildTables(coverMethod, "coverIndex"), Observation.COVERMETHOD_ID, coverMethodId);
			addForeignKey(observationHash, Observation.COVERMETHOD_ID, coverMethodId);

			// StratumMethod			
			Hashtable stratumMethod = getFKChildTable(observationHash, Observation.STRATUMMETHOD_ID, "stratumMethod");
			long stratumMethodId = insertStratumMethod(stratumMethod);
			addForeignKey(observationHash, Observation.STRATUMMETHOD_ID, stratumMethodId);

			// SoilTaxon
			Hashtable soilTaxon = getFKChildTable(observationHash, Observation.SOILTAXON_ID, "soilTaxon");
			long soilTaxonId = insertTable("soilTaxon", soilTaxon);
			addForeignKey(observationHash, Observation.SOILTAXON_ID, soilTaxonId);
			
			
			// Insert the observation
			observationId = insertTable("observation", observationHash);
			
			// TODO: insert graphic, observationSynonym
			//this.insertTables("graphic",  getChildTables(observationHash, "graphic"), "observation_id", observationId);
			
			// Add disturbanceObs
			this.insertTables("disturbanceObs",  getChildTables(observationHash, "disturbanceObs"), Disturbanceobs.OBSERVATION_ID, observationId);
			
			// Add SoilObs
			this.insertTables("soilObs",  getChildTables(observationHash, "soilObs"), Soilobs.OBSERVATION_ID, observationId);
			
			// Add observation contributors
			Enumeration ocs = this.getChildTables( observationHash, "observationContributor");
			while (ocs.hasMoreElements())
			{	
				this.insertContributor( "observationContributor", (Hashtable) ocs.nextElement(), Observationcontributor.OBSERVATION_ID,  observationId);
			}
			
			// Add stratums
			Enumeration stratums = getChildTables(observationHash, "stratum");
			while ( stratums.hasMoreElements())
			{
				Hashtable stratum = (Hashtable) stratums.nextElement();
				long stratumId = insertStratum(stratum, stratumMethodId, observationId);
			}
			
			// Add commClass
			Enumeration commClasses =  getChildTables(observationHash, "commClass");
			while ( commClasses.hasMoreElements() )
			{
				Hashtable commClass = (Hashtable) commClasses.nextElement();
				addForeignKey(commClass, Commclass.OBSERVATION_ID, observationId);
				long commClassId = insertTable("commClass", commClass);
				
				// Add Classification Contributors
				Enumeration ccs = this.getChildTables( observationHash, "classContributor");
				while (ccs.hasMoreElements())
				{	
					this.insertContributor( "classContributor", (Hashtable) ccs.nextElement(), "commclass_id",  commClassId);
				}
				
				// Add communtity interpritation
				Enumeration commIntepretations =  getChildTables(commClass, "commInterpretation");
				while ( commIntepretations.hasMoreElements() )
				{
					Hashtable commIntepretation = (Hashtable) commIntepretations.nextElement();
					addForeignKey(commIntepretation, "COMMCLASS_id", commClassId);
					this.insertCommInterpetation(commIntepretation);
				}
			}
			
			// Insert the taxonObservations
			insertTaxonObservations(
				observationHash,
				observationId,
				stratumMethodId);
				
			
			// Add the observation synonyms 
			Enumeration observationSynonyms =  getChildTables(observationHash, "observationSynonym");
			while ( observationSynonyms.hasMoreElements() )
			{
				Hashtable observationSynonym = (Hashtable) observationSynonyms.nextElement();
				
				// Get synonymobservation_id, party_id, role_id
				Hashtable observation = getFKChildTable(observationSynonym, Observationsynonym.OBSERVATIONSYNONYM_ID, "observation");
				long synonymObservationId = insertObservation(observation);
				
				long partyId = insertParty( getFKChildTable(observationSynonym, Observationsynonym.PARTY_ID, "party") );
				long roleId = 
					insertTable(
						"aux_Role", 
						this.getFKChildTable( observationSynonym, Observationsynonym.ROLE_ID, "aux_Role" )
					);

				addForeignKey(observationSynonym, Observationsynonym.PRIMARYOBSERVATION_ID, observationId);
				addForeignKey(observationSynonym, Observationsynonym.SYNONYMOBSERVATION_ID, synonymObservationId);
				addForeignKey(observationSynonym, Observationsynonym.PARTY_ID, partyId);
				addForeignKey(observationSynonym, Observationsynonym.ROLE_ID, roleId);

				long observationSynonymId = insertTable("observationSynonym", observationSynonym);
				
			}
			
			return observationId;
		}

		private long insertStratumMethod(Hashtable stratumMethod) throws SQLException
		{
			long pKey = 0;
			
			pKey = getExtantPK(stratumMethod);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
			pKey = insertTable("stratumMethod", stratumMethod);
			// And the child stratumTypes
			Enumeration stratumTypes =  getChildTables(stratumMethod, "stratumType");
			while ( stratumTypes.hasMoreElements() )
			{
				Hashtable stratumType = (Hashtable) stratumTypes.nextElement();
				addForeignKey(stratumType, Stratumtype.STRATUMMETHOD_ID, pKey);
				long stratumTypeId = insertTable("stratumType", stratumType);
			}
			
			return pKey;
		}

		/**
		 * Insert a taxonInterpretation 
		 * 
		 * @param observationHash
		 * @param observationId
		 * @param stratumMethodId
		 */
		private void insertTaxonObservations(
			Hashtable observationHash,
			long observationId,
			long stratumMethodId) throws SQLException
		{	
			// Insert the taxonObservations
			Enumeration taxonObservations = getChildTables(observationHash, "taxonObservation");
			while ( taxonObservations.hasMoreElements())
			{
				long taxonObservationId = 0;
				Hashtable taxonObservation = (Hashtable) taxonObservations.nextElement();
			
				taxonObservationId = getExtantPK(taxonObservation);
				if ( taxonObservationId != 0 )
				{
					continue;
				}
				
				// Add PlantName 
				Hashtable plantname = getChildTable(taxonObservation, "Plantname");
				long plantNameId = insertTable("Plantname", plantname);
				
				// Add FKs to taxonObservation
				addForeignKey(taxonObservation, Taxonobservation.OBSERVATION_ID, observationId);
			
				
				taxonObservationId = insertTable("taxonObservation", taxonObservation);
				//LogUtility.log("LoadTreeToDatabase: taxonObservationId: " + taxonObservationId );
			
				//  Add Taxonimportances
				Enumeration taxonImportances = getChildTables(taxonObservation, "taxonImportance");
				while ( taxonImportances.hasMoreElements())
				{
					long taxonImportanceId = 0;
					Hashtable taxonImportance = (Hashtable) taxonImportances.nextElement();
					
					taxonImportanceId = getExtantPK(taxonImportance);
					if ( taxonImportanceId != 0 )
					{
						continue;
					}
					
					// Get the stratum_id if there is one
					Hashtable stratum = getFKChildTable(taxonImportance, Taxonimportance.STRATUM_ID, "stratum");					
					if ( stratum != null )
					{
						long stratumId = insertStratum(stratum, stratumMethodId, observationId);					
						addForeignKey(taxonImportance, Taxonimportance.STRATUM_ID, stratumId);
					}
	
					addForeignKey(taxonImportance, Taxonimportance.TAXONOBSERVATION_ID, taxonObservationId);
					taxonImportanceId = insertTable("taxonImportance", taxonImportance );

					// Add  StemCount
					Enumeration stemCounts = getChildTables(taxonImportance, "stemCount");
					while ( stemCounts.hasMoreElements())
					{
						long stemCountId = 0;
						Hashtable stemCount = (Hashtable) stemCounts.nextElement();
						
						stemCountId = getExtantPK(stemCount);
						if ( stemCountId != 0 )
						{
							continue;
						}
						
						addForeignKey(stemCount, Stemcount.TAXONIMPORTANCE_ID, taxonImportanceId);
						stemCountId = insertTable("stemCount", stemCount);
						
						// Add StemLocation
						Enumeration stemLocations = getChildTables(stemCount, "stemLocation");
						while ( stemLocations.hasMoreElements())
						{
							long stemLocationId = 0;
							Hashtable stemLocation = (Hashtable) stemLocations.nextElement();
							stemLocationId = getExtantPK(stemLocation);
							if ( stemLocationId != 0 )
							{
								continue;
							}
							
							addForeignKey(stemLocation, "STEMCOUNT_ID", stemCountId);
							
							stemLocationId = insertTable("stemLocation", stemLocation);
							
							// Add TaxonInterpretation
							Enumeration taxonInterpretations = getChildTables(taxonObservation, "taxonInterpretation");
							while ( taxonInterpretations.hasMoreElements())
							{
								Hashtable taxonInterpretation = (Hashtable) taxonInterpretations.nextElement();
								
								insertTaxonInterpretation(
									taxonObservationId,
									stemLocationId, 
									taxonInterpretation);
							}	
						}
					}
				}
				
				// Add TaxonInterpretation
				Enumeration taxonInterpretations = getChildTables(taxonObservation, "taxonInterpretation");
				while ( taxonInterpretations.hasMoreElements())
				{
					Hashtable taxonInterpretation = (Hashtable) taxonInterpretations.nextElement();
					insertTaxonInterpretation(
						taxonObservationId,
						0, // No stemlocation availible
						taxonInterpretation);
				}
			}
		}

		private void insertTaxonInterpretation(
			long taxonObservationId,
			long stemLocationId,
			Hashtable taxonInterpretation) throws SQLException
		{
			long pKey = 0;
			pKey = getExtantPK(taxonInterpretation);
			if ( pKey != 0 )
			{
				return;
			}
			
			// Get the party
			Hashtable party =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.PARTY_ID, "party");
			long partyId = insertParty(party);
			
			// Get the Role
			Hashtable role =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.ROLE_ID, "aux_Role");
			long roleId = insertTable("aux_Role", role);
			
			// Get the PlantConcept
			Hashtable plantConcept =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.PLANTCONCEPT_ID, "plantConcept");
			long plantConceptId = this.insertPlantConcept(plantConcept);
			
			// Get the museum party
			Hashtable museumParty =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.MUSEUM_ID, "party");
			long museumId = insertParty(museumParty);
			
			// Get the collector party
			Hashtable collectorParty =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.COLLECTOR_ID, "party");
			long collectorId = insertParty(collectorParty);
			
			// Get the PlantName
			Hashtable plantName =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.PLANTNAME_ID, "plantName");
			long plantNameId = insertTable("plantname", plantName);
			
			
			
			// Add FKs to taxonInterpretation
			addForeignKey(taxonInterpretation, Taxoninterpretation.PLANTCONCEPT_ID, plantConceptId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.ROLE_ID, roleId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.PARTY_ID, partyId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.TAXONOBSERVATION_ID, taxonObservationId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.MUSEUM_ID, museumId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.COLLECTOR_ID, collectorId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.PLANTNAME_ID, plantNameId);
			
			pKey = this.insertTable("taxonInterpretation", taxonInterpretation);
			
			// Add taxonAlt
			Enumeration taxonAlts = getChildTables(taxonInterpretation, "taxonAlt");
			while ( taxonAlts.hasMoreElements())
			{
				Hashtable taxonAlt = (Hashtable) taxonAlts.nextElement();
				addForeignKey(taxonAlt, Taxonalt.TAXONINTERPRETATION_ID, pKey);
				
				Hashtable altPlantConcept = this.getFKChildTable(taxonAlt, Taxonalt.PLANTCONCEPT_ID, "plantConcept");
				long altPlantConceptId = insertPlantConcept(altPlantConcept);
				addForeignKey(taxonAlt, Taxonalt.PLANTCONCEPT_ID, altPlantConceptId);
				
				long taxonAltId = this.insertTable("taxonAlt", taxonAlt);
			}
		}
		
		/**
		 * Insert PlantConcept
		 * 
		 * @param plantConcept
		 * @return long -- the PK of  this row
		 */
		private long insertPlantConcept(Hashtable plantConcept) throws SQLException
		{
			long pKey = 0;
			// Handle null
			if ( plantConcept == null )
			{
				return pKey;			
			}
			
			pKey = getExtantPK(plantConcept);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
			//Utility.prettyPrintHash(plantConcept);
			
			// TODO: depends on PlantName
			Hashtable plantName = this.getFKChildTable(plantConcept, Plantconcept.PLANTNAME_ID, "plantName");
			currentConceptName = (String) plantName.get(Plantname.PLANTNAME);
			long plantNameId = insertTable("plantName", plantName);
			
			addForeignKey(plantConcept, Plantconcept.PLANTNAME_ID, plantNameId);
			pKey = this.insertTable("plantConcept", plantConcept);
			
			// Add PlantStatus 
			Enumeration plantStatuses = getChildTables(plantConcept, "plantStatus");
			while ( plantStatuses.hasMoreElements())
			{
				Hashtable plantStatus = (Hashtable) plantStatuses.nextElement();
				addForeignKey(plantStatus, Plantstatus.PLANTCONCEPT_ID, pKey);
				insertPlantStatus(plantStatus);
			}
			
			// Add plantUsage
			Enumeration plantUsages = getChildTables(plantConcept, "plantUsage");
			while ( plantUsages.hasMoreElements())
			{
				Hashtable plantUsage = (Hashtable) plantUsages.nextElement();
				addForeignKey(plantUsage, Plantusage.PLANTCONCEPT_ID, pKey);
				insertPlantUsage(plantUsage);
			}
			
			return pKey;
		}

		/**
		 * Insert CommConcept
		 * 
		 * @param commConcept
		 * @return long -- the PK of  this row
		 */
		private long insertCommConcept(Hashtable commConcept) throws SQLException
		{
			long pKey = 0;
			// Handle null
			if ( commConcept == null )
			{
				return pKey;			
			}
			
			//Utility.prettyPrintHash(commConcept);
			pKey = getExtantPK(commConcept);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
			//Utility.prettyPrintHash(commConcept);
			
			//  depends on commName
			Hashtable commName = this.getFKChildTable(commConcept, Commconcept.COMMNAME_ID, "commName");
			currentConceptName = (String) commName.get(Commname.COMMNAME);
			long commNameId = insertTable("commName", commName);
			
			addForeignKey(commConcept, Commconcept.COMMNAME_ID, commNameId);
			pKey = this.insertTable("commConcept", commConcept);
			
			// Add commStatus 
			Enumeration commStatuses = getChildTables(commConcept, "commStatus");
			while ( commStatuses.hasMoreElements())
			{
				Hashtable commStatus = (Hashtable) commStatuses.nextElement();
				addForeignKey(commStatus, Commstatus.COMMCONCEPT_ID, pKey);
				insertCommStatus(commStatus);
			}
			
			// Add commUsage
			Enumeration commUsages = getChildTables(commConcept, "commUsage");
			while ( commUsages.hasMoreElements())
			{
				Hashtable commUsage = (Hashtable) commUsages.nextElement();
				addForeignKey(commUsage, Commusage.COMMCONCEPT_ID, pKey);
				insertCommUsage(commUsage);
			}
			
			return pKey;
		}		
		
		/**
		 * @param commStatus
		 * @return long -- primary key assigned
		 */
		private long insertCommStatus(Hashtable commStatus) throws SQLException
		{
			long pKey =0;
		
			// Handle Nulls
			if ( commStatus == null)
			{
				return pKey;
			}
			pKey = getExtantPK(commStatus);
			if ( pKey != 0 )
			{
				return pKey;
			}

			//Utility.prettyPrintHash(commStatus);
		
			// Add commParent
			Hashtable commParent = this.getFKChildTable(commStatus, Commstatus.COMMPARENT_ID, "commConcept");
			long commParentId = insertCommConcept(commParent); // recursive
		
			// Add commParty
			Hashtable commParty = this.getFKChildTable(commStatus, Commstatus.PARTY_ID, "party");
			long commPartyId = insertCommParty(commParty);
			
			addForeignKey(commStatus, Commstatus.COMMPARENT_ID, commParentId);
			addForeignKey(commStatus, Commstatus.PARTY_ID, commPartyId);
			
			pKey = this.insertTable("commStatus", commStatus);
			
			// Add commLineage
			Enumeration commLineages = getChildTables(commStatus, "commLineage");
			while ( commLineages.hasMoreElements())
			{
				Hashtable commLineage = (Hashtable) commLineages.nextElement();
				addForeignKey(commLineage, Commlineage.COMMLINEAGE_ID, pKey);
				insertTable( "commLineage", commLineage);
			}
		
			// Add commCorrelation
			Enumeration commCorrelations = getChildTables(commStatus, "commCorrelation");
			while ( commCorrelations.hasMoreElements())
			{
				Hashtable commCorrelation = (Hashtable) commCorrelations.nextElement();

				// TODO:  Add commConcept
				Hashtable commConcept = this.getFKChildTable(commCorrelation, Commcorrelation.COMMCONCEPT_ID, "commConcept");
				long commConceptId = insertCommConcept(commConcept); 

				addForeignKey(commCorrelation, Commcorrelation.COMMSTATUS_ID, pKey);
				addForeignKey(commCorrelation, Commcorrelation.COMMCONCEPT_ID, commConceptId);
							
				insertTable( "commCorrelation", commCorrelation);
			}
			return pKey;
		}
		
		private void insertCommUsage(Hashtable commUsage) throws SQLException
		{
			long pKey = getExtantPK(commUsage);
			if ( pKey != 0 )
			{
				return;
			}
			
			// Add commName 
			Hashtable commName = this.getFKChildTable(commUsage, Commusage.COMMNAME_ID, "commName");
			long commNameId = insertTable("commName", commName);
			
			// Add commParty
			Hashtable commParty = this.getFKChildTable(commUsage, Commusage.PARTY_ID, "party");
			long commPartyId = insertCommParty(commParty);
			
			addForeignKey(commUsage,  Commusage.COMMNAME_ID, commNameId);
			addForeignKey(commUsage, Commusage.PARTY_ID, commPartyId);
			
			pKey =insertTable( "commUsage", commUsage);
		}
		
		/**
		 * @param commParty
		 * @return
		 */
		private long insertCommParty(Hashtable commParty) throws SQLException
		{
			long pKey = 0;
			if ( commParty == null )
			{
				return pKey;
			}
			pKey = getExtantPK(commParty);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
			
			pKey = this.insertPartyBase(commParty, "commParty");
			
			return pKey;
		}

		private void insertPlantUsage(Hashtable plantUsage) throws SQLException
		{
			long pKey = getExtantPK(plantUsage);
			if ( pKey != 0 )
			{
				return;
			}
			
			// Add plantName 
			Hashtable plantName = this.getFKChildTable(plantUsage, Plantusage.PLANTNAME_ID, "plantName");
			long plantNameId = insertTable("plantName", plantName);
			
			// Add Party
			Hashtable party = this.getFKChildTable(plantUsage, Plantusage.PARTY_ID, "party");
			long partyId = insertParty(party);
			
			addForeignKey(plantUsage, Plantusage.PLANTNAME_ID, plantNameId);
			addForeignKey(plantUsage, Plantusage.PARTY_ID, partyId);
			
			insertTable( "plantUsage", plantUsage);
		}
		
		/**
		 * @param plantStatus
		 * @return long -- primary key assigned
		 */
		private long insertPlantStatus(Hashtable plantStatus) throws SQLException
		{
			long pKey =0;
			
			// Handle Nulls
			if ( plantStatus == null)
			{
				return pKey;
			}

			pKey = getExtantPK(plantStatus);
			if ( pKey != 0 )
			{
				return pKey;
			}
			//Utility.prettyPrintHash(plantStatus);
			
			// Add plantParent
			Hashtable plantParent = this.getFKChildTable(plantStatus, Plantstatus.PLANTPARENT_ID, "plantConcept");
			long plantParentId = insertPlantConcept(plantParent); // recursive
			
			// Add PlantParty
			Hashtable party = this.getFKChildTable(plantStatus, Plantstatus.PARTY_ID, "party");
			long partyId = insertParty(party);
				
			addForeignKey(plantStatus, Plantstatus.PLANTPARENT_ID, plantParentId);
			addForeignKey(plantStatus, Plantstatus.PARTY_ID, partyId);
				
			pKey = this.insertTable("plantStatus", plantStatus);
				
			// Add plantLineage
			Enumeration plantLineages = getChildTables(plantStatus, "plantLineage");
			while ( plantLineages.hasMoreElements())
			{
				// FIXME: DANGER -- handle parent/ child correctly
				Hashtable plantLineage = (Hashtable) plantLineages.nextElement();
				addForeignKey(plantLineage, Plantlineage.PARENTPLANTSTATUS_ID, pKey);
				insertTable( "plantLineage", plantLineage);
			}
			
			// Add plantCorrelation
			Enumeration plantCorrelations = getChildTables(plantStatus, "plantCorrelation");
			while ( plantCorrelations.hasMoreElements())
			{
				Hashtable plantCorrelation = (Hashtable) plantCorrelations.nextElement();

				// TODO:  Add PlantConcept
				Hashtable plantConcept = this.getFKChildTable(plantCorrelation, Plantcorrelation.PLANTCONCEPT_ID, "plantConcept");
				long plantConceptId = insertPlantConcept(plantConcept); 

				addForeignKey(plantCorrelation, Plantcorrelation.PLANTSTATUS_ID, pKey);
				addForeignKey(plantCorrelation, Plantcorrelation.PLANTCONCEPT_ID, plantConceptId);
								
				insertTable( "plantCorrelation", plantCorrelation);
			}
			return pKey;
		}

		/**
		 * Convience method to check keyValue for 0 before adding it
		 * @param table
		 * @param FKName
		 * @param FKValue
		 */
		private void addForeignKey( Hashtable table, String FKName, long FKValue)
		{
			// Anything less than 1 is invalid
			if ( FKValue > 0 )
			{
				table.put(FKName, ""+FKValue);
				LogUtility.log("Set FK: " + FKName + " to: " + FKValue, LogUtility.TRACE);
			}
		}

		/**
		 * @param stratum
		 * @param stratumMethodId
		 * @param observationId
		 * @return
		 */
		private long insertStratum(Hashtable stratum, long stratumMethodId, long observationId) throws SQLException
		{
			long pKey = 0;
			pKey = getExtantPK(stratum);
			if ( pKey != 0 )
			{
				return pKey;
			}
			
			Hashtable stratumType = getFKChildTable(stratum, Stratum.STRATUMTYPE_ID, "stratumType");
			
			addForeignKey(stratumType, Stratumtype.STRATUMMETHOD_ID, stratumMethodId);
			
			long stratumTypeId = insertTable("stratumType", stratumType);
			
			addForeignKey(stratum, Stratum.OBSERVATION_ID, observationId);
			addForeignKey(stratum, Stratum.STRATUMTYPE_ID, stratumTypeId);
			pKey = insertTable("stratum", stratum);
			
			return pKey;
		}

		/**
		 * @param commIntepretation
		 * @param commClassId
		 */
		private long insertCommInterpetation(Hashtable commIntepretation) throws SQLException
		{			
			long pKey = 0;
			// Handle null
			if ( commIntepretation == null)
			{
				return pKey;
			}
			pKey = getExtantPK(commIntepretation);
			if ( pKey != 0 )
			{
				return pKey;
			}
				

			Hashtable commConcept = this.getFKChildTable(commIntepretation, Comminterpretation.COMMCONCEPT_ID, "commConcept");
			long commConceptId = this.insertCommConcept(commConcept);
			addForeignKey(commIntepretation, Comminterpretation.COMMCONCEPT_ID, commConceptId);
			
			Hashtable reference = this.getFKChildTable(commIntepretation, Comminterpretation.COMMAUTHORITY_ID, "reference");
			long commAuthorityId = this.insertReference(reference);
			addForeignKey(commIntepretation, Comminterpretation.COMMAUTHORITY_ID, commAuthorityId);
			
			return insertTable("commInterpretation", commIntepretation);
		}

		/**
		 * Search db for extant  entry
		 * 
		 * @param Hashtable of the entry
		 * @return long of found PK or 0 otherwise
		 */
		private long getExtantPK(Hashtable hashtable)
		{
			long pKey = 0;
			String tableName = (String) hashtable.get(TABLENAME);
			if ( Utility.isStringNullOrEmpty(tableName) )
			{
				return pKey;
			}
			
			pKey = this.getPKFromAccessionCode(hashtable, tableName);
			
			return pKey;
		}
		
		/**
		 * Check rules to see if user is allowed to load this.
		 * 
		 * @param Hashtable of the entry
		 * @return boolean, is allowed to load?
		 */
		private boolean isAllowedToLoad(Hashtable hashtable)
		{
			boolean result = false;
			String tableName = (String) hashtable.get(TABLENAME);
			if ( Utility.isStringNullOrEmpty(tableName) )
			{
				result = false;
			}
			else if ( tableName.equalsIgnoreCase("aux_role"))
			{
				String errorMessage = "You are not allowed to load a new record in the '"
					+ tableName +"' table. \n Please use an existing records AccessionCode to"
					+ " load or have someone with permissions load this record and use the"
					+ " assigned AccessionCode. ";
				LogUtility.log("LoadTreeToDatabase:  : " + errorMessage, LogUtility.ERROR );
				commit = false;
				errors.AddError(
					LoadingErrors.DATABASELOADINGERROR,
					errorMessage); 
			}
			else
			{
				result = true;
			}
			
			LogUtility.log("Check allowed to load " + tableName + ": " + result, LogUtility.TRACE);
			return result;
		}
	}