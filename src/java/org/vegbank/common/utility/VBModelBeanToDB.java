/*
 *	'$RCSfile: VBModelBeanToDB.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-02-07 06:44:13 $'
 *	'$Revision: 1.2 $'
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.vegbank.common.model.VBModelBean;

/**
 * <p>Writes a <code>VBModelBean<code> to the database.</p>
 * 
 * <p>Currently only handles many to one children not one to one's, believe it
 * or not ;) . This will be fixed but currently clients of this need to 
 * consider this.</p>
 * 
 * <todo>Handle one to one children also<todo>
 * 
 * @author farrell
 */

public class VBModelBeanToDB
{
	private boolean existsInDatabase = false;
	private DBConnection conn;	
	//protected AbstractList fieldNames = new Vector();
	//private AbstractList fieldValues = new Vector();
	//private Hashtable attributeNameValues = new Hashtable();
	//private long primaryKey;
	//private Hashtable foreignKeyHash = new Hashtable();
	
	//private VBModelBean bean = null;
	//private Class theCurrentClass = null;	
	//private Method[] currentMethods = null;
	
	private Vector beans = new Vector();
	//private Vector classes = new Vector();
	private Vector methodsList =  new Vector();
	private Vector classNameList =  new Vector();
			
	public VBModelBeanToDB() throws Exception
	{
		//super(object);
		System.out.println("VBModelBeanToDB > New Instance");
		this.initDB();
		
/*
		if (conn == null )
		{
			this.initDB();
		}
		else
		{
			this.conn = conn;
		}
		
		long pk = this.isObjectInDatabase(null);
		if ( pk == 0 )
		{
			// Does not exist in database ( reseve a pk )
			this.generatePrimaryKey();
			LogUtility.log("VBModelBeanToDB : ----------------------------------------------");
			LogUtility.log("VBModelBeanToDB : Created NEW Object for " + className);
			LogUtility.log("VBModelBeanToDB : PK is " + this.primaryKey);
			LogUtility.log("VBModelBeanToDB : ----------------------------------------------");
		}
		else 
		{
			this.primaryKey = pk;
			this.existsInDatabase = true;
			LogUtility.log("VBModelBeanToDB : ----------------------------------------------");
			LogUtility.log("VBModelBeanToDB : Object " + className + " exists in Database");
			LogUtility.log("VBModelBeanToDB : PK is " + this.primaryKey);
			LogUtility.log("VBModelBeanToDB : ----------------------------------------------");
		}
*/
	}
	
	private void initDB() throws SQLException
	{
		conn= DBConnectionPool.getInstance().getDBConnection("Need connection for inserting object");
		conn.setAutoCommit(false);
	}
		
	
//	private void setForeignKey( String fieldName, long foreignKey)
//	{	
//		LogUtility.log("VBModelBeanToDB : Allocating a FK " + fieldName + " " + foreignKey);		
//		foreignKeyHash.put( fieldName.toUpperCase() , new Long(foreignKey).toString()  );
//	}
	  
//	public void setForeignKeys( Hashtable foreignKeys)
//	{	
//		Enumeration e = foreignKeys.keys();
//		while ( e.hasMoreElements() )
//		{
//			String key = (String) e.nextElement();
//			foreignKeyHash.put( key.toUpperCase() ,  foreignKeys.get(key) ); 
//		}
//	}
	
