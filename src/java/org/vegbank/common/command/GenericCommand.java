/*
 *	'$RCSfile: GenericCommand.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-10-14 09:43:37 $'
 *	'$Revision: 1.23 $'
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


	//// PMA ORIG (reluctant)
	private static Pattern subqueryPattern = Pattern.compile("\\S*\\(.*?\\) +as +");

	//// PMA (possesive)
	//private static Pattern subqueryPattern = Pattern.compile("\\S*\\(.*?\\) as ");

	//// MLEE
	//private static Pattern subqueryPattern = Pattern.compile("\\S*\\([^)]*\\) as ");

	private static Pattern attribAsPattern = Pattern.compile("\\S* +as ");
	private static Pattern afterFromPattern = Pattern.compile(".* from ");

	//
	////////////////////////////////////////////////////////////////////
	private String selectClauseWithSimpleAttribs = null;
	private String whereClauseFormatted = null;

	////////////////////////////////////////////////////////////////////
	//


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
		String sqlFullQuery = initQuery(selectClauseKey, whereClauseKey, whereParams);



		////////////////////////////////////////////////////
		// Count results
		////////////////////////////////////////////////////
		int numItems = 0;
		
		// TODO: 
		// Only count results if they haven't already been counted
		if (getPager()) {
			numItems = countResults();

			/* 
			tmp = getNumItems();
			if (Utility.isStringNullOrEmpty(tmp)) {
				// run a query
				numItems = countResults(sqlFullQuery);

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
		sqlFullQuery += buildLimitClause();

		log.info("--------- " + selectClauseKey + " QUERY::: \n\t" + sqlFullQuery 
				+ "\n=======================================");
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect( sqlFullQuery );
		
		Vector propNames = getPropertyNames(sqlFullQuery);
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
	private int countResults() throws Exception {

		String sql = "SELECT COUNT(1) " + 
				this.selectClauseWithSimpleAttribs.substring( 
						this.selectClauseWithSimpleAttribs.indexOf("from ")) +
				this.whereClauseFormatted;
				
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
	private String initQuery(String selectClauseKey, String whereClauseKey, String[] whereParams) {
		String selectClause = "";
		String whereClause = "";
		StringBuffer sql = new StringBuffer(1024);
		
		// SELECT
		if (!Utility.isStringNullOrEmpty(selectClauseKey)) {
			selectClause = sqlResources.getString(selectClauseKey).toLowerCase();
			sql.append(selectClause);
		}		

		this.selectClauseWithSimpleAttribs = stripSQLAttribsFromSelect(selectClause);


		// WHERE
		if (!Utility.isStringNullOrEmpty(whereClauseKey)) {
			if (whereClauseKey.startsWith("where_")) {
				// get the SQL from props file
				whereClause = sqlResources.getString(whereClauseKey).toLowerCase();

			} else {
				// just use the given where clause
				log.info("Using non-configured (dangerous) where: " + whereClause);
			}
			sql.append(buildFormattedWhereClause(whereClause, whereParams, selectClause));

		} 

		return sql.toString();

	}


	/**
	 *
	 */
	private String buildFormattedWhereClause(String whereClause, String[] whereParams, String selectClause) {

		/*
		whereParams = new String[3];
		whereParams[0] = "party";
		whereParams[1] = "detailed";
		whereParams[2] = "gill";
		*/

		this.whereClauseFormatted = "";
		boolean hasWhereClause = !Utility.isStringNullOrEmpty(whereClause);
		boolean hasParams = (whereParams != null && whereParams.length > 0);
		MessageFormat format;

		log.debug("hasWhereClause: " + hasWhereClause);
		log.debug("hasParams: " + hasParams);


		/*
		if (!Utility.isStringNullOrEmpty(this.whereSubquery)) {
			// swap whereSubquery into {0} of whereClause
			String sqlWhereSubquery = sqlResources.getString(this.whereSubquery);
			log.debug("hasWhereSubquery: " + sqlWhereSubquery);
			format = new MessageFormat(whereClause);
			String[] subquerySwap = { sqlWhereSubquery };

			log.debug("old whereClause: " + whereClause);
			whereClause = format.format(subquerySwap);
			log.debug("new whereClause: " + whereClause);


			log.debug("Expanding wparams");
			StringTokenizer st = new StringTokenizer(whereParams[0], ";");
			whereParams = new String[st.countTokens()];
			int j=0;
			while (st.hasMoreTokens()) {
				whereParams[j++] = st.nextToken();
			}
			this.whereSubquery = null;
		}
		*/

		// format the where clause
		if (hasWhereClause && hasParams) {

			if (hasParams) {
				for (int i=0; i<whereParams.length; i++) {
					log.debug("whereParams: " + whereParams[i]);
				}

				// MessageFormat allows the substitution of '{x}' with Strings from 
				// a String[] where x is the array index
				format = new MessageFormat(whereClause);
				whereClause = format.format(whereParams);
			}


			if ( this.selectClauseWithSimpleAttribs.indexOf("where") == -1 ) {
				whereClause = " where " +  whereClause;
			} else {
				whereClause = " and " +  whereClause;
			}

			log.debug("Adding where clause: " + whereClause);
			this.whereClauseFormatted = whereClause;
		}

		return this.whereClauseFormatted;
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
		if (Utility.isStringNullOrEmpty(this.selectClauseWithSimpleAttribs)) {
			this.selectClauseWithSimpleAttribs = stripSQLAttribsFromSelect(selectClause);
		}

		// extract only the comma-separated attribs
		String attribList = this.selectClauseWithSimpleAttribs.substring(
				this.selectClauseWithSimpleAttribs.indexOf("select ")+7, 
				this.selectClauseWithSimpleAttribs.indexOf("from"));

		StringTokenizer st = new StringTokenizer(attribList, ",");
		int indexOfDot, indexOfComma, indexOfAs;
		
		// get the property names

		while ( st.hasMoreTokens() )
		{
			String propertyName = st.nextToken().trim();
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
				log.debug("property name:  '" + propertyName + "'");
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
	private String stripSQLAttribsFromSelect(String selectClause) {

		selectClause = selectClause.toLowerCase();
		Matcher m;

		log.debug("-=-=-=-=-=-=-=-=-=-=-=-");
		log.debug("========== ORIGINAL QUERY: " + selectClause);
		
		m = subqueryPattern.matcher(selectClause);
		selectClause = m.replaceAll("");
		log.debug("========== REGEX 1: " + subqueryPattern.pattern());
		log.debug("========== MODIFIED QUERY 1: " + selectClause);

		m = attribAsPattern.matcher(selectClause);
		selectClause = m.replaceAll("");
		log.debug("========== REGEX 2: " + attribAsPattern.pattern());
		log.debug("========== MODIFIED 2: " + selectClause);

		//m = afterFromPattern.matcher(selectClause);
		//selectClause = m.replaceAll("");
		//log.debug("========== REGEX 3: " + afterFromPattern.pattern());
		//log.debug("========== MODIFIED 3: " + selectClause);

		log.debug("-=-=-=-=-=-=-=-=-=-=-=-");

		return selectClause;
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
		log.debug("-=-=-=-=-=-=-=-=-=-=-=-");
		log.debug("REGEX: " + afterFromPattern.pattern());
		log.debug("BEFORE: " + selectClause);
		selectClause = m.replaceAll("");
		log.debug("MODIFIED: " + selectClause);

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
     * 
     */
	/*
	protected String whereSubquery;

    public String getWhereSubquery() {
        return this.whereSubquery;
    }

    public void setWhereSubquery(String s) {
        this.whereSubquery = s;
    }

	*/


	
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
