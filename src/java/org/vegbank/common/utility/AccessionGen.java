/*
 *	'$RCSfile: AccessionGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-12-02 02:10:11 $'
 *	'$Revision: 1.4 $'
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

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * @author anderson
 */
public class AccessionGen {

	private Connection conn = null;
	private static ResourceBundle res = ResourceBundle.getBundle("accession");

	private HashMap tableCodes;
	private String dbCode;

	private long count;


	public AccessionGen() {
		// load the table abbreviations
		tableCodes = new HashMap();
		tableCodes.put("plantconcept", res.getString("abbr.plantconcept"));
		tableCodes.put("commconcept", res.getString("abbr.commconcept"));
		tableCodes.put("plot", res.getString("abbr.plot"));
		tableCodes.put("observation", res.getString("abbr.observation"));
		tableCodes.put("party", res.getString("abbr.party"));
		tableCodes.put("reference", res.getString("abbr.reference"));
		tableCodes.put("referenceJournal", res.getString("abbr.referenceJournal"));
		tableCodes.put("stratumMethod", res.getString("abbr.stratumMethod"));
		tableCodes.put("coverMethod", res.getString("abbr.coverMethod"));
		tableCodes.put("namedPlace", res.getString("abbr.namedPlace"));
		tableCodes.put("project", res.getString("abbr.project"));
		tableCodes.put("soilTaxon", res.getString("abbr.soilTaxon"));
		tableCodes.put("userDefined", res.getString("abbr.userDefined"));
		tableCodes.put("taxonObservation", res.getString("abbr.taxonObservation"));
		tableCodes.put("commClass", res.getString("abbr.commClass"));
		tableCodes.put("referenceParty", res.getString("abbr.referenceParty"));
	}


	/**
	 * Generates an accession code. DB.Tbl.PK#.Confirm  ex:  VB.TC.126.AKMP
	 * @param db - database code
	 * @param table - full name of the table to be abbreviated  
	 * @param pk - primary key
	 * @param decoded - common name to be encoded
	 */
	public String getAccession(String db, String table, String pk) {

		StringBuffer accCode = new StringBuffer(28);

		// database
		accCode.append(db.toUpperCase()).append(".");

		// table -- do case insensitive lookup
		String tableCode = (String)tableCodes.get( table.toLowerCase() );
		if (tableCode == null) {
			tableCode = "??";
		}
		accCode.append(tableCode).append(".");

		// primary key
		accCode.append(pk).append(".");

		// confirmation code is different for each table
		accCode.append(getConfirmation(table, pk));

		return accCode.toString();
	}

	/**
	 * Generates a confirmation code. 
	 * @param table - full name of the table to be abbreviated  
	 * @param pk - primary key
	 */
	public String getConfirmation(String table, String pk) {

		// table -- do case insensitive lookup
		String tableCode = (String)tableCodes.get( table.toLowerCase() );
		if (tableCode == null) {
			return "????";
		}

		return null;
	}

	/**
	 *
	 */
	public Map getTableCodes() {
		return tableCodes;
	}


	/**
	 * Given a full table name, returns table code (abbreviation).
	 */
	public String getTableCode(String tableName) {
		return (String)tableCodes.get(tableName);
	}


	/**
	 * Given a table code (abbreviation), returns full table name.
	 */
	public String getTableName(String code) {
		String key, value;
		if (tableCodes.containsValue(code)) {
			Iterator it = tableCodes.keySet().iterator();
			while (it.hasNext()) {
				key = (String)it.next();
				value = (String)tableCodes.get(key);
				if (value == code) {
					return key;
				}
			}
		}
		
		return null;
	}

	/**
	 * Allows only alpha numeric (other chars deleted), not longer than 15 chars total.
	 */
	public static String formatConfirmCode(String before) {
		//before.replace(".", "");

		return before;
	}


	//////////////////////////////////////////////////////////
	// STANDALONE
	//////////////////////////////////////////////////////////
	public void run(String dbName, String dbCode, String dbHost) {
		this.dbCode = dbCode;

		String dbURL = "jdbc:postgresql://"+dbHost+"/"+dbName;
		System.out.println("connect string: " + dbURL);

		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dbURL, "datauser", "");			

			//countRecords();

			String s = prompt("\nAre you sure you want to update accession codes in " +
					dbName + " on " + dbHost + " [y|n] > ");

			if (!s.equals("y")) {
				System.out.println("Thanks anyway.");
				conn.close();
				System.exit(0);
			}

			genCodes();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * When running standalone, drives the table updates.
	 */
	private void genCodes() throws SQLException {
		String tableName;

		Iterator it = tableCodes.keySet().iterator();
		while (it.hasNext()) {
			// the tableCode key is a tableName
			// generate accession codes for this table
			tableName = (String)tableCodes.get((String)it.next());
			if (!updateTable(tableName)) {
				throw new SQLException("Problem updating " + tableName);
			}
		}
	}

