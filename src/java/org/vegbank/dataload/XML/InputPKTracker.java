/*	
 * '$RCSfile: InputPKTracker.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-01 01:54:42 $'
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
package org.vegbank.dataload.XML;

import java.util.Hashtable;

import org.vegbank.common.utility.LogUtility;


class InputPKTracker
{
	private Hashtable store = new Hashtable();
	
	public void setTablesPKs(String tableName, String xmlPK, long dbPK)
	{
		Hashtable xmlPKdbPKLookup = (Hashtable)store.get(tableName);
		if ( xmlPKdbPKLookup == null )
		{
			xmlPKdbPKLookup = new Hashtable();
			store.put(tableName, xmlPKdbPKLookup);
		}
		xmlPKdbPKLookup.put(xmlPK, new Long(dbPK));
	}
	
	public long getTablesPK( String tableName, String xmlPK)
	{
		LogUtility.log("InputPKTracker is checking for xmlPK: " + xmlPK + " for table: " + tableName, LogUtility.TRACE);
		long dbPK = 0;
		Hashtable xmlPKdbPKLookup = (Hashtable)store.get(tableName);
		if ( xmlPKdbPKLookup != null )
		{
			Object tempdbPK = xmlPKdbPKLookup.get(xmlPK);
			if ( tempdbPK != null && tempdbPK instanceof Long )
			{
				dbPK = ((Long)tempdbPK).longValue();
			}
		}
		return dbPK;
	}
}