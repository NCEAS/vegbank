/*
 *	'$RCSfile: VBObjectUtils.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-06-30 20:08:17 $'
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
import java.util.Collection;
import java.util.Vector;

/**
 * @author farrell
 */

public class VBObjectUtils
{

	public static final String DATA_MODEL_PACKAGE = "org.vegbank.common.model.";
	
	/**
	 * Seaches the vegbank object model for a a classname. If a fully qualified 
	 * name is passed only the  className is used. Only the datamodel package 
	 * is searched.
	 * 
	 * @param className
	 * @return boolean - is there a corresponding class
	 */
	public static boolean existsInVegbankObjectModel(String className)
	{
		boolean result = false;
		try
		{
			// Strip any given package name off and use the default package
			className = DATA_MODEL_PACKAGE + getUnQualifiedName(className);
			Class classDefinition = Class.forName(className);
			result = true;
		}
		catch (ClassNotFoundException e)
		{
			// No object of this name in the datamodel;
		}
		//System.out.println("VBObjectUtils > Does '" + className + "' exist in datamodel: " + result);
		return result;
	}

	/**
		 * Gets the fieldname from a get method
		 * i.e. getFieldName returns FieldName
		 * 
		 * @param methodName
		 * @param suffixToAdd 
		 * @return String -- the database fieldName
		 */
	public static String getFieldName(String methodName, String suffixToAdd)
	{
		//System.out.println(" VegBankObjectWriter > " + methodName);
		// Remove the first 3 chars
		String result = methodName.substring(3);
		
		// lowerCase the first Character 
		result = result.substring(0,1).toLowerCase() + result.substring(1);
		
		if ( suffixToAdd != null)
		{
			result = result +suffixToAdd;
		}
		return result;
	}

	/**
		 * @param method
		 * @return boolean
		 */
	public static boolean isGetMethod(Method method, String returnType)
	{
		boolean result = false;
		//System.out.println("++++++>>>> " + returnType + " & " + method.getReturnType() );
		// Check for the get method naming convention
		try
		{
			if ( isGetMethod(method))
			{
				if ( method.getReturnType().toString().equals(returnType) )
				{
					//System.out.println("match " + returnType + " & " + method.getReturnType() );
					result = true;
				}
				else if ( method.getReturnType().equals( Class.forName(returnType))  )
				{
					result = true;
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			// Return false here
		}
		return result;
	}

	public static boolean isGetMethod(Method method) 
	{
		return method.getName().startsWith("get");
	}
	
	public static String getUnQualifiedName( String qualifiedName )
	{
		String unQualifiedName = qualifiedName;
		if (qualifiedName.lastIndexOf('.') > 0) 
		{
			unQualifiedName = qualifiedName.substring(qualifiedName.lastIndexOf('.')+1);  
		}
		return unQualifiedName;
	}

	/**
	 * @param method
	 * @return
	 */
	public static boolean isSetMethod(Method method)
	{
		return method.getName().startsWith("set");
	}

	/**
	 * Gets the fullyQualified Object name from a node. Assumes that node name
	 * is an unqualified className.
	 * 
	 * @param node 
	 * @return String -- fully qualifiedClassName
	 */
	public static String getFullyQualifiedName(String className)
	{
		String result = 
			DATA_MODEL_PACKAGE + Utility.upperCaseFirstLetter(className);
		return result;
	}

	/**
	 * Takes an Object and returns all the the set methods that take the 
	 * parameter type
	 * 
	 * @param object
	 * @param parameter
	 * @return
	 */
	public static Collection getSetMethods(Object object, String parameter)
	{
		Vector matchingMethods = new Vector();
		
		//System.out.println("match " + object + " & " +  parameter );
		
		Method[] methods = object.getClass().getMethods();
		for (int i=0; i < methods.length ; i++ )
		{
			Method method = methods[i];
			// Process method
			//System.out.println("match " +  isSetMethod(method) + " & " +  method.getParameterTypes().length  ); //+ " & " + method.getParameterTypes()[0].toString() );
			if ( isSetMethod(method) && method.getParameterTypes().length == 1 
							&&  method.getParameterTypes()[0].getName().equals(parameter)
					)
			{
				matchingMethods.add(method);
			}
		}
		return matchingMethods;	
	}

	/**
	 * Given a fieldname returns the key name according to the naming convention
	 * used in vegbank. i.e. FIELDNAME_ID
	 * @param fieldName
	 * @return
	 */
	public static String getKeyName( String fieldName )
	{
		return fieldName + "_ID";
	}
}
