<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<%
BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
String permission=null;
if(manager!=null)
{
	permission=manager.getUserPermission();
	if(permission==null) permission="0";
}
%>

<table border=0 cellpadding=2 cellspacing=1 width="%80" bgcolor=#FFCCFF>
<font color="white">
<tr>
	<td> </td>
	
	<td> <a href="index.jsp">Home</a> |</td>
	<%
		if(permission!=null)
		{
			if(permission.equals("0"))
			{
			%>
			<td> <a href="menu.go?role=Author">Author</a> |</td>
			<%
			}
			else if(permission.equals("1"))
			{
			%>
			<td><a href="peer-viewer.jsp">Peer-viewer</a> |</td>
			<%
			}
			else if(permission.equals("2"))
			{
			%>
			<td><a href="manager.jsp">Manager</a> |</td>
			<%
			}
			else if(permission.equals("3"))
			{
			%>
			<td> <a href="author.jsp">Author</a> |</td>
			<td><a href="peer-viewer.jsp">Peer-viewer</a> |</td>
			<%
			}
			else if(permission.equals("4"))
			{
			%>
			<td> <a href="author.jsp">Author</a> |</td>
			<td><a href="manager.jsp">Manager</a> |</td>
			<%
			}
			else if(permission.equals("5"))
			{
			%>
			<td><a href="peer-viewer.jsp">Peer-viewer</a> |</td>
			<td><a href="manager.jsp">Manager</a> |</td>
			<%
			}
			else if(permission.equals("6"))
			{
			%>
			<td> <a href="author.jsp">Author</a> |</td>
			<td><a href="peer-viewer.jsp">Peer-viewer</a> |</td>
			<td><a href="manager.jsp">Manager</a> |</td>
			<%
			}
			else
			{
			}
		}
		%>
	
	<td><a href="about.jsp">About</a> </td>
</tr>
</font>
</table>
