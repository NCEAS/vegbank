/*
 *	'$RCSfile: GenericCommand.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-21 17:52:12 $'
 *	'$Revision: 1.4 $'
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
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;

/**
 * Uses parameters passed in via the request to construct the SQL query,
 * process the ResultSet and populate a Collection of Beans ( name passed in a parameter).
 * This collection is saved into the request as an attribute called genericBean, where it is 
 * availible for use by a jsp.
 * 
 * @author farrell
 */

public class GenericCommand implements VegbankCommand
{

	/* (non-Javadoc)
	 * @see org.vegbank.common.command.VegbankCommand#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		// Run the query and stick the generated object into the request
		String selectClauseKey = request.getParameter("SQL");
		String whereClauseKey = request.getParameter("WHERE");
		String[] whereParams = request.getParameterValues("wparam");
		String SQLStatement = getSQLStatement(selectClauseKey, whereClauseKey, whereParams);
		
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect( SQLStatement );
		
//		Collection collection = new Vector();
//		while ( rs.next())
//		{
//			GenericVegbankObject gvo = new GenericVegbankObject();
//			try
//			{
//				gvo.setFieldOne(rs.getString(1));
//				gvo.setFieldTwo(rs.getString(2));
//				gvo.setFieldThree(rs.getString(3));
//				gvo.setFieldFour(rs.getString(4));
//				gvo.setFieldFive(rs.getString(5));
//				gvo.setFieldSix(rs.getString(6));
//				gvo.setFieldSeven(rs.getString(7));
//				gvo.setFieldEight(rs.getString(8));
//				gvo.setFieldNine(rs.getString(9));
//				gvo.setFieldTen(rs.getString(10));
//			}
//			catch (SQLException e)
//			{
//				if ( e.getMessage().equals("The column index is out of range.") )
//				{
//					// This is ok
//				}
//				else
//				{
//					throw e;
//				}
//			}
//
//			collection.add(gvo);
//		}

		Vector propNames = this.getPropertyNames(SQLStatement);
		String beanName = request.getParameter("BeanName");
		Collection collection = this.getBeanCollection(beanName, rs, propNames);
		request.setAttribute("genericBean",  collection);
	}
	/**
	 * Uses parameters passed in via the request to construct a SQL Statement from a 
	 * properties file
	 * 
	 * @param request
	 * @return
	 */
	private String getSQLStatement(String selectClauseKey, String whereClauseKey, String[] whereParams)
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
		
		//System.out.println(">>>>>>>>>>" + selectClause + whereClause);
		
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
		
		//System.out.println(">>>>>>>>>>" + SQLStatement);
		return SQLStatement.toString();
	}
	
	/**
	 * Uses a ResultSet to populate a Collection of generated Beans.
	 * Needs a list of the bean properties to populate in the same order
	 * as the columns in the ResultSet.
	 * 
	 * @param className
	 * @param rs
	 * @param propNames
	 * @return
	 * @throws Exception
	 */
	private Collection getBeanCollection( String className, ResultSet rs, Vector propNames) 
		throws Exception
	{
		Collection col = new Vector();
		while ( rs.next() )
		{
			Object bean = Utility.createObject( VBObjectUtils.DATA_MODEL_PACKAGE +  className);
			for ( int i=0; i<propNames.size(); i++)
			{
				String propName = (String) propNames.get(i);
				String value = rs.getString(i+1);
				//System.out.println("####" + bean + " " + propName + " " + value);
				BeanUtils.copyProperty(bean, propName, value);	
			}
		  col.add(bean);
		}
		//System.out.println("Got " + col.size() + " " + className);
		return col;
	}
	
	/**
	 * Use the SQL select query to get the names of the properties
	 * of the bean we wish to populate with the values
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
		StringTokenizer st = new StringTokenizer(selectClause);
		
		while ( st.hasMoreTokens() )
		{
			String propertyName = st.nextToken();
			if ( propertyName.equalsIgnoreCase("SELECT") )
			{
				// Do Nothing
			}
			else if ( propertyName.equalsIgnoreCase("FROM"))
			{
				return results; // Nothing more of interest here
			}
			else
			{
				// Add this token with any trailing ',' removed
				int indexOfComma = propertyName.indexOf(',');
				if ( indexOfComma == -1 )
				{
					// no comma so let in whole token
					results.add(propertyName.toLowerCase());
				}
				else
				{
					// remove the comma
					results.add(propertyName.substring(0, indexOfComma ).toLowerCase());
				}
			}
		}	
		return results;
	}
	
	public Collection execute ( String selectClauseKey, String whereClauseKey, String beanName,  String[] whereParams ) 
		throws Exception
	{
		String SQLStatement = getSQLStatement(selectClauseKey, whereClauseKey,  whereParams);
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da.issueSelect( SQLStatement );
		
		Vector propNames = this.getPropertyNames(SQLStatement);
		Collection collection = this.getBeanCollection(beanName, rs, propNames);
		return collection;
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
			
		System.out.println(result);
	}
}
