@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
 
<TITLE>Upload Received</TITLE>
@webpage_masthead_html@
 

  
<blockquote>
<h2>Upload Received</h2>
<p>
<br/>
<span class="vegbank_normal">
&raquo; <a href="@get_link@std/userdataset">Go to your datasets</a><br/>

<p>Thanks for contributing your work to the project.
<br>
An email will be sent to <bean:write name="email"/> containing load results.

<!--
<p>Are you sure you want to add this data to Vegbank?
-->

<!--html:form action="/UploadPlot"-->
	<!--html:hidden property="ulpath" value="<%= (String)request.getSession().getAttribute("ulpath") %>"/-->
	<!--html:hidden property="action" value="load"/-->
	&nbsp; &nbsp; &nbsp; &nbsp; 
	<!--html:submit property="submit" value="Yes, load data now"/-->
<!--/html:form-->

<p>&raquo; <html:link action="/DisplayUploadPlotAction.do">Back to upload page

<blockquote>

<!--table>
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
	</table-->
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
@webpage_footer_html@



