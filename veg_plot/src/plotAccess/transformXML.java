import java.io.IOException;
import java.io.*;
import java.util.*;

import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;




/**
 *  code to run the XSL processor
 *  on the a plots xml file to translate it to via the 
 *  xslt file plot2DBaddress.xsl - to a document containing
 *  the address in the Plots DB (table.attribute) and data.
 */

public class  transformXML
{


public void transformXML()
{

/*variable to make available to the calling class -- the string containg the db location (table.attribute) and the data*/
        
outTransformedData = null;
        
}


public void getTransformed (String inputXML, String inputXSL)
	throws java.io.IOException,
        java.net.MalformedURLException,
        org.xml.sax.SAXException
	
	{

StringWriter out =new StringWriter();


    // Have the XSLTProcessorFactory obtain a interface to a
    // new XSLTProcessor object.
    XSLTProcessor processor = XSLTProcessorFactory.getProcessor();

    // Have the XSLTProcessor processor object transform inputXML  to
    // StringWriter, using the XSLT instructions found in "*.xsl".
    processor.process(new XSLTInputSource(inputXML),
                      new XSLTInputSource(inputXSL),
                     // new XSLTResultTarget(System.out)),
			new XSLTResultTarget(out));


out.toString();
outTransformedData=out;

	}


// these variables can be accessed from the calling program
public StringWriter outTransformedData=null;



}
