 /*
 *	'$RCSfile: KeywordGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-10-21 15:13:25 $'
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

	private Connection conn = null;
	private static ResourceBundle res = null;

	private List entityList;


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
		String key;

		// load the entity list
		for (Enumeration e = res.getKeys(); e.hasMoreElements() ;) {
			key = (String)e.nextElement();
			if (key.startsWith("enable.")) {
				if (res.getString(key).equalsIgnoreCase("true")) {
					entityList.add(key.substring( "enable.".length()) );
				}
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

			genKeywords();

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

			if (!updateEntityKeywords(entityName)) {
				throw new SQLException("Problem updating " + entityName);
			}
		}
	}


	/**
	 * Updates all keywords for each record in given entity. 
	 *
	 * @return true if updates were all successful
	 */
	private boolean updateEntityKeywords(String entityName) throws SQLException {
		System.out.println("\n\nUpdating keywords for entity " + entityName);

		List updateList;
		long count=0, tmpId;

		ResultSet rs;
		Statement stmt = conn.createStatement();
			
		entityName = entityName.toLowerCase();

		String entityQuery = getEntityQuery(entityName);
		if (entityQuery == null) {
			return false;
		}
		System.out.println("Got entity query: " + entityQuery);

		String select = res.getString("select." + entityName);
		StringTokenizer st = new StringTokenizer(select, ",");
		int numFields = st.countTokens();


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



		/* *************************
		// get all table_id values already in keyword table for this entity
		updateList = new ArrayList();
		rs = stmt.executeQuery("SELECT table_id FROM keywords WHERE entity='" + 
				entityName + "'");
		while (rs.next()) {
			long l = rs.getLong("table_id");

			if (l != 0) {
				updateList.add(new Long(l));
			}
		}
		System.out.println(updateList.size() + " extant keyword records will be updated");
		******************** */

		
		// update!
		PreparedStatement pstmt = getPreparedStatement(entityName);
		conn.setAutoCommit(false);

		System.out.println("Deleting current entity keywords");
		stmt.executeUpdate("DELETE FROM keywords WHERE entity='" + entityName + "'");

		System.out.println("Selecting " + count + " " + entityName + " entities...please wait");
		showSQL(entityQuery);

		rs = stmt.executeQuery(entityQuery);

		// get metadata
		ResultSetMetaData rsmd = rs.getMetaData();
		numFields = rsmd.getColumnCount();
		String tmpValue, tmpField;
		StringBuffer kwDetailed;

		// set up the progress meter
		StatusBarUtil sb = new StatusBarUtil();
		sb.setupStatusBar(count);

		while (rs.next()) {

			// update the status bar
			sb.updateStatusBar();

			try {
				// first column MUST be PK for parent entity table
				tmpId = rs.getLong(1);
			} catch (Exception ex) {
				System.err.println("ERROR: PK field is not valid");
				conn.rollback();
				return false;
			}

			kwDetailed = new StringBuffer(256);

			for (int i=1; i<numFields; i++) {
				tmpValue = rs.getString(i);
				if (tmpValue != null && !tmpValue.equals("null")) {
					kwDetailed.append(KW_DELIM).append(tmpValue);
				}

			}
			
			insertRow(pstmt, tmpId, entityName, kwDetailed.toString());
		}

		// tidy up the end of the status bar
		sb.completeStatusBar(count);

		rs.close();
		conn.commit();

		return true;
	}


	private void insertRow(PreparedStatement pstmt, long tableId, 
			String entity, String detailed) throws SQLException {
		
		pstmt.setLong(1, tableId);
		pstmt.setString(2, entity);
		pstmt.setString(3, detailed);

		pstmt.executeUpdate();
	}



	/**
	 * Not in use.
	 */
	private PreparedStatement getUpdatePreparedStatement(String entityName) throws SQLException {

		return conn.prepareStatement(
				"UPDATE keywords SET table_id = ?, keywords = ? WHERE table_id = ?");
	}


	/**
	 *
	 */
	private PreparedStatement getInsertPreparedStatement(String entityName) throws SQLException {
		return conn.prepareStatement(
			"INSERT INTO keywords (table_id,entity,keywords) VALUES (?,?,?)");
	}


	/**
	 *
	 */
	private PreparedStatement getPreparedStatement(String entityName) throws SQLException {
		return getInsertPreparedStatement(entityName);
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
