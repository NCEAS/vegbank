/*
 *	'$RCSfile: AccessionGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-10-30 23:35:46 $'
 *	'$Revision: 1.1 $'
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

	public DoubleMetaphone phone = new DoubleMetaphone();

	private HashMap tableCodes;
	private String tableType;
	private String dbCode;
	private String dbNameTable;
	private String dbConceptTable;
	private long count;

	public AccessionGen() {
		tableCodes = new HashMap();
		tableCodes.put("plantconcept", "PC");
		tableCodes.put("commconcept", "CC");
	}

	public static void main(String[] args) {
		AccessionGen ag = new AccessionGen();
		String dbCode, dbHost;

		if (args.length < 2) {
			ag.usage("Database name and entity type are required to run.");
			System.exit(0);
		} 
		if (args.length < 3) {
			// dbCode
			dbCode = "VB";
		} else {
			dbCode = args[2];
		}

		if (args.length < 4) {
			// host
			dbHost = "localhost";
		} else {
			dbHost = args[3];
		}

		String dbName = args[0];

		if (!args[1].equals("plant") && !args[1].equals("comm")) {
			ag.usage("Please choose the proper entity type, either 'plant' or 'comm'.");
			System.exit(0);
		}


		ag.run(args[0], args[1], dbCode, dbHost);
	}


	public void run(String dbName, String tableType, String dbCode, String dbHost) {
		this.tableType = tableType;
		this.dbNameTable = tableType + "name";
		this.dbConceptTable = tableType + "concept";
		this.dbCode = dbCode;

		String dbURL = "jdbc:postgresql://"+dbHost+"/"+dbName;
		System.out.println("connect string: " + dbURL);

		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dbURL, "datauser", "");			

			countRecords();

			String s = prompt("\nAre you sure you want to update accession codes in " +
					dbName + " DB's " + dbConceptTable + " table on " + dbHost + " [y|n] > ");

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
	private void genCodes() throws SQLException {
		System.out.println("==========");
		System.out.println("Generating accession codes in " + dbConceptTable);
		String select, update, tmpName, str;
		long tmpId;

		// set up the progress meter
		final double scalar = .8;  // width of screen
		long pct1  = (long)(count / 100 / scalar);
		//long pct5  = (long)(count / 20 / scalar);
		long l = 0;
		int pctCount = 0;


		// build the select statement
		select = "SELECT c." + dbConceptTable + "_id, n." + tableType + 
			"name FROM " + dbConceptTable + " c, " + dbNameTable + 
			" n WHERE n." + dbNameTable + "_id=c." + dbNameTable + "_id";
		System.out.println("Selecting names...please wait");
		showSQL(select);
		ResultSet rs = conn.createStatement().executeQuery( select );

		System.out.print("0% |");
		for (int i=0; i<100*scalar; i++) {
			System.out.print("-");
		}
		System.out.println("| 100%");


		// prepare the update statement
		update = "UPDATE " + dbConceptTable + 
			" SET accessioncode = ? WHERE " + tableType + "concept_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(update);
		System.out.println("Updating accession codes...");
		showSQL(update);

		
		// update!
		conn.setAutoCommit(false);
		System.out.print("0% |");
		while (rs.next() != false) {

			l++;

			tmpId = rs.getLong(1);
			tmpName = rs.getString(2);

			if (l < pct1) {
				// do nothing
			} else if (l % pct1 == 0) {
				System.out.print("-");
			}

			// set the accession code
			pstmt.setString(1, getCode(dbCode, dbConceptTable, Long.toString(tmpId), tmpName));
			pstmt.setLong(2, tmpId);
			pstmt.executeUpdate();
		}
		System.out.println("| 100%");
		System.out.println("updated " + l + "\n");
		conn.commit();
	}


	/**
	 * Generates an accession code. ex:  VB.TC.126.AKMP
	 * @param db - database code
	 * @param table - full name of the table to be abbreviated
	 * @param pk - primary key
	 * @param decoded - common name to be encoded
	 */
	public String getCode(String db, String table, String pk, String decoded) {

		StringBuffer accCode = new StringBuffer(28);

		// database
		accCode.append(db.toUpperCase()).append(".");

		// table
		String tableCode = (String)tableCodes.get(table);
		if (tableCode == null) {
			tableCode = "??";
		}
		accCode.append(tableCode).append(".");

		// primary key
		accCode.append(pk).append(".");

		// code
		String code = phone.doubleMetaphone(decoded);
		if (code == null || code.equals("")) {
			code = "X";
		}
		accCode.append(code);

		return accCode.toString();
	}

	public Map getTableCodes() {
		return tableCodes;
	}


	private void usage(String msg) {
			System.out.println("USAGE: AccessionGen <dbname> 'plant|community' <dbcode> <dbhost>");
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

}
