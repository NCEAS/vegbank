package org.vegbank.ui.struts;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.UserDatabaseAccess;

/*
 * '$RCSfile: EmailPasswordAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-08-09 18:34:28 $'
 *	'$Revision: 1.4 $'
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
 * Struts <code>Action</code> to email a user their password.
 * 
 * @author farrell
 */

public class EmailPasswordAction extends Action
{
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		ActionErrors errors = new ActionErrors();
		String fwd = "success";
		
		// Get the form
		DynaActionForm thisForm = (DynaActionForm) form;
		
		String email = (String ) thisForm.get("email");
		

		try
		{
			// Use the email to find the password
			UserDatabaseAccess uda = new UserDatabaseAccess();
			WebUser user = uda.getUser(email);
			
			// Check if a user is found
			if ( user == null ) 
			{
				LogUtility.log("EmailPasswordAction: No users found with: " + email);
				errors.add(
					Globals.ERROR_KEY,
					new ActionError("errors.user.not.found", email ));
			}
			else
			{
        Random randomGen = new Random(System.currentTimeMillis());
        int randomNum = randomGen.nextInt();
        if(randomNum < 0)
        {
          randomNum = randomNum * -1;
        }
        
				String passwd =  new Integer(randomNum).toString();
        uda.updatePassword(passwd, email);
        
				// Send the email
				String mailHost = null;
				String cc = "";
				String body;
				String from = "help@vegbank.org";
				String subject = "Reset VegBank Password";
			
				StringBuffer sb = new StringBuffer();
				sb.append("Dear VegBank user,\n");
				sb.append("Your password has been reset to: " + passwd + "\n");
        sb.append("Please login as soon as possible and change your password.\n\n");
				sb.append("VegBank Support Team\n\n");
				sb.append("Email: help@vegbank.org\n");
				sb.append("Website: http://vegbank.org\n");
				body = sb.toString();

				ServletUtility.sendPlainTextEmail( mailHost, from, email, cc, subject, body);
			}
		}
		catch (Exception e)
		{
			LogUtility.log("EmailPasswordAction: " + e.getMessage(), e);
			errors.add(
				Globals.ERROR_KEY,
				new ActionError("errors.action.failed", e.getMessage() ));
		}
		
		
		if ( ! errors.isEmpty() )
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());	
		}
		
		return mapping.findForward(fwd);
	}

}
