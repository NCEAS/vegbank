package org.vegbank.servlet.plugin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import org.vegbank.servlet.framework.ServletPluginInterface;

public class PlantNameList implements ServletPluginInterface
{
	
	Connection conn = null;
	
	
	public PlantNameList(  ) 
	{
		System.out.println( "PlantNameList > PlantNameList accessed by framework servlet");
		System.out.println("PlantNameList > initialized in the directory: "
		+ System.getProperty("user.dir"));
		this.conn = this.getConnection();
	}
	
	
	
	public StringBuffer servletRequestHandler(String action, Hashtable params)
	{
		StringBuffer sb = new StringBuffer();
		System.out.println("PlantNameList >  Action: " + action);
		if (! action.trim().equals("plantlookup") )
		{
			sb.append( getRequiredParameters() );
		}
		else
		{
	  	try 
			{
				String segment = (String)params.get("namesegment");
				Vector v = getPlantNames(segment);
				int row = 0;
				
				sb.append("<html> \n");
				sb.append("<body> \n");
				sb.append("<p> Results: "+ v.size() +"</p> \n");
				sb.append(" <table> \n");
				
				
				
				for (int i=0; i<v.size(); i++) 
				{
					if ( row == 0 )
					{
						sb.append("<tr> <td bgcolor=\"#9999cc\" > "+ v.elementAt(i).toString() +" </td> </tr> \n" );
						row = 1;
					}
					else
					{
						sb.append("<tr> <td bgcolor=\"white\" > "+ v.elementAt(i).toString() +" </td> </tr> \n" );
						row = 0;
					}
				}
				
				sb.append(" </table> \n");
				sb.append(" </body> \n");
				sb.append("</html> \n");
			
			}
			catch (Exception e) 
			{
				System.out.println( "Exception: " + e.getMessage() );
			}
		}
		
		return(sb);
	}
	
	
	 
	 /**
	  * method that will return a database connection for use with the database
		*/
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//the framework database
			c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/plots_dev", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("failed making db connection: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
 

 
	//method that retrives the plant names
	private Vector getPlantNames(String segment)
	{
		Vector v = new Vector();
		try
		{
			PreparedStatement pstmt;
      
			//create the accession number
			//PreparedStatement pstmt;
      pstmt = conn.prepareStatement(
				"SELECT distinct CHEATPLANTNAME FROM  STRATUMCOMPOSITION " + 
				"WHERE CHEATPLANTNAME LIKE '"+segment+"%' " );
                
      pstmt.execute();
      ResultSet rs = pstmt.getResultSet();
			
					
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (Exception e) 
		{
			System.out.println( "Exception: " + e.getMessage() );
		}
		return(v);
	}
	
	/**
	 * method that returns the required input parameters into this class
	 */
	 private String getRequiredParameters()
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html>");
		 sb.append("<br> lookup the input params in the java doc</br>");
		 sb.append("</html>");
		 return(sb.toString());
	 }
	
	
	
}
