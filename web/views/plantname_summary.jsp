@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: Plant Names - Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>
      @vegbank_header_html_normal@
      @possibly_center@
        <h2>View VegBank Plant Names</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="plantname" select="plantname" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="plantname-BEANLIST">
<p>  Sorry, no Plant Names found.</p>
</logic:empty>
<logic:notEmpty name="plantname-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
                  <%@ include file="autogen/plantname_summary_head.jsp" %></tr>
<logic:iterate id="onerowofplantname" name="plantname-BEANLIST">
<tr class="@nextcolorclass@">
                       <%@ include file="autogen/plantname_summary_data.jsp" %>
                       </tr>
<bean:define id="plantname_pk" name="onerowofplantname" property="plantname_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_plantname_pk" wparam="plantname_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@
