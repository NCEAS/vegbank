///////////////////////////////////////////////////////////
//
//  lookup_eventtype_Form.java
//  Created on Tue Apr 13 15:47:19 EDT 2004
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

public class lookup_eventtype_Form extends SuperForm
{
	private String TYPE_ID;
	private String type;


	public lookup_eventtype_Form()
	{
		super();
		TYPE_ID="";
		type="";
		updateFields();
	}


	public lookup_eventtype_Form(String TYPE_ID,String type)
	{
		this.TYPE_ID=TYPE_ID;
		this.type=type;
		updateFields();
	}



	public String getTYPE_ID()
	{
		return this.TYPE_ID;
	}


	public String getType()
	{
		return this.type;
	}



	public void setTYPE_ID(String TYPE_ID)
	{
		this.TYPE_ID=TYPE_ID;
	}


	public void setType(String type)
	{
		this.type=type;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("type");
			setTableName("lookup_eventtype");
			setPrimaryKey("TYPE_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("TYPE_ID"))
			return TYPE_ID;
		if(fldName.equals("type"))
			return type;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("TYPE_ID"))
			TYPE_ID=value;
		else if(fldName.equals("type"))
			type=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

