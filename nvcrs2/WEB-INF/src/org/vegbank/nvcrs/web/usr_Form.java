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
	private String institute;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String country;
	private String zip;
	private String phone;
	private String email;
	

	public usr_Form()
	{
		super();
		USR_ID="0";
		login_name="";
		password="";
		permission="";
		role="";
		last_name="";
		first_name="";
		middle_initial="";
		institute="";
		address1="";
		address2="";
		city="";
		state="";
		country="";
		zip="";
		phone="";
		email="";
		
		updateFields();
	}


	public usr_Form(String USR_ID,String login_name,String password,
		String permission,String role,String last_name,
		String first_name,String middle_initial,String institute, String address1,
		String address2,
		String city,String state,String country,String zip,
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
		this.institute=institute;
		this.address1=address1;
		this.address2=address2;
		this.city=city;
		this.state=state;
		this.country=country;
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


	public String getAddress1()
	{
		return this.address1;
	}
	
	public String getInstitute()
	{
		return this.institute;
	}
	
	public String getAddress2()
	{
		return this.address2;
	}


	public String getCity()
	{
		return this.city;
	}


	public String getState()
	{
		return this.state;
	}

	public String getCountry()
	{
		return this.country;
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

	public void setInstitute(String institute)
	{
		this.institute=institute;
	}



	public void setAddress1(String address1)
	{
		this.address1=address1;
	}

	public void setAddress2(String address2)
	{
		this.address2=address2;
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
	public void setCountry(String country)
	{
		this.country=country;
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
			addField("institute");
			addField("address1");
			addField("address2");
			addField("city");
			addField("state");
			addField("country");
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
		if(fldName.equals("institute"))
			return institute;
		if(fldName.equals("address1"))
			return address1;
		if(fldName.equals("address2"))
			return address2;
		if(fldName.equals("city"))
			return city;
		if(fldName.equals("state"))
			return state;
		if(fldName.equals("country"))
			return country;
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
		else if(fldName.equals("institute"))
			institute=value;
		else if(fldName.equals("address1"))
			address1=value;
		else if(fldName.equals("address2"))
			address2=value;
		else if(fldName.equals("city"))
			city=value;
		else if(fldName.equals("state"))
			state=value;
		else if(fldName.equals("country"))
			country=value;
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

