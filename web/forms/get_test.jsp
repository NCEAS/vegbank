@stdvegbankget_jspdeclarations@

<html>
<!-- 
  *   '$RCSfile: get_test.jsp,v $'
  *     Purpose: web form to query the plant taxonomy portion of vegbank
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2004-12-06 18:49:35 $'
  *  '$Revision: 1.1 $'
  *
  *
  -->

<head>
@defaultHeadToken@

  <title>vegbank:get Test</title>
  <link rel="stylesheet" href="@stylesheet@" type="text/css">

</head>

<body>
  @vegbank_header_html_normal@ 

<%
if (request.getParameter("action") != null) { 
%>

<vegbank:get xwhereEnable="true" perPage="1"/>

<logic:empty name="BEANLIST">
<font color="red">No results found</font>
</logic:empty>

<logic:notEmpty name="BEANLIST">
<bean:define id="bl" name="BEANLIST"/>
<%
out.println(bl.toString());
out.println("<BR><BR>");
%>
</logic:notEmpty>

<%
}
%>

        <form action="@web_context@forms/get_test.jsp" method="get">
		<input type="hidden" name="action" value="go"/>

		select: 
		<input type="text" name="select" value="commconcept" size="40"/><br/>
		where:
		<input type="text" name="where" value="where_simple" size="40"/><br/>
		xwhereGlue: 
		<input type="text" name="xwhereGlue" value="AND"/><br/>


		<!-- FIRST PARAM -->
		<b>FIRST PARAM</b>
	<blockquote>
		xwhereParams.a:
		<input type="text" name="xwhereParams.commname.1" value="commname"/>
		=
		<input type="text" name="xwhereParams.commname.0" value="tidal"/>
		<br>
		xwhereKey: <input type="text" name="xwhereKey" value="xwhere_match"/>
		xwhereSearch: 
			<select name="xwhereSearch">
				<option value="false" SELECTED>false</option>
				<option value="true">true</option>
			</select>
		<!--
		default: <input type="text" name="xwhereParams.entity.2" value=""/>
		key: <input type="text" name="xwhereKey.entity" value="xwhere_match"/>
		-->
	</blockquote>

		<!-- TEXT FIELD -->
		<!--
		xwhereParams.b:
		<input type="text" name="xwhereParams.keywords.1" value="keywords"/>
		= <input type="text" name="xwhereParams.keywords.0"/> 
		default: <input type="text" name="xwhereParams.keywords.2" value=""/>
		key: <input type="text" name="xwhereKey.keywords" value="xwhere_gt"/>
		<br/>
		-->

		<!-- MULTI SELECT -->
		<!--
		xwhereParams.c:
		<input type="text" name="xwhereParams.table_id.1" value="table_id"/>
		IN
	    <select property="xwhereParams.table_id.0" size="4" multiple="true">
			<option value="" selected>--ANY--</option>
			<option value="1" selected>1</option>
			<option value="2" selected>2</option>
			<option value="3" selected>3</option>
		</select>
		default: <input type="text" name="xwhereParams.table_id.2" value=""/>
		key: <input type="op" name="xwhereKey.table_id" value="xwhere_eq"/>
		<br/>
		-->

		<p>
		<input type="submit" value="submit"/>
    </form>
	
	<br>
	<p>

            @vegbank_footer_html_tworow@
</body>
</html>
