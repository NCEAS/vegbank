/*
 *	'$RCSfile: QueryResult.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-30 23:06:27 $'
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
 
package org.vegbank.servlet.request;

	 /**
	  *	Object of convience to store the results of a query
	  * 
	  */
	 public class QueryResult
	 {
			int resultsTotal;
			String xmlString;
	 	
			/**
			 * @return
			 */
			public int getResultsTotal()
			{
				return resultsTotal;
			}

			/**
			 * @return
			 */
			public String getXMLString()
			{
				return xmlString;
			}

			/**
			 * @param i
			 */
			public void setResultsTotal(int i)
			{
				resultsTotal = i;
			}

			/**
			 * @param string
			 */
			public void setXMLString(String string)
			{
				xmlString = string;
			}

	 }
