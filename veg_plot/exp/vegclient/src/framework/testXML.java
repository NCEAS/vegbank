import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
 
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.*;                         // DOM interface
import org.apache.xerces.parsers.DOMParser;   // Parser (to DOM)



public class testXML
{
    // Global value so it can be ref'd by the tree-adapter
    static Document document; 
		
		

    public static void main(String argv[])
    {
        if (argv.length != 1) {
            System.err.println("Usage: java DomEcho filename");
            System.exit(1);
        }

    //    DocumentBuilderFactory factory =
     //       DocumentBuilderFactory.newInstance();
       // factory.setValidating(true);   
       // factory.setNamespaceAware(true);
        try {
//          DocumentBuilder builder = factory.newDocumentBuilder();
//           document = builder.parse( new File(argv[0]) );
					
					
					////////////////////////
					
			
      		DOMParser dp = new DOMParser();
      		dp.parse( "test.xml" );
     			 document = dp.getDocument();
					 
					 
					 				NodeList nlist;
				Node node;
//				System.out.println(doc.getDoctype() );
				NodeList alllist = document.getElementsByTagName("project"); 
//				nlist = doc.getElementsByTagName(parent);
//				Element elmParent=(Element)nlist.item(0);
//				System.out.println("WHY: " + elmParent.getFirstChild().getNodeName() );
//				NodeList children = elmParent.getElementsByTagName(child);
				
				String value = ""; 
			//NodeList children = node.getChildNodes(); 
				for(int i = 0; i < alllist.getLength(); i++ ) 
				{ 
  				Node ci = alllist.item(i); 
 				 	if( ci.getNodeType() == Node.TEXT_NODE ) 
				 	{ 
    				value = value + ci.getNodeValue();
						System.out.println(">> "+value);
  				} 
					else if ( ci.getNodeType() == Node.CDATA_SECTION_NODE )
					{ 
    				value = value + ci.getNodeValue();
						System.out.println(">> "+value);
  				}
					else if ( ci.getNodeType() == Node.ELEMENT_NODE )
					{ 
						printAttributes(ci);
    				value = value + ci.getNodeValue();
						System.out.println(">> "+value);
  				}
					
					else
					{
						System.out.println("kinda node : "+ ci.getNodeType() );
					}
					
				} 
				

   

					//////////////////////////
					
				//	 System.out.println( extractNodeValue(document, "project", "sur") );
					// ContractorLastNamePrinter(document);
 
        } 
				catch (SAXException sxe)
				{
           // Error generated during parsing)
           Exception  x = sxe;
           if (sxe.getException() != null)
               x = sxe.getException();
           x.printStackTrace();

        } 
	//			catch (ParserConfigurationException pce) 
	//			{
   //         // Parser with specified options can't be built
   //         pce.printStackTrace();

    //    }
				catch (IOException ioe) {
           // I/O error
           ioe.printStackTrace();
        }
    } // main
		
		
		private static void printAttributes(Node thisNode) {
    System.out.print("(");
    NamedNodeMap attribs = thisNode.getAttributes(); 
    int numAttribs = attribs.getLength();
    for(int i=0; i < attribs.getLength(); i++){
      Node attrib = attribs.item(i);
      if(i>0){System.out.print(",");}
      System.out.print(attrib.getNodeName());
      System.out.print("=\"");
      System.out.print(attrib.getNodeValue());
      System.out.print("\"");
    }
    System.out.print(")");
  }

}
