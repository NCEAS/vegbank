@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<script language="javascript">
function viewAllMetadata() {
	document.metasearch_form.xwhereParams.value="vb";
	document.metasearch_form.clearSearch.value="1";
	document.metasearch_form.submit();
}

function preSubmit() {
	if (document.metasearch_form.xwhereParams.value == null ||
		document.metasearch_form.xwhereParams.value == "") {
		document.metasearch_form.xwhereParams.value="vb";
		document.metasearch_form.clearSearch.value="1";
	}
}
</script>


<%
String xwhereMatchAny = request.getParameter("xwhereMatchAny");
if (xwhereMatchAny == null) {
	xwhereMatchAny = "false";
}

String blackBoxText = "Search Results";
String xwhereParams = request.getParameter("xwhereParams");
String searchString = xwhereParams;
if (xwhereParams == null) {
	xwhereParams = "vb";
	searchString = "";
	request.setAttribute("xwhereParams", xwhereParams);
}

String clearSearch = request.getParameter("clearSearch");
if (clearSearch != null && clearSearch.equals("1")) {
	searchString = "";
}

if (searchString ==  null || searchString.equals("")) {
	blackBoxText = "Browse All Data";
}
%>

<title>VegBank Metasearch: <%= searchString %></title>






@webpage_masthead_html@
<h2 class='center'>VegBank Metasearch</h2>

<center>

<bean:define id="search" value="<%= searchString %>"/>
<bean:define id="matchAny" value="<%= xwhereMatchAny %>"/>
<vegbank:get id="meta" select="keywords_count" where="where_keywords_grouped"
		xwhereKey="xwhere_kw_match" xwhereEnable="true" xwhereSearch="true" perPage="-1"/>


<logic:empty name="meta-BEANLIST">
<logic:notEmpty name="search">
<p>
	Nothing in the database matches your keywords.<br />
	Please try again.
</p>
</logic:notEmpty>
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
			<input type="hidden" name="clearSearch" value="">
			<span class="greytext">
			&nbsp; Find anything in VegBank:
			</span>  

			<br />
			 <input type="text" name="xwhereParams" size="30" value="<%= searchString %>"/>
		 	 <html:submit value="search" onclick="javascript:preSubmit()"/>
		</td>
		</tr>
		<tr><td></td><td align="right">
			 <input type="checkbox" name="xwhereMatchAny" value="true" <logic:equal 
			 name="matchAny" value="true">checked</logic:equal>/>
			 	Match any word
			
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

<br/>
<logic:notEmpty name="meta-BEANLIST">
<!-- start metasearch results --> <!-- this tag needed for Mozilla Search Plugin -->
<% String rowClass = "evenrow"; %>

	<table bgcolor="#333333" cellspacing="0" cellpadding="1" border="0" align="center">
	<tr><td><table bgcolor="#FFFFFF" border="0" cellspacing="0" cellpadding="10" width="250">
	<tr align="center">
		<td class="major" colspan="2"><%= blackBoxText %></td>
	</tr>
	<tr align="center">
		<th>Category</th>
		<th>Count</th>
	</tr>


	<logic:iterate id="onerow" name="meta-BEANLIST">
	<!-- start metasearch item --><!-- this tag needed for Mozilla Search Plugin -->
		<tr class="@nextcolorclass@" align="center">
			<td>

			<bean:define id="entity" name="onerow" property="entity"/>
			<bean:define id="category" name="onerow" property="entity"/>
			<bean:define id="getPk" name="onerow" property="entity"/>
			<bean:define id="getName" name="onerow" property="entity"/>
			<bean:define id="getView" value="std"/>
<%
			String getExtra = "&xwhereMatchAny=" + xwhereMatchAny;
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
String params = getPk + Utility.PARAM_DELIM + entity;
String getURL = "@get_link@" + getView + "/" + getName + "/" + params + 
		"?where=where_keywords_pk_in&xwhereKey=xwhere_kw_match&xwhereSearch=true&xwhereParams=" + xwhereParams;
%>
				<a href="<%=getURL%><%=getExtra%>"><bean:write name="category"/></a>

			</td>
			<td>
				<bean:write name="onerow" property="count"/>
			</td>
		</tr>
		<!-- end metasearch item --><!-- this tag needed for Mozilla Search Plugin -->
	</logic:iterate>

	</table></td></tr></table>
	<!-- end metasearch results --> <!-- this tag needed for Mozilla Search Plugin -->
<br/>

<span class="sizesmall" align="center">
<% if (searchString !=  null && !searchString.equals("")) { %>
You searched for 
<logic:equal name="matchAny" value="true">any word in</logic:equal>
<logic:notEqual name="matchAny" value="true">all words in</logic:notEqual>
'<i><%= searchString %></i>'
<br />

Click here to <a href="javascript:viewAllMetadata()">view all metadata</a>
<% } else { %>
Showing all metadata</a>
<% } %>
</span>

</logic:notEmpty>



<p>@newPlotQueryLink@</p>





<script language="javascript">
document.metasearch_form.xwhereParams.focus();
</script>
</center>


@webpage_footer_html@
