<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<!-- <img src="image/bgcontent1.bmp"<br> -->
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
<tr><td align="left"><b>Author</b></td></tr>
<tr><td align="left"><a href="author.go?action=new"><img src="image/newproposal.gif"></a></td></tr>
<tr><td align="left"><a href="author.go?action=query&con=unsubmitted"><img src="image/unsubmitted.gif"></a></td></tr>
<tr><td align="left"><a href="author.go?action=query&con=submitted"><img src="image/submitted.gif"></a></td></tr>
<tr><td align="left"><a href="author.go?action=query&con=approved"><img src="image/approved.gif"></a></td></tr>
<tr><td align="left"><a href="author.go?action=query&con=declined"><img src="image/declined.gif"></a></td></tr>
<tr><td align="left"><a href="author.go?action=query"><img src="image/search.gif"></a></td></tr>

<%
		}
		else if(role.equals("Peer-viewer"))
		{
%>
<!-- contents for peer-viewer -->
<tr><td align="left"><b>Peer-viewer</b></td></tr>
<tr><td align="left"><a href="peer-viewer?action='query'&con='new'">New Assignment</a></td></tr>
<tr><td align="left"><a href="peer-viewer?action='query'&con='unsubmitted'">Unsubmitted Evaluation</a></td></tr>
<tr><td align="left"><a href="peer-viewer?action='query'&con='submiited'">Submitted Evaluation</a></td></tr>
<tr><td align="left"><a href="peer-viewer?action='query'">Search Evaluation</a></td></tr>

<%
		}
		else if(role.equals("Manager"))
		{
%>
<!-- contents for peer-viewer -->
<tr><td align="left"><h1>Manager</h1></td></tr>
<tr><td align="left"><h2>Proposal Management</h2></td></tr>
<tr><td align="left"><a href="Manager?target='proposal'&action='query'&con='new'">New Proposal</a></td></tr>
<tr><td align="left"><a href="Manager?target='proposal'&action='query'&con='assigned'">Assigned Proposal</a></td></tr>
<tr><td align="left"><a href="Manager?target='proposal'&action='query'&con='evaluating'">Proposal in Evaluation</a></td></tr>
<tr><td align="left"><a href="Manager?target='proposal'&action='query'&con='evaluated'">Evaluated Proposal</a></td></tr>
<tr><td align="left"><a href="Manager?target='proposal'&action='query'">Search Proposal</a></td></tr>
<tr><td align="left"><h2>User Management</h2></td></tr>
<tr><td align="left"><a href="Manager?target='usr'&action='query'&con='peer-viewer'">Peer-viewer</a></td></tr>
<tr><td align="left"><a href="Manager?target='usr'&action='query'&con='Author'">Author</a></td></tr>
<tr><td align="left"><a href="Manager?target='usr'&action='query'">Search User</a></td></tr>


<%
		}
		else
		{
		}
	}
%>
</table>

