/*
 * '$Id: typename_Form.java,v 1.1.1.1 2004-04-21 17:10:07 anderson Exp $'
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
//  typename_Form.java
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

public class typename_Form extends SuperForm
{
	private String TYPENAME_ID;
	private String typeName;
	private String typeNameSystem;
	private String typeNameStatus;
	private String startDate;
	private String stopDate;
	private String TYPE_ID;


	public typename_Form()
	{
		super();
		TYPENAME_ID="";
		typeName="";
		typeNameSystem="";
		typeNameStatus="";
		startDate="";
		stopDate="";
		TYPE_ID="";
		updateFields();
	}


	public typename_Form(String TYPENAME_ID,String typeName,String typeNameSystem,
		String typeNameStatus,String startDate,String stopDate,
		String TYPE_ID)
	{
		this.TYPENAME_ID=TYPENAME_ID;
		this.typeName=typeName;
		this.typeNameSystem=typeNameSystem;
		this.typeNameStatus=typeNameStatus;
		this.startDate=startDate;
		this.stopDate=stopDate;
		this.TYPE_ID=TYPE_ID;
		updateFields();
	}



	public String getTYPENAME_ID()
	{
		return this.TYPENAME_ID;
	}


	public String getTypeName()
	{
		return this.typeName;
	}


	public String getTypeNameSystem()
	{
		return this.typeNameSystem;
	}


	public String getTypeNameStatus()
	{
		return this.typeNameStatus;
	}


	public String getStartDate()
	{
		return this.startDate;
	}


	public String getStopDate()
	{
		return this.stopDate;
	}


	public String getTYPE_ID()
	{
		return this.TYPE_ID;
	}



	public void setTYPENAME_ID(String TYPENAME_ID)
	{
		this.TYPENAME_ID=TYPENAME_ID;
	}


	public void setTypeName(String typeName)
	{
		this.typeName=typeName;
	}


	public void setTypeNameSystem(String typeNameSystem)
	{
		this.typeNameSystem=typeNameSystem;
	}


	public void setTypeNameStatus(String typeNameStatus)
	{
		this.typeNameStatus=typeNameStatus;
	}


	public void setStartDate(String startDate)
	{
		this.startDate=startDate;
	}


	public void setStopDate(String stopDate)
	{
		this.stopDate=stopDate;
	}


	public void setTYPE_ID(String TYPE_ID)
	{
		this.TYPE_ID=TYPE_ID;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("typeName");
			addField("typeNameSystem");
			addField("typeNameStatus");
			addField("startDate");
			addField("stopDate");
			addField("TYPE_ID");
			setTableName("typename");
			setPrimaryKey("TYPENAME_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("TYPENAME_ID"))
			return TYPENAME_ID;
		if(fldName.equals("typeName"))
			return typeName;
		if(fldName.equals("typeNameSystem"))
			return typeNameSystem;
		if(fldName.equals("typeNameStatus"))
			return typeNameStatus;
		if(fldName.equals("startDate"))
			return startDate;
		if(fldName.equals("stopDate"))
			return stopDate;
		if(fldName.equals("TYPE_ID"))
			return TYPE_ID;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("TYPENAME_ID"))
			TYPENAME_ID=value;
		else if(fldName.equals("typeName"))
			typeName=value;
		else if(fldName.equals("typeNameSystem"))
			typeNameSystem=value;
		else if(fldName.equals("typeNameStatus"))
			typeNameStatus=value;
		else if(fldName.equals("startDate"))
			startDate=value;
		else if(fldName.equals("stopDate"))
			stopDate=value;
		else if(fldName.equals("TYPE_ID"))
			TYPE_ID=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

