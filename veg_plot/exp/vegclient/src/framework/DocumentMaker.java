/*
* Copyright (C) 2001 by Steve Litt
* 
* COMPLETE DOM WALKER
*
*/
import java.io.IOException; // Exception handling
import org.w3c.dom.*; // DOM interface
import org.apache.xerces.parsers.DOMParser; // Parser (to DOM)

/**************************************
class DocumentMaker encapsulates all parser dependent code.
If you change XML parsers, only this class
and the parser's import statement need be modified.
As written, DocumentMaker uses DOMParser from Apache.
**************************************/
class DocumentMaker 
{
	private Document doc;
	public Document getDocument () 
	{
		return(doc);
	}
	public DocumentMaker (String filename) 
	{
		try 
		{
			DOMParser dp = new DOMParser();
			dp.parse(filename);
			doc = dp.getDocument();
		}
		catch (Exception e) 
		{
			System.out.println("\nError: " + e.getMessage());
		}
	}
}

/**************************************
class NodeTypes encapsulates text names for the various node types.
Its asAscii method returns those strings according to its
nodeTypeNumber argument. It's a number to string translator.
**************************************/
class NodeTypes 
{
	private static String[] nodenames=
	{"ELEMENT","ATTRIBUTE","TEXT",
		"CDATA_SECTION","ENTITY_REFERENCE",
		"ENTITY","PROCESSING_INSTRUCTION",
		"COMMENT","DOCUMENT","DOCUMENT_TYPE",
		"DOCUMENT_FRAGMENT","NOTATION"
	};
	public static String asAscii(int nodeTypeNumber) 
	{
		return(nodenames[nodeTypeNumber-1]);
	}
}

/**************************************
class WhiteSpaceKiller's job is to walk the DOM
Document and delete any empty text nodes.
The tree is walked non-recursively using standard DOM
traversal methods. Once an empty text node is deleted,
the "checker" is moved back to the previous node to 
avoid attempts at calling DOM traversal methods on
a (now) null object.
**************************************/
class WhiteSpaceKiller 
{

	private Node checker; // like a checker that gets moved from 
	// square to square in a checkers game
	// points to "current" node

