/*
 * '$Id: composition_Form.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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
//  composition_Form.java
//  Created on Thu Apr 15 15:39:52 EDT 2004
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

public class composition_Form extends SuperForm
{
	private String COMPOSITION_ID;
	private String VBPlant_AccessionCode;
	private String constancy;
	private String compositionType;
	private String notes;
	private String stratum;
	private String TYPE_ID;
	private String VBMethod_AccessionCode;


	public composition_Form()
	{
		super();
		COMPOSITION_ID="";
		VBPlant_AccessionCode="";
		constancy="";
		compositionType="";
		notes="";
		stratum="";
		TYPE_ID="";
		VBMethod_AccessionCode="";
		updateFields();
	}


	public composition_Form(String COMPOSITION_ID,String VBPlant_AccessionCode,String constancy,
		String compositionType,String notes,String stratum,
		String TYPE_ID,String VBMethod_AccessionCode)
	{
		this.COMPOSITION_ID=COMPOSITION_ID;
		this.VBPlant_AccessionCode=VBPlant_AccessionCode;
		this.constancy=constancy;
		this.compositionType=compositionType;
		this.notes=notes;
		this.stratum=stratum;
		this.TYPE_ID=TYPE_ID;
		this.VBMethod_AccessionCode=VBMethod_AccessionCode;
		updateFields();
	}



	public String getCOMPOSITION_ID()
	{
		return this.COMPOSITION_ID;
	}


	public String getVBPlant_AccessionCode()
	{
		return this.VBPlant_AccessionCode;
	}


	public String getConstancy()
	{
		return this.constancy;
	}


	public String getCompositionType()
	{
		return this.compositionType;
	}


	public String getNotes()
	{
		return this.notes;
	}


	public String getStratum()
	{
		return this.stratum;
	}


	public String getTYPE_ID()
	{
		return this.TYPE_ID;
	}


	public String getVBMethod_AccessionCode()
	{
		return this.VBMethod_AccessionCode;
	}



	public void setCOMPOSITION_ID(String COMPOSITION_ID)
	{
		this.COMPOSITION_ID=COMPOSITION_ID;
	}


	public void setVBPlant_AccessionCode(String VBPlant_AccessionCode)
	{
		this.VBPlant_AccessionCode=VBPlant_AccessionCode;
	}


	public void setConstancy(String constancy)
	{
		this.constancy=constancy;
	}


	public void setCompositionType(String compositionType)
	{
		this.compositionType=compositionType;
	}


	public void setNotes(String notes)
	{
		this.notes=notes;
	}


	public void setStratum(String stratum)
	{
		this.stratum=stratum;
	}


	public void setTYPE_ID(String TYPE_ID)
	{
		this.TYPE_ID=TYPE_ID;
	}


	public void setVBMethod_AccessionCode(String VBMethod_AccessionCode)
	{
		this.VBMethod_AccessionCode=VBMethod_AccessionCode;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("VBPlant_AccessionCode");
			addField("constancy");
			addField("compositionType");
			addField("notes");
			addField("stratum");
			addField("TYPE_ID");
			addField("VBMethod_AccessionCode");
			setTableName("composition");
			setPrimaryKey("COMPOSITION_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("COMPOSITION_ID"))
			return COMPOSITION_ID;
		if(fldName.equals("VBPlant_AccessionCode"))
			return VBPlant_AccessionCode;
		if(fldName.equals("constancy"))
			return constancy;
		if(fldName.equals("compositionType"))
			return compositionType;
		if(fldName.equals("notes"))
			return notes;
		if(fldName.equals("stratum"))
			return stratum;
		if(fldName.equals("TYPE_ID"))
			return TYPE_ID;
		if(fldName.equals("VBMethod_AccessionCode"))
			return VBMethod_AccessionCode;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("COMPOSITION_ID"))
			COMPOSITION_ID=value;
		else if(fldName.equals("VBPlant_AccessionCode"))
			VBPlant_AccessionCode=value;
		else if(fldName.equals("constancy"))
			constancy=value;
		else if(fldName.equals("compositionType"))
			compositionType=value;
		else if(fldName.equals("notes"))
			notes=value;
		else if(fldName.equals("stratum"))
			stratum=value;
		else if(fldName.equals("TYPE_ID"))
			TYPE_ID=value;
		else if(fldName.equals("VBMethod_AccessionCode"))
			VBMethod_AccessionCode=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

