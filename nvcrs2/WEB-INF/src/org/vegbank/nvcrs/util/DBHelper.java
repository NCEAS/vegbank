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
