/*
 *	'$RCSfile: GenericCommand.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-10-05 02:10:52 $'
 *	'$Revision: 1.17 $'
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
 * This List is saved into the request as an attribute called BEANLIST, where it is 
 * availible for use by a JSP.  If the query returns only one result, an additional
 * attribute called BEAN is stored in the request, containing the only BEANLIST item.
 * 
 * @author farrell
 */

public class GenericCommand 
{

	private static Log log = LogFactory.getLog(GenericCommand.class);
	private static ResourceBundle sqlResources = 
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");

	public static final int DEFAULT_PER_PAGE = 10;   // -1 is all results
	private static final String BEANLIST_KEY = "BEANLIST";
	private static final String BEAN_KEY = "BEAN";
	private static final String MAP_KEY = "MAP";

	//// THIS WORKS 
	// Caveat: Can't put (parens) in subqueries, or within other (parens)
	// \S matches non-whitespace characters
	private static Pattern subqueryPattern = Pattern.compile("\\S*\\(.*?\\) as ");
	private static Pattern attribAsPattern = Pattern.compile("\\S* as ");
	private static Pattern afterFromPattern = Pattern.compile(".* from ");

	//// OTHER ATTEMPTS
	//private static Pattern selectParamPattern = Pattern.compile("\\(.*\\) as ?(.*))+ from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select[ distinct| all]? [[\\(.*\\) as ]?(.*)[,]?]+ from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select \\(.*\\) as (.*) from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select[\s]+[distinct|all]?[\s]+[\\(.*\\) as] (.*)|(.*)]+ from.*");
	//private static Pattern selectParamPattern = Pattern.compile("select [distinct |all ]? ([[.*|\\(.*\\)][,]?]+) from .*");


	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public List execute(HttpServletRequest request, HttpServletResponse response)
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
	public List execute(
			HttpServletRequest request, 
			String selectClauseKey, 
			String whereClauseKey, 
			String beanName, 
			String whereParam) 
			throws Exception {

		//log.debug("execute(req, select, where, bean, (String)where): " + whereParam);
		String[] arr = null;
		if (!Utility.isStringNullOrEmpty(whereParam)) {
			arr = new String[1];
			arr[0] = whereParam;
		}

		return execute(request, selectClauseKey, whereClauseKey, beanName, arr);
	}

	/**
	 *
	 */
	public List execute(String selectClauseKey, String whereClauseKey, 
				String beanName, String whereParam) throws Exception {

		//log.debug("execute(select, where, bean, (Object)where)");
		return execute(null, selectClauseKey, whereClauseKey, beanName, whereParam);
	}


	/**
	 *
	 */
	public List execute(String selectClauseKey, String whereClauseKey, 
				String beanName, String[] whereParams ) throws Exception
	{
		//log.debug("execute(select, where, bean, (String[])where)");
		return execute(null, selectClauseKey, whereClauseKey, beanName, whereParams);
	}


