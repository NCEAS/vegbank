<!--

  plot.jsp
   Created on Wed Apr 21 12:25:19 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	function submitClick(act)
	{
		document.plot_form.action.value=act;
		document.plot_form.submit();
	}

	function validate()
	{

		return true;
	}

</script>

<font face=Arial >
<img src="image/title_sep_line.gif">
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Proposal :: Type :: Plot
</td></tr>
</table >
<br>
<br>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	plot_Form fr=(plot_Form)pageContext.getAttribute("plot_form", PageContext.SESSION_SCOPE);
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
<center><font color=red size="1"><%= msg %></font></center>
<%
	manager.setMessage("");
	}
%>
<%
if(fr!=null && manager!=null)
{
	String accessionCode=fr.getVB_AccessionCode();
	String primaryKey=fr.getPrimaryKey();
	String primaryKeyValue=fr.getFieldValue(primaryKey);
	int intAction=manager.getPermissionByRecord("plot",primaryKey,primaryKeyValue);
%>
<center>
<html:form action="/plot" onsubmit="return validate()" >
		<html:hidden  property="PLOT_ID" />
		<html:hidden  property="TYPE_ID" />
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>PLOT</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align=right><font size=2>Vegbank plot code:</td>
		<td><html:text  property="VB_AccessionCode" />
		<%
		if(intAction>-1 && intAction<4)
		{
		%>
			&nbsp;&nbsp;<a href="http://vegbank.org/vegbank/LoadPlotQuery.do" target="_blank">[ select ]</a>
		<%
		}
		if(!accessionCode.equals(BeanManager.EMPTY_VALUE))
		{
		%>	
			&nbsp;&nbsp;<a href="http://vegbank.org/vegbank/servlet/DataRequestServlet?
			requestDataType=vegPlot&resultType=summary&queryType=simple&accessionCode=<%= accessionCode %>" target="_blank">
			[ view ]</a>
		<%
		}
		%>
		</td>
	</tr>
	<tr>
		<td align=right><font size=2>Plot use:</td>
		<td><html:text  property="plotUse" /></td>
	</tr>
	<tr>
		<td></td>
		<td><html:hidden value="add" property="action" /></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%
if(intAction==0)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="add" value="add" onclick="submitClick('add')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==1)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="save" value="save" onclick="submitClick('save')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==2)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
		</td>
</tr>
<%
}
if (intAction==3)
{
%>
<tr>
	<td align="center"><html:button property="save" value="save" onclick="submitClick('save')" /> </td>
	<td align="left"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
		<html:submit property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
%>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
</table>
</html:form>
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

