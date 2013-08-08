/*
 *	'$RCSfile: GenericCommand.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005/07/27 22:24:35 $'
 *	'$Revision: 1.38 $'
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

import java.io.*;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.common.utility.VBObjectUtils;
import org.vegbank.common.utility.QueryParameters;

/**
 * Uses parameters passed in via the request to construct the SQL query,
 * process the ResultSet and populate a List of Beans ( name passed in a parameter).
 * This List is saved into the request as an attribute called BEANLIST, where it is
 * availible for use by a JSP.  If the query returns only one result, an additional
 * attribute called BEAN is stored in the request, containing the only BEANLIST item.
 *
 * @author anderson, farrell
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
	private static final String LAST_RESULTS_KEY = "LAST_RESULTS";
	private QueryParameters queryParameters = null;

	//// THIS WORKS
	// Caveat: Can't put (parens) in subqueries, or within other (parens)
	// \S matches non-whitespace characters


	//// PMA ORIG (reluctant)
	private static Pattern subqueryPattern = Pattern.compile("\\S*\\(.*?\\) +as +");

	//// PMA (possesive)
	//private static Pattern subqueryPattern = Pattern.compile("\\S*\\(.*?\\) as ");

	//// MLEE
	private static Pattern groupbyPattern = Pattern.compile("(.* )(group by )");

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
		String orderBy = request.getParameter("orderBy");
		String[] whereParams = request.getParameterValues("wparam");

		return execute(request, selectClauseKey, whereClauseKey, beanName, orderBy, whereParams);
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
			String orderBy,
			String whereParam)
			throws Exception {

		//log.debug("execute(req, select, where, bean, (String)where): " + whereParam);
		String[] arr = null;
		if (!Utility.isStringNullOrEmpty(whereParam)) {
			arr = new String[1];
			arr[0] = whereParam;
		}

		return execute(request, selectClauseKey, whereClauseKey, beanName, orderBy, arr);
	}

	/**
	 *
	 */
	public List execute(String selectClauseKey, String whereClauseKey,
				String beanName, String orderBy, String whereParam) throws Exception {

		//log.debug("execute(select, where, bean, (Object)where)");
		return execute(null, selectClauseKey, whereClauseKey, beanName, orderBy, whereParam);
	}


	/**
	 *
	 */
	public List execute(String selectClauseKey, String whereClauseKey,
				String beanName, String orderBy, String[] whereParams ) throws Exception
	{
		//log.debug("execute(select, where, bean, (String[])where)");
		return execute(null, selectClauseKey, whereClauseKey, beanName, orderBy, whereParams);
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
			String orderBy,
			String[] whereParams)
			throws Exception {

		//log.debug("execute(req, select, where, bean, (String[])where)");

		String tmp;
		String sqlFullQuery = initQuery(selectClauseKey, whereClauseKey, orderBy, whereParams);
            HttpSession session = request.getSession();
            // log.info("SQL Full Query: " + sqlFullQuery);


            ////////////////////////////////////////////////////
            // Count results
            ////////////////////////////////////////////////////
            int numItems = 0;

            // TODO:
            // Only count results if they haven't already been counted
            if (getPager()) {
                numItems = countResults(sqlFullQuery);

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
                    //log.info("Count found " + numItems);
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



            ////////////////////////////////////////////////////////////////////////
            // Get the search results
            ////////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////
            // Load from disk
            ////////////////////////////////////////////////////
            List searchResultList = null;
            boolean skipQuery = false;
            if (queryParameters != null) {
                String sessionLoadName = queryParameters.getString("load");
                if (!Utility.isStringNullOrEmpty(sessionLoadName)) {
                    try {
                        // use the given load value as a session key to get filename
                        if (sessionLoadName.equals("true")) {
                            // get the last results key from session
                            sessionLoadName = (String)session.getAttribute(LAST_RESULTS_KEY);
                        }
                        log.debug("checking for last results key: " + sessionLoadName);
                        String loadFileName = (String)session.getAttribute(sessionLoadName);
                        log.debug("loading last results file: " + loadFileName);
                        searchResultList = loadSearchResults(loadFileName);

                        request.setAttribute(sessionLoadName, searchResultList);
                        skipQuery = true;

                    } catch (IOException ioe) {
                        log.error("could not load results from disk: " + ioe.toString());
                    }
                }
            }

            if (((Boolean)queryParameters.get("showQuery")).booleanValue()) {
                log.debug("showQuery ON:  setting getQuery to full SQL query");
                request.setAttribute("getQuery", sqlFullQuery);
                skipQuery = true;
            }


            if (!skipQuery) {
                ////////////////////////////////////////////////////
                // Run the query
                ////////////////////////////////////////////////////
                sqlFullQuery += buildLimitClause();

                log.info("--------- generic command for session:" + request.getSession().getId() + "> " + selectClauseKey + " QUERY::: \n\t" + request.getSession().getId() + ">" + sqlFullQuery
                       + "===================");
                DatabaseAccess da = new DatabaseAccess();
                ResultSet rs = da.issueSelect( sqlFullQuery );

                Vector propNames = getPropertyNames(sqlFullQuery);
                searchResultList = getBeanList(beanName, rs, propNames);
                da.closeStatement();
                rs.close();
		    } else {
                log.debug("SKIPPING QUERY");
            }

            if (request != null) {
                /*
                //
                // SAVE/LOAD RESULTS
                //
                if (queryParameters != null) {
                    // comes from <vegbank:get save="whatever" ... />
                    String sessionSaveName = queryParameters.getString("save");

                    if (Utility.isStringNullOrEmpty(sessionSaveName)) {
                        // if empty, check the "load" attrib
                        log.debug("session save value was empty...using load");
                        sessionSaveName = queryParameters.getString("load");
                    }
                    log.debug("save <vegbank:get> results?: " + sessionSaveName);

                    if (!Utility.isStringNullOrEmpty(sessionSaveName)) {
                        log.debug("Saving page of results to disk in session as " + sessionSaveName);
                        try {
                            String saveFileName = session.getId();
                            saveSearchResults(saveFileName, searchResultList);
                            log.debug("Saved results to file named: " + saveFileName);
                            session.setAttribute(LAST_RESULTS_KEY, sessionSaveName);
                            log.debug("setting " + sessionSaveName + " to " + saveFileName);
                            session.setAttribute(sessionSaveName, saveFileName);
                            Utility.setLastSearchURL(request);
                        } catch (IOException ioe) {
                            log.error("could not save results to disk", ioe);
                        }
                    }
                }
                */

                if (!skipQuery && log.isDebugEnabled()) {
                    log.debug("Setting " + BEANLIST_KEY + " and " + beanName.toUpperCase() + " as lists");
                }
                request.setAttribute(BEANLIST_KEY, searchResultList);
                request.setAttribute(beanName.toUpperCase(), searchResultList);

                String thisGetId = getId();
                if (!Utility.isStringNullOrEmpty(thisGetId)) {
                    request.setAttribute(thisGetId + "-" + BEANLIST_KEY, searchResultList);
                } else {
                    log.debug("============id is empty");
                }

                if (searchResultList != null && searchResultList.size() == 1) {
                    if (log.isDebugEnabled()) {
                        log.debug("One item, so setting " + BEAN_KEY + " and " + beanName.toUpperCase() + " as beans");
                    }
                    request.setAttribute(BEAN_KEY, searchResultList.get(0));
                    request.setAttribute(beanName.toUpperCase(), searchResultList.get(0));

                    if (!Utility.isStringNullOrEmpty(thisGetId)) {
                        if (log.isDebugEnabled()) {
                            log.debug("Also setting " + thisGetId + "-" + BEAN_KEY);
                        }
                        request.setAttribute(thisGetId + "-" + BEAN_KEY, searchResultList.get(0));
                    }
                }
            } // end if request not null


		return searchResultList;
	}


	/**
	 *
	 */
	private int countResults(String sqlFullQuery) throws Exception {

/*
	    int pos = this.whereClauseFormatted.toLowerCase().indexOf("order by ");
        String cleanWhere;
        if (pos != -1) {
            cleanWhere = this.whereClauseFormatted.substring(0, pos);
        } else {
            cleanWhere = this.whereClauseFormatted;
        }

		String sql = "SELECT COUNT(1) " +
				this.selectClauseWithSimpleAttribs.substring(
						this.selectClauseWithSimpleAttribs.indexOf("from ")) + cleanWhere;
*/
		String sql = "SELECT COUNT(1) FROM (" + sqlFullQuery + ") AS cnt";

		//log.info("::: COUNT QUERY:::\n\t" + sql + "\n=======================================");
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect( sql );

		String numItems = "";
		if (rs.next()) {
			numItems = rs.getString(1);
		}

        da.closeStatement();
        rs.close();

		return Integer.parseInt(numItems);
	}



	/**
	 * Uses parameters passed in via the request to construct a SQL Statement from a
	 * properties file
	 *
	 * @param request
	 * @return
	 */
	private String initQuery(String selectClauseKey, String whereClauseKey, String orderByKey, String[] whereParams) {
		String selectClause = "";
		String whereClause = "";
		String orderBy = "";
		String whereFormatted = "";
		StringBuffer sql = new StringBuffer(1024);

		// SELECT
		if (!Utility.isStringNullOrEmpty(selectClauseKey)) {
			selectClause = sqlResources.getString(selectClauseKey).toLowerCase();
			//sql.append(selectClause);  //MTL: don't do this yet as we may have to adjust where the WHERE statement goes if there is a group by in the statement
		}

        // MTL: this appears to remove " AS [fieldalias]" and subqueries, which is necessary for determining if we should add "WHERE" or "AND" to the query
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
			// sql.append(buildFormattedWhereClause(whereClause, whereParams, selectClause));
			whereFormatted = buildFormattedWhereClause(whereClause, whereParams, selectClause);
			if (this.selectClauseWithSimpleAttribs.indexOf(" group by ") == -1) {
				//just append as there is no group by:
				sql.append(selectClause);
				sql.append(whereFormatted);
			} else {
				//have group by in the simplified statement, append WHERE to the place before this in the larger statement
				Matcher m;

				m = groupbyPattern.matcher(selectClause);
				sql.append(m.replaceFirst("$1" + " " + whereFormatted + " " + "$2"));
				log.debug("inserted WHERE before GROUP BY: " + sql.toString());
			}

	    } else {  //no where, append select to sql
		  sql.append(selectClause);
		}

		// ORDER BY
		if (!Utility.isStringNullOrEmpty(orderByKey)) {
            log.debug("order by value: " + orderByKey);
            String sortDir = " ASC";

            // strip the sort direction if it's there
            if (orderByKey.endsWith("_desc")) {
                sortDir = " DESC";
                orderByKey = orderByKey.substring(0, orderByKey.length() - "_desc".length());
            } else if (orderByKey.endsWith("_asc")) {
                orderByKey = orderByKey.substring(0, orderByKey.length() - "_asc".length());
            }

            if (orderByKey.startsWith("xorderby_")) {
                // remove any chars other than A-Za-z0-9_
                String field = orderByKey.substring("xorderby_".length()).toLowerCase();
	            Pattern badCharsPattern = Pattern.compile("\\W");
                Matcher m = badCharsPattern.matcher(field);
		        orderBy = m.replaceAll("");

            } else {
                // look it up from SQLStore
                orderBy = sqlResources.getString(orderByKey).toLowerCase();
            }

            if (!Utility.isStringNullOrEmpty(orderBy)) {
                sql.append(" ORDER BY ").append(orderBy);

                if (orderBy.indexOf(" desc") == -1 &&
                        orderBy.indexOf(" asc") == -1) {
                    // no sorting specified yet, so add it
                    sql.append(" ").append(sortDir);
                }
            }
		}

		return sql.toString();

	}


	/**
	 *
	 */
	private String buildFormattedWhereClause(String whereClause, String[] whereParams, String selectClause) {

		this.whereClauseFormatted = "";
		boolean hasWhereClause = !Utility.isStringNullOrEmpty(whereClause);
		boolean hasParams = false;
		if (whereParams != null) {
			hasParams = true;
			if (whereParams.length == 0) {
				hasParams = false;
			} else if (whereParams.length == 1) {
				if (Utility.isStringNullOrEmpty(whereParams[0])) {
					hasParams = false;
				}
			}
		}

		MessageFormat format;

		//log.debug("hasWhereClause: " + hasWhereClause);
		//log.debug("hasParams: " + hasParams);


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
			//StringTokenizer st = new StringTokenizer(whereParams[0], Utility.PARAM_DELIM);
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

            // I know the logic looks funky the way it is --PMA
			if (hasParams) {
                //////////////////////////////////////////
                List wpList = new ArrayList();
                //log.debug("checking for delimited wparams: " + Utility.PARAM_DELIM);
                for (int i=0; i<whereParams.length; i++) {
                    //log.debug("whereParams: " + whereParams[i]);
                    //StringTokenizer st = new StringTokenizer(whereParams[i], Utility.PARAM_DELIM);
                    //while (st.hasMoreTokens()) {

                    String[] paramTokens = whereParams[i].split(Utility.PARAM_DELIM);
                    for (int j=0; j<paramTokens.length; j++) {
                        wpList.add(paramTokens[j]);
                    }
                }

                // get all of the params as an array
                whereParams = (String[])wpList.toArray( new String[1] );
                //////////////////////////////////////////

                /*
				for (int i=0; i<whereParams.length; i++) {
                    String tmpParam = DatabaseUtility.makeSQLSafe(whereParams[i], false);

					if (!Utility.isNumeric(tmpParam)) {
                        // THIS IS PROBABLY A BAD IDEA
                        // SINCE QUERIES AND STUFF CAN BE WPARAMS
					    tmpParam = "'" + tmpParam + "'";
                    }

					whereParams[i] = tmpParam;
					log.debug("whereParams: " + whereParams[i]);
				}
                */

				// MessageFormat allows the substitution of '{x}' with Strings from
				// a String[] where x is the array index
				format = new MessageFormat(whereClause);
				whereClause = format.format(whereParams);
			}


			if (this.selectClauseWithSimpleAttribs.indexOf("where") == -1) {
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
				//log.debug("property name:  '" + propertyName + "'");
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

		//log.debug("---------------------------------");
		//log.debug("attrib list: " + results.toString());
		//log.debug("---------------------------------");
		return results;
	}


    /**
     *
     */
	private String stripSQLAttribsFromSelect(String selectClause) {

		selectClause = selectClause.toLowerCase();
		Matcher m;

		//log.debug("-=-=-=-=-=-=-=-=-=-=-=-");
		//log.debug("========== ORIGINAL QUERY: " + selectClause);

		m = subqueryPattern.matcher(selectClause);
		selectClause = m.replaceAll("");
		//log.debug("========== REGEX 1: " + subqueryPattern.pattern());
		//log.debug("========== MODIFIED QUERY 1: " + selectClause);

		m = attribAsPattern.matcher(selectClause);
		selectClause = m.replaceAll("");
		//log.debug("========== REGEX 2: " + attribAsPattern.pattern());
		//log.debug("========== MODIFIED 2: " + selectClause);

		//m = afterFromPattern.matcher(selectClause);
		//selectClause = m.replaceAll("");
		//log.debug("========== REGEX 3: " + afterFromPattern.pattern());
		//log.debug("========== MODIFIED 3: " + selectClause);

		//log.debug("-=-=-=-=-=-=-=-=-=-=-=-");

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
	 * Easy way to set up a query.
     * First used with <vegbank:get>.
     * @see VegbankGetTag.
	 */
    public void setQueryParameters(QueryParameters qp) {
        queryParameters = qp;
    }

    /**
     *
     */
    private void saveSearchResults(String fileName, List l) throws IOException {
        Utility.saveBinaryFile(getCacheDir() + fileName, l);
    }

    /**
     *
     */
    private List loadSearchResults(String fileName) throws IOException {
        return (List)Utility.loadBinaryFile(getCacheDir() + fileName);
    }

    public static String getCacheDir() {
        String path = Utility.VB_HOME_DIR;
        if (!path.endsWith(File.separator)) { path += File.separator; }
        return path + "cache" + File.separator;
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
