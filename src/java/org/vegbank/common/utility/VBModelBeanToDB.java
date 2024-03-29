/*
 * '$RCSfile: VBModelBeanToDB.java,v $' 
 * Authors: @author@ Release: @release@
 * 
 * '$Author: anderson $' 
 * '$Date: 2005-07-16 03:12:42 $' 
 * '$Revision: 1.10 $'
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.vegbank.common.utility;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.Constants;
import org.vegbank.common.model.Stratum;
import org.vegbank.common.model.Stratumtype;
import org.vegbank.common.model.VBModelBean;
import org.vegbank.dataload.XML.InputPKTracker;


/**
 * <p>
 * Writes a <code>VBModelBean<code> to the database.
 * </p>
 * 
 * <p>
 * This class is handed a root VBModelBean to insert into the database. It examines
 * the bean for child beans and inserts these also.<br> 
 * Two checks are enforced to prevent the adding duplicate elements to the database.
 * <ul>
 * 	<li>
 * 		<b>AccessionCode</b> if set on a bean is checked for existence in the database.
 * 		If found that PK is used.
 * 	</li>
 *  <li>
 * 		The bean PK is checked and if a bean with the same PK has already been added in 
 * 		the <b>same</b> transaction, the database assigned PK for this bean is used.
 *    <i>
 * 			There is a difference between the bean PK, XML PK and database PK, these are poorly 
 * 			right now. Think of bean PK and XML PK as local keys for the beanTree or the XML.
 * 		</i>
 *  </li>
 * </ul>
 * </p>
 * 
 * @author farrell
 */

public class VBModelBeanToDB
{

	/**
	 * Handle for logging
	 */
	private static Log log = LogFactory.getLog(VBModelBeanToDB.class);

	private DBConnection conn;

	private List accessionCodesAdded = new ArrayList();

	private List beans = new ArrayList();

	private List classNameList = new ArrayList();

    private List methodsList = new ArrayList();

	private Map tableKeys = new HashMap();

	private Map allTableKeys = new HashMap();
	
	private InputPKTracker inputUniqueIdTracker = new InputPKTracker();

	public VBModelBeanToDB() throws Exception
	{
		//super(object);
		log.debug("New Instance");
		this.initDB();
	}

	public void initDB() throws SQLException
	{
		conn = DBConnectionPool.getInstance().getDBConnection(
				"Need connection for inserting object");
		conn.setAutoCommit(false);
	}

	private String getPrimaryKeyFieldName()
	{
		return getLastListElement(classNameList) + "_id";
	}

	/**
	 * Inserts a <code>VBModelBean</code> into the database.
	 * 
	 * @param bean the bean to write to the db.
	 * @return the Primary Key allocated
	 * @throws Exception
	 *           any errors encounter get thrown up.
	 */
	private long insertBean(VBModelBean bean) throws Exception 
    {
        return insertBean(bean, true);
    }

