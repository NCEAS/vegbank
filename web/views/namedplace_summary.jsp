@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: Named Places - Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  
<h2>View VegBank Named Places</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="namedplace" select="namedplace" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="namedplace-BEANLIST">
<p>  Sorry, no Named Places found.</p>
</logic:empty>
<logic:notEmpty name="namedplace-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/namedplace_summary_head.jsp" %>
                  
                  </tr>
<logic:iterate id="onerowofnamedplace" name="namedplace-BEANLIST">
<tr class="@nextcolorclass@">
<td class="largefield">
<a href="@get_link@detail/namedplace/@subst_lt@bean:write name='onerowofnamedplace' property='namedplace_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/namedplace_summary_data.jsp" %>
<bean:define id="namedplace_pk" name="onerowofnamedplace" property="namedplace_id" />

                       </tr>

<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_namedplace_pk" wparam="namedplace_pk" />-->

</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
</html>
          @webpage_footer_html@
