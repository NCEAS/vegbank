<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<%
BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
String role=null;
String status=null;
if(manager!=null)
{
	role=manager.getUserCurrentRole();
	status=manager.getProposalStatus();
%>

<!-- <img src="image/bgcontent1.bmp"<br> -->
<table border=0 cellpadding=2 cellspacing=2 width="90%">
<tr><td align="left" bgcolor="#ccffcc"><h3>Data</h3></td></tr>
<tr><td align="left" ><a href="proposal.go?target=proposal">Proposal</a></td></tr>
<tr><td align="left" ><a href="proposal.go?target=project">Project</a></td></tr>
<tr><td align="left" ><a href="proposal.go?target=types">Types</a></td></tr>
</table>
<%
if(role.equals("Author"))
{
%>
	<table border=0 cellpadding=2 cellspacing=2 width="90%">
	<tr><td align="left" bgcolor="#ccffcc"><h3>Related Info</h3></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=history">History</a></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=evaluation">Evaluation</a></td></tr>
	</table>
	<%
		if(status.equals("unsubmitted"))
		{
	%>
		<table border=0 cellpadding=2 cellspacing=2 width="90%">
		<tr><td align="left" bgcolor="#ccffcc"><h3>Action</h3></td></tr>
		<tr><td align="left" ><a href="proposal.go?target=submit">Submit</a></td></tr>
		</table>
	<%
		}
	%>
<%
}
else if (role.equals("Peer-viewer"))
{
%>
	<table border=0 cellpadding=2 cellspacing=2 width="90%">
	<tr><td align="left" bgcolor="#ccffcc"><h3>Related Info</h3></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=evaluation">Evaluation</a></td></tr>
	</table>
	<table border=0 cellpadding=2 cellspacing=2 width="90%">
	<tr><td align="left" bgcolor="#ccffcc"><h3>Action</h3></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=evaluate">Evaluate</a></td></tr>
	</table>
<%
}
else if(role.equals("Manager"))
{
%>
	<table border=0 cellpadding=2 cellspacing=2 width="90%">
	<tr><td align="left" bgcolor="#ccffcc"><h3>Related Info</h3></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=history">History</a></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=evaluation">Evaluation</a></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=author">Author</a></td></tr>
	</table>
	<table border=0 cellpadding=2 cellspacing=2 width="90%">
	<tr><td align="left" bgcolor="#ccffcc"><h3>Action</h3></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=assign">Assign peer-viewers</a></td></tr>
	<tr><td align="left" ><a href="proposal.go?target=decide">Decide</a></td></tr>
	</table>
<%
}
%>
<%
}
else
{
%>
Please login or register first.
<%
}
%>
