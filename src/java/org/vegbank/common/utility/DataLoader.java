/*
 *	'$RCSfile: DataLoader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-17 22:09:14 $'
 *	'$Revision: 1.4 $'
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

import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author farrell
 */

public class DataLoader
{

	/**
	 * method that will return a database connection for use with the database
	 *
	 * @return conn -- an active connection
	 */
	private static DBConnection getDBConnection() throws SQLException
	{
		DBConnection c = DBConnectionPool.getDBConnection("Need connection for inserting dataset");
		return c;
	}

	public static void main(String[] args)
	{
		String mode = "";
		String databaseHost = "localhost";
		String databaseName = "";
		String fileName = "";
		DBConnection conn = null;

		if (args.length < 4)
		{
			System.out.println(
				"USAGE: DataLoader [xml | db ] [databaseHost] [databaseName] [xmldatafile]");
			System.exit(0);
		}
		else
		{
			mode = args[0];
			databaseHost = args[1];
			databaseName = args[2];
			fileName = args[3];
		}
		try
		{
			conn = DataLoader.getDBConnection();

			DataLoader dl = new DataLoader();
			XMLToObject x2o = new XMLToObject(fileName);
			AbstractList objectList = x2o.getGeneratedObjects();

			System.out.println(
				"DataLoader > Object Total =" + objectList.size());

			Hashtable foreignKeys = new Hashtable();

			Iterator i = objectList.iterator();
			while (i.hasNext())
			{
				System.out.println("DataLoader > I am writing a new object");
				if (mode.toUpperCase().equals("DB"))
				{
					ObjectToDB o2db = new ObjectToDB(i.next());
					o2db.setForeignKeys(foreignKeys);
					o2db.insert();
					foreignKeys.put(
						o2db.getPrimaryKeyFieldName(),
						new Integer(o2db.getPrimaryKey()));
				}
				else
				{
					ObjectToXML o2X = new ObjectToXML(i.next());
					System.out.println(o2X.getXML());
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