	/**
	 * Another way to execute a command.  It's easier to call this
	 * from a JSP page directly rather than going through the 
	 * GenericDispatcherAction.
	 */
	public List execute(
			HttpServletRequest request,
			String selectClauseKey, 
			String whereClauseKey,
			String beanName,
			String[] whereParams)
			throws Exception {

		//log.debug("execute(req, select, where, bean, (String[])where)");

		String tmp;
		String sqlMainQuery = getMainQuery(selectClauseKey, whereClauseKey, whereParams);


		////////////////////////////////////////////////////
		// Count results
		////////////////////////////////////////////////////
		int numItems = 0;
		
		// FOR NOW WE'LL COUNT RESULTS EACH TIME 
		if (getPager()) {
			numItems = countResults(sqlMainQuery);

			/* 
			tmp = getNumItems();
			if (Utility.isStringNullOrEmpty(tmp)) {
				// run a query
				numItems = countResults(sqlMainQuery);

			} else {
				// use the preset numItems
				numItems = Integer.parseInt(tmp);
			}
			*/

			if (request != null) {
				log.info("Count found " + numItems);
				request.setAttribute("numItems", Integer.toString(numItems));
			} 
		}



		////////////////////////////////////////////////////
		// Set up the pager
		////////////////////////////////////////////////////
		tmp = getPerPage();
		float pp = 0;
		if (Utility.isStringNullOrEmpty(tmp) && numItems > 1) {
			// set default items per page
			setPerPage(null);

		} else if (numItems == 1) {
			//setPerPage("1");
			setPageNumber("0");
			if (Utility.isStringNullOrEmpty(tmp)) {
				pp = DEFAULT_PER_PAGE;
			} else {
				pp = Float.parseFloat(tmp);
			}
		} 

		// page number
		tmp = getPageNumber();
		int pn;
		if (Utility.isStringNullOrEmpty(tmp)) {
			pn = 1;
		} else {
			pn = Integer.parseInt(tmp);
		}

		int numTotalPages = (int)Math.ceil((float)numItems / pp);
		if (pn < 1) { 
			pn = 1; 
		} else if (pn * pp > numItems) { 
			pn = numTotalPages; 
		} 
		setPageNumber(Integer.toString(pn));



		////////////////////////////////////////////////////
		// Run the query
		////////////////////////////////////////////////////
		sqlMainQuery += buildLimitClause();

		log.info("--------- " + selectClauseKey + " QUERY::: \n\t" + sqlMainQuery 
				+ "\n=======================================");
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect( sqlMainQuery );
		
		Vector propNames = getPropertyNames(sqlMainQuery);
		List list = getBeanList(beanName, rs, propNames);

		if (request != null) {
			String id = getId();

			log.debug("Setting " + BEANLIST_KEY + " and " + beanName.toUpperCase() + " as lists");
			request.setAttribute(BEANLIST_KEY, list);
			request.setAttribute(beanName.toUpperCase(), list);

			if (!Utility.isStringNullOrEmpty(id)) {
				request.setAttribute(id + "-" + BEANLIST_KEY, list);
			}

			if (list.size() == 1) {
				log.debug("One item, so setting " + BEAN_KEY + " and " + beanName.toUpperCase() + " as beans");
				request.setAttribute(BEAN_KEY, list.get(0));
				request.setAttribute(beanName.toUpperCase(), list.get(0));

				if (!Utility.isStringNullOrEmpty(id)) {
					request.setAttribute(id + "-" + BEAN_KEY, list.get(0));
				}
			}
		} 

		return list;
	}


	/**
	 * 
	 */
	private int countResults(String origQuery) throws Exception {

		String sql = "SELECT count(1) FROM " + stripSQLAttribs(origQuery);

		log.info("::: COUNT QUERY:::\n\t" + sql + "\n=======================================");
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect( sql );
		
		String numItems = "";
		if (rs.next()) {
			numItems = rs.getString(1);
		}

		return Integer.parseInt(numItems);
	}



	/**
	 * Uses parameters passed in via the request to construct a SQL Statement from a 
	 * properties file
	 * 
	 * @param request
	 * @return
	 */
	private String getMainQuery(String selectClauseKey, String whereClauseKey, String[] whereParams) {
		String selectClause = "";
		String whereClause = "";
		StringBuffer sql = new StringBuffer(1024);
		
		// SELECT
		if (!Utility.isStringNullOrEmpty(selectClauseKey)) {
			selectClause = sqlResources.getString(selectClauseKey);
			sql.append(selectClause);
		}		

		// WHERE
		if (!Utility.isStringNullOrEmpty(whereClauseKey)) {
			if (whereClauseKey.startsWith("where_")) {
				// get the SQL from props file
				whereClause = sqlResources.getString(whereClauseKey);

			} else {
				// just use the given where clause
				log.info("Using non-configured (dangerous) where: " + whereClause);
			}
		} 


		boolean hasWhereClause = !Utility.isStringNullOrEmpty(whereClause);
		boolean hasParams = (whereParams != null && whereParams.length > 0);

		log.debug("hasWhereClause: " + hasWhereClause);
		log.debug("hasParams: " + hasParams);

		if (hasWhereClause && hasParams) {

			if (hasParams) {
				for (int i=0; i<whereParams.length; i++) {
					log.debug("whereParams: " + whereParams[i]);
				}

				// MessageFormat allows the substitution of '{x}' with Strings from 
				// a String[] where x is the array index
				MessageFormat format = new MessageFormat(whereClause);
				whereClause = format.format(whereParams);
			}


			log.debug("Adding where clause: " + whereClause);
			if ( selectClause.indexOf("WHERE") > 0 ) {
				sql.append(" AND " +  whereClause);

			} else {
				sql.append(" WHERE " +  whereClause);
			}
		}

		return sql.toString();
	}


