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
	String doc1=fr.getDocument1();
	String doc2=fr.getDocument2();
	String doc3=fr.getDocument3();
	String primaryKey=fr.getPrimaryKey();
	String primaryKeyValue=fr.getFieldValue(primaryKey);
	String status=manager.getAssignmentStatus(manager.getProposalId(),manager.getUserId());
	int intAction=-1;
	if(!status.equals("completed"))
	{
		intAction=manager.getPermissionByRecord("event",primaryKey,primaryKeyValue);
	}
%>
<center>
<h3>event</h3>
<br><br>
<html:form action="/event" onsubmit="return validate()" >
		<html:hidden  property="EVENT_ID" />
		<html:hidden  property="PROPOSAL_ID" />
		<html:hidden  property="USR_ID" />
		<html:hidden  property="ROLE_ID" />
		<html:hidden  property="SUBJECTUSR_ID" />
		<html:hidden  property="document1" />
		<html:hidden  property="document2" />
		<html:hidden  property="document3" />
<table border="0">
	<tr>
		<td>ACTION_ID:</td>
		<td><html:text  property="ACTION_ID" /></td>
		<td>eventDate:</td>
		<td><html:text  property="eventDate" /></td>
	</tr>
	<tr>
		<td>privateComments:</td>
		<td><html:text  property="privateComments" /></td>
		<td>publicComments:</td>
		<td><html:text  property="publicComments" /></td>
	</tr>
	<tr>
		<td>summary:</td>
		<td><html:text  property="summary" /></td>
		<td>reviewText:</td>
		<td><html:text  property="reviewText" /></td>
	</tr>
	<tr>
	</tr>
	<tr>
		<td align=right valign=top>Supporting Documents:</td>
			<td>
				<table width=350 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
				<%
					if(!primaryKeyValue.equals(BeanManager.UNKNOWN_ID) && (intAction==1 || intAction==2||intAction==3))
					{
				%>
					<tr><td><a href="event_document_main.jsp">[ Edit ]</a></td></tr>
				<%
					}
				%>
				<%
					if(doc1.trim().length()>0)
					{
				%>
					<tr><td><a href="documents/<%= doc1 %>" target="_blank"><%= doc1 %></a></td></tr>
				<%
					}
				%>
				<%
					if(doc2.trim().length()>0)
					{
				%>
					<tr><td><a href="documents/<%= doc2 %>" target="_blank"><%= doc2 %></a></td></tr>
				<%
					}
				%>
				<%
					if(doc3.trim().length()>0)
					{
				%>
					<tr><td><a href="documents/<%= doc3 %>" target="_blank"><%= doc3 %></a></td></tr>
				<%
					}
				%>
				</table>
			</td>
			<td></td>
			<td></td>
		</tr>	
<tr><td></td><td><html:hidden value="add" property="action" /></td></tr>
<%

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
if (intAction==1)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> 
	<td align="right"><html:button property="handin" value="handin" onclick="submitClick('handin')" /> 
</td>
</tr>
<%
}
if (intAction==3)
{
%>
<tr>
	<td></td>
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> 
	<td align="right"><html:button property="handin" value="handin" onclick="submitClick('handin')" /> 
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

