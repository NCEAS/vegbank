///////////////////////////////////////////////////////////
//
//  distribution_Form.java
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

public class distribution_Form extends SuperForm
{
	private String DISTRIBUTION_ID;
	private String placeName;
	private String VB_AccessionCode;
	private String placeType;
	private String placeNotes;
	private String placeDistribution;
	private String placePattern;
	private String confidence;
	private String TYPE_ID;


	public distribution_Form()
	{
		super();
		DISTRIBUTION_ID="";
		placeName="";
		VB_AccessionCode="";
		placeType="";
		placeNotes="";
		placeDistribution="";
		placePattern="";
		confidence="";
		TYPE_ID="";
		updateFields();
	}


	public distribution_Form(String DISTRIBUTION_ID,String placeName,String VB_AccessionCode,
		String placeType,String placeNotes,String placeDistribution,
		String placePattern,String confidence,String TYPE_ID
		)
	{
		this.DISTRIBUTION_ID=DISTRIBUTION_ID;
		this.placeName=placeName;
		this.VB_AccessionCode=VB_AccessionCode;
		this.placeType=placeType;
		this.placeNotes=placeNotes;
		this.placeDistribution=placeDistribution;
		this.placePattern=placePattern;
		this.confidence=confidence;
		this.TYPE_ID=TYPE_ID;
		updateFields();
	}



	public String getDISTRIBUTION_ID()
	{
		return this.DISTRIBUTION_ID;
	}


	public String getPlaceName()
	{
		return this.placeName;
	}


	public String getVB_AccessionCode()
	{
		return this.VB_AccessionCode;
	}


	public String getPlaceType()
	{
		return this.placeType;
	}


	public String getPlaceNotes()
	{
		return this.placeNotes;
	}


	public String getPlaceDistribution()
	{
		return this.placeDistribution;
	}


	public String getPlacePattern()
	{
		return this.placePattern;
	}


	public String getConfidence()
	{
		return this.confidence;
	}


	public String getTYPE_ID()
	{
		return this.TYPE_ID;
	}



	public void setDISTRIBUTION_ID(String DISTRIBUTION_ID)
	{
		this.DISTRIBUTION_ID=DISTRIBUTION_ID;
	}


	public void setPlaceName(String placeName)
	{
		this.placeName=placeName;
	}


	public void setVB_AccessionCode(String VB_AccessionCode)
	{
		this.VB_AccessionCode=VB_AccessionCode;
	}


	public void setPlaceType(String placeType)
	{
		this.placeType=placeType;
	}


	public void setPlaceNotes(String placeNotes)
	{
		this.placeNotes=placeNotes;
	}


	public void setPlaceDistribution(String placeDistribution)
	{
		this.placeDistribution=placeDistribution;
	}


	public void setPlacePattern(String placePattern)
	{
		this.placePattern=placePattern;
	}


	public void setConfidence(String confidence)
	{
		this.confidence=confidence;
	}


	public void setTYPE_ID(String TYPE_ID)
	{
		this.TYPE_ID=TYPE_ID;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("placeName");
			addField("VB_AccessionCode");
			addField("placeType");
			addField("placeNotes");
			addField("placeDistribution");
			addField("placePattern");
			addField("confidence");
			addField("TYPE_ID");
			setTableName("distribution");
			setPrimaryKey("DISTRIBUTION_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("DISTRIBUTION_ID"))
			return DISTRIBUTION_ID;
		if(fldName.equals("placeName"))
			return placeName;
		if(fldName.equals("VB_AccessionCode"))
			return VB_AccessionCode;
		if(fldName.equals("placeType"))
			return placeType;
		if(fldName.equals("placeNotes"))
			return placeNotes;
		if(fldName.equals("placeDistribution"))
			return placeDistribution;
		if(fldName.equals("placePattern"))
			return placePattern;
		if(fldName.equals("confidence"))
			return confidence;
		if(fldName.equals("TYPE_ID"))
			return TYPE_ID;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("DISTRIBUTION_ID"))
			DISTRIBUTION_ID=value;
		else if(fldName.equals("placeName"))
			placeName=value;
		else if(fldName.equals("VB_AccessionCode"))
			VB_AccessionCode=value;
		else if(fldName.equals("placeType"))
			placeType=value;
		else if(fldName.equals("placeNotes"))
			placeNotes=value;
		else if(fldName.equals("placeDistribution"))
			placeDistribution=value;
		else if(fldName.equals("placePattern"))
			placePattern=value;
		else if(fldName.equals("confidence"))
			confidence=value;
		else if(fldName.equals("TYPE_ID"))
			TYPE_ID=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

