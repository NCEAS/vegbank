/*
 *	'$RCSfile: LogUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-10-29 04:04:58 $'
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

import java.util.*;
import java.util.logging.*;


/**
 * @author anderson
 */

public class LogUtility
{
	private static final boolean enable = Boolean.valueOf(ResourceBundle
			.getBundle("general").getString("logging.enable")).booleanValue();
	

	public static void log(Object message) {
		if (enable && message != null) {
			System.out.println("LU--"+message.toString());
		}
	}
	
	public static void log(Object message, java.lang.Throwable t) {
		if (enable) {
			String tmp = "";
			if (message != null) {
				tmp += message.toString();
			}
			if (t != null) {
				tmp += "\nEXCEPTION: " + t.toString();
			}
			System.out.println(tmp);
		}
	}
	
}
