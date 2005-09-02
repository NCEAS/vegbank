/*
 *	'$RCSfile: DenormUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-09-02 23:11:46 $'
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
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.DatabaseAccess;


/**
 * Populates denormalied fields in the DB.
 * 
 * @author anderson
 */
public class DenormUtility
{
	
	protected static Log log = LogFactory.getLog(DenormUtility.class);
	private static ResourceBundle sqlStore =
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");
    private static Hashtable tableDenorms = null;


    /**
     *
     */
    public DenormUtility() {
    }

    /**
     * 
     * @return number of records updated
     */
    public static long update(String update, String denormtype, String[] whereParams)
            throws SQLException {

        StringBuffer sql = new StringBuffer(128);
        if (Utility.isStringNullOrEmpty(denormtype)) { denormtype="null";  }

        log.debug("update: " + update + ", type=" + denormtype );
        String updateSQL = sqlStore.getString(update);
        String whereSQL = "  true ";
        if (denormtype.equals("all")) {
            // no where statement needed
            whereSQL = " true ";
        } else {
            // get the where statement from SQLStore
            whereSQL = sqlStore.getString(update + "_w_" + denormtype);
            if (whereSQL.toLowerCase().startsWith("where ")) {
                // strip leading "where "
                whereSQL = whereSQL.substring("where ".length());
            }

            if (whereSQL.indexOf("{0}") != -1 ) {
                //need to substitude wparams for {0}, etc
                MessageFormat format;
                format = new MessageFormat(whereSQL);
                //declare wparam as an array
                whereSQL = format.format(whereParams);
                log.debug("swapped wparams for {0} stuff: " + whereSQL );
            }
        }

        sql.append(updateSQL).append(" AND ").append(whereSQL);


        log.debug("running SQL: " + sql.toString());
        DatabaseAccess da = new DatabaseAccess();
        long results = da.issueUpdate(DatabaseUtility.removeSemicolons(sql.toString()));
        log.debug("SQL IS DONE.  Result:" + results );

        return results;
    }

    /**
     * Run all queries in sql store that begin with dnrm (and don't have _w_).
     */
    public static long updateAll() 
            throws SQLException {
        return updateAll(null);
    }

    /**
     * Run all queries in sql store that begin with dnrm (and don't have _w_)
     * for a given table (or null to do all tables).
     * @param null or table name
     */
    public static long updateAll(String table) 
            throws SQLException {
        if (tableDenorms == null) {
            init();
        }

        long count = 0;
        Vector v;
        if (table != null) {
            // update ONE table
            count = updateTable(table);
        } else {
            // update ALL tables
            Iterator tables = tableDenorms.keySet().iterator();
            while (tables.hasNext()) {
                count += updateTable((String)tables.next());
            }
        }
            
        return count;
    }

    /**
     * Runs all denorms for one table.
     */
    public static long updateTable(String table) throws SQLException {
        return updateTable(table, null, null);
    }

    public static long updateTable(String table, String denormtype, String[] whereParams) 
            throws SQLException {

        if (tableDenorms == null) {
            init();
        }

        long count = 0;

        Vector v = (Vector)tableDenorms.get(table);
        if (v == null) { return 0; }

        Iterator it = v.iterator();
        while (it.hasNext()) {
            count += update((String)it.next(), denormtype, whereParams);
        }

        return count;
    }

    /**
     *
     */
    private static void init() {
        tableDenorms = new Hashtable();
        Hashtable unsorted = new Hashtable();
        String table = null;
        Vector v;

		// load the entity list
		for (Enumeration e = sqlStore.getKeys(); e.hasMoreElements() ;) {
			String key = (String)e.nextElement();
			if (key.startsWith("dnrm-") && key.indexOf("_w_") == -1) {

                try {
                    StringTokenizer st = new StringTokenizer(key, "-");

                    if (st.hasMoreTokens()) { st.nextToken(); }
                    if (st.hasMoreTokens()) { table = st.nextToken(); }

				    v = (Vector)unsorted.get(table);

                    if (v == null) { v = new Vector(); }
                    v.add(key);
                    log.debug("init: adding denorm for " + table);

				    unsorted.put(table, v);

                }  catch (Exception ex) {
                    log.debug("Can't parse denorm key: " + key);
                }
			}

		}

        // sort them
        Iterator kit = unsorted.keySet().iterator();
        while (kit.hasNext()) {
            table = (String)kit.next();
            v = (Vector)unsorted.get(table);
            Collections.sort(v);
            tableDenorms.put(table, v);
            log.debug("sorted: " + v.toString());
        }

        log.debug("INIT COMPLETE");
    }
}
