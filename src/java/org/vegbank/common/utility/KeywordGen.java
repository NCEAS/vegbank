 /*
 *	'$RCSfile: KeywordGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-06-20 21:14:49 $'
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
	private static final int MAX_PER_XACTION = 500;

	private Connection conn = null;
	private static ResourceBundle res = null;

	private List entityList;
	private Map table2entity;
	private Map extraQueries;


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

				//log.info("Adding extra for " + entityName + ": " + extraName);
				map.put(extraName, res.getString(key));
				extraQueries.put(entityName, map);
			}
		}

	}




	//////////////////////////////////////////////////////////
	// STANDALONE
	//////////////////////////////////////////////////////////
	public void run(String dbName, String dbHost, boolean updateMismatch) {

		String dbURL = "jdbc:postgresql://"+dbHost+"/"+dbName;
		System.out.println("connect string: " + dbURL);

		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dbURL, "datauser", "");			

			String s = prompt("\nAre you sure you want to update keywords in " +
					dbName + " on " + dbHost + " [y|n] > ");

			if (!s.equals("y")) {
				System.out.println("Thanks anyway.");
				conn.close();
				System.exit(0);
			}

			StopWatchUtil stopWatch = new StopWatchUtil("generate keywords");
			stopWatch.startWatch();
			genKeywords(updateMismatch);
			stopWatch.stopWatch();
			stopWatch.printTimeElapsed();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
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

        if (!insertMainKeywords(entityName, updateMismatch)) {
            throw new SQLException("Problem inserting keywords for " + entityName);
        }

        if (extraQueries.get(entityName) != null) {
            if (!insertExtraKeywords(entityName, updateMismatch)) {
                throw new SQLException("Problem inserting extras for " + entityName);
            }

            if (BUILD_MODE == SINGLE) {
                if (!appendExtraKeywords(entityName, updateMismatch)) {
                    throw new SQLException("Problem appending extras for " + entityName);
                }
            }
        }
    }


	/**
	 * Inserts a record in the keywords table for each record in given entity. 
	 *
	 * @return true if inserts were all successful
	 */
	private boolean insertMainKeywords(String entityName, boolean updateMismatch) throws SQLException {
		log.info("Inserting main keywords for entity: " + entityName);

		entityName = entityName.toLowerCase();

		String entityQuery = getEntityQuery(entityName, updateMismatch);
		if (entityQuery == null) {
			return false;
		}

		return buildKeywordTable("keywords", entityQuery, entityName, false, !updateMismatch);
	}


	/**
	 * Inserts a record in the extra keywords table for each record in given entity. 
	 *
     * @param updateMismatch find only records missing from the keywords table
	 * @return true if inserts were all successful
	 */
	private boolean insertExtraKeywords(String entityName, boolean updateMismatch) throws SQLException {
        // run it where doDelete != updateMismatch
        return insertExtraKeywords(entityName, false, !updateMismatch);
    }

    /**
     * @param doDelete is the inverse of updateMismatch
     */
	private boolean insertExtraKeywords(String entityName, boolean isUpdate, boolean doDelete) 
            throws SQLException {
		String entityQuery, extraName;
		entityName = entityName.toLowerCase();
		HashMap extraMap = (HashMap)extraQueries.get(entityName);
		if (extraMap == null) { return true; }

		log.info("##### Inserting extra keywords for entity " + entityName);

		// each entity can have many extra queries
		boolean first = true;
		boolean success = true, temporarySuccess;
		Iterator it = extraMap.keySet().iterator();
		while (it.hasNext()) {
			extraName = (String)it.next();
			entityQuery = getExtraEntityQuery(entityName, extraName, !doDelete);

			if (entityQuery == null) { 
				log.error("ERROR: query missing for extras." + entityName + "." + extraName);
				return false; 
			}

			log.info("## Inserting extras: " + extraName);
			if (BUILD_MODE == MULTI) {
				//////// MULTI
				//temporarySuccess = buildKeywordTable("keywords", entityQuery, entityName, isUpdate, false);
                log.error("not using MULTI mode");
			} else {
				/////// SINGLE
				temporarySuccess = buildKeywordTable("keywords_extra", entityQuery, entityName, isUpdate, doDelete);
			}

			if (!temporarySuccess) {
				// if any failed, success = false
				success = false;
				log.error("ERROR while inserting extra's with: " + entityQuery);
			}

			if (first) {
				first = false;
				doDelete = false;
			}
		}

		return success;
	}


	/**
	 * Appends extra keywords onto main keywords.
	 *
	 * @return true if updates were all successful
	 */
	private boolean appendExtraKeywords(String entityName, boolean updateMismatch) throws SQLException {
		String entityQuery = "SELECT table_id,keywords FROM keywords_extra ";

        if (updateMismatch) {
            entityQuery += getExtraMismatchWhere(entityName);
        } else {
		    entityQuery += "WHERE entity='" + entityName + "'";
        }

        // isUpdate = true, doDelete = false ALWAYS
		return buildKeywordTable("keywords", entityQuery, entityName, true, false);
	}


	/**
	 * Given a keyword query for one entity, this method populate the
	 * given keyword table (main or extra).
	 */
	private boolean buildKeywordTable(String kwTable, String entityQuery, 
			String entityName, boolean isUpdate, boolean doDelete) throws SQLException {


		StringBuffer sbTmpKw = null;
		HashMap kwIdMap = new HashMap();
		String tmpId;
		long lTmpId;
		long count=0;
		int numFields;
		ResultSet rs;
		Statement stmt = conn.createStatement();
		conn.setAutoCommit(false);

		showSQL(entityQuery);

		// count records
		String sqlFrom = entityQuery.substring( entityQuery.lastIndexOf("FROM") );
		String sql = "SELECT COUNT(*) AS count FROM (" + entityQuery + ") AS entityQuery";
		rs = stmt.executeQuery(sql);

		if (rs.next()) {
			count = rs.getLong("count");
		}

		if (count == 0) {
			log.info("No records found.");
			return true;
		}

		
		PreparedStatement pstmt;
		if (isUpdate) {
		 	pstmt = getUpdatePreparedStatement();
		} else {
		 	pstmt = getInsertPreparedStatement(kwTable, entityName);
		}

		if (doDelete) {
			log.info("deleting entity's current keywords");
			stmt.executeUpdate("DELETE FROM " + kwTable + " WHERE entity='" + entityName + "'");
		}

		log.info("Selecting " + count + " " + entityName + " entities...please wait");

		rs = stmt.executeQuery(entityQuery);

		// get metadata
		ResultSetMetaData rsmd = rs.getMetaData();
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

			try {
				// first column MUST be PK for parent entity table
				lTmpId = rs.getLong(1);
				tmpId = Long.toString(lTmpId);
			} catch (Exception ex) {
				log.error("ERROR: PK field is not valid");
				conn.rollback();
				return false;
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
						// compile all keywords into one value
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
			} else {
				insertRow(pstmt, lTmpId, entityName, sbKeywords.toString());
				sb.updateStatusBar();

				if (l++ > MAX_PER_XACTION) {
					conn.commit();
				}
			}
		} // end while all results 

		if (isUpdate) {
			sb.setupStatusBar(kwIdMap.size());
			// update each record en masse
			l=0;
			Iterator uit = kwIdMap.keySet().iterator();
			while (uit.hasNext()) {
				tmpId = (String)uit.next();
				updateRow(pstmt, Long.parseLong(tmpId), entityName,
						((StringBuffer)kwIdMap.get(tmpId)).toString());
				sb.updateStatusBar();
				if (l++ > MAX_PER_XACTION) {
					conn.commit();
				}
			}
		}


		// tidy up the end of the status bar
		sb.completeStatusBar(count);

		stopWatch.stopWatch();
		stopWatch.printTimeElapsed();

		rs.close();
		conn.commit();

		return true;
	}


	/**
	 *
	 */
	private void insertRow(PreparedStatement pstmt, long tableId, 
			String entity, String keywords) throws SQLException {
		pstmt.setLong(1, tableId);
		pstmt.setString(2, entity);
		pstmt.setString(3, keywords);
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
		//log.info(pstmt.toString());
		pstmt.executeUpdate();
	}


	/**
	 *
	 */
	private PreparedStatement getInsertPreparedStatement(String table, String entityName) 
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
	 */
	private String getEntityQuery(String entityName, boolean updateMismatch) {

		String pkField = res.getString("pk." + entityName);
		String from = res.getString("from." + entityName);
		String select = res.getString("select." + entityName);

		if (Utility.isStringNullOrEmpty(select)) {
			log.error("ERROR: select." + entityName + " must be specified");
			return null;
		}

		if (Utility.isStringNullOrEmpty(from)) {
			log.error("ERROR: from." + entityName + " must be specified");
			return null;
		}


		if (Utility.isStringNullOrEmpty(pkField)) {
			log.error("ERROR: pk." + entityName + " must be specified");
			return null;
		}
        if (updateMismatch) {
		    String tmp = "SELECT DISTINCT " + pkField + "," + select + " FROM " + from;
            if (from.toLowerCase().indexOf(" where ") == -1) { tmp += " WHERE ";
            } else { tmp += " AND "; }
            return tmp + getMismatchWhere(entityName);
        } else {
		    return "SELECT DISTINCT " + pkField + "," + select + " FROM " + from;
        }
	}


    /**
     *
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
            int pos = q.indexOf("order by");
            if (pos == -1) {
                log.debug("no ORDER BY clause: " + q);
                sb.append(q).append(glue).append(getMismatchWhere(entityName));
            } else {
                log.debug("found ORDER BY at " + pos + " of " + q.length());
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
     * Caveat: Does not start with WHERE or AND.
     */
    private String getMismatchWhere(String entityName) {
        String pkName = getEntityPK(entityName);
        StringBuffer where = new StringBuffer(128);
        where.append(pkName)
            .append(" NOT IN (SELECT table_id FROM keywords WHERE entity='")
            .append(entityName)
            .append("')");

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
		    .append(") AND entity='")
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
		System.out.println("SQL: " + sql);
	}

    /**
     * Gets the entity query and limits it to missing entity records.
     */
    /*
    public void updatePartialEntity(String entityName) throws SQLException {
        log.debug("uPE:" + entityName);
        String pkName = getEntityPK(entityName);
        StringBuffer where = new StringBuffer(128);
        where.append(" WHERE ")
            .append(pkName)
            .append(" NOT IN (SELECT table_id FROM keywords WHERE entity='")
            .append(entityName)
            .append("')");

        // make sure you don't delete the extant keywords!
        boolean isUpdate = false;
        boolean doDelete = false;

        log.debug("inserting new main keywords");
		buildKeywordTable("keywords", getEntityQuery(entityName) + where.toString(), 
                entityName, isUpdate, doDelete);

        if (extraQueries.get(entityName) != null) {
            log.debug("inserting new extra keywords");
            insertExtraKeywords(entityName, isUpdate, doDelete);
        }
    }
    */


    /**
     *
     */
    /*
    public void updatePartialEntity(String entity, List pkList) {
        //TODO: insert new keywords records for each pkList item

        if (pkList == null || pkList.size() == 0) return;
        StringBuffer = new StringBuffer(64);
        query.append("SELECT ");
        String pk;
        Iterator lit = pkList.iterator();
        while (lit.hasNext()) {
            pk = (String)lit.next();
            if (first) { first = false; }
            else { query.append(","); }
            query.append(pk);
        }
    }
    */


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
