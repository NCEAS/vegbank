package org.vegbank.servlet.datafileexchange;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




/**
 * handles all related to storing data files on the webserver 
 * and registering those documents in the 'framework database'
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
    
    try 
    {
		//conn = getConnection();
      	PreparedStatement pstmt;
      	pstmt = conn.prepareStatement
              ("INSERT into file_accession (create_date) " +
               "VALUES (now())");
     	// pstmt.setString(1, sitecode);
      	pstmt.execute();
      	pstmt.close();
 			
		//create the accession number
		//PreparedStatement pstmt;
      	pstmt = conn.prepareStatement(
                "SELECT accession_id, create_date FROM file_accession " + 
                "WHERE accession_id = " +
                " (SELECT max(accession_id) FROM file_accession ) " );
                
      	pstmt.execute();
      	ResultSet rs = pstmt.getResultSet();
					
		while (rs.next()) 
		{
      		if (rs != null)
			{
				String accessionId = rs.getString(1);
				String createDate = rs.getString(2);
						
				uniqueid = accessionId+createDate;
			}
		}
    } catch (Exception e) {
      	System.out.println("Error on DataFileDB.getUniqueID(): "+e.getMessage());
		e.printStackTrace();
    }
    return uniqueid;
  }
  
		// get Unique ID from DB sequence
		protected int getNewAccessionId()
		{
			int accessionId = 0;

			try
			{
				//conn = getConnection();
				PreparedStatement pstmt =
					conn.prepareStatement(
						"INSERT into file_accession (create_date) " + "VALUES (now())");
				pstmt.execute();
				pstmt.close();

				//create the accession number
				//PreparedStatement pstmt;
				pstmt =
					conn.prepareStatement(
						"SELECT max(accession_id) FROM file_accession ");
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();

				while (rs.next())
				{
					if (rs != null)
					{
						accessionId = rs.getInt(1);
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(
					"Error on DataFileDB.getUniqueID(): " + e.getMessage());
				e.printStackTrace();
			}
			return accessionId;
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
			System.out.println("DataFileDB > Exception Registering Document : " + e.getMessage() );
		}
		 return(true);
	 }
	
	
	/**
	 * method to delete an a specific type of file for a given user
	 */
	 public boolean deleteFileType(String user, String fileType)
	 {
		 boolean validDelet = false;
		 
		 try
	 		{
				PreparedStatement pstmt;
				pstmt = conn.prepareStatement
				("DELETE from datafile where user_surname ='"
				+user+"' and file_type like'"+fileType+"'");
				
				
				int i = pstmt.executeUpdate();
				pstmt.close();
				System.out.println("DataFileDB > row count: " + i);
				return(true);
			}
			catch (Exception e)
			{
				System.out.println("Exception: " + e.getMessage() );
				return(false);
			}
		 
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
			System.out.println("DataFileDB > Exception Registering Document : " + e.getMessage() );
		}
		 return(accessionNumber);
	 }
	 
	/**
	 * Register a document with the database - insert 
	 * the accession number, username, create date etc
	 *
	 */
	 public void registerDocument 	( 	
 																int accessionId,
 																int fileSize,
 																String fileName,
 																String path
 															)
	 {		 
		try
		{		
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement
			("INSERT into datafile (accession_id, file_size, user_filename, path) " +
				"VALUES (?, ?, ?,?)");
			
			pstmt.setInt(1, accessionId);
			pstmt.setInt(2, fileSize);
			pstmt.setString(3, fileName);
			pstmt.setString(4, path);
						
			pstmt.execute();
			pstmt.close();
		}
		catch (Exception e)
		{
			System.out.println("DataFileDB > Exception Registering Document : " + e.getMessage() );
		}
	 }
	
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
	
	/**
	 * method to retrieve a users style sheet
	 */
		public String getUserStyleSheet(String userName)
		{
			String s = null;
		try 
		{
			//conn = getConnection();
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement(
				"SELECT path, db_filename from datafile where user_surname like " + 
				" '"+userName+"' and file_type like 'style%' ");
                
      	pstmt.execute();
      	ResultSet rs = pstmt.getResultSet();
					
			while (rs.next()) 
			{
     	 if (rs != null)
				{
					String path = rs.getString(1);
					String file = rs.getString(2);
					s = path+file;
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
		System.out.println("DataFileDB > looking up the files stored for: " + userName);
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
					int accessionId = an.getNewAccessionId();
					int fileSize = 55435435;
					String fullPath = "Follow the yellow brick road";
					String fileName = "thisANDthat.hex";
					
					//try to register the document with the database
					an.registerDocument( accessionId, fileSize, fileName, fullPath);
						
						
					System.out.println("Finished");
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
