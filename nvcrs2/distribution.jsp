<!--

  distribution.jsp
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
		document.distribution_form.action.value=act;
		document.distribution_form.submit();
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
<b>Proposal :: Type :: Distribution
</td></tr>
</table >
<br>
<br>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	distribution_Form fr=(distribution_Form)pageContext.getAttribute("distribution_form", PageContext.SESSION_SCOPE);
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
%>
<center>
<html:form action="/distribution" onsubmit="return validate()" >
		<html:hidden  property="DISTRIBUTION_ID" />
		<html:hidden  property="TYPE_ID" />
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>DISTRIBUTION</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td  align=right><font size=2>Vegbank place code:</td>
		<td><html:text  property="VB_AccessionCode" /></td>
	</tr>
	<tr>
		<td  align=right><font size=2>Place name:</td>
		<td><html:text  property="placeName" /></td>
	</tr>
	<tr>
		<td  align=right><font size=2>Place type:</td>
		<td><html:text  property="placeType" /></td>
	</tr>
	<tr>
		<td  align=right><font size=2>Place distribution:</td>
		<td><html:text  property="placeDistribution" /></td>
	</tr>
	<tr>
		<td  align=right><font size=2>Place pattern:</td>
		<td><html:text  property="placePattern" /></td>
	</tr>
	<tr>
		<td  align=right><font size=2>Confidence:</td>
		<td><html:text  property="confidence" /></td>
	</tr>
	<tr>
		<td  align=right><font size=2>Place notes:</td>
		<td><html:textarea  property="placeNotes" cols="30" rows="5"/></td>
	</tr>
	<tr>
		<td></td>
		<td><html:hidden value="add" property="action" /></td>
	</tr>
	
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("distribution",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="add" value="add" onclick="submitClick('add')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==1)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> 
		<html:button property="cancel" value="close" onclick="submitClick('cancel')" /></td>
</tr>
<%
}
if (intAction==2)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
		</td>
</tr>
<%
}
if (intAction==3)
{
%>
<tr>

	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> 
	<td align="center"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> 
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

