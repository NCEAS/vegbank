package org.vegbank.plots.datasource;

/*
 * '$RCSfile: VegbankXMLUpload.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-22 19:31:06 $'
 *	'$Revision: 1.5 $'
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

import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
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


	public void processXMLFile( File pFile ) throws IOException, SAXException
	{
		System.out.println( "Validation on: " + this.validate);
		System.out.println( "Rectification on: " + this.rectify);
		System.out.println( "Database Loading on: " + this.load);
		
		SAXValidationErrorHandler errorHandler = new SAXValidationErrorHandler(errors);
		SAX2DBContentHandler contentHandler = new SAX2DBContentHandler(errors);
		
		if ( validate )
		{
			
			xr.setErrorHandler( errorHandler );
			xr.parse( this.getInputSource(pFile) );
			System.out.println( "This file is valid: " + errorHandler.isValid() );
		}
		
		if ( load && errorHandler.isValid() )
		{
			xr.setContentHandler( contentHandler );
			xr.parse( this.getInputSource(pFile));
		}
		else 
		{
			System.out.println( "No attempt was made to rectify or loading this dataset");
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
			vbUpload.processXMLFile(new File(fileName));
			
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
			//System.out.println("1111 " + fieldName + " >> " + tableNames.lastElement() + "_ID");
			if ( fieldName.endsWith("_ID")  && ! fieldName.equalsIgnoreCase( tableNames.lastElement() + "_ID"))
			{
				//System.out.println("222 " + tableNames.lastElement() + "_ID");
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
			
			//System.out.println("END ... " + localName);
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
				//System.out.println("Finished with field: " + localName);
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
			System.out.println("Finished storing table: " + tableName);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				//System.out.println("TABLE: " + elementName);
			}
			else
			{
				//System.out.println("NOT TABLE: " + elementName);
			}
			return result;
		}

//		private void addFKTable( String fieldName)
//		{
//			System.out.println("|||| " +  fieldName + ": " + this.getCurrentTable() );
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
				//System.out.println( getCurrentTable() + "\n----->" + fieldName );
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
					//System.out.println("Adding a FK table: " + tableName);
					this.getCurrentTable().put(tableName, table);
					//Utility.prettyPrintHash( this.getPreviousTable() );
					//System.out.println("Here we are");
					//Utility.prettyPrintHash( this.getCurrentTable() );
			}
			else
			{
	
				//if ( this.getPreviousTable() != null && this.getPreviousTable().containsKey(tableName))
				if ( this.getCurrentTable() != null && this.getCurrentTable().containsKey(tableName))
				{
					System.out.println(" ... table exists ... adding another : " + tableName );
				
					//vector = (Vector)  this.getPreviousTable().get(tableName);	
					vector = (Vector)  this.getCurrentTable().get(tableName);			
					vector.add( table );		
				}
				else
				{
					vector = new Vector();
					vector.add( new Hashtable() );
				}
				//System.out.println(this.getCurrentTable() + " and " + vector);
				this.getCurrentTable().put(tableName, vector );
				Vector tableList = (Vector) this.getCurrentTable().get(tableName);
				table = (Hashtable) tableList.lastElement();
			}
			tables.add( table );
			//System.out.println("===" + getCurrentTable() + " and " + table);
			this.getCurrentTable().put("TableName", tableName );
			//System.out.println(">>===> " + this.getPreviousTable().get("TableName") + " && " + this.getCurrentTable().get("TableName") );
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
				//System.out.println(fieldName + ": " + value);
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
	
	public class LoadTreeToDatabase
	{

		private DBConnection dbConn = null; 
		private Hashtable revisionsHash = null;
		private Hashtable noteLinksHash = null;
		private Hashtable vegbankPackage = null;
		private LoadingErrors errors = null;
		private boolean commit = false;
		
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

			//TODO:  CommConcept
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

			System.out.println(
				"VegbankXMLUpload > insertion success: " + commit);
			if (commit == true)
			{
				dbConn.commit();
			}
			else
			{
				dbConn.rollback();
			}

			System.out.println("Returning the DBConnection to the pool");
			//Return dbconnection too pool
			DBConnectionPool.returnDBConnection(
				dbConn,
				dbConn.getCheckOutSerialNumber());
		}

		private void initDB() throws SQLException
		{
			//	Get DBConnection
			dbConn=DBConnectionPool.getDBConnection("Need connection for inserting dataset");
			dbConn.setAutoCommit(false);
		}
		
		/**
		 * Get the PK of the row in the database that has the same values as record
		 * 
		 * @param tableName
		 * @param fieldValueHash
		 * @return int -- PK of the table
		 */
		private int getTablePK( String tableName, Hashtable fieldValueHash )
		{
			Vector fieldEqualsValue = new Vector();
			StringBuffer sb = new StringBuffer();
			int PK = 0;
			try 
			{
				Enumeration fields = fieldValueHash.keys();
				while ( fields.hasMoreElements())
				{
					String field = (String) fields.nextElement();
					Object value = fieldValueHash.get(field);
					if ( this.isDatabaseField(field, value, tableName) )
					{				
						fieldEqualsValue.add(field + "=" +"'" + Utility.escapeCharacters(value.toString()) + "'");
					}
				}
				
				sb.append(
					"SELECT " + getPKName(tableName) +" from "+tableName+" where " 
					+ Utility.joinArray(fieldEqualsValue.toArray(), " and ")
				);

				Statement query = dbConn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				while (rs.next()) 
				{
					PK = rs.getInt(1);
				}
			}
			catch ( SQLException se ) 
			{      
				System.out.println("Caught SQL Exception: " + se.getMessage());
				se.printStackTrace();
				commit = false;
				errors.AddError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
			}
			System.out.println(sb.toString());
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
		 * @return
		 */
		private int getNextId(String tableName) 
		{
			int PK = -1;
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT nextval('" + this.getPKName(tableName) + "_seq')");
			try 
			{
			   Statement query = dbConn.createStatement();
			   ResultSet rs = query.executeQuery(sb.toString());
			   while (rs.next()) 
			   {
					   PK = rs.getInt(1);
			   }
			}
			catch (SQLException se)
			{
				System.out.println("Caught SQL Exception: " + se.getMessage());
				if ( !se.getMessage().equals("No results were returned by the query.") )
				{
					System.out.println("sql: " + sb.toString());
					se.printStackTrace();
				}
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
				pKName = "role_id";
			}
			else
			{
				pKName = tableName + "_id";
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
		private boolean insertTables( String tableName, Enumeration tables, String fKName, int fKValue )
		{
			boolean result = true;
			
			while( tables.hasMoreElements()  )
			{
				Hashtable table = (Hashtable) tables.nextElement();
				AddForeignKey(table, fKName, fKValue);
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
		 * @return int -- PK of the table
		 */
		private int insertTable( String tableName, Hashtable fieldValueHash )
		{	
			System.out.println("LoadTreeToDatabase > insert: " + tableName);
			
			int PK = 0;

			// Handle null being passed
			if ( fieldValueHash == null)
			{
				return PK;
			}
			
			// Insert Reference if it exists
			int referenceId = 0;
			Hashtable reference = getFKChildTable(fieldValueHash, "reference_ID", "reference");
			if ( reference != null)
			{
				referenceId = insertReference(reference);
			}
			AddForeignKey(fieldValueHash, "REFERENCE_ID", referenceId);
			
			// Check if this already exists in the database
			// TODO: This is unlikely to be valid ... need per table rules for identicalness
			if ( this.tableExists(tableName, fieldValueHash) )
			{
				return this.getTablePK(tableName, fieldValueHash);
			}
			
			System.out.println("INSERT: " + tableName);
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
						fieldValues.add("'" + Utility.escapeCharacters(value.toString()) + "'");
					}
				}
				
				fieldNames.add( getPKName(tableName) );
				fieldValues.add("" + PK);

				sb.append("INSERT INTO " + tableName );
				sb.append(" (" + Utility.arrayToCommaSeparatedString( fieldNames.toArray() ) + ")" );
				sb.append(" VALUES (" + Utility.arrayToCommaSeparatedString( fieldValues.toArray() ) +")" );
			
				System.out.println("Query: " + sb);
				 
				Statement query = dbConn.createStatement();
				int rowCount = query.executeUpdate(sb.toString());
			}
			catch ( SQLException se ) 
			{      
				System.out.println("Caught SQL Exception: " + se.getMessage());
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
		private void addAccessionCode(Hashtable fieldValueHash, String tableName, int PK)
		{
			// TODO: This may need to be a configurable property
			String prefix = "VB";
			
			if ( tableName.equalsIgnoreCase("observation") )
			{
				fieldValueHash.put("obsaccessionnumber", prefix + "." + PK );
			}
			// commConcept, party, references
			else if ( 
					tableName.equalsIgnoreCase("plantConcept")
					|| tableName.equalsIgnoreCase("commConcept")
					|| tableName.equalsIgnoreCase("party")
					|| tableName.equalsIgnoreCase("reference"))
			{
				fieldValueHash.put("accessionCode", prefix + "." + PK );
			}
			else
			{
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
		private int getPKFromAccessionCode(Hashtable fieldValueHash, String tableName)
		{		
			int PK = 0;
			String accessionCode = "";
			String fieldName = "";
			
			if ( tableName.equalsIgnoreCase("observation") )
			{
				fieldName = "obsaccessionnumber";
				accessionCode = (String) fieldValueHash.get(fieldName);
			}
			// commConcept, party, references
			else if ( 
					tableName.equalsIgnoreCase("plantConcept")
					|| tableName.equalsIgnoreCase("commConcept")
					|| tableName.equalsIgnoreCase("party")
					|| tableName.equalsIgnoreCase("reference"))
			{
				fieldName = "accessionCode";
				accessionCode = (String) fieldValueHash.get(fieldName);
			}
			else
			{
				// do nothing
			}	
			
			if ( Utility.isStringNullOrEmpty( accessionCode ))
			{
				System.out.println("Found no accessionCode for " + tableName);
				// do nothing
			}
			else
			{
				// Need to get the pK of the table
				Hashtable simpleHash = new Hashtable();
				simpleHash.put(fieldName, accessionCode);
				PK = this.getTablePK(tableName, simpleHash);
				
				if ( PK != 0 )
				{
					// great got a real PK
					System.out.println(
						"Found PK ("
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
							
					System.out.println("Loading Failed: " + errorMessage);
					commit = false;
					errors.AddError(
						LoadingErrors.DATABASELOADINGERROR,
						errorMessage);
				}
			}
			
			return PK;
		}

		/**
		 * @param FK
		 * @param fieldValueHash
		 */
		private void insertNoteLink(int FK, Hashtable fieldValueHash)
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
				this.AddForeignKey(noteLinkDescription, "tableRecord", FK);
				
				
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
					int noteLinkPK = insertTable("noteLink", noteLinkDescription);
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
		private void insertRevision(int FK, Hashtable fieldValueHash)
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
				this.AddForeignKey(revisionDescription, "tableRecord", FK);
				
				
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
		private int insertReference( Hashtable reference)
		{
			int referenceId = 0;
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
			Hashtable referenceJournal = getFKChildTable(reference, "referenceJournal_ID", "referenceJournal");
			int referenceJournalId = insertTable("referenceJournal", referenceJournal);

			AddForeignKey(reference, "REFERENCEJOURNAL_ID", referenceJournalId);
			referenceId = insertTable("reference", reference);
			
			// Insert referenceAltIdent
			Enumeration referenceAltIdents =  getChildTables(reference, "referenceAltIdent");
			while ( referenceAltIdents.hasMoreElements() )
			{
				Hashtable referenceAltIdent = (Hashtable) referenceAltIdents.nextElement();
				AddForeignKey(referenceAltIdent, "REFERENCE_ID", referenceId);
				int referenceAltIdentId = insertTable("referenceAltIdent", referenceAltIdent);
			}
			
			// Insert referenceContributor
			Enumeration referenceContributors =  getChildTables(reference, "referenceContributor");
			while ( referenceContributors.hasMoreElements() )
			{
				Hashtable referenceContributor = (Hashtable) referenceContributors.nextElement();
				
				Hashtable referenceParty = getFKChildTable(referenceContributor, "referenceParty_ID", "referenceParty");
				// Add  referenceParty
				int referencePartyId = insertReferenceParty(referenceParty);
				
				AddForeignKey(referenceContributor, "REFERENCE_ID", referenceId);
				AddForeignKey(referenceContributor, "REFERENCEPARTY_ID", referencePartyId);
				int referenceContributorId = insertTable("referenceContributor", referenceContributor);
			}		
			
			return referenceId;
		}
		
		/**
		 * @param referenceParty
		 * @return
		 */
		private int insertReferenceParty(Hashtable referenceParty)
		{
			int referencePartyId = 0;
			if (referenceParty != null)
			{
				Hashtable referencePartyParent = getFKChildTable(referenceParty, "currentParty_ID", "referenceParty");
				int referencePartyParentId = insertReferenceParty(referencePartyParent);
				
				AddForeignKey(referenceParty, "CURRENTPARTY_ID", referencePartyParentId);
				referencePartyId = insertTable( "referenceParty", referenceParty);
			}
			return referencePartyId;
		}

		private boolean insertUserDefinedTables( String parentTableName, int tableRecordID, Hashtable parentHash)
		{
			boolean result = false;
			
			Enumeration userdefineds = getChildTables( parentHash, "userDefined");
			while ( userdefineds.hasMoreElements() )
			{
				Hashtable userDefined = (Hashtable) userdefineds.nextElement();
				// Use the parentTableName as the tableName value
				userDefined.put( "tableName", parentTableName);
				int userDefinedPK = insertTable( "userDefined", userDefined );
				
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
					Object value = fieldValueHash.get(field);
					if ( this.isDatabaseField(field, value, tableName) )
					{				
						fieldEqualsValue.add(field + "=" + "'" + Utility.escapeCharacters(value.toString()) + "'");
					}
				}
				
				sb.append(
					"SELECT count(*) from "+tableName+" where " 
					+ Utility.joinArray(fieldEqualsValue.toArray(), " and ")
				);

				Statement query = dbConn.createStatement();
				ResultSet rs = query.executeQuery(sb.toString());
				while (rs.next()) 
				{
					rows = rs.getInt(1);
				}
			}
			catch ( SQLException se ) 
			{      
				System.out.println("Caught SQL Exception: " + se.getMessage());
				if ( !se.getMessage().equals("No results were returned by the query.") )
				{
					System.out.println("Query: " + sb.toString() );
					se.printStackTrace();
					commit = false;
					errors.AddError(LoadingErrors.DATABASELOADINGERROR, se.getMessage());
				}
			}
				
			if (rows == 0) 
			{
				System.out.println("LoadTreeToDatabase > "+tableName+" does not exist");
				return (false);
			} 
			else 
			{
				System.out.println("LoadTreeToDatabase > "+tableName+" does exist");
				return (true);
			}
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
				System.out.println("Got asked to find: '" + tableName+ "' from a null Hashtable. This shouldn't happen but hey, what do I know?");
				return childHash;

				//Utility.prettyPrintHash(parentHash);
			}
			

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
				System.out.println("Could not find table.. " + tableName);
			}
			else
			{
				// Don't know what to do here
				System.out.println("Type: '" + o.getClass() + "' should not exist here." );
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
			Vector childVec = (Vector) parentHash.get(tableName);
			if ( childVec == null)
			{
				childVec = new Vector();
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
		private int insertParty( Hashtable party)
		{
			int pKey = 0;
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
			Enumeration addresses =getChildTables( party, "address");
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
		private int insertPartyBase(Hashtable party, String tableName)
		{
			int pKey = 0;
			
			// recursivly deal with FK party's
			Hashtable ownerParty = (Hashtable) party.get("owner_ID");
			Hashtable currentNameParty = (Hashtable) party.get("currentName_ID");
			
			int ownerPartyPK = 0;
			int currentNamePartyPK = 0;
			
			if (ownerParty != null)
			{
				ownerPartyPK = insertParty(ownerParty);
			}
			
			if ( currentNameParty != null)
			{
				currentNamePartyPK = insertParty( currentNameParty);
			}
			
			AddForeignKey(party, "owner_ID", ownerPartyPK);
			AddForeignKey(party, "currentName_ID", currentNamePartyPK);
			
			pKey = insertTable(tableName, party);
							
			return pKey;
		}
		
		private int insertContributor(String tableName, Hashtable contribHash, String keyName, int keyValue)
		{
			int pKey = 0;
			// Handle null
			if ( contribHash == null )
			{
				return pKey;			
			}
					
			// Get the party ( required )
			Hashtable party = this.getFKChildTable(contribHash, "PARTY_ID", "party");
			int partyId = insertParty(party);
							
			// Get the Role  ( not required )
			Hashtable roleIDHash = (Hashtable) this.getChildTable(contribHash, "ROLE_ID");
			if ( roleIDHash != null )
			{
				Hashtable role = this.getChildTable( roleIDHash, "aux_Role" );
				int roleId = insertTable("aux_Role", role);
				AddForeignKey(contribHash, "role_id", roleId);
			}
							
			AddForeignKey(contribHash, keyName, keyValue);
			AddForeignKey(contribHash, "party_id", partyId);
			insertTable(tableName, contribHash);
			
			return pKey;
		}
		
		/**
		 * method to initiate the insertion of a plot
		 *
		 * @param Hashtable -- Hashtable representing plot to load
		 */
		private int insertPlot( Hashtable plotHash)
		{		
			int plotId = 0;

			// Insert the parent plot if it exists
			Hashtable parentPlot =  this.getChildTable(plotHash, "PARENT_ID");
			
			int parentPlotId = 0;
			if (parentPlot != null)
			{
				parentPlotId = insertPlot(parentPlot);
			}
			AddForeignKey(plotHash, "PARENT_ID", parentPlotId);
			
			// Insert this plot
			plotId = insertTable("plot", plotHash);
			
			Enumeration observations = this.getChildTables(plotHash, "observation");
			
			while ( observations.hasMoreElements() )
			{
				Hashtable observationHash = (Hashtable) observations.nextElement();
					
				Hashtable projectIdFieldValueHash =  this.getChildTable(observationHash, "PROJECT_ID");
				Hashtable projectFieldValueHash = this.getChildTable(projectIdFieldValueHash, "project");
				int projectId=0;

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
						this.insertContributor( "projectContributor", (Hashtable) pcs.nextElement(), "project_id",  projectId);
					}	
				}
				
				// Get and insert NamedPlace and place
				Enumeration places = this.getChildTables(plotHash, "place");
				while( places.hasMoreElements()  )
				{
					Hashtable place = (Hashtable) places.nextElement();
					AddForeignKey(place, "plot_id", plotId);

					// Insert NamedPlace
					Hashtable namedPlace =   this.getFKChildTable(place, "NAMEDPLACE_ID", "namedPlace");
					int namedPlaceId = insertTable("namedPlace",namedPlace);
					
					AddForeignKey(place, "namedPlace_id", namedPlaceId);
					int placeId = insertTable("place", place);
				}
				
				int observationId = insertObservation(plotId, projectId, observationHash);
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
		 * @return int -- observationId
		 */
		private int insertObservation(int plotId, int projectId, Hashtable observationHash)
		{
			int observationId = 0;
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
			
			Hashtable previousObs = getFKChildTable(observationHash, "PREVIOUSOBS_ID", "observation");
			
			int previousObsId = 0;
			if (previousObs != null)
			{
				previousObsId = insertObservation(plotId, projectId, previousObs);
			}
			
			AddForeignKey(observationHash, "PREVIOUSOBS_ID", previousObsId);
			AddForeignKey(observationHash, "PLOT_ID", plotId);
			AddForeignKey(observationHash, "PROJECT_ID", projectId);

			// CoverMethod
			Hashtable coverMethod = getFKChildTable(observationHash, "COVERMETHOD_ID", "coverMethod");
			int coverMethodId = insertTable("coverMethod", coverMethod);
			this.insertTables("coverIndex",  getChildTables(coverMethod, "coverIndex"), "COVERMETHOD_ID", coverMethodId);
			AddForeignKey(observationHash, "COVERMETHOD_ID", coverMethodId);

			// StratumMethod			
			Hashtable stratumMethod = getFKChildTable(observationHash, "STRATUMMETHOD_ID", "stratumMethod");
			int stratumMethodId = insertTable("stratumMethod", stratumMethod);
			AddForeignKey(observationHash, "STRATUMMETHOD_ID", stratumMethodId);

			// SoilTaxon
			Hashtable soilTaxon = getFKChildTable(observationHash, "SOILTAXON_ID", "soilTaxon");
			int soilTaxonId = insertTable("soilTaxon", soilTaxon);
			AddForeignKey(observationHash, "SOILTAXON_ID", soilTaxonId);
			
			
			// Insert the observation
			observationId = insertTable("observation", observationHash);
			
			// TODO: insert graphic, observationSynonym
			//this.insertTables("graphic",  getChildTables(observationHash, "graphic"), "observation_id", observationId);
			
			// Add disturbanceObs
			this.insertTables("disturbanceObs",  getChildTables(observationHash, "disturbanceObs"), "observation_id", observationId);
			
			// Add SoilObs
			this.insertTables("soilObs",  getChildTables(observationHash, "soilObs"), "observation_id", observationId);
			
			// Add observation contributors
			Enumeration ocs = this.getChildTables( observationHash, "observationContributor");
			while (ocs.hasMoreElements())
			{	
				this.insertContributor( "observationContributor", (Hashtable) ocs.nextElement(), "observation_id",  observationId);
			}
			
			// Add stratums
			Enumeration stratums = getChildTables(observationHash, "stratum");
			while ( stratums.hasMoreElements())
			{
				Hashtable stratum = (Hashtable) stratums.nextElement();
				int stratumId = insertStratum(stratum, stratumMethodId, observationId);
			}
			
			// Add commClass
			Enumeration commClasses =  getChildTables(observationHash, "commClass");
			while ( commClasses.hasMoreElements() )
			{
				Hashtable commClass = (Hashtable) commClasses.nextElement();
				AddForeignKey(commClass, "OBSERVATION_ID", observationId);
				int commClassId = insertTable("commClass", commClass);
				
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
					AddForeignKey(commIntepretation, "COMMCLASS_id", commClassId);
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
				Hashtable observation = getFKChildTable(observationSynonym, "synonymObservation_ID", "observation");
				int synonymObservationId = insertObservation(plotId, projectId, observation);
				
				int partyId = insertParty( getFKChildTable(observationSynonym, "PARTY_ID", "party") );
				int roleId = 
					insertTable(
						"aux_Role", 
						this.getFKChildTable( observationSynonym, "ROLE_ID", "aux_Role" )
					);

				AddForeignKey(observationSynonym, "PrimaryObservation_ID", observationId);
				AddForeignKey(observationSynonym, "synonymObservation_ID", synonymObservationId);
				AddForeignKey(observationSynonym, "PARTY_ID", partyId);
				AddForeignKey(observationSynonym, "ROLE_ID", roleId);

				int observationSynonymId = insertTable("observationSynonym", observationSynonym);
				
			}
			
			return observationId;
		}

		private void insertTaxonObservations(
			Hashtable observationHash,
			int observationId,
			int stratumMethodId)
		{	
			// Insert the taxonObservations
			Enumeration taxonObservations = getChildTables(observationHash, "taxonObservation");
			while ( taxonObservations.hasMoreElements())
			{
				Hashtable taxonObservation = (Hashtable) taxonObservations.nextElement();
				
				// FIXME: This may be working correctly and just be strange ;)
				// The stemcounts  load a stemlocation each, but taxoninterpretation needs this
				// a single stemlocationId 
				// assumming all the stemlocations identical and using the last one loaded
				int stemLocationId = 0;
				
				String plantName = (String) taxonObservation.get("cheatplantname");
				
				int plantNameId = 0;
				if ( ! Utility.isStringNullOrEmpty(plantName) )
				{ 
					plantNameId = 
						RectificationUtility.getForiegnKey(dbConn, RectificationUtility.GETPLANTNAMEID, plantName );
				}
				
				// Add FKs to taxonObservation
				AddForeignKey(taxonObservation, "PLANTNAME_ID", plantNameId);
				AddForeignKey(taxonObservation, "OBSERVATION_ID", observationId);
			
				
				int taxonObservationId = insertTable("taxonObservation", taxonObservation);
				System.out.println("taxonObservationId: " + taxonObservationId );
			
				//  Add StratumComposition
				Enumeration stratumCompositions = getChildTables(taxonObservation, "stratumComposition");
				while ( stratumCompositions.hasMoreElements())
				{
					Hashtable stratumComposition = (Hashtable) stratumCompositions.nextElement();
					AddForeignKey(stratumComposition, "TAXONOBSERVATION_ID", taxonObservationId);
					
					// Get the stratum_id
					Hashtable stratum = getFKChildTable(stratumComposition, "STRATUM_ID", "stratum");
					int stratumId = insertStratum(stratum, stratumMethodId, observationId);
					
					AddForeignKey(stratumComposition, "STRATUM_ID", stratumId);
					insertTable("stratumComposition", stratumComposition );
				}
				
				// Add  StemCount
				Enumeration stemCounts = getChildTables(taxonObservation, "stemCount");
				while ( stemCounts.hasMoreElements())
				{
					Hashtable stemCount = (Hashtable) stemCounts.nextElement();
					AddForeignKey(stemCount, "TAXONOBSERVATION_ID", taxonObservationId);
					
					int stemCountId = insertTable("stemCount", stemCount);
					
					// Add StemLocation
					Enumeration stemLocations = getChildTables(stemCount, "stemLocation");
					while ( stemLocations.hasMoreElements())
					{
						Hashtable stemLocation = (Hashtable) stemLocations.nextElement();
						AddForeignKey(stemLocation, "STEMCOUNT_ID", stemCountId);
						
						stemLocationId = insertTable("stemLocation", stemLocation);
					}
				}
				
				// Add TaxonInterpretation
				Enumeration taxonInterpretations = getChildTables(taxonObservation, "taxonInterpretation");
				while ( taxonInterpretations.hasMoreElements())
				{
					Hashtable taxonInterpretation = (Hashtable) taxonInterpretations.nextElement();
					
					// Get the party
					Hashtable party =  this.getFKChildTable(taxonInterpretation, "PARTY_ID", "party");
					int partyId = insertParty(party);
					
					// Get the Role
					Hashtable role =  this.getFKChildTable(taxonInterpretation, "ROLE_ID", "aux_Role");
					int roleId = insertTable("aux_Role", role);
					
					// Get the PlantConcept
					Hashtable plantConcept =  this.getFKChildTable(taxonInterpretation, "PLANTCONCEPT_ID", "plantConcept");
					int plantConceptId = this.insertPlantConcept(plantConcept);
				 
				 	// Get the museum party
					Hashtable museumParty =  this.getFKChildTable(taxonInterpretation, "museum_ID", "party");
					int museumId = insertTable("party", museumParty);
				 	
				 	// Get the collector party
					Hashtable collectorParty =  this.getFKChildTable(taxonInterpretation, "collector_ID", "party");
					int collectorId = insertTable("party", collectorParty);
				 
					// Add FKs to taxonObservation
					AddForeignKey(taxonInterpretation, "PLANTCONCEPT_ID", plantConceptId);
					AddForeignKey(taxonInterpretation, "ROLE_ID", roleId);
					AddForeignKey(taxonInterpretation, "PARTY_ID", partyId);
					AddForeignKey(taxonInterpretation, "TAXONOBSERVATION_ID", taxonObservationId);
					AddForeignKey(taxonInterpretation, "museum_ID", museumId);
					AddForeignKey(taxonInterpretation, "collector_ID", collectorId);
					AddForeignKey(taxonInterpretation, "stemLocation_ID", stemLocationId);
					
					int taxonInterpretationId = this.insertTable("taxonInterpretation", taxonInterpretation);
					
					// Add taxonAlt
					Enumeration taxonAlts = getChildTables(taxonObservation, "taxonAlt");
					while ( taxonAlts.hasMoreElements())
					{
						Hashtable taxonAlt = (Hashtable) taxonAlts.nextElement();
						AddForeignKey(taxonAlt, "TAXONINTERPRETATION_ID", taxonInterpretationId);
						
						Hashtable altPlantConcept = this.getFKChildTable(taxonAlt, "PLANTCONCEPT_ID", "plantConcept");
						int altPlantConceptId = insertPlantConcept(altPlantConcept);
						
						int taxonAltId = this.insertTable("taxonAlt", taxonAlt);
					}
				}
			}
		}
		
		/**
		 * Insert PlantConcept
		 * 
		 * @param plantConcept
		 * @return int -- the PK of  this row
		 */
		private int insertPlantConcept(Hashtable plantConcept)
		{
			int pKey = 0;
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
			Hashtable plantName = this.getFKChildTable(plantConcept, "PLANTNAME_ID", "plantName");
			int plantNameId = insertTable("plantName", plantName);
			
			AddForeignKey(plantConcept, "PLANTNAME_ID", plantNameId);
			pKey = this.insertTable("plantConcept", plantConcept);
			
			// Add PlantStatus 
			Enumeration plantStatuses = getChildTables(plantConcept, "plantStatus");
			while ( plantStatuses.hasMoreElements())
			{
				Hashtable plantStatus = (Hashtable) plantStatuses.nextElement();
				AddForeignKey(plantStatus, "PLANTCONCEPT_ID", pKey);
				insertPlantStatus(plantStatus);
			}
			
			// Add plantUsage
			Enumeration plantUsages = getChildTables(plantConcept, "plantUsage");
			while ( plantUsages.hasMoreElements())
			{
				Hashtable plantUsage = (Hashtable) plantUsages.nextElement();
				AddForeignKey(plantUsage, "PLANTCONCEPT_ID", pKey);
				insertPlantUsage(plantUsage);
			}
			
			return pKey;
		}

		/**
		 * Insert CommConcept
		 * 
		 * @param commConcept
		 * @return int -- the PK of  this row
		 */
		private int insertCommConcept(Hashtable commConcept)
		{
			int pKey = 0;
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
			Hashtable commName = this.getFKChildTable(commConcept, "COMMNAME_ID", "commName");
			int commNameId = insertTable("commName", commName);
			
			AddForeignKey(commConcept, "COMMNAME_ID", commNameId);
			pKey = this.insertTable("commConcept", commConcept);
			
			// Add commStatus 
			Enumeration commStatuses = getChildTables(commConcept, "commStatus");
			while ( commStatuses.hasMoreElements())
			{
				Hashtable commStatus = (Hashtable) commStatuses.nextElement();
				AddForeignKey(commStatus, "COMMCONCEPT_ID", pKey);
				insertCommStatus(commStatus);
			}
			
			// Add commUsage
			Enumeration commUsages = getChildTables(commConcept, "commUsage");
			while ( commUsages.hasMoreElements())
			{
				Hashtable commUsage = (Hashtable) commUsages.nextElement();
				AddForeignKey(commUsage, "COMMCONCEPT_ID", pKey);
				insertCommUsage(commUsage);
			}
			
			return pKey;
		}		
		
		/**
		 * @param commStatus
		 * @return int -- primary key assigned
		 */
		private int insertCommStatus(Hashtable commStatus)
		{
			int pKey =0;
		
			// Handle Nulls
			if ( commStatus == null)
			{
				return pKey;
			}

			//Utility.prettyPrintHash(commStatus);
		
			// Add commParent
			Hashtable commParent = this.getFKChildTable(commStatus, "commParent_ID", "commConcept");
			int commParentId = insertCommConcept(commParent); // recursive
		
			// Add commParty
			Hashtable commParty = this.getFKChildTable(commStatus, "COMMPARTY_ID", "commParty");
			int commPartyId = insertCommParty(commParty);
			
			AddForeignKey(commStatus, "commParent_ID", commParentId);
			AddForeignKey(commStatus, "COMMPARTY_ID", commPartyId);
			
			pKey = this.insertTable("commStatus", commStatus);
			
			// Add commLineage
			Enumeration commLineages = getChildTables(commStatus, "commLineage");
			while ( commLineages.hasMoreElements())
			{
				Hashtable commLineage = (Hashtable) commLineages.nextElement();
				AddForeignKey(commLineage, "COMMSTATUS_ID", pKey);
				insertTable( "commLineage", commLineage);
			}
		
			// Add commCorrelation
			Enumeration commCorrelations = getChildTables(commStatus, "commCorrelation");
			while ( commCorrelations.hasMoreElements())
			{
				Hashtable commCorrelation = (Hashtable) commCorrelations.nextElement();

				// TODO:  Add commConcept
				Hashtable commConcept = this.getFKChildTable(commCorrelation, "PLANTCONCEPT_ID", "plantConcept");
				int commConceptId = insertCommConcept(commConcept); 

				AddForeignKey(commCorrelation, "COMMSTATUS_ID", pKey);
				AddForeignKey(commCorrelation, "COMMCONCEPT_ID", commConceptId);
							
				insertTable( "commCorrelation", commCorrelation);
			}
			return pKey;
		}
		
		private void insertCommUsage(Hashtable commUsage)
		{
			// Add commName 
			Hashtable commName = this.getFKChildTable(commUsage, "COMMNAME_ID", "commName");
			int commNameId = insertTable("commName", commName);
			
			// Add commParty
			Hashtable commParty = this.getFKChildTable(commUsage, "PLANTPARTY_ID", "plantParty");
			int commPartyId = insertCommParty(commParty);
			
			AddForeignKey(commUsage, "COMMNAME_ID", commNameId);
			AddForeignKey(commUsage, "COMMPARTY_ID", commPartyId);
			
			insertTable( "commUsage", commUsage);
		}
		
		/**
		 * @param commParty
		 * @return
		 */
		private int insertCommParty(Hashtable commParty)
		{
			int pKey = 0;
			if ( commParty == null )
			{
				return pKey;
			}
			
			pKey = this.insertPartyBase(commParty, "commParty");
			
			return pKey;
		}

		private void insertPlantUsage(Hashtable plantUsage)
		{
			// Add plantName 
			Hashtable plantName = this.getFKChildTable(plantUsage, "PLANTNAME_ID", "plantName");
			int plantNameId = insertTable("plantName", plantName);
			
			// Add PlantParty
			Hashtable plantParty = this.getFKChildTable(plantUsage, "PLANTPARTY_ID", "plantParty");
			int plantPartyId = insertPlantParty(plantParty);
			
			AddForeignKey(plantUsage, "PLANTNAME_ID", plantNameId);
			AddForeignKey(plantUsage, "PLANTPARTY_ID", plantPartyId);
			
			insertTable( "plantUsage", plantUsage);
		}
		
		/**
		 * @param plantStatus
		 * @return int -- primary key assigned
		 */
		private int insertPlantStatus(Hashtable plantStatus)
		{
			int pKey =0;
			
			// Handle Nulls
			if ( plantStatus == null)
			{
				return pKey;
			}

			//Utility.prettyPrintHash(plantStatus);
			
			// Add plantParent
			Hashtable plantParent = this.getFKChildTable(plantStatus, "plantParent_ID", "plantConcept");
			int plantParentId = insertPlantConcept(plantParent); // recursive
			
			// Add PlantParty
			Hashtable plantParty = this.getFKChildTable(plantStatus, "PLANTPARTY_ID", "plantParty");
			int plantPartyId = insertPlantParty(plantParty);
				
			AddForeignKey(plantStatus, "plantParent_ID", plantParentId);
			AddForeignKey(plantStatus, "PLANTPARTY_ID", plantPartyId);
				
			pKey = this.insertTable("plantStatus", plantStatus);
				
			// Add plantLineage
			Enumeration plantLineages = getChildTables(plantStatus, "plantLineage");
			while ( plantLineages.hasMoreElements())
			{
				Hashtable plantLineage = (Hashtable) plantLineages.nextElement();
				AddForeignKey(plantLineage, "PLANTSTATUS_ID", pKey);
				insertTable( "plantLineage", plantLineage);
			}
			
			// Add plantCorrelation
			Enumeration plantCorrelations = getChildTables(plantStatus, "plantCorrelation");
			while ( plantCorrelations.hasMoreElements())
			{
				Hashtable plantCorrelation = (Hashtable) plantCorrelations.nextElement();

				// TODO:  Add PlantConcept
				Hashtable plantConcept = this.getFKChildTable(plantCorrelation, "PLANTCONCEPT_ID", "plantConcept");
				int plantConceptId = insertPlantConcept(plantConcept); 

				AddForeignKey(plantCorrelation, "PLANTSTATUS_ID", pKey);
				AddForeignKey(plantCorrelation, "PLANTCONCEPT_ID", plantConceptId);
								
				insertTable( "plantCorrelation", plantCorrelation);
			}
			return pKey;
		}
		

		/**
		 * @param plantParty
		 * @return
		 */
		private int insertPlantParty(Hashtable plantParty)
		{
			int pKey = 0;
			if ( plantParty == null )
			{
				return pKey;
			}
			
			pKey = this.insertPartyBase(plantParty, "plantParty");
			
			return pKey;
		}

		/**
		 * Convience method to check keyValue for 0 before adding it
		 * @param table
		 * @param FKName
		 * @param FKValue
		 */
		private void AddForeignKey( Hashtable table, String FKName, int FKValue)
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
		private int insertStratum(Hashtable stratum, int stratumMethodId, int observationId)
		{
			Hashtable stratumType = getFKChildTable(stratum, "STRATUMTYPE_ID", "stratumType");
			
			AddForeignKey(stratumType, "STRATUMMETHOD_ID", stratumMethodId);
			AddForeignKey(stratum, "OBSERVATION_ID", observationId);
			
			int stratumTypeId = insertTable("stratumType", stratumType);
			
			AddForeignKey(stratum, "STRATUMTYPE_ID", stratumTypeId);
			int stratumId = insertTable("stratum", stratum);
			
			return stratumId;
		}

		/**
		 * @param commIntepretation
		 * @param commClassId
		 */
		private int insertCommInterpetation(Hashtable commIntepretation)
		{			
			int pKey = 0;
			// Handle null
			if ( commIntepretation == null)
			{
				return pKey;
			}

			Hashtable commConcept = this.getFKChildTable(commIntepretation, "COMMCONCEPT_ID", "commConcept");
			int commConceptId = this.insertCommConcept(commConcept);
			AddForeignKey(commIntepretation, "COMMCONCEPT_ID", commConceptId);
			
			Hashtable reference = this.getFKChildTable(commIntepretation, "commAuthority_ID", "reference");
			int commAuthorityId = this.insertReference(reference);
			AddForeignKey(commIntepretation, "commAuthority_ID", commAuthorityId);
			
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
					System.out.println("Invalid Error type");
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
