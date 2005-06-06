 /*
 *	'$RCSfile: AccessionGen.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2005-06-06 18:55:34 $'
 *	'$Revision: 1.19 $'
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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.vegbank.common.utility.CommandLineTools.StatusBarUtil;

/**
 * @author anderson
 */
public class AccessionGen {

	private static Log log = LogFactory.getLog(AccessionGen.class);

	private DBConnection conn = null;
	private static ResourceBundle res = null;

	private Map tableCodes;
	private String dbCode;
	private String CONFIRM_DEFAULT;
	private boolean overwriteExtant;


	public AccessionGen() {
		init();
        setDbCode(null);
	}

	public AccessionGen(DBConnection dbconn) {
		init();
		this.conn = dbconn;
        dbCode = Utility.getAccessionPrefix();
	}

	/**
	 * Hit some problems using this class regarding the connection
	 * not being initialized.
	 * Allowing it to be passed in via the constructor if need be. *
	 */
	public AccessionGen( DBConnection dbconn, String dbCode) {
		init();
		this.conn = dbconn;
		this.dbCode = dbCode;
	}

	private void init()
	{
		res = ResourceBundle.getBundle("accession");
		tableCodes = new HashMap();
		String key;

		// load the table abbreviations
		for (Enumeration e = res.getKeys(); e.hasMoreElements() ;) {
			key = (String)e.nextElement();
			if (key.startsWith("abbr.")) {
				tableCodes.put(key.substring(5).toLowerCase(),
                        res.getString(key));
			}
		}

		CONFIRM_DEFAULT = res.getString("confirm.default");
		if (CONFIRM_DEFAULT == null || CONFIRM_DEFAULT.equals("")) {
			CONFIRM_DEFAULT = "OK";
		}

		this.overwriteExtant = false;
	}

	/**
	 * Generates an accession code. DB.Tbl.PK#.Confirm  ex:  VB.TC.126.AKMP
	 * @param db - database code
	 * @param table - full name of the table to be abbreviated
	 * @param pk - primary key
	 */
	public String getAccession(String table, long pk) throws SQLException {
	    return getAccession(null, table, Long.toString(pk));
    }
	public String getAccession(String table, String pk) throws SQLException {
	    return getAccession(null, table, pk);
    }
	public String getAccession(String db, String table, String pk) throws SQLException {

        return buildAccession(table, pk, getConfirmation(table, pk));

        /*
		StringBuffer accCode = new StringBuffer(32);

		// Lay down the base AC e.g. VB.PL.
		accCode.append(this.getBaseAccessionCode(table));

		// primary key
		accCode.append(pk).append(".");

		// confirmation code is different for each table
		accCode.append(getConfirmation(table, pk));
		return accCode.toString();
        */

	}


    /**
     * Simply pastes together the values of an AC but doesn't touch the DB.
     * Useful if confirmation code is known before record is in DB.
     */
	public String buildAccession(String table, String pk, String unformattedConf) {

		StringBuffer accCode = new StringBuffer(32);

		// Lay down the base AC e.g. VB.PL.
		accCode.append(this.getBaseAccessionCode(table))
		    .append(pk).append(".")
            .append(formatConfirmCode(unformattedConf));

		return accCode.toString().toUpperCase();
	}

	/**
	 * Generates a confirmation code.
	 * @param table - full name of the table
	 * @param pk - primary key from given table
	 */
	public String getConfirmation(String table, String pk) throws SQLException {

		table = table.toLowerCase();
	    String tmpConfirm = null;
		String query = getConfirmationQuery(table);
		if (query == null) {
			return null;
		}

		// get the confirm type
		String confirmType = res.getString("confirm.type." + table).toUpperCase();
		// This is either WHERE or AND depending on if where is used in the SQL already
		String conjunction = "";
		if (confirmType.equals("JOIN") )
		{
			conjunction = " AND ";
		}
		else
		{
			conjunction = " WHERE ";
		}

		query += conjunction + Utility.getPKNameFromTableName(table) + " =" + pk;
		//log.debug("confirmation query ===> " + query);
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		if (rs.next()) {
			tmpConfirm = rs.getString(2);

			if (tmpConfirm == null) {
				tmpConfirm = rs.getString(3);
			}

			if (tmpConfirm == null) {
                // use the default
                tmpConfirm = CONFIRM_DEFAULT;
            } else {
                //log.debug("generated accession code: " + formatConfirmCode(tmpConfirm));
                tmpConfirm = formatConfirmCode(tmpConfirm);
            }
		}

        stmt.close();
        rs.close();

		return tmpConfirm;
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
        if (Utility.isStringNullOrEmpty(tableName)) { return null; }

		return (String)tableCodes.get(tableName.toLowerCase());
	}


