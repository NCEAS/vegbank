/**
 *  code to run the XSL processor
 *  on the a plots xml file to translate it to via the 
 *  xslt file plot2DBaddress.xsl - to a document containing
 *  the address in the Plots DB (table.attribute) and data.
 * 
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-01-28 20:08:28 $'
 * '$Revision: 1.2 $'
 */
//package vegclient.framework;
package xmlresource.utils; 
import java.io.IOException;
import java.io.*;
import java.util.*;

import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;

import org.w3c.dom.*;                         // DOM interface
import org.apache.xerces.parsers.DOMParser;   // Parser (to DOM)
import org.apache.xerces.*; //

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import xmlresource.utils.*;


public class  transformXML
{


	// these variables can be accessed from the calling program
	public StringWriter outTransformedData=null;

	/*variable to make available to the calling class -- the string containg the db location (table.attribute) and the data*/
	//outTransformedData = null;
        


	public void transformXML()
	{
	
	}
	
	
	/**
	*
	* This is a method that will transform an xml document with an xsl style sheet
	*
	* @param inputXML -- the file name of the input xml file
	* @param inputXSL -- the file name of the input xsl style sheet
	*
	*/
	public void getTransformed(String inputXML, String inputXSL)
	throws java.io.IOException,
		java.net.MalformedURLException,
		org.xml.sax.SAXException
	{
	
		try
		{
			//System.out.println("transforming xml file: '"+inputXML+"'");
			
			StringWriter out =new StringWriter();
			// Have the XSLTProcessorFactory obtain a interface to a
			// new XSLTProcessor object.
			XSLTProcessor processor = XSLTProcessorFactory.getProcessor();

			// Have the XSLTProcessor processor object transform inputXML  to
			// StringWriter, using the XSLT instructions found in "*.xsl".
			processor.process(new XSLTInputSource(inputXML), 
			new XSLTInputSource(inputXSL),
			new XSLTResultTarget(out));

			out.toString();
			outTransformedData=out;
		}
		catch( Exception e ) 
		{
			System.out.println(" Exception: "	+ e.getMessage() );
			e.printStackTrace();
		}
	}
	
	/**
	 * method that does the same as the above method, but 
	 * does not crate errors
	 */
	public String  getTransformedNoErrors(String inputXML, String inputXSL)
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
			
			//the file to write to
			//the printWriter
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			
			transformXML trans = new transformXML();
			trans.getTransformed( inputXML, inputXSL);
			out.println( trans.outTransformedData.toString() );
			
		}
		catch( Exception e ) 
		{
			System.out.println(" Exception: "
			+e.getMessage() );
			e.printStackTrace();
		}
	 }
	 
	 
	 
	
	public void transformXMLDocument(String xml, String xsl)
	{
		try
		{
		//XSLStylesheet style = new XSLStylesheet();
		DOMParser dp = new DOMParser();
		//dp.setValidation(false);
		//dp.parse(xml);
		//	doc = dp.getDocument();
		//dp.parse( (Reader) (new StringReader(xml)) );
		  dp.setFeature("http://xml.org/sax/features/validation", false);
			dp.parse(xml);
			
			//new XSLTProcessor().processXSL(xsl, dp.getDocument(), outTransformedData);
			
		//	XSLProcessor processor = new XSLProcessor();
			
			StringWriter out =new StringWriter();
			
			// Have the XSLTProcessor processor object transform inputXML  to
			// StringWriter, using the XSLT instructions found in "*.xsl".
	//		processor.process(new XSLTInputSource(xml),
	//		new XSLTInputSource(xsl),
	//		new XSLTResultTarget(out));
			
		}
		catch (Exception e) 
		{
			System.out.println("\nError: " + e.getMessage());
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
			System.out.println("Usage: \n java XMLparse xmlFile xslt or \n"
			+" ");
		}
		else
		{
			String xml = args[0];
			String xsl = args[1];
			try
			{
				trans.getTransformed( xml, xsl);
				System.out.println( trans.outTransformedData.toString() );
			}
			catch( Exception e ) 
		{
			System.out.println(" caught exception: "
			+e.getMessage() );
			e.printStackTrace();
		}
		}
	}
	
	
}
