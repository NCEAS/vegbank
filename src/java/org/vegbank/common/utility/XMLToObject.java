/*
 *	'$RCSfile: XMLToObject.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-04-16 17:37:44 $'
 *	'$Revision: 1.1 $'
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

package org.vegbank.common.utility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads an XML file and creates Vegbank Objects.
 * 
 * @author farrell
 */

public class XMLToObject
{
	private File file;
	private static final String DATA_MODEL_PACKAGE =
		"org.vegbank.common.model.";
	private Vector generatedObjects = new Vector();

	public XMLToObject(String fileName)
		throws ParserConfigurationException, SAXException, IOException
	{
		this.file = new File(fileName);

		// Parse the XML
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);

		this.handleNode(doc, null);
	}

	/**
	 * Handles all the DOM nodes of the input XML document. It calls
	 * itself recursively until it is finished the Node and its children.
	 * 
	 * @param node - DOM node to process
	 * @param parent - the obect created from the parent of this node
	 */
	private void handleNode(Node node, Object parent)
	{
		// I am only interested in elements nodes that have cognates in the vegbank object model
		int type = node.getNodeType();
		Object object = null;

		if (Node.ELEMENT_NODE == type)
		{
			try
			{
				object = this.handleElementNode(parent, node);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (type == Node.DOCUMENT_NODE || type == Node.ELEMENT_NODE)
		{
			// Recurse down
			for (Node child = node.getFirstChild();
				child != null;
				child = child.getNextSibling())
			{
				if (child.getNodeType() == Node.ELEMENT_NODE)
				{
					// If no object created stay with parent
					if (object == null)
					{
						System.out.println("No object created using parent");
						object = parent;
					}
//					System.out.println(
//						"XMLToObject > Recursively calling handleNode("
//							+ child
//							+ ", "
//							+ object
//							+ ")");
					handleNode(child, object);
				}
			}
		}
	}

	/**
	 * Searchs through all the methods of an object looking for the set method 
	 * that matchs the memberName parameter i.e. setMemberName()
	 * 
	 * @param object
	 * @param memberName
	 * @return Method - the set method for the member
	 */
	private Method getSetMethod(Object object, String memberName)
	{
		Method matchingMethod = null;
		// Ignore Case
		memberName = memberName.toLowerCase();
		Method[] methods = object.getClass().getMethods();

		// Search all the methods for a matching set method
		for (int i = 0; i < methods.length; i++)
		{
			String methodName = methods[i].getName().toLowerCase();
			//System.out.println(methodName + "===" + memberName);

			if (methodName.equals("set" + memberName))
			{
				//System.out.println("===" + methodName);
				matchingMethod = methods[i]; // Got it !!
			}
		}
		//System.out.println(matchingMethod);
		return matchingMethod;
	}

	//	private Object createObject(String className)
	//	{
	//		Object object = null;
	//		try
	//		{
	//			Class classDefinition = Class.forName(className);
	//			object = classDefinition.newInstance();
	//		}
	//		catch (Exception e)
	//		{
	//			e.printStackTrace();
	//		}
	//		return object;
	//	}

	public AbstractList getGeneratedObjects()
	{
		return this.generatedObjects;
	}

	/**
	 * Seaches the vegbank object model for a a classname
	 * 
	 * @param className
	 * @return boolean - is there a corresponding class
	 */
	public static boolean existsInVegbankObjectModel(String className)
	{
		boolean result = false;
		try
		{
			//System.out.println("--->" + className);
			Class classDefinition = Class.forName(className);
			result = true;
		}
		catch (ClassNotFoundException e)
		{
			// No object of this name in the datamodel;
		}
		//System.out.println("--->" + result);
		return result;
	}

	/**
	 * Gets the value of a Text Node
	 * 
	 * @param node
	 * @return String -- value of the text node
	 */
	private String getText(Node node)
	{
		StringBuffer text = new StringBuffer();
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			if (nodes.item(i).getNodeType() == Node.TEXT_NODE)
			{
				text.append(nodes.item(i).getNodeValue());
			}
		}
		return text.toString();
	}

	/**
	 * Capitalizes the first letter of a string. Leaves the rest of the Sting alone
	 * @param text
	 * @return String 
	 */
	private String upperCaseFirstLetter(String text)
	{
		String result = text.substring(0, 1).toUpperCase() + text.substring(1);
		//System.out.println("XMLToObject > " +result);
		return result;
	}

	private boolean isStringFirstParameter(Method method)
	{
		String nameOfFirstParameter = method.getParameterTypes()[0].getName();
		boolean result = nameOfFirstParameter.equals("java.lang.String");
		return result;
	}

	private Object handleElementNode(Object parent, Node node)
		throws Exception
	{
		Object newObject = null;
		String fullyQualifiedName = getFullyQualifiedName(node);
		// no parent Object
		if (parent == null)
		{
			// Is this handled in the vegbank object model
			if (existsInVegbankObjectModel(fullyQualifiedName))
			{
				// create the object
				newObject = Utility.createObject(fullyQualifiedName);
				this.generatedObjects.addElement(newObject);
				System.out.println("XMLToObject > Added new object to List");
			}
		}
		// Element Node has a parent
		else if (parent != null)
		{
			Method method = getSetMethod(parent, node.getNodeName());

			if (method != null )
			{
				newObject = this.callSetMethod(parent, method, node);
			}
			// No set method exists but there is a cognate object 
			else if (existsInVegbankObjectModel(fullyQualifiedName))
			{
				// Get the object and search it for a set for the parent
				newObject = Utility.createObject(fullyQualifiedName);
				this.generatedObjects.addElement(newObject);
				System.out.println("XMLToObject > Added new object to List");
				
				Method method2 =
					getSetMethod(newObject, parent.getClass().getName());

				if (method2 != null)
				{
					// case: add parent object to the set method of XML child
					Object[] params = { parent };
					method2.invoke(newObject, params);
				}
			}
		}
		return newObject;
	}

	private Object callSetMethod(
		Object objectWithSetMethod,
		Method setMethod,
		Node node)
		throws Exception
	{
		Object newObject = null;
		String fullyQualifiedName = getFullyQualifiedName(node);
		//System.out.println("---> " + fullyQualifiedName);
		if (existsInVegbankObjectModel(fullyQualifiedName) )
		{
			System.out.println("XMLToObject > creating Object " + fullyQualifiedName);
			// create the object and add it to the parent
			newObject= Utility.createObject(fullyQualifiedName);

			Object[] params = { newObject };

			setMethod.invoke(objectWithSetMethod, params);
		}
		// If Method takes a string argument
		if (this.isStringFirstParameter(setMethod))
		{
			String value = getText(node);
			Object[] params = { value };
			setMethod.invoke(objectWithSetMethod, params);
		}
		return newObject;
	}

	/**
	 * Gets the fullyQualified Object name from a node. Assumes that node name
	 * is an unqualified className.
	 * 
	 * @param node 
	 * @return String -- fully qualifiedClassName
	 */
	private String getFullyQualifiedName(Node node)
	{
		String result =
			DATA_MODEL_PACKAGE + upperCaseFirstLetter(node.getNodeName());
		return result;

	}
}
