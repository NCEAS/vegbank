/*
 *	'$Id: '
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-15 02:03:31 $'
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

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import java.io.StringWriter;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class VelocityParser  {

	private static Log log = LogFactory.getLog(VelocityParser.class); 

	VelocityContext mainContext = null;
	Template mainTemplate = null;

	public VelocityParser(String templateFile)  {
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.init("velocity.properties");
			mainTemplate = ve.getTemplate(templateFile);
		} catch( Exception ex ) {
			log.error("Error loading template file: " + templateFile +
					": " + ex.toString());
		}
	}

	/**
	 * Put all values in main context using given Map keys.
	 * @param tagTable
	 */
	public void putAll(Map tagTable) {
		if (tagTable == null) {
			return;
		}

		String key;
		Iterator tit = tagTable.keySet().iterator();
		while (tit.hasNext()) {
			key = (String)tit.next();
			put(key, tagTable.get(key));
		}
	}

	public void put(String key, Object value) {
		if (mainContext == null)
			mainContext = new VelocityContext();
		mainContext.put(key, value);
	}

	public void put(VelocityContext chainCtx) {
		mainContext = new VelocityContext(chainCtx);
	}

	public VelocityContext getCurrentContext() {
		return mainContext;   
	}


	public String processTemplate() {
		try {
			StringWriter writer = new StringWriter();

			if ( mainTemplate != null) {
				mainTemplate.merge(mainContext, writer);
			}
			return writer.toString();
			//writer.close();
		} catch( Exception ex )    {
			log.error("Problem processing template: " + ex.toString());
		}
		return "";
	}

	public static void main(String[] args)    {
		VelocityParser velInstance = new VelocityParser(args[0]);
		velInstance.put("treeFarm", new String [] { "redwood", "maple", "oak", "pine" });
		velInstance.put("title", "A Tree Farm");
		velInstance.put("date", new java.util.Date());
		velInstance.put("fmtr", 
			new org.apache.velocity.app.tools.VelocityFormatter(
		velInstance.getCurrentContext()));
		velInstance.processTemplate();             
	}
}
