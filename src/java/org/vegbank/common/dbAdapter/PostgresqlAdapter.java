/**
 * '$RCSfile: PostgresqlAdapter.java,v $'    
 * Purpose: An adapter class for PostgreSQL RDBMS.
 *
 * '$Author: farrell $'     
 * '$Date: 2004-01-18 20:47:47 $' 
 * '$Revision: 1.8 $'
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

package org.vegbank.common.dbAdapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.vegbank.common.utility.DBConnection;

/**
 * The PostgreSQL db adapter implementation.
 * Stolen from Metacat.
 */
public class PostgresqlAdapter extends AbstractDatabase
{

	/* (non-Javadoc)
	 * @see org.vegbank.common.dbAdapter.AbstractDatabase#getNextUniqueID(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	/**
	 * The PostgreSQL unique ID /sequence generator
	 * The name of the sequence used to generate the unique id 
	 * is made from the name of the table that uses the id by 
	 * appending "_id_seq" to it.
	 * When record is inserted in the table and insert trigger gets
	 * a nextval from that sequence, select currval of that sequence
	 * can return the generated key in the same db connection.
	 *
	 * @param conn db connection in which the unique id was generated
	 * @param tableName the name of table which unique id was generate
	 * @exception SQLException any SQLException that can be thrown 
	 *            during the db operation
	 * @return return the generated unique id as a long type
	 */
	public long getNextUniqueID(
		DBConnection conn,
		String tableName,
		String primaryKeyName)
		throws SQLException
	{
		long uniqueid = 0;
		Statement stmt = conn.createStatement();
		// constructing the name of the seq to call...
		stmt.execute(
			"SELECT nextval('" + getSequenceColumnName(tableName, primaryKeyName) + "')");
		ResultSet rs = stmt.getResultSet();
		if (rs.next())
		{
			uniqueid = rs.getLong(1);
		}
		stmt.close();

		return uniqueid;
	}

	/**
	 * There is a limit on the number of characters in a fieldname 
	 * which should be checked here but is not FIXME:
	 * 
	 * @param pKName
	 * @return String
	 */
	private String getSequenceColumnName(String tableName, String pKName)
	{
		return tableName + "_" + pKName + "_seq";
	}
	
	/**
	 * The PostgreSQL function name that gets the current date 
	 * and time from the database server
	 *
	 * @return return the current date and time function name: "now()"
	 */
	public String getDateTimeFunction()
	{
		return "now()";
	}

	/**
	 * The PostgreSQL function name that is used to return non-NULL value
	 *
	 * @return return the non-NULL function name: "coalesce"
	 */
	public String getIsNULLFunction()
	{

		return "coalesce";
	}

	/**
	 * PostgreSQL's string delimiter character: single quote (')
	 *
	 * @return return the string delimiter: single quote (')
	 */
	public String getStringDelimiter()
	{

		return "\"";
	}

}
