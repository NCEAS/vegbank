 /*
 *	'$RCSfile: KeywordGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-11-29 17:03:15 $'
 *	'$Revision: 1.8 $'
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
	private HashMap extraQueries;


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
		extraQueries = new HashMap();
		HashMap map;
		String key, entityName, extraName;

		// load the entity list
		for (Enumeration e = res.getKeys(); e.hasMoreElements() ;) {
			key = (String)e.nextElement();
			if (key.startsWith("enable.")) {
				if (res.getString(key).equalsIgnoreCase("true")) {
					entityName = key.substring("enable.".length());
					entityList.add(entityName);
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

				System.out.println("Adding extra for " + entityName + ": " + extraName);
				map.put(extraName, res.getString(key));
				extraQueries.put(entityName, map);
			}
		}

	}




	//////////////////////////////////////////////////////////
	// STANDALONE
	//////////////////////////////////////////////////////////
	public void run(String dbName, String dbHost) {

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
			genKeywords();
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
	private void genKeywords() throws SQLException {
		String entityName;

		Iterator it = entityList.iterator();
		while (it.hasNext()) {
			entityName = (String)it.next();

			if (!insertMainKeywords(entityName)) {
				throw new SQLException("Problem inserting keywords for " + entityName);
			}

			if (extraQueries.get(entityName) != null) {
				if (!insertExtraKeywords(entityName)) {
					throw new SQLException("Problem inserting extras for " + entityName);
				}

				if (BUILD_MODE == SINGLE) {
					if (!appendExtraKeywords(entityName)) {
						throw new SQLException("Problem inserting extras for " + entityName);
					}
				}
			}
		}
	}


	/**
	 * Inserts a record in the keywords table for each record in given entity. 
	 *
	 * @return true if inserts were all successful
	 */
	private boolean insertMainKeywords(String entityName) throws SQLException {
		System.out.println("\n\nInserting main keywords for entity " + entityName);

		entityName = entityName.toLowerCase();

		String entityQuery = getEntityQuery(entityName);
		if (entityQuery == null) {
			return false;
		}

		return buildKeywordTable("keywords", entityQuery, entityName, false, true);
	}


	/**
	 * Inserts a record in the extra keywords table for each record in given entity. 
	 *
	 * @return true if inserts were all successful
	 */
	private boolean insertExtraKeywords(String entityName) throws SQLException {
		String entityQuery, extraName;
		entityName = entityName.toLowerCase();
		HashMap extraMap = (HashMap)extraQueries.get(entityName);
		if (extraMap == null) { return true; }

		System.out.println("\n##### Inserting extra keywords for entity " + entityName);

		// each entity can have many extra queries
		boolean first = true;
		boolean doDelete = true;
		boolean success = true, temporarySuccess;
		Iterator it = extraMap.keySet().iterator();
		while (it.hasNext()) {
			extraName = (String)it.next();
			entityQuery = (String)extraMap.get(extraName);
			if (entityQuery == null) { 
				System.err.println("ERROR: query missing for extras." + entityName + "." + extraName);
				return false; 
			}

			System.out.println("## Inserting extras: " + extraName);
			if (BUILD_MODE == MULTI) {
				//////// MULTI
				temporarySuccess = buildKeywordTable("keywords", entityQuery, entityName, false, false);
			} else {
				/////// SINGLE
				temporarySuccess = buildKeywordTable("keywords_extra", entityQuery, entityName, false, doDelete);
			}

			if (!temporarySuccess) {
				// if any failed, success = false
				success = false;
				System.err.println("\nERROR while inserting extra's with: " + entityQuery);
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
	private boolean appendExtraKeywords(String entityName) throws SQLException {
		String entityQuery = "SELECT table_id,keywords FROM keywords_extra " +
			"WHERE entity='" + entityName + "'";

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
		rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM (" + entityQuery + ") AS entityQuery");
		if (rs.next()) {
			count = rs.getLong("count");
		}

		if (count == 0) {
			System.out.println("No records found.");
			return true;
		}

		
		PreparedStatement pstmt;
		if (isUpdate) {
		 	pstmt = getUpdatePreparedStatement();
		} else {
		 	pstmt = getInsertPreparedStatement(kwTable, entityName);
		}

		if (doDelete) {
			System.out.println("deleting entity's current keywords");
			stmt.executeUpdate("DELETE FROM " + kwTable + " WHERE entity='" + entityName + "'");
		}

		System.out.println("Selecting " + count + " " + entityName + " entities...please wait");

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
				System.err.println("ERROR: PK field is not valid");
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
							System.err.println("Problem while trying to insert keyword for id: " +
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
		//System.out.println(pstmt.toString());
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
	private String getEntityQuery(String entityName) {

		String pkField = res.getString("pk." + entityName);
		String from = res.getString("from." + entityName);
		String select = res.getString("select." + entityName);

		if (Utility.isStringNullOrEmpty(select)) {
			System.err.println("ERROR: select." + entityName + " must be specified");
			return null;
		}

		if (Utility.isStringNullOrEmpty(from)) {
			System.err.println("ERROR: from." + entityName + " must be specified");
			return null;
		}


		if (Utility.isStringNullOrEmpty(pkField)) {
			System.err.println("ERROR: pk." + entityName + " must be specified");
			return null;
		}

		return "SELECT DISTINCT " + pkField + "," + select + " FROM " + from;
	}


	/**
	 *
	 */
	private String getEntityTable(String entityName) {
		return res.getString("table." + entityName);
	}


	/**
	 *
	 */
	private String getEntityPK(String entityName) {
		return res.getString("pk." + entityName);
	}


	private void usage(String msg) {
		System.out.println("USAGE: KeywordGen <dbname> [dbhost]");
		System.out.println("Default dbhost is 'localhost'");
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
		System.out.println("SQL: " + sql);
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

		if (args.length < 1) {
			kwg.usage("Database name is required to run.");
			System.exit(0);
		} 

		if (args.length < 2) { // host
			dbHost = "localhost";
		} else {
			dbHost = args[1];
		}

		kwg.run(args[0], dbHost);
	}

}
