<!--

  event.jsp
   Created on Wed Apr 21 14:02:14 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	function submitClick(act)
	{
		document.event_form.action.value=act;
		document.event_form.submit();
	}

	function validate()
	{

		return true;
	}

</script>

<center>
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Proposal :: Decision
</td></tr>
</table >
<br>
<br>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	event_Form fr=(event_Form)pageContext.getAttribute("event_form", PageContext.SESSION_SCOPE);
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
<center><h2><%= msg %></h2></center>
<%
	manager.setMessage("");
	}
%>
<%
if(fr!=null && manager!=null)
{
%>

<html:form action="/event" onsubmit="return validate()" >
		<html:hidden  property="EVENT_ID" />
		<html:hidden  property="PROPOSAL_ID" />
		<html:hidden  property="USR_ID" />
		<html:hidden  property="ROLE_ID" />
		<html:hidden  property="SUBJECTUSR_ID" />
		<html:hidden  property="ACTION_ID" />
		<html:hidden  property="eventDate" />
<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>DECISION</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align=right><font size=2>Summary:</td>
		<td><font size=2><font size=2>
			<html:select  property="summary">	
				<option><font size=2>approved
				<option><font size=2>revision
				<option><font size=2>declined
			</html:select>
		</td>
	</tr>
	</tr>
		<td align=right><font size=2>Evaluation:</td>
		<td><font size=2><html:textarea  property="reviewText" cols="30" rows="5"/></td>
	</tr>
	<tr>
		<td align=right><font size=2>Private comments:</td>
		<td><font size=2><html:textarea  property="privateComments"  cols="30" rows="5"/></td>
	</tr>
	</tr>
		<td align=right><font size=2>Public comments:</td>
		<td><font size=2><html:textarea  property="publicComments"  cols="30" rows="5"/></td>
	</tr>
	<tr>
		<td align=right><font size=2>Supporting document URL:</td>
		<td><font size=2><html:text  property="supporting_doc_URL" /></td>
	</tr>
	<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("event",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="add" value="add" onclick="submitClick('add')" /> 
</td>
</tr>
<%
}
%>
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

