/*
 *	'$RCSfile: GenericCommand.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-08-11 18:45:06 $'
 *	'$Revision: 1.9 $'
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
 
package org.vegbank.common.command;

import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;

/**
 * Uses parameters passed in via the request to construct the SQL query,
 * process the ResultSet and populate a List of Beans ( name passed in a parameter).
 * This List is saved into the request as an attribute called genericBean, where it is 
 * availible for use by a jsp.
 * 
 * @author farrell
 */

public class GenericCommand 
{

/*

select somefield from...

select somefield as newname from...

select (subquery stuff goes here as stuff) as somename from...

*/

	private static Log log = LogFactory.getLog(GenericCommand.class);


	//// THIS WORKS 
	// Caveat: Can't put (parens) in subqueries, or within other (parens)
	private static Pattern subqueryPattern = Pattern.compile("\\(.*?\\) as ");

	//// OTHER ATTEMPTS
	//private static Pattern selectParamPattern = Pattern.compile("\\(.*\\) as ?(.*))+ from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select[ distinct| all]? [[\\(.*\\) as ]?(.*)[,]?]+ from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select \\(.*\\) as (.*) from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select[\s]+[distinct|all]?[\s]+[\\(.*\\) as] (.*)|(.*)]+ from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select [distinct |all ]? ([[.*|\\(.*\\)][,]?]+) from .*");


	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public static List execute(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		// Run the query and stick the generated object into the request
		String selectClauseKey = request.getParameter("SQL");
		String whereClauseKey = request.getParameter("WHERE");
		String beanName = request.getParameter("BeanName");
		String[] whereParams = request.getParameterValues("wparam");

		return execute(request, selectClauseKey, whereClauseKey, beanName, whereParams);
	}

	/**
	 * Another way to execute a command.  It's easier to call this
	 * from a JSP page directly rather than going through the 
	 * GenericDispatcherAction.
	 */
	public static List execute(HttpServletRequest request, String selectClauseKey, 
				String whereClauseKey, String beanName, String whereParam)
				throws Exception {

		log.debug("execute(req, select, where, bean, (String)where): " + whereParam);
		String[] arr;
		arr = new String[1];
		arr[0] = whereParam;

		///////////////////////////////////////////////////////////////////
		/*
		// parse CSV
		if (whereParam.indexOf(",") != -1) {
			StringTokenizer st = new StringTokenizer(whereParam, ", ");
			int i=0;
			arr = new String[st.countTokens()];
			while (st.hasMoreTokens()) {
				arr[i] = st.nextToken();
				log.debug("Adding param " + arr[i]);
				i++;
			}
		} else {
			arr = new String[1];
			arr[0] = whereParam;
		}
		*/
		///////////////////////////////////////////////////////////////////
		

		return execute(request, selectClauseKey, whereClauseKey, beanName, arr);
	}

	/**
	 * Another way to execute a command.  It's easier to call this
	 * from a JSP page directly rather than going through the 
	 * GenericDispatcherAction.
	 */
	/*
	public static List execute(HttpServletRequest request, String selectClauseKey, 
				String whereClauseKey, String beanName, Object whereParam)
				throws Exception {

		log.debug("execute(req, select, where, bean, (Object)where)");
		log.debug("where param: " + whereParam.toString());
		log.debug("where param's class: " + whereParam.getClass().toString());
		String str = whereParam.toString();
		String[] arr = new String[1];
		arr[0] = str;

		return execute(request, selectClauseKey, whereClauseKey, beanName, arr);
	}
	*/


	/*
	public static List execute(String selectClauseKey, String whereClauseKey, 
				String beanName, Object whereParam) throws Exception {

		log.debug("execute(select, where, bean, (Object)where)");
		HttpServletRequest req = null;
		return execute(req, selectClauseKey, whereClauseKey, beanName, whereParam);
	}
	*/


	/*
	public static List execute(String selectClauseKey, String whereClauseKey, 
				String beanName, String[] whereParams ) throws Exception
	{
		log.debug("execute(select, where, bean, (String[])where)");
		return execute(null, selectClauseKey, whereClauseKey, beanName, whereParams);
	}
	*/

	/**
	 * Another way to execute a command.  It's easier to call this
	 * from a JSP page directly rather than going through the 
	 * GenericDispatcherAction.
	 */
	public static List execute(HttpServletRequest request, String selectClauseKey, 
				String whereClauseKey, String beanName, String[] whereParams)
				throws Exception {

		log.debug("execute(req, select, where, bean, (String[])where)");

		String SQLStatement = getSQLStatement(selectClauseKey, whereClauseKey,  whereParams);
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect( SQLStatement );
		
		Vector propNames = getPropertyNames(SQLStatement);
		List list = getBeanList(beanName, rs, propNames);

		if (request != null) {
			log.debug("Setting genericBean");
			request.setAttribute("genericBean", list);
			log.debug("Setting " + beanName);
			request.setAttribute(beanName, list);

			if (list.size() == 1) {
				log.debug("Setting genericVariable");
				request.setAttribute("genericVariable", list.get(0));
				log.debug("Setting " + beanName);
				request.setAttribute(beanName, list.get(0));
			}
		} 

		return list;
	}



