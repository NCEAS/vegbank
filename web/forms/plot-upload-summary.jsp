<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>

<!-- 
  *   '$RCSfile: plot-upload-summary.jsp,v $'
  *     Purpose: to inform the user that changes they made to vegbank were successful
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2004-12-16 23:24:20 $'
  *  '$Revision: 1.1 $'
  *
  *
  -->


<HEAD>@defaultHeadToken@
 
<TITLE>Upload Summary</TITLE>

<link rel="stylesheet" href="@stylesheet@" type="text/css">

 
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY>

@vegbank_header_html_normal@
 

  
<blockquote>
<h2>Upload Summary</h2>
<p>
<br/>
<span class="vegbank_normal">
Upload Summary<br/>

<p>Are you sure you want to add this data to Vegbank?

<html:form action="/UploadPlot">
	<html:hidden property="plotFilePath" value="<%= (String)request.getSession().getAttribute("plotFilePath") %>"/>
	<html:hidden property="action" value="load"/>
	&nbsp; &nbsp; &nbsp; &nbsp; 
	<html:submit property="submit" value="Yes, load data now"/>
</html:form>

<p>&raquo; <html:link action="/DisplayUploadPlotAction.do">Back to upload page</html:link>

<blockquote>
<table>
<tr>
	<th>entity</th>
	<th>count</th>
</tr>

<!--bean:define id="smap" name="summaryMap"/-->

<%

Map smap = (Map)request.getSession().getAttribute("summaryMap");

if (smap == null) {
	smap = new HashMap();
}

Iterator kit = smap.keySet().iterator();
while (kit.hasNext()) {
	String table = (String)kit.next();
	Integer count = (Integer)smap.get(table);
%>
	
	<tr>
		<td><%= table %></td>
		<td><%= count %></td>
	</tr>

<%
}
%>
	</table>
	</blockquote>

	</span>
</p>
</blockquote>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
@vegbank_footer_html_tworow@
</BODY>
</HTML>

