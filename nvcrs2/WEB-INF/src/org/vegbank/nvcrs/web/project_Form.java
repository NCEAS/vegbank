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
	private String document1;
	private String document2;
	private String document3;

	public project_Form()
	{
		super();
		PROJECT_ID=BeanManager.UNKNOWN_ID;
		projectName="";
		projectDescription="";
		document1="";
		document2="";
		document3="";
		updateFields();
	}


	public project_Form(String PROJECT_ID,String projectName,String projectDescription,
		String document1, String document2, String document3)
	{
		this.PROJECT_ID=PROJECT_ID;
		this.projectName=projectName;
		this.projectDescription=projectDescription;
		this.document1=document1;
		this.document2=document2;
		this.document3=document3;
		updateFields();
	}


	public String getDocument1()
	{
		return this.document1;
	}
	public String getDocument2()
	{
		return this.document2;
	}

	public String getDocument3()
	{
		return this.document3;
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


	public void setDocument1(String doc)
	{
		this.document1=doc;
	}
	
	public void setDocument2(String doc)
	{
		this.document2=doc;
	}
	
	public void setDocument3(String doc)
	{
		this.document3=doc;
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

	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("projectName");
			addField("projectDescription");
			addField("document1");
			addField("document2");
			addField("document3");
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
		if(fldName.equals("document1"))
			return document1;
		if(fldName.equals("document2"))
			return document2;
		if(fldName.equals("document3"))
			return document3;
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
		else if(fldName.equals("document1"))
			document1=value;
		else if(fldName.equals("document2"))
			document2=value;
		else if(fldName.equals("document3"))
			document3=value;	
		else
			throw new Exception("Field not found: "+fldName);
	}


}

