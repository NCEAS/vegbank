/*
 *	'$RCSfile: PermComparison.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-12-10 19:38:15 $'
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

import java.io.*;
import java.sql.*;
import java.util.*;
import org.vegbank.common.utility.LogUtility;

/**
 * Utility class for comparing a user's permission
 * to a requested resource.
 *
 * @author anderson
 */
public class PermComparison {

	private static ResourceBundle res = null;
	private static Map roles = new HashMap(); 


	/**
	 *
	 */
	public PermComparison() {
		res = ResourceBundle.getBundle("general");
		String key;
		
		// load the roles
		for (Enumeration e = res.getKeys(); e.hasMoreElements() ;) {
			key = (String)e.nextElement();
			if (key.startsWith("perms.role.")) {
				roles.put(key.substring(11), res.getString(key));
			}
		}
	}

	/**
	 * @return true if given sum of roles meet the minimum required roles.
	 */
	public static boolean matchesAll(String required, String given) {
		return matchesAll(parsePermissions(required), parsePermissions(given));
	}

	/**
	 * @return true if given sum of roles meet the minimum required roles.
	 */
	public static boolean matchesAll(int required, int given) {
		LogUtility.log("PermComparison.matchesAll("+required+","+given+"): " +
				(required & given));
				
	/*	
		if (required & given == required) {
			return false;
		} else {
			return true;
		}
	*/
		return false;
	}
	
	/**
	 * NOT IMPLEMENTED YET.
	 */
	public static boolean matchesOne(int required, int given) {
		/*
		if (required | given == ???) {
			return false;
		} else {
			return true;
		}
		*/
		return false;
	}
	
	/**
	 *
	 * @param csv Comma separated list of role names found in the 
	 * properties file.
	 */
	public static int parsePermissions(String csv) {
		StringTokenizer st = new StringTokenizer(csv, ",");
		String tmpRole;
		int roleSum = 0;

		while (st.hasMoreTokens()) {
			tmpRole = st.nextToken();
			try {
				roleSum += Integer.parseInt( 
						res.getString("perms.roles." + st.nextToken()));

			} catch (Exception ex) {
				LogUtility.log("PermComparison: bad config value for role " +
						tmpRole);
			}

		}
		return roleSum;
		
	}
	
	//////////////////////////////////////////////////////////
	// MAIN
	//////////////////////////////////////////////////////////
	/**
	 *
	 */
	public static void main(String[] args) {
		int sum;
		String csv;
		csv = "guest, registered, certified, pro, dba";
		sum = PermComparison.parsePermissions(csv);
		System.out.println(csv + " = " + sum);
		System.out.println(PermComparison.matchesAll(3, sum));

		csv = "guest, registered";
		sum = PermComparison.parsePermissions(csv);
		System.out.println(csv + " = " + sum);
		System.out.println(PermComparison.matchesAll(7, sum));

	}

}
