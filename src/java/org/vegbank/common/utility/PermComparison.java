/*
 *	'$RCSfile: PermComparison.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-26 20:42:46 $'
 *	'$Revision: 1.7 $'
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for comparing a user's permission
 * to a requested resource.
 *
 * @author anderson
 */
public class PermComparison {

	public static final String PROP_KEY_BASE = "perms.role.";
	
	private static Log log = LogFactory.getLog(PermComparison.class);
	private static ResourceBundle res = ResourceBundle.getBundle("general");
	private static Map roleNameMap = null;
	
	static {
		initRoleNames();
	}

	
	/**
	 * Builds the mapping of role names to numerical value.
	 */
	private static void initRoleNames() {
		// initialize the role names and numerical values
		roleNameMap = new HashMap();
		String property, role, value;
		Enumeration keys = res.getKeys();
		
		while (keys.hasMoreElements()) {
			property = (String)keys.nextElement();
			if (property.startsWith("perms.role.")) {
				// found a role property; parse it
				role = property.substring(PROP_KEY_BASE.length());
				value = res.getString(property);
				log.debug("Adding role " + role + "=" + value);
				roleNameMap.put(role, new Long(value));
				
			}
		}
	}

	/**
	 * Given a role name, returns its corresponding numerical value.
	 * @param role name or abbreviation
	 * @return numerical value of the given role
	 */
	public static long getRoleConstant(String role) {
		
		if (!Utility.isStringNullOrEmpty(role)) {
			return 0;
		}
		
		if (!role.startsWith("perms.role.")) {
			role = "perms.role." + role;
		}
		
		String value = res.getString(role);
		if (value == null) {
			return 0;
		}
		return Long.parseLong(value);
	}

	/**
	 * Given a numerical role sum, returns all corresponding
	 * role names.
	 * @param sum
	 * @return List of Strings containing full role names (not abbrev.)
	 */
	 public List getRoleNames(long sum) {
		 List list = new ArrayList();
		 Iterator nit = roleNameMap.keySet().iterator();
		 while (nit.hasNext()) {
			 String role = (String)nit.next();
			 if (matchesOne(role, sum)) {
				 list.add(role);
			 }
		 }
		 return list;
	 }
	
	/* =============================================================== */
	/**
	 * @param required comma separated list of required role names
	 * @param given comma separated list of given role names
	 * @return true if given list of comma separated roles 
	 * match ALL of the required roles.
	 */
	public static boolean matchesAll(String required, String given) {
		return matchesAll(parsePermissions(required), parsePermissions(given));
	}

	/**
	 * @param required integer sum of required roles
	 * @param given comma separated list of given role names
	 * @return true if given list of comma separated roles 
	 * match ALL of the required roles.
	 */
	public static boolean matchesAll(long required, String given) {
		return matchesAll(required, parsePermissions(given));
	}
	
	/**
	 * @param required comma separated list of required role names
	 * @param given integer sum of given roles
	 * @return true if given role sum matches ALL of the required roles.
	 */
	public static boolean matchesAll(String required, long given) {
		return matchesAll(parsePermissions(required), given);
	}
	

	/* =============================================================== */

	/**
	 * @param required comma separated list of required role names
	 * @param given comma separated list of given role names
	 * @return true if given list of comma separated roles 
	 * match at least ONE of the required roles.
	 */
	public static boolean matchesOne(String required, String given) {
		return matchesOne(parsePermissions(required), parsePermissions(given));
	}

	/**
	 * @param required integer sum of required roles
	 * @param given comma separated list of given role names
	 * @return true if given list of comma separated roles 
	 * match at least ONE of the required roles.
	 */
	public static boolean matchesOne(long required, String given) {
		return matchesOne(required, parsePermissions(given));
	}
	
	/**
	 * @param required comma separated list of required role names
	 * @param given integer sum of given roles
	 * @return true if given role sum matches at least ONE of the required roles.
	 */
	public static boolean matchesOne(String required, long given) {
		return matchesOne(parsePermissions(required), given);
	}
	

	/* =============================================================== */


	/**
	 * @param required integer sum of required roles
	 * @param given integer sum of given roles
	 * @return true if given sum of roles match at least all of the 
	 * required roles.
	 */
	public static boolean matchesAll(long required, long given) {
		//log.debug("PermComparison.matchesAll("+required+","+given+"): " + (required & given));
				
		if ((required & given) != required) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @return true if given sum of roles meet at least one of 
	 * the required roles.
	 */
	public static boolean matchesOne(long required, long given) {
		if ((required & given) == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @param csv Comma separated list of role names found in the 
	 * properties file.
	 * @return long added all given roles' integer values
	 */
	public static long parsePermissions(String csv) {
		if (csv == null) {
			return 0;
		}
		
		StringTokenizer st = new StringTokenizer(csv, ", ");
		String tmpRole;
		long roleSum = 0;

		while (st.hasMoreTokens()) {
			tmpRole = st.nextToken();
			try {
				roleSum += Long.parseLong(res.getString("perms.role." + tmpRole));

			} catch (Exception ex) {
				log.debug("PermComparison: bad config value for role " +
						tmpRole, ex);
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
		long sum;
		String required;
		String user;
		required = "guest, registered, certified, pro, dba";
		sum = PermComparison.parsePermissions(required);
		System.out.println(required + " = " + sum);
		System.out.println(PermComparison.matchesAll(3, sum));

		required = "registered,certified";
		user = "registered, pro, certified";
		System.out.println("req: " + required + ", user has: " + user);
		System.out.println(PermComparison.matchesAll(required, user));

		required = "pro";
		user = "registered, pro";
		System.out.println("req: " + required + ", user has: " + user);
		System.out.println(PermComparison.matchesAll(required, user));

		required = "registered,pro";
		user = "registered, certified";
		System.out.println("req: " + required + ", user has: " + user);
		System.out.println(PermComparison.matchesAll(required, user));

		required = "certified,dba";
		user = "registered, certified, pro";
		System.out.println("req: " + required + ", user has: " + user);
		System.out.println(PermComparison.matchesAll(required, user));


	}

}