	public String getPrimaryKeyFieldName() 
	{
			return classNameList.lastElement() + "_id";
	}
        
//	private void prepareObjectForWriting() throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, InvocationTargetException
//	{
//		// Get current declared methods array
//		Method[] methods = ( Method[] ) methodsList.lastElement();
//		for (int i=0; i < methods.length ; i++)
//		{
//			Method method = methods[i];
//			String methodName = method.getName();
//			// empty parameter list for a get method
//			Object[] parameters = {};
//			
//			// Search this object for get methods 
//			if ( VBObjectUtils.isGetMethod(method) )
//			{
//				if ( VBObjectUtils.isGetMethod(method, "java.lang.String") )
//				{					
//					// get the name and value of the field 
//					String fieldName = VBObjectUtils.getFieldName(methodName, null, null);
//					String fieldValue = (String) method.invoke(beans.lastElement(), parameters);
//					
//					storeNameAndValue(fieldName, fieldValue);
//				}
//				else if ( VBObjectUtils.isGetMethod(method, "long") )
//				{
//					// I have a referenced Object  -- I want to get its foriegn key
//					String fieldName = VBObjectUtils.getFieldName(methodName, null , null);
//					String fieldValue = (String) foreignKeyHash.get(fieldName.toUpperCase() );
//					if (Utility.isStringNullOrEmpty(fieldValue))
//					{			
//						//System.out.println("VBModelBeanToDB > No stored FK for " + fieldName + " " + foreignKeyHash);	
//						// Check the Object
//						Long fieldValueLong = (Long) method.invoke(beans.lastElement(), parameters);
//						fieldValue = fieldValueLong.toString();	
//					}
//					
//					// -1 and 0 are not real FK ignore
//					if (! fieldValue.equals("-1") && !  fieldValue.equals("0") )
//					{				
//						storeNameAndValue(fieldName, fieldValue);
//					}
//					//System.out.println("VBModelBeanToDB > " + fieldName + " v. " + fieldValue);
//				}
//			}
//			else 
//			{
//				//System.out.println("I am a " + method.getReturnType().getName());
//				// Not of interest
//			}
//		}
//	}

//	private PreparedStatement getPopulatedPreparedStatement(String statement)
//		throws SQLException
//	{
//		PreparedStatement pstmt = conn.prepareStatement(statement);
//		
//		VBModelBean currentBean = (VBModelBean) beans.lastElement();
//		LinkedHashMap nameValues = currentBean.toOrderedHashMap();
//		Collection values = nameValues.values();
//		Iterator it = values.iterator();
//		
//		
//		//Iterator it = fieldValues.iterator();
//		//System.out.println("----> " + fieldValues.size() + " " +  fieldNames.size());
//		for (int i=1; it.hasNext() ; i++)
//		{
//			// JDBC starts counting from 1 not 0
//			Object value = it.next();
//			LogUtility.log("VBModelBeanToDB : setting '" + value +  "' for: " + );
//			if ( Utility.isStringNullOrEmpty(value) )
//			{
//				pstmt.setNull(modInt, java.sql.Types.VARCHAR );
//			}
//			else
//			{
//				pstmt.setString(modInt,  Utility.encodeForDB(value) );
//			}
//		}	
//		return pstmt;
//	}

	/**
	 * Inserts a <code>VBModelBean</code> into the database.
	 * 
	 * 
	 * @param bean the bean to write to the db.
	 * @return the Primary Key allocated
	 * @throws Exception any errors encounter get thrown up.
	 */
	public long insert(VBModelBean bean) throws Exception
	{
		// Add to variable lists ( as a convience )
		beans.add(bean);
		methodsList.add(bean.getClass().getDeclaredMethods());
		// Get unqualified name
		classNameList.add(
			VBObjectUtils.getUnQualifiedName(bean.getClass().getName()));

		long PK = this.isObjectInDatabase(bean);
		if (PK == 0)
		{
			PK = this.reservePrimaryKey();

			// Need add the PK to the object
			bean.putPrimaryKey(PK);

			//TODO: Need to recurse into childObjects for the PKs
			Hashtable getBeanMethods = this.getBeanGetFKSetPairs();
			Enumeration pairs = getBeanMethods.elements();
			while (pairs.hasMoreElements())
			{
				Vector getBeanSetFKPair = (Vector) pairs.nextElement();
				Object tempGetBeanMethod = getBeanSetFKPair.get(0);
				Object tempSetFKMethod = getBeanSetFKPair.get(1);
				// Handle nulls
				if (tempGetBeanMethod != null && tempSetFKMethod != null)
				{
					Method getBeanMethod = (Method) tempGetBeanMethod;
					VBModelBean newBean =
						(VBModelBean) getBeanMethod.invoke(bean, null);

					// Handle null
					if (newBean != null)
					{
						// TODO: write into db and get PK -- RECURSIVE
						//VBModelBeanToDB bean2db = new VBModelBeanToDB(bean, conn);
						long FKtoUse = this.insert(newBean);

						// TODO: Fill in the FK on the current bean
						Method setFKMethod = (Method) tempSetFKMethod;
						LogUtility.log(
							">>> Setting " + setFKMethod + " to " + FKtoUse);
						setFKMethod.invoke(
							bean,
							new Object[] { new Long(FKtoUse)});
					}
				}
			}


			// Get the  Statement and run
			String SQL = this.getStatement(bean);
			Statement stmt = conn.createStatement();
			stmt.execute(SQL);

			HashMap foreignKeys = new HashMap();

			// TODO: Need to get the FK name?
			// FIXME: Need a lookup here as the FKName is a function of 
			// FKtable name, tablename and ??? ... hack for now

			String foreignKeyName = this.getPrimaryKeyFieldName();
			foreignKeys.put(foreignKeyName, new Long(PK));

			// Write Objects that depend on this PK to database
			handleChildren(foreignKeys, bean);
			
		}
		else
		{
			LogUtility.log("Found an identical record in the Database");
		}

		// About to drop out of recursion CLEAN UP
		beans.removeElement(bean);
		methodsList.removeElement(methodsList.lastElement());
		classNameList.removeElement(classNameList.lastElement());

		LogUtility.log(
			"VBModelBeanToDB: Have "
				+ beans.size()
				+ " to work on before commiting.");

		// Commit the transaction if no more work
		if (beans.size() == 0)
		{
			conn.commit();
			LogUtility.log("VBModelBeanToDB: Commited Transaction");
		}

		// if we get here 
		return PK;
	}
	
