<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="java.util.*" %>

<script language="javascript" >
	function submitClick(act)
	{
		document.types.action.value=act;
		document.types.submit();
	}

	function validate()
	{

		return true;
	}

</script>


<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	
	type_Form fr=(type_Form)pageContext.getAttribute("type_form", PageContext.SESSION_SCOPE);
	String primaryKey=fr.getPrimaryKey();
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
		<br>
		<TABLE border="0" bgcolor=#aa22a2">
		<CAPTION><EM>Types</EM></CAPTION>
		<TR><TH bgcolor=#ccffcc">Select<TH bgcolor=#ccffcc">Action<TH bgcolor=#ccffcc">User Type<TH bgcolor=#ccffcc">Primary Name
		<TH bgcolor=#ccffcc">Level<TH bgcolor=#ccffcc">View<TH bgcolor=#ccffcc">Edit<TH bgcolor=#ccffcc">Delete
<%
		ArrayList records=fr.getRecords();
		int num=records.size();
		int i;
		for(i=0;i<num;i++)
		{
			Hashtable tb=(Hashtable)records.get(i);
			String id=(String)tb.get("TYPE_ID");
			String at=(String)tb.get("actionType");
			String ut=(String)tb.get("userTypeCode");
			String pn=(String)tb.get("primaryName");
			String lv=(String)tb.get("level");
			String href_view="viewtype.go?target=type&id="+id;
			String href_delete="deletetype.go?target=type&id="+id;
			int intAction=manager.getPermissionByRecord("type",primaryKey,id);
%>
			<TR><TD><input type="checkbox" ><TD><%= at %><TD><%= ut %><TD><%= pn %><TD><%= lv %>
			<TD><a href=" <%= href_view %> " ><img src="image/view.gif"></a>
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
		<form action="/nvcrs/types.go" name="types" >
		<table>

<%
		String primaryKeyValue="";
		int intAction=manager.getPermissionByRecord("type",primaryKey,primaryKeyValue);
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
		else if (intAction==1)
		{
		}
		else if (intAction==2)
		{
		}
		else if (intAction==3)
		{
		}
		else
		{
		}
%>
		</table>
		</center>
		</form>

<%
	}
	else
	{
%>
		<center>Please login or register first!</center>
<%
	}
%>
