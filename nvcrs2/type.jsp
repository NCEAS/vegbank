<!--

  type.jsp
   Created on Wed Apr 21 12:25:19 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>


<script language="javascript" >
	function submitClick1(act)
	{
		document.type_form.action.value=act;
		document.type_form.submit();
	}

	function validate1()
	{

		return true;
	}
	
	function typeChanged(sel_type)
	{
		if(sel_type!="new")
		{
			window.open("http://www.natureserve.org/explorer/servlet/NatureServe?init=Ecol"); 
			// "Select type", "width=400, height=300, location=no, menubar=yes, status=yes, toolbar=yes, scrollbars=yes, resizable=yes"); 
		}
	}
</script>
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Proposal :: Type 
</td></tr>
</table >
<br>
<br>

<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	type_Form fr=(type_Form)pageContext.getAttribute("type_form", PageContext.SESSION_SCOPE);
	String msg=(String)manager.getMessage();
	if(msg!=null)
	{
%>
<center><font color=blue size="1"><%= msg %></font></center>
<%
	manager.setMessage("");
	}
%>
<%
if(fr!=null && manager!=null)
{
	String current_status=manager.getProposalStatus();
%>
<center>

<html:form action="/type" onsubmit="return validate1()" >
		<html:hidden  property="TYPE_ID" />
		<html:hidden  property="PROPOSAL_ID" />
		<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=4 background="image/blue_back.gif"><center><font color=white>TYPE</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
		 <tr>
			<td align=right><font size=2 >Action:</td>
			<%
			if(current_status.equals("unsubmitted"))
			{
			%>
			<td><font size=2 ><html:select property="actionType" onchange="typeChanged(this.options[this.selectedIndex].value)"><html:option value="new" /><html:option value="change" /><html:option value="delete" /></html:select>    </td> <!--<html:text  property="actionType" /></td> -->
			<%
			}
			else
			{
			%>
			<td><font size=2 ><html:text property="actionType" /></td>
			<%
			}
			%>
			<td align=right><font size=2 >User type code:</td>
			<td><html:text  property="userTypeCode" /></td>
		</tr>
		<tr>
			<td align=right><font size=2 >NatureServe code:</td>
			<td><html:text  property="VB_AccessionCode" /></td>
			<td align=right><font size=2 >NVCRS code:</td>
			<td><html:text  property="sausageAccessionCode" /></td>
		</tr>
		<tr>
			
			<td align=right><font size=2 >Level:</td>
    		<%
			if(current_status.equals("unsubmitted"))
			{
			%>
    			<td><html:select property="level"><html:option value="Association" /><html:option value="Alliance" /></html:select></td>
			<%
			}
			else
			{
			%>	
				<td><html:text  property="level" /></td>
			<%
			}
			%>
			<td align=right><font size=2 >Primary name:</td>
			<td><html:text  property="primaryName" /></td>
		</tr>
		<tr>
			<td align=right><font size=2 >Confidence:</td>
			<td><html:text  property="confidence" /></td>
			<td align=right><font size=2 >Wetland indicator:</td>
			<td><html:text  property="wetlandIndicator" /></td>
			
		</tr>
		<tr>
			
			<td align=right><font size=2 >Rational:</td>
			<td><html:text  property="rational" /></td>
			<td align=right><font size=2 >GRank:</td>
			<td><html:text  property="GRank" /></td>
		</tr>
		<tr>
			<td align=right><font size=2 >GRank date:</td>
			<td><html:text  property="GRankDate" /></td>
			<td align=right><font size=2 >GRank reason:</td>
			<td><html:text  property="GRankReasons" /></td>
		</tr>
		<tr>
			<td align=right><font size=2 >Succession:</td>
			<td><html:text  property="succession" /></td>
			<td align=right><font size=2 >Classify comments:</td>
			<td><html:textarea  property="classifyComments" cols="15" rows="3"/></td>
		</tr>
		<tr>
			<td align=right><font size=2 >Vegtation summary:</td>
			<td><html:textarea  property="vegtationSummary" cols="15" rows="3"/></td>
			<td align=right><font size=2 >Environment summary:</td>
			<td><html:textarea  property="environmentSummary" cols="15" rows="3"/></td>
		</tr>
		<tr>
			<td align=right><font size=2 >Type summary:</td>
			<td><html:textarea  property="typeSummary" cols="15" rows="3"/></td>
			<td align=right><font size=2 >Additional notes:</td>
			<td><html:textarea  property="additionalNotes" cols="15" rows="3" /></td>
		</tr>
		<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
		<tr><td></td><td></td><td></td><td><html:hidden value="add" property="action" /></td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("type",primaryKey,primaryKeyValue);
if(intAction==0)
{
%>
		<tr>
			<td></td>
			<td align="right"><html:button property="add" value="add" onclick="submitClick1('add')" /></td> 
			<td><html:button property="cancel" value="close" onclick="submitClick1('cancel')" /></td>
			<td></td>
		</tr>
<%
}
if (intAction==1)
{
%>
		<tr>
			<td></td>
			<td align="right"><html:button property="save" value="save" onclick="submitClick1('save')" /> </td>
			<td><html:button property="cancel" value="close" onclick="submitClick1('cancel')" /></td>
			<td></td>
		</tr>
<%
}
if (intAction==2)
{
%>
	<tr>
		<td></td>
		<td align="right"><html:button property="delete" value="delete" onclick="submitClick1('delete')" /></td>
		<td></td>
		<td></td>
	</tr>
<%
}
if (intAction==3)
{
%>
	<tr>
		<td></td>
		<td align="right"><html:button property="save" value="save" onclick="submitClick1('save')" /> </td>
		<td align="right"><html:button property="delete" value="delete" onclick="submitClick1('delete')" /> </td>
		<td><html:submit property="cancel" value="close" onclick="submitClick1('cancel')" /></td>
	</tr>
<%
}
%>
<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
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