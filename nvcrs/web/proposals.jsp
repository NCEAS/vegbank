<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="java.util.*" %>

<script language="javascript" >
	function submitClick(act)
	{
		document.proposals.action.value=act;
		document.proposals.submit();
	}

	function validate()
	{

		return true;
	}

</script>


<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	proposal_Form fr=(proposal_Form)pageContext.getAttribute("proposal_form", PageContext.SESSION_SCOPE);
	String primaryKey=fr.getPrimaryKey();

%>

<%
if(fr!=null && manager!=null)
{
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
		<center><h2><%= msg %></h2></center>
<%
		manager.setMessage("");
	}
%>
	<center>
	<TABLE border="0" >
	<CAPTION><EM>Proposals</EM></CAPTION>
	<TR><TH bgcolor="#aacc00">Select<TH bgcolor="#ccffcc">Summary<TH bgcolor="#ccffcc">Current Status
	<TH bgcolor="#aacc00">View<TH bgcolor="#aacc00">Edit<TH bgcolor="#aacc00">Delete
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String sm=(String)tb.get("summary");
		String cs=(String)tb.get("current_status");
		String id=(String)tb.get("PROPOSAL_ID");
		String status=(String)tb.get("current_status");
		String href_view="viewproposal.go?id="+id;
		String href_delete="deleteproposal.go?id="+id;
		fr.setPROPOSAL_ID(id);
		manager.setProposalId(id);
		manager.setProposalStatus(status);

	    int intAction=manager.getPermissionByRecord("proposal",primaryKey,id);
%>
<TR><TD><input type="checkbox"><TD><%= sm %><TD><%= cs %>
<TD><a <href=" <%= href_view %> "><img src="image/view.gif"></a>
<%
if(intAction==1 || intAction==2 || intAction==3)
{
%>
<TD><a href=" <%= href_view %> "><img src="image/edit.gif"></a>
<TD><a href=" <%= href_delete %> "><img src="image/delete.gif"></a>
<%
}
else
{
%>
<TD bgcolor="#ccffcc">-
<TD bgcolor="#ccffcc">-
<%
}
}
%>

</TABLE>
<form action="/nvcrs/typereferences.go" name="proposals" >
<table>
<%
String primaryKeyValue="";
int intAction=manager.getPermissionByRecord("typereference",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
<tr>
	<td><input type="hidden" name="action" value="add" ></td>
	<td align="right"><input type="button" name ="delete" value="delete" onclick="submitClick('delete')" > </td>
</tr>
<%
}
if (intAction==1)
{
%>

<%
}
if (intAction==2)
{
%>
<%
}
if (intAction==3)
{
%>
<tr>
	<td><input type="hidden" name="action" value="add" ></td>
	<td align="right"><input type="button" name ="delete" value="delete" onclick="submitClick('delete')" > </td>
</tr>
<%
}
%>
</table>
</form>
</center>
<%
}
else
{
%>
<center>Please login or register first!</center>
<%
}
%>