	/**
	 * Much messiness lives here<br/>
	 * 
	 * I am using reflection to get all the methods related to getting FK <code>VBModelBean<code> 
	 * and setting the FKs that the bean has.<br/>
	 * 
	 * Each get method that returns a VBModelBean has a corresponding setFK(long) method. 
	 * <br/>
	 * I am tring to pair these up in a datastructure, a hashtable of all the pairs 
	 * using the attibute name as key. Each pair of methods is stored in a Vector 
	 * the first element is the get method and the second is the setFK method.
	 * <br/>
	 * 
	 * @return
	 */
	private Hashtable getBeanGetFKSetPairs()
	{
		// The key is the attribute name, contains a Vector with the two methods
		Hashtable beanGetFKSetHash = new Hashtable();
		// TODO Searches current Bean for its getBean, setFK pairs
		// Get current declared methods array
		Method[] methods = ( Method[] ) methodsList.lastElement();
		for (int i=0; i < methods.length ; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();

			// TODO: Make a method for this check
			if ( VBObjectUtils.isGetMethod(method) && VBObjectUtils.existsInVegbankObjectModel( method.getReturnType().getName()) )
			{
				// Of interest
				// Get the attribute name
				String key =  VBObjectUtils.getFieldName(method.getName(), null, null);
				
				Vector beanGetFKSet = (Vector) beanGetFKSetHash.get(key);
				if ( beanGetFKSet == null )
				{
					// Create an entry with this Key
					beanGetFKSet = new Vector();
				}
				// Add to this Vector 
				beanGetFKSet.add(0, method);
				beanGetFKSetHash.put(key, beanGetFKSet);
				
				LogUtility.log("After adding a get for : " + key + " >>> " + beanGetFKSetHash);
			}	
			else if ( VBObjectUtils.isSetMethod(method) )
			{
				Class[] parameters  = method.getParameterTypes();
				if ( parameters[0].toString().equalsIgnoreCase("long") )
				{
					// Has it the right name?
					String fieldName = VBObjectUtils.getFieldName(method.getName(), null,  null);
				
					LogUtility.log(">>>> " +fieldName+ " vs. " +this.getPrimaryKeyFieldName() );
					// Make sure not the PK
					if ( fieldName.equalsIgnoreCase( this.getPrimaryKeyFieldName() ) )
					{
						// Not interested
					}
					//Also want to ignore inverted relationship but not sure how
					//else if ( object.isInvertedRelationship(fieldName) )
					//{
					//	// Not interested
					//}
					else
					{
					
						// Save into Hash
						// Strip off the trailing 'id' to get the key
						String key = fieldName.replaceFirst("_id$", "");
						Vector beanGetFKSet = (Vector) beanGetFKSetHash.get(key);
						if ( beanGetFKSet == null )
						{
							// Create an entry with this Key
							beanGetFKSet = new Vector();
						}
						// Add to this Vector 
						if ( beanGetFKSet.isEmpty() )
						{
							//	To avoid ArrayIndexOutOfBoundsException put placeholder
							beanGetFKSet.add(0, null); 
						}
						beanGetFKSet.add(1, method);
						beanGetFKSetHash.put(key, beanGetFKSet);
						LogUtility.log("After adding a set for : " + key + " >>> " + beanGetFKSetHash);
					}
				}					
			}
			else
			{
				// Not of interest
			}
		}
		
		Utility.prettyPrintHash(beanGetFKSetHash);
		return beanGetFKSetHash;
	}

