/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-10-11 12:38:37 $'
 * '$Revision: 1.3 $'
 *
 *
 */
package vegclient.databasemanager;
import vegclient.databasemanager.*;

public interface DatabaseManagerPluginInterface
{
  /**
   * This method provides an interface to initilize the database
   */
  void initDatabase(String database)
		throws Exception;
	void startDatabase(String database)
		throws Exception;
	void stopDatabase()
		throws Exception;
	void createUser( String userName)
		throws Exception;
	void createDatabase( String databaseName )
		throws Exception;
	void createBaseTables( )
		throws Exception;
	void createSummaryTables( )
		throws Exception;
 // String getOutput();
  
}
