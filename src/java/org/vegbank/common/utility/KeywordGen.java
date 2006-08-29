 /*
 *	'$RCSfile: KeywordGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-08-29 21:48:29 $'
 *	'$Revision: 1.16 $'
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

package org.vegbank.common.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.vegbank.common.utility.CommandLineTools.StatusBarUtil;

/**
 * @author anderson
 */
public class KeywordGen {

	private static Log log = LogFactory.getLog(KeywordGen.class);
	private static final String KW_DELIM = " ";

	private static final int SINGLE = 1;
	private static final int MULTI = 2;
	private static final int BUILD_MODE = SINGLE;
	private static final int MAX_PER_XACTION = 100;
	private static final String MM_TEMP_TABLE_PREFIX = "kwmm";
	private static final String EXTRA_TEMP_TABLE_PREFIX = "kwextra";
    private static String mmTempTableName = null;

	private Connection conn = null;
	private static ResourceBundle res = null;

	private List entityList;
	private Map table2entity;
	private Map extraQueries;
  private org.vegbank.common.utility.Timer timer;


	public KeywordGen() {
		init();
	}
	
	/**
	 * Hit some problems using this class regarding the connection
	 * not being initialized.
	 * Allowing it to be passed in via the constructor if need be. * 
	 */
	public KeywordGen(Connection dbconn) {
		init();
		this.conn = dbconn;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException s99) {
            log.error("Unable to set autocommit to false in kwgen init", s99);
        }
	}
	

	/**
	 * Read the props file.
	 */
	private void init() {
		res = ResourceBundle.getBundle("keywords");
		entityList = new ArrayList();
		table2entity = new HashMap();
		extraQueries = new HashMap();
		HashMap map;
		String key, entityName, extraName, tableName;
        mmTempTableName = MM_TEMP_TABLE_PREFIX + Thread.currentThread().hashCode();


		// load the entity list
		for (Enumeration e = res.getKeys(); e.hasMoreElements() ;) {
			key = (String)e.nextElement();
			if (key.startsWith("enable.")) {
                tableName = res.getString(key);
                //log.debug("enabling table: " + tableName);
				if (!Utility.isStringNullOrEmpty(tableName)) {
					entityName = key.substring("enable.".length()).toLowerCase();
					entityList.add(entityName);
					table2entity.put(tableName, entityName);
				}
			} else if (key.startsWith("extra.")) {
				entityName = key.substring("extra.".length());
				int pos = entityName.indexOf(".");
				if (pos != -1) {
					extraName = entityName.substring(pos+1);
					entityName = entityName.substring(0, pos);
				} else {
					extraName = "default";
				}
				map = (HashMap)extraQueries.get(entityName);
				if (map == null) {
					map = new HashMap();
				}

				log.info("Adding extra for " + entityName + ": " + extraName);
				map.put(extraName, res.getString(key));
				extraQueries.put(entityName, map);
			}
		}

	}




	//////////////////////////////////////////////////////////
	// STANDALONE
	//////////////////////////////////////////////////////////
	public void run(String dbName, String dbHost, boolean updateMismatch) {
    timer = new org.vegbank.common.utility.Timer("Time for keyword generation run thread");
    
		String dbURL = "jdbc:postgresql://"+dbHost+"/"+dbName;
		//System.out.println("connect string: " + dbURL);

		try {
			Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbURL, dbName, "dta4all");          

			String s = prompt("\nAre you sure you want to update keywords in " +
					dbName + " on " + dbHost + " [y|n] > ");

			if (!s.equals("y")) {
				//System.out.println("Thanks anyway.");
				System.exit(0);
			}

			StopWatchUtil stopWatch = new StopWatchUtil("generate keywords");
			stopWatch.startWatch();
			genKeywords(updateMismatch);
			stopWatch.stopWatch();
			stopWatch.printTimeElapsed();

		} catch (Exception e) {
			e.printStackTrace();
		}
    timer.stop();
	}


	/**
	 * When running standalone, drives the table updates.
	 */
	private void genKeywords(boolean updateMismatch) throws SQLException {
		String entityName;

		Iterator it = entityList.iterator();
		while (it.hasNext()) {
			genKeywords((String)it.next(), updateMismatch);
		}
	}


    /**
     *
     */
	private void genKeywords(String entityName, boolean updateMismatch) throws SQLException {

		entityName = entityName.toLowerCase();

        if (updateMismatch) {
            // select mismatch records (those without keywords yet) into temp table
            buildMismatchTempTable(entityName);
        }


        if (!insertMainKeywords(entityName, updateMismatch)) {
            throw new SQLException("Problem inserting keywords for " + entityName);
        }

        if (extraQueries.get(entityName) != null) {
            log.debug("+++ inserting extra keywords for " + entityName);
            String tmpTableName = insertExtraKeywords(entityName, updateMismatch);
            if (tmpTableName == null) {
                throw new SQLException("Problem inserting extras for " + entityName);
            }

           // this is handled in insertExtraKeywords as each statement gets its own temp table now (MTL 20-JUL-2006)
           // if (!tmpTableName.equals("") && BUILD_MODE == SINGLE) {
           //     log.debug("+++ appending extra keywords for " + entityName);
           //     if (!appendExtraKeywords(entityName, updateMismatch, tmpTableName)) {
           //         throw new SQLException("Problem appending extras for " + entityName);
           //     }
           //     log.debug("done appending extra keywords for " + entityName + " using temp table: " + tmpTableName);
           // }
        }
        log.debug("committing keywords for " + entityName);
        conn.commit();
    }


	/**
	 * Inserts a record in the keywords table for each record in given entity. 
	 *
	 * @return true if inserts were all successful
	 */
	private boolean insertMainKeywords(String entityName, boolean updateMismatch) throws SQLException {
		log.info("Inserting main keywords for entity: " + entityName);

		String entityQuery = getEntityQuery(entityName, updateMismatch, true);
		if (entityQuery == null) { return false; }

		return (null != buildKeywordTable("keywords", entityQuery, entityName, false, !updateMismatch, false));
	}


	/**
	 * Inserts a record in the extra keywords table for each record in given entity. 
	 *
     * @param updateMismatch find only records missing from the keywords table
	 * @return temp table name if inserts were all successful, else null
	 */
	private String insertExtraKeywords(String entityName, boolean updateMismatch) throws SQLException {

        // run it where doDelete != updateMismatch
        return insertExtraKeywords(entityName, false, !updateMismatch);
    }

    /**
     * @param isUpdate should be false if we're adding new keywords
     * @param doDelete is the inverse of updateMismatch
	 * @return temp table name if inserts were all successful, else null if error
     */
	private String insertExtraKeywords(String entityName, boolean isUpdate, boolean doDelete) 
            throws SQLException {
		String entityQuery, extraName;
		entityName = entityName.toLowerCase();
		HashMap extraMap = (HashMap)extraQueries.get(entityName);
		if (extraMap == null) { return ""; }

		log.info("##### Inserting extra keywords for entity " + entityName);

		// each entity can have many extra queries
		boolean first = true;
		boolean success = true, temporarySuccess;
        long countPerTable = 0;
        String tmpTableName=null;
		Iterator it = extraMap.keySet().iterator();
		while (it.hasNext()) {
			extraName = (String)it.next();
            countPerTable = countPerTable + 1;
            entityQuery = getExtraEntityQuery(entityName, extraName, !doDelete);

			if (entityQuery == null) { 
				log.error("ERROR: query missing for extras." + entityName + "." + extraName);
				return null; 
			}

			log.info("## Inserting extras: " + extraName);
			if (BUILD_MODE == MULTI) {
				//////// MULTI
				//tmpTableName = buildKeywordTable("keywords", entityQuery, entityName, isUpdate, false);
                log.error("MULTI mode not implemented!");
			} else {
				/////// SINGLE
                
				tmpTableName = buildKeywordTable(EXTRA_TEMP_TABLE_PREFIX + countPerTable + "_", entityQuery, entityName, isUpdate, doDelete, true);
                // go ahead and append that one:
                if (!tmpTableName.equals("") && BUILD_MODE == SINGLE) {
                  log.debug("+++ appending extra keywords for " + entityName + " using temp table: " + tmpTableName);
                  if (!appendExtraKeywords(entityName, !doDelete, tmpTableName)) {
                      throw new SQLException("Problem appending extras for " + entityName);
                  }
                  log.debug("done appending extra keywords for " + entityName + " using temp table: " + tmpTableName);
                }
			}

			if (tmpTableName == null) {
				// if any failed, success = false
				success = false;
				log.error("ERROR while inserting extras with: " + entityQuery);
			}

			if (first) {
				first = false;
				doDelete = false;
			}
		}

		return tmpTableName;
	}


	/**
	 * Appends extra keywords onto main keywords.
	 *
	 * @return true if updates were all successful
	 */
	private boolean appendExtraKeywords(String entityName, boolean updateMismatch, String tmpTableName) throws SQLException {
		String entityQuery = "SELECT table_id,keywords FROM " + tmpTableName + " ";

        /*
        if (updateMismatch) {
            entityQuery += getWhereClauseConnector(entityQuery);
            entityQuery += getMismatchWhere(entityName);
        } else {
		    entityQuery += "WHERE entity='" + entityName + "'";
        }
        */

        // isUpdate = true, doDelete = false ALWAYS
		return (null != buildKeywordTable("keywords", entityQuery, entityName, true, false, false));
	}


	/**
	 * Given a keyword query for one entity, this method populate the
	 * given keyword table (main or extra).
	 */
	private String buildKeywordTable(String kwTable, String entityQuery, 
			String entityName, boolean isUpdate, boolean doDelete, boolean useTempTable) throws SQLException {


		StringBuffer sbTmpKw = null;
		HashMap kwIdMap = new HashMap();
		String tmpId, sql;
		long lTmpId;
		long count=0;
		int numFields;
		ResultSet rs;
        ResultSetMetaData rsmd;
		Statement stmt = conn.createStatement();

        /*
        // count records
		String sqlFrom = entityQuery.substring( entityQuery.lastIndexOf("FROM") );
		sql = "SELECT COUNT(*) AS count FROM (" + entityQuery + ") AS entityQuery";
        log.debug("buildKeywordTable: " + sql);
        rs = stmt.executeQuery(sql);
        */
        sql = "select count(id) from " + mmTempTableName;
        log.debug("COUNTING RECORDS: " + sql);
        rs = stmt.executeQuery(sql);

		if (rs.next()) {
			count = rs.getLong("count");
		}

		if (count == 0) {
			log.debug("No keywords for " + kwTable);
			return "";
		}

        ///////////////////////////////
        sql = "select * from " + mmTempTableName;
        log.debug("GETTING RECORDS: " + sql);
        rs = stmt.executeQuery(sql);

        rsmd = rs.getMetaData();
        int numCols = rsmd.getColumnCount();
		while (rs.next()) {
            for (int i=1; i<=numCols; i++) {
			    log.debug(i + " :: " + rs.getObject(i).toString());
            }
		}
        ///////////////////////////////


		
		PreparedStatement pstmt;
		if (isUpdate) {
		 	pstmt = getUpdatePreparedStatement();
		} else {
            if (useTempTable) {
                // create empty temp table, include name of entity to keep this entity's stuff applied to only this entity (this table doesn't get dropped)
                // and also add the name of the extra (the bit after the extra.entity.)
                
                kwTable = kwTable + Thread.currentThread().hashCode() + entityName;
                log.debug("creating TEMP TABLE " + kwTable);
                String sqlTempTbl = "SELECT * INTO TEMP TABLE " + kwTable + " FROM keywords WHERE true=false";
                try {
                    stmt.executeUpdate(sqlTempTbl);
                } catch (SQLException s2) {
                    
                    // this is not an error if there is more than one extra statement on this table.  
                    // In that case, this table already exists, and the fact that the previous sql didn't work is not significant
                    // If the temp table REALLY doesn't exist,
                    // then another error will be thrown later.  (ML)
                    //  log.error("Unable to create temp table " + kwTable , s2);
                   
                }
            }
		 	pstmt = getInsertPreparedStatement(kwTable);
		}

		if (doDelete) {
			sql = "DELETE FROM " + kwTable + " WHERE entity='" + entityName + "'";
			log.info("deleting entity's current temporary extra keywords");
			log.debug(sql);
			stmt.executeUpdate(sql);
		}

		//log.info("Selecting entities...please wait");
		log.info("Selecting " + count + " " + entityName + " entities...please wait");
		showSQL(entityQuery);
        rs = stmt.executeQuery(entityQuery);
		// get metadata
		rsmd = rs.getMetaData();
		numFields = rsmd.getColumnCount();
		String tmpValue, tmpField;
		StringBuffer sbKeywords;

		StopWatchUtil stopWatch = new StopWatchUtil("building " + entityName);
		stopWatch.startWatch();

		// set up the progress meter
		StatusBarUtil sb = new StatusBarUtil();
		if (!isUpdate) {
			sb.setupStatusBar(count);
		}

		long l=0;
		while (rs.next()) {
            log.debug(" ++++++++++ next +++++++++++");

			try {
				// first column MUST be PK for parent entity table
				lTmpId = rs.getLong(1);
				tmpId = Long.toString(lTmpId);
			} catch (Exception ex) {
				log.error("ERROR: PK field is not valid");
				conn.rollback();
				return kwTable;
			}

			sbKeywords = new StringBuffer(256);

			for (int i=2; i<=numFields; i++) {
				tmpValue = rs.getString(i);

				if (tmpValue != null && !tmpValue.equals("null")) {

					
					if (BUILD_MODE == MULTI) {
						// insert a new row for each keyword
						try {
							insertRow(pstmt, lTmpId, entityName, tmpValue);
						} catch (Exception ex) {
							// oh well.  we tried
							log.error("Problem while trying to insert keyword for id: " +
									lTmpId + ": " + tmpValue);
						}
					} else {
						// concatenate all keywords into one value
                        log.debug(lTmpId + ": " + tmpValue);
                        sbKeywords.append(KW_DELIM).append(tmpValue);
					}
				}
			}
		
            if (isUpdate) {
                // collect all keywords for same tmpId
                sbTmpKw = (StringBuffer)kwIdMap.get(tmpId);
                if (sbTmpKw == null) {
                    sbTmpKw = new StringBuffer(128);
                }

                // append the latest
                sbTmpKw.append(sbKeywords);
                kwIdMap.put(tmpId, sbTmpKw);
                log.debug("adding keywords to " + tmpId + ": " + sbTmpKw.toString());
            } else {
                insertRow(pstmt, lTmpId, entityName, sbKeywords.toString());
                sb.updateStatusBar();

                if (++l % MAX_PER_XACTION == 0) {
                    log.debug("MAX XACTIONS REACHED...committing");
                    conn.commit();
                }
            }

		} // end while all results 
        log.debug(" ++++++++++ DONE +++++++++++");

		if (isUpdate) {
            log.debug("attempting to update...");
			sb.setupStatusBar(kwIdMap.size());
			// update each record en masse
			l=0;
			Iterator uit = kwIdMap.keySet().iterator();
			while (uit.hasNext()) {
				tmpId = (String)uit.next();
				updateRow(pstmt, Long.parseLong(tmpId), entityName,
						((StringBuffer)kwIdMap.get(tmpId)).toString());
				sb.updateStatusBar();

				if (++l % MAX_PER_XACTION == 0) {
                    log.debug("MAX XACTIONS REACHED...committing");
					conn.commit();
				}
			}
		}


		// tidy up the end of the status bar
		sb.completeStatusBar(count);

		stopWatch.stopWatch();
		stopWatch.printTimeElapsed();

		rs.close();

		return kwTable;
	}


	/**
	 *
	 */
	private void insertRow(PreparedStatement pstmt, long tableId, 
			String entity, String keywords) throws SQLException {
		pstmt.setLong(1, tableId);
		pstmt.setString(2, entity);
		pstmt.setString(3, keywords);
		log.debug("insertRow: " + pstmt.toString());
		pstmt.executeUpdate();
	}


	/**
	 *
	 */
	private void updateRow(PreparedStatement pstmt, long tableId, String entityName, String keywords) 
			throws SQLException {
		pstmt.setString(1, keywords);
		pstmt.setLong(2, tableId);
		pstmt.setString(3, entityName);
		log.debug("updateRow: " + pstmt.toString());
		pstmt.executeUpdate();
	}


	/**
	 *
	 */
	private PreparedStatement getInsertPreparedStatement(String table)
			throws SQLException {
		return conn.prepareStatement(
			"INSERT INTO " + table + " (table_id,entity,keywords) VALUES (?,?,?)");
	}


	/**
	 *
	 */
	private PreparedStatement getUpdatePreparedStatement() throws SQLException {
		// the extra keywords should begin with a ' ' space char
		return conn.prepareStatement(
			"UPDATE keywords SET keywords = keywords || ' ' || ? WHERE table_id=? AND entity=?");
	}


	/**
	 *
	 * @param includeSelectFields if false, only select PK field
	 */
	private String getEntityQuery(String entityName, boolean updateMismatch, boolean includeSelectFields) {

		String pkField = getEntityPK(entityName);
		String from = res.getString("from." + entityName);

		if (Utility.isStringNullOrEmpty(from)) {
			log.error("ERROR: from." + entityName + " must be specified");
			return null;
		}


		if (Utility.isStringNullOrEmpty(pkField)) {
			log.error("ERROR: pk." + entityName + " must be specified");
			return null;
		}

        String tmp = "";

        if (includeSelectFields) {
            String select = res.getString("select." + entityName);
            if (Utility.isStringNullOrEmpty(select)) {
                log.error("ERROR: select." + entityName + " must be specified");
                return null;
            }

            tmp = "SELECT DISTINCT " + pkField + "," + select;
        }

        tmp += " FROM " + from;

        if (updateMismatch) {
            tmp += getWhereClauseConnector(tmp);
            tmp += getMismatchWhere(entityName);
        }

        return tmp;
	}


	private String getMismatchSelectQuery(String entityName) {

		String pkField = res.getString("pk." + entityName);
		String from = res.getString("from." + entityName);

		if (Utility.isStringNullOrEmpty(from)) {
			log.error("ERROR: from." + entityName + " must be specified");
			return null;
		}


		if (Utility.isStringNullOrEmpty(pkField)) {
			log.error("ERROR: pk." + entityName + " must be specified");
			return null;
		}

        String tmp = "SELECT DISTINCT " + pkField + " FROM " + from;
        if (from.toLowerCase().indexOf(" where ") == -1) {
            tmp += " WHERE ";
        } else {
            tmp += " AND "; 
        }

        return tmp + getMismatchWhere(entityName);
	}


    /**
     * This is used to populate the extra keywords temp table.
     */
    private String getExtraEntityQuery(String entityName, String extraName, boolean whereMismatch) {
		HashMap extraMap = (HashMap)extraQueries.get(entityName);
		if (extraMap == null) { return ""; }
        String q = ((String)extraMap.get(extraName)).toLowerCase();

        if (whereMismatch) {
            StringBuffer sb = new StringBuffer(128);
            String glue;

            if (q.indexOf(" where ") == -1) { glue = " where "; } 
            else { glue = " and "; }

            // find stuff that goes after WHERE
            int pos = q.indexOf("group by");
            if (pos == -1) {
                pos = q.indexOf("order by");
            }

            if (pos == -1) {
                // just tack that baby on the end
                sb.append(q).append(glue).append(getMismatchWhere(entityName));
            } else {
                // append it carefully 
                sb.append(q.substring(0,pos))
                    .append(glue).append(getMismatchWhere(entityName))
                    .append(" ").append(q.substring(pos));
            }
            return sb.toString();

        } else {
            return q;
        }

    }


    /**
     * This is used to get the where clause which builds
     * the temp table that holds just IDs 
     * of records that don't have keywords yet.
     * Caveat: Does not start with WHERE or AND.
     */
    private String getMismatchInitWhere(String entityName) {
        String pkName = getEntityPK(entityName);
        if (Utility.isStringNullOrEmpty(entityName)) { return ""; }

        StringBuffer where = new StringBuffer(128);
        where.append(pkName)
            .append(" NOT IN (SELECT table_id FROM keywords WHERE entity='")
            .append(entityName)
            .append("')");

        return where.toString();
    }


    /**
     * Produces a where clause that limits PK by those 
     * found in the mismatch temp table.
     * Caveat: Does not start with WHERE or AND.
     */
    private String getMismatchWhere(String entityName) {
        String pkName = getEntityPK(entityName);
        if (Utility.isStringNullOrEmpty(entityName)) { return ""; }

        StringBuffer where = new StringBuffer(128);
        where.append(pkName)
            .append(" IN (SELECT id FROM ")
            .append(mmTempTableName)
            .append(")");

        return where.toString();
    }


    /**
     *
     */
    private String getExtraMismatchWhere(String entityName) {
        String pkName = getEntityPK(entityName);
        StringBuffer where = new StringBuffer(128);
        where.append(" WHERE table_id NOT IN ")
            .append("(SELECT table_id FROM keywords WHERE entity='")
            .append(entityName)
		    .append("') AND entity='")
            .append(entityName)
            .append("'");

        return where.toString();
    }


	/**
	 * Given keywords.entity, return its associated DB table name.
	 */
	public String getEntityTable(String entityName) {
		return res.getString("table." + entityName);
	}


	/**
	 * Given a DB table name, return the entity name 
     * used in the keywords table.
	 */
	public String getTableEntity(String tableName) {
		return (String)table2entity.get(tableName.toLowerCase());
	}


	/**
	 *
	 */
	private String getEntityPK(String entityName) {
		return res.getString("pk." + entityName);
	}


	private void usage(String msg) {
		System.out.println("USAGE: KeywordGen <dbname> [dbhost] [add new records only]");
		System.out.println("Default dbhost is 'localhost'");
		System.out.println("Default is to add only new records and not delete entities");
		System.out.println(msg);
	}


	private String prompt(String msg) {
		System.out.print(msg);
		InputStreamReader isr = new InputStreamReader ( System.in );
		BufferedReader br = new BufferedReader ( isr );
		String answer = null;
		try {
			answer = br.readLine();
		} catch (IOException ioe) {
			// won't happen too often from the keyboard
		}
		System.out.println("You answered " + answer);
		return answer;
	}


	private void showSQL(String sql) {
		log.debug("SQL: " + sql);
		//System.out.println("SQL: " + sql);
	}

    /**
     * Returns " WHERE " or " AND ".
     */
    private String getWhereClauseConnector(String sql) {
        if (sql.toLowerCase().indexOf(" where ") == -1) {
            return " WHERE ";
        } else {
            return " AND "; 
        }
    }

    /**
     * Responsible for creating the mismatch ID temp table.
     */
    private void buildMismatchTempTable(String entityName) {
        String pkName = getEntityPK(entityName);
        String entityQuery = getEntityQuery(entityName, false, false);

        StringBuffer sb = new StringBuffer(256)
            .append("SELECT DISTINCT ")
            .append(pkName)
            .append(" AS id INTO TEMP TABLE ")
            .append(mmTempTableName)
            .append(entityQuery);

        // add WHERE or AND
        sb.append(getWhereClauseConnector(entityQuery));

        sb.append(pkName)
            .append(" NOT IN (SELECT table_id FROM keywords WHERE entity='")
            .append(entityName)
            .append("')");

        
        try {
		    Statement stmt = conn.createStatement();
            try { 
                // see if the tmp table already exists
                ResultSet rs = stmt.executeQuery("SELECT schemaname FROM pg_tables WHERE tablename='" + mmTempTableName + "'");
                if (rs.next()) {
                    log.debug("Dropping temp table: " + mmTempTableName);
                    stmt.executeUpdate("DROP TABLE " + mmTempTableName); 
                }
                rs.close();

            } catch (Exception ex) { 
                log.debug("Couldn't drop temp table " + mmTempTableName + ": " + ex.toString());
		        stmt = conn.createStatement();
            }

            log.debug("Creating mismatch temp table for " + entityName + ": " + sb.toString());
            stmt.executeUpdate(sb.toString());
            stmt.close();

        } catch (SQLException s2) {
            log.error("Unable to create mismatch temp table", s2);
        }
    }
            
    /**
     *
     */
    public void updatePartialEntityByTable(String tableName) throws SQLException {
        String entityName = getTableEntity(tableName.toLowerCase());
        if (Utility.isStringNullOrEmpty(entityName)) {
            return;
        }
        genKeywords(entityName, true);
    }


	//////////////////////////////////////////////////////////
	// MAIN
	//////////////////////////////////////////////////////////
	/**
	 *
	 */
	public static void main(String[] args) {
		KeywordGen kwg = new KeywordGen();
		String dbHost;
		String updateMismatch = "true";
		boolean bupdateMismatch = true;

		if (args.length < 1) {
			kwg.usage("Database name is required to run.");
			System.exit(0);
		} 

		if (args.length < 2) { // host
			dbHost = "localhost";
        } else {
			dbHost = args[1];
		}
        if (args.length == 3) {
			updateMismatch = args[2];
		}

        if (Utility.isStringNullOrEmpty(updateMismatch) || updateMismatch.equals("false")) {
            bupdateMismatch = false;
        } else {
            bupdateMismatch = true;
        }
		kwg.run(args[0], dbHost, bupdateMismatch);
	}

}
