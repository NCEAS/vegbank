<!--

  correlation.jsp
   Created on Wed Apr 21 15:31:07 EDT 2004
   By Auto FormBean,Action and JSP Builder 1.0
 
-->



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.vegbank.nvcrs.web.*" %>
<%@ page import="java.util.*" %>

<script language="javascript" >

	function nameChanged(obj,id)
	{
		if(obj=="fromtype")
		{
			document.correlation_form.TYPE_ID.value=id;
		}
		else
		{
			document.correlation_form.CORRELATEDTYPE_ID.value=id;
		}
	}
	function submitClick(act)
	{
		document.correlation_form.action.value=act;
		document.correlation_form.submit();
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
<b>Proposal :: Types :: Correlation
</td></tr>
</table >
<br>
<br>

<%
	BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	correlation_Form fr=(correlation_Form)pageContext.getAttribute("correlation_form", PageContext.SESSION_SCOPE);
	String fromid=fr.getTYPE_ID();
	String toid=fr.getCORRELATEDTYPE_ID();
	
	type_Form tpForm=(type_Form)pageContext.getAttribute("type_form", PageContext.SESSION_SCOPE);
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
if(fr!=null && manager!=null && tpForm!=null)
{
	ArrayList types=tpForm.getRecords();
	int num=types.size();

%>
<center>

<html:form action="/correlation" onsubmit="return validate()" >
		<html:hidden  property="CORRELATION_ID" />
		<html:hidden  property="TYPE_ID" />
		<html:hidden  property="CORRELATEDTYPE_ID" />
	<table width=600 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
    	 <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>CORRELATION</font></center></th>
	     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align=right><font size=2>From type:</td>
		<td>
			<select name="fromName" onchange="nameChanged('fromtype',this.options[this.selectedIndex].value)">
				<% 
					for(int k=0;k<num;k++)
					{
						Hashtable t=(Hashtable)types.get(k);
						String tid=(String)t.get("TYPE_ID");
						String tname=(String)t.get("primaryName");
						if(fromid.equals(tid))
						{
				%>
							<option value="<%= tid %>" selected=true><%= tname %>
				<%
						}
						else
						{
				%>
							<option value="<%= tid %>"><%= tname %>
				<%
						}
				
					}
				%>						
			</select>	
		</td>

	</tr>
	<tr>
		<td align=right><font size=2>To type:</td>
		<td>
			<select name="toName" onchange="nameChanged('totype',this.options[this.selectedIndex].value)" >
				<% 
					for(int k=0;k<num;k++)
					{
						Hashtable t=(Hashtable)types.get(k);
						String tid=(String)t.get("TYPE_ID");
						String tname=(String)t.get("primaryName" );
						if(toid.equals(tid))
						{
				%>
							<option value="<%= tid %>" selected=true><%= tname %>
				<%
						}
						else
						{
				%>
							<option value="<%= tid %>"><%= tname %>
				<%
						}
					}
				%>						
			</select>	
		</td>
	</tr>
	<tr>
		<td align=right><font size=2>Vegbank code:</td>
		<td><html:text  property="VB_AccessionCode" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Convergence:</td>
		<td><html:text  property="convergence" /></td>
	</tr>
	<tr>
		<td align=right><font size=2>Notes:</td>
		<td><html:textarea  property="notes" cols="30" rows="5" /></td>
	</tr>
	<tr>
		<td></td>
		<td><html:hidden value="add" property="action" /></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<%
String primaryKey=fr.getPrimaryKey();
String primaryKeyValue=fr.getFieldValue(primaryKey);
int intAction=manager.getPermissionByRecord("correlation",primaryKey,primaryKeyValue);
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
	<td align="right"><html:button property="save" value="save" onclick="submitClick('save')" /> </td>
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
</font>

