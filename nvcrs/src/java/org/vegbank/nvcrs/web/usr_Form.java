/*
 * '$Id: usr_Form.java,v 1.1.1.1 2004-04-21 17:10:07 anderson Exp $'
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
//  usr_Form.java
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

public class usr_Form extends SuperForm
{
	private String USR_ID;
	private String login_name;
	private String password;
	private String permission;
	private String role;
	private String last_name;
	private String first_name;
	private String middle_initial;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String email;


	public usr_Form()
	{
		super();
		USR_ID="-1";
		login_name="";
		password="";
		permission="";
		role="";
		last_name="";
		first_name="";
		middle_initial="";
		street="";
		city="";
		state="";
		zip="";
		phone="";
		email="";
		updateFields();
	}


	public usr_Form(String USR_ID,String login_name,String password,
		String permission,String role,String last_name,
		String first_name,String middle_initial,String street,
		String city,String state,String zip,
		String phone,String email)
	{
		this.USR_ID=USR_ID;
		this.login_name=login_name;
		this.password=password;
		this.permission=permission;
		this.role=role;
		this.last_name=last_name;
		this.first_name=first_name;
		this.middle_initial=middle_initial;
		this.street=street;
		this.city=city;
		this.state=state;
		this.zip=zip;
		this.phone=phone;
		this.email=email;
		updateFields();
	}



	public String getUSR_ID()
	{
		return this.USR_ID;
	}


	public String getLogin_name()
	{
		return this.login_name;
	}


	public String getPassword()
	{
		return this.password;
	}


	public String getPermission()
	{
		return this.permission;
	}


	public String getRole()
	{
		return this.role;
	}


	public String getLast_name()
	{
		return this.last_name;
	}


	public String getFirst_name()
	{
		return this.first_name;
	}


	public String getMiddle_initial()
	{
		return this.middle_initial;
	}


	public String getStreet()
	{
		return this.street;
	}


	public String getCity()
	{
		return this.city;
	}


	public String getState()
	{
		return this.state;
	}


	public String getZip()
	{
		return this.zip;
	}


	public String getPhone()
	{
		return this.phone;
	}


	public String getEmail()
	{
		return this.email;
	}



	public void setUSR_ID(String USR_ID)
	{
		this.USR_ID=USR_ID;
	}


	public void setLogin_name(String login_name)
	{
		this.login_name=login_name;
	}


	public void setPassword(String password)
	{
		this.password=password;
	}


	public void setPermission(String permission)
	{
		this.permission=permission;
	}


	public void setRole(String role)
	{
		this.role=role;
	}


	public void setLast_name(String last_name)
	{
		this.last_name=last_name;
	}


	public void setFirst_name(String first_name)
	{
		this.first_name=first_name;
	}


	public void setMiddle_initial(String middle_initial)
	{
		this.middle_initial=middle_initial;
	}


	public void setStreet(String street)
	{
		this.street=street;
	}


	public void setCity(String city)
	{
		this.city=city;
	}


	public void setState(String state)
	{
		this.state=state;
	}


	public void setZip(String zip)
	{
		this.zip=zip;
	}


	public void setPhone(String phone)
	{
		this.phone=phone;
	}


	public void setEmail(String email)
	{
		this.email=email;
	}


	public void updateFields()
	{
		if(fields.size()<1)
		{
			addField("login_name");
			addField("password");
			addField("permission");
			addField("role");
			addField("last_name");
			addField("first_name");
			addField("middle_initial");
			addField("street");
			addField("city");
			addField("state");
			addField("zip");
			addField("phone");
			addField("email");
			setTableName("usr");
			setPrimaryKey("USR_ID");
		}
	}


	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("USR_ID"))
			return USR_ID;
		if(fldName.equals("login_name"))
			return login_name;
		if(fldName.equals("password"))
			return password;
		if(fldName.equals("permission"))
			return permission;
		if(fldName.equals("role"))
			return role;
		if(fldName.equals("last_name"))
			return last_name;
		if(fldName.equals("first_name"))
			return first_name;
		if(fldName.equals("middle_initial"))
			return middle_initial;
		if(fldName.equals("street"))
			return street;
		if(fldName.equals("city"))
			return city;
		if(fldName.equals("state"))
			return state;
		if(fldName.equals("zip"))
			return zip;
		if(fldName.equals("phone"))
			return phone;
		if(fldName.equals("email"))
			return email;
		throw new Exception("Field not found: "+fldName);
	}


	public void setFieldValue(String fldName, String value) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("USR_ID"))
			USR_ID=value;
		else if(fldName.equals("login_name"))
			login_name=value;
		else if(fldName.equals("password"))
			password=value;
		else if(fldName.equals("permission"))
			permission=value;
		else if(fldName.equals("role"))
			role=value;
		else if(fldName.equals("last_name"))
			last_name=value;
		else if(fldName.equals("first_name"))
			first_name=value;
		else if(fldName.equals("middle_initial"))
			middle_initial=value;
		else if(fldName.equals("street"))
			street=value;
		else if(fldName.equals("city"))
			city=value;
		else if(fldName.equals("state"))
			state=value;
		else if(fldName.equals("zip"))
			zip=value;
		else if(fldName.equals("phone"))
			phone=value;
		else if(fldName.equals("email"))
			email=value;
		else
			throw new Exception("Field not found: "+fldName);
	}


}