	/**
	 *
	 */
	private String buildLimitClause() {

		StringBuffer sqlLimitClause = new StringBuffer(512);

		// set up pagination
		String pn = getPageNumber();
		String pp = getPerPage();


		int ipp;
		if (Utility.isStringNullOrEmpty(pp)) {
			// empty, so make it default
			ipp = DEFAULT_PER_PAGE;
		} else {
			ipp = Integer.parseInt(pp);
			if (ipp == 0) {
				ipp = DEFAULT_PER_PAGE;
			}
		}

		if (ipp != -1) {

			int ipn;
			if (Utility.isStringNullOrEmpty(pn)) {
				ipn = 0;
			} else {
				ipn = Integer.parseInt(pn) - 1;
			}

			if (ipn < 0) {
				ipn = 0;
			}

			sqlLimitClause.append(" LIMIT ")
				.append(ipp)
				.append(" OFFSET ")
				.append(ipn * ipp);
		}

		
		//log.debug(">>>>>>>>>>" + sqlLimitClause.toString());
		return sqlLimitClause.toString();
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
	private List getBeanList( String className, ResultSet rs, Vector propNames) 
		throws Exception
	{
		List beanList = new ArrayList();
		while ( rs.next() )
		{
			if (Utility.isStringNullOrEmpty(className) || className.equalsIgnoreCase(MAP_KEY)) {
				Map map = new HashMap();
				for ( int i=0; i<propNames.size(); i++)
				{
					String propName = (String) propNames.get(i);
					String value = rs.getString(i+1);
					log.debug("Mapping property " + propName + " = " + value);
					map.put(propName, value);	
				}
		  		beanList.add(map);

			} else {
				Object bean = Utility.createObject( VBObjectUtils.DATA_MODEL_PACKAGE +  className);
				for ( int i=0; i<propNames.size(); i++)
				{
					String propName = (String) propNames.get(i);
					String value = rs.getString(i+1);
					log.debug("Setting bean property " + propName + " = " + value);
					BeanUtils.copyProperty(bean, propName, value);	
				}
		  		beanList.add(bean);
			}
		}
		return beanList;
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
	private Vector getPropertyNames(String selectClause)
	{
		Vector results = new Vector();
		
		// parse selectClause to get all the fieldNames
		String attribList = null;
		selectClause = selectClause.toLowerCase();
		Matcher m;

		//log.debug("============================");
		//log.debug("ORIGINAL: " + selectClause);
		m = subqueryPattern.matcher(selectClause);
		selectClause = m.replaceAll("");
		//log.debug("REGEX: " + subqueryPattern.pattern());
		//log.debug("MODIFIED 1: " + selectClause);

		m = attribAsPattern.matcher(selectClause);
		selectClause = m.replaceAll("");
		//log.debug("REGEX: " + attribAsPattern.pattern());
		//log.debug("--------------");
		//log.debug("MODIFIED 2: " + selectClause);
		//log.debug("============================");

		//log.debug("========== QUERY: " + selectClause);

		StringTokenizer st = new StringTokenizer(selectClause);
		int indexOfDot, indexOfComma, indexOfAs;
		
		// get the property names

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
				//log.debug("property name:  " + propertyName);
				indexOfAs = propertyName.indexOf(" as ");
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

		log.debug("---------------------------------");
		log.debug("attrib list: " + results.toString());
		log.debug("---------------------------------");
		return results;
	}


    /**
     * 
     */
	private String stripSQLAttribs(String selectClause) {

		selectClause = selectClause.toLowerCase();
		Matcher m;

		m = subqueryPattern.matcher(selectClause);
		selectClause = m.replaceAll("");

		m = attribAsPattern.matcher(selectClause);
		selectClause = m.replaceAll("");

		m = afterFromPattern.matcher(selectClause);
		//log.debug("-=-=-=-=-=-=-=-=-=-=-=-");
		//log.debug("REGEX: " + afterFromPattern.pattern());
		//log.debug("BEFORE: " + selectClause);
		selectClause = m.replaceAll("");
		//log.debug("MODIFIED: " + selectClause);

		return selectClause;
	}


    /**
     * 
     */
	protected String pageNumber;

    public String getPageNumber() {
        return (this.pageNumber);
    }

    public void setPageNumber(String s) {
		try {
			if (Utility.isStringNullOrEmpty(s) || Integer.parseInt(s) < 0) {
				s = "0";
			}
		} catch (NumberFormatException nfe) {
			s = "0";
		}

        this.pageNumber = s;
    }

    /**
     * 
     */
	protected String perPage;

    public String getPerPage() {
        return (this.perPage);
    }

    public void setPerPage(String s) {
        this.perPage = s;
    }


    /**
     * 
     */
	protected String id;

    public String getId() {
        return this.id;
    }

    public void setId(String s) {
		log.debug("GC: setId: " + s);
        this.id = s;
    }


    /**
     * 
     */
	protected String numItems;

    public String getNumItems() {
        return this.numItems;
    }

    public void setNumItems(String s) {
		log.debug("GC: setNumItems: " + s);
        this.numItems = s;
    }


    /**
     * 
     */
	protected boolean pager;

    public boolean getPager() {
        return this.pager;
    }

    public void setPager(boolean b) {
        this.pager = b;
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
