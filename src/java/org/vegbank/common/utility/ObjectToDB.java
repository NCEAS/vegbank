/*
 *	'$RCSfile: ObjectToDB.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-04-16 17:37:44 $'
 *	'$Revision: 1.1 $'
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

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
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
	Connection conn;	
	protected AbstractList fieldNames = new Vector();
	private AbstractList fieldValues = new Vector();
	private int primaryKey;
	private Hashtable foreignKeyHash = new Hashtable();
	
	
	public ObjectToDB(Object object)
	{
		super(object);
		System.out.println("ObjectToDB > New Instance");
		conn= new Utility().getConnection("vegbank");
		try
		{
			this.generatePrimaryKey();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("----------------------------------------------");
		System.out.println("Created NEW Object for " + className);
		System.out.println("PK is " + this.primaryKey);
		System.out.println("----------------------------------------------");
	}
	
	public void setForeignKey( String fieldName, int foreignKey)
	{	
		System.out.println("Allocating a FK " + fieldName + " " + foreignKey);		
		foreignKeyHash.put( fieldName.toUpperCase() , new Integer(foreignKey));
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
		return className + "_ID";
	}
	
	private void prepareObjectForWriting()
	{
		for (int i=0; i < methods.length ; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();
			
			try
			{
				if ( isGetMethod(method) )
				{
					if ( isGetMethod(method, "java.lang.String") )
					{
						String fieldName = this.getFieldName(methodName, null);
						String fieldValue = (String) method.invoke(object, null);
						fieldNames.add(fieldName);
						Object[] parameters = {};
						fieldValues.add(method.invoke(object, parameters));
					}
					else if ( XMLToObject.existsInVegbankObjectModel(method.getReturnType().getName()))
					{
						String fieldName = this.getFieldName(methodName, "_ID");
						String fieldValue = null;
						//System.out.println("searching " + object.getClass().getName() + "  for " + method.getName() );
						Object obj = method.invoke(object, null);
						if ( obj != null)
						{
							//System.out.println("========== " + obj + " && " + method.getReturnType().getName() );
							ObjectToDB o2d = new ObjectToDB(obj);
							o2d.setForeignKey( fieldName, this.primaryKey );
							o2d.write();
							
							// Use the allocated pk as the foreign key for this table							
							fieldValue = Integer.toString( o2d.getPrimaryKey() ); // TODO: handle this as an int throughout !!!
						}
						else
						{
							Integer fK = (Integer) this.foreignKeyHash.get(fieldName.toUpperCase());
							
							Enumeration e = foreignKeyHash.keys();
							while ( e.hasMoreElements() )
							{
								Object  key = e.nextElement();
								//System.out.println(">>>>>>>>>>>>>" + key + "== " + foreignKeyHash.get(key));
							}
							
							if ( fK != null)
							{
								fieldValue = fK.toString();
							}
							else
							{
								fieldValue = null;
							}
							System.out.println("Could not get a VALUE for: " +this.getFieldName(methodName, "_ID") + " using " + fieldValue );
						}
						fieldNames.add(fieldName);
						fieldValues.add(fieldValue);
					}
				}
				else 
				{
					System.out.println("I am a " + method.getReturnType().getName());
					// Not of interest
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void write()
	{	
		this.prepareObjectForWriting();
		// Get all the Prepared Statements
		String statement = this.getPreparedStatementString();
		try
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
				if ( value == null || value.equals(""))
				{
					pstmt.setNull(modInt, java.sql.Types.VARCHAR );
				}
				else
				{
					pstmt.setString(modInt,  Utility.escapeCharacters(value) );
				}
			}
			
			// Get a value for the primary key and add it to statement
			System.out.println("PK is " + primaryKey);
			pstmt.setInt(fieldValues.size() +1, primaryKey);
				
			pstmt.execute();
		}
		catch (SQLException e)
		{
			System.out.println("ObjectToDB > Problem with SQL '" + statement + "'"  );
			e.printStackTrace();
		}
			

		
		// Write the prepared Statements
	}
	
	/**
	 * @return String - sql for creating prepared statement
	 */
	public String getPreparedStatementString()
	{
		//PreparedStatement pstmt = conn.prepareStatement();
		StringBuffer sb = new StringBuffer();
		StringBuffer parameters = new StringBuffer();
		sb.append("insert into " + className + " ( ");
		
		if (fieldNames != null)
		{
			Iterator iter = fieldNames.iterator();
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
	
	
	//	Utility.dbAdapter.getNextUniqueID
	
	
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
