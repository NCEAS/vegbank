
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>Browse VegBank Data by Party</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  
<h2>Browse Plots By Party</h2>


<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>


        <vegbank:get id="browseparty" select="browseparty" 
        beanName="map" pager="true"  xwhereEnable="false" />

<%@ include file="includeviews/sub_party_plotcount.jsp" %>

<br />
<vegbank:pager />
</html>
          @webpage_footer_html@
