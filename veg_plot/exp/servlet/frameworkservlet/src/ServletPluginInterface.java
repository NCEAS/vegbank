/**
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-11-22 18:56:18 $'
 * '$Revision: 1.1 $'
 *
 *
 */
package servlet.framework;
 
import java.io.*;
import java.text.*;
import java.util.*;

import servlet.framework.*;

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
