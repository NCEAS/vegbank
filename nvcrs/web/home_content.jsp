<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<center>
<img src="image/bgcontent1.bmp"<br>
<table width="90%" border="0">  
<tr><td align="right" >Please login</td></tr>
<html:form action="/logon" > <!-- onsubmit="return validateLogonForm()"> -->
	 <font size=2 color=#ccffcc>
	 <table width="90%" border="0" valign="top" bgcolor="#ccffcc">
	 <tr><td align="right">Name</td></tr>
     <tr><td align="right"><html:text property="username" /></td></tr>
  	 <tr><td align="right">Password</td></tr>
	 <tr><td align="right"><html:password property="password" /></td></tr>
	 <tr><td align="right"><html:submit property="login" value="Login" /></td></tr>
	 </table>
	 </font>
</html:form>
<table width="90%" border="0">  
<tr><td align="right" >New user? Please<a href="register.go">Register</center></td></tr>
<p></p>
<p></p>
<table width="90%" border="0">  
<tr><td align="right" >vegbank</td></tr>
<tr><td align="right" >natureserve</td></tr>
<tr><td align="right" >other links</td></tr>
</table>
</center>
