/*
 *	'$RCSfile: VegBankObjectWriter.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-25 19:31:34 $'
 *	'$Revision: 1.4 $'
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

import org.vegbank.common.model.VBModelBean;

/**
 * @author farrell
 */

public class VegBankObjectWriter
{
	
	public VegBankObjectWriter(VBModelBean object)
	{
		this.object = object;
		
		Class theClass = object.getClass();
		methods = theClass.getDeclaredMethods();
		
		// Get unqualified name
		className = VBObjectUtils.getUnQualifiedName(theClass.getName());
	}

	protected VBModelBean object = null;

	protected Method[] methods = null;

	protected String className = null;

}
