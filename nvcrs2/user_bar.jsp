<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<%
BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
%>
<table width=650 border=0 cellpadding=0 cellspacing=0 width="75%">
<tr><td align="right" width="650">
<%
if(manager!=null)
{
	
	boolean b=manager.isRegistered();
	if(b)
	{
		String username=manager.getUserName();
		String useremail=manager.getUserEmail();
%>
		<font  face="Arial" size="1">You logged in as <%= username %> (
		<%= useremail %> ) [ <a href="/nvcrs/usr.go">Edit</a> ] [ <a href="/nvcrs/logoff.go">Logoff</a> ]</font>
<%
	}
}
else
{
%>
		<font  face="Arial" size="1">Welcome! </font>
<%
}
%>
</td>
<tr>
</center>
</table>