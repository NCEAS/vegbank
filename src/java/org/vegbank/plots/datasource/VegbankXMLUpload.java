package org.vegbank.plots.datasource;

/*
 * '$RCSfile: VegbankXMLUpload.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-12-05 23:12:12 $'
 *	'$Revision: 1.18 $'
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
 * Read a Vegbank format XML file, generate a report, and 
 * create a SQL insert statements.
 */

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.vegbank.common.model.*;
import org.vegbank.common.utility.AccessionGen;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;
import org.xml.sax.*;

public class VegbankXMLUpload 
{
	private XMLReader xr = null;
	private NativeXMLReader nr = null;
	
	
	private boolean validate = true;
	// TODO: use this variable as expected
	private boolean rectify = true;	
	private boolean load = true;
	
	// Errors reported
	private LoadingErrors errors = null;
		
 	public VegbankXMLUpload() throws Exception
  	{
		xr = this.getXMLReader();
  	}

	public VegbankXMLUpload(boolean validate, boolean rectify, boolean load) throws Exception
	{
		this.setLoad(load);
		this.setRectify(rectify);
		this.setValidate(validate);
		xr = this.getXMLReader();
	}
	
	public XMLReader getXMLReader() throws Exception
	{
		this.errors = new LoadingErrors();
		nr = new NativeXMLReader(validate);
		XMLReader xr = nr.getXMLReader();
		return xr;
	}


	public void processXMLFile( String xmlFileName ) throws IOException, SAXException
	{
		LogUtility.log( "VegbankXMLUpload: Validation on: " + this.validate);
		LogUtility.log( "VegbankXMLUpload: Rectification on: " + this.rectify);
		LogUtility.log( "VegbankXMLUpload: Database Loading on: " + this.load);
		
		SAXValidationErrorHandler errorHandler = new SAXValidationErrorHandler(errors);
		SAX2DBContentHandler contentHandler = new SAX2DBContentHandler(errors);
		
		File xmlFile = new File(xmlFileName);
		
		if ( validate )
		{	
			xr.setErrorHandler( errorHandler );
			xr.parse( this.getInputSource(xmlFile) );
			LogUtility.log( "This file is valid: " + errorHandler.isValid() );
		}
		
		if ( load && errorHandler.isValid() )
		{
			xr.setContentHandler( contentHandler );
			xr.parse( this.getInputSource(xmlFile));
		}
		else 
		{
			LogUtility.log( "No attempt was made to rectify or loading this dataset");
			errors.AddError(LoadingErrors.DATABASELOADINGERROR, "No attempt was made to load this dataset into the database");
			errors.AddError(LoadingErrors.RECTIFICATIONERROR, "No attempt was made to rectify this dataset with the database");
		}
	}
	
	private InputSource getInputSource( File pFile) throws FileNotFoundException
	{
		FileInputStream mFileInputStream = new FileInputStream( pFile );
		return new InputSource( mFileInputStream );
	}

	/**
	 * Returns the errors that were encountered upon loading
	 * 
	 * @return
	 */
	public LoadingErrors getErrors()
	{
		return errors;
	}
	
	/**
	 * Provide a command line interface 
	 */
	public static void  main (String[] args) throws Exception
	{
		String fileName = null;
		VegbankXMLUpload vbUpload = new VegbankXMLUpload();
		
		System.out.println("Vegbank XML Parser version 1.0");
		System.out.println("Usage: java org.vegbank.plots.datasource  [-l] [-v] XMLFile");
		System.out.println("\t -v Validate the file against the schema");
		System.out.println("\t -l Load the file into the database");		
		System.out.println("-----------------------------------------------------------------------");
		
		int argNumber = args.length;
		for ( int i=0 ; i<argNumber; i++)
		{
			if (args[i].equals("-l"))
			{
				// Load the file
				vbUpload.setLoad(true);
			}
			else if (args[i].equals("-v"))
			{
				// Validate the file
				vbUpload.setValidate(true);
			}
			else if (!args[i].startsWith("-"))
			{
				fileName = args[i];
			}
			else
			{
				System.out.println("You entered and invalid option");
			}
		}
		
		if ( fileName == null)
		{
			System.out.println("Please enter XMLFile to process");
		}
		else
		{
			vbUpload.processXMLFile(fileName);
			
			System.out.println("REPORT:\n");
			System.out.println(vbUpload.getErrors().getSummaryMessage());
			System.out.println("-----------------------------------------------------------------------");
			System.out.println("\tVALIDATION");
			System.out.println("-----------------------------------------------------------------------");
			System.out.println(vbUpload.getErrors().getValidationReport("/n"));
			System.out.println("-----------------------------------------------------------------------");
			System.out.println("\tRECTIFICATION");
			System.out.println("-----------------------------------------------------------------------");
			System.out.println(vbUpload.getErrors().getRectificationReport("/n"));
			System.out.println("-----------------------------------------------------------------------");			
			System.out.println("\tDATABASE LOADING");
			System.out.println("-----------------------------------------------------------------------");
			System.out.println(vbUpload.getErrors().getLoadReport("/n"));
			System.out.println("-----------------------------------------------------------------------");
			System.out.println("-----------------------------------------------------------------------");
		
			System.exit(0);
		}
	}

	/**
	 * @param b
	 */
	public void setLoad(boolean b)
	{
		load = b;
	}

	/**
	 * @param b
	 */
	public void setRectify(boolean b)
	{
		rectify = b;
	}

	/**
	 * @param b
	 */
	public void setValidate(boolean b) throws Exception
	{
		validate = b;
		nr.setValidate(b);
	}
	
	
	
	public class SAX2DBContentHandler implements ContentHandler
	{
		private LoadingErrors errors = null;
		
		public SAX2DBContentHandler(LoadingErrors errors)
		{
			this.errors = errors;
			tables.add(tmpStore);
		}
		
		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endDocument()
		 */
		public void endDocument() throws SAXException
		{
			LoadTreeToDatabase ltdb = new LoadTreeToDatabase(errors);
			try
			{
				ltdb.insertVegbankPackage( (Hashtable) ( (Vector) tmpStore.get("VegBankPackage")).firstElement());
			}
			catch (SQLException e)
			{
				throw new SAXException(e);
			}
		}

		private Hashtable tmpStore = new Hashtable();
		//private String previousTableName = null;
		//private Hashtable previousTable = null;
		private Vector tableNames = new Vector();
		private String fieldName = null;
		private boolean atFirstPlot = true;
		private Vector tables = new Vector();
		private Vector fKFields = new Vector();
		// Characters Sections can be truncated in many parts 
		String currentValue = "";

		/** SAX Handler that is called at the start of each XML element */
		public void startElement(
			String uri, 
			String localName,
			String qName, 
			Attributes atts)
				throws SAXException 
		{
			// Document representation that points to the root document node
			if (atFirstPlot) 
			{
				if ( localName.trim().equals("VegBankPackage"))
				{
					atFirstPlot = false;
					tableNames.add(localName);
					this.addTable(localName);
					//this.newPlot();	
				}
			}
			// New Table?
			else if ( isTableName(localName) )
			{
				tableNames.add(localName);
				this.addTable(localName);
				// special handling? ... particular name handled differently?	
			}
			// New Field?
			else
			{
				String unQualifiedFieldName =
					VBObjectUtils.getUnQualifiedName(localName);
				
				
				// If this is a foriegn key name
				if (isFKField(unQualifiedFieldName))
				{
					this.fKFields.add(unQualifiedFieldName);
					tableNames.add(unQualifiedFieldName);
					addTable(unQualifiedFieldName);
				}
				else
				{
					this.fieldName = unQualifiedFieldName;
				}
				
			}	
			
			for ( int i=0; i < atts.getLength(); i++)
			{
				String attrName = atts.getLocalName(i);
				String attrValue = atts.getValue(i);
				if (attrName.equalsIgnoreCase("revision.REVISION_ID"))
				{
					// Add a REVISION_ID field to the hashtable 
					this.getCurrentTable().put("REVISION_ID", attrValue);
					this.addRevision(attrValue);
				}
				else if (attrName.equalsIgnoreCase("noteLink.NOTELINK_ID"))
				{
					// Add a NOTELINK_ID field to the hashtable 
					this.getCurrentTable().put("NOTELINK_ID", attrValue);
					this.addNoteLink(attrValue);
				}
				else
				{
					// No rule for this attribute  
				}
			}	
		}
		
		private void addRevision(String value)
		{
			Hashtable revision = new Hashtable();
			revision.put("tableName", tableNames.lastElement());
			revision.put("tableAttribute", fieldName);
			revision.put("REVISION_ID", value);
			
			Vector revisionList = (Vector) this.getCurrentTable().get("revisionList");
			if ( revisionList == null)
			{
				revisionList= new Vector();
				this.getCurrentTable().put("revisionList", revisionList );
			}
			revisionList.add(revision);
		}

		private void addNoteLink(String value)
		{
			Hashtable noteLink = new Hashtable();
			noteLink.put("tableName", tableNames.lastElement());
			noteLink.put("attributeName", fieldName);
			noteLink.put("NOTELINK_ID", value);
			
			Vector noteLinkList = (Vector) this.getCurrentTable().get("noteLinkList");
			if ( noteLinkList == null)
			{
				noteLinkList= new Vector();
				this.getCurrentTable().put("noteLinkList", noteLinkList );
			}
			noteLinkList.add(noteLink);
		}	
		

