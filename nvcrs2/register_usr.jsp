<!--

  usr.jsp
   Created on Thu Apr 15 18:20:09 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	
	var accept="decline";
	
	function radio_click(clicked)
	{
		accept=clicked;
	}
	function submitClick(act)
	{
		document.usr_form.action.value=act;
		document.usr_form.submit();
	}

	function validate()
	{
		var msg="";
		var tmp=trim(document.reg_form.login_name.value);
		if(tmp.length==0) msg=msg+"Login name: can not be empty\n";
		
		tmp=trim(document.reg_form.password.value);
		if(tmp.length==0) msg=msg+"Password: can not be empty\n";
		
		var tmp1=trim(document.reg_form.confirm_password.value);
		if(tmp1.length==0) msg=msg+"Password again: can not be empty\n";
		
		if(tmp1!=tmp) msg=msg+"Password: 'Password' does not match with 'password again'\n";
		
		tmp=trim(document.reg_form.first_name.value);
		if(tmp.length==0) msg=msg+"First name: can not be empty\n";

		tmp=trim(document.reg_form.last_name.value);
		if(tmp.length==0) msg=msg+"Last name: can not be empty\n";

		tmp=trim(document.reg_form.email.value);
		if(tmp.length==0) msg=msg+"Email: can not be empty\n";
		
		if(tmp.indexOf("@")<1) msg=msg+"Email: Invalid email address\n";
			
		tmp=trim(document.reg_form.termsaccept.value);
		if(accept!="accept") msg=msg+"Terms of Use: the acceptance of the terms of use is required\n";
		
			
		if(msg!="")
		{
			alert(msg);
			return false;
		}
		return true;
	}

	function trim(inputString) {
	   if (typeof inputString != "string") { return inputString; }
   		var retValue = inputString;
	    var ch = retValue.substring(0, 1);
	   while (ch == " ") { // Check for spaces at the beginning of the string
       retValue = retValue.substring(1, retValue.length);
       ch = retValue.substring(0, 1);
      }
  	 ch = retValue.substring(retValue.length-1, retValue.length);
   	while (ch == " ") { // Check for spaces at the end of the string
      retValue = retValue.substring(0, retValue.length-1);
      ch = retValue.substring(retValue.length-1, retValue.length);
   }
   while (retValue.indexOf("  ") != -1) { // Note that there are two spaces in the string - look for multiple spaces within the string
      retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length); // Again, there are two spaces in each of the strings
   }
   return retValue; // Return the trimmed string back to the user
} // Ends the "trim" function

</script>
<center>
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>NVCRS Registration
</td></tr>
</table >

<br><br>

<form action="registerme.go" method="POST" onsubmit="return validate()"  name="reg_form">
	<table width=600 border=0 cellpadding=5 cellspacing=0 bgcolor="#ecedcd">
     <tr><th colspan=4 background="image/blue_back.gif"><center><font color=white>REGISTRATION</font></center></th></tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
     <font size=2 >
  <tr><td width=300>	
	<table width=300 border=0 cellpadding=1 cellspacing=0 bgcolor="#ecedcd">
     <tr><td colspan=4 background="image/blue_back.gif"><center><font color=white>Login information</font></center></td></tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align=right><font face=Arial size=2>Login name:</td>
		<td align=left width=50><input type="text"  name="login_name"/>&nbsp; </td>
		<td align=left>*</td>
		<td></td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Password:</td>
		<td align=left><input type="password"  name="password" /></td>
		<td>*</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Password again:</td>
		<td align=left><input type="password"  name="confirm_password" /></td>
		<td>*</td>
		<td>&nbsp;</td>
	</tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
      <tr><td colspan=4 background="image/blue_back.gif"><center><font color=white>Personal information</font></center></td></tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
    
	<tr>
		<td align=right><font face=Arial size=2>Last name:</td>
		<td align=left><input type="text"  name="last_name" /></td>
		<td>*</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Middle initial:</td>
		<td align=left><input type="text"  name="middle_initial" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	<tr>
		<td align=right><font face=Arial size=2>First name:</td>
		<td align=left><input type="text"  name="first_name" /></td>
		<td>*</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Institute:</td>
		<td align=left><input type="text"  name="institute" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Address 1:</td>
		<td align=left><input type="text"  name="address1" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Address 2:</td>
		<td align=left><input type="text"  name="address2" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>City:</td>
		<td><input type="text"  name="city" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>State/Province:</td>
		<td><input type="text"  name="state" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Zip code:</td>
		<td align=left><input type="text"  name="zip" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Country:</td>
		<td align=left><input type="text"  name="country" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Email:</td>
		<td align=left><input type="text"  name="email" /></td>
		<td>*</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align=right><font face=Arial size=2>Phone:</td>
		<td align=left><input type="text"  name="phone" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr><td colspan=4 background="image/blue_back.gif"><center><font color=white>Acceptance of the terms of use</font></center></td></tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align=center colspan=2><a href="termsofuse.jsp" target="_blank"><font face=Arial size=2>Read the Terms of Use</a></td>
		<td></td>
		<td></td>
	</tr>
	<tr>	
	 <td align=right>
          <input type="radio" name="termsaccept" value="accept" onclick="radio_click('accept')"><font face=Arial size=2>I accept *
      </td>
      <td align=right>
           <input type="radio" name="termsaccept" value="decline" checked="checked" onclick="radio_click('decline')"><font face=Arial size=2>I decline
      </td>
      <td align=left>*</td>
      <td></td>
   </tr>   
	<tr>
    	<td>&nbsp;</td><td>&nbsp;</td>
    	<td>&nbsp;</td><td>&nbsp;</td>
    </tr>
	<tr>
    	<td colspan=2 >* <font face=Arial size=2 color=blue>denotes required field </td>
    	<td>&nbsp;</td><td>&nbsp;</td>
    </tr>
   	<tr>
    	<td>&nbsp;</td><td>&nbsp;</td>
    	<td>&nbsp;</td><td>&nbsp;</td>
    </tr>
	<tr>
		<td>&nbsp;</td>
		<td align="right"><input type="submit" value="Register"/></td>
    	<td align="left"><input type="reset" value="Clear"/></td>
    	<td>&nbsp;</td>
	</tr>
    <tr>
    	<td>&nbsp;</td><td>&nbsp;</td>
    	<td>&nbsp;</td><td>&nbsp;</td>
    </tr>
    
  </table>
</td>
<td width=5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>
<td valign=top width=290>
<font face=Arial size=2 color=blue> <h2>What if registered?</h2> <br>
After registered, you will defaultly have the authority as an author of vegetation type revision proposals. 
You will be able to: <br>
1. Add new proposal <br>
2. Edit your unsubnitted proposals <br>
3. Submit proposals<br>
4. Check the statuses of your submitted proposals<br><br>
You can contribute more to apply for becoming a peer-viewer to review proposals submiited by other authors.<br><br>
Registered users of NVCRS are guaranteed privacy. Information collected on users and their activities at NVCRS is kept private and never shared with other organizations or persons.
Any proposal and evaluation that you submit to NVCRS database will be considered public. <br><br>


</font>
</td><td width=5> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</form>
</center>
<br>
<br>