	/**
	 * Updates a given table.  Make sure that tableName_id is the
	 * primary key of each table.
	 *
	 * @return true if updates were all successful
	 */
	private boolean updateTable(String tableName) throws SQLException {
		System.out.println("Updating accession codes in " + tableName);

		double scalar = .8;  // width of screen
		long pct1, l=0, tmpId;

		String sqlSelect = null;
		String sqlFrom = null;
		String update, tmpConfirm;
		String confirmField = res.getString("confirm." + tableName);
		String confirmField2 = res.getString("confirm." + tableName + ".2");
		Statement stmt = conn.createStatement();
		ResultSet rs;
			
		if (confirmField == null || confirmField.equals("")) {
			return false;
		}

		// select the proper value to use as a confirmation code
		tableName = tableName.toLowerCase();

		// select from same table
		if (tableName.equals("plot") || tableName.equals("observation") || 
				tableName.equals("stratummethod") || tableName.equals("covermethod") || 
				tableName.equals("namedplace") || tableName.equals("project") || 
				tableName.equals("soiltaxon") || tableName.equals("userdefined") || 
				tableName.equals("taxonobservation") || tableName.equals("commclass") || 
				tableName.equals("commconcept")) {

			sqlSelect = "SELECT " + tableName + "_id," + confirmField + " FROM " + tableName;
		}

		// if value exists, use it, else use secondary field
		if (tableName.equals("referenceparty") || tableName.equals("party") ||
				tableName.equals("reference") || tableName.equals("referencejournal")) {

			sqlSelect  = "SELECT " + tableName + "_id," + confirmField + "," + 
					confirmField2 + " FROM " + tableName;
		}

		// do crazy stuff:
		// if scientific name without authors system exists for this concept, 
		// use the first two letters of the first word there and first 2 letters 
		// of the second word (ie ACRU for Acer rubrum), else metaphonetic code 
		// for plantName.plantName corresponding to plantConcept.plantName_ID 
		if (tableName.equals("plantconcept") || tableName.equals("plantconcept")) {
			// example query:
			// SELECT c.commconcept_id, n.commname 
			// FROM commname n, commconcept c 
			// WHERE c.commname_id=n.commname_id;
			sqlSelect = "SELECT c." + tableName + "_id, n." + confirmField;
			sqlFrom = " FROM " + tableName + " c, " + confirmField + " n WHERE c." + 
					confirmField + "_id=n." + confirmField + "_id";
		}
		
		if (sqlSelect == null) {
			return false;
		}

		System.out.println("Selecting confirmation values...please wait");

		// get confirmation values
		showSQL(sqlSelect + sqlFrom);
		rs = stmt.executeQuery(sqlSelect + sqlFrom);

		// count records
		rs = stmt.executeQuery("SELECT COUNT(*) AS count " + sqlFrom);
		if (rs.next()) {
			l = rs.getLong("count");
		}

		// set up the progress meter
		scalar = .8;  // width of display screen
		pct1 = (long)(l / 100 / scalar);
		l = 0;

		System.out.print("0% |");
		for (int i=0; i<100*scalar; i++) {
			System.out.print("-");
		}
		System.out.println("| 100%");

		// prepare the update statement
		update = "UPDATE " + tableName + " SET accessioncode = ? WHERE " + tableName + "_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(update);
		System.out.println("Updating accession codes...");
		showSQL(update);

		
		// update!
		conn.setAutoCommit(false);
		System.out.print("0% |");
		while (rs.next()) {

			// update the status bar
			if (++l < pct1) {
				// do nothing
			} else if (l % pct1 == 0) {
				System.out.print("-");
			}

			// get the selected data
			tmpId = rs.getLong(1);
			tmpConfirm = rs.getString(2);

			if (tmpConfirm == null) {
				tmpConfirm = rs.getString(3);
			}

			// set the accession code
			pstmt.setString(1, formatConfirmCode(tmpConfirm));
			pstmt.setLong(2, tmpId);
			//pstmt.executeUpdate();
		}

		rs.close();
		conn.commit();

		System.out.println("| 100%");
		System.out.println("updated " + l + "\n");
		return true;
	}

	private void usage(String msg) {
		System.out.println("USAGE: AccessionGen <dbname> [dbcode] [dbhost]");
		System.out.println("Default dbcode is 'VB'");
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
		AccessionGen ag = new AccessionGen();
		String dbCode, dbHost;

		if (args.length < 1) {
			ag.usage("Database name is required to run.");
			System.exit(0);
		} 

		if (args.length < 2) { // dbCode
			dbCode = "VB";
		} else {
			dbCode = args[1];
		}

		if (args.length < 3) { // host
			dbHost = "localhost";
		} else {
			dbHost = args[2];
		}

		String dbName = args[0];

		ag.run(args[0], dbCode, dbHost);
	}

}
