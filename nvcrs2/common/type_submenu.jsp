<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="java.util.*" %>

<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	String typeid="-1";
	String target="nothing";
	if(manager!=null)
	{
		target=manager.getSomething();
		type_Form f=(type_Form)pageContext.getAttribute("type_form", PageContext.SESSION_SCOPE);
		if(f!=null)
			typeid=f.getTYPE_ID();
		manager.setTypeId(typeid);
	}
	if(!typeid.equals(BeanManager.UNKNOWN_ID))
	{
%>

<center>
<Table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
<tr>
<%
	if(target.equals("compositions"))
	{
%>
	<td width=100><img src="image/tab_sel_composition.gif" border="0"></td>
<%
	}
	else
	{
%>
	<td width=100><a href="type.go?target=compositions"><img src="image/tab_composition.gif" border="0"></a></td>
<%
	}
%>
<%
	if(target.equals("distributions"))
	{
%>
	<td width=10><img src="image/tab_sel_distribution.gif" border=0></td>
<%
	}
	else
	{
%>
	<td width=10><a href="type.go?target=distributions"><img src="image/tab_distribution.gif" border=0></a></td>
<%
	}
%>

<%
	if(target.equals("plots"))
	{
%>
	<td width=10><img src="image/tab_sel_plot.gif" border=0></td>
<%
	}
	else
	{
%>
	<td width=10><a href="type.go?target=plots"><img src="image/tab_plot.gif" border=0></a></td>
<%
	}
%>
<%
	if(target.equals("typenames"))
	{
%>

	<td width=10><img src="image/tab_sel_name.gif" border=0></td>
<%
	}
	else
	{
%>
	<td width=10><a href="type.go?target=typenames"><img src="image/tab_name.gif" border=0></a></td>
<%
	}
%>
<%
	if(target.equals("typereferences"))
	{
%>
	<td width=10><img src="image/tab_sel_reference.gif" border=0></td>
<%
	}
	else
	{
%>
	<td width=10><a href="type.go?target=typereferences"><img src="image/tab_reference.gif" border=0></a></td>
<%
	}
%>
</tr>
<tr >
<td bgcolor=#7393d5>&nbsp;</td>
<td bgcolor=#7393d5>&nbsp;</td>
<td  bgcolor=#7393d5>&nbsp;</td>
<td  bgcolor=#7393d5>&nbsp;</td>
<td  bgcolor=#7393d5>&nbsp;</td>
</tr>
</table>
</center>
<%
}
%>