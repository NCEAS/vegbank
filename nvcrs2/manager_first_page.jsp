<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="java.util.*" %>
<%@ page import="org.vegbank.nvcrs.web.*" %>

<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Your responsibilities enable everyday running of the whole system
</td></tr>
</table >
<br>
<br>
<font size=2>
<Table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
		<td width=600 valign=bottom>
		<font size=2 >As a manager, you are responsible for the management of both proposals and user roles, including checking proposals, 
		 assigning proposals to peer-viewers, making final decisions based on peer-viewers' evaluations and changing users' roles. 
		 <br></font>
		</td>
	</tr>
</table>
<br><br>
<%
	proposal_Form fr3=(proposal_Form)pageContext.getAttribute("proposal_form", PageContext.SESSION_SCOPE);
	if(fr3!=null)
	{
		int num2=fr3.getRecordCount();
		if(num2>0)
		{
%>
<Table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
		
		<td width=600 valign=bottom>
		<font size=2 >Currently you have <%= num2 %> proposal(s) waiting for your action.</font>
		</td>
	</tr>
</table>

<%@ include file="proposals.jsp" %>
<%
		}
		else
		{
%>
			<Table width=630 border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td width=30></td>
		
				<td width=600 valign=bottom>
				<font size=2 >Currently you have no proposal waiting for your action.</font>
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
		<font size=2 >Currently you have no proposal waiting for your action.</font>
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
	    <td width=89><image src="image/assign_proposal.gif"></td>
		<td valign=center><font size=2 >Assigning proposals to peer-viewers</font><br></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td width=30></td>
	    <td width=89><image src="image/evaluation_proposal1.gif"></td>
		<td valign=center><font size=2 >Viewing evaluations of peer-viewers</font><br></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td width=30></td>
	    <td width=89><image src="image/final_decision.gif"></td>
		<td valign=center><font size=2 >Making final decisions</font><br></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td width=30></td>
	    <td width=89><image src="image/change_userrole.gif"></td>
		<td valign=center><font size=2 >Changing users' roles</font><br></td>
	</tr>
</table>
-->
</font>
<br><br><br>