
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: Named Places - Detail</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>
      @vegbank_header_html_normal@
      @possibly_center@
        <h2>View VegBank Named Places</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="namedplace" select="namedplace" beanName="map" pager="true" xwhereEnable="true"/>
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="namedplace-BEANLIST">
<p>  Sorry, no Named Places found.</p>
</logic:empty>
<logic:notEmpty name="namedplace-BEANLIST">
<logic:iterate id="onerowofnamedplace" name="namedplace-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
<tr>
  <th class="major_smaller" colspan="4"><bean:write	name="onerowofnamedplace" property="placename" /></th>
</tr>
        <%@ include file="autogen/namedplace_detail_data.jsp" %>
        <bean:define id="namedplace_pk" name="onerowofnamedplace" property="namedplace_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_namedplace_pk" wparam="namedplace_pk" />-->
<vegbank:get id="plot" select="place_count" beanName="map" pager="false" perPage="-1" 
  where="where_namedplace_pk" wparam="namedplace_pk" />

<tr class='@nextcolorclass@'><td class="datalabel">Plots in this place</td>
<td>
<logic:empty name="plot-BEAN">
-none-
</logic:empty>
<logic:notEmpty name="plot-BEAN">
<bean:write name="plot-BEAN" property="count_places" />
<logic:notEqual name="plot-BEAN" property="count_places" value="0">
<a href="@get_link@simple/observation/<bean:write name='namedplace_pk' />?where=where_place_complex">View observations</a>
</logic:notEqual>
</logic:notEmpty>
</td></tr>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@
