package org.vegbank.nvcrs.util;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import org.vegbank.nvcrs.util.*;

public class UsrControl{


    private String usrId;
    private String loginName;
    private String password;
    private String role;
    private String lastName;
    private String firstName;
    private String middleInitial;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String email;

    
    private Connection con;

    // business methods

    public UsrControl()
    {
    	usrId="";
    }
    public UsrControl (String usrId, String usrLoginName,String usrPassword,String role, String lastName,
        String firstName, String middleInitial, String street,
        String city, String state, String zip, String phone,
        String email) {
        
        this.usrId = usrId;
        this.loginName=usrLoginName;
        this.password=usrPassword;
        this.role=role;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }
    public void setValues(String usrId, String usrLoginName,String usrPassword,String role, String lastName,
        String firstName, String middleInitial, String street,
        String city, String state, String zip, String phone,
        String email) {
        
        this.usrId = usrId;
        this.loginName=usrLoginName;
        this.password=usrPassword;
        this.role=role;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }
    public UsrDetails getDetails(String id) 
    	{

        Debug.print("UsrControl getDetails");
		
		if (id == null)
            return null;

		String uid;
		
		
		if(id.equals(usrId))
		    return new UsrDetails (usrId, loginName, password,role,lastName, 
        	    firstName, middleInitial, street, city, state, 
            	zip, phone, email);
        
        try{
        	uid=FindByPrimaryKey(id);
        }
        catch(Exception e)
        {
        	usrId="";
        	
        }
        
        usrId=id;
        
        try
        {
        	loadUsr();
        }
        catch(Exception e)
        {
        	return null;
        }
        
        return new UsrDetails (usrId, loginName, password,role,lastName, 
        	    firstName, middleInitial, street, city, state, 
            	zip, phone, email);
        	
    }


    public String getFirstName()
    {
    	return firstName;
    }
    
    public String getEmail()
    {
    	return email;
    }
    public void setLoginName(String loginName)
	{
		//Debug.print("UsrControl setLastName");
        this.loginName = loginName;
	}
	
	public void setPassword(String password)
	{
		this.password=password;
	}
	
	public void setRole(String role)
	{
		this.role=role;
	}
    
