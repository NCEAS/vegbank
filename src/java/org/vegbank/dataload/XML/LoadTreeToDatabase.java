/*	
 * '$RCSfile: LoadTreeToDatabase.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-08-25 18:44:33 $'
 *	'$Revision: 1.41 $'
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
import java.sql.PreparedStatement;
import java.util.regex.*;
import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.Constants;
import org.vegbank.common.model.Address;
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
import org.vegbank.common.model.Userdatasetitem;
import org.vegbank.common.model.Userdataset;
import org.vegbank.common.model.VBModelBean;
import org.vegbank.common.utility.AccessionGen;
import org.vegbank.common.utility.DenormUtility;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DatasetUtility;
import org.vegbank.common.utility.KeywordGen;
import org.vegbank.common.utility.DataloadLog;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.utility.AccessionCode;
import org.vegbank.common.utility.Timer;
import org.vegbank.plots.datasource.DBModelBeanReader;


/**
 * 
 * @author farrell
 *
 * Takes a Hashtable representation of the XML to be loaded 
 * and writes it to the database.
 */
public class LoadTreeToDatabase
{
	private static Log log = LogFactory.getLog(LoadTreeToDatabase.class);

	public static final String TABLENAME = "TableName";
	public InputPKTracker inputPKTracker = new InputPKTracker();
	private static ResourceBundle vbResources = ResourceBundle.getBundle("vegbank");
	
	// A connection for transactions to be rollback if error and not  
	private DBConnection readConn = null;
	private DBConnection writeConn = null;
	
    private DataloadLog dlog = null;
	private Hashtable revisionsHash = null;
	private Hashtable noteLinksHash = null;
	private Hashtable vegbankPackage = null;
	private LoadingErrors errors = null;
	private List accessionCodesAdded = null;
	private boolean commit = false;
	// Allow no commit for testing
	private boolean doCommit = true;
	private HashMap tableKeys = new HashMap();
  private AccessionGen ag = null;
  //private VBModelBeanToDB bean2db = null;
  private String xmlFileName = null;
  private Long usrId = new Long(0);
  private Timer timer;

	
	// This holds the name of the current concept
	private String currentConceptName = null;
	
	public LoadTreeToDatabase(LoadingErrors errors, List accessionCodes, 
    boolean doCommit, DataloadLog dlog)
	{
		this.doCommit = doCommit;
		this.accessionCodesAdded = accessionCodes;
		this.errors = errors;
        this.dlog = dlog;
	}

	public LoadTreeToDatabase(LoadingErrors errors, DataloadLog dlog)
	{
		this.errors = errors;
        this.dlog = dlog;
	}
	
	/**
	 * Inserts the entire dataset into the database
	 * 
	 * @param vegbankPackage -- the root of the dataset 
	 */
	public void insertVegbankPackage(Hashtable vbPkg, String xmlFileName, Long usrId)
		    throws SQLException
	{
    Timer t1 = new Timer("inserting vegbank package");
		this.vegbankPackage = vbPkg;
		this.xmlFileName = xmlFileName;
		this.usrId = usrId;
		//Utility.prettyPrintHash(vegbankPackage);

        try {
            dlog.append("Beginning VegBank XML dataload");
            //this.bean2db = new VBModelBeanToDB();
        } catch(Exception vbex) {
            log.error("FATAL: problem initializing VBModelBeanToDB: " + vbex.getMessage());
		    errors.addError(LoadingErrors.DATABASELOADINGERROR, vbex.getMessage());
            dlog.append(errors.getTextReport("XML dataload"));

            try {
                dlog.saveFormatted("dataload.log", DataloadLog.TPL_LOG);
            } catch(Exception ioex) {
                log.error("Can't even write dataload.log", ioex);
            }
            return;
        }

    timer = new Timer("initing db");
		//this boolean determines if the dataset should be commited or rolled-back
		commit = true;
		this.initDB();
        dlog.append("preparing data..."); 
    timer.stop();
        
		// insert commConcepts
    timer = new Timer("inserting commConcepts");
		Enumeration commConcepts =  getChildTables(vegbankPackage, "commConcept");
		while ( commConcepts.hasMoreElements() )
		{
			Hashtable commConcept = (Hashtable) commConcepts.nextElement();
			insertCommConcept(commConcept);
		}			
    timer.stop();

		// insert plantConcepts
    timer = new Timer("inserting plantConcepts");
		Enumeration plantConcepts =  getChildTables(vegbankPackage, "plantConcept");
		while ( plantConcepts.hasMoreElements() )
		{
			Hashtable plantConcept = (Hashtable) plantConcepts.nextElement();
			insertPlantConcept(plantConcept);
		}
    timer.stop();

		// insert references
    timer = new Timer("inserting references");
		Enumeration references =
			getChildTables(vegbankPackage, "reference");
		while (references.hasMoreElements())
		{
			Hashtable reference = (Hashtable) references.nextElement();
			this.insertReference(reference);
		}
    timer.stop();

		// insert Parties
    timer = new Timer("inserting parties");
		Enumeration parties = getChildTables(vegbankPackage, "party");
		while (parties.hasMoreElements())
		{
			Hashtable party = (Hashtable) parties.nextElement();
			this.insertParty(party);
		}
    timer.stop();

		// insert observations
    timer = new Timer("inserting observations");
		Enumeration observations = getChildTables(vegbankPackage, "observation");
		while (observations.hasMoreElements())
		{
			Hashtable observation = (Hashtable) observations.nextElement();
			insertObservation(observation);
		}
    timer.stop();

        dlog.append("finished preparing data"); 

		//log.info("commit new data?: " + commit);

        AccessionCode dsAC = null;
        String receiptTpl, subject;
		if (commit == true && doCommit == true) {
            //////////////////////////////////////////////////////////////////
            // COMMIT
            //////////////////////////////////////////////////////////////////
			log.debug("committing xml data to DB");
			dlog.append("committing xml data to DB");
      timer = new Timer("committing data to db");
			writeConn.commit();
      timer.stop();

            try {
                // DATASET CREATION
                timer = new Timer("creating dataset");
                log.debug("====================== DATASET CREATION"); 
                dsAC = createDataset();
                if (dsAC != null) {
                    log.debug("done creating dataset"); 
                    dlog.addTag("datasetURL", vbResources.getString("serverAddress") + 
                            "/get/std/userdataset/" + dsAC.toString());
                } else {
                    dlog.append("!!! empty dataset");
                    log.error("empty dataset");
                }
                timer.stop();
            } catch (Exception ex) {
                log.error("problem creating dataset or dataset items", ex);
                errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                        "Problem creating dataset: " + ex.toString());
                timer.stop();
            }


            try {
                // ACCESSION CODE GENERATION
                timer = new Timer("create accession codes");
                log.debug("====================== ACCESSION GEN"); 
                List newAccessionCodes = this.addAllAccessionCodes();
                log.debug("========= DONE adding ACs");
                accessionCodesAdded.addAll(newAccessionCodes);
                writeConn.commit();
                timer.stop();
            } catch (Exception ex) {
                log.error("Problem while generating accession codes", ex);
                errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                        "Problem generating accession codes: " + ex.toString());
                timer.stop();
            }


