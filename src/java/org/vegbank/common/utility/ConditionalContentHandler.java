/*	
 * '$RCSfile: ConditionalContentHandler.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-07-24 00:55:15 $'
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class ConditionalContentHandler 
{
	private static Log log = LogFactory.getLog(ConditionalContentHandler.class);
	private ConditionalContentHandlerController controller = null;
		

	public boolean keepRunning() {
		if (controller == null) {
			return true;
		}

		if (controller.keepRunning()) {
			return true;

		} else {
			controller.setStatus("killed");
			return false;
		}
	}

	public void setController(ConditionalContentHandlerController c) {
		controller = c;
	}
}
