@stdvegbankget_jspdeclarations@

<html>
<HEAD>
@defaultHeadToken@
 
<TITLE>View VegBank Data: coverMethods - Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  
<h2>View VegBank Cover Methods</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="covermethod" select="covermethod" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->

<vegbank:pager />
<logic:empty name="covermethod-BEANLIST">
<p>  Sorry, no coverMethods found.</p>
</logic:empty>
<logic:notEmpty name="covermethod-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/covermethod_summary_head.jsp" %></tr>
<logic:iterate id="onerowofcovermethod" name="covermethod-BEANLIST">
<tr class="@nextcolorclass@">
<td class="sizetiny">
<a href="@get_link@detail/covermethod/@subst_lt@bean:write name='onerowofcovermethod' property='covermethod_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/covermethod_summary_data.jsp" %>
                       </tr>
<bean:define id="covermethod_pk" name="onerowofcovermethod" property="covermethod_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank:get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_covermethod_pk" wparam="covermethod_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@
