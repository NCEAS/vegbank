@stdvegbankget_jspdeclarations@

<html>
<HEAD>

@defaultHeadToken@
 
<TITLE>View VegBank Data: references - Detail</TITLE>
<link type="text/css" href="@stylesheet@" rel="stylesheet"/>
</HEAD>
<body>@vegbank_header_html_normal@  @possibly_center@
<h2>View VegBank References</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        
<vegbank:get id="reference" select="reference" beanName="map" pager="true"/>
<!--Where statement removed from preceding: -->
<vegbank:pager/>
<logic:empty name="reference-BEANLIST">
<p>  Sorry, no references found.</p>
</logic:empty>
<logic:notEmpty name="reference-BEANLIST">
<logic:iterate name="reference-BEANLIST" id="onerowofreference">
<!-- iterate over all records in set : new table for each -->
<table cellpadding="2" class="leftrightborders">
<tr><th class="major" colspan="4"><bean:write name="onerowofreference" property="shortname" /></th></tr>
        <%@ include file="autogen/reference_detail_data.jsp" %>
        <bean:define property="reference_id" name="onerowofreference" id="reference_pk"/>
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_reference_pk" wparam="reference_pk" />-->
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br/>
<vegbank:pager/>
</body>
</html>
          @vegbank_footer_html_tworow@
 