/*
 * '$Id: project_Form.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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
//  project_Form.java
//  Created on Tue Apr 13 15:47:20 EDT 2004
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

public class project_Form extends SuperForm
{
	private String PROJECT_ID;
	private String projectName;
	private String projectDescription;
	private String supporting_doc_URL;


	public project_Form()
	{
		super();
		PROJECT_ID="";
		projectName="";
		projectDescription="";
		supporting_doc_URL="";
		updateFields();
	}


	public project_Form(String PROJECT_ID,String projectName,String projectDescription,
		String supporting_doc_URL)
	{
		this.PROJECT_ID=PROJECT_ID;
		this.projectName=projectName;
		this.projectDescription=projectDescription;
		this.supporting_doc_URL=supporting_doc_URL;
		updateFields();
	}



	public String getPROJECT_ID()
	{
		return this.PROJECT_ID;
	}


	public String getProjectName()
	{
		return this.projectName;
	}


	public String getProjectDescription()
	{
		return this.projectDescription;
	}


	public String getSupporting_doc_URL()
	{
		return this.supporting_doc_URL;
	}



	public void setPROJECT_ID(String PROJECT_ID)
	{
		this.PROJECT_ID=PROJECT_ID;
	}


	public void setProjectName(String projectName)
	{
		this.projectName=projectName;
	}


	public void setProjectDescription(String projectDescription)
	{
		this.projectDescription=projectDescription;
	}


	public void setSupporting_doc_URL(String supporting_doc_URL)
	{
		this.supporting_doc_URL=supporting_doc_URL;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("projectName");
			addField("projectDescription");
			addField("supporting_doc_URL");
			setTableName("project");
			setPrimaryKey("PROJECT_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("PROJECT_ID"))
			return PROJECT_ID;
		if(fldName.equals("projectName"))
			return projectName;
		if(fldName.equals("projectDescription"))
			return projectDescription;
		if(fldName.equals("supporting_doc_URL"))
			return supporting_doc_URL;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("PROJECT_ID"))
			PROJECT_ID=value;
		else if(fldName.equals("projectName"))
			projectName=value;
		else if(fldName.equals("projectDescription"))
			projectDescription=value;
		else if(fldName.equals("supporting_doc_URL"))
			supporting_doc_URL=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

