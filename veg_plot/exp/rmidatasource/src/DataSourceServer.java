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
         System.setSecurityManager(new RMISecurityManager());
      }
      try 
			{
         //DataSourceServerInterface fi = new DataSourceImpl("TNCPlotsDB");
         DataSourceServerInterface fi = new DataSourceImpl("TestPlotSourcePlugin");
				 Naming.rebind("//127.0.0.1/DataSourceServer", fi);
      } catch(Exception e) 
			{
         System.out.println("FileServer: "+e.getMessage());
         e.printStackTrace();
      }
   }
}


