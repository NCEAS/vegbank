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

public class UserDetailForm extends SuperForm
{
    private String usrId;
    private String loginName;
    private String password;
    private String role;
    private String lastName;
    private String firstName;
    private String middleInitial;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String email;
    private String passwordConfirm;

    public UserDetailForm()
    {
    	super();
    	usrId="";
    	loginName="";
    	password="";
    	role="";
    	lastName="";
    	firstName="";
    	middleInitial="";
    	street="";
    	city="";
    	state="";
    	zip="";
    	phone="";
    	email="";
    	passwordConfirm="";
    	updateFields();
    }
    public UserDetailForm(UsrDetails user)
    {
    	super();
    	this.usrId = user.getusrId();
        this.loginName=user.getLoginName();
        this.password=user.getPassword();
        this.role=user.getRole();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.middleInitial =user.getMiddleInitial();
        this.street = user.getStreet();
        this.city = user.getCity();
        this.state = user.getState();
        this.zip = user.getZip();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.passwordConfirm=user.getPassword();
        updateFields();
    }
    public UserDetailForm (String usrId, String usrLoginName,String usrPassword,String role, String lastName,
        String firstName, String middleInitial, String street,
        String city, String state, String zip, String phone,
        String email, String pswConfirm) {
        super();
        this.usrId = usrId;
        this.loginName=usrLoginName;
        this.password=usrPassword;
        this.role=role;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.passwordConfirm=pswConfirm;
        updateFields();
    }

    // getters
	public void update(UsrDetails user)
	{
		this.usrId = user.getusrId();
   		this.loginName=user.getLoginName();
		this.password=user.getPassword();
	   this.role=user.getRole();
	   this.lastName = user.getLastName();
   		this.firstName = user.getFirstName();
	   this.middleInitial =user.getMiddleInitial();
	   this.street = user.getStreet();
	   this.city = user.getCity();
	   this.state = user.getState();
	   this.zip = user.getZip();
	   this.phone = user.getPhone();
	   this.email = user.getEmail();
	   this.passwordConfirm=user.getPassword();
	   updateFields();
	}
	
	public void updateFields()
	{
		if(fields.size()<1)
		{
		addField("login_name");
		addField("password");
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
		setPrimaryKey("USR_ID");
		setTableName("usr");
		}
	}
	
	public String getFieldValue(String fldName) throws Exception
	{
		if(fldName==null)
			throw new Exception("Null field name.");
		if(fldName.equals("login_name"))
			return loginName;
		if(fldName.equals("password"))
			return password;
		if(fldName.equals("role"))
			return role;
		if(fldName.equals("last_name"))
			return lastName;
		if(fldName.equals("first_name"))
			return firstName;
		if(fldName.equals("middle_initial"))
			return middleInitial;
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
		if(fldName.equals("USR_ID"))
			return usrId;
		
		throw new Exception("Field not found: "+fldName);			

	}
	
	public void setFieldValue(String fldName, String value) throws Exception
    {
    	
    	if(fldName==null)
			throw new Exception("Null field name.");
		
		if(fldName.equals("login_name"))
			loginName=value;
		else if(fldName.equals("password"))
			password=value;
		else if(fldName.equals("role"))
			role=value;
		else if(fldName.equals("last_name"))
			 lastName=value;
		else if(fldName.equals("first_name"))
			firstName=value;
		else if(fldName.equals("middle_initial"))
			middleInitial=value;
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
		else if(fldName.equals("USR_ID"))
			usrId=value;
		else 
			throw new Exception("Field not found: "+fldName);	
    }
    public String getusrId() {
        return usrId;
    }

	public String getLoginName() {
        return loginName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getPasswordConfirm()
    {
    	return passwordConfirm;
    }


    public String getRole()
    {
    	return this.role;
    }
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // setters

    public void setLoginName(String loginName)
    {
    	this.loginName=loginName;
    }
    
    public void setPassword(String psw)
    {
    	this.password=psw;
    }
    
    public void setPasswordConfirm(String pswConfirm)
    {
    	this.passwordConfirm=pswConfirm;
    }
    
    public void setRole(String role)
    {
    	this.role=role;
    }
    public void setusrId(String usrId) {
        this.usrId = usrId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
