/*
 *	'$RCSfile: ObjectToDB.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-10 23:37:13 $'
 *	'$Revision: 1.7 $'
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
 
package org.vegbank.common.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author farrell
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
/**
 * @author farrell
 */

public class ObjectToDB extends VegBankObjectWriter
{
	private boolean existsInDatabase = false;
	Connection conn;	
	protected AbstractList fieldNames = new Vector();
	private AbstractList fieldValues = new Vector();
	//private Hashtable attributeNameValues = new Hashtable();
	private int primaryKey;
	private Hashtable foreignKeyHash = new Hashtable();
	
	
	public ObjectToDB(Object object)
	{
		super(object);
		System.out.println("ObjectToDB > New Instance");
		conn= new Utility().getConnection("vegbank");
		try
		{
			int pk = this.isObjectInDatabase(null);
			if ( pk == 0 )
			{
				// Does not exist in database ( reseve a pk )
				this.generatePrimaryKey();
				System.out.println("----------------------------------------------");
				System.out.println("Created NEW Object for " + className);
				System.out.println("PK is " + this.primaryKey);
				System.out.println("----------------------------------------------");
			}
			else 
			{
				this.primaryKey = pk;
				this.existsInDatabase = true;
				System.out.println("----------------------------------------------");
				System.out.println("Object " + className + " exists in Database");
				System.out.println("PK is " + this.primaryKey);
				System.out.println("----------------------------------------------");
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setForeignKey( String fieldName, int foreignKey)
	{	
		System.out.println("Allocating a FK " + fieldName + " " + foreignKey);		
		foreignKeyHash.put( fieldName.toUpperCase() , new Integer(foreignKey).toString()  );
	}
	  
	public void setForeignKeys( Hashtable foreignKeys)
	{	
		Enumeration e = foreignKeys.keys();
		while ( e.hasMoreElements() )
		{
			String key = (String) e.nextElement();
			foreignKeyHash.put( key.toUpperCase() ,  foreignKeys.get(key) ); 
		}
	}
	
	public String getPrimaryKeyFieldName() 
	{
		return className + "_id";
	}
	
	private void prepareObjectForWriting() throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, InvocationTargetException
	{
		for (int i=0; i < methods.length ; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();
			// empty parameter list for a get method
			Object[] parameters = {};
			
			// Search this object for get methods 
			if ( VBObjectUtils.isGetMethod(method) )
			{
				if ( VBObjectUtils.isGetMethod(method, "java.lang.String") )
				{					
					// get the name and value of the field 
					String fieldName = VBObjectUtils.getFieldName(methodName, null);
					String fieldValue = (String) method.invoke(object, parameters);
					
					storeNameAndValue(fieldName, fieldValue);
				}
				else if ( VBObjectUtils.isGetMethod(method, "int") )
				{
					// I have a referenced Object  -- I want to get its foriegn key
					String fieldName = VBObjectUtils.getFieldName(methodName, "");
					String fieldValue = (String) foreignKeyHash.get(fieldName.toUpperCase() );
					if (Utility.isStringNullOrEmpty(fieldValue))
					{			
						//System.out.println("ObjectToDB > No stored FK for " + fieldName + " " + foreignKeyHash);	
						// Check the Object
						Integer fieldValueInt = (Integer) method.invoke(object, parameters);
						fieldValue = fieldValueInt.toString();	
					}
					
					// -1 and 0 are not real FK ignore
					if (! fieldValue.equals("-1") && !  fieldValue.equals("0") )
					{				
						storeNameAndValue(fieldName, fieldValue);
					}
					//System.out.println("ObjectToDB > " + fieldName + " v. " + fieldValue);
				}
			}
			else 
			{
				//System.out.println("I am a " + method.getReturnType().getName());
				// Not of interest
			}
		}
	}


	private void storeNameAndValue(String fieldName, String fieldValue)
	{
		// if not null add to the lists used in construction of query
		if ( ! Utility.isStringNullOrEmpty(fieldValue))
		{
			this.fieldNames.add(fieldName);
			this.fieldValues.add(fieldValue);
		}
	}

	private PreparedStatement getPopulatedPreparedStatement(String statement)
		throws SQLException
	{
		PreparedStatement pstmt = conn.prepareStatement(statement);
		Iterator it = fieldValues.iterator();
		//System.out.println("----> " + fieldValues.size() + " " +  fieldNames.size());
		for (int i=0; i<fieldValues.size(); i++)
		{
			// JDBC starts counting from 1 not 0
			int modInt = i+1;
			String value = (String) fieldValues.get(i);
			System.out.println("ObjectToDB > setting '" + fieldValues.get(i) +  "' for: " + fieldNames.get(i));
			if ( Utility.isStringNullOrEmpty(value) )
			{
				pstmt.setNull(modInt, java.sql.Types.VARCHAR );
			}
			else
			{
				pstmt.setString(modInt,  Utility.escapeCharacters(value) );
			}
		}	
		return pstmt;
	}
	
	public boolean insert() throws Exception
	{	
		boolean result = true;
		
		if (this.existsInDatabase)
		{
			return false;
		}
		
		// Get the Prepared Statement
		String statement = this.getPreparedStatementString();
		try
		{		
			PreparedStatement pstmt = getPopulatedPreparedStatement(statement);
			
			// Get a value for the primary key and add it to statement
			System.out.println("PK is " + primaryKey);
			pstmt.setInt(fieldValues.size() +1, primaryKey);
				
			pstmt.execute();
			
			// Write Objects that depend on this PK to database
			handleChildren();
		}
		catch (Exception e)
		{
			System.out.println("ObjectToDB > Problem with SQL '" + statement + "'"  );
			e.printStackTrace();
			result = false;
			throw new Exception("Problem with SQL '" + statement + "' --> " + e.getMessage() );
		}

		// if we get here 
		return result;
	}
	
	/**
	 * 
	 */
	private void handleChildren() throws Exception
	{
		for (int i=0; i < methods.length ; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();

			if ( VBObjectUtils.isGetMethod(method, "java.util.List") )
			{
				System.out.println("ObjectToDB > Handling a list");
				// Need to Loop throught all elements and insert them into the DB
				List objList = (List) method.invoke(object, null);
				Iterator it = objList.iterator();
				while ( it.hasNext() )
				{
					Object obj = it.next();
					// Insert the Object into the Database
					ObjectToDB o2d = new ObjectToDB(obj);
					o2d.setForeignKey( this.getPrimaryKeyFieldName() , this.primaryKey );
					o2d.insert();
				}
			}	
		}
	}

	/**
	 * @return String - sql for creating prepared statement
	 */
	public String getPreparedStatementString() throws Exception
	{
		this.prepareObjectForWriting();
		
		//PreparedStatement pstmt = conn.prepareStatement();
		StringBuffer sb = new StringBuffer();
		StringBuffer parameters = new StringBuffer();
		sb.append("insert into " + className + " ( ");
		
		if (fieldNames != null)
		{
			Iterator iter = this.fieldNames.iterator();
			while ( iter.hasNext() )
			{
				sb.append( iter.next() );
				parameters.append("?");
				
				// If there are more elements use a comma
				if (iter.hasNext())
				{
					sb.append(", ");
					parameters.append(",");
				}
			}
			// The Primary Key
			sb.append(", " + className + "_id");
			parameters.append(", ?");
		}

		sb.append(" ) values ( " + parameters.toString() +" )" );
		return sb.toString();
	}
	
	/**
	 * @return String - sql for creating prepared update statement
	 */
	private String getPreparedUpdateStatementString() throws Exception
	{
		this.prepareObjectForWriting();
		
		//PreparedStatement pstmt = conn.prepareStatement();
		StringBuffer sb = new StringBuffer();

		sb.append("update " + className + " set ");
		
		if (fieldNames != null)
		{
			Iterator iter = this.fieldNames.iterator();
			while ( iter.hasNext() )
			{
				sb.append( iter.next() +"=?");
				
				// If there are more elements use a comma
				if (iter.hasNext())
				{
					sb.append(", ");
				}
			}
		}
		sb.append(" where " + className + "_id = " + primaryKey);
		
		return sb.toString();
	}
	
	/**
	 * Searches for an identical record in the db.
	 * 
	 * @param uniqueFields -- 
	 * @return int - primaryKey ( returns 0 if no record )
	 */
	public int isObjectInDatabase( Vector uniqueFields) throws Exception
	{
		primaryKey = 0;
		Statement stmt = conn.createStatement();
		Hashtable nameValue = new Hashtable();
		
		// Construct Query using Reflection of Strings and ints only
		for (int i=0; i < methods.length ; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();
			// empty parameter list for a get method
			Object[] parameters = {};
				
			// Search this object for get methods 
			if ( VBObjectUtils.isGetMethod(method) )
			{
				String fieldName = VBObjectUtils.getFieldName(methodName, null);
				// If null or matches a field that is unique
				if (uniqueFields == null
					|| uniqueFields.contains(VBObjectUtils.getFieldName(methodName, null)) )
				{
				
					if ( VBObjectUtils.isGetMethod(method, "java.lang.String") )
					{					
						// get the name and value of the field 
						String fieldValue = (String) method.invoke(object, parameters);
						
						if ( ! Utility.isStringNullOrEmpty(fieldName) && ! Utility.isStringNullOrEmpty(fieldValue))
						{
							nameValue.put(fieldName, fieldValue);
						}
					}
					else if ( VBObjectUtils.isGetMethod(method, "int") )
					{
						// I have a referenced Object  -- I want to get its foriegn key
						String fieldValue = (String) foreignKeyHash.get(fieldName.toUpperCase() );
						if (Utility.isStringNullOrEmpty(fieldValue))
						{			
							//System.out.println("ObjectToDB > No stored FK for " + fieldName + " " + foreignKeyHash);	
							// Check the Object
							Integer fieldValueInt = (Integer) method.invoke(object, parameters);
							fieldValue = fieldValueInt.toString();	
						}
						
						// -1 and 0 are not real FK ignore
						if (! fieldValue.equals("-1") && !  fieldValue.equals("0") )
						{				
							nameValue.put(fieldName, fieldValue);
						}
					}
				}
			}
			else 
			{
				//System.out.println("I am a " + method.getReturnType().getName());
				// Not of interest
			}
		}		
		
		StringBuffer sql = new StringBuffer();
		sql.append("select " + className + "_id from " + className +" where ");
		Enumeration columnNames = nameValue.keys();
		while (columnNames.hasMoreElements() )
		{
			String columnName = (String) columnNames.nextElement();
			String columnValue = (String) nameValue.get(columnName);
			sql.append(columnName + " = '" + columnValue +"'");
			
			if (columnNames.hasMoreElements() )
			{
				sql.append(" and ");
			}
		}
		
		System.out.println(" --->" + sql);
		
		ResultSet rs = stmt.executeQuery( sql.toString() );
		
		if (rs.next() )
		{
			primaryKey  = rs.getInt(1);
		}
		return primaryKey;
	}
	
	
	/**
	 * Get the primary this object was stored with in the database
	 * 
	 * @return int -- Primary Key
	 */
	public int getPrimaryKey() {
		return primaryKey;
	}
	
	/**
	 * Generate  primarykey  this object will use when stored with in the database
	 * The value is available as the class variable primaryKey
	 */
	public void generatePrimaryKey() throws SQLException {
		System.out.println("Generating key for class: " + className);
		this.primaryKey = 
			Utility.dbAdapter.getNextUniqueID(conn, className, className + "_id");
	}
}
