<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>
<html>
<head>@defaultHeadToken@
<title>VegBank Metasearch</title>

<link type="text/css" href="@stylesheet@" rel="stylesheet">

</head>

<body>
@vegbank_header_html_normal@
<h3>VegBank Metasearch</h3>
<blockquote>
<p><span class="category">Search all of VegBank by keyword.</span></p>

<vegbank:get id="meta" select="keywords_count" wparamSearch="true"/>


<logic:empty name="meta-BEANLIST">
	Nothing in the database matches your keywords.  Please try again.
</logic:empty>


<logic:notEmpty name="meta-BEANLIST">
<% String rowClass = "evenrow"; %>
<blockquote>
	<table  bgcolor="#FFFFFF" border="0" cellspacing="0" cellpadding="10" width="300">
	<tr>
		<th colspan="10">Search Results</th>
	</tr>


	<logic:iterate id="onerow" name="meta-BEANLIST">
		<tr class="@nextcolorclass@">
			<td align="right">

			<bean:define id="entity" name="onerow" property="entity"/>
			<bean:define id="getPk" name="onerow" property="entity"/>
			<bean:define id="getName" name="onerow" property="entity"/>
			<bean:define id="getView" value="std"/>
			<bean:define id="getExtra" value=""/>

			<logic:equal name="onerow" property="entity" value="community">
				<bean:define id="getPk" value="commconcept"/>
				<bean:define id="getName" value="commconcept"/>
			</logic:equal>
			<logic:equal name="onerow" property="entity" value="plant">
				<bean:define id="getPk" value="plantconcept"/>
				<bean:define id="getName" value="plantconcept"/>
			</logic:equal>
			<logic:equal name="onerow" property="entity" value="plot">
				<bean:define id="getPk" value="observation"/>
				<bean:define id="getName" value="observation"/>
				<bean:define id="getView" value="simple"/>
				<bean:define id="getExtra" value="&perPage=3"/>
			</logic:equal>
				

<% 
String params = getPk + ";" + entity + ";" + request.getParameter("kwType") + 
		";" + request.getParameter("wparam");

String getURL = "@get_link@" + getView + "/" + getName + "/" + params + 
		"?where=where_keywords_pk";
%>


				<a href="<%=getURL%><%=getExtra%>"><bean:write name="onerow" property="entity"/></a>
			</td>
			<td>
				<bean:write name="onerow" property="count"/>
			</td>
		</tr>
	</logic:iterate>

	</table>
</blockquote>
</logic:notEmpty>




<br> <br> <br> <br> <br> <br>
<p><span class="category">Want more search options?<br>
<a href="@web_context@forms/plot-query-simle.jsp">Try the 3-in-1 plot query</a>.
<br>
<a href="/vegbank/LoadPlotQuery.do">Try the advanced plot query</a>.
</span>
</blockquote>

<br/>
<p>&nbsp;

@vegbank_footer_html_tworow@
</body>
</html>
