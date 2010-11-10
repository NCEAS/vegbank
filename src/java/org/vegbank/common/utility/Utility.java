package org.vegbank.common.utility;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.dbAdapter.AbstractDatabase;
import org.vegbank.common.model.Aux_role;
import org.vegbank.common.model.Place;
import org.vegbank.common.model.Plantconcept;
import org.vegbank.common.model.Plantstatus;
import org.vegbank.common.model.VBModelBean;
import org.vegbank.common.utility.mail.*;
import org.vegbank.common.Constants;

/*
 * '$RCSfile: Utility.java,v $'
 *
 * Purpose: An utility class for Vegbank project.
 *
 * '$Author: mlee $'
 * '$Date: 2006-08-26 22:49:44 $'
 * '$Revision: 1.56 $'
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

public class Utility implements Constants
{
	/**
	 * Handle for logging
	 */
	private static Log log = LogFactory.getLog(Utility.class);

	public static AbstractDatabase dbAdapter;
	// FIXME: Read from properties
	public static  String DB_ADAPTER_NAME = "org.vegbank.common.dbAdapter.PostgresqlAdapter";

	// Bundle:  database
	public static String DATABASE_NAME;

	// Bundle:  general
	public static boolean AUTO_APPEND_WILDCARD = false;

	// Bundle:  vegbank
	public static ResourceBundle vegbankPropFile;
	public static ResourceBundle dbPropFile;
	public static String SMTP_SERVER;
	public static String SMTP_PORT;
	public static String VEGBANK_SCHEMA_LOCATION;
	public static String VEGBANK_SCHEMA_NAME;
    public static String VEGBANK_EXPORT_SCHEMA_NAME;
	public static String VEGBANK_XML_SCHEMA;
    public static String VEGBANK_XML_EXPORT_SCHEMA;
	public static String VEGBANK_VERSION;
	public static String VEGBANK_XML_SCHEM;
	public static String VB_HOME_DIR;
	public static String VB_DATA_DIR;
	public static String VB_EXPORT_DIR;
	public static String VB_EXPORT_DIRNAME;
	public static String SERVER_ADDRESS;
	public static String WEBAPP_DIR;
	public static String WEB_DIR;
	public static String MODELBEAN_CACHING;
	public static String VB_EMAIL_FROM;
	public static String VB_EMAIL_ADMIN_TO;
	public static String VB_EMAIL_ADMIN_FROM;
	public static String DATABASE_ACCESSION_KEY_PREASSIGN;
	public static List DS_CANDIDATES;
	public static String PARAM_DELIM = "__";
	public static String DATACART_KEY = "datacart";   // found in session
	public static String DATACART_COUNT_KEY = "datacart-count";   // found in session


	static { try {
		// Bundle:  database; used elsewhere (e.g. dataload)
		dbPropFile = ResourceBundle.getBundle("database");
		DATABASE_NAME = dbPropFile.getString("databaseName");

		// Bundle:  general
		AUTO_APPEND_WILDCARD = false;
		String tmp = ResourceBundle.getBundle("general").getString("queries.auto_append_wildcard");
		if (!isStringNullOrEmpty(tmp) && !tmp.equals("false") && !tmp.equals("0")) {
			AUTO_APPEND_WILDCARD = true;
		}

		// Bundle:  vegbank
		vegbankPropFile = ResourceBundle.getBundle("vegbank");
		VEGBANK_SCHEMA_LOCATION = vegbankPropFile.getString("schemaLocation");
		if (!VEGBANK_SCHEMA_LOCATION.endsWith("/")) { VEGBANK_SCHEMA_LOCATION += "/"; }
		VEGBANK_SCHEMA_NAME = vegbankPropFile.getString("vegbankSchemaName");
        VEGBANK_EXPORT_SCHEMA_NAME = vegbankPropFile.getString("vegbankExportSchemaName");
		VEGBANK_VERSION = vegbankPropFile.getString("vegbankVersion");
		VEGBANK_XML_SCHEMA = VEGBANK_SCHEMA_LOCATION + VEGBANK_SCHEMA_NAME;
        VEGBANK_XML_EXPORT_SCHEMA = VEGBANK_SCHEMA_LOCATION + VEGBANK_EXPORT_SCHEMA_NAME;

		SERVER_ADDRESS = vegbankPropFile.getString("serverAddress");
		WEBAPP_DIR = vegbankPropFile.getString("vegbank.webapp.dir");
		WEB_DIR = vegbankPropFile.getString("vegbank.web.dir");
		VB_HOME_DIR = vegbankPropFile.getString("vegbank.home.dir");
		VB_DATA_DIR = vegbankPropFile.getString("vegbank.data.dir");
		VB_EXPORT_DIRNAME = vegbankPropFile.getString("vegbank.export.dirname");
		VB_EXPORT_DIR = WEB_DIR + VB_EXPORT_DIRNAME;
		MODELBEAN_CACHING = vegbankPropFile.getString("modelbean.caching");

		SMTP_SERVER = vegbankPropFile.getString("mailHost");
		SMTP_PORT = vegbankPropFile.getString("mailPort");
		VB_EMAIL_FROM = vegbankPropFile.getString("systemEmail");
		VB_EMAIL_ADMIN_TO = vegbankPropFile.getString("admin.email.to");
		VB_EMAIL_ADMIN_FROM = vegbankPropFile.getString("admin.email.from");

        // accessionCode properties for this database:
        // accessionPropFile = ResourceBundle.getBundle("accession");
        DATABASE_ACCESSION_KEY_PREASSIGN = vegbankPropFile.getString("database.accession.key.preassign");

	} catch (Exception ex) {
		log.error("There was a problem loading Utility properties", ex);
	} }


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
     * Generates a VBModelBean from the field names in a ResultSet.
     */
    public static VBModelBean buildBean(ResultSetMetaData meta, ResultSet rs, String beanName) {

        Object bean = null;
        try {
            if (rs == null || !rs.first()) { return null; }

            int colCount = meta.getColumnCount();
            bean = Utility.createObject(VBObjectUtils.DATA_MODEL_PACKAGE + beanName);

            // use DB column names to set properties
            for (int i=1; i<=colCount; i++) {
                String propName = meta.getColumnName(i);
                Object value = rs.getObject(i);
                //String value = rs.getString(i);
                try {
                    if (value != null) {
                        BeanUtils.copyProperty(bean, propName, value);
                    }
                } catch (Exception ex) {
                    log.debug("unable to set " + propName + " in bean " + beanName);
                }
            }
        } catch (Exception ex) {
            log.error("Problem building bean from DB results.", ex);
        }

        return (VBModelBean)bean;
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
		return ( stringToCheck == null || stringToCheck.equals("") );
	}

	/**
	 * Return true if string is not "false", "no", "O", empty or null.
	 */
	public static boolean isStringTrue(String stringToCheck)
	{
		return !(Utility.isStringNullOrEmpty(stringToCheck) ||
				stringToCheck.equalsIgnoreCase("false") ||
				stringToCheck.equalsIgnoreCase("no") ||
				stringToCheck.equalsIgnoreCase("0"));
	}

	/**
	 * <p>
	 * Utility Method to check for nulls and empty array.
	 * </p>
	 *
	 * @param arrayToCheck
	 * @return Is this Empty or null?
	 */
	public static boolean isArrayNullOrEmpty(Object[] arrayToCheck)
	{
		return arrayToCheck == null || arrayToCheck.length == 0;
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
        if (hash == null) {
            return;
        }
		log.debug(prettyPrintHash(hash, 0));
	}

	private static String prettyPrintHash( Hashtable hash,  int indent)
	{
		//log.debug(indent +" -- "+hash.hashCode());
		StringBuffer sb = new StringBuffer(256);
		Iterator keys = hash.keySet().iterator();
		while ( keys.hasNext() )
		{
			Object key = keys.next();
			sb.append(getIdent( indent ) + key + ":");
			Object value = hash.get( key  );
			if ( value instanceof  java.util.Hashtable )
			{
				sb.append("\n");
				sb.append( prettyPrintHash( (Hashtable) value, indent + 1));
			}
			else if ( value instanceof java.util.Vector )
			{
				Iterator it =  ((Vector) value).iterator();
				while ( it.hasNext())
				{
					Object element = it.next();
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
	public static java.util.Date getNow( )
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
		java.util.Date now = new java.util.Date();  //Set Date variable now to the current date and time
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
        // I'm not sure what we were thinking when this code was written,
        // but I'm disabling it.  2/1/2005 PMA
        return true;

        /*
		boolean genAC = false;
		log.debug("Utility: databaseName = " + DATABASE_NAME);

		if (DATABASE_NAME.equalsIgnoreCase("vegbank") || DATABASE_NAME.equalsIgnoreCase("vegtest")) {
			genAC = true;
		} else {
			genAC = false;
		}
		return genAC;
        */
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
			accessionPrefix = vegbankPropFile.getString("vegbank.accession.prefix");
			if (Utility.isStringNullOrEmpty(accessionPrefix)) {
			    accessionPrefix = "VB";
            } else {
			    accessionPrefix = accessionPrefix.toUpperCase();
            }
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
	 * Create and write a temp file in the system tmp dir (/tmp).
	 *
	 * @param in
	 *          Reader to write out to filesystem
	 * @throws IOException
	 * @return path to temp file
	 */
	public static String writeTempFile(Reader in) throws IOException
	{
        File outputFile = File.createTempFile(Integer.toString(in.hashCode()), null);

        FileWriter out = new FileWriter(outputFile);
        int c;

        while ((c = in.read()) != -1)
            out.write(c);

        out.close();
        in.close();
        return outputFile.getAbsolutePath();
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

        if (!outputFile.exists()) {
            // create the file
            outputFile.createNewFile();
        }

        FileWriter out = new FileWriter(outputFile);
        int c;

        while ((c = in.read()) != -1)
            out.write(c);

        out.close();
        in.close();
	}


	/**
	 * Tool to delete a file from the filesystem.
	 *
	 * @param filename
	 *          The name of the file to write to
	 * @throws IOException
	 */
	public static void deleteFile(String filename) throws IOException
	{
		(new File(filename)).delete();
	}


    /**
     * Serialize a file.
     *
     * @param fileName
     * @param the Object
     * @throws IOException
     */
    public static void saveBinaryFile(String fileName, Object o) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(new File(fileName));
        ObjectOutputStream objectOut = new ObjectOutputStream (fileOut);
        objectOut.writeObject(o);
    }


    /**
     * Deserialize a file.
     *
     * @param fileName
     * @param the Object
     * @throws IOException
     */
    public static Object loadBinaryFile(String fileName) throws IOException {
        FileInputStream fileIn = new FileInputStream(new File(fileName));
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        try {
            return objectIn.readObject();
        } catch (Exception ex) {
            log.error("couldn't load binary file " + fileName, ex);
        }
        return null;
    }



	/**
	 *
	 */
	public static String capitalize(String str) {
		if (isStringNullOrEmpty(str)) {
			return "";
		}

		return Character.toUpperCase(str.charAt(0)) +
				str.toLowerCase().substring(1);
	}


	/**
	 * @return true if the value of the given string is a number
	 */
	public static boolean isNumeric(String s) {
		if (Utility.isStringNullOrEmpty(s)) {
			return false;
		}

		try { Long.parseLong(s); }
		catch (NumberFormatException nfex) { return false; }

		return true;
	}


	/**
	 * @return true if the first/only value of the given
	 *    comma-separated list is a number
	 */
	public static boolean isNumericList(String csv) {
		if (Utility.isStringNullOrEmpty(csv)) {
			return false;
		}

		String value = csv;
		if (csv.indexOf(',') != -1 || csv.indexOf('|') != -1) {
			// a list
			StringTokenizer st = new StringTokenizer(csv, ",|");
			if (st.hasMoreTokens()) { value = st.nextToken(); }
		}

		return isNumeric(value);
	}


	/**
	 *
	 */
	public static void notifyAdmin(String subject, String body) {
		String adminFromEmail = vegbankPropFile.getString("systemEmail");
		String adminToEmail = vegbankPropFile.getString("systemEmail");
		String server = vegbankPropFile.getString("machine_url");

		if (isStringNullOrEmpty(adminFromEmail)) { adminFromEmail = "site@vegbank.org"; }
		if (isStringNullOrEmpty(adminToEmail)) { adminToEmail = "dba@vegbank.org"; }
		if (isStringNullOrEmpty(server)) { server = "unknown vegbank host"; }

		try {
			Mailer m = new Mailer();
			m.sendPlain("ADMIN NOTIFICATION FROM " + server,
					"SUBJECT:  " + subject + "\n\n" + body, adminToEmail,
					null, null, adminFromEmail);
			log.info("Sent an email notification to admin re: " + subject);
		} catch (MailerException  mex) {
			log.error("Problem notifying admin", mex);
		}
	}

	/**
     *
	 * @return
	 */
	public static boolean canAddToDatasetOnLoad(String tableName) {
        if (Utility.isStringNullOrEmpty(tableName)) {
            log.debug("canAddToDatasetOnLoad(): given empty string");
            return false;
        }

        if (DS_CANDIDATES == null) {
            String s = vegbankPropFile.getString("dataset.candidates");
            StringTokenizer st = new StringTokenizer(s, ",");
            DS_CANDIDATES = new ArrayList();
            while (st.hasMoreTokens()) {
                DS_CANDIDATES.add(st.nextToken().toLowerCase());
            }
        }

        return DS_CANDIDATES.contains(tableName.toLowerCase());
    }

    /**
     * Returns a version of the given string that is no
     * longer than len chars and ends in "..." if
     * any chars were abbreviated (string was long).
     */
    public static String abbreviate(String src, int len) {
        if (Utility.isStringNullOrEmpty(src)) {
            return "";
        }
        if (src.length() > len) {
            return src.substring(0, len-3) + "...";
        } else {
            return src;
        }
    }

    /**
     * Returns usr_id of authenticated user, or null if not logged in.
     */
    public static Long getAuthenticatedUsrId(HttpSession session) {
        return (Long)session.getAttribute(USER_KEY);
    }

    /**
     * Returns path and parameters for last search.
     */
    public static String getLastSearchURL(HttpSession session) {
        String url = (String)session.getAttribute(LAST_SEARCH_URL);
        if (url == null) {
            url = "";
        }
        log.debug("GOT " + LAST_SEARCH_URL + ": " + url);
        return url;
    }

    /**
     * Saves path and parameters for last search.
     */
    public static void setLastSearchURL(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String qs = request.getQueryString();
        if (qs != null) {
            url.append("?").append(qs);
        }
        log.debug("setting " + LAST_SEARCH_URL + " to " + url.toString());
        request.getSession().setAttribute(LAST_SEARCH_URL, url.toString());
    }

    /**
     *
     */
    public static String getVegBankProperty(String key) {
        return vegbankPropFile.getString(key);
    }
}
