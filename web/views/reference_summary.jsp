@stdvegbankget_jspdeclarations@

<html>
<HEAD>

@defaultHeadToken@
 
<TITLE>View VegBank Data: references - Summary</TITLE>
<link type="text/css" href="@stylesheet@" rel="stylesheet"/>
</HEAD>
<body>@vegbank_header_html_normal@  
@possibly_center@
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
<table cellpadding="2" class="leftrightborders">
<tr>
<th>More</th>
                  <%@ include file="autogen/reference_summary_head.jsp" %></tr>
<logic:iterate name="reference-BEANLIST" id="onerowofreference">
<tr class="@nextcolorclass@">
<td class="largefield">
<a href="@get_link@detail/reference/@subst_lt@bean:write name='onerowofreference' property='reference_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/reference_summary_data.jsp" %>
                       </tr>
<bean:define property="reference_id" name="onerowofreference" id="reference_pk"/>
<!--Insert a nested get statement here:
   example:   

<vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_reference_pk" wparam="reference_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br/>
<vegbank:pager/>
</body>
</html>
          @vegbank_footer_html_tworow@
<!-- ____________________________@END:  reference _______________________________________ 


-->
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
</rootThatGetsXMLNSBits>