	/**
	 * 
	 */
	private void handleChildren(HashMap foreignKeys, VBModelBean currentBean) throws Exception
	{
		// Get current declared methods array
		Method[] methods = ( Method[] ) methodsList.lastElement();
		for (int i=0; i < methods.length ; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();

			if ( VBObjectUtils.isGetMethod(method, "java.util.List") )
			{
				//LogUtility.log("VBModelBeanToDB : Handling a list");
				// Need to Loop throught all elements and insert them into the DB
				List objList = (List) method.invoke( currentBean, null);
				Iterator it = objList.iterator();
				while ( it.hasNext() )
				{
					VBModelBean bean = (VBModelBean) it.next();
					
					setForeignKeys(foreignKeys, bean);
					// Insert the Object into the Database
					//o2d.setForeignKey( this.getPrimaryKeyFieldName() , this.primaryKey );
					this.insert(bean);
				}
			}
			else
			{
				// I don't care	
			}
		}
	}

	/**
	 * Convience for adding foreign keys to a bean
	 * 
	 * @param foreignKeys
	 */
	private void setForeignKeys(HashMap foreignKeys, VBModelBean bean)
	{
		if ( foreignKeys != null )
		{
			Set keys = foreignKeys.keySet();
			Iterator iter = keys.iterator();
			while (iter.hasNext())
			{
				Object key = iter.next();
				Object foreignKeyValue = foreignKeys.get(key);
				
				if ( key != null && foreignKeyValue != null )
				{
					// Do casts
					String keyName = (String) key;
					Long keyValue = (Long) foreignKeyValue;
				
					LogUtility.log("Set FK name " + keyName + " to " +  foreignKeyValue);
					bean.putForeignKey(keyName,keyValue.longValue() );
				}
			}
		}
	}

	/**
	 * @return String - sql for creating prepared statement
	 */
	public String getStatement( VBModelBean currentBean ) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		StringBuffer parameters = new StringBuffer();
		sb.append(
			"insert into \""
				+ ((String) classNameList.lastElement()).toLowerCase()
				+ "\" ( ");
		
		// Get the current bean
		//VBModelBean currentBean = (VBModelBean) beans.lastElement();
		LinkedHashMap nameValues = currentBean.toOrderedHashMap();
		Set keys = nameValues.keySet();
		Iterator it = keys.iterator();

		boolean firstPass = true;
		while (it.hasNext())
		{
			Object key = it.next();
			Object value = nameValues.get(key);
			
			if ( value instanceof String )
			{
				// Cast to String for now
				String stringValue = value.toString();
				if ( Utility.isStringNullOrEmpty(stringValue) )
				{
					// Do nothing
				}
				else
				{
					firstPass = this.handleCommas(sb, parameters, firstPass);
					
					sb.append(key);
					parameters.append("'" +  Utility.encodeForDB(stringValue) + "'");
				}
			}
			else if ( value instanceof Long)
			{
				long longValue = ((Long) value).longValue();
				if ( longValue == 0 || longValue == -1 )
				{
					// Do nothing
					//pstmt.setNull( jdbcCounter, java.sql.Types.INTEGER );
				}
				else 
				{
					firstPass = this.handleCommas(sb, parameters, firstPass);
					sb.append(key);
					parameters.append(value );
				}
				//LogUtility.log(" found a Long " );
			}
			else if ( value instanceof VBModelBean )
			{
				// Nothing for now
				//LogUtility.log(" found a ???? " );
			}
			else
			{
				// ????
				//LogUtility.log(" found a ???? " );
			}
		}

