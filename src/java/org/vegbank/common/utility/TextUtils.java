/*
 *	'$RCSfile: TextUtils.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-11-25 17:48:23 $'
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
 *
 * 
 * DESCRIPTION:
 * Implements various text formatting methods.
 * 
 * @author anderson
 */

package org.vegbank.common.utility;

import java.io.*;
import java.util.*;


public class TextUtils {

	public static final int MAX_CHARS_PER_LINE = 69;


	public static String formatBody(String body, boolean useHTML)
			throws IOException {

		StringBuffer formatted = new StringBuffer();

		BufferedReader br = new BufferedReader(
				new StringReader(body));

		String line, lhs, rhs, newLine;
		int pos;
		while ((line=br.readLine()) != null) {

			rhs = line;
			while (rhs.length() > MAX_CHARS_PER_LINE) {

				// find the new end of the line
				pos = rhs.lastIndexOf(" ", MAX_CHARS_PER_LINE);

				if (pos == -1) {
					// This long line has no spaces at all
					pos = MAX_CHARS_PER_LINE;
				}

				// get the new LHS
				lhs = rhs.substring(0,pos);

				// get the new RHS
				if (rhs.length() > pos) {
					rhs = rhs.substring(pos+1);
				} 

				// write the current line
				formatted.append(lhs);
				formatted.append("\n");
				if (useHTML)
					formatted.append("<BR>");
			} 

			// write the current line
			formatted.append(rhs);
			formatted.append("\n");
			if (useHTML)
				formatted.append("<BR>");
		}

		return formatted.toString().trim();
	}


	/**
	 * Comma Separated Values.
	 */
	public static String makeListCSV(List strings) {
		StringBuffer sb = new StringBuffer();
		Iterator it = strings.iterator();
		String value;
		boolean first = true;
		while (it.hasNext()) {
			if (first) first = false;
			else sb.append(", ");

			value = (String)it.next();
			sb.append(value);
		}

		return sb.toString();
	}


	/**
	 * Line Separated Values.
	 */
	public static String makeListLSV(List strings) {
		StringBuffer sb = new StringBuffer(1024);
		Iterator it = strings.iterator();
		String value;
		while (it.hasNext()) {
			value = (String)it.next();
			sb.append(value).append("\n");
		}

		return sb.toString();
	}


	/**
	 * xxx yyy zzz becomes xxxyyyzzz.
	 */
	public static String removeSpaces(String orig) {
		StringTokenizer st = new StringTokenizer(orig, " ");
		StringBuffer sb = new StringBuffer(orig.length());
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());		
		}
		if (sb.toString().length() == 0)
			return orig;
		return sb.toString();
	}

}
