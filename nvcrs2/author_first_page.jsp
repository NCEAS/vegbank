<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="java.util.*" %>
<%@ page import="org.vegbank.nvcrs.web.*" %>

<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Your perspectives are important
</td></tr>
</table >
<br>
<br>

<Table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
		
		<td width=600 valign=bottom>
		<font size=2 >As an author, you can contribute your perspectives about vegetation type classifications by preparing and submitting proposals
		 through this website. You can also trace the status and peer-viewers' evaluations of every proposal you submitted as easy as 
		 clicking some buttons. <br></font>
		</td>
	</tr>
</table>
<br><br>
<%
	proposal_Form fr2=(proposal_Form)pageContext.getAttribute("proposal_form", PageContext.SESSION_SCOPE);
	if(fr2!=null)
	{
		int num1=fr2.getRecordCount();
		if(num1>0)
		{
%>
<Table width=630 border=0 cellpadding=0 cellspacing=0>
	<tr>
		<td width=30></td>
		
		<td width=600 valign=bottom>
		<font size=2 >Currently you have <%= num1 %> proposal(s) in preparation.</font>
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
				<font size=2 >Currently you have no proposal in preparation.</font>
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
		<font size=2 >Currently you have no proposal in preparation.</font>
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
	    <td width=89><image src="image/submit_proposal1.gif"></td>
		<td valign=center><font size=2 >Adding new proposals<br>Saving proposals<br>Editing proposals</font><br></td>
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
	    <td width=89><image src="image/tracestatus_proposal1.gif"></td>
		<td valign=center><font size=2 >Checking your submitted proposals<br>Tracing statuses of your submitted proposals</font><br></td>
	</tr>
</table>
-->
</font>
<br><br><br>