	WhiteSpaceKiller(Document doc) 
	{
		boolean ascending = false;
		Node previousChecker = null;
		try 
		{
			checker=doc.getDocumentElement();
			while (true) 
			{
				//*** TAKE ACTION ON NODE WITH CHECKER ***
				if ((!ascending) && (checker.getNodeType() == Node.TEXT_NODE)) 
				{ 
					String trimmedText = checker.getNodeValue().trim();
					if (trimmedText == "") 
					{
						checker.getParentNode().removeChild(checker);
						checker=previousChecker; //back to undeleted node
					}
				}
				previousChecker=checker;
				//*** GO DOWN IF YOU CAN ***
				if ((checker.hasChildNodes()) && (!ascending)) 
				{
					checker=checker.getFirstChild();
					ascending = false;
				}
				//*** OTHERWISE GO RIGHT IF YOU CAN ***
				else if (checker.getNextSibling() != null) 
				{
					checker=checker.getNextSibling();
					ascending = false;
				}
				//*** OTHERWISE GO UP IF YOU CAN ***
				else if (checker.getParentNode() != null) 
				{
					checker=checker.getParentNode();
					ascending = true;
				}
				//*** OTHERWISE YOU'VE ASCENDED BACK TO ***
				//*** THE DOCUMENT ELEMENT, SO YOU'RE DONE ***
				else 
				{
					break;
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("\nError: " + e.getMessage());
		}
	}
}

/**************************************
class DOMwalker's job is to walk the DOM
Document and print out each node's
type, its name, its value (null for Elements), and
in the case of elements, its attributes in parentheses.
The tree is walked non-recursively using standard DOM
traversal methods.
**************************************/
class DOMwalker 
{
	private Node checker; // like a checker that gets moved from 
	// square to square in a checkers game
	// points to "current" node

	private void indentToLevel(int level) 
	{
		for(int n=0; n < level; n++) 
		{
		System.out.print(" ");
		}
	}

	private void printAttributes(Node thisNode) 
	{
		System.out.print("(");
		NamedNodeMap attribs = thisNode.getAttributes(); 
		int numAttribs = attribs.getLength();
		for(int i=0; i < attribs.getLength(); i++)
		{
			Node attrib = attribs.item(i);
			if(i>0)
			{
				System.out.print(",");
			}
			System.out.print(attrib.getNodeName());
			System.out.print("=\"");
			System.out.print(attrib.getNodeValue());
			System.out.print("\"");
		}
		System.out.print(")");
	}

	private void printNodeInfo(Node thisNode) 
	{
		System.out.print(NodeTypes.asAscii(thisNode.getNodeType()) + " : " + 
		thisNode.getNodeName() + "  : " + 
		thisNode.getNodeValue() + "  : ");
		if(thisNode.getNodeType() == Node.ELEMENT_NODE) 
		{
			printAttributes(thisNode);
		}
		System.out.println();
	}


	public DOMwalker(Document doc) 
	{
		boolean ascending = false;
		int level = 1;
		System.out.println();
		try 
		{
			checker=doc.getDocumentElement();
			while (true) 
			{
				//*** TAKE ACTION ON NODE WITH CHECKER ***
				if (!ascending) 
				{
					indentToLevel(level);
					printNodeInfo(checker);
				}
				//*** GO DOWN IF YOU CAN ***
				if ((checker.hasChildNodes()) && (!ascending)) 
				{
					System.out.println("going down");
					checker=checker.getFirstChild();
					ascending = false;
					level++;
				}
				//*** OTHERWISE GO RIGHT IF YOU CAN ***
				else if (checker.getNextSibling() != null) 
				{
					System.out.println("going horiz");
					checker=checker.getNextSibling();
					ascending = false;
				}
				//*** OTHERWISE GO UP IF YOU CAN ***
				else if (checker.getParentNode() != null) 
				{
					System.out.println("going up");
					checker=checker.getParentNode();
					ascending = true;
					level--;
				}
				//*** OTHERWISE YOU'VE ASCENDED BACK TO ***
				//*** THE DOCUMENT ELEMENT, SO YOU'RE DONE ***
				else 
				{
					System.out.println("nowhere to go");
					break;
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("\nError: " + e.getMessage());
		}
	}
}

class ExciseXML
{
	private Node checker; // like a checker that gets moved from 
	// square to square in a checkers game
	// points to "current" node
	public ExciseXML(Document doc, String parentNode) 
	{
		boolean ascending = false;
		int level = 1;
		System.out.println();
		try 
		{
		//	checker=doc.getDocumentElement();
	NodeList nlist =doc.getElementsByTagName("sur");
	for(int i = 0; i < nlist.getLength(); i++ ) 
	{ 
  checker = nlist.item(i); 
			while (true) 
			{
				//*** GO DOWN IF YOU CAN ***
				System.out.println("node name: "+ checker.getNodeName() );
			
					if ((checker.hasChildNodes()) ) 
					{
						System.out.println("has childern");
						checker=checker.getFirstChild();
						printNodeInfo(checker);
						checker=checker.getNextSibling();
						printNodeInfo(checker);
						
						
						ascending = false;
					}
				else { break; }
				}
			}
	}
			catch (Exception e) 
			{
				System.out.println("\nError: " + e.getMessage());
			}
	}
			
		private void printNodeInfo(Node thisNode) 
		{
			System.out.print(NodeTypes.asAscii(thisNode.getNodeType()) + " : " + 
			thisNode.getNodeName() + "  : " + 
			thisNode.getNodeValue() + "  : ");
			if(thisNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				printAttributes(thisNode);
			}
			System.out.println();
		}
		
		private void printAttributes(Node thisNode) 
		{
		System.out.print("(");
		NamedNodeMap attribs = thisNode.getAttributes(); 
		int numAttribs = attribs.getLength();
		for(int i=0; i < attribs.getLength(); i++)
		{
			Node attrib = attribs.item(i);
			if(i>0)
			{
				System.out.print(",");
			}
			System.out.print(attrib.getNodeName());
			System.out.print("=\"");
			System.out.print(attrib.getNodeValue());
			System.out.print("\"");
		}
		System.out.print(")");
	}

}

/**************************************
Class Hello is the repository of this program's main routine.
It removes empty text nodes, then walks the DOM tree and
prints out the DOM tree's info.
**************************************/
class Hello 
{
	public static void main(String[] args) 
	{
		String filename = args[0];
		System.out.print("Walking XML file " + filename + " ... ");
		DocumentMaker docMaker = new DocumentMaker(filename);
		Document doc = docMaker.getDocument();
		try 
		{
			WhiteSpaceKiller wpc = new WhiteSpaceKiller(doc);
			DOMwalker walker = new DOMwalker(doc);
			ExciseXML ex = new ExciseXML(doc, "name");
		}
		catch (Exception e) 
		{
			System.out.println("\nError: " + e.getMessage());
		}
	}
}

