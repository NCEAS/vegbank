/**
 * class used to parse xml documents in various ways
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: farrell $'
 *  '$Date: 2003-05-07 02:45:51 $'
 * 	'$Revision: 1.9 $' 
 */

package xmlresource.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLparse
{

	//private static Node checker;
	private Document doc;
	private DocumentBuilderFactory dbf = null;
	private DocumentBuilder db = null;

	/**
	 * main method for testing and for use in standalone parsing
	 *
	 */
	public static void main(String[] args)
	{
		XMLparse xp = new XMLparse();
		if (args.length < 2)
		{
			System.out.println(
				"Usage: \n java XMLparse filename nodeName or \n"
					+ " java XMLparse fileName parentNode desiredNode \n"
					+ " java XMLpares [option], fileName, string \n"
					+ " options: -pathValue, -pathContent \n");
		}
		else
			if (args.length == 2)
			{
				String filename = args[0];
				String node = args[1];
				//	DocumentMaker docMaker = new DocumentMaker(filename);
				Document doc = xp.getDocument(filename);
				//		DOMwalker walker = new DOMwalker(doc);
				System.out.println(xp.getNodeValue(doc, node));
			}
			else
				if (args.length == 3)
				{
					if (args[0].equals("-pathValue"))
					{
						String option = args[0];
						String filename = args[1];
						String path = args[2];
						Document doc = xp.getDocument(filename);
						System.out.println(xp.getValuesForPath(doc, path).toString());
					}
					else
						if (args[0].equals("-pathContent"))
						{
							String option = args[0];
							String filename = args[1];
							String path = args[2];
							Document doc = xp.getDocument(filename);
							System.out.println(xp.getPathContent(doc, path).toString());
						}
						else
							if (args[0].equals("-nodePrint"))
							{
								String option = args[0];
								String filename = args[1];
								String nodeName = args[2];
								Document doc = xp.getDocument(filename);
								NodeList nodelist = doc.getElementsByTagName(nodeName);
								//get the first node
								Node node = nodelist.item(0);
								System.out.println(
									"printing the first occurence of: "
										+ nodeName
										+ "to a file named: test");
								xp.save(node, "test");
							}
							else
							{
								String filename = args[0];
								String parentNode = args[1];
								String childNode = args[2];
								Document doc = xp.getDocument(filename);
								System.out.println(
									xp.getChildNodeValue(doc, parentNode, childNode));
							}
				}
				else
				{

				}
		//		String filename = args[0];
		//		DocumentMaker docMaker = new DocumentMaker(filename);
		//		Document doc = docMaker.getDocument();
		try
		{
			//	WhiteSpaceKiller wpc = new WhiteSpaceKiller(doc);
			//	DOMwalker walker = new DOMwalker(doc);
			//	ExciseXML ex = new ExciseXML(doc, "name");
			//			ConfigXML cx = new ConfigXML(filename);
			//			System.out.println("config out: "+ cx.getValuesForPath("telephone/phoneNumber").toString() );
			//			XMLparse xp = new XMLparse();
			//			System.out.println( xp.getChildNodeValue(doc, "telephone", "phoneNumber") );
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
		}
	}

	/**
	* Assume that there is some parent node which has a subset of
	* child nodes that are repeated e.g. 
	* <parent>
	*    <name>xxx</name>
	*    <value>qqq</value>
	*    <name>yyy</value>
	*    <value>www</value>
	*    ...
	* </parent>
	*
	* this method will return a Hashtable of names-values of parent
	*/
	public Hashtable getHashtable(
		Document doc,
		String parentName,
		String keyName,
		String valueName)
	{
		String keyval = "";
		String valval = "";
		Hashtable ht = new Hashtable();
		NodeList nl = doc.getElementsByTagName(parentName);
		if (nl.getLength() > 0)
		{
			// always use the first parent
			NodeList children = nl.item(0).getChildNodes();
			if (children.getLength() > 0)
			{
				for (int j = 0; j < children.getLength(); j++)
				{
					Node cn = children.item(j);
					if ((cn.getNodeType() == Node.ELEMENT_NODE)
						&& (cn.getNodeName().equalsIgnoreCase(keyName)))
					{
						Node ccn = cn.getFirstChild(); // assumed to be a text node
						if ((ccn != null) && (ccn.getNodeType() == Node.TEXT_NODE))
						{
							keyval = ccn.getNodeValue();
						}
					}
					if ((cn.getNodeType() == Node.ELEMENT_NODE)
						&& (cn.getNodeName().equalsIgnoreCase(valueName)))
					{
						Node ccn = cn.getFirstChild(); // assumed to be a text node
						if ((ccn != null) && (ccn.getNodeType() == Node.TEXT_NODE))
						{
							valval = ccn.getNodeValue();
							ht.put(keyval, valval);
						}
					}
				}
			}
		}
		return ht;
	}

	/**
	* Gets the value(s) corresponding to a key string (i.e. the 
	* value(s) for a named parameter.
	* 
	* @param key 'key' is element name.
	* @return Returns a Vector of strings because may have repeated elements
	*/
	public Vector get(Document doc, String key)
	{
		NodeList nl = doc.getElementsByTagName(key);
		Vector result = new Vector();
		if (nl.getLength() < 1)
		{
			return result;
		}
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node cn = nl.item(i).getFirstChild(); // assume 1st child is text node
			if ((cn != null) && (cn.getNodeType() == Node.TEXT_NODE))
			{
				String temp = cn.getNodeValue();
				result.addElement(temp.trim());
			}
		}
		return result;
	}

	/**
	* Gets the value(s) corresponding to a key string (i.e. the 
	* value(s) for a named parameter. -- this uses a string representation
	* of the fileName
	* @param fileName
	* @param key 'key' is element name.
	* @return Returns a Vector of strings because may have repeated elements
	*/
	public Vector get(String fileName, String key)
	{
		Document doc = getDocument(fileName);
		NodeList nl = doc.getElementsByTagName(key);
		Vector result = new Vector();
		if (nl.getLength() < 1)
		{
			return result;
		}
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node cn = nl.item(i).getFirstChild(); // assume 1st child is text node
			if ((cn != null) && (cn.getNodeType() == Node.TEXT_NODE))
			{
				String temp = cn.getNodeValue();
				result.addElement(temp.trim());
			}
		}
		return result;
	}

	/**
	 * Gets the value(s) corresponding to a key string (i.e. the 
	 * value(s) for a named parameter.
	 * 
	 * @param key 'key' is element name.
	 * @param i zero based index of elements with the name stored in key
	 * @return Node
	 */
	public Node get(Document doc, String key, int i)
	{
		NodeList nl = doc.getElementsByTagName(key);
		System.out.println("XMLParse > get() list length: " + nl.getLength());
		Node resultNode;
		resultNode = nl.item(i);
		//		if (nl.getLength() < 1)
		//    {
		//      return result;
		//    }
		//    if (nl.getLength() < i)
		//    {
		//      return result;
		//    }
		//    Node cn = nl.item(i).getFirstChild(); // assume 1st child is text node
		//    if ((cn != null) && (cn.getNodeType() == Node.TEXT_NODE))
		//    {
		//      result = (cn.getNodeValue().trim());
		//    }
		return (resultNode);
	}

	/**
	* Gets the value(s) corresponding to a key string (i.e. the 
	* value(s) for a named parameter.
	* 
	* @param key 'key' is element name.
	* @param i zero based index of elements with the name stored in key
	* @return String value of the ith element with name in 'key'
	*/
	public String getNodeValue(Document doc, String key, int i)
	{
		NodeList nl = doc.getElementsByTagName(key);
		String result = null;
		if (nl.getLength() < 1)
		{
			return result;
		}
		if (nl.getLength() < i)
		{
			return result;
		}
		Node cn = nl.item(i).getFirstChild(); // assume 1st child is text node
		if ((cn != null) && (cn.getNodeType() == Node.TEXT_NODE))
		{
			result = (cn.getNodeValue().trim());
		}
		return result;
	}

	/**
	 * method that overloads the next method
	 *
	 */

	public void addNewElement(
		String fileName,
		String parentName,
		int i,
		String childName,
		String value)
	{
		Node newNode = null;
		Document doc = getDocument(fileName);
		newNode = addNewElement(doc, parentName, i, childName, value);
		save(newNode, "process.xml");
	}

	/**
	* Add a child node to the specified parent
	* 
	* @param parentName name of parent element
	* @param i index of parent element
	* @param childName element name of new child
	* @param value value of new child
	*/
	public Node addNewElement(
		Document doc,
		String parentName,
		int i,
		String childName,
		String value)
	{
		Node returnNode = null;
		NodeList nl = doc.getElementsByTagName(parentName);

		if (nl.getLength() > 0)
		{
			if (nl.getLength() <= i)
			{
				System.out.println("node list too short! ");
			}
			else
			{
				Node parent = nl.item(i);
				Node newElem = doc.createElement(childName);
				Node newText = doc.createTextNode(value);
				//add text to element
				newElem.appendChild(newText);
				//add newElem to parent
				parent.appendChild(newElem);
				returnNode = parent;
			}
		}
		return (returnNode);
	}

	/**
	* Save the configuration file
	*/
	public void save(Node root, String fileName)
	{
		saveDOM(root, fileName);
	}

	/**
	 * This method wraps the 'print' method to send DOM back to the
	 * XML document (file) that was used to create the DOM. i.e.
	 * this method saves changes to disk
	 * 
	 * @param nd node (usually the document root)
	 */
	public void saveDOM(Node nd, String fileName)
	{
		//	PrintWriter out;
		File outfile = new File(fileName);
		//   if (!outfile.canWrite()) 
		//	 {
		//			System.out.println( "Cannot Save configuration information to "+fileName);
		//    }
		//   else 
		//	 {
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			out.println("<?xml version=\"1.0\"?>");
			print(out, nd);
			out.close();
		}
		catch (Exception e)
		{

		}
		// 	out.println("<?xml version=\"1.0\"?>");
		//  	print(out, nd);
		//   	out.close(); 
		//}
	}

	/**
	* This method can 'print' any DOM subtree. Specifically it is
	* set (by means of 'out') to write the in-memory DOM to the
	* same XML file that was originally read. Action thus saves
	* a new version of the XML doc
	* 
	* @param node node usually set to the 'doc' node for complete XML file
	* re-write
	*/
	public void print(PrintWriter out, Node node)
	{

		// is there anything to do?
		if (node == null)
		{
			return;
		}

		int type = node.getNodeType();
		switch (type)
		{
			// print document
			case Node.DOCUMENT_NODE :
				{

					out.println("<?xml version=\"1.0\"?>");
					print(out, ((Document) node).getDocumentElement());
					out.flush();
					break;
				}

				// print element with attributes
			case Node.ELEMENT_NODE :
				{
					out.print('<');
					out.print(node.getNodeName());
					Attr attrs[] = sortAttributes(node.getAttributes());
					for (int i = 0; i < attrs.length; i++)
					{
						Attr attr = attrs[i];
						out.print(' ');
						out.print(attr.getNodeName());
						out.print("=\"");
						out.print(normalize(attr.getNodeValue()));
						out.print('"');
					}
					out.print('>');
					NodeList children = node.getChildNodes();
					if (children != null)
					{
						int len = children.getLength();
						for (int i = 0; i < len; i++)
						{
							print(out, children.item(i));
						}
					}
					break;
				}

				// handle entity reference nodes
			case Node.ENTITY_REFERENCE_NODE :
				{
					out.print('&');
					out.print(node.getNodeName());
					out.print(';');

					break;
				}

				// print cdata sections
			case Node.CDATA_SECTION_NODE :
				{
					out.print("<![CDATA[");
					out.print(node.getNodeValue());
					out.print("]]>");

					break;
				}

				// print text
			case Node.TEXT_NODE :
				{
					out.print(normalize(node.getNodeValue()));
					break;
				}

				// print processing instruction
			case Node.PROCESSING_INSTRUCTION_NODE :
				{
					out.print("<?");
					out.print(node.getNodeName());
					String data = node.getNodeValue();
					if (data != null && data.length() > 0)
					{
						out.print(' ');
						out.print(data);
					}
					out.print("?>");
					break;
				}
		}

		if (type == Node.ELEMENT_NODE)
		{
			out.print("</");
			out.print(node.getNodeName());
			out.print('>');
		}

		out.flush();

	} // print(Node)

	/** Returns a sorted list of attributes. */
	protected Attr[] sortAttributes(NamedNodeMap attrs)
	{

		int len = (attrs != null) ? attrs.getLength() : 0;
		Attr array[] = new Attr[len];
		for (int i = 0; i < len; i++)
		{
			array[i] = (Attr) attrs.item(i);
		}
		for (int i = 0; i < len - 1; i++)
		{
			String name = array[i].getNodeName();
			int index = i;
			for (int j = i + 1; j < len; j++)
			{
				String curName = array[j].getNodeName();
				if (curName.compareTo(name) < 0)
				{
					name = curName;
					index = j;
				}
			}
			if (index != i)
			{
				Attr temp = array[i];
				array[i] = array[index];
				array[index] = temp;
			}
		}

		return (array);

	} // sortAttributes(NamedNodeMap):Attr[]

	/** Normalizes the given string. */
	protected String normalize(String s)
	{
		StringBuffer str = new StringBuffer();

		int len = (s != null) ? s.length() : 0;
		for (int i = 0; i < len; i++)
		{
			char ch = s.charAt(i);
			switch (ch)
			{
				case '<' :
					{
						str.append("&lt;");
						break;
					}
				case '>' :
					{
						str.append("&gt;");
						break;
					}
				case '&' :
					{
						str.append("&amp;");
						break;
					}
				case '"' :
					{
						str.append("&quot;");
						break;
					}
				case '\r' :
				case '\n' :
					{
						// else, default append char
					}
				default :
					{
						str.append(ch);
					}
			}
		}

		return (str.toString());

	} // normalize(String):String

	/**
	 * gets the content of a tag in a given xml file with the given path
		* @param doc -- the document to extract the values from
	 * @param path the path to get the content from
		* @return vec -- a vector with the elements
	 */
	public Vector getPathContent(Document doc, String path)
	{
		Vector returnVec = new Vector();
		try
		{
			NodeList nlist = XPathAPI.selectNodeList(doc, path); 
			for (int i = 0; i < nlist.getLength(); i++)
			{
				returnVec.addElement(nlist.item(i).getNodeName());
			}
		}
		catch (Exception se)
		{
			System.err.println(se.toString());
			se.printStackTrace();
		}
		return returnVec;
	}

	/**
	 *  utility routine to return the value(s) of a node defined by
	 *  a specified XPath
	 *
	 * @param doc -- the document to extract the values from
	 * @param pathstring the path string which should look like:
	 *  /community/classCode where community would be the root node
	 *	@return vec -- a vector with the elements
	 *
	 */
	public Vector getValuesForPath(Document doc, String pathstring)
	{
		Vector val = new Vector();
		if (!pathstring.startsWith("/"))
		{
			pathstring = "//*/" + pathstring;
			System.out.println("XMLparse > pathstring: " + pathstring);
		}
		try
		{
			NodeList nl = null;
			nl = XPathAPI.selectNodeList(doc, pathstring);
			if ((nl != null) && (nl.getLength() > 0))
			{
				// loop over node list is needed if node is repeated
				for (int k = 0; k < nl.getLength(); k++)
				{
					Node cn = nl.item(k).getFirstChild();
					// assume 1st child is text node
					if ((cn != null) && (cn.getNodeType() == Node.TEXT_NODE))
					{
						String temp = cn.getNodeValue().trim();
						val.addElement(temp);
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Excpetion: " + e.getMessage());
		}
		return val;
	}

	/**
	 * method that takes a Node and the root name and creates a DOM 
	 * Document from it
	 * @param Node -- the node that is to be contained in the document
	 * @param root -- the name of the root
	 * @param doc -- the new document
	 */
	public Document createDocFromNode(Node n, String root)
	{
		Document document;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument(); // Create from whole cloth

			//Element root = (Element) document.createElement("rootElement"); 

			// Insert the root element node
			Element element = document.createElement(root);
			document.appendChild(element);

			// Insert a comment in front of the element node
			Comment comment = document.createComment("created by the XMLparse class");
			document.insertBefore(comment, element);

			// Add a text node to the element
			//element.appendChild(document.createTextNode("D"));

			Node newNode = document.importNode(n, true);
			element.appendChild(newNode);

			return (document);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (doc);
	}

	/**
	 * this method is used for parsing an xml file
	 * into a dom document
	 *
	 * @param filename -- the xml file to parse
	 *
	 */
	public Document getDocument(String filename)
	{
		DocumentBuilder db = this.getDocumentBuilder();
		try
		{
			doc = db.parse(filename);
		}
		catch (Exception e)
		{
			System.out.print("Could not create Document: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return (doc);
	}

	/**
	 * this method is used for parsing an xml string
	 * into a dom document
	 *
	 * @param s -- the string containing the xml doc
	 *
	 */
	public Document getDocumentFromString(String s)
	{
		System.out.println("XMLparse > creating a document from a string of lenght: " + s.length());
		StringReader sr = new StringReader(s);
		InputSource in = new InputSource(sr);
		Document doc = null;
		DocumentBuilder db = getDocumentBuilder();

		try
		{
			doc = db.parse(in);
		}
		catch (Exception e)
		{
			System.out.print("Could not create Document: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return (doc);
	}

	private DocumentBuilder getDocumentBuilder()
	{
		if (this.db == null)
		{
			// Need to create one then
			try
			{
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
			}
			catch (ParserConfigurationException pce)
			{
				System.out.print("Could not replace parser: ");
				System.out.println(pce.getMessage());
				pce.printStackTrace();
			}
		}
		return this.db;
	}
	
	/**
	 * method that returns the value of a child
	 * given as input both the child and parent 
	 * node name
	 *
	 * @param doc -- the parsed DOM document
	 * @param parentNode -- the name of the parent node
	 * @param childNode -- the name of the child node -- the name for which the
	 * 	value is desired
	 *
	 * limitations:  if there are multiple nodes with the parent node
	 * name then the value for the child of the first is the only one 
	 * that is returned
	 */
	public String getChildNodeValue(
		Document doc,
		String parentNode,
		String childNode)
	{
		String valString = null;
		Node checker;
		boolean ascending = false;
		int level = 1;
		System.out.println();
		try
		{
			//	checker=doc.getDocumentElement();
			NodeList nlist = doc.getElementsByTagName(parentNode);
			System.out.println("number of childern nodes: " + nlist.getLength());
			for (int i = 0; i < nlist.getLength(); i++)
			{
				checker = nlist.item(i);
				while (true)
				{

					if ((checker.hasChildNodes()))
					{

						checker = checker.getFirstChild();

						while (true)
						{
							checker = checker.getNextSibling();
							//	checker=checker.getNextSibling();
							//	checker=checker.getNextSibling();

							if (checker.getNodeName().equals(childNode))
							{
								System.out.println("isolated child: " + checker.getNodeName());
								//the value will be the child of this node make sure that it has a
								// child
								if ((checker.hasChildNodes()))
								{
									System.out.println("Good we found a child");
									System.out.println("bad value: " + checker.getNodeValue());
									//get the value into the string
									valString = checker.getFirstChild().getNodeValue();
									System.out.println("desired value: " + valString);

								}
								else
								{
									System.out.println(
										"THIS SHOULD NOT HAPPEN -- THERE SHOULD BE A CHILD");
								}
							}

						}

					}
					else
					{
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
		}
		return (valString);
	}

	/**
	 * method that overloads the next method, that uses as input a fileName
	 * and a node name
	 *
	 * @param filename -- name of the xml file
	 * @param node -- the name of the node for which the value is to be returned
	 * 	from
	 * @return nodeValue - the value for the node
	 *
	 * limitations:  if multiple nodes exist then the last one is the 
	 *	one that is returned
	 */
	public String getNodeValue(String fileName, String node)
	{
		String result = new String();
		Document doc = getDocument(fileName);
		//call the method which this method is overloading
		return (getNodeValue(doc, node));
	}

	/**
	 * method that returns the value of a node -- if 
	 * one exists or else it returns a null
	 *
	 * @param doc -- the parsed DOM document
	 * @param node -- the node for which the value is desired
	 *
	 * limitations:  if multiple nodes exist then the last one is the 
	 *	one that is returned
	 */
	public String getNodeValue(Document doc, String node)
	{
		String valString = null;
		Node checker;
		boolean ascending = false;
		int level = 1;
		//	System.out.println();
		try
		{
			//	checker=doc.getDocumentElement();
			NodeList nlist = doc.getElementsByTagName(node);
			//		System.out.println("number of childern nodes: " + nlist.getLength() );
			for (int i = 0; i < nlist.getLength(); i++)
			{
				checker = nlist.item(i);
				valString = checker.getFirstChild().getNodeValue();
			}
		}
		catch (Exception e)
		{
			System.out.print("XMLparse > Exception parsing node: " + node);
			System.out.println(" message: " + e.getMessage());
		}
		return (valString);
	}

	//method that returns the number of a given node in a document
	public int getNumberOfNodes(Document doc, String node)
	{
		int nodeNumber = 0;
		NodeList nlist = doc.getElementsByTagName(node);
		nodeNumber = nlist.getLength();
		return (nodeNumber);
	}

	/**
	* used to set a value corresponding to 'key'; value is changed
	* in DOM structure in memory
	* 
	* @param key 'key' is element name.
	* @param i index in set of elements with 'key' name
	* @param value new value to be inserted in ith key
	* @return boolean true if the operation succeeded
	*/
	public Document set(Document doc, String key, int i, String value)
	{
		boolean result = false;
		NodeList nl = doc.getElementsByTagName(key);
		if (nl.getLength() <= i)
		{
			result = false;
		}
		else
		{
			Node cn = nl.item(i).getFirstChild(); // assumed to be a text node
			if (cn == null)
			{
				// No text node, so append one with the value
				Node newText = doc.createTextNode(value);
				nl.item(i).appendChild(newText);
			}
			else
				if (cn.getNodeType() == Node.TEXT_NODE)
				{
					// found the text node, so change its value
					cn.setNodeValue(value);
				}
			result = true;
		}
		return (doc);
	}

	/**
	 * this method replaces the value of an element -- this 
	 */
	public void replaceElement(
		String fileName,
		String nodeName,
		String newNodeValue,
		String outFile)
	{
		try
		{
			Document doc = getDocument(fileName);
			Document newDoc = set(doc, nodeName, 0, newNodeValue);

			Node newNode = newDoc.getDocumentElement();

			saveDOM(newNode, outFile);

		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
		}

	}

}
