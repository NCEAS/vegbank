/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package xmlresource.utils;                    

//import util.Arguments;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

//import sax.helpers.AttributeListImpl;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.ParserFactory;
import xmlresource.utils.*;

/**
 * A sample SAX counter. This sample program illustrates how to
 * register a SAX DocumentHandler and receive the callbacks in
 * order to print information about the document.
 *
 * @version
 */
public class XMLSAXparse 
extends HandlerBase {

    //
    // Constants
    //

    /** Default parser name. */
    private static final String 
    DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
		
		//the target element  -- additions should be made for
		// a number of element names
		private static String targetElement = null;
		private static String targetElement2 = null;
		private static String currentElement = null;
		
		//this is used to store single element value
		private static Vector singleElementVector = new Vector();
		private static Vector singleElementVector2 = new Vector();
		
    private static boolean setValidation    = false; //defaults
    private static boolean setNameSpaces    = true;
    private static boolean setLoadExternalDTD = true;
    private static boolean setSchemaSupport = true;


    //
    // Data
    //

    private static boolean warmup = false;

    /** Elements. */
    private long elements;

    /** Attributes. */
    private long attributes;

    /** Characters. */
    private long characters;

    /** Ignorable whitespace. */
    private long ignorableWhitespace;

    //
    // Public static methods
    //

    /** Prints the output from the SAX callbacks. */
    public static void print(String parserName, String uri, boolean validate) 
		{

        try {
            XMLSAXparse counter = new XMLSAXparse();

            Parser parser = ParserFactory.makeParser(parserName);
            parser.setDocumentHandler(counter);
            parser.setErrorHandler(counter);
            try {
                //if (validate && parser instanceof XMLReader)
                if ( parser instanceof XMLReader ){
                    ((XMLReader)parser).setFeature( "http://xml.org/sax/features/validation", 
                                                    validate);
                    ((XMLReader)parser).setFeature( "http://xml.org/sax/features/namespaces",
                                                    setNameSpaces );
                    ((XMLReader)parser).setFeature( "http://apache.org/xml/features/validation/schema",
                                                    setSchemaSupport );
                    ((XMLReader)parser).setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                                                    setLoadExternalDTD );

                }
            } catch (Exception ex) {
            }

            if (warmup) {
                if (parser instanceof XMLReader)
                    ((XMLReader)parser).setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);

                parser.parse(uri);
                warmup = false;
            }
            long before = System.currentTimeMillis();
            parser.parse(uri);
            long after = System.currentTimeMillis();
            counter.printResults(uri, after - before);
        } catch (org.xml.sax.SAXParseException spe) {
            spe.printStackTrace(System.err);
        } catch (org.xml.sax.SAXException se) {
            if (se.getException() != null)
                se.getException().printStackTrace(System.err);
            else
                se.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    } // print(String,String)

    //
    // DocumentHandler methods
    //
		
		
		 

    /** Start document. */
    public void startDocument() {

        if (warmup)
            return;

        elements            = 0;
        attributes          = 0;
        characters          = 0;
        ignorableWhitespace = 0;

    } // startDocument()

    /** Start element. */
    public void startElement(String name, AttributeList attrs) 
		{
			//System.out.println( targetElement + " ? " + name);
			
			
			//set the glocal currentElement
			currentElement = name;
			
			
			//if the this element equals the current element
///			if ( name.trim().equals(targetElement) )
///			{
///				System.out.println(" startElements: "+ name);
///			}
			
			//BasicNode currentNode = new BasicNode(name);
			
        if (warmup)
            return;
	 
        elements++;
        if (attrs != null) {
            
						attributes += attrs.getLength();
						//System.out.println( attributes );
						//added by jhh -- most the xml docs that 
						// I ues will not require this
						for (int i = 0; i <= attrs.getLength(); i++) 
						{
							String attname = attrs.getName(i);
     					String atttype = attrs.getType(i);
     					String attvalue = attrs.getValue(i);
							//System.out.println(attrs.getNodeName());
						//	System.out.println( attname +" "+ atttype+" " + attvalue);
						}
				}

    } // startElement(String,AttributeList)

    /** Characters. */
    public void characters(char ch[], int start, int length) 
		{
			try 
			{

        if (warmup)
            return;

        characters += length;
				
				//if this is the element that we are looking for then stick into 
				// the vector used to store the single element
				if ( currentElement.trim().equals(targetElement) )
				{
					String text = new String (ch, start, length);			
					//System.out.println( "text: " + text );
					singleElementVector.addElement( text );
				}
				else if ( currentElement.trim().equals(targetElement2) )
				{
					String text = new String (ch, start, length);			
					//System.out.println( "text: " + text );
					singleElementVector2.addElement( text );
				}
			//	for (int i = 0; i <= (length); i++) 
			//	{
			//		System.out.println( "characters" + ch[i] );
			//	}
			}
			catch (Exception e)
			{
				System.out.println( "Exception " + e.getMessage() );
			}
    } // characters(char[],int,int);

    /** Ignorable whitespace. */
    public void ignorableWhitespace(char ch[], int start, int length) {

        if (warmup)
            return;

        ignorableWhitespace += length;

    } // ignorableWhitespace(char[],int,int);

    //
    // ErrorHandler methods
    //

    /** Warning. */
    public void warning(SAXParseException ex) {
        if (warmup)
            return;

        System.err.println("[Warning] "+
                           getLocationString(ex)+": "+
                           ex.getMessage());
    }

    /** Error. */
    public void error(SAXParseException ex) {
        if (warmup)
            return;

        System.err.println("[Error] "+
                           getLocationString(ex)+": "+
                           ex.getMessage());
    }

    /** Fatal error. */
    public void fatalError(SAXParseException ex) throws SAXException {
        if (warmup)
            return;

        System.err.println("[Fatal Error] "+
                           getLocationString(ex)+": "+
                           ex.getMessage());
//        throw ex;
    }

    /** Returns a string of the location. */
    private String getLocationString(SAXParseException ex) {
        StringBuffer str = new StringBuffer();

        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1)
                systemId = systemId.substring(index + 1);
            str.append(systemId);
        }
        str.append(':');
        str.append(ex.getLineNumber());
        str.append(':');
        str.append(ex.getColumnNumber());

        return str.toString();

    } // getLocationString(SAXParseException):String

    //
    // Public methods
    //

    /** Prints the results. */
    public void printResults(String uri, long time) {

        // filename.xml: 631 ms (4 elems, 0 attrs, 78 spaces, 0 chars)
        System.out.print(uri);
        System.out.print(": ");
        System.out.print(time);
        System.out.print(" ms (");
        System.out.print(elements);
        System.out.print(" elems, ");
        System.out.print(attributes);
        System.out.print(" attrs, ");
        System.out.print(ignorableWhitespace);
        System.out.print(" spaces, ");
        System.out.print(characters);
        System.out.print(" chars)");
        System.out.println();
    } // printResults(String,long)

    //
    // Main
    //
		
		/**
		 * method to get a single element
		 */
		public Vector getSingleElement(String xmlfile, String target)
		{
			 //XMLSAXparse count = new XMLSAXparse();
     //  String  xmlfile  = argv[0];
    	 targetElement = target;
			 System.out.println( "target: " + targetElement);
			 String  parserName = DEFAULT_PARSER_NAME;
       // print uri
       print(parserName, xmlfile,  setValidation);
			 return ( singleElementVector );
		}

		/**
		 * method to handle getting two elements which correspond to 
		 * one another, for like the tst class where there is a key
		 * and an object
		 *
		 * @return a vector that has two vectors
		 **/
		 public Vector getElementPair(String xmlfile, String target1, String target2)
		{
			 //XMLSAXparse count = new XMLSAXparse();
     //  String  xmlfile  = argv[0];
    	 targetElement = target1;
			 targetElement2 = target2;
			 System.out.println( "target1: " + targetElement);
			 System.out.println( "target2: " + targetElement2);
			 String  parserName = DEFAULT_PARSER_NAME;
       // print uri
       print(parserName, xmlfile,  setValidation);
			 
			 Vector v = new Vector();
			 v.addElement(singleElementVector);
			 v.addElement(singleElementVector2);
			 return ( v );
		}
		
                            /** Main program entry point. */
    public static void main(String argv[]) {

			
		if (argv.length < 2) 
		{
       System.err.println("Wrong number of arguments!!!");
       System.err.println("USAGE: java className  <xmlfile>, <elementname>");
			 System.err.println("USAGE: java className  <xmlfile>, <elementname> <elementname2>");
       System.exit(1);
     } 
		 if (argv.length == 2)
		 {
			 XMLSAXparse count = new XMLSAXparse();
       String  xmlfile  = argv[0];
       String target = argv[1];
			 //System.out.println( "target: " + count.targetElement);
			 //String  parserName = DEFAULT_PARSER_NAME;
       // print uri
       //count.print(parserName, xmlfile,  setValidation);
		 	 count.getSingleElement( xmlfile, target);
			 System.out.println( "vec size " + count.singleElementVector.toString() );
			
		 }
		 else if (argv.length == 3)
		 {
			 XMLSAXparse count = new XMLSAXparse();
       String  xmlfile  = argv[0];
       String target = argv[1];
			 String target2 = argv[2];
			 //System.out.println( "target: " + count.targetElement);
			 //String  parserName = DEFAULT_PARSER_NAME;
       // print uri
       //count.print(parserName, xmlfile,  setValidation);
		 	 Vector v = count.getElementPair( xmlfile, target, target2);
			 System.out.println( "vec: " + v.toString() );
			
		 }
		 
    } // main(String[])

} // class XMLSAXparse
