/*
 * '$Id: proposal_Form.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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

///////////////////////////////////////////////////////////
//
//  proposal_Form.java
//  Created on Mon Apr 12 16:35:37 EDT 2004
//  By Auto FormBean,Action and JSP Builder 1.0
//
///////////////////////////////////////////////////////////



package org.vegbank.nvcrs.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.ModuleException;
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.vegbank.nvcrs.util.*;

public class proposal_Form extends SuperForm
{
	private String PROPOSAL_ID;
	private String summary;
	private String previousProposal_ID;
	private String supporting_doc_URL;
	private String current_status;
	private String PROJECT_ID;


	public proposal_Form()
	{
		super();
		PROPOSAL_ID="";
		summary="";
		previousProposal_ID=" ";
		supporting_doc_URL=" ";
		current_status=" ";
		PROJECT_ID=" ";
		updateFields();
	}


	public proposal_Form(String PROPOSAL_ID,String summary,String previousProposal_ID,
		String supporting_doc_URL,String current_status,String PROJECT_ID
		)
	{
		this.PROPOSAL_ID=PROPOSAL_ID;
		this.summary=summary;
		this.previousProposal_ID=previousProposal_ID;
		this.supporting_doc_URL=supporting_doc_URL;
		this.current_status=current_status;
		this.PROJECT_ID=PROJECT_ID;
		updateFields();
	}



	public String getPROPOSAL_ID()
	{
		return this.PROPOSAL_ID;
	}


	public String getSummary()
	{
		return this.summary;
	}


	public String getPreviousProposal_ID()
	{
		return this.previousProposal_ID;
	}


	public String getSupporting_doc_URL()
	{
		return this.supporting_doc_URL;
	}


	public String getCurrent_status()
	{
		return this.current_status;
	}


	public String getPROJECT_ID()
	{
		return this.PROJECT_ID;
	}



	public void setPROPOSAL_ID(String PROPOSAL_ID)
	{
		this.PROPOSAL_ID=PROPOSAL_ID;
	}


	public void setSummary(String summary)
	{
		this.summary=summary;
	}


	public void setPreviousProposal_ID(String previousProposal_ID)
	{
		this.previousProposal_ID=previousProposal_ID;
	}


	public void setSupporting_doc_URL(String supporting_doc_URL)
	{
		this.supporting_doc_URL=supporting_doc_URL;
	}


	public void setCurrent_status(String current_status)
	{
		this.current_status=current_status;
	}


	public void setPROJECT_ID(String PROJECT_ID)
	{
		this.PROJECT_ID=PROJECT_ID;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("summary");
			addField("previousProposal_ID");
			addField("supporting_doc_URL");
			addField("current_status");
			addField("PROJECT_ID");
			setTableName("proposal");
			setPrimaryKey("PROPOSAL_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("PROPOSAL_ID"))
			return PROPOSAL_ID;
		if(fldName.equals("summary"))
			return summary;
		if(fldName.equals("previousProposal_ID"))
			return previousProposal_ID;
		if(fldName.equals("supporting_doc_URL"))
			return supporting_doc_URL;
		if(fldName.equals("current_status"))
			return current_status;
		if(fldName.equals("PROJECT_ID"))
			return PROJECT_ID;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("PROPOSAL_ID"))
			PROPOSAL_ID=value;
		else if(fldName.equals("summary"))
			summary=value;
		else if(fldName.equals("previousProposal_ID"))
			previousProposal_ID=value;
		else if(fldName.equals("supporting_doc_URL"))
			supporting_doc_URL=value;
		else if(fldName.equals("current_status"))
			current_status=value;
		else if(fldName.equals("PROJECT_ID"))
			PROJECT_ID=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

