<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.*" %>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	boolean b=true;
	if(manager==null) b=false;
	else b=manager.isRegistered();
%>

<font face="Arial" size="1">
<center>
<br>
<table width="90%" border="0" cellpadding="2" cellspacing="2">  
<tr><td align="right"><b>Home</b></td></tr>
<tr><td align="right">.................................</td></tr>
<tr><td align="right"></td></tr>

<% 
if(!b)
{
%> 
<tr><td align="right"><b>Registered user</b></td></tr>
<tr><td align="right">Please <a href="/nvcrs/login.jsp">login</a></td></tr>
<tr><td align="right"><b>New user</b></td></tr>
<tr><td align="right">Please <a href="register.go">Register</td></tr>
<%
}
%>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>

<tr><td align="right" ><b>Helpful links</b></td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td align="right" ><a href="http://vegbank.org" target="_blank"><img src="image/vegbank.gif" border=0 /></a></td></tr>
<tr><td align="right" ><a href="http://www.natureserve.org/explorer" target="_blank"><img src="image/natureserve.gif" border=0 /></a></td></tr>
<tr><td align="right" ><a href="http://www.esa.org/vegweb/" target="_blank"><font face=Arial size=2>ESA Vegetation Panel</a></td></tr>

</table>
</center>
 </font>
 <br><br>