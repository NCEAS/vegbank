package org.vegbank.common.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.dbAdapter.AbstractDatabase;
import org.vegbank.common.model.Aux_role;
import org.vegbank.common.model.Place;
import org.vegbank.common.model.Plantconcept;
import org.vegbank.common.model.Plantstatus;

/*
 * '$RCSfile: Utility.java,v $'
 * 
 * Purpose: An utility class for Vegbank project.
 * 
 * '$Author: farrell $'
 * '$Date: 2004-04-19 14:53:06 $'
 * '$Revision: 1.31 $'
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
	/**
	 * Handle for logging
	 */
	private static Log log = LogFactory.getLog(Utility.class); 
	
	public static AbstractDatabase dbAdapter;
	// FIXME: Read from properties
	public static  final String DB_ADAPTER_NAME = "org.vegbank.common.dbAdapter.PostgresqlAdapter";

	
	public static final String DATABASE_NAME = ResourceBundle.getBundle("database").getString("databaseName");
	
	public static ResourceBundle vegbankPropFile = ResourceBundle.getBundle("vegbank");
	public static final String SMTP_SERVER = vegbankPropFile.getString("mailHost");
	public static final String VEGBANK_SCHEMA_LOACATION = vegbankPropFile.getString("schemaLocation");
	public static final String VEGBANK_SCHEMA_NAME = vegbankPropFile.getString("vegbankSchemaName");	
	public static final String VEGBANK_VERSION = vegbankPropFile.getString("vegbankVersion");
	public static final String VEGBANK_XML_SCHEMA= VEGBANK_SCHEMA_LOACATION + "/" + VEGBANK_SCHEMA_NAME;
	public static final String VB_HOME_DIR = vegbankPropFile.getString("vegbank.home.dir");
	public static final String MODELBEAN_CACHING = vegbankPropFile.getString("modelbean.caching");

	
	/** 
	 * Determine our db adapter class and create an instance of that class
	 */
	static 
	{
		try 
		{
			dbAdapter = (AbstractDatabase) createObject(DB_ADAPTER_NAME);
		} 
		catch (Exception e) 
		{
			log.error("Error in Vegbank Util static block:" + e.getMessage());
		}
	}
	
	/**
	 * There are some characters that need to be escaped before they are being
	 * written to the database, this escaping needs to be removed before usage.
	 * 
	 * @param  s -- String to escape chars on
	 * @return ready for db write
	 */
	public static String decodeFromDB(String s)
	{
		// Handle nulls
		if ( s == null)
			return null;
			
		//String origString = s;

		// TODO: Implement this method 
		// '' -> ', '$ -> $, '^ -> ^ ( perhaps removing all ' will work?)
		// needs experimentation

		//System.out.println("---->" + origString + " VS. " + s );
		return s;
	}
	
	/**
	 * There are some characters that need to be escaped before they are being
	 * written to the database
	 * 
	 * @param s -- String to escape chars on
	 * @return String -- ready for db write
	 */
	public static String encodeForDB(String s)
	{
		// Handle nulls
		if ( s == null)
			return null;
			
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
   * 
   * @param string String to test for true/t equality
   * @return is this a Stringified true?
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
 * <p>
 * Instantiate a class using the name of the class at runtime
 * </p>
 * 
 * @param className
 *          the fully qualified name of the class to instantiate
 * @return Instance of the requested class
 * @throws Exception
 *           the class finding/creation exceptions
 */
	public static Object createObject(String className) throws Exception
	{

		Object object = null;
		try
		{
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e)
		{
			throw e;
		} catch (IllegalAccessException e)
		{
			throw e;
		} catch (ClassNotFoundException e)
		{
			throw e;
		}
		return object;
	}
	
	/**
	 * <p>
	 * Utility Method to check for nulls and empty String
	 * </p>
	 * 
	 * @param stringToCheck 
	 * @return Is this Empty or null?
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
	 * <p>
	 * Convience method to check several string for nullness or emtyness
	 * </p>
	 *
	 * @param stringsToCheck -- Array of Strings to check
	 * @return Are any of these Strings empty or null?
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
	 * <p>
	 * Convience method to check several string for non nullness or non emtyness
	 * </p>
	 *
	 * @param stringsToCheck Array of <code>String</code>s to check
	 * @return Are any of these <code>Strings</code> NOT empty or null?
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
	 * <p>
	 * Convience method to create a simple comma separted string from an <code>Object[]</code>.
	 * </p>
	 *
	 * @param objects 
	 * 		Array of <code>Object</code>s to convert to comma separate <code>String</code>
	 * @return String 
	 */
	public static String arrayToCommaSeparatedString(Object[] objects)
	{
		return joinArray(objects, ",");
	}
	
	/**
	 * <p>
	 * Convience method to create a simple delimetted <code>String</code> from 
	 * an <code>Object[]</code>.
	 * </p>
	 *
	 * @param objects
	 * @param delimiter 
	 * 		The delimeter to use when constructing the <code>String</code>
	 * @return The delimited List as <code>String</code>
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
	 * @param hash -- hashtable to pretty print
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
	
	/**
	 * Gets current date and time using UTC Timezone.
	 *   
	 * @return Date-- current Date
	 */
	public static Date getNow( ) 
	{
		// TODO: not tested yet
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    	return cal.getTime(); 
	}
	
	/**
	 * Gets current date in a format that can be accepted 
	 * by the RDBMS and that is like: Aug 9, 2002
	 * 
	 * @return the date in a RDBMS format 
	 */
	 public static String getCurrentDate()
	 {
	 	// TODO: use getNow() when it has been tested
		Date now = new Date();  //Set Date variable now to the current date and time
		DateFormat med = DateFormat.getDateInstance (DateFormat.MEDIUM);
		return med.format(now);
	 }
	
	/**
	 * Convience method for get a string of tabs
	 * 
	 * @param indent
	 * @return
	 */
	private static String getIdent(int indent)
	{
		StringBuffer sb = new StringBuffer();
		for ( int i=0; i<indent; i++)
		{
			sb.append("\t");
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Convience method to get the VB Primary Key Name, basically 
	 * handles expections to the naming conviention.
	 * </p>
	 * 
	 * @param table The name of this table
	 * @return the PKName for this table
	 */
	public static String getPKNameFromTableName(String table)
	{
		String PKname = "";
		if ( table.equalsIgnoreCase("place"))
		{
			PKname = Place.PKNAME;
		}
		else if ( table.equalsIgnoreCase("aux_role") )
		{
			PKname = Aux_role.PKNAME;
		}
		else
		{
			PKname = table + "_ID";
		}
		return PKname;
	}

	/**
	 * <p>
	 * Foreign Key don't always have the same name as the primary key of the
	 * the the primary table, this is a utility to allow this lookup to happen.
	 * </br>
	 * 
	 * <b>Not</b> a comprehensive lookup!
	 * </p>
	 * 
	 * @param FKName The Foriegn Key name
	 * @return The Primary Key name that corresponds to the inputed FK name.
	 */
	public static String getPKNameFromFKName(String FKName)
	{
		// TODO: this is not comprehesive as this is only a problem
		// for some lookups, should be comprehensive to avoid misuse
		String PKname = "";	
		if ( FKName.equalsIgnoreCase(Plantstatus.PLANTPARENT_ID))
		{
			PKname = Plantconcept.PKNAME;
		}
		else
		{
			PKname = FKName;
		}
		return PKname;
	}

	/**
	 * Only certain database names need to have accessionCodes loaded
	 * 
	 * @return boolean 
	 */
	public static boolean isLoadAccessionCodeOn()
	{
		boolean result = false;
		log.debug("Utility: databaseName = " + DATABASE_NAME);
		
		if ( DATABASE_NAME.equalsIgnoreCase("vegbank") || DATABASE_NAME.equalsIgnoreCase("vegtest"))
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
	 * @return
	 */
	public static String getAccessionPrefix()
	{
		String accessionPrefix = "";
		
		// This is a function of database name and host machine
		if ( DATABASE_NAME.equalsIgnoreCase("vegbank"))
		{
			accessionPrefix = "VB";
		}
		else if ( DATABASE_NAME.equalsIgnoreCase("vegtest"))
		{
			accessionPrefix = "VT";
		}
		else
		{
			accessionPrefix = "NOTVALID";
		}
		
		return accessionPrefix;
	}
	
	/**
	 * Convert an inputstream to a String. Not sure how the handle encoding 
	 * so using System default for now.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromInputStream(InputStream is) throws IOException
	{
		InputStreamReader reader = new InputStreamReader(is);
		char[] buffer = new char[4096];
		
		StringWriter writer = new StringWriter();
		
		int bytes_read;
		while ((bytes_read = reader.read(buffer)) != -1)
		{
			writer.write(buffer, 0, bytes_read);
		}
		
		String result = writer.toString();
		return result;
	}
	
	/**
	 * Convience method to Uppercase first char of a String and lower case
	 * the rest 
	 * @param stringToCapitalize
	 * @return
	 */
	public static String upperCaseFirstChar(String stringToCapitalize )
	{
		StringBuffer result = new StringBuffer();
		result.append( stringToCapitalize.substring(0,1).toUpperCase() );
		result.append( stringToCapitalize.substring(1).toLowerCase() );
		return result.toString();
	}
	
	/** 
	 * Parse up an Accessioncode into its parts
	 * 
	 * @param accessionCode Vegbank AC to be parsed.
	 * @return code, entityname, key in a <code>String[]</code>
	 */
	public static HashMap parseAccessionCode(String accessionCode)
	{
		String DBCODE = "DBCODE";
		String ENTITYCODE = "ENTITYCODE";
		String KEYVALUE = "KEYVALUE";
		String CONFIMATIONCODE = "CONFIMATIONCODE";
		
		HashMap parsedAC = new HashMap();
		// method
		log.debug("Utility: accessionCode = " + accessionCode);
		Pattern pattern = Pattern.compile("([^\\.]*)\\.([^\\.]*)\\.([^\\.]*)\\.{0,1}([^\\.]*)");
		Matcher m = pattern.matcher(accessionCode);
		if ( m.find() )
		{	
			parsedAC.put("DBCODE", m.group(1) );
			parsedAC.put("ENTITYCODE", m.group(2) );
			parsedAC.put("KEYVALUE", m.group(3) );
			parsedAC.put("CONFIMATIONCODE", m.group(4) );
		}
		log.debug("Parsed an AccessionCode: DBCode > '" + parsedAC.get(DBCODE) + "' ENTITYCODE > '" + parsedAC.get(ENTITYCODE) + "' KEYVALUE > '" + parsedAC.get(KEYVALUE) + "' CONFIMATIONCODE > '" + parsedAC.get(CONFIMATIONCODE) + "'"  );
		return parsedAC;
	}
	
	/**
	 * Ugly hack to test if an entityCode is a root entity or not.
	 * 
	 * @param entityCode
 	 * @return Is root entity?
	 */
	public static boolean isRootEntity(String entityCode)
	{
		boolean result = false;

		if (entityCode.equalsIgnoreCase("OB") || entityCode.equalsIgnoreCase("PC")
				|| entityCode.equalsIgnoreCase("CC"))
		{
			result = true;
		}

		return result;
	}
	
	/**
	 * Tool to save a file to the filesystem.
	 * 
	 * @param in
	 *          Reader to write out to filesystem
	 * @param filename
	 *          The name of the file to write to
	 * @throws IOException
	 */
	public static void saveFile( Reader in, String filename) throws IOException
	{
		File outputFile = new File(filename);

    FileWriter out = new FileWriter(outputFile);
    int c;

    while ((c = in.read()) != -1)
    	out.write(c);

    out.close();
    in.close();
	}
	
}
