@stdvegbankget_jspdeclarations@

<html>
<HEAD>
@defaultHeadToken@
 
<TITLE>View VegBank Data: stratumMethods - Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body class="center">@vegbank_header_html_normal@  
<h2>View VegBank Stratum Methods</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="stratummethod" select="stratummethod" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->

<vegbank:pager />
<logic:empty name="stratummethod-BEANLIST">
<p>  Sorry, no stratumMethods found.</p>
</logic:empty>
<logic:notEmpty name="stratummethod-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/stratummethod_summary_head.jsp" %></tr>
<logic:iterate id="onerowofstratummethod" name="stratummethod-BEANLIST">
<tr class="@nextcolorclass@">
<td class="sizetiny">
<a href="@get_link@detail/stratummethod/@subst_lt@bean:write name='onerowofstratummethod' property='stratummethod_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/stratummethod_summary_data.jsp" %>
                       </tr>
<bean:define id="stratummethod_pk" name="onerowofstratummethod" property="stratummethod_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank:get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_stratummethod_pk" wparam="stratummethod_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@
