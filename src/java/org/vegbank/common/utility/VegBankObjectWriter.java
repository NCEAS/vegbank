/*
 *	'$RCSfile: VegBankObjectWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-10 00:33:27 $'
 *	'$Revision: 1.2 $'
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

/**
 * @author farrell
 */

public class VegBankObjectWriter
{
	
	public VegBankObjectWriter(Object object)
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

	/**
		 * Gets the fieldname from a get method
		 * i.e. getFieldName returns FieldName
		 * 
		 * @param methodName
		 * @param suffixToAdd 
		 * @return String -- the database fieldName
		 */
	protected String getFieldName(String methodName, String suffixToAdd)
	{
		System.out.println(" VegBankObjectWriter > " + methodName);
		// Remove the first 3 chars
		String result = methodName.substring(3);
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
	protected boolean isGetMethod(Method method, String returnType)
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

	protected boolean isGetMethod(Method method) throws ClassNotFoundException
	{
		boolean result = false;
		// Check for the get method naming convention
		if ( method.getName().startsWith("get"))
		{
				result = true;
		}
		return result;
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

	protected Object object = null;

	protected Method[] methods = null;

	protected String className = null;

}
