/*
 *	'$RCSfile: AccessionGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-11-26 00:43:03 $'
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

import java.io.*;
import java.sql.*;
import java.util.*;
import org.apache.commons.codec.language.DoubleMetaphone;

/**
 * @author anderson
 */

public class AccessionGen {

	Connection conn = null;
	private static ResourceBundle res = ResourceBundle.getBundle("accession");

	public DoubleMetaphone phone = new DoubleMetaphone();

	private HashMap tableCodes;
	private String dbCode;
	private String databaseName;

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

	public void run(String dbName, String dbCode, String dbHost) {
		//this.dbNameTable = tableType + "name";
		//this.dbConceptTable = tableType + "concept";
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
	 * Updates a given table.  Make sure that tableName_id is the
	 * primary key of each table.
	 *
	 * @return true if updates were all successful
	 */
	private boolean updateTable(String tableName) throws SQLException {
		System.out.println("Updating accession codes in " + tableName);

		long tmpId;
		String select, update, tmpConfirm;
		String confirmField = res.getString("confirm." + tableName);
		String confirmField2 = res.getString("confirm." + tableName + ".2");

		if (confirmField == null || confirmField.equals("")) {
			return false;
		}

		// select the proper value to use as a confirmation code
		tableName = tableName.toLowerCase();

		// select from same table
		if (tableName.equals("plot") ||
				tableName.equals("observation") || tableName.equals("stratummethod") ||
				tableName.equals("covermethod") || tableName.equals("namedplace") ||
				tableName.equals("project") || tableName.equals("soiltaxon") ||
				tableName.equals("userdefined") || tableName.equals("taxonobservation") ||
				tableName.equals("commclass") || tableName.equals("commconcept")) {

			select = "SELECT " + tableName + "_id," + confirmField + " FROM " + tableName;
		}

		// {1} if exists, else {2}
		if (tableName.equals("referenceparty") || tableName.equals("party") ||
				tableName.equals("reference") || tableName.equals("referencejournal")) {

			select  = "SELECT " + tableName + "_id," + confirmField + "," + 
					confirmField2 + " FROM " + tableName;
		}

		// do crazy stuff
		if (tableName.equals("plantconcept") || tableName.equals("plantconcept")) {
//if scientific name without authors system exists for this concept, use the first two letters of the first word there and first 2 letters of the second word (ie ACRU for Acer rubrum), else metaphonetic code for plantName.plantName corresponding to plantConcept.plantName_ID 

			// SELECT c.commconcept_id, n.commname 
			// FROM commname n, commconcept c 
			// WHERE c.commname_id=n.commname_id;
			select = "SELECT c." + tableName + "_id, n." + confirmField + " FROM " + 
					tableName + " c, " + confirmField + " n WHERE c." + confirmField + 
					"_id=n." + confirmField + "_id";

		}
		
		if (select == null) {
			return false;
		}

		select += " LIMIT 1";
		System.out.println("Selecting confirmation values...please wait");
		showSQL(select);
		ResultSet rs = conn.createStatement().executeQuery( select );

		// set up the progress meter
		final double scalar = .8;  // width of screen
//		long pct1  = (long)(rs.getCount() / 100 / scalar);
		long l = 0;

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
		while (rs.next() != false) {

			// update the status bar
			l++;
			if (l < pct1) {
				// do nothing
			} else if (l % pct1 == 0) {
				System.out.print("-");
			}

			// set the accession code
			tmpId = rs.getLong(1);
			tmpConfirm = rs.getString(2);
			if (tmpConfirm == null) {
				tmpConfirm = rs.getString(3);
			}

			// no longer uses Metaphone
			//pstmt.setString(1, getAccession(dbCode, dbConceptTable, Long.toString(tmpId), tmpName));

			pstmt.setString(1, formatConfirmCode(tmpConfirm));
			pstmt.setLong(2, tmpId);
			//pstmt.executeUpdate();
		}
		System.out.println("| 100%");
		System.out.println("updated " + l + "\n");
		conn.commit();
	}


	/**
	 *
	 */
	public static String formatConfirmCode(String before) {



	}

	/**
	 *
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
	 * Generates an accession code. DB.Tbl.PK#.Confirm  ex:  VB.TC.126.AKMP
	 * @param db - database code
	 * @param table - full name of the table to be abbreviated  
	 * @param pk - primary key
	 * @param decoded - common name to be encoded
	 */
	public String getAccession(String db, String table, String pk, String decoded) {

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
		String code = phone.doubleMetaphone(decoded);
		if (code == null || code.equals("")) {
			code = "X";
		}
		accCode.append(code);

		return accCode.toString();
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
				key = (String)tableCodes.get((String)it.next());
				if (value == code) {
					return key;
				}
			}
		}
		
		return null;
	}


	///////////////////// ///////////////////// /////////////////////

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

	/*
	private void countRecords() throws SQLException {
		System.out.println("Counting records in " + dbConceptTable);
		String query = "SELECT COUNT(*) FROM " + dbConceptTable;
		showSQL(query);
		ResultSet dbresults = conn.createStatement().executeQuery( query );

		if (dbresults.next()) {
			count = dbresults.getLong(1);
			System.out.println("Found " + count + " records");
		} else {
			System.out.println("no records found");
		}
	}
	*/

	/**
	 *
	 */
	public String getDatabaseName() {
		return databaseName;
	}


	/**
	 *
	 */
	public void setDatabaseName(String s) {
		this.databaseName = s;
	}


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
