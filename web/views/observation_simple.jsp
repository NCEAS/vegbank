@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: Observations - Simple Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>
      @vegbank_header_html_normal@
      @possibly_center@
        <h2>View VegBank Observations</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
 <vegbank:get id="observation" select="plotandobservation" whereNumeric="where_observation_pk" 
     whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>


<vegbank:pager /><logic:empty name="observation-BEANLIST">
<p>  Sorry, no Observations found.</p>
</logic:empty>
<logic:notEmpty name="observation-BEANLIST">
<table class="outsideborders" cellpadding="2">
<TR><!-- one row for whole thing -->
<logic:iterate id="onerowofobservation" name="observation-BEANLIST">
<TD valign="top"><!-- new column -->
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
<bean:define id="onerowofplot" name="onerowofobservation" />
<!-- get both PKs -->
        <bean:define id="observation_pk" name="onerowofobservation" property="observation_id" />
        <bean:define id="plot_pk" name="onerowofobservation" property="plot_id" />


<% rowClass = "evenrow"; %> <!-- reset colors -->
<tr><th colspan="2"><bean:write name="onerowofobservation" property="authorobscode" />
  -<a href='@get_link@comprehensive/observation/<bean:write name="onerowofobservation" property="observation_id" />'>more details</a>
</th></tr>
        <%@ include file="autogen/observation_plotshowmany_data.jsp" %>
        <%@ include file="autogen/plot_plotshowmany_data.jsp" %>
        
      <%@ include file="includeviews/sub_place.jsp" %>
      
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_observation_pk" wparam="observation_pk" />-->
<TR><TD COLSPAN="2">
<bean:define id="smallheader" value="yes" />
<%@ include file="includeviews/sub_taxonobservation.jsp" %>
</TD></TR>

<p>&nbsp;</p>
</table>
</TD>
</logic:iterate>
</TR>
</TABLE>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@
