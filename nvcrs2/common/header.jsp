<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<%
BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
%>
<table border=0 cellpadding=5 cellspacing=0 width="75%">
<tr><td>
<%
if(manager!=null)
{
	
	boolean b=manager.isRegistered();
	if(b)
	{
		String username=manager.getUserName();
		String useremail=manager.getUserEmail();
%>
<font align="left" size="1"><%= username %><br>
<%= useremail %><br>
<a href="userinfo.jsp">Edit</a> |<a href="/nvcrs/logoff.go">Logoff</a></font>
<%
}
}
%>
</td>
<td align="center">
<center><h1>NVC Revision System</h1></center>
</td>
<tr>
</center>
</table>