    /**
     * Tests if a given table is configured to have an accession code.
     * @param tableName
     * @return true if given table has an accession code
     */
    public boolean hasAccessionCode(String tableName) {
        if (Utility.isStringNullOrEmpty(tableName)) { return false; }

		return (getTableCode(tableName) != null);
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
				if (value.equalsIgnoreCase(code)) {
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
		code = code.replaceAll("[^a-zA-Z0-9]", "");
		if (code.length() > 15) {
			code = code.substring(0, 15);
		}
		return code.toUpperCase();
	}


	//////////////////////////////////////////////////////////
	// STANDALONE
	//////////////////////////////////////////////////////////
	public void run(String dbName, String dbCode, String dbHost, boolean overwriteExtant) {
		this.dbCode = dbCode.toUpperCase();

		String dbURL = "jdbc:postgresql://"+dbHost+"/"+dbName;
		System.out.println("connect string: " + dbURL);

		this.overwriteExtant = overwriteExtant;

		try {
            if (conn == null) {
                Class.forName("org.postgresql.Driver");
                conn = new DBConnection();
                conn.setConnections(DriverManager.getConnection(dbURL, "datauser", ""));
            }

			//countRecords();

			String ow = (overwriteExtant ? " and overwrite" : "");
			String s = prompt("\nAre you sure you want to update" +
					ow + " accession codes in " +
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
	 * Updates a given table.
	 *
	 * @return true if updates were all successful
	 */
	private boolean updateTable(String tableName) throws SQLException {
		System.out.println("Updating accession codes in " + tableName);

		long count=0, tmpId;


		String baseAC, tmpConfirm;
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
        stmt.close();

		if (count == 0) {
			System.out.println("No records found.");
            rs.close();
			return true;
		}

		System.out.println("Selecting " + count + " confirmation values...please wait");
		showSQL(query);
		rs = stmt.executeQuery(query);


		// set up the progress meter
		StatusBarUtil sb = new StatusBarUtil();
		sb.setupStatusBar(count);

		PreparedStatement pstmt = getUpdatePreparedStatement(tableName);
		baseAC = getBaseAccessionCode(tableName);

		// update!
		conn.setAutoCommit(false);
		//try {
			while (rs.next()) {

				// update the status bar
				sb.updateStatusBar();

				// get the selected data
				tmpId = rs.getLong(1);
				tmpConfirm = rs.getString(2);

				if (tmpConfirm == null) {
					tmpConfirm = rs.getString(3);
				}

               //if STILL null, then get default (MTL Jun 6 2005)
               	if (tmpConfirm == null) {
               		tmpConfirm = CONFIRM_DEFAULT;
               	}

				// format
				tmpConfirm = formatConfirmCode(tmpConfirm);

				updateRowAC(tmpId, baseAC, tmpConfirm, pstmt);
			}

			// tidy up the end of the status bar
			sb.completeStatusBar(count);
		//} catch (SQLException sqlex) {
		//	log.debug("Problem updating " + tableName +"'s accession code to " + tmpConfirm);
		//}

        stmt.close();
		rs.close();
		conn.commit();

		return true;
	}



	/**
	 * Utility that updates a discrete set of rows with AccessionCodes.
	 * It takes a HashMap of tableNames.
	 * Each tableName is associated with a List of primary keys for the
	 * specific rows to be updated with newly generated AccessionCodes.
	 *
	 * @param tablesAndKeys
	 * @return List of AccessionCodes for root entities
	 * @throws SQLException
	 */
	public List updateSpecificRows(Map tablesAndKeys) throws SQLException
	{

		String tableName;
		List accessionCodeList = new ArrayList();

		Iterator it = tablesAndKeys.keySet().iterator();
		while (it.hasNext()) {
			tableName = (String)it.next();

			// Only deal with tableNames that are defined in the property file
			// i.e. filter junk and tables without accessionCode rules
            // Loop through all so that cases match
            if (hasAccessionCode(tableName)) {

                List keys = (List)tablesAndKeys.get(tableName);

                if (keys != null) {
                    // Add an AccessionCode for each Key
                    PreparedStatement pstmt = this.getUpdatePreparedStatement(tableName);

                    Iterator kit = keys.iterator();
                    while (kit.hasNext()) {
                        Long key = (Long)kit.next();
                        String baseAC = this.getBaseAccessionCode(tableName);
                        String confirmCode = getConfirmation(tableName, key.toString());

                        String accessionCode = this.updateRowAC(key.longValue(), baseAC, confirmCode, pstmt);
                        log.debug("updated accessionCode: " + accessionCode);
                        accessionCodeList.add(accessionCode);
                    }
                } else {
                    log.debug("no keys on which to gen ACs for table: " + tableName);
                }
			}
		}
		return accessionCodeList;
	}

	private String updateRowAC(long tmpId, String baseAC, String tmpConfirm, PreparedStatement pstmt) throws SQLException
	{
		StringBuffer tmpAC;
		// build the accession code
		// DB.Tbl.PK#.Confirm  ex:  VB.TC.126.AKMP
		tmpAC = new StringBuffer(32);
		tmpAC.append(baseAC).append(tmpId)
				.append(".").append(tmpConfirm);

		pstmt.setString(1, tmpAC.toString());
		pstmt.setLong(2, tmpId);
		pstmt.executeUpdate();
		return tmpAC.toString();
	}

	private PreparedStatement getUpdatePreparedStatement(String tableName) throws SQLException
	{
        if (Utility.isStringNullOrEmpty(tableName)) { return null; }
        tableName = tableName.toLowerCase();

		// prepare the update statement
		String pKName = Utility.getPKNameFromTableName(tableName);

		String update = "UPDATE " + tableName + " SET accessioncode = ? WHERE " +
				pKName + " = ?";

		if (!overwriteExtant) {
			// preserve extant codes
			update = update + " AND accessioncode IS NULL";
		}

		PreparedStatement pstmt = conn.prepareStatement(update);
		return pstmt;
	}

	private String getBaseAccessionCode(String tableName)
	{
		String baseAC;
		// table -- do case insensitive lookup
		String tableCode = getTableCode(tableName);
		if (tableCode == null) {
			tableCode = "??";
		}

        if (Utility.isStringNullOrEmpty(dbCode)) {
            setDbCode(null);
        }

		baseAC = dbCode + "." + tableCode + ".";
		return baseAC;
	}

	/**
	 *
	 */
	private String getConfirmationQuery(String tableName) {

		String query = null;
		String confirmField = res.getString("confirm." + tableName);
		String pKName = Utility.getPKNameFromTableName(tableName);

		if (confirmField == null || confirmField.equals("")) {
			return null;
		}

		// get the confirm type
		String confirmType = res.getString("confirm.type." + tableName).toUpperCase();

		if (confirmType.equals("BASIC")) {
			// select from same table
			query = "SELECT " + pKName + "," + confirmField + " FROM " + tableName;

		} else if (confirmType.equals("DUAL")) {
			// if value exists, use it, else use secondary field
			String confirmField2 = res.getString("confirm." + tableName + ".2");
			query  = "SELECT " + pKName + "," + confirmField + "," +
					confirmField2 + " FROM " + tableName;

		} else if (confirmType.equals("JOIN")) {
			// EXAMPLE QUERY:
			// SELECT c.commconcept_id, n.commname
			// FROM commname n, commconcept c
			// WHERE c.commname_id=n.commname_id;
			query = "SELECT c." +pKName + ", n." + confirmField +
					" FROM " + tableName + " c, " + confirmField + " n WHERE c." +
					confirmField + "_id=n." + confirmField + "_id";
		}

		return query;
	}

	private void usage(String msg) {
		System.out.println("USAGE: AccessionGen <dbname> [dbcode] [dbhost] [overwrite extant]");
		System.out.println("Default dbcode is 'VB'");
		System.out.println("Default dbhost is 'localhost'");
		System.out.println("Default overwrite is FALSE");
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

    public void setDbConnection(DBConnection conn) {
        this.conn = conn;
    }

    public void setDbCode(String c) {
        if (Utility.isStringNullOrEmpty(c)) {
            dbCode = Utility.getAccessionPrefix();
        } else {
            dbCode = c;
        }
    }


    public String getDbCode() {
        return dbCode;
    }


	//////////////////////////////////////////////////////////
	// MAIN
	//////////////////////////////////////////////////////////
	/**
	 *
	 */
	public static void main(String[] args) {
		AccessionGen ag = new AccessionGen();
		String dbCode, dbHost, overwriteExtant;

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

		if (args.length < 4) { // overwrite
			overwriteExtant = "false";
		} else {
			overwriteExtant = args[3].toLowerCase();
			if (!overwriteExtant.equals("false") ||
					!overwriteExtant.equals("true")) {
				ag.usage("Overwrite choice must be either 'true' or 'false'");
				System.exit(0);
			}
		}

		String dbName = args[0];

		ag.run(args[0], dbCode, dbHost, Boolean.valueOf(overwriteExtant).booleanValue());
	}

}
