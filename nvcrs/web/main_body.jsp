<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<table border=0 cellpadding=0 cellspacing=0 width="90%">
<tr><td>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	if(manager!=null)
	{
		String role=manager.getUserCurrentRole();
		if(role.equals("Author"))
		{
%>
<!-- contents for author -->
<h1>[First page for Authors]</h1>
<%
		}
		else if(role.equals("Peer-viewer"))
		{
%>
<!-- contents for peer-viewer -->
<h1>[First page for Peer-viewer]</h1>
<%
		}
		else if(role.equals("Manager"))
		{
%>
<!-- contents for peer-viewer -->
<h1>[First page for Manager]</h1>
<%
		}
		else
		{
%>
Uncertain role
<%		
		}
	}
%>
</td></tr>
</table>
