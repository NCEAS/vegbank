/*
 *	'$RCSfile: AccessionGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-12-04 02:11:59 $'
 *	'$Revision: 1.5 $'
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
import org.vegbank.common.utility.LogUtility;

/**
 * @author anderson
 */
public class AccessionGen {

	private Connection conn = null;
	private static ResourceBundle res = null;

	private HashMap tableCodes;
	private String dbCode;


	public AccessionGen() {
		res = ResourceBundle.getBundle("accession");
		tableCodes = new HashMap();
		String key;

		// load the table abbreviations
		for (Enumeration e = res.getKeys(); e.hasMoreElements() ;) {
			key = (String)e.nextElement();
			if (key.startsWith("abbr.")) {
				tableCodes.put(key.substring(5), res.getString(key));
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
	public String getAccession(String db, String table, String pk) throws SQLException {

		StringBuffer accCode = new StringBuffer(32);

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
	 * @param table - full name of the table 
	 * @param pk - primary key from given table
	 */
	public String getConfirmation(String table, String pk) throws SQLException {

		table = table.toLowerCase();
		String query = getConfirmationQuery(table);
		if (query == null) {
			return null;
		}

		query += " WHERE " + table + "_id=" + pk;
		ResultSet rs = conn.createStatement().executeQuery(query);

		if (rs.next()) {
			String tmpConfirm = rs.getString(2);

			if (tmpConfirm == null) {
				tmpConfirm = rs.getString(3);
			}

			return formatConfirmCode(tmpConfirm);
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
	public static String formatConfirmCode(String code) {
		code = code.replaceAll("[\\W]", "");
		if (code.length() > 15) {
			code = code.substring(0, 15);
		}
		return code.toUpperCase();
	}


	//////////////////////////////////////////////////////////
	// STANDALONE
	//////////////////////////////////////////////////////////
	public void run(String dbName, String dbCode, String dbHost) {
		this.dbCode = dbCode.toUpperCase();

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
			//tableName = (String)tableCodes.get((String)it.next());
			tableName = (String)it.next();
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

		double scalar = .6;  // width of screen
		long pct1, l, count=0, tmpId;
		String update, baseAC, tmpConfirm, tableCode;
		StringBuffer tmpAC = null;
		Statement stmt = conn.createStatement();
		ResultSet rs;
			
		// select the proper value to use as a confirmation code
		tableName = tableName.toLowerCase();

		// get confirmation values
		String query = getConfirmationQuery(tableName);
		if (query == null) {
			return false;
		}

		// count records
		String sqlFrom = query.substring( query.indexOf("FROM") );
		rs = stmt.executeQuery("SELECT COUNT(*) AS count " + sqlFrom);
		if (rs.next()) {
			count = rs.getLong("count");
		}

		if (count == 0) {
			System.out.println("No records found.");
			return true;
		}

		System.out.println("Selecting " + count + " confirmation values...please wait");
		showSQL(query);
		rs = stmt.executeQuery(query);

		// set up the progress meter
		boolean smallCount = false;
		pct1 = (long)((count / 100) / scalar);
		if (pct1 < 1) {
			pct1 = (long)((100 / count) * scalar);
			smallCount = true;
		} else {
			// correction
			pct1++;
		}

		System.out.print("0% |");
		for (int i=0; i<100*scalar; i++) {
			System.out.print("-");
		}
		System.out.println("| 100%  ");

		// prepare the update statement
		update = "UPDATE " + tableName + " SET accessioncode = ? WHERE " + tableName + "_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(update);

		// table -- do case insensitive lookup
		tableCode = (String)tableCodes.get( tableName.toLowerCase() );
		if (tableCode == null) {
			tableCode = "??";
		}

		baseAC = dbCode + "." + tableCode + ".";
		
		// update!
		conn.setAutoCommit(false);
		System.out.print("0% |");
		l=0;
		//try {
			while (rs.next()) {

				// update the status bar
				l++;
				if (smallCount) {
					for (int i=0; i < pct1; i++) {
						System.out.print("-");
					}

				} else {
					if (l < pct1) {
						// do nothing
					} else if (l % pct1 == 0) {
						System.out.print("-");
					}
				}

				// get the selected data
				tmpId = rs.getLong(1);
				tmpConfirm = rs.getString(2);

				if (tmpConfirm == null) {
					tmpConfirm = rs.getString(3);
				}

				// format
				tmpConfirm = formatConfirmCode(tmpConfirm);

				// build the accession code
	 			// DB.Tbl.PK#.Confirm  ex:  VB.TC.126.AKMP
				tmpAC = new StringBuffer(32);
				tmpAC.append(baseAC).append(tmpId)
						.append(".").append(tmpConfirm);

				pstmt.setString(1, tmpAC.toString());
				pstmt.setLong(2, tmpId);
				pstmt.executeUpdate();
			}

			// tidy up the end of the status bar
			if (smallCount) {
				int num = (int)(100 * scalar) - (int)pct1 * (int)l;
				for (int i=0; i < num; i++) {
					System.out.print("-");
				}
			} else {
				int num = (int)((100 * scalar) - ((int)count / (int)pct1));
				for (int i=0; i < num; i++) {
					System.out.print("-");
				}
			}
		//} catch (SQLException sqlex) {
		//	LogUtility.log("AccessionGen: Problem updating " + tableName +"'s accession code to " + tmpConfirm);
		//}

		rs.close();
		conn.commit();

		System.out.println("| 100%");
		System.out.println("updated " + l + "\n");
		return true;
	}

	/**
	 *
	 */
	private String getConfirmationQuery(String tableName) {

		String query = null;
		String confirmField = res.getString("confirm." + tableName);

		if (confirmField == null || confirmField.equals("")) {
			return null;
		}

		// get the confirm type
		String confirmType = res.getString("confirm.type." + tableName).toUpperCase();

		if (confirmType.equals("BASIC")) {
			// select from same table
			query = "SELECT " + tableName + "_id," + confirmField + " FROM " + tableName;

		} else if (confirmType.equals("DUAL")) {
			// if value exists, use it, else use secondary field
			String confirmField2 = res.getString("confirm." + tableName + ".2");
			query  = "SELECT " + tableName + "_id," + confirmField + "," + 
					confirmField2 + " FROM " + tableName;

		} else if (confirmType.equals("JOIN")) {
			// EXAMPLE QUERY:
			// SELECT c.commconcept_id, n.commname 
			// FROM commname n, commconcept c 
			// WHERE c.commname_id=n.commname_id;
			query = "SELECT c." + tableName + "_id, n." + confirmField +
					" FROM " + tableName + " c, " + confirmField + " n WHERE c." + 
					confirmField + "_id=n." + confirmField + "_id";
		}

		return query;
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
