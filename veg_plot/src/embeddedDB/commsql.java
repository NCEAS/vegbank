/*-----------------------------------------------------------------------------
 *
 * Enhydra InstantDB
 * The Initial Developer of the Original Code is Lutris Technologies Inc.
 * Portions created by Lutris are Copyright 1997-2000 Lutris Technologies Inc.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Enhydra Public License
 * Version 1.1 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * www.enhydra.org/license/epl.html
 *
 * Software distributed under the License is distributed on an "ASIS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * -----------------------------------------------------------------------------
 * $Id: commsql.java,v 1.1 2001-05-21 17:05:42 harris Exp $
 * -----------------------------------------------------------------------------
 */
//package org.enhydra.instantdb;

import java.net.URL;
import java.sql.*;
import org.enhydra.instantdb.jdbc.*;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.util.StringTokenizer;
import java.sql.*; 
import java.io.*; 
import java.util.*;
import java.math.*; 
import java.net.URL;

class commsql {

	public static void main (String args[]) {

		ResourceBundle rb = ResourceBundle.getBundle("LocalDatabaseManager");
		String database=rb.getString("database");
		
		String url   = "jdbc:idb:../../../data/nvc.prp";
		//jdbc:idb:../../../data/sample.prp
		try {
			
			// Uncomment the following if you want to enable driver-manager logging
			// java.sql.DriverManager.setLogStream(java.lang.System.out);

			idbDriver idb1=new idbDriver();
			
			//jhh removed below 
			//Class.forName ("org.enhydra.instantdb.jdbc.idbDriver").newInstance();
			//System.out.println ("Enter the url for the database");
			byte[] inBytes = new byte[512];
			//System.in.read (inBytes);
			//url = new String (inBytes,0);
			//url = url.trim();
			
			//jhh addded
			String driver_class = "org.enhydra.instantdb.jdbc.idbDriver";
			Class.forName(driver_class); 
			//assume that the idb database is alwas in the following relative directory
			Connection	con = DriverManager.getConnection("jdbc:idb:"+database);
			System.out.println("Connected.");
			
			//Connection con = DriverManager.getConnection (url);
			
			// Get the DatabaseMetaData object and display
			// some information about the connection

			DatabaseMetaData dma = con.getMetaData ();

			System.out.println("\nConnected to " + dma.getURL());
			System.out.println("Driver   " + dma.getDriverName());
			System.out.println("Version  " + dma.getDriverVersion());
			System.out.println("");

			// Create a Statement object so we can submit
			// SQL statements to the driver

			Statement stmt = con.createStatement ();

			while (true) {
				System.out.println ("Enter SQL string, or . to exit");
				inBytes = new byte[512];
				System.in.read (inBytes);
				String SQLstr = new String (inBytes,0);
				SQLstr = SQLstr.trim();
				if (SQLstr.startsWith(".")) break;
				try {
					if (stmt.execute(SQLstr)) {
						// Result set available
						ResultSet rs = stmt.getResultSet();
						dispResultSet (rs);
						rs.close();
					} // if
				} catch (SQLException e) {
					e.printStackTrace ();
				} // try-catch
			} // while

			// Close the statement

			stmt.close();

			// Close the connection

			con.close();
		}
		catch (Exception ex) {
			ex.printStackTrace ();
		}
	}

	/**
	 * Displays the current row of data.
	 */
	private static void dispRow (ResultSet rs, int numCols) throws SQLException {
		for (int i=1; i<=numCols; i++) {
			if (i > 1) System.out.print(",");
			System.out.print(rs.getString(i));
		}
		System.out.println("");
	} // method dispRow


	//-------------------------------------------------------------------
	// dispResultSet
	// Displays all columns and rows in the given result set
	//-------------------------------------------------------------------

	private static void dispResultSet (ResultSet rs) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCols = rsmd.getColumnCount ();

		// Cast to an InstantDB results set.
		// ***************************************************
		// Note: getRowCount(), setCurRow(int) and getCurRow()
		// are InstantDB specific calls. JDBC has no standard
		// methods for navigating results sets - pity :-(
		// ***************************************************

		idbResultsSet idbRs = (idbResultsSet) rs;
		int numRows = idbRs.getRowCount();
		System.out.println (numRows+" rows returned");
		System.out.print ("last  row: ");
		idbRs.setCurRow (numRows);
		idbRs.next();
		dispRow (rs, numCols);
		System.out.print ("first row: ");
		idbRs.setCurRow (1);
		idbRs.next();
		dispRow (rs, numCols);
		idbRs.setCurRow (1);
		System.out.println ("full results: ");

		for (int i=1; i<=numCols; i++) {
			if (i > 1) System.out.print(",");
			System.out.print(rsmd.getColumnLabel(i));
		}
		System.out.println("");
		
		int curRow = idbRs.getCurRow();
		while (rs.next ()) {
			System.out.print ("Row "+curRow+": ");
			dispRow (rs, numCols);
			curRow = idbRs.getCurRow();
		} // while
	}
}
