
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
@defaultHeadToken@
 
<TITLE>View VegBank Data: Parties - Detail</TITLE>
<link type="text/css" href="@stylesheet@" rel="stylesheet"/>
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@
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
<bean:define value="false" id="hadData"/>
<tr>
<th colspan="9" class="major">
<logic:notEmpty property="salutation" name="onerowofparty">
<bean:write property="salutation" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="givenname" name="onerowofparty">
<bean:write property="givenname" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="middlename" name="onerowofparty">
<bean:write property="middlename" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="surname" name="onerowofparty">
<bean:write property="surname" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:equal name="hadData" value="false">
	<logic:notEmpty property="organizationname" name="onerowofparty">
		<bean:write property="organizationname" name="onerowofparty"/>
	</logic:notEmpty>
	<logic:empty property="organizationname" name="onerowofparty">
		Unnamed Party
	</logic:empty>
</logic:equal>
</th>
</tr>

        <%@ include file="autogen/party_detail_data.jsp" %>
        <bean:define property="party_id" name="onerowofparty" id="party_pk"/>
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_party_pk" wparam="party_pk" />-->


<vegbank:get perPage="-1" wparam="party_pk" where="where_party_pk" pager="false" beanName="map" select="address" id="address"/>

<tr>
<th colspan="9">CONTACT</th>
</tr>
<logic:empty name="address-BEANLIST">
<tr>
<td colspan="9" class="@nextcolorclass@">  No address information on file.</td>
</tr>
</logic:empty>
<logic:notEmpty name="address-BEANLIST">
<logic:iterate name="address-BEANLIST" id="onerowofaddress">
<tr class="@nextcolorclass@">
<%@ include file="autogen/address_detail_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>




<vegbank:get perPage="-1" wparam="party_pk" where="where_party_pk" pager="false" beanName="map" select="telephone" id="telephone"/>
<tr>
<th colspan="9">PHONE</th>
</tr>
<logic:empty name="telephone-BEANLIST">
<tr>
<td colspan="9" class="@nextcolorclass@">  No telephone information on file.</td>
</tr>
</logic:empty>
<logic:notEmpty name="telephone-BEANLIST">

<logic:iterate name="telephone-BEANLIST" id="onerowoftelephone">
<tr class="@nextcolorclass@">
<%@ include file="autogen/telephone_summary_data.jsp" %>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br/>
<vegbank:pager/>
</body>
</html>
          @vegbank_footer_html_tworow@
