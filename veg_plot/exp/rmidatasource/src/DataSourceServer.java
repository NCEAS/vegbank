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
        
				DataSourceServerInterface fi = new DataSourceImpl("TNCPlotsDB");
        //DataSourceServerInterface fi = new DataSourceImpl("TestPlotSourcePlugin");
				
				//list the bound serveres to this host
				String[] remoteObjects = Naming.list(server);  
				for (int c = 0; c < remoteObjects.length; c++) 
				{    
					System.out.println("DataSourceServer > names already bound: "
					+ remoteObjects[c]  );
				}

				 Naming.rebind("//"+server+"/DataSourceServer", fi);
      } 
			catch(Exception e) 
			{
         System.out.println("DataSourceServer > Exception: "+e.getMessage());
         e.printStackTrace();
      }
   }
}


