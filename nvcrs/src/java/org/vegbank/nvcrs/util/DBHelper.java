/*
 * '$Id: DBHelper.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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

package org.vegbank.nvcrs.util;

import java.sql.*;
import javax.sql.*;
import org.vegbank.nvcrs.util.*;

public final class DBHelper {

    
    public static final String getMaxUsrId(Connection con) 
        throws Exception {

        Debug.print("DBHelper getNextUsrId");
        return getMaxId(con, "usr","USR_ID");
    } // getNextAccountId

    

    public static final String getMaxId(Connection con, String table,String idField)
        throws Exception {

        Debug.print("DBHelper getNextId");

        String selectStatement =
                "select max(" + idField +") from " + table;
        
        PreparedStatement prepSelect =
                con.prepareStatement(selectStatement);
        
        ResultSet rs = prepSelect.executeQuery();
        rs.next();
        int i = rs.getInt(1);
        rs.close();
        prepSelect.close();
        
        if (i <= 0) {
            i=0;
            //throw new MissingPrimaryKeyException
            //("Table " + table + " is empty.");
        }

        return Integer.toString(i);

    } // getNextId


    public static final java.sql.Date toSQLDate(java.util.Date inDate) {

        // This method returns a sql.Date version of the util.Date arg.

        return new java.sql.Date(inDate.getTime());
    }


} // class 
