/**
 * '$RCSfile: PostgresqlAdapter.java,v $'    
 * Purpose: An adapter class for PostgreSQL RDBMS.
 *
 * '$Author: farrell $'     
 * '$Date: 2003-10-17 22:09:14 $' 
 * '$Revision: 1.6 $'
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
	public int getNextUniqueID(
		DBConnection conn,
		String tableName,
		String primaryKeyName)
		throws SQLException
	{
		int uniqueid = 0;
		Statement stmt = conn.createStatement();
		// constructing the name of the seq to call...
		stmt.execute(
			"SELECT nextval('" + getSequenceColumnName(primaryKeyName) + "')");
		ResultSet rs = stmt.getResultSet();
		if (rs.next())
		{
			uniqueid = rs.getInt(1);
		}
		stmt.close();

		return uniqueid;
	}

	/**
	 * Creates the sequence name from the primarykey name by appended 
	 * a _seq. Handles the 32Char limit on names in Postgres by trunction.
	 * 
	 * @param pKName
	 * @return String
	 */
	public String getSequenceColumnName(String pKName)
	{
		String result = "";
		
		// Max columnLenght is 32 (but -4 to account for '_seq' ) 
		int maxAvailibleLength = 27;
		
		int nameLength =  pKName.length();
		//	Do I  need to truncate ?
		 while ( nameLength - maxAvailibleLength > 0)
		 {
			pKName = pKName.substring(0, pKName.length() - 1); 
			// reset Name Lenght
			nameLength = pKName.length();
		 }
		 
		result = pKName + "_seq";
		return result;
	}

	/**
	 * Postgresql pre 7.3  has a 32 character limit on column names 
	 * This is a ugly hack that attemps to reverse engineer what they are up to.
	 * <br/>
	 * 
	 * Postgresql 7.3 has a 64 character limit so upgrading to this in essence 
	 * solves this, also it is posible to directly specify the sequence name in the 
	 * SQL builds scripts. <br/>
	 * 
	 * I think the algorithm use by postgres truncateds the longest subpart of the 
	 * sequence column name ( tablename and pkname ) until it fits. I think the pk
	 * is truncated first in the event they are the same lenght. <br/>
	 * 
	 * 
	 * @param tableName
	 * @param pKName
	 * @return String
	 * @deprecated using getSequenceColumnName( String pKName )
	 */
	public String getSequenceColumnName(String tableName, String pKName)
	{
		String result = "";
		
    // Max columnLenght is 32 (but -4 to account for '_seq'  and -1 for the '_' to join the tableName and PK) 
    int maxAvailibleLength = 26;
    
		int nameLength = tableName.length() + pKName.length();

		// Do I  need to truncate ?
		if ( nameLength - maxAvailibleLength > 0)
		{

			while ( nameLength - maxAvailibleLength > 0 )
			{
        //Truncate the longest value starting with key in a tie
        if ( pKName.length() >=  tableName.length() )
        {
					pKName = pKName.substring(0, pKName.length() - 1);
        }
        else
        {
					tableName =	tableName.substring(0, tableName.length() - 1);
        }
				
				// reset Name Lenght
				nameLength = tableName.length() + pKName.length();
				//System.out.println(pKName.length() + " and " + maxAvailibleLength + " and " + nameLength + " and " +  modifiedPKName.length() + " and " + modifiedTableName.length());
			}
		}

		result = tableName  + "_" + pKName + "_seq";
		return result;
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
