/*
 *	'$RCSfile: ObjectToXML.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 20:22:12 $'
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

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

/**
 * Creates an XML from a java object.
 * 
 * This uses the getXXX methods to generate  XML nodenames and values.
 * i.e. for getXXX <XXX> getXXX</XXX>
 * 
 * @author farrell
 */

public class ObjectToXML
{
	private Object object = null;
	private Method[] methods = null;
	private String className = null;
	private int indentCount = 0;
	private String indent = "  ";
	
	public ObjectToXML(Object object)
	{
		this.object = object;
		
		Class theClass = object.getClass();
		methods = theClass.getDeclaredMethods();
		
		// Get qualified name
		className = theClass.getName();
		// Get unqualified name
		if (className.lastIndexOf('.') > 0) 
		{
			className = className.substring(className.lastIndexOf('.')+1);  
		}
	}
	
	public ObjectToXML(Object object, int indentCount)
	{
		this(object);
		this.indentCount = indentCount;
	}

	
	public String getXML()
	{
		StringBuffer result = new StringBuffer();
		result.append( getIndent()  + "<" + className + ">\n");	
		for (int i=0; i < methods.length ; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();
			//System.out.println(methodName);		
			
			try
			{
				if ( isGetMethod(method, "java.lang.String") )
				{
					indentCount++;
					String fieldName = this.getFieldName(methodName);
					String fieldValue = (String) method.invoke(object, null);
					String xmlNodeString = "<" + fieldName + ">" + fieldValue + "</" + fieldName + ">\n";				
					result.append( getIndent()  + xmlNodeString);	
					indentCount--;
				}
				else if ( isGetMethod(method, "java.util.Vector") )
				{
					indentCount++;
					Vector vector = (Vector) method.invoke(object, null);
					Iterator iter = vector.iterator();
					while (iter.hasNext())
					{
						Object obj = iter.next();
						ObjectToXML o2x = new ObjectToXML(obj, indentCount);
						result.append( o2x.getXML() );
					}
					indentCount--;
				}
				else if ( isGetMethod(method) )
				{
					indentCount++;
					Object obj = (Object) method.invoke(object, null);
					ObjectToXML o2x = new ObjectToXML(obj, indentCount);
					result.append( o2x.getXML() );
					indentCount--;
				}
				else 
				{
					// Not of interest
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.append( getIndent() + "</" + className + ">\n");	
		return result.toString();	
	}
	
	/**
	 * Gets the fieldname from a get method
	 * i.e. getFieldName returns FieldName
	 * 
	 * @param methodName
	 * @return String
	 */
	private String getFieldName(String methodName)
	{
			return methodName.substring(3);
	}

	/**
	 * @param method
	 * @return boolean
	 */
	private boolean isGetMethod(Method method, String returnType ) 
		throws ClassNotFoundException
	{
		boolean result = false;
		// Check for the get method naming convention
		if ( isGetMethod(method))
		{
			if ( method.getReturnType().equals( Class.forName(returnType))  )
			{
				result = true;
			}
		}
		return result;
	}
	
	private boolean isGetMethod(Method method) throws ClassNotFoundException
	{
		boolean result = false;
		// Check for the get method naming convention
		if ( method.getName().startsWith("get"))
		{
				result = true;
		}
		return result;
	}
	
	private String getIndent()
	{
		StringBuffer sb = new StringBuffer();
		for ( int i =0; i < indentCount ; i++)
		{
			sb.append(indent);
		}
		return sb.toString();
	}

}
