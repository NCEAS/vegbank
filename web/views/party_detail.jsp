
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
@defaultHeadToken@
 
<TITLE>View VegBank Data: Parties - Detail</TITLE>
<link type="text/css" href="@stylesheet@" rel="stylesheet"/>
</HEAD>
<body>@vegbank_header_html_normal@
<h2>View VegBank Parties</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        
<vegbank:get id="party" select="party" beanName="map" pager="true"/>
<!--Where statement removed from preceding: -->
<vegbank:pager/>
<logic:empty name="party-BEANLIST">
<p>  Sorry, no parties found.</p>
</logic:empty>
<logic:notEmpty name="party-BEANLIST">
<logic:iterate name="party-BEANLIST" id="onerowofparty">
<!-- iterate over all records in set : new table for each -->
<table cellpadding="2" class="leftrightborders" width="500">
        <%@ include file="autogen/party_detail_data.jsp" %>
        <bean:define property="party_id" name="onerowofparty" id="party_pk"/>
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_party_pk" wparam="party_pk" />-->

<TR>
<TD COLSPAN="2">
<vegbank:get perPage="-1" wparam="party_pk" where="where_party_pk" pager="false" beanName="map" select="address" id="address"/>

<table cellpadding="2" class="leftrightborders" width="100%">
<tr>
<th colspan="9">CONTACT</th>
</tr>
<logic:empty name="address-BEANLIST">
<tr>
<td class="@nextcolorclass@">  No address info on file.</td>
</tr>
</logic:empty>
<logic:notEmpty name="address-BEANLIST">
<logic:iterate name="address-BEANLIST" id="onerowofaddress">
<tr class="@nextcolorclass@">
<%@ include file="autogen/address_detail_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
</TD>
</TR>


<TR>
<TD COLSPAN="2">
<vegbank:get perPage="-1" wparam="party_pk" where="where_party_pk" pager="false" beanName="map" select="telephone" id="telephone"/>
<table cellpadding="2" class="leftrightborders" width="100%">
<logic:empty name="telephone-BEANLIST">
<tr>
<td class="@nextcolorclass@">  No telephone info on file.</td>
</tr>
</logic:empty>
<logic:notEmpty name="telephone-BEANLIST">
<tr>
<%@ include file="autogen/telephone_summary_head.jsp" %>
</tr>
<logic:iterate name="telephone-BEANLIST" id="onerowoftelephone">
<tr class="@nextcolorclass@">
<%@ include file="autogen/telephone_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
</TD>
</TR>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br/>
<vegbank:pager/>
</body>
</html>
          @vegbank_footer_html_tworow@
