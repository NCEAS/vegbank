///////////////////////////////////////////////////////////
//
//  correlation_Form.java
//  Created on Wed Apr 21 15:31:07 EDT 2004
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

public class correlation_Form extends SuperForm
{
	private String CORRELATION_ID;
	private String VB_AccessionCode;
	private String convergence;
	private String notes;
	private String TYPE_ID;
	private String CORRELATEDTYPE_ID;

	public correlation_Form()
	{
		super();
		CORRELATION_ID="";
		VB_AccessionCode="";
		convergence="";
		notes="";
		TYPE_ID="";
		CORRELATEDTYPE_ID="";
		updateFields();
	}


	public correlation_Form(String CORRELATION_ID,String VB_AccessionCode,String convergence,
		String notes,String TYPE_ID,String CORRELATEDTYPE_ID
		)
	{
		this.CORRELATION_ID=CORRELATION_ID;
		this.VB_AccessionCode=VB_AccessionCode;
		this.convergence=convergence;
		this.notes=notes;
		this.TYPE_ID=TYPE_ID;
		this.CORRELATEDTYPE_ID=CORRELATEDTYPE_ID;
		updateFields();
	}



	public String getCORRELATION_ID()
	{
		return this.CORRELATION_ID;
	}


	public String getVB_AccessionCode()
	{
		return this.VB_AccessionCode;
	}


	public String getConvergence()
	{
		return this.convergence;
	}


	public String getNotes()
	{
		return this.notes;
	}


	public String getTYPE_ID()
	{
		return this.TYPE_ID;
	}


	public String getCORRELATEDTYPE_ID()
	{
		return this.CORRELATEDTYPE_ID;
	}



	public void setCORRELATION_ID(String CORRELATION_ID)
	{
		this.CORRELATION_ID=CORRELATION_ID;
	}


	public void setVB_AccessionCode(String VB_AccessionCode)
	{
		this.VB_AccessionCode=VB_AccessionCode;
	}


	public void setConvergence(String convergence)
	{
		this.convergence=convergence;
	}


	public void setNotes(String notes)
	{
		this.notes=notes;
	}


	public void setTYPE_ID(String TYPE_ID)
	{
		this.TYPE_ID=TYPE_ID;
	}


	public void setCORRELATEDTYPE_ID(String CORRELATEDTYPE_ID)
	{
		this.CORRELATEDTYPE_ID=CORRELATEDTYPE_ID;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("VB_AccessionCode");
			addField("convergence");
			addField("notes");
			addField("TYPE_ID");
			addField("CORRELATEDTYPE_ID");
			setTableName("correlation");
			setPrimaryKey("CORRELATION_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("CORRELATION_ID"))
			return CORRELATION_ID;
		if(fldName.equals("VB_AccessionCode"))
			return VB_AccessionCode;
		if(fldName.equals("convergence"))
			return convergence;
		if(fldName.equals("notes"))
			return notes;
		if(fldName.equals("TYPE_ID"))
			return TYPE_ID;
		if(fldName.equals("CORRELATEDTYPE_ID"))
			return CORRELATEDTYPE_ID;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("CORRELATION_ID"))
			CORRELATION_ID=value;
		else if(fldName.equals("VB_AccessionCode"))
			VB_AccessionCode=value;
		else if(fldName.equals("convergence"))
			convergence=value;
		else if(fldName.equals("notes"))
			notes=value;
		else if(fldName.equals("TYPE_ID"))
			TYPE_ID=value;
		else if(fldName.equals("CORRELATEDTYPE_ID"))
			CORRELATEDTYPE_ID=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

