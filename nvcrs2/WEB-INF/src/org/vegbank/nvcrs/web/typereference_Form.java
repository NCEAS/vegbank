///////////////////////////////////////////////////////////
//
//  typereference_Form.java
//  Created on Thu Apr 15 18:31:17 EDT 2004
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

public class typereference_Form extends SuperForm
{
	private String REFERENCE_ID;
	private String VB_AccessionCode;
	private String detailText;
	private String authorReference;
	private String scope;
	private String TYPE_ID;


	public typereference_Form()
	{
		super();
		REFERENCE_ID="";
		VB_AccessionCode="";
		detailText="";
		authorReference="";
		scope="";
		TYPE_ID="";
		updateFields();
	}


	public typereference_Form(String REFERENCE_ID,String VB_AccessionCode,String detailText,
		String authorReference,String scope,String TYPE_ID
		)
	{
		this.REFERENCE_ID=REFERENCE_ID;
		this.VB_AccessionCode=VB_AccessionCode;
		this.detailText=detailText;
		this.authorReference=authorReference;
		this.scope=scope;
		this.TYPE_ID=TYPE_ID;
		updateFields();
	}



	public String getREFERENCE_ID()
	{
		return this.REFERENCE_ID;
	}


	public String getVB_AccessionCode()
	{
		return this.VB_AccessionCode;
	}


	public String getDetailText()
	{
		return this.detailText;
	}


	public String getAuthorReference()
	{
		return this.authorReference;
	}


	public String getScope()
	{
		return this.scope;
	}


	public String getTYPE_ID()
	{
		return this.TYPE_ID;
	}



	public void setREFERENCE_ID(String REFERENCE_ID)
	{
		this.REFERENCE_ID=REFERENCE_ID;
	}


	public void setVB_AccessionCode(String VB_AccessionCode)
	{
		this.VB_AccessionCode=VB_AccessionCode;
	}


	public void setDetailText(String detailText)
	{
		this.detailText=detailText;
	}


	public void setAuthorReference(String authorReference)
	{
		this.authorReference=authorReference;
	}


	public void setScope(String scope)
	{
		this.scope=scope;
	}


	public void setTYPE_ID(String TYPE_ID)
	{
		this.TYPE_ID=TYPE_ID;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("VB_AccessionCode");
			addField("detailText");
			addField("authorReference");
			addField("scope");
			addField("TYPE_ID");
			setTableName("typereference");
			setPrimaryKey("REFERENCE_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("REFERENCE_ID"))
			return REFERENCE_ID;
		if(fldName.equals("VB_AccessionCode"))
			return VB_AccessionCode;
		if(fldName.equals("detailText"))
			return detailText;
		if(fldName.equals("authorReference"))
			return authorReference;
		if(fldName.equals("scope"))
			return scope;
		if(fldName.equals("TYPE_ID"))
			return TYPE_ID;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("REFERENCE_ID"))
			REFERENCE_ID=value;
		else if(fldName.equals("VB_AccessionCode"))
			VB_AccessionCode=value;
		else if(fldName.equals("detailText"))
			detailText=value;
		else if(fldName.equals("authorReference"))
			authorReference=value;
		else if(fldName.equals("scope"))
			scope=value;
		else if(fldName.equals("TYPE_ID"))
			TYPE_ID=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

