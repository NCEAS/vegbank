<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="java.util.*" %>
<%@ page import="org.vegbank.nvcrs.web.*" %>


<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Your contributions are appreciated
</td></tr>
</table >
<br>
<br>
<font size=2>
<Table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
		<td width=600 valign=bottom>
		<font size=2 >As a peer-viewer, you can view and evaluate vegetation type revision proposals asigned to you. <br></font>
		</td>
	</tr>
</table>
<br><br>
<%
	Assignments asgns1=(Assignments)pageContext.getAttribute("assignments", PageContext.SESSION_SCOPE);
	if(asgns1!=null)
	{
		int num1=asgns1.getAssignmentCount();
		if(num1>0)
		{
%>
<Table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
		
		<td width=600 valign=bottom>
		<font size=2 >Currently you have <%= num1 %> assignment(s) waiting for your action.</font>
		</td>
	</tr>
</table>

<%@ include file="assignments.jsp" %>
<%
		}
		else
		{
%>
			<Table width=630 border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td width=30></td>
		
				<td width=600 valign=bottom>
				<font size=2 >Currently you have no assignment waiting for your action.</font>
			</td>
			</tr>
		</table>
<%		
		}
	}	
	else
	{
%>
<Table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
		
		<td width=600 valign=bottom>
		<font size=2 >Currently you have no assignment waiting for your action.</font>
		</td>
	</tr>
</table>
<%
	}
%>
<!--
<table	 width=600 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
	    <td width=89><image src="image/evaluation_proposal1.gif"></td>
		<td valign=center><font size=2 >View assigned proposals</font><br></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td width=30></td>
	    <td width=89><image src="image/evaluate_assignment.gif"></td>
		<td valign=center><font size=2 >Evaluate assigned proposals</font><br></td>
	</tr>
</table>
-->
</font>
<br><br><br>