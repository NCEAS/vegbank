/*	
 * '$RCSfile: SAX2DBContentHandler.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-05 22:24:57 $'
 *	'$Revision: 1.2 $'
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
package org.vegbank.dataload.XML;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public class SAX2DBContentHandler implements ContentHandler
	{
		
		public static final String TABLENAME = "TableName";
		private LoadingErrors errors = null;
		private boolean load = true;
		
		public SAX2DBContentHandler(LoadingErrors errors, boolean load)
		{
			this.errors = errors;
			this.load = load;
			tables.add(tmpStore);
		}
		
		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endDocument()
		 */
		public void endDocument() throws SAXException
		{
			LoadTreeToDatabase ltdb = new LoadTreeToDatabase(errors, load);
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
			LogUtility.log("SAX2DBContentHandler: Finished storing table: " + tableName, LogUtility.DEBUG);
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
					LogUtility.log("SAX2DBContentHandler: ... table exists ... adding another : " + tableName, LogUtility.DEBUG );
				
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
			this.getCurrentTable().put(TABLENAME, tableName );
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