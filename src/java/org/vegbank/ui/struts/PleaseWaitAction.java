package org.vegbank.ui.struts;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Vector; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.FileWrapper;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.datafileexchange.DataFileDB;
import org.vegbank.common.utility.PleaseWaitThread;
import org.vegbank.dataload.XML.*;

/*
 * '$Id: PleaseWaitAction.java,v 1.1 2004-05-06 22:42:54 anderson Exp $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-05-06 22:42:54 $'
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
/**
 * @author anderson
 *
 * Struts Action to wait for a running thread to complete its task.
 */
public class PleaseWaitAction extends Action
{
	private static Log log = LogFactory.getLog(PleaseWaitAction.class);
	public static final int MAX_LOOPS = 20;
	

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		log.debug(" In PleaseWaitAction ");
		ActionMessages messages = new ActionMessages();

		HttpSession session = request.getSession();

		// get threadId from session
		String threadId = (String)session.getAttribute("threadId");

		// get worker Thread from session
		PleaseWaitThread worker = (PleaseWaitThread)
			session.getAttribute(threadId);

		try {
			if (worker == null) {
				throw new Exception("Could not find worker thread #" + threadId);
			}

			// get numLoops from session
			String strNumLoops = (String)session.getAttribute("numLoops");
			int numLoops = 0;
			if (!Utility.isStringNullOrEmpty(strNumLoops)) {
				numLoops = Integer.valueOf(strNumLoops).intValue();
			}

			// get started from request
			/*
			boolean started = true;
			Boolean b = (Boolean)session.getAttribute("started");
			if (b != null) {
				started = b.booleanValue();
			}
			*/

			log.debug("[#" + strNumLoops + "] Got thread " + threadId + 
					" from session");

			//request.setAttribute("threadId",  threadId);
			//request.setAttribute("numLoops",  numLoops);

			messages.add(worker.getStatusMessages());
			saveMessages(request, messages);

			if (worker.isDone()) {
				// go to next page
				log.debug("\n\nWORKER DONE!!...");
				worker.prepareRequest(request);
				log.debug("fwd to " + worker.getForward());

				// clean up the session
				session.removeAttribute(threadId);
				session.removeAttribute("threadId");
				session.removeAttribute("numLoops");

				return mapping.findForward(worker.getForward());

			} else if (numLoops < MAX_LOOPS) {
				if (numLoops > 0) {
					log.debug("Sleeping...");
					Thread.currentThread().sleep(4000);
				}
				numLoops++;
				session.setAttribute("numLoops", new Integer(numLoops).toString());

				return mapping.findForward("self");

			} else {
				// kill the thread
				log.debug("Reached max loops; destroying thread");

				worker.destroy();
				worker = null;

				session.removeAttribute(threadId);
				session.removeAttribute("threadId");
				session.removeAttribute("numLoops");


				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.general",
							"Process took too long to complete."));
				saveErrors(request, errors);
				
				return mapping.findForward("vberror");
			}

		} catch (Exception e) {
			messages.add(
				ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage(
					"messages.action.failed",
					e.getMessage()));
			saveMessages(request, messages);
		}

		return mapping.findForward("vberror");

	}

}
