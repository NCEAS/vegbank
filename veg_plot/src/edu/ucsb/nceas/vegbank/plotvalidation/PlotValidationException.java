/**
 *  '$RCSfile: PlotValidationException.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2003-01-02 16:28:59 $'
 * '$Revision: 1.2 $'
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

package edu.ucsb.nceas.vegbank.plotvalidation;
import java.io.*;
import java.lang.*;
import java.util.*;

public class PlotValidationException extends RuntimeException
{
	private Exception hiddenException;
	
	// constructor 
	public PlotValidationException()
	{
		super();
		System.out.println("PlotValidationException > no message");
	}
	
	// constructor 
	public PlotValidationException(String s)
	{
		super(s);
		System.out.println("PlotValidationException > message: " + s);
	}
	
	// constructor 
	public PlotValidationException(String error, Exception excp)
	{
		super(error);
		System.out.println("PlotValidationException > error message: " + error);
		System.out.println("PlotValidationException > exception: " + excp);
	}
	
}