/*
 * '$Id: Database.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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
    	con = DriverManager.getConnection("jdbc:postgresql://localhost/nvcrs", "postgres", "");
    }
    catch (Exception ex)
    {
        throw new Exception("Couldn't open connection to database: " + ex.getMessage());
    }
    return con;     
   }
}
