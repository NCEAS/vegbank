package org.vegbank.common.utility;

import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.vegbank.common.dbAdapter.*;

/**
 * '$RCSfile: Utility.java,v $'
 * 
 * Purpose: An utility class for Vegbank project.
 * 
 * '$Author: farrell $'
 * '$Date: 2003-10-17 22:09:14 $'
 * '$Revision: 1.16 $'
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

public class Utility
{
	
	public static AbstractDatabase dbAdapter;
	// Make a configuration option
	public static  String dbAdapterName = "org.vegbank.common.dbAdapter.PostgresqlAdapter";
	
	/** 
	 * Determine our db adapter class and create an instance of that class
	 */
	static {
		try {
			dbAdapter = (AbstractDatabase) createObject(dbAdapterName);
		} catch (Exception e) {
			System.err.println("Error in Vegbank Util static block:" + e.getMessage());
		}
	}
	
	public static String escapeCharacters(String s)
	{
		// Handle nulls
		if ( s == null)
			return null;
			
		String origString = s;
		// List of characters to escape
		char[] specialChar = {'\'', '$', '^'};
		
		for (int i = 0; i < specialChar.length ; i++)
		{
			char currentChar = specialChar[i];
			//System.out.println("----->" + currentChar);
			
			if ( s.indexOf ( currentChar) != -1 ) 
			{
				StringBuffer hold = new StringBuffer();
				char c;
				for ( int ii = 0; ii < s.length(); ii++ ) 
				{
					if ( (c=s.charAt(ii)) == currentChar  )
						hold.append ("\\" + currentChar );
					else
						hold.append(c);
				}
				s = hold.toString();
			}
		}
		//System.out.println("---->" + origString + " VS. " + s );
		return s;
	}

  /**
   * Convert a string to a boolean.
   * Vegbank sometimes stores booleans in Strings for ease of use ( laziness )
   * This attemps to right that wrong by converting certain String patterns into 
   * boolean values
   */
   public static boolean isTrue( String string)
   {
    boolean result = false;

    // Check for braindead stuff first
    if ( string == null || string.equals("") )
      return false;
      
    if ( string.equals("true") )
      return true;
    if ( string.equals("t") )
      return true;
      
    return result;
    
   }

	/**
	 * Thin wrapper around setting a date field in a PreparedStatement to handle
	 * adding nulls when needed, e.g. empty string.
	 * 
	 * @param date
	 * @param psmnt
	 * @param i
	 * @throws SQLException
	 */
	public static void insertDateField ( String date, PreparedStatement psmnt, int i)
		throws SQLException
	{
		int sqlDateType = java.sql.Types.DATE;
		if ( date == null  ||  date.equals("") )
		{
			psmnt.setNull(i, sqlDateType);
		}
		else
		{
			// this maybe should be setDate
			psmnt.setString(i, date);
		}
	}	
	
	/**
	 * Thin wrapper around setting a double field in a PreparedStatement to handle
	 * adding nulls when needed, e.g. empty string.
	 * 
	 * @param number
	 * @param psmnt
	 * @param i
	 * @throws SQLException
	 */
	public static void insertDoubleField ( String number, PreparedStatement psmnt, int i)
		throws SQLException
	{
		int sqlDoubleType = java.sql.Types.DOUBLE;
		if ( number == null  ||  number.equals("") )
		{
			psmnt.setNull(i, sqlDoubleType);
		}
		else
		{
			// this maybe should be setDate
			psmnt.setString(i, number);
		}
	}	
	
	/**
	 * Retrieves the content of a URL as a string
	 * 
	 * @param u - the URL
	 * @return	String - the content of the URL
	 * @throws java.io.IOException
	 */
	public static String getURLContent(URL u) throws java.io.IOException
	{
			//System.out.println("url: " + u.toString());
			char istreamChar;
			int istreamInt;
			InputStreamReader istream = new InputStreamReader(u.openStream());
			StringBuffer serverResponse = new StringBuffer();
			while((istreamInt = istream.read()) != -1)
			{
				istreamChar = (char)istreamInt;
				serverResponse.append(istreamChar);
			}
    
			return serverResponse.toString();
		}

	
	/**
	 * Instantiate a class using the name of the class at runtime
	 *
	 * @param className the fully qualified name of the class to instantiate
	 */
	public static Object createObject(String className) throws Exception {
 
		Object object = null;
		try {
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}
		return object;
	}
	
	/**
	 * Utility Method to check for nulls and empty String
	 *
	 */
	public static boolean isStringNullOrEmpty(String stringToCheck)
	{
		boolean result = false;
		if ( stringToCheck == null || stringToCheck.equals("") ) 
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	/**
	 * Convience method to check several string for nullness or emtyness
	 * @author farrell
	 *
	 * @param String[] -- Strings to check
	 * @return boolean -- 
	 */
	public static boolean isAnyStringNullorEmpty( String[] stringsToCheck)
	{
		boolean result = false;
		for (int i = 0; i < stringsToCheck.length; i++)
		{
			if ( isStringNullOrEmpty(stringsToCheck[i]) )
			{
				result = true;
				return result;
			}
		}
		return result;
	}
	
	/**
	 * Convience method to check several string for non nullness or non emtyness
	 * @author farrell
	 *
	 * @param String[] -- Strings to check
	 * @return boolean -- 
	 */
	public static boolean isAnyStringNotNullorEmpty( String[] stringsToCheck)
	{
		boolean result = false;
		for (int i = 0; i < stringsToCheck.length; i++)
		{
			if ( ! isStringNullOrEmpty(stringsToCheck[i]) )
			{
				result = true;
				return result;
			}
		}
		return result;
	}
	
	/**
	 * Convience method to create a simple comma separted string from a 
	 * string array.
	 *
	 * @param String[] -- Strings to convert to comma separate string
	 * @return String 
	 */
	public static String arrayToCommaSeparatedString(Object[] objects)
	{
		return joinArray(objects, ",");
	}
	
	/**
	 * Convience method to create a simple comma separted string from a 
	 * string array.
	 *
	 * @param String[] -- Strings to convert to comma separate string
	 * @return String 
	 */
	public static String joinArray(Object[] objects, String delimiter)
	{
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < objects.length; i++)
		{
			sb.append(objects[i]);
			if( (i +1) < objects.length )
			{
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	/**
	 * Capitalizes the first letter of a string. Leaves the rest of the Sting alone
	 * @param text
	 * @return String 
	 */
	public static String upperCaseFirstLetter(String text)
	{
		String result = text.substring(0, 1).toUpperCase() + text.substring(1);
		//System.out.println("Utiltiy > " +result);
		return result;
	}
	
	/**
	 * Prints out a hashtable in an easy to read fashion
	 * 
	 * @param hashtable -- hashtable to pretty print
	 */
	public static void prettyPrintHash( Hashtable hash )
	{
		//System.out.println(hash);
		System.out.println(prettyPrintHash(hash, 0));
	}
	
	private static String prettyPrintHash( Hashtable hash,  int indent)
	{
		//System.out.println(""+indent +" -- "+hash.hashCode());
		StringBuffer sb = new StringBuffer();
		Enumeration keys = hash.keys();
		while ( keys.hasMoreElements() )
		{
			Object key = keys.nextElement();
			sb.append(getIdent( indent ) + key + ":");  
			Object value = hash.get( key  );
			if ( value instanceof  java.util.Hashtable )
			{
				sb.append("\n");
				sb.append( prettyPrintHash( (Hashtable) value, indent + 1));
			}
			else if ( value instanceof java.util.Vector )
			{
				Enumeration elements =  ((Vector) value).elements();
				while ( elements.hasMoreElements())
				{
					Object element = elements.nextElement();
					if ( element instanceof  java.util.Hashtable )
					{		
						sb.append( "--->" +((Hashtable) element).get("TableName") );
						sb.append("\n");
						sb.append( prettyPrintHash( (Hashtable) element, indent + 1));
					}
				}
			}
			else
			{
				sb.append( "\t" + value + "\n");
			}
		}
		return sb.toString();
	}
	
	private static String getIdent(int indent)
	{
		StringBuffer sb = new StringBuffer();
		for ( int i=0; i<indent; i++)
		{
			sb.append("\t");
		}
		return sb.toString();
	}
}
