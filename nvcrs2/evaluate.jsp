<!--

  evaluate.jsp
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
<b>Proposal :: Evaluation
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
<center><font face="Arial" color=blue size="1"><%= msg %></font></center>
<%
	manager.setMessage("");
	}
%>
<%
if(fr!=null && manager!=null)
{
	String cur_role=manager.getUserCurrentRole();
	String summary=fr.getSummary();
	String doc1=fr.getDocument1();
	String doc2=fr.getDocument2();
	String doc3=fr.getDocument3();
	String primaryKey=fr.getPrimaryKey();
	String primaryKeyValue=fr.getFieldValue(primaryKey);
	int intAction=-1;
	String status="";
	if(cur_role.equals("Peer-viewer"))
	{
		status=manager.getAssignmentStatus(manager.getProposalId(),manager.getUserId());
		if(!status.equals("completed"))
		{	
			intAction=manager.getPermissionByRecord("event",primaryKey,primaryKeyValue);
		}
	}
%>
<html:form action="/event" onsubmit="return validate()" >
		<html:hidden  property="EVENT_ID" />
		<html:hidden  property="PROPOSAL_ID" />
		<html:hidden  property="USR_ID" />
		<html:hidden  property="ROLE_ID" />
		<html:hidden  property="SUBJECTUSR_ID" />
		<html:hidden  property="ACTION_ID" />
		<html:hidden  property="eventDate" />
		<html:hidden  property="document1" />
		<html:hidden  property="document2" />
		<html:hidden  property="document3" />
		<html:hidden  property="publicComments" />
<%
	if(!cur_role.equals("Author"))
	{
%>
		<html:hidden  property="privateComments" />
<%
	}
%>	
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>EVALUATION</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align=right><font size=2>Summary:</td>
		<td><font size=2><font size=2>
			<html:select  property="summary">
			<%
				if(summary.trim().equals("approved"))
				{
			%>
					<option value="approved" selected="true"><font size=2>approved
			<%
				}
				else
				{
			%>
					<option value="approved" selected="true"><font size=2>approved
			<%
				}
				if(summary.trim().equals("revision"))
				{
			%>
				<option value="revision" selected="true"><font size=2>revision
			<%
				}
				else
				{
			%>
				<option value="revision"><font size=2>revision
			<%
				}
				if(summary.trim().equals("declined"))
				{
			%>
				<option value="declined" selected="true"><font size=2>declined
			<%
				}
				else
				{
			%>
				<option value="declined"><font size=2>declined
			<%
				}
			%>
			</html:select>
		</td>
	</tr>
	</tr>
		<td align=right><font size=2>Evaluation:</td>
		<td><font size=2><html:textarea  property="reviewText" cols="30" rows="5"/></td>
	</tr>
<%
	if(!cur_role.equals("Author"))
	{
%>
	<tr>
		<td align=right><font size=2>Private comments:</td>
		<td><font size=2><html:textarea  property="privateComments"  cols="30" rows="5"/></td>
	</tr>
<%
	}
%>
<!--
	</tr>
		<td align=right><font size=2>Public comments:</td>
		<td><font size=2><html:textarea  property="publicComments"  cols="30" rows="5"/></td>
	</tr> -->
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
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%
if(intAction==0)
{
%>
<tr>
	<td align=center colspan=2>
	<html:button property="add" value="add" onclick="submitClick('add')" /> 
</td>
</tr>
<%
}
if (intAction==1)
{
%>
<tr>
	<td align=center colspan=2>
		<html:button property="save" value="save" onclick="submitClick('save')" />
		<html:button property="handin" value="handin" onclick="submitClick('handin')" /> 
	</td>
</tr>
<%
}
if (intAction==3)
{
%>
<tr>
	<td align=center colspan=2>
	<html:button property="save" value="save" onclick="submitClick('save')" />
	<html:button property="handin" value="handin" onclick="submitClick('handin')" /> 
</td>
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

