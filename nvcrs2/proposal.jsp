<!--

  proposal.jsp
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
		document.proposal_form.action.value=act;
		document.proposal_form.submit();
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
<b>Proposal :: Summary
</td></tr>
</table >
<br>
<br>
<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	proposal_Form fr=(proposal_Form)pageContext.getAttribute("proposal_form", PageContext.SESSION_SCOPE);
	

	if(fr!=null && manager!=null)
	{
		String msg=(String)manager.getMessage();
		String prev_id=fr.getPreviousProposal_ID();
		if(msg!=null)
		{
%>
			<center><font color=blue size="1"><%= msg %></font></center>
<%
			manager.setMessage("");
		}
%>

<html:form action="/proposal" onsubmit="return validate()" >
		<html:hidden  property="PROPOSAL_ID" />
		<html:hidden  property="previousProposal_ID" />
		<html:hidden  property="PROJECT_ID" />
		<html:hidden  property="current_status" />
		<html:hidden  property="document1" />
		<html:hidden  property="document2" />
		<html:hidden  property="document3" />
		
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>SUMMARY</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
    	<%
    	if(!prev_id.equals(BeanManager.UNKNOWN_ID))
    	{
    	%>
    	<tr>
			<td align=right>Previous Proposal:</td>
			<td width=350><a href="proposals.go?act=view&id=<%= prev_id %>">view</a></td>
		</tr>
		<%
		}
		%>
     	
		<tr>	
			<td align=right>summary:</td>
			<td width=350><html:textarea property="summary" cols="35" rows="10"/></td>
		</tr>
		<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("proposal",primaryKey,primaryKeyValue);

if(intAction==0)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="add" value="add" onclick="submitClick('add')" />&nbsp;&nbsp;&nbsp; 
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
	<!-- <td align="right"><html:button property="delete" value="delete" onclick="submitClick('delete')" /> -->
		</td>
</tr>
<%
}
if (intAction==3)
{
%>
<tr>
	<td></td>
	<td align="left"><html:button property="save" value="save" onclick="submitClick('save')" /> 
<!--	<td align="right"><html:button property="delete" value="delete" onclick="submitClick('delete')" />  -->
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

