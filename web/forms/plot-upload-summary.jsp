
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<!-- 
  *   '$RCSfile: plot-upload-summary.jsp,v $'
  *     Purpose: to inform the user that changes they made to vegbank were successful
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2005-07-31 04:34:15 $'
  *  '$Revision: 1.7 $'
  *
  *
  -->



 
<TITLE>Upload Received</TITLE>


<!-- get the email -->
<%@ include file="/views/includeviews/sub_getwebuserid.jsp" %>
<!-- this now defines beanWebUserEmail -->
 


@webpage_masthead_html@
 

  
<blockquote>
<h2>Upload Received</h2>
<p>
<br/>
<span class="vegbank_normal">
&raquo; <a href="@get_link@std/userdataset">Go to your datasets</a><br/>

<p>Thanks for contributing your work to the project.
<br />
An email will be sent to <logic:notPresent name="beanWebUserEmail" >your email address</logic:notPresent>
<bean:write name="beanWebUserEmail" ignore="true" /> containing load results.

<!--
<p>Are you sure you want to add this data to Vegbank?
-->

<!--html:form action="/UploadPlot"-->
	<!--html:hidden property="ulpath" value="<%= (String)request.getSession().getAttribute("ulpath") %>"/-->
	<!--html:hidden property="action" value="load"/-->
	&nbsp; &nbsp; &nbsp; &nbsp; 
	<!--html:submit property="submit" value="Yes, load data now"/-->
<!--/html:form-->

<p>&raquo; <html:link action="/DisplayUploadPlotAction.do">Back to upload page</html:link>

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
<br />
<br />
<br />
<br />
<br />
<br />
<br />
<br />


@webpage_footer_html@

