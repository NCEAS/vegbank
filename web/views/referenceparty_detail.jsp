@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<TITLE>View VegBank Data: Reference Parties - Detail</TITLE>
      <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@
      @possibly_center@
        <h2>View VegBank Reference Parties</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="referenceparty" select="referenceparty" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<vegbank:pager /><logic:empty name="referenceparty-BEANLIST">
<p>  Sorry, no Reference Parties found.</p>
</logic:empty>
<logic:notEmpty name="referenceparty-BEANLIST">
<logic:iterate id="onerowofreferenceparty" name="referenceparty-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
<tr>
<th colspan="9" class="major">
<bean:define value="false" id="hadData"/>
<logic:notEmpty property="salutation" name="onerowofreferenceparty">
<bean:write property="salutation" name="onerowofreferenceparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="givenname" name="onerowofreferenceparty">
<bean:write property="givenname" name="onerowofreferenceparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="middlename" name="onerowofreferenceparty">
<bean:write property="middlename" name="onerowofreferenceparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="surname" name="onerowofreferenceparty">
<bean:write property="surname" name="onerowofreferenceparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:equal name="hadData" value="false">
	<logic:notEmpty property="organizationname" name="onerowofreferenceparty">
		<bean:write property="organizationname" name="onerowofreferenceparty"/>
	</logic:notEmpty>
	<logic:empty property="organizationname" name="onerowofreferenceparty">
		Unnamed Party
	</logic:empty>
</logic:equal>
</th>
</tr>
        <%@ include file="autogen/referenceparty_detail_data.jsp" %>
        <bean:define id="referenceparty_pk" name="onerowofreferenceparty" property="referenceparty_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_referenceparty_pk" wparam="referenceparty_pk" />-->
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
