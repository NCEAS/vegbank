package org.vegbank.common.model;
/*
 * '$RCSfile: VBModelBean.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-26 06:25:12 $'
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

 
/**
 * @author farrell
 *
 * An abstract class for the VBModelBeans, contains some utility methods 
 * and some abstract methods that need to be implemented by the beans.
 */
public abstract class VBModelBean
{
	public int indent = 0;
	
	public String toXML(int indent)
	{
	  this.indent = indent; 
	  return this.toXML();
	}   
 
 	/**
 	 * UnMarshall this bean to an XML String.
 	 * 
 	 * @return String -- XML of this Bean
 	 */
 	public abstract String toXML();
 	
 	/**
 	 * Can this bean be a top level element?
 	 * 
 	 * @return boolean
 	 */
 	public abstract boolean isRootElement();

    /**
     * This is used for pretty printing the generated XML
     * 
     * @param int - number of indents
     * @return String -- tab x indent number
     */
	public static String getIdent(int indent)
	{
	  StringBuffer sb = new StringBuffer();
	  for ( int i=0; i<indent; i++)
	  {
		sb.append("\t");
	  }
	  return sb.toString();
	}
    
	/**
	 * Replaces XML sensitive characters with their safe versions
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeXML(String str)
	{
	  str = str.replaceAll("&","&amp;");
	  str = str.replaceAll(">","&lt;");
	  str = str.replaceAll(">","&gt;");
	  str = str.replaceAll("\"","&quot;");
	  str = str.replaceAll("'","&apos;");
	  return str;
	}

}
