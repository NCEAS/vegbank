@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: Journals - Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>
      @vegbank_header_html_normal@
      @possibly_center@
        <h2>View VegBank Journals</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="referencejournal" select="referencejournal" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="referencejournal-BEANLIST">
<p>  Sorry, no Reference Journals found.</p>
</logic:empty>
<logic:notEmpty name="referencejournal-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
                  <%@ include file="autogen/referencejournal_summary_head.jsp" %></tr>
<logic:iterate id="onerowofreferencejournal" name="referencejournal-BEANLIST">
<tr class="@nextcolorclass@">
                       <%@ include file="autogen/referencejournal_summary_data.jsp" %>
                       </tr>
<bean:define id="referencejournal_pk" name="onerowofreferencejournal" property="referencejournal_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_referencejournal_pk" wparam="referencejournal_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@

