/*
 * '$Id: plot_Form.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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
//  plot_Form.java
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

public class plot_Form extends SuperForm
{
	private String PLOT_ID;
	private String VB_AccessionCode;
	private String plotUse;
	private String TYPE_ID;


	public plot_Form()
	{
		super();
		PLOT_ID="";
		VB_AccessionCode="";
		plotUse="";
		TYPE_ID="";
		updateFields();
	}


	public plot_Form(String PLOT_ID,String VB_AccessionCode,String plotUse,
		String TYPE_ID)
	{
		this.PLOT_ID=PLOT_ID;
		this.VB_AccessionCode=VB_AccessionCode;
		this.plotUse=plotUse;
		this.TYPE_ID=TYPE_ID;
		updateFields();
	}



	public String getPLOT_ID()
	{
		return this.PLOT_ID;
	}


	public String getVB_AccessionCode()
	{
		return this.VB_AccessionCode;
	}


	public String getPlotUse()
	{
		return this.plotUse;
	}


	public String getTYPE_ID()
	{
		return this.TYPE_ID;
	}



	public void setPLOT_ID(String PLOT_ID)
	{
		this.PLOT_ID=PLOT_ID;
	}


	public void setVB_AccessionCode(String VB_AccessionCode)
	{
		this.VB_AccessionCode=VB_AccessionCode;
	}


	public void setPlotUse(String plotUse)
	{
		this.plotUse=plotUse;
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
			addField("plotUse");
			addField("TYPE_ID");
			setTableName("plot");
			setPrimaryKey("PLOT_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("PLOT_ID"))
			return PLOT_ID;
		if(fldName.equals("VB_AccessionCode"))
			return VB_AccessionCode;
		if(fldName.equals("plotUse"))
			return plotUse;
		if(fldName.equals("TYPE_ID"))
			return TYPE_ID;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("PLOT_ID"))
			PLOT_ID=value;
		else if(fldName.equals("VB_AccessionCode"))
			VB_AccessionCode=value;
		else if(fldName.equals("plotUse"))
			plotUse=value;
		else if(fldName.equals("TYPE_ID"))
			TYPE_ID=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