		private boolean isFKField( String fieldName)
		{
			boolean result = false;
			//LogUtility.log("1111 " + fieldName + " >> " + tableNames.lastElement() + "_ID");
			if ( fieldName.endsWith("_ID")  && ! fieldName.equalsIgnoreCase( tableNames.lastElement() + "_ID"))
			{
				//LogUtility.log("222 " + tableNames.lastElement() + "_ID");
				result = true;
			}
			return result;
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		public void endElement(
			String uri, 
			String localName,
			String qName)
				throws SAXException
		{
			// Add the value to this field
			this.addValue(currentValue);
			// Reset CurrentValue
			currentValue = "";
			
			//LogUtility.log("END ... " + localName);
			// Table?
			if ( isTableName(localName) )
			{				
				
				if ( this.getCurrentTable() != null )
				{
					//this.showMeTheTable(localName, this.getCurrentTable());
					
					tableNames.remove( tableNames.size() -1 );	
					tables.remove( tables.size() -1 );	
				}
				
			}
			else if ( this.isFKField( VBObjectUtils.getUnQualifiedName(localName) ) )
			{
				tableNames.remove( tableNames.size() -1 );	
				tables.remove( tables.size() -1 );
				this.fKFields.remove( fKFields.size() -1 );	
			}
			//  Value?
			else
			{
				this.fieldName = null;
				//LogUtility.log("Finished with field: " + localName);
			}
		} 
		
		
		/**
		 *  Debugging method ...
		 * 
		 * @param localName
		 * @param hashtable
		 */
		private void showMeTheTable(String tableName, Hashtable hashtable)
		{
			LogUtility.log("SAX2DBContentHandler: Finished storing table: " + tableName);
			if ( tableName.equalsIgnoreCase("plantstatus"))
			{
				Utility.prettyPrintHash( hashtable );
			}
		}

		private Hashtable getPreviousTable()
		{
			Hashtable previousTable = null;
			try
			{
				previousTable = (Hashtable) tables.get( tables.size() - 2);
			}
			catch (RuntimeException e)
			{
				LogUtility.log("SAX2DBContentHandler: Misread on Hashtable constructed from XML", e);
				throw e;
			}
			return previousTable;
		}
		
		private Hashtable getCurrentTable()
		{
			return (Hashtable) tables.lastElement();
		}
	
		/**
		 * The format of the schema maps directly into the database schema.
		 * Table names have no '.' in them and values have the format of 
		 * "tablename.fieldname" and have a '.' character in them.
		 * 
		 * @param elementName
		 * @return boolean -- is this the name of a table
		 */
		private boolean isTableName( String elementName)
		{
			boolean result = false;
			if ( elementName.indexOf('.') == -1)
			{
				result = true;
				//LogUtility.log("TABLE: " + elementName);
			}
			else
			{
				//LogUtility.log("NOT TABLE: " + elementName);
			}
			return result;
		}

//		private void addFKTable( String fieldName)
//		{
//			LogUtility.log("|||| " +  fieldName + ": " + this.getCurrentTable() );
//			Hashtable table = new Hashtable();
//			this.getCurrentTable().put(fieldName,table);
//			tables.add( table );
//		}

		private void addValue( String value)
		{
			if ( fieldName == null ||  this.isFKField(fieldName))
			{
				// Do nothing
			}
			else
			{
				//LogUtility.log( getCurrentTable() + "\n----->" + fieldName );
				this.getCurrentTable().put(fieldName, value);
			}
		}
	
		private void addTable ( String tableName)
		{
			// Allows several tables of the same name to be added
			Vector vector = null;
			Hashtable table = new Hashtable();

			if ( this.fKFields.size() != 0  && tableName.equals( this.fKFields.lastElement()) )
			{
					//LogUtility.log("Adding a FK table: " + tableName);
					this.getCurrentTable().put(tableName, table);
					//Utility.prettyPrintHash( this.getPreviousTable() );
					//LogUtility.log("Here we are");
					//Utility.prettyPrintHash( this.getCurrentTable() );
			}
			else
			{
	
				//if ( this.getPreviousTable() != null && this.getPreviousTable().containsKey(tableName))
				if ( this.getCurrentTable() != null && this.getCurrentTable().containsKey(tableName))
				{
					LogUtility.log("SAX2DBContentHandler: ... table exists ... adding another : " + tableName );
				
					//vector = (Vector)  this.getPreviousTable().get(tableName);	
					vector = (Vector)  this.getCurrentTable().get(tableName);			
					vector.add( table );		
				}
				else
				{
					vector = new Vector();
					vector.add( new Hashtable() );
				}
				//LogUtility.log(this.getCurrentTable() + " and " + vector);
				this.getCurrentTable().put(tableName, vector );
				Vector tableList = (Vector) this.getCurrentTable().get(tableName);
				table = (Hashtable) tableList.lastElement();
			}
			tables.add( table );
			//LogUtility.log("===" + getCurrentTable() + " and " + table);
			this.getCurrentTable().put("TableName", tableName );
			//LogUtility.log(">>===> " + this.getPreviousTable().get("TableName") + " && " + this.getCurrentTable().get("TableName") );
		}
		
		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
		 */
		public void characters(char[] cbuf, int start, int len)
			throws SAXException
		{
			String value = new String(cbuf, start,len).trim();
			if ( fieldName != null || ! value.equals("") )
			{
				currentValue = currentValue + value;
				//LogUtility.log(fieldName + ": " + value);
			}
		}


		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
		 */
		public void endPrefixMapping(String arg0) throws SAXException
		{
			// Do Nothing

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
		 */
		public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException
		{
			// Do Nothing
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
		 */
		public void processingInstruction(String arg0, String arg1)
			throws SAXException
		{
			// Do Nothing
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
		 */
		public void setDocumentLocator(Locator arg0)
		{
			// Do Nothing

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
		 */
		public void skippedEntity(String arg0) throws SAXException
		{
			// Do Nothing

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startDocument()
		 */
		public void startDocument() throws SAXException
		{
			// Do Nothing

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
		 */
		public void startPrefixMapping(String arg0, String arg1)
			throws SAXException
		{
			// Do Nothing

		}

	}
	
	public class SAXValidationErrorHandler implements ErrorHandler
	{
		private LoadingErrors errors = null;
		private boolean valid = true;
		
		public SAXValidationErrorHandler(LoadingErrors errors)
		{
			this.errors = errors;
		}
		
		/**
		 * @return
		 */
		public boolean isValid()
		{
			return valid;
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
		 */
		public void error(SAXParseException arg0) throws SAXException
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Error: '" + arg0.getMessage() + "'\n");
			sb.append("\tLine: " + arg0.getLineNumber() + "\n");
			sb.append("\tColumn : " + arg0.getColumnNumber() + "\n");
			errors.AddError(LoadingErrors.VALIDATIONERROR, sb.toString() );
			valid = false;
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
		 */
		public void fatalError(SAXParseException arg0) throws SAXException
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Fatal Error: '" + arg0.getMessage() + "'\n");
			sb.append("\tLine: " + arg0.getLineNumber() + "\n");
			sb.append("\tColumn : " + arg0.getColumnNumber() + "\n");
			errors.AddError(LoadingErrors.VALIDATIONERROR, sb.toString() );
			arg0.printStackTrace();
			valid = false;
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
		 */
		public void warning(SAXParseException arg0) throws SAXException
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Warning: '" + arg0.getMessage() + "'\n");
			sb.append("\tLine: " + arg0.getLineNumber() + "\n");
			sb.append("\tColumn : " + arg0.getColumnNumber() + "\n");
			errors.AddError(LoadingErrors.VALIDATIONERROR, sb.toString() );
		}
	}
	
	/**
	 * 
	 * @author farrell
	 *
	 * Takes a Hashtable representation of the XML to be loaded 
	 * and writes it to the database.
	 */
	public class LoadTreeToDatabase
	{

		private DBConnection dbConn = null; 
		private Hashtable revisionsHash = null;
		private Hashtable noteLinksHash = null;
		private Hashtable vegbankPackage = null;
		private LoadingErrors errors = null;
		private boolean commit = false;
		private AccessionGen ag  = new AccessionGen();
		
		// This holds the name of the current concept
		private String currentConceptName = null;
		
		public LoadTreeToDatabase(LoadingErrors errors)
		{
			this.errors = errors;
		}
		
		/**
		 * Inserts the entire dataset into the database
		 * 
		 * @param vegbankPackage -- the root of the dataset 
		 */
		public void insertVegbankPackage(Hashtable vegbankPackage)
			throws SQLException
		{
			this.vegbankPackage = vegbankPackage;
			//Utility.prettyPrintHash(vegbankPackage);

			//this boolean determines if the dataset should be commited or rolled-back
			commit = true;
			this.initDB();

			// insert commConcepts
			Enumeration commConcepts =  getChildTables(vegbankPackage, "commConcept");
			while ( commConcepts.hasMoreElements() )
			{
				Hashtable commConcept = (Hashtable) commConcepts.nextElement();
				insertCommConcept(commConcept);
			}			

			// insert plantConcepts
			Enumeration plantConcepts =  getChildTables(vegbankPackage, "plantConcept");
			while ( plantConcepts.hasMoreElements() )
			{
				Hashtable plantConcept = (Hashtable) plantConcepts.nextElement();
				insertPlantConcept(plantConcept);
			}

			// insert references
			Enumeration references =
				getChildTables(vegbankPackage, "reference");
			while (references.hasMoreElements())
			{
				Hashtable reference = (Hashtable) references.nextElement();
				this.insertReference(reference);
			}

			// insert Parties
			Enumeration parties = getChildTables(vegbankPackage, "party");
			while (parties.hasMoreElements())
			{
				Hashtable party = (Hashtable) parties.nextElement();
				this.insertParty(party);
			}

			// insert plots
			Enumeration plots = getChildTables(vegbankPackage, "plot");
			while (plots.hasMoreElements())
			{
				Hashtable plot = (Hashtable) plots.nextElement();
				insertPlot(plot);
			}

			LogUtility.log(
				"LoadTreeToDatabase:  insertion success: " + commit);
			if (commit == true)
			{
				dbConn.commit();
			}
			else
			{
				dbConn.rollback();
			}

			LogUtility.log("LoadTreeToDatabase: Returning the DBConnection to the pool");
			//Return dbconnection too pool
			DBConnectionPool.returnDBConnection(
				dbConn,
				dbConn.getCheckOutSerialNumber());
		}

		private void initDB() throws SQLException
		{
			//	Get DBConnection
			dbConn=DBConnectionPool.getInstance().getDBConnection("Need connection for inserting dataset");
			dbConn.setAutoCommit(false);
		}
		
		/**
		 * Get the PK of the row in the database that has the same values as record
		 * 
		 * @param tableName
		 * @param fieldValueHash
		 * @return long -- PK of the table
		 */
		private long getTablePK( String tableName, Hashtable fieldValueHash )
		{
			Vector fieldEqualsValue = new Vector();
			StringBuffer sb = new StringBuffer();
			long PK = 0;
			try 
			{
				Enumeration fields = fieldValueHash.keys();
				while ( fields.hasMoreElements())
				{
					String field = (String) fields.nextElement();
					Object value = fieldValueHash.get(field);
					if ( this.isDatabaseField(field, value, tableName) )
					{				
						fieldEqualsValue.add(field + "=" +"'" + Utility.encodeForDB(value.toString()) + "'");
					}
				}
				
				sb.append(
					"SELECT " + getPKName(tableName) +" from "+tableName+" where " 
					+ Utility.joinArray(fieldEqualsValue.toArray(), " and ")
				);
				
				sb.append(this.getSQLNullValues(tableName, fieldValueHash));
				
				Statement query = dbConn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				while (rs.next()) 
				{
					PK = rs.getInt(1);
				}
			}
			catch ( SQLException se ) 
			{      
				LogUtility.log("LoadTreeToDatabase: Caught SQL Exception: " + se.getMessage());
				LogUtility.log("LoadTreeToDatabase:  SQL : " + sb.toString() );
				se.printStackTrace();
				commit = false;
				errors.AddError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
			}
			//LogUtility.log("LoadTreeToDatabase:  Query: " + sb.toString());
			return PK;
		}
		
		private boolean isDatabaseField(String field, Object value, String tableName)
		{
			boolean result = true;
			
			if (!(value instanceof String)
				|| field.equals("TableName")
				|| field.equals("REVISION_ID")
				|| (field.equals("NOTELINK_ID") && ! tableName.equalsIgnoreCase("note"))
				|| field.equalsIgnoreCase(tableName + "_ID"))
			{
				result = false;
			}

			return result;
		}
		
		/**
		 * Get the next value from the sequence of the PK of this table
		 * 
		 * @param tableName
		 * @return long -- the next availible PK for this table
		 */
		private long getNextId(String tableName) 
		{
			long PK = -1;
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT nextval('" + this.getPKName(tableName) + "_seq')");
			try 
			{
			   Statement query = dbConn.createStatement();
			   ResultSet rs = query.executeQuery(sb.toString());
			   while (rs.next()) 
			   {
					   PK = rs.getLong(1);
			   }
			}
			catch (SQLException se)
			{
				LogUtility.log("LoadTreeToDatabase: Caught SQL Exception: " + se.getMessage());
				if ( !se.getMessage().equals("No results were returned by the query.") )
				{
					LogUtility.log("LoadTreeToDatabase: sql: " + sb.toString());
					se.printStackTrace();
				}
				commit = false;
				errors.AddError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
			}
			 return (PK);
		}
		
		/**
		 * Normally with vegbank the PK name is tableName_id
		 * There are some exceptions that are handled here.
		 * 
		 * @param tableName
		 * @return the name of the PK for this table
		 */
		private String getPKName(String tableName)
		{
			String pKName = "";
			if ( tableName.equalsIgnoreCase("aux_role"))
			{
				pKName = Aux_role.PKNAME;
			}
			else
			{
				pKName = tableName.toUpperCase() + "_ID";
			}
			return pKName;
		}

		/**
		 * 
		 * @param tableName
		 * @param Enumeration 
		 * @param FKName
		 * @param FKValue
		 * @return boolean -- success or not
		 */
		private boolean insertTables( String tableName, Enumeration tables, String fKName, long fKValue ) throws SQLException
		{
			boolean result = true;
			
			while( tables.hasMoreElements()  )
			{
				Hashtable table = (Hashtable) tables.nextElement();
				//LogUtility.log("LoadTreeToDatabase: " + tableName + " : " + table);
				addForeignKey(table, fKName, fKValue);
				insertTable(tableName, table);
			}
			
			return result;
		}

		/**
		 * Insert this record into the database, check if it has been add before first
		 * 
		 * Check if there exists a reference for this table and add it first.
		 * Check if there exists a note for each field and add it last.
		 * Check and insert userdefined table last.
		 * 
		 * Returns 0 if null is passed in.
		 * 
		 * @param tableName
		 * @param fieldValueHash
		 * @return long -- PK of the table
		 */
		private long insertTable( String tableName, Hashtable fieldValueHash ) throws SQLException
		{	
			//LogUtility.log("LoadTreeToDatabase : insert: " + tableName);
			
			long PK = 0;

			// Handle null being passed or empty
			if ( fieldValueHash == null )
			{
				return PK;
			}
			
			// Insert Reference if it exists
			long referenceId = 0;
			Hashtable reference = getFKChildTable(fieldValueHash, "reference_ID", "reference");
			if ( reference != null)
			{
				referenceId = insertReference(reference);
			}
			addForeignKey(fieldValueHash, "reference_ID", referenceId);
			
			// Check if this already exists in the database
			if ( this.tableExists( tableName, fieldValueHash) )
			{
				return this.getTablePK(tableName, fieldValueHash);
			}
			
			//LogUtility.log("LoadTreeToDatabase: INSERT: " + tableName);
			PK = this.getNextId(tableName);
			
			// Some tables have accessionCodes that need contructing
			this.addAccessionCode(fieldValueHash, tableName, PK);
			
			Vector fieldNames = new Vector();
			Vector fieldValues = new Vector();
			StringBuffer sb = new StringBuffer();
			try 
			{
				Enumeration fields = fieldValueHash.keys();
				while ( fields.hasMoreElements())
				{		
					String field = (String) fields.nextElement();
					Object value = fieldValueHash.get(field);
					if ( this.isDatabaseField(field, value, tableName) )
					{				
						fieldNames.add(field);
						fieldValues.add("'" + Utility.encodeForDB(value.toString()) + "'");
					}
				}
				
				fieldNames.add( getPKName(tableName) );
				fieldValues.add("" + PK);

				sb.append("INSERT INTO " + tableName );
				sb.append(" (" + Utility.arrayToCommaSeparatedString( fieldNames.toArray() ) + ")" );
				sb.append(" VALUES (" + Utility.arrayToCommaSeparatedString( fieldValues.toArray() ) +")" );
			
				LogUtility.log("LoadTreeToDatabase: Query: " + sb);
				 
				Statement query = dbConn.createStatement();
				int rowCount = query.executeUpdate(sb.toString());
			}
			catch ( SQLException se ) 
			{      
				LogUtility.log("LoadTreeToDatabase: Caught SQL Exception: " + se.getMessage());
				se.printStackTrace();
				commit = false;
				errors.AddError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
			}
			
			// All the tables can have defined value field
			insertUserDefinedTables(tableName, PK, fieldValueHash);
			// All fields can have a revision
			insertRevision(PK, fieldValueHash);
			// All fields can have a note attached
			insertNoteLink(PK, fieldValueHash);
			
			return PK;
		}

		/**
		 * Add an accessionCode to the tables that need it
		 * 
		 * @param fieldValueHash
		 * @param tableName
		 * @param PK
		 */
		private void addAccessionCode(Hashtable fieldValueHash, String tableName, long PK) throws SQLException
		{	
			String fieldName = Utility.getAccessionCodeAttributeName(tableName);
			String accessionCode = "";
			
			//LogUtility.log("LoadTreeToDatabase: " + fieldName + " and " + Utility.isLoadAccessionCodeOn());
			if (fieldName != null && Utility.isLoadAccessionCodeOn() )
			{
				if (tableName.equalsIgnoreCase("Plantconcept") 
					|| tableName.equalsIgnoreCase("Commconcept"))
				{
					// Use the AccessionGen for these tables
					LogUtility.log("LoadTreeToDatabase: Calling AccessionGen THIS IS INCOMPLETE!!"); 
					// FIXME: Commented out active code
					accessionCode =
						ag.getAccession(
							Utility.getAccessionPrefix(),
							tableName,
							new Long(PK).toString());
					//	ag.getCode(
					//		Utility.getAccessionPrefix(),
					//		tableName,
					//		new Long(PK).toString(),
					//		this.currentConceptName);
							
					fieldValueHash.put(fieldName, accessionCode);
				}
				else
				{
					accessionCode = Utility.getAccessionPrefix() + "." + PK;
					fieldValueHash.put(fieldName, accessionCode);
				}


				LogUtility.log(
					"LoadTreeToDatabase: Adding "
						+ fieldName
						+ " = "
						+ accessionCode
						+ " to "
						+ tableName);
			}
			else
			{
				LogUtility.log("LoadTreeToDatabase: Not adding accessionCode for " + tableName);
				// do nothing
			}	
		}
		
		/**
		 * Get the accessionCode from the XML
		 * 
		 * @param fieldValueHash
		 * @param tableName
		 * @param PK
		 */
		private long getPKFromAccessionCode(Hashtable fieldValueHash, String tableName)
		{		
			long PK = 0;
			String accessionCode = "";
			
			String fieldName = Utility.getAccessionCodeAttributeName(tableName);
			
			if ( fieldName != null )
			{
				accessionCode = (String) fieldValueHash.get(fieldName);
			}
			
			if ( Utility.isStringNullOrEmpty( accessionCode ))
			{
				LogUtility.log("LoadTreeToDatabase: Found no accessionCode for " + tableName);
				// do nothing
			}
			else if ( accessionCode.startsWith(Utility.getAccessionPrefix() + ".") )
			{
				// Need to get the pK of the table
				Hashtable simpleHash = new Hashtable();
				simpleHash.put(fieldName, accessionCode);
				PK = this.getTablePK(tableName, simpleHash);
				
				if ( PK != 0 )
				{
					// great got a real PK
					LogUtility.log(
						"LoadTreeToDatabase: Found PK ("
							+ PK
							+ ") for "
							+ tableName
							+ " accessionCode: "
							+ accessionCode);
				}
				else
				{
					// Problem no accessionCode like that in database -- fail load
					String errorMessage =
						"There is no "
							+ tableName
							+ " with a "
							+ fieldName
							+ " of value '"
							+ accessionCode
							+ "' in the database.";
							
					LogUtility.log("LoadTreeToDatabase: Loading Failed: " + errorMessage);
					commit = false;
					errors.AddError(
						LoadingErrors.DATABASELOADINGERROR,
						errorMessage);
				}
			}
			else
			{
				LogUtility.log(
					"LoadTreeToDatabase: Got an alien "
						+ fieldName
						+ " in table "
						+ tableName
						+ " --> "
						+ accessionCode);
						
				// Remove from hash
				fieldValueHash.remove(fieldName);
			}
			
			return PK;
		}

		/**
		 * @param FK
		 * @param fieldValueHash
		 */
		private void insertNoteLink(long FK, Hashtable fieldValueHash) throws SQLException
		{
			// Does this table have a noteLink
			Enumeration noteLinks =  getChildTables(fieldValueHash, "noteLinkList");
			while ( noteLinks.hasMoreElements() )
			{
				Hashtable noteLink = (Hashtable) noteLinks.nextElement();
				
				// Find the noteLink that matchs this
				String noteLinkId =  (String) noteLink.get("NOTELINK_ID");	
				
				if (noteLinksHash == null)
				{
					noteLinksHash = new Hashtable();
					Enumeration allNoteLinks =  getChildTables(vegbankPackage, "noteLink");
					{
						Hashtable aNoteLink = (Hashtable) allNoteLinks.nextElement();
						noteLinksHash.put(aNoteLink.get("NOTELINK_ID"), aNoteLink);
					}
				}
				
				Hashtable noteLinkDescription = (Hashtable)noteLinksHash.get(noteLinkId);
				this.addForeignKey(noteLinkDescription, "tableRecord", FK);
				
				
				// Sanity check the tablename and attribute name are equal
				if ( ! isFieldValueEqual(noteLinkDescription, noteLink, "tableName") 
					|| ! isFieldValueEqual(noteLinkDescription, noteLink, "attributeName"))
				{
					// Housten we have a problem
					errors.AddError(LoadingErrors.VALIDATIONERROR, "Mismatch on NOTELINK_ID = " + noteLinkId);
					commit = false;
				}
				else
				{
					long noteLinkPK = insertTable("noteLink", noteLinkDescription);
					// Add note tables
					Enumeration notes =  getChildTables(noteLinkDescription, "note");
					while ( notes.hasMoreElements() )
					{
						Hashtable note = (Hashtable) notes.nextElement();
						// This is basically a contributor table .. 
						insertContributor("note", note, "NOTELINK_ID", noteLinkPK);	
					}
				}
			}

		}

		/**
		 * @param FK
		 * @param fieldValueHash
		 */
		private void insertRevision(long FK, Hashtable fieldValueHash) throws SQLException
		{
			// Does this table have a revision
			Enumeration revisions =  getChildTables(fieldValueHash, "revisionList");
			while ( revisions.hasMoreElements() )
			{
				Hashtable revision = (Hashtable) revisions.nextElement();
				
				// Find the revision that matchs this
				String revisionId =  (String) revision.get("REVISION_ID");	
				
				if (revisionsHash == null)
				{
					revisionsHash = new Hashtable();
					Enumeration allRevisions =  getChildTables(vegbankPackage, "revision");
					{
						Hashtable aRevision = (Hashtable) allRevisions.nextElement();
						revisionsHash.put(aRevision.get("REVISION_ID"), aRevision);
					}
				}

				Hashtable revisionDescription = (Hashtable)revisionsHash.get(revisionId);
				this.addForeignKey(revisionDescription, "tableRecord", FK);
				
				
				// Sanity check the tablename and attribute name are equal
				if ( ! isFieldValueEqual(revisionDescription, revision, "tableName")
					|| ! isFieldValueEqual(revisionDescription, revision, "tableAttribute"))
				{
					// Housten we have a problem
					errors.AddError(LoadingErrors.VALIDATIONERROR, "Mismatch on REVISION_ID = " + revisionId);
					commit = false;
				}
				else
				{
					insertTable("revision", revisionDescription);
				}
			}
			
		}
		
		private boolean isFieldValueEqual(Hashtable a, Hashtable b, String fieldName)
		{
			boolean result = false;
			
			if ( ((String) a.get(fieldName)).equalsIgnoreCase( ((String) b.get(fieldName) ) ) )
			{
				result = true;
			}
			
			return result;
		}

		/**
		 * Convience method to add a reference and it child tables
		 * 
		 **/
		private long insertReference( Hashtable reference) throws SQLException
		{
			long referenceId = 0;
			// Handle null
			if ( reference == null )
			{
				return referenceId;
			}
			
			referenceId = this.getPKFromAccessionCode(reference, "reference"); 
			if ( referenceId != 0)
			{
				return referenceId;
			}
			
			// Insert Reference Journal
			Hashtable referenceJournal = getFKChildTable(reference, Reference.REFERENCEJOURNAL_ID, "referenceJournal");
			long referenceJournalId = insertTable("referenceJournal", referenceJournal);

			addForeignKey(reference, Reference.REFERENCEJOURNAL_ID, referenceJournalId);
			referenceId = insertTable("reference", reference);
			
			// Insert referenceAltIdent
			Enumeration referenceAltIdents =  getChildTables(reference, "referenceAltIdent");
			while ( referenceAltIdents.hasMoreElements() )
			{
				Hashtable referenceAltIdent = (Hashtable) referenceAltIdents.nextElement();
				addForeignKey(referenceAltIdent, Referencealtident.REFERENCE_ID, referenceId);
				long referenceAltIdentId = insertTable("referenceAltIdent", referenceAltIdent);
			}
			
			// Insert referenceContributor
			Enumeration referenceContributors =  getChildTables(reference, "referenceContributor");
			while ( referenceContributors.hasMoreElements() )
			{
				Hashtable referenceContributor = (Hashtable) referenceContributors.nextElement();
				
				Hashtable referenceParty = getFKChildTable(referenceContributor, Referencecontributor.REFERENCEPARTY_ID, "referenceParty");
				// Add  referenceParty
				long referencePartyId = insertReferenceParty(referenceParty);
				
				addForeignKey(referenceContributor, Referencecontributor.REFERENCE_ID, referenceId);
				addForeignKey(referenceContributor, Referencecontributor.REFERENCEPARTY_ID, referencePartyId);
				long referenceContributorId = insertTable("referenceContributor", referenceContributor);
			}		
			
			return referenceId;
		}
		
		/**
		 * @param referenceParty
		 * @return
		 */
		private long insertReferenceParty(Hashtable referenceParty) throws SQLException
		{
			long referencePartyId = 0;
			if (referenceParty != null)
			{
				Hashtable referencePartyParent = getFKChildTable(referenceParty, Referenceparty.CURRENTPARTY_ID, "referenceParty");
				long referencePartyParentId = insertReferenceParty(referencePartyParent);
				
				addForeignKey(referenceParty, Referenceparty.CURRENTPARTY_ID, referencePartyParentId);
				referencePartyId = insertTable( "referenceParty", referenceParty);
			}
			return referencePartyId;
		}

		private boolean insertUserDefinedTables( String parentTableName, long tableRecordID, Hashtable parentHash) throws SQLException
		{
			boolean result = false;
			
			Enumeration userdefineds = getChildTables( parentHash, "userDefined");
			while ( userdefineds.hasMoreElements() )
			{
				Hashtable userDefined = (Hashtable) userdefineds.nextElement();
				// Use the parentTableName as the tableName value
				userDefined.put( "tableName", parentTableName);
				long userDefinedPK = insertTable( "userDefined", userDefined );
				
				// Get the defined value
				Hashtable definedValue = getChildTable( userDefined, "definedValue");
				definedValue.put("tablerecord_id", ""+tableRecordID);
				definedValue.put("userdefined_id", ""+userDefinedPK);
				insertTable( "definedValue", definedValue );
			}
			return result;
		}
		
		/**
		 * method that returns true if the table with these values exists in the 
		 * database
		 */ 
		private boolean tableExists( String tableName, Hashtable fieldValueHash)
		{
			int rows = 0;
			
			Vector fieldEqualsValue = new Vector();
			StringBuffer sb = new StringBuffer();
			
			try 
			{		
				Enumeration fields = fieldValueHash.keys();
				while ( fields.hasMoreElements())
				{					
					String field = (String) fields.nextElement();
					//LogUtility.log("===" + field);
					Object value = fieldValueHash.get(field);
					if ( this.isDatabaseField(field, value, tableName) )
					{				
						fieldEqualsValue.add(field + "=" + "'" + Utility.encodeForDB(value.toString()) + "'");
					}
				}
				
				// TODO: All fields that have no value should be placed in the search 
				
				sb.append(
					"SELECT count(*) from "+tableName+" where " 
					+ Utility.joinArray(fieldEqualsValue.toArray(), " and ")
				);

				sb.append(this.getSQLNullValues(tableName, fieldValueHash));
				
				Statement query = dbConn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				while (rs.next()) 
				{
					rows = rs.getInt(1);
				}
			}
			catch ( SQLException se ) 
			{      
				LogUtility.log("Caught SQL Exception: " + se.getMessage());
				if ( !se.getMessage().equals("No results were returned by the query.") )
				{
					LogUtility.log("Query: " + sb.toString() );
					se.printStackTrace();
					commit = false;
					errors.AddError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
				}
			}
				
			if (rows == 0) 
			{
				LogUtility.log("LoadTreeToDatabase : "+tableName+" does not exist");
				return (false);
			} 
			else 
			{
				LogUtility.log("LoadTreeToDatabase : "+tableName+" does exist");
				return (true);
			}
		}
		
		
		private String getSQLNullValues( String tableName, Hashtable fieldValuesHash ) throws SQLException
		{
			StringBuffer sb = new StringBuffer();
			
			ResultSet rs = dbConn.getMetaData().getColumns(null, null, tableName.toLowerCase(), "%" );
			
			//LogUtility.log("LoadTreeToDatabase : Got a Result Set");
			while ( rs.next() )
			{
				// according to api value 4 is the columnName
				String columnName = rs.getString(4);
				//LogUtility.log("LoadTreeToDatabase : ColumnName> " + columnName + " in table > " + tableName);
				
				boolean foundValueForColumn = false;
				Enumeration fields = fieldValuesHash.keys();
				while ( fields.hasMoreElements())
				{
					String fieldName = (String) fields.nextElement();
					
					// Compare with the currentColumnName
					if ( fieldName.equalsIgnoreCase(columnName))
					{
						foundValueForColumn = true;
					}
				}
				if ( ! foundValueForColumn )
				{
					// Need to add a null here
					sb.append(" and "  + columnName + " = NULL" );
				}
			}
			
			return sb.toString();
		}
		
		/**
		 * Convience method to get the a child TableHash that has is a FK
		 *  
		 * @param tableName
		 */
		private Hashtable getFKChildTable( Hashtable parentHash, String keyName ,String tableName)
		{
			Hashtable childHash = null;
			
			Hashtable keyHash =  this.getChildTable(parentHash, keyName);
			if ( keyHash != null )
			{
				childHash = this.getChildTable(keyHash, tableName);
			}
			return childHash;
		}
		
		/**
		 * Convience method to get the a child TableHash
		 * 
		 * @param tableName
		 */
		private Hashtable getChildTable( Hashtable parentHash, String tableName)
		{
			Hashtable childHash = null;
			if ( parentHash == null )
			{
				LogUtility.log("Got asked to find: '" + tableName+ "' from a null Hashtable. This shouldn't happen but hey, what do I know?");
				return childHash;

				//Utility.prettyPrintHash(parentHash);
			}
			
			//Utility.prettyPrintHash(parentHash);

			Object o = parentHash.get(tableName);
			if ( o instanceof java.util.Vector )
			{
				childHash = (Hashtable) ((Vector) o).elementAt(0);
			} 
			else  if (  o instanceof java.util.Hashtable  )
			{
				childHash = (Hashtable) o;
			}
			else if ( o == null)
			{
				LogUtility.log("LoadTreeToDatabase: Could not find table.. " + tableName + " as child of  table " + parentHash.get("TableName"));
			}
			else
			{
				// Don't know what to do here
				LogUtility.log("LoadTreeToDatabase: Type: '" + o.getClass() + "' should not exist here in" + tableName + "?." );
			}
			return childHash;
		}
		
		/**
		 * Convience method to get the a child TableHashes as a vector
		 * 
		 * @param tableName
		 * @return Vector -- List of Hashtables for each child table
		 */
		private Enumeration getChildTables( Hashtable parentHash, String tableName)
		{
			Vector childVec  = new Vector();
			if ( parentHash == null )
			{
				// childVec empty	
			}
			else 
			{
				Object childObject = parentHash.get(tableName);
				if ( childObject == null)
				{
					childObject  = new Vector();
				}
				// Need to handle potential null before casting
				childVec = (Vector) childObject;
			}

			Enumeration enum = childVec.elements();
			return enum;
		}
		
		/**
		 * Insert a party
		 * 
		 * @param party
		 * @return
		 */
		private long insertParty( Hashtable party) throws SQLException
		{
			//LogUtility.log("### " +party);
			
			long pKey = 0;
			// Handle null
			if ( party == null )
			{
				return pKey;			
			}
			// Is there an accessionCode
			pKey = this.getPKFromAccessionCode(party, "party"); 
			if ( pKey != 0)
			{
				return pKey;
			}
			
			pKey = insertPartyBase(party, "party");
			
			// Insert Tables that depend on this PK
			Enumeration telephones =getChildTables( party, "telephone");
			insertTables("telephone", telephones, getPKName("party") ,pKey );
			Enumeration addresses = getChildTables( party, "address");
			insertTables("address", addresses, getPKName("party") ,pKey );	
			
			return pKey;
		}

		/**
		 * Party, PlantParty and CommParty are very similar.
		 * This method does shared functions need for insertion.
		 * 
		 * @param party
		 * @return
		 */
		private long insertPartyBase(Hashtable party, String tableName) throws SQLException
		{
			long pKey = 0;
			
			// recursivly deal with FK party's
			Hashtable ownerParty = getFKChildTable(party, "owner_ID", "party");
			Hashtable currentNameParty = getFKChildTable(party, "currentName_ID", "party");
			
			long ownerPartyPK = 0;
			long currentNamePartyPK = 0;
			
			if (ownerParty != null)
			{
				//LogUtility.log(">>>>" + ownerParty);			
				ownerPartyPK = insertParty(ownerParty);
			}
			
			if ( currentNameParty != null)
			{
				currentNamePartyPK = insertParty( currentNameParty);
			}
			
			addForeignKey(party, Party.CURRENTNAME_ID, currentNamePartyPK);
			
			pKey = insertTable(tableName, party);
							
			return pKey;
		}
		
		private long insertContributor(String tableName, Hashtable contribHash, String keyName, long keyValue) throws SQLException
		{
			long pKey = 0;
			// Handle null
			if ( contribHash == null )
			{
				return pKey;			
			}
			
//			System.out.println("##############");
//			Utility.prettyPrintHash(contribHash);
//			System.out.println("##############");
						
			// Get the party ( required )
			Hashtable party = this.getFKChildTable(contribHash, "PARTY_ID", "party");
			long partyId = insertParty(party);
							
			// Get the Role  ( not required )
			Hashtable roleIDHash = (Hashtable) this.getChildTable(contribHash, "ROLE_ID");
			if ( roleIDHash != null )
			{
				Hashtable role = this.getChildTable( roleIDHash, "aux_Role" );
				long roleId = insertTable("aux_Role", role);
				addForeignKey(contribHash, "role_id", roleId);
			}
							
			addForeignKey(contribHash, keyName, keyValue);
			addForeignKey(contribHash, "party_id", partyId);
			insertTable(tableName, contribHash);
			
			return pKey;
		}
		
		/**
		 * method to initiate the insertion of a plot
		 *
		 * @param Hashtable -- Hashtable representing plot to load
		 */
		private long insertPlot( Hashtable plotHash) throws SQLException
		{		
			long plotId = 0;

			// Insert the parent plot if it exists
			Hashtable parentPlot =  this.getChildTable(plotHash, Plot.PARENT_ID);
			
			long parentPlotId = 0;
			if (parentPlot != null)
			{
				parentPlotId = insertPlot(parentPlot);
			}
			addForeignKey(plotHash,  Plot.PARENT_ID, parentPlotId);
			
			// Insert this plot
			plotId = insertTable("plot", plotHash);
			
			Enumeration observations = this.getChildTables(plotHash, "observation");
			
			while ( observations.hasMoreElements() )
			{
				Hashtable observationHash = (Hashtable) observations.nextElement();
					
				Hashtable projectIdFieldValueHash =  this.getChildTable(observationHash, Observation.PROJECT_ID);
				Hashtable projectFieldValueHash = this.getChildTable(projectIdFieldValueHash, "project");
				long projectId=0;

				// SEE IF THE PROJECT IN WHICH THIS PLOT IS A MEMBER EXISTS IN THE DATABASE
				if (projectFieldValueHash == null)
				{
					// This is  a hack allow loading of a plot without project info
				}
				else if (tableExists("project", projectFieldValueHash) == true)
				{
					projectId = getTablePK("project", projectFieldValueHash);
				}
				// IF THE PROJECT IS NOT THERE THEN LOAD IT AND THE PROJECT CONT. INFO
				else
				{
					projectId = insertTable("project", projectFieldValueHash);
					
					Enumeration pcs = this.getChildTables( projectFieldValueHash, "projectContributor");
					while (pcs.hasMoreElements())
					{	
						this.insertContributor( "projectContributor", (Hashtable) pcs.nextElement(), Projectcontributor.PROJECT_ID,  projectId);
					}	
				}
				
				// Get and insert NamedPlace and place
				Enumeration places = this.getChildTables(plotHash, "place");
				while( places.hasMoreElements()  )
				{
					Hashtable place = (Hashtable) places.nextElement();
					addForeignKey(place, Place.PLOT_ID, plotId);

					// Insert NamedPlace
					Hashtable namedPlace =   this.getFKChildTable(place, Place.NAMEDPLACE_ID, "namedPlace");
					long namedPlaceId = insertTable("namedPlace",namedPlace);
					
					addForeignKey(place, Place.NAMEDPLACE_ID, namedPlaceId);
					long placeId = insertTable("place", place);
				}
				
				long observationId = insertObservation(plotId, projectId, observationHash);
			}
			return plotId;
		}

		/**
		 * Insert a observation into the database.
		 * 
		 * @param plotId
		 * @param projectId
		 * @param observationHash
		 * 
		 * @return long -- observationId
		 */
		private long insertObservation(long plotId, long projectId, Hashtable observationHash) throws SQLException
		{
			long observationId = 0;
			// Handle null
			if ( observationHash == null )
			{
				return observationId;			
			}
			// Check for the obsaccessionnumber
			observationId = this.getPKFromAccessionCode(observationHash, "observation"); 
			if ( observationId != 0)
			{
				return observationId;
			}


			// Continue loading as normal
			
			Hashtable previousObs = getFKChildTable(observationHash, Observation.PREVIOUSOBS_ID, "observation");
			
			long previousObsId = 0;
			if (previousObs != null)
			{
				previousObsId = insertObservation(plotId, projectId, previousObs);
			}
			
			addForeignKey(observationHash, Observation.PREVIOUSOBS_ID, previousObsId);
			addForeignKey(observationHash, Observation.PLOT_ID, plotId);
			addForeignKey(observationHash, Observation.PROJECT_ID, projectId);

			// CoverMethod
			Hashtable coverMethod = getFKChildTable(observationHash, Observation.COVERMETHOD_ID, "coverMethod");
			long coverMethodId = insertTable("coverMethod", coverMethod);
			this.insertTables("coverIndex",  getChildTables(coverMethod, "coverIndex"), Observation.COVERMETHOD_ID, coverMethodId);
			addForeignKey(observationHash, Observation.COVERMETHOD_ID, coverMethodId);

			// StratumMethod			
			Hashtable stratumMethod = getFKChildTable(observationHash, Observation.STRATUMMETHOD_ID, "stratumMethod");
			long stratumMethodId = insertStratumMethod(stratumMethod);
			addForeignKey(observationHash, Observation.STRATUMMETHOD_ID, stratumMethodId);

			// SoilTaxon
			Hashtable soilTaxon = getFKChildTable(observationHash, Observation.SOILTAXON_ID, "soilTaxon");
			long soilTaxonId = insertTable("soilTaxon", soilTaxon);
			addForeignKey(observationHash, Observation.SOILTAXON_ID, soilTaxonId);
			
			
			// Insert the observation
			observationId = insertTable("observation", observationHash);
			
			// TODO: insert graphic, observationSynonym
			//this.insertTables("graphic",  getChildTables(observationHash, "graphic"), "observation_id", observationId);
			
			// Add disturbanceObs
			this.insertTables("disturbanceObs",  getChildTables(observationHash, "disturbanceObs"), Disturbanceobs.OBSERVATION_ID, observationId);
			
			// Add SoilObs
			this.insertTables("soilObs",  getChildTables(observationHash, "soilObs"), Soilobs.OBSERVATION_ID, observationId);
			
			// Add observation contributors
			Enumeration ocs = this.getChildTables( observationHash, "observationContributor");
			while (ocs.hasMoreElements())
			{	
				this.insertContributor( "observationContributor", (Hashtable) ocs.nextElement(), Observationcontributor.OBSERVATION_ID,  observationId);
			}
			
			// Add stratums
			Enumeration stratums = getChildTables(observationHash, "stratum");
			while ( stratums.hasMoreElements())
			{
				Hashtable stratum = (Hashtable) stratums.nextElement();
				long stratumId = insertStratum(stratum, stratumMethodId, observationId);
			}
			
			// Add commClass
			Enumeration commClasses =  getChildTables(observationHash, "commClass");
			while ( commClasses.hasMoreElements() )
			{
				Hashtable commClass = (Hashtable) commClasses.nextElement();
				addForeignKey(commClass, Commclass.OBSERVATION_ID, observationId);
				long commClassId = insertTable("commClass", commClass);
				
				// Add Classification Contributors
				Enumeration ccs = this.getChildTables( observationHash, "classContributor");
				while (ccs.hasMoreElements())
				{	
					this.insertContributor( "classContributor", (Hashtable) ccs.nextElement(), "commclass_id",  commClassId);
				}
				
				// Add communtity interpritation
				Enumeration commIntepretations =  getChildTables(commClass, "commInterpretation");
				while ( commIntepretations.hasMoreElements() )
				{
					Hashtable commIntepretation = (Hashtable) commIntepretations.nextElement();
					addForeignKey(commIntepretation, "COMMCLASS_id", commClassId);
					this.insertCommInterpetation(commIntepretation);
				}
			}
			
			// Insert the taxonObservations
			insertTaxonObservations(
				observationHash,
				observationId,
				stratumMethodId);
				
			
			// TODO: Add the observation synonyms ... assuming that they must have the same plot_id and 
			// project_id as each other ... valid, maybe not always
			Enumeration observationSynonyms =  getChildTables(observationHash, "observationSynonym");
			while ( observationSynonyms.hasMoreElements() )
			{
				Hashtable observationSynonym = (Hashtable) observationSynonyms.nextElement();
				
				// TODO: Get synonymobservation_id, party_id, role_id
				Hashtable observation = getFKChildTable(observationSynonym, Observationsynonym.OBSERVATIONSYNONYM_ID, "observation");
				long synonymObservationId = insertObservation(plotId, projectId, observation);
				
				long partyId = insertParty( getFKChildTable(observationSynonym, Observationsynonym.PARTY_ID, "party") );
				long roleId = 
					insertTable(
						"aux_Role", 
						this.getFKChildTable( observationSynonym, Observationsynonym.ROLE_ID, "aux_Role" )
					);

				addForeignKey(observationSynonym, Observationsynonym.PRIMARYOBSERVATION_ID, observationId);
				addForeignKey(observationSynonym, Observationsynonym.SYNONYMOBSERVATION_ID, synonymObservationId);
				addForeignKey(observationSynonym, Observationsynonym.PARTY_ID, partyId);
				addForeignKey(observationSynonym, Observationsynonym.ROLE_ID, roleId);

				long observationSynonymId = insertTable("observationSynonym", observationSynonym);
				
			}
			
			return observationId;
		}

		private long insertStratumMethod(Hashtable stratumMethod) throws SQLException
		{
			long stratumMethodId = insertTable("stratumMethod", stratumMethod);
			// And the child stratumTypes
			Enumeration stratumTypes =  getChildTables(stratumMethod, "stratumType");
			while ( stratumTypes.hasMoreElements() )
			{
				Hashtable stratumType = (Hashtable) stratumTypes.nextElement();
				addForeignKey(stratumType, Stratumtype.STRATUMMETHOD_ID, stratumMethodId);
				long stratumTypeId = insertTable("stratumType", stratumType);
			}
			
			return stratumMethodId;
		}

		/**
		 * Insert a taxonInterpretation 
		 * 
		 * @param observationHash
		 * @param observationId
		 * @param stratumMethodId
		 */
		private void insertTaxonObservations(
			Hashtable observationHash,
			long observationId,
			long stratumMethodId) throws SQLException
		{	
			// Insert the taxonObservations
			Enumeration taxonObservations = getChildTables(observationHash, "taxonObservation");
			while ( taxonObservations.hasMoreElements())
			{
				Hashtable taxonObservation = (Hashtable) taxonObservations.nextElement();
				
				// Add PlantName 
				Hashtable plantname = getChildTable(taxonObservation, "Plantname");
				long plantNameId = insertTable("Plantname", plantname);
				
				// Add FKs to taxonObservation
				addForeignKey(taxonObservation, Taxonobservation.OBSERVATION_ID, observationId);
			
				
				long taxonObservationId = insertTable("taxonObservation", taxonObservation);
				LogUtility.log("LoadTreeToDatabase: taxonObservationId: " + taxonObservationId );
			
				//  Add StratumComposition
				// FIXME: Add Taxonimportances
//				Enumeration stratumCompositions = getChildTables(taxonObservation, "stratumComposition");
//				while ( stratumCompositions.hasMoreElements())
//				{
//					Hashtable stratumComposition = (Hashtable) stratumCompositions.nextElement();
//					addForeignKey(stratumComposition, Stratumcomposition.TAXONOBSERVATION_ID, taxonObservationId);
//					
//					// Get the stratum_id
//					Hashtable stratum = getFKChildTable(stratumComposition, Stratumcomposition.STRATUM_ID, "stratum");
//					long stratumId = insertStratum(stratum, stratumMethodId, observationId);
//					
//					addForeignKey(stratumComposition, Stratumcomposition.STRATUM_ID, stratumId);
//					insertTable("stratumComposition", stratumComposition );
//				}
				
				// Add  StemCount
				Enumeration stemCounts = getChildTables(taxonObservation, "stemCount");
				while ( stemCounts.hasMoreElements())
				{
					Hashtable stemCount = (Hashtable) stemCounts.nextElement();
					
					long stemCountId = insertTable("stemCount", stemCount);
					
					// Add StemLocation
					Enumeration stemLocations = getChildTables(stemCount, "stemLocation");
					while ( stemLocations.hasMoreElements())
					{
						Hashtable stemLocation = (Hashtable) stemLocations.nextElement();
						addForeignKey(stemLocation, "STEMCOUNT_ID", stemCountId);
						
						long stemLocationId = insertTable("stemLocation", stemLocation);
						
						// Add TaxonInterpretation
						Enumeration taxonInterpretations = getChildTables(taxonObservation, "taxonInterpretation");
						while ( taxonInterpretations.hasMoreElements())
						{
							Hashtable taxonInterpretation = (Hashtable) taxonInterpretations.nextElement();
							insertTaxonInterpretation(
								taxonObservationId,
								stemLocationId, 
								taxonInterpretation);
						}	
					}
				}
				
				// Add TaxonInterpretation
				Enumeration taxonInterpretations = getChildTables(taxonObservation, "taxonInterpretation");
				while ( taxonInterpretations.hasMoreElements())
				{
					Hashtable taxonInterpretation = (Hashtable) taxonInterpretations.nextElement();
					insertTaxonInterpretation(
						taxonObservationId,
						0, // No stemlocation availible
						taxonInterpretation);
				}
			}
		}

		private void insertTaxonInterpretation(
			long taxonObservationId,
			long stemLocationId,
			Hashtable taxonInterpretation) throws SQLException
		{
			// Get the party
			Hashtable party =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.PARTY_ID, "party");
			long partyId = insertParty(party);
			
			// Get the Role
			Hashtable role =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.ROLE_ID, "aux_Role");
			long roleId = insertTable("aux_Role", role);
			
			// Get the PlantConcept
			Hashtable plantConcept =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.PLANTCONCEPT_ID, "plantConcept");
			long plantConceptId = this.insertPlantConcept(plantConcept);
			
			// Get the museum party
			Hashtable museumParty =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.MUSEUM_ID, "party");
			long museumId = insertTable("party", museumParty);
			
			// Get the collector party
			Hashtable collectorParty =  this.getFKChildTable(taxonInterpretation, Taxoninterpretation.COLLECTOR_ID, "party");
			long collectorId = insertTable("party", collectorParty);
			
			
			// Add FKs to taxonObservation
			addForeignKey(taxonInterpretation, Taxoninterpretation.PLANTCONCEPT_ID, plantConceptId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.ROLE_ID, roleId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.PARTY_ID, partyId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.TAXONOBSERVATION_ID, taxonObservationId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.MUSEUM_ID, museumId);
			addForeignKey(taxonInterpretation, Taxoninterpretation.COLLECTOR_ID, collectorId);
			//addForeignKey(taxonInterpretation, "stemLocation_ID", stemLocationId);
			
			long taxonInterpretationId = this.insertTable("taxonInterpretation", taxonInterpretation);
			
			// Add taxonAlt
			Enumeration taxonAlts = getChildTables(taxonInterpretation, "taxonAlt");
			while ( taxonAlts.hasMoreElements())
			{
				Hashtable taxonAlt = (Hashtable) taxonAlts.nextElement();
				addForeignKey(taxonAlt, Taxonalt.TAXONINTERPRETATION_ID, taxonInterpretationId);
				
				Hashtable altPlantConcept = this.getFKChildTable(taxonAlt, Taxonalt.PLANTCONCEPT_ID, "plantConcept");
				long altPlantConceptId = insertPlantConcept(altPlantConcept);
				addForeignKey(taxonAlt, Taxonalt.PLANTCONCEPT_ID, altPlantConceptId);
				
				long taxonAltId = this.insertTable("taxonAlt", taxonAlt);
			}
		}
		
		/**
		 * Insert PlantConcept
		 * 
		 * @param plantConcept
		 * @return long -- the PK of  this row
		 */
		private long insertPlantConcept(Hashtable plantConcept) throws SQLException
		{
			long pKey = 0;
			// Handle null
			if ( plantConcept == null )
			{
				return pKey;			
			}
			
			pKey = this.getPKFromAccessionCode(plantConcept, "plantConcept"); 
			if ( pKey != 0)
			{
				// As expected
				return pKey;
			}
			
			//Utility.prettyPrintHash(plantConcept);
			
			// TODO: depends on PlantName
			Hashtable plantName = this.getFKChildTable(plantConcept, Plantconcept.PLANTNAME_ID, "plantName");
			currentConceptName = (String) plantName.get(Plantname.PLANTNAME);
			long plantNameId = insertTable("plantName", plantName);
			
			addForeignKey(plantConcept, Plantconcept.PLANTNAME_ID, plantNameId);
			pKey = this.insertTable("plantConcept", plantConcept);
			
			// Add PlantStatus 
			Enumeration plantStatuses = getChildTables(plantConcept, "plantStatus");
			while ( plantStatuses.hasMoreElements())
			{
				Hashtable plantStatus = (Hashtable) plantStatuses.nextElement();
				addForeignKey(plantStatus, Plantstatus.PLANTCONCEPT_ID, pKey);
				insertPlantStatus(plantStatus);
			}
			
			// Add plantUsage
			Enumeration plantUsages = getChildTables(plantConcept, "plantUsage");
			while ( plantUsages.hasMoreElements())
			{
				Hashtable plantUsage = (Hashtable) plantUsages.nextElement();
				addForeignKey(plantUsage, Plantusage.PLANTCONCEPT_ID, pKey);
				insertPlantUsage(plantUsage);
			}
			
			return pKey;
		}

		/**
		 * Insert CommConcept
		 * 
		 * @param commConcept
		 * @return long -- the PK of  this row
		 */
		private long insertCommConcept(Hashtable commConcept) throws SQLException
		{
			long pKey = 0;
			// Handle null
			if ( commConcept == null )
			{
				return pKey;			
			}
			
			//Utility.prettyPrintHash(commConcept);
			
			pKey = this.getPKFromAccessionCode(commConcept, "commConcept"); 
			if ( pKey != 0)
			{
				// As expected
				return pKey;
			}
			
			//Utility.prettyPrintHash(commConcept);
			
			//  depends on commName
			Hashtable commName = this.getFKChildTable(commConcept, Commconcept.COMMNAME_ID, "commName");
			currentConceptName = (String) commName.get(Commname.COMMNAME);
			long commNameId = insertTable("commName", commName);
			
			addForeignKey(commConcept, Commconcept.COMMNAME_ID, commNameId);
			pKey = this.insertTable("commConcept", commConcept);
			
			// Add commStatus 
			Enumeration commStatuses = getChildTables(commConcept, "commStatus");
			while ( commStatuses.hasMoreElements())
			{
				Hashtable commStatus = (Hashtable) commStatuses.nextElement();
				addForeignKey(commStatus, Commstatus.COMMCONCEPT_ID, pKey);
				insertCommStatus(commStatus);
			}
			
			// Add commUsage
			Enumeration commUsages = getChildTables(commConcept, "commUsage");
			while ( commUsages.hasMoreElements())
			{
				Hashtable commUsage = (Hashtable) commUsages.nextElement();
				addForeignKey(commUsage, Commusage.COMMCONCEPT_ID, pKey);
				insertCommUsage(commUsage);
			}
			
			return pKey;
		}		
		
		/**
		 * @param commStatus
		 * @return long -- primary key assigned
		 */
		private long insertCommStatus(Hashtable commStatus) throws SQLException
		{
			long pKey =0;
		
			// Handle Nulls
			if ( commStatus == null)
			{
				return pKey;
			}

			//Utility.prettyPrintHash(commStatus);
		
			// Add commParent
			Hashtable commParent = this.getFKChildTable(commStatus, Commstatus.COMMPARENT_ID, "commConcept");
			long commParentId = insertCommConcept(commParent); // recursive
		
			// Add commParty
			Hashtable commParty = this.getFKChildTable(commStatus, Commstatus.PARTY_ID, "party");
			long commPartyId = insertCommParty(commParty);
			
			addForeignKey(commStatus, Commstatus.COMMPARENT_ID, commParentId);
			addForeignKey(commStatus, Commstatus.PARTY_ID, commPartyId);
			
			pKey = this.insertTable("commStatus", commStatus);
			
			// Add commLineage
			Enumeration commLineages = getChildTables(commStatus, "commLineage");
			while ( commLineages.hasMoreElements())
			{
				Hashtable commLineage = (Hashtable) commLineages.nextElement();
				addForeignKey(commLineage, Commlineage.COMMLINEAGE_ID, pKey);
				insertTable( "commLineage", commLineage);
			}
		
			// Add commCorrelation
			Enumeration commCorrelations = getChildTables(commStatus, "commCorrelation");
			while ( commCorrelations.hasMoreElements())
			{
				Hashtable commCorrelation = (Hashtable) commCorrelations.nextElement();

				// TODO:  Add commConcept
				Hashtable commConcept = this.getFKChildTable(commCorrelation, Commcorrelation.COMMCONCEPT_ID, "commConcept");
				long commConceptId = insertCommConcept(commConcept); 

				addForeignKey(commCorrelation, Commcorrelation.COMMSTATUS_ID, pKey);
				addForeignKey(commCorrelation, Commcorrelation.COMMCONCEPT_ID, commConceptId);
							
				insertTable( "commCorrelation", commCorrelation);
			}
			return pKey;
		}
		
		private void insertCommUsage(Hashtable commUsage) throws SQLException
		{
			// Add commName 
			Hashtable commName = this.getFKChildTable(commUsage, Commusage.COMMNAME_ID, "commName");
			long commNameId = insertTable("commName", commName);
			
			// Add commParty
			Hashtable commParty = this.getFKChildTable(commUsage, Commusage.PARTY_ID, "party");
			long commPartyId = insertCommParty(commParty);
			
			addForeignKey(commUsage,  Commusage.COMMNAME_ID, commNameId);
			addForeignKey(commUsage, Commusage.PARTY_ID, commPartyId);
			
			insertTable( "commUsage", commUsage);
		}
		
		/**
		 * @param commParty
		 * @return
		 */
		private long insertCommParty(Hashtable commParty) throws SQLException
		{
			long pKey = 0;
			if ( commParty == null )
			{
				return pKey;
			}
			
			pKey = this.insertPartyBase(commParty, "commParty");
			
			return pKey;
		}

		private void insertPlantUsage(Hashtable plantUsage) throws SQLException
		{
			// Add plantName 
			Hashtable plantName = this.getFKChildTable(plantUsage, Plantusage.PLANTNAME_ID, "plantName");
			long plantNameId = insertTable("plantName", plantName);
			
			// Add Party
			Hashtable party = this.getFKChildTable(plantUsage, Plantusage.PARTY_ID, "party");
			long partyId = insertParty(party);
			
			addForeignKey(plantUsage, Plantusage.PLANTNAME_ID, plantNameId);
			addForeignKey(plantUsage, Plantusage.PARTY_ID, partyId);
			
			insertTable( "plantUsage", plantUsage);
		}
		
		/**
		 * @param plantStatus
		 * @return long -- primary key assigned
		 */
		private long insertPlantStatus(Hashtable plantStatus) throws SQLException
		{
			long pKey =0;
			
			// Handle Nulls
			if ( plantStatus == null)
			{
				return pKey;
			}

			//Utility.prettyPrintHash(plantStatus);
			
			// Add plantParent
			Hashtable plantParent = this.getFKChildTable(plantStatus, Plantstatus.PLANTPARENT_ID, "plantConcept");
			long plantParentId = insertPlantConcept(plantParent); // recursive
			
			// Add PlantParty
			Hashtable party = this.getFKChildTable(plantStatus, Plantstatus.PARTY_ID, "party");
			long partyId = insertParty(party);
				
			addForeignKey(plantStatus, Plantstatus.PLANTPARENT_ID, plantParentId);
			addForeignKey(plantStatus, Plantstatus.PARTY_ID, partyId);
				
			pKey = this.insertTable("plantStatus", plantStatus);
				
			// Add plantLineage
			Enumeration plantLineages = getChildTables(plantStatus, "plantLineage");
			while ( plantLineages.hasMoreElements())
			{
				// FIXME: DANGER -- handle parent/ child correctly
				Hashtable plantLineage = (Hashtable) plantLineages.nextElement();
				addForeignKey(plantLineage, Plantlineage.PARENTPLANTSTATUS_ID, pKey);
				insertTable( "plantLineage", plantLineage);
			}
			
			// Add plantCorrelation
			Enumeration plantCorrelations = getChildTables(plantStatus, "plantCorrelation");
			while ( plantCorrelations.hasMoreElements())
			{
				Hashtable plantCorrelation = (Hashtable) plantCorrelations.nextElement();

				// TODO:  Add PlantConcept
				Hashtable plantConcept = this.getFKChildTable(plantCorrelation, Plantcorrelation.PLANTCONCEPT_ID, "plantConcept");
				long plantConceptId = insertPlantConcept(plantConcept); 

				addForeignKey(plantCorrelation, Plantcorrelation.PLANTSTATUS_ID, pKey);
				addForeignKey(plantCorrelation, Plantcorrelation.PLANTCONCEPT_ID, plantConceptId);
								
				insertTable( "plantCorrelation", plantCorrelation);
			}
			return pKey;
		}

		/**
		 * Convience method to check keyValue for 0 before adding it
		 * @param table
		 * @param FKName
		 * @param FKValue
		 */
		private void addForeignKey( Hashtable table, String FKName, long FKValue)
		{
			if ( FKValue != 0 )
			{
				table.put(FKName, ""+FKValue);
			}
		}

		/**
		 * @param stratum
		 * @param stratumMethodId
		 * @param observationId
		 * @return
		 */
		private long insertStratum(Hashtable stratum, long stratumMethodId, long observationId) throws SQLException
		{
			Hashtable stratumType = getFKChildTable(stratum, Stratum.STRATUMTYPE_ID, "stratumType");
			
			addForeignKey(stratumType, Stratumtype.STRATUMMETHOD_ID, stratumMethodId);
			
			long stratumTypeId = insertTable("stratumType", stratumType);
			
			addForeignKey(stratum, Stratum.OBSERVATION_ID, observationId);
			addForeignKey(stratum, Stratum.STRATUMTYPE_ID, stratumTypeId);
			long stratumId = insertTable("stratum", stratum);
			
			return stratumId;
		}

		/**
		 * @param commIntepretation
		 * @param commClassId
		 */
		private long insertCommInterpetation(Hashtable commIntepretation) throws SQLException
		{			
			long pKey = 0;
			// Handle null
			if ( commIntepretation == null)
			{
				return pKey;
			}

			Hashtable commConcept = this.getFKChildTable(commIntepretation, Comminterpretation.COMMCONCEPT_ID, "commConcept");
			long commConceptId = this.insertCommConcept(commConcept);
			addForeignKey(commIntepretation, Comminterpretation.COMMCONCEPT_ID, commConceptId);
			
			Hashtable reference = this.getFKChildTable(commIntepretation, Comminterpretation.COMMAUTHORITY_ID, "reference");
			long commAuthorityId = this.insertReference(reference);
			addForeignKey(commIntepretation, Comminterpretation.COMMAUTHORITY_ID, commAuthorityId);
			
			return insertTable("commInterpretation", commIntepretation);
		}
	}

	/**
	 * 
	 * @author farrell
	 *
	 * A Holdall for loading errors. Used for creating reports of a loading for end users.
	 */
	public class LoadingErrors
	{
		private Vector retificationErrors = new Vector();
		private Vector validationErrors = new Vector();
		private Vector databaseLoadingErrors = new Vector();
		private String bgColor = "#CCCC99"; // a tan color
		
		private boolean hasErrors = false;
		
		public static final int RECTIFICATIONERROR = 0;
		public static final int VALIDATIONERROR = 1;		
		public static final int DATABASELOADINGERROR = 2;
				
		/**
		 * Add an error message of a type, the types are public availible static ints provided 
		 * by this class
		 * 
		 * @param TYPE
		 * @param message
		 */
		public void  AddError( int TYPE, String message)
		{
			hasErrors = true;
			
			switch(TYPE)
			{
				case RECTIFICATIONERROR:
					retificationErrors.add(message);
					break;
				case VALIDATIONERROR:
					validationErrors.add(message);
					break;
				case DATABASELOADINGERROR:	
					databaseLoadingErrors.add(message);
					break;
				default:
					LogUtility.log("LoadingErrors: Invalid Error type");
			}
		}
		
		public StringBuffer getHTMLReport()
		{
			StringBuffer sb = new StringBuffer();
			sb.append("<table size=\"100%\">");
			// Title
			sb.append( "<tr><td>" + getSummaryMessage() + "</td></tr>");
			
			// some formating
			//sb.append();
			// The subparts
			sb.append( getHeader("Validation Results:") );
			sb.append( "<tr><td>" + this.getValidationReport("<br/>") + "</td></tr>");
			sb.append( getHeader("Retification Results:") );
			sb.append( "<tr><td>" + this.getRectificationReport("<br/>") + "</td></tr>");
			sb.append( getHeader("Database Load Results:") );
			sb.append( "<tr><td>" + this.getLoadReport("<br/>") + "</td></tr>");
			sb.append("</table>");
			return sb;
		}

		public String getSummaryMessage()
		{
			String message = null; 
			// general message  -- loaded or not
			if ( this.validationErrors.isEmpty() && this.databaseLoadingErrors.isEmpty() )
			{
				// no problem
				if ( this.retificationErrors.isEmpty() )
				{
					// no problem at all
					message = "Dataset Loaded into the database with no problems";
				}
				else
				{
					// Retification error
					message = "Dataset Loaded into the database with rectification issues, this dataset will not become visible to all users until you fix these issues.";
				}
			}
			else 
			{
				// Plot failed to load
				message = "Dataset failed to load, see errors below";
			}
			return message;
		}
		
		private String getHeader( String title)
		{
			return "<tr bgcolor=\""+ bgColor +"\"><td>" + title + "</td></tr>";
		}
		
		/**
		 * @return
		 */
		public StringBuffer getLoadReport(String separtor)
		{
			StringBuffer sb = new StringBuffer();
			if (errors.databaseLoadingErrors.size() == 0)
			{
				// no errors found
				sb.append("No errors loading this dataset into the database.");
			}
			else
			{
				Enumeration databaseLoadingErrors = errors.databaseLoadingErrors.elements();
				while ( databaseLoadingErrors.hasMoreElements() )
				{
					sb.append( databaseLoadingErrors.nextElement() +separtor);
				}
			}
			return sb;
		}
 
		/**
		 * @return
		 */
		public StringBuffer getRectificationReport(String separtor)
		{
			StringBuffer sb = new StringBuffer();
			if (errors.retificationErrors.size() == 0)
			{
				// no errors found
				sb.append("No errors rectifing this dataset.");
			}
			else
			{
				Enumeration rectificationErrors = errors.retificationErrors.elements();
				while ( rectificationErrors.hasMoreElements() )
				{
					sb.append( rectificationErrors.nextElement() +separtor);
				}
			}
			return sb;
		}

		/**
		 * @return
		 */
		public StringBuffer getValidationReport(String separtor)
		{
			StringBuffer sb = new StringBuffer();
			if (errors.validationErrors.size() == 0)
			{
				// no errors found
				sb.append("No errors validating this dataset.");
			}
			else
			{
				Enumeration validationErrors = errors.validationErrors.elements();
				while ( validationErrors.hasMoreElements() )
				{
					sb.append( validationErrors.nextElement() +separtor);
				}
			}
			return sb;
		}
	}
}
