/*
 * Created on Dec 6, 2003
 *
 * '$RCSfile: ExpandStatusStore.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-02-27 21:39:57 $'
 *	'$Revision: 1.2 $'
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
package org.vegbank.ui.struts;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Stores the expand contract status for each entity.
 * Also Stores the root entity being displayed
 * 
 * @author Gabriel Farrell
 * @version '$Revision: 1.2 $' '$Date: 2004-02-27 21:39:57 $'
 */
public class ExpandStatusStore implements Serializable
{
	private HashMap expandStatusStoreMap = new HashMap();
	public int NODE_CONTRACTED = 0;
	public int NODE_EXPANDED = 1;
	
	public int getExpandStatus( String nodeAddress)
	{
		// Node is contracted by default
		int status = NODE_CONTRACTED;
		
		if ( expandStatusStoreMap.containsKey(nodeAddress) )
		{	
			status = ((Integer) expandStatusStoreMap.get(nodeAddress)).intValue();
		}
		
		return status;
	}
	
	public void setNodeContracted(String nodeAddress)
	{
		expandStatusStoreMap.put(nodeAddress, new Integer(NODE_CONTRACTED) );
	}
	
	public void setNodeExpanded(String nodeAddress)
	{
		expandStatusStoreMap.put(nodeAddress, new Integer(NODE_EXPANDED) );
	}
}
