package org.vegbank.common.model;

import java.util.LinkedHashMap;

/*
 * '$RCSfile: VBModelBean.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-07 17:55:28 $'
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

import org.apache.commons.betwixt.XMLUtils;
 
/**
 * @author farrell
 *
 * An abstract class for the VBModelBeans, contains some utility methods 
 * and some abstract methods that need to be implemented by the beans.
 */
public abstract class VBModelBean
{	 
 
 	/**
 	 * UnMarshall this bean to an XML String.
 	 * 
 	 * @return String -- XML of this Bean
 	 */
 	public abstract String toXML();
 	
	/**
	 * UnMarshall this bean to an <code>LinkedHashMap</code>.
	 * 
	 * @return LinkedHashMap -- LinkedHashMap of this Bean
	 */
	public abstract LinkedHashMap toOrderedHashMap();
 	
 	/**
 	 * Allows pretty printing of xml ( tabs )
 	 * @return String
 	 */
	public abstract String toXML(int indent);  
 	
 	/**
 	 * Can this bean be a top level element?
 	 * 
 	 * @return boolean
 	 */
 	public abstract boolean isRootElement();

	/**
	 * Does this attribute have an inverted relationship in the XML?
	 * 
	 * @return boolean
	 */
	public abstract boolean isInvertedRelationship( String attributName );
	
	/**
	 * Convience to get the Primary Key
	 * 
	 * @return long
	 */
	public abstract long returnPrimaryKey();

	/**
	 * Convience to set the Primary Key
	 * 
	 * @return void
	 */
	public abstract void putPrimaryKey( long primaryKey );

	/**
	 * Convience to set a Foreign Key
	 * 
	 * @return void
	 */
	public abstract void putForeignKey( String keyName, long keyValue );
	
	// Implemented methods 
    
	/**
	 * Replaces XML sensitive characters with their safe versions
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeXML(String str)
	{
		return XMLUtils.escapeBodyValue(str);
		// The above method may be a more efficient way
		// of doing this ( loops over chars not Regex )
//	  str = str.replaceAll("&","&amp;");
//	  str = str.replaceAll(">","&lt;");
//	  str = str.replaceAll(">","&gt;");
//	  str = str.replaceAll("\"","&quot;");
//	  str = str.replaceAll("'","&apos;");
//	  return str;
	}

}