	/**
	 * Uses parameters passed in via the request to construct a SQL Statement from a 
	 * properties file
	 * 
	 * @param request
	 * @return
	 */
	private static String getSQLStatement(String selectClauseKey, String whereClauseKey, String[] whereParams)
{
		ResourceBundle SQLResources =  ResourceBundle.getBundle("org.vegbank.common.SQLStore");
		String selectClause = "";
		String whereClause = "";
		
		// Don't bother with empty strings
		if (! selectClauseKey.equals("")) 
		{
			selectClause = SQLResources.getString(selectClauseKey);
		}		
		if (whereClauseKey != null && ! whereClauseKey.equals(""))
		{
			whereClause = SQLResources.getString(whereClauseKey);
		}
		
		//log.debug(">>>>>>>>>>" + selectClause + whereClause);
		
		// Message format allows the substitution of '{x}' with Strings from 
		// a String[] where x is the array index
		MessageFormat format = new MessageFormat(whereClause);

		StringBuffer SQLStatement = new StringBuffer();
		SQLStatement.append(selectClause);
		
		// Sometimes the SELECT statement must have a WHERE, e.g for privacy 
		// reasons for eample i.e. some records are not public
		// if so the where clause is appended using an AND
		if ( whereClause != null  && ! whereClause.equals("") )
		{
			if ( selectClause.indexOf("WHERE") > 0 )
			{
				SQLStatement.append(" AND " +  format.format(whereParams));
			}
			else 
			{
				SQLStatement.append(" WHERE " +  format.format(whereParams));
			}
		}
		
		//log.debug(">>>>>>>>>>" + SQLStatement);
		return SQLStatement.toString();
	}
	
	/**
	 * Uses a ResultSet to populate a List of generated Beans.
	 * Needs a list of the bean properties to populate in the same order
	 * as the columns in the ResultSet.
	 * 
	 * @param className
	 * @param rs
	 * @param propNames
	 * @return
	 * @throws Exception
	 */
	private static List getBeanList( String className, ResultSet rs, Vector propNames) 
		throws Exception
	{
		List col = new Vector();
		while ( rs.next() )
		{
			if (className.equalsIgnoreCase("map")) {
				Map map = new HashMap();
				for ( int i=0; i<propNames.size(); i++)
				{
					String propName = (String) propNames.get(i);
					String value = rs.getString(i+1);
					log.debug("Mapping property " + propName + " = " + value);
					map.put(propName, value);	
				}
		  		col.add(map);

			} else {
				Object bean = Utility.createObject( VBObjectUtils.DATA_MODEL_PACKAGE +  className);
				for ( int i=0; i<propNames.size(); i++)
				{
					String propName = (String) propNames.get(i);
					String value = rs.getString(i+1);
					log.debug("Setting bean property " + propName + " = " + value);
					BeanUtils.copyProperty(bean, propName, value);	
				}
		  		col.add(bean);
			}
		}
		return col;
	}
	
	/**
	 * Use the SQL select query to get the names of the properties
	 * of the bean we wish to populate with the values.  Removes
	 * anything before the '.' if there is one, e.g. 'tobs.accessioncode'
	 * becomes 'accessioncode'.
	 * 
	 * <pre>
	 * 	SELECT thisField, thatField, thatTable.inthat FROM xxxxx
	 * 		=> thisField, thatField, thatTable.inthat
	 * </pre>
	 * 
	 * @param selectClause
	 * @return
	 */
	private static Vector getPropertyNames(String selectClause)
	{
		Vector results = new Vector();
		
		// parse selectClause to get all the fieldNames
		String attribList = null;
		selectClause = selectClause.toLowerCase();

		log.debug("SOURCE: " + selectClause);
		//log.debug("PATTERN: " + selectParamPattern.pattern());
		log.debug("PATTERN: " + subqueryPattern.pattern());
		Matcher m = subqueryPattern.matcher(selectClause);

		selectClause = m.replaceAll("");
		log.debug("New string is: " + selectClause);

		if (m.matches()) {
			log.debug("MATCHED!");
			//attribList = m.group(1);
		} else {
			log.debug("NO MATCH");
		}

		/*
		log.debug("attrib list: " + attribList);
		log.debug("---------------------------------");
		*/


		StringTokenizer st = new StringTokenizer(selectClause);
		int indexOfDot, indexOfComma, indexOfAs;
		
		while ( st.hasMoreTokens() )
		{
			String propertyName = st.nextToken();
			if ( propertyName.equals("select") )
			{
				// Do Nothing
			}
			else if ( propertyName.equals("from"))
			{
				return results; // Nothing more of interest here
			}
			else
			{
				// Add this token with any trailing ',' removed
				
				// Check for a named field
				log.debug("property name:  " + propertyName);
				indexOfAs = propertyName.indexOf(" as ");
				log.debug("as?  " + indexOfAs);
				if ( indexOfAs != -1 ) {
					propertyName = propertyName.substring(indexOfAs + 4 );
				}

				indexOfComma = propertyName.indexOf(',');
				if ( indexOfComma == -1 )
				{
					// no comma so let in whole token
					indexOfDot = propertyName.indexOf('.');
					if ( indexOfDot == -1 ) {
						results.add(propertyName);
					} else {
						// remove all before the dot
						results.add(propertyName.substring(indexOfDot+1));
					}
				}
				else
				{
					// remove the comma
					results.add(propertyName.substring(0, indexOfComma));
				}
			}
		}	
		return results;
	}
	
	/**
	 * Just for testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// For testing
		GenericCommand gc = new GenericCommand();
		Vector result = 
			gc.getPropertyNames("SELECT thisField, thatField, thatTable.inthat FROM xxxxx"); 
			
		log.debug(result);
	}
}
