<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<table border=0 cellpadding=0 cellspacing=0 width="90%">
<tr><td>
<%
	BeanManager manager1=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	if(manager1!=null)
	{
		String role1=manager1.getUserCurrentRole();
		if(role1.equals("Author"))
		{
%>
<!-- contents for author -->
<%@ include file="author_first_page.jsp" %> 
<%
		}
		else if(role1.equals("Peer-viewer"))
		{
%>
<!-- contents for peer-viewer -->
<%@ include file="peerviewer_first_page.jsp" %>
<%
		}
		else if(role1.equals("Manager"))
		{
%>
<!-- contents for peer-viewer -->
<%@ include file="manager_first_page.jsp" %>
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