            try {
                log.debug("====================== DENORMS");
                timer = new Timer("denormalizing sql");
                // RUN DENORMALIZATION SQL
                log.info("Running denormalizations");
                runDenorms();
                //instead of running the denorm directly, start a thread
                //to run them
                /*java.util.Timer denormTimer = new java.util.Timer();
                denormTimer.schedule(
                  new RunDenormTimerTask(System.currentTimeMillis()),
                  new Date(System.currentTimeMillis()));*/

                ////writeConn.commit();
                timer.stop();
            } catch (Exception ex) {
                log.error("Problem while running denormalizations", ex);
                errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                        "Problem while running denormalizations: " + ex.toString());
                timer.stop();
            }


            try {
                // KEYWORD GENERATION
                timer = new Timer("generate keywords");
                java.util.Timer keywordsTimer = new java.util.Timer();
                keywordsTimer.schedule(
                  new RunKeywordsTimerTask(System.currentTimeMillis()),
                  new Date(System.currentTimeMillis()));
                /*log.debug("====================== KEYWORD GEN");
                dlog.append("====================== KEYWORD GEN");
                KeywordGen kwGen = new KeywordGen(writeConn.getConnections());
                Iterator tit = tableKeys.keySet().iterator();
                int i = 0;
                while (tit.hasNext()) {
                    String tableName = ((String)tit.next()).toLowerCase();
                    kwGen.updatePartialEntityByTable(tableName);
                }*/
                timer.stop();
            } /*catch (SQLException kwex) {
                log.error("problem inserting new keywords", kwex);
                errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                        "Problem generating keywords: " + kwex.toString());
                timer.stop();
            } */catch (Exception ex) {
                log.error("Some lame problem inserting new keywords", ex);
                errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                        "Problem generating keywords: " + ex.toString());
                timer.stop();
            }
                //log.error("\n\nREMEMBER TO GEN KEYWORDS!\n\n");

             //set off a thread that caches the XML generated from the beans
            java.util.Timer xmlGenTimer = new java.util.Timer();
            Iterator accCodes = this.addAllAccessionCodes().iterator();
            RunXMLGenTimerTask task = new RunXMLGenTimerTask(System.currentTimeMillis());
            task.setAccessionCode(accCodes);
            xmlGenTimer.schedule(task, new Date(System.currentTimeMillis()));
             
            // this is still considered a successful load, despite potential errors
            receiptTpl = DataloadLog.TPL_SUCCESS;
            subject = vbResources.getString("dataload.subject.success");

		} else {
            //////////////////////////////////////////////////////////////////
            // ROLLBACK
            //////////////////////////////////////////////////////////////////
			writeConn.rollback();
            receiptTpl = DataloadLog.TPL_FAILURE;
            subject = vbResources.getString("dataload.subject.failure");
		}

        dlog.addTag("fileName", xmlFileName);

        try { 
            if (errors.hasErrors()) {
                dlog.append(errors.getTextReport("XML dataload"));
            }

            // send message to user, assuming email has been preset
            log.debug("sending message to user");
            dlog.send(subject, null, Utility.VB_EMAIL_ADMIN_FROM, receiptTpl);

            // send log to admin
            log.debug("sending message to admin");
            dlog.send(vbResources.getString("dataload.subject.admin"), Utility.VB_EMAIL_ADMIN_TO, 
                    Utility.VB_EMAIL_ADMIN_FROM, receiptTpl);
        } catch (Exception ex) {
            log.error("problem sending dataload receipt via email: ", ex);
        }

        try {
            // write log to disk
            log.debug("writing dataload.log...");
            dlog.saveFormatted("dataload.log", DataloadLog.TPL_LOG);
        } catch (IOException ioex) {
            log.error("problem writing dataload log to dataload.log: ", ioex);
        }
        
    t1.stop();
	}

	private void initDB() throws SQLException
	{
		//	Get DBConnections
		writeConn=DBConnectionPool.getInstance().getDBConnection("Need " +
      "connection for inserting dataset");
		writeConn.setAutoCommit(false);
		
		readConn=DBConnectionPool.getInstance().getDBConnection("Need read " +
      "connection to support inserting dataset");
		readConn.setReadOnly(true);

		// Initialize the AccessionGen
		ag = new AccessionGen(this.writeConn, Utility.getAccessionPrefix() );
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
		StringBuffer sb = new StringBuffer();
		long PK = 0;
		try 
		{
			sb.append(
				"SELECT " + Utility.getPKNameFromTableName(tableName) +" from "+
        tableName+" where " + Constants.ACCESSIONCODENAME + " = '" + 
        accessionCode + "'"
			);

			Statement query = readConn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			while (rs.next()) 
			{
				PK = rs.getInt(1);
			}
			rs.close();
		}
		catch ( SQLException se ) 
		{ 
			this.filterSQLException(se, sb.toString());     
		}
		//log.debug("Query: '" + sb.toString() + "' got PK = " + PK);
		return PK;
	}
	
    /**
     * Get the whether or not a preassigned PK was really assigned beforehand.
     * 
     * @param accessionCode
     * @return boolean -- true if the accessionCode passed is found in the queue of preassigned accession codes
     */
    private boolean checkPreassignedAccCode( String accessionCode )
    {
        StringBuffer sb = new StringBuffer();
        boolean preAssignFound = false;
        try 
        {
            sb.append(
                "SELECT 1 from dba_preassignacccode where " + Constants.ACCESSIONCODENAME + " = '" + accessionCode + "'"
            );

            Statement query = readConn.createStatement();
            ResultSet rs = query.executeQuery(sb.toString());
            while (rs.next()) 
            {
                // PK = rs.getInt(1);
                preAssignFound = true;
            }
            rs.close();
        }
        catch ( SQLException se ) 
        { 
            this.filterSQLException(se, sb.toString());     
        }
        // log.debug("Pre-assigned AccCode Query: '" + sb.toString() + "' wondered if it was found: " + preAssignFound);
        return preAssignFound;
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

				log.error("problematic sql: '" + sql + "'");
				log.error(se);
				commit = false;
				errors.addError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
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
		//log.debug("isPKFieldOfInput tested :" + field
		//		+ " and decided it is the PK of " + tableName + " : " + result);
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
		sb.append("SELECT nextval('" + tableName + "_" + 
      Utility.getPKNameFromTableName(tableName) + "_seq')");
		try 
		{
		   Statement query = writeConn.createStatement();
		   ResultSet rs = query.executeQuery(sb.toString());
		   while (rs.next()) 
		   {
				   PK = rs.getLong(1);
		   }
		   rs.close();
		   //log.debug("Get next PK: SQL: " + sb.toString() + " returned PK =" + PK);
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
	private boolean insertTables( String tableName, Enumeration tables, 
    String fKName, long fKValue ) throws SQLException
	{
		boolean result = true;
		
		while( tables.hasMoreElements()  )
		{
			Hashtable table = (Hashtable) tables.nextElement();
			//log.debug(tableName + " : " + table);
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
	private long insertTable( String tableName, Hashtable fieldValueHash ) 
    throws SQLException
	{
		//log.debug("insertTable: " + tableName);
		
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
			//log.debug("*** User is not allowed to load " + 
      //  fieldValueHash.get(TABLENAME));
			return PK; // Even if empty
		}
		
		// Insert Reference if it exists
		long referenceId = 0;
		Hashtable reference = getFKChildTable(fieldValueHash, "reference_ID", 
      "reference");
		if ( reference != null)
		{
			//log.debug("Inserting extant reference");
			referenceId = insertReference(reference);
		}
		addForeignKey(fieldValueHash, "reference_ID", referenceId);
		
		// Maybe need a method like this to support update extant data
		// Check if this already exists in the database
//			if ( this.tableExists( tableName, fieldValueHash) )
//			{
//				return this.getTablePK(tableName, fieldValueHash);
//			}
		
		// this creates too many skipped rows, do it later:   
        //   PK = this.getNextId(tableName);
		
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
				
				//log.debug("Handle fieldName " + field + " with value " + value);
				
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
						long storedPK = inputPKTracker.getAssignedPK(tableName, 
              stringValue.toString());
						if (storedPK != 0) 
						{
							//log.debug("Already entered record xmlPK "
							//		+ stringValue.toString() + " into " + tableName + " using PK of "
							//		+ storedPK);
							return storedPK;
						}
						// Need to add this to the datastruture that prevents
						// the adding duplicate fields
                        // now get the next value, since we are adding it for sure:
                        PK = this.getNextId(tableName);
                        
						inputPKTracker.setAssignedPK(tableName, stringValue.toString(), PK);
						//log.debug("No record added for xmlPK :" + stringValue.toString()
						//		+ " for table " + tableName + " adding PK now: " + PK);
					}
				}
			}
            
			// if for some reason the PK wasn't in the list, we'll get the value now:
            if (PK == 0) {
                
                PK = this.getNextId(tableName);
                log.debug("No PK was previously found! so I got " + PK);
            }
            
			fieldNames.add( Utility.getPKNameFromTableName(tableName) );
			fieldValues.add("" + PK);

			sb.append("INSERT INTO " + tableName );
			sb.append(" (" + Utility.arrayToCommaSeparatedString( fieldNames.toArray() ) + ")" );
			sb.append(" VALUES (" + Utility.arrayToCommaSeparatedString( fieldValues.toArray() ) +")" );
			
			//log.info("Running SQL: " + sb.toString());
			if (tableName.equals("plot")) {
				log.info("loaded table: " +tableName + " with PK of " + PK);
			}

            // update the dataload log's insertion count for this table
            //if (Utility.canAddToDatasetOnLoad(tableName)) {
                dlog.increment("entityCounts", tableName);
            //}

			Statement query = writeConn.createStatement();
			int rowCount = query.executeUpdate(sb.toString());
		}
		catch ( SQLException se ) 
		{
      log.error("sql exception while inserting: " + se.toString() + 
       "\n:::" + sb.toString());
			this.filterSQLException(se, sb.toString());
		}
		
        if (!tableName.equalsIgnoreCase("userdefined") &&
                !tableName.equalsIgnoreCase("definedvalue")) {
            // All the tables can have defined value field
            insertUserDefinedTables(tableName, PK, fieldValueHash);
            // All fields can have a revision
            insertRevision(PK, fieldValueHash);
            // All fields can have a note attached
            insertNoteLink(PK, fieldValueHash);
        }
		
		this.storeTableNameAndPK(tableName, PK);
		
		//log.debug("Returning with PK :" + PK + " for table " + tableName);
		return PK;
	}

	/**
	 * Remove accessionCode field if it exists
	 * 
	 * @param fieldValueHash
	 */
	private void removeAccessionCode(Hashtable fieldValueHash)
	{ 
          // before removing it, check to see if it is preassigned
          
             // we should try and see if this is a pre-assigned accessionCode from a client (that'd be VegBranch)
             // these are stored in the dba_preassignacccode table
             // the prefix is defined in the build.properties file
            String accessionCode = "";
            accessionCode = (String) fieldValueHash.get(Constants.ACCESSIONCODENAME);
            if ( Utility.isStringNullOrEmpty( accessionCode )) {
                // don't bother doing anything, there is no accessionCode to remove!
                //log.debug("There was no accession code");
            } else {
                //log.debug("There was an accession code and it happens to be: " + accessionCode);
                boolean skipRemoval = false;
                if (accessionCode.startsWith(Utility.DATABASE_ACCESSION_KEY_PREASSIGN + ".")) {
                   // only bother to check this if the prefix is right, this saves lots of time, as vegbranch temp codes are never checked
                   //log.debug(accessionCode + " is a preassigned candidate...");
                   skipRemoval = ( checkPreassignedAccCode( accessionCode) ) ;
                   //if (skipRemoval) {
                   //    log.debug(accessionCode + " was indeed preassigned.");
                   //}
                }
                if (!skipRemoval) {
                  //log.debug("removing accessionCode " + accessionCode + " from the record.");  
		    	  fieldValueHash.remove(Constants.ACCESSIONCODENAME);
                }
            }
	}

	/**
	 * Store the tableName and the PK so an accessionCode can be generated later
	 * 
	 * @param tableName
	 * @param PK
	 */
	private void storeTableNameAndPK(String tableName, long PK)
	{	
        Vector keys = (Vector) tableKeys.get(tableName.toLowerCase());
        if ( keys == null ) {
            keys = new Vector();
            tableKeys.put(tableName, keys);
        }
        keys.add(new Long(PK));
	}
	
	/**
	 * Add accessionCodes to all the rows added by this loading.
	 */
	private List addAllAccessionCodes() throws SQLException
	{
		List accessionCodes = null;
		
		if ( Utility.isLoadAccessionCodeOn() ) {
            log.debug("Updating accession codes for given PKs");
			accessionCodes = ag.updateSpecificRows(tableKeys);
		} else {
            log.debug("Accession code updates DISABLED");
			accessionCodes = new ArrayList();
		}
		return accessionCodes;
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
		
		if ( Utility.isStringNullOrEmpty( accessionCode )) {
			//log.debug("Found no accessionCode for " + tableName);
			// do nothing
		} else {
			// Need to get the pK of the table
			PK = this.getTablePK(tableName, accessionCode);
			
			if ( PK != 0 ) {
				// great got a real PK
				//log.debug("Found PK ("+PK+") for "+tableName+" accessionCode: "+ accessionCode);
			} else {

				/*
                // problem no accessionCode like that in database 
				StringBuffer emSb = new StringBuffer(256)
					.append("Can't find ")
					.append(tableName)
					.append(" with accessionCode ")
					.append(accessionCode);
						
				String errorMessage = emSb.toString();
				log.error(": " + errorMessage);
				//commit = false;
				errors.addError(
					LoadingErrors.DATABASELOADINGERROR,
					errorMessage);
				*/
			}
		}
//			else
//			{
//				log.debug(
//					"Got an accessionCode in table "
//						+ tableName
//						+ " --> "
//						+ accessionCode);
//						
//				// Remove from hash
//				fieldValueHash.remove(Constants.ACCESSIONCODENAME);
//			}
		
		//log.debug("Using accessionCode " + accessionCode + " in table "
		//			+ tableName + " got DB PK of " + PK);
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
				errors.addError(LoadingErrors.VALIDATIONERROR, 
          "Mismatch on NOTELINK_ID = " + noteLinkId);
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
				errors.addError(LoadingErrors.VALIDATIONERROR, 
          "Mismatch on REVISION_ID = " + revisionId);
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
		Hashtable referenceJournal = getFKChildTable(reference, 
      Reference.REFERENCEJOURNAL_ID, "referenceJournal");
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
			
			Hashtable referenceParty = getFKChildTable(referenceContributor, 
        Referencecontributor.REFERENCEPARTY_ID, "referenceParty");
			// Add  referenceParty
			long referencePartyId = insertReferenceParty(referenceParty);
			
			addForeignKey(referenceContributor, Referencecontributor.REFERENCE_ID, 
        referenceId);
			addForeignKey(referenceContributor, Referencecontributor.REFERENCEPARTY_ID, 
        referencePartyId);
			long referenceContributorId = insertTable("referenceContributor", 
        referenceContributor);
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
			
			Hashtable referencePartyParent = getFKChildTable(referenceParty, 
        Referenceparty.CURRENTPARTY_ID, "referenceParty");
			long referencePartyParentId = insertReferenceParty(referencePartyParent);
			
			addForeignKey(referenceParty, Referenceparty.CURRENTPARTY_ID, 
        referencePartyParentId);
			referencePartyId = insertTable( "referenceParty", referenceParty);
		}
		return referencePartyId;
	}

	private boolean insertUserDefinedTables( String parentTableName, 
    long tableRecordID, Hashtable parentHash) throws SQLException
	{
		boolean result = false;
		Enumeration definedValues = getChildTables( parentHash, "definedValue");

		while ( definedValues.hasMoreElements() ) {
            Hashtable definedValueHash = (Hashtable)definedValues.nextElement();
            log.debug("got a definedValue for " + parentTableName + ": " + 
              definedValueHash.get("definedValue"));


            // the actual <userDefined> is under a <definedValue.USERDEFINED_ID>
            Hashtable udIdHash = getChildTable(definedValueHash, "USERDEFINED_ID");
            if (udIdHash != null) {
                Hashtable userDefined = getChildTable(udIdHash, "userDefined");
                log.debug("got the userDefined hashtable.");

                log.debug("inserting userDefined element...");
                long userDefinedPK = insertTable("userDefined", userDefined);
                definedValueHash.put("tablerecord_id", ""+tableRecordID);
                definedValueHash.put("userdefined_id", ""+userDefinedPK);
                log.debug("userdefined_id=" + userDefinedPK);
                log.debug("inserting definedValue element...");
                insertTable( "definedValue", definedValueHash );
            }
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
				//log.debug("===" + field);
				Object value = fieldValueHash.get(field);
				if ( this.isDatabaseReadyField(field, value, tableName) )
				{				
					fieldEqualsValue.add(field + "=" + "'" + 
            Utility.encodeForDB(value.toString()) + "'");
				}
			}
			
			// TODO: All fields that have no value should be placed in the search 
			
			sb.append(
				"SELECT count(*) from "+tableName+" where " 
				+ Utility.joinArray(fieldEqualsValue.toArray(), " and ")
			);

			sb.append(this.getSQLNullValues(tableName, fieldValueHash));
			
			Statement query = readConn.createStatement();
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
			//log.debug(tableName+" does not exist");
			return (false);
		} 
		else 
		{
			//log.debug(tableName+" does exist");
			return (true);
		}
	}
	
	/**
	 * Method that returns the pk value if a record with these values exists in the DB.
	 */ 
	private long findDuplicateRecord( String tableName, Hashtable fieldValueHash)
	{
		long PK = 0;
		
		Vector fieldEqualsValue = new Vector();
		String strQuery = "";
		
		try 
		{		
			Enumeration fields = fieldValueHash.keys();
			while ( fields.hasMoreElements())
			{					
				String field = (String) fields.nextElement();
				//log.debug("===" + field);
				Object value = fieldValueHash.get(field);
				if ( this.isDatabaseReadyField(field, value, tableName) )
				{				
					fieldEqualsValue.add(field + "=" + "'" + 
            Utility.encodeForDB(value.toString()) + "'");
				}
			}
			
			
			strQuery = "SELECT " + Utility.getPKNameFromTableName(tableName) +
                " from "+tableName+" where " 
				+ Utility.joinArray(fieldEqualsValue.toArray(), " and ");

			//strQuery += this.getSQLNullValues(tableName, fieldValueHash);
            //log.debug("searching for extant record: " + strQuery);
			
			Statement query = readConn.createStatement();
			ResultSet rs = query.executeQuery(strQuery);
			while (rs.next()) {
				PK = rs.getInt(1);
			}
		} catch ( SQLException se ) {      
			this.filterSQLException(se, strQuery);
		}
			
		if (PK == 0) {
			//log.debug(tableName+" does not exist");
		} else {
			//log.debug(tableName+" does exist");
		}
		return PK;
	}
	
	
	private String getSQLNullValues( String tableName, Hashtable fieldValuesHash ) 
    throws SQLException
	{
		StringBuffer sb = new StringBuffer();
		
		ResultSet rs = readConn.getMetaData().getColumns(null, null, 
      tableName.toLowerCase(), "%" );
		
		//log.debug("Got a Result Set");
		while ( rs.next() )
		{
			// according to api value 4 is the columnName
			String columnName = rs.getString(4);
			//log.debug("ColumnName> " + columnName + " in table > " + tableName);
			
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
	private Hashtable getFKChildTable( Hashtable parentHash, String keyName ,
    String tableName)
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
			log.warn("Got asked to find: '" + tableName + 
        "' from a null Hashtable. This shouldn't happen but hey, what do I know?");
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
			//log.debug("Could not find table.. " + tableName + 
      //  " as child of  table " + parentHash.get("TableName"));
		}
		else
		{
			// Don't know what to do here
			log.warn("Type: '" + o.getClass() + "' should not exist here in" + 
        tableName + "?.");
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
                /*
                if ("USERDEFINED_ID".equals(tableName)) {
                    if (parentHash.size() > 0) {
                        Iterator kit = parentHash.keySet().iterator();
                        while (kit.hasNext()) {
                            log.debug("AVAILABLE KEYS: " + (String)kit.next());
                        }
                    }
                }
                */
			} 
			// Need to handle potential null before casting
			childVec = (Vector) childObject;
		}

		Enumeration enum = childVec.elements();
        /*
        if ("USERDEFINED_ID".equals(tableName)) {
            log.debug("##############: number children for " +
              "definedValue.USERDEFINED_ID: " + childVec.size());
            while(enum.hasMoreElements()) {
                log.debug("child: " + enum.nextElement().toString());
            }
        }
        */

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
		//log.debug("### " +party);
		
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
     * PlantParty and CommParty are obsolete.
	 * This method does shared functions need for insertion.
	 * 
	 * @param party
	 * @return
	 */
	private long insertPartyBase(Hashtable party, String tableName) 
    throws SQLException
	{
		long pKey = 0;
		
		pKey = getExtantPK(party);
		if ( pKey != 0 )
		{
			return pKey;
		}
		
		// recursivly deal with FK party's
		Hashtable ownerParty = getFKChildTable(party, "owner_ID", "party");
		Hashtable currentNameParty = getFKChildTable(party, "currentName_ID", 
      "party");
		
		long ownerPartyPK = 0;
		long currentNamePartyPK = 0;
		
		if (ownerParty != null)
		{
			//log.debug(">>>>" + ownerParty);			
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
	
	private long insertContributor(String tableName, Hashtable contribHash, 
    String keyName, long keyValue) throws SQLException
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
		Hashtable roleIDHash = this.getChildTable(contribHash, "ROLE_ID");
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
			Hashtable namedPlace =   this.getFKChildTable(place, Place.NAMEDPLACE_ID, 
        "namedPlace");
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
			this.insertContributor( "projectContributor", 
        (Hashtable) pcs.nextElement(), Projectcontributor.PROJECT_ID,  projectId);
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
		Hashtable project = this.getFKChildTable(observationHash, 
      Observation.PROJECT_ID, "project");
		long projectId = this.insertProject(project);
		
		Hashtable plot = getFKChildTable(observationHash, Observation.PLOT_ID, "plot");
		long plotId = insertPlot(plot);

		// Continue loading as normal
		
		Hashtable previousObs = getFKChildTable(observationHash, 
      Observation.PREVIOUSOBS_ID, "observation");
		
		long previousObsId = 0;
		if (previousObs != null)
		{
			previousObsId = insertObservation(previousObs);
		}
    
		addForeignKey(observationHash, Observation.PREVIOUSOBS_ID, previousObsId);
		addForeignKey(observationHash, Observation.PLOT_ID, plotId);
		addForeignKey(observationHash, Observation.PROJECT_ID, projectId);
		
		// CoverMethod
		Hashtable coverMethod = getFKChildTable(observationHash, 
      Observation.COVERMETHOD_ID, "coverMethod");
		long coverMethodId = insertTable("coverMethod", coverMethod);
		this.insertTables("coverIndex",  getChildTables(coverMethod, "coverIndex"), 
      Observation.COVERMETHOD_ID, coverMethodId);
		addForeignKey(observationHash, Observation.COVERMETHOD_ID, coverMethodId);

		// StratumMethod			
		Hashtable stratumMethod = getFKChildTable(observationHash, 
      Observation.STRATUMMETHOD_ID, "stratumMethod");
		long stratumMethodId = insertStratumMethod(stratumMethod);
		addForeignKey(observationHash, Observation.STRATUMMETHOD_ID, stratumMethodId);

		// SoilTaxon
		Hashtable soilTaxon = getFKChildTable(observationHash, 
      Observation.SOILTAXON_ID, "soilTaxon");
		long soilTaxonId = insertTable("soilTaxon", soilTaxon);
		addForeignKey(observationHash, Observation.SOILTAXON_ID, soilTaxonId);
    
		// Insert the observation
		observationId = insertTable("observation", observationHash);
		
		// TODO: insert graphic, observationSynonym
		//this.insertTables("graphic",  getChildTables(observationHash, "graphic"), 
    //  "observation_id", observationId);
		
		// Add disturbanceObs
		this.insertTables("disturbanceObs",  getChildTables(observationHash, 
      "disturbanceObs"), Disturbanceobs.OBSERVATION_ID, observationId);
		
		// Add SoilObs
		this.insertTables("soilObs",  getChildTables(observationHash, "soilObs"), 
      Soilobs.OBSERVATION_ID, observationId);
		
		// Add observation contributors
		Enumeration ocs = this.getChildTables( observationHash, "observationContributor");
		while (ocs.hasMoreElements())
		{	
			this.insertContributor( "observationContributor", 
        (Hashtable) ocs.nextElement(), Observationcontributor.OBSERVATION_ID,  
        observationId);
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
            //log.debug("added commClass #" + commClassId);
			
			// Add Classification Contributors
			Enumeration ccs = this.getChildTables( commClass, "classContributor");
			while (ccs.hasMoreElements())
			{	
				this.insertContributor( "classContributor", 
          (Hashtable) ccs.nextElement(), "commclass_id",  commClassId);
			}
			
			// Add communtity interpretation
			Enumeration commIntepretations =  getChildTables(commClass, 
        "commInterpretation");
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
		Enumeration observationSynonyms =  getChildTables(observationHash, 
      "observationSynonym");

		while ( observationSynonyms.hasMoreElements() )
		{
      
			Hashtable observationSynonym = (Hashtable) observationSynonyms.nextElement();
			
			// Get synonymobservation_id, party_id, role_id
			Hashtable observation = getFKChildTable(observationSynonym, 
        Observationsynonym.OBSERVATIONSYNONYM_ID, "observation");
			long synonymObservationId = insertObservation(observation);
			
			long partyId = insertParty( getFKChildTable(observationSynonym, 
        Observationsynonym.PARTY_ID, "party") );
			long roleId = 
				insertTable(
					"aux_Role", 
					this.getFKChildTable( observationSynonym, Observationsynonym.ROLE_ID, 
            "aux_Role" )
				);

			addForeignKey(observationSynonym, Observationsynonym.PRIMARYOBSERVATION_ID, 
        observationId);
			addForeignKey(observationSynonym, Observationsynonym.SYNONYMOBSERVATION_ID, 
        synonymObservationId);
			addForeignKey(observationSynonym, Observationsynonym.PARTY_ID, partyId);
			addForeignKey(observationSynonym, Observationsynonym.ROLE_ID, roleId);

			long observationSynonymId = insertTable("observationSynonym", 
        observationSynonym);
			
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
			///////// OLD WAY 27 jan 2005
            //long stratumTypeId = insertTable("stratumType", stratumType);
            //////////////////////////////////////////////
			long stratumTypeId = insertStratumType(stratumType);
		}
		
		return pKey;
	}

    /**
     * Checks for duplicates in the given stratumMethod.
     */
	private long insertStratumType(Hashtable stratumType) throws SQLException
	{

		long pKey = 0;
		
		pKey = getExtantPK(stratumType);
		if ( pKey != 0 )
		{
			return pKey;
		}
		
        //log.debug("**** attempting to insert a stratumType: ");
        //Utility.prettyPrintHash(stratumType);
        Hashtable dup = (Hashtable)stratumType.clone();
        String pk = (String)dup.remove(Stratumtype.PKNAME);
        String name = (String)dup.remove(Stratumtype.STRATUMNAME);
        String desc = (String)dup.remove(Stratumtype.STRATUMDESCRIPTION);

	    pKey = findDuplicateRecord("stratumType", dup);
	    if (pKey == 0) {
		    pKey = insertTable("stratumType", stratumType);
        }

		
		return pKey;
	}

	/**
	 * Insert a taxonObservation 
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
    Timer obsTimer;
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
			//log.debug("taxonObservationId: " + taxonObservationId );
		
			//  Add Taxonimportances
			Enumeration taxonImportances = getChildTables(taxonObservation, 
        "taxonImportance");
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
				Hashtable stratum = getFKChildTable(taxonImportance, 
          Taxonimportance.STRATUM_ID, "stratum");					
				if ( stratum != null )
				{
					long stratumId = insertStratum(stratum, stratumMethodId, observationId);					
					addForeignKey(taxonImportance, Taxonimportance.STRATUM_ID, stratumId);
				}
      
				addForeignKey(taxonImportance, Taxonimportance.TAXONOBSERVATION_ID, 
          taxonObservationId);
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
						Enumeration taxonInterpretations = getChildTables(taxonObservation, 
              "taxonInterpretation");
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
			Enumeration taxonInterpretations = getChildTables(taxonObservation, 
        "taxonInterpretation");
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

  /**
   * add a taxon interpretation
   */
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
		Hashtable party =  this.getFKChildTable(taxonInterpretation, 
      Taxoninterpretation.PARTY_ID, "party");
		long partyId = insertParty(party);
		
		// Get the Role
		Hashtable role =  this.getFKChildTable(taxonInterpretation, 
      Taxoninterpretation.ROLE_ID, "aux_Role");
		long roleId = insertTable("aux_Role", role);
		
		// Get the PlantConcept
		Hashtable plantConcept =  this.getFKChildTable(taxonInterpretation, 
      Taxoninterpretation.PLANTCONCEPT_ID, "plantConcept");
		long plantConceptId = this.insertPlantConcept(plantConcept);
		
		// Get the museum party
		Hashtable museumParty =  this.getFKChildTable(taxonInterpretation, 
      Taxoninterpretation.MUSEUM_ID, "party");
		long museumId = insertParty(museumParty);
		
		// Get the collector party
		Hashtable collectorParty =  this.getFKChildTable(taxonInterpretation, 
      Taxoninterpretation.COLLECTOR_ID, "party");
		long collectorId = insertParty(collectorParty);
		
		// Get the PlantName
		Hashtable plantName =  this.getFKChildTable(taxonInterpretation, 
      Taxoninterpretation.PLANTNAME_ID, "plantName");
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
			
			Hashtable altPlantConcept = this.getFKChildTable(taxonAlt, 
        Taxonalt.PLANTCONCEPT_ID, "plantConcept");
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
		long plantStatusId = 0;
		long plantUsageId = 0;

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
	    log.debug("########## INSERTING NEW PLANT CONCEPT");
		
	    //log.debug("pc: ");
		//Utility.prettyPrintHash(plantConcept);
		
		// TODO: depends on PlantName
		Hashtable plantName = this.getFKChildTable(plantConcept, 
      Plantconcept.PLANTNAME_ID, "plantName");
		currentConceptName = (String) plantName.get(Plantname.PLANTNAME);
	    log.debug("pc name: " + currentConceptName);
		long plantNameId = insertTable("plantName", plantName);
	    log.debug("plantNameId: " + plantNameId);
		
		addForeignKey(plantConcept, Plantconcept.PLANTNAME_ID, plantNameId);
		pKey = this.insertTable("plantConcept", plantConcept);
	    log.debug("pc_id: " + pKey);
		
		// Add PlantStatus 
	    log.debug("Checking for plantStati");
		Enumeration plantStatuses = getChildTables(plantConcept, "plantStatus");
		while ( plantStatuses.hasMoreElements())
		{
	        log.debug("found a plantstatus");
			Hashtable plantStatus = (Hashtable) plantStatuses.nextElement();
			addForeignKey(plantStatus, Plantstatus.PLANTCONCEPT_ID, pKey);
			plantStatusId = insertPlantStatus(plantStatus);

            // Add plantUsage
            Enumeration plantUsages = getChildTables(plantStatus, "plantUsage");
            while ( plantUsages.hasMoreElements())
            {
                Hashtable plantUsage = (Hashtable) plantUsages.nextElement();
                addForeignKey(plantUsage, Plantusage.PLANTCONCEPT_ID, pKey);

                // need to add pu.plantstatus_id needs to be populated
                addForeignKey(plantUsage, Plantusage.PLANTSTATUS_ID, plantStatusId);

                insertPlantUsage(plantUsage);
                //log.debug("added plantusage");
            }
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
		long commStatusId = 0;
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
		Hashtable commName = this.getFKChildTable(commConcept, 
      Commconcept.COMMNAME_ID, "commName");
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
			commStatusId = insertCommStatus(commStatus);

            // Add commUsage
            Enumeration commUsages = getChildTables(commStatus, "commUsage");
            while ( commUsages.hasMoreElements())
            {
                log.debug("adding commUsage to commStatus #" + commStatusId);
                Hashtable commUsage = (Hashtable) commUsages.nextElement();
                addForeignKey(commUsage, Commusage.COMMCONCEPT_ID, pKey);
                addForeignKey(commUsage, Commusage.COMMSTATUS_ID, commStatusId);
                insertCommUsage(commUsage);
            }
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
		Hashtable commParent = this.getFKChildTable(commStatus, 
      Commstatus.COMMPARENT_ID, "commConcept");
		long commParentId = insertCommConcept(commParent); // recursive
	
		// Add party
		Hashtable party = this.getFKChildTable(commStatus, Commstatus.PARTY_ID, 
      "party");
		long partyId = insertParty(party);
		
		addForeignKey(commStatus, Commstatus.COMMPARENT_ID, commParentId);
		addForeignKey(commStatus, Commstatus.PARTY_ID, partyId);
		
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
			Hashtable commConcept = this.getFKChildTable(commCorrelation, 
        Commcorrelation.COMMCONCEPT_ID, "commConcept");
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
		Hashtable commName = this.getFKChildTable(commUsage, Commusage.COMMNAME_ID, 
      "commName");
		long commNameId = insertTable("commName", commName);
		
		// Add party
		Hashtable party = this.getFKChildTable(commUsage, Commusage.PARTY_ID, 
      "party");
		long partyId = insertParty(party);
		
		addForeignKey(commUsage,  Commusage.COMMNAME_ID, commNameId);
		addForeignKey(commUsage, Commusage.PARTY_ID, partyId);
		
		pKey =insertTable( "commUsage", commUsage);
	}
	
	/**
	 * @param commParty
	 * @return
	 */
    /*
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
    */

	private long insertPlantUsage(Hashtable plantUsage) throws SQLException
	{
		long pKey = getExtantPK(plantUsage);
		if ( pKey != 0 )
		{
			return 0;
		}
		
		// Add plantName 
		Hashtable plantName = this.getFKChildTable(plantUsage, 
      Plantusage.PLANTNAME_ID, "plantName");
		long plantNameId = insertTable("plantName", plantName);
		
		// Add Party
		Hashtable party = this.getFKChildTable(plantUsage, Plantusage.PARTY_ID, 
      "party");
		long partyId = insertParty(party);
		
		addForeignKey(plantUsage, Plantusage.PLANTNAME_ID, plantNameId);
		addForeignKey(plantUsage, Plantusage.PARTY_ID, partyId);
		
		return insertTable( "plantUsage", plantUsage);
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
		Hashtable plantParent = this.getFKChildTable(plantStatus, 
      Plantstatus.PLANTPARENT_ID, "plantConcept");
		long plantParentId = insertPlantConcept(plantParent); // recursive
		
		// Add PlantParty
		Hashtable party = this.getFKChildTable(plantStatus, Plantstatus.PARTY_ID, 
      "party");
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
		Enumeration plantCorrelations = getChildTables(plantStatus, 
      "plantCorrelation");
		while ( plantCorrelations.hasMoreElements())
		{
			Hashtable plantCorrelation = (Hashtable) plantCorrelations.nextElement();

			Hashtable plantConcept = this.getFKChildTable(plantCorrelation, 
        Plantcorrelation.PLANTCONCEPT_ID, "plantConcept");
			long plantConceptId = insertPlantConcept(plantConcept); 

			addForeignKey(plantCorrelation, Plantcorrelation.PLANTSTATUS_ID, pKey);
			addForeignKey(plantCorrelation, Plantcorrelation.PLANTCONCEPT_ID, 
        plantConceptId);
							
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
			//log.debug("Set FK: " + FKName + " to: " + FKValue);
		}
	}

	/**
	 * @param stratum
	 * @param stratumMethodId
	 * @param observationId
	 * @return
	 */
	private long insertStratum(Hashtable stratum, long stratumMethodId, 
    long observationId) throws SQLException
	{
		long pKey = 0;
		pKey = getExtantPK(stratum);
		if ( pKey != 0 )
		{
			return pKey;
		}
		
		Hashtable stratumType = getFKChildTable(stratum, Stratum.STRATUMTYPE_ID, 
      "stratumType");
		
		addForeignKey(stratumType, Stratumtype.STRATUMMETHOD_ID, stratumMethodId);
		
		long stratumTypeId = insertStratumType(stratumType);
		
		addForeignKey(stratum, Stratum.OBSERVATION_ID, observationId);
		addForeignKey(stratum, Stratum.STRATUMTYPE_ID, stratumTypeId);
		pKey = insertTable("stratum", stratum);
		
		return pKey;
	}

	/**
	 * @param commIntepretation
	 * @param commClassId
	 */
	private long insertCommInterpetation(Hashtable commIntepretation) 
    throws SQLException
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
			

		Hashtable commConcept = this.getFKChildTable(commIntepretation, 
      Comminterpretation.COMMCONCEPT_ID, "commConcept");
		long commConceptId = this.insertCommConcept(commConcept);
		addForeignKey(commIntepretation, Comminterpretation.COMMCONCEPT_ID, 
      commConceptId);
		
		Hashtable reference = this.getFKChildTable(commIntepretation, 
      Comminterpretation.COMMAUTHORITY_ID, "reference");
		long commAuthorityId = this.insertReference(reference);
		addForeignKey(commIntepretation, Comminterpretation.COMMAUTHORITY_ID, 
      commAuthorityId);
		
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
				+ tableName +"' table. + [ table: " + tableName + " AcessionCode in XML: " +  
          hashtable.get(Constants.ACCESSIONCODENAME)  
				+ "] \n Please use an existing records AccessionCode to"
				+ " load or have someone with permissions load this record and use the"
				+ " assigned AccessionCode. ";
			log.error(": " + errorMessage);
			commit = false;
			errors.addError(
				LoadingErrors.DATABASELOADINGERROR,
				errorMessage); 
		}
		else
		{
			result = true;
		}
		
		//log.debug("Check allowed to load " + tableName + ": " + result);
		return result;
	}


    /**
     * Populate a Userdatasetitem as much as possible.
     */
    private Userdatasetitem createDatasetItem(String tableName, long PK) 
      throws SQLException {
        AccessionCode dsiAC = new AccessionCode(ag.getAccession(tableName, PK));
        return DatasetUtility.createDatasetItem(dsiAC, null);

        /*
        Userdatasetitem dsiBean = new Userdatasetitem();
        dsiBean.setItemaccessioncode(dsiAC.toString());
        dsiBean.setUserdatasetitem_id(-1); // don't check for duplicates
        dsiBean.setItemtype(tableName);
        dsiBean.setItemdatabase(dsiAC.getDatabaseId());
        dsiBean.setItemtable(dsiAC.getEntityName());
        dsiBean.setItemrecord(dsiAC.getEntityId().toString());

        log.debug("+++ ADDING userdatasetitem for " + tableName + ": " + 
          dsiBean.getItemaccessioncode());
        return dsiBean;
        */
    }

    /**
     * Uses DatasetUtility to creates and insert a userdataset 
     * along with all of its userdatasetitem children records.
     * @return new userdataset's accession code
     */
    private AccessionCode createDataset() throws SQLException, Exception {

        // create a userdatasetitem for each appropriate record
        String tableName;
        Userdatasetitem dsiBean = null;
        List dsiBeanList = new ArrayList();
        Iterator it = tableKeys.keySet().iterator();

        // add the children as Userdatasetitem instances
        while (it.hasNext()) {
            tableName = (String)it.next();
    
            // Is this a supported table?
            if (Utility.canAddToDatasetOnLoad(tableName) ) {
                //log.debug("+++ adding dsi: " + tableName);
                Vector keys = (Vector)tableKeys.get(tableName);
                Iterator kit = keys.iterator();
                while (kit.hasNext()) {
                    try {
                        Long itemId = (Long)kit.next();
                        //log.debug("adding DSI: " + itemId.toString());
                        dsiBean = createDatasetItem(tableName, itemId.longValue());
                        dsiBeanList.add(dsiBean);

                    } catch (Exception bex) {
                        log.error("problem while inserting userDatasetItem", bex);
                        errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                          bex.toString());
                        dlog.append("problem while inserting userDatasetItem: " + 
                          bex.toString());
                    }
                } // end while table PKs
                //log.debug("DONE");
            } // end if
        } // end while table names

        // insert the dataset items as a new dataset
        if (dsiBeanList.size() > 0) {
            log.debug("About to insert DSI list; size = " + dsiBeanList.size());
            DatasetUtility dsu = new DatasetUtility();
            AccessionCode dsAC = dsu.insertDataset(dsiBeanList, xmlFileName, 
                    "Created via XML upload", DatasetUtility.TYPE_LOAD, 
                    "private", usrId);

            log.debug("ADDED userdataset " + dsAC.toString());
            return dsAC;
        } else {
            log.error("No dataset items to create.");
        }

        return null;
    }


    private synchronized void runDenorms() {
      System.out.println("*************running denorms");
      DenormUtility denormUtil = new DenormUtility();
      Timer t = new Timer("denormalizing tables");
        try 
        {
            Iterator tit = tableKeys.keySet().iterator();
            while (tit.hasNext()) 
            {
                String tableName = ((String)tit.next()).toLowerCase();
                log.debug("Adding " + tableName + " to denormalizing queue");
                denormUtil.queueTable(tableName);
            }
            
            denormUtil.executeQueuedDenorms();
            t.stop();
            
        }
        catch (SQLException ex) 
        {
            System.out.println("UNABLE to denormalize tables: " + 
              ex.getMessage());
            t.stop();
            log.error("unable to denormalize tables", ex);
        }
        catch (Exception ex) 
        {
            t.stop();
            log.error("Problem running denormalizations", ex);
        }
    }

    /**
     * thread to run the denorms in.
     */
    private class RunKeywordsTimerTask extends java.util.TimerTask
    {
      private long runtime;
      private boolean run = true;
      
      /**
       * constructor.  pass in the scheduled time of execution in millis
       */
      public RunKeywordsTimerTask(long scheduledTime)
      {
        runtime = scheduledTime;
      }
      
      /**
       * run the task
       */
      public void run()
      {
        if(run)
        {
          System.out.println("==========Running in Keyword Thread=============");
          Timer t = new Timer("runKeywords thread");
          try {
              // KEYWORD GENERATION
              timer = new Timer("generate keywords");
              log.debug("====================== KEYWORD GEN");
              dlog.append("====================== KEYWORD GEN");
              KeywordGen kwGen = new KeywordGen(writeConn.getConnections());
              Iterator tit = tableKeys.keySet().iterator();
              int i = 0;
              while (tit.hasNext()) {
                  String tableName = ((String)tit.next()).toLowerCase();
                  kwGen.updatePartialEntityByTable(tableName);
              }
              //Return dbconnection to pool
              DBConnectionPool.returnDBConnection(writeConn);
              readConn.setReadOnly(false);
              DBConnectionPool.returnDBConnection(readConn);
              timer.stop();
          } catch (SQLException kwex) {
              log.error("problem inserting new keywords", kwex);
              errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                      "Problem generating keywords: " + kwex.toString());
              timer.stop();
          } catch (Exception ex) {
              log.error("Some lame problem inserting new keywords", ex);
              errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                      "Problem generating keywords: " + ex.toString());
              timer.stop();
          }
    
          System.out.println("==========Done with Keyword Thread=============");
          t.stop();
        }
      }
      
      /**
       * cancel the task
       */
      public boolean cancel()
      {
        run = false;
        return run;
      }
      
      /**
       * returns the scheduled exe time.
       */
      public long scheduledExecutionTime()
      {
        return runtime;
      }
    }
    
    /**
     * thread to run the denorms in.
     */
    private class RunXMLGenTimerTask extends java.util.TimerTask
    {
      private long runtime;
      private boolean run = true;
      private Iterator accCodeItt;
      
      /**
       * constructor.  pass in the scheduled time of execution in millis
       */
      public RunXMLGenTimerTask(long scheduledTime)
      {
        runtime = scheduledTime;
      }
      
      /**
       * run the task
       */
      public void run()
      {
        if(run)
        {
          /*
            The table this xml is going into looks like this:
            +---------------------+
            | AccessionCode | XML |
            |---------------|-----|
            
            SQL for the table
            -----------------
            create table dba_xmlCache (
              accessioncode   varchar(100),
              xml             bytea);
          */
          try
          {
            System.out.println("==============In runXMLGenTimerTask==============");
            DBConnection conn = null;
            conn = DBConnectionPool.getInstance().getDBConnection("Need " +
              "connection for caching xml");
            conn.setAutoCommit(true);
            System.out.println("got db connection");
            while(accCodeItt.hasNext())
            {
              String accCode = (String)accCodeItt.next();
              System.out.println("generating xml for " + accCode);
              DBModelBeanReader beanReader = new DBModelBeanReader();
              VBModelBean bean = beanReader.getVBModelBean(accCode);
              if(bean == null)
              { //if there is no bean available for the accCode, ignore it
                continue;
              }
              
              /*
               TODO: what do we do if the accession number already exists?
              */
              
              String xml = bean.toXML();
              String sql = "insert into dba_xmlCache (accessioncode, xml) values " +
                "(?, ?)";
              PreparedStatement ps = conn.prepareStatement(sql);
              ps.setString(1, accCode);
              ps.setBytes(2, xml.getBytes());
              //System.out.println("executing sql: " + sql.substring(0, 50));
              //Statement query = conn.createStatement();
              int rowcount = ps.executeUpdate();
              //int rowcount = query.executeUpdate(sql);
              System.out.println("rowcount: " + rowcount);
              if(rowcount != 1)
              {//failure
                String msg = "SQL problem caching bean xml";
                log.error(msg);
                System.out.println(msg);
              }
            } 
            
            DBConnectionPool.returnDBConnection(conn);
            System.out.println("==============Done with runXMLGenTimerTask==============");
          }
          catch (SQLException sqle) 
          {
            System.out.println("error in runXMLGenTimer: " + sqle.getMessage());
            sqle.printStackTrace();
            log.error("SQL problem caching bean xml", sqle);
            errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                    "SQL problem caching bean xml: " + sqle.toString());
          } 
          catch (Exception ex) 
          {
            System.out.println("sql error in runXMLGenTimer: " + ex.getMessage());
            ex.printStackTrace();
            log.error("problem caching bean xml", ex);
            errors.addError(LoadingErrors.DATABASELOADINGERROR, 
                    "problem caching bean xml: " + ex.toString());
          }
        }
      }
      
      /**
       * cancel the task
       */
      public boolean cancel()
      {
        run = false;
        return run;
      }
      
      /**
       * returns the scheduled exe time.
       */
      public long scheduledExecutionTime()
      {
        return runtime;
      }
      
      /**
       * sets the accession code that you want to cache the xml for
       */
      public void setAccessionCode(Iterator accCodeItt)
      {
        this.accCodeItt = accCodeItt;
      }
    }
}
