<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="java.util.*" %>

<script language="javascript" >
	function submitClick(act)
	{
		document.plots.action.value=act;
		document.plots.submit();
	}

	function validate()
	{

		return true;
	}

</script>


<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	plot_Form fr=(plot_Form)pageContext.getAttribute("plot_form", PageContext.SESSION_SCOPE);
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
	<CAPTION><EM>Plots</EM></CAPTION>
	<TR><TH bgcolor="#aacc00">Select<TH bgcolor="#ccffcc">vegBank Accession Code<TH bgcolor="#ccffcc">Plot use
	<TH bgcolor="#aacc00">View<TH bgcolor="#aacc00">Edit<TH bgcolor="#aacc00">Delete
<%
	ArrayList records=fr.getRecords();
	int num=records.size();
	int i;
	for(i=0;i<num;i++)
	{
		Hashtable tb=(Hashtable)records.get(i);
		String ac=(String)tb.get("VB_AccessionCode");
		String pu=(String)tb.get("plotUse");
		String id=(String)tb.get("PLOT_ID");
		String href_view="viewtype.go?target=plot&id="+id;
		String href_delete="deletetype.go?target=plot&id="+id;
	    int intAction=manager.getPermissionByRecord("plot",primaryKey,id);
%>
<TR><TD><input type="checkbox"><TD><%= ac %><TD><%= pu %>
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
<form action="/nvcrs/plots.go" name="plots" >
<table>
<%
String primaryKeyValue="";
int intAction=manager.getPermissionByRecord("plot",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
<tr>
	<td><input type="hidden" name="action" value="add" ></td>
	<td align="right"><input type="button" name ="add" value="add" onclick="submitClick('add')" > </td>
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
