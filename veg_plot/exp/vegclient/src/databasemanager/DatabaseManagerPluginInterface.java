/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-10-09 22:37:47 $'
 * '$Revision: 1.1 $'
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
 // String getOutput();
  
}
