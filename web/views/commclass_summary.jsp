
@stdvegbankget_jspdeclarations@


<HEAD   >
<META http-equiv="Content-Type" content="text/html; charset=UTF-16">

@defaultHeadToken@
 
<TITLE>View Data in VegBank : commClass(s) : Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  <h2>View VegBank Community Classifications</h2>
<% String rowClass = "evenrow"; %>
<vegbank:get id="commclass" select="commclass" beanName="map" pager="true"/>
<vegbank:pager />

<%@ include file="includeviews/sub_commclass_summary.jsp" %>

<br><vegbank:pager />
          @vegbank_footer_html_tworow@
