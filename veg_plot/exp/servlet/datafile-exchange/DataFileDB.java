package servlet.datafile_exchange;


import java.net.*;
import java.sql.*;




/**
 * handles all related to storing data files on the webserver 
 * and reggistering those documents in the 'framework database'
 * 
 */
public class DataFileDB  {
  

  private Connection conn = null;
    
 
  /** 
   * Construct an DataFileDB
   *
   * @param conn the db connection to read from and write Accession# to
   */
  public DataFileDB() 
  {
		try{
    	conn = getConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
  }

 

  
  // get Unique ID from DB sequence
  private String getUniqueID() 
  {
    String uniqueid = null;
    
    try {
			//conn = getConnection();
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement
              ("INSERT into file_accession (create_date, create_time) " +
               "VALUES (now()::date, now()::time)");
     	// pstmt.setString(1, sitecode);
      pstmt.execute();
      pstmt.close();
 			
			//create the accession number
			//PreparedStatement pstmt;
      pstmt = conn.prepareStatement(
                "SELECT accession_id, create_date, create_time FROM file_accession " + 
                "WHERE accession_id = " +
                " (SELECT max(accession_id) FROM file_accession ) " );
                
      pstmt.execute();
      ResultSet rs = pstmt.getResultSet();
			
			//results = query.executeQuery(sb.toString());
			
			//get the results
			
			//	if (results.getString(1) != null) 
			//	{
					
			while (rs.next()) 
			{
      if (rs != null)
			{
				String accessionId = rs.getString(1);
				String createDate = rs.getString(2);
				String createTime = rs.getString(3);
			
				uniqueid = accessionId+createDate+createTime;
			}
			}
    } catch (Exception e) {
      	System.out.println("Error on DataFileDB.getUniqueID(): "+e.getMessage());
				e.printStackTrace();
    }

    return uniqueid;
  }

	
	
	
	/** check for existence of Accesssion Number xml_acc_numbers table */
  public boolean accNumberUsed ( String accNumber )
                  throws SQLException {
        
    boolean hasAccNumber = false;
        
    try {
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement(
                "SELECT 'x' FROM xml_documents " + 
                "WHERE docid = ? " +
                "UNION " +
                "SELECT 'x' FROM xml_revisions " +
                "WHERE docid = ?");
      pstmt.setString(1,accNumber);
      pstmt.setString(2,accNumber);
      pstmt.execute();
      ResultSet rs = pstmt.getResultSet();
      hasAccNumber = rs.next();
      pstmt.close();
            
    } catch (SQLException e) {
      throw new SQLException
      ("Error on DataFileDB.accNumberUsed(accNumber): " + e.getMessage());
    }    
        
    return hasAccNumber;
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
			c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/framework", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("failed making db connection: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
 

  // get the next revision number for docid
  private String getNextRevision(String docid) throws SQLException
  {
    String rev = "";
    
    try {
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement
              ("SELECT rev+1 FROM xml_documents WHERE docid='" + docid + "'");
      pstmt.execute();

      ResultSet rs = pstmt.getResultSet();
      boolean hasRow = rs.next();
      rev = rs.getString(1);
      pstmt.close();
      
    } catch (SQLException e) {
      throw new SQLException(
      "Error on DataFileDB.getNextRevision(): " + e.getMessage());
    }

    return rev;
  }
  
	/**
	 * method that will register a document with the database - insert 
	 * the accession number, username, create date etcc
	 *
	 */
	 public boolean registerDocument( String userSurName, String userGivenName,
	 String createDate, String size, String path, String file, String fileType, 
	 String accessionNumber)
	 {
		try
		{
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement
			("INSERT into datafile (user_givenname, user_surname, insert_date, "
			+" filesize, path, user_filename, db_filename, file_type ) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ? )");
			
			pstmt.setString(1, userGivenName);
			pstmt.setString(2, userSurName);
			pstmt.setString(3, createDate);
			pstmt.setString(4, size);
			pstmt.setString(5, path);
			pstmt.setString(6, file);
			pstmt.setString(7, accessionNumber);
			pstmt.setString(8, fileType);
			

			
			pstmt.execute();
			pstmt.close();
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
		}
		 return(true);
	 }
	
	
	/**
	 * method to delete an instance of a file owned by a specific user
	 */
	 public boolean deleteFile(String user, String fileAccessionNumber)
	 {
		 boolean validDelet = false;
		 
		 try
	 		{
				PreparedStatement pstmt;
				pstmt = conn.prepareStatement
				("DELETE from datafile where user_surname ='"
				+user+"' and db_filename='"+fileAccessionNumber+"'");
				
				
				pstmt.execute();
				pstmt.close();
				return(true);
			}
			catch (Exception e)
			{
				System.out.println("Exception: " + e.getMessage() );
				return(false);
			}
		 
	 }
	
	/**
	 * method that will register a document with the database - insert 
	 * the accession number, username, create date etcc
	 *
	 */
	 public String registerDocument( String userSurName, String userGivenName,
	 String createDate, String size, String path, String file, String fileType)
	 {
		//update this with the accession number to be passed 
		//the client
		String accessionNumber = null;
		 
		try
		{
			
			accessionNumber = getUniqueID();
			
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement
			("INSERT into datafile (user_givenname, user_surname, insert_date, "
			+" filesize, path, user_filename, db_filename, file_type ) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ? )");
			
			pstmt.setString(1, userGivenName);
			pstmt.setString(2, userSurName);
			pstmt.setString(3, createDate);
			pstmt.setString(4, size);
			pstmt.setString(5, path);
			pstmt.setString(6, file);
			pstmt.setString(7, accessionNumber);
			pstmt.setString(8, fileType);
			

			
			pstmt.execute();
			pstmt.close();
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
		}
		 return(accessionNumber);
	 }
	
	/**
	 * method that passes back, as a string, the current
	 * database date
	 *
	 */
	 public String getCurrentDate()
	 {
		 String s = null;
 			java.util.Date localtime = new java.util.Date();
			//System.out.println("Database date: "+localtime);
		 return(localtime.toString() );
	 }
	 
	 //method that retuns the number of files the user has on the 
	 //server
	public String getUserFileNumber(String userName)
	{
		String s = null;
		try 
		{
			//conn = getConnection();
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement(
				"SELECT count(*) from datafile where user_surname like " + 
				" '"+userName+"'");
                
      	pstmt.execute();
      	ResultSet rs = pstmt.getResultSet();
					
			while (rs.next()) 
			{
     	 if (rs != null)
				{
					 s = rs.getString(1);
				}
    	}
		}
		catch (Exception e) 
		{
      	System.out.println("Exception: "+e.getMessage());
				e.printStackTrace();
    }  
		return(s);
	}
	
	
	
	
	 //method that retuns the number of files the user has on the 
	 //server
	public String getUserFileSummary(String userName)
	{
		String s = null;
		StringBuffer sb = new StringBuffer();
		try 
		{
			
			//conn = getConnection();
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement(
				"SELECT user_filename, db_filename, file_type, insert_date, filesize from "
				+" datafile where user_surname like " + 
				" '"+userName+"'");
                
      	pstmt.execute();
      	ResultSet rs = pstmt.getResultSet();
					
			while (rs.next()) 
			{
     	 if (rs != null)
				{
					 String userFileName = rs.getString(1).trim();
					 String dbFileName = rs.getString(2).trim();
					 String fileType = rs.getString(3).trim();
					 String fileSize = rs.getString(4).trim();
					 
					 sb.append( userFileName + "|" + dbFileName + "|" + fileType+ "|"+ fileSize+"**");
				}
    	}
		}
		catch (Exception e) 
		{
      	System.out.println("Exception: "+e.getMessage());
				e.printStackTrace();
    }  
		return(sb.toString() );
	}
	
	
	
/**
 * Main method for testing
 */
	public static void main(String[] args) 
	{
		if (args.length == 1) 
		{
			String action = args[0];
			
			if ( action.equals("insert") )
			{
				System.out.println("inserting a test document");
				try {
					DataFileDB an = new DataFileDB();
					String aNum = an.getUniqueID();
					String surName = "harris";
					String givenName = "john";
					String createDate = "26-NOV-2001";
					String size = "0";
					String path = "/usr/local/devtools/jakarta-tomcat/uploads/";
					String file = "test.txt";
					String fileType = "UNKNOWN";
					
					//try to register the document with the database
					boolean result = an.registerDocument(surName, givenName, createDate, 
						size, path, file, fileType, aNum );
						
						
					System.out.println("results: " + result);
				}
				catch (Exception e ) { 
					System.out.println("Exception: " + e.getMessage() ); 
				}
				
			}
			else 
			{
				System.out.println("Usage:  ");
			}
			
		}
		else 
		{
			try
			{
				DataFileDB an = new DataFileDB();
				System.out.println( an.getUniqueID() );
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

  
}
