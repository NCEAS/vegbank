 /*
 *	'$RCSfile: PleaseWaitThread.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-05-06 22:41:32 $'
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

import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;


/**
 * @author anderson
 */
public abstract class PleaseWaitThread extends Thread {
	public abstract ActionMessages getStatusMessages();
	public abstract boolean isDone();
	public abstract String getForward();
	public abstract void prepareRequest(HttpServletRequest request);

	public String getThreadId() {
		return "pw-" + this.hashCode();
	}
}

