/*
 *	'$RCSfile: ObjectToXML.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-06-30 20:02:59 $'
 *	'$Revision: 1.3 $'
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

public class ObjectToXML extends VegBankObjectWriter
{
	
	private int indentCount = 0;
	private String indent = "  ";
	
	public ObjectToXML(Object object)
	{
		super(object);
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
				if ( VBObjectUtils.isGetMethod(method, "java.lang.String") )
				{
					indentCount++;
					String fieldName = VBObjectUtils.getFieldName(methodName, "");
					String fieldValue = (String) method.invoke(object, null);
					String xmlNodeString = "<" + fieldName + ">" + fieldValue + "</" + fieldName + ">\n";				
					result.append( getIndent()  + xmlNodeString);	
					indentCount--;
				}
				else if ( VBObjectUtils.isGetMethod(method, "java.util.Vector") )
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
				else if ( VBObjectUtils.isGetMethod(method) )
				{
					indentCount++;
					Object obj = (Object) method.invoke(object, null);
					if (obj != null)
					{
						ObjectToXML o2x = new ObjectToXML(obj, indentCount);
						result.append( o2x.getXML() );
						indentCount--;
					}
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