    public void setLastName(String lastName) {

        Debug.print("UsrControl setLastName");
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {

        Debug.print("UsrControl setFirstName");
        this.firstName = firstName;
    }

    public void setMiddleInitial(String middleInitial) {

        Debug.print("UsrControl setMiddleInitial");
        this.middleInitial = middleInitial;
    }

    public void setStreet(String street) {

        Debug.print("UsrControl setStreet");

        this.street = street;
    }

    public void setCity(String city) {

        Debug.print("UsrControl setCity");
        this.city = city;
    }

    public void setState(String state) {

        Debug.print("UsrControl setState");
        this.state = state;
    }

    public void setZip(String zip) {

        Debug.print("UsrControl setZip");
        this.zip = zip;
    }

    public void setPhone(String phone) {

        Debug.print("UsrControl setPhone");
        this.phone = phone;
    }

    public void setEmail(String email) {

        Debug.print("UsrControl setEmail");
        this.email = email;
    }

    // ejb methods 

    public String Create() throws Exception {

        Debug.print("UsrControl Create");

                
               
        try
        {
            usrId=insertRow();
        }
        catch(Exception e)
        {
        	throw e;
        }
        
              
        
        return usrId;
  }

    public String FindByPrimaryKey(String primaryKey){
   
        Debug.print("UsrControl ejbFindByPrimaryKey");

        boolean result;
   
        try {
            result = selectByPrimaryKey(primaryKey);
        } catch (Exception ex) {
              result=false;
        }
   
        if (result) {
            return primaryKey;
        }
        else {
            return "";
        }
    }

    
      

    public Collection FindByLastName(String lastName)
    {
    	Debug.print("UsrControl ejbFindByLastName");

        Collection result=null;

        try {
            result = selectByLastName(lastName);
        } catch (Exception ex) {
              
        }
        return result;
    }
    
    public Collection FindByFirstName(String firstName)
    {
    	Debug.print("UsrControl ejbFindByFirstName");

        Collection result=null;

        try {
            result = selectByFirstName(firstName);
        } catch (Exception ex) {
        }
        return result;
    }
    
	public Collection FindByRole(String role)
	{
		Debug.print("UsrControl ejbFindByRole");

        Collection result=null;

        try {
            result = selectByRole(role);
        } catch (Exception ex) {
              
        }
        return result;
	}
        
    public String FindByLogin(String loginName, String password) throws Exception
	{
		Debug.print("UsrControl ejbFindByLogin");

        
   
        try {
            usrId = selectByLogin(loginName,password);
        } catch (Exception ex) {
              throw ex;
        }
   
        return usrId;
	}
    
    public void Remove() {
   
        Debug.print("UsrControl ejbRemove");

        try {
            deleteRow(usrId);
         } catch (Exception ex) {
         }
    } 
   
    public void Load() {
   
        Debug.print("UsrControl ejbLoad");

        try {
            loadUsr();
         } catch (Exception ex) {
         }
    }
    
    public void Store() throws Exception{
   
        Debug.print("UsrControl ejbStore");

        try {
            storeUsr();
         } catch (Exception ex) {
         	throw ex;
         }
    }
   
    
    private void makeConnection() {
   
        Debug.print("UsrControl makeConnection");

        try {
            Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root");
        } catch (Exception ex) {
        }
    } // makeConnection


    private void releaseConnection() {
   
        Debug.print("UsrControl releaseConnection");

        try {
            con.close();
        } catch (SQLException ex) {
             //throw new EJBException("releaseConnection: " + ex.getMessage());
        }

    } // releaseConnection


    private String insertRow () throws Exception {
   
        Debug.print("UsrControl insertRow");         
		
		String ret="no";
        try
        {
	        makeConnection();
	        String insertStatement =
	            "insert into usr set login_name=?,password=?,role=?,last_name=?,first_name=? , " +
	            " middle_initial=? ,street= ? , city=? , " +
	            " state=? ,zip= ? ,phone= ? ,email= ?";
	        PreparedStatement prepStmt = 
	            con.prepareStatement(insertStatement);
	   
	        prepStmt.setString(1, loginName);
	        prepStmt.setString(2, password);
	        prepStmt.setString(3, role);
	        prepStmt.setString(4, lastName);
	        prepStmt.setString(5, firstName);
	        prepStmt.setString(6, middleInitial);
	        prepStmt.setString(7, street);
	        prepStmt.setString(8, city);
	        prepStmt.setString(9, state);
	        prepStmt.setString(10, zip);
	        prepStmt.setString(11, phone);
	        prepStmt.setString(12, email);
	
	        prepStmt.executeUpdate();
	        prepStmt.close();
	        try
	        {
	        	ret=DBHelper.getMaxUsrId(con);
	        }
	        catch(Exception e)
	        {
	        	releaseConnection();
	        	throw e;
	        }
        
	        releaseConnection();
	     }
	     catch(Exception e)
	     {
	     	releaseConnection();
	     	throw e;
	     }
	     return ret;
    }
   
    private void deleteRow(String id){
   
        Debug.print("UsrControl deleteRow");

		try{
		
	        makeConnection();
	        String deleteStatement =
	                "delete from Usr where Usr_id = ? ";
	        PreparedStatement prepStmt =
	                con.prepareStatement(deleteStatement);
	   
	        prepStmt.setString(1, id);
	        prepStmt.executeUpdate();
	        prepStmt.close();
	        releaseConnection();
	       }
	       catch(Exception e)
	       {
	       }
    }
   
    private boolean selectByPrimaryKey(String primaryKey)  
        {
   
        Debug.print("UsrControl selectByPrimaryKey");
		try{
		
	        makeConnection();
	        String selectStatement =
	                "select USR_ID " +
	                "from usr where USR_ID = ? ";
	        PreparedStatement prepStmt =
	                con.prepareStatement(selectStatement);
	        prepStmt.setString(1, primaryKey);
	   
	        ResultSet rs = prepStmt.executeQuery();
	        boolean result = rs.next();
	        prepStmt.close();
	        releaseConnection();
	        return result;
	     }
	     catch(Exception e)
	     {
	     	return false;
	     }
    }
    
    
    private String selectByLogin(String loginName, String password) throws Exception{
   
        Debug.print("UsrControl selectByLogin");
		
		PreparedStatement prepStmt =null;
        try{
        
	        makeConnection();
	        String selectStatement =
	                "select * " +
	                "from usr where login_name = ? and password=?";
	        prepStmt =
	                con.prepareStatement(selectStatement);
	        prepStmt.setString(1, loginName);
	        prepStmt.setString(2, password);
	   
	        ResultSet rs = prepStmt.executeQuery();
	        
	        
	        if (rs.next()) {
	            usrId=rs.getString(1);
	            loginName=loginName;
	            password=password;
	            role=rs.getString(4);
	            lastName = rs.getString(5);
	            firstName = rs.getString(6);
	            middleInitial = rs.getString(7);
	            street = rs.getString(8);
	            city = rs.getString(9);
	            state = rs.getString(10);
	            zip = rs.getString(11);
	            phone = rs.getString(12);
	            email = rs.getString(13);
	           
	        }
	        else
	        {
	        	usrId="";	
	        }        		
	        prepStmt.close();
	        releaseConnection();
	    }
	    catch(Exception e)
	    {
	    	throw e;
	    	
	        
	    }
        return usrId;
    }
       
    private Collection selectByLastName(String lastName) 
   {
   
        Debug.print("UsrControl selectByLastName");

        try
        {
        
	        makeConnection();
	        String selectStatement =
	                "select Usr_id " +
	                "from Usr " +
	                "where last_name = ? ";
	        PreparedStatement prepStmt = 
	                con.prepareStatement(selectStatement);
	   
	        prepStmt.setString(1, lastName);
	        ResultSet rs = prepStmt.executeQuery();
	        ArrayList a = new ArrayList();
	   
	        while (rs.next()) {
	            a.add(rs.getString(1));
	        }
	   
	        prepStmt.close();
	        releaseConnection();
	        return a;
	     }
	     catch(Exception e)
	     {
	     	return null;
	     }
    }
   private Collection selectByRole(String role) 
   {
   
        Debug.print("UsrControl selectByRole");

        try
        {
        
	        makeConnection();
	        String selectStatement =
	                "select Usr_id " +
	                "from Usr " +
	                "where role = ? ";
	        PreparedStatement prepStmt = 
	                con.prepareStatement(selectStatement);
	   
	        prepStmt.setString(1, role);
	        ResultSet rs = prepStmt.executeQuery();
	        ArrayList a = new ArrayList();
	   
	        while (rs.next()) {
	            a.add(rs.getString(1));
	        }
	   
	        prepStmt.close();
	        releaseConnection();
	        return a;
	     }
	     catch(Exception e)
	     {
	     	return null;
	     }
    }
   private Collection selectByFirstName(String firstName) 
   {
   
        Debug.print("UsrControl selectByFirstName");

		try
		{
		
	        makeConnection();
	        String selectStatement =
	                "select Usr_id " +
	                "from Usr " +
	                "where first_name = ? ";
	        PreparedStatement prepStmt = 
	                con.prepareStatement(selectStatement);
	   
	        prepStmt.setString(1, firstName);
	        ResultSet rs = prepStmt.executeQuery();
	        ArrayList a = new ArrayList();
	   
	        while (rs.next()) {
	            a.add(rs.getString(1));
	        }
	   
	        prepStmt.close();
	        releaseConnection();
	        return a;
	        }
	        catch(Exception e)
	        {
	        	return null;
	        }
    }
    private void loadUsr(){
   
        Debug.print("UsrControl loadUsr");
		try
		{
			
	        makeConnection();
	        String selectStatement =
	                "select login_name,password,role,last_name, first_name, middle_initial, " +
	                "street, city, state, zip, phone, email " +
	                "from Usr where Usr_id = ? ";
	        PreparedStatement prepStmt = 
	                con.prepareStatement(selectStatement);
	   
	        prepStmt.setString(1, usrId);
	   
	        ResultSet rs = prepStmt.executeQuery();
	   
	        if (rs.next()) {
	            loginName=rs.getString(1);
	            password=rs.getString(2);
	            role=rs.getString(3);
	            lastName = rs.getString(4);
	            firstName = rs.getString(5);
	            middleInitial = rs.getString(6);
	            street = rs.getString(7);
	            city = rs.getString(8);
	            state = rs.getString(9);
	            zip = rs.getString(10);
	            phone = rs.getString(11);
	            email = rs.getString(12);
	            prepStmt.close();
	            releaseConnection();
	        }
	        else {
	            prepStmt.close();
	            releaseConnection();
	            
	        }
	       }catch(Exception e)
	       {
	       	 
	       }
    }
   

    private void storeUsr() throws Exception{
   
        Debug.print("UsrControl storeUsr");
		try{
		
	        makeConnection();
	        String updateStatement =
	                "update usr " +
	                "set last_name = ? , first_name = ? , " +
	                "middle_initial = ? , street = ? , city = ? , " +
	                "state = ? , zip = ? , phone = ? , email = ?, " +
	                "login_name=?, password=?, role=? " +
	                "where USR_ID = ? ";
	        PreparedStatement prepStmt = 
	                con.prepareStatement(updateStatement);
	   
	        prepStmt.setString(1, lastName);
	        prepStmt.setString(2, firstName);
	        prepStmt.setString(3, middleInitial);
	        prepStmt.setString(4, street);
	        prepStmt.setString(5, city);
	        prepStmt.setString(6, state);
	        prepStmt.setString(7, zip);
	        prepStmt.setString(8, phone);
	        prepStmt.setString(9, email);
	        prepStmt.setString(10, loginName);
	        prepStmt.setString(11, password);
	        prepStmt.setString(12, role);
	        prepStmt.setString(13, usrId);
	
	        int rowCount = prepStmt.executeUpdate();
	        prepStmt.close();
	        releaseConnection();
	   
	        if (rowCount == 0) {
	   			throw new Exception("Failed to update the user infomation for userid: " +usrId);	
	        }
	       }
	       catch(Exception e)
	       {
	       	throw e;
	       	
	       }
    }
    
} // UsrControl 