		sb.append(" ) VALUES ( " + parameters.toString() + " )");
			
		LogUtility.log("VBModelBeanToDB: SQL >" + sb.toString());
		return sb.toString();
	}
	
	/**
	 * @return String - sql for creating prepared update statement
	 */
//	private String getPreparedUpdateStatementString() throws Exception
//	{
//		this.prepareObjectForWriting();
//		
//		//PreparedStatement pstmt = conn.prepareStatement();
//		StringBuffer sb = new StringBuffer();
//
//		sb.append("update " + classNameList.lastElement() + " set ");
//		
//		if (fieldNames != null)
//		{
//			Iterator iter = this.fieldNames.iterator();
//			while ( iter.hasNext() )
//			{
//				sb.append( iter.next() +"=?");
//				
//				// If there are more elements use a comma
//				if (iter.hasNext())
//				{
//					sb.append(", ");
//				}
//			}
//		}
//		sb.append(" where " + classNameList.lastElement() + "_id = " + primaryKey);
//		
//		return sb.toString();
//	}
	
	/**
	 * Add a comma if not the first pass
	 * 
	 * @param sb
	 * @param parameters
	 * @param firstPass
	 * 
	 * @return boolean -- is this the first pass ( always false )
	 */
	private boolean handleCommas(StringBuffer sb, StringBuffer parameters, boolean firstPass)
	{
		//LogUtility.log("About to try and add comma if not " + firstPass);
		if ( firstPass )
		{
			firstPass = false;
		}
		else
		{
			sb.append(",");
			parameters.append(",");
		}
		return firstPass;
	}

	/**
	 * Searches for an identical record in the db.
	 * 
	 * @param uniqueFields -- 
	 * @return long - primaryKey ( returns 0 if no record )
	 */
	public long isObjectInDatabase( VBModelBean currentBean ) throws Exception
	{
		long primaryKey = 0;
		Statement stmt = conn.createStatement();
		Hashtable nameValue = new Hashtable();
		
		StringBuffer sql = new StringBuffer();
		
		// Get the current bean
		//VBModelBean currentBean = (VBModelBean) beans.lastElement();
		LinkedHashMap nameValues = currentBean.toOrderedHashMap();
		Set keys = nameValues.keySet();
		Iterator it = keys.iterator();

		boolean firstPass = true;
		boolean foundACField = false;
		while (it.hasNext())
		{
			// looking for an accession code
			// that's the only field we use to check for a duplicate
			String key = (String)it.next();
			if (key.equalsIgnoreCase("accessioncode")) {
				// found an accession code field in this table (entity/object)
				foundACField = true;

				String value = (String)nameValues.get(key);

				if (value == null || !value.equals("")) {
					// there is no AC to check
					LogUtility.log("VBMBTDB.isObjectInDatabase(): no AC value");
					return 0;
				}

				LogUtility.log("VBMBTDB.isObjectInDatabase(): got AC: " + value);
				String table = ((String)classNameList.lastElement()).toLowerCase();
				sql.append("SELECT ")
					.append(table)
					.append("_id FROM ")
					.append(table)
					.append(" WHERE ")
					.append(key)
					.append(" = '")
					.append(Utility.encodeForDB(value))
					.append("'");

				// b-b-break!!
				break;
			}
			
			/*
			if ( value instanceof String )
			{
				// Cast to String for now
				String stringValue = value.toString();
				if ( Utility.isStringNullOrEmpty(stringValue) )
				{
					if (firstPass) 
						firstPass = false;
					else
						sql.append(" AND ");
						
					sql.append(key + " IS NULL");
				}
				else
				{
					if (firstPass) 
						firstPass = false;
					else
						sql.append(" AND ");
						
					sql.append(key + " = '" + Utility.encodeForDB(stringValue) + "'");
				}
			}
			else if ( value instanceof Long)
			{
				// FIXME: Ignoring longs for now, need to filter out PK
//				long longValue = ((Long) value).longValue();
//				if ( longValue == 0 || longValue == -1 )
//				{
//					if (firstPass) 
//						firstPass = false;
//					else
//						sql.append(" AND ");
//						
//					sql.append(key + " = NULL");
//				}
//				else 
//				{
//					if (firstPass)
//						firstPass = false;
//					else
//						sql.append(" AND ");
//
//					sql.append(key + " = " + longValue);
//				}
				LogUtility.log(" found a Long " );
			}
			else if ( value instanceof VBModelBean )
			{
				// Nothing for now
				//LogUtility.log(" found a ???? " );
			}
			else
			{
				// ????
				LogUtility.log(" found a ???? " );
			}
			*/
		}

		if (!foundACField) {
			// there is no AC field in this table
			LogUtility.log("VBMBTDB.isObjectInDatabase(): no AC field");
			return 0;
		}
		///////////////////////////////////////////
		
//		// Construct Query using Reflection of Strings and ints only
//		// Get current declared methods array
//		Method[] methods = ( Method[] ) methodsList.lastElement();
//		for (int i=0; i < methods.length ; i++)
//		{
//			Method method = methods[i];
//			String methodName = method.getName();
//			// empty parameter list for a get method
//			Object[] parameters = {};
//				
//			// Search this object for get methods 
//			if ( VBObjectUtils.isGetMethod(method) )
//			{
//				String fieldName = VBObjectUtils.getFieldName(methodName, null, null);
//				// If null or matches a field that is unique
//				if (uniqueFields == null
//					|| uniqueFields.contains(VBObjectUtils.getFieldName(methodName, null, null)) )
//				{
//				
//					if ( VBObjectUtils.isGetMethod(method, "java.lang.String") )
//					{					
//						// get the name and value of the field 
//						String fieldValue = (String) method.invoke(beans.lastElement(), parameters);
//						
//						if ( ! Utility.isStringNullOrEmpty(fieldName) && ! Utility.isStringNullOrEmpty(fieldValue))
//						{
//							nameValue.put(fieldName, fieldValue);
//						}
//					}
//					else if ( VBObjectUtils.isGetMethod(method, "long") )
//					{
//						// I have a referenced Object  -- I want to get its foriegn key
//						String fieldValue = (String) foreignKeyHash.get(fieldName.toUpperCase() );
//						if (Utility.isStringNullOrEmpty(fieldValue))
//						{			
//							//System.out.println("VBModelBeanToDB > No stored FK for " + fieldName + " " + foreignKeyHash);	
//							// Check the Object
//							Long fieldValueLong = (Long) method.invoke(beans.lastElement(), parameters);
//							fieldValue = fieldValueLong.toString();	
//						}
//						
//						// -1 and 0 are not real FK ignore
//						if (! fieldValue.equals("-1") && !  fieldValue.equals("0") )
//						{				
//							nameValue.put(fieldName, fieldValue);
//						}
//					}
//				}
//			}
//			else 
//			{
//				//System.out.println("I am a " + method.getReturnType().getName());
//				// Not of interest
//			}
//		}		
//		
//		StringBuffer sql = new StringBuffer();
//		sql.append("select " + classNameList.lastElement() + "_id from " + ( (String) classNameList.lastElement()).toLowerCase() +" where ");
//		Enumeration columnNames = nameValue.keys();
//		while (columnNames.hasMoreElements() )
//		{
//			String columnName = (String) columnNames.nextElement();
//			String columnValue = (String) nameValue.get(columnName);
//			sql.append(columnName + " = '" + columnValue +"'");
//			
//			if (columnNames.hasMoreElements() )
//			{
//				sql.append(" and ");
//			}
//		}
		
		System.out.println(" --->" + sql);
		
		ResultSet rs = stmt.executeQuery( sql.toString() );
		
		if (rs.next() )
		{
			primaryKey  = rs.getLong(1);
		}
		return primaryKey;
	}
	
	/**
	 * Generate  primarykey  this object will use when stored with in the database
	 * The value is available as the class variable primaryKey
	 */
	public long reservePrimaryKey() throws SQLException 
	{
		LogUtility.log("VBModelBeanToDB : Generating key for class: " + classNameList.lastElement());
		long PK  = 
			Utility.dbAdapter.getNextUniqueID(conn, (String) classNameList.lastElement(), classNameList.lastElement() + "_id");
		return PK;
	}
	
	
}
