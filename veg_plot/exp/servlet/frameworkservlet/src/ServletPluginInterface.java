/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2003-01-14 01:12:41 $'
 * '$Revision: 1.2 $'
 *
 *
 */
package servlet.framework;

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
