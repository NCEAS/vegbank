/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2003-08-21 21:16:45 $'
 * '$Revision: 1.2 $'
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
package org.vegbank.servlet.framework;

import java.util.Hashtable;

public interface ServletPluginInterface
{
	/**
	 * This method provides an interface to initilize the database
	 */
	StringBuffer servletRequestHandler(String action, Hashtable params)
		throws Exception;
	//	void startDatabase(String database)
	//		throws Exception;
	//	void stopDatabase()
	//		throws Exception;
	//	void createUser( String userName)
	//		throws Exception;
	//	void createDatabase( String databaseName )
	//		throws Exception;
	//	void createBaseTables( )
	//		throws Exception;
	//	void createSummaryTables( )
	//		throws Exception;
	// String getOutput();

}
