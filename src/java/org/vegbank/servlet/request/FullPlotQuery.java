/*
 *	'$RCSfile: FullPlotQuery.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-11 23:14:05 $'
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
 
package org.vegbank.servlet.request;

import java.util.StringTokenizer;
import java.util.Vector;

import org.vegbank.databaseAccess.dbAccess;

/**
 * @author farrell
 */

public class FullPlotQuery
{
	
	public void execute(
		QueryResult qr,
		String plotId)
	{

		System.out.println(
			"FullPlotQuery > requesting full data set for plot: " + plotId);
					
		dbAccess dba = new dbAccess();
				
		Vector vec = new Vector();
		//check to see how many plots are being requested -- if there are 
		//commas then there are multiple
		if (plotId.indexOf(",") > 0)
		{
			System.out.println("DataRequestServlet plot collection: " + plotId);
			StringTokenizer tok = new StringTokenizer(plotId, ",");
			while (tok.hasMoreTokens())
			{
				String buf = tok.nextToken();
				vec.addElement(buf);
			}
			qr.setResultsTotal(vec.size());
		}
		else
		{
			vec.addElement(plotId);
			qr.setResultsTotal(1);
		}
		qr.setXMLString( dba.getMultipleVegBankPlotXMLString(vec) );
		System.out.println("FullPlotQuery > done writing "+ vec.size()+ " plots. ");
	}

}
