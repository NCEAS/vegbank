/*	
 * '$RCSfile: InputPKTracker.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-01-29 01:06:54 $'
 *	'$Revision: 1.5 $'
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class InputPKTracker
{
	private static Log log = LogFactory.getLog(InputPKTracker.class);

	private Hashtable store = new Hashtable();
	
	public void setAssignedPK(String tableName, String xmlPK, long dbPK)
	{
		Hashtable xmlPKdbPKLookup = (Hashtable)store.get(tableName);
		if ( xmlPKdbPKLookup == null )
		{
			xmlPKdbPKLookup = new Hashtable();
			store.put(tableName, xmlPKdbPKLookup);
		}
		xmlPKdbPKLookup.put(xmlPK, new Long(dbPK));
	}
	
	public long getAssignedPK( String tableName, String xmlPK)
	{
		//log.debug("InputPKTracker is checking for xmlPK: " + xmlPK + " for table: " + tableName);
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