	/**
	 * Inserts a <code>VBModelBean</code> into the database, optionally committing it.
	 * 
	 * @param bean the bean to write to the db.
	 * @param doCommit
	 * @return the Primary Key allocated
	 * @throws Exception
	 *           any errors encounter get thrown up.
	 */
	private long insertBean(VBModelBean bean, boolean doCommit) throws Exception
	{
		long PK = 0;
        String entityName = getEntityName(bean);
        log.debug("entity name of bean to insert: " + entityName);
        classNameList.add(entityName);

        //String entityName = VBObjectUtils.getUnQualifiedName( bean.getClass().getName() );
        //String xmlPK = this.getXMLPK(bean, entityName);
        long inputUniqueId = this.getInputUniqueId(bean);
        String inputUniqueIdAsString  = new Long(inputUniqueId).toString();

        // Handle dups prevention

        // Check if this has already been added to db
        ////////if (inputUniqueId == -1 || inputUniqueId == 0) {
        // This is problematic since one could argue
        // that a 0 value indicates new record.
        if (inputUniqueId == -1) {
            log.debug("PK is -1 so not checking for duplicates");
            PK = 0;
        } else {
            long assignedPK = inputUniqueIdTracker.getAssignedPK(entityName, inputUniqueIdAsString);
            if (assignedPK != 0) {
                log.info("Already entered record for  inputUniqueId " + inputUniqueId + 
                        " into " + entityName + " using PK of " + assignedPK);
                PK = assignedPK;
            } else {
                // find record in DB
                PK = this.isObjectInDatabase(bean);
            }
        }
        
        if (PK == 0)
        {
            PK = this.reservePrimaryKey();
            // Keep track of pks and table names do I can add AccessionCodes later
            this.storeTableNameAndPK((String) getLastListElement(classNameList), PK);
            allTableKeys.put(getLastListElement(classNameList), new Long(PK));
            
            // prevent duplicates
            inputUniqueIdTracker.setAssignedPK(entityName, inputUniqueIdAsString, PK);
            //log.debug("No record added for xml PK: " + inputUniqueId
            //				+ " for table " + entityName + "; adding PK now: " + PK);

            // Need add the PK to the object
            bean.putPrimaryKey(PK);

            Map getBeanMethods = this.getBeanGetFKSetPairs(bean);
            Iterator pairs = getBeanMethods.values().iterator();
            while (pairs.hasNext())
            {
                MemberGetterSetters getBeanSetFKPair = (MemberGetterSetters)pairs.next();
                Method getBeanMethod = getBeanSetFKPair.getGetterMethod();
                Method setFKMethod = getBeanSetFKPair.getSetterMethod();

                log.debug("Processing: " + getBeanMethod + " and " + setFKMethod);

                if (getBeanMethod != null && setFKMethod != null)
                {
                    VBModelBean newBean = (VBModelBean) getBeanMethod.invoke(bean, null);

                    if (newBean != null)
                    {
                        // TODO: write into db and get PK -- RECURSIVE
                        //VBModelBeanToDB bean2db = new VBModelBeanToDB(bean, conn);

                        // Need to add FKs to the bean in some cases
                        this.handleSpecialCases(newBean);

                        long FKtoUse = this.insertBean(newBean, false);

                        // TODO: Fill in the FK on the current bean
                        //log.debug("setting FK with " + setFKMethod + "(" + FKtoUse + ")");
                        setFKMethod.invoke(bean, new Object[] {new Long(FKtoUse)});
                    }
                }
            }

            // Get the Statement and run
            String SQL = this.getStatement(bean);
            Statement stmt = conn.createStatement();
            stmt.execute(SQL);
            stmt.close();

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
            allTableKeys.put(getLastListElement(classNameList), new Long(PK));
            log.info("Found an identical record in the Database");
        }

        // About to drop out of recursion CLEAN UP
        ///////beans.remove(bean);  // can't do it here!
        methodsList.remove(getLastListElement(methodsList));
        classNameList.remove(getLastListElement(classNameList));

        //log.debug("VBModelBeanToDB: Have " + beans.size() + " to work on before commiting.");

        if (doCommit) {
            log.debug("commiting a bean");
            conn.commit();
        }

        /*
        accessionCodesAdded.addAll(this.addAllAccessionCodes());
        Iterator ait = accessionCodesAdded.iterator();
        while (ait.hasNext()) {
            log.debug("new AC: " + (String)ait.next());
        }
        if (doCommit) {
            conn.commit();
        }
        */

        return PK;
    }


	/**
	 * Inserts all beans added with addBean, adding the given <code>VBModelBean</code> 
     * to the end of the list.
	 * 
	 * @param bean the bean to write to the db.
	 * @return the Primary Key allocated
	 * @throws Exception
	 *           any errors encounter get thrown up.
	 */
	public long insert(VBModelBean addBean) throws Exception
	{
		long PK = 0;
		
		// Add to variable lists ( as a convience )
        if (addBean != null) {
            beans.add(addBean);
        }

        Iterator bit = beans.iterator();
        while (bit.hasNext()) {
            VBModelBean bean = (VBModelBean)bit.next();
            PK = insertBean(bean, false);
        } // end while


		// Commit the transaction if no more work
        conn.commit();
        log.info("Committed Transaction");

        // Need to add accessioncodes
        log.debug("Adding AccessionCodes to loaded data");
        accessionCodesAdded.addAll(this.addAllAccessionCodes());
        conn.commit();
        //log.debug("Committed AccessionCode updates: " + accessionCodesAdded);

        // All finished with the database connection
        returnConnection();

        beans = new ArrayList();

		// if we get here
		return PK;
	}


	/**
	 * @param bean
	 * @return
	 */
	private String getEntityName(VBModelBean bean)
	{
		return VBObjectUtils.getUnQualifiedName(bean.getClass().getName());
	}

