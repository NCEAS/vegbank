<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>


<font face="Arial" size="1">
<br>
<table border=0 cellpadding=2 cellspacing=2 width="90%">
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	if(manager!=null)
	{
		String role=manager.getUserCurrentRole();
		if(role.equals("Author"))
		{
%>
<!-- contents for author -->
<tr><td align="right"><b>Author</b></td></tr>
<tr><td align="right">.................................</td></tr>
<tr><td align="right"></td></tr>
<tr><td align="right"><a href="author.go?act=new"><b>Add a proposal</b></a></td></tr>
<tr><td align="right"><b>Find my proposals</b></td></tr>
<tr><td align="right"><a href="author.go?act=query&con=unsubmitted">Unsubmitted</a></td></tr>
<tr><td align="right"><a href="author.go?act=query&con=submitted">Submitted</a></td></tr>
<tr><td align="right"><a href="author.go?act=query&con=processing">Processing</a></td></tr>
<tr><td align="right"><a href="author.go?act=query&con=approved">Approved</a></td></tr>
<tr><td align="right"><a href="author.go?act=query&con=revision">Revision Required</a></td></tr>
<tr><td align="right"><a href="author.go?act=query&con=declined">Declined</a></td></tr>
<%
		}
		else if(role.equals("Peer-viewer"))
		{
%>
<!-- contents for peer-viewer -->
<tr><td align="right"><b>Peer-viewer</b></td></tr>
<tr><td align="right">.................................</td></tr>
<tr><td align="right"></td></tr>
<tr><td align="right"><b>My assignments</b></td></tr>
<tr><td align="right"><a href="peer-viewer.go?action=query&con=new">New Assignment</a></td></tr>
<tr><td align="right"><a href="peer-viewer.go?action=query&con=accepted">Accepted Assignment</a></td></tr>
<tr><td align="right"><a href="peer-viewer.go?action=query&con=evaluating">Evaluating Assignment</a></td></tr>
<tr><td align="right"><a href="peer-viewer.go?action=query&con=completed">Completed Assignment</a></td></tr>
<%
		}
		else if(role.equals("Manager"))
		{
%>
<!-- contents for peer-viewer -->
<tr><td align="right"><b>Manager</b></td></tr>
<tr><td align="right">.................................</td></tr>
<tr><td align="right"></td></tr>
<tr><td align="right"><b>Proposal</b></td></tr>
<tr><td align="right"><a href="manager.go?target=proposal&action=query&con=submitted">New Proposal</a></td></tr>
<tr><td align="right"><a href="manager.go?target=proposal&action=query&con=assigned">Assigned</a></td></tr>
<tr><td align="right"><a href="manager.go?target=proposal&action=query&con=evaluating">Processing</a></td></tr>
<tr><td align="right"><a href="manager.go?target=proposal&action=query&con=evaluated">Evaluated</a></td></tr>
<tr><td align="right"><a href="manager.go?target=proposal&action=query&con=decided">Decided</a></td></tr>
<!-- <tr><td align="right"><a href="manager.go?target=proposal&action=query">Search Proposal</a></td></tr> -->
<tr><td align="right"><b>User</b></td></tr>
<tr><td align="right"><a href="manager.go?target=usr&action=query&con=Peer-viewer">Peer-viewer</a></td></tr>
<tr><td align="right"><a href="manager.go?target=usr&action=query&con=Author">Author</a></td></tr>
<!-- <tr><td align="right"><a href="manager.go?target=usr&action=query">Search User</a></td></tr> -->
<tr><td align="right"><b>System</b></td></tr>
<tr><td align="right"><a href="system_setting_main.jsp">Settings</a></td></tr>
<%
		}
		else
		{
		}
	}
%>
</table>
</font>
<br><br><br><br><br><br><br><br>