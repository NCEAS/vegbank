import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*; 
import java.rmi.*;


/** 
 * class that mimics the functionality of the PlotDataSource class except that 
 * this class actually makes an RMI connection to the PlotDataSource class
 * server running on a different machine.  This class is needed because there
 * are certain types of plot data sources (e.g., MS ACCESS) that run from a 
 * specific computing platform that we want to integrate with software running
 * elsewhere, also there is a need for the clients to access data stored on a
 * serever running the vegbank databases and this tool will allow for an
 * alternative to HTTP for accessing that data
 *
 */
public class DataSourceClient
{
	
	private String serverHost =  null;
	private String serverClass = "DataSourceServer";
	DataSourceServerInterface source;
	
	//constructor
	public DataSourceClient(String hostName)
	{
		try
		{
			this.serverHost = hostName;
			String url = "//" + hostName + "/"+serverClass;
			//list the bound serveres to this host
			String[] remoteObjects = Naming.list(url);  
			for (int c = 0; c < remoteObjects.length; c++) 
			{    
				System.out.println("DataSourceCleint > names bound to: "
				+ serverHost +" - " 
				+ remoteObjects[c]  );
			}
			
			//look up the inplementation class
			source = (DataSourceServerInterface)Naming.lookup(url);
			
			//make a test
			//System.out.println("DataSourceCleint > " + source.getPlotNames() );
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
		}
	}
	
	
	/**
	 * method that returns the names of the plots stored in this data 
	 * source 
	 */
	public Vector getPlotNames()
	{
		Vector v = new Vector();
		try
		{
			v =  source.getPlotNames( );
		}
			catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
	
	public String getProjectName(String plot)
	{
		return("not really implemented");
	}
	
	
	/**
	 * main method for testing
	 */
	public static void main(String argv[]) 
	{
		//get a file from the server
		if(argv.length != 2) 
		{
			try
			{
				String hostServer =  argv[0];
				DataSourceClient client = new DataSourceClient(hostServer);
				System.out.println("DataSourceCleint > " + client.getPlotNames() );
				String plot ="VOYA.01";
				//System.out.println("DataSourceCleint > " + client.getXCoord(plot) );
				//System.out.println("DataSourceCleint > " + client.getYCoord(plot) );
			}
			catch(Exception e) 
			{
				System.err.println("Exception: "+ e.getMessage());
				e.printStackTrace();
			}		
		}
		else
		{
			try 
			{
				String name = "//" + argv[1] + "/DataSourceServer";
				DataSourceServerInterface fi = (DataSourceServerInterface)Naming.lookup(name);
				byte[] filedata = fi.downloadFile(argv[0]);
				File file = new File(argv[0]);
				BufferedOutputStream output = new
				BufferedOutputStream(new FileOutputStream(file.getName()));
				output.write(filedata,0,filedata.length);
				output.flush();
				output.close();
			} 
			catch(Exception e) 
			{
				System.err.println("FileServer exception: "+ e.getMessage());
				e.printStackTrace();
			}
		}
	}

}


