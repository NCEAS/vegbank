/**
 *  code to run the XSL processor
 *  on the a plots xml file to translate it to via the 
 *  xslt file plot2DBaddress.xsl - to a document containing
 *  the address in the Plots DB (table.attribute) and data.
 * 
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2003-07-15 21:18:35 $'
 * '$Revision: 1.1 $'
 */

package org.vegbank.xmlresource;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Hashtable;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;


public class  transformXML
{	
	private Hashtable templates = new Hashtable();
	private TransformerFactory tFactory = TransformerFactory.newInstance( );
	
	// FIXME: This should be set via properties
	private String CACHE_XSL = "true";



	/*variable to make available to the calling class -- the string containg the db location (table.attribute) and the data*/
	//outTransformedData = null;
        

 /**
	*
	* This is a method that will transform an xml document with an xsl style sheet
	*
	* @param inputXML -- the file name of the input xml file
	* @param inputXSL -- the file name of the input xsl style sheet
	*
	*/
	public void getTransformed(String inputXML, String inputXSL, Writer out)
	{
		XMLparse xp = new XMLparse();
		Document doc = xp.getDocument(inputXML);
		this.getTransformed(doc, inputXSL, out);
	}
	
	private void getTransformed(Document doc, String inputXSL, Writer output)
	{
		try
		{
			Source xmlSource = new DOMSource(doc);
			Transformer trans = this.getTransformer(inputXSL);
			trans.transform(xmlSource, new StreamResult(output));
		}
		catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Transformer getTransformer(String xsltFileName )
		throws Exception {
		// Check Cache
		Transformer trans = (Transformer) templates.get(xsltFileName);

		if ( trans == null ) {
			// Ok need to create a new transformer

			if ( xsltFileName != null ) {
				StreamSource xslSource = new StreamSource(xsltFileName);
				Templates stylesheet = tFactory.newTemplates( xslSource );
				trans = stylesheet.newTransformer();

				// Cache
				if (CACHE_XSL.equals("true")) {
					templates.put(xsltFileName, trans);
					// TODO: Shoud be printing to logfile
					System.out.println("Caching .. " + xsltFileName);
				}

			} else {
				System.out.println("Cannot find xsl file " + xsltFileName);
			}
		}
		return trans;
	}

	

	/**
	*
	* This is a method that will transform xml stored 
	* as a java string with an xsl style sheet
	*
	* @param inXml -- the java String that contains the xml
	* @param inputXSL -- the file name of the input xsl style sheet
	*
	*/
	public void getTransformedFromString(String inXml, String inputXSL, Writer output)
		throws java.io.IOException,
									java.net.MalformedURLException,
									org.xml.sax.SAXException
	{
		StringWriter out = new StringWriter();
		try
		{
			//System.out.println("transformXML > input xml string: \n '"+inXml+"'");
			XMLparse xp = new XMLparse();
			Document doc = xp.getDocumentFromString(inXml);
			this.getTransformed(doc, inputXSL, output);			
		}
		catch( Exception e ) 
		{
			System.out.println(" Exception: "	+ e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public String getTransformedFromString(String inXml, String inputXSL)
	{
		StringWriter output = new StringWriter();
		try
		{
			getTransformedFromString(inXml, inputXSL, output);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output.toString();
	}

	
	
	
	/**
	 * method that does the same as the above method, but 
	 * does not crate errors
	 * @deprecated
	 */
	public String getTransformedNoErrors(String inputXML, String inputXSL)
	{
	
		String s = null;
		try
		{
			StringWriter out =new StringWriter();
			
			TransformerFactory tFac = TransformerFactory.newInstance();
			Transformer transformer = tFac.newTransformer(
				new StreamSource( inputXSL ));
			transformer.transform( new StreamSource( new StringReader(inputXML) ),
			new StreamResult(out) );
			s = out.toString();
		}
		catch( Exception e ) 
		{
			System.out.println(" Exception: "	+ e.getMessage() );
			e.printStackTrace();
		}
		return(s);
	}
	
	
	
	/**
	 * method that will transform an xml document with a stylesheet and write the
	 * results to a file
	 *
	 * @param inputXML -- the file name of the input xml file
	 * @param inputXSL -- the file name of the input xsl style sheet
	 *	@param outFileName -- the file name that should be written to
	 */
	 
	 public void transformXMLDocumentToFile(String inputXML, String inputXSL, String fileName)
	 {
		 try
		{
			
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			XMLparse xp = new XMLparse();
			Document doc = xp.getDocumentFromString(inputXML);
			this.getTransformed(doc, inputXSL, out);
//			transformXML trans = new transformXML();
//			trans.getTransformed( inputXML, inputXSL);
//			out.println( trans.outTransformedData.toString() );
//		 	out.close();	
		}
		catch( Exception e ) 
		{
			System.out.println(" Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	 }
		
	/**
	 * main method for testing and for use in standalone patransforming    
	 */
	public static void main(String[] args) 
	{
		transformXML trans = new transformXML();
		if (args.length < 2)
		{
			System.out.println("Usage: \n java XMLparse xmlFile xslt\n"
			+" ");
		}
		else
		{
			String xml = args[0];
			String xsl = args[1];
			try
			{
				StringWriter out = new StringWriter();
				trans.getTransformed( xml, xsl, out);
				System.out.println( out.toString() );
			}
			catch( Exception e ) 
		{
			System.out.println(" caught exception: " + e.getMessage() );
			e.printStackTrace();
		}
		}
	}
	
	
}
