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
	private String document1;
	private String document2;
	private String document3;
	private String current_status;
	private String PROJECT_ID;



	public proposal_Form()
	{
		super();
		PROPOSAL_ID="-1";
		summary="";
		previousProposal_ID="-1";
		current_status=" ";
		document1="";
		document2="";
		document3="";
		PROJECT_ID="-1";
		updateFields();
	}


	public proposal_Form(String PROPOSAL_ID,String summary,String previousProposal_ID,
		String document1, String document2, String document3, String current_status,String PROJECT_ID
		)
	{
		this.PROPOSAL_ID=PROPOSAL_ID;
		this.summary=summary;
		this.previousProposal_ID=previousProposal_ID;
		this.document1=document1;
		this.document2=document2;
		this.document3=document3;
		this.current_status=current_status;
		
		this.PROJECT_ID=PROJECT_ID;
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



	public String getCurrent_status()
	{
		return this.current_status;
	}


	public String getPROJECT_ID()
	{
		return this.PROJECT_ID;
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
			addField("current_status");
			addField("PROJECT_ID");
			addField("document1");
			addField("document2");
			addField("document3");
			
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
		if(fldName.equals("current_status"))
			return current_status;
		if(fldName.equals("PROJECT_ID"))
			return PROJECT_ID;
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
		if(fldName.equals("PROPOSAL_ID"))
			PROPOSAL_ID=value;
		else if(fldName.equals("summary"))
			summary=value;
		else if(fldName.equals("previousProposal_ID"))
			previousProposal_ID=value;
		else if(fldName.equals("current_status"))
			current_status=value;
		else if(fldName.equals("PROJECT_ID"))
			PROJECT_ID=value;
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

