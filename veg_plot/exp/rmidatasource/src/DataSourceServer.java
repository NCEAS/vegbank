/**
 * '$RCSfile: DataSourceServer.java,v $'
 *
 * Purpose: 
 *
 * '$Author: farrell $'
 * '$Date: 2003-08-21 21:16:43 $'
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


import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*;
import java.rmi.*;

public class DataSourceServer 
{
	
	
	/**
	 * method to start the server and specifies the plugin name to 
	 * be used in the 'PlotDataSource' interface
	 */
   public static void main(String argv[]) 
	 {
      if(System.getSecurityManager() == null) 
			{
				
				 System.out.println("DataSourceServer > starting a new security manager: ");
         System.setSecurityManager(new RMISecurityManager());
      }
      try 
			{
				//this is the name (or IP addy) of the server to bind to
				String server = "127.0.0.1";
				int port = 1099;
				System.out.print("DataSourceServer > binding to host: " + server);
				System.out.println(" on port: " + port);
        
				DataSourceServerInterface fi = new DataSourceImpl("NativeXmlPlugin");
        //DataSourceServerInterface fi = new DataSourceImpl("TestPlotSourcePlugin");
				
				//list the bound serveres to this host
				String[] remoteObjects = Naming.list(server);  
				for (int c = 0; c < remoteObjects.length; c++) 
				{    
					System.out.println("DataSourceServer > names already bound: "
					+ remoteObjects[c]  );
				}

				 Naming.rebind("//"+server+":"+port+"/DataSourceServer", fi);
      } 
			catch(Exception e) 
			{
         System.out.println("DataSourceServer > Exception: "+e.getMessage());
         e.printStackTrace();
      }
   }
}