	/**
	 * @param newBean
	 */
	private void handleSpecialCases(VBModelBean newBean)
	{
		// if this is a stratumtype bean it needs a stratummethod_id
		if (newBean instanceof Stratumtype)
		{
			Stratumtype stratumType = (Stratumtype) newBean;
			log.debug(">>>" + allTableKeys);
			Long smPK = (Long) allTableKeys.get("Stratummethod");
			stratumType.setStratummethod_id(smPK.longValue());
		} else if (newBean instanceof Stratum)
		{
			Stratum stratum = (Stratum) newBean;
			log.debug(">>>" + allTableKeys);
			Long obPK = (Long) allTableKeys.get("Observation");
			stratum.setObservation_id(obPK.longValue());
		} else
		{
			// Do nothing
		}

	}

	/**
	 * Much messiness lives here <br/>
	 * 
	 * I am using reflection to get all the methods related to getting FK
	 * <code>VBModelBean<code> 
	 * and setting the FKs that the bean has.<br/>
	 * 
	 * Each get method that returns a VBModelBean has a corresponding setFK(long) method. 
	 * <br/>
	 * I am tring to pair these up in a datastructure, a hashtable of all the pairs 
	 * using the attibute name as key. Each pair of methods is stored in a List 
	 * the first element is the get method and the second is the setFK method.
	 * <br/>
	 * 
	 * @return Map that stores the get and set methods for the current bean
	 */
	public Map getBeanGetFKSetPairs(VBModelBean bean)
	{
		// The key is the attribute name, contains a List with the two methods
		Map beanGetFKSetHash = new HashMap();
		String entityName = getEntityName(bean);

		Method[] methods = bean.getClass().getDeclaredMethods();

		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			// Is this a relevant getMethod
			if (VBObjectUtils.isGetMethod(method)
					&& VBObjectUtils.existsInVegbankObjectModel(method.getReturnType()
							.getName()))
			{
				String key = VBObjectUtils.getFieldName(method.getName(), null, null);

				MemberGetterSetters beanGetFKSet = (MemberGetterSetters) beanGetFKSetHash
						.get(key);
				if (beanGetFKSet == null)
				{
					// Create an entry with this Key
					beanGetFKSet = new MemberGetterSetters(entityName);
				}
				// Add to this List
				beanGetFKSet.setGetterMethod(method);
				beanGetFKSetHash.put(key, beanGetFKSet);

				log.debug("After adding a get for : " + key + " >>> " + beanGetFKSetHash);
			}
			// Is this a set method
			else if (VBObjectUtils.isSetMethod(method))
			{
				Class[] parameters = method.getParameterTypes();
				// has the right type?
				if (parameters[0].toString().equalsIgnoreCase("long"))
				{
					// Has it the right name?
					String fieldName = VBObjectUtils.getFieldName(method.getName(), null,
							null);

					//log.debug(">>>> " + fieldName + " vs. " + entityName + "_ID");
					// Make sure not the PK
					if (Utility.getPKNameFromTableName(entityName).equalsIgnoreCase(fieldName))
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
						MemberGetterSetters beanGetFKSet = (MemberGetterSetters) beanGetFKSetHash
								.get(key);
						if (beanGetFKSet == null)
						{
							// Create an entry with this Key
							beanGetFKSet = new MemberGetterSetters(entityName);
						}
						beanGetFKSet.setSetterMethod(method);
						beanGetFKSetHash.put(key, beanGetFKSet);
						//log.debug("After adding a set for : " + key + " >>> " + beanGetFKSetHash);
					}
				}
			} else
			{
				// Not of interest
			}

		}

		return beanGetFKSetHash;
	}

	/**
	 *  
	 */
	private void handleChildren(Map foreignKeys, VBModelBean currentBean)
			throws Exception
	{
		// Get current declared methods array
		methodsList = new ArrayList();
		methodsList.add(currentBean.getClass().getDeclaredMethods());
		Method[] methods = (Method[]) getLastListElement(methodsList);
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];

			if (VBObjectUtils.isGetMethod(method, "java.util.List")) {
				//log.debug(method.getName() + " should return a List of beans");
				// Need to loop through all elements and insert them into the DB
				List objList = (List)method.invoke(currentBean, null);
				Iterator it = objList.iterator();
				while (it.hasNext()) {
					VBModelBean bean = (VBModelBean)it.next();
					setForeignKeys(foreignKeys, bean);
                    
					this.insertBean(bean, false);
				}
			}
		}
	}

	/**
	 * Convience for adding foreign keys to a bean
	 * 
	 * @param foreignKeys
	 */
	private void setForeignKeys(Map foreignKeys, VBModelBean bean)
	{
		if (foreignKeys != null)
		{
			Set keys = foreignKeys.keySet();
			Iterator iter = keys.iterator();
			while (iter.hasNext())
			{
				Object key = iter.next();
				Object foreignKeyValue = foreignKeys.get(key);

				if (key != null && foreignKeyValue != null)
				{
					// Do casts
					String keyName = (String)key;
					Long keyValue = (Long)foreignKeyValue;

					log.debug("Set FK name " + keyName + " to " + foreignKeyValue);
					bean.putForeignKey(keyName, keyValue.longValue());
				}
			}
		}
	}

	/**
	 * @return String - sql for creating prepared statement
	 */
	private String getStatement(VBModelBean currentBean) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		StringBuffer parameters = new StringBuffer();
		sb.append("insert into \""
				+ ((String) getLastListElement(classNameList)).toLowerCase() + "\" ( ");

		// Get the current bean
		//VBModelBean currentBean = (VBModelBean) getLastListElement(beans);
		LinkedHashMap nameValues = currentBean.toOrderedHashMap();
		Set keys = nameValues.keySet();
		Iterator it = keys.iterator();

		boolean firstPass = true;
		while (it.hasNext()) {
			Object key = it.next();
			Object value = nameValues.get(key);

			if (value == null) {
                continue;
            } else if (value instanceof String) {
				// Cast to String for now
				String stringValue = value.toString();
				if (Utility.isStringNullOrEmpty(stringValue)) {
					// Do nothing
				} else {
					firstPass = this.handleCommas(sb, parameters, firstPass);

					sb.append(key);
					parameters.append("'" + Utility.encodeForDB(stringValue) + "'");
				}
			} else if (value instanceof Long) {
				long longValue = ((Long) value).longValue();
				if (longValue == 0 || longValue == -1) {
					// Do nothing
					//pstmt.setNull( jdbcCounter, java.sql.Types.INTEGER );
				} else {
					firstPass = this.handleCommas(sb, parameters, firstPass);
					sb.append(key);
					parameters.append(value);
				}
				//LogUtility.log(" found a Long " );
			} else if (value instanceof VBModelBean) {
				log.debug("value of key " + key + " is an unsupported VBModelBean");
			} else {
				log.debug("value of key " + key + " is not supported: " + value);
			}
		}

		sb.append(" ) VALUES ( " + parameters.toString() + " )");

		log.debug("VBModelBeanToDB: SQL >" + sb.toString());
		return sb.toString();
	}

	/**
	 * Add a comma if not the first pass
	 * 
	 * @param sb
	 * @param parameters
	 * @param firstPass
	 * 
	 * @return boolean -- is this the first pass ( always false )
	 */
	private boolean handleCommas(StringBuffer sb, StringBuffer parameters,
			boolean firstPass)
	{
		//LogUtility.log("About to try and add comma if not " + firstPass);
		if (firstPass)
		{
			firstPass = false;
		} else
		{
			sb.append(",");
			parameters.append(",");
		}
		return firstPass;
	}

	/**
	 * <p>Searches for an identical record in the db.</p>
	 * 
	 * <p>
	 * Searchs for identity using:
	 * 	<ul>
	 *  	<li>an exact match on AccessionCode in the database</li>
	 * 		<li>has the same unique identifier been handled already in this transaction<li>
	 * 	<ul>
	 * </p> 
	 * 
	 * @param currentBean Bean to check 
	 * @return long - primaryKey ( returns 0 if no record )
	 */
	public long isObjectInDatabase(VBModelBean currentBean) throws Exception
	{
		long primaryKey = 0;

		// Need to find the accessionCode in the bean
		LinkedHashMap nameValues = currentBean.toOrderedHashMap();
		//log.debug("isObjectInDB: Bean values: " + nameValues);
		Set keys = nameValues.keySet();
		Iterator it = keys.iterator();

		while (it.hasNext())
		{
			// looking for an accession code
			// that's the only field we use to check the db for a duplicate
			String key = (String) it.next();
			if (key.equalsIgnoreCase(Constants.ACCESSIONCODENAME))
			{
				// found an accession code field in this table (entity/object)
				//foundACField = true;

				String value = (String) nameValues.get(key);

				if (Utility.isStringNullOrEmpty(value))
				{
					// there is no AC to check
					//log.debug("isObjectInDatabase(): no AC value");
					return 0;
				}

				primaryKey = this.getExtantPK(currentBean);
			}
		}
		return primaryKey;
	}

	/**
	 * Generate primarykey this object will use when stored with in the database
	 * The value is available as the class variable primaryKey
	 */
	private long reservePrimaryKey() throws SQLException
	{
		//log.debug("Generating key for class: " + getLastListElement(classNameList));
		String tableName = (String) getLastListElement(classNameList);
		String primaryKeyName = Utility.getPKNameFromTableName(tableName);
		long PK = Utility.dbAdapter
				.getNextUniqueID(conn, tableName, primaryKeyName);
		return PK;
	}

	/**
	 * Store the tableName and the Pk so an accessionCode can be generated later
	 * 
	 * @param tableName
	 * @param PK
	 */
	private void storeTableNameAndPK(String tableName, long PK)
	{
		List keys = (List)tableKeys.get(tableName);
		if (keys == null) {
			keys = new ArrayList();
			tableKeys.put(tableName, keys);
		}
		keys.add(new Long(PK));
	}

	/**
	 * Add accessionCodes to all the rows added by this loading.
	 */
	private List addAllAccessionCodes() throws SQLException
	{
		List accessionCodes = null;
		// Initialize the AccessionGen
		AccessionGen ag = new AccessionGen(this.conn, Utility
				.getAccessionPrefix());

		if (Utility.isLoadAccessionCodeOn()) {
			accessionCodes = ag.updateSpecificRows(tableKeys);
		} else {
			accessionCodes = new ArrayList();
		}
		return accessionCodes;
	}

	/**
	 * @return AccessionCodes of the root entities loaded into the database
	 */
	public List getRootAccessionCodesLoaded()
	{
		List topLevelAccessionCodes = new ArrayList();
		// Need to filter out non top level elements
		Iterator iter = accessionCodesAdded.iterator();
		while (iter.hasNext())
		{
			String ac = (String) iter.next();
			HashMap parsedAC = Utility.parseAccessionCode(ac);
			String entityCode = (String) parsedAC.get("ENTITYCODE");
			if (Utility.isRootEntity(entityCode))
			{
				topLevelAccessionCodes.add(ac);
			}

		}
		log.debug("Returning root accessionCodes " + topLevelAccessionCodes);
		return topLevelAccessionCodes;
	}
	
	/**
	 * Search db for extant  entry
	 * 
	 * @param Map of the entry
	 * @return long of found PK or 0 otherwise
	 */
	private long getExtantPK(VBModelBean bean)
	{

		String entityName = getEntityName(bean);
		long pKey = 0;
		pKey = this.getPKFromAccessionCode(getAccessionCode(bean), entityName);
		
		return pKey;
	}
	
	
	/**
	 * @param bean The VBModelBean to query
	 * @return The AccessionCode for this bean or null if none found
	 */
	public String getAccessionCode( VBModelBean bean )
	{
		String accessionCode = null;
		
		// Need to find the accessionCode in the bean
		LinkedHashMap nameValues = bean.toOrderedHashMap();
		//log.debug("getAC: Bean values: " + nameValues);
		Set keys = nameValues.keySet();
		Iterator it = keys.iterator();

		while (it.hasNext())
		{
			// looking for an accession code
			// that's the only field we use to check the db for a duplicate
			String key = (String) it.next();
			if (key.equalsIgnoreCase(Constants.ACCESSIONCODENAME))
			{
				accessionCode = (String) nameValues.get(key);
			}
		}
		return accessionCode;
	}
	/**
	 * Searches a bean for its uniqueId
	 * 
	 * @param bean
	 * @param entityName
	 * @return
	 */
	public long getInputUniqueId( VBModelBean bean )
	{
		long inputUniqueId = 0;
		String entityName = getEntityName(bean);
		
		// Need to find the accessionCode in the bean
		LinkedHashMap nameValues = bean.toOrderedHashMap();
		//log.debug("getInputUniqueId: Bean values: " + nameValues);
		Set keys = nameValues.keySet();
		Iterator it = keys.iterator();

		// Loop all the values in this bean to find Unique Identifier
		while (it.hasNext())
		{
			String key = (String) it.next();
			// For now the Unique Identifier is defined as the PK field
			if (key.equalsIgnoreCase( Utility.getPKNameFromTableName(entityName)))
			{
				//log.debug(nameValues.get(key));
				inputUniqueId = ((Long) nameValues.get(key)).longValue();
			}
		}
		return inputUniqueId;
	}
	
	/**
	 * Find PK or this accessionCode in the database.
	 * 
	 * @param entityName
	 * @param accessionCode
	 * @return The PrimaryKey for this accessionCode in the database or 0 if none found
	 */
	private long getPKFromAccessionCode(String accessionCode, String entityName)
	{		
		long PK = 0;

		if ( Utility.isStringNullOrEmpty( accessionCode ))
		{
			log.debug("Found no accessionCode for " + entityName);
			// do nothing
		}
		else 
		{
			// Need to get the pK of the table
			PK = this.getTablePK(entityName, accessionCode);
			
			if ( PK != 0 )
			{
				// great got a real PK
				log.info("Found PK ("+ PK+ ") for "+ entityName+ " accessionCode: "+ accessionCode);
			}
			else
			{
				// Problem no accessionCode like that in database -- fail load
				String errorMessage =
					"There is no "
						+ entityName
						+ " with a accessionCode of value '"
						+ accessionCode
						+ "' in the database.";
						
				log.error(errorMessage);
				//commit = false;
				//errors.AddError(
				//	LoadingErrors.DATABASELOADINGERROR,
				//	errorMessage);
			}
		}
		
		//log.debug("Using accessionCode " + accessionCode + " in table "
		//			+ entityName + " got DB PK of " + PK);
		return PK;
	}

	/**
	 * Get the PK of the row in the database that has the same values as record
	 * 
	 * @param tableName
	 * @param AccessionCode
	 * @return long -- PK of the table
	 */
	private long getTablePK( String tableName, String accessionCode )
	{
		StringBuffer sb = new StringBuffer();
		long PK = 0;
		try 
		{
			sb.append(
				"SELECT " + Utility.getPKNameFromTableName(tableName) +" from "+tableName+" where " 
				+ Constants.ACCESSIONCODENAME + " = '" + accessionCode + "'"
			);

			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(sb.toString());
			while (rs.next()) 
			{
				PK = rs.getInt(1);
			}
			query.close();
			rs.close();
		}
		catch ( SQLException se ) 
		{ 
			//this.filterSQLException(se, sb.toString());
			log.error(se);
			se.printStackTrace();
		}
		LogUtility.log("Find PK from AC Query: '" + sb.toString() + "' got PK = " + PK);
		return PK;
	}
	
	
	/**
	 * Class to store get/set method pairs for Child VBModelBeans.
	 */
	public class MemberGetterSetters
	{

		private String memberName;

		private Method setterMethod;

		private Method getterMethod;

		public MemberGetterSetters(
				String memberName)
		{
			this.memberName = memberName;
		}

		/**
		 * @return Returns the getterMethod.
		 */
		public Method getGetterMethod()
		{
			return getterMethod;
		}

		/**
		 * @param getterMethod
		 *          The getterMethod to set.
		 */
		public void setGetterMethod(Method getterMethod)
		{
			this.getterMethod = getterMethod;
		}

		/**
		 * @return Returns the memberName.
		 */
		public String getMemberName()
		{
			return memberName;
		}

		/**
		 * @param memberName
		 *          The memberName to set.
		 */
		public void setMemberName(String memberName)
		{
			this.memberName = memberName;
		}

		/**
		 * @return Returns the setterMethod.
		 */
		public Method getSetterMethod()
		{
			return setterMethod;
		}

		/**
		 * @param setterMethod
		 *          The setterMethod to set.
		 */
		public void setSetterMethod(Method setterMethod)
		{
			this.setterMethod = setterMethod;
		}
	}

    /**
     *
     */
    public DBConnection getDBConnection() {
        try {
            if (this.conn.isClosed()) {
                initDB();
            }
        } catch (SQLException sex) {
            log.error("error while getting DB connection", sex);
        }
        return this.conn;
    }

    private Object getLastListElement(List l) {
        if (l == null || l.size() == 0) {
            return null;
        }
        return l.get(l.size()-1);
    }

    /**
     *
     */
	public void returnConnection() {
        DBConnectionPool.returnDBConnection(conn);
    }

    public void addBean(VBModelBean bean) {
        beans.add(bean);
    }
}
