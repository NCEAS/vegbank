/*
 *	'$RCSfile: DenormUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-17 00:15:54 $'
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
}
