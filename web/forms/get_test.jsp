
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- 
  *   '$RCSfile: get_test.jsp,v $'
  *     Purpose: web form to query the plant taxonomy portion of vegbank
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2005-05-06 06:38:16 $'
  *  '$Revision: 1.6 $'
  *
  *
  -->




  <title>vegbank:get Test</title>
  




  @webpage_masthead_html@ 

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
out.println("<br /><br />");
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
		<input type="text" name="where" value="where_commconcept_mpq" size="40"/><br/>
		xwhereGlue: 
		<input type="text" name="xwhereGlue" value="AND"/><br/>

		<br />

		<!-- FIRST PARAM -->
		<b>FIRST PARAM</b>
	<blockquote>
		xwhereParams_commname_*:
		<input type="text" name="xwhereParams_commname_1" value="cu.commname"/>
		=
		<input type="text" name="xwhereParams_commname_0" value="tidal"/>
		<br />
		xwhereKey_commname: <input type="text" name="xwhereKey_commname" value="xwhere_match"/>
		<br />
		xwhereSearch_commname: 
			<select name="xwhereSearch_commname">
				<option value="false" SELECTED>false</option>
				<option value="true">true</option>
			</select>
		<br />
		xwhereMatchAny_commname: 
			<select name="xwhereMatchAny_commname">
				<option value="false" SELECTED>false</option>
				<option value="true">true</option>
			</select>
	</blockquote>

		<!-- SECOND PARAM -->
		<b>SECOND PARAM</b>
	<blockquote>
		xwhereParams_party_*:
		<input type="text" name="xwhereParams_party_1" value="cs.party_id"/>
		=
		<input type="text" name="xwhereParams_party_0" value="512"/>
		<br />
		xwhereKey_party: <input type="text" name="xwhereKey_party" value="xwhere_eq"/>
		<br />
		xwhereSearch_party: 
			<select name="xwhereSearch_party">
				<option value="false" SELECTED>false</option>
				<option value="true">true</option>
			</select>
		<br />
		xwhereMatchAny_party: 
			<select name="xwhereMatchAny_party">
				<option value="false" SELECTED>false</option>
				<option value="true">true</option>
			</select>
	</blockquote>


		<!-- MULTI PARAM -->
		<b>MULTI PARAM</b>
	<blockquote>
		xwhereParams_commlevel:
		<input type="text" name="xwhereParams_commlevel_1" value="cs.commlevel"/>
		IN
	    <select name="xwhereParams_commlevel_0" size="4" multiple="true">
			<option value="" selected>--ANY--</option>
			<option value="Alliance" selected>Alliance</option>
			<option value="Association" selected>Association</option>
			<option value="Class" selected>Class</option>
		</select>
		<br />
		xwhereKey_commlevel: <input type="op" name="xwhereKey_commlevel" value="xwhere_in"/>
	</blockquote>
		<br/>

		<p>
		<input type="submit" value="submit"/>
    </form>
	
	<br />
	<p>

            

@webpage_footer_html@
