/*
 * '$Id: SuperForm.java,v 1.1.1.1 2004-04-21 17:10:07 anderson Exp $'
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

package org.vegbank.nvcrs.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.ModuleException;
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import org.vegbank.nvcrs.util.*;

public class SuperForm extends ActionForm{
	String primaryKey;
	ArrayList fields;
	ArrayList records;
	String tableName;
	Database database;
    
    public SuperForm()
    {
    	super();
    	primaryKey="";
    	fields=new ArrayList();
		records=new ArrayList();    	
		database=new Database();
    }
    
    public ArrayList getRecords()
    {
    	return records;
    }
    public void setPrimaryKey(String key)
    {
    	primaryKey=key;
    }
    public void setFields(ArrayList list)
    {
    	fields.clear();
    	fields=list;
    } 
    public String getPrimaryKey()
    {
    	return primaryKey;
    }
    
    public ArrayList getFields()
    {
    	return fields;
    }
    
    public void setTableName(String tbname)
    {
    	tableName=tbname;
    }
    public String getTableName()
    {
    	return tableName;
    }
    
    public void addField(String fldName)
    {
    	fields.add(fldName);
    }
    public void clearForm() throws Exception
    {
    	int num=fields.size();
    	setFieldValue(primaryKey,"");
    	for(int i=0; i<num;i++)
	    {
	       	String fldName=(String)fields.get(i);
	       	try
	       	{
	       		setFieldValue(fldName,"");
	       	}
	       	catch(Exception e)
	       	{
	       		throw e;
	       	}
	    }
    }
    public void updateRecord() throws Exception
    {
    	Connection con=null;
    	try
        {
        	con=database.getConnection();
	        
	        String updateStatement ="update "+tableName + " set ";
	        int num=fields.size();
	        for(int i=0; i<num;i++)
	        {
	        	String fldName=(String)fields.get(i);
	        	updateStatement += fldName + "='" + getFieldValue(fldName)+"'";
	        	if(i<num-1) 
	        		updateStatement += ",";
	        }
	        updateStatement +=" where "+primaryKey +"='" + getFieldValue(primaryKey)+"'";
	        PreparedStatement prepStmt = 
	                con.prepareStatement(updateStatement);
	   
	        int rowCount = prepStmt.executeUpdate();
	        prepStmt.close();
	        con.close();
	   
	        if (rowCount == 0)
	        {
	   			throw new Exception("Failed to update the record: " + getFieldValue(primaryKey));	
	        }
	    }
	    catch(Exception e)
	    {
	    	if(con!=null && !con.isClosed())
	    		con.close();
	      	throw e;
	    }
    }
    public void addRecord() throws Exception
    {
    	Connection con=null;
    	try
        {
			con=database.getConnection();
	        
	        String updateStatement ="insert into "+tableName + " set ";
	        int num=fields.size();
	        for(int i=0; i<num;i++)
	        {
	        	String fldName=(String)fields.get(i);
	        	updateStatement += fldName + "='" + getFieldValue(fldName)+"'";
	        	if(i<num-1)updateStatement +=",";
	        }
	        
	        PreparedStatement prepStmt = 
	                con.prepareStatement(updateStatement);
	   
	        int rowCount = prepStmt.executeUpdate();
	        prepStmt.close();
	        setFieldValue(primaryKey,DBHelper.getMaxId(con,tableName,primaryKey));
	        con.close();
	     }
	     catch(Exception e)
	     {
	     	if(con!=null && !con.isClosed())
	     		con.close();
	     	throw e;
	     }
    }
    public void deleteRecord() throws Exception
    {
    	Connection con=null;
    	try
    	{
			con=database.getConnection();
	        String deleteStatement =
	                "delete from " + tableName + " where " + primaryKey + "=" +getFieldValue(primaryKey);
	        PreparedStatement prepStmt =
	                con.prepareStatement(deleteStatement);
	   
	        prepStmt.executeUpdate();
	        prepStmt.close();
	        con.close();
	    }
	    catch(Exception e)
	    {
	    	if(con!=null && !con.isClosed())
	     		con.close();
	     	throw e;  		
	    }
    }
    public void findRecordByPrimaryKey(String primaryKeyValue) throws Exception
    {
     	
     	
     	Connection con=null;
     	try
		{
		    con=database.getConnection();
	        String selectStatement = "select * from "+tableName + " where "+ primaryKey + "=" + primaryKeyValue;
	        PreparedStatement prepStmt =  con.prepareStatement(selectStatement);
	        ResultSet rs = prepStmt.executeQuery();
	   
	        if (rs.next()) 
	        {
	        	int num=fields.size();
	        	for(int i=0;i<num;i++)
	        	{
	        		String fldName=(String)fields.get(i);
	        		setFieldValue(fldName,rs.getString(fldName));
	        	}
	        	setFieldValue(primaryKey,primaryKeyValue);
	        }
	        else
	        {
	        	prepStmt.close();
		        con.close();
		        throw new Exception("Failed to find the record: "+ primaryKeyValue);
		    }
	    	prepStmt.close();
	        con.close();
	    }
	    catch(Exception e)
	    {
	       	if(con!=null && !con.isClosed())
	     		con.close();
	     	throw e;  
	    }
    }
    
    public void loadRecord(String primaryKeyValue) throws Exception
    {
    	if(!records.isEmpty())
    	{
    		int num=records.size();
    		int i;
    		for(i=0;i<num;i++)
    		{
    			Hashtable tb=(Hashtable)records.get(i);
    			if(((String)tb.get(primaryKey)).equals(primaryKeyValue))
    			{
    				for (Enumeration e = tb.keys() ; e.hasMoreElements() ;) 
    				{
         				setFieldValue((String)e.nextElement(),(String)tb.get((String)e.nextElement()));
     				}	
     				i=num+1;
     			}
    		}
    		if(i==num+1)
    			throw new Exception("Failed to find the record in Records: "+ primaryKeyValue);
    	}
    	else
    		throw new Exception("Empty Records");
    }
    
    public void findRecordBySQL(String strSQL) throws Exception
    {
     	Connection con=null;
     	try
		{
		    con=database.getConnection();
	        String selectStatement = strSQL;
	        PreparedStatement prepStmt =  con.prepareStatement(selectStatement);
	        ResultSet rs = prepStmt.executeQuery();
	   
	        if (rs.next()) 
	        {
	        	int num=fields.size();
	        	for(int i=0;i<num;i++)
	        	{
	        		String fldName=(String)fields.get(i);
	        		setFieldValue(fldName,rs.getString(fldName));
	        	}
	        	setFieldValue(primaryKey,rs.getString(primaryKey));
	        }
	        else
	        {
	        	prepStmt.close();
		        con.close();
		        throw new Exception("Failed to find the record by SQL: "+ strSQL);
		    }
	    	prepStmt.close();
	        con.close();
	    }
	    catch(Exception e)
	    {
	       	if(con!=null && !con.isClosed())
	     		con.close();
	     	throw e;  
	    }
    }
    
    public void findRecords(String strSQL) throws Exception
    {
    	Connection con=null;

     	try
		{
	    	records.clear();
		   
		    con=database.getConnection();
	        String selectStatement =strSQL;
	        PreparedStatement prepStmt = 
	                con.prepareStatement(selectStatement);
	        ResultSet rs = prepStmt.executeQuery();
	   
	        while(rs.next()) 
	        {
	        	Hashtable rcd=new Hashtable();
	        	int num=fields.size();
	        	for(int i=0;i<num;i++)
	        	{
	        		String fldName=(String)fields.get(i);
	        		rcd.put(fldName,rs.getString(fldName));
	        	}
	        	rcd.put(primaryKey,rs.getString(primaryKey));
	        	records.add(rcd);
	        }
	    	prepStmt.close();
	        con.close();
	    }
	    catch(Exception e)
	    {
	       	if(con!=null && !con.isClosed())
	     		con.close();
	     	throw e;  
	    }
    }
    public String getFieldValue(String fldName) throws Exception
    {
    	throw new Exception("getFieldValue: Function has not been implemented yet.");
    }
    public void setFieldValue(String fldName, String value) throws Exception
    {
    	throw new Exception("setFieldValue: Function has not been implemented yet.");
    }
    
    private boolean recordExisted(String primaryKeyValue) throws Exception
    {
    	boolean ret=true;
    	Connection con=null;
        try
        {
        	con=database.getConnection();
	        
	        String queryStatement ="select * from ? where ? = ?";
	            
	        PreparedStatement prepStmt = 
	            con.prepareStatement(queryStatement);
	   
	        prepStmt.setString(1, tableName);
	        prepStmt.setString(2, primaryKey);
	        prepStmt.setString(3, primaryKeyValue);
	        ResultSet rs=prepStmt.executeQuery();
	        if(rs.next())
	        	ret=true;
	        else
	        	ret=false;
	        
	        prepStmt.close();
            con.close();
            
	     }
	     catch(Exception e)
	     {
	     	if(con!=null && !con.isClosed())
	     		con.close();
	     	throw e;
	     }
	     return ret;
    }
    
    
}
