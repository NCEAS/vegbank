package org.vegbank.nvcrs.util;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import java.util.*;

public class Database {
  
  public Database() 
  {
           
  }

  public Connection getConnection() throws Exception
  {
  	Connection con=null;
  	try
  	{
        //Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
    	//con = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root");
    	Class.forName( "org.postgresql.Driver" ).newInstance();
    	con = DriverManager.getConnection("jdbc:postgresql://localhost/nvcrs","pel","Carex?");
    }
    catch (Exception ex)
    {
        throw new Exception("Couldn't open connection to database: " + ex.getMessage());
    }
    return con;     
   }
}
