<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>
<html>
<head>@defaultHeadToken@

<%
String xwhereGlue = request.getParameter("xwhereGlue");
if (xwhereGlue == null) {
	xwhereGlue = "or";
}

String xwhereParams = request.getParameter("xwhereParams");
if (xwhereParams == null) {
	xwhereParams = "";
}
%>

<title>VegBank Metasearch: <%= xwhereParams %></title>

<link type="text/css" href="@stylesheet@" rel="stylesheet">

</head>

<body>
@vegbank_header_html_normal@
<h3>VegBank Metasearch</h3>
<blockquote>


<bean:define id="regexOp" value="<%= xwhereGlue %>"/>
<bean:define id="search" value="<%= xwhereParams %>"/>
<vegbank:get id="meta" select="keywords_count" where="where_keywords_grouped"
		xwhereKey="xwhere_kw_match" xwhereEnable="true" xwhereSearch="true" perPage="-1"/>


<logic:empty name="meta-BEANLIST">
<blockquote>
	Nothing in the database matches your keywords.<br>
	Please try again.
</blockquote>
</logic:empty>

	<table align="center" cellpadding="0" cellspacing="0" border="0" bgcolor="#DDDDDD">
	<tr>
    	<td><img src="@image_server@uplt3.gif"/></td>
		<td></td>
		<td><img src="@image_server@uprt3.gif"/></td>
	</tr>

	<tr>
		<td></td>
		<td>
		<form action="@web_context@forms/metasearch.jsp" method="get" name="metasearch_form">
			<span class="greytext">
			&nbsp; Find anything in VegBank:
			</span>  

			<br>
			 <input type="text" name="xwhereParams" size="30" value="<%= xwhereParams %>"/>
		 	 <html:submit value="search"/>
		</td>
		</tr>
		<tr><td></td><td align="right">
			 <input type="checkbox" name="xwhereGlue" value="and" <logic:equal name="regexOp" value="and">checked</logic:equal>/>
			 	Match all words
			
		</td>
		<td></td>
	</tr>
	<tr>
    	<td><img src="@image_server@lwlt3.gif"/></td>
		<td></td>
		<td><img src="@image_server@lwrt3.gif"/></td>
	</tr>
	<form> 
	</table>

<logic:notEmpty name="meta-BEANLIST">
<% String rowClass = "evenrow"; %>
<blockquote>
	<table bgcolor="#333333" cellspacing="0" cellpadding="1" border="0" align="center">
	<tr><td><table bgcolor="#FFFFFF" border="0" cellspacing="0" cellpadding="10" width="250">
	<tr align="center">
		<td class="major" colspan="2">Search Results</td>
	</tr>
	<tr align="center">
		<th>Category</th>
		<th>Count</th>
	</tr>


	<logic:iterate id="onerow" name="meta-BEANLIST">
		<tr class="@nextcolorclass@" align="center">
			<td>

			<bean:define id="entity" name="onerow" property="entity"/>
			<bean:define id="category" name="onerow" property="entity"/>
			<bean:define id="getPk" name="onerow" property="entity"/>
			<bean:define id="getName" name="onerow" property="entity"/>
			<bean:define id="getView" value="std"/>
<%
			String getExtra = "&xwhereGlue=" + xwhereGlue;
%>

			<logic:equal name="onerow" property="entity" value="community">
				<bean:define id="getPk" value="commconcept"/>
				<bean:define id="getName" value="commconcept"/>
			</logic:equal>
			<logic:equal name="onerow" property="entity" value="place">
				<bean:define id="getPk" value="namedplace"/>
				<bean:define id="getName" value="namedplace"/>
			</logic:equal>
			<logic:equal name="onerow" property="entity" value="plant">
				<bean:define id="getPk" value="plantconcept"/>
				<bean:define id="getName" value="plantconcept"/>
			</logic:equal>
			<logic:equal name="onerow" property="entity" value="plot">
				<bean:define id="getPk" value="observation"/>
				<bean:define id="getName" value="observation"/>
				<bean:define id="getView" value="summary"/>
				<% getExtra = getExtra + "&perPage=3"; %>
			</logic:equal>
			<logic:equal name="onerow" property="entity" value="covermethod">
				<bean:define id="category" value="cover method"/>
			</logic:equal>
			<logic:equal name="onerow" property="entity" value="stratummethod">
				<bean:define id="category" value="stratum method"/>
			</logic:equal>

<% 
String params = getPk + ";" + entity;
String getURL = "@get_link@" + getView + "/" + getName + "/" + params + 
		"?where=where_keywords_pk_in&xwhereKey=xwhere_kw_match&xwhereSearch=true&xwhereParams=" + xwhereParams;
%>
				<a href="<%=getURL%><%=getExtra%>"><bean:write name="category"/></a>

			</td>
			<td>
				<bean:write name="onerow" property="count"/>
			</td>
		</tr>
	</logic:iterate>

	</table></td></tr></table>
</blockquote>
	<center>
<span class="vegbank_small" align="center">
You searched for 
<logic:equal name="regexOp" value="and">all words in</logic:equal>
<logic:equal name="regexOp" value="or">any word in</logic:equal>
'<i><%= xwhereParams %></i>'
</span>
</center>
</logic:notEmpty>



<p><span class="category">Want more search options?<br>
&nbsp; &nbsp; &raquo; <a href="@web_context@forms/plot-query-simple.jsp">3-in-1 plot query</a>
<br>
&nbsp; &nbsp; &raquo; <a href="/vegbank/LoadPlotQuery.do">Advanced plot query</a>
</span>
</blockquote>

<br/>
<p>&nbsp;

@vegbank_footer_html_tworow@
</body>
</html>
