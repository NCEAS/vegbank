package org.vegbank.plots.rmi;

import org.vegbank.plots.rmi.DataSourceImpl;
import org.vegbank.plots.rmi.DataSourceServerInterface;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

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


