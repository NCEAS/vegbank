<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<%
BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
String role=null;
String status=null;
String assignment_status=null;
String proposalId=null;
if(manager!=null)
{
	role=manager.getUserCurrentRole();
	status=manager.getProposalStatus();
	proposalId=manager.getProposalId();
%>
<font face="Arial" size="1">
<br>
<!-- <img src="image/bgcontent1.bmp"<br> -->
<table border=0 cellpadding=2 cellspacing=2 width="100%">
<tr><td align="right"><b>Proposal</b></td></tr>
<tr><td align="right">.................................</td></tr>
<tr><td align="right"></td></tr>
<tr><td align="right"><b>Data</b></td></tr>
<tr><td align="right" ><a href="proposal.go?target=proposal">Summary</a></td></tr>
<% 
	if(!proposalId.equals(BeanManager.UNKNOWN_ID))
	{
%>
<tr><td align="right" ><a href="proposal_document_main.jsp">Documents</a></td></tr>
<tr><td align="right" ><a href="proposal.go?target=project">Project</a></td></tr>
<tr><td align="right" ><a href="proposal.go?target=types">Types</a></td></tr>
<%
	}
%>
</table>
<%
if(role.equals("Author"))
{
%>
	<table border=0 cellpadding=2 cellspacing=2 width="100%">
	<tr><td align="right"><b>Related Info</b></td></tr>
	<tr><td align="right" ><a href="proposal.go?target=events">History</a></td></tr>
	<tr><td align="right" ><a href="proposal.go?target=evaluation">Evaluation</a></td></tr>
	</table>
	<%
	if(status.equals("unsubmitted") && !proposalId.equals(BeanManager.UNKNOWN_ID))
	{
	%>
		<table border=0 cellpadding=2 cellspacing=2 width="100%">
		<tr><td align="right"><b>Action</b></td></tr>
		<tr><td align="right" ><a href="proposal.go?target=submit">Submit</a></td></tr>
		</table>
	<%
		}
		else if(status.equals("revision"))
		{
	%>
		<table border=0 cellpadding=2 cellspacing=2 width="100%">
		<tr><td align="right"><b>Action</b></td></tr>
		<tr><td align="right" ><a href="proposal.go?target=revise">Revise</a></td></tr>
		</table>
	<%
		}
	%>
<%
}
else if (role.equals("Peer-viewer"))
{
	assignment_status=manager.getAssignmentStatus(manager.getProposalId(),manager.getUserId());
	if(assignment_status.equals("new"))
	{
%>	
	<table border=0 cellpadding=2 cellspacing=2 width="100%">
	<tr><td align="right"><b>Action</b></td></tr>
	<tr><td align="right" ><a href="peer-viewer.go?action=accept">Accept Assignment</a></td></tr>
	<tr><td align="right" ><a href="peer-viewer.go?action=decline">Decline Assignment</a></td></tr>
	</table>
<%
	}
	else if(assignment_status.equals("completed"))
	{
%>
	<table border=0 cellpadding=2 cellspacing=2 width="100%">
	<tr><td align="right"<b>Related Info</b></td></tr>
	<tr><td align="right" ><a href="peer-viewer.go?action=evaluate">Evaluation</a></td></tr>
	</table>
<%
	}
	else
	{
%>
	<table border=0 cellpadding=2 cellspacing=2 width="100%">
	<tr><td align="right"><b>Action</b></td></tr>
	<tr><td align="right" ><a href="peer-viewer.go?action=evaluate">Evaluate</a></td></tr>
	</table>
<%
	}
}
else if(role.equals("Manager"))
{
	
%>
	<table border=0 cellpadding=2 cellspacing=2 width="100%">
	<tr><td align="right"><b>Related Info</b></td></tr>
	<tr><td align="right" ><a href="proposal.go?target=events">History</a></td></tr>
	<tr><td align="right" ><a href="proposal.go?target=assignments">Assignments</a></td></tr>
	<tr><td align="right" ><a href="proposal.go?target=evaluation">Evaluation</a></td></tr>
<%
	if(status.equals("approved") || status.equals("declined")||status.equals("revision required"))
	{
%>
		<tr><td align="right" ><a href="manager.go?action=decide">Decision</a></td></tr>
<%
	}
%>
	<tr><td align="right" ><a href="proposal.go?target=author">Author</a></td></tr>
	</table>
<%
	if(! (status.equals("approved") || 
			status.equals("declined") ||status.equals("revision required") || status.equals("unsubmitted")))
	{
%>
		<table border=0 cellpadding=2 cellspacing=2 width="100%">
		<tr><td align="right"><b>Action</b></td></tr>
		<tr><td align="right" ><a href="manager.go?action=assign">Assign peer-viewers</a></td></tr>
		<tr><td align="right" ><a href="manager.go?action=decide">Decide</a></td></tr>
		</table>
<%
	}
}
%>
<br><br><br><br><br>
<table border=0 cellpadding=2 cellspacing=2 width="100%">
	<tr><td align="right" ><a href="/nvcrs/proposals_main.jsp">Back</a></td></tr>
</table>
<%
}
else
{
%>
Please login or register first.
<%
}
%>
</font>
<br><br><br><br><br>